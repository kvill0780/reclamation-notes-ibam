import { BrowserRouter, Routes, Route, Navigate, useNavigate } from 'react-router-dom';
import { useState, useEffect } from 'react';
import api, { authApi, reclamationApi, notesApi, periodeApi } from './services/api';
import { useNotifications, NotificationContainer } from './notifications.jsx';
import { AuthProvider, useAuth } from './contexts/AuthContext.jsx';
import { Navbar } from './components/Navbar.jsx';
import { ReclamationCard } from './components/ReclamationCard.jsx';
import { ErrorHandler } from './utils/errorHandler.js';
import { ReclamationStatus } from './utils/reclamationStatus.js';

// ============ PAGES ============
function GestionPeriodes() {
    const { user } = useAuth();
    const navigate = useNavigate();
    const { success, error: notifyError } = useNotifications();
    const [periodes, setPeriodes] = useState([]);
    const [periodeActive, setPeriodeActive] = useState(null);
    const [activeTab, setActiveTab] = useState('active');
    const [nouvellePeriode, setNouvellePeriode] = useState({
        nom: '',
        dateDebut: '',
        dateFin: '',
        description: ''
    });

    const fetchPeriodes = async () => {
        try {
            const { data } = await periodeApi.getAll();
            setPeriodes(data);
        } catch (e) {
            console.error('Erreur périodes:', e);
        }
    };

    const checkPeriodeActive = async () => {
        try {
            const { data } = await periodeApi.getActive();
            setPeriodeActive(data);
        } catch (e) {
            console.error('Erreur période:', e);
        }
    };

    const handleCreatePeriode = async (e) => {
        e.preventDefault();
        try {
            await periodeApi.create(nouvellePeriode);
            success('Période créée avec succès');
            setNouvellePeriode({ nom: '', dateDebut: '', dateFin: '', description: '' });
            fetchPeriodes();
            checkPeriodeActive();
        } catch (e) {
            notifyError(e.response?.data?.message || 'Erreur lors de la création');
        }
    };

    const handleFermerPeriode = async (id) => {
        try {
            await periodeApi.fermer(id);
            success('Période fermée');
            fetchPeriodes();
            checkPeriodeActive();
        } catch (e) {
            notifyError('Erreur lors de la fermeture');
        }
    };

    useEffect(() => {
        fetchPeriodes();
        checkPeriodeActive();
    }, []);

    return (
        <div className="dashboard">
            <Navbar title="Gestion des Périodes" />
            <div className="main-content">
                <div className="page-header">
                    <h2>Gestion des Périodes de Réclamation</h2>
                    <button className="btn-secondary" onClick={() => navigate('/da/dashboard')}>
                        Retour au tableau de bord
                    </button>
                </div>

                {/* Tabs */}
                <div className="tabs">
                    <button 
                        className={activeTab === 'active' ? 'tab-active' : ''}
                        onClick={() => setActiveTab('active')}
                    >
                        Période active {periodeActive?.active ? '(1)' : '(0)'}
                    </button>
                    <button 
                        className={activeTab === 'create' ? 'tab-active' : ''}
                        onClick={() => setActiveTab('create')}
                    >
                        Créer période
                    </button>
                    <button 
                        className={activeTab === 'history' ? 'tab-active' : ''}
                        onClick={() => setActiveTab('history')}
                    >
                        Historique ({periodes.length})
                    </button>
                </div>

                {/* Contenu selon l'onglet actif */}
                {activeTab === 'active' && (
                    <div className="tab-content">
                        {periodeActive?.active ? (
                            <div className="periode-active-section">
                                <h3>🟢 Période Actuellement Active</h3>
                                <div className="periode-active-card">
                                    <h4>{periodeActive.nom}</h4>
                                    <p>Fin: {new Date(periodeActive.dateFin).toLocaleString('fr-FR')}</p>
                                    <p>Temps restant: {periodeActive.heuresRestantes}h</p>
                                    <button 
                                        className="btn-danger"
                                        onClick={() => {
                                            const periode = periodes.find(p => p.nom === periodeActive.nom);
                                            if (periode && confirm('Fermer cette période ?')) {
                                                handleFermerPeriode(periode.id);
                                            }
                                        }}
                                    >
                                        Fermer cette période
                                    </button>
                                </div>
                            </div>
                        ) : (
                            <div className="empty">
                                <h3>Aucune période active</h3>
                                <p>Il n'y a actuellement aucune période de réclamation ouverte.</p>
                                <button 
                                    className="btn-primary"
                                    onClick={() => setActiveTab('create')}
                                >
                                    Créer une nouvelle période
                                </button>
                            </div>
                        )}
                    </div>
                )}

                {activeTab === 'create' && (
                    <div className="tab-content">
                        <div className="create-periode-section">
                            <h3>Créer une Nouvelle Période</h3>
                            <form onSubmit={handleCreatePeriode} className="periode-form">
                                <div className="form-group">
                                    <label>Nom de la période</label>
                                    <input 
                                        value={nouvellePeriode.nom}
                                        onChange={e => setNouvellePeriode({...nouvellePeriode, nom: e.target.value})}
                                        placeholder="Ex: Réclamations Semestre 2 - 2024"
                                        required
                                    />
                                </div>
                                <div className="form-row">
                                    <div className="form-group">
                                        <label>Date de début</label>
                                        <input 
                                            type="datetime-local"
                                            value={nouvellePeriode.dateDebut}
                                            onChange={e => setNouvellePeriode({...nouvellePeriode, dateDebut: e.target.value})}
                                            required
                                        />
                                    </div>
                                    <div className="form-group">
                                        <label>Date de fin (max 3 jours)</label>
                                        <input 
                                            type="datetime-local"
                                            value={nouvellePeriode.dateFin}
                                            onChange={e => setNouvellePeriode({...nouvellePeriode, dateFin: e.target.value})}
                                            required
                                        />
                                    </div>
                                </div>
                                <div className="form-group">
                                    <label>Description (optionnel)</label>
                                    <textarea 
                                        value={nouvellePeriode.description}
                                        onChange={e => setNouvellePeriode({...nouvellePeriode, description: e.target.value})}
                                        placeholder="Information pour les étudiants"
                                    />
                                </div>
                                <button type="submit" className="btn-primary">
                                    Créer la période
                                </button>
                            </form>
                        </div>
                    </div>
                )}

                {activeTab === 'history' && (
                    <div className="tab-content">
                        <div className="periodes-history">
                            <h3>Historique des Périodes</h3>
                            {periodes.length === 0 ? (
                                <div className="empty">Aucune période créée</div>
                            ) : (
                                <div className="periodes-list">
                                    {periodes.map(p => (
                                        <div key={p.id} className={`periode-card ${p.active ? 'active' : 'inactive'}`}>
                                            <div className="periode-info">
                                                <h4>{p.nom}</h4>
                                                <div className="periode-dates">
                                                    {new Date(p.dateDebut).toLocaleString('fr-FR')} → {new Date(p.dateFin).toLocaleString('fr-FR')}
                                                </div>
                                                {p.description && <div className="periode-description">{p.description}</div>}
                                            </div>
                                            <div className="periode-actions">
                                                <span className={`status ${p.active ? 'status-acceptee' : 'status-refusee'}`}>
                                                    {p.active ? 'Active' : 'Fermée'}
                                                </span>
                                                {p.active && (
                                                    <button 
                                                        className="btn-danger btn-small"
                                                        onClick={() => {
                                                            if (confirm('Fermer cette période ?')) {
                                                                handleFermerPeriode(p.id);
                                                            }
                                                        }}
                                                    >
                                                        Fermer
                                                    </button>
                                                )}
                                            </div>
                                        </div>
                                    ))}
                                </div>
                            )}
                        </div>
                    </div>
                )}
            </div>
            <NotificationContainer notifications={useNotifications().notifications} />
        </div>
    );
}

