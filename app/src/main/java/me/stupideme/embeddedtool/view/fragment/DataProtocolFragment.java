package me.stupideme.embeddedtool.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.stupideme.embeddedtool.Constants;
import me.stupideme.embeddedtool.R;


public class DataProtocolFragment extends Fragment {

    private static final String TAG = DataProtocolFragment.class.getSimpleName();
    private OnDataProtocolChangedListener mListener;
    private EditText mHeader;
    private EditText mTail;

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
        mHeader = (EditText) root.findViewById(R.id.edit_text_header);
        mTail = (EditText) root.findViewById(R.id.edit_text_tail);
        FloatingActionButton fab = (FloatingActionButton) root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, String> map = new HashMap<>();
                if (!mHeader.getText().toString().isEmpty() && !mTail.getText().toString().isEmpty()) {
                    map.put(Constants.KEY_DATA_HEADER, mHeader.getText().toString());
                    map.put(Constants.KEY_DATA_TAIL, mTail.getText().toString());
                    mListener.onSaveProtocol(map);
                    Toast.makeText(getActivity(), "数据已保存！", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getActivity(), "消息头部或者尾部不能为空！", Toast.LENGTH_SHORT).show();

            }
        });

        setDataProtocol();

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

    public void recoveryDefault(List<Map<String, String>> list) {
        Log.v(TAG, String.valueOf(list.size()));
        for (Map<String, String> map : list) {
            if (map.containsKey(Constants.KEY_DATA_HEADER)) {
                mHeader.setText(map.get(Constants.KEY_DATA_HEADER));
                Log.v(TAG, map.get(Constants.KEY_DATA_HEADER));
            }
            if (map.containsKey(Constants.KEY_DATA_TAIL)) {
                mTail.setText(map.get(Constants.KEY_DATA_TAIL));
                Log.v(TAG, map.get(Constants.KEY_DATA_TAIL));
            }
        }
    }

    private void setDataProtocol() {
        List<Map<String, String>> list = mListener.loadAllProtocol();
        for (Map<String, String> map : list) {
            if (map.containsKey(Constants.KEY_DATA_HEADER)) {
                mHeader.setText(map.get(Constants.KEY_DATA_HEADER));
                Log.v(TAG, map.get(Constants.KEY_DATA_HEADER));
            }
            if (map.containsKey(Constants.KEY_DATA_TAIL)) {
                mTail.setText(map.get(Constants.KEY_DATA_TAIL));
                Log.v(TAG, map.get(Constants.KEY_DATA_TAIL));
            }
        }
    }

    public interface OnDataProtocolChangedListener {
        void onSaveProtocol(Map<String, String> map);

        List<Map<String, String>> loadAllProtocol();
    }
}
