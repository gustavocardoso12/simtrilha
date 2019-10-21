package org.applicationn.simtrilhas.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Named;
import javax.persistence.criteria.CriteriaBuilder;
import javax.transaction.Transactional;

import org.applicationn.simtrilhas.domain.TbAREAEntity;
import org.applicationn.simtrilhas.domain.TbCARGOSEntity;
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
    
    @Transactional
    public List<String> findDistinctTbAREAEntities() {
    	

        return entityManager.createQuery("SELECT DISTINCT(o.deSCAREA) FROM TbAREA o ", String.class).getResultList();
    }
    
    @Transactional
    public List<String> findDistinctTbDEPTOEntities(String Area) {
    	

        return entityManager.createQuery("SELECT DISTINCT(o.deSCDEPTO) FROM TbDEPTO o WHERE o.idAREA.deSCAREA =:deSCAREA ", String.class).setParameter("deSCAREA",Area).getResultList();
    }
    
    @Transactional
    public List<TbCARGOSEntity> findDistinctTbCARGOSEntities(String Depto, String Area) {
    	

        return entityManager.createQuery("SELECT o FROM TbCARGOS o WHERE o.idDEPTO.deSCDEPTO =:deSCDEPTO and o.idDEPTO.idAREA.deSCAREA =:deSCAREA", TbCARGOSEntity.class)
        		.setParameter("deSCDEPTO",Depto).setParameter("deSCAREA", Area)
        		.getResultList();
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
