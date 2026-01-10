import { useState } from 'react';

let notifId = 0;

export function useNotifications() {
    const [notifications, setNotifications] = useState([]);

    const notify = (message, type = 'info') => {
        const id = ++notifId;
        const notification = { id, message, type };
        
        setNotifications(prev => [...prev, notification]);
        
        setTimeout(() => {
            setNotifications(prev => prev.filter(n => n.id !== id));
        }, 3000);
    };

    const success = (message) => notify(message, 'success');
    const error = (message) => notify(message, 'error');
    const warning = (message) => notify(message, 'warning');

    return { notifications, notify, success, error, warning };
}

export function NotificationContainer({ notifications }) {
    if (!notifications.length) return null;

    return (
        <div className="notification-container">
            {notifications.map(notif => (
                <div key={notif.id} className={`notification notification-${notif.type}`}>
                    <span className="notification-icon">
                        {notif.type === 'success' && '✓'}
                        {notif.type === 'error' && '✗'}
                        {notif.type === 'warning' && '⚠'}
                        {notif.type === 'info' && 'ℹ'}
                    </span>
                    <span>{notif.message}</span>
                </div>
            ))}
        </div>
    );
}