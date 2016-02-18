package com.example.wb.calling.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wb.calling.R;
import com.example.wb.calling.entry.Course;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by wb on 16/2/17.
 */
public class CallCourseAdapter extends RecyclerView.Adapter<CallCourseAdapter.ViewHolder> implements ItemTouchAdapter{

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

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if(fromPosition < toPosition){
            for(int i = fromPosition; i < toPosition;i++){
                Collections.swap(data,i,i+1);
            }
        }else {
            for(int i = fromPosition; i > toPosition;i--){
                Collections.swap(data,i,i--);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder{


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

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }
    interface ItemTouchHelperViewHolder {

        void onItemSelected();
        void onItemClear();
    }
}
