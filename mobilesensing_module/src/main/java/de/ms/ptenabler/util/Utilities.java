package de.ms.ptenabler.util;



import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Display;
import android.widget.ImageView;
import android.widget.Toast;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import de.ms.ptenabler.locationtools.LocationService;
import android.app.ActivityManager.RunningServiceInfo;
import de.ms.ptenabler.locationtools.PTNMELocationManager;
import de.ms.ptenabler.poi.MobilePractice;
import de.schildbach.pte.dto.Line;
import de.schildbach.pte.dto.Location;
import de.schildbach.pte.dto.Stop;
import de.schildbach.pte.dto.Trip;
import de.schildbach.pte.dto.Trip.Leg;
import de.schildbach.pte.dto.Trip.Public;

public class Utilities{
    public static final int FONT_LIGHT =0;
    public static final int FONT_REGULAR =1;
    public static final int FONT_BOLD =2;
    public static final int FONT_ICONS =3;
    private static Context context;
	public static Typeface light ;
    public static Typeface bold ;
    public static Typeface regular ;
    public static Typeface icons ;
    public static int PlayServiceReqCode = 252;
    private static SharedPreferences prefs;

    private static Vector<MobilePractice> mActivities;
	public static String formatTime(int minutes){
		String minuteLbl="";
		if(minutes<10){
			minuteLbl="0"+minutes;
		}else{
			minuteLbl=""+minutes;
		}
		return minuteLbl;
	}
	public static int convertToPx(Context con, int dp) {
	    final float scale = con.getResources().getDisplayMetrics().density;
	    return (int) (dp * scale + 0.5f);
	}
	public static JSONObject getJSONObjectFromUrl(String url) {
    	String json=null;
		InputStream is = null;
        JSONObject jObj = null;
        json = getDataFromUrl(url);
        // try parse the string to a JSON object
        if(json  !=null){
        	try {
                jObj = new JSONObject(json);
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());
            }
     
            // return JSON String
            return jObj;
     	
        }else{
        	return null;
        }
        
    }
	public static int[] getDisplaySize(Activity context){
		Display display = context.getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		int height = size.y;
		return new int[]{width,height};
	}
	public static Context getContext() {
		return context;
	}
	public static void setContext(Context context) {

        Utilities.context = context;
        prefs= PreferenceManager.getDefaultSharedPreferences(Utilities.context);
    }

    public static Typeface getTypeFace(int type){

        switch(type){
        case FONT_BOLD: return bold;
        case FONT_ICONS: return icons;
        case FONT_LIGHT: return light;
        case FONT_REGULAR: return regular;
        default: return null;
        }

    }

    public static void showMessage(String text){
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

    public static void checkUpdateStatus(){
        try {
            PackageInfo pInfo = null;
            pInfo = getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 0);
            String version = ""+pInfo.versionCode;
            /*
            ParseConfig config = ParseConfig.getCurrentConfig();
            String latestVersion = config.getString("versionCode");
            if(Integer.parseInt(version)<Integer.parseInt(latestVersion)){
                showUpdateDialog();
            }
            */
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NumberFormatException e){
            e.printStackTrace();
        }
        //GoogleApiAvailability.getInstance().showErrorDialogFragment(context,PTNMELocationManager.getManager().LocationServiceStatusCode,PlayServiceReqCode);
    }
/*
    private static void showUpdateDialog(){
        Crouton.makeText(context), getParentActivity().getString(R.string.updateNotification), Style.ALERT).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final String appPackageName = getContext().getPackageName(); // getPackageName() from Context or Activity object
                try {
                    getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        })
           .show();
    }
*/
    public static Vector<MobilePractice> getActivities() {
		long timeStamp = new Date().getTime();
		while(mActivities==null){
			if(new Date().getTime()-timeStamp>5000)return null;
		}
		return mActivities;
	}
	public static String printTripLog(Trip x){
		String res = "";
		for(Leg y :x.legs){
			
			if(y instanceof Public){
				Public pub = ((Public)y);
				res+="\n"+ printTime(y.getDepartureTime())+"| "+pub.line.label+ " | " + y.departure.name;
				List<Stop> inters = pub.intermediateStops;
				if(inters !=null){
					for(Stop intermediate : inters){
						res+="\n\t-"+ printTime(intermediate.plannedArrivalTime)+" "+ intermediate.location.name;
					}	
				}
				
			}else{
				res+="\n"+ printTime(y.getDepartureTime())+"| "+ y.arrival.name;
			}
		}
		return res;
	}
	public static String printTime(Date date){
		return ""+formatTime(date.getHours())+ ":" + formatTime(date.getMinutes());
	}
    public static String getDataFromUrl(String url2get) {
        String res = "";
        InputStream is = null;

        // Making HTTP request
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(url2get);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            in.close();
            res = sb.toString();
            return res;
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
            return null;
        }
        finally {
           if(urlConnection!=null) urlConnection.disconnect();
        }
    }
    public static boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    public static SharedPreferences getPeference(){
        return prefs;
    }

    public static SharedPreferences.Editor getEditor(){
        return prefs.edit();
    }


}

