package com.example.wb.calling.entry;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobGeoPoint;

/**
 * 点名记录
 * Created by wb on 16/3/12.
 */
public class RecordItem extends BmobObject implements Comparable<RecordItem>{

    private String id;
    private String recordId;
    private String stu_id;
    private String stu_num;
    private String stu_name;
    private String time;
    private String filename;
    private String filelocalurl;
    private String fileUrl;
    private String thumbName;
    private String thumbUrl;
    private Integer status;
    private String remark;
    private Integer praise;
    private Double longitude;
    private Double latitude;
    private BmobGeoPoint geoPoint;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public BmobGeoPoint getGeoPoint() {
        return geoPoint;
    }

    public void setGeoPoint(BmobGeoPoint geoPoint) {
        this.geoPoint = geoPoint;
    }

    public Integer getPraise() {
        return praise;
    }

    public void setPraise(Integer praise) {
        this.praise = praise;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getFilelocalurl() {
        return filelocalurl;
    }

    public void setFilelocalurl(String filelocalurl) {
        this.filelocalurl = filelocalurl;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStu_num() {
        return stu_num;
    }

    public void setStu_num(String stu_num) {
        this.stu_num = stu_num;
    }

    public String getStu_id() {
        return stu_id;
    }

    public void setStu_id(String stu_id) {
        this.stu_id = stu_id;
    }


    public String getStu_name() {
        return stu_name;
    }

    public void setStu_name(String stu_name) {
        this.stu_name = stu_name;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getThumbName() {
        return thumbName;
    }

    public void setThumbName(String thumbName) {
        this.thumbName = thumbName;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    @Override
    public int compareTo(RecordItem another) {
        return this.stu_num.compareTo(another.getStu_num());
    }

    @Override
    public String toString() {
        return "RecordItem{" +
                '\'' +
                ", stu_num='" + stu_num + '\'' +
                ", status=" + status +
                '}';
    }
}
