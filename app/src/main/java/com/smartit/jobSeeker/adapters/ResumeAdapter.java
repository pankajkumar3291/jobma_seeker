package com.smartit.jobSeeker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.smartit.jobSeeker.R;

public class ResumeAdapter extends RecyclerView.Adapter<ResumeAdapter.ViewHolder> {


    private Context mCtx;
    private View mView;

    @NonNull
    @Override
    public ResumeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        mView = inflater.inflate(R.layout.row_resume, parent, false);
        return new ResumeAdapter.ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull ResumeAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }


    }
}
