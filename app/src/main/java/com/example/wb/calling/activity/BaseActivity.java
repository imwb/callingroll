package com.example.wb.calling.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wb.calling.R;

/**
 * Created by wb on 16/2/1.
 */
public class BaseActivity extends AppCompatActivity {
    protected DrawerLayout mDrawerLayout;
    protected Toolbar mToolbar;
    protected ActionBarDrawerToggle drawerToggle;

    protected LinearLayout menuCallLayout;
    protected ImageButton menuCallImg;
    protected TextView menuCallTxt;

    protected LinearLayout menuCourseLayout;
    protected ImageButton menuCourseImg;
    protected TextView menuCourseTxt;

    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
    }

    public void toast(String info) {
        Toast.makeText(this, info, Toast.LENGTH_LONG).show();
    }

    /**
     * 初始化toolbar 和 drawer
     *
     * @param title
     */

    protected void initToolbarAndDrawer(String title) {
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

    protected void initToolbar(String title){
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

        switch (type) {
            case 1: {
                changeIconSelected(menuCallImg, R.drawable.ic_touch_app_black_24dp, menuCallTxt);
                break;
            }
            case 2: {
                changeIconSelected(menuCourseImg, R.drawable.ic_import_contacts_black_24dp, menuCourseTxt);
                break;
            }
        }

        menuCallLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeIconNor(menuCourseImg, R.drawable.ic_import_contacts_black_24dp_nor, menuCourseTxt);
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
                changeIconSelected(menuCourseImg, R.drawable.ic_import_contacts_black_24dp, menuCourseTxt);

                Intent intent = new Intent(BaseActivity.this, MyCourseActivity.class);
                startActivity(intent);
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

    /**
     * 退出程序
     */
    @Override
    public void onBackPressed() {

        if ((System.currentTimeMillis() - exitTime) > 2000) {
            toast("再按一次退出程序");
            exitTime = System.currentTimeMillis();
        } else {
            this.finish();
            System.exit(0);
        }
    }
}
