package common.complaintcheflib.util;

import common.complaintcheflib.net.APIService;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by Simar Arora on 21/06/17.
 */

public class Login {

    public static String login(APIService apiService, String name, String phoneNo, String isAdmin, String categories, Callback<String> callback){
        String uid = name + System.currentTimeMillis();
        String url = "https://us-central1-fir-appfest.cloudfunctions.net/login?name=" + name + "&phone_no=" + phoneNo + "&is_admin=" + isAdmin + "&uid=" + uid + "&categories=" + categories;
        Call<String> call = apiService.loginApi(url);
        call.enqueue(callback);
        return uid;
    }
}
