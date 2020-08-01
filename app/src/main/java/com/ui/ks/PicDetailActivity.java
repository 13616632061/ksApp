package com.ui.ks;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.material.widget.CircularProgress;
import com.ui.adapter.PicDetailAdapter;
import com.ui.fragment.PicDetailFragment;
import com.ui.entity.PicsDetail;
import com.ui.util.AndroidBug5497Workaround;
import com.ui.util.BitmapUtils;
import com.ui.util.HackyViewPager;

import java.util.ArrayList;
import java.util.List;

public class PicDetailActivity extends ShareBaseActivity {
    CircularProgress loading_bar;
    String picId = "";
    String picTitle = "";

    private ArrayList<PicsDetail> newsList = new ArrayList<PicsDetail>();

    TextView pic_title;
    TextView pic_desc;
    TextView pic_size;


    HackyViewPager pager;
    PicDetailAdapter pagerAdapter;
    List<Fragment> fragments = new ArrayList<Fragment>();

    int currPosition = 0;

    RelativeLayout main_layout;
    RelativeLayout desc_layout;
    boolean hasInit = false;
    View toolbar_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, 0, false);
        setContentView(R.layout.activity_pic_detail);

        initToolbar(this, 2);

        pager = (HackyViewPager) findViewById(R.id.pager);

        loading_bar = (CircularProgress) this.findViewById(R.id.progress_large);
        main_layout = (RelativeLayout) this.findViewById(R.id.news_image_news_detail_main_layout);
        desc_layout = (RelativeLayout) this.findViewById(R.id.news_image_news_detail_buttom_introduce_layout);
        toolbar_layout = (View) this.findViewById(R.id.toolbar_layout);

        pic_title = (TextView) this.findViewById(R.id.news_image_news_detail_introduce_title_text);
        pic_desc = (TextView) this.findViewById(R.id.news_image_news_detail_introduce_content);
        pic_size = (TextView) this.findViewById(R.id.news_image_news_detail_introduce_size);

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            //图片id和图片标题
            if (bundle.containsKey("pic_id")) {
                picId = bundle.getString("pic_id");
            }

            if (bundle.containsKey("pic_title")) {
                picTitle = bundle.getString("pic_title");
                pic_title.setText(picTitle);
            }

            //初始位置
            if (bundle.containsKey("position")) {
                currPosition = bundle.getInt("position");
            }

            //图片列表
            if(bundle.containsKey("picList")) {
                newsList = bundle.getParcelableArrayList("picList");
            }
        }

        if(newsList.size() < 1) {
            finish();
        }

        pagerAdapter = new PicDetailAdapter(getSupportFragmentManager(),
                fragments,
                PicDetailActivity.this);
        pager.setAdapter(pagerAdapter);
        pager.setOnPageChangeListener(new MyOnPageChangeListener());

        AndroidBug5497Workaround.assistActivity(this);

        renderPicDetail();
    }

    private void renderPicDetail() {
        loading_bar.setVisibility(View.GONE);

        if(!hasInit) {
            hasInit = true;
        }

        getPic();
    }

    private void getPic() {
        if(newsList.size() > 0) {
            fragments.clear();
            pager.setOffscreenPageLimit(newsList.size());
            for(int i = 0; i < this.newsList.size(); i++) {
                PicsDetail nc = newsList.get(i);
                fragments.add(PicDetailFragment.newInstance(i, nc.getPicUrl(), false));
            }

            if(currPosition < 0) {
                currPosition = 0;
            } else if(currPosition >= newsList.size()) {
                currPosition = newsList.size() - 1;
            }

            pagerAdapter.notifyDataSetChanged();

            pager.setCurrentItem(currPosition);
            setPicDetail(currPosition);
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

        pic_desc.setText(n.getTitle());
    }

    public void setInterface() {
        if(desc_layout.isShown()) {
            desc_layout.setVisibility(View.GONE);
            toolbar_layout.setVisibility(View.GONE);
        } else {
            desc_layout.setVisibility(View.VISIBLE);
            toolbar_layout.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 保存图片
     */
    public void savePic() {
        if(currPosition >= 0 && currPosition < newsList.size()) {
            PicsDetail n = newsList.get(currPosition);

            String picUrl = n.getPicUrl();
            String yourTitle = n.getTitle();

            BitmapUtils.savePic(PicDetailActivity.this, picUrl, yourTitle);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pic_detail, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuId = item.getItemId();

        if(menuId == R.id.menu_main_save) {
            //保存图片
            savePic();
        } else if(menuId == R.id.menu_main_share) {
            //分享
            if(currPosition >= 0 && currPosition < newsList.size()) {
                PicsDetail n = newsList.get(currPosition);
                shareTitle = n.getTitle();
                sharePicUrl = n.getPicUrl();

                doShare(PicDetailActivity.this);
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
