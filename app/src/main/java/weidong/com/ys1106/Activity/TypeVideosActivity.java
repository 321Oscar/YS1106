package weidong.com.ys1106.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import weidong.com.ys1106.R;
import weidong.com.ys1106.Utils.AnalysisUtils;
import weidong.com.ys1106.Utils.CommonRequest;
import weidong.com.ys1106.Utils.CommonResponse;
import weidong.com.ys1106.Utils.Constant;
import weidong.com.ys1106.Utils.MyToast;
import weidong.com.ys1106.Utils.ResponseHandle;
import weidong.com.ys1106.Utils.VideoInfo;
import weidong.com.ys1106.adapter.VideoInfoAdapter;

/*
 * 某一个视频类型的所有视频
 * 标题栏 包括 返回键、视频类型名称、以及是否关注单选按钮
 * 内容用RecyclerView显示 Linear形式 内容布局类似HomeInfoFragment
 * */
public class TypeVideosActivity extends BasicActivity implements SwipeRefreshLayout.OnRefreshListener {

    private int FISRT_LOAD = 1;//是否是第一次加载
    private int CK;

    private TextView mBack;
    private CheckBox mIsGuanZhu;
    private Bundle bundle;

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_videos);

        initView();
        initData();
    }

    private void initView() {
        mBack = findViewById(R.id.back);
        TextView mTypeName = findViewById(R.id.typename);
        mIsGuanZhu = findViewById(R.id.isChecked);
        mRecyclerView = findViewById(R.id.typerv);
        mSwipeRefresh = findViewById(R.id.typerefresh);

        mSwipeRefresh.setOnRefreshListener(this);

        bundle = this.getIntent().getExtras();

        //获取类型名称，赋给标题
        mTypeName.setText(bundle.getString("name"));

        //获取关注状态
        getChecked();

        setClick();
    }

    private void getChecked() {
        //确认
        CommonRequest request = new CommonRequest();

        //请求码：82 ——  查找是否关注某一类别
        request.setRequestCode("82");
        //参数 类型名称 + 用户名
        request.addRequestParam("typename", bundle.getString("name"));
        request.addRequestParam("account", AnalysisUtils.readloginUserName(TypeVideosActivity.this));

        sendHttpPostRequst(Constant.URL_Login, request, new ResponseHandle() {
            @Override
            public void success(CommonResponse response) {
                setCK_1();
            }

            @Override
            public void failure(String failCode) {
                if (failCode.equals("10")) {
                    setCK_2();
                } else if (failCode.equals("00")) {
                    MyToast.SysInfo( "关注信息出错");
                }
            }
        });

    }

    //已关注
    private void setCK_1() {
        CK = 1;
        mIsGuanZhu.setChecked(true);
        mIsGuanZhu.setText("已关注");
    }

    //未关注
    private void setCK_2() {
        CK = 0;
        mIsGuanZhu.setChecked(false);
        mIsGuanZhu.setText("关注");
    }

    private void setClick() {

        //返回键
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //checkbox 状态改变 （关注/取消关注）
        mIsGuanZhu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //如果原先是选定状态 现在是非选定 则开始取消关注
                if (!isChecked && CK == 1) {
                    //取消关注
                    //弹出dialog确认取消关注
                    AlertDialog.Builder builder = new AlertDialog.Builder(TypeVideosActivity.this);
                    builder.setTitle("取消关注").
                            setMessage("确定取消关注本类型吗？").
                            setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //取消的话 ，把checkBox的状态还原
                                    dialog.dismiss();
                                    mIsGuanZhu.setChecked(true);
                                }
                            }).
                            setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //确认 -- 数据库取消
                                    CancelGuanZ();
                                }
                            }).show();
                } else if (isChecked && CK == 0) {
                    //如果原先是非选中状态 现在是选中 则开始关注
                    //关注
                    GuanZhu();
                }
            }
        });
    }

    //关注
    public void GuanZhu() {
        CommonRequest request = new CommonRequest();

        //请求码：80 ——  关注某一类的养生视频
        request.setRequestCode("80");
        //参数 类型名称 + 用户名
        request.addRequestParam("typename", bundle.getString("name"));
        request.addRequestParam("account", AnalysisUtils.readloginUserName(TypeVideosActivity.this));

        sendHttpPostRequst(Constant.URL_Login, request, new ResponseHandle() {
            @Override
            public void success(CommonResponse response) {
                GZSuc();
            }

            @Override
            public void failure(String failCode) {
                MyToast.MyToastShow(TypeVideosActivity.this, "数据库连接失败");
            }
        });
    }

    //关注成功后的动作
    public void GZSuc() {
        MyToast.MyToastShow(TypeVideosActivity.this, "关注成功");
        mIsGuanZhu.setText("已关注");
        CK = 1;//关注状态为 1 已关注
    }

    //取消关注
    public void CancelGuanZ() {
        CommonRequest request = new CommonRequest();

        //请求码：81 ——  取消关注某一类的养生视频
        request.setRequestCode("81");
        //参数 类型名称 + 用户名
        request.addRequestParam("typename", bundle.getString("name"));
        request.addRequestParam("account", AnalysisUtils.readloginUserName(TypeVideosActivity.this));

        sendHttpPostRequst(Constant.URL_Login, request, new ResponseHandle() {
            @Override
            public void success(CommonResponse response) {
                CancelSuc();
            }

            @Override
            public void failure(String failCode) {
                MyToast.MyToastShow(TypeVideosActivity.this, "数据库连接失败");
            }
        });
    }

    //取消关注成功 则改变checkbox的text
    public void CancelSuc() {
        MyToast.MyToastShow(TypeVideosActivity.this, "已取消");
        mIsGuanZhu.setText("关注");
        CK = 0;//关注状态为 0 未关注
    }

    /*
     *  从服务端的数据库中取出视频数据
     * */
    public void initData() {
        CommonRequest request = new CommonRequest();

        //请求码：32 —— 显示某一类的养生视频
        request.setRequestCode("32");
        //参数 类型名称
        request.addRequestParam("typename", bundle.getString("name"));

        sendHttpPostRequst(Constant.URL_Login, request, new ResponseHandle() {
            @Override
            public void success(CommonResponse response) {
                if (response.getDataList().size() > 0) {
                    initRecyclerView(setDataList(response.getDataList()));
                } else {
                    MyToast.MyToastShow(TypeVideosActivity.this, "列表无数据");
                }
            }

            @Override
            public void failure(String failCode) {
                MyToast.MyToastShow(TypeVideosActivity.this, "数据库连接失败");
            }
        });
    }

    /*
     * 加载RecyclerView
     * @param videoInfoList 数据列表
     * */
    public void initRecyclerView(ArrayList<VideoInfo> videoInfoList) {
        //创建adapter
        VideoInfoAdapter mTypeAdapter = new VideoInfoAdapter(TypeVideosActivity.this, videoInfoList);

        //给RecyclerView设置adapter
        mRecyclerView.setAdapter(mTypeAdapter);

        //设置layoutManager，可以设置显示效果
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        //设置item的边距
        if (FISRT_LOAD == 1) {
            mRecyclerView.addItemDecoration(new myDecoration());
        }

        //item点击事件
        mTypeAdapter.setOnItemClickListener(new VideoInfoAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, VideoInfo videoInfo) {
                //跳到相应的视频播放页面
                Intent intent = new Intent(TypeVideosActivity.this, VideoPlayActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("title", videoInfo.getVideoTitle());
                bundle.putString("des", videoInfo.getVideoDes());
                bundle.putString("type", videoInfo.getVideoType());
                bundle.putString("url", videoInfo.getVideoURL());
                intent.putExtras(bundle);
                startActivity(intent);
                Toast.makeText(TypeVideosActivity.this, videoInfo.getVideoURL(), Toast.LENGTH_SHORT).show();
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
    public void onRefresh() {
        mSwipeRefresh.setRefreshing(true);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                initData();
                FISRT_LOAD++;
                mSwipeRefresh.setRefreshing(false);
                MyToast.MyToastShow(TypeVideosActivity.this, "刷新完成");
            }
        }, 500);
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
