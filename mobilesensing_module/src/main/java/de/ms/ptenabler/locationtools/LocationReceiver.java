package de.ms.ptenabler.locationtools;

import java.util.Date;

import de.ms.ptenabler.util.Utilities;
import de.schildbach.pte.dto.Location;
import de.schildbach.pte.dto.LocationType;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class LocationReceiver extends BroadcastReceiver {
	public final String LAST_CLUSTERED = "LAST_CLUSTERED";
	public final static String LAST_LAT = "LAST_LAT";
	public final static String LAST_LON = "LAST_LON";
	private SharedPreferences prefs;
	private SharedPreferences.Editor editor;
	public static String LAST_POISITION_UPDATE = "lastposupd";
	public void onReceive(Context context, Intent intent) {

		Log.d("PTEnabler", "onReceive: received location update");
        android.location.Location arg0 = intent.getParcelableExtra("LOCATION");
        Location loc = Location.coord((int)(arg0.getLatitude()*1000000), (int)(arg0.getLongitude()*1000000));

        prefs= PreferenceManager.getDefaultSharedPreferences(context);
		long now = new Date().getTime();
		if(now-prefs.getLong(LAST_POISITION_UPDATE, (long) 0)>1000l*30l){
			Log.d("PTEnabler","New Location Update accepted!" );
			//TODO
			//Utilities.openDBConnection().save2LocationHistory(loc);
			editor = prefs.edit();
			editor.putLong(LAST_POISITION_UPDATE, now);
			editor.putInt(LocationReceiver.LAST_LAT, loc.lat);
			editor.putInt(LocationReceiver.LAST_LON, loc.lon);			
			editor.commit();
			//TODO
			//Log.d("PTEnabler", "Location saved! New Count: "+ Utilities.openDBConnection().getAllHistoryLocs(0, new Date().getTime()).size());
            /*
			if(now-prefs.getLong(LAST_CLUSTERED, (long) 0)>1000l*24l*3600l){
				Log.d("PTEnabler","Daily Clustering starting..." );
				Intent msgIntent = new Intent(context, ClusterService.class);
		        context.startService(msgIntent);
			}
			*/
		}else{
			Log.d("PTEnabler","Already received location update! Dropping Location..." );
		}
        
		

		
	}
		
	
	
	
		

}
