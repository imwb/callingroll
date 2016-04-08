package com.example.wb.calling.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.wb.calling.R;
import com.example.wb.calling.adapter.CourseAdapter;
import com.example.wb.calling.entry.Course;
import com.example.wb.calling.manager.CourseManager;

import java.util.ArrayList;

public class MsgActivity extends BaseActivity {

    private ListView courseLv;
    private ArrayList<Course> courseList;
    private CourseAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg);
        initToolbarAndDrawer("消息");
        initMenu(4);
        initLV();
    }

    private void initLV() {
        courseLv = (ListView) findViewById(R.id.lv_courses);
        courseList = CourseManager.getInstance(getApplicationContext()).getAllCouresFromSqlite();
        adapter = new CourseAdapter(this,courseList);
        courseLv.setAdapter(adapter);
        courseLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MsgActivity.this,ChatActivity.class);
                intent.putExtra("courseId",courseList.get(position).getId());
                startActivity(intent);
            }
        });
    }
}
