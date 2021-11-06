package org.applicationn.simtrilhas.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Named;
import javax.transaction.Transactional;

import org.applicationn.simtrilhas.domain.TbMASCARAEntity;
import org.applicationn.simtrilhas.service.security.SecurityWrapper;

@Named
public class TbMASCARAService extends BaseService<TbMASCARAEntity> implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public TbMASCARAService(){
        super(TbMASCARAEntity.class);
    }
    
    @Transactional
    public List<TbMASCARAEntity> findAllTbMASCARAEntities() {
        
        return getEntityManager().createQuery("SELECT o FROM TbMASCARA o ", TbMASCARAEntity.class).getResultList();
    }
    
    @Override
    @Transactional
    public long countAllEntries() {
        return getEntityManager().createQuery("SELECT COUNT(o) FROM TbMASCARA o", Long.class).getSingleResult();
    }
    
    

    
    @Override
    @Transactional
    public TbMASCARAEntity save(TbMASCARAEntity TbMASCARA) {
        String username = SecurityWrapper.getUsername();
        
        TbMASCARA.updateAuditInformation(username);
        
        return super.save(TbMASCARA);
    }
    
    @Override
    @Transactional
    public TbMASCARAEntity update(TbMASCARAEntity TbMASCARA) {
        String username = SecurityWrapper.getUsername();
        TbMASCARA.updateAuditInformation(username);
        return super.update(TbMASCARA);
    }
    
    @Override
    protected void handleDependenciesBeforeDelete(TbMASCARAEntity TbMASCARA) {

        /* This is called before a TbMASCARA is deleted. Place here all the
           steps to cut dependencies to other entities */
        
        this.cutAllIdAREATbCONHECBASsAssignments(TbMASCARA);
        this.cutAllIdAREATbCONHECESPsAssignments(TbMASCARA);
        this.cutAllIdAREATbPERFILsAssignments(TbMASCARA);
    }

    // Remove all assignments from all tbDEPTO a TbMASCARA. Called before delete a TbMASCARA.
    @Transactional
    private void cutAllIdAREATbCONHECBASsAssignments(TbMASCARAEntity TbMASCARA) {
    	getEntityManager()
                .createQuery("UPDATE TbCONHECIMENTOSBASICOS c SET c.grupo = NULL WHERE c.grupo = :p")
                .setParameter("p", TbMASCARA.getIdGrupo()).executeUpdate();
    }
    
    
    @Transactional
    private void cutAllIdAREATbCONHECESPsAssignments(TbMASCARAEntity TbMASCARA) {
    	getEntityManager()
                .createQuery("UPDATE TbCONHECIMENTOSESPECIFICOS c SET c.grupo = NULL WHERE c.grupo = :p")
                .setParameter("p", TbMASCARA.getIdGrupo()).executeUpdate();
    }
    
    @Transactional
    private void cutAllIdAREATbPERFILsAssignments(TbMASCARAEntity TbMASCARA) {
    	getEntityManager()
                .createQuery("UPDATE TB_PERFIL c SET c.grupo = NULL WHERE c.grupo = :p")
                .setParameter("p", TbMASCARA.getIdGrupo()).executeUpdate();
    }
    
}
