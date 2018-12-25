package org.applicationn.simtrilhas.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Named;
import javax.transaction.Transactional;

import org.applicationn.simtrilhas.domain.TbCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbESTILOPENSAMENTOCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbESTILOPENSAMENTOEntity;

@Named
public class TbESTILOPENSAMENTOCARGOSService extends BaseService<TbESTILOPENSAMENTOCARGOSEntity> implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public TbESTILOPENSAMENTOCARGOSService(){
        super(TbESTILOPENSAMENTOCARGOSEntity.class);
    }
    
    @Transactional
    public List<TbESTILOPENSAMENTOCARGOSEntity> findAllTbESTILOPENSAMENTOCARGOSEntities() {
        
        return entityManager.createQuery("SELECT o FROM TbESTILOPENSAMENTOCARGOS o ", TbESTILOPENSAMENTOCARGOSEntity.class).getResultList();
    }
    
    @Override
    @Transactional
    public long countAllEntries() {
        return entityManager.createQuery("SELECT COUNT(o) FROM TbESTILOPENSAMENTOCARGOS o", Long.class).getSingleResult();
    }
    
    @Override
    protected void handleDependenciesBeforeDelete(TbESTILOPENSAMENTOCARGOSEntity tbESTILOPENSAMENTOCARGOS) {

        /* This is called before a TbESTILOPENSAMENTOCARGOS is deleted. Place here all the
           steps to cut dependencies to other entities */
        
    }

    @Transactional
    public List<TbESTILOPENSAMENTOCARGOSEntity> findAvailableTbESTILOPENSAMENTOCARGOSs(TbCARGOSEntity tbCARGOS) {
        return entityManager.createQuery("SELECT o FROM TbESTILOPENSAMENTOCARGOS o WHERE o.idCARGOS IS NULL", TbESTILOPENSAMENTOCARGOSEntity.class).getResultList();
    }

    @Transactional
    public List<TbESTILOPENSAMENTOCARGOSEntity> findTbESTILOPENSAMENTOCARGOSsByIdCARGOS(TbCARGOSEntity tbCARGOS) {
        return entityManager.createQuery("SELECT o FROM TbESTILOPENSAMENTOCARGOS o WHERE o.idCARGOS = :tbCARGOS", TbESTILOPENSAMENTOCARGOSEntity.class).setParameter("tbCARGOS", tbCARGOS).getResultList();
    }

    @Transactional
    public List<TbESTILOPENSAMENTOCARGOSEntity> findAvailableTbESTILOPENSAMENTOCARGOSs(TbESTILOPENSAMENTOEntity tbESTILOPENSAMENTO) {
        return entityManager.createQuery("SELECT o FROM TbESTILOPENSAMENTOCARGOS o WHERE o.idESTPENSAMENTO IS NULL", TbESTILOPENSAMENTOCARGOSEntity.class).getResultList();
    }

    @Transactional
    public List<TbESTILOPENSAMENTOCARGOSEntity> findTbESTILOPENSAMENTOCARGOSsByIdESTPENSAMENTO(TbESTILOPENSAMENTOEntity tbESTILOPENSAMENTO) {
        return entityManager.createQuery("SELECT o FROM TbESTILOPENSAMENTOCARGOS o WHERE o.idESTPENSAMENTO = :tbESTILOPENSAMENTO", TbESTILOPENSAMENTOCARGOSEntity.class).setParameter("tbESTILOPENSAMENTO", tbESTILOPENSAMENTO).getResultList();
    }

}
