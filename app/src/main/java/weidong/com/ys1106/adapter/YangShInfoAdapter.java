package weidong.com.ys1106.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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
 * 养生类型展示 Adapter
 * */
public class YangShInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /*
    * viewType -- 分别为item以及空item
    * */
    public static final int VIEW_TYPE_ITEM = 1;
    public static final int VIEW_TYPE_EMPTY = 0;


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
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //根据不同的viewType引入不同的布局
        if(i == VIEW_TYPE_EMPTY ){
            View emptyView = LayoutInflater.from(context).inflate(R.layout.layout_empty,viewGroup,false);
            return new RecyclerView.ViewHolder(emptyView) {
            };
        }
        //其他的引入正常的
        View homeInfo = LayoutInflater.from(context).inflate(R.layout.guanzhu_item,viewGroup,false);
        return new YangShViewHolder(homeInfo);
    }

    /*
     * 绑定ViewHolder
     * */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof YangShViewHolder){
            YangShViewHolder hd = (YangShViewHolder) viewHolder;
            //根据点击位置绑定数据
            YangShInfo data = yangShInfos.get(i);

            //设置图片的URL
            if(data.getImgUrl()!=null){
                Glide.with(context).load(Constant.URL_Img + data.getImgUrl()).into(hd.mImg);
            }
            hd.mDes.setText(data.getDes());
            hd.mName.setText(data.getName());
        }


    }

    //列表的长度
    @Override
    public int getItemCount() {
        //这里也需要引入判断，如果数据为空的话，只引入emptylayout
        //那么，这个recyclerView的itemCount就为1
        if (yangShInfos.size() == 0){
            return 1;
        }
        return yangShInfos.size();
    }

    @Override
    public int getItemViewType(int position) {
        //再这里进行判断，如果数据集为空，则使用空布局
        if (yangShInfos.size() == 0){
            return VIEW_TYPE_EMPTY;
        }
        return VIEW_TYPE_ITEM;
    }

    //linearView holder
    class YangShViewHolder extends RecyclerView.ViewHolder {

        private TextView mName,mDes;
        private ImageView mImg;

        public YangShViewHolder(@NonNull View itemView) {
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
