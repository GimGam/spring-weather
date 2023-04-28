package study.weather.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cglib.core.Local;
import org.springframework.transaction.annotation.Transactional;
import study.weather.domain.Diary;
import study.weather.dto.DiaryDto;
import study.weather.dto.WeatherInfoDto;
import study.weather.repository.DiaryRepository;
import study.weather.util.WeatherApi;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static com.fasterxml.jackson.databind.cfg.CoercionInputShape.Array;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class) //Mock 테스트를 위한 어노테이션
@Transactional
class DiaryServiceTest {
    @Mock
    private DiaryRepository diaryRepository;
    @Mock
    private WeatherApi weatherApi;
    @InjectMocks
    private DiaryService diaryService;

    //==========================================================================
    //CREATE DIARY TEST
    //==========================================================================
    @Test
    @DisplayName("일기 생성 성공.")
    void createDiary_Success() {
        //given
        LocalDate localDate = LocalDate.now();
        String text = "테스트 텍스트";
        WeatherInfoDto weatherInfoDto = getEmptyWeatherInfoDto();

        given(weatherApi.getWeatherInfo())
                .willReturn(weatherInfoDto);

        //when
        diaryService.createDiary(localDate, text);

        //then
        ArgumentCaptor<Diary> captor =
                ArgumentCaptor.forClass(Diary.class);

        verify(diaryRepository, times(1))
                .save(captor.capture());

        Diary savedDiary = captor.getValue();
        assertEquals(weatherInfoDto.getWeather(), savedDiary.getWeather());
        assertEquals(weatherInfoDto.getIcon(), savedDiary.getIcon());
        assertEquals(weatherInfoDto.getTemperature(), savedDiary.getTemperature());
        assertEquals(localDate, savedDiary.getDate());
        assertEquals(text, savedDiary.getText());
    }

    //==========================================================================
    //GET DIARY TEST
    //==========================================================================
    @Test
    @DisplayName("일기 조회 성공.")
    void getDiary_Success() {
        //given
        LocalDate date = LocalDate.now();

        List<Diary> expectedDiaryList
                = Arrays.asList(
                Diary.builder()
                        .weather("")
                        .icon("")
                        .temperature(1d)
                        .text("")
                        .date(date)
                        .build()
        );

        given(diaryRepository.findByDate(date))
                .willReturn(expectedDiaryList);
        //when
        List<DiaryDto> responceDiaryList
                = diaryService.getDiary(date);
        //then
        assertNotEquals(0, responceDiaryList.size());
        assertEquals(expectedDiaryList.get(0).getId(),
                responceDiaryList.get(0).getId());
        assertEquals(expectedDiaryList.get(0).getWeather(),
                responceDiaryList.get(0).getWeather());
        assertEquals(expectedDiaryList.get(0).getTemperature(),
                responceDiaryList.get(0).getTemperature());
        assertEquals(expectedDiaryList.get(0).getText(),
                responceDiaryList.get(0).getText());
        assertEquals(expectedDiaryList.get(0).getDate(),
                responceDiaryList.get(0).getDate());
    }

    //==========================================================================
    //GET DIARIES TEST
    //==========================================================================
    @Test
    @DisplayName("일기 날짜 별 조회 성공.")
    void getDiaries_Success() {
        //given
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now();

        List<Diary> expectedDiaryList
                = Arrays.asList(
                Diary.builder()
                        .weather("")
                        .icon("")
                        .temperature(1d)
                        .text("")
                        .date(startDate)
                        .build()
        );

        given(diaryRepository.findByDateBetween(startDate, endDate))
                .willReturn(expectedDiaryList);
        //when
        List<DiaryDto> responceDiaryList
                = diaryService.getDiaries(startDate, endDate);
        //then
        assertNotEquals(0, responceDiaryList.size());
        assertEquals(expectedDiaryList.get(0).getId(),
                responceDiaryList.get(0).getId());
        assertEquals(expectedDiaryList.get(0).getWeather(),
                responceDiaryList.get(0).getWeather());
        assertEquals(expectedDiaryList.get(0).getTemperature(),
                responceDiaryList.get(0).getTemperature());
        assertEquals(expectedDiaryList.get(0).getText(),
                responceDiaryList.get(0).getText());
        assertEquals(expectedDiaryList.get(0).getDate(),
                responceDiaryList.get(0).getDate());
    }

    //==========================================================================
    //PUT DIARY TEST
    //==========================================================================
    @Test
    @DisplayName("일기 수정 성공.")
    void updateDiary_Success()  {
        //given
        LocalDate date = LocalDate.now();
        String newText = "new text";

        Diary diary = getEmptyDiary();
        diary.setDate(date);

        given(diaryRepository.getFirstByDate(date))
                .willReturn(diary);
        //when
        diaryService.updateDiary(date, newText);

        //then
        ArgumentCaptor<Diary> captor =
                ArgumentCaptor.forClass(Diary.class);

        verify(diaryRepository, times(1))
                .save(captor.capture());

        Diary savedDiary = captor.getValue();
        assertEquals(diary.getWeather(), savedDiary.getWeather());
        assertEquals(diary.getIcon(), savedDiary.getIcon());
        assertEquals(diary.getTemperature(), savedDiary.getTemperature());
        assertEquals(diary.getDate(), savedDiary.getDate());
        assertEquals(diary.getText(), savedDiary.getText());
    }

    //==========================================================================
    //TEST UTILITY
    //==========================================================================
    WeatherInfoDto getEmptyWeatherInfoDto() {
        return WeatherInfoDto.builder()
                .weather("테스트 날씨")
                .temperature(100d)
                .icon("테스트 아이콘")
                .build();
    }

    Diary getEmptyDiary(){
        return Diary.builder()
                .id(1)
                .weather("")
                .icon("")
                .temperature(1d)
                .text("")
                .date(LocalDate.now())
                .build();
    }
}