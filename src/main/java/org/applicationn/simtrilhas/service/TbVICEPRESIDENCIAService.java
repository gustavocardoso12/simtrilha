package org.applicationn.simtrilhas.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Named;
import javax.transaction.Transactional;

import org.applicationn.simtrilhas.domain.TbVICEPRESIDENCIAEntity;
import org.applicationn.simtrilhas.service.security.SecurityWrapper;

@Named
public class TbVICEPRESIDENCIAService extends BaseService<TbVICEPRESIDENCIAEntity> implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public TbVICEPRESIDENCIAService(){
        super(TbVICEPRESIDENCIAEntity.class);
    }
    
    @Transactional
    public List<TbVICEPRESIDENCIAEntity> findAllTbVICEPRESIDENCIAEntities() {
        
        return getEntityManager().createQuery("SELECT o FROM TbVICEPRESIDENCIA o ", TbVICEPRESIDENCIAEntity.class).getResultList();
    }
    
    @Override
    @Transactional
    public long countAllEntries() {
        return getEntityManager().createQuery("SELECT COUNT(o) FROM TbVICEPRESIDENCIA o", Long.class).getSingleResult();
    }
    
    @Override
    @Transactional
    public TbVICEPRESIDENCIAEntity save(TbVICEPRESIDENCIAEntity tbVICEPRESIDENCIA) {
        String username = SecurityWrapper.getUsername();
        
        tbVICEPRESIDENCIA.updateAuditInformation(username);
        
        return super.save(tbVICEPRESIDENCIA);
    }
    
    @Override
    @Transactional
    public TbVICEPRESIDENCIAEntity update(TbVICEPRESIDENCIAEntity tbVICEPRESIDENCIA) {
        String username = SecurityWrapper.getUsername();
        tbVICEPRESIDENCIA.updateAuditInformation(username);
        return super.update(tbVICEPRESIDENCIA);
    }
    
    @Override
    protected void handleDependenciesBeforeDelete(TbVICEPRESIDENCIAEntity tbVICEPRESIDENCIA) {
        
        this.cutAllIdVPTbDIRETORIAsAssignments(tbVICEPRESIDENCIA);
        
    }

    // Remove all assignments from all tbDIRETORIA a tbVICEPRESIDENCIA. Called before delete a tbVICEPRESIDENCIA.
    @Transactional
    private void cutAllIdVPTbDIRETORIAsAssignments(TbVICEPRESIDENCIAEntity tbVICEPRESIDENCIA) {
    	getEntityManager()
                .createQuery("UPDATE TbDIRETORIA c SET c.idVP = NULL WHERE c.idVP = :p")
                .setParameter("p", tbVICEPRESIDENCIA).executeUpdate();
    }
    
}
