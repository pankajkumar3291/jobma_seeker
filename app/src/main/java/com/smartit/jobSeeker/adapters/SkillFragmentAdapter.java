package com.smartit.jobSeeker.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.orhanobut.hawk.Hawk;
import com.smartit.jobSeeker.addUpdateSkills.AddSkill;
import com.smartit.jobSeeker.R;
import com.smartit.jobSeeker.apiResponse.delete_education.DeleteEducationDetail;
import com.smartit.jobSeeker.httpRetrofit.HttpModule;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SkillFragmentAdapter extends RecyclerView.Adapter<SkillFragmentAdapter.ViewHolder> {

    private Context mCtx;
    private View mView;

    private List<AddSkill> list;

    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;

    private ImageView imageCancel;
    private EditText edEditSkills;
    private Button btnSubmit;
    private TextView tvEditSkills;

    private Button btnOkay, btnCancel;

    public SkillFragmentAdapter(FragmentActivity activity, List<AddSkill> myList) {

        this.mCtx = activity;
        this.list = myList;

    }


    @NonNull
    @Override
    public SkillFragmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        mView = inflater.inflate(R.layout.row_skill_fragment, parent, false);
        Hawk.init(mView.getContext()).build();

        return new SkillFragmentAdapter.ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull SkillFragmentAdapter.ViewHolder holder, int position) {

        AddSkill addSkill = list.get(position);
        holder.tvSkills.setText(addSkill.getUpdateSkills());


        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                warningDilaog(list, position);

            }
        });


        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String skills = list.get(position).getUpdateSkills();
                dialogEditSkill(position, skills);


            }
        });

    }

    private void warningDilaog(List<AddSkill> list, int position) {


        LayoutInflater li = LayoutInflater.from(mCtx);
        View dialogView = li.inflate(R.layout.dialog_ask, null);

        ids(dialogView, list, position);

        alertDialogBuilder = new AlertDialog.Builder(mCtx);
        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder.setCancelable(false);
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();


    }

    private void ids(View dialogView, List<AddSkill> list, int position) {

        btnOkay = dialogView.findViewById(R.id.btnOkay);
        btnCancel = dialogView.findViewById(R.id.btnCancel);


        btnOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (list.size() > 1) {
                    alertDialog.dismiss();
                    list.remove(position);
                } else {
                    Toast.makeText(mCtx, "Atleast one skill should be there", Toast.LENGTH_SHORT).show();
                }
                notifyDataSetChanged();

            }
        });


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });


    }

    private void dialogEditSkill(int position, String skills) {


        LayoutInflater li = LayoutInflater.from(mCtx);
        View dialogView = li.inflate(R.layout.dialog_edit_skills, null);

        initViews(dialogView, position, skills);

        alertDialogBuilder = new AlertDialog.Builder(mCtx);
        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder.setCancelable(true);
        alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();


    }

    private void initViews(View dialogView, int position, String skills) {

        imageCancel = dialogView.findViewById(R.id.imageCancel);
        edEditSkills = dialogView.findViewById(R.id.edEditSkills);
        btnSubmit = dialogView.findViewById(R.id.btnSubmit);
        tvEditSkills = dialogView.findViewById(R.id.tvEditSkills);
        tvEditSkills.setTypeface(tvEditSkills.getTypeface(), Typeface.BOLD);

        edEditSkills.setText(skills);


        imageCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();

            }
        });


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (TextUtils.isEmpty(edEditSkills.getText().toString().trim())) {

                    Toast.makeText(mCtx, "Edit skill can not be blank", Toast.LENGTH_SHORT).show();
//                    edEditSkills.setError("Edit skill can not be blank");

                } else {

                    list.get(position).setUpdateSkills(edEditSkills.getText().toString());
                    notifyDataSetChanged();
                    alertDialog.dismiss();


                }


            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        private TextView tvSkills;
        private ImageView delete, edit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvSkills = itemView.findViewById(R.id.tvSkills);

            delete = itemView.findViewById(R.id.delete);
            edit = itemView.findViewById(R.id.edit);
        }


    }
}
