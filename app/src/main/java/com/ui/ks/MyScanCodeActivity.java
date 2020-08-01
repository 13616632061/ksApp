package com.ui.ks;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.MyApplication.KsApplication;
import com.afollestad.materialdialogs.MaterialDialog;
import com.base.BaseActivity;
import com.ui.entity.Order;
import com.ui.entity.OrderGoods;
import com.ui.util.BluetoothService;
import com.ui.util.PicFromPrintUtils;
import com.ui.util.PrintUtil;
import com.ui.util.QRCodeUtil;
import com.ui.util.SetEditTextInput;
import com.ui.util.SysUtils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 收款页面
 */

public class MyScanCodeActivity extends BaseActivity implements TextWatcher, View.OnClickListener {

    private EditText et_inputscancode;
    private LinearLayout keyboardcancell_layout, keyboard_add_layout;
    private RelativeLayout gopay_layout;
    private TextView keyboard_one, keyboard_two, keyboard_three, keyboard_four, keyboard_five;
    private TextView keyboard_six, keyboard_seven, keyboard_eight, keyboard_nine, keyboard_zro, keyboard_point;
    private TextView tv_scanmoney;
    private Button btn_do_printe, btn_continue_scancode;
    private PopupWindow popupWindow;
    private EditText et_keyoard;
    private ImageView iv_cursor, scanmoneysuccess;
    public final static Double MIXMONEY = 10000.00;//付款最大金额
    private int type;
    private String total_fee_double;
    //蓝牙服务
    BluetoothService mService = null;
    //表示是否连接上蓝牙打印机
    private boolean hasConnect = false;
    //尝试打开蓝牙
    private static final int REQUEST_ENABLE_BT = 2;
    private BluetoothAdapter mBluetoothAdapter = null;
    private Order order;
    private String order_id = "";
    private String sellername = "";
    private String pay_status = "";
    private String payed_time;
    public ArrayList<OrderGoods> goodsList;
    public TextView keyboard_balance;

