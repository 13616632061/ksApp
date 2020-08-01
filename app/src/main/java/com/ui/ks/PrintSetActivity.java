package com.ui.ks;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.base.BaseActivity;
import com.ui.util.PreferencesService;
import com.ui.util.SysUtils;

import java.util.Map;

public class PrintSetActivity extends BaseActivity implements View.OnClickListener {

    private ImageView iv_choose,iv_choose2;
    private Button btn_add,btn_cell,btn_add2,btn_cell2,btn_save;
    private TextView tv_num,tv_num2;
    private PreferencesService service;
    private int isprint=1;//2挂单自动打印，1手动打印
    private int isprint_success=1;//2结算自动打印，1手动打印
    private int num1;//挂单打印设置次数
    private int num2;//买单成功打印设置次数
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_set);
        SysUtils.setupUI(PrintSetActivity.this,findViewById(R.id.activity_print_set));
        initToolbar(this);

        initView();
    }

    private void initView() {
        iv_choose= (ImageView) findViewById(R.id.iv_choose);
        iv_choose2= (ImageView) findViewById(R.id.iv_choose2);
        btn_add= (Button) findViewById(R.id.btn_add);
        btn_cell= (Button) findViewById(R.id.btn_cell);
        btn_add2= (Button) findViewById(R.id.btn_add2);
        btn_cell2= (Button) findViewById(R.id.btn_cell2);
        btn_save= (Button) findViewById(R.id.btn_save);
        tv_num= (TextView) findViewById(R.id.tv_num);
        tv_num2= (TextView) findViewById(R.id.tv_num2);

        service=new PreferencesService(PrintSetActivity.this);
        //获取挂单是否自动状态
        Map<String, String> params_change1 = service.getPerferences_isprint();
        isprint=Integer.valueOf(params_change1.get("isprint"));
        if(isprint==1){
            iv_choose.setBackgroundResource(R.drawable.gray_gou);
        }else if(isprint==2){
            iv_choose.setBackgroundResource(R.drawable.green_gou);
        }
        //获取买单成功是否自动打印状态
        Map<String, String> params_change2 = service.getPerferences_isprint_success();
        isprint_success=Integer.valueOf(params_change2.get("isprint_success"));
        if(isprint_success==1){
            iv_choose2.setBackgroundResource(R.drawable.gray_gou);
        }else if(isprint_success==2){
            iv_choose2.setBackgroundResource(R.drawable.green_gou);
        }
        //获取打印设置次数
        Map<String, String> print_num=service.getPerferences_print_num();
        num1=Integer.valueOf(print_num.get("num1"));
        num2=Integer.valueOf(print_num.get("num2"));
        tv_num.setText(num1+"");
        tv_num2.setText(num2+"");

        iv_choose.setOnClickListener(this);
        iv_choose2.setOnClickListener(this);
        btn_add.setOnClickListener(this);
        btn_cell.setOnClickListener(this);
        btn_add2.setOnClickListener(this);
        btn_cell2.setOnClickListener(this);
        btn_save.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_choose:
                if(isprint==1){
                    isprint=2;
                    iv_choose.setBackgroundResource(R.drawable.green_gou);
                }else if(isprint==2){
                    isprint=1;
                    iv_choose.setBackgroundResource(R.drawable.gray_gou);
                }
                break;
            case R.id.iv_choose2:
                if(isprint_success==1){
                    isprint_success=2;
                    iv_choose2.setBackgroundResource(R.drawable.green_gou);
                }else if(isprint_success==2){
                    isprint_success=1;
                    iv_choose2.setBackgroundResource(R.drawable.gray_gou);
                }
                break;
            case R.id. btn_add:
               String num_src= tv_num.getText().toString().trim();
                int num_src_num=Integer.parseInt(num_src);
                int src_num= ++num_src_num;
                System.out.println("src_num="+src_num);
                tv_num.setText(src_num+"");
                break;
            case R.id.btn_cell:
                String num_src_cell= tv_num.getText().toString().trim();
                int num_src_num_cell=Integer.parseInt(num_src_cell);
                int src_num_cell= --num_src_num_cell;
                if(src_num_cell>=0){
                    tv_num.setText(src_num_cell+"");
                }
                break;
            case R.id.btn_add2:
                String num_src2= tv_num2.getText().toString().trim();
                int num_src_num2=Integer.parseInt(num_src2);
                int src_num2= ++num_src_num2;
                if(src_num2>=0){
                    tv_num2.setText(src_num2+"");
                }
                break;
            case R.id.btn_cell2:
                String num_src_cell2= tv_num2.getText().toString().trim();
                int num_src_num_cell2=Integer.parseInt(num_src_cell2);
                int src_num_cell2= --num_src_num_cell2;
                tv_num2.setText(src_num_cell2+"");
                break;
            case R.id.btn_save:
                String num_src_save= tv_num.getText().toString().trim();
                String num_src2_save= tv_num2.getText().toString().trim();
                service.save_isprint(isprint);
                service.save_isprint_success(isprint_success);
                service.save_print_num(Integer.parseInt(num_src_save),Integer.parseInt(num_src2_save));
                Toast.makeText(PrintSetActivity.this,getString(R.string.str91),Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }
}
