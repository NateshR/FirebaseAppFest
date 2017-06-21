package user.complaintchef.core;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import user.complaintchef.net.APIService;

/**
 * Created by Simar Arora on 21/06/17.
 */

public class MyApplication extends Application {

    private static final String API_ENDPOINT = "https://maps.googleapis.com/";

    private APIService apiService;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public APIService getAPIService() {
        initAPIService();
        return apiService;
    }

    private void initAPIService() {
        if (apiService == null) {
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .baseUrl(API_ENDPOINT)
                    .build();
            apiService = retrofit.create(APIService.class);
        }
    }


}
