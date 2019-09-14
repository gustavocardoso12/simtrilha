package org.applicationn.simtrilhas.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Named;
import javax.transaction.Transactional;

import org.applicationn.simtrilhas.domain.TbPERFILEntity;
import org.applicationn.simtrilhas.service.security.SecurityWrapper;

@Named
public class TbPERFILService extends BaseService<TbPERFILEntity> implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public TbPERFILService(){
        super(TbPERFILEntity.class);
    }
    
    @Transactional
    public List<TbPERFILEntity> findAllTbPERFILEntities() {
        
        return entityManager.createQuery("SELECT o FROM TbPERFIL o ", TbPERFILEntity.class).getResultList();
    }
    
    @Override
    @Transactional
    public long countAllEntries() {
        return entityManager.createQuery("SELECT COUNT(o) FROM TbPERFIL o", Long.class).getSingleResult();
    }
    
    @Override
    @Transactional
    public TbPERFILEntity save(TbPERFILEntity tbPERFIL) {
        String username = SecurityWrapper.getUsername();
        
        tbPERFIL.updateAuditInformation(username);
        
        return super.save(tbPERFIL);
    }
    
    @Override
    @Transactional
    public TbPERFILEntity update(TbPERFILEntity tbPERFIL) {
        String username = SecurityWrapper.getUsername();
        tbPERFIL.updateAuditInformation(username);
        return super.update(tbPERFIL);
    }
    
    @Override
    protected void handleDependenciesBeforeDelete(TbPERFILEntity tbPERFIL) {

        /* This is called before a TbPERFIL is deleted. Place here all the
           steps to cut dependencies to other entities */
        
        this.cutAllIdPERFILTbPERFILCARGOSsAssignments(tbPERFIL);
        
    }

    // Remove all assignments from all tbPERFILCARGOS a tbPERFIL. Called before delete a tbPERFIL.
    @Transactional
    private void cutAllIdPERFILTbPERFILCARGOSsAssignments(TbPERFILEntity tbPERFIL) {
        entityManager
                .createQuery("UPDATE TbPERFILCARGOS c SET c.idPERFIL = NULL WHERE c.idPERFIL = :p")
                .setParameter("p", tbPERFIL).executeUpdate();
    }
    
}
