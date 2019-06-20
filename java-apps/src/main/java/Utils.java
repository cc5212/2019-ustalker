import java.time.Instant;

public class Utils {

    public static int yearFromEpochString(String timestamp) {
        return yearFromEpoch(Double.valueOf(timestamp).longValue());
    }

    public static int yearFromEpoch(long timestamp) {
        return Integer.valueOf(Instant.ofEpochSecond(timestamp).toString().split("-")[0]);
    }

}
