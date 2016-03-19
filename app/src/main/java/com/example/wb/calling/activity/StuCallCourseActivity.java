package com.example.wb.calling.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.wb.calling.R;
import com.example.wb.calling.adapter.StuCourseAdapter;
import com.example.wb.calling.entry.Course;
import com.example.wb.calling.manager.CourseManager;
import com.example.wb.calling.utils.RegexUtil;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rengwuxian.materialedittext.validation.METValidator;
import com.shamanland.fab.ShowHideOnScroll;

import java.util.ArrayList;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.GetListener;

public class StuCallCourseActivity extends BaseActivity {

    private ListView courseLv;
    private FloatingActionButton fab;
    private ArrayList<Course> courses;
    private StuCourseAdapter adapter;
    private CourseManager manager;
    private String id ="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stu_call_course);
        manager = CourseManager.getInstance(getApplicationContext());
        initToolbar("签到");
        initFab();
        initLv();
    }

    private void initLv() {
        courseLv = (ListView) findViewById(R.id.lv_stu_call);
        courses = manager.getAllCouresFromSqlite();
        adapter = new StuCourseAdapter(this, courses);
        courseLv.setAdapter(adapter);
        courseLv.setOnTouchListener(new ShowHideOnScroll(fab));
        courseLv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(StuCallCourseActivity.this);
                builder.setTitle("删除课程");
                builder.setMessage("确定删除:"+courses.get(position).getCourse_name()+"吗？");
                builder.setNegativeButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        manager.deleteInSqlite(courses.get(position));
                        courses.remove(position);
                        adapter.notifyDataSetChanged();
                    }
                });
                builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                return true;
            }
        });
        courseLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Course course = courses.get(position);
                Intent intent = new Intent(StuCallCourseActivity.this,StuCallActivity.class);
                intent.putExtra("userID",course.getUserID());
                startActivity(intent);
            }
        });
    }

    private void initFab() {
        fab = (FloatingActionButton) findViewById(R.id.fab_add_course);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(StuCallCourseActivity.this);
                final Course course = new Course();
                View view = LayoutInflater.from(StuCallCourseActivity.this).inflate(R.layout.dialog_stu_add_call,null);
                final MaterialEditText idEdt = (MaterialEditText) view.findViewById(R.id.edt_course_id);
                idEdt.addValidator(new METValidator("格式不正确！") {
                    @Override
                    public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
                        if (RegexUtil.isIDCode(text.toString())&& !isEmpty) {
                            id = idEdt.getText().toString();
                            return true;
                        } else {
                            return false;
                        }
                    }
                });

                builder.setTitle("添加课程");
                builder.setView(view);
                builder.setNegativeButton("添加", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        BmobQuery<Course> query = new BmobQuery<Course>();
                        query.getObject(StuCallCourseActivity.this,id, new GetListener<Course>() {

                            @Override
                            public void onSuccess(Course object) {
                                // TODO Auto-generated method stub
                                course.setId(object.getObjectId());
                                course.setUserID(object.getUserID());
                                course.setTeacher_name(object.getTeacher_name());
                                course.setCourse_name(object.getCourse_name());
                                manager.saveCourse(course);
                                Log.d("course",course.toString());
                                courses.add(course);
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onFailure(int code, String msg) {
                                // TODO Auto-generated method stub
                                Log.i("life", "onFailure = "+code+",msg = "+msg);
                                toast(msg);
                            }
                        });
                    }
                });
                builder.show();

            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
