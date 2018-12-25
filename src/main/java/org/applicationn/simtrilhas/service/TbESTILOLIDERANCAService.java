package org.applicationn.simtrilhas.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Named;
import javax.transaction.Transactional;

import org.applicationn.simtrilhas.domain.TbESTILOLIDERANCAEntity;
import org.applicationn.simtrilhas.service.security.SecurityWrapper;

@Named
public class TbESTILOLIDERANCAService extends BaseService<TbESTILOLIDERANCAEntity> implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public TbESTILOLIDERANCAService(){
        super(TbESTILOLIDERANCAEntity.class);
    }
    
    @Transactional
    public List<TbESTILOLIDERANCAEntity> findAllTbESTILOLIDERANCAEntities() {
        
        return entityManager.createQuery("SELECT o FROM TbESTILOLIDERANCA o ", TbESTILOLIDERANCAEntity.class).getResultList();
    }
    
    @Override
    @Transactional
    public long countAllEntries() {
        return entityManager.createQuery("SELECT COUNT(o) FROM TbESTILOLIDERANCA o", Long.class).getSingleResult();
    }
    
    @Override
    @Transactional
    public TbESTILOLIDERANCAEntity save(TbESTILOLIDERANCAEntity tbESTILOLIDERANCA) {
        String username = SecurityWrapper.getUsername();
        
        tbESTILOLIDERANCA.updateAuditInformation(username);
        
        return super.save(tbESTILOLIDERANCA);
    }
    
    @Override
    @Transactional
    public TbESTILOLIDERANCAEntity update(TbESTILOLIDERANCAEntity tbESTILOLIDERANCA) {
        String username = SecurityWrapper.getUsername();
        tbESTILOLIDERANCA.updateAuditInformation(username);
        return super.update(tbESTILOLIDERANCA);
    }
    
    @Override
    protected void handleDependenciesBeforeDelete(TbESTILOLIDERANCAEntity tbESTILOLIDERANCA) {

        /* This is called before a TbESTILOLIDERANCA is deleted. Place here all the
           steps to cut dependencies to other entities */
        
        this.cutAllIdESTLIDERTbESTILOLIDERANCACARGOSsAssignments(tbESTILOLIDERANCA);
        
    }

    // Remove all assignments from all tbESTILOLIDERANCACARGOS a tbESTILOLIDERANCA. Called before delete a tbESTILOLIDERANCA.
    @Transactional
    private void cutAllIdESTLIDERTbESTILOLIDERANCACARGOSsAssignments(TbESTILOLIDERANCAEntity tbESTILOLIDERANCA) {
        entityManager
                .createQuery("UPDATE TbESTILOLIDERANCACARGOS c SET c.idESTLIDER = NULL WHERE c.idESTLIDER = :p")
                .setParameter("p", tbESTILOLIDERANCA).executeUpdate();
    }
    
}
