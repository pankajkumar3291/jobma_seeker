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
import com.smartit.jobSeeker.apiResponse.searchFilterModeulApiResponses.functional_area.FunctionalAreaData;

import java.util.List;

public class IndustryAdapter extends RecyclerView.Adapter<IndustryAdapter.IndustryViewHolder> {
    private List<FunctionalAreaData> skillList;
    private Context context;
    public IndustryAdapter(List<FunctionalAreaData> skillList, Context context) {
        this.skillList = skillList;
        this.context = context;
    }
    @NonNull
    @Override
    public IndustryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_industry_filter, viewGroup, false);
        return new IndustryViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull final IndustryViewHolder industryViewHolder, int i) {
        final FunctionalAreaData functionalAreaData = skillList.get(i);
        if (functionalAreaData.isIschecked())
            industryViewHolder.checkBox.setChecked(true);
        else
            industryViewHolder.checkBox.setChecked(false);
        industryViewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (industryViewHolder.checkBox.isChecked())
                    functionalAreaData.setIschecked(true);
                else
                    functionalAreaData.setIschecked(false);
            }
        });
        industryViewHolder.tvSkills.setText(functionalAreaData.getName());
    }
    @Override
    public int getItemCount() {
        return skillList.size();
    }
    class IndustryViewHolder extends RecyclerView.ViewHolder {
        private CheckBox checkBox;
        private TextView tvSkills;
        public IndustryViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkBox2);
            tvSkills = itemView.findViewById(R.id.textView12);
        }
    }
}
