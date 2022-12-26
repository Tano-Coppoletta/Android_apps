package com.example.a2;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebViewFragment;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class BrowserFragment extends Fragment{
    private static final String TAG = "BrowserFragment";
    WebView webView;
    private int currIdx = -1;
    private int arrayLen;
    private ListViewModel model;

    public BrowserFragment() {
        super() ;
        Log.i(TAG, "Created!") ;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, getClass().getSimpleName() + ":entered onCreate()");
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.browser_item, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedState) {
        super.onViewCreated(view, savedState);

        model = new ViewModelProvider(requireActivity()).get(ListViewModel.class);
        webView = (WebView) view.findViewById(R.id.webview);

        // retains last quote shown on config change
        model.getSelectedItem().observe(getViewLifecycleOwner(), item -> {

            if (item < 0 || item >= arrayLen)
                return;

            // Update UI
            currIdx = item;
            if(MainActivity.isHotel){
                webView.loadUrl(MainActivity.hotelUrlArray[currIdx]);
            }else{
                webView.loadUrl(MainActivity.attractionsUrlArray[currIdx]);
            }
        });

        if(MainActivity.isHotel){
            arrayLen = MainActivity.hotelArray.length;
        }else{
            arrayLen = MainActivity.attractionArray.length;
        }


        // Enable Javascript
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Force links and redirects to open in the WebView instead of in a browser
        webView.setWebViewClient(new WebViewClient());

    }

}
