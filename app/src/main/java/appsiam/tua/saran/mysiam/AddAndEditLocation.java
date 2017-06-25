package appsiam.tua.saran.mysiam;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

/**
 * Created by User on 25/6/2560.
 */

public class AddAndEditLocation  extends AsyncTask<String, Void, String>{

    public AddAndEditLocation(Context context) {
        this.context = context;
    }

    private Context context;

    @Override
    protected String doInBackground(String... params) {

        try {

            OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody requestBody = new FormEncodingBuilder()
                    .add("isAdd", "true")
                    .add("Name", params[0])
                    .add("Lat", params[1])
                    .add("Lng", params[2])
                    .build();
            Request.Builder builder = new Request.Builder();
            Request request = builder.url(params[3]).post(requestBody).build();
            Response response = okHttpClient.newCall(request).execute();
            return response.body().string();

        } catch (Exception e) {
            Log.d("SiamV3", "e doin==>" + e.toString());
            return null;
        }
    }
}//Main Class
