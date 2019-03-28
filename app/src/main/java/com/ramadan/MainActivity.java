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

import com.core.DownloadTask;
import com.status.DownloadResult;
import com.ui.UIThreadCallback;
import com.util.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class MainActivity extends AppCompatActivity implements UIThreadCallback {
    private static int REQUEST_PERMISSION = 0x0;
    private static final String TAG = MainActivity.class.getSimpleName();
    private String url1, url2;
    private File file1, file2;
    private String localPath1, localPath2;
    private RecyclerView downloadTasksRecyclerView;
    private List<DownloadTask> downloadTasks = new ArrayList<>();
    private TasksAdapter tasksAdapter;

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
        file1 = new File(path, "filex" + generateUniqueId() + "." + Util.getFileExtension(url1));
        localPath1 = file1.getPath();

        file2 = new File(path, "filey" + generateUniqueId() + "." + Util.getFileExtension(url2));
        localPath2 = file2.getPath();

        DownloadTask downloadTask1 = new DownloadTask(generateUniqueId(),
                "https://sample-videos.com/img/Sample-jpg-image-1mb.jpg", localPath1, this);
        downloadTask1.setFileName("task 1 - 1 MB");

        DownloadTask downloadTask2 = new DownloadTask.Builder("https://sample-videos.com/img/Sample-jpg-image-2mb.jpg").destination(localPath1)
                .fileId(generateUniqueId()).fileName("task 2 - 2 MB").UiThreadCallback(this).build();

        DownloadTask downloadTask3 = new DownloadTask(generateUniqueId(),
                "https://sample-videos.com/img/Sample-jpg-image-5mb.jpg"
                , localPath2, this);
        downloadTask3.setFileName("task 3 - 5 MB");

        DownloadTask downloadTask4 = new DownloadTask.Builder("https://sample-videos.com/img/Sample-jpg-image-10mb.jpg")
                .destination(localPath1)
                .fileId(generateUniqueId())
                .fileName("task 4 - 10 MB")
                .UiThreadCallback(this).build();

        DownloadTask downloadTask5 = new DownloadTask.Builder("https://sample-videos.com/img/Sample-jpg-image-15mb.jpeg")
                .destination(localPath1)
                .fileId(generateUniqueId())
                .fileName("task 5 - 15 MB").UiThreadCallback(this)
                .build();

        DownloadTask downloadTask6 = new DownloadTask.Builder("https://sample-videos.com/pdf/Sample-pdf-5mb.pdf")
                .destination(localPath1)
                .fileId(generateUniqueId())
                .fileName("task 6 pdf - 5 MB")
                .UiThreadCallback(this).build();

        DownloadTask downloadTask7 = new DownloadTask.Builder("http://enos.itcollege.ee/~jpoial/allalaadimised/reading/Android-Programming-Cookbook.pdf")
                .destination(localPath1)
                .fileId(generateUniqueId())
                .fileName("task 7 pdf - 8 MB").UiThreadCallback(this).build();


        downloadTasks.add(downloadTask1);
        downloadTasks.add(downloadTask2);
        downloadTasks.add(downloadTask3);
        downloadTasks.add(downloadTask4);
        downloadTasks.add(downloadTask5);
        downloadTasks.add(downloadTask6);
        downloadTasks.add(downloadTask7);

        for (DownloadTask downloadTask : downloadTasks) {
            downloadTask.setDestination(new File(path, downloadTask.getFileName() + "_" + generateUniqueId() + "." + Util.getFileExtension(downloadTask.getUrl())).getPath());
        }
    }

    private int generateUniqueId() {
        return new Random().nextInt();
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
