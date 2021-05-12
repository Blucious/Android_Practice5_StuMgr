package org.group9.stumgr.service;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;

import androidx.annotation.RequiresApi;

public class RequestPermissionRImpl implements RequestPermissionInterface{
    // Andriod R 授权实现类
    @RequiresApi(api = Build.VERSION_CODES.R)
    @TargetApi(11)
    public boolean RequestPermission(Context context, Activity activity){
        boolean isHasStoragePermission= Environment.isExternalStorageManager();
        if (!isHasStoragePermission){
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
            context.startActivity(intent);
    }
        return Environment.isExternalStorageManager();
}
}
