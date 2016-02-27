package com.example.wb.calling.entry;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import cn.bmob.v3.BmobObject;

/**
 * Created by wb on 16/2/17.
 */
@Table(name = "course")
public class Course extends BmobObject {
    @Column( name = "id" ,isId = true)
    private String id;
    @Column(name = "course_id")
    private String course_id;
    @Column(name = "course_name")
    private String course_name;
    @Column(name = "course_class")
    private String course_class;
    @Column(name = "course_room_time")
    private String course_room_time;
    @Column(name = "teacher_name")
    private String teacher_name;
    @Column(name = "IsCall")
    private Boolean IsCall;
    @Column(name = "student")
    private String student;
    @Column(name = "remarks")
    private String remarks;

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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id='" + id + '\'' +
                ", course_id='" + course_id + '\'' +
                ", course_name='" + course_name + '\'' +
                ", course_class='" + course_class + '\'' +
                ", course_room_time='" + course_room_time + '\'' +
                ", teacher_name='" + teacher_name + '\'' +
                ", IsCall=" + IsCall +
                ", student='" + student + '\'' +
                ", remarks='" + remarks + '\'' +
                '}';
    }
}
