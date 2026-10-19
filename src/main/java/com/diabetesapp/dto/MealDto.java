package com.diabetesapp.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDateTime;

public class MealDto {

    @Getter @Setter
    public static class Request {
        private String photoUrl;
        private String mealName;
        @NotNull
        private LocalDateTime eatenAt;
        private Double carbsG;
        private Double proteinG;
        private Double vitaminsMg;
        private Double calories;
        private String notes;
        private Boolean aiSuggested;
    }

    @Getter @Setter @Builder
    public static class Response {
        private Long id;
        private Long userId;
        private String photoUrl;
        private String mealName;
        private LocalDateTime eatenAt;
        private Double carbsG;
        private Double proteinG;
        private Double vitaminsMg;
        private Double calories;
        private String notes;
        private Boolean aiSuggested;
    }
}