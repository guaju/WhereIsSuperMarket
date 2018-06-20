package com.guaju.whereissupermarket.baidu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.poi.PoiSortType;
import com.guaju.whereissupermarket.R;

import java.util.List;

public class MarkerActivity extends AppCompatActivity {

    private MapView mapView;
    private EditText et_baidu_poi;
    private BaiduMap mBaiduMap;
    //监测infowindow是否显示的变量
//    private boolean isInfoWindowShowing = false;


    private BDLocation bdLocation;//定位到的值


    //定义了定位的客户端   还有监听
    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_marker);
        findId();
        if (mBaiduMap == null) {
            mBaiduMap = mapView.getMap();
        }
        locate();

        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {

            private View view;
            private InfoWindow mInfoWindow;

            @Override
            public boolean onMarkerClick(Marker marker) {
                    view = View.inflate(MarkerActivity.this, R.layout.info_winodw_baidu, null);
                    ImageView icon_baidu = (ImageView) view.findViewById(R.id.icon_baidu);
                    TextView tv_title_baidu = (TextView) view.findViewById(R.id.tv_title_baidu);
                    TextView tv_des_baidu = (TextView) view.findViewById(R.id.tv_des_baidu);
                    tv_title_baidu.setText(marker.getTitle());
                    tv_des_baidu.setText(marker.getExtraInfo().getString("des"));
                    mInfoWindow = new InfoWindow(view, marker.getPosition(), 0);
                    mBaiduMap.showInfoWindow(mInfoWindow);
                //重影bug
                mapView.removeView(view);
                return true;
            }
        });
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                //清空所有使用这个方法
//                mBaiduMap.clear();
                
                //只隐藏infowindow
                mBaiduMap.hideInfoWindow();

            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });

    }

    private void findId() {
        mapView = (MapView) findViewById(R.id.mapview);
        et_baidu_poi = (EditText) findViewById(R.id.et_baidu_poi);
        //设置edittext的搜索键的监听
        et_baidu_poi.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    // 先隐藏键盘
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(MarkerActivity.this.getCurrentFocus()
                                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    //隐藏完成之后去做自己的操作
//                    searchPoi();//检索一般的信息关键字

                    if (bdLocation == null) {
                        Toast.makeText(MarkerActivity.this, "未定位成功~~~", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    //去搜索以定位位置为圆心  以特定值为半径的区域范围内的 一些poi位置
                    searchPoiCirle(et_baidu_poi.getText().toString().trim());

                }
                return false;
            }

            private void searchPoiCirle(String keyword) {
                //1、实例化poisearch
                final PoiSearch mPoiSearch = PoiSearch.newInstance();
                //2、
                OnGetPoiSearchResultListener poiListener = new OnGetPoiSearchResultListener() {

                    public void onGetPoiResult(PoiResult result) {
                        //获取POI检索结果
                        List<PoiInfo> allPoi = result.getAllPoi();
                        if (allPoi == null || allPoi.isEmpty()) {
                            //如果他是空的话，说明没有检索到附近的商家
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MarkerActivity.this, "20公里内没有找到您要找的位置", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            for (PoiInfo pi : allPoi) {
                                showMarkers(pi);
                            }


                        }


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

                //3、设置监听
                mPoiSearch.setOnGetPoiSearchResultListener(poiListener);
                //4、检索
                //找到当前定位位置
                LatLng center = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
                //发起搜索
                mPoiSearch.searchNearby(new PoiNearbySearchOption()
                        .keyword(keyword)   //关键字
                        .sortType(PoiSortType.distance_from_near_to_far) //策略 返回什么样的结果
                        .location(center) //圆形位置
                        .radius(20000)   //半径   (什么单位呢？ m米)
                        .pageNum(10));    //每页多少个
            }
        });
    }

    //普通检索
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


    private void showMarkers(final PoiInfo poiInfo) {
//构建Marker图标

        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.mipmap.baidu_marker);

//构建MarkerOption，用于在地图上添加Marker
        LatLng point = poiInfo.location;
        Bundle bundle = new Bundle();
        bundle.putString("des", poiInfo.address);
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap)
                .title(poiInfo.name)
                .extraInfo(bundle);


//在地图上添加Marker，并显示

        mBaiduMap.addOverlay(option);
        //当marker添加完成之后，需要给marker添加点击事件


    }

    private void locate() {


        mLocationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);

        //配置定位客户端的 配置参数
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
//可选，设置定位模式，默认高精度
//LocationMode.Hight_Accuracy：高精度；
//LocationMode. Battery_Saving：低功耗；
//LocationMode. Device_Sensors：仅使用设备；

        option.setCoorType("bd09ll");
//可选，设置返回经纬度坐标类型，默认gcj02
//gcj02：国测局坐标；
//bd09ll：百度经纬度坐标；
//bd09：百度墨卡托坐标；
//海外地区定位，无需设置坐标类型，统一返回wgs84类型坐标

        option.setScanSpan(1000);
//可选，设置发起定位请求的间隔，int类型，单位ms
//如果设置为0，则代表单次定位，即仅定位一次，默认为0
//如果设置非0，需设置1000ms以上才有效

        option.setOpenGps(true);
//可选，设置是否使用gps，默认false
//使用高精度和仅用设备两种定位模式的，参数必须设置为true

        option.setLocationNotify(true);
//可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false

        option.setIgnoreKillProcess(false);
//可选，定位SDK内部是一个service，并放到了独立进程。
//设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)

        option.SetIgnoreCacheException(false);
//可选，设置是否收集Crash信息，默认收集，即参数为false

        option.setWifiCacheTimeOut(5 * 60 * 1000);
//可选，7.2版本新增能力
//如果设置了该接口，首次启动定位时，会先判断当前WiFi是否超出有效期，若超出有效期，会先重新扫描WiFi，然后定位

        option.setEnableSimulateGps(false);
//可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false

        mLocationClient.setLocOption(option);
//mLocationClient为第二步初始化过的LocationClient对象
//需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
//更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明


        mLocationClient.start();
//mLocationClient为第二步初始化过的LocationClient对象
//调用LocationClient的start()方法，便可发起定位请求
    }


    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取经纬度相关（常用）的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明
            if (location != null) {
                bdLocation = location;
                showLocationPosition(bdLocation);

                mLocationClient.stop();//停止定位
            }

//            double latitude = location.getLatitude();    //获取纬度信息
//            double longitude = location.getLongitude();    //获取经度信息
//            float radius = location.getRadius();    //获取定位精度，默认值为0.0f
//
//            String coorType = location.getCoorType();
//            //获取经纬度坐标类型，以LocationClientOption中设置过的坐标类型为准
//
//            int errorCode = location.getLocType();
//            //获取定位类型、定位错误返回码，具体信息可参照类参考中BDLocation类中的说明


        }


    }


    //这是我们自定义的方法 作用是绘制定位蓝点
    private void showLocationPosition(BDLocation location) {
        // 开启定位图层

        mBaiduMap.setMyLocationEnabled(true);
        // 构造定位数据
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(location.getRadius())
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .latitude(location.getLatitude())
                .longitude(location.getLongitude()).build();

        // 设置定位数据
        mBaiduMap.setMyLocationData(locData);

        // 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
        BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory
                .fromResource(R.mipmap.baidu_logo);
        MyLocationConfiguration config = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.FOLLOWING, true, mCurrentMarker);
        mBaiduMap.setMyLocationConfiguration(config);


    }
}
