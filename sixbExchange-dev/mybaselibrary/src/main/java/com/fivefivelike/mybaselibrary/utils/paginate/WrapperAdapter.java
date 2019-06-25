package com.fivefivelike.mybaselibrary.utils.paginate;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;


class WrapperAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM_VIEW_TYPE_LOADING = Integer.MAX_VALUE - 50; // Magic
    private int pagesize;
    private int headerCount = 0;
    private final RecyclerView.Adapter wrappedAdapter;
    private final LoadingListItemCreator loadingListItemCreator;
    private boolean displayLoadingRow = true;

    public void setHeaderCount(int headerCount) {
        this.headerCount = headerCount;
    }

    private boolean isZeroCount() {
        return wrappedAdapter.getItemCount() - headerCount == 0;
    }

    public WrapperAdapter(RecyclerView.Adapter adapter, LoadingListItemCreator creator) {
        this.wrappedAdapter = adapter;
        this.loadingListItemCreator = creator;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_VIEW_TYPE_LOADING) {
            return loadingListItemCreator.onCreateViewHolder(parent, viewType);
        } else {
            return wrappedAdapter.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isLoadingRow(position) || (isZeroCount() && position == headerCount)) {
            loadingListItemCreator.onBindViewHolder(holder, position);
        } else {
            wrappedAdapter.onBindViewHolder(holder, position);
        }
    }

    @Override
    public int getItemCount() {
        return (displayLoadingRow && wrappedAdapter.getItemCount() >= pagesize) || isZeroCount() ? wrappedAdapter.getItemCount() + 1 : wrappedAdapter.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        return (isLoadingRow(position) && wrappedAdapter.getItemCount() >= pagesize) || (isZeroCount() && position == headerCount) ? (ITEM_VIEW_TYPE_LOADING) : wrappedAdapter.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        return (isLoadingRow(position) && wrappedAdapter.getItemCount() >= pagesize) || (isZeroCount() && position == headerCount) ? RecyclerView.NO_ID : wrappedAdapter.getItemId(position);
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(hasStableIds);
        wrappedAdapter.setHasStableIds(hasStableIds);
    }

    public RecyclerView.Adapter getWrappedAdapter() {
        return wrappedAdapter;
    }

    boolean isDisplayLoadingRow() {
        return displayLoadingRow;
    }

    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }

    public int getPagesize() {
        return pagesize;
    }

    void displayLoadingRow(boolean displayLoadingRow) {
        if (this.displayLoadingRow != displayLoadingRow) {
            this.displayLoadingRow = displayLoadingRow;
            notifyDataSetChanged();
        }
    }

    boolean isLoadingRow(int position) {
        return (displayLoadingRow && wrappedAdapter.getItemCount() >= pagesize && position == getLoadingRowPosition());
    }

    private int getLoadingRowPosition() {
        return displayLoadingRow && wrappedAdapter.getItemCount() >= pagesize ? getItemCount() - 1 : -1;
    }


//    @Override
//    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
//        super.onAttachedToRecyclerView(recyclerView);
//        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
//        if (manager instanceof GridLayoutManager) {
//            final GridLayoutManager layout = ((GridLayoutManager) manager);
//            GridLayoutManager.SpanSizeLookup mGridSpanSizeLookup = new GridLayoutManager.SpanSizeLookup() {
//                @Override
//                public int getSpanSize(int position) {
////                    if (position < wrappedAdapter.getItemCount()) {
////                        return layout.getSpanCount();
////                    } else {
////                        //The number of spans occupied by the item at the provided position，Default Each item occupies 1 span.
////                        //在某个位置的item所占用的跨度的数量，默认情况下占用一个跨度。
////                        return 1;
////                    }
//                    return layout.getSpanCount();
//                }
//            };
//            layout.setSpanSizeLookup(mGridSpanSizeLookup);
//        }
//    }
}