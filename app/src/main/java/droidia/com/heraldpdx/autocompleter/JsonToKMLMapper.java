package droidia.com.heraldpdx.autocompleter;

import java.util.List;
import java.util.Map;

/**
 * Created by mandeep on 7/30/16.
 */

class Datum {

    public String name;
    public String value;

}
class Document {

    public List<Placemark> Placemark;

}

class JsonToKMLMapper {

    public Kml kml;

}
class ExtendedData {


    public List<Datum> Data;

}
class Kml {
    public String xmlns;
    public Document Document;

}
class Placemark {

    public ExtendedData ExtendedData;
    public Point Point;

}
class Point {

    public String coordinates;

}
