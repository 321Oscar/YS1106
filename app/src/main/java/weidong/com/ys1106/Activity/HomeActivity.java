package weidong.com.ys1106.Activity;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import weidong.com.ys1106.MainActivity;
import weidong.com.ys1106.R;
import weidong.com.ys1106.Utils.AnalysisUtils;
import weidong.com.ys1106.Utils.CommonRequest;
import weidong.com.ys1106.Utils.CommonResponse;
import weidong.com.ys1106.Utils.Constant;
import weidong.com.ys1106.Utils.MyToast;
import weidong.com.ys1106.Utils.ResponseHandle;
import weidong.com.ys1106.adapter.FragmentAdapter;
import weidong.com.ys1106.fragment.GuanzhuFragment;
import weidong.com.ys1106.fragment.HomeFragment;
import weidong.com.ys1106.fragment.MyOwnFragment;

public class HomeActivity extends BasicActivity implements View.OnClickListener {

    private ViewPager vp;
    private List<Fragment> mFragmentList = new ArrayList<>();

    private DrawerLayout drawerLayout;
    private Toolbar home_toolbar;

    private long lastBack = 0;

    /*
     * 再次返回键退出程序*/
    @Override
    public void onBackPressed() {
        if (lastBack == 0 || System.currentTimeMillis() - lastBack > 2000) {
            Toast.makeText(HomeActivity.this, "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            lastBack = System.currentTimeMillis();
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //设置此界面为竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        initViews();

        FragmentAdapter mFragmentAdapter = new FragmentAdapter(this.getSupportFragmentManager(), mFragmentList);
        //设置为三块
        vp.setOffscreenPageLimit(3);
        vp.setAdapter(mFragmentAdapter);
        //默认为首页
        vp.setCurrentItem(1);

        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                //改变RadioButton的选中状态
                changeRadioChecked(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    /*
     *初始化基本控件
     * */
    public void initViews() {
        //定义控件
        //侧边栏吗以及标题
        drawerLayout = findViewById(R.id.home_drawer);
        home_toolbar = findViewById(R.id.home_toolbar);

        setSupportActionBar(home_toolbar);
        home_toolbar.setTitle("首页");
        home_toolbar.setNavigationIcon(R.mipmap.menu64);
        home_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.START);
            }
        });

        TextView mChangePass = findViewById(R.id.btn_changepass);
        TextView mChangeUser = findViewById(R.id.btn_changeUser);
        TextView mExit = findViewById(R.id.exit);
        TextView mDeleteUser = findViewById(R.id.deleteaccount);
        TextView WenJuan = findViewById(R.id.Wenjuan);

        mDeleteUser.setOnClickListener(this);
        mChangePass.setOnClickListener(this);
        mChangeUser.setOnClickListener(this);
        mExit.setOnClickListener(this);
        WenJuan.setOnClickListener(this);
        
        //底部导航栏
        RadioGroup mRgTab = findViewById(R.id.rg_main);
        mRgTab.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_guanzhu:
                        vp.setCurrentItem(0, true);
                        break;
                    case R.id.rb_home:
                        vp.setCurrentItem(1, true);
                        break;
                    case R.id.rb_myown:
                        vp.setCurrentItem(2, true);
                        break;
                }
            }
        });
        vp = findViewById(R.id.vp_rbHome);
        //添加Fragment
        mFragmentList.add(new GuanzhuFragment());
        mFragmentList.add(new HomeFragment());
        mFragmentList.add(new MyOwnFragment());
    }

    /*改变按钮的选中状态 以及标题*/
    public void changeRadioChecked(int index) {
        if (index == 0) {
            RadioButton rb = findViewById(R.id.rb_guanzhu);
            rb.setChecked(true);
            home_toolbar.setTitle("关注");
        } else if (index == 1) {
            RadioButton rb = findViewById(R.id.rb_home);
            rb.setChecked(true);
            home_toolbar.setTitle("首页");
        } else if (index == 2) {
            RadioButton rb = findViewById(R.id.rb_myown);
            rb.setChecked(true);
            home_toolbar.setTitle("个人信息");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //注销账户
            case R.id.deleteaccount:
                DeleteUser();
                break;
            //退出程序
            case R.id.exit:
                ExitThis();
                break;
            //更换用户
            case R.id.btn_changeUser:
                ChangeUser();
                break;
            //更改密码
            case R.id.btn_changepass:
                ChangePass();
                break;
            case R.id.Wenjuan:
                Intent intent= new Intent(HomeActivity.this,QuestionsActivity.class);
                startActivity(intent);
                break;
        }
    }

    //修改密码
    private void ChangePass() {
        Intent intent = new Intent(HomeActivity.this, ChangePassActivity.class);
        startActivity(intent);
    }

    //切换账号
    private void ChangeUser() {
        //清除当前账号信息
        AnalysisUtils.cleanlogin(HomeActivity.this);

        //打开新的登录界面
        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
        startActivity(intent);
        HomeActivity.this.finish();
    }

    //退出程序
    private void ExitThis() {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        builder.setTitle("退出").
                setMessage("退出程序吗？").
                setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).
                setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        HomeActivity.this.finish();
                    }
                }).show();
    }

    /* 注销账号
     * 数据库中的用户存在标识改为0
     * 先确定密码，密码正确以后再执行注销
     * */
    private void DeleteUser() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        View view = LayoutInflater.from(HomeActivity.this).inflate(R.layout.layout_confirm_pass, null);
        final EditText pass = view.findViewById(R.id.et_confirmpass);
        Button sure = view.findViewById(R.id.btn_cofmpas_sure);

        //确认密码
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!pass.getText().toString().isEmpty()) {
                    if ((pass.getText().toString().trim()).
                            equals(AnalysisUtils.readloginpass(HomeActivity.this, AnalysisUtils.readloginUserName(HomeActivity.this)))) {
                        //注销
                        deleteuser();
                    } else {
                        MyToast.MyToastShow(HomeActivity.this, "密码不正确！");
                    }
                } else {
                    MyToast.MyToastShow(HomeActivity.this, "密码不能为空！");
                }
            }
        });
        builder.setTitle("账号注销").setView(view).show();
    }

    private void deleteuser(){
        CommonRequest request = new CommonRequest();
        request.setRequestCode("9");
        request.addRequestParam("account",AnalysisUtils.readloginUserName(HomeActivity.this));

        sendHttpPostRequst(Constant.URL_Login, request, new ResponseHandle() {
            @Override
            public void success(CommonResponse response) {
                //返回登录界面，清除本地用户信息
                AnalysisUtils.cleanlogin(HomeActivity.this);
                Intent intent = new Intent(HomeActivity.this,MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void failure(String failCode) {
                MyToast.MyToastShow(HomeActivity.this,"注销失败了");
            }
        });
    }
}
