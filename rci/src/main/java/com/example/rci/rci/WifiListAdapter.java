package com.example.rci.rci;

/**
 * Created by Dawson on 7/6/14.
 */
import android.content.Context;
import android.graphics.Color;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class WifiListAdapter extends ArrayAdapter<ScanResult> {
    private Context context;
    private List<ScanResult> results;

    public WifiListAdapter(Context context, List<ScanResult> results) {
        super(context, R.layout.row, results);
        this.context = context;
        this.results = results;
    }

    /**
     * Gets a View that displays the transaction at the specified position in the data set.
     * @param position the position of the transaction within the adapter's data set
     * @param convertView the old view to reuse
     * @param parent the parent that this view will be attached to
     * @return View the view corresponding to the data at the specified position
     */
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater lf = LayoutInflater.from(context);
            convertView = lf.inflate(R.layout.row, null);
        }

        ScanResult t = results.get(position);

        TextView ssid = (TextView) convertView.findViewById(R.id.ssid);
        TextView channel = (TextView) convertView.findViewById(R.id.row_channel);
        TextView power = (TextView) convertView.findViewById(R.id.power);

        ssid.setText(t.SSID);
        channel.setText("" + getChannel(t.frequency));
        power.setText("" + t.level);

        return convertView;
    }

    public int getChannel(int frequency) {
        int channel = 0;
        // 2.4 ghz
        if (frequency == 2412) channel = 1;
        else if (frequency == 2417) channel = 2;
        else if (frequency == 2422) channel = 3;
        else if (frequency == 2427) channel = 4;
        else if (frequency == 2432) channel = 5;
        else if (frequency == 2437) channel = 6;
        else if (frequency == 2442) channel = 7;
        else if (frequency == 2447) channel = 8;
        else if (frequency == 2452) channel = 9;
        else if (frequency == 2457) channel = 10;
        else if (frequency == 2462) channel = 11;
        else if (frequency == 2467) channel = 12;
        else if (frequency == 2472) channel = 13;
        else if (frequency == 2484) channel = 14;
            // 5 ghz (only US ones)
        else if (frequency == 5180) channel = 36;
        else if (frequency == 5200) channel = 40;
        else if (frequency == 5220) channel = 44;
        else if (frequency == 5240) channel = 48;
        else if (frequency == 5260) channel = 52;
        else if (frequency == 5280) channel = 56;
        else if (frequency == 5300) channel = 60;
        else if (frequency == 5320) channel = 64;
        else if (frequency == 5500) channel = 100;
        else if (frequency == 5520) channel = 104;
        else if (frequency == 5540) channel = 108;
        else if (frequency == 5560) channel = 112;
        else if (frequency == 5580) channel = 116;
        else if (frequency == 5660) channel = 132;
        else if (frequency == 5680) channel = 136;
        else if (frequency == 5700) channel = 140;
        else if (frequency == 5745) channel = 149;
        else if (frequency == 5765) channel = 153;
        else if (frequency == 5785) channel = 157;
        else if (frequency == 5805) channel = 161;
        else if (frequency == 5825) channel = 165;
        else channel = -1;

        return channel;
    }
}
