package com.example.wb.calling.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wb.calling.R;
import com.example.wb.calling.entry.Course;

import java.util.ArrayList;

/**
 * Created by wb on 16/2/17.
 */
public class CallCourseAdapter extends RecyclerView.Adapter<CallCourseAdapter.ViewHolder> {

    ArrayList<Course> data;

    public CallCourseAdapter(ArrayList<Course> data) {
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //将布局转化为View 并传递给RecyclerView 封装好的 ViewHolder
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_call,parent,false
        );
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //建立起viewholder 和 数据之间的联系
        Course course = data.get(position);
        holder.courseIdTxt.setText(course.getCourse_id());
        holder.courseTimeTxt.setText(course.getCourse_room_time());
        holder.courseClassTxt.setText(course.getCourse_class());
        holder.courseNameTxt.setText(course.getCourse_name());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView courseIdTxt;
        public TextView courseNameTxt;
        public TextView courseClassTxt;
        public TextView courseTimeTxt;
        public ViewHolder(View itemView) {
            super(itemView);
            courseIdTxt = (TextView) itemView.findViewById(R.id.txt_course_id);
            courseNameTxt = (TextView) itemView.findViewById(R.id.txt_course_name);
            courseClassTxt = (TextView) itemView.findViewById(R.id.txt_course_class);
            courseTimeTxt = (TextView) itemView.findViewById(R.id.txt_course_time);
        }
    }
}
