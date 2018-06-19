package com.guaju.whereissupermarket.baidu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.guaju.whereissupermarket.R;

import java.util.List;

public class MarkerActivity extends AppCompatActivity {

    private MapView mapView;
    private EditText et_baidu_poi;
    private BaiduMap mBaiduMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_marker);
        findId();
        if (mBaiduMap == null) {
            mBaiduMap = mapView.getMap();
        }


        showMarkers();
    }

    private void findId() {
        mapView = (MapView) findViewById(R.id.mapview);
        et_baidu_poi = (EditText) findViewById(R.id.et_baidu_poi);
        //设置edittext的搜索键的监听
        et_baidu_poi.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    // 先隐藏键盘
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(MarkerActivity.this.getCurrentFocus()
                                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    //隐藏完成之后去做自己的操作
//                    searchPoi();//检索一般的信息关键字


                }
                return false;
            }
        });
    }

    private void searchPoi() {
        //1、
        final PoiSearch mPoiSearch = PoiSearch.newInstance();

        //2、
        OnGetPoiSearchResultListener poiListener = new OnGetPoiSearchResultListener() {

            public void onGetPoiResult(PoiResult result) {
                //获取POI检索结果
                List<PoiInfo> allPoi = result.getAllPoi();
                PoiInfo poiInfo = allPoi.get(0);
                //停止检索
                mPoiSearch.destroy();
            }

            public void onGetPoiDetailResult(PoiDetailResult result) {
                //获取Place详情页检索结果
            }

            @Override
            public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {
                //获得室内poi检索结果
            }
        };

        //3、
        mPoiSearch.setOnGetPoiSearchResultListener(poiListener);
        //4
        mPoiSearch.searchInCity((new PoiCitySearchOption())
                .city("北京")
                .keyword(et_baidu_poi.getText().toString().trim())
                .pageNum(10));


    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }


    private void showMarkers() {
        LatLng point = new LatLng(39.963175, 116.400244);

//构建Marker图标

        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.mipmap.baidu_marker);

//构建MarkerOption，用于在地图上添加Marker

        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap);

//在地图上添加Marker，并显示

        mBaiduMap.addOverlay(option);
    }
}
