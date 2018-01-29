package de.ms.ptenabler.locationtools;


import android.util.Log;

public class PropableNextLocationResult implements Comparable<PropableNextLocationResult>{

private double prob;
private final ClusteredLocation cloc;

	public PropableNextLocationResult(double probability, ClusteredLocation loc){
		this.prob = probability;
		this.cloc= loc;
	}

    @Override
	public int compareTo(PropableNextLocationResult another) {

        if(this.getCloc().getId() == another.getCloc().getId()){

            return 0;

        }
		if(this.getProb()>another.getProb()){
            return -1;
        }
		if(this.getProb()<another.getProb()){
            return 1;
        }
		return this.getCloc().compareTo(another.getCloc());
	}
    @Override
    public boolean equals(Object o) {
        if(o instanceof PropableNextLocationResult){
            if(this.compareTo((PropableNextLocationResult)o) == 0){
                return true;
            }else{
                return false;
            }

            //return this.compareTo((PropableNextLocationResult)o) == 0;

        }
        return false;
    }
    @Override
    public int hashCode(){
        return (int)this.getCloc().getId();
    }


	public double getProb() {
		return prob;
	}


	public void setProb(double prob) {
		this.prob = prob;
	}


	public ClusteredLocation getCloc() {
		return cloc;
	}



	
	public void addPropability(PropableNextLocationResult other, double weight){
        setProb((this.getProb()+(weight*other.getProb()))/(weight+1));
    }

    @Override
    public String toString() {
        return this.getCloc().getLoc().place + " ("+ (int)(this.getProb()*100) +"%)";
    }
}
