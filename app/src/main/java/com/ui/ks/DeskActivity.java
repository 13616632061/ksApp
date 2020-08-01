package com.ui.ks;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.MyApplication.KsApplication;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.base.BaseActivity;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.ui.adapter.DeskOrderAdapter;
import com.ui.entity.DeskOrder;
import com.ui.entity.DeskOrderInfo;
import com.ui.entity.DeskTableOrder;
import com.ui.listview.PagingListView;
import com.ui.util.BluetoothService;
import com.ui.util.CustomRequest;
import com.ui.util.DialogUtils;
import com.ui.util.PicFromPrintUtils;
import com.ui.util.PrintUtil;
import com.ui.util.QRCodeUtil;
import com.ui.util.SelectPicPopupWindow;
import com.ui.util.SysUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2020/3/19.
 */

public class DeskActivity extends BaseActivity implements View.OnClickListener, DeskOrderAdapter.SetOnClick {


    ImageView iv_managmentfragment_back;
    PagingListView lv_content;
    int page=1;
    int total=0;
    int per_page=0;
    List<DeskOrder> deskOrders=new ArrayList<>();
    DeskOrderAdapter adapter;

    boolean loadingMore = false;//是否加载更多
    private SwipeRefreshLayout refresh_header;

    //蓝牙服务
    BluetoothService mService = null;
    //表示是否连接上蓝牙打印机
    private boolean hasConnect = false;
    //尝试打开蓝牙
    private static final int REQUEST_ENABLE_BT = 2;

    public FloatingActionButton tv_print;
    private BluetoothAdapter mBluetoothAdapter = null;

    private int printselect=0;
    List<DeskTableOrder> deskTableOrders;
    TextView tv_set_up;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desk);
        initToolbar(this);
        initView();

        loadFirst();

    }


    //第一次加载
    private void loadFirst() {
        page = 1;
        LoadDatas();
    }

    private void initView() {
        iv_managmentfragment_back= (ImageView) findViewById(R.id.iv_managmentfragment_back);
        iv_managmentfragment_back.setOnClickListener(this);
        lv_content= (PagingListView) findViewById(R.id.lv_content);
        refresh_header= (SwipeRefreshLayout) findViewById(R.id.refresh_header);

        refresh_header.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
               loadFirst();
            }
        });

        tv_set_up= (TextView) findViewById(R.id.tv_set_up);
        tv_set_up.setOnClickListener(this);

        lv_content.setHasMoreItems(true);
        lv_content.setPagingableListener(new PagingListView.Pagingable() {
            @Override
            public void onLoadMoreItems() {
                if (loadingMore) {
                    //加载更多
                    LoadDatas();
                    setRefreshing(false);
                } else {
                    updateAdapter();
                }
            }
        });


        mService = new BluetoothService(this, mHandler);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            SysUtils.showError("蓝牙不可用，无法设置打印机参数");
            finish();
        }


    }


    private void updateAdapter() {
        lv_content.onFinishLoading(loadingMore, null);
        if(adapter == null) {
            if (deskOrders!=null) {
                adapter = new DeskOrderAdapter(DeskActivity.this, deskOrders);
                adapter.SetOnClicks(this);
                lv_content.setAdapter(adapter);
            }
        } else {
            adapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_managmentfragment_back:
                finish();
                break;
            case R.id.tv_set_up:
                select_popwindow();
                break;
        }
    }


    private PopupWindow popupWindow;
    private View view_pop;
    private RelativeLayout ordersearch_layout,order_type_image,Table_pic_layout;
    private SelectPicPopupWindow mSelectPicPopupWindow;
    private void select_popwindow(){
        if(popupWindow==null) {
            view_pop= View.inflate(this, R.layout.remarks_popwindow, null);
            ordersearch_layout = (RelativeLayout) view_pop.findViewById(R.id.ordersearch_layout);
            Table_pic_layout = (RelativeLayout) view_pop.findViewById(R.id.Table_pic_layout);
            order_type_image = (RelativeLayout) view_pop.findViewById(R.id.order_type_image);
            popupWindow = new PopupWindow(view_pop, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            //实例化一个ColorDrawable颜色为半透明
            ColorDrawable mColorDrawable = new ColorDrawable(0x20000000);
            //设置弹框的背景
            popupWindow.setBackgroundDrawable(mColorDrawable);
            ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.75f, Animation.RELATIVE_TO_SELF, 0.1f);
            scaleAnimation.setDuration(200);
            view_pop.startAnimation(scaleAnimation);
            //设置弹框添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
            view_pop.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int height = view_pop.findViewById(R.id.order_type_image).getBottom();
                    int height_top = view_pop.findViewById(R.id.ordersearch_layout).getTop();
                    int weight = view_pop.findViewById(R.id.order_type_image).getLeft();
                    int y = (int) event.getY();
                    int x = (int) event.getX();
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (y > height || y < height_top) {
                            if(popupWindow!=null){
                                popupWindow.dismiss();
                                popupWindow = null;
                            }
                        }
                        if (x < weight) {
                            if(popupWindow!=null){
                                popupWindow.dismiss();
                                popupWindow = null;
                            }
                        }
                    }
                    return true;
                }
            });
            ordersearch_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(popupWindow!=null){
                        popupWindow.dismiss();
                        popupWindow = null;
                    }
                    SysUtils.startAct(DeskActivity.this, new AddRemarksActivity());
                }
            });
            order_type_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(popupWindow!=null){
                        popupWindow.dismiss();
                        popupWindow = null;
                    }
                    SysUtils.startAct(DeskActivity.this, new CodescanningActivity());
