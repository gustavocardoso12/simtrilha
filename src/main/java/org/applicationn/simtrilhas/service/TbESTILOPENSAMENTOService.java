package org.applicationn.simtrilhas.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Named;
import javax.transaction.Transactional;

import org.applicationn.simtrilhas.domain.TbESTILOPENSAMENTOEntity;
import org.applicationn.simtrilhas.service.security.SecurityWrapper;

@Named
public class TbESTILOPENSAMENTOService extends BaseService<TbESTILOPENSAMENTOEntity> implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public TbESTILOPENSAMENTOService(){
        super(TbESTILOPENSAMENTOEntity.class);
    }
    
    @Transactional
    public List<TbESTILOPENSAMENTOEntity> findAllTbESTILOPENSAMENTOEntities() {
        
        return entityManager.createQuery("SELECT o FROM TbESTILOPENSAMENTO o ", TbESTILOPENSAMENTOEntity.class).getResultList();
    }
    
    @Override
    @Transactional
    public long countAllEntries() {
        return entityManager.createQuery("SELECT COUNT(o) FROM TbESTILOPENSAMENTO o", Long.class).getSingleResult();
    }
    
    @Override
    @Transactional
    public TbESTILOPENSAMENTOEntity save(TbESTILOPENSAMENTOEntity tbESTILOPENSAMENTO) {
        String username = SecurityWrapper.getUsername();
        
        tbESTILOPENSAMENTO.updateAuditInformation(username);
        
        return super.save(tbESTILOPENSAMENTO);
    }
    
    @Override
    @Transactional
    public TbESTILOPENSAMENTOEntity update(TbESTILOPENSAMENTOEntity tbESTILOPENSAMENTO) {
        String username = SecurityWrapper.getUsername();
        tbESTILOPENSAMENTO.updateAuditInformation(username);
        return super.update(tbESTILOPENSAMENTO);
    }
    
    @Override
    protected void handleDependenciesBeforeDelete(TbESTILOPENSAMENTOEntity tbESTILOPENSAMENTO) {

        /* This is called before a TbESTILOPENSAMENTO is deleted. Place here all the
           steps to cut dependencies to other entities */
        
        this.cutAllIdESTPENSAMENTOTbESTILOPENSAMENTOCARGOSsAssignments(tbESTILOPENSAMENTO);
        
    }

    // Remove all assignments from all tbESTILOPENSAMENTOCARGOS a tbESTILOPENSAMENTO. Called before delete a tbESTILOPENSAMENTO.
    @Transactional
    private void cutAllIdESTPENSAMENTOTbESTILOPENSAMENTOCARGOSsAssignments(TbESTILOPENSAMENTOEntity tbESTILOPENSAMENTO) {
        entityManager
                .createQuery("UPDATE TbESTILOPENSAMENTOCARGOS c SET c.idESTPENSAMENTO = NULL WHERE c.idESTPENSAMENTO = :p")
                .setParameter("p", tbESTILOPENSAMENTO).executeUpdate();
    }
    
}
