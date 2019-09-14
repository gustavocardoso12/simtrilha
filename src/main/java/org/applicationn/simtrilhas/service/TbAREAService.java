package org.applicationn.simtrilhas.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Named;
import javax.transaction.Transactional;

import org.applicationn.simtrilhas.domain.TbAREAEntity;
import org.applicationn.simtrilhas.service.security.SecurityWrapper;

@Named
public class TbAREAService extends BaseService<TbAREAEntity> implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public TbAREAService(){
        super(TbAREAEntity.class);
    }
    
    @Transactional
    public List<TbAREAEntity> findAllTbAREAEntities() {
        
        return entityManager.createQuery("SELECT o FROM TbAREA o ", TbAREAEntity.class).getResultList();
    }
    
    @Override
    @Transactional
    public long countAllEntries() {
        return entityManager.createQuery("SELECT COUNT(o) FROM TbAREA o", Long.class).getSingleResult();
    }
    
    @Override
    @Transactional
    public TbAREAEntity save(TbAREAEntity tbAREA) {
        String username = SecurityWrapper.getUsername();
        
        tbAREA.updateAuditInformation(username);
        
        return super.save(tbAREA);
    }
    
    @Override
    @Transactional
    public TbAREAEntity update(TbAREAEntity tbAREA) {
        String username = SecurityWrapper.getUsername();
        tbAREA.updateAuditInformation(username);
        return super.update(tbAREA);
    }
    
    @Override
    protected void handleDependenciesBeforeDelete(TbAREAEntity tbAREA) {

        /* This is called before a TbAREA is deleted. Place here all the
           steps to cut dependencies to other entities */
        
        this.cutAllIdAREATbDEPTOsAssignments(tbAREA);
        
    }

    // Remove all assignments from all tbDEPTO a tbAREA. Called before delete a tbAREA.
    @Transactional
    private void cutAllIdAREATbDEPTOsAssignments(TbAREAEntity tbAREA) {
        entityManager
                .createQuery("UPDATE TbDEPTO c SET c.idAREA = NULL WHERE c.idAREA = :p")
                .setParameter("p", tbAREA).executeUpdate();
    }
    
}
