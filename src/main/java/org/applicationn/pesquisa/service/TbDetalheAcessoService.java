package org.applicationn.pesquisa.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Named;
import javax.transaction.Transactional;

import org.applicationn.pesquisa.domain.TbDetalhesAcesso;
import org.applicationn.pesquisa.vo.TbDetalhesAcessoVO;
import org.applicationn.simtrilhas.service.BaseService;

@Named
@TransactionManagement(TransactionManagementType.BEAN)
public class TbDetalheAcessoService  extends BaseService<TbDetalhesAcesso> implements Serializable{
	private static final long serialVersionUID = 1L;
	
	public TbDetalheAcessoService(){
		   super(TbDetalhesAcesso.class);
	}
	
	
	@Transactional
	public String findAcessoSimultaneo(){
		String results = (String) getEntityManagerMatriz().createNativeQuery("SELECT TX_PARAMETRO FROM TB_PARAMETRO_SISTEMA"
				+ " WHERE DS_PARAMETRO = 'LoginSimultaneo'").getSingleResult();
		
		return results;
		
	}
	
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<String> findDistinctMeses(){
		List<String> results = getEntityManagerMatriz().createNativeQuery("select distinct "
				+ " mesAno mesUnico"
				+ " from TB_DETALHE_ACESSOS tda").getResultList();
		
		return results;
		
	}
	
	@Transactional
	public int findDistinctMeses(String mesAno, String idUser){
		Object results = getEntityManagerMatriz().createNativeQuery(" select count(1) qtd from TB_DETALHE_ACESSOS\r\n"
				+ "   where id_user = :idUser \r\n"
				+ "  and tipoDeAtividade in('ACESSO DASHBOARD', 'ACESSO SIMULADOR')\r\n"
				+ "  and mesAno = :mesAno").
				setParameter("mesAno",mesAno).
				setParameter("idUser", idUser)
				.getSingleResult();
		int resultado = Integer.valueOf(results.toString());
		return resultado;
		
	}


	@SuppressWarnings("unchecked")
	@Transactional
	public List<TbDetalhesAcessoVO> findRelatorioUsuariosV2(){
		
		
		 List<Object[]> results = getEntityManagerMatriz().createNativeQuery("SELECT\r\n"
		 		+ "    u.username,\r\n"
		 		+ "    u.Sistema,\r\n"
		 		+ "    MAX(FORMAT(ISNULL(u.modifiedAt, u.createdAt), 'dd/MM/yyyy')) AS data_ultimo_acesso,\r\n"
		 		+ "    u.version AS qtd_acessos_legado,\r\n"
		 		+ "    COUNT(CASE WHEN tda.tipoDeAtividade = 'ACESSO EXCEL CARGO' THEN 1 END) QTD_EXCEL_CARGO,\r\n"
		 		+ "    COUNT(CASE WHEN tda.tipoDeAtividade = 'ACESSO EXCEL EMPRESA' THEN 1 END) QTD_EXCEL_EMPRESA,\r\n"
		 		+ "    COUNT(CASE WHEN tda.tipoDeAtividade = 'ACESSO EXCEL MERCADO' THEN 1 END) QTD_EXCEL_MERCADO\r\n"
		 		+ "FROM\r\n"
		 		+ "    TB_DETALHE_ACESSOS tda\r\n"
		 		+ "RIGHT JOIN\r\n"
		 		+ "    USERS u ON u.id = tda.id_user\r\n"
		 		+ " where u.id <> 74217 "
		 		+ "GROUP BY\r\n"
		 		+ "    u.username,\r\n"
		 		+ "    u.Sistema,\r\n"
		 		+ "    u.version"
		 		+ " ORDER BY MAX(FORMAT(u.modifiedAt, 'yyyyMMdd')) desc").getResultList();
		    
		    List<TbDetalhesAcessoVO> listaTbDetalhesAcessoVO = new ArrayList<TbDetalhesAcessoVO>();

		    for (Object[] result : results) {
		        TbDetalhesAcessoVO detalhesAcessoVO = new TbDetalhesAcessoVO();

		        detalhesAcessoVO.setUsername((String) result[0]);
		        detalhesAcessoVO.setSistema((String) result[1]);
		        detalhesAcessoVO.setDataUltimoAcesso((String) result[2]); 
		        detalhesAcessoVO.setQtdAcessosLegado((Integer) result[3]); 
		        detalhesAcessoVO.setQuantidadeAcessosCargo((int) result[4]); 
		        detalhesAcessoVO.setQuantidadeAcessosEmpresa((int) result[5]); 
		        detalhesAcessoVO.setQuantidadeAcessosMercado((int) result[6]); 

		        listaTbDetalhesAcessoVO.add(detalhesAcessoVO);
		    }
		
		
		 return listaTbDetalhesAcessoVO;
		 
		 
		 
	}
	
	@Override
	@Transactional
	public TbDetalhesAcesso save(TbDetalhesAcesso tbDetalhesAcesso) {
		return super.save(tbDetalhesAcesso);
	}
}
