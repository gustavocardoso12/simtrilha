package org.applicationn.simtrilhas.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Named;
import javax.transaction.Transactional;

import org.applicationn.simtrilhas.domain.TbCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbCONHECIMENTOSESPCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbCONHECIMENTOSESPECIFICOSEntity;
import org.applicationn.simtrilhas.service.security.SecurityWrapper;

@Named
public class TbCONHECIMENTOSESPCARGOSService extends BaseService<TbCONHECIMENTOSESPCARGOSEntity> implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public TbCONHECIMENTOSESPCARGOSService(){
        super(TbCONHECIMENTOSESPCARGOSEntity.class);
    }
    
    @Override
    @Transactional
    public TbCONHECIMENTOSESPCARGOSEntity save(TbCONHECIMENTOSESPCARGOSEntity tbCONHECIMENTOSESPCARGOS) {
        String username = SecurityWrapper.getUsername();
        
        tbCONHECIMENTOSESPCARGOS.updateAuditInformation(username);
        
        return super.save(tbCONHECIMENTOSESPCARGOS);
    }
    
    @Override
    @Transactional
    public TbCONHECIMENTOSESPCARGOSEntity update(TbCONHECIMENTOSESPCARGOSEntity tbCONHECIMENTOSESPCARGOS) {
        String username = SecurityWrapper.getUsername();
        tbCONHECIMENTOSESPCARGOS.updateAuditInformation(username);
        return super.update(tbCONHECIMENTOSESPCARGOS);
    }
    
    @Transactional
    public List<TbCONHECIMENTOSESPCARGOSEntity> findAllTbCONHECIMENTOSESPCARGOSEntities() {
        
        return getEntityManager().createQuery("SELECT o FROM TbCONHECIMENTOSESPCARGOS o ORDER BY o.idCARGOS , o.idCONHECESP ", TbCONHECIMENTOSESPCARGOSEntity.class).getResultList();
    }
    
    @Override
    @Transactional
    public long countAllEntries() {
        return getEntityManager().createQuery("SELECT COUNT(o) FROM TbCONHECIMENTOSESPCARGOS o", Long.class).getSingleResult();
    }
    
    @Override
    protected void handleDependenciesBeforeDelete(TbCONHECIMENTOSESPCARGOSEntity tbCONHECIMENTOSESPCARGOS) {

        /* This is called before a TbCONHECIMENTOSESPCARGOS is deleted. Place here all the
           steps to cut dependencies to other entities */
        
    }

    @Transactional
    public List<TbCONHECIMENTOSESPCARGOSEntity> findAvailableTbCONHECIMENTOSESPCARGOSs(TbCARGOSEntity tbCARGOS) {
        return getEntityManager().createQuery("SELECT o FROM TbCONHECIMENTOSESPCARGOS o WHERE o.idCARGOS IS NULL", TbCONHECIMENTOSESPCARGOSEntity.class).getResultList();
    }
    
    
    @Transactional
    public List<TbCONHECIMENTOSESPCARGOSEntity> findTbCONHECIMENTOSESPCARGOSsByIdCARGOS(TbCARGOSEntity tbCARGOS) {
        return getEntityManager().createQuery("SELECT o FROM TbCONHECIMENTOSESPCARGOS o WHERE o.idCARGOS = :tbCARGOS AND o.poNTUACAOCONESP >0 order by o.id", TbCONHECIMENTOSESPCARGOSEntity.class).setParameter("tbCARGOS", tbCARGOS).getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<TbCONHECIMENTOSESPCARGOSEntity> findTbCONHECIMENTOSESPCARGOSsByIdCARGOS(TbCARGOSEntity tbCARGOS, TbCARGOSEntity tbCARGOSPARA) {
    	return getEntityManager().createNativeQuery( "SELECT o.id, o.version, cb.PONTUACAO_CON_ESP, o.IDCARGOS_ID, o.IDCONHECESP_ID,o.MASCARA,"
    			+ " o.ENTRY_CREATED_BY, o.ENTRY_CREATED_AT, o.ENTRY_MODIFIED_BY,o.ENTRY_MODIFIED_AT "
    			+ " FROM TBCONHECIMENTOSESPCARGOS o " + 
    			"             full outer join " + 
    			"        	 TBCONHECIMENTOSESPCARGOS cb " + 
    			"        		ON ( cb.IDCARGOS_ID = :tbCARGOS  and  o.IDCONHECESP_ID = cb.IDCONHECESP_ID) " + 
    			"                where (o.IDCARGOS_ID = :tbCARGOSPARA) ORDER BY cb.IDCONHECESP_ID", TbCONHECIMENTOSESPCARGOSEntity.class)
        		.setParameter("tbCARGOS", tbCARGOS)
        		.setParameter("tbCARGOSPARA", tbCARGOSPARA).getResultList();
    }
    
   
    
    @SuppressWarnings("unchecked")
    public List<TbCONHECIMENTOSESPCARGOSEntity> findTbCONHECIMENTOSESPCARGOSsByIdCARGOSPARA(TbCARGOSEntity tbCARGOS, TbCARGOSEntity tbCARGOSPARA) {
    	return getEntityManager().createNativeQuery( " SELECT o.* FROM TBCONHECIMENTOSESPCARGOS o " + 
    			"             full outer join " + 
    			"        	 TBCONHECIMENTOSESPCARGOS cb " + 
    			"        		ON ( cb.IDCARGOS_ID = :tbCARGOS  and  o.IDCONHECESP_ID = cb.IDCONHECESP_ID) " + 
    			"                where (o.IDCARGOS_ID = :tbCARGOSPARA) ORDER BY cb.IDCONHECESP_ID"
    		    , TbCONHECIMENTOSESPCARGOSEntity.class)
        		.setParameter("tbCARGOS", tbCARGOS)
        		.setParameter("tbCARGOSPARA", tbCARGOSPARA).getResultList();
    }
    
    
   
    @Transactional
    public List<TbCONHECIMENTOSESPCARGOSEntity> findTbCONHECIMENTOSESPCARGOSsByIdCARGOSOrder(TbCARGOSEntity tbCARGOS) {
        return getEntityManager().createQuery("SELECT o FROM TbCONHECIMENTOSESPCARGOS o WHERE o.idCARGOS = :tbCARGOS ORDER BY o.id, o.poNTUACAOCONESP DESC", TbCONHECIMENTOSESPCARGOSEntity.class).setParameter("tbCARGOS", tbCARGOS).getResultList();
    }

    @Transactional
    public List<TbCONHECIMENTOSESPCARGOSEntity> findAvailableTbCONHECIMENTOSESPCARGOSs(TbCONHECIMENTOSESPECIFICOSEntity tbCONHECIMENTOSESPECIFICOS) {
        return getEntityManager().createQuery("SELECT o FROM TbCONHECIMENTOSESPCARGOS o WHERE o.idCONHECESP IS NULL", TbCONHECIMENTOSESPCARGOSEntity.class).getResultList();
    }

    @Transactional
    public List<TbCONHECIMENTOSESPCARGOSEntity> findTbCONHECIMENTOSESPCARGOSsByIdCONHECESP(TbCONHECIMENTOSESPECIFICOSEntity tbCONHECIMENTOSESPECIFICOS) {
        return getEntityManager().createQuery("SELECT o FROM TbCONHECIMENTOSESPCARGOS o WHERE o.idCONHECESP = :tbCONHECIMENTOSESPECIFICOS", TbCONHECIMENTOSESPCARGOSEntity.class).setParameter("tbCONHECIMENTOSESPECIFICOS", tbCONHECIMENTOSESPECIFICOS).getResultList();
    }

}
