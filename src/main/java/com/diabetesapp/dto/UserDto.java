package com.diabetesapp.dto;

import com.diabetesapp.model.User;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

public class UserDto {

    @Data
    public static class UpdateRequest {
        @NotBlank private String name;
        private String phone;
        private LocalDate dateOfBirth;
        private String location;
        private User.DiabetesType diabetesType;
        private Integer diagnosedYear;
        private Integer targetRangeLow;
        private Integer targetRangeHigh;
        private Double hba1cGoal;
        private String photoUrl;
    }

    @Data
    public static class Response {
        private Long id;
        private String name;
        private String email;
        private String phone;
        private LocalDate dateOfBirth;
        private String location;
        private User.DiabetesType diabetesType;
        private Integer diagnosedYear;
        private Integer targetRangeLow;
        private Integer targetRangeHigh;
        private Double hba1cGoal;
        private String photoUrl;

        public static Response from(User u) {
            Response dto = new Response();
            dto.setId(u.getId());
            dto.setName(u.getName());
            dto.setEmail(u.getEmail());
            dto.setPhone(u.getPhone());
            dto.setDateOfBirth(u.getDateOfBirth());
            dto.setLocation(u.getLocation());
            dto.setDiabetesType(u.getDiabetesType());
            dto.setDiagnosedYear(u.getDiagnosedYear());
            dto.setTargetRangeLow(u.getTargetRangeLow());
            dto.setTargetRangeHigh(u.getTargetRangeHigh());
            dto.setHba1cGoal(u.getHba1cGoal());
            dto.setPhotoUrl(u.getPhotoUrl());
            return dto;
        }
    }

    @Data
    public static class ChatRequest {
        @NotBlank private String message;
    }

    @Data
    public static class ChatResponse {
        private String reply;
    }
}
