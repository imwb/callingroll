package com.example.wb.calling.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wb.calling.R;
import com.example.wb.calling.entry.Course;
import com.example.wb.calling.entry.Record;
import com.example.wb.calling.entry.Student;
import com.example.wb.calling.manager.CourseManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.util.DensityUtil;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by wb on 16/2/17.
 */
public class CallResultAdapter extends BaseAdapter {

    private ArrayList<Record> records;
    private ArrayList<Student> students;
    private Course course;
    private Context context;
    private ImageOptions options;
    public CallResultAdapter(Context context, ArrayList<Record> records, String courseID) {
        this.context = context;
        this.records = records;
        course = CourseManager.getInstance(context.getApplicationContext()).getCourseByID(courseID);
        String stus = course.getStudent();
        Gson gson = new Gson();
        students = gson.fromJson(stus, new TypeToken<ArrayList<Student>>() {
        }.getType());
        options = new ImageOptions.Builder()
                .setSize(DensityUtil.dip2px(48), DensityUtil.dip2px(48))
                .setRadius(DensityUtil.dip2px(5))
                // 如果ImageView的大小不是定义为wrap_content, 不要crop.
                .setCrop(true) // 很多时候设置了合适的scaleType也不需要它.
                // 加载中或错误图片的ScaleType
                //.setPlaceholderScaleType(ImageView.ScaleType.MATRIX)
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .setAutoRotate(true)
                .setLoadingDrawableId(R.drawable.ic_trending_down_black_24dp)
                .setFailureDrawableId(R.drawable.ic_mood_bad_black_24dp)
                .build();
        buildRecords();
    }

    /**
     * records 中只有签到的学生记录,按顺序添加 缺席学生的记录
     */
    private void buildRecords() {
        if (records.size() == 0) {
            for (int i = 0; i < students.size(); i++) {
                addRecord(students.get(i), 1);
            }
        } else {
            Collections.sort(records);
            for (int i = 0, j = 0; i < students.size(); i++) {
                if (students.get(i).getNumber().equals(records.get(j).getStu_num())) {
                    j++;
                } else {
                    addRecord(students.get(i),1);
                }
            }
        }
        Collections.sort(records);
        Log.d("result",records.toString());
    }

    public ArrayList<Record> getRecords() {
        return records;
    }

    private void addRecord(Student stu, int status) {
        Record record = new Record();
        record.setCou_id(course.getId());
        record.setCou_name(course.getCourse_name());
        record.setStu_num(stu.getNumber());
        record.setStu_name(stu.getName());
        record.setStatus(status);
        records.add(record);
    }

    public void setData(ArrayList<Record> records) {
        this.records = records;
    }

    @Override
    public int getCount() {
        return records.size();
    }

    @Override
    public Object getItem(int position) {
        return records.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        Record record = records.get(position);
        if (convertView == null) {
            holder = new Holder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_record, null);
            holder.img = (ImageView) convertView.findViewById(R.id.img);
            holder.stu_numTxt = (TextView) convertView.findViewById(R.id.txt_stu_num);
            holder.stu_nameTxt = (TextView) convertView.findViewById(R.id.txt_stu_name);
            holder.statusTxt = (TextView) convertView.findViewById(R.id.txt_status);
            holder.recordLayout = (LinearLayout) convertView.findViewById(R.id.layout_record);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        switch (record.getStatus()){
            case 0:
                holder.recordLayout.setBackgroundColor(context.getResources().getColor(R.color.white));
                break;
            case 1:holder.recordLayout.setBackgroundColor(context.getResources().getColor(R.color.orange));
                break;
            case 2:holder.recordLayout.setBackgroundColor(context.getResources().getColor(R.color.blue));
                break;
        }
        holder.stu_nameTxt.setText(record.getStu_name());
        holder.stu_numTxt.setText(record.getStu_num());
        holder.statusTxt.setText(transStu(record.getStatus()));
        x.image().bind(holder.img,record.getThumbUrl(),options);
        return convertView;
    }

    private String transStu(Integer status) {
        switch (status){
            case 0: return "到课";
            case 1: return "缺席";
            case 2: return "请假";
            default:return " ";
        }
    }

    public class Holder {
        public ImageView img;
        public TextView stu_numTxt;
        public TextView stu_nameTxt;
        public TextView statusTxt;
        public LinearLayout recordLayout;
    }

}
