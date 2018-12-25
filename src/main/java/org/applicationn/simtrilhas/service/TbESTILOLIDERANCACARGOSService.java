package org.applicationn.simtrilhas.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Named;
import javax.transaction.Transactional;

import org.applicationn.simtrilhas.domain.TbCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbESTILOLIDERANCACARGOSEntity;
import org.applicationn.simtrilhas.domain.TbESTILOLIDERANCAEntity;

@Named
public class TbESTILOLIDERANCACARGOSService extends BaseService<TbESTILOLIDERANCACARGOSEntity> implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public TbESTILOLIDERANCACARGOSService(){
        super(TbESTILOLIDERANCACARGOSEntity.class);
    }
    
    @Transactional
    public List<TbESTILOLIDERANCACARGOSEntity> findAllTbESTILOLIDERANCACARGOSEntities() {
        
        return entityManager.createQuery("SELECT o FROM TbESTILOLIDERANCACARGOS o ", TbESTILOLIDERANCACARGOSEntity.class).getResultList();
    }
    
    @Override
    @Transactional
    public long countAllEntries() {
        return entityManager.createQuery("SELECT COUNT(o) FROM TbESTILOLIDERANCACARGOS o", Long.class).getSingleResult();
    }
    
    @Override
    protected void handleDependenciesBeforeDelete(TbESTILOLIDERANCACARGOSEntity tbESTILOLIDERANCACARGOS) {

        /* This is called before a TbESTILOLIDERANCACARGOS is deleted. Place here all the
           steps to cut dependencies to other entities */
        
    }

    @Transactional
    public List<TbESTILOLIDERANCACARGOSEntity> findAvailableTbESTILOLIDERANCACARGOSs(TbCARGOSEntity tbCARGOS) {
        return entityManager.createQuery("SELECT o FROM TbESTILOLIDERANCACARGOS o WHERE o.idCARGOS IS NULL", TbESTILOLIDERANCACARGOSEntity.class).getResultList();
    }

    @Transactional
    public List<TbESTILOLIDERANCACARGOSEntity> findTbESTILOLIDERANCACARGOSsByIdCARGOS(TbCARGOSEntity tbCARGOS) {
        return entityManager.createQuery("SELECT o FROM TbESTILOLIDERANCACARGOS o WHERE o.idCARGOS = :tbCARGOS", TbESTILOLIDERANCACARGOSEntity.class).setParameter("tbCARGOS", tbCARGOS).getResultList();
    }

    @Transactional
    public List<TbESTILOLIDERANCACARGOSEntity> findAvailableTbESTILOLIDERANCACARGOSs(TbESTILOLIDERANCAEntity tbESTILOLIDERANCA) {
        return entityManager.createQuery("SELECT o FROM TbESTILOLIDERANCACARGOS o WHERE o.idESTLIDER IS NULL", TbESTILOLIDERANCACARGOSEntity.class).getResultList();
    }

    @Transactional
    public List<TbESTILOLIDERANCACARGOSEntity> findTbESTILOLIDERANCACARGOSsByIdESTLIDER(TbESTILOLIDERANCAEntity tbESTILOLIDERANCA) {
        return entityManager.createQuery("SELECT o FROM TbESTILOLIDERANCACARGOS o WHERE o.idESTLIDER = :tbESTILOLIDERANCA", TbESTILOLIDERANCACARGOSEntity.class).setParameter("tbESTILOLIDERANCA", tbESTILOLIDERANCA).getResultList();
    }

}
