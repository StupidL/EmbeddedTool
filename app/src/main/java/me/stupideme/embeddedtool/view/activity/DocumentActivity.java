package me.stupideme.embeddedtool.view.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import me.stupideme.embeddedtool.R;

/**
 * Created by StupidL on 2016/9/30.
 */
public class DocumentActivity extends AppCompatActivity {

//    private static final String TAG = DocumentActivity.class.getSimpleName();
//    private List<String> mDocsContentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("说明文档");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocumentActivity.super.onBackPressed();
            }
        });

        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(DocumentActivity.this);
                dialog.setTitle("关于");
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.dialog_about);
                dialog.show();
            }
        });

//        mDocsContentId = new ArrayList<>();
//        mDocsContentId.add(getResources().getString(R.string.large_text_1));
//        mDocsContentId.add(getResources().getString(R.string.large_text_2));
//        mDocsContentId.add(getResources().getString(R.string.large_text_3));
//        mDocsContentId.add(getResources().getString(R.string.large_text_4));
//        mDocsContentId.add(getResources().getString(R.string.large_text_5));
//        mDocsContentId.add(getResources().getString(R.string.large_text_6));
//        mDocsContentId.add(getResources().getString(R.string.large_text_7));
//        mDocsContentId.add(getResources().getString(R.string.large_text_8));
//        mDocsContentId.add(getResources().getString(R.string.large_text_9));

//        RecyclerViewAdapter adapter = new RecyclerViewAdapter();

//        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.docs_recycler_view);
//        recyclerView.setAdapter(adapter);
    }

//    private class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
//
//        @Override
//        public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_document, null);
//            return new ViewHolder(view);
//        }
//
//        @Override
//        public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, int position) {
//            String string = mDocsContentId.get(position);
//            String[] strings = string.split("\n\n");
//            String title = strings[0];
//            Log.v(TAG, title);
//            String content = strings[1];
//            Log.v(TAG, content);
//            holder.title.setText(title);
//            holder.content.setText(content);
//        }
//
//        @Override
//        public int getItemCount() {
//            return mDocsContentId.size();
//        }
//
//        class ViewHolder extends RecyclerView.ViewHolder {
//
//            private TextView title;
//            private TextView content;
//
//            ViewHolder(View itemView) {
//                super(itemView);
//                title = (TextView) itemView.findViewById(R.id.title);
//                content = (TextView) itemView.findViewById(R.id.content);
//            }
//        }
//    }
}
