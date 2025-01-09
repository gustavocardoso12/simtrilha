package org.applicationn.pesquisa.service;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Named;
import javax.transaction.Transactional;

import org.applicationn.pesquisa.domain.TbUsuarioMercado;
import org.applicationn.simtrilhas.service.BaseService;

@Named
@TransactionManagement(TransactionManagementType.BEAN)
public class TbUsuarioPesquisaService  extends BaseService<TbUsuarioMercado> implements Serializable {
	private static final long serialVersionUID = 1L;
	public TbUsuarioPesquisaService(){
		super(TbUsuarioMercado.class);
	}
	
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<String> findMercadosDisponiveis(Long idUser) {
	    List<Object> resultados = getEntityManagerMatriz()
	        .createNativeQuery(
	            "SELECT tm.dsMercado " +
	            "FROM tb_usuario_mercado tum " +
	            "INNER JOIN users u ON u.id_empresa = tum.cd_usuario " +
	            "INNER JOIN tb_mercado tm ON tm.id = tum.cd_mercado " +
	            "WHERE u.id = :idUser")
	        .setParameter("idUser", idUser)
	        .getResultList();

	    return resultados.stream()
	                     .map(Object::toString)
	                     .collect(Collectors.toList());
	}
	
}
