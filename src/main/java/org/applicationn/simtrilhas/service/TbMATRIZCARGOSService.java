package org.applicationn.simtrilhas.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Named;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.applicationn.simtrilhas.domain.TbCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbMATRIZCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbMATRIZCARGOSEntityTrilhas;
import org.applicationn.simtrilhas.service.security.SecurityWrapper;
import org.primefaces.model.SortOrder;

@Named
public class TbMATRIZCARGOSService extends BaseService<TbMATRIZCARGOSEntity> implements Serializable {

	private static final long serialVersionUID = 1L;

	public TbMATRIZCARGOSService(){
		super(TbMATRIZCARGOSEntity.class);
	}

	@Transactional
	public void truncate() {
		getEntityManager().createNativeQuery("TRUNCATE TABLE TB_MATRIZ_CARGOS").executeUpdate();

	}

	@Transactional
	public List<TbMATRIZCARGOSEntityTrilhas> findTrilhas(double d, TbCARGOSEntity idCargos){

		return getEntityManager().createQuery(""
				+ " SELECT o FROM TbMATRIZCARGOSTrilhas o "
				+ "	WHERE o.adERENCIAFINAL > :aderenciaMinima"
				+ " AND o.idCARGODE = :idCargo"
				+ " AND o.adERENCIAFINAL <> 100"
				+ " AND o.estaganado <> 'True'"
				+ " ORDER BY o.adERENCIAFINAL DESC" , TbMATRIZCARGOSEntityTrilhas.class)
				.setParameter("aderenciaMinima", d)
				.setParameter("idCargo", idCargos)
				.setFirstResult(0)
				.setMaxResults(6)
				.getResultList();
	}

	@Transactional
	public List<TbMATRIZCARGOSEntityTrilhas> findTrilhasSubs(double d, TbCARGOSEntity idCargos){

		return getEntityManager().createQuery(""
				+ " SELECT o FROM TbMATRIZCARGOSTrilhas o "
				+ "	WHERE o.adERENCIAFINAL > :aderenciaMinima"
				+ " AND o.idCARGODE = :idCargo"
				+ " AND o.estaganado is null"
				+ " ORDER BY o.adERENCIAFINAL DESC" , TbMATRIZCARGOSEntityTrilhas.class)
				.setParameter("aderenciaMinima", d)
				.setParameter("idCargo", idCargos)
				.setFirstResult(1)
				.setMaxResults(1)
				.getResultList();
	}

	@Transactional
	public List<TbMATRIZCARGOSEntity> findAllTbMATRIZCARGOSEntities() {

		return getEntityManager().createQuery("SELECT o FROM TbMATRIZCARGOS o ", TbMATRIZCARGOSEntity.class).getResultList();
	}

	@Transactional
	public List<Object[]> findTbMATRIZCARGOSEntities(String flag_pessoa) {
		List<Object[]> result = new ArrayList<Object[]>();


		if(!flag_pessoa.equals("SIM")) {
			result = getEntityManager().createQuery("SELECT o.corAderencia, o.adERENCIAFINAL,"
					+ "									o.idCARGODE, idCARGOPARA "
					+ " FROM TbMATRIZCARGOS AS o"
					+ "  WHERE o.flagPessoa = 'NAO'"

            		+ "	ORDER BY o.idCARGODE, o.idCARGOPARA"
            		+ "", Object[].class)
					.getResultList();
		}else {
			result = getEntityManager().createQuery("SELECT o.corAderencia, o.adERENCIAFINAL,"
					+ "									o.idCARGODE, idCARGOPARA "
					+ " FROM TbMATRIZCARGOS AS o"
					+ "	ORDER BY o.idCARGODE, o.idCARGOPARA"
					+ "", Object[].class)
					.getResultList();
		}


		return result;
	}



