package com.diabetesapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @Email
    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "firebase_uid", unique = true, length = 128)
    private String firebaseUid;

    private String photoUrl;

    @Column(nullable = false)
    private String authProvider = "local"; // "firebase" (legacy rows: "local", "google")

    private String phone;

    private LocalDate dateOfBirth;

    private String location;

    @Enumerated(EnumType.STRING)
    private DiabetesType diabetesType;

    private Integer diagnosedYear;

    private Integer targetRangeLow;

    private Integer targetRangeHigh;

    private Double hba1cGoal;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<GlucoseRecord> glucoseRecords;

    public enum DiabetesType {
        TYPE_1, TYPE_2, GESTATIONAL, PREDIABETES, OTHER
    }
}