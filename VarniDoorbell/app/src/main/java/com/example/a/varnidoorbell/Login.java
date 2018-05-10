package com.example.a.varnidoorbell;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
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

public class Login extends AppCompatActivity {

    static Data_for_User d1;

    Button btnlogin, btnforgot, btnregister;
    EditText edtusername,edtpassword;
    static Boolean msg = false;
    static String User_Key;

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
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        d1 = new Data_for_User();

        final DBHandler handler = new DBHandler(Login.this);

        edtusername = (EditText) findViewById(R.id.edtUsername);
        edtpassword = (EditText) findViewById(R.id.edtPassword);
        btnlogin = (Button) findViewById(R.id.btnLogin);
        btnforgot = (Button) findViewById(R.id.btnForgot);
        btnregister = (Button) findViewById(R.id.btnSignIn);

        edtusername.setText(handler.getUSERNAME().toString());
        edtpassword.setText(handler.getPASSWORD().toString());

        if(handler.getUSERNAME().toString().equals(" ")){
            edtusername.setText("");
            edtpassword.setText("");
        }
        else {
            if (((edtusername.getText().toString()) == "") || ((edtpassword.getText().toString()) == "")) {
//            Toast.makeText(Login.this, "Please insert Username and Password", Toast.LENGTH_SHORT).show();
                edtusername.setText("");
                edtpassword.setText("");
                if ((edtusername.getText().toString() == null) || (edtpassword.getText().toString() == null)) {
                }
            } else {
                new MyAsyncTask().execute(handler.getUSERNAME().toString(), handler.getPASSWORD().toString());
            }
        }

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String s1 = edtusername.getText().toString();
                String s2= edtpassword.getText().toString();

                if (isConnected()) {
                    if ((s1.equals("")) || (s1.equals(" ")) || (s2.equals("")) || (s2.equals(" "))) {
                        Toast.makeText(Login.this, "Please insert Username and Password", Toast.LENGTH_SHORT).show();
                    } else {
                        handler.updateConnectivity(edtusername.getText().toString(), edtpassword.getText().toString());
//                            Log.e("uname getting", handler.getUserName().toString());
//                            Log.e("Pass getting", handler.getPASSWORD().toString());
                        new MyAsyncTask().execute(handler.getUSERNAME().toString(), handler.getPASSWORD().toString());
                    }
                } else {
                    Toast.makeText(Login.this, "Please Check Network Connection...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnforgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, Forgot_Password.class);
                startActivity(i);
            }
        });

        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this,Registration.class);
                startActivity(i);
            }
        });

    }
