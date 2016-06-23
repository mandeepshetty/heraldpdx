package droidia.com.heraldpdx.trimetapis.arrivals;

public class Arrival {

    public static final String STATUS_SCHEDULED = "scheduled";
    public static final String STATUS_ESTIMATED = "estimated";
    public static final String STATUS_DELAYED = "delayed";
    public static final String STATUS_CANCELED = "canceled";
    public static final String STATUS_DROP_OFF = "dropOff";

    public int feet;
    public Object inCongestion;
    public boolean departed;
    public long scheduled;
    public Object loadPercentage;
    public String shortSign;
    public long estimated;
    public boolean detoured;
    public String tripID;
    public int dir;
    public int blockID;
    public int route;
    public String piece;
    public String fullSign;
    public String id;
    public String vehicleID;
    public int locid;
    public boolean newTrip;
    public String status;
}