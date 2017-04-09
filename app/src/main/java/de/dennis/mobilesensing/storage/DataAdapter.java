package de.dennis.mobilesensing.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.intel.context.item.Call;
import com.intel.context.item.DevicePositionItem;
import com.intel.context.item.LocationCurrent;
import com.intel.context.item.ActivityRecognition;
import com.intel.context.item.Network;

import java.util.ArrayList;
import java.util.List;

import de.dennis.mobilesensing.RunningApplicationService.RunningApplication;
import de.dennis.mobilesensing.storage.Wrapper.wActivity;
import de.dennis.mobilesensing.storage.Wrapper.wCall;
import de.dennis.mobilesensing.storage.Wrapper.wDevicePosition;
import de.dennis.mobilesensing.storage.Wrapper.wLocation;
import de.dennis.mobilesensing.storage.Wrapper.wNetwork;
import de.dennis.mobilesensing.storage.Wrapper.wTrack;

/**
 * Created by Dennis on 28.02.2017.
 */
public class DataAdapter {
    // Database fields
    private SQLiteDatabase database;
    private SQLStorage dbHelper;

    private String[] allColumnsLocation = {
            SQLStorage.COLUMN_ID,
            SQLStorage.COLUMN_LAT,
            SQLStorage.COLUMN_LNG,
            SQLStorage.COLUMN_TIMESTAMP,
            SQLStorage.COLUMN_VEL,
            SQLStorage.COLUMN_ALT,
            SQLStorage.COLUMN_ACC};

    private String[] allColumnsActivity = {
            SQLStorage.COLUMN_ID,
            SQLStorage.COLUMN_TIMESTAMP,
            SQLStorage.COLUMN_TYPE,
            SQLStorage.COLUMN_PROBABILITY};

    private String[] allColumnsRunningApplication = {
            SQLStorage.COLUMN_ID,
            SQLStorage.COLUMN_TYPE,
            SQLStorage.COLUMN_TIMESTAMP};

    private String[] allColumnsCall = {
            SQLStorage.COLUMN_ID,
            SQLStorage.COLUMN_TYPE,
            SQLStorage.COLUMN_TIMESTAMP};

    private String[] allColumnsNetwork = {
            SQLStorage.COLUMN_ID,
            SQLStorage.COLUMN_TYPE,
            SQLStorage.COLUMN_TIMESTAMP};
    private String[] allColumnsDevicePosition = {
            SQLStorage.COLUMN_ID,
            SQLStorage.COLUMN_TYPE,
            SQLStorage.COLUMN_TIMESTAMP};
    private String[] allColumnsTrack = {
            SQLStorage.COLUMN_ID,
            SQLStorage.COLUMN_TIMESTAMP,
            SQLStorage.COLUMN_ENDTIMESTAMP,
            SQLStorage.COLUMN_TYPE,
            SQLStorage.COLUMN_KILOMETER};

