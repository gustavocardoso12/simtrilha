package org.applicationn.simtrilhas.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import org.applicationn.simtrilhas.domain.TbAREAEntity;
import org.applicationn.simtrilhas.domain.TbCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbDEPTOEntity;
import org.applicationn.simtrilhas.domain.security.UserEntity;
import org.applicationn.simtrilhas.service.security.SecurityWrapper;
import org.applicationn.simtrilhas.service.security.UserService;

@Named
@TransactionManagement(TransactionManagementType.BEAN)
public class TbAREAService extends BaseService<TbAREAEntity> implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private TbDEPTOService tbDEPTOService;
	

	

	private UserEntity user;

	public TbAREAService(){
		   super(TbAREAEntity.class);
	}

	@Transactional
	public List<TbAREAEntity> findAllTbAREAEntities() {
			return getEntityManager().createQuery("SELECT o FROM TbAREA o ", TbAREAEntity.class).getResultList();

	}

	@Override
	@Transactional
	public long countAllEntries() {
			return getEntityManager().createQuery("SELECT COUNT(o) FROM TbAREA o", Long.class).getSingleResult();
	}

	@Transactional
	public List<String> findDistinctTbAREAEntitiesPara() {

		
		return getEntityManager().createQuery(""
				+ "SELECT o.deSCAREA FROM TbAREA o "
				+ "		WHERE o.deSCAREA <> 'PESSOAS' "
				+ "		ORDER BY o.id", String.class).getResultList();
	
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public List<String> findDistinctTbAREAEntitiesDe(String flag_pessoa) {

		List<String> result = new ArrayList<>();
		result =  getEntityManager().createNativeQuery(
				" select distinct(o.DESC_AREA) from TB_AREA o "+
						" inner join TB_DEPTO tb " + 
						" on (o.id  = tb.IDAREA_ID ) " + 
						" inner join TBCARGOS tc " + 
						" on (tb.id  = tc.IDDEPTO_ID ) " + 
						" where tc.FLAG_PESSOA  = 'NAO' "
						+ " ORDER BY o.DESC_AREA").getResultList();
		return result;

	}

	@SuppressWarnings("unchecked")
	@Transactional
	public List<String> findDistinctTbAREAATEntitiesDe() {
		List<String> result = new ArrayList<>();
		
		
		result = getEntityManager().createNativeQuery(
				" select distinct(o.DESC_AREA) from TB_AREA o "+
						" inner join TB_DEPTO tb " + 
						" on (o.id  = tb.IDAREA_ID ) " + 
						" inner join TBCARGOS tc " + 
						" on (tb.id  = tc.IDDEPTO_ID ) " + 
						" where tc.FLAG_PESSOA  = 'SIM' "
						+ " ORDER BY o.DESC_AREA")
				.getResultList();
		
		return result;

	}

	@Transactional
	public List<String> findDistinctTbDEPTOEntities(String Area) {

		return getEntityManager().createQuery("SELECT DISTINCT(o.deSCDEPTO) FROM TbDEPTO o WHERE o.idAREA.deSCAREA =:deSCAREA ", String.class).setParameter("deSCAREA",Area).getResultList();
		}

	@Transactional
	public List<TbCARGOSEntity> findDistinctTbCARGOSEntities(String Depto, String Area, String flagPessoa) {

		return getEntityManager().createQuery("SELECT o FROM TbCARGOS o WHERE "
				+ " o.flagPessoa=:flagPessoa "
				+ "and  o.idDEPTO.deSCDEPTO =:deSCDEPTO "
				+ "and o.idDEPTO.idAREA.deSCAREA =:deSCAREA", TbCARGOSEntity.class)
				.setParameter("deSCDEPTO",Depto)
				.setParameter("deSCAREA", Area)
				.setParameter("flagPessoa", flagPessoa)
				.getResultList();
		
	}





	@Override
	@Transactional
	public TbAREAEntity save(TbAREAEntity tbAREA) {
		String username = SecurityWrapper.getUsername();

		tbAREA.updateAuditInformation(username);
		return super.save(tbAREA);
	}



	@Override
	@Transactional
	public TbAREAEntity update(TbAREAEntity tbAREA) {
		String username = SecurityWrapper.getUsername();
		tbAREA.updateAuditInformation(username);
		return super.update(tbAREA);
	}



	@Override
	protected void handleDependenciesBeforeDelete(TbAREAEntity tbAREA) {

		/* This is called before a TbAREA is deleted. Place here all the
           steps to cut dependencies to other entities */
		this.cutAllIdAREATbCARGOAssignments(tbAREA);

		this.cutAllIdAREATbDEPTOsAssignments(tbAREA);

	}
	


	// Remove all assignments from all tbDEPTO a tbAREA. Called before delete a tbAREA.
	@Transactional
	private void cutAllIdAREATbDEPTOsAssignments(TbAREAEntity tbAREA) {
		getEntityManager()
		.createQuery("DELETE FROM TbDEPTO c WHERE c.idAREA = :p")
		.setParameter("p", tbAREA).executeUpdate();
	}

	@Transactional
	private void cutAllIdAREATbCARGOAssignments(TbAREAEntity tbAREA) {
		List<TbDEPTOEntity> result = new ArrayList<TbDEPTOEntity>();
		result = tbDEPTOService.findTbDEPTOsByIdAREA(tbAREA);
		for(int i=0; i<result.size();i++) {
			getEntityManager()
			.createQuery("DELETE FROM TbCARGOS c WHERE c.idDEPTO = :p")
			.setParameter("p", result.get(i)).executeUpdate();
		}

	}







}
