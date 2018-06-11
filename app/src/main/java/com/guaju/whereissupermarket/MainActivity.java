package com.guaju.whereissupermarket;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;



    //百度地图  北京工商管理专修学院  116.133239,40.239437
//谷歌地图  北京工商管理专修学院   116.125824,40.233965


public class MainActivity extends AppCompatActivity {

    private MapView mMapView;
    private AMap aMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);

        initAmap();

        //将地图切换成交通图
//        switchToTrafficeMap();
        //将地图切换成夜景图
//        switchToNightMap();

        //为北工商添加logo
        initLogoMarker();
    }

    private void initLogoMarker() {
        //北工商坐标有了
        LatLng bubaLatLng=new LatLng(40.239437,116.133239);
        //为北工商这个位置提供自定义图标的marker
        BitmapDescriptor buba_logo = BitmapDescriptorFactory.fromResource(R.drawable.buba_logo);
        final Marker marker = aMap.addMarker(new MarkerOptions().position(bubaLatLng).icon(buba_logo));

        //为这个Marker提供一个(自定义的)infowindow
        //创建一个自定义的infowindow
//        BubaInfoWindow bubaInfoWindow = new BubaInfoWindow(this, R.drawable.buba_logo, "北工商", "啦啦啦");
        aMap.setInfoWindowAdapter(new AMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                View v = View.inflate(MainActivity.this, R.layout.bubu_infowindow, null);
                ImageView infowindow_icon = (ImageView)v.findViewById(R.id.infowindow_icon);
                TextView infowindow_title = (TextView)v.findViewById(R.id.infowindow_title);
                TextView infowindow_content =(TextView)v.findViewById(R.id.infowindow_content);
                infowindow_icon.setBackgroundResource(R.drawable.buba_logo);
                infowindow_content.setText("LALLLALALALLALALA");
                infowindow_title.setText("我是标题");
                return v;
            }

            @Override
            public View getInfoContents(Marker marker) {
                return null;
            }
        });
        aMap.setOnInfoWindowClickListener(new AMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                if (marker.isInfoWindowShown()){
                    marker.hideInfoWindow();
                }
                else{
                    marker.showInfoWindow();
                }
            }
        });
        aMap.setOnMapClickListener(new AMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (marker.isInfoWindowShown()){
                    marker.hideInfoWindow();
                }
            }
        });
                   
    }

    private void switchToNightMap() {
        aMap.setMapType(AMap.MAP_TYPE_NIGHT);//夜景地图，aMap是地图控制器对象。

    }

    private void switchToTrafficeMap() {
        aMap.setTrafficEnabled(true);


    }

    private void initAmap() {
        //初始化AMap
        if(aMap==null){
            aMap=  mMapView.getMap();
        }
        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        //定位蓝点图标自定义
        BitmapDescriptor bitmapDescriptor1 = BitmapDescriptorFactory.fromResource(R.mipmap.icon);
        myLocationStyle.myLocationIcon(bitmapDescriptor1);


        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        //aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
        //实现定位功能
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }
}