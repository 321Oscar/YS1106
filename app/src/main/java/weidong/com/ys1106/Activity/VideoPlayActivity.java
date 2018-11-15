package weidong.com.ys1106.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.net.MalformedURLException;
import java.net.URL;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;
import weidong.com.ys1106.R;
import weidong.com.ys1106.Utils.Constant;

public class VideoPlayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        Bundle bundle = this.getIntent().getExtras();
        String VideoUrl = Constant.UEL_Img+bundle.getString("url");
        String Title = bundle.getString("title");
        String Des = bundle.getString("des");

        TextView mTvTitle = findViewById(R.id.play_title);
        TextView mTvDes = findViewById(R.id.play_des);
        TextView back = findViewById(R.id.play_back);
        JzvdStd play = findViewById(R.id.play);

        play.setUp(VideoUrl,null,Jzvd.SCREEN_WINDOW_NORMAL);
        mTvDes.setText(Des);
        mTvTitle.setText(Title);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        if(Jzvd.backPress()){
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        Jzvd.releaseAllVideos();
        super.onPause();
    }
}
