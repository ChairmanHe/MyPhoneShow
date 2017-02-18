package com.hjt.phoneshow.util;

import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.hjt.phoneshow.app.MyApp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by z on 2016/11/18/018.
 */
//这是一个将数据库复制到SD卡里面的工具类
public class CopyDBUtils {
    private static final String TAG = "CopyDBUtils";
    /*改成数据库名*/
    public static String dbName = "demo.db";
//    public static String dbName2 = "kxd_contact1.db";
    public static void copyDbToSDcard() {
//        File dataDirectory = Environment.getDataDirectory();
        File databaseFile = MyApp.getContext().getDatabasePath(dbName);

        try {
            File f = new File(Environment.getExternalStorageDirectory()
                    + "/" + dbName);//这里的参数是数据库的名字
            FileOutputStream fs = new FileOutputStream(f);
            FileInputStream input = new FileInputStream(databaseFile);
            int len = -1;
            byte[] buffer = new byte[1024];
            while ((len = input.read(buffer, 0, 1024)) > 0) {
                fs.write(buffer, 0, len);
            }
            fs.close();
            input.close();
            Log.i(TAG, "copyDbToSDcard: 拷贝完成。。。");
            Toast.makeText(MyApp.getContext(), "拷贝完成。。", Toast.LENGTH_SHORT).show();
//            L.i("CopyDBUtils : copyDbToSDcard: 数据库拷贝完成 : " + f.toString());
        } catch (Exception e) {
            // TODO: handle exception
//            L.i("CopyDBUtils : copyDbToSDcard: 数据库拷贝失败.....");
            Log.i(TAG, "copyDbToSDcard: 拷贝失败哦。。。。");
            Toast.makeText(MyApp.getContext(), "拷贝失败。。。", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

//    public static void copyDb2SD() {
////        File dataDirectory = Environment.getDataDirectory();
//        File databaseFile = MyApp.getContext().getDatabasePath(dbName);
//
//        try {
//            File f = new File(Environment.getExternalStorageDirectory()
//                    + "/" + dbName2);//这里的参数是数据库的名字
//            FileOutputStream fs = new FileOutputStream(f);
//            FileInputStream input = new FileInputStream(databaseFile);
//            int len = -1;
//            byte[] buffer = new byte[1024];
//            while ((len = input.read(buffer, 0, 1024)) > 0) {
//                fs.write(buffer, 0, len);
//            }
//            fs.close();
//            input.close();
//            L.i("CopyDBUtils : copyDbToSDcard: 数据库拷贝完成 : " + f.toString());
//        } catch (Exception e) {
//            // TODO: handle exception
//            L.i("CopyDBUtils : copyDbToSDcard: 数据库拷贝失败.....");
//            e.printStackTrace();
//        }
//    }
}
