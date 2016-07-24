package com.rokpetek.breadcrumbapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class BreadcrumbFragment extends Fragment {

    public static final String FRAGMENT_NAME_TAG = "fragment_name";

    private String fragmentName;

    public static void open(FragmentActivity activity, boolean addToBackStack, String name) {
        Fragment fragment = new BreadcrumbFragment();
        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.main_content, fragment);
        if (addToBackStack) {
            ft.addToBackStack(null);
        }
        if (name != null) {
            Bundle bundle = new Bundle();
            bundle.putString(FRAGMENT_NAME_TAG, name);
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
            fragmentName = getArguments().getString(FRAGMENT_NAME_TAG);
            title.setText(fragmentName);
        }
        return rootView;
    }

    public String getFragmentName() {
        return fragmentName;
    }
}
