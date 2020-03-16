package org.applicationn.simtrilhas.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
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

	private List<Long> listaDe = new ArrayList<Long>();

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

	List<ArrayList<ArrayList<TbMATRIZCARGOSEntity>>> arrlist10 = new ArrayList<ArrayList<ArrayList<TbMATRIZCARGOSEntity>>>();

	int id_fde =0;
	int id_fpara = 0;

	private ArrayList<ArrayList<ArrayList<TbMATRIZCARGOSEntity>>> data3D = new ArrayList<ArrayList<ArrayList<TbMATRIZCARGOSEntity>>>();

	private String porcentagemFinal = "teste";


	public TbMATRIZCARGOSBean() {

	}




	@PostConstruct
	public void initizalite(){

		try {

			listaDe = tbMATRIZCARGOSService.findDistinctDe();
			List<Object[]> result = new ArrayList<Object[]>();
			listaCargos = tbCARGOSService.findAllTbCARGOSEntitiesMatriz(user.getFlag_pessoa());
			listaCargosSPessoas = tbCARGOSService.findFiveTbCARGOSEntities();
			listaCargosPara = tbCARGOSService.findTbCARGOSSPessoasEntities();
			result = tbMATRIZCARGOSService.findTbMATRIZCARGOSEntities(user.getFlag_pessoa());
			int k=0, l =0;

			for(k = 0; k<listaDe.size();k++) {
				data3D.add(new ArrayList<ArrayList<TbMATRIZCARGOSEntity>>());

				for(l=0;l<listaDe.size();l++) {
					data3D.get(k).add(new ArrayList<TbMATRIZCARGOSEntity>());
				}
			}


			int pontoinicial = listaCargosSPessoas.size() - listaCargos.size();


			if(result.size()==0) {

			}else {
				long id_de = 0;
				long id_para = 0;

				for (Object[] res : result) {
					TbMATRIZCARGOSEntity r = new TbMATRIZCARGOSEntity();
					r.setCorAderencia((String) res[0]);
					r.setAdERENCIAFINAL((double) res[1]);
					id_de = ((long) res[2] - pontoinicial);
					id_para = ((long) res[3] - 1);
					data3D.get((int) id_de).get((int) id_para).add(r);

				}

				//arrlist10 = data3D.subList(0, 10); 

			}

		}catch (Exception ex) {
			System.out.println(ex.getCause());
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
		listaDe.clear();
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

		super.init();
		setPorcentagemFinal("Carregando listas");
		System.out.println("Carregando listas");
		long startTime = System.nanoTime();
		matrizListaInsercao.clear();
		listaCargos = tbCARGOSService.findFiveTbCARGOSEntities();
		MatrizPrincipal = tbMATRIZCARGOSService.findAllTbMATRIZCARGOSEntities();

		listConhecimentosEspecificos = tbCONHECIMENTOSESPCARGOSService.findAllTbCONHECIMENTOSESPCARGOSEntities();

		listConhecimentosBasicos = tbCONHECIMENTOSBASCARGOSService.findAllTbCONHECIMENTOSBASCARGOSEntities();

		listCompetencia = tbCOMPETENCIASCARGOSService.findAllTbCOMPETENCIASCARGOSEntities();

		listGrade = tbGRADESCARGOSService.findAllTbGRADECARGOSEntities();

		listPerfil = tbPERFILCARGOSService.findAllTbPERFILCARGOSEntities();

		porcentagemFinal="Deletando matriz antiga...";
		System.out.println("Deletando matriz antiga...");

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


						calculaPerfilMatriz(cargoDe, cargoPara);

						calculaCompetenciasMatriz(cargoDe, cargoPara);

						calculaConhecimentosBasicosMatriz(cargoDe, cargoPara);

						calculaConhecimentosEspecificosMatriz(cargoDe, cargoPara,i);

						calculaAderenciaFinal(cargoDe, cargoPara);

						matrizItem.setIdCARGODE(cargoDe.getId());
						matrizItem.setIdCARGOPARA(cargoPara.getId());
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
				System.out.println("Interação I:" + i + "\n" 
						+  "Interação J:" + j + "\n"	);
				System.out.println(porcentagem * (i+1));

				porcentagemFinal = ""+porcentagem * (i+1) + "%";
			}





		}

		for(int i=0; i<matrizListaInsercao.size();i++) {
			
			
			
			tbMATRIZCARGOSService.insertWithEntityManager(matrizListaInsercao.get(i),i);
			
			
		}
		
		resetListas();
		initizalite();
		long stopTime = System.nanoTime();
		System.out.println("Tempo Decorrido: ");
		System.out.println(stopTime - startTime);
		PrimeFaces current = PrimeFaces.current();
		current.executeScript("PF('statusDialog').hide();");
	}

	public void calculaConhecimentosEspecificosMatriz(TbCARGOSEntity tbCARGOSDe,
			TbCARGOSEntity tbCARGOSPara, int posicao) {


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
				}
			}
			if(teste.equals("N")) {
				gap.setPoNTUACAOCONESP(0);
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

				int gapdePARA = 0;
				
				if(listCARGOSPara.get(i).getIdCONHECESP().getBloqueiaMovConhecEsp().equals("SIM") &&
						(listCARGOSPara.get(i).getPoNTUACAOCONESP() >=1) && 
						(listDe.get(i).getPoNTUACAOCONESP()==0)) {
					flagDowngrade = true;
				}
				
				if(listCARGOSPara.get(i).getPoNTUACAOCONESP() == listDe.get(i).getPoNTUACAOCONESP()){

				}else {
					//Se "(Cargo Para - Cargo De)" < 0 então  "(Cargo Para - Cargo De)" = "0"
					if(listCARGOSPara.get(i).getPoNTUACAOCONESP() < listDe.get(i).getPoNTUACAOCONESP()){

					}else {



						if(listDe.get(i).getIdCONHECESP().getBloqueiaMovConhecEsp()==null) {
							gapdePARA =(listCARGOSPara.get(i).getPoNTUACAOCONESP() - 
									listDe.get(i).getPoNTUACAOCONESP())
									* listDe.get(i).getIdCONHECESP().getPenalidadeConhecBas();
						}else {

							if(listDe.get(i).getIdCONHECESP().getBloqueiaMovConhecEsp().equals("SIM")) {
								flagDowngrade = true;
							}
						}
					}
				}

				gap.setPoNTUACAOCONESP(gapdePARA);
				listGapCE.add(gap);
				somaListaGap += gapdePARA;
			}

			//calculo de aderência
			aderenciaCE =  100 -somaListaGap;
			//aderenciaCE = round(aderenciaCE,2);

			//Se aderencia maior que 100%, força o valor
			if(aderenciaCE > 100) {
				aderenciaCE =100.0;
			}
			//Se aderência menor que 0%, força o valor 
			else if (aderenciaCE <0) {
				aderenciaCE =0.0;
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
				}
			}
			if(teste.equals("N")) {
				gap.setPoNTUACAOCONBAS(0);
			}
			listDe.add(gap);


		}
		listGapCB.clear();
		Double somaListaGap =0.0;

		try {
			for(int i=0; i<listDe.size();i++) {
				TbCONHECIMENTOSBASCARGOSEntity gap = new TbCONHECIMENTOSBASCARGOSEntity();
				//Calcula a subtração entre cada pontuação de cada competência
				int gapdePARA =0;
				
				
				if(listCARGOSPara.get(i).getIdCONHECBAS().getBloqueiaMovConhecBas().equals("SIM") &&
						(listCARGOSPara.get(i).getPoNTUACAOCONBAS() >=1) && 
						(listDe.get(i).getPoNTUACAOCONBAS()==0)) {
					flagDowngrade = true;
				}
				
				if(listCARGOSPara.get(i).getPoNTUACAOCONBAS() == listDe.get(i).getPoNTUACAOCONBAS()){

				}else {
					if(listCARGOSPara.get(i).getPoNTUACAOCONBAS() < listDe.get(i).getPoNTUACAOCONBAS()){

					}else {

						gapdePARA =(listCARGOSPara.get(i).getPoNTUACAOCONBAS() - 
								listDe.get(i).getPoNTUACAOCONBAS())
								* listDe.get(i).getIdCONHECBAS().getPenalidadeConhecBas();
					}
				}
				gap.setPoNTUACAOCONBAS(gapdePARA);
				listGapCB.add(gap);
				somaListaGap+=gapdePARA;
			}


			//calculo de aderência
			aderenciaCB =  100 -(somaListaGap);
			//aderenciaCB = round(aderenciaCB,2);

			//Se aderencia maior que 100%, força o valor
			if(aderenciaCB > 100) {
				aderenciaCB =100.0;
			}
			//Se aderência menor que 0%, força o valor 
			else if (aderenciaCB <0) {
				aderenciaCB =0.0;
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
				}
			}
			if(teste.equals("N")) {
				gap.setPoNTUACAOCOMPETENCIA(0);
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
				
				
				if(listCARGOSPara.get(i).getIdCOMPETENCIAS().getBloqueiaMovCompetencias().equals("SIM") &&
						(listCARGOSPara.get(i).getPoNTUACAOCOMPETENCIA() >=1) && 
						(listDe.get(i).getPoNTUACAOCOMPETENCIA()==0)) {
					flagDowngrade = true;
				}
				
				gapDePara= listCARGOSPara.get(i).getPoNTUACAOCOMPETENCIA()
						-  listDe.get(i).getPoNTUACAOCOMPETENCIA();


				if(gapDePara<0) {
					gapDePara = 0;
				}
				int gapdePARA = 0;

				// gap = (Cargo Para - Cargo De) * penalidade
				if (gapDePara==0) {

				}else {
					gapdePARA = gapDePara* listDe.get(i).getIdCOMPETENCIAS().getPenalidadeCompetencias();
				}
				gap.setPoNTUACAOCOMPETENCIA(gapdePARA);
				listGap.add(gap);
				somaListaGap+=gapdePARA;
			}

			//calculo de aderência
			aderencia =  100 -somaListaGap;
			//aderencia = round(aderencia,2);

			//Se aderencia maior que 100%, força o valor
			if(aderencia > 100) {
				aderencia =100.0;
			}
			//Se aderência menor que 0%, força o valor 
			else if (aderencia <0) {
				aderencia =0.0;
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
				}
			}
			if(teste.equals("N")) {
				gap.setPoNTUACAOGRADE(0);
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
					listDe.get(i).setPoNTUACAOGRADE(0);
				}

				if(listCARGOSPara.get(i).getPoNTUACAOGRADE() ==null) {
					listCARGOSPara.get(i).setPoNTUACAOGRADE(0);
				}  
				else {
					TbGRADECARGOSEntity gap = new TbGRADECARGOSEntity();
					int gapdePARA = 0;
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
			aderenciaGR = 100 - (somaListaGapGR);
			// aderenciaGR = round(aderenciaGR,2);

			// Se aderencia maior que 100%, força o valor
			if (aderenciaGR > 100) {
				aderenciaGR = 100.0;
			}
			// Se aderência menor que 0%, força o valor
			else if (aderenciaGR < 0) {
				aderenciaGR = 0.0;
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
				}
			}
			if(teste.equals("N")) {
				gap.setPoNTUACAOPERFIL(0);
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


				int gapdePARA = 0;
				
				if(listCARGOSPara.get(i).getIdPERFIL().getBloqueiaMovConhecPerfil().equals("SIM") &&
						(listCARGOSPara.get(i).getPoNTUACAOPERFIL() >=1) && 
						(listDe.get(i).getPoNTUACAOPERFIL()==0)
						
						) {
					flagDowngrade = true;
				}
				
				if(listCARGOSPara.get(i).getPoNTUACAOPERFIL() == listDe.get(i).getPoNTUACAOPERFIL()) {
					gapDeParaPE = 0;
				}else {
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
			aderenciaPE =  100 -somaListaGapPE;
			//aderenciaPE = round(aderenciaPE,2);

			//Se aderencia maior que 100%, força o valor
			if(aderenciaPE > 100) {
				aderenciaPE =100.0;
			}
			//Se aderência menor que 0%, força o valor 
			else if (aderenciaPE <0) {
				aderenciaPE =0.0;
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

	public List<Long> getListaDe() {

		return listaDe;
	}

	public void setListaDe(List<Long> listaDe) {
		this.listaDe = listaDe;
	}

	public List<ArrayList<ArrayList<TbMATRIZCARGOSEntity>>> getArrlist10() {
		return arrlist10;
	}




	public void setArrlist10(List<ArrayList<ArrayList<TbMATRIZCARGOSEntity>>> arrlist10) {
		this.arrlist10 = arrlist10;
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


}
