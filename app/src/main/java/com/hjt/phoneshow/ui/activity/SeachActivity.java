package com.hjt.phoneshow.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.hjt.phoneshow.R;
import com.hjt.phoneshow.base.BaseActivity;
import com.hjt.phoneshow.bean.ModelList;
import com.hjt.phoneshow.db.RecordSQLiteOpenHelper;
import com.hjt.phoneshow.ui.adapter.MainActivityAdapter;
import com.hjt.phoneshow.ui.cview.ClearEditText;
import com.hjt.phoneshow.ui.cview.MyListView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.hjt.phoneshow.R.id.tv_tip;


/**
 * Created by Administrator on 2016/12/17.
 */

public class SeachActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.search_et_input)
    ClearEditText searchEtInput;
    @BindView(R.id.cancel)
    TextView cancel;
    @BindView(tv_tip)
    TextView tvTip;
    @BindView(R.id.listView)
    MyListView listView;
    @BindView(R.id.tv_clear)
    TextView tvClear;
    @BindView(R.id.recycle_search)
    RecyclerView recycleSearch;
    @BindView(R.id.fl_search_nodata)
    FrameLayout flSearchNodata;
    @BindView(R.id.scrollView_search)
    ScrollView scrollView_search;
    private ArrayList<ModelList.List> lists;
    private RecordSQLiteOpenHelper helper = new RecordSQLiteOpenHelper(this);
    private SQLiteDatabase database;
    private SimpleCursorAdapter adapter;
    private ArrayList<ModelList.List> resultData;
    private MainActivityAdapter adapter_main;
    private Drawable mClearDrawable;
    private long firstClickTime=0;

    /**
     * 传递过来的对象集合
     */


    @Override
    protected void initParams() {
        //获取intent的传来的数据
        lists = (ArrayList<ModelList.List>) getIntent().getSerializableExtra("data");
        //int size = lists.size();
        initView();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_seach;
    }

    private void initView() {
        // 第一次进入查询所有的历史记录
        queryData("");
        //清空历史搜索记录
        tvClear.setOnClickListener(this);
        //点击取消的的按钮
        cancel.setOnClickListener(this);
        // 搜索框的键盘搜索键点击回调  输入完后按键盘上的搜索键
        searchEtInput.setOnKeyListener(new View.OnKeyListener() {

            public String result;

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // 按完搜索键后将当前查询的关键字保存起来,如果该关键字已经存在就不执行保存
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {// 修改回车键功能
                    //如果用户的输入为空  返回
                    result = searchEtInput.getText().toString().trim();
                    if (TextUtils.isEmpty(result)) {
                        Toast.makeText(SeachActivity.this, getResources().getString(R.string.input_notrim), Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    // 先隐藏键盘
                    ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                            getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    boolean hasData = hasData(result);
                    if (!hasData) {
                        insertData(result);
                        queryData("");
                    }
                    // TODO 根据输入的内容模糊查询商品，并跳转到另一个界面，由你自己去实现
                    //点击搜索后去查询并且适配适配器
                    onSearch(result);
                }
                return false;
            }
        });

        //searchEtInput搜索框的点击事件
        searchEtInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //当文本发生变化时隐藏布局
                scrollView_search.setVisibility(View.VISIBLE);
                recycleSearch.setVisibility(View.GONE);
                flSearchNodata.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().trim().length() == 0) {
                    tvTip.setText(getResources().getString(R.string.seach_history));
                } else {
                    tvTip.setText(getResources().getString(R.string.search_result));
                }
                String tempName = searchEtInput.getText().toString();
                // 根据tempName去模糊查询数据库中有没有数据
                queryData(tempName);

            }
        });
        //listView的点击监听
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                String name = textView.getText().toString();
                searchEtInput.setText(name);
                searchEtInput.setSelection(name.length());
                // TODO 获取到item上面的文字，根据该关键字跳转到另一个页面查询，由你自己去实现
                // 先隐藏键盘
                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                        getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                //去搜索
                onSearch(name);
            }
        });
    }

    //根据输入的字符串去匹配
    private void onSearch(String search) {
//        if (lists != null || lists.size() == 0) {
//            Toast.makeText(SeachActivity.this, getResources().getString(R.string.search_nodata), Toast.LENGTH_SHORT).show();
//            return;
//        }
        if (resultData == null) {
            // 初始化
            resultData = new ArrayList<>();
        }
        resultData.clear();
        for (int i = 0; i < lists.size() - 1; i++) {
            if (lists.get(i).getName().contains(search.trim())) {
                resultData.add(lists.get(i));
            }
        }
        //判断数据如果是空的说明没有搜索到数据
        if (resultData == null || resultData.size() == 0) {
            scrollView_search.setVisibility(View.GONE);
            flSearchNodata.setVisibility(View.VISIBLE);
        } else {
            scrollView_search.setVisibility(View.GONE);
            recycleSearch.setVisibility(View.VISIBLE);
            flSearchNodata.setVisibility(View.GONE);
            if (adapter_main == null) {
                adapter_main = new MainActivityAdapter(this, resultData);
                adapter_main.setOnItemClickListener(new MainActivityAdapter.MyItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        onItemClickTo(view,position);
                    }
                });
                recycleSearch.setLayoutManager(new GridLayoutManager(this, 2));
                recycleSearch.setAdapter(adapter_main);
            } else {
                adapter_main.notifyDataSetChanged();
            }
        }


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_clear:
                //清空历史记录
                deleteData();
                queryData("");
                break;
            case R.id.cancel:
                finish();
        }

    }
   /*数据库的增删该查操作*/

    /**
     * 插入数据
     *
     * @param tempName
     */
    private void insertData(String tempName) {
        database = helper.getWritableDatabase();
        database.execSQL("insert into records(name) values('" + tempName + "')");
        database.close();
    }

    /**
     * 模糊查询
     * @param
     */
    private void queryData(String tempName) {
        Cursor cursor = helper.getWritableDatabase().rawQuery("select id as _id,name from records where name like '%" + tempName + "%' order by id desc ", null);
        //这里使用了修改安卓自带的textview
        adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, cursor, new String[]{"name"}, new int[]{android.R.id.text1}, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                textView.setTextColor(Color.BLACK);
                textView.setTextSize(16f);
                return textView;
            }
        };
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    /**
     * 检查数据库中是否已经有该条记录
     */
    private boolean hasData(String tempName) {
        Cursor cursor = helper.getReadableDatabase().rawQuery(
                "select id as _id,name from records where name =?", new String[]{tempName});
        //判断是否有下一个
        return cursor.moveToNext();
    }

    /**
     * 清空数据
     */
    private void deleteData() {
        database = helper.getWritableDatabase();
        database.execSQL("delete from records");
        database.close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
    //设置recycleview的点击事件
    public void onItemClickTo(View view, int position) {
        //防止连续点击item
        long currentTime = System.currentTimeMillis();
        if (currentTime - firstClickTime < 800) {
            firstClickTime = currentTime;
            return;
        }
        firstClickTime = currentTime;
        ModelList.List model = resultData.get(position);
        Intent intent = new Intent(SeachActivity.this, DetailActivity.class);
        intent.putExtra("id", model.getId());
        intent.putExtra("name", model.getName());
        intent.putExtra("icon", model.getIcon());
        startActivity(intent);
//        //t添加activity的协作动画
//        ActivityOptionsCompat compat =
//                ActivityOptionsCompat.makeSceneTransitionAnimation(SeachActivity.this,
//                        view, getString(R.string.transition));
//        ActivityCompat.startActivity(SeachActivity.this, intent, compat.toBundle());
       finish();
    }
}
