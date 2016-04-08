package com.example.wb.calling.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wb.calling.R;
import com.example.wb.calling.entry.Course;
import com.example.wb.calling.entry.Record;
import com.example.wb.calling.entry.User;
import com.example.wb.calling.manager.CourseManager;
import com.example.wb.calling.manager.UserManager;

import org.xutils.x;

import cn.bmob.newim.BmobIM;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.CountListener;

/**
 * Created by wb on 16/2/1.
 */
public class BaseActivity extends AppCompatActivity {

    public static final int RESULT_ADD_COURSE = 1;
    public static final int RESULT_ROLL = 2;
    public static final int RESULT_UPDATE_COURSE = 3;

    public static final int REQUEST_ADD_ROLL = 4;
    public static final int REQUEST_UPDATE_COURSE = 5;
    public static final int REQUEST_ADD_COURSE = 6;

    protected DrawerLayout mDrawerLayout;
    protected Toolbar mToolbar;
    protected ActionBarDrawerToggle drawerToggle;

    protected LinearLayout menuCallLayout;
    protected ImageButton menuCallImg;
    protected TextView menuCallTxt;

    protected LinearLayout menuCourseLayout;
    protected ImageButton menuCourseImg;
    protected TextView menuCourseTxt;

    protected LinearLayout menuRecordLayout;
    protected ImageButton menuRecordImg;
    protected TextView menuRecordTxt;

    protected LinearLayout menuMsgLayout;
    protected ImageButton menuMsgImg;
    protected TextView menuMsgTxt;

    protected LinearLayout menuLogoutLayout;
    protected ImageButton menuLogoutImg;
    protected TextView menuLogoutTxt;

    protected RelativeLayout expandLayout;
    protected Button expandBtn;

    protected LinearLayout logoutLayout;
    protected LinearLayout menuLayout;

    protected TextView usernameTxt;
    protected TextView courseCountTxt;
    protected TextView callCountTxt;

