package droidia.com.heraldpdx.trimetapis.geospatialdata;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import timber.log.Timber;

/**
 * Created by mandeep on 7/29/16.
 */

public class GeoSpatialKMLDownloader {

    private static final String kmlDataSetUri = "https://developer.trimet.org/gis/data/tm_route_stops.kml";


    public static Observable<String> download()  {

        return Observable.defer(() -> {

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(kmlDataSetUri)
                    .get()
                    .build();

            Response response = null;

            try {

                response = client.newCall(request).execute();
                if (response.isSuccessful()) {

                    Timber.i("Data set download successful");
                    return Observable.just(new String(response.body().bytes()));
                }

            } catch (IOException e) {
                return Observable.error(e);
            }
            return Observable.empty();

        });
    }

    public static Observable<Date> getLastModifiedDateForDataSet(){

        return Observable.defer(() -> {

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(kmlDataSetUri)
                    .head()
                    .build();

            Response response = null;
            try {
                response = client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm:ss zzz", Locale.ENGLISH);
            Date lastModifiedDate = null;
            try {
                lastModifiedDate = format.parse(response.header("Last-Modified"));
            } catch (ParseException e) {
                System.out.println(e.getMessage());
            }
            return Observable.just(lastModifiedDate);
        });


    }
}
