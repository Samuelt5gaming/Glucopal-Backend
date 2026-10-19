package com.diabetesapp.service;

import com.diabetesapp.dto.UserDto;
import com.diabetesapp.model.User;
import com.diabetesapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepo;

    @Transactional(readOnly = true)
    public UserDto.Response getProfile(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        return UserDto.Response.from(user);
    }

    @Transactional
    public UserDto.Response updateProfile(Long userId, UserDto.UpdateRequest req) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        user.setName(req.getName());
        user.setPhone(req.getPhone());
        user.setDateOfBirth(req.getDateOfBirth());
        user.setLocation(req.getLocation());
        user.setDiabetesType(req.getDiabetesType());
        user.setDiagnosedYear(req.getDiagnosedYear());
        user.setTargetRangeLow(req.getTargetRangeLow());
        user.setTargetRangeHigh(req.getTargetRangeHigh());
        user.setHba1cGoal(req.getHba1cGoal());
        if (req.getPhotoUrl() != null) {
            // only accept images we uploaded ourselves (Cloudinary)
            if (!req.getPhotoUrl().startsWith("https://res.cloudinary.com/")) {
                throw new IllegalArgumentException("Invalid photo URL");
            }
            user.setPhotoUrl(req.getPhotoUrl());
        }

        return UserDto.Response.from(userRepo.save(user));
    }
}
