package weidong.com.ys1106.fragment;

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

import weidong.com.ys1106.R;
import weidong.com.ys1106.Utils.AnalysisUtils;
import weidong.com.ys1106.Utils.CommonRequest;
import weidong.com.ys1106.Utils.CommonResponse;
import weidong.com.ys1106.Utils.Constant;
import weidong.com.ys1106.Utils.MyToast;
import weidong.com.ys1106.Utils.ResponseHandle;
import weidong.com.ys1106.Utils.VideoInfo;
import weidong.com.ys1106.Utils.YangShInfo;
import weidong.com.ys1106.adapter.HomeInfoAdapter;
import weidong.com.ys1106.adapter.YangShInfoAdapter;

public class GuanzhuFragment extends BasicFragment {

    private View view;
    private YangShInfoAdapter adapter;
    private RecyclerView rv;

    public GuanzhuFragment() {

    }

    public static GuanzhuFragment NewInstance(){
        GuanzhuFragment fragment = new GuanzhuFragment();
        return  fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_guanzhu,container,false);

        initData();

        return view;
    }

    public void initData(){
        CommonRequest request = new CommonRequest();

        request.setRequestCode("5");
        request.addRequestParam("account",AnalysisUtils.readloginUserName(getActivity()));

        sendHttpPostRequst(Constant.URL_Login, request, new ResponseHandle() {
            @Override
            public void success(CommonResponse response) {
                ArrayList<YangShInfo> infoArrayList = new ArrayList<YangShInfo>();
                for(int i = 0;i<response.getDataList().size();i++){
                    YangShInfo info =  new YangShInfo();
                    info.setName(response.getDataList().get(i).get("name"));
                    info.setImgUrl(response.getDataList().get(i).get("url"));
                    info.setDes(response.getDataList().get(i).get("des"));
                    info.setChecked(1);
                    infoArrayList.add(info);
                }

                initRecyclerView(infoArrayList);
            }

            @Override
            public void failure(String failCode, String failMsg) {
                ArrayList<YangShInfo> infoArrayList = null;
                initRecyclerView(infoArrayList);
            }
        });

    }

    public void initRecyclerView(ArrayList<YangShInfo> infoArrayList) {
        //获取RecyclerView
        rv = view.findViewById(R.id.frag_gz_rv);

        //创建adapter
        adapter = new YangShInfoAdapter(getActivity(), infoArrayList);

        //给RecyclerView设置adapter
        rv.setAdapter(adapter);

        //设置layoutManager，可以设置显示效果
        rv.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));

        //设置item的分割线
        rv.addItemDecoration(new myDecoration());

        adapter.setOnItemClickListener(new YangShInfoAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, YangShInfo Info) {
                MyToast.MyToastShow(getActivity(),"这个呢");
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
