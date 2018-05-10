package com.example.a.varnidoorbell;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.NotificationCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class UserHome extends Activity {

    public static TextView txtdata;
    static String msg = "";
    public static Handler updateConversationHandler;// = new Handler();
    public static GifView gifView;
    public static ImageView imgbell;
    static NotificationManager manager;
    public static Boolean change = false;

    public static void change_UI(String s1) {

        change = true;
        String s = UserHome.txtdata.getText().toString();
        if (s.equals(s1)) {
        }
        else {
            UserHome.txtdata.setText(s1);
            UserHome.imgbell.setVisibility(View.INVISIBLE);
            UserHome.gifView.setVisibility(View.VISIBLE);
        }
    }
    public static void reset_UI(String s1) {
        UserHome.imgbell.setVisibility(View.VISIBLE);
        UserHome.gifView.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onStop() {
        super.onStop();
//        this.finish();
        Intent i1 = new Intent(UserHome.this, CustomService.class);
        i1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i1.addFlags(Intent.FLAG_FROM_BACKGROUND);

        PendingIntent pi1 = PendingIntent.getService(UserHome.this, 1, i1, Intent.FLAG_ACTIVITY_NEW_TASK);

        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);

        manager.setRepeating(AlarmManager.RTC_WAKEUP, 5000, 5000, pi1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_userhome, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(UserHome.this, Settings.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        new MyAsyncTask1().execute("");

        updateConversationHandler = new Handler();
        gifView = (GifView) findViewById(R.id.gif_view);
        txtdata = (TextView) findViewById(R.id.txtData);
        imgbell = (ImageView) findViewById(R.id.imgBell);


//        updateConversationHandler = new Handler();
        txtdata.setText(Login.d1.getX()+"");

//        //alarm manager code
        Intent i1 = new Intent(UserHome.this, CustomService.class);
        i1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i1.addFlags(Intent.FLAG_FROM_BACKGROUND);

        PendingIntent pi1 = PendingIntent.getService(UserHome.this, 1, i1, Intent.FLAG_ACTIVITY_NEW_TASK);

        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);

        manager.setRepeating(AlarmManager.RTC_WAKEUP, 5000, 5000, pi1);

        notification_class n1 = new notification_class();
//        manager.cancel(pi1);
//        manager.set(AlarmManager.RTC,5000, pi1);

        txtdata.addTextChangedListener(n1);
    }

    //      For Get Data class
    public class MyAsyncTask1 extends AsyncTask<String, Integer, Double> {

        ProgressDialog pre = new ProgressDialog(UserHome.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pre.setMessage("Getting Devices..\nPlease Wait...");
            pre.show();

        }

        @Override
        protected Double doInBackground(String... params) {

            URL url1;
            HttpURLConnection urlConn1;
            OutputStream printout1;
            DataInputStream input1;
            JSONObject obj1;


//            /*get
//            user's
//            devices
//            */
            try {

                /////////     Get Product List     /////////
                url1 = new URL("https://iot.espressif.cn/v1/products/");
//
                urlConn1 = (HttpURLConnection) url1.openConnection();
                urlConn1.setDoInput(true);
//                urlConn1.setDoOutput(true);
                urlConn1.setUseCaches(false);
                urlConn1.setRequestMethod("GET");
                urlConn1.setAllowUserInteraction(false);
                urlConn1.addRequestProperty("Authorization", "token " + Login.d1.getUserKey());
                urlConn1.addRequestProperty("Content-Type", "application/json");

//              Receive Msg from server
                InputStream inp = new BufferedInputStream(urlConn1.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(inp));
                StringBuilder result = new StringBuilder();
                String line = "ab  ";
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                JSONObject jObj = new JSONObject(String.valueOf(result));
                JSONArray a1 = jObj.getJSONArray("products");

                for(int i=0;i<a1.length();i++){
                    obj1 = a1.getJSONObject(i);
                    String s = obj1.getString("name");
                    Log.e("product Name", s);
//      /*/*/*/*/*/*/*/*/*/*/*/*/*      checking for product DOORBELL            */*/*/*/*/*/*/*/*/*/*/*/*/
                    if(s.equals("doorbell"))
                    {
                        msg = obj1.getString("id");
                        Login.d1.setProduct_id(Long.parseLong(msg));

                        Log.e("got doorbell at",i+"");
                        break;
                    }
                }
//                JSONObject obj2 = obj1.getJSONObject("key");

                Login.d1.setProduct_id(Long.parseLong(msg));
//                Log.e("device Key :", Login.d1.getDatastreamName());

                /////////     Get Devide List     /////////

                url1 = new URL("http://iot.espressif.cn/v1/products/"+ Login.d1.getProduct_id() +"/devices/");

                Log.e("device List URL :", url1+"");

                urlConn1 = (HttpURLConnection) url1.openConnection();
                urlConn1.setDoInput(true);
//                urlConn1.setDoOutput(true);
                urlConn1.setUseCaches(false);
                urlConn1.setRequestMethod("GET");
                urlConn1.setAllowUserInteraction(false);
                urlConn1.addRequestProperty("Authorization", "token " + Login.d1.getUserKey());
                urlConn1.addRequestProperty("Content-Type", "application/json");

//              Receive Msg from server
                inp = new BufferedInputStream(urlConn1.getInputStream());
                reader = new BufferedReader(new InputStreamReader(inp));
                result = new StringBuilder();
                line = "ab  ";
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                jObj = new JSONObject(String.valueOf(result));
                Log.e("device List :", jObj+"");
//                Log.e("userKey : ", Login.d1.getUserKey());
//                msg = jObj.getString("devices");
                a1 = jObj.getJSONArray("devices");
                for(int i=0;i<a1.length();i++){
                    obj1 = a1.getJSONObject(i);
                    String s = obj1.getString("name");
                    Log.e("device Name", s);
//      /*/*/*/*/*/*/*/*/*/*/*/*/*      checking for product HOME            */*/*/*/*/*/*/*/*/*/*/*/*/
                    if(s.equals("d1"))
                    {
                        msg = obj1.getString("id");
                        Login.d1.setDevice_id(Long.parseLong(msg));
                        break;
                    }
                }
//                Log.e("device id :", Login.d1.getDevice_id() + "");

/////////////////     	for list device keys        /////////////////////

                url1 = new URL("https://iot.espressif.cn/v1/keys/?scope=device&device_id="+ Login.d1.getDevice_id() +"");
//
                urlConn1 = (HttpURLConnection) url1.openConnection();
                urlConn1.setDoInput(true);
//                urlConn1.setDoOutput(true);
                urlConn1.setUseCaches(false);
                urlConn1.setRequestMethod("GET");
                urlConn1.setAllowUserInteraction(false);
                urlConn1.addRequestProperty("Authorization", "token " + Login.d1.getUserKey());
                urlConn1.addRequestProperty("Content-Type", "application/json");

//              Receive Msg from server
                inp = new BufferedInputStream(urlConn1.getInputStream());
                reader = new BufferedReader(new InputStreamReader(inp));
                result = new StringBuilder();
                line = "ab  ";
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                jObj = new JSONObject(String.valueOf(result));
//                Log.e("userKey : ", Login.d1.getUserKey());
//                msg = jObj.getString("devices");
                a1 = jObj.getJSONArray("keys");
                obj1 = a1.getJSONObject(0);
//                obj2 = obj1.getJSONObject("key");

                msg = obj1.getString("token");
                Login.d1.setDeviceKey(msg);
                Log.e("device Key :", Login.d1.getDevice_id() + "");


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NullPointerException e){
                e.printStackTrace();
            }

            return null;
        }



        @Override
        protected void onPostExecute(Double aDouble) {
            super.onPostExecute(aDouble);

//            Login l = new Login();
//            l.change(msg);
            txtdata.setText(msg);
            pre.dismiss();
            new MyAsyncTask2().execute("");
        }
    }

    public class MyAsyncTask2 extends AsyncTask<String, Integer, Double> {

        ProgressDialog pre = new ProgressDialog(UserHome.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pre.setMessage("Creating Connections..\nPlease Wait...");
            pre.show();

        }

        @Override
        protected Double doInBackground(String... params) {

            URL url1;
            HttpURLConnection urlConn1;
            OutputStream printout1;
            DataInputStream input1;


//            /*get
//            datastream
//            of
//            */device

            try {

                url1 = new URL("https://iot.espressif.cn/v1/products/" + Login.d1.getProduct_id() + "/datastreamTmpls/");
//
                urlConn1 = (HttpURLConnection) url1.openConnection();
                urlConn1.setDoInput(true);
//                urlConn1.setDoOutput(true);
                urlConn1.setUseCaches(false);
                urlConn1.setRequestMethod("GET");
                urlConn1.setAllowUserInteraction(false);
                urlConn1.addRequestProperty("Authorization", "token " + Login.d1.getUserKey());
                urlConn1.addRequestProperty("Content-Type", "application/json");

//              Receive Msg from server
                InputStream inp = new BufferedInputStream(urlConn1.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(inp));
                StringBuilder result = new StringBuilder();
                String line = "ab  ";
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                JSONObject jObj = new JSONObject(String.valueOf(result));
                JSONArray a1 = jObj.getJSONArray("datastreamTmpls");
                JSONObject obj1 = a1.getJSONObject(0);
//                JSONObject obj2 = obj1.getJSONObject("key");

                msg = obj1.getString("name");
                Login.d1.setDatastreamName(msg);
                Log.e("device Key :", Login.d1.getDatastreamName());

                msg = obj1.getString("id");
                Login.d1.setDatastream_id(Long.parseLong(msg));
                Log.e("device Key :", Login.d1.getDatastream_id()+"");


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NullPointerException e){
                e.printStackTrace();
            }


            return null;
        }



        @Override
        protected void onPostExecute(Double aDouble) {
            super.onPostExecute(aDouble);

//            Login l = new Login();
//            l.change(msg);
            txtdata.setText(msg);
            pre.dismiss();
            new MyAsyncTask3().execute("");
        }
    }

    public class MyAsyncTask3 extends AsyncTask<String, Integer, Double> {

        ProgressDialog pre = new ProgressDialog(UserHome.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pre.setMessage("Fetching Curent Values..\nPlease Wait...");
            pre.show();

        }

        @Override
        protected Double doInBackground(String... params) {

            URL url1;
            HttpURLConnection urlConn1;
            OutputStream printout1;
            DataInputStream input1;


//            /*get
//            datapoint
//            value*/

            try {

                url1 = new URL("https://iot.espressif.cn/v1/datastreams/" + Login.d1.getDatastreamName() + "/datapoint/");
//
                Log.e("URL datapoint", url1+"");

                urlConn1 = (HttpURLConnection) url1.openConnection();
                urlConn1.setDoInput(true);
//                urlConn1.setDoOutput(true);
                urlConn1.setUseCaches(false);
                urlConn1.setRequestMethod("GET");
                urlConn1.setAllowUserInteraction(false);
                urlConn1.addRequestProperty("Authorization", "token " + Login.d1.getDeviceKey());
                urlConn1.addRequestProperty("Content-Type", "application/json");

//              Receive Msg from server
                InputStream inp = new BufferedInputStream(urlConn1.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(inp));
                StringBuilder result = new StringBuilder();
                String line = "ab  ";
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }


                JSONObject jObj = new JSONObject(String.valueOf(result));

                JSONArray a1 = jObj.getJSONArray("datapoints");
                JSONObject obj1 = a1.getJSONObject(0);
                msg = obj1.getString("x")+"";

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NullPointerException e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Double aDouble) {
            super.onPostExecute(aDouble);

//            Login l = new Login();
//            l.change(msg);
            txtdata.setText(msg);
            pre.dismiss();
            try {
                finalize();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }


    public class notification_class implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            try {
                if (change == true) {

                    manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    Intent i = new Intent(UserHome.this, UserHome.class);

                    PendingIntent pi = PendingIntent.getActivity(UserHome.this, 3, i,
                            Intent.FLAG_ACTIVITY_NEW_TASK);

                    Notification notification = new NotificationCompat.Builder(UserHome.this)
                            .setAutoCancel(true).setContentText("Time : " + Login.d1.getTime())
                            .setContentTitle("Doorbell Notification").setSmallIcon(R.drawable.doorbell_vr1_small)
                            .setContentIntent(pi)
                            .build();

                    notification.defaults = Notification.DEFAULT_SOUND;

                    manager.notify(3, notification);
                    change = false;
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

}
