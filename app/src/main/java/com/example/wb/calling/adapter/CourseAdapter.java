package com.example.wb.calling.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.wb.calling.R;
import com.example.wb.calling.entry.Course;

import java.util.ArrayList;

/**
 * Created by wb on 16/2/17.
 */
public class CourseAdapter extends BaseAdapter{

    private ArrayList<Course> data;
    private Context context;

    public CourseAdapter(Context  context ,ArrayList<Course> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        Course course = data.get(position);
        if(convertView == null){
            holder = new Holder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_course,null);
            holder.courseIdTxt = (TextView) convertView.findViewById(R.id.txt_course_id);
            holder.courseNameTxt = (TextView) convertView.findViewById(R.id.txt_course_name);
            holder.courseClassTxt = (TextView) convertView.findViewById(R.id.txt_course_class);
            convertView.setTag(holder);
        }else {
            holder = (Holder) convertView.getTag();
        }
        holder.courseIdTxt.setText(course.getCourse_id());
        holder.courseNameTxt.setText(course.getCourse_name());
        holder.courseClassTxt.setText(course.getCourse_class());
        return convertView;
    }

    public class Holder {
        public TextView courseIdTxt;
        public TextView courseNameTxt;
        public TextView courseClassTxt;
    }
}
