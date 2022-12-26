package com.example.mp2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> animals;
    ArrayList<String> urls;
    ArrayList<Integer> animalsPictures;
    RecyclerView nameView;
    private String key = "ISLIST";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nameView = findViewById(R.id.recycler_view);

        //retrive animal's names, urls and images from resources
        List<String> animalsNames = Arrays.asList(getResources().getStringArray(R.array.animals));
        List <String> animalsUrls = Arrays.asList(getResources().getStringArray(R.array.url_animals));
        animalsPictures = new ArrayList<>(Arrays.asList(
                R.drawable.lion, R.drawable.tiger, R.drawable.cheeth,R.drawable.zebra,R.drawable.giraffe,
                R.drawable.chimpanzee, R.drawable.raccoon,R.drawable.jackal,R.drawable.koala));


        animals = new ArrayList<>();
        urls = new ArrayList<>();

        animals.addAll(animalsNames);
        urls.addAll(animalsUrls);

        //listener for each animal's image to go to the wiki
        RVClickListener listener = (view, position) -> {
            Intent wikiIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urls.get(position)));
            wikiIntent.addCategory(Intent.CATEGORY_BROWSABLE);
            startActivity(wikiIntent);
        };

        RVClickListener display_size_image_listener = (view,position) -> {
          Intent displayIntent = new Intent(MainActivity.this, DisplaySizeImage.class);
          displayIntent.putExtra("image",(int)animalsPictures.get(position));
          startActivity(displayIntent);
        };

        MyAdapter adapter = new MyAdapter(animals,animalsPictures,listener,display_size_image_listener);
        nameView.setHasFixedSize(true);
        nameView.setAdapter(adapter);
        //if we have a saved instance we retrieve the previous organization of the layout, as list or as grid and we build the view according to that
        if(savedInstanceState == null){
            //first is organized as grid
            ((MyAdapter) nameView.getAdapter()).showAsList=false;
            nameView.setLayoutManager(new GridLayoutManager(this,2));
        }else{
            if((Boolean)savedInstanceState.get(key)){
                ((MyAdapter) nameView.getAdapter()).showAsList=true;
                nameView.setLayoutManager(new LinearLayoutManager(this));
            }else{
                ((MyAdapter) nameView.getAdapter()).showAsList=false;
                nameView.setLayoutManager(new GridLayoutManager(this,2));
            }
        }


    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        //save the organization throught a boolean (is list or not)
        super.onSaveInstanceState(outState);
        outState.putBoolean(key,((MyAdapter)nameView.getAdapter()).showAsList);
    }

    // Create Options Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.view_menu, menu);
        return true;
    }

    // Process clicks on Options Menu items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.list:

                if(((MyAdapter) nameView.getAdapter()).showAsList){
                    //already set as list view
                    Log.v("TOAST","SHOW list toast");
                    Toast.makeText(this,"View already set to List",Toast.LENGTH_SHORT).show();
                }else{
                    //set as list view
                   ((MyAdapter) nameView.getAdapter()).showAsList=true;
                    nameView.setLayoutManager(new LinearLayoutManager(this));
                }
                return true;
            case R.id.grid:
               if(!((MyAdapter) nameView.getAdapter()).showAsList){
                    //already set as grid view
                    Log.v("TOAST","SHOW grid toast");
                    Toast.makeText(this,"View already set to Grid",Toast.LENGTH_SHORT).show();
                }else{
                   //set as grid view
                    ((MyAdapter) nameView.getAdapter()).showAsList=false;
                    nameView.setLayoutManager(new GridLayoutManager(this,2));
                }
                return true;
            default:
                return false;
        }
    }

}