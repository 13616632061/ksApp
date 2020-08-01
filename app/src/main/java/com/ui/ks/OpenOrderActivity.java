package com.ui.ks;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.base.BaseActivity;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.ui.adapter.MainBranchPagerAdapter;
import com.ui.adapter.ShoppingCarAdapter;
import com.ui.db.DBHelper;
import com.ui.entity.GetOpenOrder;
import com.ui.entity.GetOpenOrder_info;
import com.ui.entity.Goods_info;
import com.ui.entity.Order;
import com.ui.entity.OrderGoods;
import com.ui.fragment.BaseFragmentMainBranch;
import com.ui.fragment.ChooseGoodsFrament;
import com.ui.fragment.ChooseScanCodeFragment;
import com.ui.global.Global;
import com.ui.scancodetools.CameraManager;
import com.ui.util.CustomRequest;
import com.ui.util.NoDoubleClickUtils;
import com.ui.util.PreferencesService;
import com.ui.util.PrintUtil;
import com.ui.util.SetEditTextInput;
import com.ui.util.SysUtils;
import com.ui.view.MyListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 开单页面
 */
public class OpenOrderActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private ImageView iv_back,iv_search,iv_change,iv_putorder,iv_shoppingcar,iv_cell;
    private Button btn_shopping_num,btn_openorder_num,btn_choose_ok;
    private TextView tv_price_sum,tv_cell;
    private EditText et_inputgoodname;
    private RadioGroup mRadioGroup;
    private RadioButton  btn_good,btn_sort;
    private View btn_main_line;
    private View btn_branch_line;
    private ViewPager mViewPager;
    private List<BaseFragmentMainBranch> mBaseFragment;
    private ViewGroup anim_mask_layout;//动画层
    private ChooseGoodsFrament chooseGoodsFrament;
    private int type;
    private ShoppingCarAdapter shoppingCarAdapter;
    private ArrayList<Goods_info> shopingcarsinfoList;
    //底部数据
    private BottomSheetLayout bottomSheetLayout;
    private View bottomSheet;
    private MyListView lv_product;//购物车listview
    private ArrayList<GetOpenOrder> getOpenOrders;
    private ArrayList<GetOpenOrder_info> getOpenOrder_infos;
    private View view;//购物车的view
    private String order_print_id;//订单打印id
    private String order_mark;//订单备注
    private boolean is_order_edit=false;//是否是订单编辑
    private int openorder=0;//0表示直接买单，1表示挂单
    private String ordercreattime;//订单创建时间
    private PreferencesService service;//偏好设置
    private int change_type=1;//1表示列表样式，2表示方块样式
    private int isprint_type=1;//1表示手动打印，2表示自动打印
    private String order_id = "";
    private String sellername="";
    private String pay_status="0";//0表示未付款，1表示付款
    private String payed_time;
    private String order_mark_print;//
    private Order order;
    private String tel;
    private int num1;//挂单打印设置次数
    private int num2;//买单成功打印设置次数
    public ArrayList<OrderGoods> goodsList;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==201){
                if(malertDialog!=null) {
                    malertDialog.dismiss();
                }
                if(openorder==1&&isprint_type==2){
//                    startPrint();
//                    PrintUtil.startPrint(OpenOrderActivity.this,sellername,payed_time,pay_status,total_amount,
//                            tel,order_print_id,goodsList,order,num1);
                    new PrintUtil(OpenOrderActivity.this,sellername,payed_time,pay_status,total_amount,
                            tel,order_print_id,goodsList,order,num1,et_accountinfo_input_str);
                }
            }

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_order);
        SysUtils.setupUI(OpenOrderActivity.this,findViewById(R.id.activity_open_order));
        initToolbar(this);
        CameraManager.init(this);

        initFragment();
        initView();
        initListener();

    }
    private void initFragment() {
        mBaseFragment = new ArrayList<>();
        chooseGoodsFrament=new ChooseGoodsFrament();
        mBaseFragment.add(chooseGoodsFrament);
        mBaseFragment.add(new ChooseScanCodeFragment());

    }

    private void initView() {
        iv_back= (ImageView) findViewById(R.id.iv_back);
        iv_search= (ImageView)findViewById(R.id.iv_search);
        iv_change= (ImageView)findViewById(R.id.iv_change);
        iv_putorder= (ImageView)findViewById(R.id.iv_putorder);
        iv_shoppingcar= (ImageView)findViewById(R.id.iv_shoppingcar);
        btn_shopping_num= (Button) findViewById(R.id.btn_shopping_num);
        btn_openorder_num= (Button) findViewById(R.id.btn_openorder_num);
        btn_choose_ok= (Button) findViewById(R.id.btn_choose_ok);
        bottomSheetLayout= (BottomSheetLayout) findViewById(R.id.bottomSheetLayout);
        tv_price_sum= (TextView) findViewById(R.id.tv_price_sum);

        mRadioGroup = (RadioGroup)findViewById(R.id.rg_btn);
        btn_main_line = findViewById(R.id.btn_main_line);
        btn_branch_line = findViewById(R.id.btn_branch_line);
        mViewPager = (ViewPager)findViewById(R.id.fragment_content);

        btn_good= (RadioButton) findViewById(R.id.btn_good);
        btn_sort= (RadioButton) findViewById(R.id.btn_sort);
        btn_good.setText(getString(R.string.str31));
        btn_sort.setText(getString(R.string.str32));

         view = LayoutInflater.from(OpenOrderActivity.this).inflate(R.layout.layout_bottom_sheet,(ViewGroup) getWindow().getDecorView(),false);
        lv_product = (MyListView) view.findViewById(R.id.lv_product);

        et_inputgoodname = (EditText) findViewById(R.id.et_inputgoodname);
        iv_cell = (ImageView) findViewById(R.id.iv_cell);
        tv_cell= (TextView) findViewById(R.id.tv_cell);

        service=new PreferencesService(OpenOrderActivity.this);
        Map<String, String> params_change = service.getPerferences_change();
        change_type=Integer.valueOf(params_change.get("change"));
        Map<String, String> params_isprint = service.getPerferences_isprint();
        isprint_type=Integer.valueOf(params_isprint.get("isprint"));
        Map<String, String> params_seller_info = service.getPerferences_seller_name();
        sellername=String.valueOf(params_seller_info.get("seller_name"));
        tel=String.valueOf(params_seller_info.get("tel"));
        //获取打印设置次数
        Map<String, String> print_num=service.getPerferences_print_num();
        num1=Integer.valueOf(print_num.get("num1"));
        num2=Integer.valueOf(print_num.get("num2"));



        iv_back.setOnClickListener(this);
        iv_cell.setOnClickListener(this);
        tv_cell.setOnClickListener(this);
        btn_choose_ok.setOnClickListener(this);
        iv_search.setOnClickListener(this);
        iv_change.setOnClickListener(this);
        iv_putorder.setOnClickListener(this);
        iv_shoppingcar.setOnClickListener(this);
        mViewPager.setOnPageChangeListener(this);
        mViewPager.setAdapter(new MainBranchPagerAdapter(getSupportFragmentManager(),mBaseFragment));
//监听搜索输入框的输入变化
        et_inputgoodname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String goodsname=s.toString().trim();
                if(goodsname.length()>0){
                    iv_cell.setVisibility(View.VISIBLE);
                }else {
                    iv_cell.setVisibility(View.GONE);
                }
            }
        });
        //搜索输入框，键盘搜索按钮的监控
