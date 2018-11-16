package weidong.com.ys1106.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

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

    private EditText mOldPsw,mOldPsw_1;
    private EditText mNewPsw,mNewPsw_1;
    private Button mChangePsw;
    private ImageView mBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);

        init();
    }

    private void init(){
        mOldPsw = findViewById(R.id.et_oldpass);
        mOldPsw_1 = findViewById(R.id.et_oldpass_confirm);
        mNewPsw = findViewById(R.id.et_newpass);
        mNewPsw_1 = findViewById(R.id.et_newpass_confirm);
        mBack =findViewById(R.id.btn_finish);
        //返回键
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mChangePsw = findViewById(R.id.btn_commit);
        mChangePsw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PswEmpty()){
                    MyToast.MyToastShow(ChangePassActivity.this,"请填写完整的信息！");
                }else if(!OldPswSame()){
                    MyToast.MyToastShow(ChangePassActivity.this,"两次输入的旧密码不一致！");
                }else if (!NewPswSame()){
                    MyToast.MyToastShow(ChangePassActivity.this,"两次输入的新密码不一致！");
                }else if(SameOldNew()){
                    MyToast.MyToastShow(ChangePassActivity.this,"新旧密码不能相同");
                }else {
                    //change pass
                    DoChange(AnalysisUtils.readloginUserName(ChangePassActivity.this),
                            MD5Utils.ToMD5(mNewPsw.getText().toString().trim()),
                            MD5Utils.ToMD5(mOldPsw.getText().toString().trim()));
                }

            }
        });
    }

    private void DoChange(String name,String psw,String oldpsw){
        CommonRequest request = new CommonRequest();

        //设置更新用户信息码
        request.setRequestCode("6");

        //设置需要更新的用户的用户名、旧密码和新密码
        request.addRequestParam("account",name);
        request.addRequestParam("oldpass",oldpsw);
        request.addRequestParam("newpass",psw);

        sendHttpPostRequst(Constant.URL_Login, request, new ResponseHandle() {
            @Override
            public void success(CommonResponse response) {
                doSuccess();
            }

            @Override
            public void failure(String failCode, String failMsg) {
                doFail(failCode);
            }
        });
    }

    private void doFail(String fialCode){
        switch (fialCode){
            case "10":
                MyToast.MyToastShow(ChangePassActivity.this,"遗憾，修改失败");
                break;
            case "01":
                MyToast.MyToastShow(ChangePassActivity.this,"遗憾，旧密码错误");
                break;
        }
    }

    private void doSuccess(){
        AlertDialog.Builder successbuilder = new AlertDialog.Builder(ChangePassActivity.this);
        successbuilder.setTitle("修改成功").setMessage("请重新登录").setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //清除登录状态
                AnalysisUtils.cleanlogin(ChangePassActivity.this);
                Intent intent = new Intent(ChangePassActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        }).setCancelable(false).show();
    }

    //旧密码和新密码是否相同
    private boolean SameOldNew(){
        if(mOldPsw.getText().toString().equals(mNewPsw.getText().toString())){
            return true;
        }
        return false;
    }

    //两次旧密码是否填写一致
    private boolean OldPswSame(){
        if(mOldPsw.getText().toString().equals(mOldPsw_1.getText().toString())){
            return true;
        }
        return false;
    }

    //两次新密码是否填写一致
    private boolean NewPswSame(){
        if(mNewPsw.getText().toString().equals(mNewPsw_1.getText().toString())){
            return true;
        }
        return false;
    }

    //是否为空
    private boolean PswEmpty(){
        if(mOldPsw.getText().toString().isEmpty()){
            return true;
        }else if(mOldPsw_1.getText().toString().isEmpty()){
            return true;
        }else if(mNewPsw.getText().toString().isEmpty()){
            return true;
        }else if(mNewPsw_1.getText().toString().isEmpty()){
            return true;
        }
        return false;
    }
}
