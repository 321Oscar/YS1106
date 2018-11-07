package weidong.com.ys1106.Activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

import weidong.com.ys1106.R;
import weidong.com.ys1106.fragment.GuanzhuFragment;
import weidong.com.ys1106.fragment.HomeFragment;
import weidong.com.ys1106.fragment.MyOwnFragment;

public class VideosActivity extends AppCompatActivity {

    private RadioGroup mRgTab;
    private List<Fragment> mFragmentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos);

        mRgTab = findViewById(R.id.rg_main);
        mRgTab.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_home:
                        changeFragment(HomeFragment.class.getName());
                        break;
                    case R.id.rb_myown:
                        changeFragment(MyOwnFragment.class.getName());
                        break;
                    case R.id.rb_guanzhu:
                        changeFragment(GuanzhuFragment.class.getName());
                        break;
                }
            }
        });
        if (savedInstanceState == null) {
            changeFragment(HomeFragment.class.getName());
        }
    }

    /*
     * 显示所选的Fragment
     *
     *@param Tag
     * */
    public void changeFragment(String Tag) {
        //先将原有的fragment hide
        hideFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(Tag);
        if(null != fragment){
            transaction.show(fragment);
        }else{
            if(Tag.equals(HomeFragment.class.getName())){
                fragment = HomeFragment.NewInstance();
            }else if (Tag.equals(MyOwnFragment.class.getName())){
                fragment = MyOwnFragment.NewInstance();
            }else if(Tag.equals(GuanzhuFragment.class.getName())){
                fragment = GuanzhuFragment.NewInstance();
            }
            mFragmentList.add(fragment);
            transaction.add(R.id.fl_container,fragment,fragment.getClass().getName());
        }
        transaction.commitAllowingStateLoss();

    }


    /*
     * 隐藏所有的Fragment
     * */
    public void hideFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        for (Fragment f : mFragmentList) {
            ft.hide(f);
        }
        ft.commit();
    }
}
