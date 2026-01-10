package com.ibam.reclamation.exception;

/**
 * Exception métier racine
 */
public abstract class BusinessException extends RuntimeException {

    protected BusinessException(String message) {
        super(message);
    }

    public static class DemandeNotFoundException extends BusinessException {
        public DemandeNotFoundException(String message) {
            super(message);
        }
    }

    public static class EnseignantNotFoundException extends BusinessException {
        public EnseignantNotFoundException(String message) {
            super(message);
        }
    }

    public static class NoteNotFoundException extends BusinessException {
        public NoteNotFoundException(String message) {
            super(message);
        }
    }

    public static class DescriptionObligatoireException extends BusinessException {
        public DescriptionObligatoireException(String message) {
            super(message);
        }
    }

    public static class NoteNonAutoriseException extends BusinessException {
        public NoteNonAutoriseException(String message) {
            super(message);
        }
    }

    public static class CommentaireObligatoireException extends BusinessException {
        public CommentaireObligatoireException(String message) {
            super(message);
        }
    }
}
