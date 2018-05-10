package com.example.a.varnidoorbell;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by a on 17-Feb-2016.
 */
public class GetData implements Runnable {

    URL url1;
    HttpURLConnection urlConn1;
    double msg;
    String time;
    static double previous = 0;

    @Override
    public void run() {
        try {

//            previous = Login.d1.getX()+"0";
            Log.e("In Thread", "started");
            url1 = new URL("http://iot.espressif.cn/v1/datastreams/"+ Login.d1.getDatastreamName() +"/datapoints/");
            urlConn1 = (HttpURLConnection) url1.openConnection();
            urlConn1.setDoInput(true);
//                urlConn1.setDoOutput(true);
            urlConn1.setUseCaches(false);
            urlConn1.setRequestMethod("GET");
            urlConn1.setAllowUserInteraction(false);
            urlConn1.addRequestProperty("Authorization", "token "+ Login.d1.getDeviceKey());
            urlConn1.addRequestProperty("Content-Type", "application/json");

            //              Receive Msg from server
            InputStream inp = new BufferedInputStream(urlConn1.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(inp));
            StringBuilder result = new StringBuilder();
            String line = "ab  ";
            while ((line = reader.readLine()) != null)

            {
                result.append(line);
            }

            JSONObject jObj = new JSONObject(String.valueOf(result));
//                JSONObject obj = jObj.getJSONObject("datapoints");
//
//                msg = obj.getString("x") + " ";
////                msg = jObj + "";

            JSONArray a1 = jObj.getJSONArray("datapoints");
            JSONObject obj1 = a1.getJSONObject(0);

            msg = Double.parseDouble(obj1.getString("x"));
            time = obj1.getString("at");
            Login.d1.setTime(time+" ");

            Log.e("x = ", String.valueOf(msg));
            if (previous == msg) {
                Log.e("same", previous+"");
                Login.d1.setX(msg);
                UserHome.updateConversationHandler.post(new updateUIThread(" "));
            }
            else {
//                Login.d1.setX(msg);
                Log.e("x === ", previous + "");
//                UserHome.change_UI("prem");
                Login.d1.setX(msg);
                previous = msg;
                UserHome.updateConversationHandler.post(new updateUIThread(msg+ ""));
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            e.printStackTrace();
        }
    }
}