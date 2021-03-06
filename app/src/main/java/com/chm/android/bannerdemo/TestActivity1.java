package com.chm.android.bannerdemo;

import android.chm.com.bannerdemo.R;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.jude.rollviewpager.OnItemClickListener;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;
import com.jude.rollviewpager.hintview.ColorPointHintView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 类名: TestActivity1
 * 作者: 陈海明
 * 时间: 2017-08-10 16:30
 * 描述: NULL
 */
public class TestActivity1 extends BaseActivity {

    @BindView(R.id.mRollPagerView1)
    RollPagerView mRollViewPager1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test1);
        ButterKnife.bind(this);
        initBanner();
    }

    //初始化Banner
    private void initBanner() {
        mRollViewPager1.setAdapter(new ImageNormalAdapter());//设置适配器

        //指示器4兄弟,也就是那小圆点
       /* mRollViewPager1.setHintView(new IconHintView(this,R.drawable.point_focus,R.drawable.point_normal));//自定义指示器
        mRollViewPager1.setHintView(new TextHintView(this));//设置指示器为文字
        mRollViewPager1.setHintView(null);//隐藏指示器*/
        mRollViewPager1.setHintView(new ColorPointHintView(this, Color.YELLOW, Color.WHITE));//设置指示器颜色
      /*
        mRollViewPager1.setPlayDelay(3000);//设置播放时间间隔
        mRollViewPager1.setAnimationDurtion(500);  //设置透明度*/

        //点击监听
        mRollViewPager1.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(TestActivity1.this, "你点几了第 " + position + " 图片", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //适配器
    private class ImageNormalAdapter extends StaticPagerAdapter {
        //本地图片资源
        int[] imgs = new int[]{
                R.drawable.img1,
                R.drawable.img2,
                R.drawable.img3,
                R.drawable.img4,
                R.drawable.img5,
        };

        @Override
        public View getView(ViewGroup container, int position) {
            ImageView view = new ImageView(container.getContext());
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            view.setImageResource(imgs[position]);
            return view;
        }

        @Override
        public int getCount() {
            return imgs.length;
        }
    }

    //设置透明状态栏
    @Override
    public void statusBarColor() {
        super.statusBarColor();
        mImmersionBar.transparentStatusBar().init();
    }
}
