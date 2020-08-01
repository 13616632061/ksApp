package com.ui.ks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import com.MyApplication.KsApplication;
import com.base.BaseActivity;
import com.material.widget.PaperButton;
import com.ui.recyclerviewleftslideremove.Printer;
import com.ui.util.BluetoothService;
import com.ui.util.PicFromPrintUtils;
import com.ui.util.PrintUtil;
import com.ui.util.QRCodeUtil;
import com.ui.util.SysUtils;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Set;

public class PrintActivity extends BaseActivity {
    BluetoothService mService = null;

    private ListView printers;
    private ArrayList<Printer> printer_list;
    private PrinterAdapter adapter;

    private ProgressDialog progressDialog = null;
    private MaterialEditText printnum;
    private String printer_mac;
    private PaperButton btn_test;
    private static final int REQUEST_ENABLE_BT = 2;
    private boolean hasConnect = false;

    private BluetoothAdapter mBluetoothAdapter = null;
    private CheckBox auto_print;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print);

        initToolbar(this);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            SysUtils.showError("蓝牙不可用，无法设置打印机参数");
            finish();
        }

        auto_print = (CheckBox)findViewById(R.id.auto_print);
        auto_print.setChecked(KsApplication.getInt("auto_print", 0) == 1);
        auto_print.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                KsApplication.putInt("auto_print", isChecked ? 1 : 0);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!mBluetoothAdapter.isEnabled()) {
            //打开蓝牙
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }
        if (mService==null) {
            mService = new BluetoothService(this, mHandler);
        }

        try {
            //当前设置的打印机
            printnum = (MaterialEditText) findViewById(R.id.printnum);
            printer_mac = KsApplication.getString("printer_mac", "");
            printnum.setText(printer_mac);

            //打印机列表
            printers = (ListView) findViewById(R.id.printers);
            printer_list = new ArrayList<Printer>();
            adapter = new PrinterAdapter();
            printers.setAdapter(adapter);

            //打印测试
            btn_test = (PaperButton) findViewById(R.id.btn_test);
            btn_test.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String mac_str = printnum.getText().toString();

                    if(mac_str.length() < 1) {
                        SysUtils.showError("请输入打印机的mac地址");
                    } else {
                        //初始化打印机
                        if(hasConnect) {
                            //已连接
                            sendMessage();
                        } else {
                            //未连接，尝试连接
                            try {
                                if(mService != null) {
//                                    BluetoothDevice printDev = mService.getDevByMac(mac_str);
                                    BluetoothDevice printDev = mBluetoothAdapter.getRemoteDevice(mac_str);

                                    if(printDev == null) {
                                        SysUtils.showError("连接打印机失败，请重新选择打印机");
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

                    }
                }
            });

            //当扫描到新设备时的操作
//            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
//            this.registerReceiver(mReceiver, filter);

            //当扫描完成时的操作
//            filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
//            this.registerReceiver(mReceiver, filter);

            //开启扫描
            getPairedDeviceList();
//            doDiscovery();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BluetoothService.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                            doConnectState(true);
                            SysUtils.showSuccess("打印机连接成功");
                            break;
                        case BluetoothService.STATE_CONNECTING:
//                            print_connect_btn.setText("正在连接...");
                            break;
                        case BluetoothService.STATE_LISTEN:
                        case BluetoothService.STATE_NONE:
//                            print_connect_btn.setText("无连接");
                            break;
                    }
                    break;
                case BluetoothService.MESSAGE_WRITE:
                    //byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    //String writeMessage = new String(writeBuf);
                    break;
                case BluetoothService.MESSAGE_READ:
                    //byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    //String readMessage = new String(readBuf, 0, msg.arg1);
                    break;
                case BluetoothService.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
//                    mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
//                    Toast.makeText(getApplicationContext(), "连接至"
//                            + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    break;
                case BluetoothService.MESSAGE_TOAST:
                    SysUtils.showError(msg.getData().getString(BluetoothService.TOAST));
                    break;
            }
        }
    };

    /*
    private final  Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BluetoothService.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                            doConnectState(true);
                            SysUtils.showSuccess("打印机连接成功");
                            break;
                        case BluetoothService.STATE_CONNECTING:
                            break;
                        case BluetoothService.STATE_LISTEN:
                        case BluetoothService.STATE_NONE:
                            break;
                    }
                    break;
                case BluetoothService.MESSAGE_CONNECTION_LOST:
                    doConnectState(false);
//                    SysUtils.showError(PrintActivity.this, "打印机连接丢失");
                    break;
                case BluetoothService.MESSAGE_UNABLE_CONNECT:
                    doConnectState(false);
                    SysUtils.showError("无法连接到打印机");
                    break;
            }
        }

    };
    */

    private void doConnectState(boolean hasConnect) {
        this.hasConnect = hasConnect;

        if(hasConnect) {
            btn_test.setText("打印测试");
        } else {
            btn_test.setText("连接打印机");
        }
    }

