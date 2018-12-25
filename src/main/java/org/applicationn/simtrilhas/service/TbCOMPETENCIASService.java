package org.applicationn.simtrilhas.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Named;
import javax.transaction.Transactional;

import org.applicationn.simtrilhas.domain.TbCOMPETENCIASEntity;
import org.applicationn.simtrilhas.service.security.SecurityWrapper;

@Named
public class TbCOMPETENCIASService extends BaseService<TbCOMPETENCIASEntity> implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public TbCOMPETENCIASService(){
        super(TbCOMPETENCIASEntity.class);
    }
    
    @Transactional
    public List<TbCOMPETENCIASEntity> findAllTbCOMPETENCIASEntities() {
        
        return entityManager.createQuery("SELECT o FROM TbCOMPETENCIAS o ", TbCOMPETENCIASEntity.class).getResultList();
    }
    
    @Override
    @Transactional
    public long countAllEntries() {
        return entityManager.createQuery("SELECT COUNT(o) FROM TbCOMPETENCIAS o", Long.class).getSingleResult();
    }
    
    @Override
    @Transactional
    public TbCOMPETENCIASEntity save(TbCOMPETENCIASEntity tbCOMPETENCIAS) {
        String username = SecurityWrapper.getUsername();
        
        tbCOMPETENCIAS.updateAuditInformation(username);
        
        return super.save(tbCOMPETENCIAS);
    }
    
    @Override
    @Transactional
    public TbCOMPETENCIASEntity update(TbCOMPETENCIASEntity tbCOMPETENCIAS) {
        String username = SecurityWrapper.getUsername();
        tbCOMPETENCIAS.updateAuditInformation(username);
        return super.update(tbCOMPETENCIAS);
    }
    
    @Override
    protected void handleDependenciesBeforeDelete(TbCOMPETENCIASEntity tbCOMPETENCIAS) {

        /* This is called before a TbCOMPETENCIAS is deleted. Place here all the
           steps to cut dependencies to other entities */
        
        this.cutAllIdCOMPETENCIASTbCOMPETENCIASCARGOSsAssignments(tbCOMPETENCIAS);
        
    }

    // Remove all assignments from all tbCOMPETENCIASCARGOS a tbCOMPETENCIAS. Called before delete a tbCOMPETENCIAS.
    @Transactional
    private void cutAllIdCOMPETENCIASTbCOMPETENCIASCARGOSsAssignments(TbCOMPETENCIASEntity tbCOMPETENCIAS) {
        entityManager
                .createQuery("UPDATE TbCOMPETENCIASCARGOS c SET c.idCOMPETENCIAS = NULL WHERE c.idCOMPETENCIAS = :p")
                .setParameter("p", tbCOMPETENCIAS).executeUpdate();
    }
    
}
