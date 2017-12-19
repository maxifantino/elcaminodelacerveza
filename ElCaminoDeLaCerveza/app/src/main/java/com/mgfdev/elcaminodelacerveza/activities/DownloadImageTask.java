package com.mgfdev.elcaminodelacerveza.activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.mgfdev.elcaminodelacerveza.R;
import com.mgfdev.elcaminodelacerveza.services.MemoryCache;

import java.io.InputStream;

/**
 * Created by Maxi on 17/12/2017.
 */

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    private ImageView bmImage;
    private int height = -1;
    private int width = -1;
    private String url;
    public DownloadImageTask(ImageView bmImage, Integer height, Integer width, String url) {
        this.bmImage = bmImage;
        this.url = url;
        if (height != null && width != null){
            this.height = height;
            this.width = width;
        }
    }

    protected Bitmap doInBackground(String... urls) {
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(url).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        if (height != -1){
            //mIcon11 = ThumbnailUtils.extractThumbnail(mIcon11, width, height);
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        //final Drawable drawable = new BitmapDrawable(ctx.getResources(), result);

        bmImage.setImageBitmap(result);
        MemoryCache.getInstance().put(url,result);
    }
}
