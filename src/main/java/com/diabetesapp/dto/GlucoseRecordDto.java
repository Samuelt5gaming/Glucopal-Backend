package com.diabetesapp.dto;

import com.diabetesapp.model.GlucoseRecord;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class GlucoseRecordDto {

    @Data
    public static class Request {
        @NotNull
        @DecimalMin(value = "20.0", message = "Glucose value must be at least 20 mg/dL (glucometer minimum)")
        @DecimalMax(value = "600.0", message = "Glucose value must be at most 600 mg/dL (glucometer maximum)")
        private BigDecimal value;

        private LocalDateTime recordedAt;

        private GlucoseRecord.MealContext mealContext;

        @Size(max = 500)
        private String note;
    }

    @Data
    public static class Response {
        private Long id;
        private BigDecimal value;
        private LocalDateTime recordedAt;
        private GlucoseRecord.MealContext mealContext;
        private String note;
        private GlucoseRecord.GlucoseStatus status;

        public static Response from(GlucoseRecord r) {
            Response dto = new Response();
            dto.setId(r.getId());
            dto.setValue(r.getValue());
            dto.setRecordedAt(r.getRecordedAt());
            dto.setMealContext(r.getMealContext());
            dto.setNote(r.getNote());
            dto.setStatus(r.getStatus());
            return dto;
        }
    }

    @Data
    public static class Stats {
        private Double todayAvg;
        private Double weekAvg;
        private Integer totalReadings;
        private Integer inRangeCount;
        private Double inRangePercent;
        private Double currentA1cEstimate;
    }
}
