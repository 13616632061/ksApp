package com.ui.ks;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.base.BaseActivity;
import com.material.widget.PaperButton;
import com.ui.util.SysUtils;

public class PaySuccessActivity extends BaseActivity {

    private PaperButton btn_paysuccess;
    private TextView tv_paysuccess_money;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_success);
        initToolbar(this);
        initView();
    }

    private void initView() {
        tv_paysuccess_money= (TextView) findViewById(R.id.tv_paysuccess_money);
        btn_paysuccess= (PaperButton) findViewById(R.id.btn_paysuccess);



        btn_paysuccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SysUtils.startAct(PaySuccessActivity.this, new ShopActivity());
                finish();
            }
        });
    }
}
