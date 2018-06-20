package com.guaju.whereissupermarket.baidu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteLine;
import com.baidu.mapapi.search.route.MassTransitRoutePlanOption;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.guaju.whereissupermarket.R;
import com.guaju.whereissupermarket.baidu.overlayutil.MassTransitRouteOverlay;

public class DetailShopActivity extends AppCompatActivity {

    private TextView tv_title;
    private TextView tv_des;
    private LatLng startlatlon;
    private LatLng endlatlon;

    private MapView mMapView;
    private BaiduMap mBaiduMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_detail_shop);
        initView();
        initIntent();
    }

    private void initIntent() {
        Intent intent = getIntent();
        if (intent!=null){
            tv_title.setText(intent.getStringExtra("title"));
            tv_des.setText(intent.getStringExtra("des"));
            startlatlon = intent.getParcelableExtra("startlatlon");
            endlatlon = intent.getParcelableExtra("endlatlon");
        }



    }

    private void initView() {
        tv_title = (TextView)findViewById(R.id.tv_title);
        tv_des = (TextView)findViewById(R.id.tv_des);
        mMapView = (MapView) findViewById(R.id.mapview);
        mBaiduMap = mMapView.getMap();


    }

    public void nav(View view) {

        //去做导航

//       1 创建公交线路规划检索实例；
//        mSearch = RoutePlanSearch.newInstance();
//        2创建公交线路规划检索监听者；
//        OnGetRoutePlanResultListener routeListener = new OnGetRoutePlanResultListener(){
//
//            @Override
//            public void onGetMassTransitRouteResult(MassTransitRouteResult result) {
//                //获取跨城综合公共交通线路规划结果
//            }
//        }
//        3设置公交线路规划检索监听者；
//        mSearch.setOnGetRoutePlanResultListener( routeListener );
//        4准备检索起、终点信息；
//        PlanNode stMassNode = PlanNode.withCityNameAndPlaceName("北京", "天安门");
//
//        PlanNode enMassNode = PlanNode.withCityNameAndPlaceName("上海", "东方明珠");
//        5发起公交线路规划检索；
//        mSearch.masstransitSearch(new MassTransitRoutePlanOption().from(stMassNode).to(enMassNode));
//        6释放检索实例；
//        mSearch.destroy();

        RoutePlanSearch mSearch = RoutePlanSearch.newInstance();
        OnGetRoutePlanResultListener routeListener = new OnGetRoutePlanResultListener(){

            @Override
            public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {

            }

            @Override
            public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

            }

            @Override
            public void onGetMassTransitRouteResult(MassTransitRouteResult result) {
                //获取跨城综合公共交通线路规划结果
                Log.e("guaju", "onGetMassTransitRouteResult: "+result );
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    //未找到结果
                    return;
                }
                if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
                    //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
                    //result.getSuggestAddrInfo()
                    return;
                }
                if (result.error == SearchResult.ERRORNO.NO_ERROR) {
                    MassTransitRouteLine massTransitRouteLine = result.getRouteLines().get(0);
                    //创建公交路线规划线路覆盖物
                    MassTransitRouteOverlay overlay = new MassTransitRouteOverlay(mBaiduMap);
                    //设置公交路线规划数据
                    overlay.setData(massTransitRouteLine);
                    //将公交路线规划覆盖物添加到地图中
                    overlay.addToMap();
                    overlay.zoomToSpan();
                }
            }

            @Override
            public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {

            }

            @Override
            public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

            }

            @Override
            public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

            }
        };

        mSearch.setOnGetRoutePlanResultListener( routeListener );

        PlanNode stMassNode = PlanNode.withLocation(startlatlon);
        PlanNode enMassNode = PlanNode.withLocation(endlatlon);
        mSearch.masstransitSearch(new MassTransitRoutePlanOption().from(stMassNode).to(enMassNode));
    }
}
