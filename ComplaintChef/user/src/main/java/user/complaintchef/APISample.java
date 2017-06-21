package user.complaintchef;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import user.complaintchef.core.MyApplication;
import user.complaintchef.net.APIService;

/**
 * Created by Simar Arora on 21/06/17.
 */

public class APISample  {



    public void callAPI(MyApplication myApplication){
        APIService apiService = myApplication.getAPIService();

        Call<Object> call = apiService.myAPI("Chicago");
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });

    }
}
