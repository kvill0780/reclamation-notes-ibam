import { createContext, useContext, useState, useEffect } from 'react';
import { authApi } from '../services/api';
import { ErrorHandler } from '../utils/errorHandler';

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const token = localStorage.getItem('token');
        const stored = localStorage.getItem('user');
        if (token && stored) setUser(JSON.parse(stored));
        setLoading(false);
    }, []);

    const login = async (email, password) => {
        try {
            const { data } = await authApi.login(email, password);
            const userData = { email, role: data.role, niveau: data.niveau, filiere: data.filiere };
            localStorage.setItem('token', data.token);
            localStorage.setItem('user', JSON.stringify(userData));
            setUser(userData);
            return { ok: true, role: data.role };
        } catch (error) {
            ErrorHandler.log(error, 'Login');
            return { ok: false, error: ErrorHandler.getDisplayMessage(error) };
        }
    };

    const logout = () => {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        setUser(null);
    };

    return (
        <AuthContext.Provider value={{ user, loading, login, logout, isAuth: !!user }}>
            {children}
        </AuthContext.Provider>
    );
}

export const useAuth = () => useContext(AuthContext);