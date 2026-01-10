package com.ibam.reclamation.exception;

import com.ibam.reclamation.exception.BusinessException.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Gestionnaire global des exceptions - Retourne des codes HTTP appropriés
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // --- 400 Bad Request : Validation et arguments invalides ---

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        String errors = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return buildResponse(HttpStatus.BAD_REQUEST, "Erreur de validation", errors, request);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(
            IllegalArgumentException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, "Argument invalide", ex.getMessage(), request);
    }

    @ExceptionHandler(DescriptionObligatoireException.class)
    public ResponseEntity<Map<String, Object>> handleDescriptionObligatoire(
            DescriptionObligatoireException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, "Validation échouée", ex.getMessage(), request);
    }

    // --- 401 Unauthorized : Authentification ---

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Map<String, Object>> handleUnauthorizedException(
            UnauthorizedException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.UNAUTHORIZED, "Non autorisé", ex.getMessage(), request);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUsernameNotFoundException(
            UsernameNotFoundException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.UNAUTHORIZED, "Authentification échouée", ex.getMessage(), request);
    }

    // --- 403 Forbidden : Autorisation ---

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDeniedException(
            AccessDeniedException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.FORBIDDEN, "Accès refusé", "Vous n'avez pas les droits pour cette action",
                request);
    }

    // --- 404 Not Found : Ressources introuvables ---

    @ExceptionHandler(DemandeNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleDemandeNotFoundException(
            DemandeNotFoundException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, "Demande introuvable", ex.getMessage(), request);
    }

    @ExceptionHandler(NoteNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNoteNotFoundException(
            NoteNotFoundException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, "Note introuvable", ex.getMessage(), request);
    }

    @ExceptionHandler(EnseignantNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleEnseignantNotFoundException(
            EnseignantNotFoundException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, "Enseignant introuvable", ex.getMessage(), request);
    }

    // --- 409 Conflict : État invalide (workflow) ---

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalStateException(
            IllegalStateException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.CONFLICT, "État invalide", ex.getMessage(), request);
    }

    // --- 500 Internal Server Error : Erreurs génériques ---

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(
            Exception ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Erreur serveur",
                "Une erreur interne s'est produite", request);
    }

    // --- Méthode utilitaire pour construire les réponses ---

    private ResponseEntity<Map<String, Object>> buildResponse(
            HttpStatus status, String error, String message, HttpServletRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", status.value());
        body.put("error", error);
        body.put("message", message);
        body.put("path", request.getRequestURI());
        return ResponseEntity.status(status).body(body);
    }
}