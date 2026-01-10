// Machine à états centralisée pour les réclamations
export const ReclamationStatus = {
    SOUMISE: 'SOUMISE',
    TRANSMISE_DA: 'TRANSMISE_DA',
    IMPUTEE: 'IMPUTEE',
    ACCEPTEE: 'ACCEPTEE',
    REFUSEE: 'REFUSEE',
    APPLIQUEE: 'APPLIQUEE',
    REJETEE: 'REJETEE'
};

export const StatusLabels = {
    [ReclamationStatus.SOUMISE]: 'Soumise',
    [ReclamationStatus.TRANSMISE_DA]: 'Transmise DA',
    [ReclamationStatus.IMPUTEE]: 'Imputée',
    [ReclamationStatus.ACCEPTEE]: 'Acceptée',
    [ReclamationStatus.REFUSEE]: 'Refusée',
    [ReclamationStatus.APPLIQUEE]: 'Appliquée',
    [ReclamationStatus.REJETEE]: 'Rejetée'
};

export const getActionsForRole = (role, status) => {
    const actions = [{ label: 'Voir', action: 'voir', className: 'btn-small' }];
    
    const actionMap = {
        ETUDIANT: {
            [ReclamationStatus.SOUMISE]: [
                { label: 'Éditer', action: 'editer', className: 'btn-small' },
                { label: 'Supprimer', action: 'supprimer', className: 'btn-danger' }
            ]
        },
        SCOLARITE: {
            [ReclamationStatus.SOUMISE]: [
                { label: 'Accepter', action: 'verifier-ok', className: 'btn-success' },
                { label: 'Rejeter', action: 'verifier-ko', className: 'btn-danger' }
            ],
            [ReclamationStatus.ACCEPTEE]: [{ label: 'Appliquer', action: 'appliquer', className: 'btn-primary' }],
            [ReclamationStatus.REFUSEE]: [{ label: 'Appliquer', action: 'appliquer', className: 'btn-primary' }]
        },
        DA: {
            [ReclamationStatus.TRANSMISE_DA]: [{ label: 'Imputer', action: 'imputer-auto', className: 'btn-primary' }],
            [ReclamationStatus.SOUMISE]: [
                { label: 'Éditer', action: 'editer', className: 'btn-small' },
                { label: 'Supprimer', action: 'supprimer', className: 'btn-danger' }
            ]
        },
        ENSEIGNANT: {
            [ReclamationStatus.IMPUTEE]: [
                { label: 'Accepter', action: 'accepter', className: 'btn-success' },
                { label: 'Refuser', action: 'refuser', className: 'btn-danger' }
            ],
            [ReclamationStatus.ACCEPTEE]: [{ label: 'Appliquer', action: 'appliquer', className: 'btn-primary' }],
            [ReclamationStatus.REFUSEE]: [{ label: 'Appliquer', action: 'appliquer', className: 'btn-primary' }]
        }
    };
    
    return [...actions, ...(actionMap[role]?.[status] || [])];
};