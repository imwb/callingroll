package com.example.wb.calling.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wb.calling.R;
import com.example.wb.calling.entry.Course;
import com.example.wb.calling.entry.CourseOrder;
import com.example.wb.calling.manager.CourseManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by wb on 16/2/17.
 */
public class CallCourseAdapter extends RecyclerView.Adapter<CallCourseAdapter.ViewHolder> implements ItemTouchAdapter{

    private ArrayList<Course> data;
    private ArrayList<Course> orderdata = new ArrayList<>();
    private Context context;
    private OnItemClickListener itemClickListener;

    public CallCourseAdapter(ArrayList<Course> data,Context context) {
        this.context = context;
        this.data = data;
        orderdata.addAll(data);
    }

    public void setOrderdata(ArrayList<Course> orderdata) {
        this.orderdata.clear();
        this.orderdata.addAll(orderdata);
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
        holder.courseIdTxt.setText(course.getId());
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
        Collections.swap(orderdata,fromPosition,toPosition);
        refreshSort();
    }

    public void refreshSort() {
        List<CourseOrder> orders= new ArrayList<>();
        Log.d("data",orderdata.toString());
        for(int i = 0; i < orderdata.size(); i++){
            CourseOrder order = new CourseOrder();
            order.setId(i+"");
            order.setCouseId(orderdata.get(i).getId());
            orders.add(order);
        }
        CourseManager.getInstance(context.getApplicationContext()).refreshOrder(orders);
    }

    @Override
    public void onItemDismiss(int position) {
        data.remove(position);
        setOrderdata(data);
        refreshSort();
        notifyItemRemoved(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder,View.OnClickListener{


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
            itemView.setOnClickListener(this);
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }

        @Override
        public void onClick(View v) {
            if(itemClickListener != null){
                itemClickListener.onItemClick(v,getAdapterPosition());
            }
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.itemClickListener = onItemClickListener;
    }

    interface ItemTouchHelperViewHolder {
        void onItemSelected();
        void onItemClear();
    }

    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }
}