//                    mSelectPicPopupWindow=new SelectPicPopupWindow(DeskActivity.this, 1, new AdapterView.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////                            setOrderPay(position + 1);
//                            mSelectPicPopupWindow.dismiss();
//                        }
//                    });
//                    mSelectPicPopupWindow.showAtLocation(findViewById(R.id.scroll_layout) , Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);

                }
            });
            Table_pic_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(popupWindow!=null){
                        popupWindow.dismiss();
                        popupWindow = null;
                    }
                    SysUtils.startAct(DeskActivity.this, new TablePicActivity());
                }
            });

            popupWindow.showAtLocation(tv_set_up, Gravity.TOP, 0, 0);
        }

    }


    public void LoadDatas(){
        Map<String,String> map=new HashMap<>();
        map.put("page",page+"");
        String url= SysUtils.getnewsellerUrl("Menu/orders");
        CustomRequest r=new CustomRequest(Request.Method.POST, url, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if(page <= 1) {
                    deskOrders.clear();
                }
                hideLoading();
                try {
                    JSONObject object=new JSONObject(response.toString());
                    JSONObject ret = null;
                    ret = object.getJSONObject("response");
                    Log.d("print","打印出来的商品详情为"+ret);
                    String status=ret.getString("status");
                    if (status.equals("0")){
                        JSONObject data=ret.getJSONObject("data");
                        total=data.getInt("total");
                        per_page=data.getInt("current_page");
                        JSONArray jadata=data.getJSONArray("data");
//                        deskOrders.clear();
                        for (int i=0;i<jadata.length();i++){
                            DeskOrder deskOrder=new DeskOrder();
                            JSONObject obj=jadata.getJSONObject(i);
                            String order_id=obj.getString("order_id");
                            deskOrder.setOrder_id(order_id);
                            String mark_text=obj.getString("mark_text");
                            deskOrder.setMark_text(mark_text);
                            String pay_status=obj.getString("pay_status");
                            deskOrder.setPay_status(pay_status);
                            String createtime=obj.getString("createtime");
                            deskOrder.setCreatetime(createtime);
                            String total_amount=obj.getString("total_amount");
                            deskOrder.setTotal_amount(total_amount);
                            String is_menu_bind=obj.getString("is_menu_bind");
                            deskOrder.setIs_menu_bind(is_menu_bind);
                            String desk_num= URLDecoder.decode(obj.getString("desk_num"));
                            deskOrder.setDesk_num(desk_num);
                            String menu_confirm=obj.getString("menu_confirm");
                            deskOrder.setMenu_confirm(menu_confirm);
                            String memo=obj.getString("memo");
                            deskOrder.setMemo(memo);
                            List<DeskOrderInfo> deskOrderInfos=new ArrayList<>();
                            JSONArray ja=obj.getJSONArray("info");
                            for (int j=0;j<ja.length();j++){
                                DeskOrderInfo deskOrderInfo=new DeskOrderInfo();
                                JSONObject ob=ja.getJSONObject(j);
                                String goods_id= ob.getString("goods_id");
                                deskOrderInfo.setGoods_id(goods_id);
                                String name= ob.getString("name");
                                deskOrderInfo.setName(name);
                                String price= ob.getString("price");
                                deskOrderInfo.setPrice(price);
                                String quantity= ob.getString("quantity");
                                deskOrderInfo.setQuantity(quantity);
                                deskOrderInfos.add(deskOrderInfo);
                            }
                            deskOrder.setDeskOrderInfos(deskOrderInfos);
                            deskOrders.add(deskOrder);
                        }
                        if (page < per_page ) {
                            loadingMore=true;
                        }else {
                            loadingMore= false;
                            setRefreshing(false);
                            lv_content.setIsLoading(false);
                        }
                        setRefreshing(false);
                        if (loadingMore) {
                            page++;
                        }
                    }else {
                        loadingMore=false;
                        setRefreshing(false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {
                    updateAdapter();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideLoading();
                setRefreshing(false);
                lv_content.setIsLoading(false);
            }
        });
        executeRequest(r);
        showLoading(DeskActivity.this);
    }


    //是否下拉刷新
    private void setRefreshing(boolean refreshing) {
        refresh_header.setRefreshing(refreshing);
    }


    @Override
    public void ondelete(final int i) {
        DialogUtils.SetDialog(DeskActivity.this);
        DialogUtils dialog=new DialogUtils();
        dialog.SetOnDeterminecancel(new DialogUtils.OnDeterminecancel() {
            @Override
            public void Determinecancel() {
                DeleteOrder(false,deskOrders.get(i).getDesk_num(),i);
            }
        });
    }

    @Override
    public void oncomplete(int i) {
        DeleteOrder(true,deskOrders.get(i).getDesk_num(),i);
    }

    @Override
    public void onEdit(int i,boolean isedit) {
        DialogUtils.ShowOrderTable(DeskActivity.this,deskOrders.get(i).getOrder_id(),deskOrders.get(i).getDesk_num(),isedit);
    }

    @Override
    public void OnPrint(final int i) {
        printselect=i;
        new MaterialDialog.Builder(DeskActivity.this)
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


    public void DeleteOrder( final boolean iscancel, String desk_num, final int i){
        Map<String,String> map=new HashMap<>();
        String type="0";
        if (iscancel){
            type="0";
            map.put("is_app_print","0");
        }else {
            type="1";
        }
        map.put("desk_num", URLEncoder.encode(desk_num));
        map.put("type",type);
        String url=SysUtils.getnewsellerUrl("Menu/confirmOrder");
        final CustomRequest r=new CustomRequest(Request.Method.POST, url, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                hideLoading();
                try {
                    Log.d("print","打印出来的商品数据为"+response.toString());
                    JSONObject object=new JSONObject(response.toString());
                    JSONObject rep=object.getJSONObject("response");
                    String status=rep.getString("status");
                    if (status.equals("0")){
                        if (iscancel){
                            deskOrders.get(i).setMenu_confirm("1");
                            adapter.notifyDataSetChanged();
                        }else {
                            deskOrders.remove(i);
                            adapter.notifyDataSetChanged();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideLoading();
            }
        });
        executeRequest(r);
        showLoading(DeskActivity.this);
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
                            SysUtils.startAct(DeskActivity.this, new PrintActivity());
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


    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
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
//                case BluetoothService.MESSAGE_CONNECTION_LOST:
//                    hasConnect = false;
//                    break;
//                case BluetoothService.MESSAGE_UNABLE_CONNECT:
//                    hasConnect = false;
//                    SysUtils.showError("无法连接到配对的蓝牙打印机，请先通过打印机测试再进行收银小票打印");
//                    break;
            }
        }

    };


    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mService != null) {
            mService.stop();
        }

        mService = null;
    }


    public void doPrint() {
        //连接成功，开始打印，同时提交订单更新到服务器
        System.out.println("开始打印");
        getOrderdetails( deskOrders.get(printselect).getOrder_id(), deskOrders.get(printselect).getDesk_num());

    }

    private void addQrCode(final String uri) {
        final String filePath = QRCodeUtil.getFileRoot(DeskActivity.this) + File.separator
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

    //获取订单详情
    public void getOrderdetails(final String order_id, final String desk_num){
        final String[] mark_text = new String[1];
        final String[] total_amount = new String[1];
        Map<String,String> map=new HashMap<>();
        map.put("order_id",order_id);
        map.put("desk_num",URLEncoder.encode(desk_num));
        String url=SysUtils.getnewsellerUrl("Menu/getOrderInfo");
        CustomRequest r=new CustomRequest(Request.Method.POST, url, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                hideLoading();
                deskTableOrders=new ArrayList<>();
                try {
                    JSONObject jsonObject=new JSONObject(response.toString());
                    JSONObject ret=jsonObject.getJSONObject("response");
                    String status=ret.getString("status");
                    if (status.equals("0")){
                        JSONObject data=ret.getJSONObject("data");
                        String order_id=data.getString("order_id");
                        mark_text[0] =data.getString("mark_text");
                        total_amount[0] =data.getString("total_amount");
                        JSONArray info=data.getJSONArray("info");
                        for (int i=0;i<info.length();i++) {
                            DeskTableOrder order=new DeskTableOrder();
                            JSONObject object = info.getJSONObject(i);
                            order.setGoods_id(object.getString("goods_id"));
                            order.setName(object.getString("name"));
                            order.setPrice(object.getString("price"));
                            order.setQuantity(object.getString("quantity"));
                            order.setObj_id(object.getString("obj_id"));
                            order.setMenu_memo(object.getString("menu_memo"));
                            deskTableOrders.add(order);
                        }
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }finally {
                    try {
                        String tmp = PrintUtil.getPrinterDesk("",deskOrders.get(printselect).getDesk_num(),deskOrders.get(printselect).getOrder_id(),
                                deskOrders.get(printselect).getCreatetime(),deskOrders.get(printselect).getMemo(),deskTableOrders);
                        String printMsg = "";
                        printMsg += tmp + "\n\n";

                        sendMessage(printMsg);
//            if(order.hasQrCode()) {
//                addQrCode(order.getQr_uri());
//            }
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideLoading();
            }
        });
        executeRequest(r);
    }


}
