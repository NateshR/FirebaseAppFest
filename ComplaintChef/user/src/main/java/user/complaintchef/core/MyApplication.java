package user.complaintchef.core;

import android.app.Application;

import common.complaintcheflib.net.APIService;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by Simar Arora on 21/06/17.
 */

public class MyApplication extends Application {

    private static final String API_ENDPOINT = "https://maps.googleapis.com/";

    private static APIService apiService;

    public static APIService getAPIService() {
        initAPIService();
        return apiService;
    }

    private static void initAPIService() {
        if (apiService == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .baseUrl(API_ENDPOINT)
                    .build();
            apiService = retrofit.create(APIService.class);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }


}