et_inputgoodname.setOnEditorActionListener(new TextView.OnEditorActionListener() {
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if(actionId== EditorInfo.IME_ACTION_SEARCH)
        {
            String goodsname=et_inputgoodname.getText().toString().trim();
            OpenOrderActivity.this.sendBroadcast(new Intent(Global.BROADCAST_ChooseGoodsFrament_ACTION).putExtra("type",6).putExtra("goodsname",goodsname));
            // search pressed and perform your functionality.
            if(isSoftShowing()){
                //再次调用软键盘消失
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        }





        return false;
    }
});
        shopingcarsinfoList=new ArrayList<>();
        getOpenOrders=new ArrayList<>();
        getOpenOrder_infos=new ArrayList<>();

        getopenorder_num();

        clearShoppingCarDB();

    }
    /**
     *编辑
     * @param goods_info
     */
    private void addopenorder(Goods_info goods_info){
//        clearShoppingCarDB();
        DBHelper dbHelper = DBHelper.getInstance(OpenOrderActivity.this);
        SQLiteDatabase sqlite = dbHelper.getWritableDatabase();
        String sql = "SELECT * FROM openorder WHERE id = ?";
        Cursor cursor = sqlite.rawQuery(sql, new String[] { goods_info.getGoods_id()});
        if(cursor.moveToFirst()){
            int num_db=cursor.getInt(cursor.getColumnIndex("num"));
            num_db++;
            sqlite.execSQL("UPDATE openorder SET num = ? where id="+goods_info.getGoods_id(),
                    new Object[] {num_db});
        }else {
            sqlite.execSQL("insert into openorder (id,tag_id,name,price,num,obj_id,item_id) values(?,?,?,?,?,?,?)",
                    new Object[]{goods_info.getGoods_id(),goods_info.getTag_id(),
                            goods_info.getName(),goods_info.getPrice(),goods_info.getSelect_num(),goods_info.getObj_id(),goods_info.getItem_id()});

        }
        cursor.close();
        sqlite.close();
    }

    /**
     * 订单处理提醒弹窗
     */
    AlertDialog mdo_alertDialog = null;
    private void  do_handle_alertDialog(){
        DBHelper dbHelper = DBHelper.getInstance(OpenOrderActivity.this);
        SQLiteDatabase sqlite = dbHelper.getWritableDatabase();
        String sql = "SELECT * FROM openorder";
        Cursor cursor = sqlite.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(OpenOrderActivity.this, R.style.AlertDialog)
                    .setMessage(getString(R.string.str33))
                    .setPositiveButton(getString(R.string.str34), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mdo_alertDialog.dismiss();
                        }
                    })
                    .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
            mdo_alertDialog = alertDialog.show();
            mdo_alertDialog.show();
        }else {
            finish();
        }
        cursor.close();
        sqlite.close();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                do_handle_alertDialog();
                break;
            case R.id.iv_cell:
                et_inputgoodname.setText("");
                break;
            case R.id.tv_cell:
                et_inputgoodname.setText("");
                iv_search.setVisibility(View.VISIBLE);
                iv_change.setVisibility(View.VISIBLE);
                iv_putorder.setVisibility(View.VISIBLE);
                iv_cell.setVisibility(View.GONE);
                tv_cell.setVisibility(View.GONE);
                et_inputgoodname.setVisibility(View.GONE);
                if(btn_openorder_num.getText().toString().length()>0){
                    btn_openorder_num.setVisibility(View.VISIBLE);
                }
                OpenOrderActivity.this.sendBroadcast(new Intent(Global.BROADCAST_ChooseGoodsFrament_ACTION).putExtra("type",7));

                if(isSoftShowing()){
                    //再次调用软键盘消失
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }
                break;
            case R.id.iv_search:
                iv_search.setVisibility(View.GONE);
                iv_change.setVisibility(View.GONE);
                iv_putorder.setVisibility(View.GONE);
                tv_cell.setVisibility(View.VISIBLE);
                et_inputgoodname.setVisibility(View.VISIBLE);
                btn_openorder_num.setVisibility(View.GONE);

                et_inputgoodname.setText("");
                et_inputgoodname.setFocusable(true);
                et_inputgoodname.setFocusableInTouchMode(true);
                et_inputgoodname.requestFocus();
                //软键盘显示
                imm = (InputMethodManager) OpenOrderActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                break;
            case R.id.iv_change:
                if(change_type==1){
                    change_type=2;
                    service.save_change(change_type);

                }else if(change_type==2){
                    change_type=1;
                    service.save_change(change_type);
                }
                ischange();
                break;
            case R.id.iv_putorder:
                DBHelper dbHelper = DBHelper.getInstance(OpenOrderActivity.this);
                SQLiteDatabase sqlite = dbHelper.getWritableDatabase();
                String sql = "SELECT * FROM openorder";
                Cursor cursor = sqlite.rawQuery(sql, null);
                if (cursor.moveToFirst()) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(OpenOrderActivity.this, R.style.AlertDialog)
                            .setMessage(getString(R.string.str33))
                            .setPositiveButton(getString(R.string.str34), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mdo_alertDialog.dismiss();
                                }
                            })
                            .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    clearShoppingCar();
                                    order_mark="";
                                    is_order_edit=false;
                                    Intent intent=new Intent(OpenOrderActivity.this,GetOpenOrderActivity.class);
                                    startActivity(intent);
                                }
                            });
                    mdo_alertDialog = alertDialog.show();
                    mdo_alertDialog.show();
                }else {
                    clearShoppingCar();
                    order_mark="";
                    is_order_edit=false;
                    Intent intent=new Intent(OpenOrderActivity.this,GetOpenOrderActivity.class);
                    startActivity(intent);
                }
                cursor.close();
                sqlite.close();
                break;
            case R.id.iv_shoppingcar:
                showBottomSheet();
                break;
            case R.id.btn_choose_ok:
