package de.ms.ptenabler.poi;

import java.util.Vector;

public class MobilePractice {
	private String label_de;
	private Vector<String> categories_yelp;
private Vector<String> categories_fs;
	
	
	public MobilePractice(String label_de, Vector<String> categories_yelp, Vector<String> categories_fs){
		this.label_de = label_de;
		this.categories_yelp = categories_yelp;
        this.categories_fs = categories_fs;
	}


	public String getLabel_de() {
		return label_de;
	}


	public void setLabel_de(String label_de) {
		this.label_de = label_de;
	}


	public Vector<String> getCategories_yelp() {
		return categories_yelp;
	}


	public void setCategories_yelp(Vector<String> categories_yelp) {
		this.categories_yelp = categories_yelp;
	}
    public Vector<String> getCategories_fs() {
        return categories_fs;
    }

    public void setCategories_fs(Vector<String> categories_fs) {
        this.categories_fs = categories_fs;
    }

    public String getFSCategoriesAsString(){
        return asString(categories_fs);
    }
    public String getYelpCategoriesAsString(){
        return asString(categories_yelp);
    }

    private String asString(Vector<String> cats){
        String res ="";
        boolean first = true;
        for(String txt:cats){
            if(first){
                res+=txt;
                first=false;
            }else{
                res+=","+txt;
            }

        }
        return res;
    }
}
