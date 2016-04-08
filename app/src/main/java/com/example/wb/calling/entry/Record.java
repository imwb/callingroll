package com.example.wb.calling.entry;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;

/**
 * Created by wb on 16/3/25.
 */
public class Record extends BmobObject {
    private String id;
    private String teacherName;
    private String courseClass;
    private String courseName;
    private String courseID;
    private String time;
    private BmobDate date;
    private Integer total;
    //到课
    private Integer cstatu0;
    //旷课
    private Integer cstatu1;
    //请假
    private Integer cstatu2;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseClass() {
        return courseClass;
    }

    public void setCourseClass(String courseClass) {
        this.courseClass = courseClass;
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public BmobDate getDate() {
        return date;
    }

    public void setDate(BmobDate date) {
        this.date = date;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getCstatu0() {
        return cstatu0;
    }

    public void setCstatu0(Integer cstatu0) {
        this.cstatu0 = cstatu0;
    }

    public Integer getCstatu1() {
        return cstatu1;
    }

    public void setCstatu1(Integer cstatu1) {
        this.cstatu1 = cstatu1;
    }

    public Integer getCstatu2() {
        return cstatu2;
    }

    public void setCstatu2(Integer cstatu2) {
        this.cstatu2 = cstatu2;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }
}
