package weidong.com.ys1106.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import weidong.com.ys1106.R;
import weidong.com.ys1106.Utils.AnalysisUtils;
import weidong.com.ys1106.Utils.CommonRequest;
import weidong.com.ys1106.Utils.CommonResponse;
import weidong.com.ys1106.Utils.Constant;
import weidong.com.ys1106.Utils.MyToast;
import weidong.com.ys1106.Utils.ResponseHandle;

public class ChangeInfoActivity extends BasicActivity {

    private ImageView back;
    private TextView mCommitChange;
    private EditText mChangeInfo;
    private Intent intent;
    private Bundle bundle;

    //修改结果码
    private int CODE = 0x710;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_info);
        /*
         * bundle中包含的参数
         * @param type 修改的类型
         * @param oldInfo 修改的旧内容
         * */
        intent = this.getIntent();
        bundle = this.getIntent().getExtras();

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

        //在textView上显示旧的信息
        mChangeInfo.setText(bundle.getString("oldInfo"));

        //确定是更改那个类型，设置EditText的输入类型
        switch (bundle.getString("type")){
            case "account":
                break;
            case "age":
                mChangeInfo.setInputType(InputType.TYPE_CLASS_NUMBER);
                break;
            case "name":
                mChangeInfo.setInputType(InputType.TYPE_CLASS_TEXT);
                break;
            case "qq":
                mChangeInfo.setInputType(InputType.TYPE_CLASS_NUMBER);
                break;
            case "email":
                mChangeInfo.setInputType(InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS);
                break;
            case "ph":
                mChangeInfo.setInputType(InputType.TYPE_CLASS_PHONE);
                break;

        }

        //保存按钮 设置点击事件
        mCommitChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!IsSame()){
                    UpdateInfo();
                }else{
                    MyToast.MyToastShow(ChangeInfoActivity.this,"不能和以前的信息相同！");
                }

            }
        });

    }

    //判断新的信息和旧的信息是否相同
    private boolean IsSame(){
        if(mChangeInfo.getText().toString().equals(bundle.getString("oldInfo"))){
            return true;
        }
        return false;
    }

    //更新信息
    private void UpdateInfo(){
        //根据类型判断需要改变的是哪种信息
        switch (bundle.getString("type")){
            case "account":
                ChangeAccount("1");
                break;
            case "age":
                ChangeAccount("4");
                break;
            case "name":
                ChangeAccount("8");
                break;
            case "qq":
                ChangeAccount("5");
                break;
            case "email":
                ChangeAccount("7");
                break;
            case "ph":
                ChangeAccount("6");
                break;

        }
    }

    private void ChangeAccount(String updateCode){
        CommonRequest request = new CommonRequest();

        //设置用户更新信息码
        request.setRequestCode("7");

        //设置用户更新信息类型码
        request.setRequestUpCode(updateCode);

        //添加用户更改的参数 account --用户名, param -- 新的信息
        request.addRequestParam("account",AnalysisUtils.readloginUserName(ChangeInfoActivity.this));
        request.addRequestParam("param",mChangeInfo.getText().toString());

        sendHttpPostRequst(Constant.URL_Login, request, new ResponseHandle() {
            @Override
            public void success(CommonResponse response) {
                dosuccess();
            }

            @Override
            public void failure(String failCode, String failMsg) {
                dofail();
            }
        });
    }

    private void dosuccess(){
        //如果修改的是用户名，则要修改在本地的信息，用户名，以及重新设置新的用户名密码组合
        if(bundle.getString("type").equals("account")){
            AnalysisUtils.saveLoginStatus(ChangeInfoActivity.this,true,mChangeInfo.getText().toString());
            AnalysisUtils.addloginPass(ChangeInfoActivity.this,
                    AnalysisUtils.readloginpass(ChangeInfoActivity.this,bundle.getString("oldInfo")),
                    mChangeInfo.getText().toString());
        }
        MyToast.MyToastShow(ChangeInfoActivity.this,"修改成功！");
        //设置成功的返回Code ，以便判断是否需要刷新数据
        CODE = 0x711;
    }
    private void dofail(){
        MyToast.MyToastShow(ChangeInfoActivity.this,"修改失败！");
    }

    @Override
    public void finish() {
        setResult(CODE,intent);
        super.finish();
    }
}
