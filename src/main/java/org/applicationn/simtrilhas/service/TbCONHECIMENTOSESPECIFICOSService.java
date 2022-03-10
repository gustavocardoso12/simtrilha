package org.applicationn.simtrilhas.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Named;
import javax.transaction.Transactional;

import org.applicationn.simtrilhas.domain.TbCONHECIMENTOSESPECIFICOSEntity;
import org.applicationn.simtrilhas.service.security.SecurityWrapper;

@Named
public class TbCONHECIMENTOSESPECIFICOSService extends BaseService<TbCONHECIMENTOSESPECIFICOSEntity> implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public TbCONHECIMENTOSESPECIFICOSService(){
        super(TbCONHECIMENTOSESPECIFICOSEntity.class);
    }
    
    @Transactional
    public List<TbCONHECIMENTOSESPECIFICOSEntity> findAllTbCONHECIMENTOSESPECIFICOSEntities() {
        
        return getEntityManager().createQuery("SELECT o FROM TbCONHECIMENTOSESPECIFICOS o ", TbCONHECIMENTOSESPECIFICOSEntity.class).getResultList();
    }
    
    
    @Transactional
    public List<TbCONHECIMENTOSESPECIFICOSEntity> findAllTbCONHECIMENTOSESPECIFICOSEntitiesAlfa() {
        
        return getEntityManager().createQuery("SELECT o FROM TbCONHECIMENTOSESPECIFICOS o ORDER BY o.deSCCONHECIMENTOSESPECIFICOS", TbCONHECIMENTOSESPECIFICOSEntity.class).getResultList();
    }
    
    @Transactional
    public List<TbCONHECIMENTOSESPECIFICOSEntity> findAllTbCONHECIMENTOSESPECIFICOSEntitiesCE() {
        
        return getEntityManager().createQuery("SELECT o FROM TbCONHECIMENTOSESPECIFICOS o order by o.id asc ", TbCONHECIMENTOSESPECIFICOSEntity.class).getResultList();
    }
    
    
    
    @Override
    @Transactional
    public long countAllEntries() {
        return getEntityManager().createQuery("SELECT COUNT(o) FROM TbCONHECIMENTOSESPECIFICOS o ", Long.class).getSingleResult();
    }
    
    @Override
    @Transactional
    public TbCONHECIMENTOSESPECIFICOSEntity save(TbCONHECIMENTOSESPECIFICOSEntity tbCONHECIMENTOSESPECIFICOS) {
        String username = SecurityWrapper.getUsername();
        
        tbCONHECIMENTOSESPECIFICOS.updateAuditInformation(username);
        
        return super.save(tbCONHECIMENTOSESPECIFICOS);
    }
    
    @Override
    @Transactional
    public TbCONHECIMENTOSESPECIFICOSEntity update(TbCONHECIMENTOSESPECIFICOSEntity tbCONHECIMENTOSESPECIFICOS) {
        String username = SecurityWrapper.getUsername();
        tbCONHECIMENTOSESPECIFICOS.updateAuditInformation(username);
        return super.update(tbCONHECIMENTOSESPECIFICOS);
    }
    
    @Override
    protected void handleDependenciesBeforeDelete(TbCONHECIMENTOSESPECIFICOSEntity tbCONHECIMENTOSESPECIFICOS) {

        /* This is called before a TbCONHECIMENTOSESPECIFICOS is deleted. Place here all the
           steps to cut dependencies to other entities */
        
        this.cutAllIdCONHECESPTbCONHECIMENTOSESPCARGOSsAssignments(tbCONHECIMENTOSESPECIFICOS);
        
    }

    // Remove all assignments from all tbCONHECIMENTOSESPCARGOS a tbCONHECIMENTOSESPECIFICOS. Called before delete a tbCONHECIMENTOSESPECIFICOS.
    @Transactional
    private void cutAllIdCONHECESPTbCONHECIMENTOSESPCARGOSsAssignments(TbCONHECIMENTOSESPECIFICOSEntity tbCONHECIMENTOSESPECIFICOS) {
    	getEntityManager()
                .createQuery("DELETE FROM TbCONHECIMENTOSESPCARGOS c WHERE c.idCONHECESP = :p")
                .setParameter("p", tbCONHECIMENTOSESPECIFICOS).executeUpdate();
    }
    
}
