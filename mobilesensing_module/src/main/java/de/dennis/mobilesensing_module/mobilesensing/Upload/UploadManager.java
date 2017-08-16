package de.dennis.mobilesensing_module.mobilesensing.Upload;

import android.content.Context;
import android.content.SharedPreferences;

import de.dennis.mobilesensing_module.mobilesensing.Module;
import de.dennis.mobilesensing_module.mobilesensing.Upload.DailyUpload.UploadService;

/**
 * Created by Dennis on 16.08.2017.
 */

public class UploadManager {
    public UploadManager(){

    }
    public void setLiveUpload(){

    }
    public void setDailyUpload(Context context, String session, String url){
        //BaasUser.current().getToken(),"http://141.99.12.45:3000/sendData"
        SharedPreferences prefs = Module.getContext().getSharedPreferences("Settings",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("UploadUrl",url);
        editor.putString("UploadSession",session);
        editor.apply();
        UploadService us = new UploadService();
        us.startUploadService(context, 3600000);
    }
}
