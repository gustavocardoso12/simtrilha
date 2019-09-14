package org.applicationn.simtrilhas.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Named;
import javax.transaction.Transactional;

import org.applicationn.simtrilhas.domain.TbNOEntity;
import org.applicationn.simtrilhas.service.security.SecurityWrapper;

@Named
public class TbNOService extends BaseService<TbNOEntity> implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public TbNOService(){
        super(TbNOEntity.class);
    }
    
    @Transactional
    public List<TbNOEntity> findAllTbNOEntities() {
        
        return entityManager.createQuery("SELECT o FROM TbNO o ", TbNOEntity.class).getResultList();
    }
    
    @Override
    @Transactional
    public long countAllEntries() {
        return entityManager.createQuery("SELECT COUNT(o) FROM TbNO o", Long.class).getSingleResult();
    }
    
    @Override
    @Transactional
    public TbNOEntity save(TbNOEntity tbNO) {
        String username = SecurityWrapper.getUsername();
        
        tbNO.updateAuditInformation(username);
        
        return super.save(tbNO);
    }
    
    @Override
    @Transactional
    public TbNOEntity update(TbNOEntity tbNO) {
        String username = SecurityWrapper.getUsername();
        tbNO.updateAuditInformation(username);
        return super.update(tbNO);
    }
    
    @Override
    protected void handleDependenciesBeforeDelete(TbNOEntity tbNO) {

        /* This is called before a TbNO is deleted. Place here all the
           steps to cut dependencies to other entities */
        
    }

}
