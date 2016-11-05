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

    //debug
    private static final String TAG = DataProtocolFragment.class.getSimpleName();

    /**
     * protocol changed listener
     */
    private OnDataProtocolChangedListener mListener;

    /**
     * header of protocol
     */
    private EditText mHeader;

    /**
     * tail of protocol
     */
    private EditText mTail;

    public DataProtocolFragment() {
        // Required empty public constructor
    }

    /**
     * get instance of this fragment
     * @return instance
     */
    public static DataProtocolFragment newInstance() {
        DataProtocolFragment fragment = new DataProtocolFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * set listener
     * @param listener OnDataProtocolChangedListener
     */
    public void setOnDataProtocolChangedListener(OnDataProtocolChangedListener listener) {
        mListener = listener;
    }

    /**
     * onCreate life circle
     * @param savedInstanceState state
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * onCreateView life circle
     * @param inflater layout inflater
     * @param container container
     * @param savedInstanceState state
     * @return view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_settings_protocol, container, false);
        mHeader = (EditText) root.findViewById(R.id.edit_text_header);
        mTail = (EditText) root.findViewById(R.id.edit_text_tail);
        FloatingActionButton fab = (FloatingActionButton) root.findViewById(R.id.fab);
        //set on click listener
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, String> map = new HashMap<>();
                if (!mHeader.getText().toString().isEmpty() && !mTail.getText().toString().isEmpty()) {
                    map.put(Constants.KEY_DATA_HEADER, mHeader.getText().toString());
                    map.put(Constants.KEY_DATA_TAIL, mTail.getText().toString());
                    Log.v(TAG, "tail: " + mTail.getText().toString());
                    mListener.onSaveProtocol(map);
                    Toast.makeText(getActivity(), "数据已保存！", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getActivity(), "消息头部或者尾部不能为空！", Toast.LENGTH_SHORT).show();

            }
        });

        //load all data protocol when create view
        setDataProtocol();

        return root;
    }

    /**
     * attach activity
     * @param context activity
     */
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

    /**
     * detach activity
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * recovery default protocol from database
     * @param list a list contains default protocol
     */
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

    /**
     * set protocol when fragment created
     */
    private void setDataProtocol() {
        List<Map<String, String>> list = mListener.getDataProtocol();
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

    /**
     * a callback interface to save custom protocol
     */
    public interface OnDataProtocolChangedListener {

        /**
         * save custom protocol
         * @param map a map contains custom protocol
         */
        void onSaveProtocol(Map<String, String> map);

        /**
         * get protocol from database
         * @return a list
         */
        List<Map<String, String>> getDataProtocol();
    }
}
