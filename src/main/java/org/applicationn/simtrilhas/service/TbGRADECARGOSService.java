package org.applicationn.simtrilhas.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Named;
import javax.transaction.Transactional;

import org.applicationn.simtrilhas.domain.TbCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbGRADECARGOSEntity;
import org.applicationn.simtrilhas.domain.TbGRADEEntity;
import org.applicationn.simtrilhas.service.security.SecurityWrapper;

@Named
public class TbGRADECARGOSService extends BaseService<TbGRADECARGOSEntity> implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public TbGRADECARGOSService(){
        super(TbGRADECARGOSEntity.class);
    }
    
    
    @Override
    @Transactional
    public TbGRADECARGOSEntity save(TbGRADECARGOSEntity TbGRADECARGOS) {
        String username = SecurityWrapper.getUsername();
        
        TbGRADECARGOS.updateAuditInformation(username);
        
        return super.save(TbGRADECARGOS);
    }
    
    
    @Override
    @Transactional
    public TbGRADECARGOSEntity update(TbGRADECARGOSEntity TbGRADECARGOS) {
        String username = SecurityWrapper.getUsername();
        TbGRADECARGOS.updateAuditInformation(username);
        return super.update(TbGRADECARGOS);
    }
    
    
    
    @Transactional
    public List<TbGRADECARGOSEntity> findAllTbGRADECARGOSEntities() {
        
        return getEntityManager().createQuery("SELECT o FROM TbGRADECARGOS o  ORDER BY o.idCARGOS,o.idGRADE ", TbGRADECARGOSEntity.class).getResultList();
    }
    
    
    @Transactional
    public TbGRADECARGOSEntity findTbGRADECARGOSEntities() {
        
        return getEntityManager().createQuery("SELECT o FROM TbGRADECARGOS o  ORDER BY o.idCARGOS,o.idGRADE ", TbGRADECARGOSEntity.class).getSingleResult();
    }
    
    
    @Override
    @Transactional
    public long countAllEntries() {
        return getEntityManager().createQuery("SELECT COUNT(o) FROM TbGRADECARGOS o", Long.class).getSingleResult();
    }
    
    @Override
    protected void handleDependenciesBeforeDelete(TbGRADECARGOSEntity tbGRADECARGOS) {

        /* This is called before a TbGRADECARGOS is deleted. Place here all the
           steps to cut dependencies to other entities */
        
    }

    @Transactional
    public List<TbGRADECARGOSEntity> findAvailableTbGRADECARGOSs(TbCARGOSEntity tbCARGOS) {
        return getEntityManager().createQuery("SELECT o FROM TbGRADECARGOS o WHERE o.idCARGOS IS NULL", TbGRADECARGOSEntity.class).getResultList();
    }

    @Transactional
    public List<TbGRADECARGOSEntity> findTbGRADECARGOSsByIdCARGOS(TbCARGOSEntity tbCARGOS) {
        return getEntityManager().createQuery("SELECT o FROM TbGRADECARGOS o WHERE o.idCARGOS = :tbCARGOS AND o.poNTUACAOGRADE >0 ORDER BY o.id", TbGRADECARGOSEntity.class).setParameter("tbCARGOS", tbCARGOS).getResultList();
    }
    
    
    
    @SuppressWarnings("unchecked")
    public List<TbGRADECARGOSEntity> findTbGRADECARGOSsByIdCARGOS(TbCARGOSEntity tbCARGOS, TbCARGOSEntity tbCARGOSPARA) {
        return getEntityManager().createNativeQuery( "SELECT o.id, o.version, cb.PONTUACAO_GRADE, o.IDCARGOS_ID, o.IDGRADE_ID, o.ENTRY_CREATED_BY, o.ENTRY_CREATED_AT, o.ENTRY_MODIFIED_BY,o.ENTRY_MODIFIED_AT"
        		+ " FROM TB_GRADES_CARGOS o " + 
        		"             full outer join " + 
        		"        	 TB_GRADES_CARGOS cb " + 
        		"        		ON ( cb.IDCARGOS_ID = :tbCARGOS  and  o.IDGRADE_ID = cb.IDGRADE_ID) " + 
        		"                where (o.IDCARGOS_ID = :tbCARGOSPARA) ORDER BY cb.IDGRADE_ID", TbGRADECARGOSEntity.class)
        		.setParameter("tbCARGOS", tbCARGOS)
        		.setParameter("tbCARGOSPARA", tbCARGOSPARA).getResultList();
    }
    
   
    
    
    
    @SuppressWarnings("unchecked")
	public List<TbGRADECARGOSEntity> findTbGRADECARGOSsByIdCARGOSPARA(TbCARGOSEntity tbCARGOS, TbCARGOSEntity tbCARGOSPARA) {
        return getEntityManager().createNativeQuery(   " SELECT o.* FROM TB_GRADES_CARGOS o " + 
        		"             full outer join " + 
        		"        	 TB_GRADES_CARGOS cb " + 
        		"        		ON ( cb.IDCARGOS_ID = :tbCARGOS  and  o.IDGRADE_ID = cb.IDGRADE_ID) " + 
        		"                where (o.IDCARGOS_ID = :tbCARGOSPARA) ORDER BY cb.IDGRADE_ID", TbGRADECARGOSEntity.class)
        		.setParameter("tbCARGOS", tbCARGOS)
        		.setParameter("tbCARGOSPARA", tbCARGOSPARA).getResultList();
    }
    
 
    
    
    
    @Transactional
    public List<TbGRADECARGOSEntity> findTbGRADECARGOSsByIdCARGOSOrder(TbCARGOSEntity tbCARGOS) {
        return getEntityManager().createQuery("SELECT o FROM TbGRADECARGOS o WHERE o.idCARGOS = :tbCARGOS ORDER BY o.id,o.poNTUACAOGRADE DESC", TbGRADECARGOSEntity.class).setParameter("tbCARGOS", tbCARGOS).getResultList();
    }

    @Transactional
    public List<TbGRADECARGOSEntity> findAvailableTbGRADECARGOSs(TbGRADEEntity tbGRADE) {
        return getEntityManager().createQuery("SELECT o FROM TbGRADECARGOS o WHERE o.idGRADE IS NULL", TbGRADECARGOSEntity.class).getResultList();
    }

    @Transactional
    public List<TbGRADECARGOSEntity> findTbGRADECARGOSsByIdGRADE(TbGRADEEntity tbGRADE) {
        return getEntityManager().createQuery("SELECT o FROM TbGRADECARGOS o WHERE o.idGRADE = :tbGRADE", TbGRADECARGOSEntity.class).setParameter("tbGRADE", tbGRADE).getResultList();
    }

}
