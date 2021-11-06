package org.applicationn.simtrilhas.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;
import javax.transaction.Transactional;


import org.applicationn.simtrilhas.domain.TbPONTCARGOSEntity;

@Named
public class TbPONTCARGOSService extends BaseService<TbPONTCARGOSEntity> implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public TbPONTCARGOSService(){
        super(TbPONTCARGOSEntity.class);
    }
    
    @Transactional
    public List<TbPONTCARGOSEntity> findAllTbPONTCARGOSEntities() {
    	List<TbPONTCARGOSEntity>  result = new ArrayList<>();

    	result =getEntityManager().createQuery("SELECT o FROM TbPONTCARGOS o order by o.id", TbPONTCARGOSEntity.class).getResultList();  
        return result;
    }
    
    
    @Transactional
    public TbPONTCARGOSEntity findPONTCARGOSByRequisito(String Requisito) {
    	TbPONTCARGOSEntity result =getEntityManager().createQuery("SELECT o FROM TbPONTCARGOS o WHERE o.deSCPONT = :deSCPONT", TbPONTCARGOSEntity.class)
    			.setParameter("deSCPONT", Requisito)
    			.getSingleResult();
        return result;
    }
    
    @Override
    @Transactional
    public long countAllEntries() {
        return getEntityManager().createQuery("SELECT COUNT(o) FROM TbPONTCARGOS o", Long.class).getSingleResult();
    }
    
    @Override
    protected void handleDependenciesBeforeDelete(TbPONTCARGOSEntity tbPONTCARGOS) {


        
    }

}
