package com.ui.weight;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ui.ks.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lyf on 2020/8/2.
 */

public class SortView extends FrameLayout implements View.OnClickListener {

    TextView tvText;
    ImageView ivUp;
    ImageView ivDown;
    private RelativeLayout layoutSort;
    private View view;
    private Context context;

    private int sortType = 0;//0默认无排序，1升序，2降序

    private SortListener sortListener;


    public SortView(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public SortView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public SortView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SortView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
        this.context = context;
        view = View.inflate(context, R.layout.sort_view_layout, null);
        initView();

        setDefautSort();

        addView(view);


    }

    private void initView() {
        layoutSort = view.findViewById(R.id.layout_sort);
        tvText = view.findViewById(R.id.tv_text);
        ivUp = view.findViewById(R.id.iv_up);
        ivDown = view.findViewById(R.id.iv_down);

        layoutSort.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (view.getId()) {
            case R.id.layout_sort:
                setSortType();
                break;
        }
    }


    private void setSortType() {
        switch (sortType) {
            case 1:
                setUpSort();
                break;
            case 2:
                setDownSort();
                break;
            default:
                setUpSort();
                break;

        }
    }

    /**
     * @Description:设置文本
     * @Author:lyf
     * @Date: 2020/8/2
     */
    public void setText(String text) {
        tvText.setText(text);
    }

    /**
     * @Description:升序
     * @Author:lyf
     * @Date: 2020/8/2
     */
    public void setUpSort() {
        sortType = 2;
        ivUp.setImageResource(R.drawable.horn_red_up);
        ivDown.setImageResource(R.drawable.horn_black_put);
        //升序监听
        if (sortListener != null) {
            sortListener.setUpSort(this);
        }
    }

    /**
     * @Description:降序
     * @Author:lyf
     * @Date: 2020/8/2
     */
    public void setDownSort() {
        sortType = 1;
        ivUp.setImageResource(R.drawable.horn_black_up);
        ivDown.setImageResource(R.drawable.horn_red_put);
        //降序监听
        if (sortListener != null) {
            sortListener.setDownSort(this);
        }
    }

    /**
     * @Description:默认
     * @Author:lyf
     * @Date: 2020/8/2
     */
    public void setDefautSort() {
        sortType = 0;
        ivUp.setImageResource(R.drawable.horn_black_up);
        ivDown.setImageResource(R.drawable.horn_black_put);
    }


    public interface SortListener {
        //升序
        void setUpSort(View view);

        //降序
        void setDownSort(View view);
    }

    public void setSortListener(SortListener sortListener) {
        this.sortListener = sortListener;
    }
}
