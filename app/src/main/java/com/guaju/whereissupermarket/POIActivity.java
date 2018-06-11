package com.guaju.whereissupermarket;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;

import java.util.ArrayList;

public class POIActivity extends AppCompatActivity implements OnPoiSearchListener  {

    private EditText et_poi;
    private String searchInfo;
    private MapView mapView;
    private AMap map;

    private ArrayList<Marker> markerList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi);
        initView(savedInstanceState);
    }

    private void initView(Bundle savedInstanceState) {
        et_poi = (EditText)findViewById(R.id.et_poi);
        mapView = findViewById(R.id.mapview);
        map = mapView.getMap();
        mapView.onCreate(savedInstanceState);
    }

    public void search(View view) {
        //搜索方法
        searchInfo = et_poi.getText().toString().trim();
        //判断是否合法
        if (!TextUtils.isEmpty(searchInfo)){
            //去做搜索的操作
          // 1、 构造 PoiSearch.Query 对象
            PoiSearch.Query query = new PoiSearch.Query(searchInfo, "", "北京");
            query.setPageSize(10);// 设置每页最多返回多少条poiitem
            query.setPageNum(0);//设置查询页码
          //2、创建Search对象
            PoiSearch poiSearch = new PoiSearch(this, query);
            poiSearch.setOnPoiSearchListener(this);
         //3、调用 PoiSearch 的 searchPOIAsyn() 方法发送请求
            poiSearch.searchPOIAsyn();


        }else{
            //为空 谈提示吐司
            Toast.makeText(this, "您输入的信息为空，请重新输入", Toast.LENGTH_SHORT).show();

        }


    }


    @Override
    public void onPoiSearched(PoiResult poiResult, int i) {
        ArrayList<PoiItem> pois = poiResult.getPois();
        initMarker(pois);


    }



    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }


    private void initMarker(ArrayList<PoiItem> pois ) {
        for (PoiItem pi:pois) {
            LatLonPoint latLonPoint = pi.getLatLonPoint();
            LatLng latLng = new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude());
            BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.restaurant);
            Marker marker = map.addMarker(new MarkerOptions().position(latLng).icon(bitmapDescriptor).title(pi.getTitle()).snippet(pi.getSnippet()));
            markerList.add(marker);

            map.setInfoWindowAdapter(new AMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(Marker marker) {
                    View v = View.inflate(POIActivity.this, R.layout.bubu_infowindow, null);
                    ImageView infowindow_icon = (ImageView) v.findViewById(R.id.infowindow_icon);
                    TextView infowindow_title = (TextView) v.findViewById(R.id.infowindow_title);
                    TextView infowindow_content = (TextView) v.findViewById(R.id.infowindow_content);

                    infowindow_icon.setBackgroundResource(R.drawable.restaurant);
                    infowindow_content.setText(marker.getSnippet());
                    infowindow_title.setText(marker.getTitle());
                    return v;
                }

                @Override
                public View getInfoContents(Marker marker) {
                    return null;
                }
            });

            map.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                     if (marker.isInfoWindowShown()) {
                         marker.hideInfoWindow();
                     }else{
                         marker.showInfoWindow();
                     }
                    return true;
                }
            });
            map.setOnMapClickListener(new AMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    for (Marker marker:markerList){
                        if (marker.isInfoWindowShown()) {
                            marker.hideInfoWindow();
                        }
                    }

                }
            });


        }



    }
}

