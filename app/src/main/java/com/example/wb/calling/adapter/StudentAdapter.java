package com.example.wb.calling.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wb.calling.R;
import com.example.wb.calling.entry.Student;

import java.util.ArrayList;

/**
 * Created by wb on 16/2/23.
 */
public class StudentAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Student> data;

    public StudentAdapter(ArrayList<Student> data,Context context) {
        this.data = data;
        this.context = context;
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
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_student,null);
            holder = new Holder();
            holder.nameTxt = (TextView) convertView.findViewById(R.id.txt_student_name);
            holder.numberTxt = (TextView) convertView.findViewById(R.id.txt_student_number);
            holder.layout = (LinearLayout) convertView.findViewById(R.id.layout_studnet);
            convertView.setTag(holder);
        }else {
            holder = (Holder) convertView.getTag();
        }
        Student student = data.get(position);
        holder.nameTxt.setText(student.getName());
        holder.numberTxt.setText(student.getNumber());
//        if((position%2) == 0){
//            holder.layout.setBackgroundColor(context.getResources().getColor(R.color.iconColor));
//        }
        return convertView;
    }
    public class Holder{
        TextView nameTxt;
        TextView numberTxt;
        LinearLayout layout;
    }
}
