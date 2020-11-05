package app.ads.control.funtion;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;

/**
 * Created by dch on 9/16/16.
 */
public class JSONParser {

    public static JSONObject getJSONFromUrl(String url) {

        // Making HTTP request
        try {
            // defaultHttpClient
            URL net = new URL(url);
            // Read all the text returned by the server
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    net.openStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            reader.close();
            JSONObject jObj;
            try {
                jObj = new JSONObject(sb.toString());
                return jObj;
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());
            }


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        return null;

    }
}