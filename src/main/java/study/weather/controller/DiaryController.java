package study.weather.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import study.weather.dto.DiaryDto;
import study.weather.service.DiaryService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class DiaryController {
    private  final DiaryService diaryService;

    @PostMapping("create/diary")
    public void createDiary(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date,
            @RequestBody
            String text){
        diaryService.createDiary(date, text);
    }

    @GetMapping("read/diary")
    public List<DiaryDto> getDiary(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date){
        return diaryService.getDiary(date);
    }

    @GetMapping("read/diaries")
    public List<DiaryDto> getDiaries(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate){
        return diaryService.getDiaries(startDate, endDate);
    }

    @PutMapping("update/diary")
    public void updateDiary(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date,
            @RequestBody
            String text
    ){
        diaryService.updateDiary(date, text);
    }

    @DeleteMapping("delete/diary")
    public void deleteDiary(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date
    ){
        diaryService.deleteDiary(date);
    }
}
