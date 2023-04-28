package study.weather.util;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import study.weather.dto.WeatherInfoDto;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Component
@NoArgsConstructor
public class OpenWeatherApi
implements WeatherApi{
    @Value("${openweathermap.key}")
    private String apiKey;

    final private String url =
            "https://api.openweathermap.org/data/2.5/weather?q=seoul&appid=";

    @Override
    public WeatherInfoDto getWeatherInfo() {
        Map<String, Object> parsedMap = parseJsonToWeatherInfo(getJson());

        return WeatherInfoDto.builder()
                .weather((String) parsedMap.get("weather"))
                .icon((String) parsedMap.get("icon"))
                .temperature((double) parsedMap.get("temperature"))
                .build();
    }

    private String getJson() {
        URL url = null;
        try {
            url = new URL(this.url + apiKey);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            BufferedReader br;
            if (200 == responseCode) {
                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            }
            String inputLine = null;
            StringBuilder response = new StringBuilder();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();

            return response.toString();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, Object> parseJsonToWeatherInfo(String jsonStr){
        //https://openweathermap.org/current
        //openweathermap api 사이트의 Json 데이터 구조

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = null;

        try {
            jsonObject = (JSONObject) jsonParser.parse(jsonStr);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        //WeatherInfoDto 에 필요한 정보들만 파싱
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("temperature", ((JSONObject)jsonObject.get("main")).get("temp"));


        //weather.main Group of weather parameters (Rain, Snow, Extreme etc.)
        //weather.icon Weather icon id
        JSONObject weatherData =
                (JSONObject) ((JSONArray) jsonObject.get("weather")).get(0);

        resultMap.put("weather", weatherData.get("main"));
        resultMap.put("icon", weatherData.get("icon"));

        return resultMap;
    }
}
