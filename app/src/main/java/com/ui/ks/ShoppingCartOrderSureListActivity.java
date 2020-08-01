package com.ui.ks;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.base.BaseActivity;
import com.ui.adapter.ShoppingCartOrderSureListAdapter;
import com.ui.entity.ShopperCartInfo_item;
import com.ui.util.SysUtils;

import java.util.ArrayList;

public class ShoppingCartOrderSureListActivity extends BaseActivity {

    private TextView btn_set;
    private View toolbar_layout;
    private ListView list_content;
    private ArrayList<ShopperCartInfo_item> ShopperCartInfo_items;
    private ShoppingCartOrderSureListAdapter mShoppingCartOrderSureListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart_order_sure_list);
        SysUtils.setupUI(ShoppingCartOrderSureListActivity.this,findViewById(R.id.activity_shopping_cart_order_sure_list));
        initToolbar(this);

        initView();
    }

    private void initView() {
        toolbar_layout=findViewById(R.id.toolbar_layout);
        btn_set= (TextView) toolbar_layout.findViewById(R.id.btn_set);
        btn_set.setVisibility(View.VISIBLE);
        btn_set.setBackgroundColor(Color.parseColor("#ffff8905"));
        list_content= (ListView) findViewById(R.id.list_content);

        Intent Intent=getIntent();
        if(Intent!=null){
            ShopperCartInfo_items=Intent.getParcelableArrayListExtra("ShopperCartInfo_items");
            mShoppingCartOrderSureListAdapter=new ShoppingCartOrderSureListAdapter(ShoppingCartOrderSureListActivity.this,ShopperCartInfo_items);
            list_content.setAdapter(mShoppingCartOrderSureListAdapter);
            //小计件数，价格
            int total_nums = 0;
            for (int i = 0; i < ShopperCartInfo_items.size(); i++) {
                String nums_src = ShopperCartInfo_items.get(i).getGoods_nums();
                int nums = Integer.parseInt(nums_src);
                total_nums += nums;
            }
            btn_set.setText(getString(R.string.all)+total_nums+getString(R.string.str309));
        }
    }
}
