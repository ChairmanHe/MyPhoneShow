package com.hjt.phoneshow.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.hjt.phoneshow.R;
import com.hjt.phoneshow.base.BaseActivity;
import com.hjt.phoneshow.ui.cview.PinchImageView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/11/30.
 */
//这里使用图片放大的时候要注意
//1.使用的imageview要继承PinchImageView
//2.使用的viewpager要使用PinchImageViewPager
//3.重写setPrimaryItem方法，具体可以参考下面被注解掉的方法
public class BigImageActivity extends BaseActivity {
    private ArrayList<String> lists;
    private LayoutInflater mInflater;
    //  private TabListener tabListener;
    private int positon;
    private RadioGroup radioGroup;

    @Override
    protected void initParams() {
        //ButterKnife.bind(this);
        initData();
        initview();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.big_image_activity;
    }

    private void initData() {
        //获取intent的数据
        Intent intent = getIntent();
        Bundle base_img = intent.getExtras();
        lists = base_img.getStringArrayList("base_img");
        positon = base_img.getInt("positon");
        //初始化
        mInflater = LayoutInflater.from(this);
    }

    private void initview() {
        initToolBarAsHome("产品详情");
        //设置viewpager
        ViewPager pager = (ViewPager) findViewById(R.id.viewPager);
        CustomPagerAdapter adapter = new CustomPagerAdapter();
        pager.setAdapter(adapter);
        pager.setCurrentItem(positon, false);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                change(arg0);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });


        int size = 10;
        RadioGroup.LayoutParams lp = new RadioGroup.LayoutParams(size, size);
        lp.leftMargin = 2;
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        for (int i = 0, len = lists.size(); i < len; ++i) {
            RadioButton rb = new RadioButton(this);
            rb.setLayoutParams(lp);
            rb.setBackgroundResource(R.drawable.point);
            rb.setButtonDrawable(android.R.color.transparent);
            rb.setText("");
            rb.setId(i);
            radioGroup.addView(rb);
        }
        change(positon);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        lists.clear();
        lists = null;
    }

    private class CustomPagerAdapter extends PagerAdapter {

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            final ProgressBar progressBar;
            View view = mInflater.inflate(R.layout.zoomimage, null);
            PinchImageView pinchImageView = (PinchImageView) view.findViewById(R.id.model_image);
            progressBar = (ProgressBar) view.findViewById(R.id.progress_loading1);
            Glide.with(BigImageActivity.this).load(lists.get(position)).into(new GlideDrawableImageViewTarget(pinchImageView) {
                @Override
                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                    super.onResourceReady(resource, animation);
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onStart() {
                    super.onStart();
                    progressBar.setVisibility(View.VISIBLE);
                }
            });
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return lists.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }
        // +++++++++++++++++++++++++++++++++++
//       @Override
//       public int getCount() {
//           return Global.getTestImagesCount();
//       }
//
//        @Override
//        public boolean isViewFromObject(View view, Object o) {
//            return view == o;
//        }
//
//        @Override
//        public Object instantiateItem(ViewGroup container, int position) {
//            PinchImageView piv;
//            if (viewCache.size() > 0) {
//                piv = viewCache.remove();
//                piv.reset();
//            } else {
//                piv = new PinchImageView(PagerActivity.this);
//            }
//            ImageSource image = Global.getTestImage(position);
//            Global.getImageLoader(getApplicationContext()).displayImage(image.getUrl(100, 100), piv, thumbOptions);
//            container.addView(piv);
//            return piv;
//        }
//
//        @Override
//        public void destroyItem(ViewGroup container, int position, Object object) {
//            PinchImageView piv = (PinchImageView) object;
//            container.removeView(piv);
//            viewCache.add(piv);
//        }
//
//        @Override
//        public void setPrimaryItem(ViewGroup container, int position, Object object) {
//            PinchImageView piv = (PinchImageView) object;
//            ImageSource image = Global.getTestImage(position);
//            Global.getImageLoader(getApplicationContext()).displayImage(image.getUrl(image.getOriginWidth(), image.getOriginHeight()), piv, originOptions);
//            pager.setMainPinchImageView(piv);
//        }
    }

    /**
     * 改变小圆点的位置
     *
     * @param pos
     */
    private void change(int pos) {
        radioGroup.check(pos);
    }


}
