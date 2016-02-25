package ru.mipt.botay;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {
    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_NFCID = "nfcId";
    public static final String APP_PREFERENCES_ID = "id";
    public static final String APP_PREFERENCES_STAT = "stat";
    SharedPreferences myUserPreferences;
    SharedPreferences.Editor editorMyPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myUserPreferences = this.getSharedPreferences("ru.mipt.botay", Context.MODE_PRIVATE);
        editorMyPreferences = myUserPreferences.edit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_login) {
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
            editorMyPreferences.remove(APP_PREFERENCES_STAT);
            editorMyPreferences.apply();
            this.finish();
            return true;
        }
        if (id == R.id.action_exit) {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onSubjectButton1Click(View view) {
        Intent intent = new Intent(MainActivity.this, Tm1.class);
        startActivity(intent);
    }

    public void onSubjectButton2Click(View view) {
        Intent intent = new Intent(MainActivity.this, Tm2.class);
        startActivity(intent);
    }

    public void onButtonClick(View view) {
        Intent intent = new Intent(MainActivity.this, About.class);
        startActivity(intent);
        this.finish();
    }
}
