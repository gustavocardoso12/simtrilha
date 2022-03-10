package org.applicationn.simtrilhas.web;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.PostActivate;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;
import org.applicationn.simtrilhas.domain.TbCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbCOMPETENCIASCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbCOMPETENCIASEntity;
import org.applicationn.simtrilhas.domain.TbCONHECIMENTOSBASCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbCONHECIMENTOSBASICOSEntity;
import org.applicationn.simtrilhas.domain.TbCONHECIMENTOSESPCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbCONHECIMENTOSESPECIFICOSEntity;
import org.applicationn.simtrilhas.domain.TbGRADECARGOSEntity;
import org.applicationn.simtrilhas.domain.TbGRADEEntity;
import org.applicationn.simtrilhas.domain.TbMATRIZCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbPERFILCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbPERFILEntity;
import org.applicationn.simtrilhas.service.AlteracoesService;
import org.applicationn.simtrilhas.service.TbCARGOSService;
import org.applicationn.simtrilhas.service.TbCOMPETENCIASService;
import org.applicationn.simtrilhas.service.TbCONHECIMENTOSBASICOSService;
import org.applicationn.simtrilhas.service.TbCONHECIMENTOSESPECIFICOSService;
import org.applicationn.simtrilhas.service.TbGRADEService;
import org.applicationn.simtrilhas.service.TbMATRIZCARGOSService;
import org.applicationn.simtrilhas.service.TbPERFILService;
import org.applicationn.simtrilhas.service.security.SecurityWrapper;
import org.applicationn.simtrilhas.web.generic.GenericLazyDataModel;
import org.applicationn.simtrilhas.web.util.MessageFactory;
import org.primefaces.PrimeFaces;
import org.primefaces.extensions.component.gchart.GChart;


