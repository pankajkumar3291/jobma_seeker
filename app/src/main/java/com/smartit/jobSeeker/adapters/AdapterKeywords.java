package com.smartit.jobSeeker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.smartit.jobSeeker.R;
import com.smartit.jobSeeker.activities.ActivitySwappingLikeTinder;

import java.util.ArrayList;
import java.util.List;

public class AdapterKeywords extends RecyclerView.Adapter<AdapterKeywords.ViewHolder> {


    private View mView;
    private Context mCtx;

    private List<String> list;

    public AdapterKeywords(ActivitySwappingLikeTinder swipeDeckAdapter, List<String> result) {


        this.mCtx = swipeDeckAdapter;
        this.list = result;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        mView = inflater.inflate(R.layout.row_adapter_keywords, parent,false);
        return new AdapterKeywords.ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.tvKeyId.setText(list.get(position).toString());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        private TextView tvKeyId;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvKeyId = itemView.findViewById(R.id.tvKeyId);


        }
    }
}