	@SuppressWarnings("unchecked")
	@Transactional
	public List<TbMATRIZCARGOSEntity> findTbMATRIZCARGOSEntitiesFT( String familiaDe, String familiaPara) {
		List<TbMATRIZCARGOSEntity> result = new ArrayList<TbMATRIZCARGOSEntity>();


		if(familiaDe.equals("TODOS")) {
			familiaDe = "select distinct(DESC_AREA) from TB_AREA";
		}else {
			familiaDe = "'"+familiaDe+"'";
		}

		if(familiaPara.equals("TODOS")) {
			familiaPara = "select distinct(DESC_AREA) from TB_AREA";
		}else {
			familiaPara = "'"+familiaPara+"'";
		}

		String res = "SELECT o.* " + 
				"      FROM TB_MATRIZ_CARGOS o " + 
				"      	inner loop join TBCARGOS tc " + 
				"      		on(tc.ID = o.ID_CARGODE )" + 
				"      	inner loop join TB_DEPTO td " + 
				"			on (tc.IDDEPTO_ID  = td.id )" + 
				"		inner loop join TB_AREA ta " + 
				"			on (td.IDAREA_ID = ta.id )" + 
				"		inner loop join TBCARGOS tc2 " + 
				"      		on(tc2.ID = o.ID_CARGOPARA)" + 
				"      	inner loop join TB_DEPTO td2 " + 
				"			on (tc2.IDDEPTO_ID  = td2.id )" + 
				"		inner loop join TB_AREA ta2 " + 
				"			on (td2.IDAREA_ID = ta2.id )" + 
				"       WHERE 1=1" + 
				"       and ta.DESC_AREA  in("+familiaDe+")" + 
				"       and ta2.DESC_AREA in("+familiaPara+")" + 
				" ORDER BY o.ID_CARGODE , o.ID_CARGOPARA";

		result = getEntityManager().createNativeQuery(res, TbMATRIZCARGOSEntity.class)
				.getResultList();



		return result;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public List<Object[]> findTbMATRIZCARGOSEntitiesCompleta( String familiaDe, String familiaPara) {
		List<Object[]> result = new ArrayList<Object[]>();


		if(familiaDe.equals("TODOS")) {
			familiaDe = "select distinct(DESC_AREA) from TB_AREA";
		}else {
			familiaDe = "'"+familiaDe+"'";
		}

		if(familiaPara.equals("TODOS")) {
			familiaPara = "select distinct(DESC_AREA) from TB_AREA";
		}else {
			familiaPara = "'"+familiaPara+"'";
		}

		String res=""
				+ " select cast(cargode.id as int) as id_de, cast(cargopara.id as int)  as id_para, " + 
				" cast((DENSE_RANK () OVER (  order by cargopara.id))-1 as int) AS id_matrizpara," + 
				" cast((DENSE_RANK () OVER (  order by cargode.id))-1 as int) AS id_matrizde from (" + 
				" SELECT o.id FROM TbCARGOS o" + 
				" inner loop join TB_DEPTO td " + 
				" on (o.IDDEPTO_ID  = td.id)" + 
				" inner loop join TB_AREA ta " + 
				" on (td.IDAREA_ID = ta.id )" + 
				" WHERE o.FLAG_PESSOA = 'NAO' "+
				"and ta.DESC_AREA  in("+familiaPara+")"+ 
				" ) cargopara" + 
				" cross join (" + 
				" SELECT o.id FROM TbCARGOS o" + 
				" inner loop join TB_DEPTO td " + 
				" on (o.IDDEPTO_ID  = td.id)" + 
				" inner loop join TB_AREA ta " + 
				" on (td.IDAREA_ID = ta.id )" + 
				" WHERE 1=1 "+
				"and ta.DESC_AREA  in("+familiaDe+")"+
				" ) cargode" + 
				" order by cargode.id,cargopara.id";

		result = getEntityManager().createNativeQuery(res)
				.getResultList();


		return result;
	}


	@Transactional
	public List<Long> findDistinctDe() {

		return getEntityManager().createQuery("SELECT distinct(o.id) FROM TbCARGOS o ", Long.class).getResultList();
	}

	@Transactional
	public List<Long> findDistinctPara() {

		return getEntityManager().createQuery("SELECT distinct(o.idCARGOPARA) FROM TbMATRIZCARGOS o ", Long.class).getResultList();
	}

	@Override
	@Transactional
	public long countAllEntries() {
		return getEntityManager().createQuery("SELECT COUNT(o) FROM TbMATRIZCARGOS o", Long.class).getSingleResult();
	}



	@Override
	@Transactional
	public TbMATRIZCARGOSEntity save(TbMATRIZCARGOSEntity tbMATRIZCARGOS) {
		String username = SecurityWrapper.getUsername();

		tbMATRIZCARGOS.updateAuditInformation(username);

		return super.save(tbMATRIZCARGOS);
	}



	@Transactional
	public void insertWithentityManager(TbMATRIZCARGOSEntity tbMATRIZCARGOS, int i, String username) {




		tbMATRIZCARGOS.updateAuditInformation(username);

		this.getEntityManager().persist(tbMATRIZCARGOS);


		if(i % 10000 == 0) {
			System.out.println("Flushing in batches");
			this.getEntityManager().flush();
			this.getEntityManager().clear();
			System.out.println(i);

		}

	}

	@Override
	@Transactional
	public TbMATRIZCARGOSEntity update(TbMATRIZCARGOSEntity tbMATRIZCARGOS) {
		String username = SecurityWrapper.getUsername();
		tbMATRIZCARGOS.updateAuditInformation(username);
		return super.update(tbMATRIZCARGOS);
	}

	@Override
	protected void handleDependenciesBeforeDelete(TbMATRIZCARGOSEntity tbMATRIZCARGOS) {

		/* This is called before a TbMATRIZCARGOS is deleted. Place here all the
           steps to cut dependencies to other entities */

	}

	// This is the central method called by the DataTable
	@SuppressWarnings("deprecation")
	@Override
	@Transactional
	public List<TbMATRIZCARGOSEntity> findEntriesPagedAndFilteredAndSorted(int firstResult, int maxResults, String sortField, SortOrder sortOrder, Map<String, Object> filters) {

		StringBuilder query = new StringBuilder();

		query.append("SELECT o FROM TbMATRIZCARGOS o");

		String nextConnective = " WHERE";

		Map<String, Object> queryParameters = new HashMap<>();

		if (filters != null && !filters.isEmpty()) {

			nextConnective += " ( ";

			for(String filterProperty : filters.keySet()) {

				if (filters.get(filterProperty) == null) {
					continue;
				}

				switch (filterProperty) {

				case "idCARGODE":
					query.append(nextConnective).append(" o.idCARGODE = :idCARGODE");
					queryParameters.put("idCARGODE", new Integer(filters.get(filterProperty).toString()));
					break;

				case "idCARGOPARA":
					query.append(nextConnective).append(" o.idCARGOPARA = :idCARGOPARA");
					queryParameters.put("idCARGOPARA", new Integer(filters.get(filterProperty).toString()));
					break;

				case "adERENCIAFINAL":
					query.append(nextConnective).append(" o.adERENCIAFINAL = :adERENCIAFINAL");
					queryParameters.put("adERENCIAFINAL", new Integer(filters.get(filterProperty).toString()));
					break;

				}

				nextConnective = " AND";
			}

			query.append(" ) ");
			nextConnective = " AND";
		}

		if (sortField != null && !sortField.isEmpty()) {
			query.append(" ORDER BY o.").append(sortField);
			query.append(SortOrder.DESCENDING.equals(sortOrder) ? " DESC" : " ASC");
		}

		TypedQuery<TbMATRIZCARGOSEntity> q = this.getEntityManager().createQuery(query.toString(), this.getType());

		for(String queryParameter : queryParameters.keySet()) {
			q.setParameter(queryParameter, queryParameters.get(queryParameter));
		}

		return q.setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
	}

}
