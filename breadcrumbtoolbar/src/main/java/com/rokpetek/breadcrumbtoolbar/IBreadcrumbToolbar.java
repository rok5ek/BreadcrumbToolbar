package com.rokpetek.breadcrumbtoolbar;

import android.support.annotation.NonNull;

public interface IBreadcrumbToolbar {

    void initToolbar(BreadcrumbToolbarItem object);

    void addItem(@NonNull BreadcrumbToolbarItem object);

    void removeItem();

    void cleanToolbar();

    void onBackStackChanged(int backStackCount);

    void onBlockBackStackChanged(boolean block);
}
