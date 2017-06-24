package common.complaintcheflib.net;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by Simar Arora on 21/06/17.
 */

public interface APIService {
    @GET
    Call<String> directionsApi(@Url String url,@Query("origin") String origin, @Query("destination") String destination, @Query("mode") String mode, @Query("key") String apiKey);

    @GET
    Call<String> loginApi(@Url String url);
}
