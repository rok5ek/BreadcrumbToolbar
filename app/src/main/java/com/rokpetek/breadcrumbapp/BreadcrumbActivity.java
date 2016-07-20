package com.rokpetek.breadcrumbapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.rokpetek.breadcrumbtoolbar.BreadcrumbToolbar;
import com.rokpetek.breadcrumbtoolbar.BreadcrumbToolbar.BreadcrumbToolbarListener;
import com.rokpetek.breadcrumbtoolbar.BreadcrumbToolbarItem;

public class BreadcrumbActivity extends AppCompatActivity implements OnBackStackChangedListener, BreadcrumbToolbarListener {

    private static final String TAG = BreadcrumbActivity.class.getSimpleName();

    // Gui
    private BreadcrumbToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breadcrumb);
        toolbar = (BreadcrumbToolbar) findViewById(R.id.toolbar);
        toolbar.setBreadcrumbToolbarListener(this);

        if (savedInstanceState == null) {
            BreadcrumbFragment.open(this, false, null);
        }
        getSupportFragmentManager().addOnBackStackChangedListener(this);

        // TODO delete
        int backCount = getSupportFragmentManager().getBackStackEntryCount();
        Log.i(TAG, "[app] onCreate backCount:" + backCount);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener((View view) -> {
            // Add item in toolbar
            toolbar.addItem(new BreadcrumbToolbarItem("item " + (getSupportFragmentManager().getBackStackEntryCount() + 1)));
            // Open a new fragment
            BreadcrumbFragment.open(this, true, getSupportFragmentManager().getBackStackEntryCount() + 1);
            // Show snackbar
            Snackbar.make(view, R.string.new_folder_created, Snackbar.LENGTH_LONG)
                    .setAction(R.string.undo, (View v) -> {
                        onBackPressed();
                    }).show();
        });
    }

    @Override
    public void onBackStackChanged() {
        int backCount = getSupportFragmentManager().getBackStackEntryCount();
        Log.i(TAG, "[app] onBackStackChanged backCount:" + backCount);
        // Notify toolbar of stack change
        if (toolbar != null) {
//            toolbar.onBackStackChanged(backCount);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_breadcrumb, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        int backCount = getSupportFragmentManager().getBackStackEntryCount();
        if (backCount > 0) {
            if (toolbar != null) {
                toolbar.removeItem();
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onBreadcrumbToolbarItemPop(int stackSize) {
        // We need to adopt the method popBackStackImmediate() if are removing multiple fragments.
        getSupportFragmentManager().popBackStack();
    }
}
