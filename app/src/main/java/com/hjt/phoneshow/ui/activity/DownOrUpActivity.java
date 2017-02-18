package com.hjt.phoneshow.ui.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hjt.phoneshow.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/1/3.
 */
//这是用来执行断点上传下载的activity
//里面包含了使用rx延时，okhttp反问下载一张图片
public class DownOrUpActivity extends Activity {
    //private String path="http://p1.so.qhmsg.com/t0196896c77ff138dd6.jpg";
    @BindView(R.id.buttondown)
    Button button1;
    @BindView(R.id.imageview)
    ImageView imageview;
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.passage)
    EditText passage;
    @BindView(R.id.save)
    Button save;
    @BindView(R.id.textview_)
    TextView textview;
    private CameraManager cameraManager;
    private String[] mCameraIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downorup);
        ButterKnife.bind(this);
        //获取手电筒的manager
        cameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);
        getCameraList();//获取当前手机的摄像头个数
    }

    @OnClick({R.id.buttondown, R.id.imageview, R.id.openLight, R.id.save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttondown:
                //点击按钮 delay(5,TimeUnit.SECONDS)后再发送数据
                String path = "http://p1.so.qhmsg.com/t0196896c77ff138dd6.jpg";
                downloadImage(path).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<byte[]>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(byte[] bytes) {
                        //获得传来的数据后显示在imageview上面
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        //对图片进行压缩
                        Bitmap bitmap1 = compreessIamge(bitmap);
                        imageview.setImageBitmap(bitmap1);
                        if (bitmap != null) {
                            bitmap.recycle();
                        }
                    }
                });
                break;
            case R.id.imageview:
                //interval表示每间隔5秒钟执行一次操作
                Observable.interval(5, TimeUnit.SECONDS).subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {
                        //Log.e("test","------>alongtime:"+ SystemClock.elapsedRealtime());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Long aLong) {
                        Log.e("test", "------>along：" + aLong + " time:" + SystemClock.elapsedRealtime());
                    }
                });
                break;
            case R.id.openLight:
                //测试打开7.0手电筒   7.0上面才能运行
                changeFlashState(true);
                break;
            case R.id.save:
                //将用户输入的保存到properties文件中
                save_info();
                //将用户输入的数据从文件中取出来并且显示出来
                Map<String, String> map = show_info();
                textview.setText("账号：" + map.get("name") + "\n" + "密码：" + map.get("pass"));
                break;
        }
    }

    //******************************保存properties文件*************************************//
    private void save_info() {
        //获取用户输入
        String pass = passage.getText().toString().trim();
        String name_input = name.getText().toString().trim();
        Properties properties = new Properties();
        properties.put("name", name_input);
        properties.put("pass", pass);
        try {
            //将读取到的properties写入到文件输出流下面
            FileOutputStream outputStream = this.openFileOutput("info.cfg", Context.MODE_WORLD_WRITEABLE);
            properties.store(outputStream, "");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Map<String, String> show_info() {
        Map<String, String> map = new HashMap<String, String>();
        //读取properties中的数据
        Properties properties = new Properties();
        try {
            FileInputStream inputStream = this.openFileInput("info.cfg");
            //将文件中的数据直接读到properties中
            properties.load(inputStream);
            String name = properties.get("name").toString();
            String pass = properties.get("pass").toString();
            map.put("name", name);
            map.put("pass", pass);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    //*****************************************************************************//
    @Override
    protected void onResume() {
        super.onResume();
    }

    /************************************
     * 这里是测试手电筒
     *******************************************/
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void getCameraList() {
        if (cameraManager != null) {
            try {
                mCameraIds = cameraManager.getCameraIdList();
                Log.e("mCameraIds", mCameraIds + "");
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressLint("NewApi") //只有M版本的手机可以使用这个方法
    private void changeFlashState(boolean isFlashOn) {
        if (cameraManager != null && mCameraIds != null) {
            try {
                cameraManager.setTorchMode(mCameraIds[0], isFlashOn);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /*********************************************************************************/

    //***************这个里面是实现点击后延时2秒后去异步下载网络图片*********************//
    //返回一个下载的被观察者    这里的byte数组泛型是传递的结果类型
    public Observable<byte[]> downloadImage(final String path) {
        //这里是代表延时两秒后再去发送网络请求
//        timer()：用于创建Observable，延迟发送一次。
//        interval()：用于创建Observable，跟TimerTask类似，用于周期性发送。
//        delay()：用于事件流中，可以延迟发送事件流中的某一次发送。
        return Observable.create(new Observable.OnSubscribe<byte[]>() {
            @Override
            public void call(final Subscriber<? super byte[]> subscriber) {
                if (!subscriber.isUnsubscribed()) {
                    //反问网络操作
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url(path).build();
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            subscriber.onError(e);
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if (response.isSuccessful()) {
                                if (response.body() != null) {
                                    subscriber.onNext(response.body().bytes());
                                }
                            }
                        }
                    });
                }
            }
        }).delay(5, TimeUnit.SECONDS);//这里是延迟了5秒钟在去发送结果
    }

    //这里是对图片进行压缩
    public Bitmap compreessIamge(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();
            options -= 100;
            //这里压缩options%，把压缩后的数据存放到baos中
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);
        }

        ByteArrayInputStream inputStream = new ByteArrayInputStream(baos.toByteArray());
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        return bitmap;
    }

    //****************这个里面是实现点击后延时2秒后去异步下载网络图片**********************//
}
