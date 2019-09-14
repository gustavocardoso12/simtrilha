package org.applicationn.simtrilhas.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Named;
import javax.transaction.Transactional;

import org.applicationn.simtrilhas.domain.TbGRADEEntity;
import org.applicationn.simtrilhas.service.security.SecurityWrapper;

@Named
public class TbGRADEService extends BaseService<TbGRADEEntity> implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public TbGRADEService(){
        super(TbGRADEEntity.class);
    }
    
    @Transactional
    public List<TbGRADEEntity> findAllTbGRADEEntities() {
        
        return entityManager.createQuery("SELECT o FROM TbGRADE o ", TbGRADEEntity.class).getResultList();
    }
    
    @Override
    @Transactional
    public long countAllEntries() {
        return entityManager.createQuery("SELECT COUNT(o) FROM TbGRADE o", Long.class).getSingleResult();
    }
    
    @Override
    @Transactional
    public TbGRADEEntity save(TbGRADEEntity tbGRADE) {
        String username = SecurityWrapper.getUsername();
        
        tbGRADE.updateAuditInformation(username);
        
        return super.save(tbGRADE);
    }
    
    @Override
    @Transactional
    public TbGRADEEntity update(TbGRADEEntity tbGRADE) {
        String username = SecurityWrapper.getUsername();
        tbGRADE.updateAuditInformation(username);
        return super.update(tbGRADE);
    }
    
    @Override
    protected void handleDependenciesBeforeDelete(TbGRADEEntity tbGRADE) {

        /* This is called before a TbGRADE is deleted. Place here all the
           steps to cut dependencies to other entities */
        
        this.cutAllIdGRADETbGRADECARGOSsAssignments(tbGRADE);
        
    }

    // Remove all assignments from all tbGRADECARGOS a tbGRADE. Called before delete a tbGRADE.
    @Transactional
    private void cutAllIdGRADETbGRADECARGOSsAssignments(TbGRADEEntity tbGRADE) {
        entityManager
                .createQuery("UPDATE TbGRADECARGOS c SET c.idGRADE = NULL WHERE c.idGRADE = :p")
                .setParameter("p", tbGRADE).executeUpdate();
    }
    
}
