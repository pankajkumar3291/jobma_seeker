package com.smartit.jobSeeker.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.smartit.jobSeeker.R;
import com.smartit.jobSeeker.skills.Skills;

import java.util.List;

public class KeySkillAdapeter extends RecyclerView.Adapter<KeySkillAdapeter.ViewHolder> {


    private Context mCtx;
    private View view;
    private List<Skills> stringList;


    public KeySkillAdapeter(FragmentActivity activity, List<Skills> myList) {

        this.mCtx = activity;
        this.stringList = myList;
    }


    @NonNull
    @Override
    public KeySkillAdapeter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        view = inflater.inflate(R.layout.row_keyskill_adapter, viewGroup, false);
        return new ViewHolder(view);

    }


    @Override
    public void onBindViewHolder(@NonNull KeySkillAdapeter.ViewHolder viewHolder, int position) {

        Skills skill = stringList.get(position);
        viewHolder.tvTags.setText(skill.getUpdateSkills());


    }


    @Override
    public int getItemCount() {
        return stringList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTags;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTags = itemView.findViewById(R.id.tvTags);


        }


    }
}
