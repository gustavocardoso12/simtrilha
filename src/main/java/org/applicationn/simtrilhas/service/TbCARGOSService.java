package org.applicationn.simtrilhas.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;
import javax.transaction.Transactional;


import org.applicationn.simtrilhas.domain.TbCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbDEPTOEntity;
import org.applicationn.simtrilhas.domain.TbNOEntity;
import org.applicationn.simtrilhas.service.security.SecurityWrapper;

@Named
public class TbCARGOSService extends BaseService<TbCARGOSEntity> implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public TbCARGOSService(){
        super(TbCARGOSEntity.class);
    }
    
    
    @Override
    @Transactional
    public TbCARGOSEntity save(TbCARGOSEntity tbCARGOS) {
        String username = SecurityWrapper.getUsername();
        
        tbCARGOS.updateAuditInformation(username);
        
        return super.save(tbCARGOS);
    }
    
    
    @Override
    @Transactional
    public TbCARGOSEntity update(TbCARGOSEntity tbCARGOS) {
        String username = SecurityWrapper.getUsername();
        tbCARGOS.updateAuditInformation(username);
        return super.update(tbCARGOS);
    }
    
    
    
    @Transactional
    public List<TbCARGOSEntity> findAllTbCARGOSEntities() {
        
        return getEntityManager().createQuery("SELECT o FROM TbCARGOS o WHERE o.flagPessoa = 'NAO' ORDER BY o.id ", TbCARGOSEntity.class).getResultList();
    }
    
    
    @Transactional
    public List<TbCARGOSEntity> findAllTbPESSOASEntities() {
        
        return getEntityManager().createQuery("SELECT o FROM TbCARGOS o WHERE o.flagPessoa = 'SIM' ORDER BY o.id ", TbCARGOSEntity.class).getResultList();
    }
    
    
    @Transactional
    public List<TbCARGOSEntity> findAllTbCARGOSEntitiesMatriz(String flag_pessoa) {
        
    	List<TbCARGOSEntity> result = new ArrayList<>();
    	if(flag_pessoa== null) {

    	}else {
    		if (flag_pessoa.equals("NAO")) {
    			result = getEntityManager().createQuery("SELECT o FROM TbCARGOS o WHERE o.flagPessoa = 'NAO' ORDER BY o.id ", TbCARGOSEntity.class).getResultList();
    		}else {
    			result = getEntityManager().createQuery("SELECT o FROM TbCARGOS o ORDER BY o.id ", TbCARGOSEntity.class).getResultList();
    		}
    	}
		return result;
    }
    
    
    
    @SuppressWarnings("unchecked")
	@Transactional
    public List<TbCARGOSEntity> findAllTbCARGOSEntitiesMatrizFamiliaDe(String flag_pessoa, String familiaDe) {
        
    	List<TbCARGOSEntity> result = new ArrayList<>();
    	if(flag_pessoa== null) {

    	}else {
    		if(familiaDe.equals("TODOS")) {
    			result = getEntityManager().createNativeQuery(
    					"SELECT o.* FROM TbCARGOS o "+
    					" inner loop join TB_DEPTO td " + 
    					" on (o.IDDEPTO_ID  = td.id ) " + 
    					" inner loop join TB_AREA ta " + 
    					" on (td.IDAREA_ID = ta.id ) " + 
    					" WHERE 1=1"  + 
    					" ORDER BY o.ID", TbCARGOSEntity.class)
    					.getResultList();
    		}else {
    		if (flag_pessoa.equals("NAO")) {
    			result = getEntityManager().createNativeQuery(
    					"SELECT o.* FROM TbCARGOS o "+
    					" inner loop join TB_DEPTO td " + 
    					" on (o.IDDEPTO_ID  = td.id ) " + 
    					" inner loop join TB_AREA ta " + 
    					" on (td.IDAREA_ID = ta.id ) " + 
    					" WHERE o.FLAG_PESSOA = 'NAO' " + 
    					" and ta.DESC_AREA = :familiaDe " + 
    					" ORDER BY o.ID", TbCARGOSEntity.class)
    					.setParameter("familiaDe", familiaDe)
    					.getResultList();
    		}else {
    			result = getEntityManager().createNativeQuery(
    					"SELECT o.* FROM TbCARGOS o "+
    					" inner loop join TB_DEPTO td " + 
    					" on (o.IDDEPTO_ID  = td.id ) " + 
    					" inner loop join TB_AREA ta " + 
    					" on (td.IDAREA_ID = ta.id ) " + 
    					" WHERE 1=1 " + 
    					" and ta.DESC_AREA = :familiaDe " + 
    					" ORDER BY o.ID", TbCARGOSEntity.class)
    					.setParameter("familiaDe", familiaDe)
    					.getResultList();
    		}
    	}
    	}
		return result;
    }
    
    
    @SuppressWarnings("unchecked")
	@Transactional
    public List<TbCARGOSEntity> findAllTbCARGOSEntitiesMatrizFamiliaPara( String familiaPara) {
        
    	List<TbCARGOSEntity> result = new ArrayList<>();
    	
    	if(familiaPara.equals("TODOS")) {
    		result = getEntityManager().createNativeQuery(
					"SELECT o.* FROM TbCARGOS o "+
					" inner loop join TB_DEPTO td " + 
					" on (o.IDDEPTO_ID  = td.id ) " + 
					" inner loop join TB_AREA ta " + 
					" on (td.IDAREA_ID = ta.id ) " + 
					" WHERE 1=1 "
					+ "and o.FLAG_PESSOA = 'NAO' "+ 
					" ORDER BY o.ID", TbCARGOSEntity.class)
					.getResultList();
    	}else {
    			result = getEntityManager().createNativeQuery(
    					"SELECT o.* FROM TbCARGOS o "+
    					" inner loop join TB_DEPTO td " + 
    					" on (o.IDDEPTO_ID  = td.id ) " + 
    					" inner loop join TB_AREA ta " + 
    					" on (td.IDAREA_ID = ta.id ) " + 
    					" WHERE 1=1 " + 
    					" and ta.DESC_AREA = :familiaPara " + 
    					" ORDER BY o.ID", TbCARGOSEntity.class)
    					.setParameter("familiaPara", familiaPara)
    					.getResultList();
    	}
		return result;
    }
    
    @Transactional
    public List<TbCARGOSEntity> findTbCARGOSEntities(Long idCargos) {
        
        return getEntityManager().createQuery("SELECT o FROM TbCARGOS o where o.id = :idCargos ORDER BY o.id", TbCARGOSEntity.class)
        		.setParameter("idCargos", idCargos)
        		.getResultList();
    }
    
    
    
    @Transactional
    public List<TbCARGOSEntity> findFiveTbCARGOSEntities() {
        
    	return getEntityManager().createQuery("SELECT o FROM TbCARGOS o ORDER BY o.id ", TbCARGOSEntity.class)
    			.getResultList();
    }
    
    @Transactional
    public List<TbCARGOSEntity> findTbCARGOSSPessoasEntities() {
        
    	return getEntityManager().createQuery("SELECT o FROM TbCARGOS o WHERE o.flagPessoa = 'NAO' ORDER BY o.id ", TbCARGOSEntity.class)
    			.getResultList();
    }
    
    @Transactional
    public List<TbCARGOSEntity> AllTbCARGOSEntities() {
        
    	return getEntityManager().createQuery("SELECT o FROM TbCARGOS o ORDER BY o.id DESC ", TbCARGOSEntity.class)
    			.getResultList();
    }
    
    @Override
    @Transactional
    public long countAllEntries() {
        return getEntityManager().createQuery("SELECT COUNT(o) FROM TbCARGOS o", Long.class).getSingleResult();
    }
    
    @Transactional
    public long UltimoID() {
        return getEntityManager().createQuery("SELECT MAX(o.id) FROM TbCARGOS o", Long.class).getSingleResult();
    }
    
    @Override
    protected void handleDependenciesBeforeDelete(TbCARGOSEntity tbCARGOS) {

        /* This is called before a TbCARGOS is deleted. Place here all the
           steps to cut dependencies to other entities */
        
        this.cutAllIdCARGOSTbCOMPETENCIASCARGOSsAssignments(tbCARGOS);
        
        this.cutAllIdCARGOSTbGRADECARGOSsAssignments(tbCARGOS);
        
        this.cutAllIdCARGOSTbPERFILCARGOSsAssignments(tbCARGOS);
        
      this.cutAllIdCARGOSTbONHECESPCARGOSsAssignments(tbCARGOS);
      
      this.cutAllIdCARGOSTbONHECBASCARGOSsAssignments(tbCARGOS);
        
        
    }
    
 // Remove all assignments from all tbCOMPETENCIASCARGOS a tbCARGOS. Called before delete a tbCARGOS.
    @Transactional
    private void cutAllIdCARGOSTbONHECBASCARGOSsAssignments(TbCARGOSEntity tbCARGOS) {
        getEntityManager()
                .createQuery("DELETE FROM TbCONHECIMENTOSBASCARGOS c WHERE c.idCARGOS = :p")
                .setParameter("p", tbCARGOS).executeUpdate();
    }
    
    // Remove all assignments from all tbCOMPETENCIASCARGOS a tbCARGOS. Called before delete a tbCARGOS.
    @Transactional
    private void cutAllIdCARGOSTbONHECESPCARGOSsAssignments(TbCARGOSEntity tbCARGOS) {
    	getEntityManager()
                .createQuery("DELETE FROM TbCONHECIMENTOSESPCARGOS c WHERE c.idCARGOS = :p")
                .setParameter("p", tbCARGOS).executeUpdate();
    }

    // Remove all assignments from all tbCOMPETENCIASCARGOS a tbCARGOS. Called before delete a tbCARGOS.
    @Transactional
    private void cutAllIdCARGOSTbCOMPETENCIASCARGOSsAssignments(TbCARGOSEntity tbCARGOS) {
    	getEntityManager()
                .createQuery("DELETE FROM TbCOMPETENCIASCARGOS c WHERE c.idCARGOS = :p")
                .setParameter("p", tbCARGOS).executeUpdate();
    }
    
    // Remove all assignments from all tbGRADECARGOS a tbCARGOS. Called before delete a tbCARGOS.
    @Transactional
    private void cutAllIdCARGOSTbGRADECARGOSsAssignments(TbCARGOSEntity tbCARGOS) {
    	getEntityManager()
                .createQuery("DELETE FROM TbGRADECARGOS c WHERE c.idCARGOS = :p")
                .setParameter("p", tbCARGOS).executeUpdate();
    }
    
    // Remove all assignments from all tbPERFILCARGOS a tbCARGOS. Called before delete a tbCARGOS.
    @Transactional
    private void cutAllIdCARGOSTbPERFILCARGOSsAssignments(TbCARGOSEntity tbCARGOS) {
    	getEntityManager()
                .createQuery("DELETE FROM TbPERFILCARGOS c WHERE c.idCARGOS = :p")
                .setParameter("p", tbCARGOS).executeUpdate();
    }
    
    // Remove all assignments from all tbMOTIVADORESCARGOS a tbCARGOS. Called before delete a tbCARGOS.
    @Transactional
    private void cutAllIdCARGOSTbMOTIVADORESCARGOSsAssignments(TbCARGOSEntity tbCARGOS) {
 
    }
    
    @Transactional
    public List<TbCARGOSEntity> findAvailableTbCARGOSs(TbDEPTOEntity tbDEPTO) {
        return getEntityManager().createQuery("SELECT o FROM TbCARGOS o WHERE o.idDEPTO IS NULL", TbCARGOSEntity.class).getResultList();
    }

    @Transactional
    public List<TbCARGOSEntity> findTbCARGOSsByIdDEPTO(TbDEPTOEntity tbDEPTO) {
        return getEntityManager().createQuery("SELECT o FROM TbCARGOS o WHERE o.idDEPTO = :tbDEPTO", TbCARGOSEntity.class).setParameter("tbDEPTO", tbDEPTO).getResultList();
    }
    
    @Transactional
    public List<TbCARGOSEntity> findAvailableTbCARGOSs(TbNOEntity tbNO) {
        return getEntityManager().createQuery("SELECT o FROM TbCARGOS o WHERE o.idNO IS NULL", TbCARGOSEntity.class).getResultList();
    }

    @Transactional
    public List<TbCARGOSEntity> findTbCARGOSsByIdNO(TbNOEntity tbNO) {
        return getEntityManager().createQuery("SELECT o FROM TbCARGOS o WHERE o.idNO = :tbNO", TbCARGOSEntity.class).setParameter("tbNO", tbNO).getResultList();
    }

}
