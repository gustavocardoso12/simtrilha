package org.applicationn.simtrilhas.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Named;
import javax.transaction.Transactional;

import org.applicationn.simtrilhas.domain.TbCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbMOTIVADORESCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbMOTIVADORESEntity;

@Named
public class TbMOTIVADORESCARGOSService extends BaseService<TbMOTIVADORESCARGOSEntity> implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public TbMOTIVADORESCARGOSService(){
        super(TbMOTIVADORESCARGOSEntity.class);
    }
    
    @Transactional
    public List<TbMOTIVADORESCARGOSEntity> findAllTbMOTIVADORESCARGOSEntities() {
        
        return entityManager.createQuery("SELECT o FROM TbMOTIVADORESCARGOS o ", TbMOTIVADORESCARGOSEntity.class).getResultList();
    }
    
    @Override
    @Transactional
    public long countAllEntries() {
        return entityManager.createQuery("SELECT COUNT(o) FROM TbMOTIVADORESCARGOS o", Long.class).getSingleResult();
    }
    
    @Override
    protected void handleDependenciesBeforeDelete(TbMOTIVADORESCARGOSEntity tbMOTIVADORESCARGOS) {

        /* This is called before a TbMOTIVADORESCARGOS is deleted. Place here all the
           steps to cut dependencies to other entities */
        
    }

    @Transactional
    public List<TbMOTIVADORESCARGOSEntity> findAvailableTbMOTIVADORESCARGOSs(TbCARGOSEntity tbCARGOS) {
        return entityManager.createQuery("SELECT o FROM TbMOTIVADORESCARGOS o WHERE o.idCARGOS IS NULL", TbMOTIVADORESCARGOSEntity.class).getResultList();
    }

    @Transactional
    public List<TbMOTIVADORESCARGOSEntity> findTbMOTIVADORESCARGOSsByIdCARGOS(TbCARGOSEntity tbCARGOS) {
        return entityManager.createQuery("SELECT o FROM TbMOTIVADORESCARGOS o WHERE o.idCARGOS = :tbCARGOS", TbMOTIVADORESCARGOSEntity.class).setParameter("tbCARGOS", tbCARGOS).getResultList();
    }

    @Transactional
    public List<TbMOTIVADORESCARGOSEntity> findAvailableTbMOTIVADORESCARGOSs(TbMOTIVADORESEntity tbMOTIVADORES) {
        return entityManager.createQuery("SELECT o FROM TbMOTIVADORESCARGOS o WHERE o.idMOTIVADORES IS NULL", TbMOTIVADORESCARGOSEntity.class).getResultList();
    }

    @Transactional
    public List<TbMOTIVADORESCARGOSEntity> findTbMOTIVADORESCARGOSsByIdMOTIVADORES(TbMOTIVADORESEntity tbMOTIVADORES) {
        return entityManager.createQuery("SELECT o FROM TbMOTIVADORESCARGOS o WHERE o.idMOTIVADORES = :tbMOTIVADORES", TbMOTIVADORESCARGOSEntity.class).setParameter("tbMOTIVADORES", tbMOTIVADORES).getResultList();
    }

}
