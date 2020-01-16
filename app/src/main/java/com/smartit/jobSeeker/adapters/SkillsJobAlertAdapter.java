package com.smartit.jobSeeker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.smartit.jobSeeker.R;

import java.util.List;

public class SkillsJobAlertAdapter extends RecyclerView.Adapter<SkillsJobAlertAdapter.ViewHolder> {

    private Context mCtx;
    private View mView;
    private List<String> list;


    public SkillsJobAlertAdapter(Context mCtx, List<String> skStringArrayList) {

        this.mCtx = mCtx;
        this.list = skStringArrayList;
        setHasStableIds(true);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        mView = inflater.inflate(R.layout.row_skills_job_adapter, parent, false);
        return new SkillsJobAlertAdapter.ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        String skills = list.get(position);
        holder.tvSkills.setText(skills);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvSkills;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvSkills = itemView.findViewById(R.id.tvSkills);
        }
    }
}
