package com.ui.ks;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.base.BaseActivity;
import com.material.widget.PaperButton;
import com.ui.util.CustomRequest;
import com.ui.util.SysUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 修改银行卡提交页面
 */
public class SubmitEditBnakCardActivity extends BaseActivity {

    private TextView tv_khr;
    private EditText et_depositbank;
    private EditText et_branchbank;
    private EditText et_cardnum;
    private EditText et_precardnum;
    private ImageView iv_bankremind;
    private PaperButton bt_editcard_submit;
    private String str_bankcard="";//原银行卡号

    private AlertDialog.Builder dialog;
   private AlertDialog   mAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_edit_bnak_card);

        SysUtils.setupUI(this, findViewById(R.id.activity_submit_edit_bnak_card));//点击空白处隐藏软键盘
        initToolbar(this);//封装的页面返回键，及返回监听

        initView();
        setListener();
    }
    private  void initView(){
        tv_khr=(TextView)findViewById(R.id.tv_khr);
        et_depositbank=(EditText)findViewById(R.id.et_depositbank);
        et_branchbank=(EditText)findViewById(R.id.et_branchbank);
        et_cardnum=(EditText)findViewById(R.id.et_cardnum);
        et_precardnum=(EditText)findViewById(R.id.et_precardnum);
        bt_editcard_submit=(PaperButton)findViewById(R.id.bt_editcard_submit);
        iv_bankremind=(ImageView) findViewById(R.id.iv_bankremind);


        Intent intent=getIntent();
        String str_khr=intent.getStringExtra("khr");
        str_bankcard=intent.getStringExtra("bankcard");
        tv_khr.setText(str_khr);
    }
    private void setListener(){
        //提示弹窗
        iv_bankremind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               dialog=new AlertDialog.Builder(SubmitEditBnakCardActivity.this);
                View view=View.inflate(SubmitEditBnakCardActivity.this, R.layout.editbank_dialog,null);
                view.findViewById(R.id.tv_known).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    mAlertDialog.dismiss();
                    }
                });
                    mAlertDialog =dialog.setView(view).show();
                mAlertDialog.show();
            }
        });
        //修改银行卡信息保存
        bt_editcard_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(tv_khr.getText().toString().trim())){
                    SysUtils.showError("开户人不能为空!");
                    return;
                }
                if(TextUtils.isEmpty(et_depositbank.getText().toString().trim())){
                    SysUtils.showError("开户银行不能为空!");
                    return;
                }
                if(TextUtils.isEmpty(et_branchbank.getText().toString().trim())){
                    SysUtils.showError("开户支行不能为空!");
                    return;
                }
                if(TextUtils.isEmpty(et_cardnum.getText().toString().trim())){
                    SysUtils.showError("卡号不能为空!");
                    return;
                }
                if(TextUtils.isEmpty(et_precardnum.getText().toString().trim())){
                    SysUtils.showError("原卡号不能为空!");
                    return;
                }
                if(!et_precardnum.getText().toString().trim().equals(str_bankcard)){
                    SysUtils.showError("原卡号输入不正确!");
                    return;
                }
                Map<String,String> map = new HashMap<String,String>();
                map.put("bankcard",et_cardnum.getText().toString().trim() );
                map.put("acbank",et_depositbank.getText().toString().trim() );
                map.put("branchbank", et_branchbank.getText().toString().trim() );
                CustomRequest r = new CustomRequest(Request.Method.POST, SysUtils.editBankCardServiceUrl("bank_edit"), map, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            hideLoading();
                            JSONObject ret = SysUtils.didResponse(jsonObject);
                            String status = ret.getString("status");
                            String message = ret.getString("message");

                            if (!status.equals("200")) {
                                SysUtils.showError(message);
                            } else {
                                SysUtils.showSuccess("银行卡信息修改成功");
                                finish();
                            }
                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        hideLoading();
                        SysUtils.showNetworkError();
                    }
                });

                executeRequest(r);

                showLoading(SubmitEditBnakCardActivity.this, getString(R.string.str92));
            }
        });
    }

}