//                order_mark="";
//                order_id="";
                openorder=0;
                shopingcarsinfoList.clear();
                DBHelper dbHelper1 = DBHelper.getInstance(OpenOrderActivity.this);
                SQLiteDatabase sqlite1 = dbHelper1.getWritableDatabase();
                String sql1 = "SELECT * FROM openorder";
                Cursor cursor1 = sqlite1.rawQuery(sql1, null);
                if (cursor1.moveToFirst()) {
                    do {
                        String id = cursor1.getString(cursor1.getColumnIndex("id"));
                        String name = cursor1.getString(cursor1.getColumnIndex("name"));
                        Double price = cursor1.getDouble(cursor1.getColumnIndex("price"));
                        int num_db = cursor1.getInt(cursor1.getColumnIndex("num"));
                        String obj_id = cursor1.getString(cursor1.getColumnIndex("obj_id"));
                        String item_id = cursor1.getString(cursor1.getColumnIndex("item_id"));
                        shopingcarsinfoList.add(new Goods_info(name, "", id, 0, 0, price, "", 0, num_db, "", false, 0,obj_id,item_id));
                    } while (cursor1.moveToNext());
                }
                cursor1.close();
                sqlite1.close();
                    shoppingCarAdapter = new ShoppingCarAdapter(OpenOrderActivity.this, shopingcarsinfoList);
                    lv_product.setAdapter(shoppingCarAdapter);
                if(shopingcarsinfoList!=null&&shopingcarsinfoList.size()>0) {
                    putOpenOrder();
                }
                break;
        }


    }

    /**
     * 商品展示样式切换
     */
    private void ischange(){
    DBHelper dbHelper = DBHelper.getInstance(OpenOrderActivity.this);
    SQLiteDatabase sqlite = dbHelper.getWritableDatabase();
    String sql = "SELECT * FROM openorder";
    Cursor cursor = sqlite.rawQuery(sql, null);
    if (cursor.moveToFirst()) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(OpenOrderActivity.this, R.style.AlertDialog)
                .setMessage(getString(R.string.str33)                            )
                .setPositiveButton(getString(R.string.str34), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mdo_alertDialog.dismiss();
                    }
                })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        clearShoppingCar();
                        order_mark="";
                        is_order_edit=false;
                        OpenOrderActivity.this.sendBroadcast(new Intent(Global.BROADCAST_ChooseGoodsFrament_ACTION).putExtra("type",4).putExtra("change_type",change_type));
                    }
                });
        mdo_alertDialog = alertDialog.show();
        mdo_alertDialog.show();
    }else {
        clearShoppingCar();
        order_mark="";
        is_order_edit=false;
        OpenOrderActivity.this.sendBroadcast(new Intent(Global.BROADCAST_ChooseGoodsFrament_ACTION).putExtra("type",4).putExtra("change_type",change_type));
    }
    cursor.close();
    sqlite.close();
}
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK )
        {
            do_handle_alertDialog();
        }

        return false;

    }
    /**
     * viewpager的左右滑动监听
     * @param position
     * @param positionOffset
     * @param positionOffsetPixels
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                mRadioGroup.check(R.id.btn_good);
                break;
            case 1:
                mRadioGroup.check(R.id.btn_sort);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * RadioGroup的选择监听
     */
    private void initListener() {
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.btn_good:
                        mViewPager.setCurrentItem(0);
                        btn_branch_line.setVisibility(View.INVISIBLE);
                        btn_main_line.setVisibility(View.VISIBLE);
                        break;
                    case R.id.btn_sort:
                            mViewPager.setCurrentItem(1);
                            btn_main_line.setVisibility(View.INVISIBLE);
                            btn_branch_line.setVisibility(View.VISIBLE);
                        break;

                    default:
                        mViewPager.setCurrentItem(0);
                        btn_branch_line.setVisibility(View.INVISIBLE);
                        btn_main_line.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

        Intent intent = getIntent();
        if (intent.getStringExtra("type")!=null){
            mRadioGroup.check(R.id.btn_sort);
        }else {
            mRadioGroup.check(R.id.btn_good);
        }

    }

    /**
     * 接收广播
     */
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
           View view=View.inflate(OpenOrderActivity.this,R.layout.imageview_point,null);
            /**
             * type:
             * 1表示商品列表增加商品数量时动画的飞出
             *  2表示商品列表商品减少
             * 2表示购物车列表选择数量增加
             * 3表示购物车列表选择数量减少
             * 7.finish
             *
             */
            type=intent.getIntExtra("type",0);
            if(type==7){
                finish();
            }
            if(type==1){
                int[] startLocation=intent.getIntArrayExtra("startLocation");
                if(startLocation!=null){
                    setAnim(view,startLocation);
                }
            }
            //查询总开单数量
            DBHelper dbHelper = DBHelper.getInstance(context);
            SQLiteDatabase sqlite = dbHelper.getWritableDatabase();
            String sql = "SELECT * FROM openorder";
            Cursor cursor = sqlite.rawQuery(sql, null);
             int sub_num=0;
            double sum_price=0.00;
            if(type==3||type==2){
                shopingcarsinfoList.clear();
            }
            if(cursor.moveToFirst()){
                do{
                    String id=cursor.getString(cursor.getColumnIndex("id"));
                    String name=cursor.getString(cursor.getColumnIndex("name"));
                    int num_db=cursor.getInt(cursor.getColumnIndex("num"));
                    double price_num=cursor.getDouble(cursor.getColumnIndex("price"));
                    String obj_id = cursor.getString(cursor.getColumnIndex("obj_id"));
                    String item_id = cursor.getString(cursor.getColumnIndex("item_id"));
                    String  tag_id=cursor.getString(cursor.getColumnIndex("tag_id"));
                    sub_num+=num_db;
                    sum_price+=(price_num*num_db);
                    if(type==3||type==2){
                        shopingcarsinfoList.add(new Goods_info(name,"",id,0,0,price_num,"",0,num_db,tag_id,false,0,obj_id,item_id));
                    }
                } while (cursor.moveToNext());
            }
            if(type==3){
                shoppingCarAdapter = new ShoppingCarAdapter(OpenOrderActivity.this,shopingcarsinfoList);
                lv_product.setAdapter(shoppingCarAdapter);
                shoppingCarAdapter.notifyDataSetChanged();
            }
            cursor.close();
            sqlite.close();
                if(sub_num>0){
                    tv_price_sum.setText("￥"+sum_price);
                    tv_price_sum.setText("￥"+ SetEditTextInput.stringpointtwo(sum_price+""));
                    btn_shopping_num.setVisibility(View.VISIBLE);
                    btn_shopping_num.setText(sub_num+"");
                    btn_choose_ok.setBackgroundResource(R.drawable.btn_corner_red);
                }else {
                    tv_price_sum.setText("￥"+"0.00");
                    btn_shopping_num.setVisibility(View.GONE);
                    btn_choose_ok.setBackgroundResource(R.drawable.btn_corner_gray);
                    if(shopingcarsinfoList.size()==0&&bottomSheetLayout.isSheetShowing()){
                        bottomSheetLayout.dismissSheet();
                    }
                }
            if(type==6){
                finish();
            }
        if(type==5){
            OpenOrderActivity.this.sendBroadcast(new Intent(Global.BROADCAST_ChooseGoodsFrament_ACTION).putExtra("type",5));
            order_mark="";
            order_id="";
            ordercreattime="";
            bottomSheetLayout.setVisibility(View.VISIBLE);
            getOpenOrders.clear();
            is_order_edit=true;
            Bundle bundle=intent.getBundleExtra("bundle");
            ArrayList arrayList1 =bundle.getParcelableArrayList("list1");
            ArrayList arrayList2 =bundle.getParcelableArrayList("list2");
            getOpenOrders.add((GetOpenOrder) arrayList1.get(0));
            order_id=getOpenOrders.get(0).getOrder_id();
            order_mark=getOpenOrders.get(0).getNotes();
            ordercreattime=getOpenOrders.get(0).getCreat_time();
            getOpenOrder_infos=arrayList2;
            int total_num=0;
            for(int i=0;i<getOpenOrder_infos.size();i++){
                total_num+=Integer.parseInt(getOpenOrder_infos.get(i).getNum());
                addopenorder(new Goods_info(getOpenOrder_infos.get(i).getName(),"",getOpenOrder_infos.get(i).getId(),0,0,
                        Double.parseDouble(getOpenOrder_infos.get(i).getPrice()),"",0,Integer.parseInt(getOpenOrder_infos.get(i).getNum()),getOpenOrder_infos.get(i).getTagid(),false,0,
                        getOpenOrder_infos.get(i).getObj_id(),getOpenOrder_infos.get(i).getItem_id()));
            }
            if(total_num>0){
                showBottomSheet();
                tv_price_sum.setText("￥"+getOpenOrders.get(0).getPrice());
                btn_shopping_num.setVisibility(View.VISIBLE);
                btn_shopping_num.setText(total_num+"");
                btn_choose_ok.setBackgroundResource(R.drawable.btn_corner_red);
            }else {
                tv_price_sum.setText("￥" + "0.00");
                btn_shopping_num.setVisibility(View.GONE);
                btn_choose_ok.setBackgroundResource(R.drawable.btn_corner_gray);
                if (bottomSheetLayout.isSheetShowing()) {
                    bottomSheetLayout.dismissSheet();
                }
            }
        }

        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            clearShoppingCarDB();
            OpenOrderActivity.this.unregisterReceiver(broadcastReceiver);
        } catch(Exception e) {

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        clearShoppingCarDB();
       OpenOrderActivity.this.registerReceiver(broadcastReceiver, new IntentFilter(Global.BROADCAST_OpenOrderActivity_ACTION));
    }

    /**
     * 清空数据表
     */
    private void clearShoppingCarDB(){
        DBHelper dbHelper = DBHelper.getInstance(OpenOrderActivity.this);
        SQLiteDatabase sqlite = dbHelper.getWritableDatabase();
        String sql = "DELETE FROM  openorder ";
        sqlite.execSQL(sql);
    }
    //创建购物车view
    private void showBottomSheet(){
        bottomSheet = createBottomSheetView();
        if(bottomSheetLayout.isSheetShowing()){
            bottomSheetLayout.dismissSheet();
        }else {
            if(shopingcarsinfoList.size()!=0){
                //默认设置为显示
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                        }
                    }).start();
                bottomSheetLayout.showWithSheetView(bottomSheet);
            }
        }
    }
    //查看购物车布局
    AlertDialog malertDialog_shoppingCar = null;
    private View createBottomSheetView(){
        LinearLayout layout_openorder= (LinearLayout) view.findViewById(R.id.layout_openorder);
        final LinearLayout layout_clear= (LinearLayout) view.findViewById(R.id.layout_clear);
        ImageView iv_clear= (ImageView) view.findViewById(R.id.iv_clear);
        TextView tv_clear= (TextView) view.findViewById(R.id.tv_clear);
        if(is_order_edit){
            layout_clear.setVisibility(View.GONE);
//            layout_clear.setOnClickListener(null);
//            tv_clear.setTextColor(Color.parseColor("#adadad"));
        }else {
            //清空
            layout_clear.setVisibility(View.VISIBLE);
            layout_clear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alertDialog=new AlertDialog.Builder(OpenOrderActivity.this,R.style.AlertDialog)
                            .setMessage(getString(R.string.str35)                            )
                            .setPositiveButton(getString(R.string.sure), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    clearShoppingCar();
                                }
                            })
                            .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    malertDialog_shoppingCar.dismiss();
                                }
                            });
                    malertDialog_shoppingCar=alertDialog.show();
                    malertDialog_shoppingCar.show();

                }
            });
        }
        //挂单
        layout_openorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                        bottomSheetLayout.dismissSheet();
                bottomSheetLayout.setVisibility(View.GONE);
                isaddmark(OpenOrderActivity.this);
            }
        });
            //查询总开单数量
            shopingcarsinfoList.clear();
            DBHelper dbHelper = DBHelper.getInstance(OpenOrderActivity.this);
            SQLiteDatabase sqlite = dbHelper.getWritableDatabase();
            String sql = "SELECT * FROM openorder";
            Cursor cursor = sqlite.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    String id = cursor.getString(cursor.getColumnIndex("id"));
                    String name = cursor.getString(cursor.getColumnIndex("name"));
                    Double price = cursor.getDouble(cursor.getColumnIndex("price"));
                    int num_db = cursor.getInt(cursor.getColumnIndex("num"));
                    String obj_id = cursor.getString(cursor.getColumnIndex("obj_id"));
                    String item_id = cursor.getString(cursor.getColumnIndex("item_id"));
                    shopingcarsinfoList.add(new Goods_info(name, "", id, 0, 0, price, "", 0, num_db, "", false, 0,obj_id,item_id));
                } while (cursor.moveToNext());
            }
            cursor.close();
            sqlite.close();
        shoppingCarAdapter = new ShoppingCarAdapter(OpenOrderActivity.this,shopingcarsinfoList);
        lv_product.setAdapter(shoppingCarAdapter);
        return view;
    }
    /**
     * @Description: 创建动画层
     * @param
     * @return void
     * @throws
     */
    private ViewGroup createAnimLayout() {
        ViewGroup rootView = (ViewGroup) OpenOrderActivity.this.getWindow().getDecorView();
        LinearLayout animLayout = new LinearLayout(OpenOrderActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        animLayout.setLayoutParams(lp);
        animLayout.setId(Integer.MAX_VALUE-1);
        animLayout.setBackgroundResource(android.R.color.transparent);
        rootView.addView(animLayout);
        return animLayout;
    }

    private View addViewToAnimLayout(final ViewGroup parent, final View view,
                                     int[] location) {
        int x = location[0];
        int y = location[1];
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = x;
        lp.topMargin = y;
        view.setLayoutParams(lp);
        return view;
    }

    public void setAnim(final View v, int[] startLocation) {
        anim_mask_layout = null;
        anim_mask_layout = createAnimLayout();
        anim_mask_layout.addView(v);//把动画小球添加到动画层
        final View view = addViewToAnimLayout(anim_mask_layout, v, startLocation);
        int[] endLocation = new int[2];// 存储动画结束位置的X、Y坐标
        iv_shoppingcar.getLocationInWindow(endLocation);
        // 计算位移
        int endX = 0 - startLocation[0] + 85;// 动画位移的X坐标
        int endY = endLocation[1] - startLocation[1];// 动画位移的y坐标

        TranslateAnimation translateAnimationX = new TranslateAnimation(0,endX, 0, 0);
        translateAnimationX.setInterpolator(new LinearInterpolator());
        translateAnimationX.setRepeatCount(0);// 动画重复执行的次数
        translateAnimationX.setFillAfter(true);

        TranslateAnimation translateAnimationY = new TranslateAnimation(0, 0, 0, endY);
        translateAnimationY.setInterpolator(new AccelerateInterpolator());
        translateAnimationY.setRepeatCount(0);// 动画重复执行的次数
        translateAnimationY.setFillAfter(true);

        AnimationSet set = new AnimationSet(false);
        set.setFillAfter(false);
        set.addAnimation(translateAnimationY);
        set.addAnimation(translateAnimationX);
        set.setDuration(500);// 动画的执行时间
        view.startAnimation(set);
        // 动画监听事件
        set.setAnimationListener(new Animation.AnimationListener() {
            // 动画的开始
            @Override
            public void onAnimationStart(Animation animation) {
                v.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub
            }

            // 动画的结束
            @Override
            public void onAnimationEnd(Animation animation) {
                v.setVisibility(View.GONE);
            }
        });

    }
    /**
     * 添加挂单备注
     * @param mContext
     */
    InputMethodManager imm;
    AlertDialog mAlertDialog;
    EditText et_accountinfo_input;
    boolean isshow=false;
    String et_accountinfo_input_str;
    CheckBox is_print;
    public  void isaddmark(final Context mContext){
        AlertDialog.Builder dialog=new AlertDialog.Builder(mContext);
        View view =View.inflate(mContext, R.layout.openorder_dialog,null);
        //自动打印
        is_print= (CheckBox) view.findViewById(R.id.is_print);
        if(isprint_type==1){
            is_print.setChecked(false);
        }else if(isprint_type==2){
            is_print.setChecked(true);
        }
        is_print.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    isprint_type=2;
                }else {
                    isprint_type=1;
                }
                service.save_isprint(isprint_type);

            }
        });
        et_accountinfo_input=(EditText)view.findViewById(R.id.et_input);
        et_accountinfo_input.requestFocus();
        et_accountinfo_input.setFocusable(true);
        if(!TextUtils.isEmpty(order_mark)){
            et_accountinfo_input.setText(order_mark);
            et_accountinfo_input.setSelection(order_mark.length());
        }
        if("null".equals(order_mark)){
            et_accountinfo_input.setText("");
        }
        //转换成数字键盘
