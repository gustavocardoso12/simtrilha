package org.applicationn.simtrilhas.web;

import java.io.Serializable;
import java.util.ArrayList;
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
@SessionScoped
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

	private List<TbCONHECIMENTOSBASICOSEntity> listCB = new ArrayList<TbCONHECIMENTOSBASICOSEntity>();

	List<TbCONHECIMENTOSESPECIFICOSEntity> listCE = new ArrayList<TbCONHECIMENTOSESPECIFICOSEntity>();

	List<TbCOMPETENCIASEntity> listCO = new ArrayList<TbCOMPETENCIASEntity>();
	
	List<TbPERFILEntity> listPE = new ArrayList<TbPERFILEntity>();

	List<TbGRADEEntity> listGR = new ArrayList<TbGRADEEntity>();

	List<TbMATRIZCARGOSEntity> matrizCargos = new ArrayList<TbMATRIZCARGOSEntity>();
	
	private String flagEstaganado;

	private ArrayList<ArrayList<ArrayList<TbMATRIZCARGOSEntity>>> data3D = new ArrayList<ArrayList<ArrayList<TbMATRIZCARGOSEntity>>>();

	private String porcentagemFinal = "teste";
	

	public TbMATRIZCARGOSBean() {

	}




	@PostConstruct
	public void initizalite(){
		// Setup 3 dimensional structure


		try {

			listaDe = tbMATRIZCARGOSService.findDistinctDe();
			List<Object[]> result = new ArrayList<Object[]>();
			listaCargos = tbCARGOSService.findAllTbCARGOSEntities();
			result = tbMATRIZCARGOSService.findTbMATRIZCARGOSEntities();
			int k=0, l =0;

			for(k = 0; k<listaDe.size();k++) {
				data3D.add(new ArrayList<ArrayList<TbMATRIZCARGOSEntity>>());

				for(l=0;l<listaDe.size();l++) {
					data3D.get(k).add(new ArrayList<TbMATRIZCARGOSEntity>());
				}
			}
			if(result.size()==0) {

			}else {


				for (Object[] res : result) {


					TbMATRIZCARGOSEntity r = new TbMATRIZCARGOSEntity();
					r.setCorAderencia((String) res[0]);
					r.setAdERENCIAFINAL((double) res[1]);
					long id_de = ((long) res[2] - 1);
					long id_para = ((long) res[3] - 1);

					int id_fde = Math.toIntExact(id_de);
					int id_fpara = Math.toIntExact(id_para);

					data3D.get(id_fde).get(id_fpara).add(r);

					System.out.println("Interação I:" +id_fde +
							"Interação J: " +id_fpara);


				}
			}

		}catch (Exception ex) {

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

		
		
		
		listConhecimentosBasicos = tbCONHECIMENTOSBASCARGOSService.findAllTbCONHECIMENTOSBASCARGOSEntities();
		listCB = tbCONHECIMENTOSBASICOSService.findAllTbCONHECIMENTOSBASICOSEntities();

		listConhecimentosEspecificos = tbCONHECIMENTOSESPCARGOSService.findAllTbCONHECIMENTOSESPCARGOSEntities();
		listCE = tbCONHECIMENTOSESPECIFICOSService.findAllTbCONHECIMENTOSESPECIFICOSEntities();

		listCompetencia = tbCOMPETENCIASCARGOSService.findAllTbCOMPETENCIASCARGOSEntities();
		listCO = tbCOMPETENCIASService.findAllTbCOMPETENCIASEntities();

		listGrade = tbGRADESCARGOSService.findAllTbGRADECARGOSEntities();
		listGR = tbGRADEService.findAllTbGRADEEntities();
		
		listPerfil = tbPERFILCARGOSService.findAllTbPERFILCARGOSEntities();
		listPE = tbPERFILService.findAllTbPERFILEntities();

		listaCargos = tbCARGOSService.findFiveTbCARGOSEntities();
		MatrizPrincipal = tbMATRIZCARGOSService.findAllTbMATRIZCARGOSEntities();

	

		porcentagemFinal="Deletando matriz antiga...";
		System.out.println("Deletando matriz antiga...");
		/*for(int k=0; k<MatrizPrincipal.size();k++) {
			tbMATRIZCARGOSService.delete(MatrizPrincipal.get(k));
		}

		MatrizPrincipal.clear();*/
		
		tbMATRIZCARGOSService.truncate();

		double porcentagem =  100.0 / listaCargos.size() ;

		for (int i=0; i<listaCargos.size();i++) {

			for(int j=0; j<listaCargos.size();j++) {
				TbMATRIZCARGOSEntity matrizItem = new TbMATRIZCARGOSEntity(); 
				cargoDe = listaCargos.get(i);
				cargoPara = listaCargos.get(j);
				
				if(cargoDe.getId()==cargoPara.getId()) {
					matrizItem.setAdERENCIAFINAL(100.0);
					aderenciaFinal = 100.0;
					matrizItem.setCorAderencia("#16600A");
					matrizItem.setIdCARGODE(cargoDe.getId());
					matrizItem.setIdCARGOPARA(cargoPara.getId());
					flagDowngrade = false;
				}else {
					calculaGRADESMatriz(cargoDe, cargoPara);
					if(flagDowngrade==true) {

					}else {

						calculaConhecimentosBasicosMatriz(cargoDe, cargoPara);

						calculaConhecimentosEspecificosMatriz(cargoDe, cargoPara);

						calculaCompetenciasMatriz(cargoDe, cargoPara);
						
						calculaPerfilMatriz(cargoDe, cargoPara);
						
						calculaAderenciaFinal(cargoDe, cargoPara);

						matrizItem.setIdCARGODE(cargoDe.getId());
						matrizItem.setIdCARGOPARA(cargoPara.getId());
						matrizItem.setAdERENCIAFINAL(aderenciaFinal);
						matrizItem.setCorAderencia(corFinal);
						matrizItem.setEstaganado(flagEstaganado);
					}



				}
				if((aderenciaFinal==0.0) || (flagDowngrade==true)) {

				}else {
					matrizItem = tbMATRIZCARGOSService.save(matrizItem);
				}
				System.out.println("Interação I:" + i + "\n" 
						+  "Interação J:" + j + "\n"	);
				System.out.println(porcentagem * (i+1));
				
				porcentagemFinal = ""+porcentagem * (i+1) + "%";
			}





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
			TbCARGOSEntity tbCARGOSPara) {

		List<TbCONHECIMENTOSESPCARGOSEntity> listCARGOSDe = new ArrayList<TbCONHECIMENTOSESPCARGOSEntity>();
		List<TbCONHECIMENTOSESPCARGOSEntity> listCARGOSPara = new ArrayList<TbCONHECIMENTOSESPCARGOSEntity>();
		for (int k =0 ; k<listConhecimentosEspecificos.size();k++) {
			if(listConhecimentosEspecificos.get(k).getIdCARGOS().getDeSCCARGO().equals(tbCARGOSDe.getDeSCCARGO())) {
				listCARGOSDe.add(listConhecimentosEspecificos.get(k));

			}
			if(listCARGOSDe.size()==listCE.size()) {
				break;
			}

		}
		
		for (int k =0 ; k<listConhecimentosEspecificos.size();k++) {

			if(listConhecimentosEspecificos.get(k).getIdCARGOS().getDeSCCARGO().equals(tbCARGOSPara.getDeSCCARGO())) {
				listCARGOSPara.add(listConhecimentosEspecificos.get(k));
			}
			if(listCARGOSPara.size()==listCE.size()) {
				break;
			}

		}
		
		
		
		listGapCE.clear();
		Double somaListaGap =0.0;
		flagCultura = 1;

		try {
			for(int i=0; i<listCARGOSDe.size();i++) {
				TbCONHECIMENTOSESPCARGOSEntity gap = new TbCONHECIMENTOSESPCARGOSEntity();
				//Calcula a subtração entre cada pontuação de cada competência

				int gapdePARA = 0;

				if(listCARGOSPara.get(i).getPoNTUACAOCONESP() == listCARGOSDe.get(i).getPoNTUACAOCONESP()){

				}else {
					//Se "(Cargo Para - Cargo De)" < 0 então  "(Cargo Para - Cargo De)" = "0"
					if(listCARGOSPara.get(i).getPoNTUACAOCONESP() < listCARGOSDe.get(i).getPoNTUACAOCONESP()){

					}else {

						gapdePARA =(listCARGOSPara.get(i).getPoNTUACAOCONESP() - 
								listCARGOSDe.get(i).getPoNTUACAOCONESP())
								* listCARGOSDe.get(i).getIdCONHECESP().getPenalidadeConhecBas();
						if((gapdePARA==100.0) && (listCARGOSDe.get(i).getIdCONHECESP().getBloqueiaMovConhecEsp().equals("S"))) {
							flagDowngrade = true;
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
		List<TbCONHECIMENTOSBASCARGOSEntity> listCARGOSDe = new ArrayList<TbCONHECIMENTOSBASCARGOSEntity>();
		List<TbCONHECIMENTOSBASCARGOSEntity> listCARGOSPara = new ArrayList<TbCONHECIMENTOSBASCARGOSEntity>();
		for (int k =0 ; k<listConhecimentosBasicos.size();k++) {
			if(listConhecimentosBasicos.get(k).getIdCARGOS().getDeSCCARGO().equals(tbCARGOSDe.getDeSCCARGO())) {
				listCARGOSDe.add(listConhecimentosBasicos.get(k));

			}
			if(listCARGOSDe.size()==listCB.size()) {
				break;
			}

		}
		
		for (int k =0 ; k<listConhecimentosBasicos.size();k++) {
			if(listConhecimentosBasicos.get(k).getIdCARGOS().getDeSCCARGO().equals(tbCARGOSPara.getDeSCCARGO())) {
				listCARGOSPara.add(listConhecimentosBasicos.get(k));
			}

			if(listCARGOSPara.size()==listCB.size()) {
				break;
			}

		}
		
	



		listGapCB.clear();
		Double somaListaGap =0.0;

		try {
			for(int i=0; i<listCARGOSDe.size();i++) {
				TbCONHECIMENTOSBASCARGOSEntity gap = new TbCONHECIMENTOSBASCARGOSEntity();
				//Calcula a subtração entre cada pontuação de cada competência
				int gapdePARA =0;
				if(listCARGOSPara.get(i).getPoNTUACAOCONBAS() == listCARGOSDe.get(i).getPoNTUACAOCONBAS()){

				}else {
					if(listCARGOSPara.get(i).getPoNTUACAOCONBAS() < listCARGOSDe.get(i).getPoNTUACAOCONBAS()){

					}else {

						gapdePARA =(listCARGOSPara.get(i).getPoNTUACAOCONBAS() - 
								listCARGOSDe.get(i).getPoNTUACAOCONBAS())
								* listCARGOSDe.get(i).getIdCONHECBAS().getPenalidadeConhecBas();
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

		List<TbCOMPETENCIASCARGOSEntity> listCARGOSDe = new ArrayList<TbCOMPETENCIASCARGOSEntity>();
		List<TbCOMPETENCIASCARGOSEntity> listCARGOSPara = new ArrayList<TbCOMPETENCIASCARGOSEntity>();
		for (int k =0 ; k<listCompetencia.size();k++) {
			if(listCompetencia.get(k).getIdCARGOS().getDeSCCARGO().equals(tbCARGOSDe.getDeSCCARGO())) {
				listCARGOSDe.add(listCompetencia.get(k));
			}
			if(listCARGOSDe.size()==listCO.size()) {
				break;
			}

		}
		
		for (int k =0 ; k<listCompetencia.size();k++) {
			if(listCompetencia.get(k).getIdCARGOS().getDeSCCARGO().equals(tbCARGOSPara.getDeSCCARGO())) {
				listCARGOSPara.add(listCompetencia.get(k));
			}
			if(listCARGOSPara.size()==listCO.size()) {
				break;
			}

		}
		

		listGap.clear();
		Double somaListaGap =0.0;

		flagCultura = 1;
		try {
			for(int i=0; i<listCARGOSDe.size();i++) {
				TbCOMPETENCIASCARGOSEntity gap = new TbCOMPETENCIASCARGOSEntity();
				//Calcula a subtração entre cada pontuação de cada competência

				gapDePara= listCARGOSPara.get(i).getPoNTUACAOCOMPETENCIA()
						-  listCARGOSDe.get(i).getPoNTUACAOCOMPETENCIA();


				//Se "(Cargo Para - Cargo De)" < 0 então  "(Cargo Para - Cargo De)" = "(Cargo Para - Cargo De) / 2"
				if(gapDePara<0) {
					gapDePara = 0;
				}
				int gapdePARA = 0;

				// gap = (Cargo Para - Cargo De) * penalidade
				if (gapDePara==0) {

				}else {
					gapdePARA = gapDePara* listCARGOSDe.get(i).getIdCOMPETENCIAS().getPenalidadeCompetencias();
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



	public void calculaGRADESMatriz(TbCARGOSEntity tbCARGOSDe,
			TbCARGOSEntity tbCARGOSPara) {
		flagEstaganado="";
		aderenciaGR = 0.0;
		List<TbGRADECARGOSEntity> listCARGOSDe = new ArrayList<TbGRADECARGOSEntity>();
		List<TbGRADECARGOSEntity> listCARGOSPara = new ArrayList<TbGRADECARGOSEntity>();
		for (int k =0 ; k<listGrade.size();k++) {
			if(listGrade.get(k).getIdCARGOS().getDeSCCARGO().equals(tbCARGOSDe.getDeSCCARGO())) {
				listCARGOSDe.add(listGrade.get(k));
			}
			
			if(listCARGOSDe.size()==listGR.size()) {
				break;
			}
			
		}
		
		for (int l =0 ; l<listGrade.size();l++) {
			if(listGrade.get(l).getIdCARGOS().getDeSCCARGO().equals(tbCARGOSPara.getDeSCCARGO())) {
				listCARGOSPara.add(listGrade.get(l));
			}
			
			if(listCARGOSPara.size()==listGR.size()) {
				break;
			}
			
		}
		
		if(listCARGOSPara.size()==0) {
			System.out.println("ERRO: ");
			System.out.println(tbCARGOSPara.getId());
		}

		listGapGR.clear();
		Double somaListaGapGR =0.0;
		flagDowngrade = false;
		flagCultura = 1;

		try {
			for(int i=0; i<listCARGOSDe.size();i++) {
				TbGRADECARGOSEntity gap = new TbGRADECARGOSEntity();
				int gapdePARA = 0;
				if(listCARGOSPara.get(i).getPoNTUACAOGRADE() < listCARGOSDe.get(i).getPoNTUACAOGRADE() ) {
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
				}else if (listCARGOSPara.get(i).getPoNTUACAOGRADE() == listCARGOSDe.get(i).getPoNTUACAOGRADE()) {
					flagEstaganado = "True";
				}else {
					// gap = (Cargo Para - Cargo De) * penalidade
					gapdePARA = (listCARGOSPara.get(i).getPoNTUACAOGRADE()-
							listCARGOSDe.get(i).getPoNTUACAOGRADE())
							* listCARGOSDe.get(i).getIdGRADE().getPenalidadeConhecGrade();				
					
				}
				gap.setPoNTUACAOGRADE(gapdePARA);
				listGapGR.add(gap);
				somaListaGapGR+=gapdePARA;

			}


			//calculo de aderência
			aderenciaGR =  100 -(somaListaGapGR);
			//aderenciaGR = round(aderenciaGR,2);

			//Se aderencia maior que 100%, força o valor
			if(aderenciaGR > 100) {
				aderenciaGR =100.0;
			}
			//Se aderência menor que 0%, força o valor 
			else if (aderenciaGR <0) {
				aderenciaGR =0.0;
			}
		}catch(IndexOutOfBoundsException ex) {

		}
	}
	
	
	public void calculaPerfilMatriz(TbCARGOSEntity tbCARGOSDe,
			TbCARGOSEntity tbCARGOSPara) {

		List<TbPERFILCARGOSEntity> listCARGOSDe = new ArrayList<TbPERFILCARGOSEntity>();
		List<TbPERFILCARGOSEntity> listCARGOSPara = new ArrayList<TbPERFILCARGOSEntity>();
		for (int k =0 ; k<listPerfil.size();k++) {
			if(listPerfil.get(k).getIdCARGOS().getDeSCCARGO().equals(tbCARGOSDe.getDeSCCARGO())) {
				listCARGOSDe.add(listPerfil.get(k));

			}
			if(listCARGOSDe.size()==listPE.size()) {
				break;
			}

		}
		for (int k =0 ; k<listPerfil.size();k++) {

			if(listPerfil.get(k).getIdCARGOS().getDeSCCARGO().equals(tbCARGOSPara.getDeSCCARGO())) {
				listCARGOSPara.add(listPerfil.get(k));
			}
			if(listCARGOSPara.size()==listPE.size()) {
				break;
			}

		}
		
		


		listGapPE.clear();
		Double somaListaGapPE =0.0;
		flagCultura = 1;

		try {
			for(int i=0; i<listCARGOSDe.size();i++) {
				TbPERFILCARGOSEntity gap = new TbPERFILCARGOSEntity();
				//Calcula a subtração entre cada pontuação de cada perfil


				int gapdePARA = 0;

				if(listCARGOSPara.get(i).getPoNTUACAOPERFIL() == listCARGOSDe.get(i).getPoNTUACAOPERFIL()) {
					gapDeParaPE = 0;
				}else {
					//(Módulo(Cargo Para - Cargo De)
					gapDeParaPE= listCARGOSPara.get(i).getPoNTUACAOPERFIL() - listCARGOSDe.get(i).getPoNTUACAOPERFIL();

					// gap = (Módulo(Cargo Para - Cargo De) - 4) 
					gapDeParaPE = Math.abs(gapDeParaPE) - 4;

					//if (Módulo(Cargo Para - Cargo De) - 4)  < 0 Then Gap = 0
					if(gapDeParaPE< 0) {
						gapDeParaPE = 0;
					}

				}

				// gap = (Módulo(Cargo Para - Cargo De) - 4) * penalidade

				if(gapDeParaPE==0) {

				}else {
					gapdePARA = gapDeParaPE*listCARGOSDe.get(i).getIdPERFIL().getPenalidadeConhecPerfil();
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


}
