package org.applicationn.simtrilhas.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Named;
import javax.transaction.Transactional;

import org.applicationn.simtrilhas.domain.TbCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbCONHECIMENTOSBASCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbGRADECARGOSEntity;
import org.applicationn.simtrilhas.domain.TbGRADEEntity;

@Named
public class TbGRADECARGOSService extends BaseService<TbGRADECARGOSEntity> implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public TbGRADECARGOSService(){
        super(TbGRADECARGOSEntity.class);
    }
    
    @Transactional
    public List<TbGRADECARGOSEntity> findAllTbGRADECARGOSEntities() {
        
        return entityManager.createQuery("SELECT o FROM TbGRADECARGOS o  ORDER BY o.idCARGOS,o.idGRADE ", TbGRADECARGOSEntity.class).getResultList();
    }
    
    @Override
    @Transactional
    public long countAllEntries() {
        return entityManager.createQuery("SELECT COUNT(o) FROM TbGRADECARGOS o", Long.class).getSingleResult();
    }
    
    @Override
    protected void handleDependenciesBeforeDelete(TbGRADECARGOSEntity tbGRADECARGOS) {

        /* This is called before a TbGRADECARGOS is deleted. Place here all the
           steps to cut dependencies to other entities */
        
    }

    @Transactional
    public List<TbGRADECARGOSEntity> findAvailableTbGRADECARGOSs(TbCARGOSEntity tbCARGOS) {
        return entityManager.createQuery("SELECT o FROM TbGRADECARGOS o WHERE o.idCARGOS IS NULL", TbGRADECARGOSEntity.class).getResultList();
    }

    @Transactional
    public List<TbGRADECARGOSEntity> findTbGRADECARGOSsByIdCARGOS(TbCARGOSEntity tbCARGOS) {
        return entityManager.createQuery("SELECT o FROM TbGRADECARGOS o WHERE o.idCARGOS = :tbCARGOS AND o.poNTUACAOGRADE >0 ORDER BY o.id", TbGRADECARGOSEntity.class).setParameter("tbCARGOS", tbCARGOS).getResultList();
    }
    
    
    
    @SuppressWarnings("unchecked")
    public List<TbGRADECARGOSEntity> findTbGRADECARGOSsByIdCARGOS(TbCARGOSEntity tbCARGOS, TbCARGOSEntity tbCARGOSPARA) {
        return entityManager.createNativeQuery( "SELECT o.id, o.version, cb.PONTUACAO_GRADE, o.IDCARGOS_ID, o.IDGRADE_ID FROM TB_GRADES_CARGOS o " + 
        		"             full outer join " + 
        		"        	 TB_GRADES_CARGOS cb " + 
        		"        		ON ( cb.IDCARGOS_ID = :tbCARGOS  and  o.IDGRADE_ID = cb.IDGRADE_ID) " + 
        		"                where (o.IDCARGOS_ID = :tbCARGOSPARA) ORDER BY cb.IDGRADE_ID", TbGRADECARGOSEntity.class)
        		.setParameter("tbCARGOS", tbCARGOS)
        		.setParameter("tbCARGOSPARA", tbCARGOSPARA).getResultList();
    }
    
   
    
    
    
    @SuppressWarnings("unchecked")
	public List<TbGRADECARGOSEntity> findTbGRADECARGOSsByIdCARGOSPARA(TbCARGOSEntity tbCARGOS, TbCARGOSEntity tbCARGOSPARA) {
        return entityManager.createNativeQuery(   " SELECT o.* FROM TB_GRADES_CARGOS o " + 
        		"             full outer join " + 
        		"        	 TB_GRADES_CARGOS cb " + 
        		"        		ON ( cb.IDCARGOS_ID = :tbCARGOS  and  o.IDGRADE_ID = cb.IDGRADE_ID) " + 
        		"                where (o.IDCARGOS_ID = :tbCARGOSPARA) ORDER BY cb.IDGRADE_ID", TbGRADECARGOSEntity.class)
        		.setParameter("tbCARGOS", tbCARGOS)
        		.setParameter("tbCARGOSPARA", tbCARGOSPARA).getResultList();
    }
    
 
    
    
    
    @Transactional
    public List<TbGRADECARGOSEntity> findTbGRADECARGOSsByIdCARGOSOrder(TbCARGOSEntity tbCARGOS) {
        return entityManager.createQuery("SELECT o FROM TbGRADECARGOS o WHERE o.idCARGOS = :tbCARGOS ORDER BY o.id,o.poNTUACAOGRADE DESC", TbGRADECARGOSEntity.class).setParameter("tbCARGOS", tbCARGOS).getResultList();
    }

    @Transactional
    public List<TbGRADECARGOSEntity> findAvailableTbGRADECARGOSs(TbGRADEEntity tbGRADE) {
        return entityManager.createQuery("SELECT o FROM TbGRADECARGOS o WHERE o.idGRADE IS NULL", TbGRADECARGOSEntity.class).getResultList();
    }

    @Transactional
    public List<TbGRADECARGOSEntity> findTbGRADECARGOSsByIdGRADE(TbGRADEEntity tbGRADE) {
        return entityManager.createQuery("SELECT o FROM TbGRADECARGOS o WHERE o.idGRADE = :tbGRADE", TbGRADECARGOSEntity.class).setParameter("tbGRADE", tbGRADE).getResultList();
    }

}
