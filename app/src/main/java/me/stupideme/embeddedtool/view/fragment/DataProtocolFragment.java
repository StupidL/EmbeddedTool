package me.stupideme.embeddedtool.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Map;

import me.stupideme.embeddedtool.R;


public class DataProtocolFragment extends Fragment {

    private OnDataProtocolChangedListener mListener;

    public DataProtocolFragment() {
        // Required empty public constructor
    }

    public static DataProtocolFragment newInstance() {
        DataProtocolFragment fragment = new DataProtocolFragment();
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
        View root = inflater.inflate(R.layout.fragment_settings_protocol, container, false);
        FloatingActionButton fab = (FloatingActionButton) root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String ,String> map = new HashMap<>();

                mListener.onSaveProtocol(map);
            }
        });


        return root;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDataProtocolChangedListener) {
            mListener = (OnDataProtocolChangedListener) context;
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

    public void recoveryDefault(Map<String,String> map){

    }

    public interface OnDataProtocolChangedListener {
        void onSaveProtocol(Map<String, String> map);
    }
}
