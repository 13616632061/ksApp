package com.ui.ks;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.base.BaseActivity;
import com.ui.entity.GetOpenOrder;
import com.ui.entity.OrderGoods;
import com.ui.util.CustomRequest;
import com.ui.util.SetEditTextInput;
import com.ui.util.SysUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 现金记账
 */
public class CashChargeActivity extends BaseActivity implements TextWatcher, View.OnClickListener {
    private TextView keyboard_one,keyboard_two,keyboard_three,keyboard_four,keyboard_five,tv_keyboard_change;
    private TextView keyboard_six,keyboard_seven,keyboard_eight,keyboard_nine,tv_keyboard_zro,tv_keyboard_point;
    private LinearLayout layout_scancode,layout_cashcharge;
    private RelativeLayout layout_changemoney,keyboard_et_layout;
    private TextView tv_gopayfu,tv_gopayji,tv_gopaykuan,tv_gopayzhang,btn_set,tv_keyboard_change_cell,tv_notes;
    private EditText et_inputscancode;
    private LinearLayout keyboardcancell_layout,keyboard_add_layout;
    private RelativeLayout gopay_layout;
    private EditText et_keyoard,et_realitygetmoney;
    private ImageView iv_cursor,iv_cursor2;
    private TextView tv_realitygetmoney,et_changegetmoney;
    public  final static Double MIXMONEY=99999.99;//付款最大金额
    private int type_input=1;//1应收金额输入，2实收金额输入
    private int INTENT_NOTES=301;
    private int type;
    private String order_id;
    private double total_price;
    private ArrayList<GetOpenOrder> getOpenOrders_choose;
    private JSONArray jsonArray;
    private ArrayList<OrderGoods> goodsList;
    private String goodsname;
    private String goodsprice;
    private String goodsnum;
    //光标
    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 1) {
                iv_cursor.setVisibility(View.VISIBLE);
                iv_cursor2.setVisibility(View.VISIBLE);
            }
            if (msg.what == 2) {
                iv_cursor.setVisibility(View.GONE);
                iv_cursor2.setVisibility(View.GONE);
            }
            super.handleMessage(msg);
        };
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_charge);
        SysUtils.setupUI(CashChargeActivity.this,findViewById(R.id.activity_cash_charge));
        initToolbar(this);

        initView();

    }

    private void initView() {
        keyboard_one= (TextView) findViewById(R.id.keyboard_one);
        keyboard_two= (TextView) findViewById(R.id.keyboard_two);
        keyboard_three= (TextView) findViewById(R.id.keyboard_three);
        keyboard_four= (TextView) findViewById(R.id.keyboard_four);
        keyboard_five= (TextView) findViewById(R.id.keyboard_five);
        keyboard_six= (TextView) findViewById(R.id.keyboard_six);
        keyboard_seven= (TextView) findViewById(R.id.keyboard_seven);
        keyboard_eight= (TextView) findViewById(R.id.keyboard_eight);
        keyboard_nine= (TextView) findViewById(R.id.keyboard_nine);
        tv_keyboard_zro= (TextView) findViewById(R.id.tv_keyboard_zro);
        tv_keyboard_point= (TextView) findViewById(R.id.tv_keyboard_point);

        tv_keyboard_change= (TextView) findViewById(R.id.tv_keyboard_change);
        tv_keyboard_change_cell= (TextView) findViewById(R.id.tv_keyboard_change_cell);
        tv_gopayzhang= (TextView) findViewById(R.id.tv_gopayzhang);
        tv_gopaykuan= (TextView) findViewById(R.id.tv_gopaykuan);
        tv_gopayfu= (TextView) findViewById(R.id.tv_gopayfu);
        tv_gopayji= (TextView) findViewById(R.id.tv_gopayji);
        btn_set= (TextView) findViewById(R.id.btn_set);
        btn_set.setVisibility(View.VISIBLE);
        btn_set.setBackgroundResource(R.drawable.btn_corner_orange);
        btn_set.setText(getString(R.string.str27));
        tv_gopaykuan.setVisibility(View.GONE);
        tv_gopayfu.setVisibility(View.GONE);
        tv_gopayji.setVisibility(View.VISIBLE);
        tv_gopayzhang.setVisibility(View.VISIBLE);

        layout_cashcharge= (LinearLayout) findViewById(R.id.layout_cashcharge);
        layout_scancode= (LinearLayout) findViewById(R.id.layout_scancode);
        layout_scancode.setVisibility(View.GONE);
        layout_cashcharge.setVisibility(View.VISIBLE);

        layout_changemoney= (RelativeLayout) findViewById(R.id.layout_changemoney);
        keyboard_et_layout= (RelativeLayout) findViewById(R.id.keyboard_et_layout);
        tv_realitygetmoney= (TextView) findViewById(R.id.tv_realitygetmoney);
        et_changegetmoney= (TextView) findViewById(R.id.et_changegetmoney);
        tv_notes= (TextView) findViewById(R.id.tv_notes);
        et_realitygetmoney= (EditText) findViewById(R.id.et_realitygetmoney);

        et_inputscancode= (EditText) findViewById(R.id.et_inputscancode);
        keyboardcancell_layout= (LinearLayout) findViewById(R.id.keyboardcancell_layout);
        keyboard_add_layout= (LinearLayout) findViewById(R.id.keyboard_add_layout);
        gopay_layout= (RelativeLayout) findViewById(R.id.gopay_layout);
        et_keyoard= (EditText) findViewById(R.id.et_keyoard);
        iv_cursor= (ImageView) findViewById(R.id.iv_cursor);
        iv_cursor2= (ImageView) findViewById(R.id.iv_cursor2);

        et_inputscancode.setInputType(InputType.TYPE_NULL);//设置输入框不能点击
        et_keyoard.setInputType(InputType.TYPE_NULL);//设置输入框不能点击
        et_realitygetmoney.setInputType(InputType.TYPE_NULL);//设置输入框不能点击
        SetEditTextInput.setPricePoint(et_keyoard);
        SetEditTextInput.setEtPoint(et_inputscancode);
        SetEditTextInput.judgeNumber(et_realitygetmoney);
        SetEditTextInput.judgeNumber(et_changegetmoney);
        timer.schedule(task, 1000, 1000); // 1s后执行task,经过1s再次执行
        handler.sendEmptyMessageDelayed(3,200);

        et_inputscancode.addTextChangedListener(this);
        keyboard_add_layout.setOnClickListener(this);
        keyboardcancell_layout.setOnClickListener(this);
        gopay_layout.setOnClickListener(null);
        keyboard_one.setOnClickListener(this);
        keyboard_two.setOnClickListener(this);
        keyboard_three.setOnClickListener(this);
        keyboard_four.setOnClickListener(this);
        keyboard_five.setOnClickListener(this);
        keyboard_six.setOnClickListener(this);
        keyboard_seven.setOnClickListener(this);
        keyboard_eight.setOnClickListener(this);
        keyboard_nine.setOnClickListener(this);
        tv_keyboard_zro.setOnClickListener(this);
        tv_keyboard_point.setOnClickListener(this);
        tv_keyboard_change.setOnClickListener(this);
        tv_keyboard_change_cell.setOnClickListener(this);
        btn_set.setOnClickListener(this);
        /**
         * 实收金额输入监听
         */
        et_realitygetmoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String src_et_realitygetmoney=et_realitygetmoney.getText().toString().trim();
                String src_tv_realitygetmoney=tv_realitygetmoney.getText().toString().trim();
                Double double_et_realitygetmoney;
                Double double_tv_realitygetmoney;
                Double double_tv_changegetmoney;
                if(!TextUtils.isEmpty(src_tv_realitygetmoney)&&!TextUtils.isEmpty(src_et_realitygetmoney)) {
                    double_et_realitygetmoney = Double.parseDouble(src_et_realitygetmoney);
                    double_tv_realitygetmoney = Double.parseDouble(src_tv_realitygetmoney);
                    double_tv_changegetmoney = double_et_realitygetmoney - double_tv_realitygetmoney;
                    if(double_tv_changegetmoney<0){
                        et_changegetmoney.setText("0.00");
                    }else {
                        et_changegetmoney.setText(double_tv_changegetmoney + "");
                    }
                }else {
                    et_changegetmoney.setText("0.00");
                }

            }
        });

        Intent intent=getIntent();
         type=intent.getIntExtra("type",0);
        if(type==2){
            type_input=2;
//            order_id=intent.getStringExtra("order_id");
            getOpenOrders_choose=intent.getParcelableArrayListExtra("getOpenOrders_choose");
            total_price=Double.parseDouble(intent.getStringExtra("total_price"));
            System.out.println("total_price="+total_price);
            layout_changemoney.setVisibility(View.VISIBLE);
            tv_keyboard_change.setVisibility(View.GONE);
            tv_keyboard_change_cell.setVisibility(View.GONE);
            tv_notes.setVisibility(View.GONE);
            keyboard_et_layout.setVisibility(View.GONE);
            et_keyoard.setVisibility(View.GONE);
            et_realitygetmoney .setText("");
            tv_realitygetmoney.setText(total_price+"");
            et_inputscancode.setText(total_price+"");
            String inputscancode=et_inputscancode.getText().toString().trim();

            goodsList=new ArrayList<>();
            for(int i=0;i<getOpenOrders_choose.size();i++){
                boolean ishas=true;
                for(int j=0;j<getOpenOrders_choose.get(i).getGetOpenOrder_infos().size();j++){
                    goodsname= getOpenOrders_choose.get(i).getGetOpenOrder_infos().get(j).getName();
                    goodsnum= getOpenOrders_choose.get(i).getGetOpenOrder_infos().get(j).getNum();
                    goodsprice= getOpenOrders_choose.get(i).getGetOpenOrder_infos().get(j).getPrice();
                    for(int z=0;z<goodsList.size();z++){
                        if(goodsList.get(z).getName().equals(goodsname)){
                            goodsList.get(z).setQuantity(goodsList.get(z).getQuantity()+Integer.parseInt(goodsnum));
                            ishas=false;
                        }
                    }
                    if(ishas){
                        goodsList.add(new OrderGoods(Integer.parseInt(goodsnum),goodsname,Double.parseDouble(goodsprice)));
                    }
                }
            }

        }else {
            layout_changemoney.setVisibility(View.GONE);
            tv_keyboard_change.setVisibility(View.VISIBLE);
            tv_notes.setVisibility(View.VISIBLE);
            keyboard_et_layout.setVisibility(View.VISIBLE);
            et_keyoard.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 光标定时器
     */
    Timer timer = new Timer();
    TimerTask task = new TimerTask() {

        @Override
        public void run() {
            // 需要做的事:发送消息
            android.os.Message message1 = new android.os.Message();
            message1.what = 1;
            handler.sendMessage(message1);
            android.os.Message message2 = new android.os.Message();
            message2.what = 2;
            handler.sendMessageDelayed(message2,500);
        }
    };

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

    }

    @Override
    public void afterTextChanged(Editable s) {
            if(!TextUtils.isEmpty(et_inputscancode.getText().toString().trim())&&getSumstr(et_inputscancode.getText().toString())>0){
                gopay_layout.setClickable(true);
                gopay_layout.setOnClickListener(this);
                gopay_layout.setBackgroundResource(R.drawable.selector_paycode_btn);


            }else {
                gopay_layout.setClickable(false);
                gopay_layout.setBackgroundColor(Color.parseColor("#ababab"));
            }
    }
