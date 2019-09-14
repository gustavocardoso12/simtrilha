package org.applicationn.simtrilhas.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Named;
import javax.transaction.Transactional;

import org.applicationn.simtrilhas.domain.TbAREAEntity;
import org.applicationn.simtrilhas.domain.TbDEPTOEntity;
import org.applicationn.simtrilhas.service.security.SecurityWrapper;

@Named
public class TbDEPTOService extends BaseService<TbDEPTOEntity> implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public TbDEPTOService(){
        super(TbDEPTOEntity.class);
    }
    
    @Transactional
    public List<TbDEPTOEntity> findAllTbDEPTOEntities() {
        
        return entityManager.createQuery("SELECT o FROM TbDEPTO o ", TbDEPTOEntity.class).getResultList();
    }
    
    @Override
    @Transactional
    public long countAllEntries() {
        return entityManager.createQuery("SELECT COUNT(o) FROM TbDEPTO o", Long.class).getSingleResult();
    }
    
    @Override
    @Transactional
    public TbDEPTOEntity save(TbDEPTOEntity tbDEPTO) {
        String username = SecurityWrapper.getUsername();
        
        tbDEPTO.updateAuditInformation(username);
        
        return super.save(tbDEPTO);
    }
    
    @Override
    @Transactional
    public TbDEPTOEntity update(TbDEPTOEntity tbDEPTO) {
        String username = SecurityWrapper.getUsername();
        tbDEPTO.updateAuditInformation(username);
        return super.update(tbDEPTO);
    }
    
    @Override
    protected void handleDependenciesBeforeDelete(TbDEPTOEntity tbDEPTO) {

        /* This is called before a TbDEPTO is deleted. Place here all the
           steps to cut dependencies to other entities */
        
        this.cutAllIdDEPTOTbCARGOSsAssignments(tbDEPTO);
        
    }

    // Remove all assignments from all tbCARGOS a tbDEPTO. Called before delete a tbDEPTO.
    @Transactional
    private void cutAllIdDEPTOTbCARGOSsAssignments(TbDEPTOEntity tbDEPTO) {
        entityManager
                .createQuery("UPDATE TbCARGOS c SET c.idDEPTO = NULL WHERE c.idDEPTO = :p")
                .setParameter("p", tbDEPTO).executeUpdate();
    }
    
    @Transactional
    public List<TbDEPTOEntity> findAvailableTbDEPTOs(TbAREAEntity tbAREA) {
        return entityManager.createQuery("SELECT o FROM TbDEPTO o WHERE o.idAREA IS NULL", TbDEPTOEntity.class).getResultList();
    }

    @Transactional
    public List<TbDEPTOEntity> findTbDEPTOsByIdAREA(TbAREAEntity tbAREA) {
        return entityManager.createQuery("SELECT o FROM TbDEPTO o WHERE o.idAREA = :tbAREA", TbDEPTOEntity.class).setParameter("tbAREA", tbAREA).getResultList();
    }

}
