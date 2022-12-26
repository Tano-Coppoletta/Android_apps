package com.example.a2;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

public class HotelActivity extends AppCompatActivity {

    private FrameLayout nameFrameLayout, browserFrameLayout;

    FragmentManager mFragmentManager;

    private BrowserFragment browserFragment;
    private NameListFragment nameListFragment;

    private final String LIST_FRAGMENT_TAG = "list_tag";
    private final String DIPLAY_FRAGMENT_TAG = "display_tag";

    private static final int MATCH_PARENT = LinearLayout.LayoutParams.MATCH_PARENT;

    private ListViewModel mModel ;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get references to the NameListFragment and to the BrowserFragment
        nameFrameLayout = (FrameLayout) findViewById(R.id.names_fragment_container);
        browserFrameLayout = (FrameLayout) findViewById(R.id.browser_fragment_container);

        // Get a reference to the SupportFragmentManager
        mFragmentManager = getSupportFragmentManager();

        // Start a new FragmentTransaction
        final FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

        nameListFragment = (NameListFragment) mFragmentManager.findFragmentByTag(LIST_FRAGMENT_TAG);
        if(nameListFragment == null){
            nameListFragment = new NameListFragment();
        }

        // Add the nameFragment to the layout
        fragmentTransaction.replace(
                R.id.names_fragment_container,
                nameListFragment, LIST_FRAGMENT_TAG);

        // Commit the FragmentTransaction
        fragmentTransaction.commit();

        // Add a OnBackStackChangedListener to reset the layout when the back stack changes
        mFragmentManager.addOnBackStackChangedListener(
                this::setLayout);

        browserFragment = (BrowserFragment) mFragmentManager.findFragmentByTag(DIPLAY_FRAGMENT_TAG);
        if(browserFragment == null){
            browserFragment = new BrowserFragment();
        }

        mModel = new ViewModelProvider(this).get(ListViewModel.class) ;
        mModel.getSelectedItem().observe(this, item -> {
            if (!browserFragment.isAdded()) {
                FragmentTransaction fragmentTransaction2 = mFragmentManager.beginTransaction() ;

                // add quote fragment to display
                fragmentTransaction2.add(R.id.browser_fragment_container,
                        browserFragment,DIPLAY_FRAGMENT_TAG);

                // Add this FragmentTransaction to the backstack
                fragmentTransaction2.addToBackStack(null);

                // Commit the FragmentTransaction
                fragmentTransaction2.commit();

                // Force Android to execute the committed FragmentTransaction
                mFragmentManager.executePendingTransactions();
            }
        });
        setLayout() ;
    }

    private void setLayout() {

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Determine whether the hotelDisplayFragment has been added
            if (!browserFragment.isAdded()) {

                // Make the List of names occupy the entire layout
                nameFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(
                        MATCH_PARENT, MATCH_PARENT));
                browserFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(0,
                        MATCH_PARENT));
            } else {

                // Make the nameFrameLayout take 1/3 of the layout's width
                nameFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(0,
                        MATCH_PARENT, 1f));

                // Make the browserFrameLayout take 2/3's of the layout's width
                browserFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(0,
                        MATCH_PARENT, 2f));
            }
        }else{
            if(!browserFragment.isAdded()){
                nameFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(
                        MATCH_PARENT, MATCH_PARENT));
                browserFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(0,
                        MATCH_PARENT));
            }else{
                nameFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(0,
                        MATCH_PARENT, 0));


                browserFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(0,
                        MATCH_PARENT,1));
            }
        }


    }


    // Create Options Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);
        return true;
    }

    // Process clicks on Options Menu items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.hotels:
                MainActivity.isHotel=true;
                intent = new Intent(HotelActivity.this,HotelActivity.class);
                startActivity(intent);
                return true;
            case R.id.attractions:
                MainActivity.isHotel=false;
                intent = new Intent(HotelActivity.this, AttractionActivity.class);
                startActivity(intent);
                return true;
            default:
                return false;
        }
    }
}
