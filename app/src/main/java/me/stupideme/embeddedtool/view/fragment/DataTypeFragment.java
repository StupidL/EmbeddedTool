package me.stupideme.embeddedtool.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Map;

import me.stupideme.embeddedtool.R;


public class DataTypeFragment extends Fragment {

    private OnDataTypeChangedListener mListener;

    public DataTypeFragment() {
        // Required empty public constructor
    }

    public static DataTypeFragment newInstance() {
        DataTypeFragment fragment = new DataTypeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings_type, container, false);
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
            }
        });
        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDataTypeChangedListener) {
            mListener = (OnDataTypeChangedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnDataProtocolChangedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void recoveryDefault(Map<String, String> map) {

    }

    public interface OnDataTypeChangedListener {
        void addDataType(Map<String, String> map);

        void removeDataType(String name);
    }
}
