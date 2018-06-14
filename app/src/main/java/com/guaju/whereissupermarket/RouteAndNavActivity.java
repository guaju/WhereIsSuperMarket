package com.guaju.whereissupermarket;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Poi;
import com.amap.api.navi.AmapNaviPage;
import com.amap.api.navi.AmapNaviParams;
import com.amap.api.navi.AmapNaviType;
import com.amap.api.navi.INaviInfoCallback;
import com.amap.api.navi.model.AMapNaviLocation;

public class RouteAndNavActivity extends AppCompatActivity implements INaviInfoCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_and_nav);
    }

    public void startRouteAndNav(View view) {
        Poi start = new Poi("三元桥", new LatLng(39.96087,116.45798), "");
        /**终点传入的是北京站坐标,但是POI的ID "B000A83M61"对应的是北京西站，所以实际算路以北京西站作为终点**/
        Poi end = new Poi("北京站", new LatLng(39.904556, 116.427231), "B000A83M61");
//        List<Poi> wayList = new ArrayList();//途径点目前最多支持3个。
//        wayList.add(new Poi("团结湖", new LatLng(39.93413,116.461676), ""));
//        wayList.add(new Poi("呼家楼", new LatLng(39.923484,116.461327), ""));
//        wayList.add(new Poi("华润大厦", new LatLng(39.912914,116.434247), ""));
        AmapNaviPage.getInstance().showRouteActivity(this, new AmapNaviParams(start, null, end, AmapNaviType.DRIVER), this);

    }

    @Override
    public void onInitNaviFailure() {
     //导航失败时
    }

    @Override
    public void onGetNavigationText(String s) {
        //拿到导航文字时

    }

    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {
          //当定位变更的时候
    }

    @Override
    public void onArriveDestination(boolean b) {
         //当到达目的地时
    }

    @Override
    public void onStartNavi(int i) {
       //当开始导航时
    }

    @Override
    public void onCalculateRouteSuccess(int[] ints) {
        //当计算路线成功时
    }

    @Override
    public void onCalculateRouteFailure(int i) {
       //当计算路线失败时
    }

    @Override
    public void onStopSpeaking() {
         //当停止讲话时
    }

    @Override
    public void onReCalculateRoute(int i) {
       //当再次计算路线时
    }

    @Override
    public void onExitPage(int i) {
       //在退出页面时
    }

    @Override
    public void onStrategyChanged(int i) {
         //当导航策略变化时
    }

    @Override
    public View getCustomNaviBottomView() {
        //获得导航底部view
        return null;
    }

    @Override
    public View getCustomNaviView() {
        //获得自定义的的导航view
        return null;
    }

    @Override
    public void onArrivedWayPoint(int i) {
         //当到达目的地时
    }
}
