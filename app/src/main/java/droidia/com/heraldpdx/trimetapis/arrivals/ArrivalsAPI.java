package droidia.com.heraldpdx.trimetapis.arrivals;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by mandeep on 17/6/16.
 */

public interface ArrivalsAPI {

    String BASE_URL = "http://developer.trimet.org/ws/v2/";

    @GET("arrivals?appID=782117164D5CE65C764B5B958")
    Observable<ArrivalResults> getArrivalsAtLocations(@Query("locIDs") String locationIDs);

}
