package weidong.com.ys1106.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;

import weidong.com.ys1106.R;
import weidong.com.ys1106.Utils.Constant;
import weidong.com.ys1106.Utils.MyToast;
import weidong.com.ys1106.Utils.Question;
import weidong.com.ys1106.Utils.VideoInfo;

/*
 *
 * */
public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.LinearViewHolder> {

    private Context context;
    private ArrayList<Question> questionArrayList;
    public HashMap<Integer,Integer> Scores = new HashMap<>();

    //创建构造函数
    public QuestionAdapter(Context context, ArrayList<Question> questions) {
        this.context = context;
        this.questionArrayList = questions;
    }

    /*
     * 创建view holder
     *
     * @param viewGroup
     * @param i
     * @return
     * */
    @NonNull
    @Override
    public QuestionAdapter.LinearViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View Question = LayoutInflater.from(context).inflate(R.layout.items_question,viewGroup,false);
        return new LinearViewHolder(Question);
    }

    /*
     * 绑定View Holder
     * */
    @Override
    public void onBindViewHolder(@NonNull QuestionAdapter.LinearViewHolder viewHolder, int i) {
        //根据点击位置绑定数据
        Question data = questionArrayList.get(i);

        viewHolder.mContent.setText(data.getContent());
        viewHolder.Answer_A.setText(data.getAnswerA());
        viewHolder.Answer_B.setText(data.getAnsWerB());

        viewHolder.answers.setOnCheckedChangeListener(new MyRadioButtonCheckedChange(viewHolder,Scores));
    }

    //列表的长度
    @Override
    public int getItemCount() {
        return questionArrayList.size();
    }

    //linear view holder
    class LinearViewHolder extends RecyclerView.ViewHolder {

        TextView mContent;
        RadioGroup answers;
        RadioButton Answer_A;
        RadioButton Answer_B;

        public LinearViewHolder(@NonNull View itemView) {
            super(itemView);

            mContent = itemView.findViewById(R.id.question_content);
            Answer_A = itemView.findViewById(R.id.answer_A);
            Answer_B = itemView.findViewById(R.id.answer_B);
            answers = itemView.findViewById(R.id.answers);

        }
    }

    public class MyRadioButtonCheckedChange implements RadioGroup.OnCheckedChangeListener{

        public LinearViewHolder holder;
        public HashMap<Integer,Integer> scores;

        public MyRadioButtonCheckedChange(LinearViewHolder holder, HashMap<Integer, Integer> scores) {
            this.holder = holder;
            this.scores = scores;
        }

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.answer_A://选择A得10分
                    scores.put(holder.getAdapterPosition(),10);
                    break;
                case R.id.answer_B:
                    scores.put(holder.getAdapterPosition(),5);
                    break;
            }
            MyToast.SysInfo("---"+holder.getAdapterPosition()+"---"+scores.get(holder.getAdapterPosition())+"---");
        }
    }
}
