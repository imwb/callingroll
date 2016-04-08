package com.example.wb.calling.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.wb.calling.R;
import com.example.wb.calling.entry.Record;
import com.example.wb.calling.entry.RecordItem;
import com.example.wb.calling.entry.RecordItemEntry;
import com.example.wb.calling.manager.UserManager;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

public class CollectActivity extends BaseActivity {

    public static final String TAG = "collection activity";
    private String courseName;
    private String courseClass;
    private List<Record> records;
    private ArrayList<RecordItemEntry>[] entryAarry;
    private CollectItemAdapter adapter;

    private LinearLayout titleLayout;
    private ListView mListView;
    private ProgressBar mPb;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    Log.d(TAG,entryAarry.toString());
                    adapter = new CollectItemAdapter(entryAarry);
                    mListView.setAdapter(adapter);
                    mPb.setVisibility(View.GONE);
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
        Intent intent = getIntent();
        if (intent != null) {
            courseName = intent.getStringExtra("courseName");
            courseClass = intent.getStringExtra("courseClass");
            initView();
            initTitle();


        }
    }

    private void initView() {
        titleLayout = (LinearLayout) findViewById(R.id.layout_title);
        mListView = (ListView) findViewById(R.id.listview);
        mPb = (ProgressBar) findViewById(R.id.progressBar);
        initToolbar("汇总");
        mToolbar.setSubtitle(courseName + "  " + courseClass);
    }

    private void initTitle() {
        records = new ArrayList<>();
        Log.d(TAG, courseName);
        Log.d(TAG, courseClass);
        BmobQuery<Record> query = new BmobQuery<>();
        query.setLimit(100);
        query.addWhereEqualTo("teacherName", UserManager.getInstance(getApplicationContext()).getuserInfo().getUsername());
        query.addWhereEqualTo("courseName", courseName);
        query.addWhereEqualTo("courseClass", courseClass);
        query.findObjects(this, new FindListener<Record>() {
            @Override
            public void onSuccess(List<Record> list) {
                records.addAll(list);
                for (int i = 0; i < list.size(); i++) {
                    TextView tv = new TextView(CollectActivity.this);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(200, ViewGroup.LayoutParams.MATCH_PARENT);
                    params.setMargins(10, 0, 10, 0);
                    params.gravity = Gravity.CENTER;
                    tv.setLayoutParams(params);
                    tv.setText(list.get(i).getTime().substring(0, 6));
                    tv.setTextColor(Color.WHITE);
                    tv.setBackgroundColor(getResources().getColor(R.color.ToolBarColor));

                    titleLayout.addView(tv);
                }
                initRecordItems();
            }

            @Override
            public void onError(int i, String s) {
                toast(i + s);
            }
        });

    }

    private void initRecordItems() {
        entryAarry = new ArrayList[records.size()];
        for (int i = 0; i < records.size(); i++) {
            final int j = i;
            Record record = records.get(i);
            final ArrayList<RecordItemEntry> entries = new ArrayList<>();
            BmobQuery<RecordItem> query = new BmobQuery<>();
            query.setLimit(500);
            query.addWhereEqualTo("recordId", record.getObjectId());
            query.findObjects(this, new FindListener<RecordItem>() {
                @Override
                public void onSuccess(List<RecordItem> list) {
                    for (RecordItem item : list) {
                        RecordItemEntry entry = new RecordItemEntry();
                        entry.setStu_name(item.getStu_name());
                        entry.setStu_number(item.getStu_num());
                        entry.setStatu(item.getStatus());
                        entries.add(entry);
                    }
                    entryAarry[j] = entries;
                }

                @Override
                public void onError(int i, String s) {
                    toast(s);
                }
            });
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(!isFinished()){

                }
                mHandler.sendEmptyMessage(1);
            }
        }).start();

    }

    private boolean isFinished() {
        boolean isFinished = true;
        for (int i = 0; i < entryAarry.length; i++) {
            if (entryAarry[i] == null) {
                isFinished = false;
                break;
            }
        }
        return isFinished;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    class CollectItemAdapter extends BaseAdapter {
        private ArrayList<RecordItemEntry>[] entryAarry;

        public CollectItemAdapter(ArrayList<RecordItemEntry>[] entryAarry) {
            this.entryAarry = entryAarry;
        }

        @Override
        public int getCount() {
            if (entryAarry[0] != null)
                return entryAarry[0].size();
            else
                return 0;
        }

        @Override
        public Object getItem(int position) {
            if ((getCount() != 0) && isFinished()) {
                String[] entry = new String[2 + entryAarry.length];
                entry[0] = entryAarry[0].get(position).getStu_number();
                entry[1] = entryAarry[0].get(position).getStu_name();
                for (int i = 2; i < entry.length; i++) {
                    entry[i] = transStu(entryAarry[i - 2].get(position).getStatu());
                }
                return entry;
            } else
                return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if ((getCount() != 0) && isFinished()) {
                String[] content = (String[]) getItem(position);
                convertView = LayoutInflater.from(CollectActivity.this).inflate(R.layout.item_collect_record, null);
                LinearLayout itemLayout = (LinearLayout) convertView.findViewById(R.id.layout_item);

                TextView nameTxt = (TextView) convertView.findViewById(R.id.txt_name);
                TextView numTxt = (TextView) convertView.findViewById(R.id.txt_num);

                numTxt.setText(content[0]);
                nameTxt.setText(content[1]);
                for (int i = 0; i < content.length - 2; i++) {
                    TextView tv = new TextView(CollectActivity.this);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(200, ViewGroup.LayoutParams.MATCH_PARENT);
                    params.setMargins(10, 0, 10, 0);
                    params.gravity = Gravity.CENTER_VERTICAL;
                    tv.setLayoutParams(params);
                    tv.setText(content[i + 2]);
                    if(content[i+2].equals("缺席")){
                        tv.setTextColor(getResources().getColor(R.color.edPrimaryColor));
                    }else if(content[i+2].equals("请假")){
                        tv.setTextColor(getResources().getColor(R.color.ToolBarColor));
                    }else {
                        tv.setTextColor(Color.BLACK);
                    }
                    itemLayout.addView(tv);
                }

                return convertView;
            } else
                return null;
        }


        private String transStu(Integer status) {
            switch (status) {
                case 0:
                    return "到课";
                case 1:
                    return "缺席";
                case 2:
                    return "请假";
                default:
                    return " ";
            }
        }


    }
}
