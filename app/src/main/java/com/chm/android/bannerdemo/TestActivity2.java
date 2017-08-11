package com.chm.android.bannerdemo;

import android.chm.com.bannerdemo.R;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jude.rollviewpager.OnItemClickListener;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;
import com.jude.rollviewpager.hintview.ColorPointHintView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 类名: TestActivity1
 * 作者: 陈海明
 * 时间: 2017-08-10 16:30
 * 描述: NULL
 */
public class TestActivity2 extends BaseActivity {
    @BindView(R.id.mRollPagerView2)
    RollPagerView mRollViewPager;
    @BindView(R.id.btn_reduce)
    Button btnReduce;
    @BindView(R.id.btn_add)
    Button btnAdd;

    private ImageNormalAdapter mAdapter;
    private static OkHttpClient client;
    int page=0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);
        ButterKnife.bind(this);
        initBanner();
        //请求图片
        getData(page);
    }
    //上一页下一页点击事件
    @OnClick({R.id.btn_reduce, R.id.btn_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_reduce:
                if (page!=0){
                    --page;
                }
                break;
            case R.id.btn_add:
                page++;
                break;
        }
        getData(page);
    }

    private void initBanner() {
        mRollViewPager.setAdapter(mAdapter = new ImageNormalAdapter(mRollViewPager));//设置适配器

        //指示器4兄弟,也就是那小圆点
       /* mRollViewPager.setHintView(new IconHintView(this,R.drawable.point_focus,R.drawable.point_normal));//自定义指示器
        mRollViewPager.setHintView(new TextHintView(this));//设置指示器为文字
        mRollViewPager.setHintView(null);//隐藏指示器*/
        mRollViewPager.setHintView(new ColorPointHintView(this, Color.YELLOW, Color.WHITE));//设置指示器颜色
      /*
        mRollViewPager.setPlayDelay(3000);//设置播放时间间隔
        mRollViewPager.setAnimationDurtion(500);  //设置透明度*/


        mRollViewPager.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(TestActivity2.this, "你点几了第 " + position + " 图片", Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * 网络请求获得图片url
     * @param page 页码
     */
    public void getData(final int page) {
        client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://gank.io/api/data/%E7%A6%8F%E5%88%A9/" + 5 + "/" + page)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                Log.i("NetImageActivity", "error:" + e.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(TestActivity2.this, "网络请求失败，error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String content = response.body().string();
                    Log.i("TestActivity2", "raw data:" + content);
                    JSONObject jsonObject = new JSONObject(content);
                    JSONArray strArr = jsonObject.getJSONArray("results");
                    final String[] imgs = new String[strArr.length()];
                    for (int i = 0; i < strArr.length(); i++) {
                        JSONObject obj = strArr.getJSONObject(i);
                        imgs[i] = obj.getString("url");
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.setImgs(imgs);//添加图片
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    //适配器
    private class ImageNormalAdapter extends LoopPagerAdapter {

        String[] imgs = new String[0];
        //添加图片url
        public void setImgs(String[] imgs) {
            this.imgs = imgs;
            notifyDataSetChanged();
        }

        public ImageNormalAdapter(RollPagerView viewPager) {
            super(viewPager);
        }

        @Override
        public View getView(ViewGroup container, int position) {
            ImageView view = new ImageView(container.getContext());
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            Glide.with(TestActivity2.this)//Glide解析url获得图片
                    .load(imgs[position])
                    .into(view);
            return view;
        }


        @Override
        public int getRealCount() {
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
