package org.applicationn.simtrilhas.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Named;
import javax.transaction.Transactional;

import org.applicationn.simtrilhas.domain.TbCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbHABILIDADESCULTCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbHABILIDADESCULTURAISEntity;

@Named
public class TbHABILIDADESCULTCARGOSService extends BaseService<TbHABILIDADESCULTCARGOSEntity> implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public TbHABILIDADESCULTCARGOSService(){
        super(TbHABILIDADESCULTCARGOSEntity.class);
    }
    
    @Transactional
    public List<TbHABILIDADESCULTCARGOSEntity> findAllTbHABILIDADESCULTCARGOSEntities() {
        
        return entityManager.createQuery("SELECT o FROM TbHABILIDADESCULTCARGOS o  ", TbHABILIDADESCULTCARGOSEntity.class).getResultList();
    }
    
    @Override
    @Transactional
    public long countAllEntries() {
        return entityManager.createQuery("SELECT COUNT(o) FROM TbHABILIDADESCULTCARGOS o", Long.class).getSingleResult();
    }
    
    @Override
    protected void handleDependenciesBeforeDelete(TbHABILIDADESCULTCARGOSEntity tbHABILIDADESCULTCARGOS) {

        /* This is called before a TbHABILIDADESCULTCARGOS is deleted. Place here all the
           steps to cut dependencies to other entities */
        
    }

    @Transactional
    public List<TbHABILIDADESCULTCARGOSEntity> findAvailableTbHABILIDADESCULTCARGOSs(TbCARGOSEntity tbCARGOS) {
        return entityManager.createQuery("SELECT o FROM TbHABILIDADESCULTCARGOS o WHERE o.idCARGOS IS NULL", TbHABILIDADESCULTCARGOSEntity.class).getResultList();
    }

    @Transactional
    public List<TbHABILIDADESCULTCARGOSEntity> findTbHABILIDADESCULTCARGOSsByIdCARGOS(TbCARGOSEntity tbCARGOS) {
        return entityManager.createQuery("SELECT o FROM TbHABILIDADESCULTCARGOS o WHERE o.idCARGOS = :tbCARGOS", TbHABILIDADESCULTCARGOSEntity.class).setParameter("tbCARGOS", tbCARGOS).getResultList();
    }

    @Transactional
    public List<TbHABILIDADESCULTCARGOSEntity> findAvailableTbHABILIDADESCULTCARGOSs(TbHABILIDADESCULTURAISEntity tbHABILIDADESCULTURAIS) {
        return entityManager.createQuery("SELECT o FROM TbHABILIDADESCULTCARGOS o WHERE o.idHABCULTCAR IS NULL", TbHABILIDADESCULTCARGOSEntity.class).getResultList();
    }

    @Transactional
    public List<TbHABILIDADESCULTCARGOSEntity> findTbHABILIDADESCULTCARGOSsByIdHABCULTCAR(TbHABILIDADESCULTURAISEntity tbHABILIDADESCULTURAIS) {
        return entityManager.createQuery("SELECT o FROM TbHABILIDADESCULTCARGOS o WHERE o.idHABCULTCAR = :tbHABILIDADESCULTURAIS", TbHABILIDADESCULTCARGOSEntity.class).setParameter("tbHABILIDADESCULTURAIS", tbHABILIDADESCULTURAIS).getResultList();
    }

}
