package com.example.MyFragment1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.god.southcar.MainActivity_slider;
import com.example.god.southcar.R;

/**
 * Created by GOD on 2016/9/25.
 */
public class MyFragment1_broadcast extends AppCompatActivity {

    private TextView broadcast_content;
    private TextView broadcast_date;
    private TextView broadcast_tittle;
    private ImageView broadcast_image;

    private TextView broadcast_back;
    private void setSelected(){
        broadcast_back.setSelected(false);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myfragment1_broadcast);

        broadcast_back = (TextView) findViewById(R.id.broadcast_back);

        broadcast_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelected();
                broadcast_back.setSelected(false);
                broadcast_back.setSelected(true);
                finish();
            }
        });

        Bundle bundle = this.getIntent().getExtras();
        String tittle = bundle.getString("tittle");
        String date = bundle.getString("date");
        String avatar = bundle.getString("avatar");
        String content = bundle.getString("content");

        broadcast_tittle = (TextView) findViewById(R.id.broadcast_tittle);
        broadcast_date = (TextView) findViewById(R.id.broadcast_date);
        broadcast_image = (ImageView) findViewById(R.id.broadcast_image);
        broadcast_content = (TextView) findViewById(R.id.broadcast_content);

        broadcast_tittle.setText(tittle);
        broadcast_date.setText(date);
        Glide.with(MyFragment1_broadcast.this).load(avatar).fitCenter().skipMemoryCache(true).into(broadcast_image);
        broadcast_content.setText(content);
    }
}
