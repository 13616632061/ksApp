package com.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ui.entity.GoodSort;
import com.ui.ks.R;
import com.ui.recyclerviewleftslideremove.SlidingButtonView;
import com.ui.util.SysUtils;

import java.util.ArrayList;

/**
 * 商品分类适配器
 * Created by Administrator on 2017/3/9.
 */

public class GoodSortAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements SlidingButtonView.IonSlidingButtonListener{
    private Context context;
    private ArrayList<GoodSort> goodsortList;
    private SlidingButtonView mMenu = null;
    private IonSlidingViewClickListener mIDeleteBtnClickListener;
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;

    public GoodSortAdapter(Context context, ArrayList<GoodSort> goodsortList) {
        this.context = context;
        this.goodsortList = goodsortList;
        mIDeleteBtnClickListener = (IonSlidingViewClickListener) context;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==TYPE_ITEM){
        View  view=View.inflate(context,R.layout.goodsortitem_layout,null);
        Holder holder1=new Holder(view);
            return holder1;
        }else if (viewType == TYPE_FOOTER){
            View  view=View.inflate(context,R.layout.loading_view,null);
            view.setVisibility(View.GONE);
            FootViewHolder footViewHolder=new FootViewHolder(view);
            return footViewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof Holder) {
            ((Holder) holder).btn_goodsortnum.setText(position+1+"");
        ((Holder) holder).tv_goodsortname.setText(goodsortList.get(position).getName());
        ((Holder) holder).layout_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断是否有删除菜单打开
                if (menuIsOpen()) {
                    closeMenu();//关闭菜单
                } else {
                    int n = holder.getLayoutPosition();
                    mIDeleteBtnClickListener.onItemClick(v, n);
                }

            }
        });
        ((Holder) holder).tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SysUtils.isFastDoubleClick();
                int n = holder.getLayoutPosition();
                mIDeleteBtnClickListener.onDeleteBtnCilck(v, n);
                closeMenu();//关闭菜单
            }
        });
        }

    }



    @Override
    public int getItemCount() {
        return goodsortList.size()==0?0:goodsortList.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

     class Holder extends  RecyclerView.ViewHolder {
        Button btn_goodsortnum;
        TextView tv_goodsortname;
        TextView tv_delete;
        RelativeLayout layout_content;

        public Holder(View itemView) {
            super(itemView);
            btn_goodsortnum= (Button) itemView.findViewById(R.id.btn_goodsortnum);
            tv_goodsortname= (TextView) itemView.findViewById(R.id.tv_goodsortname);
            tv_delete= (TextView) itemView.findViewById(R.id.tv_delete);
            layout_content= (RelativeLayout) itemView.findViewById(R.id.layout_content);

            ((SlidingButtonView) itemView).setSlidingButtonListener(GoodSortAdapter.this);

        }
}

    /**
     * 上拉刷新的布局类
     */
   public class FootViewHolder extends RecyclerView.ViewHolder {

        public FootViewHolder(View view) {
            super(view);
        }
    }
    public void removeData(int position){
        goodsortList.remove(position);
        notifyItemRemoved(position);

    }

    /**
     * 删除菜单打开信息接收
     */
    @Override
    public void onMenuIsOpen(View view) {
        mMenu = (SlidingButtonView) view;
    }

    /**
     * 滑动或者点击了Item监听
     * @param slidingButtonView
     */
    @Override
    public void onDownOrMove(SlidingButtonView slidingButtonView) {
        if(menuIsOpen()){
            if(mMenu != slidingButtonView){
                closeMenu();
            }
        }
    }

    /**
     * 关闭菜单
     */
    public void closeMenu() {
        if(mMenu != null){
            mMenu.closeMenu();
        }
        mMenu = null;

    }
    /**
     * 判断是否有菜单打开
     */
    public Boolean menuIsOpen() {
        if(mMenu != null){
            return true;
        }
        return false;
    }



    public interface IonSlidingViewClickListener {
        void onItemClick(View view,int position);
        void onDeleteBtnCilck(View view,int position);
    }
}
