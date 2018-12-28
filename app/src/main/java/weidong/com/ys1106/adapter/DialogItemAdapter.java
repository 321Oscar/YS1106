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
import weidong.com.ys1106.Utils.VideoInfo;

/*
 *
 * */
public class DialogItemAdapter extends RecyclerView.Adapter<DialogItemAdapter.LinearViewHolder> {

    private Context context;
    private String[] data;

    //创建构造函数
    public DialogItemAdapter(Context context, String[] data) {
        this.context = context;
        this.data = data;
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
    public DialogItemAdapter.LinearViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View Info = LayoutInflater.from(context).inflate(R.layout.item_dialog,viewGroup,false);
        return new LinearViewHolder(Info);
    }

    /*
     * 绑定View Holder
     * */
    @Override
    public void onBindViewHolder(@NonNull DialogItemAdapter.LinearViewHolder viewHolder, int i) {
        //根据点击位置绑定数据
        viewHolder.mTitle.setText(data[i]);
    }

    //列表的长度
    @Override
    public int getItemCount() {
        return data.length;
    }

    //linear view holder
    class LinearViewHolder extends RecyclerView.ViewHolder {

         TextView mTitle;

        public LinearViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.item_dialog_wenjuan);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        //此处回传点击监听事件
                        onItemClickListener.OnItemClick(v, data[getLayoutPosition()]);
                    }
                }
            });
        }
    }

    /*
     * 设置Item的监听事件的接口
     * */
    public interface OnItemClickListener {
        /*
         * 接口中的点击每一项的实现方法，参数自己定义
         * @param view 点击的item的视图
         * @param videoInfo 点击的item的数据
         * */
        void OnItemClick(View view, String data);
    }

    //需要外部访问，所以需要设置set方法，方便调用
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
