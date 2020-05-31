package com.kaligoExercise.kaligo.restservice.Utilities;
import java.io.IOException;
import java.util.HashMap;

import com.kaligoExercise.kaligo.restservice.JSONClasses.*;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class HTTPUtility {

    public static HashMap<Integer, String> APIEndpoints = new HashMap<>();
    public static String country = null;

    public static void setURL(){
        APIEndpoints.put(0, "http://www.mocky.io/v2/5ebbea002e000054009f3ffc");
        APIEndpoints.put(1, "http://www.mocky.io/v2/5ebbea102e000029009f3fff");
        APIEndpoints.put(2, "http://www.mocky.io/v2/5ebbea1f2e00002b009f4000");
    }

    public static String getURL(int id){
        return APIEndpoints.get(id);
    }


    public static int http(String url, int supplierId) {

        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpPost request = new HttpPost(url);
            request.addHeader("content-type", "application/json");
            HttpResponse result = httpClient.execute(request);
            String json = EntityUtils.toString(result.getEntity(), "UTF-8");

            com.google.gson.Gson gson = new com.google.gson.Gson();

            Supplier1[] respuest1 = null;
            Supplier2[] respuest2 = null;
            Supplier3[] respuest3 = null;
            CountrySupplier3 resprequestLoc = null;

            if(supplierId == 0){
                respuest1 = gson.fromJson(json, Supplier1[].class);
                DataUtility.supp1 = respuest1;
            }else if (supplierId == 1){
                respuest2 = gson.fromJson(json, Supplier2[].class);
                DataUtility.supp2 = respuest2;
            }else if(supplierId ==2) {
                respuest3 = gson.fromJson(json, Supplier3[].class);
                DataUtility.supp3 = respuest3;
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return supplierId;
    }
}
