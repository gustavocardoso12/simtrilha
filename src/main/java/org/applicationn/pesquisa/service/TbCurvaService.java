package org.applicationn.pesquisa.service;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Named;
import javax.transaction.Transactional;

import org.apache.commons.math3.stat.regression.MultipleLinearRegression;
import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.applicationn.pesquisa.domain.TbEmpresa;
import org.applicationn.pesquisa.domain.TbMercado;
import org.applicationn.pesquisa.domain.TbPesquisaCurva;
import org.applicationn.pesquisa.vo.CurvaVO;
import org.applicationn.pesquisa.vo.MediasVO;
import org.applicationn.simtrilhas.service.BaseService;
import org.applicationn.simtrilhas.service.security.SecurityWrapper;
@Named
@TransactionManagement(TransactionManagementType.BEAN)
public class TbCurvaService extends BaseService<TbPesquisaCurva> implements Serializable {

	private static final long serialVersionUID = 1L;

	public static void main(String[] args) throws NoSuchAlgorithmException {
       

		 String plainText = "936e7899d6d4aab6d56a0d2601f1d3e5068755fd61eba55de0477377c8710565ccedb36e02735acaecfb89514cc9b352ec2ec41f5443305fe355b7fcff83eb4f" 
					+ "7upjj6c8h5djhvflvpro0jhq1sk05ebf86c64gcevckh415unk";
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
        byte[] hash = messageDigest.digest( plainText.getBytes() );
    }

	public TbCurvaService(){
		   super(TbPesquisaCurva.class);
	}

	@Transactional
	public List<CurvaVO> findCurvaMercados(String descMercado) {
		

		List<CurvaVO> curvaLista = new ArrayList<CurvaVO>();
		
		
		@SuppressWarnings("unchecked")
		List<Object> lista =  getEntityManagerMatriz().
				createNativeQuery("  SELECT tp.nome_empresa AS 'Nome empresa', \n"
						+ "       tp.grade AS 'Grade', \n"
						+ "       COUNT(tp.nome_cargo) AS 'Quantidade de cargos', \n"
						+ "       CAST(ROUND(AVG(tp.media_sb) / 13.33, 0) AS INT) AS 'Média salário base mensal',\n"
						+ "       CAST(ROUND(AVG(tp.media_tb ), 0) AS INT) AS 'Média Total em Dinheiro',\n"
						+ "       CAST(ROUND(AVG(tp.media_tda ), 0) AS INT) AS 'Média Total em Dinheiro Alvo',\n"
						+ "       CAST(ROUND(AVG(tp.media_rd ), 0) AS INT) AS 'Média Renumeração direta',\n"
						+ "       CAST(ROUND(AVG(tp.media_rda ), 0) AS INT) AS 'Média Renumeração direta Alvo'\n"
						+ "FROM tb_pesquisa tp \n"
						+ "INNER JOIN TB_PESQUISA_MERCADO tpm ON tp.codigo_empresa = tpm.cd_empresa \n"
						+ "INNER JOIN tb_mercado tm ON tpm.cd_mercado = tm.id \n"
						+ "WHERE tm.dsMercado = :nomeMercado          \n"
						+ "and tp.media_tb is not null \n"
						+ "and tp.media_tda is not null\n"
						+ "GROUP BY tp.nome_empresa, tp.grade \n"
						+ "ORDER BY tp.nome_empresa ,tp.grade DESC").setParameter("nomeMercado",descMercado)
				.getResultList();
			
			
			
			for(Object record: lista) {
				Object[] linha = (Object[]) record; 
				CurvaVO curva = new CurvaVO();
				curva.setEmpresa((String) linha[0]);
				curva.setGrade((int) linha[1]);
				curva.setQuantidade_cargos((int) linha[2]);
				curva.setMedia_sbm((int)linha[3]);
				curva.setMedia_td((int)linha[4]);
				curva.setMedia_tda((int) linha[5]);
				
				curvaLista.add(curva);
			}
			
			
			 // Criação do objeto de regressão linear múltipla
			OLSMultipleLinearRegression regressao = new OLSMultipleLinearRegression();
				


	        // Matriz de variáveis explicativas (grade e media sbm)
	        double[][] X = new double[curvaLista.size()][3];
	        
	        for (int i = 0; i < curvaLista.size(); i++) {
	        	CurvaVO cliente = curvaLista.get(i);
	            X[i][0] = cliente.getGrade();
	            X[i][1] = cliente.getMedia_sbm();
	            X[i][2] = cliente.getQuantidade_cargos();
	        }

	        // Vetor de variável resposta (salário)
	        double[] y = new double[curvaLista.size()];
	        for (int i = 0; i < curvaLista.size(); i++) {
	        	CurvaVO cliente = curvaLista.get(i);
	            y[i] = cliente.getMedia_td();
	        }
	        
	        // Adiciona os pontos na regressão
	        regressao.newSampleData(y, X);

	        // Realiza a regressão e obtém os coeficientes
	        double[] coeficientes = regressao.estimateRegressionParameters();
	        double intercepto = coeficientes[0];
	        double coeficienteGrade = coeficientes[1];
	        double coeficienteMediaSbm = coeficientes[2];
	        double coeficientequantidadeCargos= coeficientes[3];
			
	        // Faz a predição  do RD
	        double gradePredicao = 19;
	        double SbmPredicao = 4151;
	        double quantidadeCargos = 2;
	        double salarioPredicao = intercepto + 
	        		coeficienteGrade * gradePredicao + 
	        		coeficienteMediaSbm * SbmPredicao + 
	        		coeficientequantidadeCargos * quantidadeCargos;

	        //formula = 899122 + (-52582*grade) + (32.35 * sbm) + (4082.59*quantidadeCargos)
	        System.out.println(salarioPredicao);
	        
	        // Calcula o coeficiente de determinação (R²)
	        double rSquared = regressao.calculateRSquared();
	        System.out.println("Coeficiente de determinação (R²): " + rSquared);

	        
			return null;
	}



}
