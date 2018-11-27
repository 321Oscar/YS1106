package weidong.com.ys1106.Activity;

import android.content.Intent;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.IOException;
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

    private RecyclerView mTypeVideos;
    private VideoInfoAdapter mTypeVideosAdapter;

    private JzvdStd play;
    private VideoView play_1;
    private SurfaceView play_2;
    private ProgressBar play_2_1;
    private MediaPlayer player;
    private SurfaceHolder surfaceHolder;

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
        String VideoUrl = Constant.UEL_Img + bundle.getString("url");
        String Title = bundle.getString("title");
        String Des = bundle.getString("des");
        String Type = bundle.getString("type");

        mTypeVideos = findViewById(R.id.sameType);
        back = findViewById(R.id.play_back);

        mTvTitle = findViewById(R.id.play_title);
        mTvDes = findViewById(R.id.play_des);
        mVideoType = findViewById(R.id.videotype);
//视频播放 method 3
        //使用VideoView + mediaController 拉进度条就会 die掉
//        play_1 = findViewById(R.id.play_1);
//        MediaController mediaController = new MediaController(this);
//        play_1.setMediaController(mediaController);
//        play_1.setVideoURI(Uri.parse(VideoUrl));

        //视频播放 method 2
        //使用SurfaceView+MediaPlayer
        play_2 = findViewById(R.id.play_2);
        play_2_1 = findViewById(R.id.play_2_1);
        player = new MediaPlayer();
        try {
            player.setDataSource(this,Uri.parse(VideoUrl));
            surfaceHolder = play_2.getHolder();
            surfaceHolder.addCallback(new MyCallBack());
            player.prepare();
            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    play_2_1.setVisibility(View.INVISIBLE);
                    player.start();
                    player.setLooping(true);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        //视频播放 method 1
        //使用网上的插件 JiaoZiVideoPlayer
        play = findViewById(R.id.play);
        play.setUp(VideoUrl,null,Jzvd.SCREEN_WINDOW_NORMAL);

        //设置显示文本
        mTvDes.setText(Des);
        mTvTitle.setText(Title);
        mVideoType.setText(Type);
    }

    private class MyCallBack implements SurfaceHolder.Callback{
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            player.setDisplay(surfaceHolder);
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }
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
