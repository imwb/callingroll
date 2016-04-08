package com.example.wb.calling.entry;

import cn.bmob.v3.BmobObject;

/**
 * Created by wb on 16/4/7.
 */
public class MsgRecord extends BmobObject {

    private String msgId;
    private String courseId;
    private String courseName;
    private String courseClass;
    private String content;

    private String from;
    private String fromName;
    private String to;

    public String getCourseName() {
        return courseName;
    }

    public String getMsgId() {
        return msgId;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getCourseClass() {
        return courseClass;
    }

    public String getContent() {
        return content;
    }

    public String getFrom() {
        return from;
    }

    public String getFromName() {
        return fromName;
    }

    public String getTo() {
        return to;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setCourseClass(String courseClass) {
        this.courseClass = courseClass;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public void setTo(String to) {
        this.to = to;
    }

    @Override
    public String toString() {
        return "Msg{" +
                "msgId='" + msgId + '\'' +
                ", courseId='" + courseId + '\'' +
                ", courseName='" + courseName + '\'' +
                ", courseClass='" + courseClass + '\'' +
                ", content='" + content + '\'' +
                ", from='" + from + '\'' +
                ", fromName='" + fromName + '\'' +
                ", to='" + to + '\'' +
                '}';
    }
}
