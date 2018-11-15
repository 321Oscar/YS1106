package weidong.com.ys1106.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import weidong.com.ys1106.R;

public class ChangeInfoActivity extends AppCompatActivity {

    private ImageView back;
    private TextView mCommitChange;
    private EditText mChangeInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_info);

        Bundle bundle = this.getIntent().getExtras();
        /*
        * bundle中包含的参数
        * @param type 修改的类型
        * @param oldInfo 修改的旧内容
        * */

        back = findViewById(R.id.btn_finish);
        mCommitChange = findViewById(R.id.btn_change_commit);
        mChangeInfo = findViewById(R.id.et_change);

        //返回键
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        switch (bundle.getString("type")){
            case "account":
                mChangeInfo.setText(bundle.getString("oldInfo"));
                UpdateAccount();
                break;
        }

    }

    private void UpdateAccount(){

    }
}
