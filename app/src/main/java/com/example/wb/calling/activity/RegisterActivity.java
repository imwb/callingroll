package com.example.wb.calling.activity;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;

import com.example.wb.calling.R;
import com.example.wb.calling.entry.User;
import com.example.wb.calling.manager.UserManager;
import com.example.wb.calling.utils.RegexUtil;
import com.gc.materialdesign.views.ButtonRectangle;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rengwuxian.materialedittext.validation.METValidator;

import java.util.regex.Pattern;

import static com.example.wb.calling.R.id.btn_register;

public class RegisterActivity extends BaseActivity {

    private Toolbar toolbar;
    private String username;
    private String password;
    private String name;
    private String email;
    private Integer type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initview();
    }

    private void initview() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("注册");
        setSupportActionBar(toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.StatusBarColor));
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radgrup_type);
        int id = radioGroup.getCheckedRadioButtonId();
        if(id == R.id.radio_stuent){
            type = 1;
        }else {
            type = 0;
        }
        final MaterialEditText usernameEdt = (MaterialEditText) findViewById(R.id.edt_username);
        usernameEdt.addValidator(new METValidator("请输入正确的用户名") {
            @Override
            public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
                if(RegexUtil.isIDCode(text.toString())){
                    username = text.toString();
                    return true;
                }else {
                    return false;
                }
            }
        });

        final MaterialEditText pwEdt = (MaterialEditText) findViewById(R.id.edt_password);
        pwEdt.addValidator(new METValidator("密码要大于六位") {
            @Override
            public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
                if((text != null)&& (text.length()>=6)){
                    password = text.toString();
                    Log.d("pw",password);
                    return true;
                }else {
                    return false;
                }
            }
        });


        final MaterialEditText nameEdt = (MaterialEditText) findViewById(R.id.edt_name);
        nameEdt.addValidator(new METValidator("要真实姓名哦") {
            @Override
            public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
                Pattern pattern = Pattern.compile("^[\u4e00-\u9fa5]+$");
                if(pattern.matcher(text).matches()){
                    name = text.toString();
                    Log.d("name",name);
                    return true;
                }else {
                    return false;
                }
            }
        });

        final MaterialEditText emailEdt = (MaterialEditText) findViewById(R.id.edt_email);
        emailEdt.addValidator(new METValidator("邮箱地址不正确") {
            @Override
            public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {

                if(RegexUtil.isEmail(text.toString())){
                    email = text.toString();
                    Log.d("email",email);
                    return true;
                }else {
                    return false;
                }
            }
        });

        ButtonRectangle registerBtn = (ButtonRectangle) findViewById(btn_register);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(usernameEdt.validate() && pwEdt.validate() && nameEdt.validate()
                        && emailEdt.validate()){
                    User user = new User();
                    user.setName(name);
                    user.setUsername(username);
                    user.setPassword(password);
                    user.setType(type);
                    user.setEmail(email);
                    UserManager.getInstance(RegisterActivity.this).registerUser(user);
                }else {
                    toast("请核对注册信息");
                }
            }
        });
    }

}
