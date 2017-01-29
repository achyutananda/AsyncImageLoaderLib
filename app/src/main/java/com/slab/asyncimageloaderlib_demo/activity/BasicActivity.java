package com.slab.asyncimageloaderlib_demo.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.slab.asyncimageloaderlib_demo.R;
import com.slab.imageloaderlib.LoadStringFromURL;
import com.slab.imageloaderlib.Loader;

/**
 * Basic uses of image loading library ,
 * -loading image,json etc
 * -Downloading file
 * -Concurrent loading of images with same and different resource url
 * -Configuration of max cache size
 * -Cancelling the image loading( if user tap on the thumbnail image while loading then that request will be cancelled.
 */
public class BasicActivity extends AppCompatActivity {

    final int PERMISSION_ALL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);


//loading IMAGE content to ImageView
        ImageView iv1 = (ImageView) findViewById(R.id.iv1);
        ImageView iv2 = (ImageView) findViewById(R.id.iv2);
        ImageView iv3 = (ImageView) findViewById(R.id.iv3);
        ImageView iv4 = (ImageView) findViewById(R.id.iv4);
        /**
         * Configure the max cache size
         */
        Loader.configureMaxCacheSize(5);

        //Concurrent loading of images to ImageView
        new Loader(this).loadImage(iv1, getString(R.string.url2));
        new Loader(this).loadImage(iv2, getString(R.string.url3));
        new Loader(this).loadImage(iv3, getString(R.string.url1));
        new Loader(this).loadImage(iv4, getString(R.string.url2));

        new Loader(this).loadImage(iv1, getString(R.string.url2));
        new Loader(this).loadImage(iv2, getString(R.string.url3));
        new Loader(this).loadImage(iv3, getString(R.string.url1));
        new Loader(this).loadImage(iv4, getString(R.string.url2));

        //Implementation 1
        //loading json content to TextView
//        TextView tv = (TextView) findViewById(R.id.tv);
//        Loader loader = new Loader(this);
//        loader.loadText(tv, "http://pastebin.com/raw/wgkJgazE");

        //Implementation 2
        final TextView tv = (TextView) findViewById(R.id.tv);
        LoadStringFromURL loaderTask = new LoadStringFromURL() {
            @Override
            public void onResponseReceived(Object result) {
                if (result != null)
                    tv.setText((String) result);
            }
        };
        loaderTask.execute(getString(R.string.data));


        String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE};//Manifest.permission.READ_EXTERNAL_STORAGE,

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        } else {
            //download file
            Loader fileDownloader = new Loader(this);
            /**
             * Specify the file type which is publicly available some implemented type available in Loader.java
             */
            fileDownloader.downLoadFile(Loader.JPG_FILE_TYPE, getString(R.string.url2));
        }
    }

    /**
     * @param context
     * @param permissions String array which contain the permission name
     * @return
     */
    private boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ALL: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //download file
                    Loader fileDownloader1 = new Loader(this);
                    fileDownloader1.downLoadFile(Loader.JPG_FILE_TYPE, getString(R.string.url1));

//                    Loader fileDownloader2 = new Loader(this);
//                    fileDownloader2.downLoadFile(Loader.PDF_FILE_TYPE, "<.pdf file url>");
//
//                    Loader fileDownloader3 = new Loader(this);
//                    fileDownloader3.downLoadFile(Loader.ZIP_FILE_TYPE, "<.zip file url>");
//
//                    Loader fileDownloader4= new Loader(this);
//                    fileDownloader4.downLoadFile(Loader.DOC_FILE_TYPE, "<.doc file url>");
                }
                return;
            }


        }
    }
}
