package org.applicationn.simtrilhas.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Named;
import javax.transaction.Transactional;

import org.applicationn.simtrilhas.domain.TbCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbCONHECIMENTOSBASCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbCONHECIMENTOSBASICOSEntity;
import org.applicationn.simtrilhas.service.security.SecurityWrapper;

@Named
public class TbCONHECIMENTOSBASCARGOSService extends BaseService<TbCONHECIMENTOSBASCARGOSEntity> implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public TbCONHECIMENTOSBASCARGOSService(){
        super(TbCONHECIMENTOSBASCARGOSEntity.class);
    }
    
    @Override
    @Transactional
    public TbCONHECIMENTOSBASCARGOSEntity save(TbCONHECIMENTOSBASCARGOSEntity tbCONHECIMENTOSBASCARGOS) {
        String username = SecurityWrapper.getUsername();
        
        tbCONHECIMENTOSBASCARGOS.updateAuditInformation(username);
        
        return super.save(tbCONHECIMENTOSBASCARGOS);
    }
    
    @Override
    @Transactional
    public TbCONHECIMENTOSBASCARGOSEntity update(TbCONHECIMENTOSBASCARGOSEntity tbCONHECIMENTOSBASCARGOS) {
        String username = SecurityWrapper.getUsername();
        tbCONHECIMENTOSBASCARGOS.updateAuditInformation(username);
        return super.update(tbCONHECIMENTOSBASCARGOS);
    }
    
    
    @Transactional
    public List<TbCONHECIMENTOSBASCARGOSEntity> findAllTbCONHECIMENTOSBASCARGOSEntities() {
        
        return getEntityManager().createQuery("SELECT o FROM TbCONHECIMENTOSBASCARGOS o ORDER BY o.idCARGOS, o.idCONHECBAS  ", TbCONHECIMENTOSBASCARGOSEntity.class).getResultList();
    }
    
    @Override
    @Transactional
    public long countAllEntries() {
        return getEntityManager().createQuery("SELECT COUNT(o) FROM TbCONHECIMENTOSBASCARGOS o", Long.class).getSingleResult();
    }
    
    @Override
    protected void handleDependenciesBeforeDelete(TbCONHECIMENTOSBASCARGOSEntity tbCONHECIMENTOSBASCARGOS) {

        /* This is called before a TbCONHECIMENTOSBASCARGOS is deleted. Place here all the
           steps to cut dependencies to other entities */
        
    }

    @Transactional
    public List<TbCONHECIMENTOSBASCARGOSEntity> findAvailableTbCONHECIMENTOSBASCARGOSs(TbCARGOSEntity tbCARGOS) {
        return getEntityManager().createQuery("SELECT o FROM TbCONHECIMENTOSBASCARGOS o WHERE o.idCARGOS IS NULL", TbCONHECIMENTOSBASCARGOSEntity.class).getResultList();
    }
    
    @Transactional
    public List<TbCONHECIMENTOSBASCARGOSEntity> findTbCONHECIMENTOSBASCARGOSsByIdCARGOS(TbCARGOSEntity tbCARGOS) {
        return getEntityManager().createQuery("SELECT o FROM TbCONHECIMENTOSBASCARGOS o WHERE o.idCARGOS = :tbCARGOS AND o.poNTUACAOCONBAS >0 order by o.id", TbCONHECIMENTOSBASCARGOSEntity.class).setParameter("tbCARGOS", tbCARGOS).getResultList();
    }
    
    
    
    @SuppressWarnings("unchecked")
    public List<TbCONHECIMENTOSBASCARGOSEntity> findTbCONHECIMENTOSBASCARGOSsByIdCARGOSPARA(TbCARGOSEntity tbCARGOS, TbCARGOSEntity tbCARGOSPARA) {
        return getEntityManager().createNativeQuery(" SELECT o.* FROM TBCONHECIMENTOSBASCARGOS o " + 
        		"             full outer join " + 
        		"        	 TBCONHECIMENTOSBASCARGOS cb " + 
        		"        		ON ( cb.IDCARGOS_ID = :tbCARGOS  and  o.IDCONHECBAS_ID = cb.IDCONHECBAS_ID) " + 
        		"                where (o.IDCARGOS_ID = :tbCARGOSPARA) ORDER BY cb.IDCONHECBAS_ID"
        		, TbCONHECIMENTOSBASCARGOSEntity.class)
        		.setParameter("tbCARGOS", tbCARGOS)
        		.setParameter("tbCARGOSPARA", tbCARGOSPARA)
        		.getResultList();
    }
    
    @SuppressWarnings("unchecked")
	public List<TbCONHECIMENTOSBASCARGOSEntity> findTbCONHECIMENTOSBASCARGOSsByIdCARGOS(TbCARGOSEntity tbCARGOS, TbCARGOSEntity tbCARGOSPARA) {
        return getEntityManager().createNativeQuery("SELECT o.id, o.version, cb.PONTUACAOCONBAS, o.IDCARGOS_ID, o.IDCONHECBAS_ID,o.MASCARA, "
        		+ " o.ENTRY_CREATED_BY, o.ENTRY_CREATED_AT, o.ENTRY_MODIFIED_BY,o.ENTRY_MODIFIED_AT "
        		+ " FROM TBCONHECIMENTOSBASCARGOS o " + 
        		"             full outer join " + 
        		"        	 TBCONHECIMENTOSBASCARGOS cb " + 
        		"        		ON ( cb.IDCARGOS_ID = :tbCARGOS  and  o.IDCONHECBAS_ID = cb.IDCONHECBAS_ID) " + 
        		"                where (o.IDCARGOS_ID = :tbCARGOSPARA) ORDER BY cb.IDCONHECBAS_ID", TbCONHECIMENTOSBASCARGOSEntity.class)
        		.setParameter("tbCARGOS", tbCARGOS)
        		.setParameter("tbCARGOSPARA", tbCARGOSPARA)
        		.getResultList();
    }
    
    
    
    

    
    
    @Transactional
    public List<TbCONHECIMENTOSBASCARGOSEntity> findTbCONHECIMENTOSBASCARGOSsByIdCARGOSOrder(TbCARGOSEntity tbCARGOS) {
        return getEntityManager().createQuery("SELECT o FROM TbCONHECIMENTOSBASCARGOS o WHERE o.idCARGOS = :tbCARGOS ORDER BY o.id,o.poNTUACAOCONBAS DESC", TbCONHECIMENTOSBASCARGOSEntity.class).setParameter("tbCARGOS", tbCARGOS).getResultList();
    }

    @Transactional
    public List<TbCONHECIMENTOSBASCARGOSEntity> findAvailableTbCONHECIMENTOSBASCARGOSs(TbCONHECIMENTOSBASICOSEntity tbCONHECIMENTOSBASICOS) {
        return getEntityManager().createQuery("SELECT o FROM TbCONHECIMENTOSBASCARGOS o WHERE o.idCONHECBAS IS NULL", TbCONHECIMENTOSBASCARGOSEntity.class).getResultList();
    }

    @Transactional
    public List<TbCONHECIMENTOSBASCARGOSEntity> findTbCONHECIMENTOSBASCARGOSsByIdCONHECBAS(TbCONHECIMENTOSBASICOSEntity tbCONHECIMENTOSBASICOS) {
        return getEntityManager().createQuery("SELECT o FROM TbCONHECIMENTOSBASCARGOS o WHERE o.idCONHECBAS = :tbCONHECIMENTOSBASICOS", TbCONHECIMENTOSBASCARGOSEntity.class).setParameter("tbCONHECIMENTOSBASICOS", tbCONHECIMENTOSBASICOS).getResultList();
    }

}
