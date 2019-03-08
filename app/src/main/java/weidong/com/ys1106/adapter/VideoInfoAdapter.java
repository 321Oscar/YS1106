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
import weidong.com.ys1106.Utils.AnalysisUtils;
import weidong.com.ys1106.Utils.Constant;
import weidong.com.ys1106.Utils.VideoInfo;

/*
 *
 * */
public class VideoInfoAdapter extends RecyclerView.Adapter<VideoInfoAdapter.LinearViewHolder> {

    private Context context;
    private ArrayList<VideoInfo> videoInfos;

    //创建构造函数
    public VideoInfoAdapter(Context context, ArrayList<VideoInfo> videoInfos) {
        this.context = context;
        this.videoInfos = videoInfos;
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
    public VideoInfoAdapter.LinearViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View homeInfo = View.inflate(context, R.layout.home_item, null);
        return new LinearViewHolder(homeInfo);
    }

    /*
     * 绑定View Holder
     * */
    @Override
    public void onBindViewHolder(@NonNull VideoInfoAdapter.LinearViewHolder viewHolder, int i) {
        //根据点击位置绑定数据
        VideoInfo data = videoInfos.get(i);
        System.out.println("视频数据：" + data.getVideoType());
        //设置图片的URL
        if (null != data.getImgUrl()){
            Glide.with(context).load(Constant.URL_Img + data.getImgUrl()).into(viewHolder.mVideoImg);
        }
//        if (null!= data.getVideoURL()){
//            AnalysisUtils.LoadVideoScreenShot(context,Constant.URL_Img + data.getVideoURL(),viewHolder.mVideoImg,10);
//        }
//        viewHolder.mType.setText(data.getVideoType());
        viewHolder.mDetails.setText(data.getVideoDes());
        viewHolder.mTitle.setText(data.getVideoTitle());

    }

    //列表的长度
    @Override
    public int getItemCount() {
        return videoInfos.size();
    }

    //linear view holder
    class LinearViewHolder extends RecyclerView.ViewHolder {

        private TextView mTitle, mDetails;
        private ImageView mVideoImg;

        public LinearViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.item_home_title);
            mDetails = itemView.findViewById(R.id.item_home_detail);
            mVideoImg = itemView.findViewById(R.id.item_home_iv);
//            mType = itemView.findViewById(R.id.item_home_type);

            //
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        //此处回传点击监听事件
                        onItemClickListener.OnItemClick(v, videoInfos.get(getLayoutPosition()));
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
        void OnItemClick(View view, VideoInfo videoInfo);
    }

    //需要外部访问，所以需要设置set方法，方便调用
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
