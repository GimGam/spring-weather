package study.weather.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.weather.domain.DateWeather;
import study.weather.domain.Diary;
import study.weather.dto.DiaryDto;
import study.weather.dto.WeatherInfoDto;
import study.weather.repository.DateWeatherRepository;
import study.weather.repository.DiaryRepository;
import study.weather.util.WeatherApi;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiaryService {

    private final DiaryRepository diaryRepository;

    private final DateWeatherRepository dateWeatherRepository;

    // 사용 API 가 변경되어도 사용할 수 있게 하고싶어서
    // WeatherApi 인터페이스를 상속받는 클래스가 injection 될수 있게 만들어봤는데
    // 잘모르겠다..
    private final WeatherApi weatherApi;

    @Transactional
    public void createDiary(LocalDate date, String text) {
        WeatherInfoDto weatherInfo = weatherApi.getWeatherInfo();

        diaryRepository.save(Diary.builder()
                .weather(weatherInfo.getWeather())
                .icon(weatherInfo.getIcon())
                .temperature(weatherInfo.getTemperature())
                .date(date)
                .text(text)
                .build());
    }

    @Transactional(readOnly = true)
    public List<DiaryDto> getDiary(LocalDate date) {
        List<Diary> diaries = diaryRepository.findByDate(date);

        return diaries.stream()
                .map(DiaryDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DiaryDto> getDiaries(LocalDate startDate, LocalDate endDate) {
        List<Diary> diaries
                = diaryRepository.findByDateBetween(startDate, endDate);

        return diaries.stream()
                .map(DiaryDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateDiary(LocalDate date, String text) {
        Diary firstByDate = diaryRepository.getFirstByDate(date);
        firstByDate.setText(text);
        diaryRepository.save(firstByDate);
    }

    @Transactional
    public void deleteDiary(LocalDate date) {
        diaryRepository.deleteAllByDate(date);
    }

    @Transactional
    //초 분 시 일 월 년
    //0초 0분 1시  매일 매월 매년 동작
    @Scheduled(cron = "0 0 1 * * *")
    public void saveWeatherDate() {
        WeatherInfoDto weatherInfo = weatherApi.getWeatherInfo();
        dateWeatherRepository.save(DateWeather.builder()
                .date(LocalDate.now())
                .weather(weatherInfo.getWeather())
                .temperature(weatherInfo.getTemperature())
                .icon(weatherInfo.getIcon())
                .build());
    }
}
