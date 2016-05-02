package com.example.wb.calling.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.example.wb.calling.R;
import com.example.wb.calling.adapter.CallResultAdapter;
import com.example.wb.calling.entry.Record;
import com.example.wb.calling.entry.RecordItem;
import com.example.wb.calling.manager.RecordManager;
import com.example.wb.calling.manager.UserManager;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.shamanland.fab.ShowHideOnScroll;
import com.yydcdut.sdlv.Menu;
import com.yydcdut.sdlv.MenuItem;
import com.yydcdut.sdlv.SlideAndDragListView;

import net.sf.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.iwgang.countdownview.CountdownView;
import de.greenrobot.event.EventBus;

public class CallActivity extends BaseActivity {

    private long exitTime = 0;
    private String code;
    private Button callBtn;
    private TextView codeTxt;
    private CountdownView timeView;
    private TextView callingTxt;
    private SlideAndDragListView recordLV;
    private CallResultAdapter adapter;
    private ArrayList<RecordItem> records = new ArrayList<>();
    private String cou_id = " ";
    private String cou_name = " ";
    private String cou_class = " ";
    private int result_total = 0;
    private int result_statu0 = 0;
    private int result_statu1 = 0;
    private int result_statu2 = 0;
    public static double mlatitude;
    public static double mlongitute;