//    public void showLoading(final String msg) {
//        hideLoading();
//        progressDialog = new ProgressDialog(PrintActivity.this);
//        progressDialog.setMessage(msg);
//        progressDialog.setIndeterminate(false);
//        progressDialog.show();
//    }
//
//    public void hideLoading() {
//        if(progressDialog != null) {
//            progressDialog.dismiss();
//        }
//    }

    public class PrinterAdapter extends BaseAdapter {
        public int getCount() {
            return printer_list.size();
        }

        public Object getItem(int position) {
            return printer_list.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(PrintActivity.this).inflate(R.layout.printer_listview_item, null);
            final CheckBox checkBox1 = (CheckBox) convertView.findViewById(R.id.checkBox1);
            TextView name = (TextView) convertView.findViewById(R.id.name);
            TextView ip = (TextView) convertView.findViewById(R.id.ip);

            Printer bean = printer_list.get(position);
            name.setText(bean.getTitle());
            ip.setText(bean.getMac());

            if(bean.getIsCurrent()) {
                checkBox1.setChecked(true);
            } else {
                checkBox1.setChecked(false);
            }

            checkBox1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for(int i = 0; i < getCount(); i++) {
                        Printer bean = printer_list.get(i);
                        if(i == position) {
                            printnum.setText(bean.getMac());
//                            checkBox1.setChecked(true);
                            bean.setIsCurrent(true);
                        } else {
//                            checkBox1.setChecked(false);
                            bean.setIsCurrent(false);
                        }

                        printer_list.set(i, bean);
                    }

                    notifyDataSetChanged();
                }
            });

//            convertView.setBackgroundResource(R.drawable.category_list_odd_row);

            return convertView;
        }
    }

    //扫描
//    private void doDiscovery() {
//        //首先找到配对的蓝牙设备
//        showLoading("扫描中...");
//
//        if (mService.isDiscovering()) {
//            mService.cancelDiscovery();
//        }
//
//        mService.startDiscovery();
//    }

//    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//
//            hideLoading();
//
//            //发现新设备
//            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
//                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
//                    Printer bean = new Printer();
//                    bean.setTitle(device.getName());
//                    bean.setMac(device.getAddress());
//                    bean.setIsCurrent(device.getAddress().equals(printer_mac));
//
//                    printer_list.add(bean);
//                }
//            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
//                //没找到
//                if (adapter.getCount() == 0) {
//                    printer_list.clear();
//                }
//            }
//
//            adapter.notifyDataSetChanged();
//        }
//    };

    //得到配对的蓝牙列表
    public void getPairedDeviceList() {
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

        if(pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                Printer bean = new Printer();
                bean.setTitle(device.getName());
                bean.setMac(device.getAddress());
                bean.setIsCurrent(device.getAddress().equals(printer_mac));

                printer_list.add(bean);
            }

            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mService != null) {
            mService.stop();
        }

        mService = null;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_print, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuId = item.getItemId();

        if(menuId == android.R.id.home) {
            onBackPressed();
        } else if(menuId == R.id.menu_save) {
            //保存
            String mac_str = printnum.getText().toString();
            KsApplication.putString("printer_mac", mac_str);

            SysUtils.showSuccess(getString(R.string.str259));//操作已执行

            onBackPressed();
        }

        return true;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                if (resultCode == Activity.RESULT_OK) {
                    //蓝牙打开成功
                    SysUtils.showSuccess(getString(R.string.str260));//蓝牙已打开
                } else {
                    SysUtils.showError(getString(R.string.str261));//蓝牙打开失败，无法设置打印机参数
                    finish();
                }
                break;
            default:
                break;
        }
    }

    private void sendMessage() {
        String printMsg = PrintUtil.getTestMsg();
        sendMessage(printMsg);
        sendMessage("\n");

        //添加二维码
        addQrCode("http://www.baidu.com");
    }

    private void sendMessage(String message) {
        if (mService.getState() != BluetoothService.STATE_CONNECTED) {
            SysUtils.showError(getString(R.string.str1));//蓝牙没有连接
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

    private void sendMessage(Bitmap bitmap) {
        // Check that we're actually connected before trying anything
        if (mService.getState() != BluetoothService.STATE_CONNECTED) {
            SysUtils.showError(getString(R.string.str1));//蓝牙没有连接
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

    private void addQrCode(final String uri) {
        final String filePath = QRCodeUtil.getFileRoot(PrintActivity.this) + File.separator
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
                            sendMessage(getString(R.string.str2)+"\n");//扫码付款
                            sendMessage(BitmapFactory.decodeFile(filePath));
                            sendMessage("\n\n\n");
                        }
                    });
                }
            }
        }).start();
    }
}