/**
 * 输入提示
 */
    private boolean input_et_realitygetmoney(EditText editText){
        boolean isinput=true;
        String src_et_realitygetmoney=editText.getText().toString().trim();
        if(!TextUtils.isEmpty(src_et_realitygetmoney)) {
            if (Double.parseDouble(src_et_realitygetmoney) > MIXMONEY) {
//                Toast.makeText(CashChargeActivity.this, "实收金额不能大于100000元！", Toast.LENGTH_SHORT).show();
               isinput=false;
            }
        }
        return isinput;
    }
    /**
     * 点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.keyboard_one:
                if(type_input==1) {
                    String myString1 = et_keyoard.getText().toString();
                    myString1 += "1";
                    if (getSumstr(myString1) >= MIXMONEY) {
                        Toast.makeText(CashChargeActivity.this, "付款金额不能超过最大金额！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    et_keyoard.setText(myString1);
                        et_inputscancode.setText(getSumstr(myString1) + "");
                }else if(type_input == 2){
                   if(input_et_realitygetmoney(et_realitygetmoney)) {
                       et_realitygetmoney.setText(et_realitygetmoney.getText().toString().trim()+1);
                   }
                }
                break;
            case R.id.keyboard_two:
                if(type_input==1) {
                    String myString2 = et_keyoard.getText().toString();
                    myString2 += "2";
                    if (getSumstr(myString2) >= MIXMONEY) {
                        Toast.makeText(CashChargeActivity.this, "付款金额不能超过最大金额！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    et_keyoard.setText(myString2);
                    et_inputscancode.setText(getSumstr(myString2) + "");
                }else if(type_input == 2){
                    if(input_et_realitygetmoney(et_realitygetmoney)) {
                        et_realitygetmoney.setText(et_realitygetmoney.getText().toString().trim()+2);
                    }
                }
                break;
            case R.id.keyboard_three:
                if(type_input==1) {
                    String myString3=et_keyoard.getText().toString();
                myString3+="3";
                if(getSumstr(myString3)>=MIXMONEY){
                    Toast.makeText(CashChargeActivity.this,"付款金额不能超过最大金额！",Toast.LENGTH_SHORT).show();
                    return;
                }
                et_keyoard.setText(myString3);
                    et_inputscancode.setText(getSumstr(myString3)+"");
                }else if(type_input == 2){
                    input_et_realitygetmoney(et_realitygetmoney);
                    et_realitygetmoney.setText(et_realitygetmoney.getText().toString().trim()+3);
                }
                break;
            case R.id.keyboard_four:
                if(type_input==1) {
                String myString4=et_keyoard.getText().toString();
                myString4+="4";
                if(getSumstr(myString4)>=MIXMONEY){
                    Toast.makeText(CashChargeActivity.this,"付款金额不能超过最大金额！",Toast.LENGTH_SHORT).show();
                    return;
                }
                et_keyoard.setText(myString4);
                    et_inputscancode.setText(getSumstr(myString4)+"");
                }else if(type_input == 2){
                    if(input_et_realitygetmoney(et_realitygetmoney)) {
                        et_realitygetmoney.setText(et_realitygetmoney.getText().toString().trim()+4);
                    }
                }
                break;
            case R.id.keyboard_five:
                if(type_input==1) {
                String myString5=et_keyoard.getText().toString();
                myString5+="5";
                if(getSumstr(myString5)>=MIXMONEY){
                    Toast.makeText(CashChargeActivity.this,"付款金额不能超过最大金额！",Toast.LENGTH_SHORT).show();
                    return;
                }
                et_keyoard.setText(myString5);
                    et_inputscancode.setText(getSumstr(myString5)+"");
                }else if(type_input == 2){
                    if(input_et_realitygetmoney(et_realitygetmoney)) {
                        et_realitygetmoney.setText(et_realitygetmoney.getText().toString().trim()+5);
                    }
                }
                break;
            case R.id.keyboard_six:
                if(type_input==1) {
                String myString6=et_keyoard.getText().toString();
                myString6+="6";
                if(getSumstr(myString6)>=MIXMONEY){
                    Toast.makeText(CashChargeActivity.this,"付款金额不能超过最大金额！",Toast.LENGTH_SHORT).show();
                    return;
                }
                et_keyoard.setText(myString6);
                    et_inputscancode.setText(getSumstr(myString6)+"");
                }else if(type_input == 2){
                    if(input_et_realitygetmoney(et_realitygetmoney)) {
                        et_realitygetmoney.setText(et_realitygetmoney.getText().toString().trim()+6);
                    }
                }
                break;
            case R.id.keyboard_seven:
                if(type_input==1) {
                String myString7=et_keyoard.getText().toString();
                myString7+="7";
                if(getSumstr(myString7)>=MIXMONEY){
                    Toast.makeText(CashChargeActivity.this,"付款金额不能超过最大金额！",Toast.LENGTH_SHORT).show();
                    return;
                }
                et_keyoard.setText(myString7);
                et_inputscancode.setText(getSumstr(myString7)+"");
                }else if(type_input == 2){
                    if(input_et_realitygetmoney(et_realitygetmoney)) {
                        et_realitygetmoney.setText(et_realitygetmoney.getText().toString().trim()+7);
                    }
                }
                break;
            case R.id.keyboard_eight:
                if(type_input==1) {
                String myString8=et_keyoard.getText().toString();
                myString8+="8";
                if(getSumstr(myString8)>=MIXMONEY){
                    Toast.makeText(CashChargeActivity.this,"付款金额不能超过最大金额！",Toast.LENGTH_SHORT).show();
                    return;
                }
                et_keyoard.setText(myString8);
                et_inputscancode.setText(getSumstr(myString8)+"");
                }else if(type_input == 2){
                    if(input_et_realitygetmoney(et_realitygetmoney)) {
                        et_realitygetmoney.setText(et_realitygetmoney.getText().toString().trim()+8);
                    }
                }
                break;
            case R.id.keyboard_nine:
                if(type_input==1) {
                String myString9=et_keyoard.getText().toString();
                myString9+="9";
                if(getSumstr(myString9)>=MIXMONEY){
                    Toast.makeText(CashChargeActivity.this,"付款金额不能超过最大金额！",Toast.LENGTH_SHORT).show();
                    return;
                }
                et_keyoard.setText( myString9);
                et_inputscancode.setText(getSumstr(myString9)+"");
                }else if(type_input == 2){
                    if(input_et_realitygetmoney(et_realitygetmoney)) {
                        et_realitygetmoney.setText(et_realitygetmoney.getText().toString().trim()+9);
                    };
                }
                break;
            case R.id.tv_keyboard_zro:
                if(type_input==1) {
                String myString0=et_keyoard.getText().toString();
                myString0+="0";
                if(getSumstr(myString0)>=MIXMONEY){
                    Toast.makeText(CashChargeActivity.this,"付款金额不能超过最大金额！",Toast.LENGTH_SHORT).show();
                    return;
                }
                et_keyoard.setText(myString0);
                et_inputscancode.setText(getSumstr(myString0)+"");
                }else if(type_input == 2&&(et_realitygetmoney.getText().toString().trim())!="00"){
                    if(input_et_realitygetmoney(et_realitygetmoney)) {
                        et_realitygetmoney.setText(et_realitygetmoney.getText().toString().trim()+0);
                    }
                }
                break;
            case R.id.tv_keyboard_point:
                if(type_input==1) {
                String myStringpoint=et_keyoard.getText().toString();
                if(myStringpoint.length()<1){
                    return;
                }
                if(myStringpoint .substring(myStringpoint .length()-1).equals("+")){
                    et_keyoard.setText(myStringpoint);
                }
//                else {
//                    myStringpoint += ".";
//                    et_keyoard.setText(myStringpoint);
//                }
                    int po_add=myStringpoint.lastIndexOf("+");
                    boolean istrue1=true;
                    if(po_add>0) {
                        String src_poadd=myStringpoint.substring(po_add,myStringpoint.length());
                        for (int i = 1; i <= src_poadd.length(); i++) {
                            if ((src_poadd.substring(src_poadd.length() - i, src_poadd.length() - i + 1)).equals(".")) {
                                istrue1= false;
                            }
                        }
                        if (istrue1) {
                            myStringpoint+= ".";
                        }
                        et_keyoard.setText(myStringpoint);
                    }else {
                        String str_et_realitygetmoney=et_keyoard.getText().toString().trim();
                        boolean istrue2=true;
                        for(int i=1;i<=str_et_realitygetmoney.length();i++){
                            if((str_et_realitygetmoney.substring(str_et_realitygetmoney.length()-i,str_et_realitygetmoney.length()-i+1)).equals(".")){
                                istrue2=false;
                            }
                        }
                        if(istrue2){
                            str_et_realitygetmoney += ".";
                            et_keyoard.setText(str_et_realitygetmoney);
                        }else {
                            et_keyoard.setText(str_et_realitygetmoney);
                        }
                    }
                }else if(type_input == 2&&(et_realitygetmoney.getText().toString().trim())!="."&&!TextUtils.isEmpty(et_realitygetmoney.getText().toString().trim())){
                    String str_et_realitygetmoney=et_realitygetmoney.getText().toString().trim();
                    boolean istrue=true;
                    for(int i=1;i<=str_et_realitygetmoney.length();i++){
                        if((str_et_realitygetmoney.substring(str_et_realitygetmoney.length()-i,str_et_realitygetmoney.length()-i+1)).equals(".")){
                            istrue=false;
                        }
                    }
                    if(istrue){
                        str_et_realitygetmoney += ".";
                        et_realitygetmoney.setText(str_et_realitygetmoney);
                    }else {
                        et_realitygetmoney.setText(str_et_realitygetmoney);
                    }
                }

                break;

            case R.id.keyboardcancell_layout:
                if(type_input==1) {
                String cancell_str=et_keyoard.getText().toString().trim();
                if(cancell_str.length()<=0){
                    et_inputscancode.setText("");
                    return;
                }
                et_keyoard.setText( cancell_str.substring(0,cancell_str.length()-1));
                if((getSumstr(cancell_str.substring(0,cancell_str.length()-1))+"")==""){
                    et_inputscancode.setText("");
                }else {
                    et_inputscancode.setText(getSumstr(cancell_str.substring(0,cancell_str.length()-1))+"");
                }
                }else if(type_input == 2){
                    int i=1;
                    String cancell_str2=et_realitygetmoney.getText().toString().trim();
                    if(i<cancell_str2.length()) {
                        if ((cancell_str2.substring(0, cancell_str2.length() - 1)) + "" == "") {
                            et_inputscancode.setText("");
                        } else {
                            et_realitygetmoney.setText(cancell_str2.substring(0, cancell_str2.length() - i) + "");
                        }
                        if(cancell_str2.length()==1){
                            et_realitygetmoney.setText("");
                        }
                        i++;
                    }else {
                        et_realitygetmoney.setText("");
                    }
                }
                break;
            case R.id.keyboard_add_layout:
                if(type_input==1) {
                    CharSequence myStringadd = et_keyoard.getText();
                    et_keyoard.setText(myStringadd);
                    String str = myStringadd.toString();
                    if (myStringadd.length() > 0) {
                        if ((str.substring(myStringadd.length() - 1)).equals("+") || (str.substring(myStringadd.length() - 1)).equals(".")) {
                            et_keyoard.setText(myStringadd);
                        } else {
                            str += "+";
                            et_keyoard.setText(str);
                            if (getSumstr(str) <= MIXMONEY) {
                                et_inputscancode.setText(getSumstr(str) + "");
                            } else {
                                Toast.makeText(CashChargeActivity.this, "付款金额不能超过最大金额！", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
                break;

            case R.id.gopay_layout:
                goToPay();
                break;
            case R.id.tv_keyboard_change_cell:
                layout_changemoney.setVisibility(View.GONE);
                keyboard_et_layout.setVisibility(View.VISIBLE);
                tv_keyboard_change_cell.setVisibility(View.GONE);
                tv_keyboard_change.setVisibility(View.VISIBLE);
                tv_notes.setVisibility(View.VISIBLE);
                type_input=1;
                et_realitygetmoney .setText("");
                break;
            case R.id.tv_keyboard_change:
                String inputscancode=et_inputscancode.getText().toString().trim();
                if(TextUtils.isEmpty(inputscancode)){
                    Toast.makeText(CashChargeActivity.this,"请输入记账金额！",Toast.LENGTH_SHORT).show();
                    return;
                }
                type_input=2;
                layout_changemoney.setVisibility(View.VISIBLE);
                tv_keyboard_change_cell.setVisibility(View.VISIBLE);
                tv_keyboard_change.setVisibility(View.GONE);
                tv_realitygetmoney.setText(inputscancode);
                tv_notes.setVisibility(View.GONE);
                keyboard_et_layout.setVisibility(View.GONE);
                break;
            case R.id.btn_set:
               String notes_src= tv_notes.getText().toString().trim();
                Intent intent_notes=new Intent(CashChargeActivity.this,CashChargeNotesActivity.class);
                intent_notes.putExtra("notes_src",notes_src);
                intent_notes.putExtra("type_input",type_input);
                startActivityForResult(intent_notes,INTENT_NOTES);
                break;
            default:
                break;
        }

    }

    /**
     * 记账
     */
    String amount_receivable="";
    String mark_text="";
    private void goToPay(){
        amount_receivable=et_inputscancode.getText().toString().trim();
        String receive_amount=et_realitygetmoney.getText().toString().trim();
        String add_change=et_changegetmoney.getText().toString().trim();
       mark_text=tv_notes.getText().toString().trim();

        Map<String,String>  map=new HashMap<>();
        if(type_input==2){
            double receive_amount_double;
            if(TextUtils.isEmpty(receive_amount)){
                Toast.makeText(CashChargeActivity.this,"实收金额不能为空！",Toast.LENGTH_SHORT).show();
                return;
            }else {
               receive_amount_double=Double.parseDouble(receive_amount);
                if(receive_amount_double<Double.parseDouble(amount_receivable)){
                    Toast.makeText(CashChargeActivity.this,"实收金额小于应收金额！",Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            if(getOpenOrders_choose!=null) {
                ArrayList<Map<String, String>> mapArrayList = new ArrayList<>();
                for (int i = 0; i < getOpenOrders_choose.size(); i++) {
                    Map<String, String> map1 = new HashMap<>();
                    map1.put("order_id", getOpenOrders_choose.get(i).getOrder_id());
                    map1.put("price", getOpenOrders_choose.get(i).getPrice() + "");
                    mapArrayList.add(map1);
                }
                map.put("map", mapArrayList.toString());
                JSONObject jsonObject = new JSONObject(map);
                try {
                    String map_str = jsonObject.getString("map");
                    jsonArray = new JSONArray(map_str);
                    map.put("map", jsonArray + "");//转化为json数组的字符串
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            map.put("receive_amount",receive_amount);//进入找零界面，实收金额
        }else if(type_input==1){
            map.put("receive_amount",amount_receivable);//不进入找零界面，实收金额
        }
        map.put("amount_receivable",amount_receivable);//应收金额
        map.put("add_change",add_change);//找零
        map.put("mark_text",mark_text);//备注
        System.out.println("map=="+map);
        CustomRequest r=new CustomRequest(Request.Method.POST, SysUtils.getSellerServiceUrl("cashPay"),map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                hideLoading();

                try {
                    JSONObject ret = SysUtils.didResponse(jsonObject);
                    System.out.println("现金记账ret="+ret);
                    String status=ret.getString("status");
                    String message=ret.getString("message");
                    if(!status.equals("200")){
                        SysUtils.showError(message);
                    }else {
                        JSONObject data=ret.getJSONObject("data");
                        String order_id=data.getString("order_id");
                        String sellername=data.getString("sellername");
                        String payed_time=data.getString("payed_time");
                        if(type==2){
                            Intent intent = new Intent(CashChargeActivity.this, PayOpenOrderSuccessActivity.class);
                            intent.putExtra("type", 3);
                            intent.putExtra("order_id", order_id);
                            intent.putExtra("payed_time", payed_time);
                            intent.putExtra("pay_status", 1+"");
                            intent.putParcelableArrayListExtra("goodsList",goodsList);
                            intent.putExtra("total_price", total_price+"");
                            startActivity(intent);
                            finish();
                        }else {
                            Intent intent = new Intent(CashChargeActivity.this, CashChargeSuccessActivity.class);
                            intent.putExtra("amount_receivable", amount_receivable);
                            intent.putExtra("mark_text", mark_text);
                            intent.putExtra("order_id", order_id);
                            intent.putExtra("sellername", sellername);
                            intent.putExtra("payed_time", payed_time);
                            startActivity(intent);
                            finish();
                            tv_notes.setText("");
                            et_inputscancode.setText("");
                            et_inputscancode.setText("");
                            et_keyoard.setText("");
                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideLoading();
                SysUtils.showNetworkError();

            }
        });
        executeRequest(r);
        showLoading(CashChargeActivity.this);
    }
    /**
     * 带结果返回
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==INTENT_NOTES&&resultCode==302){
            String notes=data.getStringExtra("cash_charge_notes_src");
            int type_input_result=data.getIntExtra("type_input",1);
            if(type_input_result==2){
                tv_notes.setVisibility(View.GONE);
            }else {
                tv_notes.setVisibility(View.VISIBLE);
            }
            tv_notes.setText(notes);
        }
    }

    /**
     * 总金额
     * @param str
     * @return
     */
    private double getSumstr(String str) {
        String s = new String(str);
        String a[] = s.split("[+]");
        double res = 0;
        String s11 = "";
        for (int i = 0; i < a.length; i++) {
            if (!TextUtils.isEmpty(a[i])){
                double yy = Double.parseDouble(a[i]);
                CharSequence tt = (yy + "");
                if (tt.toString().contains(".")) {
                    if (tt.length() - 1 - tt.toString().indexOf(".") > 2) {
                        tt = tt.toString().subSequence(0,
                                tt.toString().indexOf(".") + 3);
                    }
                }
                res += yy;
            }
        }
        return res;

    }
}
