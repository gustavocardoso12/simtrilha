package org.applicationn.simtrilhas.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Named;
import javax.transaction.Transactional;

import org.applicationn.simtrilhas.domain.TbHABILIDADESEntity;
import org.applicationn.simtrilhas.service.security.SecurityWrapper;

@Named
public class TbHABILIDADESService extends BaseService<TbHABILIDADESEntity> implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public TbHABILIDADESService(){
        super(TbHABILIDADESEntity.class);
    }
    
    @Transactional
    public List<TbHABILIDADESEntity> findAllTbHABILIDADESEntities() {
        
        return getEntityManager().createQuery("SELECT o FROM TbHABILIDADES o ", TbHABILIDADESEntity.class).getResultList();
    }
    
    @Override
    @Transactional
    public long countAllEntries() {
        return getEntityManager().createQuery("SELECT COUNT(o) FROM TbHABILIDADES o", Long.class).getSingleResult();
    }
    
    @Override
    @Transactional
    public TbHABILIDADESEntity save(TbHABILIDADESEntity tbHABILIDADES) {
        String username = SecurityWrapper.getUsername();
        
        tbHABILIDADES.updateAuditInformation(username);
        
        return super.save(tbHABILIDADES);
    }
    
    @Override
    @Transactional
    public TbHABILIDADESEntity update(TbHABILIDADESEntity tbHABILIDADES) {
        String username = SecurityWrapper.getUsername();
        tbHABILIDADES.updateAuditInformation(username);
        return super.update(tbHABILIDADES);
    }
    
    @Override
    protected void handleDependenciesBeforeDelete(TbHABILIDADESEntity tbHABILIDADES) {

        /* This is called before a TbHABILIDADES is deleted. Place here all the
           steps to cut dependencies to other entities */
        
        this.cutAllIdHABCARGOSTbHABILIDADESCARGOSsAssignments(tbHABILIDADES);
        
    }

    // Remove all assignments from all tbHABILIDADESCARGOS a tbHABILIDADES. Called before delete a tbHABILIDADES.
    @Transactional
    private void cutAllIdHABCARGOSTbHABILIDADESCARGOSsAssignments(TbHABILIDADESEntity tbHABILIDADES) {
    	getEntityManager()
                .createQuery("UPDATE TbHABILIDADESCARGOS c SET c.idHABCARGOS = NULL WHERE c.idHABCARGOS = :p")
                .setParameter("p", tbHABILIDADES).executeUpdate();
    }
    
}
