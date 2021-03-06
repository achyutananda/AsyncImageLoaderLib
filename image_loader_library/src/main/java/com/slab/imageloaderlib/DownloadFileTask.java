package com.slab.imageloaderlib;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;


public class DownloadFileTask extends AsyncTask<String, Integer, String> {

    private int type;
    private String filePath;
    private ProgressDialog mProgressDialog;
    private Context mContext;
    private PowerManager.WakeLock mWakeLock;

    /**
     * @param mContext
     * @param type     : Type of file need to be downloaded
     */
    public DownloadFileTask(Context mContext, int type) {
        this.mContext = mContext;
        this.type = type;
    }

    @Override
    protected String doInBackground(String... sUrl) {
        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(sUrl[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            // expect HTTP 200 OK, so we don't mistakenly save error report
            // instead of the file
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return "Server returned HTTP " + connection.getResponseCode()
                        + " " + connection.getResponseMessage();
            }

            // this will be useful to display download percentage
            // might be -1: server did not report the length
            int fileLength = connection.getContentLength();

            // download the file
            input = connection.getInputStream();
            filePath = getFileName(type);
            output = new FileOutputStream(filePath);

            byte data[] = new byte[4096];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                // allow canceling with back button
                if (isCancelled()) {
                    input.close();
                    return null;
                }
                total += count;
                // publishing the progress....
                if (fileLength > 0) // only if total length is known
                    publishProgress((int) (total * 100 / fileLength));
                output.write(data, 0, count);
            }
        } catch (Exception e) {
            return e.toString();
        } finally {
            try {
                if (output != null)
                    output.close();
                if (input != null)
                    input.close();
            } catch (IOException ignored) {
            }

            if (connection != null)
                connection.disconnect();
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // take CPU lock to prevent CPU from going off if the user
        // presses the power button during download
        PowerManager pm = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                getClass().getName());
        mWakeLock.acquire();

        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage(mContext.getString(R.string.downloading));
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(true);

        mProgressDialog.show();
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        super.onProgressUpdate(progress);
        // if we get here, length is known, now set indeterminate to false
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setMax(100);
        mProgressDialog.setProgress(progress[0]);
    }

    @Override
    protected void onPostExecute(String result) {
        mWakeLock.release();
        mProgressDialog.dismiss();
        if (result != null)
            Toast.makeText(mContext, "Download error: " + result, Toast.LENGTH_LONG).show();
        else
            Toast.makeText(mContext, "File downloaded\n" + filePath, Toast.LENGTH_SHORT).show();
    }

    /**
     * It will generate a random value from min to max
     *
     * @param min
     * @param max
     * @return
     */
    private int randInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }

    /**
     *
     * @param type Type of file to be downloaded
     * @return String which will be the file name
     */
    private String getFileName(int type) {
        String filePath = null;
        switch (type) {
            case Loader.JPG_FILE_TYPE: {
                filePath = Environment.getExternalStorageDirectory() + "/recentJPG" + randInt(1, 100000) + ".jpg";
                break;
            }
            case Loader.PDF_FILE_TYPE: {
                filePath = Environment.getExternalStorageDirectory() + "/recentPDF" + randInt(1, 100000) + ".pdf";
                break;
            }
            case Loader.ZIP_FILE_TYPE: {
                filePath = Environment.getExternalStorageDirectory() + "/recentZIP" + randInt(1, 100000) + ".zip";
                break;
            }
            case Loader.DOC_FILE_TYPE: {
                filePath = Environment.getExternalStorageDirectory() + "/recentZIP" + randInt(1, 100000) + ".doc";
                break;
            }
            default:
                break;
        }
        Log.i("FilePath", filePath);
        return filePath;
    }
}