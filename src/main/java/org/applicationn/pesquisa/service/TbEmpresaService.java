package org.applicationn.pesquisa.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Named;
import javax.transaction.Transactional;

import org.applicationn.pesquisa.domain.TbEmpresa;
import org.applicationn.pesquisa.domain.TbMercado;
import org.applicationn.simtrilhas.service.BaseService;
import org.applicationn.simtrilhas.service.security.SecurityWrapper;
@Named
@TransactionManagement(TransactionManagementType.BEAN)
public class TbEmpresaService extends BaseService<TbEmpresa> implements Serializable {

	private static final long serialVersionUID = 1L;


	public TbEmpresaService(){
		   super(TbEmpresa.class);
	}

	@Transactional
	public List<TbEmpresa> findAllTbEmpresa() {
			return getEntityManagerMatriz().createQuery("SELECT o FROM TbEmpresa o ",
					TbEmpresa.class).getResultList();
	}

	@Override
	@Transactional
	public long countAllEntries() {
			return getEntityManagerMatriz().createQuery("SELECT COUNT(o) FROM TbEmpresa o", Long.class).getSingleResult();
	}
	
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<String> empresasDisponiveis(String nomeEmpresa){
		
		List<String> lista = getEntityManagerMatriz().createNativeQuery("select DISTINCT tm.dsMercado from TB_EMPRESA te \n"
				+ "inner join TB_PESQUISA tp \n"
				+ "on(te.dsEmpresa = tp.nome_empresa )\n"
				+ "inner join TB_PESQUISA_MERCADO tpm \n"
				+ "on(tp.codigo_empresa  = tpm.cd_empresa )\n"
				+ "inner join TB_MERCADO tm \n"
				+ "on(tm.id = tpm.cd_mercado )\n"
				+ "where tp.nome_empresa = :nomeEmpresa\n"
				+ "AND tm.dsMercado not in ('Mercado AptaXR','Mercado Geral')").
				setParameter("nomeEmpresa", nomeEmpresa).getResultList();
		
		List<TbMercado> mercados = new ArrayList<TbMercado>();
		
		for(int i=0;i<lista.size();i++) {
			TbMercado mercado = new TbMercado();
			mercado.setDescMercado(lista.get(i));
			mercados.add(mercado);
		}
		
		return lista;
	}
	
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<String> empresasDisponiveis(){
		
		List<String> lista = getEntityManagerMatriz().createNativeQuery("select DISTINCT tm.dsMercado from TB_EMPRESA te \n"
				+ "inner join TB_PESQUISA tp \n"
				+ "on(te.dsEmpresa = tp.nome_empresa )\n"
				+ "inner join TB_PESQUISA_MERCADO tpm \n"
				+ "on(tp.codigo_empresa  = tpm.cd_empresa )\n"
				+ "inner join TB_MERCADO tm \n"
				+ "on(tm.id = tpm.cd_mercado )\n"
				+ "AND tm.dsMercado not in ('Mercado AptaXR','Mercado Geral')").getResultList();
		
	
		
		return lista;
	}
	
	@Override
	@Transactional
	public TbEmpresa save(TbEmpresa tbAREA) {
		String username = SecurityWrapper.getUsername();

		tbAREA.updateAuditInformation(username);
		return super.save(tbAREA);
	}

	@Override
	@Transactional
	public TbEmpresa update(TbEmpresa tbAREA) {
		String username = SecurityWrapper.getUsername();
		tbAREA.updateAuditInformation(username);
		return super.update(tbAREA);
	}

	@Override
	protected void handleDependenciesBeforeDelete(TbEmpresa tbAREA) {

		/* This is called before a TbAREA is deleted. Place here all the
           steps to cut dependencies to other entities */
		//this.cutAllIdAREATbCARGOAssignments(tbAREA);

		//this.cutAllIdAREATbDEPTOsAssignments(tbAREA);

	}
	


	/*// Remove all assignments from all tbDEPTO a tbAREA. Called before delete a tbAREA.
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

	}*/







}