    public DataAdapter(Context context) {
        dbHelper = new SQLStorage(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void deleteActivity(long timestamp){
        database.delete(SQLStorage.TABLE_ACTIVITY_HISTORY, SQLStorage.COLUMN_TIMESTAMP + " = " + timestamp, null);
    }

    public void deleteLocation(long timestamp){
        database.delete(SQLStorage.TABLE_LOCATION_HISTORY, SQLStorage.COLUMN_TIMESTAMP + " = " + timestamp, null);
    }

    public void deleteCall(long timestamp){
        database.delete(SQLStorage.TABLE_CALL_HISTORY,SQLStorage.COLUMN_TIMESTAMP + " = " + timestamp, null);
    }

    public void deleteDevicePosition(long timestamp){
        database.delete(SQLStorage.TABLE_DEVICE_POSITION_HISTORY, SQLStorage.COLUMN_TIMESTAMP + " = " + timestamp, null);
    }

    public void deleteNetwork(long timestamp){
        database.delete(SQLStorage.TABLE_NETWORK_HISTORY, SQLStorage.COLUMN_TIMESTAMP + " = " + timestamp, null);
    }
    public void deleteRunningApp(long timestamp){
        database.delete(SQLStorage.TABLE_RUNNING_APPLICATION_HISTORY, SQLStorage.COLUMN_TIMESTAMP + " = " + timestamp, null);
    }
    public void deleteTrack(long timestamp){
        database.delete(SQLStorage.TABLE_TRACK_HISTORY, SQLStorage.COLUMN_TIMESTAMP + " = " + timestamp, null);
    }
    public void save2LocHistory(LocationCurrent loc) {
        ContentValues values = new ContentValues();
        values.put(SQLStorage.COLUMN_LAT, loc.getLocation().getLatitude());
        values.put(SQLStorage.COLUMN_LNG, loc.getLocation().getLongitude());
        values.put(SQLStorage.COLUMN_TIMESTAMP, loc.getTimestamp());
        values.put(SQLStorage.COLUMN_VEL, loc.getLocation().getSpeed());
        values.put(SQLStorage.COLUMN_ALT,loc.getLocation().getAltitude());
        values.put(SQLStorage.COLUMN_ACC,loc.getAccuracy());

        database.insert(SQLStorage.TABLE_LOCATION_HISTORY, null,
                values);
    }
    public void save2ActHistory(ActivityRecognition act) {
        ContentValues values = new ContentValues();
        values.put(SQLStorage.COLUMN_TIMESTAMP, act.getTimestamp());
        values.put(SQLStorage.COLUMN_TYPE, act.getMostProbableActivity().getActivity().name());
        values.put(SQLStorage.COLUMN_PROBABILITY, act.getMostProbableActivity().getProbability());

        database.insert(SQLStorage.TABLE_ACTIVITY_HISTORY, null,
                values);
    }
    public void save2RunningAppication(RunningApplication runApp) {
        ContentValues values = new ContentValues();
        values.put(SQLStorage.COLUMN_TIMESTAMP, runApp.getTimestamp());
        values.put(SQLStorage.COLUMN_TYPE, runApp.getPackagename());

        database.insert(SQLStorage.TABLE_RUNNING_APPLICATION_HISTORY, null,
                values);
    }
    public void save2CallHistory(Call call){
        ContentValues values = new ContentValues();
        values.put(SQLStorage.COLUMN_TIMESTAMP, call.getTimestamp());
        values.put(SQLStorage.COLUMN_TYPE, call.getNotificationType().toString());

        database.insert(SQLStorage.TABLE_CALL_HISTORY, null,
                values);
    }
    public void save2NetworkHistory(Network network){
        ContentValues values = new ContentValues();
        values.put(SQLStorage.COLUMN_TIMESTAMP, network.getTimestamp());
        values.put(SQLStorage.COLUMN_TYPE, network.getNetworkType().toString());

        database.insert(SQLStorage.TABLE_NETWORK_HISTORY, null,
                values);
    }
    public void save2DevicePositionHistory(DevicePositionItem devPosItem){
        ContentValues values = new ContentValues();
        values.put(SQLStorage.COLUMN_TIMESTAMP, devPosItem.getTimestamp());
        values.put(SQLStorage.COLUMN_TYPE, devPosItem.getType().toString());

        database.insert(SQLStorage.TABLE_DEVICE_POSITION_HISTORY, null,
                values);
    }
    public void save2TrackHistory(wTrack track){
        ContentValues values = new ContentValues();
        values.put(SQLStorage.COLUMN_TIMESTAMP,track.getStartTime());
        values.put(SQLStorage.COLUMN_ENDTIMESTAMP, track.getEndTime());
        values.put(SQLStorage.COLUMN_TYPE, track.getMode());
        values.put(SQLStorage.COLUMN_KILOMETER,track.getKilometers());

        database.insert(SQLStorage.TABLE_TRACK_HISTORY, null,
                values);
    }
    public List<wLocation> getAllHistoryLocs(long since, long until, boolean descending) {
        List<wLocation> locs = new ArrayList<wLocation>();
        String order = "DESC";
        if(!descending){
            order = "ASC";
        }

        Cursor cursor = database.query(SQLStorage.TABLE_LOCATION_HISTORY,
                allColumnsLocation, SQLStorage.COLUMN_TIMESTAMP+" > "+since + " and " +SQLStorage.COLUMN_TIMESTAMP+ " < "+until, null, null, null, SQLStorage.COLUMN_TIMESTAMP+" "+order);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            wLocation loc = cursorToLocation(cursor);
            locs.add(loc);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return locs;
    }
    private wLocation cursorToLocation(Cursor cursor) {
            /*SQLStorage.COLUMN_ID = 0
              SQLStorage.COLUMN_LAT = 1
              SQLStorage.COLUMN_LNG = 2
              SQLStorage.COLUMN_TIMESTAMP = 3
              SQLStorage.COLUMN_VEL = 4
              SQLStorage.COLUMN_ALT = 5
              SQLStorage.COLUMN_ACC = 6 */
        wLocation loc = new wLocation(cursor.getInt(3),cursor.getDouble(1),cursor.getDouble(2),cursor.getDouble(4),cursor.getDouble(5),cursor.getDouble(6));
        return loc;
    }
    public List<wActivity> getAllActivities(long since, long until, boolean descending) {
        List<wActivity> acts = new ArrayList<wActivity>();
        String order = "DESC";
        if(!descending){
            order = "ASC";
        }

        Cursor cursor = database.query(SQLStorage.TABLE_ACTIVITY_HISTORY,
                allColumnsActivity, SQLStorage.COLUMN_TIMESTAMP+" > "+since + " and " +SQLStorage.COLUMN_TIMESTAMP+ " < "+until, null, null, null, SQLStorage.COLUMN_TIMESTAMP+" "+order);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            wActivity act = cursorToActivity(cursor);
            acts.add(act);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return acts;
    }
    private wActivity cursorToActivity(Cursor cursor) {
            /*SQLStorage.COLUMN_ID = 0
            SQLStorage.COLUMN_TIMESTAMP = 1
            SQLStorage.COLUMN_TYPE = 2
            SQLStorage.COLUMN_PROBABILITY = 3*/
        long timestamp =cursor.getLong(1);
        String mode = cursor.getString(2);
        int probability = cursor.getInt(3);
        wActivity act = new wActivity(timestamp,mode,probability);
        return act;
    }
    public List<RunningApplication> getAllRunningApplications(long since, long until, boolean descending) {
        List<RunningApplication> apps = new ArrayList<RunningApplication>();
        String order = "DESC";
        if(!descending){
            order = "ASC";
        }

        Cursor cursor = database.query(SQLStorage.TABLE_RUNNING_APPLICATION_HISTORY,
                allColumnsRunningApplication, SQLStorage.COLUMN_TIMESTAMP+" > "+since + " and " +SQLStorage.COLUMN_TIMESTAMP+ " < "+until, null, null, null, SQLStorage.COLUMN_TIMESTAMP+" "+order);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            RunningApplication app = cursorToRunningApplication(cursor);
            apps.add(app);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return apps;
    }
    private RunningApplication cursorToRunningApplication(Cursor cursor) {
            /*SQLStorage.COLUMN_ID,
            SQLStorage.COLUMN_TYPE,
            SQLStorage.COLUMN_TIMESTAMP}*/
        RunningApplication app = new RunningApplication(cursor.getString(1),cursor.getLong(2));
        return app;
    }
    public List<wCall> getAllCalls(long since, long until, boolean descending) {
        List<wCall> calls = new ArrayList<wCall>();
        String order = "DESC";
        if(!descending){
            order = "ASC";
        }

        Cursor cursor = database.query(SQLStorage.TABLE_CALL_HISTORY,
                allColumnsCall, SQLStorage.COLUMN_TIMESTAMP+" > "+since + " and " +SQLStorage.COLUMN_TIMESTAMP+ " < "+until, null, null, null, SQLStorage.COLUMN_TIMESTAMP+" "+order);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            wCall call = cursorToCall(cursor);
            calls.add(call);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return calls;
    }
    private wCall cursorToCall(Cursor cursor) {
            /*SQLStorage.COLUMN_ID,
            SQLStorage.COLUMN_TYPE,
            SQLStorage.COLUMN_TIMESTAMP};*/
        wCall call = new wCall(cursor.getString(1),cursor.getLong(2));
        return call;
    }
    public List<wDevicePosition> getAllDevicePositions(long since, long until, boolean descending) {
        List<wDevicePosition> devPoss = new ArrayList<wDevicePosition>();
        String order = "DESC";
        if(!descending){
            order = "ASC";
        }

        Cursor cursor = database.query(SQLStorage.TABLE_DEVICE_POSITION_HISTORY,
                allColumnsDevicePosition, SQLStorage.COLUMN_TIMESTAMP+" > "+since + " and " +SQLStorage.COLUMN_TIMESTAMP+ " < "+until, null, null, null, SQLStorage.COLUMN_TIMESTAMP+" "+order);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            wDevicePosition devPos = cursorToDevicePosition(cursor);
            devPoss.add(devPos);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return devPoss;
    }
    private wDevicePosition cursorToDevicePosition(Cursor cursor) {
            /*SQLStorage.COLUMN_ID,
            SQLStorage.COLUMN_TYPE,
            SQLStorage.COLUMN_TIMESTAMP};*/
        wDevicePosition devPos = new wDevicePosition(cursor.getString(1),cursor.getLong(2));
        return devPos;
    }
    public List<wNetwork> getAllNetworks(long since, long until, boolean descending) {
        List<wNetwork> networks = new ArrayList<wNetwork>();
        String order = "DESC";
        if(!descending){
            order = "ASC";
        }

        Cursor cursor = database.query(SQLStorage.TABLE_NETWORK_HISTORY,
                allColumnsNetwork, SQLStorage.COLUMN_TIMESTAMP+" > "+since + " and " +SQLStorage.COLUMN_TIMESTAMP+ " < "+until, null, null, null, SQLStorage.COLUMN_TIMESTAMP+" "+order);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            wNetwork network = cursorToNetwork(cursor);
            networks.add(network);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return networks;
    }
    private wNetwork cursorToNetwork(Cursor cursor) {
            /*SQLStorage.COLUMN_ID,
            SQLStorage.COLUMN_TYPE,
            SQLStorage.COLUMN_TIMESTAMP*/
        wNetwork network = new wNetwork(cursor.getString(1),cursor.getLong(2));
        return network;
    }
    public List<wTrack> getAllTracks(long since, long until, boolean descending) {
        List<wTrack> tracks = new ArrayList<wTrack>();
        String order = "DESC";
        if(!descending){
            order = "ASC";
        }

        Cursor cursor = database.query(SQLStorage.TABLE_TRACK_HISTORY,
                allColumnsTrack, SQLStorage.COLUMN_TIMESTAMP+" > "+since + " and " +SQLStorage.COLUMN_TIMESTAMP+ " < "+until, null, null, null, SQLStorage.COLUMN_TIMESTAMP+" "+order);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            wTrack track = cursorToTrack(cursor);
            tracks.add(track);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return tracks;
    }
    private wTrack cursorToTrack(Cursor cursor) {
            /*SQLStorage.COLUMN_ID,
            SQLStorage.COLUMN_TIMESTAMP,
            SQLStorage.COLUMN_ENDTIMESTAMP,
            SQLStorage.COLUMN_TYPE,
            SQLStorage.COLUMN_KILOMETER};*/
        wTrack track = new wTrack(cursor.getLong(1),cursor.getLong(2),cursor.getString(3),cursor.getDouble(4));
        return track;
    }
}
