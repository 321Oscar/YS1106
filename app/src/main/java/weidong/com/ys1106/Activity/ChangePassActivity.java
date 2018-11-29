package weidong.com.ys1106.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import weidong.com.ys1106.MainActivity;
import weidong.com.ys1106.R;
import weidong.com.ys1106.Utils.AnalysisUtils;
import weidong.com.ys1106.Utils.CommonRequest;
import weidong.com.ys1106.Utils.CommonResponse;
import weidong.com.ys1106.Utils.Constant;
import weidong.com.ys1106.Utils.MD5Utils;
import weidong.com.ys1106.Utils.MyToast;
import weidong.com.ys1106.Utils.ResponseHandle;

public class ChangePassActivity extends BasicActivity {

    private EditText mOldPsw;
    private EditText mNewPsw;
    private TextView Wrong_old, Wrong_new;

    private boolean OLDWrongtype = true;
    private boolean NEWWrongtype = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);

        init();
    }

    private void init() {
        mOldPsw = findViewById(R.id.et_oldpass);
        mNewPsw = findViewById(R.id.et_newpass);
        ImageView mBack = findViewById(R.id.btn_finish);
        Button mChangePsw = findViewById(R.id.btn_commit);
        Wrong_old = findViewById(R.id.oldwrong);
        Wrong_new = findViewById(R.id.newwrong);

        //设置监听
        mOldPsw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //格式判断
                if (s.length() < 6) {
                    Wrong_old.setText("不能少于6位");
                    Wrong_old.setVisibility(View.VISIBLE);
                    OLDWrongtype = true;
                } else {
                    Wrong_old.setVisibility(View.GONE);
                    OLDWrongtype = false;
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mNewPsw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //格式判断
                if (s.length() < 6) {
                    Wrong_new.setText("不能少于6位");
                    Wrong_new.setVisibility(View.VISIBLE);
                    NEWWrongtype = true;
                } else {
                    Wrong_new.setVisibility(View.GONE);
                    NEWWrongtype = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //返回键
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //提交更改
        mChangePsw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PswEmpty()) {
                    MyToast.MyToastShow(ChangePassActivity.this, "请填写完整的信息！");
                } else if (NEWWrongtype || OLDWrongtype) {
                    MyToast.MyToastShow(ChangePassActivity.this, "密码格式不正确！");
                } else if (SameOldNew()) {
                    MyToast.MyToastShow(ChangePassActivity.this, "新旧密码不能相同");
                } else {
                    //change pass
                    DoChange(AnalysisUtils.readloginUserName(ChangePassActivity.this),
                            MD5Utils.ToMD5(mNewPsw.getText().toString().trim()),
                            MD5Utils.ToMD5(mOldPsw.getText().toString().trim()));
                }

            }
        });
    }

    private void DoChange(String name, String psw, String oldpsw) {
        CommonRequest request = new CommonRequest();

        //设置更新用户信息码
        request.setRequestCode("6");

        //设置需要更新的用户的用户名、旧密码和新密码
        request.addRequestParam("account", name);
        request.addRequestParam("oldpass", oldpsw);
        request.addRequestParam("newpass", psw);

        sendHttpPostRequst(Constant.URL_Login, request, new ResponseHandle() {
            @Override
            public void success(CommonResponse response) {
                doSuccess();
            }

            @Override
            public void failure(String failCode) {
                doFail(failCode);
            }
        });
    }

    private void doFail(String fialCode) {
        switch (fialCode) {
            case "10":
                MyToast.MyToastShow(ChangePassActivity.this, "遗憾，修改失败");
                break;
            case "01":
                MyToast.MyToastShow(ChangePassActivity.this, "遗憾，旧密码错误");
                break;
        }
    }

    private void doSuccess() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ChangePassActivity.this);
        builder.setTitle("修改成功").
                setMessage("请重新登录").
                setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //清除登录状态
                        AnalysisUtils.cleanlogin(ChangePassActivity.this);
                        Intent intent = new Intent(ChangePassActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }).setCancelable(false).show();
    }

    //旧密码和新密码是否相同
    private boolean SameOldNew() {
        return mOldPsw.getText().toString().equals(mNewPsw.getText().toString());
    }

    //是否为空
    private boolean PswEmpty() {
        if (mOldPsw.getText().toString().isEmpty()) {
            return true;
        } else
            return mNewPsw.getText().toString().isEmpty();
    }
}
