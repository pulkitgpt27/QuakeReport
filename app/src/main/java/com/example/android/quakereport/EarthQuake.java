package com.example.android.quakereport;

import java.util.Date;

/**
 * Created by Pulkit on 07-06-2017.
 */
public class EarthQuake {
    private double  mag;

    private String place;

    private long timeinms;
    private String url;

    public EarthQuake(double m, String p,long t,String u)
    {
        mag=m;
        place=p;
        timeinms=t;
        url=u;
    }

    public double  getMag(){return mag;}

    public String getPlace(){return place;}

    public long getDate(){return timeinms;}

    public String getUrl(){return url;}
}
