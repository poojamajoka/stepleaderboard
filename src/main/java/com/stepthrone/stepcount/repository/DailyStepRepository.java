package com.stepthrone.stepcount.repository;

import com.stepthrone.stepcount.model.DailyStepData;
import com.stepthrone.userprofile.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DailyStepRepository extends JpaRepository<DailyStepData, Long> {
    Optional<DailyStepData> findByUserAndDate(User user, LocalDate date);

    @Query("SELECT d FROM DailyStepData d WHERE d.date = :date ORDER BY d.steps DESC")
    List<DailyStepData> findAllByDateOrderByStepsDesc(@Param("date") LocalDate date);

    @Query(value = "SELECT d.user_id, u.first_name, u.last_name, SUM(d.steps) as total_steps " +
            "FROM daily_steps d " +
            "JOIN users u ON d.user_id = u.id " +
            "GROUP BY d.user_id, u.first_name, u.last_name " +
            "ORDER BY total_steps DESC",
            nativeQuery = true)
    List<Object[]> findUserTotalStepsRanking();


    @Query("SELECT d FROM DailyStepData d WHERE d.user.id = :userId")
    List<DailyStepData> findAllByUserId(@Param("userId") Long userId);
}