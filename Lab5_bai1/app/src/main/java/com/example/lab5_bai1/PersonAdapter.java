package com.example.lab5_bai1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class PersonAdapter extends ArrayAdapter<Person> {
    private int resource;
    private List<Person> listPerson;

    public PersonAdapter(@NonNull Context context, int resource, @NonNull List<Person> objects) {
        super(context, resource, objects);
        this.resource = resource;
        this.listPerson = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createView(position, convertView, parent);
    }

    private View createView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(resource, parent, false);
        }

        ImageView imgPerson = convertView.findViewById(R.id.imgPerson);
        TextView tvNamePerson = convertView.findViewById(R.id.tvNamePerson);

        Person person = getItem(position);
        if (person != null) {
            imgPerson.setImageResource(person.getImagePerson());
            tvNamePerson.setText(person.getNamePerson());
        }

        return convertView;
    }
}