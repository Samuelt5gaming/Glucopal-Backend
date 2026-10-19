package com.diabetesapp.repository;

import com.diabetesapp.model.GlucoseRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface GlucoseRecordRepository extends JpaRepository<GlucoseRecord, Long> {

    List<GlucoseRecord> findByUserIdOrderByRecordedAtDesc(Long userId);

    List<GlucoseRecord> findByUserIdAndRecordedAtBetweenOrderByRecordedAtDesc(
            Long userId, LocalDateTime from, LocalDateTime to);

    @Query(
        "SELECT AVG(g.value) FROM GlucoseRecord g WHERE g.user.id = :userId AND g.recordedAt >= :from"
    )
    Optional<Double> findAverageByUserIdSince(
        @Param("userId") Long userId,
        @Param("from") LocalDateTime from);

    @Query(
        "SELECT COUNT(g) FROM GlucoseRecord g WHERE g.user.id = :userId " +
        "AND g.recordedAt >= :from AND g.value BETWEEN :low AND :high"
    )
    Long countInRangeByUserIdSince(
        @Param("userId") Long userId,
        @Param("from") LocalDateTime from,
        @Param("low") BigDecimal low,
        @Param("high") BigDecimal high);

    @Query("SELECT COUNT(g) FROM GlucoseRecord g WHERE g.user.id = :userId AND g.recordedAt >= :from")
    Long countByUserIdSince(@Param("userId") Long userId, @Param("from") LocalDateTime from);
}