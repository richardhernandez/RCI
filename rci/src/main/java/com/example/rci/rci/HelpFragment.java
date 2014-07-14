package com.example.rci.rci;

import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


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

        helpText = (TextView) V.findViewById(R.id.helpTextView);
        helpText.setMovementMethod(new ScrollingMovementMethod());
        routerList = (ListView) V.findViewById(R.id.router_list);
        spinner = (Spinner) V.findViewById(R.id.spinner);
        //ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity().getApplicationContext(), R.array.help_options, android.R.layout.simple_spinner_item);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity().getApplicationContext(), R.array.help_options, R.layout.spinner_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        return V;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        spinnerText = (TextView) view;
        //Delete below once it's set up
        //Toast.makeText(getActivity().getApplicationContext(), "You Selected " + myText.getText(), Toast.LENGTH_SHORT).show();
        if (spinnerText.getText().equals("Scan")){
            helpText.scrollTo(0,0);
            helpText.setText("SCAN. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer " +
                    "tristique turpis in nisl commodo, eget luctus velit malesuada. Pellentesque faucibus" +
                    " purus arcu, sed luctus turpis volutpat ac. Suspendisse ut dignissim elit. Etiam sodales" +
                    " magna non neque vestibulum feugiat eget quis justo. Ut elementum tortor lectus, consequat " +
                    "tempus arcu malesuada at. Curabitur blandit enim a orci sollicitudin blandit. Mauris sollicitudin " +
                    "placerat tempus. Praesent pretium facilisis tellus quis eleifend. Vivamus id diam " +
                    "laoreet, faucibus metus a, facilisis elit. Duis ac metus at purus vestibulum iaculis ac non nibh.\n" +
                    "\n" +
                    "Donec turpis lorem, fermentum scelerisque convallis sit amet, ultrices luctus ante. " +
                    "Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; " +
                    "Sed dui ipsum, dapibus sed convallis in, rhoncus eget leo. Cras varius turpis quis felis" +
                    " malesuada fermentum. Maecenas a leo sed leo scelerisque imperdiet at eget lacus. " +
                    "Phasellus interdum ultrices nunc. Sed ultricies ligula tortor, a cursus purus vehicula quis." +
                    " Curabitur malesuada vitae nisl nec mollis. Aenean lobortis iaculis felis, ut mattis tellus " +
                    "venenatis et. Nunc viverra vel libero ac pharetra. Pellentesque scelerisque euismod ipsum, " +
                    "ut faucibus eros porta eget. Phasellus pellentesque velit eget felis dignissim, ac fermentum " +
                    "eros tempus. Ut feugiat lectus orci, non facilisis lorem mollis nec. Integer eget iaculis metus.");
        }
        else if (spinnerText.getText().equals("Change Router")){
            helpText.scrollTo(0,0);
            helpText.setText("THIS IS THE CHANGE ROUTER HELP. Lorem ipsum dolor sit amet, " +
                    "consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et " +
                    "dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco " +
                    "laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit " +
                    "in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint " +
                    "occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer tristique turpis in nisl commodo, eget luctus velit malesuada. Pellentesque faucibus purus arcu, sed luctus turpis volutpat ac. Suspendisse ut dignissim elit. Etiam sodales magna non neque vestibulum feugiat eget quis justo. Ut elementum tortor lectus, consequat tempus arcu malesuada at. Curabitur blandit enim a orci sollicitudin blandit. Mauris sollicitudin placerat tempus. Praesent pretium facilisis tellus quis eleifend. Vivamus id diam laoreet, faucibus metus a, facilisis elit. Duis ac metus at purus vestibulum iaculis ac non nibh.\n" +
                    "\n" +
                    "Donec turpis lorem, fermentum scelerisque convallis sit amet, ultrices luctus ante. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Sed dui ipsum, dapibus sed convallis in, rhoncus eget leo. Cras varius turpis quis felis malesuada fermentum. Maecenas a leo sed leo scelerisque imperdiet at eget lacus. Phasellus interdum ultrices nunc. Sed ultricies ligula tortor, a cursus purus vehicula quis. Curabitur malesuada vitae nisl nec mollis. Aenean lobortis iaculis felis, ut mattis tellus venenatis et. Nunc viverra vel libero ac pharetra. Pellentesque scelerisque euismod ipsum, ut faucibus eros porta eget. Phasellus pellentesque velit eget felis dignissim, ac fermentum eros tempus. Ut feugiat lectus orci, non facilisis lorem mollis nec. Integer eget iaculis metus.\n" +
                    "\n" +
                    "Nam placerat rhoncus congue. Praesent vestibulum, ligula at convallis sollicitudin, nisl dui mattis turpis, vitae cursus dui augue at felis. Integer condimentum in odio id ullamcorper. Maecenas lobortis placerat mi in eleifend. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Phasellus eu sem eu mi elementum dapibus a a magna. Praesent ultrices, ipsum eget porttitor placerat, libero elit auctor lorem, vitae ultricies arcu augue ullamcorper tellus. Donec imperdiet leo vel eleifend auctor. Integer dignissim dapibus augue vel dictum. Pellentesque faucibus cursus consectetur. Fusce vitae posuere odio.\n" +
                    "\n" +
                    "Duis sed laoreet est. Morbi pellentesque posuere tortor vel vulputate. Donec dapibus erat non nisi aliquam ultrices. Integer pellentesque condimentum lectus in varius. Nulla a tristique odio. Suspendisse tempor porta arcu eu vehicula. Nunc consequat magna ligula, ut mollis risus fermentum ut. Donec ac tempor dolor. In molestie, sapien vitae bibendum adipiscing, ante lectus congue elit, condimentum fermentum metus sem id lectus. Curabitur pharetra consequat volutpat. Nulla fermentum in erat eu condimentum. Duis sodales varius tortor, ac tempor ipsum ultricies id.\n" +
                    "\n" +
                    "Etiam ultricies aliquet tortor, eu tincidunt dolor ultrices ut. Integer condimentum volutpat lorem quis gravida. Vestibulum auctor velit molestie eleifend euismod. Duis sit amet porttitor dolor, a euismod turpis. Nulla varius fringilla tempor. Pellentesque euismod pulvinar ipsum quis lobortis. In libero nibh, ornare vel nibh sed, convallis viverra felis. Proin commodo magna ut mi dapibus commodo. In hac habitasse platea dictumst.\n" +
                    "\n" +
                    "Fusce eget volutpat velit. Mauris mollis aliquet ligula non elementum. Nulla enim dui, placerat id risus vel, sagittis tincidunt sem. Donec sagittis mi a dui placerat ultricies. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Nulla accumsan eleifend mauris, at pharetra dolor congue iaculis. Etiam tristique enim quam, pretium condimentum ante vulputate placerat. Pellentesque eget augue sed massa adipiscing dapibus a ut ipsum.\n" +
                    "\n" +
                    "Nunc nec nulla sem. Sed rhoncus tellus nibh, id malesuada est pellentesque sit amet. In nec diam a tellus consectetur lacinia. Sed ultrices scelerisque pharetra. Maecenas nibh turpis, lacinia non ullamcorper sed, fringilla eget nisi. Duis sodales lorem non feugiat tempor. Duis in lorem consectetur, condimentum nunc nec, laoreet quam. Sed rutrum lacus sed mi bibendum, et faucibus sapien commodo. Nunc varius sem quis urna scelerisque, sit amet iaculis urna elementum. Duis sagittis volutpat est ut placerat.\n" +
                    "\n" +
                    "Maecenas pharetra placerat ornare. In hac habitasse platea dictumst. Integer augue quam, mollis a mattis vel, mollis et magna. Morbi aliquet, nulla sit amet feugiat blandit, nulla risus aliquam sem, in aliquam nunc dui sed velit. Vivamus condimentum posuere lacus. Cras tempor eget nibh vitae hendrerit. Nullam pulvinar nunc non eleifend suscipit. Mauris rutrum suscipit tempor.");
        }
        else if (spinnerText.getText().equals("Understand Channels")){
            //Toast.makeText(getActivity().getApplicationContext(), "You Selected CHANNELS", Toast.LENGTH_SHORT).show();
            helpText.scrollTo(0,0);
            helpText.setText("THIS IS THE UNDERSTAND CHANNELS HELP");
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
