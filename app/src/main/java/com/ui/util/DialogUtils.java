package com.ui.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.blankj.utilcode.util.KeyboardUtils;
import com.google.gson.Gson;
import com.ui.adapter.TableorderAdapter;
import com.ui.entity.Carousel;
import com.ui.entity.DeskTableOrder;
import com.ui.entity.Goods_Inventory;
import com.ui.entity.MemberSpecifications;
import com.ui.ks.DeskActivity;
import com.ui.ks.InventoryActivity;
import com.ui.ks.R;
import com.ui.listview.PagingListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DialogUtils {
    private static AlertDialog mAlertDialog;
    private static EditText et_accountinfo_input;
    private static InputMethodManager imm;

    public static Dialog createLoadingDialog(Context context, String text, boolean canCancel) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.progress_bar, null);

        final Dialog loadingDialog = new Dialog(context, R.style.MyDialog);
        loadingDialog.setContentView(v);
        loadingDialog.setCancelable(false);
        ImageButton imgbtn_guanbi = (ImageButton) v.findViewById(R.id.imgbtn_guanbi);
        if (canCancel) {
            //可以被取消
            imgbtn_guanbi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loadingDialog.dismiss();
                }
            });
        }

        TextView top_process_promot = (TextView) v.findViewById(R.id.top_process_promot);
        top_process_promot.setText(text);

        return loadingDialog;
    }

    public static Dialog createLoadingDialog(Context context, String text) {
        return createLoadingDialog(context, text, true);
    }

    public static Dialog createLoadingDialog(Context context) {
        return createLoadingDialog(context, context.getResources().getString(R.string.str92), true);
    }

    /**
     * 添加账户资料弹窗
     *
     * @param mContext
     * @param str_name
     * @param text
     */
    public static void editAccountDialog(final Context mContext, String str_name, final TextView text) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        View view = View.inflate(mContext, R.layout.editaccountinfo_dialog, null);
        TextView tv_accountinfo_name = (TextView) view.findViewById(R.id.tv_accountinfo_name);
        et_accountinfo_input = (EditText) view.findViewById(R.id.et_accountinfo_input);
        et_accountinfo_input.requestFocus();
        et_accountinfo_input.setFocusable(true);
        //软键盘显示
        imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        TextView tv_editaccount_cancel = (TextView) view.findViewById(R.id.tv_editaccount_cancel);
        TextView tv_editaccount_save = (TextView) view.findViewById(R.id.tv_editaccount_save);
        tv_accountinfo_name.setText(str_name);
        tv_editaccount_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAlertDialog.dismiss();
                //再次调用软键盘消失
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        });
        tv_editaccount_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(et_accountinfo_input.getText().toString().trim())) {
                    text.setText(et_accountinfo_input.getText().toString().trim());
                    mAlertDialog.dismiss();
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }
            }
        });
        mAlertDialog = dialog.setView(view).show();
        mAlertDialog.show();
    }

    /**
     * 含有确定的弹框
     *
     * @param context
     * @param message
     */
    public static void showbuilder(final Activity context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton(context.getString(R.string.sure), new AlertDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        context.finish();
                    }
                });
        builder.show();
    }

    //判断软键盘是否存在
    public static boolean isSoftShowing(Activity context) {
        //获取当前屏幕内容的高度
        int screenHeight = context.getWindow().getDecorView().getHeight();
        //获取View可见区域的bottom
        Rect rect = new Rect();
        context.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);

        return screenHeight - rect.bottom != 0;
    }


    //显示盘点库存
    public static void ShowInventory(Context mContext, Goods_Inventory goods_inventory) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        View view = View.inflate(mContext, R.layout.inventorygoods_dialog, null);
        TextView tv_goods_name = (TextView) view.findViewById(R.id.tv_goods_name);
        TextView tv_goods_code = (TextView) view.findViewById(R.id.tv_goods_code);
        TextView tv_stock = (TextView) view.findViewById(R.id.tv_stock);
        final EditText ed_inventory_stock = (EditText) view.findViewById(R.id.ed_inventory_stock);
        Button btn_add_inventory = (Button) view.findViewById(R.id.btn_add_inventory);
        tv_goods_name.setText(goods_inventory.getName());
        tv_goods_code.setText(goods_inventory.getBncode());
        tv_stock.setText(goods_inventory.getStore());
        ed_inventory_stock.setText(goods_inventory.getStore());
        ed_inventory_stock.setSelection(ed_inventory_stock.getText().toString().length());
        btn_add_inventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddInventoryStock.addInventoryStock(ed_inventory_stock.getText().toString());
                DismissDialog();
                KeyboardUtils.hideSoftInput((Activity) mContext);
            }
        });
        mAlertDialog = dialog.setView(view).show();
        ed_inventory_stock.setFocusable(true);
        ed_inventory_stock.setFocusableInTouchMode(true);
        ed_inventory_stock.requestFocus();


        Window window = mAlertDialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;//如果不设置,可能部分机型出现左右有空隙,也就是产生margin的感觉
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE;//显示dialog的时候,就显示软键盘
        params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;//就是这个属性导致不能获取焦点,默认的是FLAG_NOT_FOCUSABLE,故名思义不能获取输入焦点,
        window.setAttributes(params);
