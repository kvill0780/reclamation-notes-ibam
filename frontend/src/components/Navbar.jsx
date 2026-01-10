import { useNavigate } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';

export function Navbar({ title }) {
    const { user, logout } = useAuth();
    const navigate = useNavigate();
    const role = user?.role?.replace('ROLE_', '');

    return (
        <nav className="navbar">
            <span className="navbar-brand">{title}</span>
            <div className="navbar-user">
                <span className="user-role">{role}</span>
                <button className="btn-logout" onClick={() => { logout(); navigate('/login'); }}>
                    Déconnexion
                </button>
            </div>
        </nav>
    );
}