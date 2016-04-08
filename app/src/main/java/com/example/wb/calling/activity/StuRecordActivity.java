package com.example.wb.calling.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.wb.calling.R;
import com.example.wb.calling.entry.RecordItem;
import com.example.wb.calling.manager.UserManager;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

public class StuRecordActivity extends BaseActivity {

    public final static String TAG = "stuRecordActivity";
    private ProgressBar mpb;
    private TabLayout mTabLayout;
    private ViewPager vp;
    private String[] titles = new String[]{"全部", "到课", "旷课", "请假"};
    private List<View> views = new ArrayList<>();
    private List<RecordItem> recordItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stu_record);
        initToolbar("点名记录");
        mpb = (ProgressBar) findViewById(R.id.progressBar);
        initTab();
        loadData();
    }

    private void initTab() {
        mTabLayout = (TabLayout) findViewById(R.id.tab);
        mTabLayout.addTab(mTabLayout.newTab().setText("全部"));
        mTabLayout.addTab(mTabLayout.newTab().setText("到课"));
        mTabLayout.addTab(mTabLayout.newTab().setText("旷课"));
        mTabLayout.addTab(mTabLayout.newTab().setText("请假"));
    }

    /**
     * 加载数据
     */
    private void loadData() {
        BmobQuery<RecordItem> query = new BmobQuery<RecordItem>();
        String userName = UserManager.getInstance(getApplicationContext()).getuserInfo().getUsername();
        query.addWhereEqualTo("stu_num", userName);
        query.setLimit(500);
        query.findObjects(this, new FindListener<RecordItem>() {
            @Override
            public void onSuccess(List<RecordItem> list) {
                Log.d("query ", list.size() + " ");
                recordItems.addAll(list);
                mpb.setVisibility(View.GONE);
                initViewPager();
            }

            @Override
            public void onError(int i, String s) {
                Log.d(" query ", i + s);
            }

        });
    }

    private void initViewPager() {

        View view = LayoutInflater.from(this).inflate(R.layout.item_showrecord_pager, null);
        View view0 = LayoutInflater.from(this).inflate(R.layout.item_showrecord_pager0, null);
        View view1 = LayoutInflater.from(this).inflate(R.layout.item_showrecord_pager1, null);
        View view2 = LayoutInflater.from(this).inflate(R.layout.item_showrecord_pager2, null);

        initView(view, 0);
        initView(view0, 1);
        initView(view1, 2);
        initView(view2, 3);

        views.add(view);
        views.add(view0);
        views.add(view1);
        views.add(view2);

        vp = (ViewPager) findViewById(R.id.viewpager);
        PagerAdapter padapter = new MPagerAdapter();
        vp.setAdapter(padapter);
        mTabLayout.setupWithViewPager(vp);
        mTabLayout.setTabsFromPagerAdapter(padapter);
    }

    private void initView(View view, int id) {
        switch (id) {
            case 0:
                ListView recorditemsLv = (ListView) view.findViewById(R.id.lv_records);
                RecordItemAdapter adapter = new RecordItemAdapter(recordItems);
                recorditemsLv.setAdapter(adapter);

                break;
            case 1:
                ListView recorditemsLv0 = (ListView) view.findViewById(R.id.lv_records);
                RecordItemAdapter adapter0 = new RecordItemAdapter(getItems(0));
                recorditemsLv0.setAdapter(adapter0);
                View emptyView0 = view.findViewById(R.id.empty_view);
                recorditemsLv0.setEmptyView(emptyView0);
                break;
            case 2:
                ListView recorditemsLv1 = (ListView) view.findViewById(R.id.lv_records);
                RecordItemAdapter adapter1 = new RecordItemAdapter(getItems(1));
                recorditemsLv1.setAdapter(adapter1);
                View emptyView1 = view.findViewById(R.id.empty_view);
                recorditemsLv1.setEmptyView(emptyView1);
                break;
            case 3:
                ListView recorditemsLv2 = (ListView) view.findViewById(R.id.lv_records);
                RecordItemAdapter adapter2 = new RecordItemAdapter(getItems(2));
                recorditemsLv2.setAdapter(adapter2);
                View emptyView2 = view.findViewById(R.id.empty_view);
                recorditemsLv2.setEmptyView(emptyView2);
                break;

        }

    }

    private List<RecordItem> getItems(int i) {
        List<RecordItem> items = new ArrayList<>();
        Log.d("local items size ", recordItems.size() + "  ");
        switch (i) {
            case 0:
                for (int j = 0; j < recordItems.size(); j++) {
                    if (recordItems.get(j).getStatus().equals(0)) {
                        items.add(recordItems.get(j));
                    }
                }
                break;
            case 1:
                for (int j = 0; j < recordItems.size(); j++) {
                    if (recordItems.get(j).getStatus().equals(1)) {
                        items.add(recordItems.get(j));
                    }
                }
                break;
            case 2:
                for (int j = 0; j < recordItems.size(); j++) {
                    if (recordItems.get(j).getStatus().equals(2)) {
                        items.add(recordItems.get(j));
                    }
                }
                break;
        }
        return items;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    class MPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return views.size();
        }

        //滑动切换的时候销毁当前的组件
        @Override
        public void destroyItem(ViewGroup container, int position,
                                Object object) {
            container.removeView(views.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(views.get(position));
            return views.get(position);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }

    class RecordItemAdapter extends BaseAdapter {

        List<RecordItem> items;

        public RecordItemAdapter(List<RecordItem> items) {
            this.items = items;
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(StuRecordActivity.this).inflate(R.layout.item_lv_stu_recorditem, null);
                holder = new Holder();
                holder.courseNameTxt = (TextView) convertView.findViewById(R.id.txt_course_name);
                holder.timeTxt = (TextView) convertView.findViewById(R.id.txt_time);
                holder.statuTxt = (TextView) convertView.findViewById(R.id.txt_status);
                holder.praiseTxt = (TextView) convertView.findViewById(R.id.txt_praise);

                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            RecordItem item = items.get(position);
            holder.courseNameTxt.setText(item.getCourseName());
            holder.timeTxt.setText(item.getCreatedAt());
            holder.statuTxt.setText(transStu(item.getStatus()));
            holder.praiseTxt.setText(item.getPraise() + "分");

            return convertView;
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

        class Holder {
            TextView courseNameTxt;
            TextView timeTxt;
            TextView statuTxt;
            TextView praiseTxt;
        }
    }
}
