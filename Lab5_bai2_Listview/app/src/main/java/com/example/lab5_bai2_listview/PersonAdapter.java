package com.example.lab5_bai2_listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class PersonAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<Person> personList;

    public PersonAdapter(Context context, int layout, List<Person> personList) {
        this.context = context;
        this.layout = layout;
        this.personList = personList;
    }

    @Override
    public int getCount() {
        return personList.size();
    }

    @Override
    public Object getItem(int position) {
        return personList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null);
        }

        Person person = personList.get(position);

        ImageView imgAvatar = convertView.findViewById(R.id.img_avatar);
        TextView tvName = convertView.findViewById(R.id.tv_name);

        imgAvatar.setImageResource(person.getImageResId());
        tvName.setText(person.getName());

        return convertView;
    }
}