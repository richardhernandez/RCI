package com.example.rci.rci;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.BarGraphView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewSeries;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ScanFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ScanFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class ScanFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private TextView currentChannel, optimalChannel;

    private TextView curCH;
    private TextView optCH;
    private TextView curCHnum;
    private TextView optCHnum;
    private Button advance;
    private Button scan;
    private Button basic;

    private WifiManager mainWifi;
    private WifiReceiver receiverWifi;
    ArrayList<ScanResult> wifiList;

    StringBuilder sb = new StringBuilder();
    private final Handler handler = new Handler();

    private TextView currChannelText;
    private TextView optChannelText;

    private static int stop = 0;

    private GraphView graphView;
    private LinearLayout layout;
    private TextView numNetworksAxis;
    private TextView channelNumAxis;

    private ListView routerList;
    private WifiListAdapter adapter;

    private TextView ssid_title;
    private TextView channel_title;
    private TextView power_title;

    private int numScans;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ScanFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ScanFragment newInstance(String param1, String param2) {
        ScanFragment fragment = new ScanFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ScanFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        currentChannel = (TextView)getActivity().findViewById(R.id.current_channel);
        optimalChannel = (TextView)getActivity().findViewById(R.id.optimal_channel);

        //Set up wifi stuff here
        mainWifi = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);

        receiverWifi = new WifiReceiver();
        getActivity().registerReceiver(receiverWifi, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

        if(!mainWifi.isWifiEnabled()) {
            mainWifi.setWifiEnabled(true);
        }


        graphView = new BarGraphView(getActivity(), "Router Channels Nearby");
        graphView.getGraphViewStyle().setNumHorizontalLabels(11);
        GraphViewSeries series = new GraphViewSeries("aaa", null, new GraphView.GraphViewData[]{
                new GraphView.GraphViewData(1, 0),
                new GraphView.GraphViewData(6, 0),
                new GraphView.GraphViewData(11, 0)
        });
        graphView.addSeries(series);

        layout = (LinearLayout) getActivity().findViewById(R.id.graph1);
        //layout.addView(graphView);

        //routerList = (ListView) getActivity().findViewById(R.id.router_list);
        //routerList.setVisibility(View.INVISIBLE);

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("PAUSING!!!!!!!!", "");

        //** TESTING SAVING LIST

        //**/

        mainWifi = null;
        while (handler.hasMessages(0)) {
            handler.removeMessages(0);
        }

        stopScanning();
    }

    @Override
    public void onDestroy() {
        while (handler.hasMessages(0)) {
            handler.removeMessages(0);
        }
        getActivity().unregisterReceiver(receiverWifi);
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("RESUMING!!!!!!!!", "");
        //Set up wifi stuff here
        mainWifi = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);

        receiverWifi = new WifiReceiver();
        getActivity().registerReceiver(receiverWifi, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

        if(!mainWifi.isWifiEnabled()) {
            mainWifi.setWifiEnabled(true);
        }

        //** TESTING RESTORE LIST

        //**/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View V = inflater.inflate(R.layout.fragment_scan, container, false);

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        scan = (Button)V.findViewById(R.id.scan_scannow);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stop = 0;
                Toast.makeText(getActivity().getApplicationContext(), "Scanning", Toast.LENGTH_SHORT).show();
                mainWifi.startScan();
                startScanning();
            }
        });

        currChannelText = (TextView)V.findViewById(R.id.current_channel);
        optChannelText = (TextView)V.findViewById(R.id.optimal_channel);

        layout = (LinearLayout) getActivity().findViewById(R.id.graph1);

        routerList = (ListView) V.findViewById(R.id.router_list);
        wifiList = new ArrayList<ScanResult>();

        adapter = new WifiListAdapter(getActivity(), wifiList);
        routerList.setAdapter(adapter);
        routerList.setVisibility(View.INVISIBLE);
        numScans = 0;
        //routerList.setOnItemClickListener(getActivity());
        ssid_title = (TextView)V.findViewById(R.id.ssid_title);
        channel_title = (TextView)V.findViewById(R.id.channel_title);
        power_title = (TextView)V.findViewById(R.id.power_title);
        power_title.setVisibility(View.INVISIBLE);
        ssid_title.setVisibility(View.INVISIBLE);
        channel_title.setVisibility(View.INVISIBLE);

        //Chase advance Button
        advance = (Button)V.findViewById(R.id.options_button_adv);
        basic = (Button)V.findViewById(R.id.options_button_basic);
        basic.setVisibility(View.INVISIBLE);
        curCH = (TextView)V.findViewById(R.id.messageText);
        optCH = (TextView)V.findViewById(R.id.textView2);
        curCHnum = (TextView)V.findViewById(R.id.current_channel);
        optCHnum = (TextView)V.findViewById(R.id.optimal_channel);
        channelNumAxis = (TextView)V.findViewById(R.id.textView3);
        numNetworksAxis = (TextView)V.findViewById(R.id.textView4);

        advance.setOnClickListener(new View.OnClickListener(){
            @Override
        public void onClick(View view) {
                curCH.setVisibility(View.INVISIBLE);
                optCH.setVisibility(View.INVISIBLE);
                curCHnum.setVisibility(View.INVISIBLE);
                optCHnum.setVisibility(View.INVISIBLE);
                advance.setVisibility(View.INVISIBLE);
                basic.setVisibility(View.VISIBLE);

                graphView.setVisibility(View.INVISIBLE);
                layout = (LinearLayout) getActivity().findViewById(R.id.graph1);
                layout.setVisibility(View.INVISIBLE);
                channelNumAxis.setVisibility(View.INVISIBLE);
                numNetworksAxis.setVisibility(View.INVISIBLE);

                routerList.setVisibility(View.VISIBLE);

                power_title.setVisibility(View.VISIBLE);
                ssid_title.setVisibility(View.VISIBLE);
                channel_title.setVisibility(View.VISIBLE);
            }
        });

        basic.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                curCH.setVisibility(View.VISIBLE);
                optCH.setVisibility(View.VISIBLE);
                curCHnum.setVisibility(View.VISIBLE);
                optCHnum.setVisibility(View.VISIBLE);
                basic.setVisibility(View.INVISIBLE);
                advance.setVisibility(View.VISIBLE);

                graphView.setVisibility(View.VISIBLE);
                layout = (LinearLayout) getActivity().findViewById(R.id.graph1);
                layout.setVisibility(View.VISIBLE);
                channelNumAxis.setVisibility(View.VISIBLE);
                numNetworksAxis.setVisibility(View.VISIBLE);

                routerList.setVisibility(View.INVISIBLE);

                power_title.setVisibility(View.INVISIBLE);
                ssid_title.setVisibility(View.INVISIBLE);
                channel_title.setVisibility(View.INVISIBLE);
            }
        });

        return V;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public Runnable scanRunnable = new Runnable() {
        @Override
        public void run() {
            mainWifi = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);
            getActivity().registerReceiver(receiverWifi, new IntentFilter(
                    WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
            mainWifi.startScan();
            setCurrChannel(receiverWifi.get());
            setOptChannel(receiverWifi.getOptChannel());

            List<ScanResult> list = receiverWifi.getWifiList();
            if (list != null && !list.isEmpty()) {
                numScans++;
                if (numScans == 1) {
                    setWifiList(list);
                }
            }

            handler.postDelayed(scanRunnable, 1000);
        }
    };

    public void startScanning() {
        scanRunnable.run();
    }

    public void stopScanning() {
        handler.removeCallbacks(scanRunnable);
    }

    private void setCurrChannel(String t) {
        currChannelText.setText(t);
    }

    private void setOptChannel(String t) {
        optChannelText.setText(t);
    }

    private void setWifiList(List<ScanResult> wifiListArg) {
        try {
            adapter = new WifiListAdapter(getActivity(), wifiListArg);
            routerList.setAdapter(adapter);
/*
            //remove items no longer found from wifiList
            for (int i = 0; i < wifiList.size(); i++) {
                if (!wifiListArg.contains(wifiList.get(i))) {
                    wifiList.remove(wifiList.get(i));
                }
            }
            //add new items to wifiList
            for (int i = 0; i < wifiListArg.size(); i++) {
                if (!wifiList.contains(wifiListArg.get(i))) {
                    wifiList.add(wifiList.get(i));
                }
            }

            adapter.notifyDataSetChanged(); */
        }
        catch (Exception e) {
            // Probably do something here
        }
    }

    class WifiReceiver extends BroadcastReceiver
    {
        private ArrayList<String> connections;
        private ArrayList<Float> signalStrength;
        private List<ScanResult> wifiList;
        private int fq;
        private ArrayList<Integer> channels;
        private int optChannel;

        public void onReceive(Context c, Intent intent) {
            if (mainWifi == null) return;  // god bless
            connections = new ArrayList<String>();
            signalStrength = new ArrayList<Float>();
            sb = new StringBuilder();
            wifiList = mainWifi.getScanResults();
            channels = new ArrayList<Integer>();

            for(int i = 0; i < wifiList.size(); i++) {
                ScanResult sr = wifiList.get(i);
                connections.add(sr.SSID);
                channels.add(getChannel(sr.frequency));
                //Toast.makeText(getActivity().getApplicationContext(), connections.get(i), Toast.LENGTH_SHORT).show();
                if (i == 0) {
                    fq = getChannel(sr.frequency);
                }
            }

            optChannel = getOptimalChannel(channels);
        }

        public String get() {
            String s;
            try {
                s = "" + getChannel(wifiList.get(0).frequency);
            }
            catch (NullPointerException n) {
                s = "";
            }

            return s;
        }

        public String getOptChannel() {
            return ""+optChannel;
        }

        public String getCurrentSsid(Context context) {
            String ssid = null;
            ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (networkInfo.isConnected()) {
                final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
                if (connectionInfo != null && !TextUtils.isEmpty(connectionInfo.getSSID())) {
                    ssid = connectionInfo.getSSID();
                }
            }
            return ssid;
        }

        public List<ScanResult> getWifiList() {
            //wifiList.remove()
            // Get power level to make a threshold value of -75db
            ArrayList<ScanResult> newWifiList = new ArrayList<ScanResult>();
            if (wifiList != null && !wifiList.isEmpty()) {
                for (int i = 0; i < wifiList.size(); i++) {
                    ScanResult scanObj = wifiList.get(i);
                    int RSSI = scanObj.level;
                    if (Math.abs(RSSI) < 75) {
                        newWifiList.add(scanObj);
                    }
                }
                wifiList = newWifiList;
            }
            return wifiList;
        }

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
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

    public int getOptimalChannel(ArrayList<Integer> channels) {
        int num1 = 0;
        int num2 = 0;
        int num3 = 0;
        int num4 = 0;
        int num5 = 0;
        int num6 = 0;
        int num7 = 0;
        int num8 = 0;
        int num9 = 0;
        int num10 = 0;
        int num11 = 0;
        int maxNum = 0;

        // Get number of access points on channels 1, 6, 11
        for (int i = 0; i < channels.size(); i++) {
            if (channels.get(i) == 1)
                num1++;
            else if (channels.get(i) == 2)
                num2++;
            else if (channels.get(i) == 3)
                num3++;
            else if (channels.get(i) == 4)
                num4++;
            else if (channels.get(i) == 5)
                num5++;
            else if (channels.get(i) == 6)
                num6++;
            else if (channels.get(i) == 7)
                num7++;
            else if (channels.get(i) == 8)
                num8++;
            else if (channels.get(i) == 9)
                num9++;
            else if (channels.get(i) == 10)
                num10++;
            else if (channels.get(i) == 11)
                num11++;
        }


        int optChannel = 0;

        //return channel with lowest number of access points
        if (num1 < num6 && num1 < num11 || (num1 == num6 && num1 == num11)) {
            optChannel = 1;
        }
        else if (num6 < num11) {
            optChannel = 6;
        }
        else {
            optChannel = 11;
        }

        maxNum = Math.max(Math.max(Math.max(Math.max(Math.max(num1, num2), num3), num4), num5), num6);
        maxNum = Math.max(Math.max(Math.max(Math.max(Math.max(maxNum, num7), num8), num9), num10), num11);

        //set graph here
        GraphViewSeries series = new GraphViewSeries("graphViewSeries", null, new GraphView.GraphViewData[]{
                new GraphView.GraphViewData(1, num1),
                new GraphView.GraphViewData(2, num2),
                new GraphView.GraphViewData(3, num3),
                new GraphView.GraphViewData(4, num4),
                new GraphView.GraphViewData(5, num5),
                new GraphView.GraphViewData(6, num6),
                new GraphView.GraphViewData(7, num7),
                new GraphView.GraphViewData(8, num8),
                new GraphView.GraphViewData(9, num9),
                new GraphView.GraphViewData(10, num10),
                new GraphView.GraphViewData(11, num11)

        });
        graphView.setManualYAxisBounds(maxNum, 0);
        graphView.getGraphViewStyle().setNumVerticalLabels(maxNum + 1);
        graphView.removeAllSeries();
        graphView.addSeries(series);
        layout = (LinearLayout) getActivity().findViewById(R.id.graph1);
        try {
            layout.addView(graphView);
        }
        catch (Exception e) {
            Log.i("EXCEPTION!!!!!!!!", "");
        }


        return optChannel;
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        // This should just gray out all of the EditTexts and only update if data was changed
        // Maybe I should implement a UserAccount.equals method.

    }

}
