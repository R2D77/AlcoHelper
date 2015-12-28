package com.fd2r.alcohelper;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

/**
 * Created by roman on 23.12.15.
 */
public class Fragment_alco extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.alco, container, false);


        // UNIVERSAL IMAGE LOADER SETUP
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getContext())
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .diskCacheSize(100 * 1024 * 1024).build();

        ImageLoader.getInstance().init(config);
        // END - UNIVERSAL IMAGE LOADER SETUP

        ImageLoader imageLoader = ImageLoader.getInstance();
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisk(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(R.drawable.cast_ic_notification_0)
                .showImageOnFail(R.drawable.common_google_signin_btn_icon_light_disabled)
                .showImageOnLoading(R.drawable.common_full_open_on_phone).build();

        String url1 = "https://en.wikipedia.org/static/apple-touch/wikipedia.png";
        String url2 = "https://upload.wikimedia.org/wikipedia/en/6/6e/New_York_Cosmos_originalcrest.png";
        String url3 = "http://theskymap.com/img/cosmos.png";

        ImageView imageView1 = (ImageView)rootView.findViewById(R.id.image1);
        ImageView imageView2 = (ImageView)rootView.findViewById(R.id.image2);
        ImageView imageView3 = (ImageView)rootView.findViewById(R.id.image3);

        imageLoader.displayImage(url3, imageView3, options);
        imageLoader.displayImage(url2, imageView2, options);
        imageLoader.displayImage(url1, imageView1, options);

        return rootView;
    }
}
