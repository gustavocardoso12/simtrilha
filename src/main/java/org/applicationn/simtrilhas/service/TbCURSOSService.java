package org.applicationn.simtrilhas.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Named;
import javax.transaction.Transactional;

import org.applicationn.simtrilhas.domain.TbCURSOSEntity;
import org.applicationn.simtrilhas.domain.TbFORMACAOEntity;
import org.applicationn.simtrilhas.service.security.SecurityWrapper;

@Named
public class TbCURSOSService extends BaseService<TbCURSOSEntity> implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public TbCURSOSService(){
        super(TbCURSOSEntity.class);
    }
    
    @Transactional
    public List<TbCURSOSEntity> findAllTbCURSOSEntities() {
        
        return getEntityManager().createQuery("SELECT o FROM TbCURSOS o ", TbCURSOSEntity.class).getResultList();
    }
    
    @Override
    @Transactional
    public long countAllEntries() {
        return getEntityManager().createQuery("SELECT COUNT(o) FROM TbCURSOS o", Long.class).getSingleResult();
    }
    
    @Override
    @Transactional
    public TbCURSOSEntity save(TbCURSOSEntity tbCURSOS) {
        String username = SecurityWrapper.getUsername();
        
        tbCURSOS.updateAuditInformation(username);
        
        return super.save(tbCURSOS);
    }
    
    @Override
    @Transactional
    public TbCURSOSEntity update(TbCURSOSEntity tbCURSOS) {
        String username = SecurityWrapper.getUsername();
        tbCURSOS.updateAuditInformation(username);
        return super.update(tbCURSOS);
    }
    
    @Override
    protected void handleDependenciesBeforeDelete(TbCURSOSEntity tbCURSOS) {

        /* This is called before a TbCURSOS is deleted. Place here all the
           steps to cut dependencies to other entities */
        
        this.cutAllIdCURSOSTbCARGOSsAssignments(tbCURSOS);
        
    }

    // Remove all assignments from all tbCARGOS a tbCURSOS. Called before delete a tbCURSOS.
    @Transactional
    private void cutAllIdCURSOSTbCARGOSsAssignments(TbCURSOSEntity tbCURSOS) {
    	getEntityManager()
                .createQuery("UPDATE TbCARGOS c SET c.idCURSOS = NULL WHERE c.idCURSOS = :p")
                .setParameter("p", tbCURSOS).executeUpdate();
    }
    
    @Transactional
    public List<TbCURSOSEntity> findAvailableTbCURSOSs(TbFORMACAOEntity tbFORMACAO) {
        return getEntityManager().createQuery("SELECT o FROM TbCURSOS o WHERE o.idFORMACAO IS NULL", TbCURSOSEntity.class).getResultList();
    }

    @Transactional
    public List<TbCURSOSEntity> findTbCURSOSsByIdFORMACAO(TbFORMACAOEntity tbFORMACAO) {
        return getEntityManager().createQuery("SELECT o FROM TbCURSOS o WHERE o.idFORMACAO = :tbFORMACAO", TbCURSOSEntity.class).setParameter("tbFORMACAO", tbFORMACAO).getResultList();
    }

}
