package com.guaju.whereissupermarket;

import android.app.Application;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

/**
 * Created by guaju on 2018/6/13.
 */

public class MyApplication extends Application {
    //位置信息：待会定位到的信息
    public static AMapLocation myLocation = null;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = null;

    //声明AMapLocationClientOption对象  定位的配置
    public AMapLocationClientOption mLocationOption = null;
    public static MyApplication app;

    @Override
    public void onCreate() {
        super.onCreate();
        this.app = this;
        locate();

    }

    private void locate() {
        mLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null) {
                    myLocation = aMapLocation;
                    aMapLocation.toStr();
                    //停止定位
                    mLocationClient.stopLocation();
                }
            }
        };
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //设置定位模式       ：这是高准确度定位模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //开始定位     ：设置定位的意图  ：这是标记
        mLocationOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.SignIn);
        if (null != mLocationClient) {
            mLocationClient.setLocationOption(mLocationOption);
            //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
            mLocationClient.stopLocation();
            mLocationClient.startLocation();
        }

    }

    //提供获得applicaiton的方法
    public static  MyApplication getApp() {
        return app;
    }


    //提供获得当前位置的方法
    public static AMapLocation getMyLocation() {

        return myLocation;

    }
}