//        ed_inventory_stock.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean focused) {
////                if (focused) {
//                //dialog弹出软键盘
//                mAlertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
//                        | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
////                }
//            }
//        });
        mAlertDialog.show();
        KeyboardUtils.showSoftInput((Activity) mContext);
    }

    //关闭dialog的设置
    public static void DismissDialog() {
        if (mAlertDialog != null) {
            mAlertDialog.dismiss();
        }
    }

    /**
     * 设置盘点库存
     */
    public void SetOnAddInventoryStock(OnAddInventoryStock onAddInventoryStock) {
        this.onAddInventoryStock = onAddInventoryStock;
    }

    public static OnAddInventoryStock onAddInventoryStock;

    public interface OnAddInventoryStock {
        void addInventoryStock(String InventoryStock);
    }

    public static void ShowAddMember(final Context mContext) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        View view = View.inflate(mContext, R.layout.add_member_dialog, null);
        final EditText ed_member_name = (EditText) view.findViewById(R.id.ed_member_name);
        final EditText ed_member_phone = (EditText) view.findViewById(R.id.ed_member_phone);
        final EditText ed_member_balance = (EditText) view.findViewById(R.id.ed_member_balance);
        final EditText ed_member_integral = (EditText) view.findViewById(R.id.ed_member_integral);
        final EditText ed_discount_rate = (EditText) view.findViewById(R.id.ed_discount_rate);
        Button btn_add_member = (Button) view.findViewById(R.id.btn_add_member);
        btn_add_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Map<String, String>> listmap = new ArrayList<Map<String, String>>();
                Map<String, String> map = new HashMap<String, String>();
                if (ed_member_phone.getText().toString().length() == 11) {
                    map.put("mobile", ed_member_phone.getText().toString());
                } else {
                    Toast.makeText(mContext, mContext.getResources().getString(R.string.str116), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (ed_member_integral.getText().toString() != null && !ed_member_integral.getText().toString().equals("")) {
                    map.put("score", ed_member_integral.getText().toString());
                } else {
                    map.put("score", ed_member_integral.getHint().toString());
                }
                if (ed_member_balance.getText().toString() != null && !ed_member_balance.getText().toString().equals("")) {
                    map.put("surplus", ed_member_balance.getText().toString());
                } else {
                    map.put("surplus", ed_member_balance.getHint().toString());
                }
                if (ed_discount_rate.getText().toString() != null && !ed_discount_rate.getText().toString().equals("")) {
                    map.put("discount_rate", ed_discount_rate.getText().toString());
                } else {
                    map.put("discount_rate", ed_discount_rate.getHint().toString());
                }
                map.put("member_lv_custom_id", "");
                map.put("member_lv_custom_key", "");
                map.put("member_name", ed_member_name.getText().toString());
                map.put("birthday", DateUtils.getCurrentTime() + "");
                map.put("remark", "");
                map.put("is_require_pass", "no");
                listmap.add(map);
                Gson gson = new Gson();
                final String mapstr = gson.toJson(listmap);
                onAddMember.addMember(mapstr);
                DismissDialog();
            }
        });
        //打印出来的数据为
        mAlertDialog = dialog.setView(view).show();
        mAlertDialog.show();
    }

    public void SetOnAddMember(OnAddMember onAddMember) {
        this.onAddMember = onAddMember;
    }

    public static OnAddMember onAddMember;

    public interface OnAddMember {
        void addMember(String InventoryStock);
    }

    //添加桌上点餐的图片弹窗
    public static void ShowAddRotationchart(Context mContext, Uri uri, final Carousel carousel) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.product)
                .showImageForEmptyUri(R.drawable.product)
                .cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new RoundedBitmapDisplayer(20))
                .build();
        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        View view = View.inflate(mContext, R.layout.addrotationchart, null);
        ImageView iv_goodpicture = (ImageView) view.findViewById(R.id.iv_goodpicture);
        final EditText ed_describe = (EditText) view.findViewById(R.id.ed_describe);
        final EditText ed_link = (EditText) view.findViewById(R.id.ed_link);
        if (carousel == null) {
            iv_goodpicture.setImageURI(uri);
        } else {
            ImageLoader.getInstance().displayImage(carousel.getUrl(), iv_goodpicture, options);
            ed_describe.setText(carousel.getTitle());
            ed_link.setText(carousel.getHref());
        }
        Button btn_delete = (Button) view.findViewById(R.id.btn_delete);
        Button btn_preservation = (Button) view.findViewById(R.id.btn_preservation);
        iv_goodpicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddRotationchart.AddImagview();
                DismissDialog();
            }
        });
        //保存这个轮播图
        btn_preservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddRotationchart.AddAddRotationchart("", ed_describe.getText().toString().trim(), ed_link.getText().toString().trim(), "1");
                DismissDialog();
            }
        });
        //删除这个轮播图
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddRotationchart.DeleteRotationchart(carousel.getId() + "", ed_describe.getText().toString().trim(), ed_link.getText().toString().trim(), "1");
                DismissDialog();
            }
        });
        //打印出来的数据为
        mAlertDialog = dialog.setView(view).show();
        mAlertDialog.show();
    }

    public void SetOnAddRotationchart(OnAddRotationchart onAddRotationchart) {
        this.onAddRotationchart = onAddRotationchart;
    }

    public static OnAddRotationchart onAddRotationchart;

    public interface OnAddRotationchart {
        void AddImagview();

        void DeleteRotationchart(String id, String title, String href, String sort_num);

        void AddAddRotationchart(String id, String title, String href, String sort_num);

    }


    public static void ShowAddSpecifications(Context mContext, final MemberSpecifications memberSpecifications) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        View view = View.inflate(mContext, R.layout.addmemberspecifications, null);
        final EditText ed_recharge = (EditText) view.findViewById(R.id.ed_recharge);
        final EditText ed_give = (EditText) view.findViewById(R.id.ed_give);
        Button btn_preservation = (Button) view.findViewById(R.id.btn_preservation);
        Button btn_delete = (Button) view.findViewById(R.id.btn_delete);
        if (memberSpecifications != null) {
            ed_recharge.setText(memberSpecifications.getVal());
            ed_give.setText(memberSpecifications.getGive());
            btn_delete.setVisibility(View.VISIBLE);
        } else {
            btn_delete.setVisibility(View.GONE);
        }
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddSpecifications.DeleteSpecifications(memberSpecifications.getRecharge_id());
                DismissDialog();
            }
        });
        btn_preservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (memberSpecifications != null) {
                    onAddSpecifications.AddSpecifications(ed_recharge.getText().toString(), ed_give.getText().toString(), memberSpecifications.getRecharge_id());
                } else {
                    onAddSpecifications.AddSpecifications(ed_recharge.getText().toString(), ed_give.getText().toString(), "");
                }
                DismissDialog();
            }
        });
        mAlertDialog = dialog.setView(view).show();
        mAlertDialog.show();
    }

    public void SetOnAddSpecifications(OnAddSpecifications onAddSpecifications) {
        this.onAddSpecifications = onAddSpecifications;
    }

    public static OnAddSpecifications onAddSpecifications;

    public interface OnAddSpecifications {
        void AddSpecifications(String recharge, String give, String id);

        void DeleteSpecifications(String id);
    }


    public static void SetDialog(Context mContext) {
        View view = View.inflate(mContext, R.layout.dialog_sure_delete_layout, null);
        final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(view);
        Button btn_sure = (Button) view.findViewById(R.id.btn_sure);
        Button btn_cell = (Button) view.findViewById(R.id.btn_cell);
        TextView tv_message = (TextView) view.findViewById(R.id.tv_message);
        tv_message.setText(mContext.getString(R.string.str117));
        btn_sure.setText(mContext.getString(R.string.sure));
        btn_cell.setText(mContext.getResources().getString(R.string.cancel));
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeterminecancel.Determinecancel();
                dialog.dismiss();
            }
        });
        btn_cell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    public void SetOnDeterminecancel(OnDeterminecancel onDeterminecancel) {
        this.onDeterminecancel = onDeterminecancel;
    }

    public static OnDeterminecancel onDeterminecancel;

    public interface OnDeterminecancel {
        void Determinecancel();
    }


    private static android.support.v7.app.AlertDialog orderTable = null;

    public static void ShowOrderTable(final DeskActivity mContext, final String order_id, final String nums, final boolean isedit) {
        android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(mContext, R.style.AlertDialog);
        View view = View.inflate(mContext, R.layout.table_order, null);
        TextView tv_number = (TextView) view.findViewById(R.id.tv_number);
        TextView tv_remarks = (TextView) view.findViewById(R.id.tv_remarks);
        TextView tv_Total_price = (TextView) view.findViewById(R.id.tv_Total_price);
        PagingListView mylv = (PagingListView) view.findViewById(R.id.mylv);
        tv_number.setText(nums);
        getOrderdetails(mContext, order_id, nums, tv_remarks, tv_Total_price, mylv, isedit);
        orderTable = alertDialog.setView(view).show();
        orderTable.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        orderTable.show();
    }

    //获取订单详情
    public static void getOrderdetails(final DeskActivity mContext, final String order_id, final String desk_num, final TextView tv_remarks, final TextView tv_Total_price, final PagingListView mylv, final boolean isedit) {
        final String[] mark_text = new String[1];
        final String[] total_amount = new String[1];
        Map<String, String> map = new HashMap<>();
        map.put("order_id", order_id);
        map.put("desk_num", desk_num);
        String url = SysUtils.getnewsellerUrl("Menu/getOrderInfo");
        CustomRequest r = new CustomRequest(Request.Method.POST, url, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                final List<DeskTableOrder> deskTableOrders = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    JSONObject ret = jsonObject.getJSONObject("response");
                    Log.d("print", "打印出来的商品详情为" + ret);
                    String status = ret.getString("status");
                    if (status.equals("0")) {
                        JSONObject data = ret.getJSONObject("data");
                        String order_id = data.getString("order_id");
                        mark_text[0] = data.getString("mark_text");
                        total_amount[0] = data.getString("total_amount");
                        JSONArray info = data.getJSONArray("info");
                        for (int i = 0; i < info.length(); i++) {
                            DeskTableOrder order = new DeskTableOrder();
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
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    tv_remarks.setText(mark_text[0].replace("订单备注", ""));
                    tv_Total_price.setText(StringUtils.round_down(total_amount[0], 2));
                    final TableorderAdapter adapter = new TableorderAdapter(mContext, deskTableOrders);
                    adapter.setIsedit(isedit);
                    adapter.SetOnClicks(new TableorderAdapter.SetOnClick() {
                        @Override
                        public void ondelete(int i) {
                            List<Map<String, String>> list = new ArrayList<>();
                            Map<String, String> map = new HashMap<>();
                            map.put("obj_id", deskTableOrders.get(i).getObj_id());
                            map.put("goods_id", "0");
                            map.put("num", "-" + deskTableOrders.get(i).getQuantity());
                            list.add(map);
                            Gson gson = new Gson();
                            String s = gson.toJson(list);
                            updateOrderGoods(mContext, desk_num, s);
                            deskTableOrders.remove(i);
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onEdit(int i, EditText editText) {
                            if (editText.getText().toString().equals("") || editText.getText().toString() == null) {
                                return;
                            }
                            boolean isadd = false;
                            int subtract = Integer.parseInt(editText.getText().toString()) - Integer.parseInt(deskTableOrders.get(i).getQuantity());

                            if (subtract > 0) {
                                isadd = true;
                            } else {
                                isadd = false;
                            }
                            List<Map<String, String>> list = new ArrayList<>();
                            Map<String, String> map = new HashMap<>();
                            if (isadd) {
                                map.put("obj_id", deskTableOrders.get(i).getObj_id());
                            } else {
                                map.put("obj_id", deskTableOrders.get(i).getObj_id());
                            }
                            if (isadd) {
                                map.put("goods_id", deskTableOrders.get(i).getGoods_id());
                            } else {
                                map.put("goods_id", "0");
                            }
                            if (isadd) {
                                map.put("num", "+" + subtract);
                            } else {
                                map.put("num", subtract + "");
                            }
                            list.add(map);
                            Gson gson = new Gson();
                            String s = gson.toJson(list);
                            updateOrderGoods(mContext, desk_num, s);
                        }
                    });
                    mylv.setAdapter(adapter);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        mContext.executeRequest(r);
//        OkGo.<String>post(SysUtils.getnewSellerService("Menu/getOrderInfo"))
//                .tag("Menu/getOrderInfo")
//                .params("order_id",order_id)
//                .params("desk_num",desk_num)
//                .execute(new StringCallback() {
//                    @Override
//                    public void onSuccess(Response<String> response) {
//                        try {
//                            JSONObject jsonObject=new JSONObject(response.body().toString());
//                            JSONObject ret=jsonObject.getJSONObject("response");
//                            String status=ret.getString("status");
//                            if (status.equals("0")){
//                                JSONObject data=ret.getJSONObject("data");
//                                String order_id=data.getString("order_id");
//                                mark_text[0] =data.getString("mark_text");
//                                total_amount[0] =data.getString("total_amount");
//                                JSONArray info=data.getJSONArray("info");
//                                for (int i=0;i<info.length();i++){
//                                    JSONObject object=info.getJSONObject(i);
//                                    Self_Service_GoodsInfo goodsInfo=new Self_Service_GoodsInfo();
//                                    goodsInfo.setGoods_id(object.getString("goods_id"));
//                                    goodsInfo.setName(object.getString("name"));
//                                    goodsInfo.setPrice(object.getString("price"));
//                                    goodsInfo.setNumber(object.getString("quantity"));
//                                    goodsInfo.setObj_id(object.getString("obj_id"));
//                                    goodsInfos.add(goodsInfo);
//                                }
//                            }
//                            tv_remarks.setText(mark_text[0]);
//                            tv_Total_price.setText(TlossUtils.round_down(total_amount[0],2));
//                            final TableorderAdapter adpater=new TableorderAdapter(mContext,goodsInfos);
//                            adpater.setType("1");
//                            adpater.setSetOnclick(new TableorderAdapter.SetOnclick() {
//                                @Override
//                                public void onclickdialog(int i) {
//                                }
//                                @Override
//                                public void OnClickDelete(int i) {
//                                    List<Map<String,String>> list=new ArrayList<>();
//                                    Map<String,String> map=new HashMap<>();
//                                    map.put("obj_id",goodsInfos.get(i).getObj_id());
//                                    map.put("goods_id","0");
//                                    map.put("num","-"+goodsInfos.get(i).getNumber());
//                                    list.add(map);
//                                    Gson gson=new Gson();
//                                    String s = gson.toJson(list);
//                                    Sqlite_Entity sqlite_entity=new Sqlite_Entity(mContext);
//                                    if (goodsInfos.size()==1){
//                                        sqlite_entity.deleteguadan(order_id);
//                                        sqlite_entity.update_Table(id,"0","0","0","");
//                                        if (orderTable!=null){
//                                            orderTable.dismiss();
//                                        }
//                                    }else {
//                                        sqlite_entity.DeleteTableGoods(order_id,goodsInfos.get(i).getGoods_id());
//                                    }
//                                    updateOrderGoods(mContext,desk_num,s,order_id,goodsInfos.get(i).getGoods_id(),(Integer.parseInt(goodsInfos.get(i).getNumber())-1)+"");
//                                    goodsInfos.remove(i);
//                                    adpater.notifyDataSetChanged();
//                                }
//                                //点击保存的数据
//                                @Override
//                                public void OnclickEditext(int i, EditText editText) {
//                                    if (editText.getText().toString().equals("")||editText.getText().toString()==null){
//                                        return;
//                                    }
//                                    boolean isadd=false;
//                                    int subtract=Integer.parseInt(editText.getText().toString())-Integer.parseInt(goodsInfos.get(i).getNumber());
//
//                                    if (subtract>0){
//                                        isadd=true;
//                                    }else {
//                                        isadd=false;
//                                    }
//                                    List<Map<String,String>> list=new ArrayList<>();
//                                    Map<String,String> map=new HashMap<>();
//                                    if (isadd){
//                                        map.put("obj_id",goodsInfos.get(i).getObj_id());
//                                    }else {
//                                        map.put("obj_id",goodsInfos.get(i).getObj_id());
//                                    }
//                                    if (isadd){
//                                        map.put("goods_id",goodsInfos.get(i).getGoods_id());
//                                    }else {
//                                        map.put("goods_id","0");
//                                    }
//                                    if (isadd){
//                                        map.put("num","+"+subtract);
//                                    }else {
//                                        map.put("num",subtract+"");
//                                    }
//                                    list.add(map);
//                                    Gson gson=new Gson();
//                                    String s = gson.toJson(list);
//                                    Log.d("print","打印出来的减少的数据为"+s);
//                                    updateOrderGoods(mContext,desk_num,s,order_id,goodsInfos.get(i).getGoods_id(),editText.getText().toString());
//                                }
//                            });
//                            mylv.setAdapter(adpater);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }finally {
//                            mylv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                                @Override
//                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                                    goodsInfos.get(position).getGoods_id();
//                                }
//                            });
//                        }
//                    }
//                });
    }


    //设置更新数据
    public static void updateOrderGoods(final DeskActivity mContext, String desk_num, String map) {
        String url = SysUtils.getnewsellerUrl("Menu/updateOrderGoods");
        Map<String, String> map1 = new HashMap<>();
        map1.put("desk_num", desk_num);
        map1.put("map", map);
        CustomRequest r = new CustomRequest(Request.Method.POST, url, map1, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject object = new JSONObject(response.toString());
                    JSONObject rep = object.getJSONObject("response");
                    String status = rep.getString("status");
                    if (status.equals("0")) {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        mContext.executeRequest(r);
//        OkGo.<String>post(SysUtils.getnewSellerService("Menu/updateOrderGoods"))
//                .tag("Menu/updateOrderGoods")
//                .params("desk_num",desk_num)
//                .params("map",map)
//                .execute(new StringCallback() {
//                    @Override
//                    public void onSuccess(Response<String> response) {
//                        Log.d("print","打印出编辑商品的接口"+response.toString());
//                        try {
//                            JSONObject object=new JSONObject(response.toString());
//                            JSONObject rep=object.getJSONObject("response");
//                            String status=rep.getString("status");
//                            if (status.equals("0")){
//                                JSONObject data=rep.getJSONObject("data");
//                            }else {
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
    }


}
