package com.rokpetek.breadcrumbtoolbar;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.Stack;


public class BreadcrumbToolbar extends android.support.v7.widget.Toolbar implements IBreadcrumbToolbar, BreadcrumbScrollView.BreadcrumbItemCallback {

    private static final String TAG = BreadcrumbToolbar.class.getSimpleName();

    // Data
    private String toolbarTitle;

    // Meta
    public static final String SAVE_INSTANCE_STATE_TAG = "save_instance_state_tag";
    public static final String SAVE_TOOLBAR_STACK_TAG = "save_toolbar_stack_tag";

    // Listeners
    private BreadcrumbToolbarListener breadcrumbToolbarListener;

    public interface BreadcrumbToolbarListener {
        void onBreadcrumbToolbarItemPop(int stackSize);

        void onDrawerToggleReset();

        String getFragmentName();
    }

    // You can serve any kind of object on the stack
    private Stack<BreadcrumbToolbarItem> toolbarItemStack = new Stack<>();
    private LayoutInflater inflater;
    private BreadcrumbScrollView breadcrumbScrollView;

    int stackSize;

    public BreadcrumbToolbar(Context context) {
        super(context);
        init();
    }

    public BreadcrumbToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BreadcrumbToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        setSaveEnabled(true);
        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public void initToolbar(BreadcrumbToolbarItem object) {
        breadcrumbScrollView = (BreadcrumbScrollView) inflater.inflate(R.layout.breadcrumb_scroll_view, null);
        breadcrumbScrollView.setBreadcrumbItemCallback(this);
        addView(breadcrumbScrollView);

        // Animate otherwise toolbar hamburger icon if exists, set navigation back icon otherwise
        if (getNavigationIcon() instanceof DrawerArrowDrawable) {
            animateNavigationIcon(((DrawerArrowDrawable) getNavigationIcon()), true);
        } else {
            initNavigationListener(true);
        }

        // Primary title needs to be saved
        if (getTitle() != null) {
            toolbarTitle = getTitle().toString();
        }

        // Clear toolbar title on breadcrumb before adding breadcrumbs
        setTitle("");

        // Breadcrumb scroll view is now initialized and needs its first root element
        addItem(object);
    }

    private void initNavigationListener(boolean withIcon) {
        if (withIcon) {
            setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        }
        setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (breadcrumbToolbarListener != null) {
                    breadcrumbToolbarListener.onBreadcrumbToolbarItemPop(toolbarItemStack.size());
                }
            }
        });
    }

    private void animateNavigationIcon(DrawerArrowDrawable arrowDrawable, final boolean showArrow) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(arrowDrawable, "progress", showArrow ? 1 : 0).setDuration(300);
        animator.start();
        animator.addListener(new AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (!showArrow && breadcrumbToolbarListener != null) {
                    breadcrumbToolbarListener.onDrawerToggleReset();
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        if (showArrow) {
            initNavigationListener(false);
        }
    }

    @Override
    public void addItem(@NonNull BreadcrumbToolbarItem object) {
        if (breadcrumbScrollView != null) {
            toolbarItemStack.add(object);
            stackSize++;
            breadcrumbScrollView.addItem(object.getName(), toolbarItemStack.size());
        } else {
            initToolbar(object);
        }
    }

    @Override
    public void removeItem() {
        stackSize--;
        Log.d(TAG, "[toolbar] removeItem stackSize zzz:" + stackSize);
        if (breadcrumbScrollView != null && breadcrumbToolbarListener != null) {
            if (stackSize > 0) {
                toolbarItemStack.pop();
                breadcrumbScrollView.popBreadcrumbItem(stackSize);
            } else {
                cleanToolbar();
            }
        }
    }

    @Override
    public void cleanToolbar() {
        if (breadcrumbScrollView != null) {
            toolbarItemStack.removeAllElements();

            // Navigation icon must be removed if none existed or animated.
            if (getNavigationIcon() instanceof DrawerArrowDrawable) {
                animateNavigationIcon(((DrawerArrowDrawable) getNavigationIcon()), false);
            } else {
                setNavigationIcon(null);
            }

            // Remove scroll view
            removeView(breadcrumbScrollView);
            breadcrumbScrollView = null;

            // Reset the toolbar title
            setTitle(toolbarTitle);
        }
    }

    @Override
    public void onItemClick(int position) {
        if (breadcrumbScrollView != null && breadcrumbToolbarListener != null) {
            for (int i = toolbarItemStack.size(); i > position; i--) {
                // We must call pop on views after removing the top item from stack
                breadcrumbToolbarListener.onBreadcrumbToolbarItemPop(i - 1);
            }
        }
    }

    public void onBreadcrumbAction(int toolbarStackSize) {
        if (breadcrumbToolbarListener != null) {
            String itemName = breadcrumbToolbarListener.getFragmentName();

            Log.d(TAG, "[toolbar] onBackStackChanged toolbarStackSize:" + toolbarStackSize + " stackSize:" + stackSize + " toolbarItemStack:" + toolbarItemStack.size() + " itemName:" + itemName);
            if (toolbarStackSize > stackSize) {
                addItem(new BreadcrumbToolbarItem(itemName));
            } else {
                removeItem();
            }
        }
    }

    private int getStackSize() {
        return toolbarItemStack.size();
    }

    public void setBreadcrumbToolbarListener(BreadcrumbToolbarListener listener) {
        this.breadcrumbToolbarListener = listener;
    }

    private Stack getToolbarItemStack() {
        return toolbarItemStack;
    }

    private void setRestoredStack(Stack<BreadcrumbToolbarItem> stack) {
        for (BreadcrumbToolbarItem item : stack) {
            addItem(item);
        }
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(SAVE_INSTANCE_STATE_TAG, super.onSaveInstanceState());
        bundle.putSerializable(SAVE_TOOLBAR_STACK_TAG, toolbarItemStack);
        return bundle;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            Object o = bundle.getSerializable(SAVE_TOOLBAR_STACK_TAG);

            // Stack gets de-serialized as an ArrayList - known bug !!!
            // Stack does not implement Serializable but rather just extends the Serializable Vector
            // which is equivalent to ArrayList
            Stack itemStack = new Stack();
            if (o instanceof Stack) {
                itemStack = (Stack) o;
            } else if (o instanceof ArrayList) {
                itemStack.addAll((ArrayList) o);
            }
            // Restore toolbar items
            setRestoredStack(itemStack);
            state = bundle.getParcelable(SAVE_INSTANCE_STATE_TAG);
        }
        super.onRestoreInstanceState(state);
    }

    @Override
    protected void dispatchSaveInstanceState(SparseArray<Parcelable> container) {
        super.dispatchFreezeSelfOnly(container);
    }

    @Override
    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
        super.dispatchThawSelfOnly(container);
    }
}
