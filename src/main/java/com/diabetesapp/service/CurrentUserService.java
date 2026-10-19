package com.diabetesapp.service;

import com.diabetesapp.model.User;
import com.diabetesapp.repository.UserRepository;
import com.diabetesapp.security.FirebasePrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/**
 * Maps a verified Firebase identity to a row in our users table.
 * - Known uid: return it.
 * - Legacy row with the same (verified) email: link the uid to it so old data is kept.
 * - Otherwise: create a new user on first request.
 */
@Service
@RequiredArgsConstructor
public class CurrentUserService {

    private final UserRepository userRepo;

    public Long id(FirebasePrincipal principal) {
        return resolve(principal).getId();
    }

    public User resolve(FirebasePrincipal principal) {
        return userRepo.findByFirebaseUid(principal.uid())
                .orElseGet(() -> linkOrCreate(principal));
    }

    private User linkOrCreate(FirebasePrincipal p) {
        if (p.email() == null || p.email().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account has no email address");
        }
        try {
            User existing = userRepo.findByEmail(p.email()).orElse(null);
            if (existing != null) {
                if (existing.getFirebaseUid() != null && !existing.getFirebaseUid().equals(p.uid())) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already linked to another account");
                }
                if (!p.emailVerified()) {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Verify your email before signing in");
                }
                existing.setFirebaseUid(p.uid());
                return userRepo.save(existing);
            }

            User user = new User();
            user.setFirebaseUid(p.uid());
            user.setEmail(p.email());
            user.setName(p.name() != null && !p.name().isBlank() ? p.name() : p.email().split("@")[0]);
            user.setPhotoUrl(p.picture());
            user.setAuthProvider("firebase");
            return userRepo.save(user);
        } catch (DataIntegrityViolationException race) {
            // Parallel first requests from the same new user: the other request won, reuse its row.
            return userRepo.findByFirebaseUid(p.uid()).orElseThrow(() -> race);
        }
    }
}
