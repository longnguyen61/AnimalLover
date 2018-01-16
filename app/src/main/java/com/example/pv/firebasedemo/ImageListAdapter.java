package com.example.pv.firebasedemo;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import javax.microedition.khronos.opengles.GL;

import static com.example.pv.firebasedemo.R.id.tvDescription;
import static com.example.pv.firebasedemo.R.id.tvImageName;

//
///**
// * Created by PV on 1/15/2018.
//// */
////
public class ImageListAdapter extends ArrayAdapter<ImageUpload> {
    private Activity context;
    private int resource;
    private List<ImageUpload> listImage;


    public ImageListAdapter(@NonNull Activity context, int resource, @NonNull List<ImageUpload> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        listImage = objects;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View v = inflater.inflate(resource, null);
        TextView tvName = (TextView) v.findViewById(tvImageName);
        TextView tvDes = (TextView) v.findViewById(tvDescription);
//        TextView tvDescription = (TextView) v.findViewById(tvDescription);
        ImageView img = (ImageView) v.findViewById(R.id.imgView);

        tvDes.setText(listImage.get(position).getDescription());
        tvName.setText(listImage.get(position).getName());
        Glide.with(context).load(listImage.get(position).getUrl()).into(img);

        return v;
    }
}
