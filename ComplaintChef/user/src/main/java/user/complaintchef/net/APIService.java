package user.complaintchef.net;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Simar Arora on 21/06/17.
 */

public interface APIService {

    @GET("maps/api/directions/json?")
    Call<Object> myAPI(@Query("origin") String origin);
}
