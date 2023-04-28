package study.weather.repository;

import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import study.weather.domain.Diary;

import java.time.LocalDate;
import java.util.List;
@Repository
public interface DiaryRepository
extends JpaRepository<Diary, Integer> {
    List<Diary> findByDate(LocalDate date);
    List<Diary> findByDateBetween(LocalDate startDate, LocalDate endDate);
    Diary getFirstByDate(LocalDate date);
    @Transactional
    void deleteAllByDate(LocalDate date);
}
