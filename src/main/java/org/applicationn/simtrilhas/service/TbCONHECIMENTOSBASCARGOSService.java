package org.applicationn.simtrilhas.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Named;
import javax.transaction.Transactional;

import org.applicationn.simtrilhas.domain.TbCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbCONHECIMENTOSBASCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbCONHECIMENTOSBASICOSEntity;

@Named
public class TbCONHECIMENTOSBASCARGOSService extends BaseService<TbCONHECIMENTOSBASCARGOSEntity> implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public TbCONHECIMENTOSBASCARGOSService(){
        super(TbCONHECIMENTOSBASCARGOSEntity.class);
    }
    
    @Transactional
    public List<TbCONHECIMENTOSBASCARGOSEntity> findAllTbCONHECIMENTOSBASCARGOSEntities() {
        
        return entityManager.createQuery("SELECT o FROM TbCONHECIMENTOSBASCARGOS o ORDER BY o.idCARGOS, o.idCONHECBAS  ", TbCONHECIMENTOSBASCARGOSEntity.class).getResultList();
    }
    
    @Override
    @Transactional
    public long countAllEntries() {
        return entityManager.createQuery("SELECT COUNT(o) FROM TbCONHECIMENTOSBASCARGOS o", Long.class).getSingleResult();
    }
    
    @Override
    protected void handleDependenciesBeforeDelete(TbCONHECIMENTOSBASCARGOSEntity tbCONHECIMENTOSBASCARGOS) {

        /* This is called before a TbCONHECIMENTOSBASCARGOS is deleted. Place here all the
           steps to cut dependencies to other entities */
        
    }

    @Transactional
    public List<TbCONHECIMENTOSBASCARGOSEntity> findAvailableTbCONHECIMENTOSBASCARGOSs(TbCARGOSEntity tbCARGOS) {
        return entityManager.createQuery("SELECT o FROM TbCONHECIMENTOSBASCARGOS o WHERE o.idCARGOS IS NULL", TbCONHECIMENTOSBASCARGOSEntity.class).getResultList();
    }

    @Transactional
    public List<TbCONHECIMENTOSBASCARGOSEntity> findTbCONHECIMENTOSBASCARGOSsByIdCARGOS(TbCARGOSEntity tbCARGOS) {
        return entityManager.createQuery("SELECT o FROM TbCONHECIMENTOSBASCARGOS o WHERE o.idCARGOS = :tbCARGOS ORDER BY o.id", TbCONHECIMENTOSBASCARGOSEntity.class).setParameter("tbCARGOS", tbCARGOS).getResultList();
    }

    @Transactional
    public List<TbCONHECIMENTOSBASCARGOSEntity> findAvailableTbCONHECIMENTOSBASCARGOSs(TbCONHECIMENTOSBASICOSEntity tbCONHECIMENTOSBASICOS) {
        return entityManager.createQuery("SELECT o FROM TbCONHECIMENTOSBASCARGOS o WHERE o.idCONHECBAS IS NULL", TbCONHECIMENTOSBASCARGOSEntity.class).getResultList();
    }

    @Transactional
    public List<TbCONHECIMENTOSBASCARGOSEntity> findTbCONHECIMENTOSBASCARGOSsByIdCONHECBAS(TbCONHECIMENTOSBASICOSEntity tbCONHECIMENTOSBASICOS) {
        return entityManager.createQuery("SELECT o FROM TbCONHECIMENTOSBASCARGOS o WHERE o.idCONHECBAS = :tbCONHECIMENTOSBASICOS", TbCONHECIMENTOSBASCARGOSEntity.class).setParameter("tbCONHECIMENTOSBASICOS", tbCONHECIMENTOSBASICOS).getResultList();
    }

}
