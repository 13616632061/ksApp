package com.base;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ui.intef.BaseFragmentInterface;
import com.ui.ks.R;
import com.ui.util.DialogUtils;
import com.ui.util.RequestManager;
import com.ui.util.SysUtils;

import butterknife.ButterKnife;

public class BaseFragment extends Fragment implements
        android.view.View.OnClickListener, BaseFragmentInterface {

    protected LayoutInflater mInflater;

    private Dialog progressDialog = null;

    public void showLoading(Context ctx, String msg) {
        progressDialog = DialogUtils.createLoadingDialog(ctx, msg);
        progressDialog.show();
    }

    public void showLoading(Context ctx) {
        showLoading(ctx, getString(R.string.str92));
    }

    public void hideLoading() {
        if(progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RequestManager.cancelAll(this);
    }

    protected void executeRequest(Request request) {
        RequestManager.addRequest(request, this);
    }
    protected Response.ErrorListener errorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                SysUtils.showNetworkError();
            }
        };
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.mInflater = inflater;
        View view = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    protected int getLayoutId() {
        return 0;
    }

    protected View inflateView(int resId) {
        return this.mInflater.inflate(resId, null);
    }

    public boolean onBackPressed() {
        return false;
    }

    @Override
    public void initView(View view) {

    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {

    }
}
