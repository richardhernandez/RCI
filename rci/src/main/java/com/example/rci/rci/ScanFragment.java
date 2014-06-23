package com.example.rci.rci;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


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
    List<ScanResult> wifiList;

    StringBuilder sb = new StringBuilder();
    private final Handler handler = new Handler();

    private TextView currChannelText;

    private static int stop = 0;

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


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View V = inflater.inflate(R.layout.fragment_scan, container, false);

        scan = (Button)V.findViewById(R.id.scan_scannow);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stop = 0;
                Toast.makeText(getActivity().getApplicationContext(), "Scanning", Toast.LENGTH_SHORT).show();
                mainWifi.startScan();
                doInback();
            }
        });

        currChannelText = (TextView)V.findViewById(R.id.current_channel);

        //Chase advance Button
        advance = (Button)V.findViewById(R.id.options_button_adv);
        basic = (Button)V.findViewById(R.id.options_button_basic);
        basic.setVisibility(View.INVISIBLE);
        curCH = (TextView)V.findViewById(R.id.messageText);
        optCH = (TextView)V.findViewById(R.id.textView2);
        curCHnum = (TextView)V.findViewById(R.id.current_channel);
        optCHnum = (TextView)V.findViewById(R.id.optimal_channel);
        advance.setOnClickListener(new View.OnClickListener(){
            @Override
        public void onClick(View view) {
                curCH.setVisibility(View.INVISIBLE);
                optCH.setVisibility(View.INVISIBLE);
                curCHnum.setVisibility(View.INVISIBLE);
                optCHnum.setVisibility(View.INVISIBLE);
                advance.setVisibility(View.INVISIBLE);
                basic.setVisibility(View.VISIBLE);
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

    public void doInback() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mainWifi = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);
                getActivity().registerReceiver(receiverWifi, new IntentFilter(
                        WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
                mainWifi.startScan();
                something(receiverWifi.get());
                if (stop < 5) {
                    stop++;
                    doInback();
                }
            }
        }, 1000);

    }

    private void something(String t) {
        currChannelText.setText(t);
    }

    class WifiReceiver extends BroadcastReceiver
    {
        private ArrayList<String> connections;
        private ArrayList<Float> signalStrength;
        private List<ScanResult> wifiList;
        private int fq;

        public void onReceive(Context c, Intent intent) {
            connections = new ArrayList<String>();
            signalStrength = new ArrayList<Float>();
            sb = new StringBuilder();
            wifiList = mainWifi.getScanResults();

            for(int i = 0; i < wifiList.size(); i++) {
                ScanResult sr = wifiList.get(i);
                connections.add(sr.SSID);
                //Toast.makeText(getActivity().getApplicationContext(), connections.get(i), Toast.LENGTH_SHORT).show();
                if (i == 0) {
                    fq = sr.frequency;
                }
            }
        }

        public String get() {
            String s;
            try {
                s = "" + wifiList.get(0).frequency;
            }
            catch (NullPointerException n) {
                s = "";
            }

            return s;
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


}
