package ru.mipt.botay;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Login extends Activity {
    String strId;
    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_NFCID = "nfcId";
    public static final String APP_PREFERENCES_ID = "id";
    public static final String APP_PREFERENCES_STAT = "stat";
    String file_src = "http://80.85.86.242/";
    String addres = "http://80.85.86.242/ios.jpg";
    SharedPreferences myUserPreferences;
    SharedPreferences.Editor editorMyPreferences;

    // list of NFC technologies detected:
    private final String[][] techList = new String[][] {
            new String[] {
                    NfcA.class.getName(),
                    NfcB.class.getName(),
                    NfcF.class.getName(),
                    NfcV.class.getName(),
                    IsoDep.class.getName(),
                    MifareClassic.class.getName(),
                    MifareUltralight.class.getName(), Ndef.class.getName()
            }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        myUserPreferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_WORLD_READABLE | MODE_WORLD_WRITEABLE);
        myUserPreferences = this.getSharedPreferences("ru.mipt.botay", Context.MODE_PRIVATE);
        editorMyPreferences = myUserPreferences.edit();

        TextView textView = (TextView)findViewById(R.id.textViewNFCID);
        textView.setText(myUserPreferences.getString(APP_PREFERENCES_NFCID, ""));

        ImageView imageView = (ImageView) findViewById(R.id.imageViewLogo);
        if(myUserPreferences.contains(APP_PREFERENCES_NFCID)) {
            File personFile = new  File(String.valueOf(getApplicationContext().getFilesDir())+"/person.jpg");
            Bitmap myBitmap = BitmapFactory.decodeFile(personFile.getAbsolutePath());
            imageView.setImageBitmap(myBitmap);
        }
        else{
            imageView.setImageResource(R.drawable.eng_text);
        }
        if(myUserPreferences.contains(APP_PREFERENCES_NFCID) & myUserPreferences.contains(APP_PREFERENCES_STAT)) {
            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
            this.finish();
        }

        editorMyPreferences.putString(APP_PREFERENCES_STAT,"contain");
        editorMyPreferences.apply();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // creating pending intent:
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        // creating intent receiver for NFC events:
        IntentFilter filter = new IntentFilter();
        filter.addAction(NfcAdapter.ACTION_TAG_DISCOVERED);
        filter.addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filter.addAction(NfcAdapter.ACTION_TECH_DISCOVERED);
        // enabling foreground dispatch for getting intent from NFC event:
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, new IntentFilter[]{filter}, this.techList);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // disabling foreground dispatch:
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (intent.getAction().equals(NfcAdapter.ACTION_TAG_DISCOVERED)) {

            strId = ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID));
            Toast toast = Toast.makeText(getApplicationContext(),"New tag discovered uid = " +
                    String.valueOf(strId), Toast.LENGTH_SHORT);
            toast.show();

            this.editorMyPreferences.putString(APP_PREFERENCES_NFCID, strId);
            this.editorMyPreferences.apply();
            //myRestart();
        }
    }


    private String ByteArrayToHexString(byte [] inArray) {
        int i, j, in;
        String [] hex = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};
        String out= "";

        for(j = 0 ; j < inArray.length ; ++j)
        {
            in = (int) inArray[j] & 0xff;
            i = (in >> 4) & 0x0f;
            out += hex[i];
            i = in & 0x0f;
            out += hex[i];
        }
        return out;
    }

    public void onButtonLogInClick(View view) {
        if(myUserPreferences.contains(APP_PREFERENCES_NFCID)) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Login in procces", Toast.LENGTH_SHORT);
            toast.show();
            Intent intent = new Intent(Login.this, Downloader.class);
            startActivity(intent);
        }
    }

    public void onButtonLogOutClick(View view) {

        editorMyPreferences.clear();
        editorMyPreferences.apply();
        File personFile = new  File(String.valueOf(getApplicationContext().getFilesDir())+"/person.jpg");
        personFile.delete();
        myRestart();
    }
    public void myRestart(){

            Intent intent = new Intent(Login.this, Login.class);
            startActivity(intent);
            this.finish();
    }

    public void onButtonBotayClick(View view) {
        EditText editLogin = (EditText) findViewById(R.id.editLOGIN);
        EditText editPass = (EditText) findViewById(R.id.editPASSWORD);
        if ( editLogin.getText().toString().equals("admin") & editPass.getText().toString().equals("admin")){
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Hello Admin", Toast.LENGTH_SHORT);
            toast.show();
            this.editorMyPreferences.putString(APP_PREFERENCES_NFCID, "E3865FC4");
            this.editorMyPreferences.putString(APP_PREFERENCES_STAT, "ADMIN");
            this.editorMyPreferences.apply();
            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
            this.finish();
        }
        Intent intent = new Intent(Login.this, Login.class);
        startActivity(intent);
        this.finish();
    }
}
