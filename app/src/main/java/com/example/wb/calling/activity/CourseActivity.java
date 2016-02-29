package com.example.wb.calling.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.example.wb.calling.R;
import com.example.wb.calling.adapter.StudentAdapter;
import com.example.wb.calling.entry.Course;
import com.example.wb.calling.entry.Student;
import com.example.wb.calling.manager.CourseManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.view.annotation.ContentView;

import java.util.ArrayList;
@ContentView(R.layout.activity_course)
public class CourseActivity extends BaseActivity {

    private Course course;

    private ArrayList<Student> students;

    private StudentAdapter adapter;

    private TextView idTxt;

    private TextView numberTxt;

    private TextView timeTxt;

    private TextView classTxt;

    private TextView remarksTxt;

    private ListView stuLv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        String courseId = getIntent().getStringExtra("courseId");
        course = CourseManager.getInstance(getApplicationContext()).getCourseByID(courseId);
        initToolbar(course.getCourse_name());
        initView();
    }

    private void initView() {

        idTxt = (TextView) findViewById(R.id.txt_course_id);
        numberTxt = (TextView) findViewById(R.id.txt_course_number);
        timeTxt = (TextView) findViewById(R.id.txt_course_time);
        classTxt = (TextView) findViewById(R.id.txt_course_class);
        remarksTxt = (TextView) findViewById(R.id.txt_course_remarks);

        idTxt.setText(course.getId());
        numberTxt.setText(course.getCourse_id());
        timeTxt.setText(course.getCourse_room_time());
        classTxt.setText(course.getCourse_class());
        remarksTxt.setText(course.getRemarks());

        stuLv = (ListView) findViewById(R.id.lv_students);
        String stustr = course.getStudent();
        Gson gson = new Gson();
        students = gson.fromJson(stustr,new TypeToken<ArrayList<Student>>(){}.getType());
        Log.d("stus",students.toString());
        adapter = new StudentAdapter(students,this);
        stuLv.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
