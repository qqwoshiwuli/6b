package com.fivefivelike.mybaselibrary.utils.paginate;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.fivefivelike.mybaselibrary.R;
import com.wang.avi.AVLoadingIndicatorView;
import com.wang.avi.indicators.BallPulseIndicator;


/** RecyclerView creator that will be called to create and bind loading list item */
public interface LoadingListItemCreator {

    /**
     * Create new loading list item {@link RecyclerView.ViewHolder}.
     *
     * @param parent   parent ViewGroup.
     * @param viewType type of the loading list item.
     * @return ViewHolder that will be used as loading list item.
     */
    RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType);

    /**
     * Bind the loading list item.
     *
     * @param holder   loading list item ViewHolder.
     * @param position loading list item position.
     */
    void onBindViewHolder(RecyclerView.ViewHolder holder, int position);

    LoadingListItemCreator DEFAULT = new LoadingListItemCreator() {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer_layout, parent, false);
            LinearLayout linearLayout= (LinearLayout) view.findViewById(R.id.loading_view_layout);
            AVLoadingIndicatorView loadingView=new AVLoadingIndicatorView(parent.getContext());
            //ProgressView loadingView = new ProgressView(parent.getContext());//尾部加载中状态
            loadingView.setIndicator(new BallPulseIndicator());
            loadingView.setIndicatorColor(0xff69b3e0);
            linearLayout.addView(loadingView);
            return new RecyclerView.ViewHolder(view) {
            };
        }
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            // No binding for default loading row
        }
    };
}