package com.example.lab11_bai2;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "BluetoothApp";
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket bluetoothSocket;
    private ArrayAdapter<String> deviceArrayAdapter;
    private ArrayList<BluetoothDevice> deviceList = new ArrayList<>();
    private ProgressBar progressBar;
    private TextView noDeviceText;

    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        ListView deviceListView = findViewById(R.id.deviceList);
        Button scanButton = findViewById(R.id.scanButton);
        progressBar = findViewById(R.id.progressBar);
        noDeviceText = findViewById(R.id.noDeviceText);

        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Thiết bị không hỗ trợ Bluetooth", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        deviceArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        deviceListView.setAdapter(deviceArrayAdapter);

        // Đăng ký Receiver một lần duy nhất
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(receiver, filter);

        checkAndRequestPermissions();
        showPairedDevices();

        scanButton.setOnClickListener(v -> {
            if (hasRequiredPermissions()) {
                if (!bluetoothAdapter.isEnabled()) {
                    Toast.makeText(this, "Vui lòng bật Bluetooth", Toast.LENGTH_SHORT).show();
                } else if (!isLocationEnabled()) {
                    Toast.makeText(this, "Bạn cần bật Vị trí (GPS) để tìm thiết bị mới", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                } else {
                    startDiscovery();
                }
            } else {
                requestPermissions();
            }
        });

        deviceListView.setOnItemClickListener((parent, view, position, id) -> {
            BluetoothDevice device = deviceList.get(position);
            connectToDevice(device);
        });
    }

    private boolean isLocationEnabled() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private void showPairedDevices() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) return;
        
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                addToList(device, true);
            }
        }
    }

    private void addToList(BluetoothDevice device, boolean isPaired) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) return;
        
        for (BluetoothDevice d : deviceList) {
            if (d.getAddress().equals(device.getAddress())) return;
        }

        String name = device.getName() != null ? device.getName() : "Không tên";
        String info = (isPaired ? "[Đã ghép nối] " : "[Mới] ") + name + "\n" + device.getAddress();
        deviceArrayAdapter.add(info);
        deviceList.add(device);
        deviceArrayAdapter.notifyDataSetChanged();
    }

    private void startDiscovery() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) return;
        
        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }

        deviceArrayAdapter.clear();
        deviceList.clear();
        showPairedDevices(); // Hiện lại danh sách cũ trước
        
        if (bluetoothAdapter.startDiscovery()) {
            progressBar.setVisibility(View.VISIBLE);
            noDeviceText.setVisibility(View.GONE);
            Toast.makeText(this, "Đang quét...", Toast.LENGTH_SHORT).show();
        }
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device != null) addToList(device, false);
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(context, "Đã quét xong", Toast.LENGTH_SHORT).show();
                if (deviceList.isEmpty()) noDeviceText.setVisibility(View.VISIBLE);
            }
        }
    };

    private void connectToDevice(BluetoothDevice device) {
        new Thread(() -> {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) return;
            
            runOnUiThread(() -> Toast.makeText(this, "Đang kết nối...", Toast.LENGTH_SHORT).show());
            bluetoothAdapter.cancelDiscovery();

            try {
                bluetoothSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
                bluetoothSocket.connect();
                runOnUiThread(() -> Toast.makeText(this, "Kết nối thành công!", Toast.LENGTH_SHORT).show());
                
                // Gửi dữ liệu mẫu
                bluetoothSocket.getOutputStream().write("Hello\n".getBytes());
                
            } catch (IOException e) {
                runOnUiThread(() -> Toast.makeText(this, "Kết nối thất bại (SPP không hỗ trợ)", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private boolean hasRequiredPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED &&
                   ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED &&
                   ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        }
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ArrayList<String> p = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            p.add(Manifest.permission.BLUETOOTH_SCAN);
            p.add(Manifest.permission.BLUETOOTH_CONNECT);
        }
        p.add(Manifest.permission.ACCESS_FINE_LOCATION);
        ActivityCompat.requestPermissions(this, p.toArray(new String[0]), 1);
    }

    private void checkAndRequestPermissions() { if (!hasRequiredPermissions()) requestPermissions(); }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try { unregisterReceiver(receiver); } catch (Exception e) {}
    }
}