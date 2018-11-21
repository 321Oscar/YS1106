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

import java.util.ArrayList;

import weidong.com.ys1106.Activity.TypeVideosActivity;
import weidong.com.ys1106.R;
import weidong.com.ys1106.Utils.AnalysisUtils;
import weidong.com.ys1106.Utils.CommonRequest;
import weidong.com.ys1106.Utils.CommonResponse;
import weidong.com.ys1106.Utils.Constant;
import weidong.com.ys1106.Utils.MyToast;
import weidong.com.ys1106.Utils.ResponseHandle;
import weidong.com.ys1106.Utils.YangShInfo;
import weidong.com.ys1106.adapter.YangShInfoAdapter;

public class GuanzhuFragment extends BasicFragment implements SwipeRefreshLayout.OnRefreshListener {

    private int FISRT_LOAD = 1;
    private View view;
    private YangShInfoAdapter adapter;
    private RecyclerView rv;

    private SwipeRefreshLayout mSwipeRefresh;

    public GuanzhuFragment() {

    }

    public static GuanzhuFragment NewInstance() {
        GuanzhuFragment fragment = new GuanzhuFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_guanzhu, container, false);
        //获取RecyclerView
        rv = view.findViewById(R.id.frag_gz_rv);
        mSwipeRefresh = view.findViewById(R.id.frag_gz_refresh);
        mSwipeRefresh.setOnRefreshListener(this);
        initData();
        return view;
    }

    public void initData() {
        CommonRequest request = new CommonRequest();

        request.setRequestCode("5");
        request.addRequestParam("account", AnalysisUtils.readloginUserName(getActivity()));

        sendHttpPostRequest(Constant.URL_Login, request, new ResponseHandle() {
            @Override
            public void success(CommonResponse response) {
                ArrayList<YangShInfo> infoArrayList = new ArrayList<>();
                if (response.getDataList().size()>0){
                    for (int i = 0; i < response.getDataList().size(); i++) {
                        YangShInfo info = new YangShInfo();
                        info.setName(response.getDataList().get(i).get("name"));
                        info.setImgUrl(response.getDataList().get(i).get("url"));
                        info.setDes(response.getDataList().get(i).get("des"));
                        info.setChecked(1);
                        infoArrayList.add(info);
                    }
                    initRecyclerView(infoArrayList);
                }else{
                    initRecyclerView(infoArrayList);
                }

            }

            @Override
            public void failure(String failCode, String failMsg) {
                ArrayList<YangShInfo> infoArrayList = null;
                initRecyclerView(infoArrayList);
            }
        });

    }

    public void initRecyclerView(ArrayList<YangShInfo> infoArrayList) {
        //创建adapter
        adapter = new YangShInfoAdapter(getActivity(), infoArrayList);

        //给RecyclerView设置adapter
        rv.setAdapter(adapter);

        //设置layoutManager，可以设置显示效果
        rv.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        //设置item的分割线
        if (FISRT_LOAD == 1){
            rv.addItemDecoration(new myDecoration());
        }

        adapter.setOnItemClickListener(new YangShInfoAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, YangShInfo Info) {
                //跳到相应的类型页面
                Intent intent = new Intent(getActivity(), TypeVideosActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("name", Info.getName());
                bundle.putString("type", Info.getType());
                bundle.putInt("isChecked", Info.getChecked());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onRefresh() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                FISRT_LOAD++;
                initData();
                mSwipeRefresh.setRefreshing(false);
                MyToast.MyToastShow(getActivity(),"刷新完成");
            }
        },500);
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
