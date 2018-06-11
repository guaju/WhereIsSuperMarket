package com.guaju.whereissupermarket;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.Marker;

/**
 * Created by guaju on 2018/6/11.
 */

public class BubaInfoWindow  implements AMap.InfoWindowAdapter{
    Context context;
    private ImageView infowindow_icon;
    private TextView infowindow_title;
    private TextView infowindow_content;

    @Override
    public View getInfoWindow(Marker marker) {
        View v = View.inflate(context, R.layout.bubu_infowindow, null);
        infowindow_icon = (ImageView)v.findViewById(R.id.infowindow_icon);
        infowindow_title = (TextView)v.findViewById(R.id.infowindow_title);
        infowindow_content =(TextView)v.findViewById(R.id.infowindow_content);
        return v;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    public BubaInfoWindow(Context context, int infowindow_icon_res, String infowindow_title_str, String infowindow_content_str) {
        this.context = context;
       


        infowindow_icon.setBackgroundResource(infowindow_icon_res);
        infowindow_title.setText(infowindow_title_str);
        infowindow_content.setText(infowindow_content_str);
    }
}
