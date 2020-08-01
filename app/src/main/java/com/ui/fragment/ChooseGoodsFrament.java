package com.ui.fragment;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ui.adapter.ChooseGoodsSortListAdapter;
import com.ui.adapter.OpenOrderGoodListAdapter;
import com.ui.adapter.OpenOrderGoodSquareAdapter;
import com.ui.db.DBHelper;
import com.ui.entity.GoodSort;
import com.ui.entity.Goods_info;
import com.ui.entity.Goods_info_type;
import com.ui.global.Global;
import com.ui.ks.R;
import com.ui.listview.PagingListView;
import com.ui.listview.PinnedHeaderListView;
import com.ui.util.CustomRequest;
import com.ui.util.DialogUtils;
import com.ui.util.PreferencesService;
import com.ui.util.RequestManager;
import com.ui.util.SysUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/30.
 */

public class ChooseGoodsFrament extends BaseFragmentMainBranch{

    private View layout_err, include_nowifi, include_noresult;
    private Button load_btn_refresh_net, load_btn_retry;
    private TextView load_tv_noresult;

    private LinearLayout layout_choosegoodsframent;
    private PagingListView list_sort;
    private PinnedHeaderListView list_goods;
    private ArrayList<Goods_info_type> pruduct_info;
    private ArrayList<GoodSort> goodsortList;//商品类
    private ArrayList<Goods_info> goodsinfoList;//商品信息
    private ArrayList<Integer> goodsinfo_lenth;//分类总数
    private ChooseGoodsSortListAdapter chooseGoodsSortListAdapter;
    private OpenOrderGoodListAdapter openOrderGoodListAdapter;
    private OpenOrderGoodSquareAdapter openOrderGoodSquareAdapter;
    private int change_type=1;//1表示列表样式，2表示方块样式
    private boolean isScroll = true;
    private int type;
    private boolean isok=false;//数据是否加载完成
    private boolean order_edit=false;//是否为订单编辑
    private int type_code=0;
    private  int total_choose_sort_num=0;//选择该类别的商品总数
    private PreferencesService service;//偏好设置
    private String goodsname;
    @Override
    protected View initView() {
        View view=View.inflate(mContext, R.layout.choosegoodsframent,null);

        layout_err = (View) view.findViewById(R.id.layout_err);
        include_noresult = layout_err.findViewById(R.id.include_noresult);
        load_btn_retry = (Button) layout_err.findViewById(R.id.load_btn_retry);
        load_btn_retry.setVisibility(View.GONE);
        load_tv_noresult = (TextView) layout_err.findViewById(R.id.load_tv_noresult);
        load_tv_noresult.setText(getString(R.string.str37));
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
                getData();
            }
        });

        list_sort= (PagingListView) view.findViewById(R.id.list_sort);
        list_goods= (PinnedHeaderListView) view.findViewById(R.id.list_goods);
        layout_choosegoodsframent= (LinearLayout) view.findViewById(R.id.layout_choosegoodsframent);

        service=new PreferencesService(getActivity());
        Map<String, String> params = service.getPerferences_change();
        change_type=Integer.valueOf(params.get("change"));

        list_sort.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                isScroll = false;
                        chooseGoodsSortListAdapter.setDefSelect(position);

                int rightSection = 0;
                if(change_type==1) {
                    for (int i = 0; i < position; i++) {
                        rightSection += pruduct_info.get(i).getProduct_info().size() + 1;
                    }
                    list_goods.setSelection(rightSection);
                }else if(change_type==2){
                    for (int i = 0; i < position; i++) {
                        rightSection +=(int)(Math.ceil((float)pruduct_info.get(i).getProduct_info().size() / 3)) + 1;
                    }
                    list_goods.setSelection(rightSection);
                }
            }
        });
        //商品类别滑动
        list_goods.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                isScroll = true;
                switch (scrollState) {
                    case SCROLL_STATE_TOUCH_SCROLL:
                        new PauseOnScrollListener(ImageLoader.getInstance(), true, true);//滑动时，不加载图片
//                        isScroll = true;
                        break;
                    case SCROLL_STATE_FLING:
//                        isScroll= false;
                        break;
                    case SCROLL_STATE_IDLE:
//                        isScroll = false;
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (isScroll) {
                    if (pruduct_info != null) {
                        if(change_type==1&&openOrderGoodListAdapter!=null) {

                            for (int i = 0; i < pruduct_info.size(); i++) {
                                if (i == openOrderGoodListAdapter.getSectionForPosition(firstVisibleItem)) {
                                    if (i > visibleItemCount) {
                                        list_sort.smoothScrollToPosition(i);
                                    } else {
                                        list_sort.smoothScrollToPosition(0);
                                    }
                                    chooseGoodsSortListAdapter.setDefSelect(i);
                                } else {

                                }
                            }
                        }else if(change_type==2&&openOrderGoodSquareAdapter!=null){
                            for (int i = 0; i < pruduct_info.size(); i++) {
                                if (i == openOrderGoodSquareAdapter.getSectionForPosition(firstVisibleItem)) {
                                    if (i > visibleItemCount) {
                                        list_sort.smoothScrollToPosition(i);
                                    } else {
                                        list_sort.smoothScrollToPosition(0);
                                    }
                                    chooseGoodsSortListAdapter.setDefSelect(i);
                                } else {

                                }
                            }
                        }
                    } else {
                        isScroll = true;
                    }
                }
            }
        });

        return view;
    }
    /**
     * 接收广播
     */
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //查询总开单数量
            String id_position=intent.getStringExtra("id_position");
            /*
              *type:1表示增加
              * type 2类型表示购物车清空
              *  3表示购物车列表商品减少
              *  4表示更改商品列表样式
              *  5表示商品编辑
              *  6商品搜索
              *  7取消搜索
              *  8分类选择的商品总数
             */
            type=intent.getIntExtra("type",0);
            isok=intent.getBooleanExtra("isok",false);
            if(type==9){
                DBHelper dbHelper = DBHelper.getInstance(context);
                SQLiteDatabase sqlite = dbHelper.getWritableDatabase();

                String sql = "SELECT * FROM openorder";
                Cursor cursor = sqlite.rawQuery(sql, null);
                if (cursor.moveToFirst()) {
                    do {
                        String id = cursor.getString(cursor.getColumnIndex("id"));
                        int num_db = cursor.getInt(cursor.getColumnIndex("num"));
                        for(int j=0;j<pruduct_info.size();j++) {
                            for (int i = 0; i <  pruduct_info.get(j).getProduct_info().size(); i++) {
                                //商品存在，获取实际的数量
                                if ( pruduct_info.get(j).getProduct_info().get(i).getGoods_id().equals(id)) {
                                    pruduct_info.get(j).getProduct_info().get(i).setSelect_num(num_db);
                                    //扫码传过来的商品都是选中状态
                                    pruduct_info.get(j).getProduct_info().get(i).setIschoose(true);
                                }
                            }
                        }
                    } while (cursor.moveToNext());
                }

                for(int i=0;i<pruduct_info.size();i++){
                    for(int j=0;j<pruduct_info.get(i).getProduct_info().size();j++){
                        if(pruduct_info.get(i).getProduct_info().get(j).ischoose()){
                            String sql2 = "SELECT * FROM openorder WHERE tag_id = ?";
                            Cursor cursor2 = sqlite.rawQuery(sql2, new String[]{pruduct_info.get(i).getProduct_info().get(j).getTag_id()});
                            int total_choose_sort_num_total=0;
                            if (cursor2.moveToFirst()) {
                                do {
                                    int num_db = cursor2.getInt(cursor2.getColumnIndex("num"));
                                    total_choose_sort_num_total+=num_db;
                                    goodsortList.get(i).setChoose_num(total_choose_sort_num_total);
                                    chooseGoodsSortListAdapter.notifyDataSetChanged();

                                } while (cursor2.moveToNext());
                            }else {
                                goodsortList.get(i).setChoose_num(0);
                                chooseGoodsSortListAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                }
            }
            if(type==8){
                DBHelper dbHelper = DBHelper.getInstance(context);
                SQLiteDatabase sqlite = dbHelper.getWritableDatabase();
                for(int i=0;i<goodsortList.size();i++){
                    String sql2 = "SELECT * FROM openorder WHERE tag_id = ?";
                    Cursor cursor2 = sqlite.rawQuery(sql2, new String[]{goodsortList.get(i).getId()});
                    int total_choose_sort_num_total=0;
                    if (cursor2.moveToFirst()) {
                        do {
                            int num_db = cursor2.getInt(cursor2.getColumnIndex("num"));
                            total_choose_sort_num_total+=num_db;
                            goodsortList.get(i).setChoose_num(total_choose_sort_num_total);
                            chooseGoodsSortListAdapter.notifyDataSetChanged();

                        } while (cursor2.moveToNext());
                    }else {
                        goodsortList.get(i).setChoose_num(0);
                        chooseGoodsSortListAdapter.notifyDataSetChanged();
                    }

                }
            }
            if(type==7){
                if (change_type == 2) {
                    openOrderGoodSquareAdapter = new OpenOrderGoodSquareAdapter(getActivity(), pruduct_info, 0);
                    list_goods.setAdapter(openOrderGoodSquareAdapter);
                } else {
                    openOrderGoodListAdapter = new OpenOrderGoodListAdapter(getActivity(), pruduct_info, 0);
                    list_goods.setAdapter(openOrderGoodListAdapter);
                }
            }
            if(type==6) {
                goodsname = intent.getStringExtra("goodsname");
//                getData();
                ArrayList<Goods_info_type> pruduct_info1 = new ArrayList<>();
                for (int i = 0; i < pruduct_info.size(); i++) {
                    ArrayList<Goods_info> goodsinfoList1 = new ArrayList<>();
                    boolean ishas=false;
                    for (int j = 0; j < pruduct_info.get(i).getProduct_info().size(); j++) {
                        if (pruduct_info.get(i).getProduct_info().get(j).getName().indexOf(goodsname) != -1) {
                            goodsinfoList1.add(pruduct_info.get(i).getProduct_info().get(j));
                            ishas=true;
                        }
                    }
                    if(ishas){
                        pruduct_info1.add(new Goods_info_type(pruduct_info.get(i).getTag_id(), pruduct_info.get(i).getTag_name(), goodsinfoList1));
                    }
                }
                if (change_type == 2) {
                    openOrderGoodSquareAdapter = new OpenOrderGoodSquareAdapter(getActivity(), pruduct_info1, 0);
                    list_goods.setAdapter(openOrderGoodSquareAdapter);
                } else {
                    openOrderGoodListAdapter = new OpenOrderGoodListAdapter(getActivity(), pruduct_info1, 0);
                    list_goods.setAdapter(openOrderGoodListAdapter);
                }
            }
            if(type==5){
                order_edit=true;
            }
            if(type==4){
                change_type=intent.getIntExtra("change_type",2);
                if(change_type==2){
                    openOrderGoodSquareAdapter=new OpenOrderGoodSquareAdapter(getActivity(),pruduct_info,0);
                    list_goods.setAdapter(openOrderGoodSquareAdapter);
                }else if(change_type==1){
                    openOrderGoodListAdapter = new OpenOrderGoodListAdapter(getActivity(), pruduct_info,0);
                    list_goods.setAdapter(openOrderGoodListAdapter);
                }
            }else {
                DBHelper dbHelper = DBHelper.getInstance(context);
                SQLiteDatabase sqlite = dbHelper.getWritableDatabase();
                if (type == 3) {
                    String sql2 = "SELECT * FROM openorder WHERE id = ?";
                    Cursor cursor2 = sqlite.rawQuery(sql2, new String[]{id_position});
                    if (!cursor2.moveToFirst()) {
                        for(int j=0;j<pruduct_info.size();j++){
                            for (int i = 0; i < pruduct_info.get(j).getProduct_info().size(); i++) {
                                if (pruduct_info.get(j).getProduct_info().get(i).getGoods_id().equals(id_position)) {
                                    pruduct_info.get(j).getProduct_info().get(i).setIschoose(false);
                                    pruduct_info.get(j).getProduct_info().get(i).setSelect_num(0);
                                }
                            }
                        }
                    }
                    cursor2.close();
                }
                String sql = "SELECT * FROM openorder";
                Cursor cursor = sqlite.rawQuery(sql, null);
//                int total_choose_sort_num_total=0;
                if (cursor.moveToFirst()) {
                    do {
                        String id = cursor.getString(cursor.getColumnIndex("id"));
                        String name = cursor.getString(cursor.getColumnIndex("name"));
                        int num_db = cursor.getInt(cursor.getColumnIndex("num"));
                        double price_num = cursor.getDouble(cursor.getColumnIndex("price"));
                        String obj_id = cursor.getString(cursor.getColumnIndex("obj_id"));
                        String item_id = cursor.getString(cursor.getColumnIndex("item_id"));
                        String  tag_id=cursor.getString(cursor.getColumnIndex("tag_id"));
                        for(int j=0;j<pruduct_info.size();j++) {
                            for (int i = 0; i <  pruduct_info.get(j).getProduct_info().size(); i++) {
                                //商品存在，获取实际的数量
                                if ( pruduct_info.get(j).getProduct_info().get(i).getGoods_id().equals(id)) {
                                    pruduct_info.get(j).getProduct_info().get(i).setSelect_num(num_db);
                                    //订单编辑传过来的商品都是选中状态
                                    if(order_edit){
                                        pruduct_info.get(j).getProduct_info().get(i).setIschoose(true);
                                    }
                                }
                            }
                        }
                    } while (cursor.moveToNext());
                } else {
                    for(int i=0;i<goodsortList.size();i++){
                            total_choose_sort_num=0;
                            goodsortList.get(i).setChoose_num(total_choose_sort_num);
                            chooseGoodsSortListAdapter.notifyDataSetChanged();

                    }
                    //开单数据表不存在此商品，改变数组中此商品为未选择，切选择的数量为0；
                    for(int j=0;j<pruduct_info.size();j++) {
                        for (int i = 0; i < pruduct_info.get(j).getProduct_info().size(); i++) {
                            if (pruduct_info.get(j).getProduct_info().get(i).ischoose()) {
                                pruduct_info.get(j).getProduct_info().get(i).setIschoose(false);
                                pruduct_info.get(j).getProduct_info().get(i).setSelect_num(0);
                            }
                        }
                    }
                }
                /**
                 * 订单编辑
                 */
                if(type==5){
                    order_edit=false;
                    isok=false;
                    if(change_type==1){
                        openOrderGoodListAdapter = new OpenOrderGoodListAdapter(getActivity(), pruduct_info,1);
                        list_goods.setAdapter(openOrderGoodListAdapter);
                    }else if(change_type==2){
                        openOrderGoodSquareAdapter=new OpenOrderGoodSquareAdapter(getActivity(), pruduct_info,1);
                        list_goods.setAdapter(openOrderGoodSquareAdapter);
                    }
                    //编辑进入开单页面，商品分类选择商品的总数显示
                    for(int i=0;i<pruduct_info.size();i++){
                        for(int j=0;j<pruduct_info.get(i).getProduct_info().size();j++){
                            if(pruduct_info.get(i).getProduct_info().get(j).ischoose()){
                                String sql2 = "SELECT * FROM openorder WHERE tag_id = ?";
                                Cursor cursor2 = sqlite.rawQuery(sql2, new String[]{pruduct_info.get(i).getProduct_info().get(j).getTag_id()});
                                int total_choose_sort_num_total=0;
                                if (cursor2.moveToFirst()) {
                                    do {
                                        int num_db = cursor2.getInt(cursor2.getColumnIndex("num"));
                                        total_choose_sort_num_total+=num_db;
                                        goodsortList.get(i).setChoose_num(total_choose_sort_num_total);
                                        chooseGoodsSortListAdapter.notifyDataSetChanged();

                                    } while (cursor2.moveToNext());
                                }else {
                                    goodsortList.get(i).setChoose_num(0);
                                    chooseGoodsSortListAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    }
                }
                if(type==2||type==1||type==3){
                    if(change_type==1){
                        openOrderGoodListAdapter.notifyDataSetChanged();
                    }else if(change_type==2){
                        openOrderGoodSquareAdapter.notifyDataSetChanged();
                    }
                }
                cursor.close();
                sqlite.close();
            }
        }
    };
    @Override
    protected void initData() {
        super.initData();
        mContext.registerReceiver(broadcastReceiver, new IntentFilter(Global.BROADCAST_ChooseGoodsFrament_ACTION));
            getData();


    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            getActivity().unregisterReceiver(broadcastReceiver);
        } catch(Exception e) {

        }
    }

    private void jsonData(){
        goodsortList=new ArrayList<>();
        goodsinfo_lenth=new ArrayList<>();
        pruduct_info=new ArrayList<>();
        int num=0;
        try {
            InputStreamReader isr = new InputStreamReader(mContext.getAssets().open("meituan.json"),"UTF-8");
            BufferedReader br = new BufferedReader(isr);
            String line;
            StringBuilder builder = new StringBuilder();
            while((line = br.readLine()) != null){
                builder.append(line);
            }
            br.close();
            isr.close();
            JSONObject testjson = new JSONObject(builder.toString());//builder读取了JSON中的数据。
            //直接传入JSONObject来构造一个实例
            JSONObject data=testjson.getJSONObject("data");
            JSONArray array = data.getJSONArray("food_spu_tags");
            for (int i = 0; i < array.length(); i++) {
                JSONObject role = array.getJSONObject(i);    //取出数组中的对象
                String tag_name=role.getString("name");
                String tag=role.getString("tag");
                GoodSort goodSort=new GoodSort(tag_name,"id"+i,0);
                goodsortList.add(goodSort);
                JSONArray spus = role.getJSONArray("spus");

                goodsinfoList=new ArrayList<>();
//                goodList_Item_check=new ArrayList<>();
                for(int j = 0; j < spus.length(); j++){
                    JSONObject role2 = spus.getJSONObject(j);
                    String name=role2.getString("name");
                    double min_price=role2.getDouble("min_price");
                    int month_saled=role2.getInt("month_saled");
                    String picture=role2.getString("picture");
                    String id=role2.getString("id");
                        Goods_info goodsInfo=new Goods_info(name,"",id,month_saled,0,min_price,picture,0,0,tag,false,0,"","");
                        goodsinfoList.add(goodsInfo);
//                        OpenOrderGoodList_Choose goodList_item_check=new OpenOrderGoodList_Choose(id,false,0);
//                        goodList_Item_check.add(goodList_item_check);
                }
                Goods_info_type goods_info_type=new Goods_info_type("id"+i,tag_name,goodsinfoList);
                pruduct_info.add(goods_info_type);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            chooseGoodsSortListAdapter=new ChooseGoodsSortListAdapter(getActivity(),goodsortList);
            list_sort.setAdapter(chooseGoodsSortListAdapter);
            chooseGoodsSortListAdapter.setDefSelect(0);
                 if(change_type==2){
                     if(openOrderGoodSquareAdapter==null){
//                         openOrderGoodSquareAdapter=new OpenOrderGoodSquareAdapter(getActivity(),pruduct_info,goodList_Item_check);
//                         list_goods.setAdapter(openOrderGoodSquareAdapter);
                     }else {
                         openOrderGoodListAdapter.notifyDataSetChanged();
                     }
                 }else {

                        if(openOrderGoodListAdapter==null){
                                openOrderGoodListAdapter=new OpenOrderGoodListAdapter(getActivity(),pruduct_info,0);
                                list_goods.setAdapter(openOrderGoodListAdapter);
                            }else {
                                openOrderGoodListAdapter.notifyDataSetChanged();
                            }
                        }
                    }
    }

    /**
     * 获取商品数据
     */
    private Dialog progressDialog = null;
    private void getData(){
        goodsortList=new ArrayList<>();
        pruduct_info=new ArrayList<>();
        CustomRequest r=new CustomRequest(CustomRequest.Method.POST, SysUtils.getGoodsServiceUrl("goods_app"), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if(progressDialog != null) {
                    progressDialog.dismiss();
                    progressDialog = null;
                }
                try {
                    JSONObject ret = SysUtils.didResponse(jsonObject);
                    System.out.println("商品ret="+ret);
                    String status = ret.getString("status");
                    String message = ret.getString("message");
                    JSONObject  data=null;
                    if(!status.equals("200")){
                        SysUtils.showError(message);
                    }else {
                        data=ret.getJSONObject("data");
                        JSONArray catagory_infos=data.getJSONArray("catagory_infos");
                        if(catagory_infos!=null){
                            String tag_id="";
                            String tag_name="";
                            for(int i=0;i<catagory_infos.length();i++){
                                JSONObject order_info=catagory_infos.getJSONObject(i);
                                JSONArray cat=order_info.getJSONArray("cat");
                                if(cat!=null){
                                    for(int j=0;j<cat.length();j++){
                                        JSONObject tag=cat.getJSONObject(j);
                                        tag_name=tag.getString("tag_name");
                                        tag_id=tag.getString("tag_id");
                                        if(type!=6){
                                            GoodSort goodSort=new GoodSort(tag_name,tag_id,0);
                                            goodsortList.add(goodSort);
                                        }
                                    }
                                }
                                JSONArray pro_info = null;
                                try{
                                     pro_info=order_info.getJSONArray("pro_info");
                                }catch (Exception e){
                                    
                                }
                                goodsinfoList=new ArrayList<>();
                                if(pro_info!=null){
                                    for(int w=0;w<pro_info.length();w++){
                                        JSONObject good_info=pro_info.getJSONObject(w);
                                        String name=good_info.getString("name");
                                        String price=good_info.getString("price");
                                        String store=good_info.getString("store");
                                        String img_src=good_info.getString("img_src");
                                        String buy_count=good_info.getString("buy_count");
                                        String goods_id=good_info.getString("goods_id");
                                        Goods_info goodsInfo=new Goods_info(name,"",goods_id,Integer.parseInt(buy_count),Double.parseDouble(store),Double.parseDouble(price),img_src,0,0,tag_id,false,0,"","");
                                        goodsinfoList.add(goodsInfo);
                                    }
                                }
                                Goods_info_type goods_info_type=new Goods_info_type(tag_id,tag_name,goodsinfoList);
                                pruduct_info.add(goods_info_type);
                            }
                        }
                        setView();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("e="+e.toString());
                }finally {
                    if(type!=6) {
                        chooseGoodsSortListAdapter = new ChooseGoodsSortListAdapter(getActivity(), goodsortList);
                        list_sort.setAdapter(chooseGoodsSortListAdapter);
                        chooseGoodsSortListAdapter.setDefSelect(0);
                    }
                    if(change_type==2){
                        if(openOrderGoodSquareAdapter==null){
                         openOrderGoodSquareAdapter=new OpenOrderGoodSquareAdapter(getActivity(),pruduct_info,0);
                         list_goods.setAdapter(openOrderGoodSquareAdapter);
                        }else {
                            openOrderGoodListAdapter.notifyDataSetChanged();
                        }
                    }else {
                        if(openOrderGoodListAdapter==null){
                            openOrderGoodListAdapter=new OpenOrderGoodListAdapter(getActivity(),pruduct_info,0);
                            list_goods.setAdapter(openOrderGoodListAdapter);
                        }else {
                            openOrderGoodListAdapter.notifyDataSetChanged();
                        }
//                        getActivity().sendBroadcast(new Intent(Global.BROADCAST_ChooseGoodsFrament_ACTION).putExtra("isok",true));
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(progressDialog != null) {
                    progressDialog.dismiss();
                    progressDialog = null;
                }
                setNoNetwork();
            }

        });
        RequestManager.addRequest(r, mContext);
        progressDialog = DialogUtils.createLoadingDialog(mContext, getString(R.string.str92), true);
        progressDialog.show();
    }
    private void setView() {
            if(pruduct_info.size() < 1) {
                //没有结果
                layout_choosegoodsframent.setVisibility(View.GONE);
                include_nowifi.setVisibility(View.GONE);
                include_noresult.setVisibility(View.VISIBLE);
                layout_err.setVisibility(View.VISIBLE);
            } else {
                //有结果
                include_noresult.setVisibility(View.GONE);
                include_nowifi.setVisibility(View.GONE);
                layout_err.setVisibility(View.GONE);
                layout_choosegoodsframent.setVisibility(View.VISIBLE);
            }
    }

    private void setNoNetwork() {
        //网络不通
        if(pruduct_info.size() < 1) {
            if(!include_nowifi.isShown()) {
                layout_choosegoodsframent.setVisibility(View.GONE);
                include_noresult.setVisibility(View.GONE);
                include_nowifi.setVisibility(View.VISIBLE);
                layout_err.setVisibility(View.VISIBLE);
            }
        } else {
            SysUtils.showNetworkError();
        }
    }
}
