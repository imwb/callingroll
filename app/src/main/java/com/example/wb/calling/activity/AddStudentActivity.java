package com.example.wb.calling.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.wb.calling.R;
import com.example.wb.calling.adapter.StudentAdapter;
import com.example.wb.calling.entry.Student;
import com.shamanland.fab.ShowHideOnScroll;

import java.util.ArrayList;

public class AddStudentActivity extends BaseActivity {

    private ListView stuLv;
    private ArrayList<Student> students;
    private LinearLayout stumenuLayout;
    private Button importBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initToolbar("学生名单");
        initLv();
        initView();
    }

    private void initView() {
        importBtn = (Button) findViewById(R.id.btn_import);
        importBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(AddStudentActivity.this,FileExplorerActivity.class),1);
            }
        });
    }

    private void initLv() {
        stuLv = (ListView) findViewById(R.id.list_student);
        stumenuLayout = (LinearLayout) findViewById(R.id.layout_student_menu);

        students = new ArrayList<>();
        Student s1 = new Student();
        s1.setName("吴比");
        s1.setNumber("2012201223");
        Student s2 = new Student();
        s2.setName("吴比");
        s2.setNumber("2012201223");
        Student s3 = new Student();
        s3.setName("吴比");
        s3.setNumber("2012201223");
        Student s4 = new Student();
        s4.setName("吴比");
        s4.setNumber("2012201223");
        Student s5 = new Student();
        s5.setName("吴比");
        s5.setNumber("2012201223");
        Student s6 = new Student();
        s6.setName("吴比");
        s6.setNumber("2012201223");
        Student s7 = new Student();
        s7.setName("吴比");
        s7.setNumber("2012201223");
        Student s8 = new Student();
        s8.setName("吴比");
        s8.setNumber("2012201223");
        Student s9 = new Student();
        s9.setName("吴比");
        s9.setNumber("2012201223");
        Student s10 = new Student();
        s10.setName("吴比");
        s10.setNumber("2012201223");
        Student s11 = new Student();
        s11.setName("吴比");
        s11.setNumber("2012201223");
        Student s12 = new Student();
        s12.setName("吴比");
        s12.setNumber("2012201223");
        students.add(s1);
        students.add(s2);
        students.add(s3);
        students.add(s4);
        students.add(s5);
        students.add(s6);
        students.add(s7);
        students.add(s8);
        students.add(s9);
        students.add(s10);
        students.add(s11);
        students.add(s12);

        StudentAdapter adapter = new StudentAdapter(students,this);

        stuLv.setAdapter(adapter);
        stuLv.setOnTouchListener( new ShowHideOnScroll(stumenuLayout));


    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case FileExplorerActivity.RESULTDIR:
                String path = data.getStringExtra("path");
                toast(path);
        }
    }
}
