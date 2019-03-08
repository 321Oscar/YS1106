package weidong.com.ys1106.Utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.util.Arrays;

import weidong.com.ys1106.Activity.TypeVideosActivity;
import weidong.com.ys1106.R;
import weidong.com.ys1106.adapter.DialogItemAdapter;

/*
* 问卷结果展示Dialog
* 根据得分推荐养生类型
* 得分区间为 50-100
 * 50-60（包括60）推荐 "饮食养生", "运动养生"
 * 60-70 "情志养生", "针灸养生",
 * 70-80 "房事养生", "按摩养生",
 * 80-90 "药饵养生", "起居养生"
 * 90-100 "沐浴养生", "休闲养生"
* */

public class WenJuanDialog extends Dialog {
    private Context mContext;

    //得分
    private int count;

    //视频类型数据
    String[] DATA = {
            "饮食养生", "运动养生",
            "情志养生", "针灸养生",
            "房事养生", "按摩养生",
            "药饵养生", "起居养生",
            "沐浴养生", "休闲养生"};

    public WenJuanDialog(Context context) {
        super(context);
        mContext = context;
    }

    public WenJuanDialog(Context context, int count) {
        super(context);
        this.mContext = context;
        this.count = count;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.layout_wenjuandialog, null);

        RecyclerView rv = view.findViewById(R.id.dialog_wenjuan_rv);
        rv.setLayoutManager(new LinearLayoutManager(mContext));
        //根据得分控制显示的DATA
        String[] data={};
        switch (count/10+1){
            case 6: data = Arrays.copyOfRange(DATA,0,2);
                break;
            case 7: data = Arrays.copyOfRange(DATA,2,4);
                break;
            case 8: data = Arrays.copyOfRange(DATA,4,6);
                break;
            case 9: data = Arrays.copyOfRange(DATA,6,8);
                break;
            case 10:
            case 11:data = Arrays.copyOfRange(DATA,8,10);
                break;
        }
        DialogItemAdapter adapter = new DialogItemAdapter(mContext, data);

        //设置item点击事件
        adapter.setOnItemClickListener(new DialogItemAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, String data) {
                //跳转该类型的界面 以类型的名称为参数
                //MyToast.MyToastShow( mContext, "数据：" + data);
                Intent intent = new Intent(mContext,TypeVideosActivity.class);
                Bundle bundle =new Bundle();
                bundle.putString("name",data);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
//                ((Activity)mContext).finish();
            }
        });
        rv.setAdapter(adapter);
        rv.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));//item的分割线
        this.setContentView(view);

        Window dialogwindow = getWindow();
        WindowManager.LayoutParams lp = dialogwindow.getAttributes();
        DisplayMetrics d = mContext.getResources().getDisplayMetrics();//获取屏幕高、宽
        lp.width = d.widthPixels;
        lp.height = (int) (d.heightPixels * 0.6);
        dialogwindow.setAttributes(lp);//设置宽高
    }
}
