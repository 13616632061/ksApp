package com.ui.util;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.ui.entity.DeskTableOrder;
import com.ui.entity.Order;
import com.ui.entity.OrderGoods;
import com.MyApplication.KsApplication;
import com.ui.ks.PrintActivity;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class PrintUtil {
    private static int length = 32;

    public static  int  getWordCount(String s) {
        int  length  =   0 ;
        for ( int  i  =   0 ; i  <  s.length(); i ++ )
        {
            int  ascii  =  Character.codePointAt(s, i);
            if (ascii  >=   0   &&  ascii  <= 255 )
                length ++ ;
            else
                length  +=   2 ;

        }
        return  length;
    }
    //得到收银小票的打印模板
    public static String getScanCodePrinterMsg(
            String shopName,
            String order_id,
            String orderDate,
            boolean hasPay,
            double payed
    ){
        String ret = "";
        //商家名称
        ret += "\r\n";
        shopName = getPrintCenter(shopName);
        ret += shopName;
        ret += "\r\n";
        ret += "\r\n";
        //订单号
        ret += getPrintCenter("订单号：" + order_id);
        //下单时间
        ret += getPrintCenter("下单时间：" + orderDate);
        ret += "\r\n";
        //分隔符
        ret += getPrintChar("-", length);
        //商品列表
        String ss = "数量    小计";   //12
        int ss_l = length - getWordCount(ss);
        ret += "名称";
        ret += getPrintChar(" ", ss_l - 4);
        ret += ss;
        ret += getPrintChar("-", length);
        //名称
        ret +="扫码收款";
        int l1 = ss_l - getWordCount(shopName);
        for(int j = 0; j < l1; j++) {
            ret += " ";
        }

        int nn = 1;
        double pp =  payed;
        String su = SysUtils.priceFormat(nn * pp, false);
        //数量
        ret += getPrintRight(String.valueOf(1), 16);

        //小计
        ret += getPrintRight(su, 8);
        ret += "\r\n";
        String sss = "" + payed;
        int sss_l = length - getWordCount(sss);
        ret += hasPay ? "已支付" : "未支付";
        ret += getPrintChar(" ", sss_l - 6);
        ret += sss;
        ret += "\r\n";
        ret += getPrintChar("-", length);
        ret += getPrintLeft("备注：" , length);
        ret += "\r\n";
        ret += getPrintChar("-", length);
        ret += getPrintCenter("本地生活就选 易之星");

        ret += "\n\n";

        return ret;
    };
    //得到收银小票的打印模板
    public static String getcashPrinterMsg(
            String shopName,
            String order_id,
            String orderDate,
            boolean hasPay,
            double payed,
            String mark_text
    ){
        String ret = "";
        //商家名称
        ret += "\r\n";
        shopName = getPrintCenter(shopName);
        ret += shopName;
        ret += "\r\n";
        ret += "\r\n";
        //订单号
        ret += getPrintCenter("订单号：" + order_id);
        //下单时间
        ret += getPrintCenter("下单时间：" + orderDate);
        ret += "\r\n";
        //分隔符
        ret += getPrintChar("-", length);
        //商品列表
        String ss = "数量    小计";   //12
        int ss_l = length - getWordCount(ss);
        ret += "名称";
        ret += getPrintChar(" ", ss_l - 4);
        ret += ss;
        ret += getPrintChar("-", length);
        //名称
        ret +="现金交易";
        int l1 = ss_l - getWordCount(shopName);
        for(int j = 0; j < l1; j++) {
            ret += " ";
        }

        int nn = 1;
        double pp =  payed;
        String su = SysUtils.priceFormat(nn * pp, false);
        //数量
        ret += getPrintRight(String.valueOf(1), 16);

        //小计
        ret += getPrintRight(su, 8);
        ret += "\r\n";
        String sss = "" + payed;
        int sss_l = length - getWordCount(sss);
        ret += hasPay ? "已支付" : "未支付";
        ret += getPrintChar(" ", sss_l - 6);
        ret += sss;
        ret += "\r\n";
        ret += getPrintChar("-", length);
        ret += getPrintLeft("备注："+mark_text , length);
        ret += "\r\n";
        ret += getPrintChar("-", length);
        ret += getPrintCenter("本地生活就选 易之星");

        ret += "\n\n";

        return ret;
    };
    //得到收银小票的打印模板
    public static String getPrinterMsg(String index,
                                       String shippingStr,
                                       String shopName,
                                       String deskNo,
                                       String shopTel,
                                       String orderSn,
                                       String orderDate,
                                       ArrayList<OrderGoods> goodsList,
                                       boolean hasPay,
                                       double payed,
                                       String orderRemark,
                                       boolean hasAddress,
                                       String consignee,
                                       String mobile,
                                       String address) {
        String ret = "";

        //抬头
        ret += "----------";
        ret += index;
        int a = 32 - 20 - getWordCount(index) - getWordCount(shippingStr);
        ret += getPrintChar(" ", a);
        ret += shippingStr;
        ret += "----------";
        ret += "\r\n";

        //商家名称
        ret += "\r\n";
        shopName = getPrintCenter(shopName);
        ret += shopName;
        if(!TextUtils.isEmpty(deskNo)) {
            ret += "\r\n";
            ret += getPrintCenter("桌号：" + deskNo);
        }
        ret += "\r\n";
        ret += "\r\n";

        //电话
        ret += getPrintLeft("电话：" + shopTel,length);
//        if(TextUtils.isEmpty(shopTel)){
//            ret += "\n";
//        }
        //订单号
        ret += getPrintLeft("订单号：" + orderSn,length);
        //下单时间
        ret += getPrintLeft("下单时间：" + orderDate,length);
        ret += "\r\n";
        //分隔符
        ret += getPrintChar("-", length);
        //商品列表
        String ss = "数量    小计";   //12
        int ss_l = length - getWordCount(ss);
        ret += "名称";
        ret += getPrintChar(" ", ss_l - 4);
        ret += ss;
        ret += getPrintChar("-", length);
        //打印菜品
        for(int i = 0; i < goodsList.size(); i++) {
            OrderGoods bean = goodsList.get(i);

            //名称
            ret += bean.getName();
            int l1 = ss_l - getWordCount(bean.getName());
            for(int j = 0; j < l1; j++) {
                ret += " ";
            }

            float nn = bean.getQuantity();
            double pp = bean.getPrice();
            String su = SysUtils.priceFormat(nn * pp, false);
            //数量
            ret += getPrintRight(String.valueOf(bean.getQuantity()), 4);

            //小计
            ret += getPrintRight(su, 8);

            if(!TextUtils.isEmpty(bean.getAttr())) {
                //属性
                ret += getPrintLeft(bean.getAttr(), length);
            }
        }
        ret += getPrintChar("-", length);

        String sss = "" + payed;
        int sss_l = length - getWordCount(sss);
        ret += hasPay ? "已支付" : "未支付";
        ret += getPrintChar(" ", sss_l - 6);
        ret += sss;
        ret += "\r\n";
        ret += getPrintChar("-", length);

        //备注
        orderRemark = orderRemark.replaceAll("<br/>","\r\n");
        ret += getPrintLeft("备注：" + orderRemark, length);
        ret += "\r\n";
        if (hasAddress) {
            ret += getPrintLeft(consignee + "  " + mobile, length);
            ret += "\r\n";
            ret += getPrintLeft(address, length);
            ret += "\r\n";
        }
        ret += getPrintChar("-", length);
        ret += getPrintCenter("本地生活就选 易之星");

        ret += "\n\n";
        return ret;
    }

    //得到收银小票的打印模板
    public static String getPrinterDesk(String shopName, String deskNo, String orderSn,
                                         String orderDate,String orderRemark,List<DeskTableOrder> goodsList) {
        String ret = "";

        //抬头
        ret += "----------";
        ret += "桌上点餐";
        int a = 32 - 20 - getWordCount("桌上点餐");
        ret += getPrintChar(" ", a);
        ret += "----------";
        ret += "\r\n";

        //商家名称
        ret += "\r\n";
        shopName = getPrintCenter(shopName);
        ret += shopName;
        if(!TextUtils.isEmpty(deskNo)) {
            ret += "\r\n";
            ret += getPrintCenter("桌号：" + deskNo);
        }
        ret += "\r\n";
        ret += "\r\n";

        //电话
//        ret += getPrintLeft("电话：" + shopTel,length);
//        if(TextUtils.isEmpty(shopTel)){
//            ret += "\n";
//        }
        //订单号
        ret += getPrintLeft("订单号：" + orderSn,length);
        //下单时间
        ret += getPrintLeft("下单时间：" + orderDate,length);
        ret += "\r\n";
        //分隔符
        ret += getPrintChar("-", length);
        //商品列表
        String ss = "数量    小计";   //12
        int ss_l = length - getWordCount(ss);
        ret += "名称";
        ret += getPrintChar(" ", ss_l - 4);
        ret += ss;
        ret += getPrintChar("-", length);
        //打印菜品
        for(int i = 0; i < goodsList.size(); i++) {
            DeskTableOrder bean = goodsList.get(i);

            //名称
            ret += bean.getName();
            int l1 = ss_l - getWordCount(bean.getName());
            for(int j = 0; j < l1; j++) {
                ret += " ";
            }

            float nn = Float.parseFloat(bean.getQuantity());
//            double pp = bean.getPrice();
            String su = SysUtils.priceFormat(Double.parseDouble(bean.getPrice()), false);
            //数量
            ret += getPrintRight(String.valueOf(bean.getQuantity()), 4);

            //小计
            ret += getPrintRight(su, 8);

            if(!TextUtils.isEmpty(bean.getMenu_memo())) {
                //属性
                ret += getPrintLeft(bean.getMenu_memo(), length);
            }
        }
        ret += getPrintChar("-", length);

//        String sss = "" + payed;
//        int sss_l = length - getWordCount(sss);
//        ret += hasPay ? "已支付" : "未支付";
//        ret += getPrintChar(" ", sss_l - 6);
//        ret += sss;
//        ret += "\r\n";
        ret += getPrintChar("-", length);

        //备注
        orderRemark = orderRemark.replaceAll("<br/>","\r\n");
        ret += getPrintLeft("备注：" + orderRemark, length);
        ret += "\r\n";
//        if (hasAddress) {
//            ret += getPrintLeft(consignee + "  " + mobile, length);
//            ret += "\r\n";
//            ret += getPrintLeft(address, length);
//            ret += "\r\n";
//        }
        ret += getPrintChar("-", length);
//        ret += getPrintCenter("本地生活就选 易之星");

        ret += "\n\n";
        ret += "\n\n";
        return ret;
    }





    public static String getTestMsg() {
        String index = "1";
        String shippingStr = "配送";
        String shopName ="xx餐厅";
        String deskNo = "B11";
        String shopTel ="13000000000";
        String orderSn = "0026001";
        String orderDate = "2015-02-13 11:11";

        ArrayList<OrderGoods> goodsList = new ArrayList<OrderGoods>();

        OrderGoods a = new OrderGoods();
        a.setName("可口可乐");
        a.setQuantity(4);
        a.setPrice(10);

        goodsList.add(a);

        a = new OrderGoods();
        a.setName("鲟龙鱼");
        a.setQuantity(1);
        a.setPrice(132);
        goodsList.add(a);

        a = new OrderGoods();
        a.setName("蛏子");
        a.setQuantity(1);
        a.setPrice(28);
        goodsList.add(a);


        a = new OrderGoods();
        a.setName("多春鱼");
        a.setQuantity(1);
        a.setPrice(28);
        a.setAttr("大小: 大份, 味道: 不辣");
        goodsList.add(a);

        boolean hasPay = true;
        double payed = 228.0;

        String orderRemark = "订单备注";
        boolean hasAddress = true;

        String consignee = "王小二";
        String mobile = "18900000000";
        String address = "上海市黄浦区北京东路";

        return getPrinterMsg(index,
                            shippingStr,
                            shopName,
                            deskNo,
                            shopTel,
                            orderSn,
                            orderDate,
                            goodsList,
                            hasPay,
                            payed,
                            orderRemark,
                            hasAddress,
                            consignee,
                            mobile,
                            address);
    }

    //打印在中间
    public static String getPrintCenter(String title) {
        String ret = "";

        int shopNameLength = getWordCount(title);
        int l = length - shopNameLength;
        if(l > 0) {
            int l_left = l / 2;
            int l_right = l - l_left;

            ret = getPrintChar(" ", l_left) + title + getPrintChar(" ", l_right);
        }

//        if(ret.length() > 0) {
//            ret += "\n";
//        }

        return ret;
    }

    //打印在左边
    public static String getPrintLeft(String title, int l) {
        String ret = title;

        int shopNameLength = getWordCount(title);
        l -= shopNameLength;
        if(l > 0) {
            for(int i = 0; i < l; i++) {
                ret += " ";
            }
        }

        return ret;
    }

    public static String getPrintChar(String c, int length) {
        String ret = "";

        for(int i = 0; i < length; i++) {
            ret += c;
        }

        return ret;
    }


    //打印在右边
    public static String getPrintRight(String title, int l) {
        return getPrintRight(title, " ", l);
    }

    public static String getPrintRight(String title, String ch, int l) {
        String ret = "";

        int shopNameLength = getWordCount(title);
        l -= shopNameLength;
        if(l > 0) {
            for(int i = 0; i < l; i++) {
                ret += ch;
            }
        }
        ret += title;

        return ret;
    }
    //表示是否连接上蓝牙打印机
    private  boolean hasConnect = false;
    //尝试打开蓝牙
    private  final int REQUEST_ENABLE_BT = 2;
    private   BluetoothAdapter mBluetoothAdapter=null;
    private   BluetoothService mService=null;
    private Activity context;
    private String sellername ;
    private String payed_time;
    private String paystatus;
    private String total_amount;
    private String tel;
    private String order_print_id;
    private String order_print_mark;
    private ArrayList<OrderGoods> goodsList ;
    private Order order;
    private int num1;

    public PrintUtil(Activity context, String sellername, String payed_time, String paystatus, String total_amount, String tel, String order_print_id, ArrayList<OrderGoods> goodsList, Order order, int num1,String order_print_mark) {
        this.context = context;
        this.sellername = sellername;
        this.payed_time = payed_time;
        this.paystatus = paystatus;
        this.total_amount = total_amount;
        this.tel = tel;
        this.order_print_id = order_print_id;
        this.goodsList = goodsList;
        this.order = order;
        this.num1 = num1;
        this.order_print_mark = order_print_mark;

        startPrint(context,sellername,payed_time,paystatus,total_amount,
                tel,order_print_id,goodsList,order,num1,order_print_mark);
    }

    //开始打印
    public  void startPrint(final Activity context, final String sellername, final String payed_time, final String paystatus, final String total_amount,
                            final String tel, final String order_print_id , final ArrayList<OrderGoods> goodsList , final Order order,
                            final int num1, final String order_print_mark) {
        Handler mHandler = new Handler() {
            @Override
            public void handleMessage(android.os.Message msg) {
                switch (msg.what) {
                    case BluetoothService.MESSAGE_STATE_CHANGE:
                        switch (msg.arg1) {
                            case BluetoothService.STATE_CONNECTED:
                                hasConnect = true;
                                doPrint(sellername, payed_time, paystatus, total_amount,
                                        tel, order_print_id ,goodsList ,order,
                                        num1,context, mService,order_print_mark);
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
        mService = new BluetoothService(context, mHandler);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            SysUtils.showError("蓝牙不可用，无法设置打印机参数");
            context.finish();
        }

        if(!hasConnect) {
            //还没有连接，先尝试连接
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                context.startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            } else {
                //尝试连接
                try {
                    if(mService != null) {
                        BluetoothDevice printDev = mBluetoothAdapter.getRemoteDevice(KsApplication.getString("printer_mac", ""));

                        if(printDev == null) {
                            SysUtils.showError("连接打印机失败，请重新选择打印机");

                            //跳到设置界面
                            SysUtils.startAct(context, new PrintActivity());
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
            doPrint(sellername, payed_time, paystatus, total_amount,
                    tel, order_print_id ,goodsList ,order,
            num1,context,mService,order_print_mark);
        }
    }
   private  void doPrint(String sellername, String payed_time, String paystatus, String total_amount,
                               String tel, String order_print_id , ArrayList<OrderGoods> goodsList ,Order order,
                               int num1,Activity activity,BluetoothService mService,String order_print_mark) {
        //连接成功，开始打印，同时提交订单更新到服务器
        String orderDate;
        try {
            String shopName =sellername;
            if(payed_time.length()>13){
                 orderDate =payed_time;
            }else {
              orderDate = DateUtils.getDateTimeFromMillisecond(Long.parseLong( payed_time)*1000)+"";
            }
            boolean hasPay =Integer.parseInt(paystatus)> 0;
            double payed = Double.parseDouble(total_amount);
            String tmp = PrintUtil.getPrinterMsg(
                    "",
                    "",
                    shopName,
                    "",
                    tel,
                    order_print_id,
                    orderDate,
                    goodsList,
                    hasPay,
                    payed,
                    order_print_mark,
                    false,
                    "",
                    "",
                    ""
            );
            String printMsg = "";
            printMsg += tmp + "\n\n";
            sendMessage(printMsg,num1,mService);
            if(order.hasQrCode()) {
                addQrCode(order.getQr_uri(),activity,mService);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    byte[] send;
    private  void sendMessage(String message,int num1,BluetoothService mService) {
        if (mService.getState() != BluetoothService.STATE_CONNECTED) {
            SysUtils.showError("蓝牙没有连接");
            return;
        }
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothService to write
            try {
                send = message.getBytes("GB2312");
            } catch (UnsupportedEncodingException e) {
                send = message.getBytes();
            }
            //这只打印次数及打印间隔
            for(int i=0;i<num1;i++) {
                if(i==0){
                    mService.write(send);
                }else {
//                    if(i<num1-1){
//                    SystemClock.sleep(5000);
                        handler.sendEmptyMessageDelayed(201,5000);
//                    }
                }
            }
        }
    }
    private  Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mService.write(send);
        }
    };
    private  void addQrCode(final String uri, Activity activity, final BluetoothService mService) {
        final String filePath = QRCodeUtil.getFileRoot(activity) + File.separator
                + "qr_" + System.currentTimeMillis() + ".jpg";
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean success = QRCodeUtil.createQRImage(uri, 360, 360, null, filePath);

                if (success) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
//                            imageView.setImageBitmap(BitmapFactory.decodeFile(filePath));

                            mService.printCenter();
                            sendMessage("扫码付款\n",0,mService);
                            sendMessage(BitmapFactory.decodeFile(filePath),mService);
                            sendMessage("\n\n\n",0,mService);
                        }
                    }).start();
                }
            }
        }).start();
    }
    private  void sendMessage(Bitmap bitmap,BluetoothService mService) {
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
