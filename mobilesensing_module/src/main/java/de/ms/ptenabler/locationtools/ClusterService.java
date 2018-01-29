package de.ms.ptenabler.locationtools;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

import net.sf.javaml.core.Dataset;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Log;

import de.dennis.mobilesensing_module.mobilesensing.Module;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.Cluster.ClusterObject;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBoxAdapter;
import de.schildbach.pte.dto.Location;

public class ClusterService extends IntentService {

	public final static String LAST_CLUSTERED = "LAST_CLUSTERED";
	private double mintransitionprob = 0.05;
    private int timeinFuture= 30;
    private SharedPreferences prefs;
	private SharedPreferences.Editor editor;
	public ClusterService() {
		super("TMClusteringService");
	}

	
	protected void onHandleIntent(Intent intent) {
		//prefs= PreferenceManager.getDefaultSharedPreferences(this);
        prefs = Module.getContext().getSharedPreferences("Clustering",MODE_PRIVATE);
		//Utilities.openDBConnection().clearLocationHistory(14);
		Date start = new Date(prefs.getLong(LAST_CLUSTERED,new Date().getTime()-(1000l*3600l*24l)));
        long now = new Date().getTime();
        Map<Location, Dataset> clusteredLocs = ClusterManagement.getManager().clusterLocations(start, new Date(), 2000);
        //Map<Location, Dataset> clusteredLocs = Utilities.clusterLocations(new Date(0) , new Date(), 10000);
			Log.d("PTEnablerClustering","Done with yesterday! "+ clusteredLocs.size()+ "Locations calculated" );
            ClusterManagement.getManager().addNewClusteredLocations(clusteredLocs, ClusterMetaData.ClusterType.DAILY_CLUSTER);
			if(prefs.getBoolean("INCLUDE_NOISE", false)){
                Log.d("PTEnablerClustering","Checking remaining noise! ");
                clusteredLocs.clear();
                clusteredLocs.putAll(ClusterManagement.getManager().clusterLocations(new Date(0), new Date(), 2000, true));
                ClusterManagement.getManager().addNewClusteredLocations(clusteredLocs, ClusterMetaData.ClusterType.NOISE_CLUSTER);
            }
			Log.d("PTEnablerClustering","Done! "+ clusteredLocs.size()+ " Locations calculated in total" );

			ClusterManagement.getManager().clearClusters();
            ClusterManagement.getManager().invalidateTrajectorySums();
			List<ClusteredLocation> clocs = ClusterManagement.getManager().getClusteredLocationsFromCache(true);
            clocs = ClusterManagement.getManager().cleanCluster(clocs);
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		    	for (ClusteredLocation caller:clocs){
                    if(activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                        if (caller.getLoc().name == null || caller.getLoc().name.equals("NOT SET")) {

                            Log.d("PTEnablerClustering", "Reverse Geocoding Cluster " + caller.getId());
                            double shortenX = (double) caller.getLoc().lat / 1000000;
                            double shortenY = (double) caller.getLoc().lon / 1000000;
                            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                            String place = "";
                            try {
                                List<Address> res = geocoder.getFromLocation(shortenX, shortenY, 1);
                                if (res != null && res.size() > 0) {
                                    Address address = res.get(0);

                                    for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                                        place += address.getAddressLine(i);
                                        if (i != address.getMaxAddressLineIndex() - 1)
                                            place += ", ";
                                    }
                                    Log.d("PTEnablerClustering", "Address: " + place);

                                }
                            } catch (IOException e) {
                                place = "NOT SET";
                                e.printStackTrace();
                            }
                            Location loc = new Location(caller.getLoc().type, caller.getLoc().id, caller.getLoc().lat, caller.getLoc().lon, place, place);
                            caller.setLoc(loc);
                        }
                    }

					Vector<UserLocation> hull = new Vector<UserLocation>();
					hull.addAll(ClusterManagement.getManager().getConvexHullofCluster(caller.getId()));
					Vector<long[]> forPersistence = new Vector<long[]>();
                    for(UserLocation uloc:hull){
                        forPersistence.add(new long[]{uloc.getLoc().lat,uloc.getLoc().lon,uloc.getDate(),uloc.getParentCluster()});
                    }
                    caller.getMeta().hull = forPersistence;
			    						
			    }

                ClusterManagement.getManager().addNewClusteredLocations(clocs);
                Log.d("PTEnabler", "Update Clustered Locations. Analyzing Presence Probability");
                ClusterManagement.getManager().updateTimeDistribution();
                ObjectBoxAdapter oba = new ObjectBoxAdapter();
                List<ClusterObject> cl = oba.getAllClusterObjects();
                Log.d("PTEnabler", "Analyzed Presence Probability");
                ClusterManagement.getManager().createTrajectorySums();
                //Logger.saveClusterLog(start.getTime());
                Log.d("PTEnabler", "Completly Finished");

    }


}