function Login() {
    const { login } = useAuth();
    const navigate = useNavigate();
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        const result = await login(email, password);
        setLoading(false);
        if (result.ok) {
            navigate(`/${result.role.replace('ROLE_', '').toLowerCase()}/dashboard`);
        } else {
            setError(result.error);
        }
    };

    const demo = (e) => { setEmail(e); setPassword('password123'); };

    return (
        <div className="login-page">
            <div className="login-form">
                <h1>IBAM - Réclamations</h1>
                {error && <div className="error">{error}</div>}
                <form onSubmit={handleSubmit}>
                    <div className="form-group">
                        <label>Email</label>
                        <input type="email" value={email} onChange={e => setEmail(e.target.value)} required />
                    </div>
                    <div className="form-group">
                        <label>Mot de passe</label>
                        <input type="password" value={password} onChange={e => setPassword(e.target.value)} required />
                    </div>
                    <button type="submit" className="btn-primary" disabled={loading}>
                        {loading ? 'Connexion...' : 'Se connecter'}
                    </button>
                </form>
                <div className="demo-accounts">
                    <p>Comptes de test :</p>
                    <button onClick={() => demo('joel.soulama@ibam.ma')}>Étudiant</button>
                    <button onClick={() => demo('yaya.traore@ibam.ma')}>Enseignant</button>
                    <button onClick={() => demo('omar.tazi@ibam.ma')}>Scolarité</button>
                    <button onClick={() => demo('rachid.bennani@ibam.ma')}>DA</button>
                </div>
            </div>
        </div>
    );
}

