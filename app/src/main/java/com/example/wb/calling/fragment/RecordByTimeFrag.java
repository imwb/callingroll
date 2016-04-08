package com.example.wb.calling.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.wb.calling.R;
import com.example.wb.calling.activity.ShowCallRecordActivity;
import com.example.wb.calling.entry.Record;
import com.example.wb.calling.manager.RecordManager;
import com.example.wb.calling.manager.UserManager;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by wb on 16/3/28.
 */
public class RecordByTimeFrag extends Fragment {


    public static final String TAG = "RecordByTimeFrag";
    private View view;
    public boolean isDelete = false;
    public MyAdapter adapter;
    List<Record> records;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d(TAG, "onAttach");

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateview");
        view = LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_record_time, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        final ListView lv = (ListView) view.findViewById(R.id.lv_records);
        records = new ArrayList<>();
        adapter = new MyAdapter(getActivity(), records);
        lv.setEmptyView(view.findViewById(R.id.empty_view));
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Record record = records.get(position);
                String courseName = record.getCourseName();
                String courseClass = record.getCourseClass();
                String time = record.getDate().getDate();
                String recordId = record.getObjectId();
                Intent intent = new Intent(getActivity(), ShowCallRecordActivity.class);
                intent.putExtra("courseName", courseName);
                intent.putExtra("courseClass", courseClass);
                intent.putExtra("time", time);
                intent.putExtra("recordID", recordId);
                startActivity(intent);
            }
        });
//        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                Record record = records.get(position);
//                String recordId = record.getObjectId();
//
//                showDeleteDilog(recordId,position);
//                return true;
//            }
//        });


    }

    private boolean showDeleteDilog(final String recordId, final Integer positoin) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("确定删除点名记录？")
                .setNegativeButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        records.remove(positoin);
                        adapter.notifyDataSetChanged();
                        RecordManager.getInstance(getActivity().getApplicationContext()).deleteByRecordID(recordId);
                        isDelete = true;

                    }
                });
        builder.show();
        return isDelete;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart");

        super.onStart();
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume");
        initView(view);
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.d(TAG, "onStop");
        super.onStop();
    }

    @Override
    public void onDetach() {
        Log.d(TAG, "onDetach");
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "onDestroyView");
        super.onDestroyView();
    }

}

class MyAdapter extends BaseAdapter {

    protected List<Record> records;
    private Context context;

    public MyAdapter(Context context, List<Record> records) {
        this.context = context;
        this.records = records;
        initData();
    }


    private void initData() {
        BmobQuery<Record> query2 = new BmobQuery<>();
        query2.setLimit(1000);
        query2.order("-createdAt");
        query2.addWhereEqualTo("teacherName", UserManager.getInstance(context.getApplicationContext()).getuserInfo().getUsername());
        //先判断是否有缓存
//        boolean isCache = query2.hasCachedResult(context, Record.class);
//        if (isCache) {
//            query2.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);    // 先从缓存取数据，如果没有的话，再从网络取。
//        } else {
//            query2.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);    // 如果没有缓存的话，则先从网络中取
//        }
        query2.findObjects(context, new FindListener<Record>() {
            @Override
            public void onSuccess(List<Record> list) {
                records.addAll(list);
                notifyDataSetChanged();
            }

            @Override
            public void onError(int i, String s) {
                Log.d("show record", i + s);
            }
        });
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
        Holder mHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_recordbytime, null);
            mHolder = new Holder();
            mHolder.courseNameTxt = (TextView) convertView.findViewById(R.id.txt_course_name);
            mHolder.courseClassTxt = (TextView) convertView.findViewById(R.id.txt_course_class);
            mHolder.timeTxt = (TextView) convertView.findViewById(R.id.txt_time);
            mHolder.s0Txt = (TextView) convertView.findViewById(R.id.txt_s0);
            mHolder.s1Txt = (TextView) convertView.findViewById(R.id.txt_s1);
            mHolder.s2Txt = (TextView) convertView.findViewById(R.id.txt_s2);
            convertView.setTag(mHolder);
        } else {
            mHolder = (Holder) convertView.getTag();
        }
        Record record = records.get(position);
        mHolder.courseNameTxt.setText(record.getCourseName());
        mHolder.courseClassTxt.setText(record.getCourseClass());
        mHolder.timeTxt.setText(record.getDate().getDate());
        mHolder.s0Txt.setText("到课:" + record.getCstatu0());
        mHolder.s1Txt.setText("旷课:" + record.getCstatu1());
        mHolder.s2Txt.setText("请假:" + record.getCstatu2());
        return convertView;
    }

    class Holder {
        TextView courseNameTxt;
        TextView timeTxt;
        TextView courseClassTxt;
        TextView s0Txt;
        TextView s1Txt;
        TextView s2Txt;
    }
}