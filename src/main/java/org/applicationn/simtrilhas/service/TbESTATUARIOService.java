package org.applicationn.simtrilhas.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Named;
import javax.transaction.Transactional;

import org.applicationn.simtrilhas.domain.TbDIRETORIAEntity;
import org.applicationn.simtrilhas.domain.TbESTATUARIOEntity;
import org.applicationn.simtrilhas.service.security.SecurityWrapper;

@Named
public class TbESTATUARIOService extends BaseService<TbESTATUARIOEntity> implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public TbESTATUARIOService(){
        super(TbESTATUARIOEntity.class);
    }
    
    @Transactional
    public List<TbESTATUARIOEntity> findAllTbESTATUARIOEntities() {
        
        return entityManager.createQuery("SELECT o FROM TbESTATUARIO o ", TbESTATUARIOEntity.class).getResultList();
    }
    
    @Transactional
    public List<String> findByTbESTATUARIOEntities() {
        
        return entityManager.createQuery("SELECT DISTINCT(o.deSCESTATUARIO) FROM TbESTATUARIO o ", String.class).getResultList();
    }
    
    @Override
    @Transactional
    public long countAllEntries() {
        return entityManager.createQuery("SELECT COUNT(o) FROM TbESTATUARIO o", Long.class).getSingleResult();
    }
    
    @Override
    @Transactional
    public TbESTATUARIOEntity save(TbESTATUARIOEntity tbESTATUARIO) {
        String username = SecurityWrapper.getUsername();
        
        tbESTATUARIO.updateAuditInformation(username);
        
        return super.save(tbESTATUARIO);
    }
    
    @Override
    @Transactional
    public TbESTATUARIOEntity update(TbESTATUARIOEntity tbESTATUARIO) {
        String username = SecurityWrapper.getUsername();
        tbESTATUARIO.updateAuditInformation(username);
        return super.update(tbESTATUARIO);
    }
    
    @Override
    protected void handleDependenciesBeforeDelete(TbESTATUARIOEntity tbESTATUARIO) {

        /* This is called before a TbESTATUARIO is deleted. Place here all the
           steps to cut dependencies to other entities */
        
        this.cutAllIdESTTbCARGOSsAssignments(tbESTATUARIO);
        
    }

    // Remove all assignments from all tbCARGOS a tbESTATUARIO. Called before delete a tbESTATUARIO.
    @Transactional
    private void cutAllIdESTTbCARGOSsAssignments(TbESTATUARIOEntity tbESTATUARIO) {
        entityManager
                .createQuery("UPDATE TbCARGOS c SET c.idEST = NULL WHERE c.idEST = :p")
                .setParameter("p", tbESTATUARIO).executeUpdate();
    }
    
    @Transactional
    public List<TbESTATUARIOEntity> findAvailableTbESTATUARIOs(TbDIRETORIAEntity tbDIRETORIA) {
        return entityManager.createQuery("SELECT o FROM TbESTATUARIO o WHERE o.idDIRETORIA IS NULL", TbESTATUARIOEntity.class).getResultList();
    }

    @Transactional
    public List<TbESTATUARIOEntity> findTbESTATUARIOsByIdDIRETORIA(TbDIRETORIAEntity tbDIRETORIA) {
        return entityManager.createQuery("SELECT o FROM TbESTATUARIO o WHERE o.idDIRETORIA = :tbDIRETORIA", TbESTATUARIOEntity.class).setParameter("tbDIRETORIA", tbDIRETORIA).getResultList();
    }

}
