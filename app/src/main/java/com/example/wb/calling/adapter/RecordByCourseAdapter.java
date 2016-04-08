package com.example.wb.calling.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.wb.calling.R;
import com.example.wb.calling.entry.Record;
import com.example.wb.calling.entry.RecordAdapterEntry;
import com.example.wb.calling.manager.UserManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.FindStatisticsListener;

/**
 * Created by wb on 16/3/28.
 */
public class RecordByCourseAdapter extends BaseExpandableListAdapter {
    private static final String TAG = "record adapter";
    private Context context;
    private List<RecordAdapterEntry> parents = new ArrayList<>();
    private List<Record> allchildrens = new ArrayList<>();


    public RecordByCourseAdapter(Context context, List<Record> allchildrens, List<RecordAdapterEntry> parents) {
        this.context = context;
        this.allchildrens = allchildrens;
        this.parents = parents;
        initData();
    }

    private void initData() {
        BmobQuery<Record> query = new BmobQuery<>();
        query.addWhereEqualTo("teacherName", UserManager.getInstance((context.getApplicationContext())).getuserInfo().getUsername());
        query.groupby(new String[]{"courseName", "courseClass"});
        query.setHasGroupCount(true);
        query.findStatistics(context, Record.class, new FindStatisticsListener() {
            @Override
            public void onSuccess(Object o) {
                JSONArray ary = (JSONArray) o;
                if (ary != null) {
                    int length = ary.length();
                    try {
                        for (int i = 0; i < length; i++) {
                            JSONObject object = ary.getJSONObject(i);
                            Log.d(TAG,object.toString());
                            RecordAdapterEntry entry = new RecordAdapterEntry();
                            entry.setCount(object.getInt("_count"));
                            entry.setCourseName(object.getString("courseName"));
                            entry.setCourseClass(object.getString("courseClass"));
                            parents.add(entry);
                        }
                        notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(int i, String s) {
                Log.d("adapter", i + s);
            }
        });

        BmobQuery<Record> query2 = new BmobQuery<>();
        query2.setLimit(1000);
        query2.order("-createdAt");
        query2.addWhereEqualTo("teacherName", UserManager.getInstance(context.getApplicationContext()).getuserInfo().getUsername());
        query2.findObjects(context, new FindListener<Record>() {
            @Override
            public void onSuccess(List<Record> list) {
                allchildrens.addAll(list);
                notifyDataSetChanged();
            }

            @Override
            public void onError(int i, String s) {
                Log.d(TAG, i + s);
            }
        });
    }

    public boolean isInitFinished() {
        if (allchildrens != null && parents !=null) {
            return true;
        } else
            return false;
    }


    @Override
    public int getGroupCount() {
        if (isInitFinished())
            return parents.size();
        else
            return 0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (isInitFinished()) {
            List<Record> childrens = new ArrayList<>();
            String courseName = parents.get(groupPosition).getCourseName();
            String courseClass = parents.get(groupPosition).getCourseClass();
            for (int i = 0; i < allchildrens.size(); i++) {
                Record record = allchildrens.get(i);
                if (record.getCourseName().equals(courseName)&&record.getCourseClass().equals(courseClass)) {
                    childrens.add(record);
                }
            }
            return childrens.size();
        } else {
            return 0;
        }

    }

    @Override
    public Object getGroup(int groupPosition) {
        if (isInitFinished())
            return parents.get(groupPosition);
        else
            return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        if (isInitFinished()) {


            List<Record> childrens = new ArrayList<>();
            String courseName = parents.get(groupPosition).getCourseName();
            String courseClass = parents.get(groupPosition).getCourseClass();
            for (int i = 0; i < allchildrens.size(); i++) {
                Record record = allchildrens.get(i);
                if (record.getCourseName().equals(courseName)&&record.getCourseClass().equals(courseClass)) {
                    childrens.add(record);
                }
            }
            return childrens.get(childPosition);

        }else
            return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View
            convertView, ViewGroup parent) {
        if(isInitFinished()){
            ParentHolder parentHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_record_parent, null);
                TextView nameTxt = (TextView) convertView.findViewById(R.id.txt_name);
                TextView countTxt = (TextView) convertView.findViewById(R.id.txt_count1);
                TextView classTxt = (TextView) convertView.findViewById(R.id.txt_class);

                parentHolder = new ParentHolder();
                parentHolder.nameTxt = nameTxt;
                parentHolder.countTxt = countTxt;
                parentHolder.classTxt = classTxt;

                convertView.setTag(parentHolder);
            } else {
                parentHolder = (ParentHolder) convertView.getTag();
            }

            parentHolder.nameTxt.setText(parents.get(groupPosition).getCourseName());
            parentHolder.countTxt.setText(parents.get(groupPosition).getCount()+" ");
            parentHolder.classTxt.setText(parents.get(groupPosition).getCourseClass());

            return convertView;
        }else
            return null;

    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View
            convertView, ViewGroup parent) {
        if(isInitFinished()){
            ChildrenHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_record_children, null);
                TextView timeTxt = (TextView) convertView.findViewById(R.id.txt_time);
                TextView s2Txt = (TextView) convertView.findViewById(R.id.txt_status2);
                TextView s1Txt = (TextView) convertView.findViewById(R.id.txt_status1);
                holder = new ChildrenHolder();
                holder.s1Txt = s1Txt;
                holder.s2Txt = s2Txt;
                holder.timeTxt = timeTxt;

                convertView.setTag(holder);
            } else {
                holder = (ChildrenHolder) convertView.getTag();
            }
            Record record = (Record) getChild(groupPosition, childPosition);
            holder.timeTxt.setText(record.getTime());
            holder.s1Txt.setText("旷课 :" + record.getCstatu1() + " 人");
            holder.s2Txt.setText("请假 :" + record.getCstatu2() + " 人");
            return convertView;
        }else {
            return null;
        }

    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {

    }

    @Override
    public void onGroupCollapsed(int groupPosition) {

    }

    @Override
    public long getCombinedChildId(long groupId, long childId) {
        return 0;
    }

    @Override
    public long getCombinedGroupId(long groupId) {
        return 0;
    }

    public List<Record> getAllchildrens() {
        return allchildrens;
    }

    public void setAllchildrens(List<Record> allchildrens) {
        this.allchildrens = allchildrens;
    }

    public List<RecordAdapterEntry> getParents() {
        return parents;
    }

    public void setParents(List<RecordAdapterEntry> parents) {
        this.parents = parents;
    }

    class ParentHolder {
        public TextView nameTxt;
        public TextView countTxt;
        public TextView classTxt;
    }

    class ChildrenHolder {
        TextView timeTxt;
        TextView s2Txt;
        TextView s1Txt;
    }
}
