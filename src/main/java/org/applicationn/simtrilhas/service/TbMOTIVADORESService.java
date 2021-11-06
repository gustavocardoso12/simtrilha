package org.applicationn.simtrilhas.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Named;
import javax.transaction.Transactional;

import org.applicationn.simtrilhas.domain.TbMOTIVADORESEntity;
import org.applicationn.simtrilhas.service.security.SecurityWrapper;

@Named
public class TbMOTIVADORESService extends BaseService<TbMOTIVADORESEntity> implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public TbMOTIVADORESService(){
        super(TbMOTIVADORESEntity.class);
    }
    
    @Transactional
    public List<TbMOTIVADORESEntity> findAllTbMOTIVADORESEntities() {
        
        return getEntityManager().createQuery("SELECT o FROM TbMOTIVADORES o ", TbMOTIVADORESEntity.class).getResultList();
    }
    
    @Override
    @Transactional
    public long countAllEntries() {
        return getEntityManager().createQuery("SELECT COUNT(o) FROM TbMOTIVADORES o", Long.class).getSingleResult();
    }
    
    @Override
    @Transactional
    public TbMOTIVADORESEntity save(TbMOTIVADORESEntity tbMOTIVADORES) {
        String username = SecurityWrapper.getUsername();
        
        tbMOTIVADORES.updateAuditInformation(username);
        
        return super.save(tbMOTIVADORES);
    }
    
    @Override
    @Transactional
    public TbMOTIVADORESEntity update(TbMOTIVADORESEntity tbMOTIVADORES) {
        String username = SecurityWrapper.getUsername();
        tbMOTIVADORES.updateAuditInformation(username);
        return super.update(tbMOTIVADORES);
    }
    
    @Override
    protected void handleDependenciesBeforeDelete(TbMOTIVADORESEntity tbMOTIVADORES) {

        /* This is called before a TbMOTIVADORES is deleted. Place here all the
           steps to cut dependencies to other entities */
        
        this.cutAllIdMOTIVADORESTbMOTIVADORESCARGOSsAssignments(tbMOTIVADORES);
        
    }

    // Remove all assignments from all tbMOTIVADORESCARGOS a tbMOTIVADORES. Called before delete a tbMOTIVADORES.
    @Transactional
    private void cutAllIdMOTIVADORESTbMOTIVADORESCARGOSsAssignments(TbMOTIVADORESEntity tbMOTIVADORES) {
    	getEntityManager()
                .createQuery("UPDATE TbMOTIVADORESCARGOS c SET c.idMOTIVADORES = NULL WHERE c.idMOTIVADORES = :p")
                .setParameter("p", tbMOTIVADORES).executeUpdate();
    }
    
}
