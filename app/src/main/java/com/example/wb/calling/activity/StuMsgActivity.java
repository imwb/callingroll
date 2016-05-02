package com.example.wb.calling.activity;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.wb.calling.R;
import com.example.wb.calling.entry.Msg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

public class StuMsgActivity extends BaseActivity {

    private ProgressBar mPb;
    private RecyclerView msgRecy;
    private LinearLayoutManager mlayoutManager;
    private List<Msg> msgs = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stu_msg);
        initToolbar("消息");
        initMsg();
    }

    @Override
    protected void initToolbar(String title) {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(title);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        mToolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setResult(1);
                        finish();
                    }
                }
        );
    }

    private void initMsg() {
        mPb = (ProgressBar) findViewById(R.id.progressBar);
        msgRecy = (RecyclerView) findViewById(R.id.recycler_msg);

        mlayoutManager = new LinearLayoutManager(this);
        mlayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        msgRecy.setLayoutManager(mlayoutManager);
        msgRecy.setHasFixedSize(true);
        msgRecy.setItemAnimator(new DefaultItemAnimator());

        final StuMsgAdapter adapter= new StuMsgAdapter(msgs);
        msgRecy.setAdapter(adapter);
        BmobQuery<Msg> query = new BmobQuery<>();
        query.order("createdAt");
        query.addWhereEqualTo("to", BmobUser.getCurrentUser(this).getUsername());
        query.setLimit(1000);
        query.findObjects(this, new FindListener<Msg>() {
            @Override
            public void onSuccess(List<Msg> list) {
                Collections.reverse(list);
                msgs.addAll(list);
                adapter.notifyDataSetChanged();
                mPb.setVisibility(View.GONE);
            }

            @Override
            public void onError(int i, String s) {
                toast(s);
            }
        });
    }

    @Override
    public void onBackPressed() {
        setResult(1);
        finish();
    }

    @Override
    public void finish() {

        super.finish();
    }

    class StuMsgAdapter extends RecyclerView.Adapter<StuMsgAdapter.ViewHolder>{

        private List<Msg> msgs;

        public StuMsgAdapter(List<Msg> msgs) {
            this.msgs = msgs;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //将布局转化为View 并传递给RecyclerView 封装好的 ViewHolder
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_stu_msg,parent,false
            );
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Msg msg = msgs.get(position);
            holder.contentTxt.setText(msg.getContent());
            holder.courseNameTxt.setText(msg.getCourseName());
            holder.timeTxt.setText(msg.getCreatedAt());
        }

        @Override
        public int getItemCount() {
            return msgs.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView courseNameTxt;
            public TextView contentTxt;
            public TextView timeTxt;
            public ViewHolder(View itemView) {
                super(itemView);
                courseNameTxt = (TextView) itemView.findViewById(R.id.txt_course_name);
                contentTxt = (TextView) itemView.findViewById(R.id.txt_content);
                timeTxt = (TextView) itemView.findViewById(R.id.txt_time);
            }
        }

    }
}
