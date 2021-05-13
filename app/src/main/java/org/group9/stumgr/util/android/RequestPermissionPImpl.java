package org.group9.stumgr.util.android;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

//安卓p授权实现类
public class RequestPermissionPImpl implements RequestPermissionInterface{
    @TargetApi(9)
    @Override
    public boolean RequestPermission(Context context, Activity stuactivity) {
        boolean isHasStoragePermission = (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        if (!isHasStoragePermission) {
            ActivityCompat.requestPermissions(stuactivity,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }
        return (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }


}
