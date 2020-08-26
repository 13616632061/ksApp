package com.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.MyApplication.KsApplication;
import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.android.arouter.launcher.ARouter;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.api.ApiConstant;
import com.base.BaseFragment;
import com.constant.RouterPath;
import com.material.widget.PaperButton;
import com.ui.global.Global;
import com.ui.ks.AddressActivity;
import com.ui.ks.EditBnakCardActivity;
import com.ui.ks.GetMoneyActivity;
import com.ui.ks.LoginActivity;
import com.ui.ks.MsgActivity;
import com.ui.ks.PrintActivity;
import com.ui.ks.ProfileEntryActivity;
import com.ui.ks.ProfilePasswordActivity;
import com.ui.ks.R;
import com.ui.ks.SetActivity;
import com.ui.languageSet.LanguageSetActivity;
import com.ui.update.UpdateAsyncTask;
import com.ui.util.CustomRequest;
import com.ui.util.LoginUtils;
import com.ui.util.SysUtils;

import org.json.JSONObject;

import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ProfileFragment extends BaseFragment {


    @BindView(R.id.textView2)
    TextView textView2;
    Unbinder unbinder;
    private boolean isLoading = false;
    private ImageView iv_profilefragment_back;
    private ImageView iv_profilefragment_refresh;
    private int isgetmoney;//1表示有提现权限，0表示没有提现权限
    private String mainseller;
    private boolean iscleck;
    TextView set_version_text;
    private String versionName;


    public static MainFragment newInstance() {
        MainFragment f = new MainFragment();

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);


        //余额中心
        LinearLayout linearLayout2 = (LinearLayout) view.findViewById(R.id.linearLayout2);
//        linearLayout2.setVisibility(View.VISIBLE);
        iscleck = false;
        linearLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                SysUtils.startAct(getActivity(), new MoneyActivity());
                iscleck = true;
                getData();
            }
        });

        //账号信息
        LinearLayout linearLayout3 = (LinearLayout) view.findViewById(R.id.linearLayout3);
        linearLayout3.setVisibility(View.VISIBLE);
        linearLayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SysUtils.startAct(getActivity(), new ProfileEntryActivity());
            }
        });

        //修改密码
        LinearLayout linearLayout8 = (LinearLayout) view.findViewById(R.id.linearLayout8);
        linearLayout8.setVisibility(View.VISIBLE);
        linearLayout8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SysUtils.startAct(getActivity(), new ProfilePasswordActivity());
            }
        });

        //商户设置
        LinearLayout linearLayout1 = (LinearLayout) view.findViewById(R.id.linearLayout1);
        linearLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SysUtils.startAct(getActivity(), new SetActivity());
            }
        });

        //发货地址  外卖设置
        LinearLayout linearLayout5 = (LinearLayout) view.findViewById(R.id.linearLayout5);
        linearLayout5.setVisibility(View.VISIBLE);
        linearLayout5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SysUtils.startAct(getActivity(), new AddressActivity());
            }
        });

        //消息推送
        LinearLayout linearLayout6 = (LinearLayout) view.findViewById(R.id.linearLayout6);
        linearLayout6.setVisibility(View.VISIBLE);
        linearLayout6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SysUtils.startAct(getActivity(), new MsgActivity());
            }
        });

        LinearLayout linearlayout01 = (LinearLayout) view.findViewById(R.id.linearlayout01);
        linearlayout01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SysUtils.startAct(getActivity(), new PrintActivity());
            }
        });
        //修改银行卡
        LinearLayout linearLayout_editcard = (LinearLayout) view.findViewById(R.id.linearLayout_editcard);
        linearLayout_editcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SysUtils.startAct(getActivity(), new EditBnakCardActivity());
            }
        });


        versionName = SysUtils.getAppVersionName(getContext());

        View set_update = (View) view.findViewById(R.id.set_update_item);
        set_version_text = (TextView) set_update.findViewById(R.id.ll_set_hint_text);
        SysUtils.setLine(set_update, Global.SET_CELLUP, getString(R.string.system_upgrade), 0, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkVersion();
            }
        });

        //联系客服
        View set_kefu = (View) view.findViewById(R.id.set_kefu_item);
        TextView set_kefu_text = (TextView) set_kefu.findViewById(R.id.ll_set_hint_text);
        set_kefu_text.setText(Global.SERVICE_PHONE);
        SysUtils.setLine(set_kefu, Global.SET_SINGLE_LINE, getString(R.string.contact_service), 0, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SysUtils.callTel(getActivity(), Global.SERVICE_PHONE);
            }
        });


        PaperButton button1 = (PaperButton) view.findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialDialog.Builder(getActivity())
                        .theme(SysUtils.getDialogTheme())
                        .content(getString(R.string.are_you_sure_sign_out))
                        .positiveText(getString(R.string.sure))
                        .negativeText(getString(R.string.cancel))
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                LoginUtils.logout(getActivity(), 2);
                                SysUtils.showSuccess(getString(R.string.signed_out));
                                startActivity(new Intent(getActivity(), LoginActivity.class));
                                getActivity().finish();
                            }
                        })
                        .show();
            }
        });


        if (LoginUtils.jurisdiction()) {
//        if (false) {
            //受限
            linearLayout2.setVisibility(View.GONE);
            linearLayout3.setVisibility(View.GONE);
            linearLayout5.setVisibility(View.GONE);
            linearLayout6.setVisibility(View.GONE);
            linearLayout8.setVisibility(View.GONE);

            linearLayout1.setBackgroundResource(R.drawable.selector_cell_left_blank);
        } else {
            getData();
        }

        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @OnClick({R.id.iv_profilefragment_refresh, R.id.iv_profilefragment_back, R.id.ly_language_set, R.id.ly_suggestions_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_profilefragment_refresh://刷新
                getData();
                break;
            case R.id.iv_profilefragment_back://返回
                getActivity().finish();
                break;
            case R.id.ly_language_set://语言设置
                Intent intent = new Intent(getActivity(), LanguageSetActivity.class);
                startActivity(intent);
                break;
            case R.id.ly_suggestions_back://建议反馈
                ARouter.getInstance().build(RouterPath.ACTIVITY_HTML).withString("url", ApiConstant.SUGGESTION_BACK).navigation();
                break;
        }
    }

    public void getData() {
        if (isLoading) {
            return;
        }

        isLoading = true;
        CustomRequest r = new CustomRequest(Request.Method.POST, SysUtils.getSellerServiceUrl("center"), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                hideLoading();
                isLoading = false;

                try {
                    JSONObject ret = SysUtils.didResponse(jsonObject);
                    System.out.println("center:" + ret);
                    String status = ret.getString("status");
                    String message = ret.getString("message");
                    JSONObject dataObject = ret.getJSONObject("data");

                    if (!status.equals("200")) {
                        SysUtils.showError(message);
                    } else {
                        double advance = dataObject.getDouble("advance");
                        isgetmoney = dataObject.getInt("ketixian");
                        mainseller = dataObject.getString("mainseller");
                        if (mainseller.equals("true")) {
                            if (isgetmoney == 1) {
                                isgetmoney = 0;
                            } else {
                                isgetmoney = 1;
                            }
                        }
                        if (isgetmoney == 1) {
                            textView2.setText(SysUtils.getMoneyFormat(advance));
                        } else if (isgetmoney == 0) {
                            textView2.setText("￥" + 0.00);
                        }
                        if (iscleck) {
                            if (isgetmoney == 1) {
                                Intent intentgetmoney = new Intent(getActivity(), GetMoneyActivity.class);
                                intentgetmoney.putExtra("isstore", 2);
                                startActivity(intentgetmoney);
                            } else {
                                SysUtils.showError("您没有开启提现功能！");
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                isLoading = false;
                hideLoading();
                SysUtils.showNetworkError();
            }
        });

        executeRequest(r);

        showLoading(getActivity());
    }

    public void checkVersion() {
        UpdateAsyncTask myAsyncTask = new UpdateAsyncTask(getContext(), true);
        myAsyncTask.execute();
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            getData();
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        iscleck = false;
        getData();
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter(Global.BROADCAST_REFRESH_PROFILE_ACTION));

        if (KsApplication.hasNewVersion) {
            set_version_text.setText(getString(R.string.new_version_found) + KsApplication.newVersionName);
        } else {
            set_version_text.setText("V" + versionName);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        try {
            getActivity().unregisterReceiver(broadcastReceiver);
        } catch (Exception e) {

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private class myTimerTask extends TimerTask {
        @Override
        public void run() {
            Message message = new Message();
            message.what = 2;
            myHandler.sendMessage(message);  //发送message
        }
    }

    Handler myHandler = new Handler() {
        // 接收到消息后处理
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    //UI操作
                    break;
                case 2:
                    //UI操作
                    getData();
                    break;
            }
            super.handleMessage(msg);
        }
    };
}

