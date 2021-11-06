package org.applicationn.simtrilhas.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Named;
import javax.transaction.Transactional;

import org.applicationn.simtrilhas.domain.TbCOMPETENCIASEMOCIONAISEntity;
import org.applicationn.simtrilhas.service.security.SecurityWrapper;

@Named
public class TbCOMPETENCIASEMOCIONAISService extends BaseService<TbCOMPETENCIASEMOCIONAISEntity> implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public TbCOMPETENCIASEMOCIONAISService(){
        super(TbCOMPETENCIASEMOCIONAISEntity.class);
    }
    
    @Transactional
    public List<TbCOMPETENCIASEMOCIONAISEntity> findAllTbCOMPETENCIASEMOCIONAISEntities() {
        
        return getEntityManager().createQuery("SELECT o FROM TbCOMPETENCIASEMOCIONAIS o ", TbCOMPETENCIASEMOCIONAISEntity.class).getResultList();
    }
    
    @Override
    @Transactional
    public long countAllEntries() {
        return getEntityManager().createQuery("SELECT COUNT(o) FROM TbCOMPETENCIASEMOCIONAIS o", Long.class).getSingleResult();
    }
    
    @Override
    @Transactional
    public TbCOMPETENCIASEMOCIONAISEntity save(TbCOMPETENCIASEMOCIONAISEntity tbCOMPETENCIASEMOCIONAIS) {
        String username = SecurityWrapper.getUsername();
        
        tbCOMPETENCIASEMOCIONAIS.updateAuditInformation(username);
        
        return super.save(tbCOMPETENCIASEMOCIONAIS);
    }
    
    @Override
    @Transactional
    public TbCOMPETENCIASEMOCIONAISEntity update(TbCOMPETENCIASEMOCIONAISEntity tbCOMPETENCIASEMOCIONAIS) {
        String username = SecurityWrapper.getUsername();
        tbCOMPETENCIASEMOCIONAIS.updateAuditInformation(username);
        return super.update(tbCOMPETENCIASEMOCIONAIS);
    }
    
    @Override
    protected void handleDependenciesBeforeDelete(TbCOMPETENCIASEMOCIONAISEntity tbCOMPETENCIASEMOCIONAIS) {

        /* This is called before a TbCOMPETENCIASEMOCIONAIS is deleted. Place here all the
           steps to cut dependencies to other entities */
        
        this.cutAllIdCOMPEMTbCOMPETENCIASEMCARGOSsAssignments(tbCOMPETENCIASEMOCIONAIS);
        
    }

    // Remove all assignments from all tbCOMPETENCIASEMCARGOS a tbCOMPETENCIASEMOCIONAIS. Called before delete a tbCOMPETENCIASEMOCIONAIS.
    @Transactional
    private void cutAllIdCOMPEMTbCOMPETENCIASEMCARGOSsAssignments(TbCOMPETENCIASEMOCIONAISEntity tbCOMPETENCIASEMOCIONAIS) {
    	getEntityManager()
                .createQuery("UPDATE TbCOMPETENCIASEMCARGOS c SET c.idCOMPEM = NULL WHERE c.idCOMPEM = :p")
                .setParameter("p", tbCOMPETENCIASEMOCIONAIS).executeUpdate();
    }
    
}
