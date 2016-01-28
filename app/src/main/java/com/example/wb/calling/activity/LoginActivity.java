package com.example.wb.calling.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.example.wb.calling.R;
import com.gc.materialdesign.views.ButtonFlat;

import static com.example.wb.calling.R.id.btn_register;

/**
 * Created by wb on 16/1/23.
 */
public class LoginActivity extends Activity {
    private ButtonFlat regBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        regBtn = (ButtonFlat) findViewById(btn_register);
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });
    }
}
