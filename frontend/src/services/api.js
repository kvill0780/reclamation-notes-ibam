import axios from 'axios';
import { ErrorHandler } from '../utils/errorHandler';

const api = axios.create({
    baseURL: 'http://localhost:8080',
    headers: {
        'Content-Type': 'application/json',
    },
});

// Request interceptor - Add JWT token
api.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('token');
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    (error) => {
        ErrorHandler.log(error, 'Request interceptor');
        return Promise.reject(error);
    }
);

// Response interceptor - Handle 401 errors
api.interceptors.response.use(
    (response) => response,
    (error) => {
        ErrorHandler.log(error, 'Response interceptor');
        const status = error.response?.status;
        const requestUrl = error.config?.url || '';
        const isLoginRequest = requestUrl.includes('/api/auth/login');

        // Keep inline error on login form instead of forcing a full page reload.
        if (status === 401 && !isLoginRequest) {
            localStorage.removeItem('token');
            localStorage.removeItem('user');
            window.location.href = '/login';
        }
        return Promise.reject(error);
    }
);

export default api;

// ============================================
// API Helper functions organized by domain
// ============================================

// Auth API
export const authApi = {
    login: (email, password) => api.post('/api/auth/login', { email, password }),
    logout: () => api.post('/api/auth/logout'),
};

// Notes API (Étudiant)
export const notesApi = {
    getMesNotes: (niveau = null) => {
        const params = niveau ? `?niveau=${niveau}` : '';
        return api.get(`/api/notes/mes-notes${params}`);
    },
};

// Réclamations API - Unifié
export const reclamationApi = {
    // Opérations de base
    getAll: () => api.get('/api/reclamations'),
    getById: (id) => api.get(`/api/reclamations/${id}`),
    create: (formData) => api.post('/api/reclamations', formData, {
        headers: { 'Content-Type': 'multipart/form-data' }
    }),
    getJustificatif: (id) => api.get(`/api/reclamations/${id}/justificatif`, {
        responseType: 'blob'
    }),

    // Actions par rôle
    verifier: (id, recevable, commentaire) => {
        const params = new URLSearchParams({ recevable: String(recevable) });
        if (commentaire && commentaire.trim()) {
            params.append('commentaire', commentaire.trim());
        }
        return api.put(`/api/reclamations/${id}/verifier?${params.toString()}`);
    },
    
    imputer: (id, enseignantId) =>
        api.put(`/api/reclamations/${id}/imputer?enseignantId=${enseignantId}`),
    
    imputerAuto: (id) =>
        api.put(`/api/reclamations/${id}/imputer-auto`),
    
    imputerLot: (demandeIds) =>
        api.put('/api/reclamations/imputer-lot', demandeIds),
    
    analyser: (id, acceptee, commentaire) =>
        api.put(`/api/reclamations/${id}/analyser?acceptee=${acceptee}&commentaire=${encodeURIComponent(commentaire)}`),

    appliquer: (id) =>
        api.put(`/api/reclamations/${id}/appliquer`),

    // Ressources
    getEnseignants: () => api.get('/api/reclamations/enseignants'),
    getEnseignantsImputables: (id) => api.get(`/api/reclamations/${id}/enseignants-imputables`),
};

// API pour périodes
export const periodeApi = {
    getActive: () => api.get('/api/periodes/active'),
    getAll: () => api.get('/api/periodes'),
    create: (periode) => api.post('/api/periodes', periode),
    fermer: (id) => api.put(`/api/periodes/${id}/fermer`),
};
