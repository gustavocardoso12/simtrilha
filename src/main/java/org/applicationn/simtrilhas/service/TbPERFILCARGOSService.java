package org.applicationn.simtrilhas.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Named;
import javax.transaction.Transactional;

import org.applicationn.simtrilhas.domain.TbCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbPERFILCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbPERFILEntity;

@Named
public class TbPERFILCARGOSService extends BaseService<TbPERFILCARGOSEntity> implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public TbPERFILCARGOSService(){
        super(TbPERFILCARGOSEntity.class);
    }
    
    @Transactional
    public List<TbPERFILCARGOSEntity> findAllTbPERFILCARGOSEntities() {
        
        return entityManager.createQuery("SELECT o FROM TbPERFILCARGOS o ", TbPERFILCARGOSEntity.class).getResultList();
    }
    
    @Override
    @Transactional
    public long countAllEntries() {
        return entityManager.createQuery("SELECT COUNT(o) FROM TbPERFILCARGOS o", Long.class).getSingleResult();
    }
    
    @Override
    protected void handleDependenciesBeforeDelete(TbPERFILCARGOSEntity tbPERFILCARGOS) {

        /* This is called before a TbPERFILCARGOS is deleted. Place here all the
           steps to cut dependencies to other entities */
        
    }

    @Transactional
    public List<TbPERFILCARGOSEntity> findAvailableTbPERFILCARGOSs(TbCARGOSEntity tbCARGOS) {
        return entityManager.createQuery("SELECT o FROM TbPERFILCARGOS o WHERE o.idCARGOS IS NULL", TbPERFILCARGOSEntity.class).getResultList();
    }

    @Transactional
    public List<TbPERFILCARGOSEntity> findTbPERFILCARGOSsByIdCARGOS(TbCARGOSEntity tbCARGOS) {
        return entityManager.createQuery("SELECT o FROM TbPERFILCARGOS o WHERE o.idCARGOS = :tbCARGOS", TbPERFILCARGOSEntity.class).setParameter("tbCARGOS", tbCARGOS).getResultList();
    }

    @Transactional
    public List<TbPERFILCARGOSEntity> findAvailableTbPERFILCARGOSs(TbPERFILEntity tbPERFIL) {
        return entityManager.createQuery("SELECT o FROM TbPERFILCARGOS o WHERE o.idPERFIL IS NULL", TbPERFILCARGOSEntity.class).getResultList();
    }

    @Transactional
    public List<TbPERFILCARGOSEntity> findTbPERFILCARGOSsByIdPERFIL(TbPERFILEntity tbPERFIL) {
        return entityManager.createQuery("SELECT o FROM TbPERFILCARGOS o WHERE o.idPERFIL = :tbPERFIL", TbPERFILCARGOSEntity.class).setParameter("tbPERFIL", tbPERFIL).getResultList();
    }

}
