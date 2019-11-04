package com.github.jiawei.intelligent_parking_system.view;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.github.jiawei.intelligent_parking_system.MobileActivity;
import com.github.jiawei.intelligent_parking_system.R;
import com.github.jiawei.intelligent_parking_system.activity.WifiActivity;
import com.github.jiawei.intelligent_parking_system.animate.TransitionActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuActivity extends AppCompatActivity {

    private GridView gridView;
    private SimpleAdapter simpleAdapter;
    private List<Map<String, Object>> dataList;

    private int[] imgIds = new int[]{R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher,
            R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher};
    private String[] txts = new String[]{"动画", "Wifi", "强制使用移动网络", "停简单", "蓝牙", "HTTP"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        gridView = findViewById(R.id.gridView);
        dataList = new ArrayList<>();

        simpleAdapter = new SimpleAdapter(this, getDataList(), R.layout.meun_item,
                new String[]{"img", "txt"}, new int[]{R.id.imageItem, R.id.titleItem});

        gridView.setAdapter(simpleAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        startActivity(new Intent(MenuActivity.this, TransitionActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(MenuActivity.this, WifiActivity.class));
                        break;
                    case 2:
                        Intent mobileIntent = new Intent();
                        mobileIntent.setAction("com.github.jiawei.intelligent_parking_system.MobileActivity");
//                        mobileIntent.addCategory("android.intent.category.DEFAULT");
                        mobileIntent.setDataAndType(Uri.parse("http://www.baidu.com"), "image/png");
                        startActivity(mobileIntent);
                        //   startActivity(new Intent(MenuActivity.this, MobileActivity.class));
                        break;
                }
            }
        });
        gridView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

    }

    private List<Map<String, Object>> getDataList() {
        dataList.clear();
        for (int i = 0; i < imgIds.length; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("img", imgIds[i]);
            map.put("txt", txts[i]);
            dataList.add(map);
        }
        return dataList;
    }


}
