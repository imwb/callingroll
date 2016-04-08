package com.example.wb.calling.fragment;

import android.app.Activity;
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
import android.widget.Button;
import android.widget.ExpandableListView;

import com.example.wb.calling.R;
import com.example.wb.calling.activity.CollectActivity;
import com.example.wb.calling.activity.ShowCallRecordActivity;
import com.example.wb.calling.adapter.RecordByCourseAdapter;
import com.example.wb.calling.entry.Record;
import com.example.wb.calling.entry.RecordAdapterEntry;
import com.example.wb.calling.manager.RecordManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wb on 16/3/28.
 */
public class RecordByCourseFrag extends Fragment {

    public static final String TAG = "RecordCourseFrag";
    private ExpandableListView mlistView;
    private List<RecordAdapterEntry> parents;
    private List<Record> allchildrens;
    private View view;

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
        view = LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_record_course, null);
        initLv(view);
        return view;
    }

    private void initLv(View view) {
        parents = new ArrayList<>();
        allchildrens = new ArrayList<>();
        mlistView = (ExpandableListView) view.findViewById(R.id.listview);
        final RecordByCourseAdapter adapter = new RecordByCourseAdapter(getActivity(), allchildrens, parents);
        mlistView.setAdapter(adapter);
        mlistView.setEmptyView(view.findViewById(R.id.empty_view));
        mlistView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Object object = adapter.getChild(groupPosition, childPosition);
                Log.d("onclick", "onclick");
                if (object != null) {
                    Record record = (Record) object;
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
                return false;
            }
        });

        mlistView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                View alertview = LayoutInflater.from(getActivity()).inflate(R.layout.alert_button, null);
                Button button = (Button) alertview.findViewById(R.id.btn_collect);

                builder.setView(alertview);
                final AlertDialog dialog = builder.show();
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RecordAdapterEntry entry = (RecordAdapterEntry) adapter.getGroup(position);
                        String courseName = entry.getCourseName();
                        String courseClass = entry.getCourseClass();
                        Intent intent = new Intent(getActivity(), CollectActivity.class);
                        intent.putExtra("courseName", courseName);
                        intent.putExtra("courseClass", courseClass);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });
                return true;
            }
        });


    }

    private void showDeleteDilog(final String recordId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("确定删除点名记录？")
                .setNegativeButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RecordManager.getInstance(getActivity().getApplicationContext()).deleteByRecordID(recordId);
                    }
                });
        builder.show();
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
        initLv(view);
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
