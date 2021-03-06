package com.ramadan;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.core.DownloadManager;
import com.core.DownloadTask;

import java.util.List;


public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TaskViewHolder> {
   private  List<DownloadTask> downloadTasks;
   private  Context context;

    public TasksAdapter(Context context) {
        this.context = context;
    }

    TasksAdapter(Context context, List<DownloadTask> tasks) {
        this.context = context;
        this.downloadTasks = tasks;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.task_item_view, parent, false);
        return new TaskViewHolder(itemView);
    }

    private DownloadTask getTask(int position) {
        return downloadTasks.get(position);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        DownloadTask task = getTask(position);
        holder.fileNameTextView.setText(task.getFileName());
        if(task.getProgress() != -1){holder.seekBar.setText("downloading...");}else{holder.seekBar.setText("");}
        holder.seekBar.setVisibility(task.getProgress() == -1 ? View.GONE : View.VISIBLE);
        holder.downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.seekBar.setVisibility(View.VISIBLE);
                if(task.getProgress() == -1) {
                    DownloadManager.getInstance().downloadFile(task);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return downloadTasks.size();
    }

    public List<DownloadTask> getDownloadTasks() {
        return downloadTasks;
    }

    class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView seekBar;
        TextView fileNameTextView;
        Button downloadButton;

        TaskViewHolder(View taskItemView) {
            super(taskItemView);
            seekBar = taskItemView.findViewById(R.id.seek_bar);
            fileNameTextView = taskItemView.findViewById(R.id.tv_filename);
            downloadButton = taskItemView.findViewById(R.id.download_button);
        }
    }
}
