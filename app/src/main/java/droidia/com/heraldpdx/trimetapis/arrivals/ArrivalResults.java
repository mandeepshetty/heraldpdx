package droidia.com.heraldpdx.trimetapis.arrivals;

import java.util.List;

public class ArrivalResults {
    public Result resultSet;

    public boolean containsError(){
        return resultSet.error != null;
    }

    public String getErrorMessage(){
        return resultSet.error.content == null ? "" : resultSet.error.content;
    }
}

