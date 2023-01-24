package model.api;

import okhttp3.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

public class TMDBClient {

    public static final MediaType JSON_MEDIA_TYPE = MediaType.get("application/json; charset=utf-8");
    private final static String BASE_URL = "https://api.themoviedb.org/3";
    private final static OkHttpClient okHttpClient = new OkHttpClient();
    private URL url;

    private final static String TMDBKEY;
    //initialize the TMDBKEY
    static{
        String TMDBKEY1;
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream("src/main/resources/config.properties"));
            TMDBKEY1 = properties.getProperty("TMDBKEY");
        }catch (IOException e){
            TMDBKEY1 = null;
        }
        TMDBKEY = TMDBKEY1;
    }

    public TMDBClient(String endPoint, QueryString... queryStrings) throws MalformedURLException {
        rebuildURL(endPoint, queryStrings);
    }
    public TMDBClient(){

    }
    public void rebuildURL(String endPoint, QueryString... queryStrings) throws MalformedURLException {
        StringBuilder builder = new StringBuilder(BASE_URL);
        builder.append(endPoint);
        builder.append("?api_key=");
        builder.append(TMDBKEY);
        //add the querry strings
        for (int i = 0; i < queryStrings.length; i++) {
            builder.append("&");
            builder.append(queryStrings[i].getParameterName());
            builder.append("=");
            builder.append(queryStrings[i].getParameterValue());
        }
        url = new URL(builder.toString());
    }

    public ResponseBody getRequest() throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Response response = okHttpClient.newCall(request).execute();
        return response.body();
    }
}
