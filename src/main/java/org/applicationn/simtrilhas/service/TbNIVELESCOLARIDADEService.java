package org.applicationn.simtrilhas.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Named;
import javax.transaction.Transactional;

import org.applicationn.simtrilhas.domain.TbNivelEscolaridadeEntity;
import org.applicationn.simtrilhas.service.security.SecurityWrapper;

@Named
public class TbNIVELESCOLARIDADEService extends BaseService<TbNivelEscolaridadeEntity> implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public TbNIVELESCOLARIDADEService(){
        super(TbNivelEscolaridadeEntity.class);
    }
    
    @Transactional
    public List<TbNivelEscolaridadeEntity> findAlltbNIVELESCOLARIDADEEntities() {
        
        return getEntityManager().createQuery("SELECT o FROM tbNIVELESCOLARIDADE o order by o.idFormacao ", TbNivelEscolaridadeEntity.class).getResultList();
    }
    
    
    @Transactional
    public List<TbNivelEscolaridadeEntity> findAlltbNIVELESCOLARIDADEEntitiesGrupo2() {
        
        return getEntityManager().createQuery("SELECT o FROM tbNIVELESCOLARIDADE o where o.grupo = 2 order by o.idFormacao ", TbNivelEscolaridadeEntity.class).getResultList();
    }
    
    @Transactional
    public List<TbNivelEscolaridadeEntity> findAlltbNIVELESCOLARIDADEEntitiesGrupo3() {
        
        return getEntityManager().createQuery("SELECT o FROM tbNIVELESCOLARIDADE o where o.grupo = 3 order by o.idFormacao ", TbNivelEscolaridadeEntity.class).getResultList();
    }
    
    @Transactional
    public List<TbNivelEscolaridadeEntity> findAlltbNIVELESCOLARIDADEEntitiesGrupo4() {
        
        return getEntityManager().createQuery("SELECT o FROM tbNIVELESCOLARIDADE o where o.grupo = 4 order by o.idFormacao ", TbNivelEscolaridadeEntity.class).getResultList();
    }
    
    @Transactional
    public List<TbNivelEscolaridadeEntity> findAlltbNIVELESCOLARIDADEEntitiesGrupo5() {
        
        return getEntityManager().createQuery("SELECT o FROM tbNIVELESCOLARIDADE o where o.grupo = 5 order by o.idFormacao ", TbNivelEscolaridadeEntity.class).getResultList();
    }
    
    
    @Transactional
    public List<TbNivelEscolaridadeEntity> findAlltbNIVELESCOLARIDADEEntitiesGrupo6() {
        
        return getEntityManager().createQuery("SELECT o FROM tbNIVELESCOLARIDADE o where o.grupo = 6 order by o.idFormacao ", TbNivelEscolaridadeEntity.class).getResultList();
    }
    
    @Transactional
    public List<TbNivelEscolaridadeEntity> findAlltbNIVELESCOLARIDADEEntitiesGrupo1() {
        
        return getEntityManager().createQuery("SELECT o FROM tbNIVELESCOLARIDADE o where o.grupo = 1 order by o.idFormacao ", TbNivelEscolaridadeEntity.class).getResultList();
    }
    
    
    @Override
    @Transactional
    public long countAllEntries() {
        return getEntityManager().createQuery("SELECT COUNT(o) FROM tbNIVELESCOLARIDADE o", Long.class).getSingleResult();
    }

    
    @Override
    @Transactional
    public TbNivelEscolaridadeEntity save(TbNivelEscolaridadeEntity tbNIVELESCOLARIDADE) {
        String username = SecurityWrapper.getUsername();
        
        tbNIVELESCOLARIDADE.updateAuditInformation(username);
        
        return super.save(tbNIVELESCOLARIDADE);
    }
    
    @Override
    @Transactional
    public TbNivelEscolaridadeEntity update(TbNivelEscolaridadeEntity tbNIVELESCOLARIDADE) {
        String username = SecurityWrapper.getUsername();
        tbNIVELESCOLARIDADE.updateAuditInformation(username);
        return super.update(tbNIVELESCOLARIDADE);
    }
    
    @Override
    protected void handleDependenciesBeforeDelete(TbNivelEscolaridadeEntity tbNIVELESCOLARIDADE) {

        /* This is called before a tbNIVELESCOLARIDADE is deleted. Place here all the
           steps to cut dependencies to other entities */
        
      //  this.cutAllIdAREATbDEPTOsAssignments(tbNIVELESCOLARIDADE);
        
    }

    
    
}
