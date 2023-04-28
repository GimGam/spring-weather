package study.weather.controller;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cglib.core.Local;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import study.weather.dto.DiaryDto;
import study.weather.service.DiaryService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(DiaryController.class)
class DiaryControllerTest {
    @MockBean
    DiaryService diaryService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("일기 생성 성공.")
    void createDiary_Success() throws Exception {
        //given
        LocalDate date = LocalDate.now();
        String text = "텍스트.";

        //when
        //then
        mockMvc.perform(post("/create/diary")
                        .param("date", date.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(text))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("일기 조회 성공.")
    void getDiary_Success() throws Exception {
        //given
        LocalDate date = LocalDate.now();

        List<DiaryDto> expectedDiaryList = new ArrayList<>();
        expectedDiaryList.add(DiaryDto.builder()
                .id(1)
                .weather("")
                .temperature(1d)
                .text("")
                .date(date)
                .build());

        given(diaryService.getDiary(date))
                .willReturn(expectedDiaryList);

        //when
        MvcResult expect = mockMvc.perform(get("/read/diary")
                        .param("date", date.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        //then

        //어려워서 주석..
        //1.MvcResult 의 HTTP 응답 내용을 문자열로 가져오는 과정.
        String responseBody = expect.getResponse().getContentAsString();
        //2. ObjectMapper 를 이용해서 추출한 ResponseBody 의 문자열을
        //  원하는 List<DiaryDto> 형태로 변환한다
        //  https://lovon.tistory.com/130
        List<DiaryDto> responceDiaryList = objectMapper.readValue(responseBody,
                new TypeReference<List<DiaryDto>>() {
                });

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

    @Test
    @DisplayName("일기 날짜 별 조회 성공.")
    void getDiaries_Success() throws Exception {
        //given
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now();

        List<DiaryDto> expectedDiaryList = new ArrayList<>();
        expectedDiaryList.add(DiaryDto.builder()
                .id(1)
                .weather("")
                .temperature(1d)
                .text("")
                .date(startDate)
                .build());

        given(diaryService.getDiaries(startDate, endDate))
                .willReturn(expectedDiaryList);

        //when
        MvcResult expect = mockMvc.perform(get("/read/diaries")
                        .param("startDate", startDate.toString())
                        .param("endDate", endDate.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        //then

        //어려워서 주석..
        //1.MvcResult 의 HTTP 응답 내용을 문자열로 가져오는 과정.
        String responseBody = expect.getResponse().getContentAsString();
        //2. ObjectMapper 를 이용해서 추출한 ResponseBody 의 문자열을
        //  원하는 List<DiaryDto> 형태로 변환한다
        //  https://lovon.tistory.com/130
        List<DiaryDto> responceDiaryList = objectMapper.readValue(responseBody,
                new TypeReference<List<DiaryDto>>() {
                });

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

    @Test
    @DisplayName("일기 수정 성공")
    void updateDiary_Success() throws Exception {
        //given
        LocalDate date = LocalDate.now();
        String text = "new text";
        //when
        //then
        mockMvc.perform(put("/update/diary")
                        .param("date", date.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(text))
                .andExpect(status().isOk())
                .andDo(print());
    }
}