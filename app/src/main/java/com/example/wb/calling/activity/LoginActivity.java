package com.example.wb.calling.activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import com.example.wb.calling.R;
import com.example.wb.calling.manager.UserManager;
import com.example.wb.calling.utils.RegexUtil;
import com.gc.materialdesign.views.ButtonRectangle;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rengwuxian.materialedittext.validation.METValidator;

import static com.example.wb.calling.R.id.btn_register;

/**
 * Created by wb on 16/1/23.
 */
public class LoginActivity extends AppCompatActivity {
    private Button regBtn;
    private ButtonRectangle loginBtn;
    private String username;
    private String password;
    private boolean isFirst = true;
    private SharedPreferences sp;
    private UserManager userManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        setContentView(R.layout.activity_login);
        userManager = UserManager.getInstance(this);
        initView();
    }

    private void initView() {
        regBtn = (Button) findViewById(btn_register);
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });

        sp = getSharedPreferences("userinfo",MODE_PRIVATE);
        username = sp.getString("username","no");
        password = sp.getString("password","no");
        Log.d("username",username);
        Log.d("pw",password);
        if( !username.equals("no") && !password.equals("no")){
            userManager.login(username,password);
        }else {
            final MaterialEditText usernameEdt = (MaterialEditText) findViewById(R.id.edt_account);
            usernameEdt.addValidator(new METValidator("用户名格式不正确") {
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
            pwEdt.addValidator(new METValidator("请填写密码") {
                @Override
                public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
                   if(text != null && text.length() > 0){
                       password = text.toString();
                       return true;
                   }else {
                       return false;
                   }
                }
            });

            loginBtn = (ButtonRectangle) findViewById(R.id.btn_login);

            loginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    userManager.login(username,
                            password);
                }
            });
        }

    }
}
