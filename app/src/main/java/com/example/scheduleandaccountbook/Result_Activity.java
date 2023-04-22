package com.example.scheduleandaccountbook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Result_Activity extends AppCompatActivity {
    final String TAG = this.getClass().getSimpleName();



    private TextView tv_result; // 닉네임 text
    private ImageView iv_profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Intent intent = getIntent();
        String nickName = intent.getStringExtra("nickname");
        String photoUrl = intent.getStringExtra("photoUrl");

        tv_result = findViewById(R.id.textView2);
        tv_result.setText(nickName);

        iv_profile = findViewById(R.id.imageView2);
        Glide.with(this).load(photoUrl).into(iv_profile);



}}