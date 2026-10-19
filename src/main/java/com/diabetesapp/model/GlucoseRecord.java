package com.diabetesapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Entity
@Table(name = "glucose_records")
@Data
@NoArgsConstructor
public class GlucoseRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @DecimalMin(value = "20.0", message = "Glucose value must be at least 20 mg/dL")
    @DecimalMax(value = "600.0", message = "Glucose value must be at most 600 mg/dL")
    private BigDecimal value;

    @NotNull
    private LocalDateTime recordedAt;

    @Enumerated(EnumType.STRING)
    private MealContext mealContext;

    @Size(max = 500)
    private String note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public enum MealContext {
        FASTING, BEFORE_MEAL, AFTER_MEAL
    }

    @Transient
    public GlucoseStatus getStatus() {
        if (value.compareTo(BigDecimal.valueOf(70)) < 0) return GlucoseStatus.LOW;
        if (value.compareTo(BigDecimal.valueOf(140)) <= 0 ) return GlucoseStatus.NORMAL;
        return GlucoseStatus.HIGH;
    }

    public enum GlucoseStatus { LOW, NORMAL, HIGH }
}

