package com.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ui.entity.ShopperCartInfo;
import com.ui.entity.ShopperCartInfo_item;
import com.ui.global.Global;
import com.ui.ks.R;
import com.ui.ks.ShoppingCartOrderSureListActivity;
import com.library.utils.BigDecimalArith;
import com.ui.util.DateUtils;
import com.ui.util.DialogUtils;
import com.ui.util.SetEditTextInput;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/6/22.
 */

public class ShoppingCartOrderSureAdapter extends BaseAdapter {
    private Activity context;
    private ArrayList<ShopperCartInfo> shopperCartInfos_isSeclect;
    private DisplayImageOptions options;
    private InputMethodManager imm;

    public ShoppingCartOrderSureAdapter(Activity context, ArrayList<ShopperCartInfo> shopperCartInfos_isSeclect) {
        this.context = context;
        this.shopperCartInfos_isSeclect = shopperCartInfos_isSeclect;
        options=new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.picture_default)
                .showImageForEmptyUri(R.drawable.picture_default)
                .cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new RoundedBitmapDisplayer(300))
                .build();
    }

    @Override
    public int getCount() {
        if(shopperCartInfos_isSeclect==null){
            return 0;
        }
        return shopperCartInfos_isSeclect.size();
    }

    @Override
    public Object getItem(int position) {
        return shopperCartInfos_isSeclect.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_shopingcartordersure, null);
            holder = new Holder();

            holder.iv_logo = (ImageView) convertView.findViewById(R.id.iv_logo);
            holder.tv_shoppername = (TextView) convertView.findViewById(R.id.tv_shoppername);
            holder.iv_shoppingpicture1 = (ImageView) convertView.findViewById(R.id.iv_shoppingpicture1);
            holder.iv_shoppingpicture2 = (ImageView) convertView.findViewById(R.id.iv_shoppingpicture2);
            holder.iv_shoppingpicture3= (ImageView) convertView.findViewById(R.id.iv_shoppingpicture3);
            holder.tv_shoppingname1= (TextView) convertView.findViewById(R.id.tv_shoppingname1);
            holder.tv_shoppingname2= (TextView) convertView.findViewById(R.id.tv_shoppingname2);
            holder.tv_shoppingname3= (TextView) convertView.findViewById(R.id.tv_shoppingname3);
            holder.tv_shoppingprice1= (TextView) convertView.findViewById(R.id.tv_shoppingprice1);
            holder.tv_shoppingprice2= (TextView) convertView.findViewById(R.id.tv_shoppingprice2);
            holder.tv_shoppingprice3= (TextView) convertView.findViewById(R.id.tv_shoppingprice3);
            holder.tv_nums1= (TextView) convertView.findViewById(R.id.tv_nums1);
            holder.tv_nums2= (TextView) convertView.findViewById(R.id.tv_nums2);
            holder.tv_nums3= (TextView) convertView.findViewById(R.id.tv_nums3);
            holder.layout1= (RelativeLayout) convertView.findViewById(R.id.layout1);
            holder.layout2= (RelativeLayout) convertView.findViewById(R.id.layout2);
            holder.layout3= (RelativeLayout) convertView.findViewById(R.id.layout3);
            holder.layout_moregoods= (RelativeLayout) convertView.findViewById(R.id.layout_moregoods);

            holder.tv_sendmoney = (TextView) convertView.findViewById(R.id.tv_sendmoney);
            holder.layout_sendtime = (RelativeLayout) convertView.findViewById(R.id.layout_sendtime);
            holder.radbtn_take = (RadioButton) convertView.findViewById(R.id.radbtn_take);
            holder.radbtn_send = (RadioButton) convertView.findViewById(R.id.radbtn_send);
            holder.et_sendmessage = (EditText) convertView.findViewById(R.id.et_sendmessage);
            holder.tv_money = (TextView) convertView.findViewById(R.id.tv_money);
            holder.tv_nums = (TextView) convertView.findViewById(R.id.tv_nums);
            holder.tv_take = (TextView) convertView.findViewById(R.id.tv_take);
            holder.tv_send = (TextView) convertView.findViewById(R.id.tv_send);
            holder.tv_sendtime = (TextView) convertView.findViewById(R.id.tv_sendtime);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        if (shopperCartInfos_isSeclect.get(position).getShopperCartInfo_items().size() > 0){
            if(shopperCartInfos_isSeclect.get(position).getShopperCartInfo_items().size()==1){
                holder.layout1.setVisibility(View.VISIBLE);
                holder.layout2.setVisibility(View.GONE);
                holder.layout3.setVisibility(View.GONE);
                //加载图片
                ImageLoader.getInstance().displayImage(shopperCartInfos_isSeclect.get(position).getShopperCartInfo_items().get(0).getImg_src(),holder.iv_shoppingpicture1,options);
                holder.tv_shoppingname1.setText(shopperCartInfos_isSeclect.get(position).getShopperCartInfo_items().get(0).getGoods_name());
                holder.tv_shoppingprice1.setText("￥"+shopperCartInfos_isSeclect.get(position).getShopperCartInfo_items().get(0).getGoods_price());
                holder.tv_nums1.setText("x "+shopperCartInfos_isSeclect.get(position).getShopperCartInfo_items().get(0).getGoods_nums());
            } if(shopperCartInfos_isSeclect.get(position).getShopperCartInfo_items().size()==2){
                holder.layout1.setVisibility(View.VISIBLE);
                holder.layout2.setVisibility(View.VISIBLE);
                holder.layout3.setVisibility(View.GONE);
                //加载图片
                ImageLoader.getInstance().displayImage(shopperCartInfos_isSeclect.get(position).getShopperCartInfo_items().get(0).getImg_src(),holder.iv_shoppingpicture1,options);
                holder.tv_shoppingname1.setText(shopperCartInfos_isSeclect.get(position).getShopperCartInfo_items().get(0).getGoods_name());
                holder.tv_shoppingprice1.setText("￥"+shopperCartInfos_isSeclect.get(position).getShopperCartInfo_items().get(0).getGoods_price());
                holder.tv_nums1.setText("x "+shopperCartInfos_isSeclect.get(position).getShopperCartInfo_items().get(0).getGoods_nums());
                //加载图片
                ImageLoader.getInstance().displayImage(shopperCartInfos_isSeclect.get(position).getShopperCartInfo_items().get(1).getImg_src(),holder.iv_shoppingpicture2,options);
                holder.tv_shoppingname2.setText(shopperCartInfos_isSeclect.get(position).getShopperCartInfo_items().get(1).getGoods_name());
                holder.tv_shoppingprice2.setText("￥"+shopperCartInfos_isSeclect.get(position).getShopperCartInfo_items().get(1).getGoods_price());
                holder.tv_nums2.setText("x "+shopperCartInfos_isSeclect.get(position).getShopperCartInfo_items().get(1).getGoods_nums());
            } if(shopperCartInfos_isSeclect.get(position).getShopperCartInfo_items().size()>=3){
                holder.layout1.setVisibility(View.VISIBLE);
                holder.layout2.setVisibility(View.VISIBLE);
                holder.layout3.setVisibility(View.VISIBLE);
                //加载图片
                ImageLoader.getInstance().displayImage(shopperCartInfos_isSeclect.get(position).getShopperCartInfo_items().get(0).getImg_src(),holder.iv_shoppingpicture1,options);
                holder.tv_shoppingname1.setText(shopperCartInfos_isSeclect.get(position).getShopperCartInfo_items().get(0).getGoods_name());
                holder.tv_shoppingprice1.setText("￥"+shopperCartInfos_isSeclect.get(position).getShopperCartInfo_items().get(0).getGoods_price());
                holder.tv_nums1.setText("x "+shopperCartInfos_isSeclect.get(position).getShopperCartInfo_items().get(0).getGoods_nums());
                //加载图片
                ImageLoader.getInstance().displayImage(shopperCartInfos_isSeclect.get(position).getShopperCartInfo_items().get(1).getImg_src(),holder.iv_shoppingpicture2,options);
                holder.tv_shoppingname2.setText(shopperCartInfos_isSeclect.get(position).getShopperCartInfo_items().get(1).getGoods_name());
                holder.tv_shoppingprice2.setText("￥"+shopperCartInfos_isSeclect.get(position).getShopperCartInfo_items().get(1).getGoods_price());
                holder.tv_nums2.setText("x "+shopperCartInfos_isSeclect.get(position).getShopperCartInfo_items().get(1).getGoods_nums());
                //加载图片
                ImageLoader.getInstance().displayImage(shopperCartInfos_isSeclect.get(position).getShopperCartInfo_items().get(2).getImg_src(),holder.iv_shoppingpicture3,options);
                holder.tv_shoppingname3.setText(shopperCartInfos_isSeclect.get(position).getShopperCartInfo_items().get(2).getGoods_name());
                holder.tv_shoppingprice3.setText("￥"+shopperCartInfos_isSeclect.get(position).getShopperCartInfo_items().get(2).getGoods_price());
                holder.tv_nums3.setText("x "+shopperCartInfos_isSeclect.get(position).getShopperCartInfo_items().get(2).getGoods_nums());
            }
            holder.tv_shoppername.setText(shopperCartInfos_isSeclect.get(position).getSeller_name());
            final Holder finalHolder1 = holder;
        //留言
        holder.et_sendmessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!DialogUtils.isSoftShowing(context)) {
                    imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }
            }
        });
            holder.et_sendmessage.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    if (!DialogUtils.isSoftShowing(context)) {
                        imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    }
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!DialogUtils.isSoftShowing(context)) {
                        imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (!DialogUtils.isSoftShowing(context)) {
                        imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    }
                    shopperCartInfos_isSeclect.get(position).setMessage(finalHolder1.et_sendmessage.getText().toString().trim());
                }
            });
            //小计件数，价格
            int total_nums = 0;
            double total_moneys = 0.00;
            for (int i = 0; i < shopperCartInfos_isSeclect.get(position).getShopperCartInfo_items().size(); i++) {
                String nums_src = shopperCartInfos_isSeclect.get(position).getShopperCartInfo_items().get(i).getGoods_nums();
                String money_src = shopperCartInfos_isSeclect.get(position).getShopperCartInfo_items().get(i).getGoods_price();
                int nums = Integer.parseInt(nums_src);
                double moneys = Double.parseDouble(money_src);
                total_nums += nums;
                total_moneys = BigDecimalArith.add(total_moneys, (moneys * nums));
            }
            holder.tv_nums.setText(context.getString(R.string.all) + total_nums + context.getString(R.string.str304));
            //配送费
            setsendmoney(total_moneys,position,holder.tv_sendmoney,holder.tv_money);
        //默认配送
            if(shopperCartInfos_isSeclect.get(position).isSend()){
                finalHolder1.radbtn_send.setChecked(true);
                finalHolder1.radbtn_send.setTextColor(Color.parseColor("#ffff8905"));
                finalHolder1.tv_take.setTextColor(Color.parseColor("#000000"));
                shopperCartInfos_isSeclect.get(position).setSend(true);
                setsendmoney(total_moneys,position,holder.tv_sendmoney,holder.tv_money);
            }else {
                finalHolder1.radbtn_take.setChecked(true);
                finalHolder1.tv_take.setTextColor(Color.parseColor("#ffff8905"));
                finalHolder1.tv_send.setTextColor(Color.parseColor("#000000"));
                shopperCartInfos_isSeclect.get(position).setSend(false);
                holder.tv_sendmoney.setText(context.getString(R.string.str310));//免配送费
            }
        //选择自提
            final double finalTotal_moneys1 = total_moneys;
            holder.radbtn_take.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finalHolder1.radbtn_take.setChecked(true);
                    finalHolder1.tv_take.setTextColor(Color.parseColor("#ffff8905"));
                    finalHolder1.tv_send.setTextColor(Color.parseColor("#000000"));
                    shopperCartInfos_isSeclect.get(position).setSend(false);
                    finalHolder1.tv_sendmoney.setText(context.getString(R.string.str310));//免配送费
                    finalHolder1.tv_money.setText("￥" + finalTotal_moneys1);
                    if(finalHolder1.radbtn_take.isChecked()&&finalTotal_moneys1<Double.parseDouble(shopperCartInfos_isSeclect.get(position).getFreight())){
                        context.sendBroadcast(new Intent(Global.BROADCAST_ShoppingCartOrderSureActivity_ACTION).putExtra("type",1).putExtra("Reward",shopperCartInfos_isSeclect.get(position).getReward()));
                    }
                }
            });
        //选择配送
            holder.radbtn_send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finalHolder1.radbtn_send.setChecked(true);
                    finalHolder1.radbtn_send.setTextColor(Color.parseColor("#ffff8905"));
                    finalHolder1.tv_take.setTextColor(Color.parseColor("#000000"));
                    shopperCartInfos_isSeclect.get(position).setSend(true);
                    setsendmoney(finalTotal_moneys1,position,finalHolder1.tv_sendmoney,finalHolder1.tv_money);
                    if(finalHolder1.radbtn_send.isChecked()&&finalTotal_moneys1<Double.parseDouble(shopperCartInfos_isSeclect.get(position).getFreight())){
                        context.sendBroadcast(new Intent(Global.BROADCAST_ShoppingCartOrderSureActivity_ACTION).putExtra("type",2).putExtra("Reward",shopperCartInfos_isSeclect.get(position).getReward()));
                    }
                }
            });

        //预约送货时间
        holder.layout_sendtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateUtils.runTime(context, finalHolder1.tv_sendtime);
            }
        });
            holder.tv_sendtime.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    shopperCartInfos_isSeclect.get(position).setSendtime(finalHolder1.tv_sendtime.getText().toString().trim());
                }
            });
            //查看更多商品
            holder.layout_moregoods.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(shopperCartInfos_isSeclect.get(position).getShopperCartInfo_items().size()>3){
                        Intent Intent=new Intent(context,ShoppingCartOrderSureListActivity.class);
                        Intent.putParcelableArrayListExtra("ShopperCartInfo_items",shopperCartInfos_isSeclect.get(position).getShopperCartInfo_items());
                        context.startActivity(Intent);
                    }else {
                        Toast.makeText(context,context.getString(R.string.str311),Toast.LENGTH_SHORT).show();//没有更多商品
                    }

                }
            });

    }
        return convertView;
    }
    //配送费
    private void setsendmoney(double total_moneys,int position,TextView tv_sendmoney,TextView tv_money){
        if(total_moneys<Double.parseDouble(shopperCartInfos_isSeclect.get(position).getFreight())){
            tv_sendmoney.setText("配送费"+SetEditTextInput.stringpointtwo(shopperCartInfos_isSeclect.get(position).getReward())+"元");
            tv_money.setText("￥" + BigDecimalArith.add(total_moneys,Double.parseDouble(SetEditTextInput.stringpointtwo(shopperCartInfos_isSeclect.get(position).getReward()))));

        }else {
            tv_sendmoney.setText("免配送费");
            tv_money.setText("￥" + total_moneys);
        }
    }
    public class Holder{
        ImageView iv_logo,iv_shoppingpicture1,iv_shoppingpicture2,iv_shoppingpicture3;
        TextView tv_shoppername,tv_shoppingname1,tv_shoppingname2,tv_shoppingname3;
        TextView tv_sendtime,tv_shoppingprice1,tv_shoppingprice2,tv_shoppingprice3;
        TextView tv_sendmoney,tv_nums1,tv_nums2,tv_nums3;
        RelativeLayout layout_sendtime,layout1,layout2,layout3,layout_moregoods;
        RadioButton radbtn_take;
        RadioButton radbtn_send;
        EditText et_sendmessage;
        TextView tv_money;
        TextView tv_nums;
        TextView tv_take;
        TextView tv_send;
    }
    /***
     * 动态设置listview的高度 item 总布局必须是linearLayout
     *
     * @param listView
     */
    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1))
                + 85;
        listView.setLayoutParams(params);
    }
    private class Item_ShoppingCartOrderSureAdapter extends BaseAdapter{
        private Context context;
        private ArrayList<ShopperCartInfo_item> shopperCartInfo_items_isSeclect;
        private DisplayImageOptions options;

        public Item_ShoppingCartOrderSureAdapter(Context context, ArrayList<ShopperCartInfo_item> shopperCartInfo_items_isSeclect) {
            this.context = context;
            this.shopperCartInfo_items_isSeclect = shopperCartInfo_items_isSeclect;
            options=new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.book)
                    .showImageForEmptyUri(R.drawable.book)
                    .cacheInMemory(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .displayer(new RoundedBitmapDisplayer(20))
                    .build();
        }

        @Override
        public int getCount() {
            if(shopperCartInfo_items_isSeclect==null){
                return 0;
            }
            return shopperCartInfo_items_isSeclect.size();
        }

        @Override
        public Object getItem(int position) {
            return shopperCartInfo_items_isSeclect.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            holder holder=null;
            if(convertView==null){
                convertView=View.inflate(context, R.layout.item_shoppingcaifragment_item,null);
                holder=new holder();
                holder.check_shopping= (CheckBox) convertView.findViewById(R.id.check_shopping);
                holder.iv_shoppingpicture= ( ImageView) convertView.findViewById(R.id.iv_shoppingpicture);
                holder.tv_shoppingname= (TextView) convertView.findViewById(R.id.tv_shoppingname);
                holder.tv_shoppingdetails= (TextView) convertView.findViewById(R.id.tv_shoppingdetails);
                holder.tv_shoppingprice= (TextView) convertView.findViewById(R.id.tv_shoppingprice);
                holder.tv_nums= (TextView) convertView.findViewById(R.id.tv_nums);
                holder.btn_add= (Button) convertView.findViewById(R.id.btn_add);
                holder.btn_cell= (Button) convertView.findViewById(R.id.btn_cell);
                holder.et_num= (EditText) convertView.findViewById(R.id.et_num);
                convertView.setBackgroundResource(R.color.gray_bg);
                convertView.setTag(holder);
            }else {
                holder= (holder) convertView.getTag();
            }
            holder.check_shopping.setVisibility(View.GONE);
            holder.btn_add.setVisibility(View.GONE);
            holder.btn_cell.setVisibility(View.GONE);
            holder.et_num.setVisibility(View.GONE);
            holder.tv_nums.setVisibility(View.VISIBLE);
            //加载图片
            ImageLoader.getInstance().displayImage(shopperCartInfo_items_isSeclect.get(position).getImg_src(),holder.iv_shoppingpicture,options);
            holder.tv_shoppingname.setText(shopperCartInfo_items_isSeclect.get(position).getGoods_name());
            holder.tv_shoppingdetails.setText(shopperCartInfo_items_isSeclect.get(position).getBrief());
            holder.tv_shoppingprice.setText("￥"+shopperCartInfo_items_isSeclect.get(position).getGoods_price());
            holder.tv_nums.setText("x "+shopperCartInfo_items_isSeclect.get(position).getGoods_nums());
            return convertView;
        }
        private class holder {
            CheckBox check_shopping;
            ImageView iv_shoppingpicture;
            TextView tv_shoppingname;
            TextView tv_shoppingdetails;
            TextView tv_shoppingprice;
            TextView tv_nums;
            Button btn_add;
            Button btn_cell;
            EditText et_num;
        }
    }
}
