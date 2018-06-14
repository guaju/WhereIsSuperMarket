package com.guaju.whereissupermarket;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.guaju.whereissupermarket.baidu.HomeActivity;
import com.guaju.whereissupermarket.gaode.CategrayActivity;

/**
 * Created by guaju on 2018/6/14.
 */

public class GuideActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
    }

    //高德地图
    public void gaode(View view) {
        startActivity(new Intent(this, CategrayActivity.class));
    }
    //百度地图
    public void baidu(View view) {
        startActivity(new Intent(this, HomeActivity.class));
    }
    //腾讯
    public void tencent(View view) {
    }
}
