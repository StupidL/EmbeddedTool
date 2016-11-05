package me.stupideme.embeddedtool.view.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.stupideme.embeddedtool.Constants;
import me.stupideme.embeddedtool.R;

/**
 * a fragment in SettingsActivity where you can custom data type and code
 */
public class DataTypeFragment extends Fragment {

    //debug
    private static final String TAG = DataTypeFragment.class.getSimpleName();

    /**
     * data type changed listener
     */
    private OnDataTypeChangedListener mListener;

    /**
     * a list contains data types
     */
    private List<String> mDataTypesList;

    /**
     * adapter for list view
     */
    private DataTypeAdapter mAdapter;

    /**
     * constructor
     */
    public DataTypeFragment() {
        //init list
        mDataTypesList = new ArrayList<>();
        //init adapter
        mAdapter = new DataTypeAdapter();
    }

    /**
     * get instance of this fragment
     * @return a object of this fragment
     */
    public static DataTypeFragment newInstance() {
        return new DataTypeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //init views
        View rootView = inflater.inflate(R.layout.fragment_settings_type, container, false);
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        ListView listView = (ListView) rootView.findViewById(R.id.list_view);
        listView.setAdapter(mAdapter);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
        //attach listener
        mListener = (OnDataTypeChangedListener) getActivity();
        //set default data types
        setTypes(mListener.getDataType());
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDataTypesList.clear();
    }

    /**
     * recovery default data types
     *
     * @param list a list contains default data types
     */
    public void recoveryDefault(List<Map<String, String>> list) {
        //clear current list
        mDataTypesList.clear();
        //reset content of list
        setTypes(list);
    }

    /**
     * set data types for list view
     * @param list a list contains data types
     */
    private void setTypes(List<Map<String, String>> list) {
        Log.v(TAG, String.valueOf(list.size()));
        for (Map<String, String> map : list) {
            StringBuilder builder = new StringBuilder();
            int count = 0;
            if (map.containsKey(Constants.KEY_DATA_NAME) && !map.get(Constants.KEY_DATA_NAME).isEmpty()) {
                builder.append(map.get(Constants.KEY_DATA_NAME)).append(":0x");
                count++;
            }
            if (map.containsKey(Constants.KEY_DATA_CODE) && !map.get(Constants.KEY_DATA_CODE).isEmpty()) {
                builder.append(map.get(Constants.KEY_DATA_CODE));
                count++;
            }
            if (count == 2)
                mDataTypesList.add(builder.toString());
            Log.v(TAG, builder.toString());
        }
        //notify data set changed
        mAdapter.notifyDataSetChanged();
    }

    /**
     * show add data type dialog
     */
    public void showDialog() {
        //create a linear layout
        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        //create a text input layout to contain edit text
        TextInputLayout layout = new TextInputLayout(getActivity());
        layout.setPadding(32, 32, 32, 0);
        //create text input edit text
        final TextInputEditText editText = new TextInputEditText(getActivity());
        editText.setHint("数据类型名称");
        //add edit text in text input layout
        layout.addView(editText);
        //create another text input layout to contain another edit text
        TextInputLayout layout2 = new TextInputLayout(getActivity());
        layout2.setPadding(32, 32, 32, 0);
        //create another text input edit text
        final TextInputEditText editText2 = new TextInputEditText(getActivity());
        editText2.setHint("数据类型编码");
        //add edit text in text input layout
        layout2.addView(editText2);
        //add two text input layout in linear layout
        linearLayout.addView(layout);
        linearLayout.addView(layout2);
        //create a alert dialog
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setPositiveButton("保存", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Map<String, String> map = new HashMap<>();
                        if (!editText.getText().toString().isEmpty() && !editText2.getText().toString().isEmpty()) {
                            map.put(Constants.KEY_DATA_NAME, editText.getText().toString());
                            map.put(Constants.KEY_DATA_CODE, editText2.getText().toString());
                            //save data type to data base by listener
                            mListener.addDataType(map);
                            //update list view
                            String text = editText.getText().toString() + ":0x" + editText2.getText().toString();
                            mDataTypesList.add(text);
                            mAdapter.notifyDataSetChanged();
                            Log.v(TAG, "result ok");
                        } else
                            Toast.makeText(getActivity(), "数据类型名称或编码不能为空！", Toast.LENGTH_SHORT).show();

                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setTitle("添加数据类型")
                //add custom view
                .setView(linearLayout).create();
        //show dialog
        dialog.show();
    }

    /**
     * a callback interface to listener data type changes
     */
    public interface OnDataTypeChangedListener {

        /**
         * add a data type
         * @param map a map contains data type name and code
         */
        void addDataType(Map<String, String> map);

        /**
         * remove a data type by name
         * @param name name of data type
         */
        void removeDataType(String name);

        /**
         * get all data types
         * @return a list
         */
        List<Map<String, String>> getDataType();
    }

    /**
     * adapter of list view to show all data types
     */
    private class DataTypeAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mDataTypesList.size();
        }

        @Override
        public Object getItem(int i) {
            return mDataTypesList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            final ViewHolder holder;
            if (view == null) {
                view = getActivity().getLayoutInflater().inflate(R.layout.item_data_type, null);
                holder = new ViewHolder();
                holder.mTextView = (TextView) view.findViewById(R.id.data_type_name);
                holder.mImageButton = (ImageButton) view.findViewById(R.id.data_type_delete);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            holder.mTextView.setText(mDataTypesList.get(i));
            if (i < 4)
                holder.mImageButton.setVisibility(View.GONE);
            holder.mImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String[] strings = holder.mTextView.getText().toString().split(":");
                    String name = strings[0];
                    mListener.removeDataType(name);
                    mDataTypesList.remove(i);
                    notifyDataSetChanged();
                }
            });

            return view;
        }

        /**
         * view holder
         */
        private class ViewHolder {
            /**
             * text view to show name of data type
             */
            private TextView mTextView;

            /**
             * image button to delete a data type
             */
            private ImageButton mImageButton;
        }
    }
}
