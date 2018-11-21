package weidong.com.ys1106.Activity;


import android.content.pm.ActivityInfo;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import weidong.com.ys1106.R;
import weidong.com.ys1106.adapter.FragmentAdapter;
import weidong.com.ys1106.fragment.GuanzhuFragment;
import weidong.com.ys1106.fragment.HomeFragment;
import weidong.com.ys1106.fragment.MyOwnFragment;

public class HomeActivity extends AppCompatActivity {

    private RadioGroup mRgTab;
    private ViewPager vp;
    private List<Fragment> mFragmentList = new ArrayList<>();
    private FragmentAdapter mFragmentAdapter;

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

        mFragmentAdapter = new FragmentAdapter(this.getSupportFragmentManager(), mFragmentList);
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
        mRgTab = findViewById(R.id.rg_main);
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

    /*改变按钮的选中状态*/
    public void changeRadioChecked(int index) {
        if (index == 0) {
            RadioButton rb = findViewById(R.id.rb_guanzhu);
            rb.setChecked(true);
        } else if (index == 1) {
            RadioButton rb = findViewById(R.id.rb_home);
            rb.setChecked(true);
        } else if (index == 2) {
            RadioButton rb = findViewById(R.id.rb_myown);
            rb.setChecked(true);
        }
    }

}
