package weidong.com.ys1106.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import weidong.com.ys1106.R;

public class GuanzhuFragment extends Fragment {

    public GuanzhuFragment() {

    }

    public static GuanzhuFragment NewInstance(){
        GuanzhuFragment fragment = new GuanzhuFragment();
        return  fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_guanzhu,container,false);
    }
}
