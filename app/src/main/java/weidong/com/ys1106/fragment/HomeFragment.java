package weidong.com.ys1106.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import weidong.com.ys1106.R;
import weidong.com.ys1106.Utils.CommonRequest;
import weidong.com.ys1106.Utils.CommonResponse;
import weidong.com.ys1106.Utils.Constant;
import weidong.com.ys1106.Utils.MyToast;
import weidong.com.ys1106.Utils.ResponseHandle;
import weidong.com.ys1106.Utils.VideoInfo;
import weidong.com.ys1106.adapter.HomeInfoAdapter;

public class HomeFragment extends BasicFragment {
    private View view;
    private ArrayList<VideoInfo> videoInfoList = new ArrayList<VideoInfo>();
    private RecyclerView mRvvideo;
    private HomeInfoAdapter homeadapter;
    private SharedPreferences sharedPreferences;

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
        //对RecyclerView进行配置
        initRecyclerView();
        //模拟数据
        initData();
        return view;
    }

    /*
     * todo 模拟数据
     * */
    public void initData() {

        CommonRequest request = new CommonRequest();
        //请求码：3 —— 查找用户关注的养生视频
        request.setRequestCode("3");
        //使用用户的用户名查找
        sharedPreferences = getActivity().getSharedPreferences("userinfo",Context.MODE_PRIVATE);
        String acccount = sharedPreferences.getString("account","");
        System.out.println("account:"+acccount);

        request.addRequestParam("account",acccount);

        sendHttpPostRequst(Constant.URL_Login, request, new ResponseHandle() {
            @Override
            public void success(CommonResponse response) {
                if (response.getDataList().size() > 0){
                    for (int i = 0; i < response.getDataList().size(); i++) {
                        VideoInfo videoInfo = new VideoInfo();
                        videoInfo.setVideoType(response.getDataList().get(i).get("type"));
                        videoInfo.setVideodetail(response.getDataList().get(i).get("des"));
                        videoInfo.setVideotitle(response.getDataList().get(i).get("title"));
                        videoInfoList.add(videoInfo);
                    }
                }else {
                    MyToast.MyToastShow(getActivity(),"列表无数据");
                }
            }

            @Override
            public void failure(String failCode, String failMsg) {
                MyToast.MyToastShow(getActivity(),"数据库连接失败");
            }
        });

    }

    public void initRecyclerView() {
        //获取RecyclerView
        mRvvideo = view.findViewById(R.id.frag_home_rv);
        //创建adapter
        homeadapter = new HomeInfoAdapter(getActivity(), videoInfoList);
        //给RecyclerView设置adapter
        mRvvideo.setAdapter(homeadapter);
        //设置layoutManager，可以设置显示效果
        //参数是：上下文，列表方向，是否倒叙
        mRvvideo.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL,
                false));
        //设置item的分割线
        mRvvideo.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));
        homeadapter.setOnItemClickListener(new HomeInfoAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, VideoInfo videoInfo) {
                //跳到相应的视频播放页面
                Toast.makeText(getActivity(),"item",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
