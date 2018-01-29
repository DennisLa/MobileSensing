package de.ms.ptenabler.poi;

import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.ms.ptenabler.util.Utilities;

public class VenueCategory {

	private String id;
	private String name;
	private String plName;
	private String imgUrl;
	private Vector<VenueCategory> subcategories;
    public static HashMap<String,VenueCategory> IDMap = new HashMap<String,VenueCategory>();
    public enum PoiType{
        FOURSQUARE,
        YELP,
        OSM
    }
	public VenueCategory(){
		
	}
	public VenueCategory(String id, String name, String pName, String imgUrl, Vector<VenueCategory> subs){
		this.id =id;
		this.name = name;
		this.plName = pName;
		this.imgUrl = imgUrl;
		this.subcategories = subs;
	}
	
	public static Vector<VenueCategory> fromJSON(String JSON, PoiType type) throws IOException, JSONException {
		JSONObject jobj= new JSONObject(JSON);
		if(type==PoiType.FOURSQUARE){
            int resCode = 200;
            JSONObject meta = jobj.getJSONObject("meta");
            if(meta!=null){
                resCode = meta.getInt("code");
                if(resCode !=200){
                    throw new IOException("Error Code received " + resCode);
                }
            }
            JSONArray results = jobj.getJSONObject("response").getJSONArray("categories");
            return fromJSONArray(results, type);
        }
        return null;
		
	}
	public static Vector<VenueCategory> fromJSONArray(JSONArray array, PoiType type) throws JSONException{
		if(type == PoiType.FOURSQUARE){
            return getFoursquareCatsFromArray(array);
        }

		
		return null;
	}
    public static Vector<VenueCategory>initCategoriesfromURL(String URL) throws IOException, JSONException {
        String cats = Utilities.getDataFromUrl(URL);
        return fromJSON(cats, PoiType.FOURSQUARE);
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPlName() {
		return plName;
	}

	public void setPlName(String plName) {
		this.plName = plName;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public Vector<VenueCategory>getSubcategories() {
		return subcategories;
	}

	public void setSubcategories(Vector<VenueCategory> subcategories) {
		this.subcategories = subcategories;
	}
    private static Vector<VenueCategory> getFoursquareCatsFromArray(JSONArray array) throws JSONException {
        if(array!=null && array.length()>0){
            Vector<VenueCategory> results = new Vector<VenueCategory>();
            for(int i=0; i<array.length(); i++){
                if(!array.isNull(i)){
                    JSONObject jobj= array.getJSONObject(i);
                    JSONArray subJson = jobj.optJSONArray("categories");
                    Vector<VenueCategory> subs = null;
                    if(subJson!=null){
                        if(subJson.length()>0){

                            subs = getFoursquareCatsFromArray(subJson);
                        }else{
                            subs = null;
                        }
                    }
                    VenueCategory toAdd= new VenueCategory (jobj.getString("id"),
                            jobj.getString("name"),
                            jobj.getString("pluralName"),
                            jobj.getJSONObject("icon").getString("prefix")+"88"+jobj.getJSONObject("icon").getString("suffix"),
                            subs);
                    results.add(toAdd);
                    IDMap.put(toAdd.getId(),toAdd);
                }
            }
            return results;
        }
        return null;
    }
}
