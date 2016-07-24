package com.rokpetek.breadcrumbtoolbar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;


public class BreadcrumbScrollView extends HorizontalScrollView {
    private LayoutInflater inflater;

    private BreadcrumbItemCallback breadcrumbItemCallback;

    public interface BreadcrumbItemCallback {
        void onItemClick(int position);
    }

    public BreadcrumbScrollView(Context context) {
        super(context);
        init();
    }

    public BreadcrumbScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BreadcrumbScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // Disable scroll bars
        setVerticalScrollBarEnabled(false);
        setHorizontalScrollBarEnabled(false);
    }

    public void addItem(String name, int stackSize) {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.breadcrumb_scroll_view_inner_layout);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        linearLayout.addView(getBreadcrumbItemView(name, stackSize), lp);
    }


    public LinearLayout getBreadcrumbItemView(String name, int stackSize) {
        LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.breadcrumb_toolbar_item, null);
        if (stackSize <= 1) {
            linearLayout.findViewById(R.id.breadcrumb_item_icon).setVisibility(View.GONE);
        }

        Button button = (Button) linearLayout.findViewById(R.id.breadcrumb_item_button);
        button.setText(name);
        button.setTransformationMethod(null);
        button.setOnClickListener(onClickItemListener);

        // Assign a sequential number to the item button
        button.setTag(stackSize);

        return linearLayout;
    }

    public void removeBreadcrumbItemFrom(int position) {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.breadcrumb_scroll_view_inner_layout);
        for (int i = linearLayout.getChildCount(); i > position; i--) {
            if (linearLayout.getChildAt(i - 1) != null) {
                linearLayout.removeViewAt(i - 1);
            }
        }
    }

    public void popBreadcrumbItem(int position) {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.breadcrumb_scroll_view_inner_layout);
        linearLayout.removeViewAt(position);
    }

    OnClickListener onClickItemListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (breadcrumbItemCallback != null) {
                int position = (Integer) v.getTag();
                breadcrumbItemCallback.onItemClick(position);
            }
        }
    };

    public void setBreadcrumbItemCallback(BreadcrumbItemCallback callback) {
        this.breadcrumbItemCallback = callback;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        // Always focus on the right most element when a new element is added
        fullScroll(getResources().getBoolean(R.bool.is_rtl) ? HorizontalScrollView.FOCUS_LEFT : HorizontalScrollView.FOCUS_RIGHT);
    }

}
