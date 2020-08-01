package com.ui.ks;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.base.BaseActivity;
import com.ui.util.SysUtils;

public class CashChargeNotesActivity extends BaseActivity implements View.OnClickListener, TextWatcher {
    private EditText et_cash_charge_notes;
    private TextView tv_cash_charge_notes_num,btn_set;
    private Button btn_cash_charge_notes_save;
    private String notes_src;
    private int type_input;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_charge_notes);
        SysUtils.setupUI(CashChargeNotesActivity.this,findViewById(R.id.activity_cash_charge_notes));
        initToolbar(this);
        
        initView();
        Intent intent=getIntent();
        if(intent!=null){
            notes_src=intent.getStringExtra("notes_src");
            type_input=intent.getIntExtra("type_input",1);
            et_cash_charge_notes.setText(notes_src);
            et_cash_charge_notes.setSelection(notes_src.length());
        }

    }

    private void initView() {
        btn_set= (TextView) findViewById(R.id.btn_set);
        btn_set.setVisibility(View.VISIBLE);
        btn_set.setBackgroundResource(R.drawable.btn_corner_orange);
        btn_set.setText(getString(R.string.str42));

        et_cash_charge_notes= (EditText) findViewById(R.id.et_cash_charge_notes);
        tv_cash_charge_notes_num= (TextView) findViewById(R.id.tv_cash_charge_notes_num);
        btn_cash_charge_notes_save= (Button) findViewById(R.id.btn_cash_charge_notes_save);

        tv_cash_charge_notes_num.setOnClickListener(this);
        btn_cash_charge_notes_save.setOnClickListener(this);
        btn_set.setOnClickListener(this);
        et_cash_charge_notes.addTextChangedListener(this);


    }

    /**
     * 点击监听
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_set:
                et_cash_charge_notes.setText("");
                break;
            case R.id.btn_cash_charge_notes_save:
                String cash_charge_notes_src= et_cash_charge_notes.getText().toString().trim();
                Intent intent=new Intent();
                intent.putExtra("cash_charge_notes_src",cash_charge_notes_src);
                intent.putExtra("type_input",type_input);
                setResult(302,intent);
                finish();
                break;
        }

    }

    /**
     * 输入框变化监听
     * @param s
     * @param start
     * @param count
     * @param after
     */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String cash_charge_notes_src= et_cash_charge_notes.getText().toString().trim();
        if(cash_charge_notes_src.length()<51){
            tv_cash_charge_notes_num.setText(cash_charge_notes_src.length()+"/50");
        }else {
            et_cash_charge_notes.setText(cash_charge_notes_src.substring(0,50));
            et_cash_charge_notes.setSelection(cash_charge_notes_src.substring(0,50).length());
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

}
