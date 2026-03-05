// Gestion centralisée des erreurs avec logging
export const ErrorHandler = {
    log: (error, context = '') => {
        const errorInfo = {
            message: error.message || 'Erreur inconnue',
            status: error.response?.status,
            url: error.config?.url,
            context,
            timestamp: new Date().toISOString()
        };
        console.error('[ERROR]', errorInfo);
        // TODO: Envoyer à un service de logging en production
    },

    getDisplayMessage: (error) => {
        const status = error.response?.status;
        const serverMessage = error.response?.data?.message;
        const requestUrl = error.config?.url || '';
        const isLoginRequest = requestUrl.includes('/api/auth/login');

        // Always show a neutral auth error on login attempts.
        if (isLoginRequest && (status === 400 || status === 401)) return 'Identifiant ou mot de passe invalide';
        
        if (serverMessage) return serverMessage;
        
        switch (status) {
            case 400: return 'Données invalides';
            case 401: return 'Session expirée';
            case 403: return 'Accès refusé';
            case 404: return 'Ressource introuvable';
            case 500: return 'Erreur serveur';
            default: return 'Erreur de connexion';
        }
    }
};
