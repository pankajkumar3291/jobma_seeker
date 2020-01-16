package com.smartit.jobSeeker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.orhanobut.hawk.Hawk;
import com.smartit.jobSeeker.R;
import com.smartit.jobSeeker.activities.ActivityAddSkills;
import com.smartit.jobSeeker.addUpdateSkills.AddSkill;
import com.smartit.jobSeeker.skills.Skills;

import java.util.ArrayList;
import java.util.List;

public class AddSkillAdapter extends RecyclerView.Adapter<AddSkillAdapter.ViewHolder> {

    private Context mCtx;
    private View mView;

    List<AddSkill> skillList;
    List<Skills> list;
    boolean homeFragment;

    public AddSkillAdapter(ActivityAddSkills activityAddSkills, ArrayList<AddSkill> addSkillArrayList) {

        this.mCtx = activityAddSkills;
        this.skillList = addSkillArrayList;

    }

    public AddSkillAdapter(ActivityAddSkills activityAddSkills, ArrayList<Skills> arrayList, boolean fromHomeFragment) {

        this.mCtx = activityAddSkills;
        this.list = arrayList;
        this.homeFragment = fromHomeFragment;

    }

    @NonNull
    @Override
    public AddSkillAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        mView = inflater.inflate(R.layout.row_add_skills, parent, false);
        return new AddSkillAdapter.ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull AddSkillAdapter.ViewHolder holder, int position) {


        if (homeFragment) {

            Skills skills = list.get(position);
            holder.tvSkills.setText(skills.getUpdateSkills());

            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (list.size() > 1) {
                        list.remove(position);
                        notifyDataSetChanged();

                    } else {
                        Toast.makeText(mCtx, "Atleast one skill should be there", Toast.LENGTH_SHORT).show();

                    }


                }
            });


        } else {

            AddSkill addSkill = skillList.get(position);
            holder.tvSkills.setText(addSkill.getUpdateSkills());


            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (skillList.size() > 1) {
                        skillList.remove(position);
                        notifyDataSetChanged();

                    } else {
                        Toast.makeText(mCtx, "Atleast one skill should be there", Toast.LENGTH_SHORT).show();

                    }


                }
            });
        }


    }


    @Override
    public int getItemCount() {
        if (homeFragment) {

            return list.size();
        } else {

            return skillList.size();
        }

    }


    public class ViewHolder extends RecyclerView.ViewHolder {


        private TextView tvSkills;
        private ImageView delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvSkills = itemView.findViewById(R.id.tvSkills);
            delete = itemView.findViewById(R.id.delete);

        }
    }
}
