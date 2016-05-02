package com.example.wb.calling.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.wb.calling.R;
import com.example.wb.calling.entry.Course;
import com.example.wb.calling.entry.Msg;
import com.example.wb.calling.entry.MsgRecord;
import com.example.wb.calling.entry.Student;
import com.example.wb.calling.manager.CourseManager;
import com.example.wb.calling.manager.UserManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMTextMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.listener.ConversationListener;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class ChatActivity extends BaseActivity {

    public static final String TAG = "chat Activity";
    private ListView chatLv;
    private MsgAdapter adapter;
    private EditText contentEdt;
    private Button sendBtn;
    private ProgressBar mPb;
    private ArrayList<Student> students;
    private Map<String, BmobIMConversation> conversationMap = new HashMap<>();
    private String courseId;
    private Course course;
    private List<MsgRecord> msgs = new ArrayList<>();
    private int totalconvers = 0;
    private int totalsend = 0;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    mPb.setVisibility(View.GONE);
                    Log.d(TAG, conversationMap.entrySet().toString());
                    break;
                case 2:
                    mPb.setVisibility(View.GONE);
                    toast("发送成功");
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        courseId = getIntent().getStringExtra("courseId");
        course = CourseManager.getInstance(getApplicationContext()).getCourseByID(courseId);
        initToolbar(course.getCourse_name());
        mToolbar.setSubtitle(course.getCourse_class());
        mPb = (ProgressBar) findViewById(R.id.progressBar);
        initLV();
        initChat();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!isCreateFinished()) {

                }
                mHandler.sendEmptyMessage(1);
            }
        }).start();


    }

    private void initLV() {
        chatLv = (ListView) findViewById(R.id.lv_chat);
        adapter = new MsgAdapter(msgs);
        chatLv.setAdapter(adapter);
        if(adapter.getMsgs().size()>0)
            chatLv.setSelection(adapter.getMsgs().size()-1);
        BmobQuery<MsgRecord> query = new BmobQuery<>();
        query.addWhereEqualTo("courseId",courseId);
        query.setLimit(1000);
        query.findObjects(this, new FindListener<MsgRecord>() {
            @Override
            public void onSuccess(List<MsgRecord> list) {
                Log.d(TAG,list.toString());
                msgs.addAll(list);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }


    private void initChat() {
        contentEdt = (EditText) findViewById(R.id.edt_content);
        sendBtn = (Button) findViewById(R.id.btn_send);

        String stustr = course.getStudent();
        Gson gson = new Gson();
        students = gson.fromJson(stustr, new TypeToken<ArrayList<Student>>() {
        }.getType());
        createConversations();

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = contentEdt.getText().toString();
                if (TextUtils.isEmpty(text.trim())) {
                    toast("请输入内容");
                    return;
                }
                final BmobIMTextMessage msg = new BmobIMTextMessage();
                msg.setContent(text);
                //可设置额外信息
                final Map<String, Object> map = new HashMap<>();
                map.put("courseId", courseId);//随意增加信息
                map.put("type","msg");
                msg.setExtraMap(map);
                final MsgRecord tmsg = new MsgRecord();
                tmsg.setCourseId(courseId);
                tmsg.setCourseClass(course.getCourse_class());
                tmsg.setContent(contentEdt.getText().toString());
                tmsg.setFrom(UserManager.getInstance(getApplicationContext()).getuserInfo().getUsername());
                tmsg.save(ChatActivity.this, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        msgs.add(tmsg);
                        refreshAdapter();
                    }

                    @Override
                    public void onFailure(int i, String s) {

                    }
                });
                for (final Map.Entry<String, BmobIMConversation> entry : conversationMap.entrySet()) {
                    BmobIMConversation c = entry.getValue();
                    BmobIMConversation im;
                    im = BmobIMConversation.obtain(BmobIMClient.getInstance(), c);
                    im.sendMessage(msg, new MessageSendListener() {
                        @Override
                        public void done(BmobIMMessage bmobIMMessage, BmobException e) {
                            final Msg mmsg = new Msg();
                            mmsg.setCourseName(course.getCourse_name());
                            mmsg.setCourseClass(course.getCourse_class());
                            mmsg.setFrom(UserManager.getInstance(getApplicationContext()).getuserInfo().getUsername());
                            mmsg.setTo(entry.getKey());
                            mmsg.setContent(msg.getContent());
                            mmsg.setCourseId(course.getId());
                            mmsg.save(ChatActivity.this, new SaveListener() {
                                @Override
                                public void onSuccess()
                                {   contentEdt.setText("");
                                    Log.d(TAG, "save msg suceess");
                                }

                                @Override
                                public void onFailure(int i, String s) {
                                    Log.d(TAG, "save msg failure" + s);
                                }
                            });
                            totalsend++;
                        }
                    });
                }

                mPb.setVisibility(View.VISIBLE);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (!isSendFinished()) {

                        }
                        mHandler.sendEmptyMessage(2);
                    }
                }).start();
            }
        });


    }

    private boolean isSendFinished() {
        if (totalsend == conversationMap.size())
            return true;
        else
            return false;
    }

    private void createConversations() {


        for (int i = 0; i < students.size(); i++) {
            BmobQuery<BmobUser> query = new BmobQuery<>();
            query.addWhereEqualTo("username", students.get(i).getNumber());
            query.findObjects(this, new FindListener<BmobUser>() {
                @Override
                public void onSuccess(List<BmobUser> list) {
                    if (list.size() > 0) {
                        final BmobUser user = list.get(0);
                        Log.d(TAG, "user" + user.toString());
                        BmobIMUserInfo userInfo = new BmobIMUserInfo();
                        userInfo.setUserId(user.getObjectId());
                        userInfo.setName(" ");
                        BmobIM.getInstance().startPrivateConversation(userInfo, new ConversationListener() {
                            @Override
                            public void done(BmobIMConversation bmobIMConversation, BmobException e) {
                                conversationMap.put(user.getUsername(), bmobIMConversation);
//                                conversations.add(bmobIMConversation);
                                totalconvers++;
                            }
                        });
                    } else {
                        totalconvers++;
                    }

                }

                @Override
                public void onError(int i, String s) {
                    Log.d(TAG, s);
                    totalconvers++;
                }
            });

        }
    }

    private boolean isCreateFinished() {
        if (totalconvers == students.size())
            return true;
        else
            return false;
    }


    private void refreshAdapter(){
        adapter.notifyDataSetChanged();
        if(adapter.getMsgs().size()>0)
            chatLv.setSelection(adapter.getMsgs().size()-1);
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (conversationMap.size() > 0) {
            for (Map.Entry<String, BmobIMConversation> c : conversationMap.entrySet()) {
                BmobIM.getInstance().deleteConversation(c.getValue());
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
    class MsgAdapter extends BaseAdapter{

        private List<MsgRecord> msgs;

        public List<MsgRecord> getMsgs() {
            return msgs;
        }

        public MsgAdapter(List<MsgRecord> msgs) {
            this.msgs = msgs;
        }

        @Override
        public int getCount() {
            return msgs.size();
        }

        @Override
        public Object getItem(int position) {
            return msgs.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder;
            if(convertView == null){
                convertView = LayoutInflater.from(ChatActivity.this).inflate(R.layout.item_chat_msg,null);
                holder = new Holder();
                holder.classTxt = (TextView) convertView.findViewById(R.id.txt_course_class);
                holder.contentTxt = (TextView) convertView.findViewById(R.id.txt_content);
                holder.timeTxt = (TextView) convertView.findViewById(R.id.txt_time);
                convertView.setTag(holder);
            }else {
                holder = (Holder) convertView.getTag();
            }

            MsgRecord msg = msgs.get(position);
            holder.contentTxt.setText(msg.getContent());
            holder.timeTxt.setText(msg.getCreatedAt());
            holder.classTxt.setText(msg.getCourseClass());

            return convertView;
        }

        class Holder{
            TextView classTxt;
            TextView contentTxt;
            TextView timeTxt;
        }
    }
}
