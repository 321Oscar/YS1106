package weidong.com.ys1106.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import weidong.com.ys1106.Activity.VideoPlayActivity;
import weidong.com.ys1106.R;
import weidong.com.ys1106.Utils.AnalysisUtils;
import weidong.com.ys1106.Utils.CommonRequest;
import weidong.com.ys1106.Utils.CommonResponse;
import weidong.com.ys1106.Utils.Constant;
import weidong.com.ys1106.Utils.MyToast;
import weidong.com.ys1106.Utils.ResponseHandle;
import weidong.com.ys1106.Utils.VideoInfo;
import weidong.com.ys1106.adapter.HomeInfoAdapter;

public class HomeFragment extends BasicFragment {
    private View view;
    private RecyclerView mRvvideo;
    private HomeInfoAdapter homeadapter;

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

        initData();

        return view;
    }

    /*
     *  加载数据
     * */
    public void initData() {
        CommonRequest request = new CommonRequest();
        //请求码：3 —— 查找用户关注的养生视频
        request.setRequestCode("3");
        //使用用户的用户名查找
        String acccount = AnalysisUtils.readloginUserName(getActivity());
        System.out.println("account:" + acccount);

        request.addRequestParam("account", acccount);

        sendHttpPostRequst(Constant.URL_Login, request, new ResponseHandle() {
            @Override
            public void success(CommonResponse response) {
                if (response.getDataList().size() > 0) {
                    ArrayList<VideoInfo> videoInfoList = new ArrayList<VideoInfo>();
                    for (int i = 0; i < response.getDataList().size(); i++) {
                        VideoInfo videoInfo = new VideoInfo();
                        videoInfo.setVideoType(response.getDataList().get(i).get("type"));
                        videoInfo.setVideodetail(response.getDataList().get(i).get("des"));
                        videoInfo.setVideotitle(response.getDataList().get(i).get("title"));
                        videoInfo.setImgUrl(response.getDataList().get(i).get("imgurl"));
                        videoInfo.setVideoURL(response.getDataList().get(i).get("url"));
                        videoInfoList.add(videoInfo);
                    }
                    initRecyclerView(videoInfoList);
                    System.out.println("1:" + videoInfoList.get(0).getVideoType());
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
        //获取RecyclerView
        mRvvideo = view.findViewById(R.id.frag_home_rv);

        //创建adapter
        homeadapter = new HomeInfoAdapter(getActivity(), videoInfoList);

        //给RecyclerView设置adapter
        mRvvideo.setAdapter(homeadapter);

        //设置layoutManager，可以设置显示效果
        mRvvideo.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        //设置item的边距
        mRvvideo.addItemDecoration(new myDecoration());

        homeadapter.setOnItemClickListener(new HomeInfoAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, VideoInfo videoInfo) {
                //跳到相应的视频播放页面
                Intent intent = new Intent(getActivity(),VideoPlayActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("title",videoInfo.getVideotitle());
                bundle.putString("des",videoInfo.getVideodetail());
                bundle.putString("type",videoInfo.getVideoType());
                bundle.putString("url",videoInfo.getVideoURL());
                intent.putExtras(bundle);
                startActivity(intent);
                Toast.makeText(getActivity(), videoInfo.getVideoURL(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    class myDecoration extends RecyclerView.ItemDecoration{
        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            int gap =getResources().getDimensionPixelSize(R.dimen.itemdp_left);
            outRect.set(gap,gap,gap,gap);
        }
    }
}
