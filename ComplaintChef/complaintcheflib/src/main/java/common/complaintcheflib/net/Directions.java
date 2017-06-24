package common.complaintcheflib.net;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by nateshrelhan on 6/24/17.
 */

public class Directions {
    public static Call<String> findRoute(APIService apiService, String origin, String destination, String mode, String apiKey) {
        String url = "https://maps.googleapis.com/maps/api/directions/json?";
        return apiService.directionsApi(url, origin, destination, mode, apiKey);
    }
}
