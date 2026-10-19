package com.diabetesapp.controller;

import com.diabetesapp.dto.GlucoseRecordDto;
import com.diabetesapp.security.FirebasePrincipal;
import com.diabetesapp.service.CurrentUserService;
import com.diabetesapp.service.GlucoseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/me/glucose")
@RequiredArgsConstructor
public class GlucoseController {

    private final GlucoseService glucoseService;
    private final CurrentUserService currentUser;

    /** Log a new reading */
    @PostMapping
    public ResponseEntity<GlucoseRecordDto.Response> addReading(
            @AuthenticationPrincipal FirebasePrincipal me,
            @Valid @RequestBody GlucoseRecordDto.Request request) {
        return ResponseEntity.ok(glucoseService.addReading(currentUser.id(me), request));
    }

    /** List all readings */
    @GetMapping
    public ResponseEntity<List<GlucoseRecordDto.Response>> getReadings(
            @AuthenticationPrincipal FirebasePrincipal me,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {

        if (from != null && to != null) {
            return ResponseEntity.ok(glucoseService.getReadingsInRange(currentUser.id(me), from, to));
        }
        return ResponseEntity.ok(glucoseService.getAllReadings(currentUser.id(me)));
    }

    /** Dashboard Summary */
    @GetMapping("/stats")
    public ResponseEntity<GlucoseRecordDto.Stats> getStats(
            @AuthenticationPrincipal FirebasePrincipal me,
            @RequestParam(defaultValue = "70") int targetLow,
            @RequestParam(defaultValue = "140") int targetHigh) {
        return ResponseEntity.ok(glucoseService.getStats(currentUser.id(me), targetLow, targetHigh));
    }

    /** DELETE */
    @DeleteMapping("/{recordId}")
    public ResponseEntity<Void> deleteReading(
            @AuthenticationPrincipal FirebasePrincipal me,
            @PathVariable Long recordId) {
        glucoseService.deleteReading(currentUser.id(me), recordId);
        return ResponseEntity.noContent().build();
    }
}