    //光标
    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 1) {
                iv_cursor.setVisibility(View.VISIBLE);
            }
            if (msg.what == 2) {
                iv_cursor.setVisibility(View.GONE);
            }
            if (msg.what == 3) {
                Intent intent = getIntent();
                if (intent != null) {
                    type = intent.getIntExtra("type", 0);
                    if (type == 2) {
                        total_fee_double = intent.getStringExtra("total_fee_double");
                        order_id = intent.getStringExtra("order_id");
                        sellername = intent.getStringExtra("sellername");
                        pay_status = intent.getStringExtra("pay_status");
                        payed_time = intent.getStringExtra("payed_time");
                        scancodesuccess();
                    }
                }
            }
            super.handleMessage(msg);
        }

        ;
    };
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case BluetoothService.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                            hasConnect = true;

                            doPrint();
                            break;
                        case BluetoothService.STATE_CONNECTING:
                            break;
                        case BluetoothService.STATE_LISTEN:
                        case BluetoothService.STATE_NONE:
                            break;
                    }
                    break;
                case BluetoothService.MESSAGE_WRITE:
                    break;
                case BluetoothService.MESSAGE_READ:
                    break;
                case BluetoothService.MESSAGE_DEVICE_NAME:
                    break;
                case BluetoothService.MESSAGE_TOAST:
                    SysUtils.showError(msg.getData().getString(BluetoothService.TOAST));
                    break;
            }
        }

    };

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
            handler.sendMessageDelayed(message2, 500);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_scan_code);
        SysUtils.setupUI(this, findViewById(R.id.activity_my_scan_code));

        initToolbar(this);
        initView();


    }

    private void initView() {
        keyboard_one = (TextView) findViewById(R.id.keyboard_one);
        keyboard_two = (TextView) findViewById(R.id.keyboard_two);
        keyboard_three = (TextView) findViewById(R.id.keyboard_three);
        keyboard_four = (TextView) findViewById(R.id.keyboard_four);
        keyboard_five = (TextView) findViewById(R.id.keyboard_five);
        keyboard_six = (TextView) findViewById(R.id.keyboard_six);
        keyboard_seven = (TextView) findViewById(R.id.keyboard_seven);
        keyboard_eight = (TextView) findViewById(R.id.keyboard_eight);
        keyboard_nine = (TextView) findViewById(R.id.keyboard_nine);
        keyboard_zro = (TextView) findViewById(R.id.keyboard_zro);
        keyboard_point = (TextView) findViewById(R.id.keyboard_point);
        keyboard_balance = (TextView) findViewById(R.id.keyboard_balance);


        et_inputscancode = (EditText) findViewById(R.id.et_inputscancode);
        keyboardcancell_layout = (LinearLayout) findViewById(R.id.keyboardcancell_layout);
        keyboard_add_layout = (LinearLayout) findViewById(R.id.keyboard_add_layout);
        gopay_layout = (RelativeLayout) findViewById(R.id.gopay_layout);
        et_keyoard = (EditText) findViewById(R.id.et_keyoard);
        iv_cursor = (ImageView) findViewById(R.id.iv_cursor);


        et_inputscancode.setInputType(InputType.TYPE_NULL);//设置输入框不能点击
//        new KeyboardUtil(mActivity,mContext,et_inputscancode,et_keyoard).showKeyboard();//显示自定义键盘

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
        keyboard_zro.setOnClickListener(this);
        keyboard_point.setOnClickListener(this);
        keyboard_balance.setOnClickListener(this);

        SetEditTextInput.setPricePoint(et_keyoard);
        SetEditTextInput.setEtPoint(et_inputscancode);
        timer.schedule(task, 1000, 1000); // 1s后执行task,经过1s再次执行
        handler.sendEmptyMessageDelayed(3, 200);
        mService = new BluetoothService(this, mHandler);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            SysUtils.showError(getString(R.string.Bluetooth_not_available_unable_to_set_printer_parameters));
            finish();
        }
    }

    /**
     * 输入框的输入监听
     *
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
        if (!TextUtils.isEmpty(et_inputscancode.getText().toString().trim()) && getSumstr(et_inputscancode.getText().toString()) > 0) {
            gopay_layout.setClickable(true);
            gopay_layout.setOnClickListener(this);
            gopay_layout.setBackgroundResource(R.drawable.selector_paycode_btn);


        } else {
            gopay_layout.setClickable(false);
            gopay_layout.setBackgroundColor(Color.parseColor("#ababab"));
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.keyboard_one:
                String myString1 = et_keyoard.getText().toString();
                myString1 += "1";
                if (getSumstr(myString1) >= MIXMONEY) {
                    Toast.makeText(MyScanCodeActivity.this, getString(R.string.cannot_exceed_maximum_amount), Toast.LENGTH_SHORT).show();
                    return;
                }
                et_keyoard.setText(myString1);
                et_inputscancode.setText(getSumstr(myString1) + "");
                break;
            case R.id.keyboard_two:
                String myString2 = et_keyoard.getText().toString();
                myString2 += "2";
                if (getSumstr(myString2) >= MIXMONEY) {
                    Toast.makeText(MyScanCodeActivity.this, getString(R.string.cannot_exceed_maximum_amount), Toast.LENGTH_SHORT).show();
                    return;
                }
                et_keyoard.setText(myString2);
                et_inputscancode.setText(getSumstr(myString2) + "");
                break;
            case R.id.keyboard_three:
                String myString3 = et_keyoard.getText().toString();
                myString3 += "3";
                if (getSumstr(myString3) >= MIXMONEY) {
                    Toast.makeText(MyScanCodeActivity.this, getString(R.string.cannot_exceed_maximum_amount), Toast.LENGTH_SHORT).show();
                    return;
                }
                et_keyoard.setText(myString3);
                et_inputscancode.setText(getSumstr(myString3) + "");
                break;
            case R.id.keyboard_four:
                String myString4 = et_keyoard.getText().toString();
                myString4 += "4";
                if (getSumstr(myString4) >= MIXMONEY) {
                    Toast.makeText(MyScanCodeActivity.this, getString(R.string.cannot_exceed_maximum_amount), Toast.LENGTH_SHORT).show();
                    return;
                }
                et_keyoard.setText(myString4);
                et_inputscancode.setText(getSumstr(myString4) + "");
                break;
            case R.id.keyboard_five:
                String myString5 = et_keyoard.getText().toString();
                myString5 += "5";
                if (getSumstr(myString5) >= MIXMONEY) {
                    Toast.makeText(MyScanCodeActivity.this, getString(R.string.cannot_exceed_maximum_amount), Toast.LENGTH_SHORT).show();
                    return;
                }
                et_keyoard.setText(myString5);
                et_inputscancode.setText(getSumstr(myString5) + "");
                break;
            case R.id.keyboard_six:
                String myString6 = et_keyoard.getText().toString();
                myString6 += "6";
                if (getSumstr(myString6) >= MIXMONEY) {
                    Toast.makeText(MyScanCodeActivity.this, getString(R.string.cannot_exceed_maximum_amount), Toast.LENGTH_SHORT).show();
                    return;
                }
                et_keyoard.setText(myString6);
                et_inputscancode.setText(getSumstr(myString6) + "");
                break;
            case R.id.keyboard_seven:
                String myString7 = et_keyoard.getText().toString();
                myString7 += "7";
                if (getSumstr(myString7) >= MIXMONEY) {
                    Toast.makeText(MyScanCodeActivity.this, getString(R.string.cannot_exceed_maximum_amount), Toast.LENGTH_SHORT).show();
                    return;
                }
                et_keyoard.setText(myString7);
                et_inputscancode.setText(getSumstr(myString7) + "");
                break;
            case R.id.keyboard_eight:
                String myString8 = et_keyoard.getText().toString();
                myString8 += "8";
                if (getSumstr(myString8) >= MIXMONEY) {
                    Toast.makeText(MyScanCodeActivity.this, getString(R.string.cannot_exceed_maximum_amount), Toast.LENGTH_SHORT).show();
                    return;
                }
                et_keyoard.setText(myString8);
                et_inputscancode.setText(getSumstr(myString8) + "");
                break;
            case R.id.keyboard_nine:
                String myString9 = et_keyoard.getText().toString();
                myString9 += "9";
                if (getSumstr(myString9) >= MIXMONEY) {
                    Toast.makeText(MyScanCodeActivity.this, getString(R.string.cannot_exceed_maximum_amount), Toast.LENGTH_SHORT).show();
                    return;
                }

                et_keyoard.setText(myString9);
                et_inputscancode.setText(getSumstr(myString9) + "");
                break;
            case R.id.keyboard_zro:
                String myString0 = et_keyoard.getText().toString();
                myString0 += "0";
                if (getSumstr(myString0) >= MIXMONEY) {
                    Toast.makeText(MyScanCodeActivity.this, getString(R.string.cannot_exceed_maximum_amount), Toast.LENGTH_SHORT).show();
                    return;
                }
                et_keyoard.setText(myString0);
                et_inputscancode.setText(getSumstr(myString0) + "");
                break;
            case R.id.keyboard_point:
                String myStringpoint = et_keyoard.getText().toString();
                if (myStringpoint.length() < 1) {
                    return;
                }
                if (myStringpoint.substring(myStringpoint.length() - 1).equals("+")) {
                    et_keyoard.setText(myStringpoint);
                }
                int po_add = myStringpoint.lastIndexOf("+");
                boolean istrue1 = true;
                if (po_add > 0) {
                    String src_poadd = myStringpoint.substring(po_add, myStringpoint.length());
                    for (int i = 1; i <= src_poadd.length(); i++) {
                        if ((src_poadd.substring(src_poadd.length() - i, src_poadd.length() - i + 1)).equals(".")) {
                            istrue1 = false;
                        }
                    }
                    if (istrue1) {
                        myStringpoint += ".";
                    }
                    et_keyoard.setText(myStringpoint);
                } else {
                    String str_et_realitygetmoney = et_keyoard.getText().toString().trim();
                    boolean istrue2 = true;
                    for (int i = 1; i <= str_et_realitygetmoney.length(); i++) {
                        if ((str_et_realitygetmoney.substring(str_et_realitygetmoney.length() - i, str_et_realitygetmoney.length() - i + 1)).equals(".")) {
                            istrue2 = false;
                        }
                    }
                    if (istrue2) {
                        str_et_realitygetmoney += ".";
                        et_keyoard.setText(str_et_realitygetmoney);
                    } else {
                        et_keyoard.setText(str_et_realitygetmoney);
                    }
                }


                break;

            case R.id.keyboardcancell_layout:
                String cancell_str = et_keyoard.getText().toString().trim();
                if (cancell_str.length() <= 0) {
                    et_inputscancode.setText("");
                    return;
                }
                et_keyoard.setText(cancell_str.substring(0, cancell_str.length() - 1));
                if ((getSumstr(cancell_str.substring(0, cancell_str.length() - 1)) + "") == "") {
                    et_inputscancode.setText("");
                } else {
                    et_inputscancode.setText(getSumstr(cancell_str.substring(0, cancell_str.length() - 1)) + "");
                }

                break;
            case R.id.keyboard_add_layout:

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
                            Toast.makeText(MyScanCodeActivity.this, getString(R.string.cannot_exceed_maximum_amount), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                break;
            case R.id.keyboard_balance:
                //sdk>23,动态添加权限  会员付款码
                String total_fee_str1 = et_inputscancode.getText().toString().trim();
                if (TextUtils.isEmpty(total_fee_str1)) {
                    return;
                }
                Double total_fee_double1 = Double.parseDouble(total_fee_str1);
                int total_fee1 = (int) (total_fee_double1 * 100);
                Log.d("print", "打印出来的数据为" + total_fee1 + "打印出阿里的总金额为" + total_fee_double);
                if (Build.VERSION.SDK_INT >= 23) {
                    int checkCallPhonePermission = ContextCompat.checkSelfPermission(MyScanCodeActivity.this, android.Manifest.permission.CAMERA);
                    if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MyScanCodeActivity.this, new String[]{android.Manifest.permission.CAMERA}, 222);
                        return;
                    } else {
                        Intent intentzhifubao = new Intent(MyScanCodeActivity.this, MipcaActivityCapture.class);
                        intentzhifubao.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intentzhifubao.putExtra("total_fee", total_fee_double1 + "");
                        intentzhifubao.putExtra("total_fee_double", total_fee_double1 + "");
                        intentzhifubao.putExtra("pay_type", "micro");
                        intentzhifubao.putExtra("type", 4);
                        startActivity(intentzhifubao);
                        finish();
                    }
                } else {
                    Intent intentzhifubao = new Intent(MyScanCodeActivity.this, MipcaActivityCapture.class);
                    intentzhifubao.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intentzhifubao.putExtra("total_fee", total_fee_double1 + "");
                    intentzhifubao.putExtra("total_fee_double", total_fee_double1 + "");
                    intentzhifubao.putExtra("pay_type", "micro");
                    intentzhifubao.putExtra("type", 4);
                    startActivity(intentzhifubao);
                    finish();
                }
                break;
            case R.id.gopay_layout:
                //sdk>23,动态添加权限  会员付款码
                String total_fee_str = et_inputscancode.getText().toString().trim();
                Double total_fee_double = Double.parseDouble(total_fee_str);
                int total_fee = (int) (total_fee_double * 100);
                if (Build.VERSION.SDK_INT >= 23) {
                    int checkCallPhonePermission = ContextCompat.checkSelfPermission(MyScanCodeActivity.this, android.Manifest.permission.CAMERA);
                    if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MyScanCodeActivity.this, new String[]{android.Manifest.permission.CAMERA}, 222);
                        return;
                    } else {
                        Intent intentzhifubao = new Intent(MyScanCodeActivity.this, MipcaActivityCapture.class);
                        intentzhifubao.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intentzhifubao.putExtra("total_fee", total_fee + "");
                        intentzhifubao.putExtra("total_fee_double", total_fee_double + "");
                        intentzhifubao.putExtra("pay_type", "micro");
                        startActivity(intentzhifubao);
                        finish();
                    }
                } else {
                    Intent intentzhifubao = new Intent(MyScanCodeActivity.this, MipcaActivityCapture.class);
                    intentzhifubao.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intentzhifubao.putExtra("total_fee", total_fee + "");
                    intentzhifubao.putExtra("total_fee_double", total_fee_double + "");
                    intentzhifubao.putExtra("pay_type", "micro");
                    startActivity(intentzhifubao);
                    finish();
                }
                break;
            default:
                break;
        }

    }

    /**
     * 总金额
     *
     * @param str
     * @return
     */
    private double getSumstr(String str) {
        String s = new String(str);
        String a[] = s.split("[+]");
        double res = 0;
        String s11 = "";
        for (int i = 0; i < a.length; i++) {
            if (!TextUtils.isEmpty(a[i])) {
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

    private void scancodesuccess() {
        if (popupWindow == null) {
            View view = View.inflate(MyScanCodeActivity.this, R.layout.iv_popwindow_scancode, null);
            tv_scanmoney = (TextView) view.findViewById(R.id.tv_scanmoney);
            tv_scanmoney.setText("￥" + total_fee_double);
            btn_continue_scancode = (Button) view.findViewById(R.id.btn_continue_scancode);
            btn_do_printe = (Button) view.findViewById(R.id.btn_do_printe);
            scanmoneysuccess = (ImageView) view.findViewById(R.id.scanmoneysuccess);
            ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0);
            scaleAnimation.setDuration(1500);
            scaleAnimation.setFillAfter(true);
            RotateAnimation rotateAnimation = new RotateAnimation(0, 360);
            rotateAnimation.setFillAfter(true);
            rotateAnimation.setDuration(1500);
            TranslateAnimation translateAnimation = new TranslateAnimation(0, 100, 0, 100, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
            AnimationSet animationSet = new AnimationSet(true);
            animationSet.addAnimation(scaleAnimation);
            animationSet.addAnimation(rotateAnimation);
            scanmoneysuccess.setAnimation(animationSet);
            btn_continue_scancode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (popupWindow.isShowing()) {
                        popupWindow.dismiss();
                    }
                }
            });
            btn_do_printe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new MaterialDialog.Builder(MyScanCodeActivity.this)
                            .theme(SysUtils.getDialogTheme())
                            .content(getString(R.string.sure_small_ticket))
                            .positiveText(getString(R.string.sure))
                            .negativeText(getString(R.string.cancel))
                            .callback(new MaterialDialog.ButtonCallback() {
                                @Override
                                public void onPositive(MaterialDialog dialog) {
                                    startPrint();
                                }
                            })
                            .show();
                }
            });
            popupWindow = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            //实例化一个ColorDrawable颜色为半透明
            ColorDrawable mColorDrawable = new ColorDrawable(0xb0000000);
            //设置弹框的背景
            popupWindow.setBackgroundDrawable(mColorDrawable);
        }
        popupWindow.showAtLocation(findViewById(R.id.et_inputscancode), Gravity.CENTER, 0, 0);

    }

    //开始打印
    public void startPrint() {
        if (!hasConnect) {
            //还没有连接，先尝试连接
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            } else {
                //尝试连接
                try {
                    if (mService != null) {
//                        BluetoothDevice printDev = mService.getDevByMac(KsApplication.getString("printer_mac", ""));
                        BluetoothDevice printDev = mBluetoothAdapter.getRemoteDevice(KsApplication.getString("printer_mac", ""));

                        if (printDev == null) {
                            SysUtils.showError(getString(R.string.Failed_connect_printer_select_again));

                            //跳到设置界面
                            SysUtils.startAct(MyScanCodeActivity.this, new PrintActivity());
                        } else {
                            mService.connect(printDev);
                        }
                    } else {
                        SysUtils.showError(getString(R.string.turn_on_Bluetooth_close_printer));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            //已经连接了，直接打印
            doPrint();
        }
    }

    public void doPrint() {
        //连接成功，开始打印，同时提交订单更新到服务器
        try {
            String shopName = sellername;
            String orderDate = payed_time;
            boolean hasPay = Integer.parseInt(pay_status) > 0;
            double payed = Double.parseDouble(total_fee_double);

            String tmp = PrintUtil.getScanCodePrinterMsg(
                    shopName,
                    order_id,
                    orderDate,
                    hasPay,
                    payed
            );
            String printMsg = "";
            printMsg += tmp + "\n\n";
            sendMessage(printMsg);

            if (order.hasQrCode()) {
                addQrCode(order.getQr_uri());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(String message) {
        if (mService.getState() != BluetoothService.STATE_CONNECTED) {
            SysUtils.showError(getString(R.string.str1));
            return;
        }
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothService to write
            byte[] send;
            try {
                send = message.getBytes("GB2312");
            } catch (UnsupportedEncodingException e) {
                send = message.getBytes();
            }

            mService.write(send);
        }
    }

    private void addQrCode(final String uri) {
        final String filePath = QRCodeUtil.getFileRoot(MyScanCodeActivity.this) + File.separator
                + "qr_" + System.currentTimeMillis() + ".jpg";
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean success = QRCodeUtil.createQRImage(uri, 360, 360, null, filePath);

                if (success) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            imageView.setImageBitmap(BitmapFactory.decodeFile(filePath));

                            mService.printCenter();
                            sendMessage(getString(R.string.str2) + "\n");
                            sendMessage(BitmapFactory.decodeFile(filePath));
                            sendMessage("\n\n\n");
                        }
                    });
                }
            }
        }).start();
    }

    private void sendMessage(Bitmap bitmap) {
        // Check that we're actually connected before trying anything
        if (mService.getState() != BluetoothService.STATE_CONNECTED) {
            SysUtils.showError(getString(R.string.str1));
            return;
        }
        // 发送打印图片前导指令
//        byte[] start = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x1B,
//                0x40, 0x1B, 0x33, 0x00 };
//        mService.write(start);

        /**获取打印图片的数据**/
//		byte[] send = getReadBitMapBytes(bitmap);

        mService.printCenter();
        byte[] draw2PxPoint = PicFromPrintUtils.draw2PxPoint(bitmap);

        mService.write(draw2PxPoint);
        // 发送结束指令
//        byte[] end = { 0x1d, 0x4c, 0x1f, 0x00 };
//        mService.write(end);
    }
}
