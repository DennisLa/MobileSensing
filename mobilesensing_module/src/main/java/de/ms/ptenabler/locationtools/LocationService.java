package de.ms.ptenabler.locationtools;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Date;

import de.ms.ptenabler.Message.ClusterMessage;
import de.ms.ptenabler.Message.LocationMessage;
import de.ms.ptenabler.util.Utilities;

public class LocationService extends Service implements 
								GoogleApiClient.ConnectionCallbacks,
								GoogleApiClient.OnConnectionFailedListener,
								LocationListener {

	private GoogleApiClient mGoogleApiClient;
    private LocationRequest forceUpdateRequest = null;
    private final String TAG = "PTEnabler";
    private LocationRequest defaulLocRequest=null; 
    private LocationRequest current = null;
    private Handler handler = new Handler();
    private Runnable resetIntervalFallback = new Runnable() {

        public void run() {
            setNormalInterval();
          }
    };
	public final String LAST_CLUSTERED = "LAST_CLUSTERED";
	private SharedPreferences prefs;
	private SharedPreferences.Editor editor;
    @Override
    public void onCreate() {
        super.onCreate();
        connectService();
        EventBus.getDefault().register(this);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        //TODO
		//Utilities.openDBConnection();
		if(mGoogleApiClient!=null && (mGoogleApiClient.isConnected() || mGoogleApiClient.isConnecting())){
            Log.d(TAG,"Service already running...");
        }else{
            connectService();
        }
		return START_STICKY;

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
        Log.d(TAG, "LocationService destroyed");
		mGoogleApiClient.disconnect();
		//TODO
		//Utilities.closeDBConnection();
        }


	public void onConnected(Bundle bundle) {
		prefs= PreferenceManager.getDefaultSharedPreferences(this);
		Log.i(TAG, "GoogleApiClient connection has been connected");
		if(mGoogleApiClient.isConnected()){
            forceLocationUpdateSetting();
			checkIfClusteringDue();
        }
	    PTNMELocationManager.getManager().setService(this);
    }

	
	public void onConnectionSuspended(int i) {
	Log.i(TAG, "GoogleApiClient connection has been suspend");
	PTNMELocationManager.getManager().setService(null);
	}
	
	
	public void onConnectionFailed(ConnectionResult result) {
	Log.i(TAG, "GoogleApiClient connection has failed");
	PTNMELocationManager.getManager().setService(null);
	}
	
	
	
	public void onLocationChanged(Location location) {
        Log.d(TAG, "Received Location: " + location.getLatitude() + "/" + location.getLongitude() + "(Accuracy: " + location.getAccuracy() + " " + location.getProvider() + ")");
        if((location.getLatitude()!=0.0 && location.getLongitude()!=0.0)){
		    EventBus.getDefault().postSticky(new LocationMessage(PTNMELocationManager.getManager().fromLatLng(location), new Date().getTime()));
		}else{
			long ts = prefs.getLong(PTNMELocationManager.LAST_LOCTS, 0l);
			int lat= prefs.getInt(PTNMELocationManager.LAST_LAT, 0);
			int lon= prefs.getInt(PTNMELocationManager.LAST_LON, 0);
			de.schildbach.pte.dto.Location loc = de.schildbach.pte.dto.Location.coord(lat,lon);
			EventBus.getDefault().postSticky(new LocationMessage(loc, ts));
			Log.d("PTEnabler", "Dropped Location due to insufficient accuracy: " + location.getAccuracy());
		}

		if (current == forceUpdateRequest){
		Log.d("PTEnabler", "Resetting Location Request Interval");
			setNormalInterval();
		}
	}

	@Subscribe
    public void saveLocationToHistory(LocationMessage location){

        ArrayList<ClusteredLocation> clocs = (ArrayList<ClusteredLocation>)ClusterManagement.getManager().getClusteredLocationsFromCache(false);
        ClusteredLocation newCluster=null;
        for(ClusteredLocation cl:clocs){
            UserLocation toTest = new UserLocation(location.loc,location.timestamp,cl.getId());
            if(ClusterManagement.getManager().coversLocation(cl.getMeta().getHull(),toTest)){
                newCluster=cl;
                break;
            }
        }
        ClusterMessage cm= EventBus.getDefault().getStickyEvent(ClusterMessage.class);
        if(newCluster !=null){
            UserLocation toTest = new UserLocation(location.loc,location.timestamp,newCluster.getId());
            if(cm!=null && cm.cloc.getId() == newCluster.getId()){
                EventBus.getDefault().postSticky(new ClusterMessage(newCluster, ClusterMessage.STATE.INSIDE,toTest));
            }else{
				EventBus.getDefault().postSticky(new ClusterMessage(newCluster, ClusterMessage.STATE.ENTERING,toTest));
            }
        }else{
            if(cm!=null){
                UserLocation toTest = new UserLocation(location.loc,location.timestamp,cm.cloc.getId());
                EventBus.getDefault().postSticky(new ClusterMessage(cm.cloc, ClusterMessage.STATE.LEAVING,toTest));
                EventBus.getDefault().removeStickyEvent(ClusterMessage.class);
            }
        }
		de.schildbach.pte.dto.Location loc = location.loc;

		long now = new Date().getTime();
		if(now-prefs.getLong(PTNMELocationManager.LAST_LOCTS, (long) 0)>1000l*30l){
			Log.d("PTEnabler","New Location Update accepted!" );
			//TODO
			//Utilities.openDBConnection().save2LocationHistory(loc);
			editor = prefs.edit();
			editor.putLong(PTNMELocationManager.LAST_LOCTS, now);
			editor.putInt(PTNMELocationManager.LAST_LAT, loc.lat);
			editor.putInt(PTNMELocationManager.LAST_LON, loc.lon);
			editor.commit();
			checkIfClusteringDue();
		}else{
			//Log.d("PTEnabler","Already received location update! Dropping Location..." );
		}



	}

    @Subscribe
    public void onClusterMessage(ClusterMessage message){
       // Toast.makeText(this, ""+ message.state.name()+" " + message.cloc.getLoc().place, Toast.LENGTH_SHORT).show();
    }

	
	public void forceLocationUpdateSetting (){
		Log.d("PTEnabler", "Forcing Location Request");
		forceUpdateRequest = LocationRequest.create();
		forceUpdateRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		forceUpdateRequest.setFastestInterval(5000l);
		forceUpdateRequest.setInterval(1000l);
	    //TODO
		/*LocationServices.FusedLocationApi.requestLocationUpdates(
		        mGoogleApiClient, forceUpdateRequest, this);*/
	    current = forceUpdateRequest;
        handler.postDelayed(resetIntervalFallback, 10000l);

	}
	public void setNormalInterval(){

		defaulLocRequest = LocationRequest.create();
	    defaulLocRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
	    defaulLocRequest.setFastestInterval(1000l);
	    defaulLocRequest.setInterval(1000l * 300l);
	    //TODO
		/*LocationServices.FusedLocationApi.requestLocationUpdates(
		        mGoogleApiClient, defaulLocRequest, this);*/
	    current = defaulLocRequest;

	}
	
	
	
	public IBinder onBind(Intent intent) {
		
		return null;
	}

	private void connectService(){
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
        Log.d(TAG, "LocationService starting....");

    }
	public void checkIfClusteringDue(){
		Date now = new Date();
		Date last = new Date(prefs.getLong(LAST_CLUSTERED, (long) 0));
//		if(now.getTime()-last.getTime()>1000l*24l*3600l && !Utilities.isMyServiceRunning(ClusterService.class)){
		if(now.getTime()-last.getTime()>1000l * 60l && !Utilities.isMyServiceRunning(ClusterService.class)){
			Log.d("PTEnabler","Clustering Locations from" +last.toLocaleString() +" until " + now.toLocaleString());
			Intent msgIntent = new Intent(this, ClusterService.class);
			this.startService(msgIntent);

		}else{
			Log.d("PTEnabler","Already Clustered tody...Skipping!" );
		}
	}

}
