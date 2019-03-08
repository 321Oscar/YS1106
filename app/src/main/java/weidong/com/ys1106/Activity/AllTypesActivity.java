package weidong.com.ys1106.Activity;

import android.content.Intent;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

import weidong.com.ys1106.R;
import weidong.com.ys1106.Utils.CommonRequest;
import weidong.com.ys1106.Utils.CommonResponse;
import weidong.com.ys1106.Utils.Constant;
import weidong.com.ys1106.Utils.ResponseHandle;
import weidong.com.ys1106.Utils.YangShInfo;
import weidong.com.ys1106.adapter.YangShInfoAdapter;


/*
* 全部类型界面
* */
public class AllTypesActivity extends BasicActivity {

    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_types);
        //获取RecyclerView
        rv = findViewById(R.id.rv_alltypes);

        initData();
    }

    public void initData() {
        CommonRequest request = new CommonRequest();

        request.setRequestCode("51");

        sendHttpPostRequst(Constant.URL_Login, request, new ResponseHandle() {
            @Override
            public void success(CommonResponse response) {
                ArrayList<YangShInfo> infoArrayList = new ArrayList<>();
                if (response.getDataList().size()>0){
                    for (int i = 0; i < response.getDataList().size(); i++) {
                        YangShInfo info = new YangShInfo();
                        info.setName(response.getDataList().get(i).get("name"));
                        info.setImgUrl(response.getDataList().get(i).get("url"));
                        info.setDes(response.getDataList().get(i).get("des"));
                        infoArrayList.add(info);
                    }
                    initRecyclerView(infoArrayList);
                }else{
                    initRecyclerView(infoArrayList);
                }

            }

            @Override
            public void failure(String failCode) {
                initRecyclerView(null);
            }
        });

    }

    public void initRecyclerView(ArrayList<YangShInfo> infoArrayList) {
        //创建adapter
        YangShInfoAdapter adapter = new YangShInfoAdapter(AllTypesActivity.this, infoArrayList);

        //给RecyclerView设置adapter
        rv.setAdapter(adapter);

        //设置layoutManager，可以设置显示效果
        LinearLayoutManager manager = new LinearLayoutManager(AllTypesActivity.this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(manager);

        //设置item的分割线
//        if (FISRT_LOAD == 1){
            rv.addItemDecoration(new myDecoration());
//        }

        adapter.setOnItemClickListener(new YangShInfoAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, YangShInfo Info) {
                //跳到相应的类型页面
                Intent intent = new Intent(AllTypesActivity.this, TypeVideosActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("name", Info.getName());
                bundle.putString("type", Info.getType());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
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
