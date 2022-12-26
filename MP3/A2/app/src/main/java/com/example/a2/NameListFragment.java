package com.example.a2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.ListFragment;
import androidx.lifecycle.ViewModelProvider;

public class NameListFragment extends ListFragment {
    private ListViewModel model;

    private static final String TAG = "NameListFragment";

    public NameListFragment(){
        super();

        Log.i(TAG, "Fragment created!");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, getClass().getSimpleName() + ":entered onCreate()");
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int pos, long id){
        // Indicates the selected item has been checked
        getListView().setItemChecked(pos, true);
        // Inform the ModelView that the selection may have changed
        model.selectItem(pos);
    }

    @Override
    public void onViewCreated(View view, Bundle savedState) {
        super.onViewCreated(view, savedState);
        model = new ViewModelProvider(requireActivity()).get(ListViewModel.class);

        if(MainActivity.isHotel){
            //in this case we are displaying the list of hotels
            setListAdapter(new ArrayAdapter<>(getActivity(),
                    R.layout.list_item, MainActivity.hotelArray));
        }else{
            //in this case we are displaying the list of attractions
            setListAdapter(new ArrayAdapter<>(getActivity(),
                    R.layout.list_item, MainActivity.attractionArray));
        }
        // Set the list choice mode to allow only one selection at a time
        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }


}
