package com.example.wb.calling.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.hardware.Camera;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.bmob.BTPFileResponse;
import com.bmob.BmobProFile;
import com.bmob.btp.callback.LocalThumbnailListener;
import com.bmob.btp.callback.UploadBatchListener;
import com.bmob.btp.callback.UploadListener;
import com.example.wb.calling.R;
import com.example.wb.calling.manager.UserManager;
import com.example.wb.calling.utils.RegexUtil;
import com.example.wb.calling.view.CameraView;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rengwuxian.materialedittext.validation.METValidator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMTextMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.listener.ConversationListener;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;

public class StuCallActivity extends BaseActivity {

    private Button signBtn;
    private String code = "";
    private String userID = "";
    private Camera mCamera;
    private CameraView mPreview;
    private WindowManager wm;
    private String imgpath;
    private String mfilename;
    private String mfileUrl;
    private String mthumbUrl;
    private String mthumname;
    private String mthumpath;
    private Double latitude;
    private Double longitute;
    public ProgressDialog pd;
    private LocationManager mLocationManger;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            Thread.currentThread().getName();
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    //定位成功回调信息，设置相关消息
                    amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                    latitude = amapLocation.getLatitude();//获取纬度
                    longitute = amapLocation.getLongitude();//获取经度
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
    public AMapLocationClientOption mLocationOption = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stu_call);
        wm = getWindowManager();
        Intent intent = getIntent();
        if (intent != null) {
            userID = intent.getStringExtra("userID");
        }

        initLocatoin();
        //初始化定位

        initToolbar("签到");
        //  initview();
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
        mLocationOption.setInterval(2000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }


    private void initview() {
        final MaterialEditText codeEdt = (MaterialEditText) findViewById(R.id.edt_code);
        codeEdt.addValidator(new METValidator("请输入验证码") {
            @Override
            public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
                if (RegexUtil.isIDCode(text.toString()) && !isEmpty) {
                    code = text.toString();
                    return true;
                } else {
                    return false;
                }
            }
        });
        //初始化照相机
        mCamera = getCameraInstance();
        if (mCamera != null) {
            mCamera.setDisplayOrientation(90);
            // Create our Preview view and set it as the content of our activity.
            mPreview = new CameraView(this, mCamera);
            mPreview.getHolder().setFixedSize(320, 240); // 设置分辨率

            LinearLayout preview = (LinearLayout) findViewById(R.id.camera_preview);
            preview.removeAllViews();

            int w = (int) (preview.getLayoutParams().height / getScaleXY());
            int h = preview.getLayoutParams().height;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(w, h);
            preview.addView(mPreview, params);

            signBtn = (Button) findViewById(R.id.btn_sign);
            signBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (codeEdt.validate()) {

                        mCamera.takePicture(null, null, mPicture);

                    } else {
                    }
                }
            });
        } else {
            toast("相机初始化失败！");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initview();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mCamera != null)
            mCamera.release();
        if (mLocationClient != null) {
            mLocationClient.stopLocation();//停止定位
            mLocationClient.onDestroy();//销毁定位客户端。
        }
    }

    /**
     * 上传图片
     *
     * @param imgpath
     */
    private void uploadImg(String imgpath) {
        showdialog();
        BTPFileResponse response = BmobProFile.getInstance(this).upload(imgpath, new UploadListener() {


            @Override
            public void onSuccess(String fileName, String url, BmobFile file) {
                Log.i("bmob", "文件上传成功：" + fileName + ",可访问的文件地址：" + file.getUrl());
                // fileName ：文件名（带后缀），这个文件名是唯一的，开发者需要记录下该文件名，方便后续下载或者进行缩略图的处理
                // url        ：文件地址
                // file        :BmobFile文件类型，`V3.4.1版本`开始提供，用于兼容新旧文件服务。
                mfilename = fileName;
            }

            @Override
            public void onProgress(int progress) {
                // TODO Auto-generated method stub
                Log.i("bmob", "onProgress :" + progress);
                pd.setProgress(progress);

            }

            @Override
            public void onError(int statuscode, String errormsg) {
                // TODO Auto-generated method stub
                Log.i("bmob", "文件上传失败：" + errormsg);
                toast("上传失败：" + errormsg);
                pd.dismiss();
            }
        });

    }

    /**
     * 得到本地缩略图
     *
     * @param path
     */
    private void getLocalThumbnail(String path) {

        int modeId = 1;
        int width = 140;
        int height = 200;
        BmobProFile.getInstance(StuCallActivity.this).getLocalThumbnail(path, modeId, width, height, new LocalThumbnailListener() {

            @Override
            public void onError(int statuscode, String errormsg) {
                // TODO Auto-generated method stub
                Log.d("thumb", "本地缩略图创建失败 :" + statuscode + "," + errormsg);
                toast("上传失败：" + errormsg);
            }

            @Override
            public void onSuccess(String thumbnailPath) {
                // TODO Auto-generated method stub
                mthumpath = thumbnailPath;
                uploadBatchFile();
            }
        });
    }

    /**
     * @param
     * @return void
     * @throws
     * @Title: updateBatchFile
     * @Description: 文件批量上传
     */
    private void uploadBatchFile() {
        showdialog();
        String[] files = new String[]{imgpath, mthumpath};
        BmobProFile.getInstance(StuCallActivity.this).uploadBatch(files, new UploadBatchListener() {

            @Override
            public void onSuccess(boolean isFinish, String[] fileNames, String[] urls, BmobFile[] files) {
                // TODO Auto-generated method stub
                if (isFinish) {

                    mfilename = files[0].getFilename();
                    mfileUrl = files[0].getUrl();
                    mthumname = files[1].getFilename();
                    mthumbUrl = files[1].getUrl();
                    Log.i("bmob", "文件上传成功：" + mfilename + ",可访问的文件地址：" + mfileUrl);
                    Log.i("bmob", "缩略图传成功：" + mthumname + ",可访问的文件地址：" + mthumbUrl);
                    sendMessage(mfilename, mfileUrl, mthumname, mthumbUrl);
                    pd.dismiss();
                }
                Log.d("upload", "NewBmobFileActivity -onSuccess :" + isFinish + "-----" + Arrays.asList(fileNames) + "----" + Arrays.asList(urls) + "----" + Arrays.asList(files));
            }

            @Override
            public void onProgress(int curIndex, int curPercent, int total, int totalPercent) {
                // TODO Auto-generated method stub
                pd.setProgress(curPercent);
            }

            @Override
            public void onError(int statuscode, String errormsg) {
                // TODO Auto-generated method stub
                Log.d("upload", "NewBmobFileActivity -onError :" + statuscode + "--" + errormsg);
                pd.dismiss();
                toast("上传出错：" + errormsg);
            }
        });
    }


    private void showdialog() {
        pd = new ProgressDialog(this);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setTitle("正在上传");
        pd.setProgress(0);
        pd.setMax(100);
        pd.setIndeterminate(false);
        pd.setCancelable(false);
        pd.show();
    }

    /**
     * 获取屏幕 长宽 比例
     *
     * @return
     */
    private float getScaleXY() {
        Point size = new Point();
        wm.getDefaultDisplay().getSize(size);// 1080 1920
        return (float) size.y / size.x;
    }

    private Camera getCameraInstance() {
        int i = FindFrontCamera();
        if (i == -1) {
            toast("请开启前置摄像头");
            return null;
        }
        Camera camera = null;
        try {
            camera = Camera.open(FindFrontCamera());
        } catch (Exception e) {
            toast("相机启动失败");
        }

        return camera;

    }

    /**
     * 获取前置摄像头 id
     *
     * @return
     */
    private int FindFrontCamera() {
        int cameraCount = 0;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras(); // get cameras number

        for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
            Camera.getCameraInfo(camIdx, cameraInfo); // get camerainfo
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                // 代表摄像头的方位，目前有定义值两个分别为CAMERA_FACING_FRONT前置和CAMERA_FACING_BACK后置
                return camIdx;
            }
        }
        return -1;
    }

    /**
     * 向教师端发送消息
     */
    private void sendMessage(final String fileName, final String fileUrl, final String thumbName, final String thumbUrl) {
        BmobIMUserInfo info = new BmobIMUserInfo();
        info.setUserId(userID);
        info.setName(" ");
        BmobIM.getInstance().startPrivateConversation(info, new ConversationListener() {
            @Override
            public void done(BmobIMConversation c, BmobException e) {
                if (e == null) {
                    BmobIMConversation im;
                    im = BmobIMConversation.obtain(BmobIMClient.getInstance(), c);
                    BmobIMTextMessage msg = new BmobIMTextMessage();
                    msg.setContent(code);
                    Map<String, Object> map = new HashMap<>();
                    map.put("username", UserManager.getInstance(getApplicationContext()).getuserInfo().getUsername());
                    map.put("name", UserManager.getInstance(getApplicationContext()).getuserInfo().getName());
                    map.put("filename", fileName);
                    map.put("fileUrl", fileUrl);
                    map.put("thumbName", thumbName);
                    map.put("thumbUrl", thumbUrl);
                    map.put("latitude", latitude);
                    map.put("longitude", longitute);
                    msg.setExtraMap(map);
                    im.sendMessage(msg, new MessageSendListener() {
                        @Override
                        public void onStart(BmobIMMessage msg) {
                            super.onStart(msg);
                        }

                        @Override
                        public void done(BmobIMMessage msg, BmobException e) {
                            toast("已签到！");
                        }
                    });
                } else {
                    toast(e.getMessage() + "(" + e.getErrorCode() + ")");
                }
            }
        });


    }

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            File pictureFile = getOutputMediaFile(System.currentTimeMillis() + "");
            if (pictureFile == null) {

                return;
            }

            try {
                //旋转
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                Matrix matrix = new Matrix();
                matrix.setRotate(-90);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                FileOutputStream fos = new FileOutputStream(pictureFile.getPath());
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.close();
                imgpath = pictureFile.getAbsolutePath();
                //uploadImg(imgpath);
                getLocalThumbnail(imgpath);
            } catch (FileNotFoundException e) {
                toast("采集图像失败！");
                Log.d("camera", "File not found: " + e.getMessage());
            } catch (IOException e) {
                toast("采集图像失败！");
                Log.d("camera", "Error accessing file: " + e.getMessage());
            }
        }
    };


    /**
     * 获取照片文件地址
     *
     * @param name
     * @return
     */
    private File getOutputMediaFile(String name) {

        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/calling/img/";
        Log.d("filepath", path);

        File f = new File(path);
        if (!f.exists()) {
            f.mkdirs();
        }
        return new File(f, name + ".jpg");
    }


    @Override
    public void onBackPressed() {
        finish();
    }
}
