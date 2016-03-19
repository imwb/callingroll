package com.example.wb.calling.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.wb.calling.R;
import com.example.wb.calling.adapter.CallResultAdapter;
import com.example.wb.calling.entry.Record;
import com.example.wb.calling.manager.UserManager;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.yydcdut.sdlv.Menu;
import com.yydcdut.sdlv.MenuItem;
import com.yydcdut.sdlv.SlideAndDragListView;

import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMTextMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.listener.ConversationListener;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.exception.BmobException;
import cn.iwgang.countdownview.CountdownView;
import de.greenrobot.event.EventBus;

public class CallActivity extends BaseActivity {

    private String code;
    private Button callBtn;
    private TextView codeTxt;
    private CountdownView timeView;
    private TextView callingTxt;
    private ArrayList<Record> records = new ArrayList<>();
    private String cou_id = " ";
    private String cou_name = " ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);
        Intent intent = getIntent();
        if (intent != null) {
            cou_id = intent.getStringExtra("cou_id");
            cou_name = intent.getStringExtra("cou_name");
        }
        initToolbar("点名" + cou_name);
        initView();
    }

    private void initView() {
        //初始化验证码
        code = getRandomCode();
        codeTxt = (TextView) findViewById(R.id.txt_code);
        codeTxt.setText("验证码：" + code);

        //初始化点名
        timeView = (CountdownView) findViewById(R.id.cd);
        callingTxt = (TextView) findViewById(R.id.txt_calling);
        callBtn = (Button) findViewById(R.id.btn_call);

        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBtn.setVisibility(View.GONE);
                callingTxt.setVisibility(View.VISIBLE);
                timeView.setVisibility(View.VISIBLE);
                timeView.start(3000);
                EventBus.getDefault().register(CallActivity.this);
                timeView.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
                    @Override
                    public void onEnd(CountdownView cv) {
                        callingTxt.setVisibility(View.GONE);
                        toast("时间到了");
                        setContentView(R.layout.activity_call_result);
                        initToolbar("点名结果");
                        Record record = new Record();
                        record.setCou_id(cou_id);
                        record.setCou_name(cou_name);
                        record.setStu_num("2012201232");
                        record.setStu_name("张宇");
                        record.setStatus(0);
                        record.setFileUrl("http://file.bmob.cn/M03/EA/07/oYYBAFbs4JKAOuBQABM8lrSVYBI057.jpg");
                        record.setThumbUrl("http://file.bmob.cn/M03/EA/07/oYYBAFbs4JSAQGeFAAD1-D-qicQ908.jpg");
                        records.add(record);
                        SlideAndDragListView recordLV = (SlideAndDragListView) findViewById(R.id.sdlv_result);
                        new BuildResult(recordLV, CallActivity.this).execute();
                    }
                });
            }
        });

    }

    /**
     * 聊天消息接收事件
     *
     * @param event
     */
    public void onEventMainThread(MessageEvent event) {
        BmobIMMessage msg = event.getMessage();
        Log.d("msg2", msg.toString());
        String content = msg.getContent();
        if (content.equals(code)) {//验证码正确
            Record record = new Record();

            String extra = msg.getExtra();

            JSONObject object = JSONObject.fromObject(extra);
            String username = object.getString("username");
            String name = object.getString("name");
            String fileName = object.getString("filename");
//            String fileName = "2fda2eff6efe4db6aaf47caf365583ae.jpg";
            String fileUrl = object.getString("fileUrl");
            String thumbName = object.getString("thumbName");
            String thumbUrl = object.getString("thumbUrl");
//            String thumbUrl = object.getString("thumbUrl");

            record.setCou_id(cou_id);
            record.setCou_name(cou_name);
            record.setStu_id(msg.getFromId());
            record.setStu_num(username);
            record.setStu_name(name);
            record.setStatus(0);
            record.setFilename(fileName);
            record.setFileUrl(fileUrl);
            record.setThumbName(thumbName);
            record.setThumbUrl(thumbUrl);

            records.add(record);

        } else {
            sendMessage(msg.getFromId(), "验证码错误！");
        }


    }

    private void sendMessage(String fromId, final String content) {
        BmobIMUserInfo info = new BmobIMUserInfo();
        info.setUserId(fromId);
        info.setName(UserManager.getInstance(getApplicationContext()).getuserInfo().getName());
        BmobIM.getInstance().startPrivateConversation(info, new ConversationListener() {
            @Override
            public void done(BmobIMConversation c, BmobException e) {
                if (e == null) {
                    BmobIMConversation im;
                    im = BmobIMConversation.obtain(BmobIMClient.getInstance(), c);
                    BmobIMTextMessage msg = new BmobIMTextMessage();
                    msg.setContent(content);
                    im.sendMessage(msg, new MessageSendListener() {
                        @Override
                        public void onStart(BmobIMMessage msg) {
                            super.onStart(msg);
                        }

                        @Override
                        public void done(BmobIMMessage msg, BmobException e) {
                            Log.d("call", "teacher send msg:" + msg.toString());
                        }
                    });
                } else {
                    toast(e.getMessage() + "(" + e.getErrorCode() + ")");
                }
            }
        });

    }

    @Override
    public void onBackPressed() {

    }

    private String getRandomCode() {
        char[] str = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
                'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
                'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        StringBuffer code = new StringBuffer("");
        final int maxNum = 36;
        int count = 0;
        int i;
        Random r = new Random();
        while (count < 4) {
            // 生成随机数，取绝对值，防止生成负数，

            i = Math.abs(r.nextInt(maxNum)); // 生成的数最大为36-1

            if (i >= 0 && i < str.length) {
                code.append(str[i]);
                count++;
            }
        }
        return code.toString();
    }

    /**
     * 异步处理 点名结果
     */
    class BuildResult extends AsyncTask<Void, Integer, CallResultAdapter> {

        private SlideAndDragListView recordLV;
        private Context context;
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd.setTitle("点名结果");
            pd.setMessage("正在处理点名结果，请稍候...");
            pd.setCancelable(false);
            pd.show();

        }

        public BuildResult(SlideAndDragListView recordLV, Context context) {
            this.recordLV = recordLV;
            this.context = context;
            pd = new ProgressDialog(context);
        }

        @Override
        protected CallResultAdapter doInBackground(Void... params) {
            CallResultAdapter adapter = new CallResultAdapter(context, records, cou_id);
            return adapter;
        }

        @Override
        protected void onPostExecute(final CallResultAdapter callResultAdapter) {
            final Menu menu = new Menu(new ColorDrawable(Color.WHITE), false, 0);//第2个参数表示滑动item是否能滑的过量(true表示过量，就像Gif中显示的那样；false表示不过量，就像QQ中的那样)


            menu.addItem(new MenuItem.Builder().setWidth(150)//单个菜单button的宽度
                    .setBackground(new ColorDrawable(getResources().getColor(R.color.menuItemDelete)))//设置菜单的背景
                    .setDirection(MenuItem.DIRECTION_RIGHT)
                    .setText("备注")//set text string
                    .setTextColor(Color.WHITE)//set text color
                    .setTextSize(16)//set text size
                    .build());
            menu.addItem(new MenuItem.Builder().setWidth(150)//单个菜单button的宽度
                    .setBackground(new ColorDrawable(getResources().getColor(R.color.ToolBarColor)))//设置菜单的背景
                    .setDirection(MenuItem.DIRECTION_RIGHT)
                    .setText("加分")
                    .setTextColor(Color.WHITE)//set text color
                    .setTextSize(16)//set text size
                    .build());
            menu.addItem(new MenuItem.Builder().setWidth(150)
                    .setBackground(new ColorDrawable(getResources().getColor(R.color.menuTxtColor)))
                    .setText("修改")
                    .setDirection(MenuItem.DIRECTION_RIGHT)
                    .setTextColor(Color.WHITE)
                    .setTextSize(16)
                    .build());
            recordLV.setMenu(menu);
            recordLV.setAdapter(callResultAdapter);
            recordLV.setOnMenuItemClickListener(new SlideAndDragListView.OnMenuItemClickListener() {
                @Override
                public int onMenuItemClick(View v, int itemPosition, int buttonPosition, int direction) {
                    switch (buttonPosition) {
                        case 0:
                            showmDialog(0, itemPosition, callResultAdapter);
                            break;
                        case 1:
                            showmDialog(1, itemPosition, callResultAdapter);
                            break;
                        case 2:
                            showmDialog(2, itemPosition, callResultAdapter);

                        default:
                            return Menu.ITEM_NOTHING;

                    }
                    return Menu.ITEM_SCROLL_BACK;
                }
            });
            pd.dismiss();
        }

    }

    private void showmDialog(int i, final int itemPosition, final CallResultAdapter callResultAdapter) {
        AlertDialog.Builder mDialog = new AlertDialog.Builder(this);
        View view;
        switch (i) {
            case 0://备注
                view = LayoutInflater.from(this).inflate(R.layout.dialog_record_remark, null);
                final MaterialEditText remarkEdt = (MaterialEditText) view.findViewById(R.id.edt_remark);
                remarkEdt.setText(records.get(itemPosition).getRemark());
                mDialog.setTitle("添加备注")
                        .setPositiveButton("保存", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String remark = remarkEdt.getText().toString();
                                records.get(itemPosition).setRemark(remark);
                            }
                        })
                        .setView(view)
                        .show();
                break;
            case 1://加分
                view = LayoutInflater.from(this).inflate(R.layout.dialog_record_praise, null);
                final CheckBox cb1 = (CheckBox) view.findViewById(R.id.checkBox_1);//10
                final CheckBox cb2 = (CheckBox) view.findViewById(R.id.checkBox_2);//20
                final CheckBox cb3 = (CheckBox) view.findViewById(R.id.checkBox_3);//10
                final CheckBox cb4 = (CheckBox) view.findViewById(R.id.checkBox_4);//30
                mDialog.setTitle("加分")
                        .setPositiveButton("保存", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (cb1.isChecked())
                                    records.get(itemPosition).setPraise(records.get(itemPosition).getPraise() + 10);
                                if (cb2.isChecked())
                                    records.get(itemPosition).setPraise(records.get(itemPosition).getPraise() + 20);
                                if (cb3.isChecked())
                                    records.get(itemPosition).setPraise(records.get(itemPosition).getPraise() + 10);
                                if (cb4.isChecked())
                                    records.get(itemPosition).setPraise(records.get(itemPosition).getPraise() + 30);
                            }
                        })
                        .setView(view)
                        .show();
                break;
            case 2://修改
                view = LayoutInflater.from(this).inflate(R.layout.dialog_record_alert, null);
                final RadioGroup rgroup = (RadioGroup) view.findViewById(R.id.radgroup);
                switch (records.get(itemPosition).getStatus()) {
                    case 0:
                        rgroup.check(R.id.radioButton1);
                        break;
                    case 1:
                        rgroup.check(R.id.radioButton2);
                        break;
                    case 2:
                        rgroup.check(R.id.radioButton3);
                        break;
                }

                mDialog.setTitle("修改")
                        .setPositiveButton("保存", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int id = rgroup.getCheckedRadioButtonId();
                                switch (id) {
                                    case R.id.radioButton1://到课

                                        records.get(itemPosition).setStatus(0);

                                        break;
                                    case R.id.radioButton2://缺席
                                        records.get(itemPosition).setStatus(1);
                                        break;
                                    case R.id.radioButton3:
                                        records.get(itemPosition).setStatus(2);
                                        break;
                                }
                                callResultAdapter.notifyDataSetChanged();
                                Log.d("dialog",records.get(itemPosition).toString());
                            }
                        })
                        .setView(view)
                        .show();
                break;
        }

    }

}
