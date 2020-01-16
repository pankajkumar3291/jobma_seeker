package com.smartit.jobSeeker.adapters.filterAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.smartit.jobSeeker.R;
import com.smartit.jobSeeker.apiResponse.searchFilterModeulApiResponses.SkillName;

import java.util.List;

public class SkillAdapter extends RecyclerView.Adapter<SkillAdapter.SkillViewHolder> {

    private List<SkillName> skillList;
    private Context context;
    public SkillAdapter(List<SkillName> skillList, Context context) {
        this.skillList = skillList;
        this.context = context;
    }
    @NonNull
    @Override
    public SkillViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_skills_filter,viewGroup,false);
        return new SkillViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull SkillViewHolder skillViewHolder,  int i) {
        SkillName skillName=skillList.get(i);
        skillViewHolder.tvSkills.setText(skillName.getSkillName());

        skillViewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             if (skillViewHolder.checkBox.isChecked())
             {
                 for (int j=0;j<skillList.size();j++)
                 {
                     if (j==i)
                     {
                         skillList.get(i).setChecked(true);
                     }
                     else
                     {
                         skillList.get(i).setChecked(true);
                     }
                 }

             }
             else
             {
                 skillList.get(i).setChecked(false);
             }
            }
        });
    }
    @Override
    public int getItemCount() {
        return skillList.size();
    }
    class SkillViewHolder extends RecyclerView.ViewHolder {
        private CheckBox checkBox;
        private TextView tvSkills;

        public SkillViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox=itemView.findViewById(R.id.checkBox);
            tvSkills=itemView.findViewById(R.id.textView8);
        }
    }
}
