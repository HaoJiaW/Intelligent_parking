package com.github.jiawei.intelligent_parking_system;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.github.jiawei.intelligent_parking_system.activity.LoginActivity;
import com.github.jiawei.intelligent_parking_system.activity.MembersActivity;
import com.github.jiawei.intelligent_parking_system.activity.ParkLotActivity;
import com.github.jiawei.intelligent_parking_system.activity.PersonalActivity;
import com.github.jiawei.intelligent_parking_system.bean.UserBean;
import com.github.jiawei.intelligent_parking_system.event.RefreshUserDataEvent;
import com.github.jiawei.intelligent_parking_system.helper.MyDataBaseHelper;
import com.github.jiawei.intelligent_parking_system.utils.CacheManager;
import com.github.jiawei.intelligent_parking_system.utils.MyDbManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    @ViewInject(R.id.main_drawer_layout)
    private DrawerLayout drawerLayout;

    @ViewInject(R.id.main_personal_img)
    private ImageView personImg;

    @ViewInject(R.id.main_nav)
    private NavigationView navView;


    private CircleImageView headPortrait;

    private LocationClient locationClient;
    private MapView mMapView;
    private BaiduMap baiduMap;
    private Boolean isFirstLocal=true;
    private View headView;
    private TextView headNumber;

    private DbManager dbManager;

    private String numberTxt=null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        //添加注解事件
        x.view().inject(this);

        dbManager=MyDbManager.getDbManger();

        EventBus.getDefault().register(this);

        mMapView=findViewById(R.id.map_view);
        baiduMap=mMapView.getMap();
        headView=navView.inflateHeaderView(R.layout.nav_header);
        headPortrait=headView.findViewById(R.id.head_portrait);
        headPortrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numberTxt!=null){
                Intent intent=new Intent(MainActivity.this,PersonalActivity.class);
                intent.putExtra("number",numberTxt);
                startActivity(intent);
                }else {
                    startActivity(new Intent(MainActivity.this,PersonalActivity.class));
                }
            }
        });
        headNumber=headView.findViewById(R.id.head_number);
        headNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
            }
        });
        Intent intent=getIntent();
        String intentNumber=intent.getStringExtra("number");
        if (intentNumber!=null){
        if (intentNumber.length()!=0){
            headNumber.setText(intentNumber);
        }
        }
        initLocation();

        //导航列表界面的按钮点击
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_vip:
                        startActivity(new Intent(MainActivity.this,MembersActivity.class));
                        break;
                        case R.id.nav_car:
                        startActivity(new Intent(MainActivity.this,ParkLotActivity.class));
                        break;
                }


                return false;
            }
        });

    }

    public void requestLocation() {
        initLocationOption();
        locationClient.start();
    }


    @Event(value = R.id.main_personal_img,type = View.OnClickListener.class)
    private void openDrawerLayout(View view){
        //指定DrawLayout的方向,left是指在左边,从左到右的划,start表示自动
        drawerLayout.openDrawer(Gravity.START);
    }

    /*@Event(value = R.id.head_portrait,type = View.OnClickListener.class)
    private void toLoginActivity(View view){
        startActivity(new Intent(MainActivity.this,LoginActivity.class));
    }*/

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(MainActivity.this, "必选同意所有权限才能使用本程序!", Toast.LENGTH_SHORT).show();
                            MainActivity.this.finish();
                            return;
                        }
                        requestLocation();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "发生未知错误!", Toast.LENGTH_SHORT).show();

                }
                break;
            default:
                break;
        }
    }


    //初始化定位参数
    private void initLocation(){
        //
        locationClient=new LocationClient(getApplicationContext());
        locationClient.registerLocationListener(new MyLocationListener());

        //危险权限申请
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
/*
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ADD_VOICEMAIL)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ADD_VOICEMAIL);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
*/

        if (permissionList.isEmpty()) {
            requestLocation();
        } else {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this, permissions, 1);
        }
    }


    /**
     * 实现定位回调
     */
    class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取经纬度相关（常用）的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明

            android.util.Log.i("百度地图","十秒更新一次");
            StringBuilder currentPosition = new StringBuilder();
            android.util.Log.i("onReceiveLocation", "地图更新了一次");
            currentPosition.append("纬度: ").append(bdLocation.getLatitude());
            android.util.Log.i("纬度:", "" + bdLocation.getLatitude());
            currentPosition.append("经度: ").append(bdLocation.getLongitude());
            android.util.Log.i("经度:", "" + bdLocation.getLongitude());
            currentPosition.append("定位方式: ");
            if (bdLocation.getLocType() == BDLocation.TypeGpsLocation) {
                currentPosition.append("GPS");
            } else if (bdLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
                currentPosition.append("网络");
            }
            currentPosition.append("国家: ").append(bdLocation.getCountry());
            currentPosition.append("省: ").append(bdLocation.getProvince());
            currentPosition.append("市: ").append(bdLocation.getCity());
            currentPosition.append("区: ").append(bdLocation.getDistrict());
            currentPosition.append("街道: ").append(bdLocation.getStreet());
            android.util.Log.i("你现在在:", "" + bdLocation.getDistrict() + "," + bdLocation.getStreet() + "," + bdLocation.getAddrStr());

            //获取定位精度，默认值为0.0f
          /*  float radius = location.getRadius();
            //获取经纬度坐标类型，以LocationClientOption中设置过的坐标类型为准
            String coorType = location.getCoorType();
            //获取定位类型、定位错误返回码，具体信息可参照类参考中BDLocation类中的说明
            int errorCode = location.getLocType();*/

            if (bdLocation.getLocType() == BDLocation.TypeGpsLocation ||
                    bdLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
                nativeTo(bdLocation);
            }
            /*pointlist.add(new LatLng(bdLocation.getLatitude(),bdLocation.getLongitude()));
            if (pointlist.size()>2){
                OverlayOptions mOverlayOptions=new PolylineOptions().width(5).color(getResources()
                        .getColor(R.color.blue)).points(pointlist);
                baiduMap.addOverlay(mOverlayOptions);
            }*/
        }

    }


    //配置定位参数
    private void initLocationOption(){
        LocationClientOption option=new LocationClientOption();
        //可选，默认0，即仅定位一次，设置发起连续定位请求的间隔需要大于等于1000ms才是有效的
        option.setScanSpan(2000);
        //可选，设置是否需要地址信息，默认不需要
        option.setIsNeedAddress(true);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
        option.setCoorType("bd09ll");
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationDescribe(true);
        //可选，设置是否需要设备方向结果
        option.setNeedDeviceDirect(true);
        //可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setLocationNotify(true);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.setIgnoreKillProcess(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIsNeedLocationPoiList(true);
        //可选，默认false，设置是否收集CRASH信息，默认收集
        option.SetIgnoreCacheException(false);
        //可选，默认false，设置是否开启Gps定位
        option.setOpenGps(true);
        //可选，默认false，设置定位时是否需要海拔信息，默认不需要，除基础定位版本都可用
        option.setIsNeedAltitude(false);
        //设置打开自动回调位置模式，该开关打开后，期间只要定位SDK检测到位置变化就会主动回调给开发者，该模式下开发者无需再关心定位间隔是多少，定位SDK本身发现位置变化就会及时回调给开发者
        option.setOpenAutoNotifyMode();
        //设置打开自动回调位置模式，该开关打开后，期间只要定位SDK检测到位置变化就会主动回调给开发者
        // option.setOpenAutoNotifyMode(3000,1, LocationClientOption.LOC_SENSITIVITY_HIGHT);
        //
        locationClient.setLocOption(option);
        //开启定位图层
        baiduMap.setMyLocationEnabled(true);
        initLocAttribute();
    }

    //通过MyLocationConfiguration类来构造包括定位的属性，定位模式、是否开启方向、
    // 设置自定义定位图标、精度圈填充颜色以及精度圈边框颜色5个属性。
    private void initLocAttribute(){
        //mCurrentMode = LocationMode.FOLLOWING;//定位跟随态
        //mCurrentMode = LocationMode.NORMAL;   //默认为 LocationMode.NORMAL 普通态
        //mCurrentMode = LocationMode.COMPASS;  //定位罗盘态
        MyLocationConfiguration.LocationMode mode=MyLocationConfiguration.LocationMode.FOLLOWING;
        BitmapDescriptor locIcon=BitmapDescriptorFactory.fromResource(R.drawable.loc_icon);
        int accuracyCircleFillColor=getResources().getColor(R.color.alpha_colorPrimary);
        int accuracyCircleStrokeColor=getResources().getColor(R.color.colorPrimary);
        MyLocationConfiguration myLocationConfiguration=new MyLocationConfiguration(mode,true,locIcon,accuracyCircleFillColor,accuracyCircleStrokeColor);
        baiduMap.setMyLocationConfigeration(myLocationConfiguration);
    }



    public void nativeTo(BDLocation location) {
        if (isFirstLocal) {
            //更新到我的位置
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
            baiduMap.animateMapStatus(update);
            update = MapStatusUpdateFactory.zoomTo(20f);
            baiduMap.animateMapStatus(update);
            isFirstLocal = false;
        }

        //设置我的指针出现
        MyLocationData.Builder builder = new MyLocationData.Builder();
        builder.latitude(location.getLatitude());
        builder.longitude(location.getLongitude());
        //获取开发者获取的方向信息
        builder.accuracy(location.getRadius());
        builder.direction(100);
        MyLocationData locationData = builder.build();
        baiduMap.setMyLocationData(locationData);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void refreshData(RefreshUserDataEvent event){
        String number=event.getNumber();
        try {
          UserBean bean=dbManager.selector(UserBean.class).where("account","=",number)
                    .findFirst();
          if (bean!=null){
              headPortrait.setImageDrawable(CacheManager.changePicture(bean.getImageUrl()));
              headNumber.setText(bean.getAccount());
              numberTxt=bean.getAccount();
          }
        } catch (DbException e) {
            e.printStackTrace();
        }

    }
}
