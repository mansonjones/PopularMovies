package com.example.android.popularmovies2.app;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collection;
import java.util.List;

/**
 * Created by mansonjones on 8/21/15.
 */

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private List<String> mObjects;

    public ImageAdapter(Context c, List<String> objects) {
        mContext = c;
        mObjects = objects;
    }

    public String getItem(int position) {
        return mObjects.get(position);
    }

    public String getItem(String key, int position) {
        String value = "";
        try {
            JSONObject jsonObject = new JSONObject(mObjects.get(position));
            value = jsonObject.getString(key);
            return value;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return value;
    }

    public int getCount() {
      //  return mThumbIds.length;
        return mObjects.size();
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(100, 185));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        // imageView.setImageResource(mThumbIds[position]);
        // imageView.setImageResource(mObjects.get(position));
        String posterPath = "http://image.tmdb.org/t/p/w185" + getItem("poster_path",position);
        Picasso.with(mContext)
                .load(posterPath)
                        //   .load("http://image.tmdb.org/t/p/w185/kqjL17yufvn9OVLyXYpvtyrFfak.jpg")
                .placeholder(R.drawable.sample_0)
                .resize(100,185)
                .centerCrop()
                .into(imageView);


        /* old
        String posterPath = "http://image.tmdb.org/t/p/w185" + mObjects.get(position);
        Picasso.with(mContext)
                .load(posterPath)
             //   .load("http://image.tmdb.org/t/p/w185/kqjL17yufvn9OVLyXYpvtyrFfak.jpg")
                .placeholder(R.drawable.sample_0)
                .resize(100,185)
                .centerCrop()
                .into(imageView);
                */
        return imageView;
    }

    public void add(String object) {
        if (mObjects != null) {
            mObjects.add(object);
        }
        // This causes the grid view to update.
        notifyDataSetChanged();
    }
    // To Do: Add an addAll() method.  This will reduce the number of calls
    // to notifyDataSetChanged
    public void addAll(Collection<? extends String> collection) {
        if (mObjects != null) {
            mObjects.addAll(collection);
        }
        notifyDataSetChanged();
    }

    public void clear() {
        if (mObjects != null) {
            mObjects.clear();
        }
    }
    // references to our images
    /*
    private Integer[] mThumbIds = {
            R.drawable.sample_3, R.drawable.sample_3,
            R.drawable.sample_3, R.drawable.sample_5,
            R.drawable.sample_6, R.drawable.sample_7,
            R.drawable.sample_0, R.drawable.sample_1,
            R.drawable.sample_2, R.drawable.sample_3,
            R.drawable.sample_4, R.drawable.sample_5,
            R.drawable.sample_6, R.drawable.sample_7,
            R.drawable.sample_0, R.drawable.sample_1,
            R.drawable.sample_2, R.drawable.sample_3,
            R.drawable.sample_4, R.drawable.sample_5,
            R.drawable.sample_6, R.drawable.sample_7
    };
    */
}