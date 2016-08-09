package droidia.com.heraldpdx.storage;

import android.os.Parcel;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by mandeep on 7/31/16.
 */

public class RealmHeraldLocation extends RealmObject implements SearchSuggestion{

    @PrimaryKey
    private String primaryKey;
    private String locationID;
    private String locationName;
    private String transportType;
    private double latittude;
    private double longitude;
    private String direction;

    public String getDirectionBoolean() {
        return directionBoolean;
    }

    public void setDirectionBoolean(String directionBoolean) {
        this.directionBoolean = directionBoolean;
    }

    private String directionBoolean;
    private String routeDescription;
    boolean isFrequent;

    public RealmHeraldLocation() {}



    public RealmHeraldLocation(Parcel in) {
        this.locationID = in.readString();
        this.locationName = in.readString();
        this.transportType = in.readString();
        this.direction = in.readString();
        this.routeDescription = in.readString();
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getRouteDescription() {
        return routeDescription;
    }

    public void setPrimaryKey (){
        primaryKey = locationID + locationName + direction;
    }

    public void setRouteDescription(String routeDescription) {
        this.routeDescription = routeDescription;
    }

    public boolean isFrequent() {
        return isFrequent;
    }

    public void setFrequent(boolean frequent) {
        isFrequent = frequent;
    }



    @Ignore
    public double fuzzyScore;

    public String getLocationID() {
        return locationID;
    }

    public void setLocationID(String locationID) {
        this.locationID = locationID;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getTransportType() {
        return transportType;
    }

    public void setTransportType(String transportType) {
        this.transportType = transportType;
    }

    public double getLatittude() {
        return latittude;
    }

    public void setLatittude(double latittude) {
        this.latittude = latittude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public static final Creator<RealmHeraldLocation> CREATOR = new Creator<RealmHeraldLocation>() {
        @Override
        public RealmHeraldLocation createFromParcel(Parcel in) {
            return new RealmHeraldLocation(in);
        }

        @Override
        public RealmHeraldLocation[] newArray(int size) {
            return new RealmHeraldLocation[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RealmHeraldLocation that = (RealmHeraldLocation) o;

        if (!locationID.equals(that.locationID)) return false;
        if (!locationName.equals(that.locationName)) return false;
        return direction.equals(that.direction);

    }

    @Override
    public int hashCode() {
        int result = locationID.hashCode();
        result = 31 * result + locationName.hashCode();
        result = 31 * result + direction.hashCode();
        return result;
    }

    @Override
    public String getBody() {
        return locationName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(locationID);
        parcel.writeString(locationName);
        parcel.writeString(transportType);
        parcel.writeString(direction);
        parcel.writeString(routeDescription);
    }
}
