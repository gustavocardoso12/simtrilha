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

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Date;
import java.util.Iterator;


@Named
@TransactionManagement(TransactionManagementType.BEAN)
public class TbCurvaService extends BaseService<TbPesquisaCurva> implements Serializable {

	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
        String inputFilePath = "C:/tmp/logins.xlsx";
        String outputFilePath = "C:/tmp/output.xlsx";

        try {
            FileInputStream file = new FileInputStream(inputFilePath);
            Workbook workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheetAt(0); // Pegue a primeira aba
            Iterator<Row> rowIterator = sheet.iterator();

            Workbook outputWorkbook = new XSSFWorkbook();
            Sheet outputSheet = outputWorkbook.createSheet("Output");

            int rowIndex = 0;
            Row headerRow = outputSheet.createRow(rowIndex++);

            // Cria o cabeçalho para o arquivo de saída
            String[] headers = {"Empresa banco de dados", "E-mail", "Usuário", "Senha Original", "Salt", "Password Hash", 
                                "CreatedAt", "Version", "Status", "Tema", "Banco de Dados", "Flag Pessoa", 
                                "Flag Grade", "Ativo", "Sistema", "Priv Acesso"};
            for (int i = 0; i < headers.length; i++) {
                headerRow.createCell(i).setCellValue(headers[i]);
            }

            // Pular a primeira linha (cabeçalho do arquivo original)
            rowIterator.next();

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                String empresaBancoDados = row.getCell(0).getStringCellValue();
                String email = row.getCell(1).getStringCellValue();
                String usuario = row.getCell(2).getStringCellValue();
                String senhaOriginal = row.getCell(3).getStringCellValue();

                // Gerar salt e hash para a senha
                String salt = SecurityWrapper.generateSalt();
                String hashedPassword = SecurityWrapper.hashPassword(senhaOriginal, salt);

                // Criar nova linha para a saída
                Row outputRow = outputSheet.createRow(rowIndex++);

                // Preencher os valores na planilha de saída
                outputRow.createCell(0).setCellValue(empresaBancoDados);
                outputRow.createCell(1).setCellValue(email);
                outputRow.createCell(2).setCellValue(usuario);
                outputRow.createCell(3).setCellValue(senhaOriginal); // Senha original
                outputRow.createCell(4).setCellValue(salt);
                outputRow.createCell(5).setCellValue(hashedPassword);
                outputRow.createCell(6).setCellValue(new Date().toString()); // CreatedAt
                outputRow.createCell(7).setCellValue(1); // Version
                outputRow.createCell(8).setCellValue("Active"); // Status
                outputRow.createCell(9).setCellValue("APTA"); // Tema
                outputRow.createCell(10).setCellValue("APTA"); // Banco de Dados
                outputRow.createCell(11).setCellValue("NAO"); // Flag Pessoa
                outputRow.createCell(12).setCellValue("NAO"); // Flag Grade
                outputRow.createCell(13).setCellValue("NAO"); // Ativo
                outputRow.createCell(14).setCellValue("Pesquisa"); // Sistema
                outputRow.createCell(15).setCellValue("Premium"); // Priv Acesso
            }

            // Gravar o arquivo de saída
            FileOutputStream outputStream = new FileOutputStream(outputFilePath);
            outputWorkbook.write(outputStream);
            outputWorkbook.close();
            file.close();
            System.out.println("Arquivo de saída gerado com sucesso: " + outputFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
