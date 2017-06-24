package common.complaintcheflib.net;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by Simar Arora on 21/06/17.
 */

public class Login {
    public static void login(APIService apiService, String uid, String name, String phoneNo, String isAdmin, String categories, Callback<String> callback) {
        String url = "https://us-central1-fir-appfest.cloudfunctions.net/login?name=" + name + "&mobile_no=" + phoneNo + "&is_admin=" + isAdmin + "&uid=" + uid + "&categories=" + categories;
        Call<String> call = apiService.loginApi(url);
        call.enqueue(callback);
    }
}
