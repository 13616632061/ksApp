package com.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ui.entity.SupplyOrderPageOrder;
import com.ui.ks.NearlyShopperGoodsActivity;
import com.ui.ks.R;
import com.ui.util.SetEditTextInput;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/7/6.
 */

public class AllSupplyOrderFragmentAdapter extends BaseAdapter {
    private Activity mActivity;
    private ArrayList<SupplyOrderPageOrder> mSupplyOrderPageOrderlist;
    private DisplayImageOptions options;
    private InputMethodManager imm;

    public AllSupplyOrderFragmentAdapter(Activity mActivity, ArrayList<SupplyOrderPageOrder> mSupplyOrderPageOrderlist) {
        this.mActivity = mActivity;
        this.mSupplyOrderPageOrderlist = mSupplyOrderPageOrderlist;
        options=new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.picture_default)
                .showImageForEmptyUri(R.drawable.picture_default)
                .cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new RoundedBitmapDisplayer(5))
                .build();
    }

    @Override
    public int getCount() {
        if(mSupplyOrderPageOrderlist==null){
            return 0;
        }
        return mSupplyOrderPageOrderlist.size();
    }

    @Override
    public Object getItem(int position) {
        return mSupplyOrderPageOrderlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Horlder mHorlder=null;
        if(convertView==null){
            convertView=View.inflate(mActivity, R.layout.item_shoppingmallorderpagefragment,null);
            mHorlder=new Horlder();
            mHorlder.iv_logo= (ImageView) convertView.findViewById(R.id.iv_logo);
            mHorlder.tv_shoppername= (TextView) convertView.findViewById(R.id.tv_shoppername);
            mHorlder.tv_paystatus= (TextView) convertView.findViewById(R.id.tv_paystatus);
            mHorlder.iv_shoppingpicture1= (ImageView) convertView.findViewById(R.id.iv_shoppingpicture1);
            mHorlder.iv_shoppingpicture2= (ImageView) convertView.findViewById(R.id.iv_shoppingpicture2);
            mHorlder.iv_shoppingpicture3= (ImageView) convertView.findViewById(R.id.iv_shoppingpicture3);
            mHorlder.layout1= (RelativeLayout) convertView.findViewById(R.id.layout1);
            mHorlder.layout2= (RelativeLayout) convertView.findViewById(R.id.layout2);
            mHorlder.layout3= (RelativeLayout) convertView.findViewById(R.id.layout3);
            mHorlder.layout_shopper= (RelativeLayout) convertView.findViewById(R.id.layout_shopper);
            mHorlder.layout_moregoods= (RelativeLayout) convertView.findViewById(R.id.layout_moregoods);
            mHorlder.tv_shoppingname1= (TextView) convertView.findViewById(R.id.tv_shoppingname1);
            mHorlder.tv_shoppingname2= (TextView) convertView.findViewById(R.id.tv_shoppingname2);
            mHorlder.tv_shoppingname3= (TextView) convertView.findViewById(R.id.tv_shoppingname3);
            mHorlder.tv_shoppingprice1= (TextView) convertView.findViewById(R.id.tv_shoppingprice1);
            mHorlder.tv_shoppingprice2= (TextView) convertView.findViewById(R.id.tv_shoppingprice2);
            mHorlder.tv_shoppingprice3= (TextView) convertView.findViewById(R.id.tv_shoppingprice3);
            mHorlder.tv_nums1= (TextView) convertView.findViewById(R.id.tv_nums1);
            mHorlder.tv_nums2= (TextView) convertView.findViewById(R.id.tv_nums2);
            mHorlder.tv_nums3= (TextView) convertView.findViewById(R.id.tv_nums3);
            mHorlder.tv_money= (TextView) convertView.findViewById(R.id.tv_money);
            mHorlder.tv_min= (TextView) convertView.findViewById(R.id.tv_min);
            mHorlder.tv_nums= (TextView) convertView.findViewById(R.id.tv_nums);
            mHorlder.btn_gopay= (Button) convertView.findViewById(R.id.btn_gopay);
            mHorlder.btn_sure= (Button) convertView.findViewById(R.id.btn_sure);
            mHorlder.btn_cell= (Button) convertView.findViewById(R.id.btn_cell);
            convertView.setTag(mHorlder);
        }else {
            mHorlder= (Horlder) convertView.getTag();
        }
        mHorlder.tv_shoppername.setText(mSupplyOrderPageOrderlist.get(position).getSeller_name());
        mHorlder.layout_shopper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent_shopper=new Intent(mActivity, NearlyShopperGoodsActivity.class);
                mIntent_shopper.putExtra("shopperid",mSupplyOrderPageOrderlist.get(position).getSeller_id());
                mActivity.startActivity(mIntent_shopper);
            }
        });
        if(mSupplyOrderPageOrderlist.get(position).getmOrderPageorderlist().size()==1){
            mHorlder.layout1.setVisibility(View.VISIBLE);
            mHorlder.layout2.setVisibility(View.GONE);
            mHorlder.layout3.setVisibility(View.GONE);
            //加载图片
            ImageLoader.getInstance().displayImage(mSupplyOrderPageOrderlist.get(position).getmOrderPageorderlist().get(0).getImg_src(),mHorlder.iv_shoppingpicture1,options);
            mHorlder.tv_shoppingname1.setText(mSupplyOrderPageOrderlist.get(position).getmOrderPageorderlist().get(0).getName());
            mHorlder.tv_shoppingprice1.setText(mSupplyOrderPageOrderlist.get(position).getmOrderPageorderlist().get(0).getPrice());
            mHorlder.tv_nums1.setText(mSupplyOrderPageOrderlist.get(position).getmOrderPageorderlist().get(0).getQuantity());
        }  if(mSupplyOrderPageOrderlist.get(position).getmOrderPageorderlist().size()==2){
            mHorlder.layout1.setVisibility(View.VISIBLE);
            mHorlder.layout2.setVisibility(View.VISIBLE);
            mHorlder.layout3.setVisibility(View.GONE);
            //加载图片
            ImageLoader.getInstance().displayImage(mSupplyOrderPageOrderlist.get(position).getmOrderPageorderlist().get(0).getImg_src(),mHorlder.iv_shoppingpicture1,options);
            mHorlder.tv_shoppingname1.setText(mSupplyOrderPageOrderlist.get(position).getmOrderPageorderlist().get(0).getName());
            mHorlder.tv_shoppingprice1.setText(mSupplyOrderPageOrderlist.get(position).getmOrderPageorderlist().get(0).getPrice());
            mHorlder.tv_nums1.setText(mSupplyOrderPageOrderlist.get(position).getmOrderPageorderlist().get(0).getQuantity());
            //加载图片
            ImageLoader.getInstance().displayImage(mSupplyOrderPageOrderlist.get(position).getmOrderPageorderlist().get(1).getImg_src(),mHorlder.iv_shoppingpicture2,options);
            mHorlder.tv_shoppingname2.setText(mSupplyOrderPageOrderlist.get(position).getmOrderPageorderlist().get(1).getName());
            mHorlder.tv_shoppingprice2.setText(mSupplyOrderPageOrderlist.get(position).getmOrderPageorderlist().get(1).getPrice());
            mHorlder.tv_nums2.setText(mSupplyOrderPageOrderlist.get(position).getmOrderPageorderlist().get(1).getQuantity());
        }if(mSupplyOrderPageOrderlist.get(position).getmOrderPageorderlist().size()>=3){
            mHorlder.layout1.setVisibility(View.VISIBLE);
            mHorlder.layout2.setVisibility(View.VISIBLE);
            mHorlder.layout3.setVisibility(View.VISIBLE);
            //加载图片
            ImageLoader.getInstance().displayImage(mSupplyOrderPageOrderlist.get(position).getmOrderPageorderlist().get(0).getImg_src(),mHorlder.iv_shoppingpicture1,options);
            mHorlder.tv_shoppingname1.setText(mSupplyOrderPageOrderlist.get(position).getmOrderPageorderlist().get(0).getName());
            mHorlder.tv_shoppingprice1.setText(mSupplyOrderPageOrderlist.get(position).getmOrderPageorderlist().get(0).getPrice());
            mHorlder.tv_nums1.setText(mSupplyOrderPageOrderlist.get(position).getmOrderPageorderlist().get(0).getQuantity());
            //加载图片
            ImageLoader.getInstance().displayImage(mSupplyOrderPageOrderlist.get(position).getmOrderPageorderlist().get(1).getImg_src(),mHorlder.iv_shoppingpicture2,options);
            mHorlder.tv_shoppingname2.setText(mSupplyOrderPageOrderlist.get(position).getmOrderPageorderlist().get(1).getName());
            mHorlder.tv_shoppingprice2.setText(mSupplyOrderPageOrderlist.get(position).getmOrderPageorderlist().get(1).getPrice());
            mHorlder.tv_nums2.setText(mSupplyOrderPageOrderlist.get(position).getmOrderPageorderlist().get(1).getQuantity());
            //加载图片
            ImageLoader.getInstance().displayImage(mSupplyOrderPageOrderlist.get(position).getmOrderPageorderlist().get(2).getImg_src(),mHorlder.iv_shoppingpicture3,options);
            mHorlder.tv_shoppingname3.setText(mSupplyOrderPageOrderlist.get(position).getmOrderPageorderlist().get(2).getName());
            mHorlder.tv_shoppingprice3.setText(mSupplyOrderPageOrderlist.get(position).getmOrderPageorderlist().get(2).getPrice());
            mHorlder.tv_nums3.setText(mSupplyOrderPageOrderlist.get(position).getmOrderPageorderlist().get(2).getQuantity());
        }
        mHorlder.tv_money.setText("￥"+SetEditTextInput.stringpointtwo(mSupplyOrderPageOrderlist.get(position).getFinal_amount())+mActivity.getString(R.string.str303)+"￥"+SetEditTextInput.stringpointtwo(mSupplyOrderPageOrderlist.get(position).getCost_freight())+")");
        mHorlder.tv_nums.setText(mActivity.getString(R.string.turnover_of_last_month)+mSupplyOrderPageOrderlist.get(position).getTotal_quantity()+mActivity.getString(R.string.str304));
        if(mSupplyOrderPageOrderlist.get(position).getPay_status().equals("0")){
            mHorlder.tv_paystatus.setText(mActivity.getString(R.string.str305));//待付款
        }
        if(mSupplyOrderPageOrderlist.get(position).getPay_status().equals("1")&&mSupplyOrderPageOrderlist.get(position).getShip_status().equals("0")){
            mHorlder.tv_paystatus.setText(mActivity.getString(R.string.str306));//待发货
        }
        if(mSupplyOrderPageOrderlist.get(position).getPay_status().equals("1")&&mSupplyOrderPageOrderlist.get(position).getShip_status().equals("1")){
            mHorlder.tv_paystatus.setText(mActivity.getString(R.string.str307));//待收货
        }
        if(mSupplyOrderPageOrderlist.get(position).getStatus().equals("finish")){
            mHorlder.tv_paystatus.setText(mActivity.getString(R.string.str308));//交易成功
        }
        return convertView;
    }
    private class Horlder{
        ImageView iv_logo,iv_shoppingpicture1,iv_shoppingpicture2,iv_shoppingpicture3;
        RelativeLayout layout1,layout2,layout3,layout_moregoods,layout_shopper;
        TextView tv_shoppingname1,tv_shoppingname2,tv_shoppingname3,tv_shoppername,tv_paystatus;
        TextView tv_shoppingprice1,tv_shoppingprice2,tv_shoppingprice3;
        TextView tv_nums1,tv_nums2,tv_nums3;
        TextView tv_money,tv_min,tv_nums;
        Button btn_gopay,btn_sure,btn_cell;
    }
}
