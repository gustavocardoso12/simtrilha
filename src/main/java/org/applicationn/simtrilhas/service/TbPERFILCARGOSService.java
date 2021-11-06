package org.applicationn.simtrilhas.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Named;
import javax.transaction.Transactional;

import org.applicationn.simtrilhas.domain.TbCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbPERFILCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbPERFILEntity;
import org.applicationn.simtrilhas.service.security.SecurityWrapper;

@Named
public class TbPERFILCARGOSService extends BaseService<TbPERFILCARGOSEntity> implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public TbPERFILCARGOSService(){
        super(TbPERFILCARGOSEntity.class);
    }
    
    
    @Override
    @Transactional
    public TbPERFILCARGOSEntity save(TbPERFILCARGOSEntity TbPERFILCARGOS) {
        String username = SecurityWrapper.getUsername();
        
        TbPERFILCARGOS.updateAuditInformation(username);
        
        return super.save(TbPERFILCARGOS);
    }
    
    
    @Override
    @Transactional
    public TbPERFILCARGOSEntity update(TbPERFILCARGOSEntity TbPERFILCARGOS) {
        String username = SecurityWrapper.getUsername();
        TbPERFILCARGOS.updateAuditInformation(username);
        return super.update(TbPERFILCARGOS);
    }
    
    @Transactional
    public List<TbPERFILCARGOSEntity> findAllTbPERFILCARGOSEntities() {
        
        return getEntityManager().createQuery("SELECT o FROM TbPERFILCARGOS o ORDER BY o.idCARGOS, o.idPERFIL ", TbPERFILCARGOSEntity.class).getResultList();
    }
    
    @Override
    @Transactional
    public long countAllEntries() {
        return getEntityManager().createQuery("SELECT COUNT(o) FROM TbPERFILCARGOS o", Long.class).getSingleResult();
    }
    
    @Override
    protected void handleDependenciesBeforeDelete(TbPERFILCARGOSEntity tbPERFILCARGOS) {

        /* This is called before a TbPERFILCARGOS is deleted. Place here all the
           steps to cut dependencies to other entities */
        
    }

    @Transactional
    public List<TbPERFILCARGOSEntity> findAvailableTbPERFILCARGOSs(TbCARGOSEntity tbCARGOS) {
        return getEntityManager().createQuery("SELECT o FROM TbPERFILCARGOS o WHERE o.idCARGOS IS NULL", TbPERFILCARGOSEntity.class).getResultList();
    }

    @Transactional
    public List<TbPERFILCARGOSEntity> findTbPERFILCARGOSsByIdCARGOS(TbCARGOSEntity tbCARGOS) {
        return getEntityManager().createQuery("SELECT o FROM TbPERFILCARGOS o WHERE o.idCARGOS = :tbCARGOS AND o.poNTUACAOPERFIL > 0 ORDER BY o.id", TbPERFILCARGOSEntity.class).setParameter("tbCARGOS", tbCARGOS).getResultList();
    }
    
    @SuppressWarnings("unchecked")
    public List<TbPERFILCARGOSEntity> findTbPERFILCARGOSsByIdCARGOS(TbCARGOSEntity tbCARGOS, TbCARGOSEntity tbCARGOSPARA) {
    	return getEntityManager().createNativeQuery(  "SELECT o.id, o.version, cb.PONTUACAO_PERFIL, o.IDCARGOS_ID, o.IDPERFIL_ID,o.MASCARA, "
    			+ " o.ENTRY_CREATED_BY, o.ENTRY_CREATED_AT, o.ENTRY_MODIFIED_BY,o.ENTRY_MODIFIED_AT "
    			+ "FROM TB_PERFIL_CARGOS o " + 
    			"             full outer join " + 
    			"        	 TB_PERFIL_CARGOS cb " + 
    			"        		ON ( cb.IDCARGOS_ID = :tbCARGOS  and  o.IDPERFIL_ID = cb.IDPERFIL_ID) " + 
    			"                where (o.IDCARGOS_ID = :tbCARGOSPARA) ORDER BY cb.IDPERFIL_ID", TbPERFILCARGOSEntity.class)
        		.setParameter("tbCARGOS", tbCARGOS)
        		.setParameter("tbCARGOSPARA", tbCARGOSPARA).getResultList();
    }
    
  
    
    
    @SuppressWarnings("unchecked")
   	public List<TbPERFILCARGOSEntity> findTbPERFILCARGOSsByIdCARGOSPARA(TbCARGOSEntity tbCARGOS, TbCARGOSEntity tbCARGOSPARA) {
           return getEntityManager().createNativeQuery("SELECT o.* FROM TB_PERFIL_CARGOS o " + 
        			"             full outer join " + 
        			"        	 TB_PERFIL_CARGOS cb " + 
        			"        		ON ( cb.IDCARGOS_ID = :tbCARGOS  and  o.IDPERFIL_ID = cb.IDPERFIL_ID) " + 
        			"                where (o.IDCARGOS_ID = :tbCARGOSPARA) ORDER BY cb.IDPERFIL_ID", TbPERFILCARGOSEntity.class)
           		.setParameter("tbCARGOS", tbCARGOS)
           		.setParameter("tbCARGOSPARA", tbCARGOSPARA).getResultList();
       }
       
    
   
    
    @Transactional
    public List<TbPERFILCARGOSEntity> findTbPERFILCARGOSsByIdCARGOSOrder(TbCARGOSEntity tbCARGOS) {
        return getEntityManager().createQuery("SELECT o FROM TbPERFILCARGOS o WHERE o.idCARGOS = :tbCARGOS ORDER BY o.id,o.poNTUACAOPERFIL DESC", TbPERFILCARGOSEntity.class).setParameter("tbCARGOS", tbCARGOS).getResultList();
    }

    @Transactional
    public List<TbPERFILCARGOSEntity> findAvailableTbPERFILCARGOSs(TbPERFILEntity tbPERFIL) {
        return getEntityManager().createQuery("SELECT o FROM TbPERFILCARGOS o WHERE o.idPERFIL IS NULL", TbPERFILCARGOSEntity.class).getResultList();
    }

    @Transactional
    public List<TbPERFILCARGOSEntity> findTbPERFILCARGOSsByIdPERFIL(TbPERFILEntity tbPERFIL) {
        return getEntityManager().createQuery("SELECT o FROM TbPERFILCARGOS o WHERE o.idPERFIL = :tbPERFIL", TbPERFILCARGOSEntity.class).setParameter("tbPERFIL", tbPERFIL).getResultList();
    }

}