//        et_accountinfo_input.setKeyListener(new BaseKeyListener() {
//            @Override
//            public int getInputType() {
//                return InputType.TYPE_CLASS_NUMBER;
//            }
//        });
        et_accountinfo_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                    String et=et_accountinfo_input.getText().toString().trim();
                if(et!=null){
                    if(et.length()>5){
                        String et_src=et.substring(0,5);
                        et_accountinfo_input.setText(et_src);
                        et_accountinfo_input.setSelection(et_src.length());
                    }
                }
            }
        });
        //软键盘显示
        imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        ImageView iv_close= (ImageView) view.findViewById(R.id.iv_close);
        TextView tv_sure=(TextView)view.findViewById(R.id.tv_sure);
        TextView tv_nomark=(TextView)view.findViewById(R.id.tv_nomark);
        //关闭
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAlertDialog.dismiss();
                if(isSoftShowing()){
                    //再次调用软键盘消失
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }
                bottomSheetLayout.setVisibility(View.VISIBLE);
//                showBottomSheet();
            }
        });
        //确定按钮
        tv_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(et_accountinfo_input.getText().toString().trim())){
                    openorder=1;
                    if (!NoDoubleClickUtils.isDoubleClick()) {{
                        putOpenOrder();
                    }}
                }else {
                    Toast.makeText(OpenOrderActivity.this,getString(R.string.str36),Toast.LENGTH_SHORT).show();
                }
            }
        });
        //不备注
        tv_nomark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openorder=1;
                if (!NoDoubleClickUtils.isDoubleClick()) {{
                    putOpenOrder();
                }}
            }
        });
        mAlertDialog= dialog.setView(view).show();
        mAlertDialog.setCancelable(false);
        mAlertDialog.show();
    }

    /**
     * 获取挂单订单总数
     */
    private void getopenorder_num(){
        CustomRequest r=new CustomRequest(CustomRequest.Method.POST, SysUtils.getSellerServiceUrl("get_order_totalnum"),null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    JSONObject ret = SysUtils.didResponse(jsonObject);
                    System.out.println("ret="+ret);
                    String status = ret.getString("status");
                    String message = ret.getString("message");
                    if(!status.equals("200")){
                        SysUtils.showError(message);
                    }else {
                        JSONObject data=ret.getJSONObject("data");
                        if(data!=null) {
                            int num = data.getInt("num");
                            if (num > 0) {
                                btn_openorder_num.setVisibility(View.VISIBLE);
                                btn_openorder_num.setText(num + "");
                            }
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
            }

        });
        executeRequest(r);

    }
    /**
     * 挂单
     */
    private JSONArray jsonArray;
    private String  total_amount;
    int orders_status;
    private void putOpenOrder(){
        if(openorder==1){
            et_accountinfo_input_str=et_accountinfo_input.getText().toString();
        }
        if(TextUtils.isEmpty(et_accountinfo_input_str)){
            et_accountinfo_input_str="null";
        }
        String  total_amount_str=tv_price_sum.getText().toString().trim();
        System.out.println("total_amount_str="+total_amount_str);
       total_amount=total_amount_str.replace("￥","");
        System.out.println("total_amount="+total_amount);

        final Map<String, String> Map=new HashMap<>();
        final ArrayList<Map<String,String>> mapArrayList=new ArrayList<>();
        goodsList=new ArrayList<>();
        for(int i=0;i<shopingcarsinfoList.size();i++){
            String goods_id=shopingcarsinfoList.get(i).getGoods_id();
            String name=shopingcarsinfoList.get(i).getName();
            double price=shopingcarsinfoList.get(i).getPrice();
            int nums=shopingcarsinfoList.get(i).getSelect_num();
            if(openorder==1){
                orders_status=1;//(0代表不挂单，1代表挂单)
            }else if(openorder==0){
                if(TextUtils.isEmpty(order_id)){
                    orders_status=0;//(0代表不挂单，1代表挂单)
                }else {
                    orders_status=1;//(0代表不挂单，1代表挂单)
                }
            }
            int pay_status=0;//付款款状态：pay_status（0:未支付;1:已支付;2:已付款至到担保方;3:部分付款;4:部分退款;5:全额退款）
            Map<String,String> map=new HashMap<>();
            map.put("goods_id",goods_id);
            name = name.replace(" ","");//将空格装换
            map.put("name", name);
            map.put("price",price+"");
            map.put("nums",nums+"");
            map.put("mark_text",et_accountinfo_input_str);
            map.put("orders_status",orders_status+"");
            map.put("pay_status",pay_status+"");
            if(is_order_edit){
                if(!TextUtils.isEmpty(shopingcarsinfoList.get(i).getItem_id())&&!TextUtils.isEmpty(shopingcarsinfoList.get(i).getObj_id())){
                    map.put("item_id",shopingcarsinfoList.get(i).getItem_id()+"");
                    map.put("obj_id", shopingcarsinfoList.get(i).getObj_id()+"");
                }
            }
            mapArrayList.add(map);
            OrderGoods orderGoods=new OrderGoods(nums,name,price);
            goodsList.add(orderGoods);
        }
        Map.put("map",mapArrayList.toString());
        JSONObject jsonObject=new JSONObject(Map);
        try {
            String map_str=jsonObject.getString("map");
            jsonArray=new JSONArray(map_str.replace("/","|"));
            Map.put("map",jsonArray+"");//转化为json数组的字符串
            Map.put("total_amount",total_amount+"");//总价
            if(is_order_edit){
                //订单编辑，增加order_id
                is_order_edit=false;
                Map.put("order_id", order_id);//订单id
            }
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("e:"+e.toString());
        }
        System.out.println("Map="+Map);
        CustomRequest r=new CustomRequest(CustomRequest.Method.POST, SysUtils.getSellerServiceUrl("shengcheng_order"),Map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                hideLoading();
                try {
                    JSONObject ret = SysUtils.didResponse(jsonObject);
                    System.out.println("商品ret="+ret);
                    String status = ret.getString("status");
                    String message = ret.getString("message");
                    if(!status.equals("200")){
                        SysUtils.showError(message);
                    }else {
                        if(openorder==1){
                            JSONObject data = ret.getJSONObject("data");
                            if(data!=null) {
                                order_print_id = data.getString("order_id");
                                payed_time = String.valueOf(data.getLong("time"));
                                JSONArray num_str = data.getJSONArray("num");
                                JSONObject num_obj = num_str.getJSONObject(0);
                                int num = Integer.parseInt(num_obj.getString("num"));
                                if(num>0){
                                    btn_openorder_num.setVisibility(View.VISIBLE);
                                    btn_openorder_num.setText(num+"");
                                }else {
                                    btn_openorder_num.setVisibility(View.GONE);
                                    btn_openorder_num.setText("");
                                }

                            }
                        }else if(openorder==0) {
                            if (orders_status == 0) {
                                JSONObject data = ret.getJSONObject("data");
                                if (data != null) {
                                    String order_id = data.getString("order_id");
                                    long time = data.getLong("time");
                                    JSONArray num_str = data.getJSONArray("num");
                                    JSONObject num_obj = num_str.getJSONObject(0);
                                    int num;
                                    if(TextUtils.isEmpty(num_obj.getString("num"))){
                                         num = Integer.parseInt(0+"");
                                    }else {
                                        num = Integer.parseInt(num_obj.getString("num"));
                                    }
                                    if (num > 0) {
                                        btn_openorder_num.setVisibility(View.VISIBLE);
                                        btn_openorder_num.setText(num + "");
                                    } else {
                                        btn_openorder_num.setVisibility(View.GONE);
                                        btn_openorder_num.setText("");
                                    }
                                    Intent intent = new Intent(OpenOrderActivity.this, SubmitOrderActivity.class);
                                    intent.putExtra("type", 2);
                                    intent.putExtra("order_id", order_id);
                                    intent.putExtra("time", time + "");
                                    intent.putExtra("total_price", total_amount + "");
                                    intent.putExtra("getOpenOrders", mapArrayList.toString());
                                    startActivity(intent);
                                }
                            }else if(orders_status == 1){//订单编辑选择直接买单
                                JSONObject data = ret.getJSONObject("data");
                                if(data!=null) {
                                    String order_id = data.getString("order_id");
                                    long time = data.getLong("time");
                                    JSONArray num_str = data.getJSONArray("num");
                                    JSONObject num_obj = num_str.getJSONObject(0);
                                    int num = Integer.parseInt(num_obj.getString("num"));
                                    if(num>0){
                                        btn_openorder_num.setVisibility(View.VISIBLE);
                                        btn_openorder_num.setText(num+"");
                                    }else {
                                        btn_openorder_num.setVisibility(View.GONE);
                                        btn_openorder_num.setText("");
                                    }
                                }
                                Intent intent = new Intent(OpenOrderActivity.this, SubmitOrderActivity.class);
                                intent.putExtra("type", 2);
                                intent.putExtra("order_id", order_id);
                                intent.putExtra("time", ordercreattime + "");
                                intent.putExtra("total_price", total_amount + "");
                                intent.putExtra("getOpenOrders", mapArrayList.toString());
                                startActivity(intent);

                            }
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {
                    order_id="";
                    order_mark="";
                    ordercreattime="";
                    clearShoppingCar();
                    if(mAlertDialog!=null){
                        mAlertDialog.dismiss();
                    }
                    if(imm!=null&&isSoftShowing()){
                        //再次调用软键盘消失
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    }
                    if(openorder==1){
                        creatDialogSuccess_Gtay();
                        handler.sendEmptyMessageDelayed(201,1000);
                    }
                    bottomSheetLayout.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                SysUtils.showNetworkError();
                hideLoading();
            }

        });
        executeRequest(r);
        showLoading(OpenOrderActivity.this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mAlertDialog!=null&& mAlertDialog.isShowing()){
            mAlertDialog.dismiss();
        }
    }

    //判断软键盘是否存在
    private boolean isSoftShowing() {
        //获取当前屏幕内容的高度
        int screenHeight = getWindow().getDecorView().getHeight();
        //获取View可见区域的bottom
        Rect rect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);

        return screenHeight - rect.bottom != 0;
    }
    /**
     * 清空购物车
     */
    private void clearShoppingCar(){
        order_mark="";
        order_id="";
        shopingcarsinfoList.clear();
        clearShoppingCarDB();
        btn_choose_ok.setBackgroundResource(R.drawable.btn_corner_gray);
        tv_price_sum.setText("￥"+"0.00");
        btn_shopping_num.setVisibility(View.GONE);
        if(shopingcarsinfoList.size()==0&&bottomSheetLayout.isSheetShowing()){
            bottomSheetLayout.dismissSheet();
        }
        OpenOrderActivity.this.sendBroadcast(new Intent(Global.BROADCAST_ChooseGoodsFrament_ACTION).putExtra("type",2));
    }

    /**
     * 挂单成功弹窗
     */
    AlertDialog malertDialog = null;
    private void creatDialogSuccess_Gtay(){
        View view=View.inflate(OpenOrderActivity.this,R.layout.alertdialog_success_gray,null);
        AlertDialog.Builder builder=new AlertDialog.Builder(OpenOrderActivity.this,R.style.AlertDialog_success_gray);
        malertDialog=builder.setView(view).show();
        malertDialog.show();
    }
}
