package com.rokpetek.breadcrumbapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class BreadcrumbFragment extends Fragment {

    public static final String FRAGMENT_NUMBER_TAG = "fragment_number";

    public static void open(FragmentActivity activity, boolean addToBackStack, Integer number) {
        Fragment fragment = new BreadcrumbFragment();
        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.main_content, fragment);
        if (addToBackStack) {
            ft.addToBackStack(null);
        }
        if (number != null) {
            Bundle bundle = new Bundle();
            bundle.putInt(FRAGMENT_NUMBER_TAG, number);
            fragment.setArguments(bundle);
        }
        ft.commit();
    }

    public BreadcrumbFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_breadcrumb, container, false);
        TextView title = (TextView) rootView.findViewById(R.id.breadcrumb_folder_name);
        if (getArguments() != null) {
            String text = getString(R.string.breadcrumb_name, getArguments().getInt(FRAGMENT_NUMBER_TAG));
            title.setText(text);

//            BreadcrumbToolbarItem item = new BreadcrumbToolbarItem(text);
//            .addItem(item);
        }
        return rootView;
    }
}
