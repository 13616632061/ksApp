package com.ui.fragment;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ui.adapter.ShoppingMallShoppingCarFragmentAdapter;
import com.ui.entity.Goods_info;
import com.ui.entity.ShopperCartInfo;
import com.ui.entity.ShopperCartInfo_item;
import com.ui.entity.ShoppingMallShoppingCar;
import com.ui.entity.ShoppingMallShoppingCar_info;
import com.ui.global.Global;
import com.ui.ks.R;
import com.ui.ks.ShoppingCartOrderSureActivity;
import com.ui.listview.PagingListView;
import com.library.utils.BigDecimalArith;
import com.ui.util.CustomRequest;
import com.ui.util.DialogUtils;
import com.ui.util.SysUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/8.
 */

public class ShoppingMallShoppingCarFragment extends BaseFragmentMainBranch implements View.OnClickListener {
    private View layout_err, include_nowifi, include_noresult;
    private Button load_btn_refresh_net, load_btn_retry;
    private TextView load_tv_noresult;
    private ImageView iv_back;
    private TextView tv_edit,tv_goaccount,tv_order_total_money,tv_remove,tv_totalname;
    private CheckBox checkbox_all;
    private PagingListView lv_content;
    private ArrayList<ShoppingMallShoppingCar> shoppingMallShoppingCars;
    private ArrayList<ShoppingMallShoppingCar_info> shoppingMallShoppingCar_infos;
    private ShoppingMallShoppingCarFragmentAdapter shoppingMallShoppingCarFragmentAdapter;
    private String shopperid;
    private String nums;
    private String amount;
    private ArrayList<Goods_info> shoppingcar_goodsinfolist;
    private Dialog progressDialog = null;
    private ArrayList<ShopperCartInfo_item> shopperCartInfo_items;
    private ArrayList<ShopperCartInfo_item> shopperCartInfo_items_isSeclect;
    private ArrayList<ShopperCartInfo> shopperCartInfos;
    private ArrayList<ShopperCartInfo> shopperCartInfos_isSeclect;
    @Override
    protected View initView() {
        View view=View.inflate(mContext, R.layout.shoppingmallshoppingcarfragment,null);
        initview(view);
        return view;
    }

    @Override
    protected void initData() {
        super.initData();
//        getShoppingCart_list();
    }

