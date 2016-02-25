package ru.mipt.botay;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

public class Tm1 extends ActionBarActivity {
    public ButtonTaskAdapter adapter;
    public GridView.OnItemClickListener gridviewOnItemClickListener = new GridView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
            Intent i = new Intent(getApplicationContext(), FullImageActivity.class);
            i.putExtra("id", position);
            startActivity(i);
        }
    };
    private GridView.OnItemLongClickListener gridviewOnItemLongClickListener = new GridView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            mSubjectProgress[position] = 1;
            adapter.notifyDataSetInvalidated();
            return true;
        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);

        GridView gridview = (GridView) findViewById(R.id.gridView);
        adapter = new ButtonTaskAdapter(this, mSubjectProgress,mTaskName);
        gridview.setAdapter(adapter);
        gridview.setOnItemClickListener(gridviewOnItemClickListener);
        gridview.setOnItemLongClickListener(gridviewOnItemLongClickListener);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_login) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public Integer[] mTaskImg = {R.drawable.tm15_1, R.drawable.tm15_9, R.drawable.tm15_14,
            R.drawable.tm15_22, R.drawable.tm16_12, R.drawable.tm16_40,
            R.drawable.tm16_64, R.drawable.tm16_66,R.drawable.tm17_4, R.drawable.tm17_11_d,
            R.drawable.tm17_23,R.drawable.tm18_34, R.drawable.tm18_42, R.drawable.tm18_43_d
    };
    public String[] mTaskName = {"15.1","15.9","15.14","15.22","16.12","16.40","16.64","16.66","17.4","17.11.d","17.23","18.34","18.42","18.43.d"
    };
    public Integer[] mSubjectProgress = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,0, 0, 0
    };
}
