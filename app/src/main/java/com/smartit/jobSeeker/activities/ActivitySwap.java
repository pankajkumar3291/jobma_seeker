package com.smartit.jobSeeker.activities;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.smartit.jobSeeker.R;

import java.util.ArrayList;
import java.util.List;

import link.fls.swipestack.SwipeStack;

public class ActivitySwap extends AppCompatActivity implements SwipeStack.SwipeStackListener {


    private SwipeStack mSwipeStack;
    private SwipeStackAdapter mAdapter;
    private ArrayList<String> mData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swapp);

        mSwipeStack = (SwipeStack) findViewById(R.id.swipeStack);

        mData = new ArrayList<>();
        mAdapter = new SwipeStackAdapter(mData);
        mSwipeStack.setAdapter(mAdapter);

        fillWithTestData();


    }

    private void fillWithTestData() {

        for (int x = 0; x < 5; x++) {
            mData.add("Dummy" + " " + (x + 1));
        }
    }


    @Override
    public void onViewSwipedToRight(int position) {
        String swipedElement = mAdapter.getItem(position);

    }

    @Override
    public void onViewSwipedToLeft(int position) {
        String swipedElement = mAdapter.getItem(position);

    }

    @Override
    public void onStackEmpty() {

    }


    public class SwipeStackAdapter extends BaseAdapter {

        private List<String> mData;

        public SwipeStackAdapter(List<String> data) {
            this.mData = data;
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public String getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.row_swapping_like_tinder, parent, false);
            }

            TextView textViewCard = (TextView) convertView.findViewById(R.id.tvJobTitle);
            textViewCard.setText(mData.get(position));

            return convertView;
        }
    }
}
