package np.com.naxa.simpledynamicforms.uitils;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;


import javax.annotation.Nullable;

import np.com.naxa.simpledynamicforms.R;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

////https://stackoverflow.com/questions/28217436/how-to-show-an-empty-view-with-a-recyclerview
public class RecyclerViewEmptySupport extends RecyclerView {
    private View emptyView;
    private View progressView;

    private long lastDispatch;


    private AdapterDataObserver emptyObserver = new AdapterDataObserver() {

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            super.onItemRangeChanged(positionStart, itemCount);
            dispatchViewChanges();

        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            dispatchViewChanges();

        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, @android.support.annotation.Nullable Object payload) {
            super.onItemRangeChanged(positionStart, itemCount, payload);
            dispatchViewChanges();

        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount);
            dispatchViewChanges();

        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
            dispatchViewChanges();

        }

        @Override
        public void onChanged() {
            super.onChanged();
            dispatchViewChanges();
        }


    };


    public void dispatchViewChanges() {

        Adapter<?> adapter = getAdapter();
        if (adapter != null && emptyView != null) {
            if (adapter.getItemCount() == 0) {
                emptyView.setVisibility(View.VISIBLE);
                RecyclerViewEmptySupport.this.setVisibility(View.GONE);
            } else {
                emptyView.setVisibility(View.GONE);
                RecyclerViewEmptySupport.this.setVisibility(View.VISIBLE);
            }
        }
    }


    public RecyclerViewEmptySupport(Context context) {
        super(context);
    }

    public RecyclerViewEmptySupport(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyclerViewEmptySupport(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);

        if (adapter != null) {
            adapter.registerAdapterDataObserver(emptyObserver);
        }
    }

    public void setEmptyView(View emptyView, @Nullable String message, OnEmptyLayoutClickListener onEmptyLayoutClickListener) {
        this.emptyView = emptyView;
        TextView tvMsg = this.emptyView.findViewById(R.id.msg);
        if (message != null) {
            tvMsg.setText(message);
        }
        if (onEmptyLayoutClickListener != null) {
        }
    }


    public void setProgressView(View progressView) {
        this.progressView = progressView;
        progressView.setVisibility(GONE);
    }


    public void showProgressView(boolean show) {
        if (show) {
            emptyView.setVisibility(GONE);
            RecyclerViewEmptySupport.this.setVisibility(GONE);
            progressView.setVisibility(VISIBLE);
        } else {
            emptyView.setVisibility(VISIBLE);
            RecyclerViewEmptySupport.this.setVisibility(VISIBLE);
            progressView.setVisibility(GONE);
        }
    }

    public interface OnEmptyLayoutClickListener {
        void onRetryButtonClick();
    }


}