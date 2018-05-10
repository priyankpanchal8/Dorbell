package com.example.a.varnidoorbell;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class Settings extends AppCompatActivity {

    TextView txtuser;
    Button btnlogout;
    static String data;
    final DBHandler handler = new DBHandler(Settings.this);

    public boolean isConnected() {
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conMgr.getActiveNetworkInfo();
        if ((networkInfo != null) && (networkInfo.isConnected())) {
            //Toast.makeText(Login.this, "Connected..", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            //Toast.makeText(Login.this, "Please Check Network Connection...", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        txtuser = (TextView) findViewById(R.id.txtUser);
        btnlogout = (Button) findViewById(R.id.btnLogout);

        txtuser.setText(handler.getUSERNAME());

        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected()){
                    new MyAsyncTask().execute(handler.getUSERNAME().toString(), handler.getPASSWORD().toString());
                }
                else{
                    Toast.makeText(Settings.this, "Please Check Network Connection...", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

//        For Logout
    public class MyAsyncTask extends AsyncTask<String, Integer, Double> {

        ProgressDialog pre = new ProgressDialog(Settings.this);


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pre.setMessage("Logging Off...");
            pre.show();

        }

        @Override
        protected Double doInBackground(String... params) {

            URL url;
            HttpURLConnection urlConn;
            OutputStream printout;
            DataInputStream input;

            try {

                Login.msg = false;

                url = new URL("https://iot.espressif.cn/v1/user/logout/");
//
                urlConn = (HttpURLConnection) url.openConnection();
                urlConn.setDoInput(true);
                urlConn.setDoOutput(true);
                urlConn.setUseCaches(false);
                urlConn.setRequestMethod("POST");
                urlConn.setAllowUserInteraction(false);
//                urlConn.setConnectTimeout(3000);
//                urlConn.setReadTimeout(3000);
                urlConn.setRequestProperty("Content-Type", "text/html");
//                urlConn.addRequestProperty("Content-Type", "application/json");

//              Works but give me BAD Request in message
//                JSONObject jsonParam = new JSONObject();
//                jsonParam.put("remember", 1);
//                jsonParam.put("password", params[1]);
//                jsonParam.put("email", params[0]);
//                Log.e("request", urlConn.getRequestProperty("Content-Type").toString());
//                printout = new BufferedOutputStream(urlConn.getOutputStream());
//                printout.write((jsonParam.toString().getBytes()));
//                printout.flush();
//                printout.close();
                urlConn.connect();

//              Receive Msg from server
                InputStream in = new BufferedInputStream(urlConn.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder result = new StringBuilder();
                String line = "ab  ";
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                Log.e("code", String.valueOf(urlConn.getResponseCode()));

                Log.e("in", String.valueOf(urlConn.getResponseCode()));
                JSONObject jObj = new JSONObject(String.valueOf(result));

                int i = urlConn.getResponseCode();

                if (i == 200) {
                    Log.e("i value", String.valueOf(i));
//                    userValues.setJsonObject(jObj);
//                    JSONObject obj1 = jObj.getJSONObject("key");
//                    userValues.setKey(obj1.getString("token").toString());
//                    Login.User_Key = obj1.getString("token").toString();

                    Log.e("input", jObj.getString("status"));
                    Login.msg = true;

                    urlConn.disconnect();
                }
                else{
                    Log.e("i value 403", String.valueOf(i));
                    Toast.makeText(Settings.this, "Login failed \n Please check Username and Password", Toast.LENGTH_SHORT).show();
                    Login.msg = false;
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Double aDouble) {
            super.onPostExecute(aDouble);

//            Login l = new Login();

            if(Login.msg == true){
                stopService(new Intent(Settings.this,CustomService.class));
                handler.updateConnectivity(" ", " ");
                Login.d1.setDeviceName("");
                Login.d1.setDatastreamName("");
                Login.d1.setDeviceKey("");
                Login.d1.setUserKey("");
                Intent i = new Intent(Settings.this, Login.class);
                startActivity(i);
            }
            pre.dismiss();


        }
    }
}
