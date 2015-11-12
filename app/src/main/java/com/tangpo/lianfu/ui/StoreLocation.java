package com.tangpo.lianfu.ui;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.tangpo.lianfu.R;

public class StoreLocation extends ActionBarActivity {


    private MapView mMapView=null;
    private BaiduMap mBaiduMap=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_location);
        /**
         * ��Ӧ�ó��򴴽�ʱ��ʼ��SDK���õ�Contextȫ�ֱ�����ע��������Ҫ��ȡ����Ӧ�õ�Context����ApplicationContext
         * ����ע��Ҫ��setContentView����֮ǰʵ��
         *ע�⣺��SDK���������ʹ��֮ǰ����Ҫ����
         SDKInitializer.initialize(getApplicationContext());��������ǽ���÷�������Application�ĳ�ʼ��������
         */
        SDKInitializer.initialize(getApplication());
        setContentView(R.layout.activity_test_baidu_map);
        //��ȡ��ͼ�ؼ�����
        mMapView= (MapView) findViewById(R.id.bmapView);
        //��ȡ��ͼ
        mBaiduMap=mMapView.getMap();

        Intent intent=getIntent();
        float longitude=Float.valueOf(intent.getStringExtra("lng"))/(10^6);
        float latitude=Float.valueOf(intent.getStringExtra("lat"))/(10^6);

        LatLng cenpt=new LatLng(longitude,latitude);
        //�����ͼ״̬
        MapStatus mMapStatus=new MapStatus.Builder().target(cenpt).zoom(13).build();
        //����MapStatusUpdate�����Ա�������ͼ״̬��Ҫ�����ı仯
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //�ı��ͼ״̬
        mBaiduMap.setMapStatus(mMapStatusUpdate);

        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mBaiduMap.setMyLocationEnabled(true);
        //��ʾ��ǰ�̵��λ��
        BitmapDescriptor mCurrentMaker= BitmapDescriptorFactory.fromResource(R.drawable.shop_gound_r);
        OverlayOptions overlayOptions=new MarkerOptions().position(cenpt).icon(mCurrentMaker).zIndex(11).animateType(MarkerOptions.MarkerAnimateType.drop);
        mBaiduMap.addOverlay(overlayOptions);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_store_location, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
