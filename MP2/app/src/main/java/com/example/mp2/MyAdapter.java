package com.example.mp2;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private final ArrayList<String> nameList;
    private final ArrayList<Integer> imageList;
    private final RVClickListener RVlistener;
    private final RVClickListener RVdisplayListener;
    boolean showAsList = false; //initially we have a grid


    public MyAdapter(ArrayList<String> names,ArrayList<Integer> pictures, RVClickListener listener,RVClickListener display_size_image_listener){
        // save list of names to be displayed passed by main activity
        nameList = names;
        imageList = pictures;

        // save listener defined and passed by main activity
        this.RVlistener = listener;
        this.RVdisplayListener = display_size_image_listener;

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View listView = inflater.inflate(R.layout.rv_item,parent,false);

        ViewHolder viewHolder = new ViewHolder(listView,RVlistener,RVdisplayListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.ViewHolder holder, int position) {

        holder.setViewAsList(showAsList);
        holder.name.setText(nameList.get(position));
        holder.image.setImageResource(imageList.get(position));
    }

    @Override
    public int getItemCount() {
        return nameList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener{

        public TextView name;
        public ImageView image;
        private final RVClickListener listener;
        private final RVClickListener displayListener;
        private final View itemView;
        private final LinearLayout.LayoutParams imageInList;
        private final LinearLayout.LayoutParams textInList;
        private final LinearLayout.LayoutParams imageInGrid;
        private final LinearLayout.LayoutParams textInGrid;



        public ViewHolder(@NonNull View itemView, RVClickListener passedListener,RVClickListener displayListener) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.textView);
            image = (ImageView) itemView.findViewById(R.id.imageView);
            //set up list parameters
            imageInList = new LinearLayout.LayoutParams((LinearLayout.LayoutParams) image.getLayoutParams());
            imageInList.width = 0;
            imageInList.height = 400;
            imageInList.weight=0.65f;
            textInList = new LinearLayout.LayoutParams((LinearLayout.LayoutParams) name.getLayoutParams());
            textInList.width=0;
            textInList.height=400;
            textInList.weight=0.35f;
            //set up grid parameters
            imageInGrid = new LinearLayout.LayoutParams((LinearLayout.LayoutParams) image.getLayoutParams());
            textInGrid = new LinearLayout.LayoutParams((LinearLayout.LayoutParams) name.getLayoutParams());
            name.setTextSize(20f);

            //first we show as grid


            this.itemView = itemView;
            itemView.setOnCreateContextMenuListener(this); //set context menu for each list item (long click)
            this.listener = passedListener;
            this.displayListener = displayListener;


            itemView.setOnClickListener(this); //set short click listener
        }

        public void setViewAsList(boolean isList){
            if(isList){
                //set view as list
                ((LinearLayout) itemView).setOrientation(LinearLayout.HORIZONTAL);
                image.setLayoutParams(imageInList);
                name.setLayoutParams(textInList);
            }else{
                //set view as grid
                ((LinearLayout) itemView).setOrientation(LinearLayout.VERTICAL);
                image.setLayoutParams(imageInGrid);
                name.setLayoutParams(textInGrid);
            }
        }


        @Override
        public void onClick(View v) {
            // getAdapterPosition() returns the position of the current ViewHolder in the adapter.
            listener.onClick(v, getAdapterPosition());
            Log.i("ON_CLICK", "in the onclick in view holder");
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

            //create menu in code

            //add menu items and set the listener for each
            menu.add(0,v.getId(),0,"Go to Wiki").setOnMenuItemClickListener(onMenuGoToWiki);
            menu.add(0,v.getId(),0,"See bigger image").setOnMenuItemClickListener(onMenuBigImage);


        }

        /*
            listener for menu items clicked
         */
        private final MenuItem.OnMenuItemClickListener onMenuGoToWiki = new MenuItem.OnMenuItemClickListener(){
            @Override
            public boolean onMenuItemClick(MenuItem item){
                listener.onClick(itemView,getAdapterPosition());
                return true;
            }
        };

        private final MenuItem.OnMenuItemClickListener onMenuBigImage = new MenuItem.OnMenuItemClickListener(){
            @Override
            public boolean onMenuItemClick(MenuItem item){
                displayListener.onClick(itemView,getAdapterPosition());
                return true;
            }
        };



    }
}
