package study.weather.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
//날씨 API 에서 가져온 정보 -> 가공 -> 다이어리 Service
public class WeatherInfoDto {
    private String weather;
    private String icon;
    private double temperature;
}
