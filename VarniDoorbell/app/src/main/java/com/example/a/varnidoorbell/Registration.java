package com.example.a.varnidoorbell;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class Registration extends AppCompatActivity {

    EditText edtusername, edtemail, edtpassword, edt1;
    Button btnregister;
    TextView txtalert;
    static String msg;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        edtusername = (EditText) findViewById(R.id.edtUsername);
        edtpassword = (EditText) findViewById(R.id.edtPass);
        edtemail = (EditText) findViewById(R.id.edtEmail);
        edt1 = (EditText) findViewById(R.id.edt1);

        txtalert = (TextView) findViewById(R.id.txtAlertMessage);

        btnregister = (Button) findViewById(R.id.btnRegistration);

        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String s1 = edtusername.getText().toString();
                String s2 = edtemail.getText().toString();
                String s3 = edtpassword.getText().toString();
                String s4 = edt1.getText().toString();

                if (!isConnected()) {
                    Toast.makeText(Registration.this, "Please Check Network Connection...", Toast.LENGTH_SHORT).show();
                } else {
                    if ((s1.equals("")) || (s2.equals("")) || (s3.equals("")) || (s4.equals(""))) {
                        txtalert.setText("All fields are Compulsory");

                    } else {
                        if (s3.length() > 7) {
                            if (s3.equals(s4)) {
                                new MyAsyncTask().execute(edtusername.getText().toString(), edtemail.getText().toString(), edtpassword.getText().toString());
                            } else {
                                txtalert.setText("Password and Confirm Password doesn't match..");
                            }
                        }
                        else{
                            txtalert.setText("min 8 characters are required in password");
                        }
                    }
                }
            }
        });


    }

    //    For Login
    public class MyAsyncTask extends AsyncTask<String, Integer, Double> {

        ProgressDialog pre = new ProgressDialog(Registration.this);


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pre.setMessage("Registration Details are submitted...");
            pre.show();

        }

        @Override
        protected Double doInBackground(String... params) {

            URL url;
            HttpURLConnection urlConn;
            OutputStream printout;
            DataInputStream input;


            try {

                url = new URL("http://iot.espressif.cn/v1/user/join/");
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

                jsonParam.put("username", params[0]);
                jsonParam.put("email", params[1]);
                jsonParam.put("password", params[2]);
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

                int i = Integer.parseInt(jObj.getString("status"));

                if (i == 200) {
                    Log.e("i value", String.valueOf(i));
                    msg = "We have sent you a confirmation mail on your e-mail id.\n Please Confirm your Email Account.";
//                    userValues.setJsonObject(jObj);
                    //JSONObject obj1 = jObj.getJSONObject("key");
//                    userValues.setKey(obj1.getString("token").toString());
                    //Login.User_Key = obj1.getString("token").toString();

                    Log.e("input", jObj.getString("status"));
                    Login.msg = true;

                    urlConn.disconnect();
                }
                else{

                    Log.e("i value 403", String.valueOf(i));
//                    Toast.makeText(Registration.this, "Login failed \n Please check Username and Password", Toast.LENGTH_SHORT).show();
                    Login.msg = false;
                    msg = jObj.getString("message");
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

            txtalert.setText(msg);
//            Login l = new Login();
            if(Login.msg == true) {
//                Intent i = new Intent(Registration.this, UserHome.class);
//                startActivity(i);
                txtalert.setTextColor(Color.BLUE);
            }
            pre.dismiss();


        }
    }
}
