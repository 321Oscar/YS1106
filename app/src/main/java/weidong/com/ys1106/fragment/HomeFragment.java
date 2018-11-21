package weidong.com.ys1106.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import weidong.com.ys1106.Activity.VideoPlayActivity;
import weidong.com.ys1106.R;
import weidong.com.ys1106.Utils.CommonRequest;
import weidong.com.ys1106.Utils.CommonResponse;
import weidong.com.ys1106.Utils.Constant;
import weidong.com.ys1106.Utils.MyToast;
import weidong.com.ys1106.Utils.ResponseHandle;
import weidong.com.ys1106.Utils.VideoInfo;
import weidong.com.ys1106.adapter.VideoInfoAdapter;

public class HomeFragment extends BasicFragment implements SwipeRefreshLayout.OnRefreshListener {

    private int FISRT_LOAD = 1;//是否是第一次加载

    private View view;

    private RecyclerView mRvvideo;
    private SwipeRefreshLayout mRefreshLayout;

    private VideoInfoAdapter homeAdapter;

//    private ArrayList<VideoInfo> videoInfoList = new ArrayList<VideoInfo>();

    public HomeFragment() {

    }

    public static HomeFragment NewInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        mRvvideo = view.findViewById(R.id.frag_home_rv);
        mRefreshLayout = view.findViewById(R.id.frag_home_refresh);
        mRefreshLayout.setOnRefreshListener(this);
        initData();
        return view;
    }

    /*
     *  从服务端的数据库中取出数据
     * */
    public void initData() {
        CommonRequest request = new CommonRequest();

        //请求码：3 —— 首页显示的所有养生视频
        request.setRequestCode("3");
        //无需参数

        sendHttpPostRequest(Constant.URL_Login, request, new ResponseHandle() {
            @Override
            public void success(CommonResponse response) {
                if (response.getDataList().size() > 0) {
                    initRecyclerView(setDataList(response.getDataList()));
                } else {
                    MyToast.MyToastShow(getActivity(), "列表无数据");
                }
            }

            @Override
            public void failure(String failCode, String failMsg) {
                MyToast.MyToastShow(getActivity(), "数据库连接失败");
            }
        });
    }

    /*
     * 加载RecyclerView
     * @param videoInfoList 数据列表
     * */
    public void initRecyclerView(ArrayList<VideoInfo> videoInfoList) {
        //创建adapter
        homeAdapter = new VideoInfoAdapter(getActivity(), videoInfoList);

        //给RecyclerView设置adapter
        mRvvideo.setAdapter(homeAdapter);

        //设置layoutManager，可以设置显示效果
        mRvvideo.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        //设置item的边距
        if (FISRT_LOAD == 1){
            mRvvideo.addItemDecoration(new myDecoration());
        }

        //item点击事件
        homeAdapter.setOnItemClickListener(new VideoInfoAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, VideoInfo videoInfo) {
                //跳到相应的视频播放页面
                Intent intent = new Intent(getActivity(), VideoPlayActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("title", videoInfo.getVideoTitle());
                bundle.putString("des", videoInfo.getVideoDes());
                bundle.putString("type", videoInfo.getVideoType());
                bundle.putString("url", videoInfo.getVideoURL());
                intent.putExtras(bundle);
                startActivity(intent);
                Toast.makeText(getActivity(), videoInfo.getVideoURL(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //把报文赋值给videoInfoList
    public ArrayList<VideoInfo> setDataList(ArrayList<HashMap<String,String>> list){
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

    //下拉刷新
    @Override
    public void onRefresh() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                initData();
                FISRT_LOAD++;
                mRefreshLayout.setRefreshing(false);
                MyToast.MyToastShow(getActivity(),"刷新完成");
            }
        },500);
    }

    //分割线
    class myDecoration extends RecyclerView.ItemDecoration {
        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            int gap = getResources().getDimensionPixelSize(R.dimen.itemdp_left);
            outRect.set(gap, gap, gap, gap);
        }
    }
}
