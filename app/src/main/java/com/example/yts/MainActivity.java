package com.example.yts;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.example.yts.core.fetcher.SearchFilterFetcher;
import com.example.yts.torrent.TorrentDownloadsList;
import com.example.yts.ui.downloads.DownloadsFragment;
import com.example.yts.ui.home.HomeFragment;
import com.example.yts.ui.search.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Fragment selectedFragment = new HomeFragment(getSupportFragmentManager());
    private int currentFragment = R.id.navigation_home;
    private BottomNavigationView navView;
    private SearchFilterFetcher searchFilterFetcher;
    private ArrayList<Integer> fragmentIndex = new ArrayList<>();
    private boolean onDownloadsSection = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentIndex.add(0);
        setContentView(R.layout.activity_main);
        navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        try {
            searchFilterFetcher = new SearchFilterFetcher(getString(R.string.yts_movies));
        } catch (IOException e) {
            e.printStackTrace();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_fragment_container, selectedFragment).commit();
        requestPermission();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        if(onDownloadsSection){
            boolean bool = ((TorrentDownloadsList) getApplication()).getHasSelectedItem();
            menu.setGroupVisible(R.id.group_1,true);
            menu.findItem(R.id.delete).setVisible(true);
            if(bool){
                menu.findItem(R.id.delete).setVisible(true);
            }else{
                menu.findItem(R.id.delete).setVisible(false);
            }
        }else{
            menu.setGroupVisible(R.id.group_1,false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.delete:
                final boolean[] deleteFolder = {false};
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("Are you sure you want to delete these files?");
                alertDialogBuilder.setCancelable(true);

                String[] strings = {"Also delete files in storage"};
                boolean[] checkedItems = {false};
                alertDialogBuilder.setMultiChoiceItems(strings, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        deleteFolder[0] = isChecked;
                    }
                });

                alertDialogBuilder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ((TorrentDownloadsList) getApplication()).selectedItemClearDelete(deleteFolder[0]);
                            }
                        });
                alertDialogBuilder.setNegativeButton("No", null);
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
//                ((TorrentDownloadsList) getApplication()).selectedItemClearDelete(true);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener(){
        String fragmentTag = "";
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.navigation_home:
                    if(currentFragment != item.getItemId()){
                        currentFragment = item.getItemId();
                        selectedFragment = new HomeFragment(getSupportFragmentManager());
                        fragmentIndex.add(0);
                        fragmentTag = "home";
                        invalidateOptionsMenu();
                        onDownloadsSection = false;
                    }
                    break;
                case R.id.navigation_search:
                    if(currentFragment != item.getItemId()){
                        currentFragment = item.getItemId();
                        selectedFragment = new SearchFragment(getSupportFragmentManager(), searchFilterFetcher);
                        fragmentIndex.add(1);
                        fragmentTag = "search";
                        invalidateOptionsMenu();
                        onDownloadsSection = false;
                    }
                    break;
                case R.id.navigation_downloads:
                    if(currentFragment != item.getItemId()){
                        currentFragment = item.getItemId();
                        selectedFragment = new DownloadsFragment();
                        fragmentIndex.add(2);
                        fragmentTag = "downloads";
                        invalidateOptionsMenu();
                        onDownloadsSection = true;
                    }
                    break;
            }
            if(fragmentTag != "")
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_fragment_container, selectedFragment, fragmentTag).addToBackStack(fragmentTag).commit();
            return true;
        }
    };

    @Override
    public void onBackPressed(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            boolean bool = ((TorrentDownloadsList) getApplication()).getHasSelectedItem();
            if(currentFragment == R.id.navigation_downloads && bool){
                ((TorrentDownloadsList) getApplication()).resetSelectedItems();
            }else{
                String tag = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName();
                Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_fragment_container, fragment).commit();
                if(tag != "movie_details"){
                    fragmentIndex.remove(fragmentIndex.size()-1);
                    navView.getMenu().getItem((fragmentIndex.get(fragmentIndex.size()-1))).setChecked(true);
                }
                fragmentManager.popBackStackImmediate();
            }
        } else {
            super.onBackPressed();
        }
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[] {
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[]
            permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    // check whether storage permission granted or not.
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    }
                }
                break;
            default:
                break;
        }
    }
}