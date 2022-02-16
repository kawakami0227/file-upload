package com.example.file_upload_demo.page;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class getResult {
    public static String getResult(String urlString){
        String result = "";
        try{
            URL url = new URL(urlString);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.connect();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String tmp = "";
            while((tmp = in.readLine()) != null){
                result += tmp;
            }
            in.close();
            con.disconnect();
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
}