//        });

    //    For Login
    public class MyAsyncTask extends AsyncTask<String, Integer, Double> {

        ProgressDialog pre = new ProgressDialog(Login.this);


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pre.setMessage("Logging In...");
            pre.show();

        }

        @Override
        protected Double doInBackground(String... params) {

            URL url;
            HttpURLConnection urlConn;
            OutputStream printout;
            DataInputStream input;

            try {

                url = new URL("http://iot.espressif.cn/v1/user/login/");
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
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("remember", 1);
                jsonParam.put("password", params[1]);
                jsonParam.put("email", params[0]);
                Log.e("request", urlConn.getRequestProperty("Content-Type").toString());
                printout = new BufferedOutputStream(urlConn.getOutputStream());
                printout.write((jsonParam.toString().getBytes()));
                printout.flush();
                printout.close();
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
                    JSONObject obj1 = jObj.getJSONObject("key");
//                    userValues.setKey(obj1.getString("token").toString());
                    Login.User_Key = obj1.getString("token").toString();

                    d1.setUserKey(Login.User_Key);

                    Log.e("input", jObj.getString("status"));
                    Login.msg = true;


//          /*/*/*/*/*/*/*/*/*/*/*/*/*/*          Check product DOORBELL         */*/*/*/*/*/*/*/*/*/*/*/*/*/

                    url = new URL("https://iot.espressif.cn/v1/products/");
//
                    urlConn = (HttpURLConnection) url.openConnection();
                    urlConn.setDoInput(true);
//                urlConn1.setDoOutput(true);
                    urlConn.setUseCaches(false);
                    urlConn.setRequestMethod("GET");
                    urlConn.setAllowUserInteraction(false);
                    urlConn.addRequestProperty("Authorization", "token " + d1.getUserKey());
                    urlConn.addRequestProperty("Content-Type", "application/json");

//              Receive Msg from server
                    InputStream inp = new BufferedInputStream(urlConn.getInputStream());
                    reader = new BufferedReader(new InputStreamReader(inp));
                    result = new StringBuilder();
                    line = "ab  ";
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    jObj = new JSONObject(String.valueOf(result));
                    JSONArray a1 = jObj.getJSONArray("products");

                    for (i = 0; i < a1.length(); i++) {
                        obj1 = a1.getJSONObject(i);
                        String s = obj1.getString("name");
                        Log.e("product Name", s);


//      /*/*/*/*/*/*/*/*/*/*/*/*/*      checking for product DOORBELL            */*/*/*/*/*/*/*/*/*/*/*/*/
                        if (s.equals("doorbell")) {
                            Log.e("got doorbell at", i + "");
                            break;
                        }

                    }

                    if (i < a1.length()) {
                        String msg = obj1.getString("id");
                        d1.setProduct_id(Long.parseLong(msg));
                    }

//     /*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*        adding product DOORBELL           */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/
                    else {
                        url = new URL("https://iot.espressif.cn/v1/products");
//
                        urlConn = (HttpURLConnection) url.openConnection();
                        urlConn.setDoInput(true);
                        urlConn.setDoOutput(true);
                        urlConn.setUseCaches(false);
                        urlConn.setRequestMethod("POST");
                        urlConn.setAllowUserInteraction(false);
                        urlConn.addRequestProperty("Authorization", "token " + d1.getUserKey());
                        urlConn.addRequestProperty("Content-Type", "application/json");

                        jsonParam = new JSONObject();
                        jsonParam.put("name", "doorbell");
                        jsonParam.put("is_private", 1);
                        jsonParam.put("ptype", 27388);
                        jsonParam.put("status", 1);

                        JSONArray in1 = new JSONArray();
                        in1.put(jsonParam);

                        JSONObject o1 = new JSONObject();
                        o1.put("products", in1);

                        Log.e("request", urlConn.getRequestProperty("Content-Type").toString());
                        printout = new BufferedOutputStream(urlConn.getOutputStream());
                        printout.write((o1.toString().getBytes()));
                        printout.flush();
                        printout.close();
                        urlConn.connect();

//              Receive Msg from server
                        inp = new BufferedInputStream(urlConn.getInputStream());
                        reader = new BufferedReader(new InputStreamReader(inp));
                        result = new StringBuilder();
                        line = "ab  ";
                        while ((line = reader.readLine()) != null) {
                            result.append(line);
                        }

                        jObj = new JSONObject(String.valueOf(result));
                        a1 = jObj.getJSONArray("products");

                        obj1 = a1.getJSONObject(0);
//                JSONObject obj2 = obj1.getJSONObject("key");

                        String str = obj1.getString("id");
//                            UserHome.d1.setDatastreamName(msg);
                        d1.setProduct_id(Long.parseLong(str));
                        Log.e("Product Id", str + "");

//  /*/*/*/*/*/*/*/*/*/*/*/*          Creating Device               */*/*/*/*/*/*/*/*/*/*/*/

                        url = new URL("https://iot.espressif.cn/v1/products/" + d1.getProduct_id() + "/devices/");
//
                        urlConn = (HttpURLConnection) url.openConnection();
                        urlConn.setDoInput(true);
                        urlConn.setDoOutput(true);
                        urlConn.setUseCaches(false);
                        urlConn.setRequestMethod("POST");
                        urlConn.setAllowUserInteraction(false);
                        urlConn.addRequestProperty("Authorization", "token " + d1.getUserKey());
                        urlConn.addRequestProperty("Content-Type", "application/json");

                        jsonParam = new JSONObject();
                        jsonParam.put("name", "d1");
                        jsonParam.put("is_private", 1);
                        jsonParam.put("status", 1);

                        in1 = new JSONArray();
                        in1.put(jsonParam);

                        o1 = new JSONObject();
                        o1.put("devices", in1);

                        Log.e("request", urlConn.getRequestProperty("Content-Type").toString());
                        printout = new BufferedOutputStream(urlConn.getOutputStream());
                        printout.write((o1.toString().getBytes()));
                        printout.flush();
                        printout.close();
                        urlConn.connect();

//              Receive Msg from server
                        inp = new BufferedInputStream(urlConn.getInputStream());
                        reader = new BufferedReader(new InputStreamReader(inp));
                        result = new StringBuilder();
                        line = "ab  ";
                        while ((line = reader.readLine()) != null) {
                            result.append(line);
                        }

                        jObj = new JSONObject(String.valueOf(result));
                        a1 = jObj.getJSONArray("devices");

                        obj1 = a1.getJSONObject(0);
                        str = obj1.getString("id");
//                      UserHome.d1.setDatastreamName(msg);
                        d1.setDevice_id(Long.parseLong(str));
                        Log.e("device Id", str + "");

                        JSONObject obj2 = obj1.getJSONObject("key");
                        str = obj2.getString("token");
                        d1.setDeviceKey(str);
                        Log.e("device Key", str);

//  /*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*             Creating New Datastream             */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/

                        url = new URL("https://iot.espressif.cn/v1/products/" + d1.getProduct_id() + "/datastreamTmpls/");
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
                        urlConn.setRequestProperty("Authorization", "token " + d1.getUserKey());
//                urlConn.addRequestProperty("Content-Type", "application/json");

//              Works but give me BAD Request in message
                        jsonParam = new JSONObject();
                        jsonParam.put("name", "cpu");
                        jsonParam.put("description", "");
                        jsonParam.put("dimension", 1);
                        jsonParam.put("tags", "");
                        jsonParam.put("unit", "");
                        jsonParam.put("symbol", "%");

                        a1 = new JSONArray();
                        a1.put(jsonParam);
                        obj1 = new JSONObject();
                        obj1.put("datastreamTmpls", a1);
                        d1.setDatastreamName("cpu");

                        Log.e("request", urlConn.getRequestProperty("Content-Type").toString());
                        printout = new BufferedOutputStream(urlConn.getOutputStream());
                        printout.write((obj1.toString().getBytes()));
                        printout.flush();
                        printout.close();
                        urlConn.connect();

//              Receive Msg from server
                        in = new BufferedInputStream(urlConn.getInputStream());
                        reader = new BufferedReader(new InputStreamReader(in));
                        result = new StringBuilder();
                        line = "ab  ";
                        while ((line = reader.readLine()) != null) {
                            result.append(line);
                        }

                        jObj = new JSONObject(String.valueOf(result));
                        Log.e("Datastream value", jObj + "");


//   /*/*/*/*/*/*/*/*/*/*/*/*                 Initializing datapoint                      */*/*/*/*/*/*/*/*/*/*/*/

                            url = new URL("https://iot.espressif.cn/v1/datastreams/" + d1.getDatastreamName() + "/datapoints/");
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
                            urlConn.setRequestProperty("Authorization", "token " + d1.getDeviceKey());
//                urlConn.addRequestProperty("Content-Type", "application/json");

//              Works but give me BAD Request in message
                            jsonParam = new JSONObject();
                            double i1 = 1.0;
                            jsonParam.put("x", i1);

                            a1 = new JSONArray();
                            a1.put(jsonParam);
                            obj1 = new JSONObject();
                            obj1.put("datapoints", a1);

                            Log.e("request", urlConn.getRequestProperty("Content-Type").toString());
                            printout = new BufferedOutputStream(urlConn.getOutputStream());
                            printout.write((obj1.toString().getBytes()));
                            printout.flush();
                            printout.close();
                            urlConn.connect();

//              Receive Msg from server
                            in = new BufferedInputStream(urlConn.getInputStream());
                            reader = new BufferedReader(new InputStreamReader(in));
                            result = new StringBuilder();
                            line = "ab  ";
                            while ((line = reader.readLine()) != null) {
                                result.append(line);
                            }

                            jObj = new JSONObject(String.valueOf(result));

                            Log.e("Datapoint value", jObj + "");

                    }

                    urlConn.disconnect();
                }
                else{
                    Log.e("i value 403", String.valueOf(i));
//                    Toast.makeText(Login.this, "Login failed \n Please check Username and Password", Toast.LENGTH_SHORT).show();
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
                Intent i = new Intent(Login.this, UserHome.class);
                startActivity(i);
            }
            else {
                Toast.makeText(Login.this, "Login failed \n Please check Username and Password", Toast.LENGTH_SHORT).show();
            }
            pre.dismiss();
        }
    }
}
