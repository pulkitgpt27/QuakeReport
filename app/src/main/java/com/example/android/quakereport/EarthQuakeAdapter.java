package com.example.android.quakereport;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import android.graphics.drawable.GradientDrawable;


/**
 * Created by Pulkit on 07-06-2017.
 */
public class EarthQuakeAdapter extends ArrayAdapter<EarthQuake> {
    /**
     *
     * @param context
     * @param word
     */
    public EarthQuakeAdapter(Activity context, ArrayList<EarthQuake> word) {
        super(context,0,word);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
     View listItemView=convertView;

        if(listItemView==null)
            listItemView= LayoutInflater.from(getContext()).inflate(R.layout.earthquake_list_item,parent,false);

        View contain=listItemView.findViewById(R.id.list);

        EarthQuake curquake=getItem(position);


        DecimalFormat formatter = new DecimalFormat("0.0");
        String output=formatter.format(curquake.getMag());


        TextView magTextView=(TextView) listItemView.findViewById(R.id.magnitude);
        GradientDrawable magCircle=(GradientDrawable) magTextView.getBackground();
        int magColor=getMagnitudeColor(curquake.getMag());
        magCircle.setColor(magColor);
        magTextView.setText(output);

        String s1="";
        String s2="";

        if(curquake.getPlace().contains("of"))
        {
         int find=curquake.getPlace().indexOf("of");
            s1=curquake.getPlace().substring(0,find+2);
            s2=curquake.getPlace().substring(find+3,curquake.getPlace().length());
        }
        else
        {
            s1="Near";
            s2=curquake.getPlace();
        }

        TextView offsetTextView=(TextView) listItemView.findViewById(R.id.reference);
        offsetTextView.setText(s1);


        TextView placeTextView=(TextView) listItemView.findViewById(R.id.place);
        placeTextView.setText(s2);



        Date dateObject=new Date(curquake.getDate());
        TextView dateTextView=(TextView) listItemView.findViewById(R.id.date);
        String dateToDisplay=formatDate(dateObject);
        dateTextView.setText(dateToDisplay);

        TextView timeview=(TextView) listItemView.findViewById(R.id.time);
        String formattedTime=formatTime(dateObject);
        timeview.setText(formattedTime);

        return listItemView;
    }

    private int getMagnitudeColor(double mag) {
        int magColorResoureId;
        int magFloor=(int) mag;
        switch (magFloor)
        {
            case 1:magColorResoureId=R.color.magnitude1;
                break;
            case 2:magColorResoureId=R.color.magnitude2;
                break;
            case 3:magColorResoureId=R.color.magnitude3;
                break;
            case 4:magColorResoureId=R.color.magnitude4;
                break;
            case 5:magColorResoureId=R.color.magnitude5;
                break;
            case 6:magColorResoureId=R.color.magnitude6;
                break;
            case 7:magColorResoureId=R.color.magnitude7;
                break;
            case 8:magColorResoureId=R.color.magnitude8;
                break;
            case 9:magColorResoureId=R.color.magnitude9;
                break;
            default:
                magColorResoureId=R.color.magnitude10plus;
                break;

        }
        return ContextCompat.getColor(getContext(),magColorResoureId);
    }

    private String formatTime(Date dateObject) {
        SimpleDateFormat timeformat=new SimpleDateFormat("h:mm a");
        return timeformat.format(dateObject);
    }

    private String formatDate(Date dateObject) {
        SimpleDateFormat dateformat=new SimpleDateFormat("MMM DD,yyyy");
        return dateformat.format(dateObject);

    }
}
