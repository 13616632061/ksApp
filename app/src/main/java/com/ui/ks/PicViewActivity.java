package com.ui.ks;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.base.BaseActivity;
import com.ui.adapter.PicDetailAdapter;
import com.ui.fragment.PicDetailFragment;
import com.ui.entity.PicsDetail;
import com.ui.util.BitmapUtils;
import com.ui.util.HackyViewPager;

import java.util.ArrayList;
import java.util.List;

public class PicViewActivity extends BaseActivity {
    RelativeLayout buttom_layout, main_layout;

    String[] picList;

    private ArrayList<PicsDetail> newsList = new ArrayList<PicsDetail>();

    ImageView pic_save;

    private Bitmap mBitmap = null;

    HackyViewPager pager;
    PicDetailAdapter pagerAdapter;
    List<Fragment> fragments = new ArrayList<Fragment>();

    int currPosition = 0;
    TextView pic_size;
    int offset = 0;
    boolean in_weibo = false;
    private boolean no_bottom = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, 0, false);
        setContentView(R.layout.activity_pic_view);

        Bundle bundle = new Bundle();
        bundle = this.getIntent().getExtras();

        if (bundle != null) {
            if (bundle.containsKey("pic_list")) {
                picList = bundle.getString("pic_list").split("##");
            }
            if (bundle.containsKey("offset")) {
                offset = bundle.getInt("offset");

                if(offset < 0 || offset > (picList.length - 1)) {
                    offset = 0;
                }
            }

            if (bundle.containsKey("in_weibo")) {
                in_weibo = bundle.getBoolean("in_weibo");
            }
            if (bundle.containsKey("no_bottom")) {
                no_bottom = bundle.getBoolean("no_bottom");
            }
        }
        pager = (HackyViewPager) findViewById(R.id.pager);

        buttom_layout = (RelativeLayout) this.findViewById(R.id.news_image_news_detail_buttom_layout);
//        buttom_layout.setOnClickListener(null);
        if(no_bottom) {
            buttom_layout.setVisibility(View.GONE);
        }
        main_layout = (RelativeLayout) this.findViewById(R.id.news_image_news_detail_main_layout);
        pic_size = (TextView) this.findViewById(R.id.news_image_news_detail_top_title);

        for(int i = 0; i < picList.length; i++) {
            PicsDetail news = new PicsDetail("0", "", picList[i]);
            newsList.add(news);
        }

        //保存图片
        pic_save = (ImageView) this.findViewById(R.id.news_image_news_detail_buttom_download);
        pic_save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (currPosition >= 0 && currPosition < newsList.size()) {
                    PicsDetail n = newsList.get(currPosition);

                    String picUrl = n.getPicUrl();
                    String yourTitle = n.getTitle();

                    BitmapUtils.savePic(PicViewActivity.this, picUrl, yourTitle);
                }
            }
        });

        getPic();
    }

    private void getPic() {
        if(newsList.size() > 0) {
            pager.setOffscreenPageLimit(newsList.size());
            for(int i = 0; i < this.newsList.size(); i++) {
                PicsDetail nc = newsList.get(i);
                fragments.add(PicDetailFragment.newInstance(i, nc.getPicUrl(), true));
            }

            pagerAdapter = new PicDetailAdapter(getSupportFragmentManager(),
                    fragments,
                    PicViewActivity.this);
            pager.setAdapter(pagerAdapter);
            pager.setCurrentItem(offset);
            setPicDetail(offset);

            pager.setOnPageChangeListener(new MyOnPageChangeListener());
        }
    }

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        public void onPageSelected(int position) {
            //选中后

            setPicDetail(position);
        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        public void onPageScrollStateChanged(int arg0) {
        }
    }

    private void setPicDetail(int position) {
        currPosition = position;
        PicsDetail n = newsList.get(position);

        String tt = String.valueOf(position + 1);
        tt += "/";
        tt += String.valueOf(newsList.size());

        pic_size.setText(tt);
    }

    public void setInterface() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
