package com.ramadan;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.core.Config;
import com.core.DownloadManager;
import com.status.DownloadResult;
import com.status.DownloadStatus;
import com.ui.UIThreadCallback;
import com.util.Util;

import java.util.Date;


public class MainActivity extends AppCompatActivity implements UIThreadCallback {
    private static int REQUEST_PERMISSION = 0x0;
    private static final String TAG = MainActivity.class.getSimpleName();
    private DownloadManager mDownloadManager;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String root = Environment.getExternalStorageDirectory().toString();

        url = "https://images.homedepot-static.com/productImages/612ae505-9daf-45c3-ac16-67f97dcb251d/svn/globalrose-flower-bouquets-prime-100-red-roses-64_1000.jpg";

        String localPath = root + "/" + new Date().getTime() + "." + Util.getFileExtension(url);
        Log.e("localpath", localPath);

        Config config = new Config(5, 5, 30, localPath);
        mDownloadManager = DownloadManager.getInstance(config);
        mDownloadManager.setUiThreadCallback(this);
        isStoragePermissionGranted();

    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
                String url = "https://images.homedepot-static.com/productImages/612ae505-9daf-45c3-ac16-67f97dcb251d/svn/globalrose-flower-bouquets-prime-100-red-roses-64_1000.jpg";
                mDownloadManager.downloadFile(url);
String url2 ="https://i-h2.pinimg.com/564x/14/5c/69/145c69be0d39bfc078d2ea17502281a8.jpg";
                mDownloadManager.downloadFile(url);
                return true;
            } else {

                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted");
            mDownloadManager.downloadFile(url);
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.v(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
            //resume tasks needing this permission
            mDownloadManager.downloadFile(url);
            String url2 ="https://i-h2.pinimg.com/564x/14/5c/69/145c69be0d39bfc078d2ea17502281a8.jpg";
            mDownloadManager.downloadFile(url);

        }
    }


    @Override
    public void publishToUIThread(DownloadResult result) {
        DownloadStatus status = null;
        if (result != null)
            status = result.getDownloadStatus();

        Log.e("msg_ui", status.name());
        ((TextView) findViewById(R.id.statustextview)).setText(status.name());
    }

}
