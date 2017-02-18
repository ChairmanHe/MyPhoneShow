package com.hjt.phoneshow.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hjt.phoneshow.R;
import com.hjt.phoneshow.base.BaseActivity;
import com.hjt.phoneshow.bean.ChatBean;
import com.hjt.phoneshow.bean.VoiceBean;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Administrator on 2016/12/8.
 */
//这是科大讯飞的语音识别

/**
 * 1.去官网申请科大讯飞的APPID，下载依赖库，将依赖库中的jar和so文件导入到项目中
 * 2.在配置清单文件里面添加语音权限
 * 3.在需要调用语音的界面SpeechUtility.createUtility(this, SpeechConstant.APPID + "=5848c02b")；
 * 初始化语音
 * 4.拷贝语音需要的assets资源文件，这里是讯飞自带的语音ui
 * 5.RecognizerDialogListener recognizerDialogListener编写监听类。实现里面的方法得到语音输入的结果
 * 6.  read（）方法里面是用来读取语音
 */
public class VoiceActivity extends BaseActivity {
    private ListView lvList;
    private ArrayList<ChatBean> mChatList = new ArrayList<ChatBean>();
    private ChatAdapter mAdapter;
    private String[] mMMAnswers = new String[] { "约吗?", "讨厌!", "不要再要了!",
            "这是最后一张了!", "漂亮吧?" };
    private int[] mMMImageIDs = new int[] { R.drawable.p1, R.drawable.p2,
            R.drawable.p3, R.drawable.p4 };

    @Override
    protected void initParams() {
        lvList = (ListView) findViewById(R.id.lv_list);
        mAdapter = new ChatAdapter();
        lvList.setAdapter(mAdapter);
        // 初始化语音引擎   这个需要去申请id
       SpeechUtility.createUtility(this, SpeechConstant.APPID + "=5848c02b");
        //注册极光推送过来的广播   用来接收数据后播报语音
        IntentFilter filter= new IntentFilter("jiguang");
        PlusReceive receive= new PlusReceive();
        registerReceiver(receive,filter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_voice;
    }

    StringBuffer mTextBuffer = new StringBuffer();
    private RecognizerDialogListener recognizerDialogListener = new RecognizerDialogListener() {

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            // System.out.println(results.getResultString());
            // System.out.println("isLast=" + isLast);

            String text = parseData(results.getResultString());
            mTextBuffer.append(text);

            if (isLast) {// 会话结束
                String finalText = mTextBuffer.toString();
                mTextBuffer = new StringBuffer();// 清理buffer
                System.out.println("最终结果:" + finalText);
                mChatList.add(new ChatBean(finalText, true, -1));

                String answer = "没听清";
                int imageId = -1;
                if (finalText.contains("你好")) {
                    answer = "大家好,才是真的好!";
                } else if (finalText.contains("你是谁")) {
                    answer = "我是你的小助手!";
                } else if (finalText.contains("美女")) {
                    Random random = new Random();
                    int i = random.nextInt(mMMAnswers.length);
                    int j = random.nextInt(mMMImageIDs.length);
                    answer = mMMAnswers[i];
                    imageId = mMMImageIDs[j];
                }
                mChatList.add(new ChatBean(answer, false, imageId));// 添加回答数据
                mAdapter.notifyDataSetChanged();// 刷新listview
                lvList.setSelection(mChatList.size() - 1);// 定位到最后一张
                read(answer);
            }

        }

        @Override
        public void onError(SpeechError arg0) {

        }
    };

    /**
     * 语音朗诵
     */
    public void read(String text) {
        SpeechSynthesizer mTts = SpeechSynthesizer
                .createSynthesizer(this, null);

        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");
        mTts.setParameter(SpeechConstant.SPEED, "50");
        mTts.setParameter(SpeechConstant.VOLUME, "80");
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);

        mTts.startSpeaking(text, null);
    }

    /**
     * 开始语音识别
     *
     * @param view
     */
    public void startListen(View view) {
        RecognizerDialog iatDialog = new RecognizerDialog(this, null);

        // 2.设置听写参数，详见《科大讯飞MSC API手册(Android)》SpeechConstant类
        iatDialog.setParameter(SpeechConstant.DOMAIN, "iat");
        iatDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        iatDialog.setParameter(SpeechConstant.ACCENT, "mandarin");

        iatDialog.setListener(recognizerDialogListener);

        iatDialog.show();
    }

    class ChatAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mChatList.size();
        }

        @Override
        public ChatBean getItem(int position) {
            return mChatList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(VoiceActivity.this,
                        R.layout.list_item, null);

                holder.tvAsk = (TextView) convertView.findViewById(R.id.tv_ask);
                holder.tvAnswer = (TextView) convertView
                        .findViewById(R.id.tv_answer);
                holder.llAnswer = (LinearLayout) convertView
                        .findViewById(R.id.ll_answer);
                holder.ivPic = (ImageView) convertView
                        .findViewById(R.id.iv_pic);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            ChatBean item = getItem(position);

            if (item.isAsker) {// 是提问者
                holder.tvAsk.setVisibility(View.VISIBLE);
                holder.llAnswer.setVisibility(View.GONE);

                holder.tvAsk.setText(item.text);
            } else {
                holder.tvAsk.setVisibility(View.GONE);
                holder.llAnswer.setVisibility(View.VISIBLE);
                holder.tvAnswer.setText(item.text);
                if (item.imageId != -1) {// 有图片
                    holder.ivPic.setVisibility(View.VISIBLE);
                    holder.ivPic.setImageResource(item.imageId);
                } else {
                    holder.ivPic.setVisibility(View.GONE);
                }
            }

            return convertView;
        }

    }

    static class ViewHolder {
        public TextView tvAsk;
        public TextView tvAnswer;
        public LinearLayout llAnswer;
        public ImageView ivPic;
    }

    /**
     * 解析语音数据
     *
     * @param resultString
     */
    protected String parseData(String resultString) {
        Gson gson = new Gson();
        VoiceBean bean = gson.fromJson(resultString, VoiceBean.class);
        ArrayList<VoiceBean.WSBean> ws = bean.ws;

        StringBuffer sb = new StringBuffer();
        for (VoiceBean.WSBean wsBean : ws) {
            String text = wsBean.cw.get(0).w;
            sb.append(text);
        }
        return sb.toString();
    }
  //极光推送的广播接收后用来播报消息内容
    public class  PlusReceive extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String result = intent.getStringExtra("result");
             read(result);
        }
    }


}
