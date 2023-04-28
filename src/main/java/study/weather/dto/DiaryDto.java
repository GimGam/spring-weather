package study.weather.dto;

import lombok.*;
import study.weather.domain.Diary;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DiaryDto {
    private int id;
    private String weather;
    private String icon;
    private double temperature;
    private String text;
    private LocalDate date;

    static public DiaryDto fromEntity(Diary entity){
        return DiaryDto.builder()
                .id(entity.getId())
                .weather(entity.getWeather())
                .temperature(entity.getTemperature())
                .text(entity.getText())
                .date(entity.getDate())
                .build();
    }
}
