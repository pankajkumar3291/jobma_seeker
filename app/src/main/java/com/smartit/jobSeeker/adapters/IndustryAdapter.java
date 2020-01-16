package com.smartit.jobSeeker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.smartit.jobSeeker.R;
import com.smartit.jobSeeker.activities.ActivityIndustryType;
import com.smartit.jobSeeker.apiResponse.industry.Datum;

import java.util.List;


public class IndustryAdapter extends RecyclerView.Adapter<IndustryAdapter.ViewHolder> {


    private Context mCtx;
    private View mView;


    private List<Datum> datumList;

    public IndustryAdapter(ActivityIndustryType activityIndustryType, List<Datum> data) {

        this.mCtx = activityIndustryType;
        this.datumList = data;

        setHasStableIds(true);

    }


    @NonNull
    @Override
    public IndustryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        mView = inflater.inflate(R.layout.row_industry, parent, false);
        return new IndustryAdapter.ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull IndustryAdapter.ViewHolder holder, int position) {


        Datum datum = datumList.get(position);
        holder.tvTitle.setText(datum.getName());

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                datum.setIschecked(isChecked);
            }
        });

    }

    @Override
    public int getItemCount() {

        return datumList.size();
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


        private TextView tvTitle;
        private CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            checkBox = itemView.findViewById(R.id.checkBox);

        }
    }
}
