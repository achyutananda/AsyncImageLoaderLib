package com.slab.imageloaderlib;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

public class ImageDownloaderTask extends AsyncTask<String, Void, Bitmap> {

    private ImageView mImageView;

    public ImageDownloaderTask(ImageView mImageView) {
        this.mImageView = mImageView;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        /**
         * check in cache first if not available then go for download
         */
        if (Cache.memoryCacheStorage.containsKey(params[0])) {
//            Log.i("found", params[0] + "\nCache Size:"+Cache.memoryCacheStorage.size());
            return (Bitmap) Cache.memoryCacheStorage.get(params[0]);
        } else {
//            Log.i("not found", params[0] + "\n Cache Size:"+Cache.memoryCacheStorage.size());

            Bitmap bitmap = getContentFromURL(params[0]);
            Cache.memoryCacheStorage.put(params[0], bitmap);
            return bitmap;
        }

    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (isCancelled()) {
            bitmap = null;
        }


        if (mImageView != null && bitmap != null) {

            mImageView.setImageBitmap(bitmap);

        }


    }

    /**
     * This method will download the content from url
     *
     * @param src url public link ,if this not found then this method will return null
     * @return Bitmap object
     */
    private Bitmap getContentFromURL(String src) {
        try {
            java.net.URL url = new java.net.URL(src);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            try {
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                options.inScaled = false;
//               return  BitmapFactory.decodeStream(input,null,options);
                return BitmapFactory.decodeStream(input);
            } catch (Error error) {
                error.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
