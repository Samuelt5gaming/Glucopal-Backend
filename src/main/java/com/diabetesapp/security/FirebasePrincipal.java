package com.diabetesapp.security;

/** Identity extracted from a verified Firebase ID token. */
public record FirebasePrincipal(
        String uid,
        String email,
        String name,
        boolean emailVerified,
        String picture) {
}
