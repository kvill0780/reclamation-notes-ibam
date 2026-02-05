import { StatusLabels, getActionsForRole } from '../utils/reclamationStatus';

export function ReclamationCard({ r, onClick, role, onQuickAction, isSelectable, isSelected, onToggleSelect }) {
    const actions = getActionsForRole(role, r.statut);

    return (
        <div className={`reclamation-card ${isSelected ? 'selected' : ''}`}>
            {isSelectable && (
                <div className="card-checkbox">
                    <input 
                        type="checkbox" 
                        checked={isSelected}
                        onChange={() => onToggleSelect(r.id)}
                        onClick={e => e.stopPropagation()}
                    />
                </div>
            )}
            <div className="card-header">
                <h3 onClick={onClick} style={{ cursor: 'pointer' }}>{r.matiereNom} ({r.semestre})</h3>
                <span className={`status status-${r.statut.toLowerCase()}`}>
                    {StatusLabels[r.statut]}
                </span>
            </div>
            <div className="card-content" onClick={onClick} style={{ cursor: 'pointer' }}>
                <p><strong>Note:</strong> {r.noteValeur}/20</p>
                <p><strong>Étudiant:</strong> {r.etudiantPrenom} {r.etudiantNom}</p>
                {role === 'DA' && r.enseignantResponsableNom && (
                    <p><strong>Enseignant responsable:</strong> {r.enseignantResponsablePrenom} {r.enseignantResponsableNom}</p>
                )}
                {r.enseignantImputeNom && (
                    <p><strong>Imputé à:</strong> {r.enseignantImputePrenom} {r.enseignantImputeNom}</p>
                )}
                {r.nouvelleNoteProposee && <p><strong>Note proposée:</strong> {r.nouvelleNoteProposee}/20</p>}
                {role === 'ETUDIANT' && r.statut === 'APPLIQUEE' && r.commentaireEnseignant && (
                    <div className="comments-section">
                        <p><strong>Commentaire enseignant:</strong> {r.commentaireEnseignant}</p>
                    </div>
                )}
                {role === 'ETUDIANT' && r.statut === 'REJETEE' && r.commentaireScolarite && (
                    <div className="comments-section">
                        <p><strong>Commentaire scolarité:</strong> {r.commentaireScolarite}</p>
                    </div>
                )}
            </div>
            <div className="card-actions">
                {actions.map((action, index) => (
                    <button
                        key={index}
                        className={`btn-small ${action.className}`}
                        onClick={(e) => {
                            e.stopPropagation();
                            action.action === 'voir' ? onClick() : onQuickAction(r, action.action);
                        }}
                    >
                        {action.label}
                    </button>
                ))}
            </div>
        </div>
    );
}