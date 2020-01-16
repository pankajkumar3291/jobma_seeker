package com.smartit.jobSeeker.adapters;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.smartit.jobSeeker.R;
import com.smartit.jobSeeker.activities.ActivityMultipleChoiceQuestion;

import java.util.ArrayList;

public class MultipleChoiceQuestionAdapter extends RecyclerView.Adapter<MultipleChoiceQuestionAdapter.ViewHolder> {


    private Context mContext;
    private View mView;
    private ArrayList<String> questionsAllList;
    public int mSelectedItem = -1;


    public MultipleChoiceQuestionAdapter(ActivityMultipleChoiceQuestion activityMultipleChoiceQuestion, ArrayList<String> questionsAll) {

        this.mContext = activityMultipleChoiceQuestion;
        this.questionsAllList = questionsAll;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        mView = inflater.inflate(R.layout.row_multiple_choice_questions_adapter, viewGroup, false);
        return new MultipleChoiceQuestionAdapter.ViewHolder(mView);

    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        String option = questionsAllList.get(position);
        holder.radioBtnAnswer.setText(option);

        holder.radioBtnAnswer.setChecked(position == mSelectedItem);


    }


    @Override
    public int getItemCount() {
        return questionsAllList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private RadioButton radioBtnAnswer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            radioBtnAnswer = itemView.findViewById(R.id.radioBtnAnswer);


            // CODE FOR SELECTING SINGLE SELECTION OF RADIO BUTTON

            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mSelectedItem = getAdapterPosition();

                    System.out.println("Check pos" + mSelectedItem); // GETTING POS OF THE SELECTED ITEMS HERE

                    Intent intent = new Intent("custom-message");
                    intent.putExtra("selectedValue",mSelectedItem);
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);



                    notifyDataSetChanged();
                }
            };
            itemView.setOnClickListener(clickListener);
            radioBtnAnswer.setOnClickListener(clickListener);

        }


    }


}
