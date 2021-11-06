package org.applicationn.simtrilhas.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Named;
import javax.transaction.Transactional;

import org.applicationn.simtrilhas.domain.TbCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbHABILIDADESCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbHABILIDADESEntity;

@Named
public class TbHABILIDADESCARGOSService extends BaseService<TbHABILIDADESCARGOSEntity> implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public TbHABILIDADESCARGOSService(){
        super(TbHABILIDADESCARGOSEntity.class);
    }
    
    @Transactional
    public List<TbHABILIDADESCARGOSEntity> findAllTbHABILIDADESCARGOSEntities() {
        
        return getEntityManager().createQuery("SELECT o FROM TbHABILIDADESCARGOS o ", TbHABILIDADESCARGOSEntity.class).getResultList();
    }
    
    @Override
    @Transactional
    public long countAllEntries() {
        return getEntityManager().createQuery("SELECT COUNT(o) FROM TbHABILIDADESCARGOS o", Long.class).getSingleResult();
    }
    
    @Override
    protected void handleDependenciesBeforeDelete(TbHABILIDADESCARGOSEntity tbHABILIDADESCARGOS) {

        /* This is called before a TbHABILIDADESCARGOS is deleted. Place here all the
           steps to cut dependencies to other entities */
        
    }

    @Transactional
    public List<TbHABILIDADESCARGOSEntity> findAvailableTbHABILIDADESCARGOSs(TbCARGOSEntity tbCARGOS) {
        return getEntityManager().createQuery("SELECT o FROM TbHABILIDADESCARGOS o WHERE o.idCARGOS IS NULL", TbHABILIDADESCARGOSEntity.class).getResultList();
    }

    @Transactional
    public List<TbHABILIDADESCARGOSEntity> findTbHABILIDADESCARGOSsByIdCARGOS(TbCARGOSEntity tbCARGOS) {
        return getEntityManager().createQuery("SELECT o FROM TbHABILIDADESCARGOS o WHERE o.idCARGOS = :tbCARGOS ORDER BY o.idHABCARGOS", TbHABILIDADESCARGOSEntity.class).setParameter("tbCARGOS", tbCARGOS).getResultList();
    }

    @Transactional
    public List<TbHABILIDADESCARGOSEntity> findAvailableTbHABILIDADESCARGOSs(TbHABILIDADESEntity tbHABILIDADES) {
        return getEntityManager().createQuery("SELECT o FROM TbHABILIDADESCARGOS o WHERE o.idHABCARGOS IS NULL", TbHABILIDADESCARGOSEntity.class).getResultList();
    }

    @Transactional
    public List<TbHABILIDADESCARGOSEntity> findTbHABILIDADESCARGOSsByIdHABCARGOS(TbHABILIDADESEntity tbHABILIDADES) {
        return getEntityManager().createQuery("SELECT o FROM TbHABILIDADESCARGOS o WHERE o.idHABCARGOS = :tbHABILIDADES", TbHABILIDADESCARGOSEntity.class).setParameter("tbHABILIDADES", tbHABILIDADES).getResultList();
    }

}
