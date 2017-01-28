package com.slab.imageloaderlib;


import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public abstract class LoadStringFromURL extends AsyncTask<String, Void, String> {


    @Override
    protected String doInBackground(String... params) {
        /**
         * check in cache first if not available then go for download
         */
        if (Cache.memoryCacheStorage.containsKey(params[0])) {
//            Log.i("found text", params[0] + "\nCache Size:" + Cache.memoryCacheStorage.size());
            return (String) Cache.memoryCacheStorage.get(params[0]);
        } else {
//            Log.i("not found text", params[0] + "\n Cache Size:" + Cache.memoryCacheStorage.size());
            String result = getContentFromURL(params[0]);
            Cache.memoryCacheStorage.put(params[0], result);
            return result;
        }

    }

    @Override
    protected void onPostExecute(String result) {
        if (isCancelled()) {
            result = null;
        }

        onResponseReceived(result);

    }
    public abstract void onResponseReceived(Object result);
    /**
     * This method will download the content from url
     *
     * @param url public link ,if this not found then this method will return null
     * @return String object
     */
    private String getContentFromURL(String url) {
        try {
            URL website = new URL(url);
            URLConnection connection = website.openConnection();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            connection.getInputStream()));

            StringBuilder response = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null)
                response.append(inputLine);

            in.close();

            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
