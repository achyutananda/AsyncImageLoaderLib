package com.slab.imageloaderlib;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class Loader {

    /**
     * Constant for file type
     */
    public static final int JPG_FILE_TYPE = 1;
    public static final int PDF_FILE_TYPE = 2;
    public static final int ZIP_FILE_TYPE = 3;
    public static final int DOC_FILE_TYPE = 4;
    /**
     * Reference for Context
     */
    private Context mContext;

    /**
     * Reference for downloader
     */
    private ImageDownloaderTask imageDownloaderTask;
    private TextContentDownloaderTask textContentDownloaderTask;
    private DownloadFileTask downloadFileTask;


    /**
     * default initialization of Loader Constructor
     *
     * @param mContext reference to Context
     */
    public Loader(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * Constructor with maxSize which  will define upto how many images it will cache and when it exceed it will remove the least often used item from cache.
     *
     * @param mContext reference to Context
     * @param maxSize  restrict number of images it will cache
     */
    public Loader(Context mContext, int maxSize) {
        this.mContext = mContext;
        Cache.MAX_SIZE = maxSize;
    }

    /**
     * @param maxSize call this method if you want to configure the CACHE SIZE, default cache size is 10
     */
    public static void configureMaxCacheSize(int maxSize) {
        Cache.MAX_SIZE = maxSize;
    }

    /**
     * @param mImageView ,in this view downloaded content will loaded
     * @param url,       public remote resource URL
     */
    public void loadImage(ImageView mImageView, String url) {
        if (mImageView == null || url == null) {
            return;
        }
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelLoadingImage();
            }
        });
        mImageView.setImageResource(R.drawable.ic_image);
        imageDownloaderTask = new ImageDownloaderTask(mImageView);
        imageDownloaderTask.execute(url);
    }

    /**
     * @param mTextView , this will be loaded with JSON, XML, etc
     * @param url       , URL for the remote resource
     */
    public void loadText(TextView mTextView, String url) {
        if (mTextView == null || url == null) {
            return;
        }
        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelLoadingTextContent();
            }
        });
        mTextView.setText(R.string.downloading);
        textContentDownloaderTask = new TextContentDownloaderTask(mTextView);
        textContentDownloaderTask.execute(url);
    }

    /**
     * @param fileType , this will be any file type i.e. zip,pdf,jpg,doc etc
     * @param url      , URL for the remote resource
     */
    public void downLoadFile(int fileType, String url) {
        downloadFileTask = new DownloadFileTask(mContext, fileType);
        downloadFileTask.execute(url);
    }

    /**
     * cancelLoadingImage() will cancel the particular downloading content, this will not effect the other
     * downloading options
     */
    private void cancelLoadingImage() {
        if (imageDownloaderTask != null && !imageDownloaderTask.isCancelled()) {
            imageDownloaderTask.cancel(true);
            Log.i("Cancel Loader", " imageDownloaderTask id-" + imageDownloaderTask.toString());
        }
    }

    /**
     * cancelLoadingTextContent() will cancel the particular downloading content, this will not effect the other
     * downloading options
     */
    private void cancelLoadingTextContent() {
        if (textContentDownloaderTask != null && !textContentDownloaderTask.isCancelled()) {
            textContentDownloaderTask.cancel(true);
            Log.i("Cancel Loader", " textContentDownloaderTask id-" + textContentDownloaderTask.toString());
        }
    }

    /**
     * cancelDownloadingFile() will cancel the particular downloading content, this will not effect the other
     * downloading options
     */
    private void cancelDownloadingFile() {
        if (downloadFileTask != null && !downloadFileTask.isCancelled()) {
            downloadFileTask.cancel(true);
            Log.i("Cancel Loader", " downloadFileTask id-" + downloadFileTask.toString());
        }
    }


}
