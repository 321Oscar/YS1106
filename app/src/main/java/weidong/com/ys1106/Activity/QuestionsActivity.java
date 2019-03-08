package weidong.com.ys1106.Activity;


import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import weidong.com.ys1106.R;
import weidong.com.ys1106.Utils.MyToast;
import weidong.com.ys1106.Utils.Question;
import weidong.com.ys1106.Utils.WenJuanDialog;
import weidong.com.ys1106.adapter.QuestionAdapter;

/*
* 问卷调查界面
* 选A 10分 选B 5分 共十道题
* 得分区间为 50-100
* 50-60（包括60）推荐 "饮食养生", "运动养生"
* 60-70 "情志养生", "针灸养生",
* 70-80 "房事养生", "按摩养生",
* 80-90 "药饵养生", "起居养生"
* 90-100 "沐浴养生", "休闲养生"
* */

public class QuestionsActivity extends BasicActivity {

    RecyclerView WenJuan;
    QuestionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        initView();
        initData();
    }

    private void initView() {
        ImageView back = findViewById(R.id.question_finish);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final TextView WenjuanTiJiao = findViewById(R.id.question_btn);
        WenJuan = findViewById(R.id.question_rv);

        //提交问卷
        WenjuanTiJiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<Integer, Integer> Scores = adapter.Scores;
                int mak = 0;//总得分
                int kong = 0;//是否有未答的题
                for (int i = 0; i < adapter.getItemCount(); i++) {
                    if (null != Scores.get(i)) {
                        mak = mak + Scores.get(i);
                    } else {
                        kong = i + 1;
                        break;
                    }
                }
                if (0 == kong) {//全部答完
                    WenjuanTiJiao.setText(String.valueOf(mak));
                    WenJuanDialog dialog = new WenJuanDialog(QuestionsActivity.this,mak);
                    dialog.show();
                } else {
                    MyToast.MyToastShow(QuestionsActivity.this, "第" + kong + "个问题未回答。");
                }

            }
        });
    }

    //问题演示
    private void initData() {
        ArrayList<Question> data = new ArrayList<>();
        for (int i = 1; i < 11; i++) {
            Question question = new Question();
            question.setContent("问题" + i + ":选择A或B");
            question.setAnswerA("A.选我");
            question.setAnsWerB("B.Choose ME");
            data.add(question);
        }
        initRecycleView(data);
    }

    private void initRecycleView(ArrayList<Question> questions) {
        adapter = new QuestionAdapter(QuestionsActivity.this, questions);

        WenJuan.setAdapter(adapter);

        LinearLayoutManager manager = new LinearLayoutManager(QuestionsActivity.this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        WenJuan.setLayoutManager(manager);
        WenJuan.addItemDecoration(new DividerItemDecoration(QuestionsActivity.this,DividerItemDecoration.VERTICAL));
    }
}
