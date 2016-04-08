package com.example.wb.calling.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;

import com.example.wb.calling.R;
import com.example.wb.calling.entry.User;

import cn.bmob.v3.BmobUser;

/**
 * 启动界面
 *
 * @author :smile
 * @project:SplashActivity
 * @date :2016-01-15-18:23
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                User user = BmobUser.getCurrentUser(SplashActivity.this, User.class);
                if (user == null) {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));

                    finish();
                } else {
                    if(user.getType() == 0){
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    }else {
                        startActivity(new Intent(SplashActivity.this, StuMainActivity.class));
                    }
                    finish();
                }
            }
        }, 1000);

    }
}
