package com.example.lab11_bai3;

import android.content.SharedPreferences;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.InputType;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private NfcAdapter mNfcAdapter;
    private TextView mTvNfcData;
    private Button mBtnReadNfc, mBtnUseSimulatedCard, mBtnStopSimulatedCard;
    private ListView mLvNfcTags;
    private List<String> nfcTagList = new ArrayList<>();
    private List<String> nfcTagNames = new ArrayList<>(); // Danh sách tên thẻ
    private ArrayAdapter<String> adapter;
    private String selectedTag = null; // UID thẻ được chọn để giả lập
    private boolean isSimulatedCardActive = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Khởi tạo các phần tử giao diện
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        mTvNfcData = findViewById(R.id.tvNfcData);
        mBtnReadNfc = findViewById(R.id.btnReadNfc);
        mBtnUseSimulatedCard = findViewById(R.id.btnUseSimulatedCard);
        mBtnStopSimulatedCard = findViewById(R.id.btnStopSimulatedCard);
        mLvNfcTags = findViewById(R.id.lvNfcTags);

        // Thiết lập adapter cho ListView
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, nfcTagNames);
        mLvNfcTags.setAdapter(adapter);

        // Tải dữ liệu thẻ đã lưu
        loadNfcData();

        // Đăng ký menu ngữ cảnh
        registerForContextMenu(mLvNfcTags);

        // Sự kiện đọc thẻ NFC
        mBtnReadNfc.setOnClickListener(v -> {
            if (mNfcAdapter == null || !mNfcAdapter.isEnabled()) {
                Toast.makeText(this, "Vui lòng bật NFC", Toast.LENGTH_SHORT).show();
                return;
            }
            mNfcAdapter.enableReaderMode(this, this::readNfcData, NfcAdapter.FLAG_READER_NFC_A, null);
            Toast.makeText(this, "Đưa thẻ vào vùng NFC để đọc", Toast.LENGTH_SHORT).show();
        });

        // Sự kiện chọn thẻ từ danh sách
        mLvNfcTags.setOnItemClickListener((parent, view, position, id) -> {
            selectedTag = nfcTagList.get(position);
            String tagName = nfcTagNames.get(position);
            Toast.makeText(this, "Đã chọn thẻ: " + tagName, Toast.LENGTH_SHORT).show();
            showTagDetailsDialog(tagName, selectedTag);
        });

        // Sự kiện bật giả lập thẻ
        mBtnUseSimulatedCard.setOnClickListener(v -> {
            if (selectedTag == null) {
                Toast.makeText(this, "Vui lòng chọn một thẻ từ danh sách", Toast.LENGTH_SHORT).show();
                return;
            }
            if (isSimulatedCardActive) {
                Toast.makeText(this, "Thẻ giả lập đã được bật", Toast.LENGTH_SHORT).show();
                return;
            }
            emulateNfcTag(selectedTag);
        });

        // Sự kiện tắt giả lập thẻ
        mBtnStopSimulatedCard.setOnClickListener(v -> {
            if (!isSimulatedCardActive) {
                Toast.makeText(this, "Không có thẻ giả lập đang hoạt động", Toast.LENGTH_SHORT).show();
                return;
            }
            stopSimulatedCard();
        });
    }

    // Hiển thị thông tin chi tiết của thẻ bằng Dialog
    private void showTagDetailsDialog(String tagName, String tagUID) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thông tin thẻ");
        // Nội dung hiển thị
        String message = "Tên thẻ: " + tagName + "\nUID: " + tagUID;
        builder.setMessage(message);
        // Nút đóng
        builder.setPositiveButton("Đóng", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    // Đọc dữ liệu từ thẻ NFC
    private void readNfcData(Tag tag) {
        String uid = bytesToHex(tag.getId()); // Lấy UID thẻ
        runOnUiThread(() -> {
            if (!nfcTagList.contains(uid)) {
                nfcTagList.add(uid);
                nfcTagNames.add("Thẻ " + (nfcTagList.size())); // Gán tên mặc định
                adapter.notifyDataSetChanged();
                saveNfcData(); // Lưu danh sách thẻ
                Toast.makeText(this, "Đã lưu thẻ: " + uid, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Thẻ đã tồn tại trong danh sách", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Tạo menu ngữ cảnh
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_menu, menu); // context_menu.xml
    }

    // Xử lý menu ngữ cảnh
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if (item.getItemId() == R.id.action_delete) {
            deleteTag(info.position);
            return true;
        } else if (item.getItemId() == R.id.action_edit_name) {
            showRenameDialog(info.position);
            return true;
        }
        return super.onContextItemSelected(item);
    }

    // Hiển thị hộp thoại đổi tên thẻ
    private void showRenameDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Đổi tên thẻ");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText(nfcTagNames.get(position)); // Hiển thị tên hiện tại
        builder.setView(input);
        builder.setPositiveButton("Lưu", (dialog, which) -> {
            String newName = input.getText().toString().trim();
            if (!newName.isEmpty()) {
                nfcTagNames.set(position, newName);
                adapter.notifyDataSetChanged();
                saveNfcData(); // Cập nhật dữ liệu đã lưu
                Toast.makeText(this, "Đã đổi tên thẻ", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Tên thẻ không được để trống", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    // Xóa thẻ khỏi danh sách
    private void deleteTag(int position) {
        nfcTagList.remove(position);
        nfcTagNames.remove(position);
        adapter.notifyDataSetChanged();
        saveNfcData(); // Lưu lại thay đổi
        Toast.makeText(this, "Đã xóa thẻ", Toast.LENGTH_SHORT).show();
    }

    // Giả lập thẻ NFC
    private void emulateNfcTag(String uid) {
        isSimulatedCardActive = true;
        Toast.makeText(this, "Đang sử dụng thẻ giả lập: " + uid, Toast.LENGTH_LONG).show();
    }

    // Tắt thẻ giả lập
    private void stopSimulatedCard() {
        isSimulatedCardActive = false;
        Toast.makeText(this, "Thẻ giả lập đã tắt", Toast.LENGTH_SHORT).show();
    }

    // Lưu danh sách thẻ vào SharedPreferences
    private void saveNfcData() {
        SharedPreferences sharedPreferences = getSharedPreferences("NfcData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < nfcTagList.size(); i++) {
            JSONObject tagObject = new JSONObject();
            try {
                tagObject.put("uid", nfcTagList.get(i));
                tagObject.put("name", nfcTagNames.get(i));
            } catch (Exception e) {
                e.printStackTrace();
            }
            jsonArray.put(tagObject);
        }
        editor.putString("tags", jsonArray.toString());
        editor.apply();
    }

    // Tải danh sách thẻ từ SharedPreferences
    private void loadNfcData() {
        SharedPreferences sharedPreferences = getSharedPreferences("NfcData", MODE_PRIVATE);
        String savedData = sharedPreferences.getString("tags", "[]");
        try {
            JSONArray jsonArray = new JSONArray(savedData);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject tagObject = jsonArray.getJSONObject(i);
                nfcTagList.add(tagObject.getString("uid"));
                nfcTagNames.add(tagObject.getString("name"));
            }
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Chuyển đổi byte[] sang chuỗi hex
    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }
}
