package com.example.wb.calling.entry;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by wb on 16/3/2.
 */
@Table(name = "course_order")
public class CourseOrder {

    @Column(name = "id" ,isId = true)
    public String id;

    @Column(name = "courseId")
    public String courseId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCouseId(String couseId) {
        this.courseId = couseId;
    }

    @Override
    public String toString() {
        return "CourseOrder{" +
                "id='" + id + '\'' +
                ", courseId='" + courseId + '\'' +
                '}';
    }
}
