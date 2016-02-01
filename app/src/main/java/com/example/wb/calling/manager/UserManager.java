package com.example.wb.calling.manager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.wb.calling.activity.MainActivity;
import com.example.wb.calling.entry.User;

import cn.bmob.v3.listener.SaveListener;

/**
 * Created by wb on 16/1/29.
 */
public class UserManager {

    private Context context;
    private static UserManager manager;
    private UserManager(Context context){
        this.context = context;
    }

    public static UserManager getInstance(Context context){
        if(manager != null){
            manager.context = context;
            return manager;
        }else {
            return new UserManager(context);
        }
    }

    public  void registerUser(final User user){
        user.signUp(context, new SaveListener() {
            @Override
            public void onSuccess() {
//                Intent intent = new Intent(context, LoginActivity.class);
//                intent.putExtra("username",user.getUsername());
                Toast.makeText(context,"注册成功，请及时通过邮件验证",Toast.LENGTH_LONG)
                        .show();

            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(context,"注册失败 : "+ i + "  " +s,Toast.LENGTH_LONG)
                        .show();
            }
        });
    }

    public void login(final String username, final String pw){
        User user = new User();
        user.setUsername(username);
        user.setPassword(pw);

        user.login(context, new SaveListener() {
            @Override
            public void onSuccess() {
                SharedPreferences sp = context.getSharedPreferences("userinfo",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("username",username);
                editor.putString("password",pw);
                editor.commit();
                //引导页。。
               // editor.putBoolean("isFirst",false);
                context.startActivity(new Intent(context, MainActivity.class));

            }

            @Override
            public void onFailure(int i, String s) {
                switch (i){
                    case 101:toast("帐号或密码错误");
                        break;
                }
                Toast.makeText(context,"登录失败 : "+ i + "  " +s,Toast.LENGTH_LONG)
                        .show();
            }
        });
    }
    protected  void toast(String content){
        Toast.makeText(context,content,Toast.LENGTH_SHORT).show();
    }

}
