package com.example.rci.rci;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HelpFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HelpFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class HelpFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Spinner spinner;
    private ListView routerList;
    TextView helpText;
    TextView spinnerText;
    ArrayAdapter<LinkItem> mAdapter;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HelpFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HelpFragment newInstance(String param1, String param2) {
        HelpFragment fragment = new HelpFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public HelpFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View V = inflater.inflate(R.layout.fragment_help, container, false);

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        helpText = (TextView) V.findViewById(R.id.helpTextView);
        helpText.setMovementMethod(new ScrollingMovementMethod());
        routerList = (ListView) V.findViewById(R.id.router_list);
        spinner = (Spinner) V.findViewById(R.id.spinner);


        //ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity().getApplicationContext(), R.array.help_options, android.R.layout.simple_spinner_item);
        ArrayAdapter adapterSpinner = ArrayAdapter.createFromResource(getActivity(), R.array.help_options, R.layout.spinner_item);
        //ArrayAdapter adapterRouterList = ArrayAdapter.createFromResource(getActivity(), R.array.router_hardware, android.R.layout.simple_dropdown_item_1line);// R.layout.simple_list_item_1);
        //routerList.setAdapter(adapterRouterList);
        spinner.setAdapter(adapterSpinner);
        spinner.setOnItemSelectedListener(this);

        List<LinkItem> items = new ArrayList<LinkItem>();
        items.add(new LinkItem("3Com", "http://setuprouter.com/router/3com/3crwer200-75/manual-1328.pdf"));
        items.add(new LinkItem("Apple", "http://manuals.info.apple.com/MANUALS/1000/MA1576/en_US/airport_extreme_5th_gen_setup.pdf"));
        items.add(new LinkItem("Asus", "http://setuprouter.com/router/asus/rt-n10/manual-1095.pdf"));
        items.add(new LinkItem("Belkin", "http://www.belkin.com/networking/manual/MAN_F9K1001_8820-00773_RevA01_N150_Router.pdf"));
        items.add(new LinkItem("Buffalo", "http://www.danets.com/download/WHR-HP-G54-Manual.pdf"));
        items.add(new LinkItem("Linksys", "http://www.cisco.com/c/dam/en/us/td/docs/routers/csbr/wrv54g/user/guide/wrv54g_ug.pdf"));
        items.add(new LinkItem("Microsoft", "http://www.manualslib.com/manual/295598/Microsoft-Mn-500.html"));
        items.add(new LinkItem("Motorola", "http://www.motorola.com/mdirect/manuals/br700_user_manual_e.pdf"));
        items.add(new LinkItem("MSI", "http://faenl.msi.com/ftp/Networking/RG54SE%20+%20US54SE/Manuals/RG54SE_User%20Manual.pdf"));
        items.add(new LinkItem("Netgear", "https://www.upc.nl/pdf/upc-handleiding-netgear-wnr2000.pdf"));
        items.add(new LinkItem("Senao", "http://www.tcshop.nl/docs/senao/scb3220_usermanual.pdf"));
        items.add(new LinkItem("SpeedTouch", "http://www.thinkbroadband.com/router-manuals/ST585_UserGuide.pdf"));
        items.add(new LinkItem("Trendnet", "http://setuprouter.com/router/trendnet/tew-652brp/manual-813.pdf"));
        items.add(new LinkItem("U.S. Robotics", "http://support.usr.com/support/5462/5462-files/5462-ug.pdf"));
        items.add(new LinkItem("Zyxel", "http://www.winncom.com/pdf/zyxel_x-550_userguide.pdf"));

        // R.layout.row is a layout, that contains only one TextView
        mAdapter = new ArrayAdapter<LinkItem>(getActivity(), android.R.layout.simple_dropdown_item_1line, items);

        routerList.setAdapter(mAdapter);
        routerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String url = mAdapter.getItem(position).getLink();
                try
                {
                    Intent intentUrl = new Intent(Intent.ACTION_VIEW);
                    intentUrl.setDataAndType(Uri.parse(url), "application/pdf");
                    intentUrl.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    getActivity().startActivity(intentUrl);
                }
                catch (ActivityNotFoundException e)
                {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
            }
        });

        return V;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        spinnerText = (TextView) view;
        //Delete below once it's set up
        //Toast.makeText(getActivity().getApplicationContext(), "You Selected " + myText.getText(), Toast.LENGTH_SHORT).show();
        if (spinnerText.getText().equals("Scan")){
            routerList.setVisibility(View.INVISIBLE);
            helpText.setVisibility(View.VISIBLE);
            helpText.scrollTo(0,0);
            helpText.setText("SCAN.\n" +
                    "Hit the “Scan Now” button to scan the available Wi-Fi networks around you. " +
                    "When it is done scanning, you will get the following information: current channel, optimal channel, and a graph.\n" +
                    "\n" +
                    "Current Channel: The current Wi-Fi network that you are connected to is displayed here. " +
                    "However it is only the channel that this network is on that is displayed. " +
                    "Example: 6, this means that your Wi-Fi network that you are connected to is broadcasting on channel 6.\n" +
                    "\n" +
                    "Optimal Chanel: This is the channel that the network you are connected to should broadcast on. " +
                    "This has been determined by a couple different factors. " +
                    "By looking at all surrounding channels that could interfere with the network that you are connected to, " +
                    "RCI groups those networks and organizes them by which channels they are broadcasting on. " +
                    "By looking at which networks occupy which channels RCI can decide which channel has the least amount of networks and eventually evenly distribute those networks around. " +
                    "Optimally RCI would have only channels 1, 6 and 11 in use and populated equally.\n" +
                    "\n" +
                    "Graph: This shows what is explained above. RCI has grouped all networks that can be seen by which channels they are currently broadcasting on. " +
                    "The height of the graph is the amount of networks on that particular channel. " +
                    "Example: channel 6 goes up to 4. This means that there are 4 networks that are currently broadcasting on channel 6.\n");
        }
        else if (spinnerText.getText().equals("Understand Channels")){
            routerList.setVisibility(View.INVISIBLE);
            helpText.setVisibility(View.VISIBLE);
            helpText.scrollTo(0,0);
            helpText.setText("UNDERSTAND THE IMPORTANCE OF CHANNELS\n" +
                    "Let’s get technical for a moment. The 2.4 GHz Wi-Fi is made up of 11 channels. " +
                    "There is not enough room for all channels to be completely separate. " +
                    "This means there will be some overlap with neighboring channels. " +
                    "For example, if your network is on channel 9, it will overlap with channels 7, 8, 10, 11.\n" +
                    "\n" +
                    "This creates a situation where your network is yelling loudly in a foreign language, for example, distracting any network on an overlapping channel. " +
                    "This means the optimal channels are 1, 6, and 11 in the United States since they do not overlap each other. " +
                    "The way RCI will handle this situation is by scanning the available channels, and showing the user which channel would best benefit them.\n");
        }
        else if (spinnerText.getText().equals("Change Router Channel")){
            routerList.setVisibility(View.VISIBLE);
            helpText.setVisibility(View.INVISIBLE);
            helpText.scrollTo(0,0);

        }
        else{

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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