    private static String username;
    private static String courseCount;
    private static String callCount;

    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        x.view().inject(this);
    }

    private void initMenuTitle() {
        usernameTxt = (TextView) findViewById(R.id.txt_user_name);
        courseCountTxt = (TextView) findViewById(R.id.txt_course_count);
        callCountTxt = (TextView) findViewById(R.id.txt_call_count);

        User user = BmobUser.getCurrentUser(this, User.class);
        usernameTxt.setText(user.getName());


        BmobQuery<Course> query = new BmobQuery<>();
        query.addWhereEqualTo("userID", user.getObjectId());
        query.count(this, Course.class, new CountListener() {
            @Override
            public void onSuccess(int i) {
                courseCount = i + "";
                courseCountTxt.setText(courseCount);
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });


        BmobQuery<Record> query2 = new BmobQuery<Record>();
        query2.addWhereEqualTo("teacherName", UserManager.getInstance(getApplicationContext()).getuserInfo().getUsername());
        query2.count(this, Record.class, new CountListener() {
            @Override
            public void onSuccess(int count) {
                // TODO Auto-generated method stub
                callCountTxt.setText(count + " ");
            }

            @Override
            public void onFailure(int code, String msg) {
                // TODO Auto-generated method stub
            }
        });
    }

    public void toast(String info) {
        Toast.makeText(this, info, Toast.LENGTH_SHORT).show();
    }

    /**
     * 初始化toolbar 和 drawer
     *
     * @param title
     */

    protected void initToolbarAndDrawer(String title) {
        initMenuTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(title);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.StatusBarColor));
        }

        drawerToggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar,
                R.string.abc_action_bar_home_description, R.string.abc_action_bar_home_description_format
        );

        drawerToggle.syncState();
        mDrawerLayout.setDrawerListener(drawerToggle);
    }

    protected void initToolbar(String title) {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(title);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        mToolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );
    }


    /**
     * 初始化菜单
     *
     * @param type
     */
    protected void initMenu(int type) {
        menuCallLayout = (LinearLayout) findViewById(R.id.layout_menu_call);
        menuCallImg = (ImageButton) findViewById(R.id.img_menu_call);
        menuCallTxt = (TextView) findViewById(R.id.txt_menu_call);

        menuCourseLayout = (LinearLayout) findViewById(R.id.layout_menu_course);
        menuCourseImg = (ImageButton) findViewById(R.id.img_menu_course);
        menuCourseTxt = (TextView) findViewById(R.id.txt_menu_course);

        menuRecordLayout = (LinearLayout) findViewById(R.id.layout_menu_record);
        menuRecordImg = (ImageButton) findViewById(R.id.img_menu_record);
        menuRecordTxt = (TextView) findViewById(R.id.txt_menu_record);

        menuMsgLayout = (LinearLayout) findViewById(R.id.layout_menu_msg);
        menuMsgImg = (ImageButton) findViewById(R.id.img_menu_msg);
        menuMsgTxt = (TextView) findViewById(R.id.txt_menu_msg);

        switch (type) {
            case 1: {
                changeIconSelected(menuCallImg, R.drawable.ic_touch_app_black_24dp, menuCallTxt);
                break;
            }
            case 2: {
                changeIconSelected(menuCourseImg, R.drawable.ic_import_contacts_black_24dp, menuCourseTxt);
                break;
            }
            case 3: {
                changeIconSelected(menuRecordImg, R.drawable.ic_assignment_black_24dp, menuRecordTxt);
                break;
            }
            case 4: {
                changeIconSelected(menuMsgImg, R.drawable.ic_markunread_black_24dp, menuMsgTxt);
                break;
            }

        }

        menuCallLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeIconNor(menuCourseImg, R.drawable.ic_import_contacts_black_24dp_nor, menuCourseTxt);
                changeIconNor(menuRecordImg, R.drawable.ic_assignment_black_24dp_nor, menuRecordTxt);
                changeIconNor(menuMsgImg, R.drawable.ic_markunread_black_24dp_nor, menuMsgTxt);
                changeIconSelected(menuCallImg, R.drawable.ic_touch_app_black_24dp, menuCallTxt);

                Intent intent = new Intent(BaseActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        menuCourseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeIconNor(menuCallImg, R.drawable.ic_touch_app_black_24dp_nor, menuCallTxt);
                changeIconNor(menuRecordImg, R.drawable.ic_assignment_black_24dp_nor, menuRecordTxt);
                changeIconNor(menuMsgImg, R.drawable.ic_markunread_black_24dp_nor, menuMsgTxt);
                changeIconSelected(menuCourseImg, R.drawable.ic_import_contacts_black_24dp, menuCourseTxt);

                Intent intent = new Intent(BaseActivity.this, MyCourseActivity.class);
                startActivity(intent);
                finish();
            }
        });
        menuRecordLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeIconNor(menuCallImg, R.drawable.ic_touch_app_black_24dp_nor, menuCallTxt);
                changeIconNor(menuCourseImg, R.drawable.ic_import_contacts_black_24dp_nor, menuCourseTxt);
                changeIconNor(menuMsgImg, R.drawable.ic_markunread_black_24dp_nor, menuMsgTxt);
                changeIconSelected(menuRecordImg, R.drawable.ic_assignment_black_24dp, menuRecordTxt);
                Intent intent = new Intent(BaseActivity.this, RecordActivity.class);
                startActivity(intent);
                finish();
            }
        });

        menuMsgLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeIconNor(menuCallImg, R.drawable.ic_touch_app_black_24dp_nor, menuCallTxt);
                changeIconNor(menuCourseImg, R.drawable.ic_import_contacts_black_24dp_nor, menuCourseTxt);
                changeIconNor(menuRecordImg, R.drawable.ic_assignment_black_24dp_nor, menuRecordTxt);
                changeIconSelected(menuMsgImg, R.drawable.ic_markunread_black_24dp, menuMsgTxt);

                Intent intent = new Intent(BaseActivity.this, MsgActivity.class);
                startActivity(intent);
                finish();
            }
        });

        expandLayout = (RelativeLayout) findViewById(R.id.layout_expand);
        expandBtn = (Button) findViewById(R.id.btn_expand);
        logoutLayout = (LinearLayout) findViewById(R.id.layout_logout);
        menuLayout = (LinearLayout) findViewById(R.id.layout_menu);

        expandLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (logoutLayout.getVisibility() == View.GONE) {
                    expandBtn.setBackgroundResource(R.drawable.ic_expand_less_black_24dp);
                    menuLayout.setVisibility(View.GONE);
                    logoutLayout.setVisibility(View.VISIBLE);
                } else {
                    expandBtn.setBackgroundResource(R.drawable.ic_expand_more_black_12dp1);
                    logoutLayout.setVisibility(View.GONE);
                    menuLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        menuLogoutLayout = (LinearLayout) findViewById(R.id.layout_menu_logout);
        menuLogoutImg = (ImageButton) findViewById(R.id.img_menu_logout);
        menuLogoutTxt = (TextView) findViewById(R.id.txt_menu_logout);

        menuLogoutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuLogoutImg.setBackgroundResource(R.drawable.ic_power_settings_new_black_24dp);
                menuLogoutTxt.setTextColor(getResources().getColor(R.color.black));
                clearUserInfo();
                startActivity(new Intent(BaseActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    private void changeIconSelected(ImageButton img, int bg, TextView txt) {
        txt.setTextColor(getResources().getColor(R.color.black));
        img.setBackgroundResource(bg);
    }

    private void changeIconNor(ImageButton img, int bg, TextView txt) {
        txt.setTextColor(getResources().getColor(R.color.menuTxtColor));
        img.setBackgroundResource(bg);
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

    /**
     * 退出程序
     */
    @Override
    public void onBackPressed() {

        if ((System.currentTimeMillis() - exitTime) > 2000) {
            toast("再按一次退出程序");
            exitTime = System.currentTimeMillis();
        } else {
            //断开 IM 连接
            BmobIM.getInstance().disConnect();
            this.finish();
            System.exit(0);
        }
    }
}
