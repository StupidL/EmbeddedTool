package me.stupideme.embeddedtool.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import me.stupideme.embeddedtool.Constants;
import me.stupideme.embeddedtool.R;
import me.stupideme.embeddedtool.db.DBManager;

public class TemplateActivity extends AppCompatActivity {

    //debug
    private static final String TAG = TemplateActivity.class.getSimpleName();
    /**
     * a list contains all templates' names
     */
    private List<String> mTemplateNames;

    /**
     * data base manager to query or delete templates
     */
    private DBManager mManager;

    /**
     * a list view adapter
     */
    private MyAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.activity_lable_select_template);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TemplateActivity.super.onBackPressed();
            }
        });

        mTemplateNames = new ArrayList<>();

        mManager = DBManager.getInstance();

        ListView mListView = (ListView) findViewById(R.id.template_list);
        //fill list view
        Cursor cursor = mManager.queryAllTemplateName();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String name = cursor.getString(cursor.getColumnIndex(Constants.TEMPLATE_NAME));
            mTemplateNames.add(name);
            Log.v(TAG, name);
            cursor.moveToNext();
        }

        TextView empty = (TextView) findViewById(R.id.empty_view);
        mListView.setEmptyView(empty);
        mAdapter = new MyAdapter();
        mListView.setAdapter(mAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_template, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            //clear all templates
            case R.id.action_clear:
                mTemplateNames.clear();
                mAdapter.notifyDataSetChanged();
                mManager.deleteAllTemplates();
                break;
            case R.id.action_settings:
                //do nothing
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mTemplateNames.clear();
        mAdapter.notifyDataSetChanged();
    }

    /**
     * a adapter of list view
     */
    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mTemplateNames.size();
        }

        @Override
        public Object getItem(int i) {
            return mTemplateNames.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            final ViewHolder holder;
            if (view == null) {
                view = getLayoutInflater().inflate(R.layout.item_tamplate, null);
                holder = new ViewHolder();
                holder.mName = (TextView) view.findViewById(R.id.template_name);
                holder.mDelete = (ImageButton) view.findViewById(R.id.template_delete);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            final String name = mTemplateNames.get(i);
            //set template name from database
            holder.mName.setText(name);
            //delete a template
            holder.mDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mManager.deleteTemplate(name);
                    // remove from list view
                    mTemplateNames.remove(i);
                    notifyDataSetChanged();
                    // delete from database
                    // show toast
                    Toast.makeText(TemplateActivity.this,
                            "已删除模板 \"" + name + " \"", Toast.LENGTH_SHORT).show();
                }
            });

            //select a template and return to MainActivity
            holder.mName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.putExtra(Constants.TEMPLATE_NAME, holder.mName.getText().toString());
                    setResult(Activity.RESULT_OK, intent);
                    TemplateActivity.this.finish();
                }
            });

            return view;
        }

        /**
         * view holder
         */
        private class ViewHolder {
            /**
             * a text view to show template's name
             */
            TextView mName;

            /**
             * a image button to delete a template
             */
            ImageButton mDelete;
        }
    }
}
