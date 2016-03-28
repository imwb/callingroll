package com.example.wb.calling.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.wb.calling.R;
import com.example.wb.calling.entry.User;
import com.example.wb.calling.manager.CourseManager;
import com.example.wb.calling.utils.RegexUtil;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rengwuxian.materialedittext.validation.METValidator;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

import static com.example.wb.calling.R.id.btn_register;

/**
 * Created by wb on 16/1/23.
 */
public class LoginActivity extends AppCompatActivity {

    private Button regBtn;
    private Button loginBtn;
    private String username;
    private String password;
    private boolean isFirst = true;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {

        regBtn = (Button) findViewById(btn_register);
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        sp = getSharedPreferences("userinfo", MODE_PRIVATE);
        username = sp.getString("username", "");
        password = sp.getString("password", "");
        isFirst = sp.getBoolean("isFirst", true);
        Log.d("username", username);
        Log.d("pw", password);

        final MaterialEditText usernameEdt = (MaterialEditText) findViewById(R.id.edt_account);
        final MaterialEditText pwEdt = (MaterialEditText) findViewById(R.id.edt_password);

        if (!username.isEmpty() && !password.isEmpty()) {
            usernameEdt.setText(username);
            pwEdt.setText(password);
            login(username, password);
        } else {
            usernameEdt.addValidator(new METValidator("用户名格式不正确") {
                @Override
                public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
                    if (RegexUtil.isIDCode(text.toString())) {
                        username = text.toString();
                        return true;
                    } else {
                        return false;
                    }
                }
            });

            pwEdt.addValidator(new METValidator("请填写密码") {
                @Override
                public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
                    if (text != null && text.length() > 0) {
                        password = text.toString();
                        return true;
                    } else {
                        return false;
                    }
                }
            });

            loginBtn = (Button) findViewById(R.id.btn_login);

            loginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (usernameEdt.validate() && pwEdt.validate()) {
                        login(username, password);
                    }
                }
            });
        }

    }

    /**
     * 登录
     *
     * @param username
     * @param pw
     */
    public void login(final String username, final String pw) {
        final User user = new User();
        user.setUsername(username);
        user.setPassword(pw);

        user.login(this, new SaveListener() {
            @Override
            public void onSuccess() {
                User user = BmobUser.getCurrentUser(LoginActivity.this, User.class);
                password = pw;
                if (user.getType() == 0) {//教师
                    if (isFirst == true) {
                        //引导页。。

                        //加载数据 启动一个 Service
                        CourseManager.getInstance(getApplicationContext()).loadCourseByUserID(user.getObjectId());

                    }
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                } else {//学生
                    if (isFirst == true) {
                        //引导页。。
                    }
                    startActivity(new Intent(LoginActivity.this, StuMainActivity.class));
                }
                //保存用户信息
                saveUserInfo(user);
                BmobIM.connect(user.getObjectId(), new ConnectListener() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
                            Log.d("im","connect success");
                        } else {
                            Log.d("im",e.getErrorCode() + "/" + e.getMessage());
                        }
                    }
                });
                finish();
            }

            @Override
            public void onFailure(int i, String s) {
                switch (i) {
                    case 101:
                        toast("帐号或密码错误");
                        break;
                }
                toast("登录失败 : " + i + "  " + s);
            }
        });
    }

    /**
     * 将用户信息保存到本地
     *
     * @param user
     */
    private void saveUserInfo(User user) {
        SharedPreferences sp = LoginActivity.this.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("username", user.getUsername());
        editor.putString("objectId",user.getObjectId());
        editor.putString("password", password);
        editor.putString("name", user.getName());
        editor.putString("email", user.getEmail());
        editor.putInt("type", user.getType());
        editor.putBoolean("isFirst", false);
        editor.commit();
    }

    private void toast(String content) {
        Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
    }
}
