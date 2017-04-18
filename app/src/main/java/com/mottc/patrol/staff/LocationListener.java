package com.mottc.patrol.staff;

import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.mottc.patrol.Constant;

import java.util.List;

/**
 * Created with Android Studio
 * User: mottc
 * Date: 2017/4/15
 * Time: 14:51
 */
public class LocationListener implements BDLocationListener {

//    StringBuffer sb = new StringBuffer(256);

    @Override
    public void onReceiveLocation(BDLocation location) {


        final String content = Constant.LOCATION_CALLBACK +"{\"latitude\": " + location.getLongitude() + "," + "\"longitude\":" + location.getLatitude() + "}";
        EMClient.getInstance().contactManager().aysncGetAllContactsFromServer(new EMValueCallBack<List<String>>() {
            @Override
            public void onSuccess(final List<String> value) {
                if (value.size() != 0) {
                    Log.i("LocationListener", "onSuccess: " + content);
                    EMMessage message = EMMessage.createTxtSendMessage(content, value.get(0));
                    EMClient.getInstance().chatManager().sendMessage(message);
                }
            }

            @Override
            public void onError(int error, String errorMsg) {

            }
        });

        //获取定位结果


//        sb.append("time : ");
//        sb.append(location.getTime());    //获取定位时间
//
//        sb.append("\nerror code : ");
//        sb.append(location.getLocType());    //获取类型类型
//
//        sb.append("\nlatitude : ");
//        sb.append(location.getLatitude());    //获取纬度信息
//
//        sb.append("\nlontitude : ");
//        sb.append(location.getLongitude());    //获取经度信息
//
//        sb.append("\nradius : ");
//        sb.append(location.getRadius());    //获取定位精准度
//
//        if (location.getLocType() == BDLocation.TypeGpsLocation){
//
//            // GPS定位结果
//            sb.append("\nspeed : ");
//            sb.append(location.getSpeed());    // 单位：公里每小时
//
//            sb.append("\nsatellite : ");
//            sb.append(location.getSatelliteNumber());    //获取卫星数
//
//            sb.append("\nheight : ");
//            sb.append(location.getAltitude());    //获取海拔高度信息，单位米
//
//            sb.append("\ndirection : ");
//            sb.append(location.getDirection());    //获取方向信息，单位度
//
//            sb.append("\naddr : ");
//            sb.append(location.getAddrStr());    //获取地址信息
//
//            sb.append("\ndescribe : ");
//            sb.append("gps定位成功");
//
//        } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
//
//            // 网络定位结果
//            sb.append("\naddr : ");
//            sb.append(location.getAddrStr());    //获取地址信息
//
//            sb.append("\noperationers : ");
//            sb.append(location.getOperators());    //获取运营商信息
//
//            sb.append("\ndescribe : ");
//            sb.append("网络定位成功");
//
//        } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {
//
//            // 离线定位结果
//            sb.append("\ndescribe : ");
//            sb.append("离线定位成功，离线定位结果也是有效的");
//
//        } else if (location.getLocType() == BDLocation.TypeServerError) {
//
//            sb.append("\ndescribe : ");
//            sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
//
//        } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
//
//            sb.append("\ndescribe : ");
//            sb.append("网络不同导致定位失败，请检查网络是否通畅");
//
//        } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
//
//            sb.append("\ndescribe : ");
//            sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
//
//        }
//
//        sb.append("\nlocationdescribe : ");
//        sb.append(location.getLocationDescribe());    //位置语义化信息
//
//        List<Poi> list = location.getPoiList();    // POI数据
//        if (list != null) {
//            sb.append("\npoilist size = : ");
//            sb.append(list.size());
//            for (Poi p : list) {
//                sb.append("\npoi= : ");
//                sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
//            }
//        }

//        Log.i("BaiduLocationApiDem", sb.toString());
//        Toast.makeText(PatrolApplication.applicationContext, sb.toString(), Toast.LENGTH_SHORT).show();

    }


    @Override
    public void onConnectHotSpotMessage(String s, int i) {

    }
}
