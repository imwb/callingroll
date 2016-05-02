package com.example.wb.calling.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wb.calling.R;
import com.example.wb.calling.entry.RecordItem;
import com.example.wb.calling.entry.User;
import com.example.wb.calling.manager.CourseManager;

import net.sf.json.JSONObject;

import java.util.List;
import java.util.Map;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.CountListener;
import de.greenrobot.event.EventBus;


public class StuMainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = "StuMainActivity";
    private long exitTime = 0;
    private LinearLayout callLayout;
    private LinearLayout recordLayout;
    private LinearLayout msgLayout;
    private ImageView msgImg;
    private TextView usernameTxt;
    private TextView countTxt;
    private TextView countNoTxt;
    private TextView countLeaveTxt;
    private Button logoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        setContentView(R.layout.activity_stu_main);
        initView();
        loadInfo();
        EventBus.getDefault().register(this);
    }

    private void loadInfo() {
        User user = BmobUser.getCurrentUser(this,User.class);
        usernameTxt.setText(user.getName());
        BmobQuery<RecordItem> query = new BmobQuery<>();
        query.addWhereEqualTo("stu_num", user.getUsername());
        query.addWhereEqualTo("status",0);
        query.count(this, RecordItem.class, new CountListener() {
            @Override
            public void onSuccess(int i) {
                countTxt.setText(i+" ");
            }

            @Override
            public void onFailure(int i, String s) {
                countTxt.setText(0+" ");
            }
        });

        BmobQuery<RecordItem> query1 = new BmobQuery<>();
        query1.addWhereEqualTo("stu_num", user.getUsername());
        query1.addWhereEqualTo("status",1);
        query1.count(this, RecordItem.class, new CountListener() {
            @Override
            public void onSuccess(int i) {
                countNoTxt.setText(i+" ");
            }

            @Override
            public void onFailure(int i, String s) {
                countNoTxt.setText(0+" ");
            }
        });

        BmobQuery<RecordItem> query2 = new BmobQuery<>();
        query2.addWhereEqualTo("stu_num", user.getUsername());
        query2.addWhereEqualTo("status",2);
        query2.count(this, RecordItem.class, new CountListener() {
            @Override
            public void onSuccess(int i) {
                countLeaveTxt.setText(i+" ");
            }

            @Override
            public void onFailure(int i, String s) {
                countLeaveTxt.setText(0+" ");
            }
        });

    }


    private void initView() {
        usernameTxt = (TextView) findViewById(R.id.txt_name);
        countTxt = (TextView) findViewById(R.id.txt_count);
        countNoTxt = (TextView) findViewById(R.id.txt_count_no);
        countLeaveTxt = (TextView) findViewById(R.id.txt_count_leave);
        msgImg = (ImageView) findViewById(R.id.img_msg);
        logoutBtn = (Button) findViewById(R.id.btn_logoff);
        logoutBtn.setOnClickListener(this);
        callLayout = (LinearLayout) findViewById(R.id.layout_call);
        recordLayout = (LinearLayout) findViewById(R.id.layout_record);
        msgLayout = (LinearLayout) findViewById(R.id.layout_msg);

        callLayout.setOnClickListener(this);
        recordLayout.setOnClickListener(this);
        msgLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_call:
                startActivity(new Intent(this,StuCallCourseActivity.class));
                break;
            case R.id.layout_record:
                startActivity(new Intent(this,StuRecordActivity.class));
                break;
            case R.id.layout_msg:
                startActivityForResult(new Intent(this,StuMsgActivity.class),1);
                break;
            case R.id.btn_logoff:
                logOff();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case 1:
                msgImg.setImageResource(R.drawable.ic_markunread_black_24dp);
                break;
        }
    }

    private void logOff() {
        clearUserInfo();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    private void clearUserInfo() {
        SharedPreferences sp = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("username", "");
        editor.putString("objectId", "");
        editor.putString("password", "");
        editor.putString("name", "");
        editor.putString("email", "");
        editor.commit();
        CourseManager.getInstance(getApplicationContext()).clearSqlite();
        BmobUser.logOut(this);
    }
    /**注册离线消息接收事件
     * @param event
     */
    public void onEventMainThread(OfflineMessageEvent event){

        msgImg.setImageResource(R.drawable.ic_new_msg_black_24dp);
        Log.d(TAG,"envent thread :"+Thread.currentThread().getName());
        //SDK内部已根据用户id对离线消息进行分组
        Map<String,List<MessageEvent>> map =event.getEventMap();
        Log.d(TAG,"离线消息属于"+map.size()+"个用户");
        for (Map.Entry<String, List<MessageEvent>> entry : map.entrySet()) {
            List<MessageEvent> list =entry.getValue();
            Log.d(TAG,"off line message" + list.toString());
        }
    }

    /**注册消息接收事件
     * @param event
     * 1、与用户相关的由开发者自己维护，SDK内部只存储用户信息
     * 2、开发者获取到信息后，可调用SDK内部提供的方法更新会话
     */
    public void onEventMainThread(final MessageEvent event){
        BmobIMMessage msg = event.getMessage();
        String extra = msg.getExtra();
        if(extra != null && extra !=""){
            JSONObject object = JSONObject.fromObject(extra);
            String type = object.getString("type");
            if(type.equals("msg"));{//消息发来的信息
                msgImg.setImageResource(R.drawable.ic_new_msg_black_24dp);
                Log.d(TAG,"msg" + event);
            }
            if(type.equals("callresult")){//点名结果
                Toast.makeText(this,msg.getContent(),Toast.LENGTH_LONG).show();
            }



        }

    }
    @Override
    public void onBackPressed() {

        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(this,"再按一次退出程序",Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            //断开 IM 连接
            EventBus.getDefault().unregister(this);
            BmobIM.getInstance().disConnect();
            this.finish();
            System.exit(0);
        }
    }
}
