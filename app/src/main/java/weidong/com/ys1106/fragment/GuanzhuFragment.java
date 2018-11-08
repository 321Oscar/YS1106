package weidong.com.ys1106.fragment;

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
import weidong.com.ys1106.Utils.VideoInfo;
import weidong.com.ys1106.Utils.YangShInfo;
import weidong.com.ys1106.adapter.HomeInfoAdapter;
import weidong.com.ys1106.adapter.YangShInfoAdapter;

public class GuanzhuFragment extends Fragment {

    private View view;
    private ArrayList<YangShInfo> infoArrayList = new ArrayList<YangShInfo>();
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
        initRecyclerView();
        initData();
        return view;
    }

    public void initData(){
        for (int i = 0; i < 5; i++) {
            YangShInfo info = new YangShInfo();
            info.setChecked(1);
            info.setName("养生类别" + i);
            infoArrayList.add(info);
        }
    }

    public void initRecyclerView() {
        //获取RecyclerView
        rv = view.findViewById(R.id.frag_gz_rv);
        //创建adapter
        adapter = new YangShInfoAdapter(getActivity(), infoArrayList);
        //给RecyclerView设置adapter
        rv.setAdapter(adapter);
        //设置layoutManager，可以设置显示效果
        //参数是：上下文，列表方向，是否倒叙
        rv.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL,
                false));
        //设置item的分割线
        rv.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));
        adapter.setOnItemClickListener(new YangShInfoAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, YangShInfo Info) {

            }
        });
    }
}
