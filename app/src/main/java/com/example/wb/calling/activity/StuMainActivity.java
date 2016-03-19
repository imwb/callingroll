package com.example.wb.calling.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.wb.calling.R;

import cn.bmob.newim.BmobIM;


public class StuMainActivity extends AppCompatActivity implements View.OnClickListener {

    private long exitTime = 0;
    private LinearLayout callLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        setContentView(R.layout.activity_stu_main);
        initView();
    }

    private void initView() {
        callLayout = (LinearLayout) findViewById(R.id.layout_call);
        callLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_call:
                startActivity(new Intent(this,StuCallCourseActivity.class));
                break;
        }
    }

    @Override
    public void onBackPressed() {

        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(this,"再按一次退出程序",Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            //断开 IM 连接
            BmobIM.getInstance().disConnect();
            this.finish();
            System.exit(0);
        }
    }
}
