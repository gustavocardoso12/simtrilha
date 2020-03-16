package org.applicationn.simtrilhas.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Named;
import javax.persistence.NoResultException;
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
    	entityManager.createNativeQuery("TRUNCATE TABLE TB_MATRIZ_CARGOS").executeUpdate();
    	
    }
    
    @Transactional
    public List<TbMATRIZCARGOSEntityTrilhas> findTrilhas(double d, TbCARGOSEntity idCargos){
    	
    	return entityManager.createQuery(""
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
    	
    	return entityManager.createQuery(""
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
        
        return entityManager.createQuery("SELECT o FROM TbMATRIZCARGOS o ", TbMATRIZCARGOSEntity.class).getResultList();
    }
    
    @Transactional
    public List<Object[]> findTbMATRIZCARGOSEntities(String flag_pessoa) {
    	List<Object[]> result = new ArrayList<Object[]>();
    	
    	
    	if(!flag_pessoa.equals("SIM")) {
    		result = entityManager.createQuery("SELECT o.corAderencia, o.adERENCIAFINAL,"
        			+ "									o.idCARGODE, idCARGOPARA "
            		+ " FROM TbMATRIZCARGOS AS o"
            		+ "  WHERE o.flagPessoa <> 'SIM'"
        			
            		+ "	ORDER BY o.idCARGODE, o.idCARGOPARA"
            		+ "", Object[].class)
            		.getResultList();
    	}else {
    		result = entityManager.createQuery("SELECT o.corAderencia, o.adERENCIAFINAL,"
        			+ "									o.idCARGODE, idCARGOPARA "
            		+ " FROM TbMATRIZCARGOS AS o"
            		+ "	ORDER BY o.idCARGODE, o.idCARGOPARA"
            		+ "", Object[].class)
            		.getResultList();
    	}
    	
    	
        return result;
    }
  
    
    @Transactional
    public List<Long> findDistinctDe() {
        
        return entityManager.createQuery("SELECT distinct(o.id) FROM TbCARGOS o ", Long.class).getResultList();
    }
    
    @Transactional
    public List<Long> findDistinctPara() {
        
        return entityManager.createQuery("SELECT distinct(o.idCARGOPARA) FROM TbMATRIZCARGOS o ", Long.class).getResultList();
    }
    
    @Override
    @Transactional
    public long countAllEntries() {
        return entityManager.createQuery("SELECT COUNT(o) FROM TbMATRIZCARGOS o", Long.class).getSingleResult();
    }
    
    
    
    @Override
    @Transactional
    public TbMATRIZCARGOSEntity save(TbMATRIZCARGOSEntity tbMATRIZCARGOS) {
        //String username = SecurityWrapper.getUsername();
        
        //tbMATRIZCARGOS.updateAuditInformation(username);
        
        return super.save(tbMATRIZCARGOS);
    }
    
    
    
    @Transactional
    public void insertWithEntityManager(TbMATRIZCARGOSEntity tbMATRIZCARGOS, int i) {
    
    
    	 
    	

        this.entityManager.persist(tbMATRIZCARGOS);
        
        if(i % 1000 == 0) {
        	System.out.println("Flushing in batches");
   	   this.entityManager.flush();
   	   this.entityManager.clear();
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
        
        TypedQuery<TbMATRIZCARGOSEntity> q = this.entityManager.createQuery(query.toString(), this.getType());
        
        for(String queryParameter : queryParameters.keySet()) {
            q.setParameter(queryParameter, queryParameters.get(queryParameter));
        }

        return q.setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
}
