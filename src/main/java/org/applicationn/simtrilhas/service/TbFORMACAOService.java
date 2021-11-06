package org.applicationn.simtrilhas.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Named;
import javax.transaction.Transactional;

import org.applicationn.simtrilhas.domain.TbFORMACAOEntity;
import org.applicationn.simtrilhas.service.security.SecurityWrapper;

@Named
public class TbFORMACAOService extends BaseService<TbFORMACAOEntity> implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public TbFORMACAOService(){
        super(TbFORMACAOEntity.class);
    }
    
    @Transactional
    public List<TbFORMACAOEntity> findAllTbFORMACAOEntities() {
        
        return getEntityManager().createQuery("SELECT o FROM TbFORMACAO o ", TbFORMACAOEntity.class).getResultList();
    }
    
    @Override
    @Transactional
    public long countAllEntries() {
        return getEntityManager().createQuery("SELECT COUNT(o) FROM TbFORMACAO o", Long.class).getSingleResult();
    }
    
    @Override
    @Transactional
    public TbFORMACAOEntity save(TbFORMACAOEntity tbFORMACAO) {
        String username = SecurityWrapper.getUsername();
        
        tbFORMACAO.updateAuditInformation(username);
        
        return super.save(tbFORMACAO);
    }
    
    @Override
    @Transactional
    public TbFORMACAOEntity update(TbFORMACAOEntity tbFORMACAO) {
        String username = SecurityWrapper.getUsername();
        tbFORMACAO.updateAuditInformation(username);
        return super.update(tbFORMACAO);
    }
    
    @Override
    protected void handleDependenciesBeforeDelete(TbFORMACAOEntity tbFORMACAO) {

        /* This is called before a TbFORMACAO is deleted. Place here all the
           steps to cut dependencies to other entities */
        
        this.cutAllIdFORMACAOTbCURSOSsAssignments(tbFORMACAO);
        
    }

    // Remove all assignments from all tbCURSOS a tbFORMACAO. Called before delete a tbFORMACAO.
    @Transactional
    private void cutAllIdFORMACAOTbCURSOSsAssignments(TbFORMACAOEntity tbFORMACAO) {
    	getEntityManager()
                .createQuery("UPDATE TbCURSOS c SET c.idFORMACAO = NULL WHERE c.idFORMACAO = :p")
                .setParameter("p", tbFORMACAO).executeUpdate();
    }
    
}
