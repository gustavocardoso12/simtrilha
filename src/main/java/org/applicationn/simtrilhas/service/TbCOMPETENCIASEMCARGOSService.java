package org.applicationn.simtrilhas.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Named;
import javax.transaction.Transactional;

import org.applicationn.simtrilhas.domain.TbCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbCOMPETENCIASEMCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbCOMPETENCIASEMOCIONAISEntity;

@Named
public class TbCOMPETENCIASEMCARGOSService extends BaseService<TbCOMPETENCIASEMCARGOSEntity> implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public TbCOMPETENCIASEMCARGOSService(){
        super(TbCOMPETENCIASEMCARGOSEntity.class);
    }
    
    @Transactional
    public List<TbCOMPETENCIASEMCARGOSEntity> findAllTbCOMPETENCIASEMCARGOSEntities() {
        
        return getEntityManager().createQuery("SELECT o FROM TbCOMPETENCIASEMCARGOS o ", TbCOMPETENCIASEMCARGOSEntity.class).getResultList();
    }
    
    @Override
    @Transactional
    public long countAllEntries() {
        return getEntityManager().createQuery("SELECT COUNT(o) FROM TbCOMPETENCIASEMCARGOS o", Long.class).getSingleResult();
    }
    
    @Override
    protected void handleDependenciesBeforeDelete(TbCOMPETENCIASEMCARGOSEntity tbCOMPETENCIASEMCARGOS) {

        /* This is called before a TbCOMPETENCIASEMCARGOS is deleted. Place here all the
           steps to cut dependencies to other entities */
        
    }

    @Transactional
    public List<TbCOMPETENCIASEMCARGOSEntity> findAvailableTbCOMPETENCIASEMCARGOSs(TbCARGOSEntity tbCARGOS) {
        return getEntityManager().createQuery("SELECT o FROM TbCOMPETENCIASEMCARGOS o WHERE o.idCARGOS IS NULL", TbCOMPETENCIASEMCARGOSEntity.class).getResultList();
    }

    @Transactional
    public List<TbCOMPETENCIASEMCARGOSEntity> findTbCOMPETENCIASEMCARGOSsByIdCARGOS(TbCARGOSEntity tbCARGOS) {
        return getEntityManager().createQuery("SELECT o FROM TbCOMPETENCIASEMCARGOS o WHERE o.idCARGOS = :tbCARGOS", TbCOMPETENCIASEMCARGOSEntity.class).setParameter("tbCARGOS", tbCARGOS).getResultList();
    }

    @Transactional
    public List<TbCOMPETENCIASEMCARGOSEntity> findAvailableTbCOMPETENCIASEMCARGOSs(TbCOMPETENCIASEMOCIONAISEntity tbCOMPETENCIASEMOCIONAIS) {
        return getEntityManager().createQuery("SELECT o FROM TbCOMPETENCIASEMCARGOS o WHERE o.idCOMPEM IS NULL", TbCOMPETENCIASEMCARGOSEntity.class).getResultList();
    }

    @Transactional
    public List<TbCOMPETENCIASEMCARGOSEntity> findTbCOMPETENCIASEMCARGOSsByIdCOMPEM(TbCOMPETENCIASEMOCIONAISEntity tbCOMPETENCIASEMOCIONAIS) {
        return getEntityManager().createQuery("SELECT o FROM TbCOMPETENCIASEMCARGOS o WHERE o.idCOMPEM = :tbCOMPETENCIASEMOCIONAIS", TbCOMPETENCIASEMCARGOSEntity.class).setParameter("tbCOMPETENCIASEMOCIONAIS", tbCOMPETENCIASEMOCIONAIS).getResultList();
    }

}
