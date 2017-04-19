package com.mottc.patrol.manager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.mottc.patrol.Constant;
import com.mottc.patrol.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MapActivity extends AppCompatActivity {
    private MapView mMapView = null;
    private BaiduMap mBaiduMap;
    private String username;
    private LocationListener mLocationListener;
    private boolean isFirst = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_map);
        mMapView = (MapView) findViewById(R.id.mapView);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);

        username = this.getIntent().getStringExtra("username");
        askForLocation();

        mLocationListener = new LocationListener();
        EMClient.getInstance().chatManager().addMessageListener(mLocationListener);

    }


    private void markLocation(double latitude, double longitude) {

        if (isFirst) {
            isFirst = false;
            moveToCurrent(latitude, longitude);
        }

        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 构造定位数据
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(0)
                .latitude(latitude)
                .longitude(longitude)
                .build();
        // 设置定位数据
        mBaiduMap.setMyLocationData(locData);
        // 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
        MyLocationConfiguration config = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, false, null);
        mBaiduMap.setMyLocationConfiguration(config);
    }

    private void moveToCurrent(double latitude, double longitude) {
        LatLng current = new LatLng(latitude, longitude);
        MapStatusUpdate mapStatus = MapStatusUpdateFactory.newLatLngZoom(current, 18);
        mBaiduMap.setMapStatus(mapStatus);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        // 当不需要定位图层时关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        stopAskForLocation();
        EMClient.getInstance().chatManager().removeMessageListener(mLocationListener);
    }


    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }


    private void askForLocation() {

        EMMessage message = EMMessage.createTxtSendMessage(Constant.ASK_FOR_LOCATION, username);
        EMClient.getInstance().chatManager().sendMessage(message);
    }

    private void stopAskForLocation() {
        EMMessage message = EMMessage.createTxtSendMessage(Constant.ASK_FOR_LOCATION, username);
        EMClient.getInstance().chatManager().sendMessage(message);
    }

    private class LocationListener implements EMMessageListener {
        @Override
        public void onMessageReceived(List<EMMessage> messages) {

            for (EMMessage message : messages) {
                if (((EMTextMessageBody) message.getBody()).getMessage().startsWith(Constant.LOCATION_CALLBACK)) {
                    String locationJson = ((EMTextMessageBody) message.getBody()).getMessage().substring(17);

                    JSONObject content = null;
                    try {
                        content = new JSONObject(locationJson);
                        markLocation(content.getDouble("latitude"),content.getDouble("longitude"));
                        Log.i("LocationListener", "onMessageReceived: " + content.getDouble("longitude"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {

        }

        @Override
        public void onMessageRead(List<EMMessage> messages) {

        }

        @Override
        public void onMessageDelivered(List<EMMessage> messages) {

        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {

        }
    }


}
