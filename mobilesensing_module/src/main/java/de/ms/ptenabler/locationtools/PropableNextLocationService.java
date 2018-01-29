package de.ms.ptenabler.locationtools;

import android.app.IntentService;
import android.content.Intent;


import org.greenrobot.eventbus.EventBus;

import java.util.Date;

import de.ms.ptenabler.locationtools.ClusterManagement;
import de.ms.ptenabler.locationtools.ClusteredLocation;
import de.ms.ptenabler.locationtools.trajectory.PNLMessage;

/**
 * Created by Martin on 08.06.2016.
 */
public class PropableNextLocationService extends IntentService {

    public static final String CID= "cid";
    public static final String timeInFuture= "TiF";
    public static final String minTransitionProbability= "MTB";

    private double minTransitionProb;
    private long timeInFutureInMin;
    private long clusterID;

    public PropableNextLocationService() {
        super("PNLService");
    }

    protected void onHandleIntent(Intent intent) {

        clusterID=  intent.getLongExtra(CID,0);
        timeInFutureInMin = intent.getLongExtra(timeInFuture, 15l);
        minTransitionProb = intent.getDoubleExtra(minTransitionProbability, 0.0);
        if(clusterID == 0){
            return;
        }
        ClusteredLocation cl = ClusterManagement.getManager().getClusteredLocation(clusterID);
        if(cl==null){
            return;
        }
        EventBus.getDefault().postSticky(new PNLMessage(new Date().getTime(),cl.getProbableNextLocations(minTransitionProb,timeInFutureInMin),cl));

        return;
    }


}
