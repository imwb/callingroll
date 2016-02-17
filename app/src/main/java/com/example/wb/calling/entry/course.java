package com.example.wb.calling.entry;

import cn.bmob.v3.BmobObject;

/**
 * Created by wb on 16/2/17.
 */
public class Course extends BmobObject {
    private String course_id;
    private String course_name;
    private String course_class;
    private String course_room_time;
    private String teacher_name;
    private Boolean IsCall;
    private String student;

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getCourse_class() {
        return course_class;
    }

    public void setCourse_class(String course_class) {
        this.course_class = course_class;
    }

    public String getCourse_room_time() {
        return course_room_time;
    }

    public void setCourse_room_time(String course_room_time) {
        this.course_room_time = course_room_time;
    }

    public String getTeacher_name() {
        return teacher_name;
    }

    public void setTeacher_name(String teacher_name) {
        this.teacher_name = teacher_name;
    }

    public Boolean getCall() {
        return IsCall;
    }

    public void setCall(Boolean call) {
        IsCall = call;
    }

    public String getStudent() {
        return student;
    }

    public void setStudent(String student) {
        this.student = student;
    }
}
