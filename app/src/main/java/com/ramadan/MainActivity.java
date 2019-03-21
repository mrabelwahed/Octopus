package com.ramadan;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.core.DownloadManager;
import com.core.DownloadTask;
import com.status.DownloadResult;
import com.status.DownloadStatus;
import com.ui.UIThreadCallback;
import com.util.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;




public class MainActivity extends AppCompatActivity implements UIThreadCallback {
    public static final String url3 = "https://doc-00-50-docs.googleusercontent.com/docs/securesc/1r14jt81f7jnfbd9piaptmojvlobuogr/13epqo5he43gt6o7bntmpn8k9lc12tt7/1553083200000/14676411655443883941/14676411655443883941/0BwnvTqAnwmkaYVlfQzRrRU1uX3c?e=download&nonce=g7ft2cf127jho&user=14676411655443883941&hash=rh3qsfv5cvk2udc05sn2hrb0j4chbb0f";
    private static int REQUEST_PERMISSION = 0x0;
    private static final String TAG = MainActivity.class.getSimpleName();
    private DownloadManager mDownloadManager;
    private String url1, url2;
    private File file1, file2;
    private String localPath1, localPath2;
    private RecyclerView downloadTasksRecyclerView;
    private List<DownloadTask> downloadTasks = new ArrayList<>();
    TasksAdapter tasksAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mockData();
        isStoragePermissionGranted();
        initUi();


    }

    private void mockData() {
        url1 = "https://images.homedepot-static.com/productImages/612ae505-9daf-45c3-ac16-67f97dcb251d/svn/globalrose-flower-bouquets-prime-100-red-roses-64_1000.jpg";
        url2 = "https://i-h2.pinimg.com/564x/14/5c/69/145c69be0d39bfc078d2ea17502281a8.jpg";
        // Get the external storage directory path
        String path = Environment.getExternalStorageDirectory().toString() + "/ramadan";
        new File(path).mkdir();
        file1 = new File(path, "filex" + new Date().getTime() + "." + Util.getFileExtension(url1));
        localPath1 = file1.getPath();

        file2 = new File(path, "filey" + new Date().getTime() + "." + Util.getFileExtension(url2));
        localPath2 = file2.getPath();

        DownloadTask downloadTask1 = new DownloadTask(new Date().getTime(), url3, localPath1, this);
        downloadTask1.setFileName("task 1");
        DownloadTask downloadTask2 = new DownloadTask.Builder(url1).destination(localPath1)
                .fileId(new Date().getTime()).fileName("task 2").UiThreadCallback(this).build();
        DownloadTask downloadTask3 = new DownloadTask(new Date().getTime(), url2, localPath2, this);
        downloadTask3.setFileName("task 3");

        DownloadTask downloadTask4 = new DownloadTask.Builder(url1).destination(localPath1)
                .fileId(new Date().getTime()).fileName("task 4").UiThreadCallback(this).build();

        DownloadTask downloadTask5 = new DownloadTask.Builder(url1).destination(localPath1)
                .fileId(new Date().getTime()).fileName("task 5").UiThreadCallback(this).build();

        DownloadTask downloadTask6 = new DownloadTask.Builder(url1).destination(localPath1)
                .fileId(new Date().getTime()).fileName("task 6").UiThreadCallback(this).build();

        DownloadTask downloadTask7 = new DownloadTask.Builder(url1).destination(localPath1)
                .fileId(new Date().getTime()).fileName("task 7").UiThreadCallback(this).build();


        downloadTasks.add(downloadTask1);
        downloadTasks.add(downloadTask2);
        downloadTasks.add(downloadTask3);
        downloadTasks.add(downloadTask4);
        downloadTasks.add(downloadTask5);
        downloadTasks.add(downloadTask6);
        downloadTasks.add(downloadTask7);
        for(DownloadTask downloadTask : downloadTasks){
            downloadTask.setDestination(new File(path, downloadTask.getFileName() + "_" + new Date().getTime() + "." + Util.getFileExtension(downloadTask.getUrl())).getPath());
        }
    }

    private void initUi() {
        downloadTasksRecyclerView = findViewById(R.id.rv_tasks);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        downloadTasksRecyclerView.setLayoutManager(linearLayoutManager);

        tasksAdapter = new TasksAdapter(this, downloadTasks);
        downloadTasksRecyclerView.setAdapter(tasksAdapter);




    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
                return false;
            }
        } else {
            //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.v(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
            //resume tasks needing this permission
        }
    }


    @Override
    public void publishToUIThread(DownloadResult result) {
        for (DownloadTask downloadTask : downloadTasks) {
            if (downloadTask.getId() == result.getId()) {
                downloadTask.setProgress(result.getProgress());
            }
        }
        tasksAdapter.notifyDataSetChanged();
    }

}
