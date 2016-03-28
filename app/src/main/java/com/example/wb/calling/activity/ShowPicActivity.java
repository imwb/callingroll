package com.example.wb.calling.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.wb.calling.R;

import org.xutils.common.Callback;
import org.xutils.image.ImageOptions;
import org.xutils.x;

public class ShowPicActivity extends AppCompatActivity {

    private String url;
    private ImageView img;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_pic);
        Intent intent = getIntent();
        if(intent != null){
            url = intent.getStringExtra("url");
            img = (ImageView) findViewById(R.id.img);

            loadFile(url);
        }
    }

    private void loadFile(String url) {
        ImageOptions options = new ImageOptions.Builder()
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .setAutoRotate(true)
                .setFailureDrawableId(R.drawable.ic_mood_bad_black_24dp)
                .build();
        pd = new ProgressDialog(this);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setTitle("正在下载...");
        pd.setMessage("请稍候...");
        pd.setMax(100);
        pd.setCancelable(false);
        x.image().bind(img, url, options, new Callback.ProgressCallback<Drawable>() {
            @Override
            public void onWaiting() {
                pd.show();
            }

            @Override
            public void onStarted() {

            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                pd.setProgress((int) current);

            }

            @Override
            public void onSuccess(Drawable result) {
                pd.dismiss();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                pd.dismiss();
                toast("下载失败");

            }

            @Override
            public void onCancelled(CancelledException cex) {
                toast("取消下载");
            }

            @Override
            public void onFinished() {
                pd.dismiss();
            }
        });
    }

    private void toast(String msg) {
        Toast.makeText(this, msg,Toast.LENGTH_SHORT).show();
    }

}