function Dashboard() {
    const { user } = useAuth();
    const navigate = useNavigate();
    const role = user?.role?.replace('ROLE_', '');
    const { notifications, success, error: notifyError, warning } = useNotifications();

    const [reclamations, setReclamations] = useState([]);
    const [notes, setNotes] = useState([]);
    const [enseignants, setEnseignants] = useState([]);
    const [loading, setLoading] = useState(true);
    const [selected, setSelected] = useState(null);
    const [error, setError] = useState('');
    const getDefaultTab = (role) => {
        switch(role) {
            case 'SCOLARITE': return 'soumises';
            case 'DA': return 'transmises';
            case 'ENSEIGNANT': return 'imputees';
            case 'ETUDIANT': return 'reclamations';
            default: return 'reclamations';
        }
    };
    const [activeTab, setActiveTab] = useState(getDefaultTab(role));
    const [notesSubTab, setNotesSubTab] = useState(user?.niveau || 'L1'); // Niveau de l'étudiant

    // Form states
    const [noteId, setNoteId] = useState('');
    const [description, setDescription] = useState('');
    const [justificatif, setJustificatif] = useState(null);
    const [commentaire, setCommentaire] = useState('');
    const [nouvelleNote, setNouvelleNote] = useState('');
    const [nouvelleNoteProposee, setNouvelleNoteProposee] = useState('');
    const [enseignantId, setEnseignantId] = useState('');
    const [showForm, setShowForm] = useState(false);
    const [createLoading, setCreateLoading] = useState(false);
    const [periodeActive, setPeriodeActive] = useState(null);
    const [selectedReclamations, setSelectedReclamations] = useState([]);
    const [showBulkActions, setShowBulkActions] = useState(false);

    const checkPeriodeActive = async () => {
        try {
            const { data } = await periodeApi.getActive();
            setPeriodeActive(data);
        } catch (e) {
            console.error('Erreur période:', e);
        }
    };

    const fetchPeriodes = async () => {
        // No longer needed in Dashboard
    };

    const handleCreatePeriode = async (e) => {
        // No longer needed in Dashboard
    };

    const handleFermerPeriode = async (id) => {
        // No longer needed in Dashboard
    };

    const handleQuickAction = async (reclamation, action) => {
        try {
            switch (action) {
                case 'verifier-ok':
                    await reclamationApi.verifier(reclamation.id, true, null);
                    success('Réclamation acceptée');
                    break;
                case 'verifier-ko':
                    const comment = prompt('Commentaire obligatoire pour le rejet:');
                    if (!comment?.trim()) return;
                    await reclamationApi.verifier(reclamation.id, false, comment.trim());
                    success('Réclamation rejetée');
                    break;
                case 'imputer-auto':
                    await reclamationApi.imputerAuto(reclamation.id);
                    success('Réclamation imputée automatiquement');
                    break;
                case 'accepter':
                case 'refuser':
                    setSelected(reclamation);
                    return;
                case 'appliquer':
                    await reclamationApi.appliquer(reclamation.id, reclamation.nouvelleNoteProposee);
                    success('Décision appliquée');
                    break;
                case 'editer':
                    if (role === 'ETUDIANT' && reclamation.statut === 'SOUMISE' && periodeActive?.active) {
                        alert('Fonction édition à implémenter');
                    } else {
                        notifyError('Modification impossible : période fermée ou demande déjà traitée');
                    }
                    return;
                case 'supprimer':
                    if (role === 'ETUDIANT' && reclamation.statut === 'SOUMISE' && periodeActive?.active) {
                        if (confirm('Êtes-vous sûr de vouloir supprimer cette réclamation ?')) {
                            alert('Fonction suppression à implémenter');
                        }
                    } else {
                        notifyError('Suppression impossible : période fermée ou demande déjà traitée');
                    }
                    return;
            }
            fetchData();
        } catch (e) {
            ErrorHandler.log(e, 'Quick action');
            const errorMsg = ErrorHandler.getDisplayMessage(e);
            notifyError(errorMsg);
        }
    };

    const handleBulkImputation = async () => {
        if (selectedReclamations.length === 0) {
            notifyError('Aucune réclamation sélectionnée');
            return;
        }
        try {
            await reclamationApi.imputerLot(selectedReclamations);
            success(`${selectedReclamations.length} réclamations imputées`);
            setSelectedReclamations([]);
            setShowBulkActions(false);
            fetchData();
        } catch (e) {
            ErrorHandler.log(e, 'Bulk imputation');
            notifyError(ErrorHandler.getDisplayMessage(e));
        }
    };

    const toggleReclamationSelection = (id) => {
        setSelectedReclamations(prev => 
            prev.includes(id) 
                ? prev.filter(recId => recId !== id)
                : [...prev, id]
        );
    };

    const fetchData = async () => {
        try {
            setLoading(true);
            const { data } = await reclamationApi.getAll();
            setReclamations(data);
            if (role === 'ETUDIANT') {
                const n = await notesApi.getMesNotes();
                setNotes(n.data);
            }
            if (role === 'DA') {
                const e = await reclamationApi.getEnseignants();
                setEnseignants(e.data);
            }
        } catch (e) {
            ErrorHandler.log(e, 'Data fetch');
            setError(ErrorHandler.getDisplayMessage(e));
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => { 
        fetchData(); 
        checkPeriodeActive();
    }, []);

    const handleCreate = async (e) => {
        e.preventDefault();
        if (!noteId || !description.trim() || !justificatif) {
            setError('Tous les champs sont obligatoires');
            return;
        }

        setCreateLoading(true);
        setError('');
        try {
            const formData = new FormData();
            formData.append('noteId', noteId);
            formData.append('description', description.trim());
            formData.append('justificatif', justificatif);
            
            await reclamationApi.create(formData);
            success('Réclamation envoyée');
            setShowForm(false);
            setNoteId('');
            setDescription('');
            setJustificatif(null);
            fetchData();
        } catch (e) {
            ErrorHandler.log(e, 'Create reclamation');
            const errorMsg = ErrorHandler.getDisplayMessage(e);
            notifyError(errorMsg);
            setError(errorMsg);
        } finally {
            setCreateLoading(false);
        }
    };

    const handleAction = async (action) => {
        try {
            setError('');
            switch (action) {
                case 'verifier-ok': 
                    await reclamationApi.verifier(selected.id, true, commentaire.trim() || null); 
                    break;
                case 'verifier-ko': 
                    if (!commentaire.trim()) {
                        setError('Le commentaire est obligatoire pour expliquer le rejet.');
                        return;
                    }
                    await reclamationApi.verifier(selected.id, false, commentaire.trim()); 
                    break;
                case 'imputer-auto': 
                    await reclamationApi.imputerAuto(selected.id); 
                    break;
                case 'imputer': 
                    if (!enseignantId) {
                        setError('Veuillez sélectionner un enseignant.');
                        return;
                    }
                    await reclamationApi.imputer(selected.id, parseInt(enseignantId)); 
                    break;
                case 'accepter': 
                    if (!commentaire.trim() || !nouvelleNoteProposee) {
                        setError('Le commentaire et la nouvelle note sont obligatoires.');
                        return;
                    }
                    await reclamationApi.analyser(selected.id, true, commentaire.trim(), parseFloat(nouvelleNoteProposee)); 
                    break;
                case 'refuser': 
                    if (!commentaire.trim()) {
                        setError('Le commentaire est obligatoire pour refuser.');
                        return;
                    }
                    await reclamationApi.analyser(selected.id, false, commentaire.trim(), null); 
                    break;
                case 'appliquer':
                    await reclamationApi.appliquer(selected.id, null);
                    success('Décision de l\'enseignant appliquée');
                    break;
            }
            setSelected(null);
            setCommentaire('');
            setNouvelleNote('');
            setNouvelleNoteProposee('');
            setEnseignantId('');
            success('Action exécutée');
            fetchData();
        } catch (e) {
            ErrorHandler.log(e, 'Action execution');
            const errorMsg = ErrorHandler.getDisplayMessage(e);
            notifyError(errorMsg);
            setError(errorMsg);
        }
    };

    const stats = {
        total: reclamations.length,
        pending: reclamations.filter(r => ['SOUMISE', 'TRANSMISE_DA', 'IMPUTEE'].includes(r.statut)).length,
    };

    const titles = { ETUDIANT: 'Mes Réclamations', ENSEIGNANT: 'Réclamations à Analyser', SCOLARITE: 'Toutes les Réclamations', DA: 'Supervision des Réclamations' };

    return (
        <div className="dashboard">
            <Navbar title={titles[role] || 'Dashboard'} />
            <div className="main-content">
                {error && <div className="error">{error}</div>}

                <div className="page-header">
                    <h2>{titles[role]}</h2>
                    <div style={{ display: 'flex', gap: '10px' }}>
                        {role === 'DA' && (
                            <button className="btn-primary" onClick={() => navigate('/da/periodes')}>
                                Gérer périodes
                            </button>
                        )}
                        {role === 'ETUDIANT' && ['reclamations', 'en-cours', 'terminees'].includes(activeTab) && (
                            <button 
                                className="btn-primary" 
                                onClick={() => setShowForm(true)}
                                disabled={!periodeActive?.active}
                            >
                                Nouvelle réclamation
                            </button>
                        )}
                    </div>
                </div>

                {/* Alerte période pour étudiants */}
                {role === 'ETUDIANT' && (
                    <div className={`periode-alert ${periodeActive?.active ? 'periode-active' : 'periode-inactive'}`}>
                        {periodeActive?.active ? (
                            <div className="periode-content">
                                <div className="periode-icon">🟢</div>
                                <div className="periode-info">
                                    <div className="periode-title">Période ouverte</div>
                                    <div className="periode-details">{periodeActive.nom} - {periodeActive.heuresRestantes}h restantes</div>
                                </div>
                            </div>
                        ) : (
                            <div className="periode-content periode-closed">
                                <div className="periode-icon-container">
                                    <div className="periode-icon-bg"></div>
                                    <div className="periode-icon">🚫</div>
                                </div>
                                <div className="periode-info">
                                    <div className="periode-title shake">Période fermée</div>
                                    <div className="periode-details">Aucune réclamation n'est acceptée pour le moment</div>
                                    <div className="periode-countdown">Prochaine ouverture : À déterminer</div>
                                </div>
                                <div className="periode-status-indicator"></div>
                            </div>
                        )}
                    </div>
                )}

                {/* Domaines pour DA */}
                {role === 'DA' && (
                    <div className="tabs">
                        <button 
                            className={activeTab === 'transmises' ? 'tab-active' : ''}
                            onClick={() => setActiveTab('transmises')}
                        >
                            À imputer ({reclamations.filter(r => r.statut === 'TRANSMISE_DA').length})
                        </button>
                        <button 
                            className={activeTab === 'en-cours' ? 'tab-active' : ''}
                            onClick={() => setActiveTab('en-cours')}
                        >
                            En cours ({reclamations.filter(r => r.statut === 'IMPUTEE').length})
                        </button>
                        <button 
                            className={activeTab === 'decisions' ? 'tab-active' : ''}
                            onClick={() => setActiveTab('decisions')}
                        >
                            Décisions ({reclamations.filter(r => ['ACCEPTEE', 'REFUSEE'].includes(r.statut)).length})
                        </button>
                        <button 
                            className={activeTab === 'terminees' ? 'tab-active' : ''}
                            onClick={() => setActiveTab('terminees')}
                        >
                            Terminées ({reclamations.filter(r => ['APPLIQUEE', 'REJETEE'].includes(r.statut)).length})
                        </button>
                        <button 
                            className={activeTab === 'toutes' ? 'tab-active' : ''}
                            onClick={() => setActiveTab('toutes')}
                        >
                            Toutes ({stats.total})
                        </button>
                    </div>
                )}

                {/* Actions en lot pour DA */}
                {role === 'DA' && activeTab === 'transmises' && (
                    <div className="bulk-actions">
                        <button 
                            className="btn-secondary"
                            onClick={() => setShowBulkActions(!showBulkActions)}
                        >
                            {showBulkActions ? 'Annuler sélection' : 'Sélection multiple'}
                        </button>
                        {showBulkActions && selectedReclamations.length > 0 && (
                            <button 
                                className="btn-primary"
                                onClick={handleBulkImputation}
                            >
                                Imputer {selectedReclamations.length} réclamation(s)
                            </button>
                        )}
                    </div>
                )}

                {/* Domaines pour Enseignant */}
                {role === 'ENSEIGNANT' && (
                    <div className="tabs">
                        <button 
                            className={activeTab === 'imputees' ? 'tab-active' : ''}
                            onClick={() => setActiveTab('imputees')}
                        >
                            À analyser ({reclamations.filter(r => r.statut === 'IMPUTEE').length})
                        </button>
                        <button 
                            className={activeTab === 'acceptees' ? 'tab-active' : ''}
                            onClick={() => setActiveTab('acceptees')}
                        >
                            Acceptées ({reclamations.filter(r => r.statut === 'ACCEPTEE').length})
                        </button>
                        <button 
                            className={activeTab === 'refusees' ? 'tab-active' : ''}
                            onClick={() => setActiveTab('refusees')}
                        >
                            Refusées ({reclamations.filter(r => r.statut === 'REFUSEE').length})
                        </button>
                        <button 
                            className={activeTab === 'appliquees' ? 'tab-active' : ''}
                            onClick={() => setActiveTab('appliquees')}
                        >
                            Appliquées ({reclamations.filter(r => r.statut === 'APPLIQUEE').length})
                        </button>
                        <button 
                            className={activeTab === 'toutes' ? 'tab-active' : ''}
                            onClick={() => setActiveTab('toutes')}
                        >
                            Toutes ({stats.total})
                        </button>
                    </div>
                )}

                {/* Stats avec domaines pour Scolarité */}
                {role === 'SCOLARITE' && (
                    <div className="tabs">
                        <button 
                            className={activeTab === 'soumises' ? 'tab-active' : ''}
                            onClick={() => setActiveTab('soumises')}
                        >
                            À vérifier ({reclamations.filter(r => r.statut === 'SOUMISE').length})
                        </button>
                        <button 
                            className={activeTab === 'decisions' ? 'tab-active' : ''}
                            onClick={() => setActiveTab('decisions')}
                        >
                            Décisions prof ({reclamations.filter(r => ['ACCEPTEE', 'REFUSEE'].includes(r.statut)).length})
                        </button>
                        <button 
                            className={activeTab === 'appliquees' ? 'tab-active' : ''}
                            onClick={() => setActiveTab('appliquees')}
                        >
                            Appliquées ({reclamations.filter(r => r.statut === 'APPLIQUEE').length})
                        </button>
                        <button 
                            className={activeTab === 'rejetees' ? 'tab-active' : ''}
                            onClick={() => setActiveTab('rejetees')}
                        >
                            Rejetées ({reclamations.filter(r => r.statut === 'REJETEE').length})
                        </button>
                        <button 
                            className={activeTab === 'toutes' ? 'tab-active' : ''}
                            onClick={() => setActiveTab('toutes')}
                        >
                            Toutes ({stats.total})
                        </button>
                    </div>
                )}

                {/* Stats simples pour Étudiant uniquement */}
                {role === 'ETUDIANT' && (
                    <div style={{ display: 'none' }}>
                        <div className="stat-item">
                            <span className="stat-number">{stats.total}</span>
                            <span className="stat-label">Total</span>
                        </div>
                        <div className="stat-item">
                            <span className="stat-number">{stats.pending}</span>
                            <span className="stat-label">En cours</span>
                        </div>
                    </div>
                )}

                {/* Tabs pour étudiant */}
                {role === 'ETUDIANT' && (
                    <div className="tabs">
                        <button 
                            className={activeTab === 'reclamations' ? 'tab-active' : ''}
                            onClick={() => setActiveTab('reclamations')}
                        >
                            Toutes ({reclamations.length})
                        </button>
                        <button 
                            className={activeTab === 'en-cours' ? 'tab-active' : ''}
                            onClick={() => setActiveTab('en-cours')}
                        >
                            En cours ({reclamations.filter(r => ['SOUMISE', 'TRANSMISE_DA', 'IMPUTEE'].includes(r.statut)).length})
                        </button>
                        <button 
                            className={activeTab === 'terminees' ? 'tab-active' : ''}
                            onClick={() => setActiveTab('terminees')}
                        >
                            Terminées ({reclamations.filter(r => ['ACCEPTEE', 'REFUSEE', 'APPLIQUEE', 'REJETEE'].includes(r.statut)).length})
                        </button>
                        <button 
                            className={activeTab === 'notes' ? 'tab-active' : ''}
                            onClick={() => setActiveTab('notes')}
                        >
                            Mes notes
                        </button>
                    </div>
                )}

                {/* Sous-onglets pour les notes */}
                {role === 'ETUDIANT' && activeTab === 'notes' && (
                    <div className="sub-tabs">
                        <button 
                            className={notesSubTab === 'L1' ? 'sub-tab-active' : ''}
                            onClick={() => setNotesSubTab('L1')}
                        >
                            Licence 1
                        </button>
                        <button 
                            className={notesSubTab === 'L2' ? 'sub-tab-active' : ''}
                            onClick={() => setNotesSubTab('L2')}
                        >
                            Licence 2
                        </button>
                        <button 
                            className={notesSubTab === 'L3' ? 'sub-tab-active' : ''}
                            onClick={() => setNotesSubTab('L3')}
                        >
                            Licence 3   
                        </button>
                    </div>
                )}

                {loading ? (
                    <div className="loading">Chargement...</div>
                ) : (
                    role === 'ETUDIANT' && activeTab === 'notes' ? (
                        <div className="notes-list">
                            <h3>Mes notes - {user?.filiere || 'MIAGE'} {notesSubTab}</h3>
                            {notes.filter(n => n.niveau === notesSubTab).length === 0 ? (
                                <div className="empty">Aucune note pour ce niveau</div>
                            ) : (
                                <div>
                                    {/* Grouper par semestre */}
                                    {['S1', 'S2', 'S3', 'S4', 'S5', 'S6'].filter(semestre => {
                                        const niveauSemestres = {
                                            'L1': ['S1', 'S2'],
                                            'L2': ['S3', 'S4'], 
                                            'L3': ['S5', 'S6']
                                        };
                                        return niveauSemestres[notesSubTab]?.includes(semestre);
                                    }).map(semestre => {
                                        const notesSemestre = notes.filter(n => n.niveau === notesSubTab && n.semestre === semestre);
                                        if (notesSemestre.length === 0) return null;
                                        
                                        return (
                                            <div key={semestre} style={{ marginBottom: '30px' }}>
                                                <h4>{semestre}</h4>
                                                <table className="notes-table">
                                                    <thead>
                                                        <tr>
                                                            <th>Matière</th>
                                                            <th>Note</th>
                                                            <th>Action</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        {notesSemestre.map(n => (
                                                            <tr key={n.id}>
                                                                <td>{n.matiereNom}</td>
                                                                <td className={n.valeur < 10 ? 'note-fail' : 'note-pass'}>
                                                                    {n.valeur}/20
                                                                </td>
                                                                <td>
                                                                    {notesSubTab === user?.niveau ? (
                                                                        <button 
                                                                            className="btn-small"
                                                                            onClick={() => {
                                                                                if (!periodeActive?.active) {
                                                                                    notifyError('Aucune période de réclamation n\'est ouverte');
                                                                                    return;
                                                                                }
                                                                                if (reclamations.some(r => r.noteId === n.id)) {
                                                                                    notifyError('Une réclamation existe déjà pour cette note');
                                                                                    return;
                                                                                }
                                                                                setNoteId(n.id);
                                                                                setActiveTab('reclamations');
                                                                                setShowForm(true);
                                                                            }}
                                                                            disabled={reclamations.some(r => r.noteId === n.id) || !periodeActive?.active}
                                                                        >
                                                                            {reclamations.some(r => r.noteId === n.id) ? 'Déjà réclamée' : !periodeActive?.active ? 'Période fermée' : 'Réclamer'}
                                                                        </button>
                                                                    ) : (
                                                                        <span className="text-muted">Non réclamable</span>
                                                                    )}
                                                                </td>
                                                            </tr>
                                                        ))}
                                                    </tbody>
                                                </table>
                                            </div>
                                        );
                                    })}
                                </div>
                            )}
                        </div>
                    ) : (
                        <div className="reclamations-list">
                            {(() => {
                                let filteredReclamations = reclamations;
                                if (role === 'ETUDIANT') {
                                    if (activeTab === 'en-cours') {
                                        filteredReclamations = reclamations.filter(r => ['SOUMISE', 'TRANSMISE_DA', 'IMPUTEE'].includes(r.statut));
                                    } else if (activeTab === 'terminees') {
                                        filteredReclamations = reclamations.filter(r => ['ACCEPTEE', 'REFUSEE', 'APPLIQUEE', 'REJETEE'].includes(r.statut));
                                    }
                                } else if (role === 'SCOLARITE') {
                                    if (activeTab === 'soumises') {
                                        filteredReclamations = reclamations.filter(r => r.statut === 'SOUMISE');
                                    } else if (activeTab === 'decisions') {
                                        filteredReclamations = reclamations.filter(r => ['ACCEPTEE', 'REFUSEE'].includes(r.statut));
                                    } else if (activeTab === 'appliquees') {
                                        filteredReclamations = reclamations.filter(r => r.statut === 'APPLIQUEE');
                                    } else if (activeTab === 'rejetees') {
                                        filteredReclamations = reclamations.filter(r => r.statut === 'REJETEE');
                                    }
                                } else if (role === 'DA') {
                                    if (activeTab === 'transmises') {
                                        filteredReclamations = reclamations.filter(r => r.statut === 'TRANSMISE_DA');
                                    } else if (activeTab === 'en-cours') {
                                        filteredReclamations = reclamations.filter(r => r.statut === 'IMPUTEE');
                                    } else if (activeTab === 'decisions') {
                                        filteredReclamations = reclamations.filter(r => ['ACCEPTEE', 'REFUSEE'].includes(r.statut));
                                    } else if (activeTab === 'terminees') {
                                        filteredReclamations = reclamations.filter(r => ['APPLIQUEE', 'REJETEE'].includes(r.statut));
                                    }
                                } else if (role === 'ENSEIGNANT') {
                                    if (activeTab === 'imputees') {
                                        filteredReclamations = reclamations.filter(r => r.statut === 'IMPUTEE');
                                    } else if (activeTab === 'acceptees') {
                                        filteredReclamations = reclamations.filter(r => r.statut === 'ACCEPTEE');
                                    } else if (activeTab === 'refusees') {
                                        filteredReclamations = reclamations.filter(r => r.statut === 'REFUSEE');
                                    } else if (activeTab === 'appliquees') {
                                        filteredReclamations = reclamations.filter(r => r.statut === 'APPLIQUEE');
                                    }
                                }
                                return filteredReclamations.length === 0 ? (
                                    <div className="empty">Aucune réclamation</div>
                                ) : (
                                    filteredReclamations.map(r => (
                                        <ReclamationCard 
                                            key={r.id} 
                                            r={r} 
                                            role={role}
                                            onClick={() => setSelected(r)} 
                                            onQuickAction={handleQuickAction}
                                            isSelectable={role === 'DA' && activeTab === 'transmises' && showBulkActions && r.statut === 'TRANSMISE_DA'}
                                            isSelected={selectedReclamations.includes(r.id)}
                                            onToggleSelect={toggleReclamationSelection}
                                        />
                                    ))
                                );
                            })()
                            }
                        </div>
                    )
                )}
            </div>

            {/* Modal Création */}
            {showForm && (
                <div className="modal-overlay" onClick={() => setShowForm(false)}>
                    <div className="modal" onClick={e => e.stopPropagation()}>
                        <div className="modal-header">
                            <h3>Nouvelle réclamation</h3>
                            <button className="modal-close" onClick={() => setShowForm(false)}>×</button>
                        </div>
                        <form onSubmit={handleCreate}>
                            <div className="form-group">
                                <label>Note</label>
                                <select value={noteId} onChange={e => setNoteId(e.target.value)} required>
                                    <option value="">-- Choisir une note --</option>
                                    {notes
                                        .filter(n => n.niveau === notesSubTab && !reclamations.some(r => r.noteId === n.id))
                                        .map(n => (
                                        <option key={n.id} value={n.id}>
                                            {n.matiereNom} ({n.semestre}) - {n.valeur}/20
                                        </option>
                                    ))}
                                </select>
                            </div>
                            <div className="form-group">
                                <label>Description</label>
                                <textarea 
                                    value={description} 
                                    onChange={e => setDescription(e.target.value)} 
                                    placeholder="Décrivez votre réclamation"
                                    required 
                                />
                            </div>
                            <div className="form-group">
                                <label>Justificatif (PDF, JPEG, PNG)</label>
                                <input 
                                    type="file" 
                                    accept=".pdf,.jpg,.jpeg,.png"
                                    onChange={e => setJustificatif(e.target.files[0])}
                                    required 
                                />
                            </div>
                            <div className="form-actions">
                                <button type="button" onClick={() => setShowForm(false)}>
                                    Annuler
                                </button>
                                <button type="submit" className="btn-primary" disabled={createLoading}>
                                    {createLoading ? 'Envoi...' : 'Soumettre'}
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            )}

            {/* Modal Détail */}
            {selected && (
                <div className="modal-overlay" onClick={() => setSelected(null)}>
                    <div className="modal" onClick={e => e.stopPropagation()}>
                        <div className="modal-header">
                            <h3>Réclamation - {selected.matiereNom} ({selected.semestre})</h3>
                            <button className="modal-close" onClick={() => setSelected(null)}>×</button>
                        </div>

                        <div className="modal-content">
                            <p><strong>Étudiant:</strong> {selected.etudiantPrenom} {selected.etudiantNom}</p>
                            <p><strong>Note:</strong> {selected.noteValeur}/20</p>
                            <p><strong>Statut:</strong> {selected.statut}</p>
                            <p><strong>Description:</strong></p>
                            <p className="description">{selected.description}</p>
                            
                            {selected.hasJustificatif && (
                                <p>
                                    <strong>Justificatif:</strong>
                                    <button 
                                        className="btn-small"
                                        onClick={() => {
                                            reclamationApi.getJustificatif(selected.id)
                                                .then(response => {
                                                    const url = window.URL.createObjectURL(new Blob([response.data]));
                                                    const link = document.createElement('a');
                                                    link.href = url;
                                                    link.setAttribute('download', selected.justificatifNom || 'justificatif');
                                                    document.body.appendChild(link);
                                                    link.click();
                                                    link.remove();
                                                })
                                                .catch(e => notifyError('Erreur lors du téléchargement'));
                                        }}
                                    >
                                        Télécharger
                                    </button>
                                </p>
                            )}
                        </div>

                        {/* Actions selon le rôle */}
                        <div className="modal-actions">
                            {role === 'SCOLARITE' && selected.statut === 'SOUMISE' && (
                                <div>
                                    <div className="form-group">
                                        <label>Commentaire</label>
                                        <input 
                                            value={commentaire} 
                                            onChange={e => setCommentaire(e.target.value)}
                                            placeholder="Commentaire (obligatoire pour rejet)"
                                        />
                                    </div>
                                    <button onClick={() => handleAction('verifier-ko')} disabled={!commentaire.trim()}>
                                        Rejeter
                                    </button>
                                    <button className="btn-primary" onClick={() => handleAction('verifier-ok')}>
                                        Accepter
                                    </button>
                                </div>
                            )}

                            {role === 'DA' && selected.statut === 'TRANSMISE_DA' && (
                                <div>
                                    <div className="form-group">
                                        <label>Enseignant</label>
                                        <select value={enseignantId} onChange={e => setEnseignantId(e.target.value)}>
                                            <option value="">-- Automatique --</option>
                                            {enseignants.map(e => (
                                                <option key={e.id} value={e.id}>
                                                    {e.prenom} {e.nom}
                                                </option>
                                            ))}
                                        </select>
                                    </div>
                                    <button onClick={() => handleAction('imputer-auto')}>
                                        Imputation automatique
                                    </button>
                                    <button onClick={() => handleAction('imputer')} disabled={!enseignantId}>
                                        Imputer manuellement
                                    </button>
                                </div>
                            )}

                            {role === 'ENSEIGNANT' && selected.statut === 'IMPUTEE' && (
                                <div>
                                    <div className="form-group">
                                        <label>Commentaire</label>
                                        <input 
                                            value={commentaire} 
                                            onChange={e => setCommentaire(e.target.value)}
                                            placeholder="Commentaire obligatoire"
                                            required
                                        />
                                    </div>
                                    <div className="form-group">
                                        <label>Nouvelle note proposée</label>
                                        <input 
                                            type="number" 
                                            min="0" 
                                            max="20" 
                                            step="0.5" 
                                            value={nouvelleNoteProposee} 
                                            onChange={e => setNouvelleNoteProposee(e.target.value)}
                                            placeholder="Note si acceptée"
                                            required
                                        />
                                    </div>
                                    <button onClick={() => handleAction('refuser')} disabled={!commentaire.trim()}>
                                        Refuser
                                    </button>
                                    <button 
                                        className="btn-primary" 
                                        onClick={() => handleAction('accepter')} 
                                        disabled={!commentaire.trim() || !nouvelleNoteProposee}
                                    >
                                        Accepter
                                    </button>
                                </div>
                            )}

                            {role === 'ENSEIGNANT' && ['ACCEPTEE', 'REFUSEE'].includes(selected.statut) && (
                                <div>
                                    {selected.statut === 'ACCEPTEE' && (
                                        <div className="form-group">
                                            <p><strong>Votre décision:</strong> Accepter la réclamation</p>
                                            <p><strong>Note à appliquer:</strong> {selected.nouvelleNoteProposee}/20</p>
                                        </div>
                                    )}
                                    {selected.statut === 'REFUSEE' && (
                                        <div className="form-group">
                                            <p><strong>Votre décision:</strong> Refuser la réclamation</p>
                                            <p style={{ fontSize: '14px', color: '#666' }}>La note restera inchangée</p>
                                        </div>
                                    )}
                                    <button className="btn-primary" onClick={() => handleAction('appliquer')}>
                                        Appliquer ma décision maintenant
                                    </button>
                                </div>
                            )}

                            {role === 'SCOLARITE' && ['ACCEPTEE', 'REFUSEE'].includes(selected.statut) && (
                                <div>
                                    {selected.statut === 'ACCEPTEE' && (
                                        <div className="form-group">
                                            <p><strong>Décision de l'enseignant:</strong> Accepter la réclamation</p>
                                            <p><strong>Note à appliquer:</strong> {selected.nouvelleNoteProposee}/20</p>
                                            <p style={{ fontSize: '14px', color: '#666' }}>Cette note sera appliquée automatiquement</p>
                                        </div>
                                    )}
                                    {selected.statut === 'REFUSEE' && (
                                        <div className="form-group">
                                            <p><strong>Décision de l'enseignant:</strong> Refuser la réclamation</p>
                                            <p style={{ fontSize: '14px', color: '#666' }}>La note restera inchangée</p>
                                        </div>
                                    )}
                                    <button className="btn-primary" onClick={() => handleAction('appliquer')}>
                                        Appliquer la décision de l'enseignant
                                    </button>
                                </div>
                            )}
                        </div>
                    </div>
                </div>
            )}

            
            <NotificationContainer notifications={notifications} />
        </div>
    );
}

// ============ ROUTING ============
function ProtectedRoute({ children }) {
    const { isAuth, loading } = useAuth();
    if (loading) return <div className="loading">Chargement...</div>;
    return isAuth ? children : <Navigate to="/login" />;
}

function RoleRedirect() {
    const { user, isAuth } = useAuth();
    if (!isAuth) return <Navigate to="/login" />;
    const role = user?.role?.replace('ROLE_', '').toLowerCase();
    return <Navigate to={`/${role}/dashboard`} />;
}

export default function App() {
    return (
        <BrowserRouter>
            <AuthProvider>
                <Routes>
                    <Route path="/login" element={<Login />} />
                    <Route path="/etudiant/dashboard" element={<ProtectedRoute><Dashboard /></ProtectedRoute>} />
                    <Route path="/enseignant/dashboard" element={<ProtectedRoute><Dashboard /></ProtectedRoute>} />
                    <Route path="/scolarite/dashboard" element={<ProtectedRoute><Dashboard /></ProtectedRoute>} />
                    <Route path="/da/dashboard" element={<ProtectedRoute><Dashboard /></ProtectedRoute>} />
                    <Route path="/da/periodes" element={<ProtectedRoute><GestionPeriodes /></ProtectedRoute>} />
                    <Route path="*" element={<RoleRedirect />} />
                </Routes>
            </AuthProvider>
        </BrowserRouter>
    );
}