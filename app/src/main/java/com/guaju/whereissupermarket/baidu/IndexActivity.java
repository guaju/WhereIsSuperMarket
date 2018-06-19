package com.guaju.whereissupermarket.baidu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.guaju.whereissupermarket.R;

public class IndexActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
    }

    public void showMapAndLocation(View view) {
        startActivity(new Intent(this,HomeActivity.class));
    }

    public void showMarker(View view) {
        startActivity(new Intent(this,MarkerActivity.class));

    }


}
