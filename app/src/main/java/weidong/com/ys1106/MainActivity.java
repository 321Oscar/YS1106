package weidong.com.ys1106;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import weidong.com.ys1106.Activity.BasicActivity;
import weidong.com.ys1106.Activity.RegisterActivity;
import weidong.com.ys1106.Activity.HomeActivity;
import weidong.com.ys1106.Utils.CommonRequest;
import weidong.com.ys1106.Utils.CommonResponse;
import weidong.com.ys1106.Utils.Constant;
import weidong.com.ys1106.Utils.ResponseHandle;
import weidong.com.ys1106.Utils.UserInfo;

public class MainActivity extends BasicActivity {

    private Button mBtnLogin;
    private TextView mTvRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText Account = findViewById(R.id.login_et_name);
        final EditText pass = findViewById(R.id.login_et_pass);

        mTvRegister = findViewById(R.id.register);
        mTvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        //登录 用MySQL+servlet
        mBtnLogin = findViewById(R.id.login);
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Account.getText().toString().isEmpty() || pass.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "用户名和密码不能为空！", Toast.LENGTH_SHORT).show();
                } else {
                    UserInfo info =new UserInfo();
                    info.setAccount(Account.getText().toString());
                    info.setPass(pass.getText().toString());
                    login(info);
                }

            }
        });
    }

    private void login(final UserInfo info) {
        //请求数据集
        CommonRequest request = new CommonRequest();
        request.addRequestParam("account", info.getAccount());
        request.addRequestParam("password", info.getPass());
        request.setRequestCode("1");//标识登录

        sendHttpPostRequst(Constant.URL_Login, request, new ResponseHandle() {
            @Override
            public void success(CommonResponse response) {
                //成功之后跳转到首页
                Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                Bundle bundle =new Bundle();
                bundle.putString("username",info.getAccount());
                intent.putExtras(bundle);
                SharedPreferences sharedPreferences =getSharedPreferences("userinfo",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("account",info.getAccount());
                editor.putString("pass",info.getPass());
                startActivity(intent);
            }

            @Override
            public void failure(String failCode, String failMsg) {
                String Msg = "";
                //10,00,0
                switch (failCode) {
                    case "10":
                        Msg = "密码错误";
                        break;
                    case "00":
                        Msg = "账号不存在";
                        break;
                    case "0":
                        Msg = "大错误";
                        break;
                }
                Toast.makeText(MainActivity.this, Msg, Toast.LENGTH_SHORT).show();
            }
        });

    }
}
