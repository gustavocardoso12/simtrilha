package org.applicationn.simtrilhas.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Named;
import javax.transaction.Transactional;

import org.applicationn.simtrilhas.domain.TbDIRETORIAEntity;
import org.applicationn.simtrilhas.domain.TbVICEPRESIDENCIAEntity;
import org.applicationn.simtrilhas.service.security.SecurityWrapper;

@Named
public class TbDIRETORIAService extends BaseService<TbDIRETORIAEntity> implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public TbDIRETORIAService(){
        super(TbDIRETORIAEntity.class);
    }
    
    @Transactional
    public List<TbDIRETORIAEntity> findAllTbDIRETORIAEntities() {
        
        return entityManager.createQuery("SELECT o FROM TbDIRETORIA o ", TbDIRETORIAEntity.class).getResultList();
    }
    
    @Transactional
    public List<TbDIRETORIAEntity> findTbDIRETORIAVP(int idVP) {
        Long n = (long) idVP;
     	System.out.println(idVP);
        return entityManager.createQuery("SELECT o FROM TbDIRETORIA o WHERE o.idVP.id= :idVP ", TbDIRETORIAEntity.class).setMaxResults(1).setParameter("idVP", n).getResultList();
    }
    
    @Override
    @Transactional
    public long countAllEntries() {
        return entityManager.createQuery("SELECT COUNT(o) FROM TbDIRETORIA o", Long.class).getSingleResult();
    }
    
    @Override
    @Transactional
    public TbDIRETORIAEntity save(TbDIRETORIAEntity tbDIRETORIA) {
        String username = SecurityWrapper.getUsername();
        
        tbDIRETORIA.updateAuditInformation(username);
        
        return super.save(tbDIRETORIA);
    }
    
    @Override
    @Transactional
    public TbDIRETORIAEntity update(TbDIRETORIAEntity tbDIRETORIA) {
        String username = SecurityWrapper.getUsername();
        tbDIRETORIA.updateAuditInformation(username);
        return super.update(tbDIRETORIA);
    }
    
    @Override
    protected void handleDependenciesBeforeDelete(TbDIRETORIAEntity tbDIRETORIA) {

        /* This is called before a TbDIRETORIA is deleted. Place here all the
           steps to cut dependencies to other entities */
        
        this.cutAllIdDIRETORIATbESTATUARIOsAssignments(tbDIRETORIA);
        
    }

    // Remove all assignments from all tbESTATUARIO a tbDIRETORIA. Called before delete a tbDIRETORIA.
    @Transactional
    private void cutAllIdDIRETORIATbESTATUARIOsAssignments(TbDIRETORIAEntity tbDIRETORIA) {
        entityManager
                .createQuery("UPDATE TbESTATUARIO c SET c.idDIRETORIA = NULL WHERE c.idDIRETORIA = :p")
                .setParameter("p", tbDIRETORIA).executeUpdate();
    }
    
    @Transactional
    public List<TbDIRETORIAEntity> findAvailableTbDIRETORIAs(TbVICEPRESIDENCIAEntity tbVICEPRESIDENCIA) {
        return entityManager.createQuery("SELECT o FROM TbDIRETORIA o WHERE o.idVP IS NULL", TbDIRETORIAEntity.class).getResultList();
    }

    @Transactional
    public List<TbDIRETORIAEntity> findTbDIRETORIAsByIdVP(TbVICEPRESIDENCIAEntity tbVICEPRESIDENCIA) {
        return entityManager.createQuery("SELECT o FROM TbDIRETORIA o WHERE o.idVP = :tbVICEPRESIDENCIA", TbDIRETORIAEntity.class).setParameter("tbVICEPRESIDENCIA", tbVICEPRESIDENCIA).getResultList();
    }

}
