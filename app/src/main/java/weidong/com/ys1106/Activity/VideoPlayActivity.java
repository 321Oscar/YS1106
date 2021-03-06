package weidong.com.ys1106.Activity;

import android.content.Intent;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;
import weidong.com.ys1106.R;
import weidong.com.ys1106.Utils.CommonRequest;
import weidong.com.ys1106.Utils.CommonResponse;
import weidong.com.ys1106.Utils.Constant;
import weidong.com.ys1106.Utils.MyToast;
import weidong.com.ys1106.Utils.ResponseHandle;
import weidong.com.ys1106.Utils.VideoInfo;
import weidong.com.ys1106.adapter.VideoInfoAdapter;

public class VideoPlayActivity extends BasicActivity {

    private Bundle bundle;

    private TextView mTvTitle;
    private TextView mTvDes;
    private TextView back;
    private TextView mVideoType;
    private RadioGroup Qxd;

    private RecyclerView mTypeVideos;
    private VideoInfoAdapter mTypeVideosAdapter;

    private JzvdStd play;
    String[] urls = new String[3];
//    private VideoView play_1;
//    private SurfaceView play_2;
//    private ProgressBar play_2_1;
//    private MediaPlayer player;
//    private SurfaceHolder surfaceHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);

        initView();
        setClick();
        initData();
    }

    private void initView(){
        //获取数据
        bundle = this.getIntent().getExtras();

        try {
            JSONObject videourls = new JSONObject(bundle.getString("url"));
            urls[0] = videourls.getString("360p");
            urls[1] = videourls.getString("720p");
            urls[2] = videourls.getString("1080p");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        String Title = bundle.getString("title");
        String Des = bundle.getString("des");
        String Type = bundle.getString("type");

        mTypeVideos = findViewById(R.id.sameType);
        back = findViewById(R.id.play_back);

        mTvTitle = findViewById(R.id.play_title);
        mTvDes = findViewById(R.id.play_des);
        mVideoType = findViewById(R.id.videotype);

        Qxd = findViewById(R.id.qxd);
        RadioButton bq = findViewById(R.id.bq);

        play = findViewById(R.id.play);
        //默认标清
        String VideoUrl = Constant.URL_Img + urls[0];
        MyToast.SysInfo("URL----"+VideoUrl);
        bq.setChecked(true);
        //清晰度改变
        Qxd.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.bq:
                        setQxd(Constant.URL_Img+urls[0]);
                        break;
                    case R.id.gq:
                        setQxd(Constant.URL_Img+urls[1]);
                        break;
                    case R.id.cq:
                        setQxd(Constant.URL_Img+urls[2]);
                        break;
                }
            }
        });

        //视频播放 method 1
        //使用网上的插件 JiaoZiVideoPlayer
        play.setUp(VideoUrl,null,Jzvd.SCREEN_WINDOW_NORMAL);
//        play.setUp("http://jzvd.nathen.cn/c6e3dc12a1154626b3476d9bf3bd7266/6b56c5f0dc31428083757a45764763b0-5287d2089db37e62345123a1be272f8b.mp4",null,Jzvd.SCREEN_WINDOW_NORMAL);

        //设置显示文本
        mTvDes.setText(Des);
        mTvTitle.setText(Title);
        mVideoType.setText(Type);
    }

    private void setQxd(String url){
        Jzvd.releaseAllVideos();
        play.setUp(url,null,Jzvd.SCREEN_WINDOW_NORMAL);
        play.startVideo();
    }

    private void setClick(){
        //类型的点击事件
        //跳转到相应的类型视频界面
        mVideoType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VideoPlayActivity.this, TypeVideosActivity.class);
                Bundle bundle1 = new Bundle();
                bundle1.putString("name", mVideoType.getText().toString());
                intent.putExtras(bundle1);
                startActivity(intent);
            }
        });

        //返回键
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /*
     *  从服务端的数据库中取出视频数据
     * */
    public void initData() {
        CommonRequest request = new CommonRequest();

        //请求码：32 —— 显示某一类的养生视频
        request.setRequestCode("32");
        //参数 类型名称
        request.addRequestParam("typename", bundle.getString("type"));

        sendHttpPostRequst(Constant.URL_Login, request, new ResponseHandle() {
            @Override
            public void success(CommonResponse response) {
                if (response.getDataList().size() > 0) {
                    initRecyclerView(setDataList(response.getDataList()));
                } else {
                    MyToast.MyToastShow(VideoPlayActivity.this, "列表无数据");
                }
            }

            @Override
            public void failure(String failCode) {
                MyToast.MyToastShow(VideoPlayActivity.this, "数据库连接失败");
            }
        });
    }

    /*
     * 加载RecyclerView
     * @param videoInfoList 数据列表
     * */
    public void initRecyclerView(ArrayList<VideoInfo> videoInfoList) {
        //创建adapter
        mTypeVideosAdapter = new VideoInfoAdapter(VideoPlayActivity.this, videoInfoList);

        //给RecyclerView设置adapter
        mTypeVideos.setAdapter(mTypeVideosAdapter);

        //设置layoutManager，可以设置显示效果
        mTypeVideos.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        //设置item的边距
        mTypeVideos.addItemDecoration(new myDecoration());


        //item点击事件
        mTypeVideosAdapter.setOnItemClickListener(new VideoInfoAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, VideoInfo videoInfo) {
                //跳到相应的视频播放页面
                Intent intent = new Intent(VideoPlayActivity.this, VideoPlayActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("title", videoInfo.getVideoTitle());
                bundle.putString("des", videoInfo.getVideoDes());
                bundle.putString("type", videoInfo.getVideoType());
                bundle.putString("url", videoInfo.getVideoURL());
                intent.putExtras(bundle);
                startActivity(intent);
                Toast.makeText(VideoPlayActivity.this, videoInfo.getVideoURL(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //把报文赋值给videoInfoList
    public ArrayList<VideoInfo> setDataList(ArrayList<HashMap<String, String>> list) {
        ArrayList<VideoInfo> videoInfoList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            VideoInfo videoInfo = new VideoInfo();
            videoInfo.setVideoType(list.get(i).get("type"));//类型名称
            videoInfo.setVideoDes(list.get(i).get("des"));//视频描述
            videoInfo.setVideoTitle(list.get(i).get("title"));//视频标题
            videoInfo.setImgUrl(list.get(i).get("imgurl"));//图片路径
            videoInfo.setVideoURL(list.get(i).get("url"));//视频路径
            videoInfoList.add(videoInfo);
        }
        return videoInfoList;
    }

    @Override
    public void onBackPressed() {
        if (Jzvd.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        Jzvd.releaseAllVideos();
        super.onPause();
    }

    class myDecoration extends RecyclerView.ItemDecoration {
        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            int gap = getResources().getDimensionPixelSize(R.dimen.itemdp_left);
            outRect.set(gap, gap, gap, gap);
        }
    }
}
