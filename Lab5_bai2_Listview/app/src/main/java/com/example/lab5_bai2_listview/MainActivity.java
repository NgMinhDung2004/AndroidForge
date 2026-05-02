package com.example.lab5_bai2_listview;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView lvPerson;
    private List<Person> personList;
    private PersonAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        lvPerson = findViewById(R.id.lv_person);
        personList = new ArrayList<>();

        // Add dummy data
        personList.add(new Person("Người thứ 1", R.drawable.chasca));
        personList.add(new Person("Người thứ 2", R.drawable.furina));
        personList.add(new Person("Người thứ 3", R.drawable.mavuika));
        personList.add(new Person("Người thứ 4", R.drawable.xilolen));
        personList.add(new Person("Người thứ 5", R.drawable.yae_miko));

        adapter = new PersonAdapter(this, R.layout.item_person, personList);
        lvPerson.setAdapter(adapter);

        lvPerson.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Person person = personList.get(position);
                Toast.makeText(MainActivity.this, "Bạn vừa chọn " + person.getName(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}