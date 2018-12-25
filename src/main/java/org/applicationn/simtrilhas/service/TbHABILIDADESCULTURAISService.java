package org.applicationn.simtrilhas.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Named;
import javax.transaction.Transactional;

import org.applicationn.simtrilhas.domain.TbHABILIDADESCULTURAISEntity;
import org.applicationn.simtrilhas.service.security.SecurityWrapper;

@Named
public class TbHABILIDADESCULTURAISService extends BaseService<TbHABILIDADESCULTURAISEntity> implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public TbHABILIDADESCULTURAISService(){
        super(TbHABILIDADESCULTURAISEntity.class);
    }
    
    @Transactional
    public List<TbHABILIDADESCULTURAISEntity> findAllTbHABILIDADESCULTURAISEntities() {
        
        return entityManager.createQuery("SELECT o FROM TbHABILIDADESCULTURAIS o ", TbHABILIDADESCULTURAISEntity.class).getResultList();
    }
    
    @Override
    @Transactional
    public long countAllEntries() {
        return entityManager.createQuery("SELECT COUNT(o) FROM TbHABILIDADESCULTURAIS o", Long.class).getSingleResult();
    }
    
    @Override
    @Transactional
    public TbHABILIDADESCULTURAISEntity save(TbHABILIDADESCULTURAISEntity tbHABILIDADESCULTURAIS) {
        String username = SecurityWrapper.getUsername();
        
        tbHABILIDADESCULTURAIS.updateAuditInformation(username);
        
        return super.save(tbHABILIDADESCULTURAIS);
    }
    
    @Override
    @Transactional
    public TbHABILIDADESCULTURAISEntity update(TbHABILIDADESCULTURAISEntity tbHABILIDADESCULTURAIS) {
        String username = SecurityWrapper.getUsername();
        tbHABILIDADESCULTURAIS.updateAuditInformation(username);
        return super.update(tbHABILIDADESCULTURAIS);
    }
    
    @Override
    protected void handleDependenciesBeforeDelete(TbHABILIDADESCULTURAISEntity tbHABILIDADESCULTURAIS) {

        /* This is called before a TbHABILIDADESCULTURAIS is deleted. Place here all the
           steps to cut dependencies to other entities */
        
        this.cutAllIdHABCULTCARTbHABILIDADESCULTCARGOSsAssignments(tbHABILIDADESCULTURAIS);
        
    }

    // Remove all assignments from all tbHABILIDADESCULTCARGOS a tbHABILIDADESCULTURAIS. Called before delete a tbHABILIDADESCULTURAIS.
    @Transactional
    private void cutAllIdHABCULTCARTbHABILIDADESCULTCARGOSsAssignments(TbHABILIDADESCULTURAISEntity tbHABILIDADESCULTURAIS) {
        entityManager
                .createQuery("UPDATE TbHABILIDADESCULTCARGOS c SET c.idHABCULTCAR = NULL WHERE c.idHABCULTCAR = :p")
                .setParameter("p", tbHABILIDADESCULTURAIS).executeUpdate();
    }
    
}
