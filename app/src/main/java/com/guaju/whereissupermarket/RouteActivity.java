package com.guaju.whereissupermarket;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.guaju.whereissupermarket.overlay.DrivingRouteOverlay;

public class RouteActivity extends AppCompatActivity implements RouteSearch.OnRouteSearchListener {

    private MapView mapView;
    private String title;
    private String des;
    private LatLng latLng;
    private AMap amap;
    //定位的自己的位置
    private Location myLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);

        initView();
        initMapView(savedInstanceState);
        initLocation();
        initIntent();
        initSearchDriveRoute();


    }
    //定位
    private void initLocation() {

        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);
        amap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        //aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
        //实现定位功能
        amap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        //启动定位1秒钟之后去拿到定位
        new Handler(getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                myLocation = amap.getMyLocation();
                if (myLocation ==null|| myLocation.getExtras().get("City")==null){
                    Toast.makeText( RouteActivity.this, "定位失败。。。", Toast.LENGTH_SHORT).show();
                }else{


                }


            }
        },1000);


    }
    //开始获取驾驶路线
    private void initSearchDriveRoute() {
        //第 1 步，初始化 RouteSearch 对象
        RouteSearch  routeSearch = new RouteSearch(this);
        //第 2 步，设置数据回调监听器
        routeSearch.setRouteSearchListener(this);
        //第 3 步，设置搜索参数
        //参数1 起始位置  参数2 结束为止
        myLocation=MyApplication.getMyLocation();
        if (myLocation!=null) {
            //起始位置点
            LatLonPoint startLatLonPoint = new LatLonPoint(myLocation.getLatitude(), myLocation.getLongitude());
            //结束位置点
            LatLonPoint endLatLonPoint = new LatLonPoint(latLng.latitude, latLng.longitude);

            RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(startLatLonPoint, endLatLonPoint);


            RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, RouteSearch.DRIVING_SINGLE_SHORTEST, null, null, "");

            //第 4 步，发送请求

            routeSearch.calculateDriveRouteAsyn(query);

        }





    }

    private void initIntent() {
        Intent intent = getIntent();
        if (intent!=null){
            title = intent.getStringExtra("title");
            des = intent.getStringExtra("des");
            latLng = intent.getParcelableExtra("latlon");
        }
    }

    private void initMapView(Bundle savedInstanceState) {
        mapView.onCreate(savedInstanceState);
        amap = mapView.getMap();
    }

    private void initView() {
        mapView = (MapView)findViewById(R.id.mapview);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }


    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }
    //拿到驾车路线的回调
    @Override
    public void onDriveRouteSearched(DriveRouteResult result, int i) {
          //5拿回调
        amap.clear();// 清理地图上的所有覆盖物
        if (i == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    final DrivePath drivePath = result.getPaths()
                            .get(0);
                    if(drivePath == null) {
                        return;
                    }
                    DrivingRouteOverlay drivingRouteOverlay = new DrivingRouteOverlay(
                            RouteActivity.this, amap, drivePath,
                            result.getStartPos(),
                            result.getTargetPos(), null);
                    drivingRouteOverlay.setNodeIconVisibility(false);//设置节点marker是否显示
                    drivingRouteOverlay.setIsColorfulline(true);//是否用颜色展示交通拥堵情况，默认true
                    drivingRouteOverlay.removeFromMap();
                    drivingRouteOverlay.addToMap();
                    drivingRouteOverlay.zoomToSpan();
                }

            }
        }



    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }
}