    private LocationManager mLocationManger;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    public AMapLocationClientOption mLocationOption = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            Thread.currentThread().getName();
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    //定位成功回调信息，设置相关消息
                    amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                    mlatitude = amapLocation.getLatitude();//获取纬度
                    mlongitute = amapLocation.getLongitude();//获取经度
                    Log.d("location", amapLocation.toString());
                } else {
                    //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //保持屏幕不进入休眠
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_call);
        Intent intent = getIntent();
        if (intent != null) {
            cou_id = intent.getStringExtra("cou_id");
            cou_name = intent.getStringExtra("cou_name");
            cou_class = intent.getStringExtra("cou_class");
        }
        initLocatoin();
        initToolbar("点名" + cou_name);
        initView();
    }


    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu_callactivity, menu);
        final android.view.MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        ;
        searchView.setQueryHint("搜索学号...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                int index = -1;
                for (int i = 0; i < records.size(); i++) {
                    if (records.get(i).getStu_num().contains(newText)) {
                        index = i;
                        break;
                    }
                }
                if (recordLV != null) {
                    if (index != -1)
                        recordLV.setSelection(index);
                    else
                        toast("无符合条件记录！");

                }
                return true;
            }
        });

        final android.view.MenuItem item2 = menu.findItem(R.id.action_search);
        item2.setOnMenuItemClickListener(new android.view.MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(android.view.MenuItem item) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void initLocatoin() {
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(false);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(60000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
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
                timeView.start(60000);
                EventBus.getDefault().register(CallActivity.this);
                timeView.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
                    @Override
                    public void onEnd(CountdownView cv) {
                        EventBus.getDefault().unregister(CallActivity.this);
                        callingTxt.setVisibility(View.GONE);
                        toast("时间到了");
                        setContentView(R.layout.activity_call_result);
                        initToolbar("点名结果");
//                        RecordItem record = new RecordItem();
//                        record.setStu_num("2012201232");
//                        record.setStu_name("张宇");
//                        record.setStatus(0);
//                        record.setFileUrl("http://file.bmob.cn/M03/EA/07/oYYBAFbs4JKAOuBQABM8lrSVYBI057.jpg");
//                        record.setThumbUrl("http://file.bmob.cn/M03/EA/07/oYYBAFbs4JSAQGeFAAD1-D-qicQ908.jpg");
//                        record.setLatitude(45.776007);
//                        record.setLongitude(126.683592);
//                        record.setPraise(0);
//                        record.setCourseName(cou_name);
//                        records.add(record);
                        recordLV = (SlideAndDragListView) findViewById(R.id.sdlv_result);
                        recordLV.setOnTouchListener(new ShowHideOnScroll(mToolbar));

                        new BuildResult(recordLV, CallActivity.this).execute();
                        mToolbar.setNavigationIcon(R.drawable.ic_save_black_24dp);
                        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showmDialog();
                            }
                        });
                    }
                });
            }
        });

    }


    @Override
    protected void onResume() {

        super.onResume();

        Log.d("activity", "onresume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("activity", "onpause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mLocationClient != null) {
            mLocationClient.stopLocation();//停止定位
            mLocationClient.onDestroy();//销毁定位客户端。
        }

    }

    /**
     * 聊天消息接收事件
     *
     * @param event
     */
    public void onEventMainThread(MessageEvent event) {
        BmobIMMessage msg = event.getMessage();
        BmobIMTextMessage returnmsg = new BmobIMTextMessage();
        Map<String,Object> map = new HashMap<>();
        map.put("type","callresult");

        Log.d("msg2", msg.toString());
        String content = msg.getContent();
        if (content.equals(code)) {//验证码正确
            RecordItem record = new RecordItem();

            String extra = msg.getExtra();

            JSONObject object = JSONObject.fromObject(extra);
            String username = object.getString("username");
            String name = object.getString("name");
//            String fileName = object.getString("filename");
////            String fileName = "2fda2eff6efe4db6aaf47caf365583ae.jpg";
//            String fileUrl = object.getString("fileUrl");
            String thumbName = object.getString("thumbName");
            String thumbUrl = object.getString("thumbUrl");
            Double longitude = object.getDouble("longitude");
            Double latitude = object.getDouble("latitude");
            //latitude=45.776007 longitude=126.683592
//            String thumbUrl = object.getString("thumbUrl");
            record.setStu_id(msg.getFromId());
            record.setStu_num(username);
            record.setStu_name(name);
            record.setStatus(0);
//            record.setFilename(fileName);
//            record.setFileUrl(fileUrl);
            record.setThumbName(thumbName);
            //http:\/\/newfile.codenow.cn:8080\/5926e42d26a0482e923769a77e8e7963.jpg
            record.setThumbUrl(thumbUrl);
            record.setPraise(0);
            record.setCourseName(cou_name);
            record.setLatitude(latitude);
            record.setLongitude(longitude);
            records.add(record);

            returnmsg.setContent("点名成功！");
            returnmsg.setExtraMap(map);
            event.getConversation().sendMessage(returnmsg);
        } else {
            returnmsg.setContent("验证码错误！");
            returnmsg.setExtraMap(map);
            event.getConversation().sendMessage(returnmsg);
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
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            toast("亲，点名还没有完成！");
            exitTime = System.currentTimeMillis();
        } else {
            this.finish();
        }

    }

    private String getRandomCode() {
        char[] str = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
                'x', 'y', 'z', '0', '2', '3', '4', '5', '6', '7', '8', '9'};
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
            adapter = new CallResultAdapter(context, records, cou_id);
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

    /**
     * 保存 点名结果
     */
    private void showmDialog() {
        AlertDialog.Builder mDialog = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_show_call_result, null);
        TextView nameTxt = (TextView) view.findViewById(R.id.txt_name);
        TextView classTxt = (TextView) view.findViewById(R.id.txt_class);
        TextView totalTxt = (TextView) view.findViewById(R.id.txt_total);
        TextView sta0Txt = (TextView) view.findViewById(R.id.txt_status0);
        TextView sta1Txt = (TextView) view.findViewById(R.id.txt_status1);
        TextView sta2Txt = (TextView) view.findViewById(R.id.txt_status2);
        TextView timeTxt = (TextView) view.findViewById(R.id.txt_time);
        countResult();
        nameTxt.setText(cou_name);
        classTxt.setText(cou_class);
        totalTxt.setText(result_total + " 人");
        sta0Txt.setText(result_statu0 + " 人");
        sta1Txt.setText(result_statu1 + " 人");
        sta2Txt.setText(result_statu2 + " 人");

        SimpleDateFormat format = new SimpleDateFormat("MM月dd日 hh:mm");
        final String fdate = format.format(new Date());
        timeTxt.setText(fdate);

        mDialog.setTitle("点名结果")
                .setPositiveButton("保存", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (saveRecord(fdate)) {
                            toast("保存成功！");
                            finish();
                        } else {
                            toast("保存失败！");
                        }
                    }
                });
        mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                result_statu0 = 0;
                result_statu1 = 0;
                result_statu2 = 0;
            }
        });
        mDialog.setView(view);
        mDialog.show();
    }

    /**
     * 保存结果
     */
    private boolean saveRecord(String time) {
        Record record = new Record();
        record.setCourseID(cou_id);
        record.setCourseName(cou_name);
        record.setDate(new BmobDate(new Date()));
        record.setTime(time);
        record.setTotal(result_total);
        record.setCstatu0(result_statu0);
        record.setCstatu1(result_statu1);
        record.setCstatu2(result_statu2);
        record.setCourseClass(cou_class);
        record.setTeacherName(UserManager.getInstance(getApplicationContext()).getuserInfo().getUsername());
        return RecordManager.getInstance(getApplicationContext()).saveRecords(record, records);

    }

    /**
     * 计算 点名结果
     */
    private void countResult() {
        result_total = records.size();
        for (int i = 0; i < records.size(); i++) {
            switch (records.get(i).getStatus()) {
                case 0:
                    result_statu0++;
                    break;
                case 1:
                    result_statu1++;
                    break;
                case 2:
                    result_statu2++;
                    break;
            }
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
                                if(records.get(itemPosition).getPraise() == null)
                                      records.get(itemPosition).setPraise(0);
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
                                Log.d("dialog", records.get(itemPosition).toString());
                            }
                        })
                        .setView(view)
                        .show();
                break;
        }

    }

}
