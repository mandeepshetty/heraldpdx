package droidia.com.heraldpdx.arrivalchecker;

import droidia.com.heraldpdx.trimetapis.arrivals.ArrivalsAPI;
import droidia.com.heraldpdx.trimetapis.arrivals.ArrivalResults;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.RxJavaCallAdapterFactory;
import rx.Observable;

/**
 * Created by mandeep on 18/6/16.
 */

public class ArrivalInteractor implements IArrivalInteractor {

    private final int DEFAULT_ARRIVAL_COUNT = 4;
    @Override
    public Observable<ArrivalResults> getArrivalsAtLocation(String locationID) {

        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ArrivalsAPI.BASE_URL)
                .build();

        ArrivalsAPI arrivalsAPI = retrofit.create(ArrivalsAPI.class);

        return Observable.defer(() -> arrivalsAPI.getArrivalsAtLocations(locationID, DEFAULT_ARRIVAL_COUNT));
    }
}
