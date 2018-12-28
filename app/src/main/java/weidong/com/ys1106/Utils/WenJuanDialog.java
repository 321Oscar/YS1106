package weidong.com.ys1106.Utils;

import android.app.Activity;
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

import weidong.com.ys1106.Activity.TypeVideosActivity;
import weidong.com.ys1106.R;
import weidong.com.ys1106.adapter.DialogItemAdapter;

public class WenJuanDialog extends Dialog {
    private Context mContext;
    private int count;//得分
    String[] DATA = {
            "饮食养生", "运动养生",
            "情志养生", "针灸养生",
            "房事养生", "按摩养生",
            "药饵养生", "起居养生",
            "沐浴养生", "休闲养生"};//视频类型数据

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
        DialogItemAdapter adapter = new DialogItemAdapter(mContext, DATA);

        //设置item点击事件
        adapter.setOnItemClickListener(new DialogItemAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, String data) {
                //跳转该类型的界面
                //MyToast.MyToastShow(mContext, "数据：" + data);
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
