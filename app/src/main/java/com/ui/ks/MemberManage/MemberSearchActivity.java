package com.ui.ks.MemberManage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bigkoo.convenientbanner.utils.ScreenUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.constant.RouterPath;
import com.library.base.mvp.BaseActivity;
import com.library.weight.DividerDecoration;
import com.ui.adapter.MemberManageAdapter;
import com.ui.entity.Member;
import com.ui.ks.MemberDetailsActivity;
import com.ui.ks.MemberManage.contract.MemberSearchContract;
import com.ui.ks.MemberManage.presenter.MemberSearchPresenter;
import com.ui.ks.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * @Description:会员搜索
 * @Author:lyf
 * @Date: 2020/9/20
 */
@Route(path = RouterPath.ACTIVITY_SEARCH_MEMBER)
public class MemberSearchActivity extends BaseActivity implements MemberSearchContract.View {


    @BindView(R.id.rb_member_name)
    RadioButton rbMemberName;
    @BindView(R.id.rb_member_phone)
    RadioButton rbMemberPhone;
    @BindView(R.id.rg_btn)
    RadioGroup rgBtn;
    @BindView(R.id.ed_content)
    EditText edContent;
    @BindView(R.id.list)
    RecyclerView list;

    private MemberSearchPresenter mPresenter;

    @Override
    public int getContentView() {
        return R.layout.activity_member_search;
    }

    @Override
    protected void initView() {
        initTabTitle(getResources().getString(R.string.str429), "");//会员搜索
        mPresenter = new MemberSearchPresenter(this);
        mPresenter.initAdapter();
        getSearchTypeCheckedChangeListener();
    }

    @OnClick({R.id.btn_save})
    public void setOnClick(View view) {
        switch (view.getId()) {
            case R.id.btn_save://确定
                mPresenter.memberSearch();
                break;
        }
    }

    /**
     * @Description:搜索内容
     * @Author:lyf
     * @Date: 2020/9/20
     */
    @Override
    public String getSearchContent() {
        return edContent.getText().toString().trim();
    }

    /**
     * @Description:搜索类型
     * @Author:lyf
     * @Date: 2020/9/20
     */
    @Override
    public void getSearchTypeCheckedChangeListener() {
        rgBtn.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_member_name://会员名
                        mPresenter.setType(0);
                        break;
                    case R.id.rb_member_phone://手机号码
                        mPresenter.setType(1);
                        break;
                }
            }
        });
    }

    @Override
    public MemberManageAdapter initAdapter() {
        MemberManageAdapter adapter = new MemberManageAdapter(R.layout.itme_member_manage, mPresenter.getData());
        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.addItemDecoration(new DividerDecoration(this, LinearLayoutManager.VERTICAL, getResources().getColor(R.color.gray_bg), ScreenUtil.dip2px(this, 1), 0, 0));
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                toGoMemberEdidPage(mPresenter.getData().get(position));
            }
        });
        return adapter;
    }
    /**
    *@Description:跳转会员编辑
    *@Author:lyf
    *@Date: 2020/9/20
    */
    @Override
    public void toGoMemberEdidPage(Member.ResponseBean.DataBean.InfoBean memberBean) {
        ARouter.getInstance().build(RouterPath.ACTIVITY_ADD_MEMBER)
                .withParcelable("memberBean",memberBean)
                .navigation();
    }

}
