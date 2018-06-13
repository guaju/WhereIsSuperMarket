package com.guaju.whereissupermarket;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.amap.api.maps.model.LatLng;

public class ShopDetailActivity extends AppCompatActivity {

    private TextView tv_location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_detail);
        initView();
        initIntent();
    }

    private void initIntent() {
        Intent intent = getIntent();
        if (intent!=null){
            String title = intent.getStringExtra("title");
            String des = intent.getStringExtra("des");
            LatLng latLng = intent.getParcelableExtra("latlon");

            tv_location.setText(title);
            tv_location.append("\n");
            tv_location.append(des);

        }
    }

    private void initView() {
        tv_location = (TextView)findViewById(R.id.tv_location);
        //给tv_location设置左边的图片
    }
}
