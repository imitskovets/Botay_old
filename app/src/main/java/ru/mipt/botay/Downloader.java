package ru.mipt.botay;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


public class Downloader extends Activity {

    ProgressBar pb;
    Dialog dialog;
    int downloadedSize = 0;
    int totalSize = 0;
    TextView cur_val;
    String file_src_person;
    String file_src = "http://80.85.86.242/";
    String file_dst ="/person.jpg";
    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_NFCID = "nfcId";
    public static final String APP_PREFERENCES_ID = "id";
    public static final String APP_PREFERENCES_STAT = "stat";
    SharedPreferences myUserPreferences;
    SharedPreferences.Editor editorMyPreferences;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloader);
//////////////////////////////
        myUserPreferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_WORLD_READABLE | MODE_WORLD_WRITEABLE);
        myUserPreferences = this.getSharedPreferences("ru.mipt.botay", Context.MODE_PRIVATE);
        editorMyPreferences = myUserPreferences.edit();

        file_src_person = file_src + myUserPreferences.getString(APP_PREFERENCES_NFCID, "") + ".jpg";
       // file_src_person = file_src + "ios.jpg";
        ////////////////////
        showProgress(file_src);
        new Thread(new Runnable() {
            public void run() {
                downloadFile(file_src_person,file_dst);
            }
        }).start();
    }

    void downloadFile(String source,String destonation){

        try {
            URL url = new URL(source);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 ( compatible ) ");
            urlConnection.setRequestProperty("Accept", "*/*");
            //urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.connect();

            FileOutputStream fileOutput = new FileOutputStream(String.valueOf(getApplicationContext().getFilesDir())+destonation);//file
            //Stream used for reading the data from the internet
            InputStream inputStream = urlConnection.getInputStream();

            //this is the total size of the file which we are downloading
            totalSize = urlConnection.getContentLength();

            //Toast.makeText(Downloader.this, String.valueOf(totalSize), Toast.LENGTH_LONG).show();

            runOnUiThread(new Runnable() {
                public void run() {
                    pb.setMax(totalSize);
                }
            });

            //create a buffer...
            byte[] buffer = new byte[1024];
            int bufferLength = 0;

            while ( (bufferLength = inputStream.read(buffer)) > 0 ) {
                fileOutput.write(buffer, 0, bufferLength);
                downloadedSize += bufferLength;
                // update the progressbar //
                runOnUiThread(new Runnable() {
                    public void run() {
                        pb.setProgress(downloadedSize);
                        float per = ((float)downloadedSize/totalSize) * 100;
                        cur_val.setText("Downloading " + downloadedSize + "KB / " + totalSize + "KB (" + (int)per + "%)" );
                    }
                });
            }
            //close the output stream when complete //
            fileOutput.close();
            runOnUiThread(new Runnable() {
                public void run() {
                    //  pb.dismiss(); // if you want close it..
                }
            });

        } catch (final MalformedURLException e) {
            showError("Error : MalformedURLException " + e);
            e.printStackTrace();
        } catch (final IOException e) {
            showError("Error : IOException " + e);
            e.printStackTrace();
        }
        catch (final Exception e) {
            showError("Error : Please check your internet connection " + e);
        }

        if (totalSize > 1000) {
            Intent intent = new Intent(Downloader.this, Login.class);
            startActivity(intent);
            this.finish();
        }
        else{
            editorMyPreferences.remove(APP_PREFERENCES_NFCID);
            editorMyPreferences.remove(APP_PREFERENCES_STAT);
            editorMyPreferences.apply();
            showError("Permission denied");
            this.finish();
        }
    }

    void showError(final String err){
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(Downloader.this, err, Toast.LENGTH_LONG).show();
            }
        });
    }

    void showProgress(String file_path){
        dialog = new Dialog(Downloader.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_downloader_prog);
        dialog.setTitle("Download Progress");

        TextView text = (TextView) dialog.findViewById(R.id.tv1);
        text.setText("Downloading file from ... " + file_path);
        cur_val = (TextView) dialog.findViewById(R.id.cur_pg_tv);
        cur_val.setText("Starting download...");
        dialog.show();

        pb = (ProgressBar)dialog.findViewById(R.id.progress_bar);
        pb.setProgress(0);
        pb.setProgressDrawable(getResources().getDrawable(R.drawable.green_progress));
    }
}

