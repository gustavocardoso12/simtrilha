package org.applicationn.simtrilhas.service;

import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.applicationn.simtrilhas.domain.BaseEntity;
import org.applicationn.simtrilhas.domain.security.UserEntity;
import org.applicationn.simtrilhas.service.security.SecurityWrapper;
import org.primefaces.model.SortOrder;

public class BaseService<T extends BaseEntity> {

	@PersistenceContext(unitName = "Moura")
	private  EntityManager entityManager;

	@PersistenceContext(unitName = "Cocamar")
	private EntityManager entityManagerCocamar;

	@PersistenceContext(unitName = "Moura")
	private EntityManager entityManagerBRB;

	@PersistenceContext(unitName = "Moura")
	private EntityManager entityManagerCIEE;
	
	@PersistenceContext(unitName = "Moura")
	private EntityManager entityManagerHP;


	@Transactional
	public UserEntity findUserByUsername(String username) {
		UserEntity user;
		try {
			user =getEntityManagerMatriz().createQuery("SELECT o FROM User o WHERE o.username = :p", UserEntity.class)
					.setParameter("p", username).getSingleResult();
		} catch (NoResultException e) {

			return null;
		}
		return user;
	}

	public BaseService() {

	}
	/*
	 * Obtenção do banco de dados que será utilizado para a operação. 
	 * Se o username for null, o que é comum nas consultas iniciais será usado o banco padrão
	 * Considerado o "Matriz"
	 * Caso haja a indicação da tabela de usuários, será usado o banco de dados definido. 
	 * @return EntityManager
	 */
	public EntityManager getEntityManager() {

		try{
			String username = SecurityWrapper.getUsername();
			UserEntity user = findUserByUsername(username);
			if(username==null) {
				return entityManager;
			}else {
				switch(user.getBanco_dados()) {
				case "Moura":
					return entityManager;
				case "Cocamar":
					return entityManagerCocamar;
				case "BRB":
					return entityManagerBRB;
				case "CIEE":
					return entityManagerCIEE;
				case "HP":
					return entityManagerHP;
				default:
					return entityManager;
				}
			} 
		}catch (Exception ex) {

		}

		return entityManager;
	}

	public EntityManager getEntityManagerMatriz() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}


	private Class<T> type;

	public BaseService(Class<T> type) {
		this.type = type;
	}

	public Class<T> getType() {
		return type;
	}

	@Transactional
	public T save(T entity) {
		this.getEntityManager() .persist(entity);
		this.getEntityManager() .flush();
		this.getEntityManager() .refresh(entity);
		return entity;
	}

	@Transactional
	public T update(T entity) {
		return this.getEntityManager() .merge(entity);
	}
	
	@Transactional
	public T updateMatriz(T entity) {
		return this.getEntityManagerMatriz() .merge(entity);
	}

	@Transactional
	public T find(Long id) {
		if (id == null) return null;
		return this.getEntityManager() .find(this.type, id);
	}
	
	@Transactional
	public T findMatriz(Long id) {
		if (id == null) return null;
		return this.getEntityManagerMatriz() .find(this.type, id);
	}


	@Transactional
	public T find(Class<T> type, Object id) {
		if (id == null) return null;
		return this.getEntityManager() .find(type, id);
	}

	@Transactional
	public void delete(T entity) {

		handleDependenciesBeforeDelete(entity);

		if (this.getEntityManager() .contains(entity)) {
			this.getEntityManager() .remove(entity);
		} else {
			T attached = find(entity.getId());
			this.getEntityManager() .remove(attached);
		}

		this.getEntityManager() .flush();
	}

	/**
	 * This method is called to handle dependend entities before delete an entity
	 * @param entity
	 */
	protected void handleDependenciesBeforeDelete(T entity) {

	}

	// This is the central method called by the DataTable
	public List<T> findEntriesPagedAndFilteredAndSorted(int firstResult, int maxResults, String sortField, SortOrder sortOrder, Map<String, Object> columnFilters) throws RuntimeException {
		// overwrite this method in extending class
		return null;
	}

	public long countAllEntries() {
		// overwrite this method in extending class
	return 0;
	}

}
