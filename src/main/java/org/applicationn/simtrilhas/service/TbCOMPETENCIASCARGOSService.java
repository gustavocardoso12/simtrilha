package org.applicationn.simtrilhas.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Named;
import javax.transaction.Transactional;

import org.applicationn.simtrilhas.domain.TbCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbCOMPETENCIASCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbCOMPETENCIASEntity;
import org.applicationn.simtrilhas.domain.TbCONHECIMENTOSESPCARGOSEntity;

@Named
public class TbCOMPETENCIASCARGOSService extends BaseService<TbCOMPETENCIASCARGOSEntity> implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public TbCOMPETENCIASCARGOSService(){
        super(TbCOMPETENCIASCARGOSEntity.class);
    }
    
    @Transactional
    public List<TbCOMPETENCIASCARGOSEntity> findAllTbCOMPETENCIASCARGOSEntities() {
        
        return entityManager.createQuery("SELECT o FROM TbCOMPETENCIASCARGOS o ORDER BY o.idCARGOS, o.idCOMPETENCIAS ", TbCOMPETENCIASCARGOSEntity.class).getResultList();
    }
    
    @Override
    @Transactional
    public long countAllEntries() {
        return entityManager.createQuery("SELECT COUNT(o) FROM TbCOMPETENCIASCARGOS o", Long.class).getSingleResult();
    }
    
    @Override
    protected void handleDependenciesBeforeDelete(TbCOMPETENCIASCARGOSEntity tbCOMPETENCIASCARGOS) {

        /* This is called before a TbCOMPETENCIASCARGOS is deleted. Place here all the
           steps to cut dependencies to other entities */
        
    }

    @Transactional
    public List<TbCOMPETENCIASCARGOSEntity> findAvailableTbCOMPETENCIASCARGOSs(TbCOMPETENCIASEntity tbCOMPETENCIAS) {
        return entityManager.createQuery("SELECT o FROM TbCOMPETENCIASCARGOS o WHERE o.idCOMPETENCIAS IS NULL", TbCOMPETENCIASCARGOSEntity.class).getResultList();
    }

    @Transactional
    public List<TbCOMPETENCIASCARGOSEntity> findTbCOMPETENCIASCARGOSsByIdCOMPETENCIAS(TbCOMPETENCIASEntity tbCOMPETENCIAS) {
        return entityManager.createQuery("SELECT o FROM TbCOMPETENCIASCARGOS o WHERE o.idCOMPETENCIAS = :tbCOMPETENCIAS", TbCOMPETENCIASCARGOSEntity.class).setParameter("tbCOMPETENCIAS", tbCOMPETENCIAS).getResultList();
    }

    @Transactional
    public List<TbCOMPETENCIASCARGOSEntity> findAvailableTbCOMPETENCIASCARGOSs(TbCARGOSEntity tbCARGOS) {
        return entityManager.createQuery("SELECT o FROM TbCOMPETENCIASCARGOS o WHERE o.idCARGOS IS NULL", TbCOMPETENCIASCARGOSEntity.class).getResultList();
    }

    @Transactional
    public List<TbCOMPETENCIASCARGOSEntity> findTbCOMPETENCIASCARGOSsByIdCARGOS(TbCARGOSEntity tbCARGOS) {
        return entityManager.createQuery("SELECT o FROM TbCOMPETENCIASCARGOS o WHERE o.idCARGOS = :tbCARGOS AND o.poNTUACAOCOMPETENCIA >0 order by o.id", TbCOMPETENCIASCARGOSEntity.class).setParameter("tbCARGOS", tbCARGOS).getResultList();
    }
    
    @SuppressWarnings("unchecked")
    public List<TbCOMPETENCIASCARGOSEntity> findTbCOMPETENCIASCARGOSsByIdCARGOS(TbCARGOSEntity tbCARGOS, TbCARGOSEntity tbCARGOSPARA) {
    	return entityManager.createNativeQuery(  "SELECT o.id, o.version, cb.PONTUACAO_COMPETENCIA, o.IDCARGOS_ID, o.IDCOMPETENCIAS_ID FROM TbCOMPETENCIASCARGOS o " + 
    			"             full outer join " + 
    			"        	 TbCOMPETENCIASCARGOS cb " + 
    			"        		ON ( cb.IDCARGOS_ID = :tbCARGOS  and  o.IDCOMPETENCIAS_ID = cb.IDCOMPETENCIAS_ID) " + 
    			"                where (o.IDCARGOS_ID = :tbCARGOSPARA) ORDER BY cb.IDCOMPETENCIAS_ID", TbCOMPETENCIASCARGOSEntity.class)
        		.setParameter("tbCARGOS", tbCARGOS)
        		.setParameter("tbCARGOSPARA", tbCARGOSPARA).getResultList();
    }
    
  

    
    @SuppressWarnings("unchecked")
    public List<TbCOMPETENCIASCARGOSEntity> findTbCOMPETENCIASCARGOSsByIdCARGOSPARA(TbCARGOSEntity tbCARGOS, TbCARGOSEntity tbCARGOSPARA) {
    	return entityManager.createNativeQuery("SELECT o.* FROM TbCOMPETENCIASCARGOS o " + 
    			"             full outer join " + 
    			"        	 TbCOMPETENCIASCARGOS cb " + 
    			"        		ON ( cb.IDCARGOS_ID = :tbCARGOS  and  o.IDCOMPETENCIAS_ID = cb.IDCOMPETENCIAS_ID) " + 
    			"              where (o.IDCARGOS_ID = :tbCARGOSPARA) ORDER BY cb.IDCOMPETENCIAS_ID", TbCOMPETENCIASCARGOSEntity.class)
        		.setParameter("tbCARGOS", tbCARGOS)
        		.setParameter("tbCARGOSPARA", tbCARGOSPARA).getResultList();
    }
    
    
    @Transactional
    public List<TbCOMPETENCIASCARGOSEntity> findTbCOMPETENCIASCARGOSsByIdCARGOSOrder(TbCARGOSEntity tbCARGOS) {
        return entityManager.createQuery("SELECT o FROM TbCOMPETENCIASCARGOS o WHERE o.idCARGOS = :tbCARGOS order by o.id,o.poNTUACAOCOMPETENCIA DESC", TbCOMPETENCIASCARGOSEntity.class).setParameter("tbCARGOS", tbCARGOS).getResultList();
    }

}
