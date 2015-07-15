package io.github.indrora.jouretnuit;

import android.database.DataSetObserver;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

/**
 * Created by indrora on 7/14/15.
 */
public class ActivityHistoryAdapter extends RecyclerView.Adapter {


    View[] fixedItems;
    ListAdapter dataAdapter;

    public ActivityHistoryAdapter(View[] forced_items, ListAdapter source) {
        fixedItems = forced_items;
        dataAdapter = source;
        // make it such that updates to the data adapter are propogated downwards to the RecyclerView
        if(dataAdapter != null ) {
            dataAdapter.registerDataSetObserver(new DataSetObserver() {
                /**
                 * This method is called when the entire data set has changed,
                 * most likely through a call to {@link Cursor#requery()} on a {@link Cursor}.
                 */
                @Override
                public void onChanged() {
                    ActivityHistoryAdapter.this.notifyDataSetChanged();
                }

                /**
                 * This method is called when the entire data becomes invalid,
                 * most likely through a call to {@link Cursor#deactivate()} or {@link Cursor#close()} on a
                 * {@link Cursor}.
                 */
                @Override
                public void onInvalidated() {
                   ActivityHistoryAdapter.this.notifyDataSetChanged();
                }
            });
        }
    }

    public class CardViewHolder extends RecyclerView.ViewHolder {
        private View internalView;
        private CardView wrapperView;
        private ViewGroup vg;
        public CardViewHolder(CardView v) {
            super(v);
            wrapperView = v;
            internalView = new View(v.getContext());
        }
        public void setViewGroup(ViewGroup vv) {
            vg = vv;
        }
        public void setCardContents(View vx) {
            internalView = vx;
            try {
                ((CardView)(vx.getParent())).removeView(vx);
            } catch (Exception e) { }
            wrapperView.addView(internalView);

            wrapperView.invalidate();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardViewHolder cvh;
        CardView cv = new CardView(parent.getContext());
        cv.setLayoutParams(new CardView.LayoutParams(CardView.LayoutParams.MATCH_PARENT, CardView.LayoutParams.WRAP_CONTENT));
        //cv.setCardElevation(10f);
        cv.setPreventCornerOverlap(true);
        cv.setUseCompatPadding(true);
        cvh = new CardViewHolder(cv);
        return cvh;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // get the appropriate view.
        CardViewHolder cvh = (CardViewHolder)holder;
        if(fixedItems != null && position < fixedItems.length) {
            View v = fixedItems[position];
            cvh.setCardContents(fixedItems[position]);
            cvh.wrapperView.invalidate();
        }
        else {
            if(dataAdapter == null ) { return; }
            int fixedPostion = position - fixedItems.length;
            View tmpView = cvh.internalView;
            cvh.setCardContents(dataAdapter.getView(fixedPostion, tmpView, cvh.vg));
        }

    }
    @Override
    public int getItemCount() {
        if(dataAdapter == null) { return fixedItems.length; }
        else { return dataAdapter.getCount() + fixedItems.length; }
    }
}
