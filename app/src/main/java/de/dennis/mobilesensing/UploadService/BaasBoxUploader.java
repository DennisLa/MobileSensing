package de.dennis.mobilesensing.UploadService;

import android.util.Log;

import com.baasbox.android.BaasDocument;
import com.baasbox.android.BaasHandler;
import com.baasbox.android.BaasResult;

import java.util.List;

import de.dennis.mobilesensing_module.mobilesensing.Sensors.OtherSensors.RunningApplicationService.RunningApplication;
import de.dennis.mobilesensing_module.mobilesensing.Sensors.OtherSensors.ScreenOnService.ScreenOn;

/**
 * Created by Dennis on 07.04.2017.
 */
public class BaasBoxUploader {
   private static boolean isUploaded;
    /*
    public static long startUpload(final long from, long to)
    {
        isUploaded = true;
        //Upload Activities
        List<wActivity> activities = StorageHelper.openDBConnection().getAllActivities(from, to, false);
        for(wActivity activity: activities){
            final wActivity act = activity;
            BaasDocument doc = new BaasDocument("mobileSensing_Activity");
            doc.put("mode",activity.getMode())
                    .put("timestamp", activity.getTimestamp())
                    .put("probability", activity.getProbability());
            doc.save(new BaasHandler<BaasDocument>() {
                @Override
                public void handle(BaasResult<BaasDocument> res) {
                    if(res.isSuccess()) {
                        Log.d("LOG", "Saved: " + res.value());
                        StorageHelper.openDBConnection().deleteActivity(act.getTimestamp());
                    } else {
                        isUploaded = false;
                    }
                }
            });
        }
        //Upload Call
        List<wCall> calls = StorageHelper.openDBConnection().getAllCalls(from, to, false);
        for(wCall call: calls){
            final wCall fcall = call;
            BaasDocument doc = new BaasDocument("mobileSensing_Call");
            doc.put("timestamp", call.getTimestamp())
                    .put("notificationtype", call.getNotificationType());
            doc.save(new BaasHandler<BaasDocument>() {
                @Override
                public void handle(BaasResult<BaasDocument> res) {
                    if(res.isSuccess()) {
                        Log.d("LOG", "Saved: " + res.value());
                        StorageHelper.openDBConnection().deleteCall(fcall.getTimestamp());
                    } else {
                        isUploaded = false;
                    }
                }
            });
        }
        //Upload ScreenOn
        List<ScreenOn> scrOns = StorageHelper.openDBConnection().getAllScreenOn(from, to, false);
        for(ScreenOn scrOn: scrOns){
            final ScreenOn fScrOn = scrOn;
            BaasDocument doc = new BaasDocument("mobileSensing_ScreenOn");
            doc.put("timestamp", scrOn.getTimestamp())
                    .put("isScreenOn", scrOn.isScreenOn());
            doc.save(new BaasHandler<BaasDocument>() {
                @Override
                public void handle(BaasResult<BaasDocument> res) {
                    if(res.isSuccess()) {
                        Log.d("LOG", "Saved: " + res.value());
                        StorageHelper.openDBConnection().deleteScreenOn(fScrOn.getTimestamp());
                    } else {
                        isUploaded = false;
                    }
                }
            });
        }
        //Upload Location
        List<wLocation> locations = StorageHelper.openDBConnection().getAllHistoryLocs(from, to, false);
        for(wLocation location: locations){
            final wLocation loc = location;
            BaasDocument doc = new BaasDocument("mobileSensing_Location");
            doc.put("timestamp", location.getTimestamp())
                    .put("latitude",location.getLatitude())
                    .put("longitude",location.getLongitude())
                    .put("velocity",location.getVelocity())
                    .put("altitude", location.getAltitude())
                    .put("accuracy", location.getAccuracy());
            doc.save(new BaasHandler<BaasDocument>() {
                @Override
                public void handle(BaasResult<BaasDocument> res) {
                    if(res.isSuccess()) {
                        Log.d("LOG", "Saved: " + res.value());
                        StorageHelper.openDBConnection().deleteLocation(loc.getTimestamp());
                    } else {
                        isUploaded = false;
                    }
                }
            });
        }
        //Upload Network
        List<wNetwork> networks = StorageHelper.openDBConnection().getAllNetworks(from, to, false);
        for(wNetwork network: networks){
            final wNetwork fnetwork = network;
            BaasDocument doc = new BaasDocument("mobileSensing_Network");
            doc.put("timestamp", network.getTimestamp())
                    .put("networktype",network.getNetworkType());
            doc.save(new BaasHandler<BaasDocument>() {
                @Override
                public void handle(BaasResult<BaasDocument> res) {
                    if(res.isSuccess()) {
                        Log.d("LOG", "Saved: " + res.value());
                        StorageHelper.openDBConnection().deleteNetwork(fnetwork.getTimestamp());
                    } else {
                        isUploaded = false;
                    }
                }
            });
        }
        //Upload RunningApplication
        List<RunningApplication> apps = StorageHelper.openDBConnection().getAllRunningApplications(from, to, false);
        for(RunningApplication app: apps){
            final RunningApplication fapp = app;
            BaasDocument doc = new BaasDocument("mobileSensing_RunningApplication");
            doc.put("timestamp", app.getTimestamp())
                    .put("packagename",app.getPackagename());
            doc.save(new BaasHandler<BaasDocument>() {
                @Override
                public void handle(BaasResult<BaasDocument> res) {
                    if(res.isSuccess()) {
                        Log.d("LOG", "Saved: " + res.value());
                        StorageHelper.openDBConnection().deleteRunningApp(fapp.getTimestamp());
                    } else {
                        isUploaded = false;
                    }
                }
            });
        }
        //Upload Track
        List<wTrack> tracks = StorageHelper.openDBConnection().getAllTracks(from, to, false);
        for(wTrack track: tracks){
            final wTrack ftrack = track;
            BaasDocument doc = new BaasDocument("mobileSensing_Track");
            doc.put("timestamp", track.getStartTime())
                    .put("endtimestamp",track.getEndTime())
                    .put("kilometers",track.getKilometers())
                    .put("mode",track.getMode());
            doc.save(new BaasHandler<BaasDocument>() {
                @Override
                public void handle(BaasResult<BaasDocument> res) {
                    if(res.isSuccess()) {
                        Log.d("LOG", "Saved: " + res.value());
                        StorageHelper.openDBConnection().deleteTrack(ftrack.getStartTime());
                    } else{
                        isUploaded = false;
                    }
                }
            });
        }
        if(isUploaded == true)
        {
            return to;
        }else{
            return from;
        }

    }
    */
}
