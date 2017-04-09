package de.dennis.mobilesensing.TrackService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.dennis.mobilesensing.Application;
import de.dennis.mobilesensing.storage.StorageHelper;
import de.dennis.mobilesensing.storage.Wrapper.wActivity;
import de.dennis.mobilesensing.storage.Wrapper.wLocation;
import de.dennis.mobilesensing.storage.Wrapper.wTrack;

/**
 * Created by Dennis on 21.03.2017.
 */
public class TrackListener extends BroadcastReceiver {
    private String TAG = TrackListener.class.getName();
    ArrayList <wTrack> tracks;

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences prefs = Application.getContext().getSharedPreferences("Settings",Context.MODE_PRIVATE);
        if(prefs.getLong("lastTrackServiceExecution",0L)-(new Date()).getTime() >= 24 * 3600000){ //IF last Execution is more than 24h ago
            tracks = getTracks();
            for(wTrack track: tracks)
            {
                StorageHelper.openDBConnection().save2TrackHistory(track);
            }
            SharedPreferences.Editor editor = prefs.edit();
            editor.putLong("lastTrackServiceExecution",(new Date()).getTime());
            editor.apply();
        }

    }
    private static ArrayList<wTrack> getTracks()
    {
        long offset = (new java.util.Date()).getTimezoneOffset()*60000;
        long time = (new Date()).getTime();
        long endOfDay = (time / 86400000) * 86400000 +offset;
        long startOfDay = endOfDay -86400000;

        List<wLocation> locList =  StorageHelper.openDBConnection().getAllHistoryLocs(startOfDay-1, endOfDay+1 , false);
        ArrayList <wTrack> paths = new ArrayList<>();
        if(locList.size() > 1)
        {
            for(int i = 0; i < locList.size()-1;i++) {
                double kilometer = calculateKilometer(locList.get(i).getLatitude(), locList.get(i).getLongitude(), locList.get(i + 1).getLatitude(), locList.get(i + 1).getLongitude());//calculateKilometer(locList.get(i).getDate()-1,locList.get(i+1).getDate()+1);
                if(kilometer >=0.05)
                {
                    wTrack myPath = new wTrack(locList.get(i).getTimestamp(),locList.get(i+1).getTimestamp(),"INCAR",kilometer);
                    paths.add(myPath);
                }
            }
        }
        for(wTrack myPath: paths)
        {
            List<wActivity> myActis = StorageHelper.openDBConnection().getAllActivities(myPath.getStartTime(), myPath.getEndTime(), false);
            if(myActis.size()>0)
            {
                int car = 0;
                int on_foot = 0;
                int bike = 0;
                for(wActivity myActivtiy:myActis)
                {
                    switch (myActivtiy.getMode()) {
                        case "INCAR":
                            car++;
                            break;
                        case "BIKING":
                            bike++;
                            break;
                        case "WALKING":
                            on_foot++;
                            break;
                        case "RUNNING":
                            on_foot++;
                            break;
                        default:
                            car++;
                            break;
                    }
                }
                if(car > on_foot && car > bike)
                {
                    myPath.setMode("INCAR");
                }else if(bike > car && bike > on_foot)
                {
                    myPath.setMode("BIKING");
                }else if(on_foot > car && on_foot> bike)
                {
                    myPath.setMode("WALKING");
                }
            }else{ myPath.setMode("None");}
        }
        ArrayList <wTrack> mergedPaths = new ArrayList<>();
        if(paths.size()>1)
        {
            mergedPaths.add(paths.get(0));
            for(int i = 1; i < paths.size();i++) {
                long startTime= paths.get(i).getStartTime();
                String mode =paths.get(i).getMode();
                if(mergedPaths.get(mergedPaths.size()-1).getEndTime() == startTime && (mergedPaths.get(mergedPaths.size()-1).getMode().equals(mode) || mode.equals("None")))
                {
                    mergedPaths.get(mergedPaths.size()-1).setEndTime(paths.get(i).getEndTime());
                    mergedPaths.get(mergedPaths.size()-1).setKilometers(calculateKilometer(mergedPaths.get(mergedPaths.size()-1).getStartTime()-1,mergedPaths.get(mergedPaths.size()-1).getEndTime()+1));
                }else{
                    if(mode.equals("None"))
                    {
                        mode = "INCAR";
                    }
                    mergedPaths.add(new wTrack(paths.get(i).getStartTime(),paths.get(i).getEndTime(),mode,paths.get(i).getKilometers()));
                }
            }
        }else mergedPaths = paths;
        return mergedPaths;
    }
    public static double calculateKilometer(double lat1, double lon1,double lat2, double lon2)
    {
        double earthRadius = 6367.4445; //kilometer
        int i = 0;
        double distance = 0;
        double bowLat1= lat1 * Math.PI/180;
        double bowLat2= lat2 * Math.PI/180;
        double bowlon1= lon1 * Math.PI/180;
        double bowlon2 = lon2 * Math.PI/180;

        Log.d("Bogenmaß", bowLat1 + "," + bowLat2 + "," + bowlon1 + "," + bowlon2);

        double dist = Math.acos(Math.sin(bowLat1)*Math.sin(bowLat2)+Math.cos(bowLat1)*Math.cos(bowLat2)*Math.cos(bowlon2-bowlon1))*earthRadius;
        //double dist = Math.toDegrees(Math.acos(Math.toDegrees(Math.sin(bowLat1))*Math.toDegrees(Math.sin(bowLat2))+Math.toDegrees(Math.cos(bowLat1))*Math.toDegrees(Math.cos(bowLat2))*Math.toDegrees(Math.cos(bowlon2-bowlon1))))*earthRadius;
        if(bowLat1 == bowLat2 && bowlon1 == bowlon2)
        {
            dist = 0 ;
        }
        Log.d("AddDist","OldDistance: "+distance+";"+"ToAddDistance: "+dist);
        distance = distance +  Math.abs(dist);
        distance = Math.floor(distance*100)/100.0;
        Log.d("AddDist", "DefDistance: " + distance);
        return distance;
    }
    public static double calculateKilometer(long startTime, long endTime)
    {
        double earthRadius = 6367.4445; //kilometer
        int i = 0;
        double distance = 0;
        List<wLocation> locList =  StorageHelper.openDBConnection().getAllHistoryLocs(startTime-1, endTime+1, true);
        for(wLocation loc: locList)
        {   if(locList.size()> i+1) {
            wLocation loc2 = locList.get(i + 1);
            double bowLat1= loc.getLatitude() * Math.PI/180;
            double bowLat2= loc2.getLatitude() * Math.PI/180;
            double bowlon1= loc.getLongitude() * Math.PI/180;
            double bowlon2 = loc2.getLongitude() * Math.PI/180;

            Log.d("Bogenmaß", bowLat1 +","+ bowLat2+","+ bowlon1 +","+bowlon2);

            double dist = Math.acos(Math.sin(bowLat1)*Math.sin(bowLat2)+Math.cos(bowLat1)*Math.cos(bowLat2)*Math.cos(bowlon2-bowlon1))*earthRadius;
            //double dist = Math.toDegrees(Math.acos(Math.toDegrees(Math.sin(bowLat1))*Math.toDegrees(Math.sin(bowLat2))+Math.toDegrees(Math.cos(bowLat1))*Math.toDegrees(Math.cos(bowLat2))*Math.toDegrees(Math.cos(bowlon2-bowlon1))))*earthRadius;
            Log.d("CalcDistance",startTime +" "+ endTime+ " " +dist);
            i++;
            if(bowLat1 == bowLat2 && bowlon1 == bowlon2)
            {
                dist = 0 ;
            }
            Log.d("AddDist","OldDistance: "+distance+";"+"ToAddDistance: "+dist);
            distance = distance +  Math.abs(dist);

        }
        }

        distance = Math.floor(distance*100)/100.0;
        Log.d("AddDist", "DefDistance: " + distance);
        return distance;
    }
}