@Named("tbMATRIZCARGOSBean")
@ViewScoped
public class TbMATRIZCARGOSBean extends TbCARGOSBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = Logger.getLogger(TbMATRIZCARGOSBean.class.getName());

	private GenericLazyDataModel<TbMATRIZCARGOSEntity> lazyModel;

	private TbMATRIZCARGOSEntity tbMATRIZCARGOS;

	@Inject
	private TbMATRIZCARGOSService tbMATRIZCARGOSService;

	@Inject
	private TbCARGOSService tbCARGOSService;

	@Inject
	private TbCONHECIMENTOSBASICOSService tbCONHECIMENTOSBASICOSService;

	@Inject
	private TbCONHECIMENTOSESPECIFICOSService tbCONHECIMENTOSESPECIFICOSService;

	@Inject
	private TbCOMPETENCIASService tbCOMPETENCIASService;

	@Inject
	private TbGRADEService tbGRADEService;

	@Inject
	private TbPERFILService tbPERFILService; 

	@Inject
	private AlteracoesService alteracoesService;

	private Long countDe;

	private List<Long> listaPara = new ArrayList<Long>();

	List<TbCONHECIMENTOSBASCARGOSEntity> listConhecimentosBasicos = new ArrayList<TbCONHECIMENTOSBASCARGOSEntity>();

	List<TbCONHECIMENTOSESPCARGOSEntity> listConhecimentosEspecificos = new ArrayList<TbCONHECIMENTOSESPCARGOSEntity>();

	List<TbCOMPETENCIASCARGOSEntity> listCompetencia = new ArrayList<TbCOMPETENCIASCARGOSEntity>();

	List<TbGRADECARGOSEntity> listGrade = new ArrayList<TbGRADECARGOSEntity>();

	List<TbPERFILCARGOSEntity> listPerfil = new ArrayList<TbPERFILCARGOSEntity>();

	@SuppressWarnings("unused")
	private List<TbMATRIZCARGOSEntity> MatrizPrincipal = new ArrayList<TbMATRIZCARGOSEntity>();

	private List<TbCARGOSEntity> listaCargos = new ArrayList<TbCARGOSEntity>();

	private List<TbCARGOSEntity> listaCargosPara = new ArrayList<TbCARGOSEntity>();

	private List<TbCARGOSEntity> listaCargosSPessoas = new ArrayList<TbCARGOSEntity>();

	private List<TbCONHECIMENTOSBASICOSEntity> listCB = new ArrayList<TbCONHECIMENTOSBASICOSEntity>();

	List<TbCONHECIMENTOSESPECIFICOSEntity> listCE = new ArrayList<TbCONHECIMENTOSESPECIFICOSEntity>();

	List<TbCOMPETENCIASEntity> listCO = new ArrayList<TbCOMPETENCIASEntity>();

	List<TbPERFILEntity> listPE = new ArrayList<TbPERFILEntity>();

	List<TbGRADEEntity> listGR = new ArrayList<TbGRADEEntity>();

	List<TbMATRIZCARGOSEntity> matrizCargos = new ArrayList<TbMATRIZCARGOSEntity>();

	List<TbMATRIZCARGOSEntity> matrizListaInsercao = new ArrayList<TbMATRIZCARGOSEntity>();

	List<TbCONHECIMENTOSESPCARGOSEntity> listCECARGOSDe = new ArrayList<TbCONHECIMENTOSESPCARGOSEntity>();

	List<TbCONHECIMENTOSBASCARGOSEntity> listCBCARGOSDe = new ArrayList<TbCONHECIMENTOSBASCARGOSEntity>();

	List<TbCOMPETENCIASCARGOSEntity> listCOCARGOSDe = new ArrayList<TbCOMPETENCIASCARGOSEntity>();

	List<TbPERFILCARGOSEntity> listPECARGOSDe = new ArrayList<TbPERFILCARGOSEntity>();

	List<TbGRADECARGOSEntity> listGRCARGOSDe = new ArrayList<TbGRADECARGOSEntity>();

	boolean oldposicao =false;

	boolean oldsposicaoCB = false;

	boolean oldposicaoPE = false;

	boolean oldposicaoCO = false; 

	boolean oldposicaoGR = false;

	List<ArrayList<ArrayList<TbMATRIZCARGOSEntity>>> arrayCompleta = new ArrayList<ArrayList<ArrayList<TbMATRIZCARGOSEntity>>>();

	int id_fde =0;
	int id_fpara = 0;

	private ArrayList<ArrayList<ArrayList<TbMATRIZCARGOSEntity>>> data3D = new ArrayList<ArrayList<ArrayList<TbMATRIZCARGOSEntity>>>();

	private List<TbMATRIZCARGOSEntity> resultMatriz = new ArrayList<TbMATRIZCARGOSEntity>();

	private String ajaxStatus="Exportando Matriz... O processo leva de 2 a 3 minutos";

	private String porcentagemFinal = "";

	private String statusMatriz ="";

	private boolean matrizProgresso = false;

	private boolean statusMatrizControle; 

	private int tamanhoMatriz; 

	public TbMATRIZCARGOSBean() {

	}


	public void filtraMatriz(String familiaDe, String familiaPara) {
		try {



			if((familiaDe=="") || (familiaPara=="")){

			}else {


				arrayCompleta =  new ArrayList<ArrayList<ArrayList<TbMATRIZCARGOSEntity>>>();

				listaCargos = tbCARGOSService.findAllTbCARGOSEntitiesMatrizFamiliaDe(user.getFlag_pessoa(),familiaDe);
				listaCargosPara = tbCARGOSService.findAllTbCARGOSEntitiesMatrizFamiliaPara(familiaPara);


				for (int i=0; i<listaCargos.size();i++) {

					arrayCompleta.add(new ArrayList<ArrayList<TbMATRIZCARGOSEntity>>());

					for(int j=0; j<listaCargosPara.size();j++) {

						arrayCompleta.get(i).add(new ArrayList<TbMATRIZCARGOSEntity>());
					}

				}

				tamanhoMatriz = (int) (233.75 * listaCargosPara.size());

				List<Object[]> result = new ArrayList<Object[]>();

				result = tbMATRIZCARGOSService.findTbMATRIZCARGOSEntitiesCompleta( familiaDe, familiaPara);

				resultMatriz= tbMATRIZCARGOSService.findTbMATRIZCARGOSEntitiesFT( familiaDe, familiaPara);



				int id_de = 0;
				int id_para = 0;

				int id_matrizde  =0;
				int id_matrizpara  = 0;

				int tamanho = resultMatriz.size();

				int tamanhoK = result.size();

				int ultimaposicao =0;

				int k =0;
				int p =0;







				for (p=0; p<tamanho;p++) {

					for (k=ultimaposicao; k<tamanhoK;k++) {
						id_de = (int) result.get(k)[0];

						id_para = (int) result.get(k)[1];

						id_matrizde = (int) result.get(k)[3];

						id_matrizpara = (int) result.get(k)[2];


						if(id_de==resultMatriz.get(p).getIdCARGODE()) {


							if(id_para==resultMatriz.get(p).getIdCARGOPARA()) {

								TbMATRIZCARGOSEntity r = new TbMATRIZCARGOSEntity();

								r.setIdCARGODE((long) id_de);
								r.setIdCARGOPARA((long) id_para);
								r.setCorAderencia(resultMatriz.get(p).getCorAderencia());
								r.setAdERENCIAFINAL(resultMatriz.get(p).getAdERENCIAFINAL());
								arrayCompleta.get(id_matrizde).get(id_matrizpara).add(r);

								ultimaposicao = k;
								break;
							}


						}

					}








				}



			}
			System.out.println(arrayCompleta.size());

			if(arrayCompleta.size()==1) {
				String message = "message_matriz_vazia";
				FacesMessage facesMessage = MessageFactory.getMessage(message);
				FacesContext.getCurrentInstance().addMessage(null, facesMessage);
			}

		}catch(IndexOutOfBoundsException ex) {

		}

	}

	public Double exportMatrizValue(int indiceX, int indiceY) {
		List<TbMATRIZCARGOSEntity> lista = new ArrayList<TbMATRIZCARGOSEntity>();
		Double aderencia =0.0;
		lista=arrayCompleta.get(indiceX).get(indiceY);
		for(TbMATRIZCARGOSEntity t: lista) {
			aderencia= t.getAdERENCIAFINAL();
		}
		return aderencia;
	}


	public String exportMatrizValueCor(int indiceX, int indiceY) {
		List<TbMATRIZCARGOSEntity> lista = new ArrayList<TbMATRIZCARGOSEntity>();
		String corAderencia ="";
		lista=arrayCompleta.get(indiceX).get(indiceY);
		for(TbMATRIZCARGOSEntity t: lista) {
			corAderencia= t.getCorAderencia();
		}
		return corAderencia;
	}

	@PostConstruct
	public void initizalite(){
		try {









			tamanhoMatriz = (int) (193.75 * countDe);






		}catch (Exception ex) {
			System.out.println(ex.getCause());
		}


		try {
			checaMatrizStatus();
		}catch(ParseException ex) {

		}

	}





	public void checaMatrizStatus() throws ParseException {

		Date alteracaoMatriz = new Date();

		Date alteracaoArea = new Date();
		Date alteracaoDepto = new Date();
		Date alteracaoCargo = new Date();


		Date alteracaoCompetencias = new Date();
		Date alteracaoGrade = new Date();
		Date alteracaoPerfil = new Date();
		Date alteracaoConhecBAS = new Date();
		Date alteracaoConhecESP = new Date();


		Date alteracaoCompetenciasCargos = new Date();
		Date alteracaoGradesCargos = new Date();
		Date alteracaoPerfilCargos = new Date();
		Date alteracaoConhecBASCargos = new Date();
		Date alteracaoConhecESPCargos = new Date();


		alteracaoMatriz = alteracoesService.findUltimaAlteracaoTbMatriz();



		alteracaoArea = alteracoesService.findUltimaAlteracaoTbMAREA();

		alteracaoDepto = alteracoesService.findUltimaAlteracaoTbDEPTO();

		alteracaoCargo = alteracoesService.findUltimaAlteracaoTbCARGO();



		alteracaoCompetencias = alteracoesService.findUltimaAlteracaoTbCOMPETENCIAS();

		alteracaoGrade = alteracoesService.findUltimaAlteracaoTbGRADE();

		alteracaoPerfil = alteracoesService.findUltimaAlteracaoTbPERFIL();

		alteracaoConhecBAS = alteracoesService.findUltimaAlteracaoTbCONHECBAS();

		alteracaoConhecESP = alteracoesService.findUltimaAlteracaoTbCONHECESP();



		alteracaoCompetenciasCargos = alteracoesService.findUltimaAlteracaoTbCOMPETENCIASCARGOS();

		alteracaoGradesCargos = alteracoesService.findUltimaAlteracaoTbGRADECARGOS();

		alteracaoPerfilCargos = alteracoesService.findUltimaAlteracaoTbPERFILCARGOS();

		alteracaoConhecBASCargos = alteracoesService.findUltimaAlteracaoTbCONHECBASCARGOS();

		alteracaoConhecESPCargos = alteracoesService.findUltimaAlteracaoTbCONHECESPCARGOS();


		List<Date> dates = new ArrayList<Date>();

		if(alteracaoArea==null) {

		}else {
			dates.add(alteracaoArea);
		}

		if(alteracaoArea==null) {
		}
		else {
			dates.add(alteracaoDepto);
		}

		if(alteracaoCargo==null) {

		}else {
			dates.add(alteracaoCargo);
		}

		if(alteracaoCompetencias==null) {

		}else {
			dates.add(alteracaoCompetencias);
		}

		if(alteracaoGrade==null) {
		}else {
			dates.add(alteracaoGrade);
		}

		if(alteracaoPerfil==null) {

		}else {
			dates.add(alteracaoPerfil);
		}

		if(alteracaoConhecBAS==null) {

		}else {
			dates.add(alteracaoConhecBAS);
		}

		if(alteracaoConhecESP==null) {

		}else {
			dates.add(alteracaoConhecESP);
		}

		if(alteracaoCompetenciasCargos==null) {

		}else {
			dates.add(alteracaoCompetenciasCargos);
		}

		if(alteracaoGradesCargos==null) {

		}else {
			dates.add(alteracaoGradesCargos);
		}
		if(alteracaoPerfilCargos==null) {

		}else {
			dates.add(alteracaoPerfilCargos);
		}
		if(alteracaoConhecBASCargos==null) {

		}else {
			dates.add(alteracaoConhecBASCargos);
		}
		if(alteracaoConhecESPCargos==null) {

		}else {
			dates.add(alteracaoConhecESPCargos);
		}

		Date latest = Collections.max(dates);

		if(alteracaoMatriz==null) {
			statusMatriz = "A Matriz está vazia! Clique no botão Atualizar Matriz";
			statusMatrizControle = false;
		}else {
			if(latest==null) {

			}else {
				if(alteracaoMatriz.after(latest)) {

					statusMatriz = "A matriz está atualizada!";
					statusMatrizControle = true;

				}else {
					if(alteracaoMatriz.before(latest)) {

						final String OLD_FORMAT = "yyyy-MM-dd HH:mm:ss";
						final String NEW_FORMAT = "dd/MM";
						final String NEW_FORMAT_HOURS = "HH:MM";



						String oldDateString = latest+"";
						String diaMes;
						String horas;





						SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT);
						Date d = sdf.parse(oldDateString);
						sdf.applyPattern(NEW_FORMAT);
						diaMes = sdf.format(d);

						SimpleDateFormat sdfhr = new SimpleDateFormat(OLD_FORMAT);
						Date dhoras = sdfhr.parse(oldDateString);
						sdfhr.applyPattern(NEW_FORMAT_HOURS);
						horas = sdfhr.format(dhoras);


						statusMatriz = "Atenção: A Matriz encontra-se desatualizada devido a alteração realizada em "
								+ diaMes +" <br> Atualize a Matriz antes de prosseguir.";
						statusMatrizControle = false;
					}
				}

			}
		}

	}


	public void prepareNewTbMATRIZCARGOS() {
		reset();
		this.tbMATRIZCARGOS = new TbMATRIZCARGOSEntity();
		// set any default values now, if you need
		// Example: this.tbMATRIZCARGOS.setAnything("test");
	}

	public GenericLazyDataModel<TbMATRIZCARGOSEntity> getLazyModel() {
		if (this.lazyModel == null) {
			this.lazyModel = new GenericLazyDataModel<>(tbMATRIZCARGOSService);
		}
		return this.lazyModel;
	}


	public void resetListas() {
		listaPara.clear();
		data3D.clear();
		listaCargos.clear();
		listConhecimentosBasicos.clear();
		listCB.clear();
		listConhecimentosEspecificos.clear();
		listCE.clear();
		listCompetencia.clear();
		listCO.clear();
		listGrade.clear();
		listGR.clear();
		listPerfil.clear();
		listPE.clear();
	}

	private UIComponent getUIComponent(String id) {  
		return FacesContext.getCurrentInstance().getViewRoot().findComponent(id) ;  
	}



	public void calcularMatriz() {
		matrizProgresso = true;
		super.init();
		setPorcentagemFinal("Iniciando atualização da Matriz...");
		PrimeFaces.current().ajax().update("formAtt:porcentagemFinal");
		long startTime = System.nanoTime();
		matrizListaInsercao.clear();
		listaCargos = tbCARGOSService.findFiveTbCARGOSEntities();
		MatrizPrincipal = tbMATRIZCARGOSService.findAllTbMATRIZCARGOSEntities();

		listConhecimentosEspecificos = tbCONHECIMENTOSESPCARGOSService.findAllTbCONHECIMENTOSESPCARGOSEntities();

		listConhecimentosBasicos = tbCONHECIMENTOSBASCARGOSService.findAllTbCONHECIMENTOSBASCARGOSEntities();

		listCompetencia = tbCOMPETENCIASCARGOSService.findAllTbCOMPETENCIASCARGOSEntities();

		listGrade = tbGRADESCARGOSService.findAllTbGRADECARGOSEntities();

		listPerfil = tbPERFILCARGOSService.findAllTbPERFILCARGOSEntities();


		String flag_pessoa = "";

		tbMATRIZCARGOSService.truncate();

		double porcentagem =  100.0 / listaCargos.size() ;

		for (int i=0; i<listaCargos.size();i++) {
			cargoDe = listaCargos.get(i);
			flag_pessoa = "NAO";
			if(cargoDe.getFlagPessoa().equals("SIM")) {
				flag_pessoa = "SIM";
			}
			oldposicao = true;
			oldposicaoCO = true;
			oldposicaoGR = true;
			oldposicaoPE = true;
			oldsposicaoCB = true;
			for(int j=0; j<listaCargos.size();j++) {
				TbMATRIZCARGOSEntity matrizItem = new TbMATRIZCARGOSEntity(); 



				cargoPara = listaCargos.get(j);

				if(cargoDe.getId()==cargoPara.getId()) {
					matrizItem.setAdERENCIAFINAL(100.0);
					aderenciaFinal = 100.0;
					flagEstaganado = "True";
					matrizItem.setCorAderencia("#16600A");
					matrizItem.setIdCARGODE(cargoDe.getId());
					matrizItem.setIdCARGOPARA(cargoPara.getId());
					matrizItem.setEstaganado(flagEstaganado);
					flagDowngrade = false;
				}else {
					calculaGRADES(cargoDe, cargoPara);
					if(flagDowngrade==true) {

					}else {

						if(cargoDe.getId()==23 && (cargoPara.getId()==446)){
							System.out.println("odi");
						}

						calculaPerfilMatriz(cargoDe, cargoPara);

						calculaCompetenciasMatriz(cargoDe, cargoPara);

						calculaConhecimentosBasicosMatriz(cargoDe, cargoPara);
						
						calculaConhecimentosEspecificosMatrizArrumado(cargoDe, cargoPara);

						calculaAderenciaFinal(cargoDe, cargoPara);

						matrizItem.setIdCARGODE(cargoDe.getId());
						matrizItem.setIdCARGOPARA(cargoPara.getId());
						aderenciaFinal = round(aderenciaFinal,2);
						matrizItem.setAdERENCIAFINAL(aderenciaFinal);
						matrizItem.setCorAderencia(corFinal);
						matrizItem.setEstaganado(flagEstaganado);
					}



				}
				if((aderenciaFinal==0.0) || (flagDowngrade==true) || (cargoPara.getFlagPessoa().trim().equals("SIM"))) {

				}else {
					matrizItem.setFlagPessoa(flag_pessoa);
					matrizListaInsercao.add(matrizItem);

				}

				setPorcentagemFinal("Fase 1/2: Calculando a Matriz, progresso: "+Math.round(porcentagem * (i+1)) + "%");
				PrimeFaces.current().ajax().update("formAtt:porcentagemFinal");
			}





		}

		double percentageOfInserting = 0.0;
		percentageOfInserting = 100.0 / matrizListaInsercao.size();

		String username = SecurityWrapper.getUsername();
		for(int i=0; i<matrizListaInsercao.size();i++) {

			tbMATRIZCARGOSService.insertWithentityManager(matrizListaInsercao.get(i),i,username);


			if(i % 100 == 0) {

				setPorcentagemFinal("Fase 2/2: Carregando a matriz no banco de dados, progresso: "+Math.round(percentageOfInserting * (i+1)) + "%");
			}
		}

		resetListas();
		initizalite();
		matrizProgresso = false;
		long stopTime = System.nanoTime();
		System.out.println("Tempo Decorrido: ");
		System.out.println(stopTime - startTime);
		PrimeFaces current = PrimeFaces.current();
		current.executeScript("PF('statusDialog').hide();");
	}

	public void calculaConhecimentosEspecificosMatriz(TbCARGOSEntity tbCARGOSDe,
			TbCARGOSEntity tbCARGOSPara, int posicao) {

		if((tbCARGOSDe.getId()==1) && (tbCARGOSPara.getId()==7)) {
			System.out.println("oi");
		}

		List<TbCONHECIMENTOSESPCARGOSEntity> listCARGOSPara = new ArrayList<TbCONHECIMENTOSESPCARGOSEntity>();
		List<TbCONHECIMENTOSESPCARGOSEntity> listDe = new ArrayList<TbCONHECIMENTOSESPCARGOSEntity>();
		String controle = "";
		String controle2 = "";
		int n = 0;
		int n2 = 0;
		for (int k =0 ; k<listConhecimentosEspecificos.size();k++) {

			if(listConhecimentosEspecificos.get(k).getIdCARGOS().getDeSCCARGO().equals(tbCARGOSPara.getDeSCCARGO())) {
				listCARGOSPara.add(listConhecimentosEspecificos.get(k));
				controle="S";
				n++;
			}else {
				controle="N";
			}
			if(controle.equals("N") && (n>0)) {
				break;
			}

		}


		if((oldposicao==true)){
			listCECARGOSDe.clear();
			for (int k =0 ; k<listConhecimentosEspecificos.size();k++) {

				if(listConhecimentosEspecificos.get(k).getIdCARGOS().getDeSCCARGO().equals(tbCARGOSDe.getDeSCCARGO())) {
					listCECARGOSDe.add(listConhecimentosEspecificos.get(k));
					controle2="S";
					n2++;
					break;
				}else {
					controle2="N";
				}

				if(controle2.equals("N") && (n2>0)) {
					break;
				}
			}
			oldposicao = false;

		}

		for(int i=0; i<listCARGOSPara.size();i++) {
			String teste="";
			TbCONHECIMENTOSESPCARGOSEntity gap = new TbCONHECIMENTOSESPCARGOSEntity();

			gap.setIdCARGOS(tbCARGOSDe);
			gap.setIdCONHECESP(listCARGOSPara.get(i).getIdCONHECESP());

			for(int j = 0; j<listCECARGOSDe.size();j++) {
				teste = "N";
				if(gap.getIdCONHECESP().getDeSCCONHECIMENTOSESPECIFICOS().equals(listCECARGOSDe.get(j).getIdCONHECESP().getDeSCCONHECIMENTOSESPECIFICOS())) {
					teste ="S";
					gap.setPoNTUACAOCONESP(listCECARGOSDe.get(j).getPoNTUACAOCONESP());
					break;
				}
			}
			if(teste.equals("N")) {
				gap.setPoNTUACAOCONESP(0.0);
			}
			listDe.add(gap);


		}

		listGapCE.clear();
		Double somaListaGap =0.0;
		flagCultura = 1;

		try {
			for(int i=0; i<listDe.size();i++) {
				TbCONHECIMENTOSESPCARGOSEntity gap = new TbCONHECIMENTOSESPCARGOSEntity();
				//Calcula a subtração entre cada pontuação de cada competência

				double gapdePARA = 0;


				if(listDe.get(i).getPoNTUACAOCONESP()==null) {
					listDe.get(i).setPoNTUACAOCONESP(0.0);
				}

				if(listCARGOSPara.get(i).getPoNTUACAOCONESP()==null) {
					listCARGOSPara.get(i).setPoNTUACAOCONESP(0.0);
				}else {

					if(listCARGOSPara.get(i).getIdCONHECESP().getBloqueiaMovConhecEsp()==null) {

					}else {



						if(listCARGOSPara.get(i).getIdCONHECESP().getBloqueiaMovConhecEsp().equals("SIM") &&
								(listCARGOSPara.get(i).getPoNTUACAOCONESP() >=1) && 
								(listDe.get(i).getPoNTUACAOCONESP()==0)) {
							flagDowngrade = true;
						}
					}

					if(listCARGOSPara.get(i).getIdCONHECESP().getDeSCCONHECIMENTOSESPECIFICOS().equals(listDe.get(i).getIdCONHECESP().getDeSCCONHECIMENTOSESPECIFICOS())) {


						if(listDe.get(i).getPoNTUACAOCONESP()==null) {
							listDe.get(i).setPoNTUACAOCONESP(0.0);
						}

						gapDeParaCE = listCARGOSPara.get(i).getPoNTUACAOCONESP()
								- listDe.get(i).getPoNTUACAOCONESP();
						if (gapDeParaCE < 0) {
							gapDeParaCE = 0;
						}else {

							if (gapDeParaCE == 0) {
							} else {
								gapdePARA = gapDeParaCE * listDe.get(i).getIdCONHECESP().getPenalidadeConhecBas();
							}
							gap.setPoNTUACAOCONESP(gapdePARA);
							listGapCE.add(gap);
						}
						
						somaListaGap += gapdePARA;
						System.out.println(somaListaGap);
					}
					

				}
			}

			somaListaGap = round(somaListaGap,2);
			//calculo de aderência
			aderenciaCE =  100 -somaListaGap;
			//aderenciaCE = round(aderenciaCE,2);

			//Se aderencia maior que 100%, força o valor
			if(aderenciaCE > 100) {
				aderenciaCE =100.0;
			}
			//Se aderência menor que -100%, força o valor 
			else if (aderenciaCE <-100) {
				aderenciaCE =-100.0;
			}
		}catch(IndexOutOfBoundsException ex) {

		}
	}

	public static double round(double value, int places) {
		if (places < 0) throw new IllegalArgumentException();

		BigDecimal bd = BigDecimal.valueOf(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}
	
	public void calculaConhecimentosEspecificosMatrizArrumado(TbCARGOSEntity tbCARGOSDe,
			TbCARGOSEntity tbCARGOSPara) {
		List<TbCONHECIMENTOSESPCARGOSEntity> listCARGOSPara = new ArrayList<TbCONHECIMENTOSESPCARGOSEntity>();
		List<TbCONHECIMENTOSESPCARGOSEntity> listDe = new ArrayList<TbCONHECIMENTOSESPCARGOSEntity>();
		String controle = "";
		String controle2 = "";
		int n = 0;
		int n2 = 0;
		for (int k =0 ; k<listConhecimentosEspecificos.size();k++) {

			if(listConhecimentosEspecificos.get(k).getIdCARGOS().getDeSCCARGO().equals(tbCARGOSPara.getDeSCCARGO())) {
				listCARGOSPara.add(listConhecimentosEspecificos.get(k));
				controle="S";
				n++;
			}else {
				controle="N";
			}

			if(controle.equals("N") && (n>0)) {
				break;
			}

		}


			listCECARGOSDe.clear();
			for (int k =0 ; k<listConhecimentosEspecificos.size();k++) {

				if(listConhecimentosEspecificos.get(k).getIdCARGOS().getDeSCCARGO().equals(tbCARGOSDe.getDeSCCARGO())) {
					listCECARGOSDe.add(listConhecimentosEspecificos.get(k));
					controle2="S";
					n2++;

				}
				else {
					controle2="N";
				}
				if(controle2.equals("N") && (n2>0)) {
					break;
				}

			}

		for(int i=0; i<listCARGOSPara.size();i++) {
			String teste="";
			TbCONHECIMENTOSESPCARGOSEntity gap = new TbCONHECIMENTOSESPCARGOSEntity();

			gap.setIdCARGOS(tbCARGOSDe);
			gap.setIdCONHECESP(listCARGOSPara.get(i).getIdCONHECESP());

			for(int j = 0; j<listCECARGOSDe.size();j++) {
				teste = "N";
				if(gap.getIdCONHECESP().getDeSCCONHECIMENTOSESPECIFICOS().equals(listCECARGOSDe.get(j).getIdCONHECESP().getDeSCCONHECIMENTOSESPECIFICOS())) {
					teste ="S";
					gap.setPoNTUACAOCONESP(listCECARGOSDe.get(j).getPoNTUACAOCONESP());
					break;
				}
			}
			if(teste.equals("N")) {
				gap.setPoNTUACAOCONESP(0.0);
			}
			listDe.add(gap);


		}
		listGapCE.clear();
		Double somaListaGap =0.0;

		try {
			for(int i=0; i<listCARGOSPara.size();i++) {
				TbCONHECIMENTOSESPCARGOSEntity gap = new TbCONHECIMENTOSESPCARGOSEntity();
				//Calcula a subtração entre cada pontuação de cada competência
				double gapdePARA =0;
				
				if( listCARGOSPara.get(i).getPoNTUACAOCONESP()==null) {
					listCARGOSPara.get(i).setPoNTUACAOCONESP(0.0);
				}

				if( listDe.get(i).getPoNTUACAOCONESP()==null) {
					listDe.get(i).setPoNTUACAOCONESP(0.0);
				}
				
				
				if(listCARGOSPara.get(i).getIdCONHECESP().getBloqueiaMovConhecEsp()==null) {

				}else {
					if(listCARGOSPara.get(i).getIdCONHECESP().getBloqueiaMovConhecEsp().equals("SIM") &&
							(listCARGOSPara.get(i).getPoNTUACAOCONESP() >=1) && 
							(listDe.get(i).getPoNTUACAOCONESP()==0)) {
						flagDowngrade = true;
					}
				}



				if(listCARGOSPara.get(i).getIdCONHECESP().getDeSCCONHECIMENTOSESPECIFICOS().equals(listDe.get(i).getIdCONHECESP().getDeSCCONHECIMENTOSESPECIFICOS())) {

					if( listCARGOSPara.get(i).getPoNTUACAOCONESP()==null) {
						listCARGOSPara.get(i).setPoNTUACAOCONESP(0.0);
					}

					if( listDe.get(i).getPoNTUACAOCONESP()==null) {
						listDe.get(i).setPoNTUACAOCONESP(0.0);
					}

					gapDeParaCE = listCARGOSPara.get(i).getPoNTUACAOCONESP()
							- listDe.get(i).getPoNTUACAOCONESP();
					if (gapDeParaCE < 0) {
						gapDeParaCE = 0;
					}else {

						if (gapDeParaCE == 0) {
						} else {
							gapdePARA = gapDeParaCE * listDe.get(i).getIdCONHECESP().getPenalidadeConhecBas();
						}
					}
					gap.setPoNTUACAOCONESP(gapdePARA);
					listGapCE.add(gap);
				}


				somaListaGap+=gapdePARA;
			}


			//calculo de aderência
			
			somaListaGap = round(somaListaGap,2);

			aderenciaCE =  100 -(somaListaGap);
			//aderenciaCB = round(aderenciaCB,2);

			//Se aderencia maior que 100%, força o valor
			if(aderenciaCE > 100) {
				aderenciaCE =100.0;
			}
			//Se aderência menor que 0%, força o valor 
			else if (aderenciaCE <-100) {
				aderenciaCE =-100.0;
			}
		}catch(IndexOutOfBoundsException ex) {

		}
	}

	public void calculaConhecimentosBasicosMatriz(TbCARGOSEntity tbCARGOSDe,
			TbCARGOSEntity tbCARGOSPara) {
		List<TbCONHECIMENTOSBASCARGOSEntity> listCARGOSPara = new ArrayList<TbCONHECIMENTOSBASCARGOSEntity>();
		List<TbCONHECIMENTOSBASCARGOSEntity> listDe = new ArrayList<TbCONHECIMENTOSBASCARGOSEntity>();
		String controle = "";
		String controle2 = "";
		int n = 0;
		int n2 = 0;
		for (int k =0 ; k<listConhecimentosBasicos.size();k++) {

			if(listConhecimentosBasicos.get(k).getIdCARGOS().getDeSCCARGO().equals(tbCARGOSPara.getDeSCCARGO())) {
				listCARGOSPara.add(listConhecimentosBasicos.get(k));
				controle="S";
				n++;
			}else {
				controle="N";
			}

			if(controle.equals("N") && (n>0)) {
				break;
			}

		}

		if((oldsposicaoCB==true)){
			listCBCARGOSDe.clear();
			for (int k =0 ; k<listConhecimentosBasicos.size();k++) {

				if(listConhecimentosBasicos.get(k).getIdCARGOS().getDeSCCARGO().equals(tbCARGOSDe.getDeSCCARGO())) {
					listCBCARGOSDe.add(listConhecimentosBasicos.get(k));
					controle2="S";
					n2++;

				}
				else {
					controle2="N";
				}
				if(controle2.equals("N") && (n2>0)) {
					break;
				}

			}
			oldsposicaoCB = false;
		}
		for(int i=0; i<listCARGOSPara.size();i++) {
			String teste="";
			TbCONHECIMENTOSBASCARGOSEntity gap = new TbCONHECIMENTOSBASCARGOSEntity();

			gap.setIdCARGOS(tbCARGOSDe);
			gap.setIdCONHECBAS(listCARGOSPara.get(i).getIdCONHECBAS());

			for(int j = 0; j<listCBCARGOSDe.size();j++) {
				teste = "N";
				if(gap.getIdCONHECBAS().getDeSCCONHECIMENTOSBASICOS().equals(listCBCARGOSDe.get(j).getIdCONHECBAS().getDeSCCONHECIMENTOSBASICOS())) {
					teste ="S";
					gap.setPoNTUACAOCONBAS(listCBCARGOSDe.get(j).getPoNTUACAOCONBAS());
					break;
				}
			}
			if(teste.equals("N")) {
				gap.setPoNTUACAOCONBAS(0.0);
			}
			listDe.add(gap);


		}
		listGapCB.clear();
		Double somaListaGap =0.0;

		try {
			for(int i=0; i<listDe.size();i++) {
				TbCONHECIMENTOSBASCARGOSEntity gap = new TbCONHECIMENTOSBASCARGOSEntity();
				//Calcula a subtração entre cada pontuação de cada competência
				double gapdePARA =0;

				if(listCARGOSPara.get(i).getIdCONHECBAS().getBloqueiaMovConhecBas()==null) {

				}else {
					if(listCARGOSPara.get(i).getIdCONHECBAS().getBloqueiaMovConhecBas().equals("SIM") &&
							(listCARGOSPara.get(i).getPoNTUACAOCONBAS() >=1) && 
							(listDe.get(i).getPoNTUACAOCONBAS()==0)) {
						flagDowngrade = true;
					}
				}



				if(listCARGOSPara.get(i).getIdCONHECBAS().getDeSCCONHECIMENTOSBASICOS().equals(listDe.get(i).getIdCONHECBAS().getDeSCCONHECIMENTOSBASICOS())) {

					if( listCARGOSPara.get(i).getPoNTUACAOCONBAS()==null) {
						listCARGOSPara.get(i).setPoNTUACAOCONBAS(0.0);
					}

					if( listDe.get(i).getPoNTUACAOCONBAS()==null) {
						listDe.get(i).setPoNTUACAOCONBAS(0.0);
					}

					gapDeParaCB = listCARGOSPara.get(i).getPoNTUACAOCONBAS()
							- listDe.get(i).getPoNTUACAOCONBAS();
					if (gapDeParaCB < 0) {
						gapDeParaCB = 0;
					}else {

						if (gapDeParaCB == 0) {
						} else {
							gapdePARA = gapDeParaCB * listDe.get(i).getIdCONHECBAS().getPenalidadeConhecBas();
						}
					}
					gap.setPoNTUACAOCONBAS(gapdePARA);
					listGapCB.add(gap);
				}


				somaListaGap+=gapdePARA;
			}


			//calculo de aderência

			somaListaGap = round(somaListaGap,2);

			aderenciaCB =  100 -(somaListaGap);
			//aderenciaCB = round(aderenciaCB,2);

			//Se aderencia maior que 100%, força o valor
			if(aderenciaCB > 100) {
				aderenciaCB =100.0;
			}
			//Se aderência menor que 0%, força o valor 
			else if (aderenciaCB <-100) {
				aderenciaCB =-100.0;
			}
		}catch(IndexOutOfBoundsException ex) {

		}
	}


	public void calculaCompetenciasMatriz(TbCARGOSEntity tbCARGOSDe,
			TbCARGOSEntity tbCARGOSPara) {


		List<TbCOMPETENCIASCARGOSEntity> listCARGOSPara = new ArrayList<TbCOMPETENCIASCARGOSEntity>();
		List<TbCOMPETENCIASCARGOSEntity> listDe = new ArrayList<TbCOMPETENCIASCARGOSEntity>();
		String controle = "";
		String controle2 = "";
		int n = 0;
		int n2 = 0;

		if(oldposicaoCO == true) {
			listCOCARGOSDe.clear();
			for (int k =0 ; k<listCompetencia.size();k++) {
				if(listCompetencia.get(k).getIdCARGOS().getDeSCCARGO().equals(tbCARGOSDe.getDeSCCARGO())) {
					listCOCARGOSDe.add(listCompetencia.get(k));
					controle="S";
					n++;


				}else {
					controle="N";
				}

				if(controle.equals("N") && (n>0)) {
					break;
				}


			}
			oldposicaoCO = false;
		}

		for (int k =0 ; k<listCompetencia.size();k++) {
			if(listCompetencia.get(k).getIdCARGOS().getDeSCCARGO().equals(tbCARGOSPara.getDeSCCARGO())) {
				listCARGOSPara.add(listCompetencia.get(k));
				controle2="S";
				n2++;
			}else {
				controle2="N";
			}

			if(controle2.equals("N") && (n2>0)) {
				break;
			}


		}

		for(int i=0; i<listCARGOSPara.size();i++) {
			String teste="";
			TbCOMPETENCIASCARGOSEntity gap = new TbCOMPETENCIASCARGOSEntity();

			gap.setIdCARGOS(tbCARGOSDe);
			gap.setIdCOMPETENCIAS(listCARGOSPara.get(i).getIdCOMPETENCIAS());

			for(int j = 0; j<listCOCARGOSDe.size();j++) {
				teste = "N";
				if(gap.getIdCOMPETENCIAS().getDeSCCOMPETENCIA().equals(listCOCARGOSDe.get(j).getIdCOMPETENCIAS().getDeSCCOMPETENCIA())) {
					teste ="S";
					gap.setPoNTUACAOCOMPETENCIA(listCOCARGOSDe.get(j).getPoNTUACAOCOMPETENCIA());
					break;
				}
			}
			if(teste.equals("N")) {
				gap.setPoNTUACAOCOMPETENCIA(0.0);
			}
			listDe.add(gap);


		}

		listGap.clear();
		Double somaListaGap =0.0;

		flagCultura = 1;
		try {
			for(int i=0; i<listDe.size();i++) {
				TbCOMPETENCIASCARGOSEntity gap = new TbCOMPETENCIASCARGOSEntity();
				//Calcula a subtração entre cada pontuação de cada competência

				if(listCARGOSPara.get(i).getIdCOMPETENCIAS().getBloqueiaMovCompetencias()==null) {

				}else {
					if(listCARGOSPara.get(i).getIdCOMPETENCIAS().getBloqueiaMovCompetencias().equals("SIM") &&
							(listCARGOSPara.get(i).getPoNTUACAOCOMPETENCIA() >=1) && 
							(listDe.get(i).getPoNTUACAOCOMPETENCIA()==0)) {
						flagDowngrade = true;
					}
				}

				if(listCARGOSPara.get(i).getPoNTUACAOCOMPETENCIA()==null) {
					listCARGOSPara.get(i).setPoNTUACAOCOMPETENCIA(0.0);
				}

				if( listDe.get(i).getPoNTUACAOCOMPETENCIA()==null) {
					listDe.get(i).setPoNTUACAOCOMPETENCIA(0.0);
				}


				gapDePara= listCARGOSPara.get(i).getPoNTUACAOCOMPETENCIA()
						-  listDe.get(i).getPoNTUACAOCOMPETENCIA();


				if(gapDePara<0.0) {
					gapDePara = 0.0;
				}
				double gapdePARA = 0.0;

				// gap = (Cargo Para - Cargo De) * penalidade
				if (gapDePara==0.0) {

				}else {
					gapdePARA = gapDePara* listDe.get(i).getIdCOMPETENCIAS().getPenalidadeCompetencias();
				}
				gap.setPoNTUACAOCOMPETENCIA(gapdePARA);
				listGap.add(gap);
				somaListaGap+=gapdePARA;
			}

			//calculo de aderência


			somaListaGap = round(somaListaGap,2);

			aderencia =  100 -somaListaGap;
			//aderencia = round(aderencia,2);

			//Se aderencia maior que 100%, força o valor
			if(aderencia > 100) {
				aderencia =100.0;
			}
			//Se aderência menor que 0%, força o valor 
			else if (aderencia <-100) {
				aderencia =-100.0;
			}
		}catch(IndexOutOfBoundsException ex) {

		}
	}

	public void calculaGRADES(TbCARGOSEntity tbCARGOSDe,
			TbCARGOSEntity tbCARGOSPara) {
		flagEstaganado = "false";
		aderenciaGR = 0.0;
		List<TbGRADECARGOSEntity> listCARGOSPara = new ArrayList<TbGRADECARGOSEntity>();
		List<TbGRADECARGOSEntity> listDe = new ArrayList<TbGRADECARGOSEntity>();
		String controle = "";
		String controle2 = "";
		int n = 0;
		int n2 = 0;
		if(oldposicaoGR==true) {
			listGRCARGOSDe.clear();
			for (int k =0 ; k<listGrade.size();k++) {
				if(listGrade.get(k).getIdCARGOS().getDeSCCARGO().equals(tbCARGOSDe.getDeSCCARGO())) {
					listGRCARGOSDe.add(listGrade.get(k));
					controle="S";
					n++;

				}else {
					controle="N";
				}

				if(controle.equals("N") && (n>0)) {
					break;
				}


			}
			oldposicaoGR=false;
		}

		for (int k =0 ; k<listGrade.size();k++) {
			if(listGrade.get(k).getIdCARGOS().getDeSCCARGO().equals(tbCARGOSPara.getDeSCCARGO())) {
				listCARGOSPara.add(listGrade.get(k));
				controle2="S";
				n2++;
			}else {
				controle2="N";
			}

			if(controle2.equals("N") && (n2>0)) {
				break;
			}
		}


		for(int i=0; i<listCARGOSPara.size();i++) {
			String teste="";
			TbGRADECARGOSEntity gap = new TbGRADECARGOSEntity();

			gap.setIdCARGOS(tbCARGOSDe);
			gap.setIdGRADE(listCARGOSPara.get(i).getIdGRADE());

			for(int j = 0; j<listGRCARGOSDe.size();j++) {
				teste = "N";
				if(gap.getIdGRADE().getDeSCGRADE().equals(listGRCARGOSDe.get(j).getIdGRADE().getDeSCGRADE())) {
					teste ="S";
					gap.setPoNTUACAOGRADE(listGRCARGOSDe.get(j).getPoNTUACAOGRADE());
					break;
				}
			}
			if(teste.equals("N")) {
				gap.setPoNTUACAOGRADE(0.0);
			}
			listDe.add(gap);


		}
		listGapGR.clear();
		Double somaListaGapGR = 0.0;
		flagDowngrade = false;
		flagCultura = 1;
		int i =0;
		try {
			for (i = 0; i < listCARGOSPara.size(); i++) {


				if(listDe.get(i).getPoNTUACAOGRADE() ==null) {
					listDe.get(i).setPoNTUACAOGRADE(0.0);
				}

				if(listCARGOSPara.get(i).getPoNTUACAOGRADE() ==null) {
					listCARGOSPara.get(i).setPoNTUACAOGRADE(0.0);
				}  
				else {
					TbGRADECARGOSEntity gap = new TbGRADECARGOSEntity();
					double gapdePARA = 0;
					if(listCARGOSPara.get(i).getIdGRADE().getDeSCGRADE().equals(listDe.get(i).getIdGRADE().getDeSCGRADE())){

						gapDeParaGR = listCARGOSPara.get(i).getPoNTUACAOGRADE() - listDe.get(i).getPoNTUACAOGRADE();

						if(gapDeParaGR <0) {
							flagDowngrade = true;
							aderenciaGR = 0.0;
							aderenciaFinal =0.0;
							aderencia = 0.0;
							aderenciaCB = 0.0;
							aderenciaCE =0.0;
							aderenciaGR =0.0;
							aderenciaPE= 0.0;
							corFinal = "#F2F2F2";
							return;
						}
						if(gapDeParaGR==0) {
							flagEstaganado = "True";
						} else {
							gapdePARA = gapDeParaGR * listDe.get(i).getIdGRADE().getPenalidadeConhecGrade();
						}
					}
					gap.setPoNTUACAOGRADE(gapdePARA);
					listGapGR.add(gap);
					somaListaGapGR += gapdePARA;	
				}
			}

			// calculo de aderência

			somaListaGapGR = round(somaListaGapGR,2);

			aderenciaGR = 100 - (somaListaGapGR);
			// aderenciaGR = round(aderenciaGR,2);

			// Se aderencia maior que 100%, força o valor
			if (aderenciaGR > 100) {
				aderenciaGR = 100.0;
			}
			// Se aderência menor que 0%, força o valor
			else if (aderenciaGR < -100) {
				aderenciaGR = -100.0;
			}
		} catch (IndexOutOfBoundsException ex) {

		}

	}



	public void calculaPerfilMatriz(TbCARGOSEntity tbCARGOSDe,
			TbCARGOSEntity tbCARGOSPara) {


		List<TbPERFILCARGOSEntity> listCARGOSPara = new ArrayList<TbPERFILCARGOSEntity>();
		List<TbPERFILCARGOSEntity> listDe = new ArrayList<TbPERFILCARGOSEntity>();
		String controle = "";
		String controle2 = "";
		int n = 0;
		int n2 = 0;
		if(oldposicaoPE==true) {
			listPECARGOSDe.clear();
			for (int k =0 ; k<listPerfil.size();k++) {
				if(listPerfil.get(k).getIdCARGOS().getDeSCCARGO().equals(tbCARGOSDe.getDeSCCARGO())) {
					listPECARGOSDe.add(listPerfil.get(k));
					controle="S";
					n++;
				}else {
					controle="N";
				}

				if(controle.equals("N") && (n>0)) {
					break;
				}


			}
			oldposicaoPE=false;
		}
		for (int k =0 ; k<listPerfil.size();k++) {

			if(listPerfil.get(k).getIdCARGOS().getDeSCCARGO().equals(tbCARGOSPara.getDeSCCARGO())) {
				listCARGOSPara.add(listPerfil.get(k));
				controle2="S";
				n2++;
			}else {
				controle2="N";
			}

			if(controle2.equals("N") && (n2>0)) {
				break;
			}
		}




		for(int i=0; i<listCARGOSPara.size();i++) {
			String teste="";
			TbPERFILCARGOSEntity gap = new TbPERFILCARGOSEntity();

			gap.setIdCARGOS(tbCARGOSDe);
			gap.setIdPERFIL(listCARGOSPara.get(i).getIdPERFIL());

			for(int j = 0; j<listPECARGOSDe.size();j++) {
				teste = "N";
				if(gap.getIdPERFIL().getDeSCPERFIL().equals(listPECARGOSDe.get(j).getIdPERFIL().getDeSCPERFIL())) {
					teste ="S";
					gap.setPoNTUACAOPERFIL(listPECARGOSDe.get(j).getPoNTUACAOPERFIL());
					break;
				}
			}
			if(teste.equals("N")) {
				gap.setPoNTUACAOPERFIL(0.0);
			}
			listDe.add(gap);


		}


		listGapPE.clear();
		Double somaListaGapPE =0.0;
		flagCultura = 1;

		try {
			for(int i=0; i<listDe.size();i++) {
				TbPERFILCARGOSEntity gap = new TbPERFILCARGOSEntity();
				//Calcula a subtração entre cada pontuação de cada perfil


				double gapdePARA = 0;
				if(listCARGOSPara.get(i).getIdPERFIL().getBloqueiaMovConhecPerfil()==null) {

				}else {
					if(listCARGOSPara.get(i).getIdPERFIL().getBloqueiaMovConhecPerfil().equals("SIM") &&
							(listCARGOSPara.get(i).getPoNTUACAOPERFIL() >=1) && 
							(listDe.get(i).getPoNTUACAOPERFIL()==0)

							) {
						flagDowngrade = true;
					}
				}

				if(listCARGOSPara.get(i).getPoNTUACAOPERFIL() == listDe.get(i).getPoNTUACAOPERFIL()) {
					gapDeParaPE = 0;
				}else {

					if(listCARGOSPara.get(i).getPoNTUACAOPERFIL()==null) {
						listCARGOSPara.get(i).setPoNTUACAOPERFIL(0.0);
					}
					if(listDe.get(i).getPoNTUACAOPERFIL()==null) {
						listDe.get(i).setPoNTUACAOPERFIL(0.0);
					}

					//(Módulo(Cargo Para - Cargo De)
					gapDeParaPE= listCARGOSPara.get(i).getPoNTUACAOPERFIL() - listDe.get(i).getPoNTUACAOPERFIL();
					gapDeParaPE = Math.abs(gapDeParaPE);

				}

				if(gapDeParaPE==0) {

				}else {
					gapdePARA = gapDeParaPE*listDe.get(i).getIdPERFIL().getPenalidadeConhecPerfil();
				}
				gap.setPoNTUACAOPERFIL(gapdePARA);
				listGapPE.add(gap);
				somaListaGapPE+=gapdePARA;
			}


			//calculo de aderência

			somaListaGapPE = round(somaListaGapPE,2);

			aderenciaPE =  100 -somaListaGapPE;
			//aderenciaPE = round(aderenciaPE,2);

			//Se aderencia maior que 100%, força o valor
			if(aderenciaPE > 100) {
				aderenciaPE =100.0;
			}
			//Se aderência menor que 0%, força o valor 
			else if (aderenciaPE <-100) {
				aderenciaPE =-100.0;
			}
		}catch(IndexOutOfBoundsException ex) {

		}
	}


	public String persist() {

		if (tbMATRIZCARGOS.getId() == null && !isPermitted("tbMATRIZCARGOS:create")) {
			return "accessDenied";
		} else if (tbMATRIZCARGOS.getId() != null && !isPermitted(tbMATRIZCARGOS, "tbMATRIZCARGOS:update")) {
			return "accessDenied";
		}

		String message;

		try {

			if (tbMATRIZCARGOS.getId() != null) {
				tbMATRIZCARGOS = tbMATRIZCARGOSService.update(tbMATRIZCARGOS);
				message = "message_successfully_updated";
			} else {
				tbMATRIZCARGOS = tbMATRIZCARGOSService.save(tbMATRIZCARGOS);
				message = "message_successfully_created";
			}
		} catch (OptimisticLockException e) {
			logger.log(Level.SEVERE, "Error occured", e);
			message = "message_optimistic_locking_exception";
			// Set validationFailed to keep the dialog open
			FacesContext.getCurrentInstance().validationFailed();
		} catch (PersistenceException e) {
			logger.log(Level.SEVERE, "Error occured", e);
			message = "message_save_exception";
			// Set validationFailed to keep the dialog open
			FacesContext.getCurrentInstance().validationFailed();
		}

		FacesMessage facesMessage = MessageFactory.getMessage(message);
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);

		return null;
	}

	public String delete() {

		if (!isPermitted(tbMATRIZCARGOS, "tbMATRIZCARGOS:delete")) {
			return "accessDenied";
		}

		String message;

		try {
			tbMATRIZCARGOSService.delete(tbMATRIZCARGOS);
			message = "message_successfully_deleted";
			reset();
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error occured", e);
			message = "message_delete_exception";
			// Set validationFailed to keep the dialog open
			FacesContext.getCurrentInstance().validationFailed();
		}
		FacesContext.getCurrentInstance().addMessage(null, MessageFactory.getMessage(message));

		return null;
	}

	public void onDialogOpen(TbMATRIZCARGOSEntity tbMATRIZCARGOS) {
		reset();
		this.tbMATRIZCARGOS = tbMATRIZCARGOS;
	}

	public void reset() {
		tbMATRIZCARGOS = null;

	}

	public TbMATRIZCARGOSEntity getTbMATRIZCARGOS() {
		// Need to check for null, because some strange behaviour of f:viewParam
		// Sometimes it is setting to null
		if (this.tbMATRIZCARGOS == null) {
			prepareNewTbMATRIZCARGOS();
		}
		return this.tbMATRIZCARGOS;
	}

	public void setTbMATRIZCARGOS(TbMATRIZCARGOSEntity tbMATRIZCARGOS) {
		if (tbMATRIZCARGOS != null) {
			this.tbMATRIZCARGOS = tbMATRIZCARGOS;
		}
	}

	public boolean isPermitted(String permission) {
		return SecurityWrapper.isPermitted(permission);
	}

	public boolean isPermitted(TbMATRIZCARGOSEntity tbMATRIZCARGOS, String permission) {

		return SecurityWrapper.isPermitted(permission);

	}

	public List<TbCARGOSEntity> getListaCargos() {
		return listaCargos;
	}

	public void setListaCargos(List<TbCARGOSEntity> listaCargos) {
		this.listaCargos = listaCargos;
	}





	public List<Long> getListaPara() {

		return listaPara;
	}

	public void setListaPara(List<Long> listaPara) {
		this.listaPara = listaPara;
	}

	public ArrayList<ArrayList<ArrayList<TbMATRIZCARGOSEntity>>> getData3D() {


		return data3D;
	}

	public void setData3D(ArrayList<ArrayList<ArrayList<TbMATRIZCARGOSEntity>>>  data3d) {
		data3D = data3d;
	}
	public List<TbMATRIZCARGOSEntity> getMatrizCargos() {
		return matrizCargos;
	}
	public void setMatrizCargos(List<TbMATRIZCARGOSEntity> matrizCargos) {
		this.matrizCargos = matrizCargos;
	}
	public String getCorFinal() {
		return corFinal;
	}
	public void setCorFinal(String corFinal) {
		this.corFinal = corFinal;
	}




	public String getPorcentagemFinal() {
		return porcentagemFinal;
	}




	public void setPorcentagemFinal(String porcentagemFinal) {
		this.porcentagemFinal = porcentagemFinal;
	}




	public String getFlagEstaganado() {
		return flagEstaganado;
	}




	public void setFlagEstaganado(String flagEstaganado) {
		this.flagEstaganado = flagEstaganado;
	}




	public List<TbCARGOSEntity> getListaCargosSPessoas() {
		return listaCargosSPessoas;
	}




	public void setListaCargosSPessoas(List<TbCARGOSEntity> listaCargosSPessoas) {
		this.listaCargosSPessoas = listaCargosSPessoas;
	}




	public int getId_fde() {
		return id_fde;
	}




	public void setId_fde(int id_fde) {
		this.id_fde = id_fde;
	}




	public int getId_fpara() {
		return id_fpara;
	}




	public void setId_fpara(int id_fpara) {
		this.id_fpara = id_fpara;
	}




	public List<TbCARGOSEntity> getListaCargosPara() {
		return listaCargosPara;
	}




	public void setListaCargosPara(List<TbCARGOSEntity> listaCargosPara) {
		this.listaCargosPara = listaCargosPara;
	}




	public List<TbMATRIZCARGOSEntity> getMatrizListaInsercao() {
		return matrizListaInsercao;
	}




	public void setMatrizListaInsercao(List<TbMATRIZCARGOSEntity> matrizListaInsercao) {
		this.matrizListaInsercao = matrizListaInsercao;
	}




	public String getStatusMatriz() {
		return statusMatriz;
	}




	public void setStatusMatriz(String statusMatriz) {
		this.statusMatriz = statusMatriz;
	}




	public boolean isStatusMatrizControle() {
		return statusMatrizControle;
	}




	public void setStatusMatrizControle(boolean statusMatrizControle) {
		this.statusMatrizControle = statusMatrizControle;
	}




	public boolean isMatrizProgresso() {
		return matrizProgresso;
	}




	public void setMatrizProgresso(boolean matrizProgresso) {
		this.matrizProgresso = matrizProgresso;
	}


	public Long getCountDe() {
		return countDe;
	}


	public void setCountDe(Long countDe) {
		this.countDe = countDe;
	}


	public int getTamanhoMatriz() {
		return tamanhoMatriz;
	}


	public void setTamanhoMatriz(int tamanhoMatriz) {
		this.tamanhoMatriz = tamanhoMatriz;
	}


	public List<ArrayList<ArrayList<TbMATRIZCARGOSEntity>>> getArrayCompleta() {
		return arrayCompleta;
	}


	public void setArrayCompleta(List<ArrayList<ArrayList<TbMATRIZCARGOSEntity>>> arrayCompleta) {
		this.arrayCompleta = arrayCompleta;
	}


	public List<TbMATRIZCARGOSEntity> getResultMatriz() {
		return resultMatriz;
	}


	public void setResultMatriz(List<TbMATRIZCARGOSEntity> resultMatriz) {
		this.resultMatriz = resultMatriz;
	}


	public String getAjaxStatus() {
		return ajaxStatus;
	}


	public void setAjaxStatus(String ajaxStatus) {
		this.ajaxStatus = ajaxStatus;
	}


}
