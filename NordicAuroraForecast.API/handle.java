import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import org.json.JSONObject;
import org.json.JSONArray;

public class NordicAuroraForecast {
    private static final String BASE_URL = "https://api.met.no/weatherapi/locationforecast/2.0/";
    private static final String USER_AGENT = "NordicAuroraForecast/1.0 support@example.com";

    public static String getWeatherData(double lat, double lon) throws Exception {
        String endpoint = BASE_URL + "compact";
        String params = String.format("lat=%.4f&lon=%.4f", lat, lon);
        URL url = new URL(endpoint + "?" + params);
        
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            
            String expires = con.getHeaderField("Expires");
            String lastModified = con.getHeaderField("Last-Modified");
            System.out.println("Expires: " + expires);
            System.out.println("Last-Modified: " + lastModified);
            
            return content.toString();
        } else if (responseCode == HttpURLConnection.HTTP_NOT_MODIFIED) {
            System.out.println("Data not modified since last request");
            return null;
        } else {
            throw new RuntimeException("HTTP error code : " + responseCode);
        }
    }

    public static boolean predictAurora(String weatherData) {
        JSONObject json = new JSONObject(weatherData);
        JSONArray timeseries = json.getJSONObject("properties").getJSONArray("timeseries");
        
        for (int i = 0; i < timeseries.length(); i++) {
            JSONObject timepoint = timeseries.getJSONObject(i);
            String time = timepoint.getString("time");
            JSONObject data = timepoint.getJSONObject("data");
            JSONObject instant = data.getJSONObject("instant").getJSONObject("details");
            
            double cloudAreaFraction = instant.getDouble("cloud_area_fraction");
            double airTemperature = instant.getDouble("air_temperature");
            
            Instant timestamp = Instant.parse(time);
            int hour = timestamp.atZone(ZoneOffset.UTC).getHour();
            
            if (hour >= 22 || hour <= 4) {
                if (cloudAreaFraction < 20 && airTemperature < 0) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void main(String[] args) {
        try {
            double lat = 69.6492; // Tromsø, Norway
            double lon = 18.9553;
            
            String weatherData = getWeatherData(lat, lon);
            if (weatherData != null) {
                boolean auroraLikely = predictAurora(weatherData);
                System.out.println("Aurora likely to be visible: " + auroraLikely);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}