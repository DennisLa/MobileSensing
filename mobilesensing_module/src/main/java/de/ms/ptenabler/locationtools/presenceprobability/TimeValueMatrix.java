package de.ms.ptenabler.locationtools.presenceprobability;

import java.lang.reflect.Array;
import java.util.Date;

/**
 * Created by Martin on 27.07.2016.
 */
public class TimeValueMatrix {

    private double[][] matrix;
    private long MillisOfDay = 1000l*3600l*24l;
    private long slotSizeInMillis;
    private double [][] base;
    private int slots;
    /**
     * Creates a Matrix containing 7 days, each of which contains slotsInDay numbers of elements
     * whereat matrix[0][0] will contain the number of events happened on Sunday at slot 1.
      * @param slotsInDay Days will be divided into these number of slots. Milliseconds of a Day % slotsInDay must be 0;
     */
    public TimeValueMatrix(int slotsInDay){
        this(slotsInDay,defaultBase(slotsInDay));
    }
    public TimeValueMatrix(int slotsInDay, double[][] base){
        if(MillisOfDay % slotsInDay !=0){
            throw  new IllegalArgumentException("Milliseconds of a Day % Number of Slots must be 0");
        }
        this.matrix = new double[7][slotsInDay];
        slotSizeInMillis = MillisOfDay / slotsInDay;
        this.slots = slotsInDay;
        this.base =  base;
    }

    public void addTimeEvent(long ts){
        Date date  = new Date(ts);
        ts -=date.getTimezoneOffset()*60l*1000l;
        matrix[date.getDay()][(int)((ts%MillisOfDay)/slotSizeInMillis)] += 1;
    }

    public double[][] getBasedMatrix(){
        double[][] resforCloc= new double[7][slots];
        for(int i=0; i<7; i++){
            for(int j=0; j<slots; j++){
                resforCloc[i][j] = base[i][j] ==0.0 ? 0.0 : (matrix[i][j]/base[i][j]);
            }
        }
        return  resforCloc;
    }
    public double[][] getMatrix(){
        return matrix;
    }


    @Override
    public String toString() {
        double[][] matrix = getBasedMatrix();
        String res ="";
        for(int i = 0; i<7; i++){
            for(int j =0; j<matrix[i].length; j++){
                res+="\t\t"+ matrix[i][j];
            }
        res+="\n";
        }
    return res;
    }
    private static double[][] defaultBase(int slotsInDay){
        double[][] tempBase=new double[7][slotsInDay];
        for(int i=0; i<7; i++){
            for(int j=0; j<slotsInDay; j++){
                tempBase[i][j] = 1;
            }
        }
        return tempBase;
    }
}
