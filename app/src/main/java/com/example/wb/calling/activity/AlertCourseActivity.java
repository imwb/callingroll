package com.example.wb.calling.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.example.wb.calling.R;
import com.example.wb.calling.entry.Course;
import com.example.wb.calling.manager.CourseManager;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rengwuxian.materialedittext.validation.METValidator;

public class AlertCourseActivity extends BaseActivity {

    private Button addRollBtn;
    private Button saveBtn;
    private Course mcourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        String courseId = getIntent().getStringExtra("courseId");
        mcourse = CourseManager.getInstance(getApplicationContext()).getCourseByID(courseId);
        setSupportActionBar(toolbar);
        initToolbar("修改课程");
        initView();

    }

    private void initView() {
        addRollBtn = (Button) findViewById(R.id.btn_add_roll);
        addRollBtn.setText("修改名单");
        addRollBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AlertCourseActivity.this,AddStudentActivity.class);
                intent.putExtra("jstudents",mcourse.getStudent());
                startActivityForResult(intent, REQUEST_ADD_ROLL);
            }
        });

        final MaterialEditText idEdt = (MaterialEditText) findViewById(R.id.edt_course_id);
        final MaterialEditText nameEdt = (MaterialEditText) findViewById(R.id.edt_course_name);
        final MaterialEditText timeEdt = (MaterialEditText) findViewById(R.id.edt_course_time);
        final MaterialEditText classEdt = (MaterialEditText) findViewById(R.id.edt_course_class);
        final MaterialEditText remarksEdt = (MaterialEditText) findViewById(R.id.edt_course_remarks);

        idEdt.setText(mcourse.getCourse_id());
        nameEdt.setText(mcourse.getCourse_name());
        timeEdt.setText(mcourse.getCourse_room_time());
        classEdt.setText(mcourse.getCourse_class());
        remarksEdt.setText(mcourse.getRemarks());

        idEdt.addValidator(new METValidator("课程号不能为空") {
            @Override
            public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
                if(isEmpty){
                    return false;
                }else {
                    mcourse.setCourse_id(idEdt.getText().toString());
                    return true;
                }
            }
        });

        nameEdt.addValidator(new METValidator("课程名不能为空") {
            @Override
            public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
                if(isEmpty){
                    return false;
                }else {
                    mcourse.setCourse_name(nameEdt.getText().toString());
                    return true;
                }
            }
        });

        timeEdt.addValidator(new METValidator("") {
            @Override
            public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
                mcourse.setCourse_room_time(timeEdt.getText().toString());
                return true;
            }
        });

        classEdt.addValidator(new METValidator("") {
            @Override
            public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
                mcourse.setCourse_class(classEdt.getText().toString());
                return true;
            }
        });

        remarksEdt.addValidator(new METValidator("") {
            @Override
            public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
                mcourse.setRemarks(remarksEdt.getText().toString());
                return true;
            }
        });

        saveBtn = (Button) findViewById(R.id.btn_save_course);
        saveBtn.setText("修改");
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(idEdt.validate() && timeEdt.validate() && nameEdt.validate()
                        && classEdt.validate() && remarksEdt.validate()){

                    if(mcourse.getStudent() != null && !mcourse.getStudent().isEmpty()){
                        CourseManager.getInstance(getApplicationContext()).updateCourse(mcourse);
                    }else {
                        toast("请添加学生名单");
                    }

                }else {
                    toast("请核对信息");
                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode){
            case AddStudentActivity.RESULT_ROLL:
                if(data != null){
                    String jstudents = data.getStringExtra("students");
                    mcourse.setStudent(jstudents);
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_UPDATE_COURSE);
        finish();
    }
}
