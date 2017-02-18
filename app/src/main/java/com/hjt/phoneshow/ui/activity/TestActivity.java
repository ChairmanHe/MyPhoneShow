package com.hjt.phoneshow.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.hjt.phoneshow.R;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/12/22.
 */
//测试跳转到第三方应用和文件流的读取
public class TestActivity extends Activity {
    @BindView(R.id.textview)
    TextView textview;
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
        //在sd卡下新建一个文件夹
        path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/hjt";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        //设置textview的文字
        StringBuffer note= new StringBuffer();
        note.append("InputStream / OutputStream"+"字节流的抽象父类");
        note.append("\n");
        note.append("\n");
        note.append("BufferedInputStream/BufferedOutputStream"+"提供一个内存缓冲区,提升单字节读写效率");
        note.append("\n");
        note.append("\n");
        note.append("Reader/Writer"+"字符流的抽象父类");
        note.append("\n");
        note.append("\n");
        note.append("InputStreamReader/OutputStreamWriter"+"编码转换流");
        note.append("\n");
        note.append("\n");
        note.append("FileReader/FileWriter"+"本质是转换流接文件流");
        note.append("\n");
        note.append("\n");
        note.append("BufferedReader/BufferedWriter"+" 字符缓冲流，提高单个字符读写效率，可以readLine()");
        note.append("\n");
        note.append("\n");
        note.append("PrintWriter"+"与 PrintStream 相同任何数据转为字符串，\n *) PrintStream 只能接字节流\n" +
                "*) PrintWriter 可以接字符流或字节流");
        note.append("\n");
        note.append("\n");
        note.append("ByteArrayInputStream /ByteArrayOutputStream"+"与内存中的 byte[] 数组相接,读写数组中的数据");
        note.append("\n");
        note.append("\n");
        note.append("ObjectOutputStream  /java.io.ObjectInputStream "+"对象序列化、反序列化,可以readObject()");
        note.append("\n");
        note.append("\n");
        textview.setText(note);
        //设置textview可以滑动
        textview.setMovementMethod(ScrollingMovementMethod.getInstance());
    }

    public void Test(View view) {
        //测试跳转到录音机
//        Intent intent = new Intent(Intent.ACTION_MAIN);
//        intent.addCategory(Intent.CATEGORY_LAUNCHER);
//        ComponentName cn = new ComponentName("com.kxd.soundrecorder", "com.kxd.soundrecorder.audio.MainActivity");
//        intent.setComponent(cn);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setClassName("com.kxd.soundrecorder", "com.kxd.soundrecorder.audio.MainActivity");
        intent.putExtra(MediaStore.Audio.Media.EXTRA_MAX_BYTES, 10);
        //intent.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == 100) {
            Uri data1 = data.getData();
            Log.e("data1", data1.toString());
        }
    }

    @OnClick({R.id.button1, R.id.button2, R.id.button3, R.id.button4})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button1:
                try {
                    RandomAccessFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.button2:
                try {
                    DataOutputStreamRW();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.button3:
                try {
                    PrintStreamR();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.button4:
                break;
        }
    }

    //用这个读写文件可以定位到指定位置，这个读写可以用来做断点下载的时候记录下载的位置，下次可以
    //直接从下载的位置开始读写   如果你的流刚才已经读过一遍了  那么流里面的数据就没有了
    public void RandomAccessFile() throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile(path + "/test.txt", "rw");
        //写入一个数    这个是一个字节一个字节的写入  里面也有写入int 或者字符串型的
        //一般就是你怎么写的就怎么读
        randomAccessFile.write(97);
        randomAccessFile.write(65);
        byte[] bytes = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        randomAccessFile.write(bytes);
        //从指定位置2写5个
        randomAccessFile.write(bytes, 2, 5);
        //下标定位到0位置
        randomAccessFile.seek(0);

        //单字节读取
        int b;
        while ((b = randomAccessFile.read()) != -1) {
            int b1 = b;
            System.out.println(b);
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //读到数组缓存中  字节数为5
        bytes = new byte[5];
        int n;
        while ((n = randomAccessFile.read(bytes)) != -1) {
            String string = bytes.toString();
            System.out.println(bytes.toString());
            //Toast.makeText(MyApp.getContext(),"读到的数据"+bytes.toString(),Toast.LENGTH_SHORT);
        }
        randomAccessFile.close();
    }

    //DataOutputStream可以写java基本类型  还可以写中文
    //注意事项：这里怎么写的就要怎么读，要不然会抛出异常
    public void DataOutputStreamRW() throws IOException {
        DataOutputStream out = new DataOutputStream(new FileOutputStream(path + "/test2.txt"));
        out.write(97);
        out.writeInt(4);
        out.writeBytes("454");
        out.writeUTF("中文");
        out.flush();

        DataInputStream in = new DataInputStream(new FileInputStream(path + "/test2.txt"));
        int read = in.read();
        int readInt = in.readInt();
        System.out.println(in.readBoolean());
        System.out.println(in.readUTF());
        in.close();
    }

    // 打印输出流 任何数据转为字符串输出
    public void PrintStreamR() throws FileNotFoundException {
        PrintStream out = new PrintStream(path + "/test3.txt");
        out.write(97);
        out.write(13);//回车符 \r
        out.write(10);//换行符 \n
        out.println(3.14);
        out.print(new Date());
        out.close();
    }

    //***********************************文件复制**********************************************//
    private void copy(
            File f1,
            File f2) throws Exception {
		/*
		 * 1.创建FileInputStream赋给 in
		 *   与文件 f1 相接
		 * 2.创建FileOutputStream赋给 out
		 *   与文件 f2 相接
		 * 3.批量读取标准格式，数组长度8192
		 *     4.批量输出数组中0开始的n个
		 * 5.in.close()
		 *   out.close()
		 */
        BufferedInputStream in =
                new BufferedInputStream(
                        new FileInputStream(f1));

        BufferedOutputStream out =
                new BufferedOutputStream(
                        new FileOutputStream(f2));
		/*byte[] buff = new byte[8192];
		int n;
		while((n = in.read(buff)) != -1) {
			out.write(buff,0,n);
		}*/
        int b;
        while((b = in.read()) != -1) {
            out.write(b);
        }
        in.close();
        out.close();
    }
    //***********************************文件拆分合并********************************************//
    private static void split(
            File file,
            long size) throws Exception{

        //准备目录,获得文件名
        String name = file.getName();

        File dir =
                new File(
                        file.getAbsolutePath()+"_split");
        if(dir.exists()) {
            File[] files = dir.listFiles();
            for (File f : files) {
                f.delete();
            }
        } else {
            dir.mkdirs();
        }

        ///////////////////////////////////////
		/*
		 * 1.新建FileInputStream对象 in 接文件 file
		 * 2.定义FileOutputStream out=null;
		 * 3.定义字节计数变量 byteCount=0
		 * 4.定义文件计数变量 fileCount=0
		 * 5.单字节循环读取，字节值赋给 b
		 *     6.如果out==null或者byteCount==size
		 *         7.如果out!=null, out.close()
		 *         8.新建 FileOutputStream 赋给out
		 *           接文件dir目录下
		 *           的name+"."+(++fileCount)
		 *             new File(dir,  name+"."+(++fileCount));
		 *         9.byteCount=0
		 *     10.out.write(b)
		 *     11.byteCount++
		 * 12.in.close()
		 *    out.close()
		 */
        BufferedInputStream in =
                new BufferedInputStream(
                        new FileInputStream(file));

        BufferedOutputStream out = null;

        long byteCount=0;
        int fileCount=0;
        int b;
        while((b = in.read()) != -1) {
            if(out==null || byteCount==size) {
                if(out!=null)out.close();

                out =
                        new BufferedOutputStream(
                                new FileOutputStream(
                                        new File(
                                                dir,
                                                name+"."+(++fileCount))));

                byteCount=0;
            }
            out.write(b);
            byteCount++;
        }
        in.close();
        out.close();


    }

}
