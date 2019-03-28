package com.ramadan;

import android.os.Environment;

import java.io.File;

public class Utils {
    public static boolean deleteFiles(String path) {
        File dir = new File(path);
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                new File(dir, children[i]).delete();
            }
        }
        return true;
    }
}
