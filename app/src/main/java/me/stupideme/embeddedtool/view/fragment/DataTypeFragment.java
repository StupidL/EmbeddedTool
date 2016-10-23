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


public class DataTypeFragment extends Fragment {

    private static final String TAG = DataTypeFragment.class.getSimpleName();
    private OnDataTypeChangedListener mListener;
    private List<String> mDataTypesList;
    private DataTypeAdapter mAdapter;

    public DataTypeFragment() {
        mDataTypesList = new ArrayList<>();
        mAdapter = new DataTypeAdapter();
    }

    public static DataTypeFragment newInstance() {
        return new DataTypeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
        mListener = (OnDataTypeChangedListener) getActivity();
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
        mDataTypesList.clear();
        setTypes(list);
    }

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
        mAdapter.notifyDataSetChanged();
    }

    public void showDialog() {
        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        TextInputLayout layout = new TextInputLayout(getActivity());
        layout.setPadding(32, 32, 32, 0);
        final TextInputEditText editText = new TextInputEditText(getActivity());
        editText.setHint("数据类型名称");
        layout.addView(editText);
        TextInputLayout layout2 = new TextInputLayout(getActivity());
        layout2.setPadding(32, 32, 32, 0);
        final TextInputEditText editText2 = new TextInputEditText(getActivity());
        editText2.setHint("数据类型编码");
        layout2.addView(editText2);
        linearLayout.addView(layout);
        linearLayout.addView(layout2);
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setPositiveButton("保存", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Map<String, String> map = new HashMap<>();
                        if (!editText.getText().toString().isEmpty() && !editText2.getText().toString().isEmpty()) {
                            map.put(Constants.KEY_DATA_NAME, editText.getText().toString());
                            map.put(Constants.KEY_DATA_CODE, editText2.getText().toString());
                            mListener.addDataType(map);
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
                .setView(linearLayout).create();
        dialog.show();
    }

    public interface OnDataTypeChangedListener {
        void addDataType(Map<String, String> map);

        void removeDataType(String name);

        List<Map<String, String>> getDataType();
    }

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
            if (i < 3)
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

        private class ViewHolder {
            private TextView mTextView;
            private ImageButton mImageButton;
        }
    }
}
