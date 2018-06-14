package com.pingxundata.pxcore.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pingxundata.pxcore.R;
import com.pingxundata.pxcore.metadata.entity.ProductRecommend;
import com.pingxundata.pxcore.metadata.interfaces.IFunction;
import com.pingxundata.pxmeta.utils.GlideImgManager;
import com.pingxundata.pxmeta.utils.ObjectHelper;
import com.pingxundata.pxmeta.utils.ToastUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
* @Title: RecoListAdapter.java
* @Description: 产品申请推荐适配器
* @author Away
* @date 2017/10/20 17:27
* @copyright 重庆平讯数据
* @version V1.0
*/
public class RecoListAdapter extends BaseAdapter {

    Context mContext;

    List<ProductRecommend> datas;

    /**
     * 布局文件拾取器
     */
    LayoutInflater layoutInflater;

    IFunction onItemClickFunction;

    public RecoListAdapter(Context context,IFunction iFunction) {
        mContext=context;
        onItemClickFunction=iFunction;
        if(ObjectHelper.isNotEmpty(context)){
            this.layoutInflater = LayoutInflater.from(context);
        }
    }

    @Override
    public int getCount() {
        if(ObjectHelper.isNotEmpty(datas)){
            return datas.size();
        }else{
            return 0;
        }
    }

    @Override
    public Object getItem(int i) {
        if(ObjectHelper.isNotEmpty(datas)&&datas.size()>0){
            return datas.get(i);
        }else{
            return null;
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(ObjectHelper.isNotEmpty(datas)&&datas.size()>0&&ObjectHelper.isNotEmpty(mContext)){
            view = layoutInflater.inflate(R.layout.recommend_item, null);
            if(ObjectHelper.isEmpty(view))return view;

            LinearLayout recoItemContainer=view.findViewById(R.id.recoItemContainer);
            ImageView recommendItemBigImg=view.findViewById(R.id.recommendItemBigImg);
            ImageView itemProductIco=view.findViewById(R.id.itemProductIco);
            TextView itemProductTitle=view.findViewById(R.id.itemProductTitle);
            TextView itemProductLoanNm=view.findViewById(R.id.itemProductLoanNm);

            ProductRecommend productRecommend=datas.get(i);
            GlideImgManager.glideLoader(mContext,productRecommend.getRecommendImg(),R.mipmap.empty_banner,R.mipmap.empty_banner,recommendItemBigImg);
//            Glide.with(mContext).load(productRecommend.getRecommendImg()).into(recommendItemBigImg);
            GlideImgManager.glideLoader(mContext,productRecommend.getImg(),R.mipmap.empty_logo,R.mipmap.empty_logo,itemProductIco);
//            Glide.with(mContext).load(productRecommend.getImg()).into(itemProductIco);
            itemProductTitle.setText(productRecommend.getName()+"");
            itemProductLoanNm.setText(new BigDecimal(productRecommend.getLoanAmount()).divide(new BigDecimal(10000),2,BigDecimal.ROUND_HALF_UP)+"万");

            recoItemContainer.setOnClickListener(view1 -> onItemClickFunction.doFunction(productRecommend));

        }else{
            setData(new ArrayList<ProductRecommend>());
        }
        return view;
    }

    public void setData(List<ProductRecommend> sourceData){
        this.datas=sourceData;
        if(ObjectHelper.isEmpty(sourceData)||sourceData.size()==0){
            ToastUtils.showToast(mContext,"暂无相关数据");
        }
        notifyDataSetChanged();
    }
}