    private void initview(View view) {
        layout_err = (View) view.findViewById(R.id.layout_err);
        include_noresult = layout_err.findViewById(R.id.include_noresult);
        load_btn_retry = (Button) layout_err.findViewById(R.id.load_btn_retry);
        load_btn_retry.setVisibility(View.GONE);
        load_tv_noresult = (TextView) layout_err.findViewById(R.id.load_tv_noresult);
        load_tv_noresult.setText("还没有订单记录哦");
        load_tv_noresult.setCompoundDrawablesWithIntrinsicBounds(
                0, //left
                R.drawable.icon_no_result_melt, //top
                0, //right
                0//bottom
        );
        include_nowifi = layout_err.findViewById(R.id.include_nowifi);
        load_btn_refresh_net = (Button) include_nowifi.findViewById(R.id.load_btn_refresh_net);
        load_btn_refresh_net.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //重新加载数据
                getShoppingCart_list();
            }
        });
        iv_back= (ImageView) view.findViewById(R.id.iv_back);
        tv_edit= (TextView) view.findViewById(R.id.tv_edit);
        tv_order_total_money= (TextView) view.findViewById(R.id.tv_order_total_money);
        tv_goaccount= (TextView) view.findViewById(R.id.tv_goaccount);
        tv_remove= (TextView) view.findViewById(R.id.tv_remove);
        tv_totalname= (TextView) view.findViewById(R.id.tv_totalname);
        lv_content= (PagingListView) view.findViewById(R.id.lv_content);
        checkbox_all= (CheckBox) view.findViewById(R.id.checkbox_all);

        iv_back.setOnClickListener(this);
        checkbox_all.setOnClickListener(this);
        tv_edit.setOnClickListener(this);
        tv_goaccount.setOnClickListener(this);
        tv_remove.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                getActivity().sendBroadcast(new Intent(Global.BROADCAST_WholeSaleOrdersActivity_ACTION).putExtra("type",1));
                break;
            case R.id.tv_remove:
                editShoppingCart_list();
                break;
            case R.id.tv_goaccount:
                if(shopperCartInfos_isSeclect!=null&&shopperCartInfos_isSeclect.size()>0){
                    Intent intent=new Intent(getActivity(), ShoppingCartOrderSureActivity.class);
                    intent.putParcelableArrayListExtra("shopperCartInfos_isSeclect",shopperCartInfos_isSeclect);
                    startActivity(intent);
                }else {
                    Toast.makeText(getActivity(),"选择的商品不能为空！",Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.tv_edit:
                if("编辑".equals(tv_edit.getText().toString().trim())){
                    tv_edit.setText("完成");
                    tv_goaccount.setVisibility(View.GONE);
                    tv_order_total_money.setVisibility(View.GONE);
                    tv_totalname.setVisibility(View.GONE);
                    tv_remove.setVisibility(View.VISIBLE);
                }else if("完成".equals(tv_edit.getText().toString().trim())){
                    tv_edit.setText("编辑");
                    tv_goaccount.setVisibility(View.VISIBLE);
                    tv_order_total_money.setVisibility(View.VISIBLE);
                    tv_totalname.setVisibility(View.VISIBLE);
                    tv_remove.setVisibility(View.GONE);
                    setshopperCartInfos_isSeclect();
                }
                break;
            case R.id.checkbox_all:
                if(checkbox_all.isChecked()){
                    for(int i=0;i<shopperCartInfos.size();i++){
                        for(int j=0;j<shopperCartInfos.get(i).getShopperCartInfo_items().size();j++){
                            shopperCartInfos.get(i).setSelect(true);
                            shopperCartInfos.get(i).getShopperCartInfo_items().get(j).setSelect(true);
                        }
                    }
                }else{
                    for(int i=0;i<shopperCartInfos.size();i++){
                        for(int j=0;j<shopperCartInfos.get(i).getShopperCartInfo_items().size();j++){
                            shopperCartInfos.get(i).setSelect(false);
                            shopperCartInfos.get(i).getShopperCartInfo_items().get(j).setSelect(false);
                        }
                    }

                }
                shoppingMallShoppingCarFragmentAdapter.notifyDataSetChanged();
                getActivity().sendBroadcast(new Intent(Global.BROADCAST_ShoppingMallShoppingCarFragment_ACTION).putExtra("type",1));
                break;
        }
    }
    /**
     * 编辑购物车数据
     */
    private void editShoppingCart_list(){
        JSONArray jsonArray=null;
        ArrayList<Map<String,String>> mapArrayList=new ArrayList<>();
        for(int i=0;i<shopperCartInfos.size();i++){
            for(int j=0;j<shopperCartInfos.get(i).getShopperCartInfo_items().size();j++){
                if(shopperCartInfos.get(i).getShopperCartInfo_items().get(j).isSelect()){
                  String cart_id=shopperCartInfos.get(i).getCart_id();
                  String supply_id =shopperCartInfos.get(i).getSeller_id();
                  String goods_id  =shopperCartInfos.get(i).getShopperCartInfo_items().get(j).getGoods_id();
                  String goods_nums  =shopperCartInfos.get(i).getShopperCartInfo_items().get(j).getGoods_nums();
                  String goods_price  =shopperCartInfos.get(i).getShopperCartInfo_items().get(j).getGoods_price();
                    Map<String,String> map1=new HashMap<>();
                    map1.put("cart_id",cart_id);
                    map1.put("supply_id",supply_id);
                    map1.put("goods_id",goods_id);
//                    map1.put("goods_nums",goods_nums);
//                    map1.put("goods_price",goods_price);
                    mapArrayList.add(map1);
                }
            }
        }
        Map<String,String> map=new HashMap<>();
        map.put("delete_shopperCartInfos",mapArrayList.toString());
        JSONObject mJSONObject=new JSONObject(map);
        try {
            String map_str=mJSONObject.getString("delete_shopperCartInfos");
            jsonArray=new JSONArray(map_str.replace("/","|"));
            map.put("delete_shopperCartInfos",jsonArray+"");
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("e="+e.toString());
        }
        System.out.println("map="+map);
        CustomRequest r = new CustomRequest(Request.Method.POST, SysUtils.getSellerServiceUrl("cart_delete"),map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if(progressDialog != null) {
                    progressDialog.dismiss();
                    progressDialog = null;
                }
                try {
                    JSONObject ret = SysUtils.didResponse(jsonObject);
                    System.out.println("编辑ret："+ret);
                    String status = ret.getString("status");
                    String message = ret.getString("message");
                    if (!status.equals("200")) {
                        SysUtils.showError(message);
                    } else {
                        getShoppingCart_list();
                        tv_order_total_money.setText("￥"+0.00);
                        tv_goaccount.setText("去结算("+0+")");
                    }

                } catch(Exception e) {
                    e.printStackTrace();
                } finally {
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if(progressDialog != null) {
                    progressDialog.dismiss();
                    progressDialog = null;
                }
                SysUtils.showError("网络不给力");
            }
        });
        executeRequest(r);

    }

    /**
     * 获取购物车数据
     */
    private void getShoppingCart_list(){
        CustomRequest r = new CustomRequest(Request.Method.POST, SysUtils.getSellerServiceUrl("cart_list"),null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if(progressDialog != null) {
                    progressDialog.dismiss();
                    progressDialog = null;
                }
                try {
                    JSONObject ret = SysUtils.didResponse(jsonObject);
                    System.out.println("ret："+ret);
                    String status = ret.getString("status");
                    String message = ret.getString("message");
                    if (!status.equals("200")) {
                        SysUtils.showError(message);
                    } else {
                        JSONArray data=ret.getJSONArray("data");
                        if(data!=null){
                            shopperCartInfos=new ArrayList<>();
                            for(int i=0;i<data.length();i++){
                                JSONObject data_object=data.getJSONObject(i);
                                String cart_id=data_object.getString("cart_id");
//                                String total_nums=data_object.getString("nums");
//                                String total_amount=data_object.getString("amount");
                                String seller_id=data_object.getString("seller_id");
                                String seller_name=data_object.getString("seller_name");
                                String freight=data_object.getString("freight");
                                String reward=data_object.getString("reward");
                                JSONArray commodity=null;
                                try {
                                   commodity=data_object.getJSONArray("commodity");
                                }catch (Exception e){
                                    System.out.println("e="+e.toString());
                                }
//                                if(commodity!=null){
                                    shopperCartInfo_items=new ArrayList<>();
                                    for(int j=0;j<commodity.length();j++){
                                        JSONObject commodity_object=commodity.getJSONObject(j);
                                        String goods_id=commodity_object.getString("goods_id");
                                        String goods_name=commodity_object.getString("goods_name");
                                        String goods_nums=commodity_object.getString("goods_nums");
                                        String goods_price=commodity_object.getString("goods_price");
                                        String brief=commodity_object.getString("brief");
                                        if(TextUtils.isEmpty(brief)||brief==null||"null".equals(brief)){
                                            brief="";
                                        }
                                        String img_src=commodity_object.getString("img_src");
                                        ShopperCartInfo_item shopperCartInfo_item=new ShopperCartInfo_item(goods_id,goods_name,goods_nums,goods_price,brief,img_src,false);
                                        //同一种商品合并
                                        if(shopperCartInfo_items.size()>0){
                                            boolean ishas=false;
                                            for(int a=0;a<shopperCartInfo_items.size();a++){
                                                if(shopperCartInfo_items.get(a).getGoods_id().equals(shopperCartInfo_item.getGoods_id())){
                                                    String num_src=shopperCartInfo_items.get(a).getGoods_nums();
                                                    int nums=Integer.parseInt(num_src);
                                                    ishas=true;
                                                    shopperCartInfo_items.get(a).setGoods_nums((nums+Integer.parseInt(shopperCartInfo_item.getGoods_nums()))+"");
                                                }
                                            }
                                            if(!ishas){
                                                shopperCartInfo_items.add(shopperCartInfo_item);
                                            }
                                        }else {
                                            shopperCartInfo_items.add(shopperCartInfo_item);
                                        }
                                    }
//                                }
                                ShopperCartInfo shopperCartInfo=new ShopperCartInfo(cart_id,"","",seller_id,seller_name,freight,reward,false,true,"","",shopperCartInfo_items);
                                shopperCartInfos.add(shopperCartInfo);
                            }
                        }
                        shoppingMallShoppingCarFragmentAdapter=new ShoppingMallShoppingCarFragmentAdapter(getActivity(),shopperCartInfos);
                        lv_content.setAdapter(shoppingMallShoppingCarFragmentAdapter);
                    }

                } catch(Exception e) {
                    e.printStackTrace();
                } finally {
                    setView();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if(progressDialog != null) {
                    progressDialog.dismiss();
                    progressDialog = null;
                }
                setNoNetwork();
            }
        });
        executeRequest(r);
        progressDialog = DialogUtils.createLoadingDialog(mContext, getString(R.string.str92), true);
        progressDialog.show();

    }
private BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
        int type=intent.getIntExtra("type",0);
        if(type==1){
            setshopperCartInfos_isSeclect();
        }

    }
};

    /**
     * 运算选中商品的数量及总价
     */
    private void setshopperCartInfos_isSeclect(){
        if(shopperCartInfos_isSeclect!=null){
            shopperCartInfos_isSeclect.clear();
        }
        shopperCartInfos_isSeclect=new ArrayList<>();
        double total_money=0.00;
        int total_select_nums=0;
        for(int i=0;i<shopperCartInfos.size();i++){
            double money=0.00;
            int select_nums=0;
            shopperCartInfo_items_isSeclect=new ArrayList<>();
            for(int j=0;j<shopperCartInfos.get(i).getShopperCartInfo_items().size();j++){

                if(shopperCartInfos.get(i).getShopperCartInfo_items().get(j).isSelect()){
                    String price=shopperCartInfos.get(i).getShopperCartInfo_items().get(j).getGoods_price();
                    String nums=shopperCartInfos.get(i).getShopperCartInfo_items().get(j).getGoods_nums();
                    double price_double=Double.parseDouble(price);
                    int nums_double=Integer.parseInt(nums);
                    money= BigDecimalArith.add(BigDecimalArith.mul(price_double,nums_double),money);
                    select_nums+=nums_double;
                    ShopperCartInfo_item shopperCartInfo_item=new ShopperCartInfo_item(shopperCartInfos.get(i).getShopperCartInfo_items().get(j).getGoods_id(),
                            shopperCartInfos.get(i).getShopperCartInfo_items().get(j).getGoods_name(),nums,price,shopperCartInfos.get(i).getShopperCartInfo_items().get(j).getBrief(),
                            shopperCartInfos.get(i).getShopperCartInfo_items().get(j).getImg_src(),true);
                    shopperCartInfo_items_isSeclect.add(shopperCartInfo_item);
                }
            }
            if(shopperCartInfo_items_isSeclect.size()>0) {
                ShopperCartInfo shopperCartInfo = new ShopperCartInfo(shopperCartInfos.get(i).getCart_id(), select_nums + "",
                        money + "", shopperCartInfos.get(i).getSeller_id(), shopperCartInfos.get(i).getSeller_name(),
                        shopperCartInfos.get(i).getFreight(),shopperCartInfos.get(i).getReward(), shopperCartInfos.get(i).isSelect(),true,"","" ,shopperCartInfo_items_isSeclect);
                shopperCartInfos_isSeclect.add(shopperCartInfo);
            }
            total_money=BigDecimalArith.add(money,total_money);
            total_select_nums+=select_nums;
        }
        tv_order_total_money.setText("￥"+total_money);
        tv_goaccount.setText("去结算("+(int)total_select_nums+")");
    }
    @Override
    public void onResume() {
        super.onResume();
        getShoppingCart_list();
        mContext.registerReceiver(broadcastReceiver,new IntentFilter(Global.BROADCAST_ShoppingMallShoppingCarFragment_ACTION));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(broadcastReceiver);
    }
    private void setView() {
        if(shopperCartInfos==null){
            shopperCartInfos=new ArrayList<>();
        }
            if(shopperCartInfos.size() < 1) {
                //没有结果
                lv_content.setVisibility(View.GONE);
                include_nowifi.setVisibility(View.GONE);
                include_noresult.setVisibility(View.VISIBLE);
                layout_err.setVisibility(View.VISIBLE);
            } else {
                //有结果
                include_noresult.setVisibility(View.GONE);
                include_nowifi.setVisibility(View.GONE);
                layout_err.setVisibility(View.GONE);
                lv_content.setVisibility(View.VISIBLE);
            }
    }

    private void setNoNetwork() {
        if(shopperCartInfos==null){
            shopperCartInfos=new ArrayList<>();
        }
        //网络不通
        if(shopperCartInfos.size() < 1) {
            if(!include_nowifi.isShown()) {
                lv_content.setVisibility(View.GONE);
                include_noresult.setVisibility(View.GONE);
                include_nowifi.setVisibility(View.VISIBLE);
                layout_err.setVisibility(View.VISIBLE);
            }
        } else {
            SysUtils.showNetworkError();
        }
    }
}
