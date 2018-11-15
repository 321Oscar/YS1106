package weidong.com.ys1106.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import weidong.com.ys1106.R;
import weidong.com.ys1106.Utils.Constant;
import weidong.com.ys1106.Utils.YangShInfo;

/*
 * 传入一个范型
 * */
public class YangShInfoAdapter extends RecyclerView.Adapter<YangShInfoAdapter.LinearViewHolder> {

    private Context context;
    private ArrayList<YangShInfo> yangShInfos;

    //创建构造函数
    public YangShInfoAdapter(Context context, ArrayList<YangShInfo> yangShInfos) {
        this.context = context;
        this.yangShInfos = yangShInfos;
    }

    /*
     * 创建view holder
     *
     * @param viewGroup
     * @param i
     * @return
     * */
    @NonNull
    @Override
    public YangShInfoAdapter.LinearViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View homeInfo = View.inflate(context, R.layout.guanzhu_item, null);
        return new LinearViewHolder(homeInfo);
    }

    /*
     * 绑定View Holder
     * */
    @Override
    public void onBindViewHolder(@NonNull YangShInfoAdapter.LinearViewHolder viewHolder, int i) {
        //根据点击位置绑定数据
        YangShInfo data = yangShInfos.get(i);

        //设置图片的URL
        System.out.println(data.getImgUrl());
        if(data.getImgUrl()!=null){
            Glide.with(context).load(Constant.UEL_Img + data.getImgUrl()).into(viewHolder.mImg);
        }
        viewHolder.mDes.setText(data.getDes());
        viewHolder.mName.setText(data.getName());

    }

    //列表的长度
    @Override
    public int getItemCount() {
        return yangShInfos.size();
    }

    //linear view holder
    class LinearViewHolder extends RecyclerView.ViewHolder {

        private TextView mName,mDes;
        private ImageView mImg;

        public LinearViewHolder(@NonNull View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.item_gz_tv);
            mImg = itemView.findViewById(R.id.item_gz_iv);
            mDes= itemView.findViewById(R.id.item_gz_tv_des);

            //
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener !=null){
                        //此处回传点击监听事件
                        onItemClickListener.OnItemClick(v,yangShInfos.get(getLayoutPosition()));
                    }
                }
            });
        }
    }

    /*
    * 设置Item的监听事件的接口
    * */
    public interface OnItemClickListener{
        /*
        * 接口中的点击每一项的实现方法，参数自己定义
        * @param view 点击的item的视图
        * @param videoInfo 点击的item的数据
        * */
        void OnItemClick(View view, YangShInfo Info);
    }

    //需要外部访问，所以需要设置set方法，方便调用
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
}
