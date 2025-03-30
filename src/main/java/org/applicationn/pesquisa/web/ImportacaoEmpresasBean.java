package org.applicationn.pesquisa.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.Part;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import org.applicationn.pesquisa.vo.ProgressoImportacao;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.apache.connector.ApacheConnectorProvider;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import javax.ws.rs.core.GenericEntity;
import java.net.URI;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.Duration;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
@Named("importacaoEmpresasBean")
@ViewScoped
public class ImportacaoEmpresasBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final String ENDPOINT_URL = "https://simuladorapta.brazilsouth.cloudapp.azure.com/integracao/empresa/import";
  
    private static final List<String> ABAS_ESPERADAS = Arrays.asList(
            "Empresas", "Mercados", "Detalhes Pesquisa");
    
    private static final Map<String, List<String>> COLUNAS_ESPERADAS = new HashMap<>();
    static {
        COLUNAS_ESPERADAS.put("Empresas", Arrays.asList(
                "cvCodPes", "ccFamilia", "ccSubFam", "cvSeqCar", "ccCodCar", 
                "ccNomCar", "cvLevel", "cvCodEmp", "ccNomEmp", "cvFreqFun", 
                "MedSB", "MedTD", "MedTDA", "MedRD", "MedRDA", "MedICP_ALV", 
                "MedICP_PG", "MedILP"));
        
        COLUNAS_ESPERADAS.put("Mercados", Arrays.asList(
                "EMPRESAS MERCADO SELECIONADO"));
        
        COLUNAS_ESPERADAS.put("Detalhes Pesquisa", Arrays.asList(
                "ccTipLin", "cvCodPes", "cvCodEmp", "cvLinArq", "ccAbvPes", 
                "ccNomEmp", "ccNomArq", "LinArq", "ccMatFun", "cvIdade", 
                "ccGenero", "cdDatAdm", "ccTipContr", "ccMudCar", "ccNomCar", 
                "ccNivHie", "ccDepto", "ccMatSup", "ccCrgHor", "ccCidade", 
                "ccEstado", "ccCEP", "ccCodCar", "CarXr", "ccTipMtdEmp", 
                "cvPtoMed", "ccGrdEmp", "cvLevel", "cvNumSalAno", "cvSalMes", 
                "cvAdicMesTemp", "cvAdicMesPeric", "cvAdicMesInsab", "cvAdicMesOut", 
                "ccTipAdicMesOut", "cvPtoMedFxSal", "cvEleBon", "cvElePlr", 
                "cvBonSalAlv", "cvBonVlrAlv", "cvPlrSalAlv", "cvPlrVlrAlv", 
                "cvBonSalPgto", "cvBonVlrPgto", "cvPlrSalPgto", "cvPlrVlrPgto", 
                "cvEleIlp", "ccPlan01Tip", "ccPlan01Crit", "cvPlan01Freq", 
                "cdPlan01Dat", "ccPlan01Und", "cvPlan01Vlr", "cvPlan01Prec", 
                "ccPlan02Tip", "ccPlan02Crit", "cvPlan02Freq", "cdPlan02Dat", 
                "ccPlan02Und", "cvPlan02Vlr", "cvPlan02Prec", "ccPlan03Tip", 
                "ccPlan03Crit", "cvPlan03Freq", "cdPlan03Dat", "ccPlan03Und", 
                "cvPlan03Vlr", "cvPlan03Prec", "cvSalTab", "cvSalPgto", 
                "cvRemIcpAlv", "cvRemIcpPgto", "cvRemIlpVlr", "cvRemTda", 
                "cvRemTd", "cvRemRda", "cvRemRd", "cvPlan01Vest", "cvPlan02Vest", 
                "cvPlan03Vest"));
    }
    
    private Part arquivo;
    private String mercadoPrincipal;
    private boolean carregando;
    private String resultadoImportacao;
    private String statusImportacao;
    private StringBuilder errosValidacao;
    
    private boolean checandoImportacao;
    
    private boolean importarEmpresaAPTAXR = true;
    
    private final AtomicBoolean enviandoArquivo = new AtomicBoolean(false);
    
    private ProgressoImportacao progressoImport;
    
    private final AtomicBoolean monitoramentoAtivo = new AtomicBoolean(false);
    
    // Serviços para processamento assíncrono
    private transient ExecutorService executorService;
    private transient ScheduledExecutorService scheduledExecutorService;
    
    @PostConstruct
    public void init() {
        executorService = Executors.newSingleThreadExecutor();
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    }
    
    @PreDestroy
    public void destroy() {
        if (executorService != null) {
            executorService.shutdownNow();
        }
        if (scheduledExecutorService != null) {
            scheduledExecutorService.shutdownNow();
        }
    }

    public String enviarArquivo() {
        if (!enviandoArquivo.compareAndSet(false, true)) {
            adicionarMensagemAviso("Processamento em andamento. Aguarde a conclusão da operação atual.");
            return null;
        }
        checandoImportacao = true;
        carregando = true;
        resultadoImportacao = null;
        statusImportacao = null;
        errosValidacao = new StringBuilder();
        
        try {
            if (arquivo == null || arquivo.getSize() == 0) {
                adicionarMensagemErro("Por favor, selecione um arquivo para upload.");
                return null;
            }
            
            String nomeArquivo = arquivo.getSubmittedFileName().toLowerCase();
            if (!nomeArquivo.endsWith(".xls") && !nomeArquivo.endsWith(".xlsx")) {
                adicionarMensagemErro("Por favor, selecione apenas arquivos Excel (.xls ou .xlsx)");
                return null;
            }
            
            if (!importarEmpresaAPTAXR) {
                if (mercadoPrincipal == null || mercadoPrincipal.trim().isEmpty()) {
                    adicionarMensagemErro("Por favor, informe o mercado selecionado.");
                    return null;
                }
            }
            
            if (!validarEstruturaPlanilha()) {
                adicionarMensagemErro("Estrutura da planilha inválida. " + errosValidacao.toString());
                return null;
            }
            
            enviarArquivoParaAPI();
            
            
            
            
        } catch (Exception e) {
            statusImportacao = "error";
            resultadoImportacao = "Erro ao processar importação: " + e.getMessage();
            e.printStackTrace();
            carregando = false;
            enviandoArquivo.set(false);
            checandoImportacao = false;
        }
        System.gc();
        return null;
    }
    
    private boolean validarEstruturaPlanilha() {
        try (InputStream is = arquivo.getInputStream();
             Workbook workbook = WorkbookFactory.create(is)) {
            
            int numSheets = workbook.getNumberOfSheets();
            if (numSheets != ABAS_ESPERADAS.size()) {
                errosValidacao.append("Número de abas incorreto. Esperado: ")
                        .append(ABAS_ESPERADAS.size())
                        .append(", encontrado: ")
                        .append(numSheets)
                        .append(". ");
                return false;
            }
            
            for (int i = 0; i < ABAS_ESPERADAS.size(); i++) {
                String abaEsperada = ABAS_ESPERADAS.get(i);
                String abaEncontrada = workbook.getSheetName(i);
                
                if (!abaEsperada.equals(abaEncontrada)) {
                    errosValidacao.append("Nome da aba na posição ")
                            .append(i + 1)
                            .append(" incorreto. Esperado: '")
                            .append(abaEsperada)
                            .append("', encontrado: '")
                            .append(abaEncontrada)
                            .append("'. ");
                    return false;
                }
                
                Sheet sheet = workbook.getSheetAt(i);
                Row headerRow = sheet.getRow(0);
                
                if (headerRow == null) {
                    errosValidacao.append("Aba '")
                            .append(abaEsperada)
                            .append("' não possui linha de cabeçalho. ");
                    return false;
                }
                
                List<String> colunasEsperadas = COLUNAS_ESPERADAS.get(abaEsperada);
                if (headerRow.getLastCellNum() < colunasEsperadas.size()) {
                    errosValidacao.append("Número insuficiente de colunas na aba '")
                            .append(abaEsperada)
                            .append("'. Esperado: ")
                            .append(colunasEsperadas.size())
                            .append(", encontrado: ")
                            .append(headerRow.getLastCellNum())
                            .append(". ");
                    return false;
                }
                
                for (int j = 0; j < colunasEsperadas.size(); j++) {
                    String colunaEsperada = colunasEsperadas.get(j);
                    String colunaEncontrada = headerRow.getCell(j) != null 
                            ? headerRow.getCell(j).getStringCellValue() 
                            : null;
                    
                    if (colunaEncontrada == null || !colunaEsperada.equals(colunaEncontrada)) {
                        errosValidacao.append("Coluna na posição ")
                                .append(j + 1)
                                .append(" da aba '")
                                .append(abaEsperada)
                                .append("' incorreta. Esperado: '")
                                .append(colunaEsperada)
                                .append("', encontrado: '")
                                .append(colunaEncontrada)
                                .append("'. ");
                        return false;
                    }
                }
            }
            
            return true;
        } catch (Exception e) {
            errosValidacao.append("Erro ao validar estrutura da planilha: ")
                    .append(e.getMessage());
            return false;
        }
    }
    
   
    
    
    private String enviarArquivoParaAPI() throws Exception {
        // Cria um arquivo temporário a partir do arquivo enviado (javax.servlet.http.Part)
        File tempFile = File.createTempFile("upload", arquivo.getSubmittedFileName());
        try (InputStream input = arquivo.getInputStream();
             OutputStream out = new FileOutputStream(tempFile)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = input.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
        
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            // Configura timeouts: 10s para conexão e 1h para resposta (socket timeout)
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(10 * 1000)   // 10 segundos de timeout para conexão
                    .setSocketTimeout(60 * 60 * 1000) // 1 hora de timeout para a leitura dos dados
                    .build();
            
            HttpPost httpPost = new HttpPost(ENDPOINT_URL);
            httpPost.setConfig(requestConfig);
            
            // Monta a requisição multipart/form-data
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.addTextBody("dsMercadoPrincipal", mercadoPrincipal != null ? mercadoPrincipal : "Mercado AptaXR");
            builder.addTextBody("importarEmpresaAPTAXR", String.valueOf(importarEmpresaAPTAXR));
            builder.addBinaryBody("file", tempFile, org.apache.http.entity.ContentType.APPLICATION_OCTET_STREAM, arquivo.getSubmittedFileName());
            
            HttpEntity multipart = builder.build();
            httpPost.setEntity(multipart);
            
            // Executa a requisição e obtém a resposta
            HttpResponse response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            String responseBody = EntityUtils.toString(response.getEntity());
            System.out.println(statusCode);
            if (statusCode == 200) {
                return responseBody;
            } else {
                return "Erro do servidor: " + statusCode + " - " + responseBody;
            }
        } finally {
            if (tempFile.exists()) {
                tempFile.delete();
            }
        }
    }
    
    private void adicionarMensagemSucesso(String mensagem) {
        
    }
    
    private void adicionarMensagemErro(String mensagem) {

    }
    
    private void adicionarMensagemAviso(String mensagem) {
        
    }
    
    public boolean isEnviandoArquivo() {
        return enviandoArquivo.get();
    }
    
    public boolean isMonitoramentoAtivo() {
        return monitoramentoAtivo.get();
    }

    // Getters e Setters
    public Part getArquivo() {
        return arquivo;
    }

    public void setArquivo(Part arquivo) {
        this.arquivo = arquivo;
    }

    public String getMercadoPrincipal() {
        return mercadoPrincipal;
    }

    public void setMercadoPrincipal(String mercadoPrincipal) {
        this.mercadoPrincipal = mercadoPrincipal;
    }
    
    public boolean isCarregando() {
        return carregando;
    }
    
    public void setCarregando(boolean carregando) {
        this.carregando = carregando;
    }
    
    public String getResultadoImportacao() {
        return resultadoImportacao;
    }
    
    public void setResultadoImportacao(String resultadoImportacao) {
        this.resultadoImportacao = resultadoImportacao;
    }
    
    public String getStatusImportacao() {
        return statusImportacao;
    }
    
    public void setStatusImportacao(String statusImportacao) {
        this.statusImportacao = statusImportacao;
    }
    
    public boolean isImportarEmpresaAPTAXR() {
        return importarEmpresaAPTAXR;
    }
    
    public void setImportarEmpresaAPTAXR(boolean importarEmpresaAPTAXR) {
        this.importarEmpresaAPTAXR = importarEmpresaAPTAXR;
    }
    
    public ProgressoImportacao getProgressoImport() {
        return progressoImport;
    }
    
    public void setProgressoImport(ProgressoImportacao progressoImport) {
        this.progressoImport = progressoImport;
    }

    public boolean isChecandoImportacao() {
        return checandoImportacao;
    }

    public void setChecandoImportacao(boolean checandoImportacao) {
        this.checandoImportacao = checandoImportacao;
    }
}
