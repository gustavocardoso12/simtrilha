package org.applicationn.simtrilhas.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;
import javax.transaction.Transactional;

import org.applicationn.simtrilhas.domain.TbCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbDEPTOEntity;
import org.applicationn.simtrilhas.domain.TbMATRIZCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbNOEntity;

@Named
public class TbCARGOSService extends BaseService<TbCARGOSEntity> implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public TbCARGOSService(){
        super(TbCARGOSEntity.class);
    }
    
    @Transactional
    public List<TbCARGOSEntity> findAllTbCARGOSEntities() {
        
        return entityManager.createQuery("SELECT o FROM TbCARGOS o WHERE o.flagPessoa = 'NAO' ORDER BY o.id ", TbCARGOSEntity.class).getResultList();
    }
    
    @Transactional
    public List<TbCARGOSEntity> findAllTbCARGOSEntitiesMatriz(String flag_pessoa) {
        
    	List<TbCARGOSEntity> result = new ArrayList<>();
    	if(flag_pessoa== null) {

    	}else {
    		if (flag_pessoa.equals("NAO")) {
    			result = entityManager.createQuery("SELECT o FROM TbCARGOS o WHERE o.flagPessoa = 'NAO' ORDER BY o.id ", TbCARGOSEntity.class).getResultList();
    		}else {
    			result = entityManager.createQuery("SELECT o FROM TbCARGOS o ORDER BY o.id ", TbCARGOSEntity.class).getResultList();
    		}
    	}
		return result;
    }
    
    
    
    
    @Transactional
    public List<TbCARGOSEntity> findTbCARGOSEntities(Long idCargos) {
        
        return entityManager.createQuery("SELECT o FROM TbCARGOS o where o.id = :idCargos ORDER BY o.id", TbCARGOSEntity.class)
        		.setParameter("idCargos", idCargos)
        		.getResultList();
    }
    
    
    
    @Transactional
    public List<TbCARGOSEntity> findFiveTbCARGOSEntities() {
        
    	return entityManager.createQuery("SELECT o FROM TbCARGOS o ORDER BY o.id ", TbCARGOSEntity.class)
    			.getResultList();
    }
    
    @Transactional
    public List<TbCARGOSEntity> findTbCARGOSSPessoasEntities() {
        
    	return entityManager.createQuery("SELECT o FROM TbCARGOS o WHERE o.flagPessoa = 'NAO' ORDER BY o.id ", TbCARGOSEntity.class)
    			.getResultList();
    }
    
    @Transactional
    public List<TbCARGOSEntity> AllTbCARGOSEntities() {
        
    	return entityManager.createQuery("SELECT o FROM TbCARGOS o ORDER BY o.id DESC ", TbCARGOSEntity.class)
    			.getResultList();
    }
    
    @Override
    @Transactional
    public long countAllEntries() {
        return entityManager.createQuery("SELECT COUNT(o) FROM TbCARGOS o", Long.class).getSingleResult();
    }
    
    @Transactional
    public long UltimoID() {
        return entityManager.createQuery("SELECT MAX(o.id) FROM TbCARGOS o", Long.class).getSingleResult();
    }
    
    @Override
    protected void handleDependenciesBeforeDelete(TbCARGOSEntity tbCARGOS) {

        /* This is called before a TbCARGOS is deleted. Place here all the
           steps to cut dependencies to other entities */
        
        this.cutAllIdCARGOSTbCOMPETENCIASCARGOSsAssignments(tbCARGOS);
        
        this.cutAllIdCARGOSTbGRADECARGOSsAssignments(tbCARGOS);
        
        this.cutAllIdCARGOSTbPERFILCARGOSsAssignments(tbCARGOS);
        
        this.cutAllIdCARGOSTbMOTIVADORESCARGOSsAssignments(tbCARGOS);
        
      this.cutAllIdCARGOSTbONHECESPCARGOSsAssignments(tbCARGOS);
      
      this.cutAllIdCARGOSTbONHECBASCARGOSsAssignments(tbCARGOS);
        
        
    }
    
 // Remove all assignments from all tbCOMPETENCIASCARGOS a tbCARGOS. Called before delete a tbCARGOS.
    @Transactional
    private void cutAllIdCARGOSTbONHECBASCARGOSsAssignments(TbCARGOSEntity tbCARGOS) {
        entityManager
                .createQuery("UPDATE TbCONHECIMENTOSBASCARGOS c SET c.idCARGOS = NULL WHERE c.idCARGOS = :p")
                .setParameter("p", tbCARGOS).executeUpdate();
    }
    
    // Remove all assignments from all tbCOMPETENCIASCARGOS a tbCARGOS. Called before delete a tbCARGOS.
    @Transactional
    private void cutAllIdCARGOSTbONHECESPCARGOSsAssignments(TbCARGOSEntity tbCARGOS) {
        entityManager
                .createQuery("UPDATE TbCONHECIMENTOSESPCARGOS c SET c.idCARGOS = NULL WHERE c.idCARGOS = :p")
                .setParameter("p", tbCARGOS).executeUpdate();
    }

    // Remove all assignments from all tbCOMPETENCIASCARGOS a tbCARGOS. Called before delete a tbCARGOS.
    @Transactional
    private void cutAllIdCARGOSTbCOMPETENCIASCARGOSsAssignments(TbCARGOSEntity tbCARGOS) {
        entityManager
                .createQuery("UPDATE TbCOMPETENCIASCARGOS c SET c.idCARGOS = NULL WHERE c.idCARGOS = :p")
                .setParameter("p", tbCARGOS).executeUpdate();
    }
    
    // Remove all assignments from all tbGRADECARGOS a tbCARGOS. Called before delete a tbCARGOS.
    @Transactional
    private void cutAllIdCARGOSTbGRADECARGOSsAssignments(TbCARGOSEntity tbCARGOS) {
        entityManager
                .createQuery("UPDATE TbGRADECARGOS c SET c.idCARGOS = NULL WHERE c.idCARGOS = :p")
                .setParameter("p", tbCARGOS).executeUpdate();
    }
    
    // Remove all assignments from all tbPERFILCARGOS a tbCARGOS. Called before delete a tbCARGOS.
    @Transactional
    private void cutAllIdCARGOSTbPERFILCARGOSsAssignments(TbCARGOSEntity tbCARGOS) {
        entityManager
                .createQuery("UPDATE TbPERFILCARGOS c SET c.idCARGOS = NULL WHERE c.idCARGOS = :p")
                .setParameter("p", tbCARGOS).executeUpdate();
    }
    
    // Remove all assignments from all tbMOTIVADORESCARGOS a tbCARGOS. Called before delete a tbCARGOS.
    @Transactional
    private void cutAllIdCARGOSTbMOTIVADORESCARGOSsAssignments(TbCARGOSEntity tbCARGOS) {
 
    }
    
    @Transactional
    public List<TbCARGOSEntity> findAvailableTbCARGOSs(TbDEPTOEntity tbDEPTO) {
        return entityManager.createQuery("SELECT o FROM TbCARGOS o WHERE o.idDEPTO IS NULL", TbCARGOSEntity.class).getResultList();
    }

    @Transactional
    public List<TbCARGOSEntity> findTbCARGOSsByIdDEPTO(TbDEPTOEntity tbDEPTO) {
        return entityManager.createQuery("SELECT o FROM TbCARGOS o WHERE o.idDEPTO = :tbDEPTO", TbCARGOSEntity.class).setParameter("tbDEPTO", tbDEPTO).getResultList();
    }
    
    @Transactional
    public List<TbCARGOSEntity> findAvailableTbCARGOSs(TbNOEntity tbNO) {
        return entityManager.createQuery("SELECT o FROM TbCARGOS o WHERE o.idNO IS NULL", TbCARGOSEntity.class).getResultList();
    }

    @Transactional
    public List<TbCARGOSEntity> findTbCARGOSsByIdNO(TbNOEntity tbNO) {
        return entityManager.createQuery("SELECT o FROM TbCARGOS o WHERE o.idNO = :tbNO", TbCARGOSEntity.class).setParameter("tbNO", tbNO).getResultList();
    }

}
