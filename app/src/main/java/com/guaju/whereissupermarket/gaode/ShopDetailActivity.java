package com.guaju.whereissupermarket.gaode;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Poi;
import com.amap.api.navi.AmapNaviPage;
import com.amap.api.navi.AmapNaviParams;
import com.amap.api.navi.AmapNaviType;
import com.amap.api.navi.INaviInfoCallback;
import com.amap.api.navi.model.AMapNaviLocation;
import com.guaju.whereissupermarket.R;

public class ShopDetailActivity extends AppCompatActivity implements INaviInfoCallback {

    private TextView tv_location;
    private LatLng latLng;
    private String title;
    private String des;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_detail);
        initView();
        initIntent();
    }

    private void initIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            title = intent.getStringExtra("title");
            des = intent.getStringExtra("des");
            latLng = intent.getParcelableExtra("latlon");

            tv_location.setText(title);
            tv_location.append("\n");
            tv_location.append(des);

        }
    }

    private void initView() {
        tv_location = (TextView) findViewById(R.id.tv_location);
        //给tv_location设置左边的图片
    }

    public void routeGuide(View view) {
        //路线规划
//        Intent intent = new Intent(this,RouteAndNavActivity.class);
//        intent.putExtra("latlon", latLng);
//        intent.putExtra("title", title);
//        intent.putExtra("des", des);
//        startActivity(intent);
        AMapLocation myLocation = MyApplication.getMyLocation();

        Poi start = new Poi(myLocation.getPoiName(), new LatLng(myLocation.getLatitude(),myLocation.getLongitude()), "");
        /**终点传入的是北京站坐标,但是POI的ID "B000A83M61"对应的是北京西站，所以实际算路以北京西站作为终点**/
        Poi end = new Poi(title,latLng, "B000A83M61");
//        List<Poi> wayList = new ArrayList();//途径点目前最多支持3个。
//        wayList.add(new Poi("团结湖", new LatLng(39.93413,116.461676), ""));
//        wayList.add(new Poi("呼家楼", new LatLng(39.923484,116.461327), ""));
//        wayList.add(new Poi("华润大厦", new LatLng(39.912914,116.434247), ""));
        AmapNaviPage.getInstance().showRouteActivity(this, new AmapNaviParams(start, null, end, AmapNaviType.DRIVER), this);
    }

    @Override
    public void onInitNaviFailure() {

    }

    @Override
    public void onGetNavigationText(String s) {

    }

    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {

    }

    @Override
    public void onArriveDestination(boolean b) {

    }

    @Override
    public void onStartNavi(int i) {

    }

    @Override
    public void onCalculateRouteSuccess(int[] ints) {

    }

    @Override
    public void onCalculateRouteFailure(int i) {

    }

    @Override
    public void onStopSpeaking() {

    }

    @Override
    public void onReCalculateRoute(int i) {

    }

    @Override
    public void onExitPage(int i) {

    }

    @Override
    public void onStrategyChanged(int i) {

    }

    @Override
    public View getCustomNaviBottomView() {
        return null;
    }

    @Override
    public View getCustomNaviView() {
        return null;
    }

    @Override
    public void onArrivedWayPoint(int i) {

    }
}
