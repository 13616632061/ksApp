package com.ui.ks;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.MyApplication.KsApplication;
import com.afollestad.materialdialogs.MaterialDialog;
import com.base.BaseActivity;
import com.ui.entity.Order;
import com.ui.util.BluetoothService;
import com.ui.util.PicFromPrintUtils;
import com.ui.util.PrintUtil;
import com.ui.util.QRCodeUtil;
import com.ui.util.SysUtils;

import java.io.File;
import java.io.UnsupportedEncodingException;

public class CashChargeSuccessActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_price,tv_mark;
    private Button btn_print,btn_continue_cash;

    private String amount_receivable,mark_text;
    private Order order;
    private String order_id = "";
    private String sellername="";
    private String pay_status="1";
    private String payed_time;
    //蓝牙服务
    BluetoothService mService = null;
    //表示是否连接上蓝牙打印机
    private boolean hasConnect = false;
    //尝试打开蓝牙
    private static final int REQUEST_ENABLE_BT = 2;
    private BluetoothAdapter mBluetoothAdapter = null;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_charge_success);
        SysUtils.setupUI(CashChargeSuccessActivity.this,findViewById(R.id.activity_cash_charge_success));
        initToolbar(this);
        Intent intent=getIntent();
        amount_receivable=intent.getStringExtra("amount_receivable");
        mark_text=intent.getStringExtra("mark_text");
        order_id=intent.getStringExtra("order_id");
        sellername=intent.getStringExtra("sellername");
        payed_time=intent.getStringExtra("payed_time");

        initView();
    }

    private void initView() {
        tv_price= (TextView) findViewById(R.id.tv_price);
        tv_mark= (TextView) findViewById(R.id.tv_mark);
        btn_print= (Button) findViewById(R.id.btn_print);
        btn_continue_cash= (Button) findViewById(R.id.btn_continue_cash);

        tv_price.setText("￥"+amount_receivable);
        tv_mark.setText(mark_text);

        btn_print.setOnClickListener(this);
        btn_continue_cash.setOnClickListener(this);

        mService = new BluetoothService(this, mHandler);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            SysUtils.showError("蓝牙不可用，无法设置打印机参数");
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_print:
                new MaterialDialog.Builder(CashChargeSuccessActivity.this)
                        .theme(SysUtils.getDialogTheme())
                        .content(getString(R.string.sure_small_ticket))
                        .positiveText(getString(R.string.sure))
                        .negativeText(getString(R.string.cancel))
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
//                                new PrintUtil(CashChargeSuccessActivity.this,sellername,payed_time,pay_status,amount_receivable,
//                                        "", order_id,null,order,1,mark_text);
                                startPrint();
                            }
                        })
                        .show();
                break;
            case R.id.btn_continue_cash:
                Intent intent=new Intent(CashChargeSuccessActivity.this, CashChargeActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
    //开始打印
    public void startPrint() {
        if(!hasConnect) {
            //还没有连接，先尝试连接
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            } else {
                //尝试连接
                try {
                    if(mService != null) {
//                        BluetoothDevice printDev = mService.getDevByMac(KsApplication.getString("printer_mac", ""));
                        BluetoothDevice printDev = mBluetoothAdapter.getRemoteDevice(KsApplication.getString("printer_mac", ""));

                        if(printDev == null) {
                            SysUtils.showError("连接打印机失败，请重新选择打印机");

                            //跳到设置界面
                            SysUtils.startAct(CashChargeSuccessActivity.this, new PrintActivity());
                        } else {
                            mService.connect(printDev);
                        }
                    } else {
                        SysUtils.showError("请开启蓝牙并且靠近打印机");
                    }

                } catch(Exception e) {
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
            String shopName =sellername;
            String orderDate = payed_time;
            boolean hasPay =Integer.parseInt(pay_status)> 0;
            double payed = Double.parseDouble(amount_receivable);

            String tmp = PrintUtil.getcashPrinterMsg(
                    shopName,
                    order_id,
                    orderDate,
                    hasPay,
                    payed,
                    mark_text
            );
            String printMsg = "";
            printMsg += tmp + "\n\n";
            sendMessage(printMsg);

            if(order.hasQrCode()) {
                addQrCode(order.getQr_uri());
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    private void sendMessage(String message) {
        if (mService.getState() != BluetoothService.STATE_CONNECTED) {
            SysUtils.showError("蓝牙没有连接");
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
        final String filePath = QRCodeUtil.getFileRoot(CashChargeSuccessActivity.this) + File.separator
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
                            sendMessage("扫码付款\n");
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
            SysUtils.showError("蓝牙没有连接");
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
