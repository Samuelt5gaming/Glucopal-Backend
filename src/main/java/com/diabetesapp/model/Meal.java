package com.diabetesapp.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "meals")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "photo_url")
    private String photoUrl;

    @Column(name = "meal_name")
    private String mealName;

    @Column(name = "eaten_at", nullable = false)
    private LocalDateTime eatenAt;

    @Column(name = "carbs_g")
    private Double carbsG;

    @Column(name = "protein_g")
    private Double proteinG;

    @Column(name = "vitamins_mg")
    private Double vitaminsMg;

    @Column(name = "calories")
    private Double calories;

    @Column(name = "notes")
    private String notes;

    @Column(name = "ai_suggested")
    private Boolean aiSuggested;
}