package com.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ui.entity.ShopperCartInfo;
import com.ui.global.Global;
import com.ui.ks.R;
import com.ui.util.SetEditTextInput;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/6/15.
 */

public class ShoppingMallShoppingCarFragmentAdapter extends SectionedBaseAdapter {
    private Context context;
    private ArrayList<ShopperCartInfo> shopperCartInfos;
    private DisplayImageOptions options;

    public ShoppingMallShoppingCarFragmentAdapter(Context context, ArrayList<ShopperCartInfo> shopperCartInfos) {
        this.context = context;
        this.shopperCartInfos = shopperCartInfos;
        options=new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.picture_default)
                .showImageForEmptyUri(R.drawable.picture_default)
                .cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new RoundedBitmapDisplayer(20))
                .build();
    }

    @Override
    public Object getItem(int section, int position) {
        return shopperCartInfos.get(section).getShopperCartInfo_items().get(position);
    }

    @Override
    public long getItemId(int section, int position) {
        return position;
    }

    @Override
    public int getSectionCount() {
        return shopperCartInfos.size();
    }

    @Override
    public int getCountForSection(int section) {
        return shopperCartInfos.get(section).getShopperCartInfo_items().size();
    }

    @Override
    public View getItemView(final int section, final int position, View convertView, ViewGroup parent) {
        holder holder=null;
        if(convertView==null){
            convertView=View.inflate(context, R.layout.item_shoppingcaifragment_item,null);
            holder=new holder();
            holder.check_shopping= (CheckBox) convertView.findViewById(R.id.check_shopping);
            holder.iv_shoppingpicture= ( ImageView) convertView.findViewById(R.id.iv_shoppingpicture);
            holder.tv_shoppingname= (TextView) convertView.findViewById(R.id.tv_shoppingname);
            holder.tv_shoppingdetails= (TextView) convertView.findViewById(R.id.tv_shoppingdetails);
            holder.tv_shoppingprice= (TextView) convertView.findViewById(R.id.tv_shoppingprice);
            holder.btn_add= (Button) convertView.findViewById(R.id.btn_add);
            holder.btn_cell= (Button) convertView.findViewById(R.id.btn_cell);
            holder.et_num= (EditText) convertView.findViewById(R.id.et_num);
            convertView.setTag(holder);
        }else {
            holder= (holder) convertView.getTag();
        }
        holder.tv_shoppingname.setText(shopperCartInfos.get(section).getShopperCartInfo_items().get(position).getGoods_name());
        holder.tv_shoppingdetails.setText(shopperCartInfos.get(section).getShopperCartInfo_items().get(position).getBrief());
        holder.tv_shoppingprice.setText("￥"+shopperCartInfos.get(section).getShopperCartInfo_items().get(position).getGoods_price());
        holder.et_num.setText(shopperCartInfos.get(section).getShopperCartInfo_items().get(position).getGoods_nums());
        //加载图片
        ImageLoader.getInstance().displayImage(shopperCartInfos.get(section).getShopperCartInfo_items().get(position).getImg_src(),holder.iv_shoppingpicture,options);

        final ShoppingMallShoppingCarFragmentAdapter.holder finalHolder = holder;
        holder.btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nums_src=  finalHolder.et_num.getText().toString().toString();
                int nums=Integer.parseInt(nums_src);
                nums++;
                shopperCartInfos.get(section).getShopperCartInfo_items().get(position).setGoods_nums(nums+"");
                finalHolder.et_num.setText(shopperCartInfos.get(section).getShopperCartInfo_items().get(position).getGoods_nums());
                context.sendBroadcast(new Intent(Global.BROADCAST_ShoppingMallShoppingCarFragment_ACTION).putExtra("type",1));

            }
        });
        holder.btn_cell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nums_src= finalHolder.et_num.getText().toString().toString();
                int nums=Integer.parseInt(nums_src);
                if(nums>0){
                    nums--;
                }
                shopperCartInfos.get(section).getShopperCartInfo_items().get(position).setGoods_nums(nums+"");
                finalHolder.et_num.setText(shopperCartInfos.get(section).getShopperCartInfo_items().get(position).getGoods_nums());
                context.sendBroadcast(new Intent(Global.BROADCAST_ShoppingMallShoppingCarFragment_ACTION).putExtra("type",1));
            }
        });
        holder.check_shopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(finalHolder.check_shopping.isChecked()){
                        shopperCartInfos.get(section).getShopperCartInfo_items().get(position).setSelect(true);
                }else {
                    shopperCartInfos.get(section).getShopperCartInfo_items().get(position).setSelect(false);
                }
                context.sendBroadcast(new Intent(Global.BROADCAST_ShoppingMallShoppingCarFragment_ACTION).putExtra("type",1));
            }
        });
        holder.check_shopping.setChecked(shopperCartInfos.get(section).getShopperCartInfo_items().get(position).isSelect());
        return convertView;
    }

    @Override
    public View getSectionHeaderView(final int section, View convertView, ViewGroup parent) {
        RelativeLayout layout = null;
        HeaderView headerView=null;
        if (convertView == null) {
            LayoutInflater inflator = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflator.inflate(R.layout.item_shoppingcarfragment, null);
            headerView=new HeaderView();
            headerView.check_shopper= (CheckBox) convertView.findViewById(R.id.check_shopper);
            headerView.tv_shoppername= (TextView) convertView.findViewById(R.id.tv_shoppername);
            headerView.tv_shoppernotes= (TextView) convertView.findViewById(R.id.tv_shoppernotes);
            convertView.setTag(headerView);
        } else {
//            layout = (RelativeLayout ) convertView;
            headerView= (HeaderView) convertView.getTag();
        }
        headerView.tv_shoppername.setText(shopperCartInfos.get(section).getSeller_name());
        headerView.tv_shoppernotes.setText("满"+ SetEditTextInput.stringpointtwo(shopperCartInfos.get(section).getFreight())+"起送");
        headerView.check_shopper.setChecked(shopperCartInfos.get(section).isSelect());
        final HeaderView finalHeaderView = headerView;
        headerView.check_shopper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(finalHeaderView.check_shopper.isChecked()){
                    shopperCartInfos.get(section).setSelect(true);
                    for(int i=0;i<shopperCartInfos.get(section).getShopperCartInfo_items().size();i++){
                        shopperCartInfos.get(section).getShopperCartInfo_items().get(i).setSelect(true);
                    }
                }else {
                    shopperCartInfos.get(section).setSelect(false);
                    for(int i=0;i<shopperCartInfos.get(section).getShopperCartInfo_items().size();i++){
                        shopperCartInfos.get(section).getShopperCartInfo_items().get(i).setSelect(false);
                    }
                }
                notifyDataSetChanged();
                context.sendBroadcast(new Intent(Global.BROADCAST_ShoppingMallShoppingCarFragment_ACTION).putExtra("type",1));
            }
        });
//        layout.setClickable(false);
        return convertView;
    }
    private class holder {
        CheckBox check_shopping;
        ImageView iv_shoppingpicture;
        TextView tv_shoppingname;
        TextView tv_shoppingdetails;
        TextView tv_shoppingprice;
        Button btn_add;
        Button btn_cell;
        EditText et_num;
    }
    private class HeaderView {
        CheckBox check_shopper;
        TextView tv_shoppername;
        TextView tv_shoppernotes;
    }
}
