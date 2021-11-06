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
        
        return getEntityManager().createQuery("SELECT o FROM TbCOMPETENCIAS o ", TbCOMPETENCIASEntity.class).getResultList();
    }
    
    @Override
    @Transactional
    public long countAllEntries() {
        return getEntityManager().createQuery("SELECT COUNT(o) FROM TbCOMPETENCIAS o", Long.class).getSingleResult();
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

       
        
        this.cutAllIdCOMPETENCIASTbCOMPETENCIASCARGOSsAssignments(tbCOMPETENCIAS);
        
    }

    // Remove all assignments from all tbCOMPETENCIASCARGOS a tbCOMPETENCIAS. Called before delete a tbCOMPETENCIAS.
    @Transactional
    private void cutAllIdCOMPETENCIASTbCOMPETENCIASCARGOSsAssignments(TbCOMPETENCIASEntity tbCOMPETENCIAS) {
    	getEntityManager()
                .createQuery("DELETE FROM TbCOMPETENCIASCARGOS c WHERE c.idCOMPETENCIAS = :p")
                .setParameter("p", tbCOMPETENCIAS).executeUpdate();
    }
    
}
