package weidong.com.ys1106.Activity;


import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import weidong.com.ys1106.R;
import weidong.com.ys1106.Utils.MyToast;
import weidong.com.ys1106.Utils.Question;
import weidong.com.ys1106.Utils.WenJuanDialog;
import weidong.com.ys1106.adapter.QuestionAdapter;

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
        final TextView WenjuanTiJiao = findViewById(R.id.question_btn);
        WenJuan = findViewById(R.id.question_rv);

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
                    WenJuanDialog dialog = new WenJuanDialog(QuestionsActivity.this);
                    dialog.show();
                } else {
                    MyToast.MyToastShow(QuestionsActivity.this, "第" + kong + "个问题未回答。");
                }

            }
        });
    }

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
