package weidong.com.ys1106;

import android.accounts.Account;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import weidong.com.ys1106.Activity.BasicActivity;
import weidong.com.ys1106.Activity.RegisterActivity;
import weidong.com.ys1106.Activity.HomeActivity;
import weidong.com.ys1106.Utils.CommonRequest;
import weidong.com.ys1106.Utils.CommonResponse;
import weidong.com.ys1106.Utils.Constant;
import weidong.com.ys1106.Utils.MyToast;
import weidong.com.ys1106.Utils.ResponseHandle;
import weidong.com.ys1106.Utils.UserInfo;

public class MainActivity extends BasicActivity {

    private Button mBtnLogin;
    private TextView mTvRegister;

    private CheckBox cb_rem;
    private CheckBox cb_auto;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText Account = findViewById(R.id.login_et_name);
        final EditText pass = findViewById(R.id.login_et_pass);
        //自动登录
        cb_rem = findViewById(R.id.login_cb_rem);
        cb_auto = findViewById(R.id.login_cb_auto);
        sp = getSharedPreferences("userinfo", MODE_PRIVATE);

        System.out.println("是否记住:" + sp.getString("rem", "") +
                "————是否自动登录：" + sp.getString("auto_login", ""));
        System.out.println("账户:" + sp.getString("account", "") +
                "————密码：" + sp.getString("pass", ""));

        //是否记住密码和自动登录
        if (sp.getString("rem", "").equals("1")) {
            cb_rem.setChecked(true);
            Account.setText(sp.getString("account", ""));
            pass.setText(sp.getString("pass", ""));
            if ((sp.getString("auto_login", "").equals("1"))) {
                cb_auto.setChecked(true);
                UserInfo autoinfo = new UserInfo();
                autoinfo.setAccount(sp.getString("account", ""));
                autoinfo.setPass(sp.getString("pass", ""));
                login(autoinfo);
            }
        }else{
            MyToast.MyToastShow(MainActivity.this,"cuowu");
        }


        //自动登录选中则自动选中记住密码
        cb_auto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cb_rem.setChecked(true);
                }
            }
        });

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
                    UserInfo info = new UserInfo();
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

        final ProgressDialog loginwait = new ProgressDialog(MainActivity.this);
        loginwait.setMessage("登录中...");
        loginwait.show();

        sendHttpPostRequst(Constant.URL_Login, request, new ResponseHandle() {
            @Override
            public void success(CommonResponse response) {
                loginwait.setMessage("成功");
                loginwait.cancel();

                //将用户信息保存
                sp = getSharedPreferences("userinfo", MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("account", info.getAccount());
                editor.putString("pass", info.getPass());
                if (cb_rem.isChecked()) {
                    editor.putString("rem", "1");
                    if (cb_auto.isChecked()) {
                        editor.putString("auto_login", "1");
                    }else {
                        editor.putString("auto_login","0");
                    }
                }else{
                    editor.putString("rem", "0");
                }
                editor.apply();

                //成功之后跳转到首页
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);

                finish();
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
