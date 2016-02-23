package com.example.wb.calling.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.example.wb.calling.R;

public class AddCourseActivity extends BaseActivity {

    private Button addRollBtn;
    static final int ADDROLL = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initToolbar("添加课程");
        initView();

    }

    private void initView() {
        addRollBtn = (Button) findViewById(R.id.btn_add_roll);
        addRollBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(
                        new Intent(AddCourseActivity.this,AddStudentActivity.class),
                        ADDROLL);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
