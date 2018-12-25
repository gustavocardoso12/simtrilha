package org.applicationn.simtrilhas.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Named;
import javax.transaction.Transactional;

import org.applicationn.simtrilhas.domain.TbCONHECIMENTOSBASICOSEntity;
import org.applicationn.simtrilhas.service.security.SecurityWrapper;

@Named
public class TbCONHECIMENTOSBASICOSService extends BaseService<TbCONHECIMENTOSBASICOSEntity> implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public TbCONHECIMENTOSBASICOSService(){
        super(TbCONHECIMENTOSBASICOSEntity.class);
    }
    
    @Transactional
    public List<TbCONHECIMENTOSBASICOSEntity> findAllTbCONHECIMENTOSBASICOSEntities() {
        
        return entityManager.createQuery("SELECT o FROM TbCONHECIMENTOSBASICOS o ", TbCONHECIMENTOSBASICOSEntity.class).getResultList();
    }
    
    @Override
    @Transactional
    public long countAllEntries() {
        return entityManager.createQuery("SELECT COUNT(o) FROM TbCONHECIMENTOSBASICOS o", Long.class).getSingleResult();
    }
    
    @Override
    @Transactional
    public TbCONHECIMENTOSBASICOSEntity save(TbCONHECIMENTOSBASICOSEntity tbCONHECIMENTOSBASICOS) {
        String username = SecurityWrapper.getUsername();
        
        tbCONHECIMENTOSBASICOS.updateAuditInformation(username);
        
        return super.save(tbCONHECIMENTOSBASICOS);
    }
    
    @Override
    @Transactional
    public TbCONHECIMENTOSBASICOSEntity update(TbCONHECIMENTOSBASICOSEntity tbCONHECIMENTOSBASICOS) {
        String username = SecurityWrapper.getUsername();
        tbCONHECIMENTOSBASICOS.updateAuditInformation(username);
        return super.update(tbCONHECIMENTOSBASICOS);
    }
    
    @Override
    protected void handleDependenciesBeforeDelete(TbCONHECIMENTOSBASICOSEntity tbCONHECIMENTOSBASICOS) {

        /* This is called before a TbCONHECIMENTOSBASICOS is deleted. Place here all the
           steps to cut dependencies to other entities */
        
        this.cutAllIdCONHECBASTbCONHECIMENTOSBASCARGOSsAssignments(tbCONHECIMENTOSBASICOS);
        
    }

    // Remove all assignments from all tbCONHECIMENTOSBASCARGOS a tbCONHECIMENTOSBASICOS. Called before delete a tbCONHECIMENTOSBASICOS.
    @Transactional
    private void cutAllIdCONHECBASTbCONHECIMENTOSBASCARGOSsAssignments(TbCONHECIMENTOSBASICOSEntity tbCONHECIMENTOSBASICOS) {
        entityManager
                .createQuery("UPDATE TbCONHECIMENTOSBASCARGOS c SET c.idCONHECBAS = NULL WHERE c.idCONHECBAS = :p")
                .setParameter("p", tbCONHECIMENTOSBASICOS).executeUpdate();
    }
    
}
