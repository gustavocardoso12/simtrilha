package org.applicationn.simtrilhas.web;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceException;

import org.applicationn.simtrilhas.domain.TbCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbCOMPETENCIASCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbCOMPETENCIASEMCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbCOMPETENCIASEntity;
import org.applicationn.simtrilhas.domain.TbCONHECIMENTOSBASCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbCONHECIMENTOSESPCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbCURSOSEntity;
import org.applicationn.simtrilhas.domain.TbDEPTOEntity;
import org.applicationn.simtrilhas.domain.TbNOEntity;
import org.applicationn.simtrilhas.domain.TbDIRETORIAEntity;
import org.applicationn.simtrilhas.domain.TbESTATUARIOEntity;
import org.applicationn.simtrilhas.domain.TbESTILOLIDERANCACARGOSEntity;
import org.applicationn.simtrilhas.domain.TbESTILOLIDERANCAEntity;
import org.applicationn.simtrilhas.domain.TbESTILOPENSAMENTOCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbGRADECARGOSEntity;
import org.applicationn.simtrilhas.domain.TbGRADEEntity;
import org.applicationn.simtrilhas.domain.TbHABILIDADESCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbHABILIDADESCULTCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbHABILIDADESCULTURAISEntity;
import org.applicationn.simtrilhas.domain.TbMOTIVADORESCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbPERFILCARGOSEntity;
import org.applicationn.simtrilhas.service.TbCARGOSService;
import org.applicationn.simtrilhas.service.TbCOMPETENCIASCARGOSService;
import org.applicationn.simtrilhas.service.TbCOMPETENCIASEMCARGOSService;
import org.applicationn.simtrilhas.service.TbCONHECIMENTOSBASCARGOSService;
import org.applicationn.simtrilhas.service.TbCONHECIMENTOSESPCARGOSService;
import org.applicationn.simtrilhas.service.TbCURSOSService;
import org.applicationn.simtrilhas.service.TbDEPTOService;
import org.applicationn.simtrilhas.service.TbNOService;
import org.applicationn.simtrilhas.service.TbDIRETORIAService;
import org.applicationn.simtrilhas.service.TbESTATUARIOService;
import org.applicationn.simtrilhas.service.TbESTILOLIDERANCACARGOSService;
import org.applicationn.simtrilhas.service.TbESTILOLIDERANCAService;
import org.applicationn.simtrilhas.service.TbESTILOPENSAMENTOCARGOSService;
import org.applicationn.simtrilhas.service.TbGRADECARGOSService;
import org.applicationn.simtrilhas.service.TbGRADEService;
import org.applicationn.simtrilhas.service.TbHABILIDADESCARGOSService;
import org.applicationn.simtrilhas.service.TbHABILIDADESCULTCARGOSService;
import org.applicationn.simtrilhas.service.TbHABILIDADESCULTURAISService;
import org.applicationn.simtrilhas.service.TbMOTIVADORESCARGOSService;
import org.applicationn.simtrilhas.service.TbPERFILCARGOSService;
import org.applicationn.simtrilhas.service.security.SecurityWrapper;
import org.applicationn.simtrilhas.web.util.MessageFactory;
import org.primefaces.event.SlideEndEvent;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;

@Named("tbCARGOSBean")
@ViewScoped
public class TbCARGOSBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = Logger.getLogger(TbCARGOSBean.class.getName());

	private List<TbCARGOSEntity> tbCARGOSList;

	private List<TbCARGOSEntity> tbCARGOSListFiltrada = new ArrayList<TbCARGOSEntity>();

	private TbCARGOSEntity tbCARGOS;

	@Inject
	private TbCARGOSService tbCARGOSService;

	@Inject
	private TbESTATUARIOService tbESTATUARIOService;

	@Inject
	private TbCURSOSService tbCURSOSService;

	@Inject
	private TbDIRETORIAService tbDIRETORIAService;

	@Inject
	private TbCOMPETENCIASCARGOSService tbCOMPETENCIASCARGOSService;

	@Inject
	private TbCOMPETENCIASEMCARGOSService tbCOMPETENCIASEMCARGOSService;

	@Inject
	private TbHABILIDADESCULTURAISService tbHABILIDADESCULTURAISService;

	@Inject
	private TbCONHECIMENTOSBASCARGOSService tbCONHECIMENTOSBASCARGOSService;

	@Inject
	private TbCONHECIMENTOSESPCARGOSService tbCONHECIMENTOSESPCARGOSService;

	@Inject
	private TbESTILOLIDERANCACARGOSService tbESTILOLIDERANCACARGOSService;

	@Inject
	private TbESTILOPENSAMENTOCARGOSService tbESTILOPENSAMENTOCARGOSService;

	@Inject
	private TbHABILIDADESCARGOSService tbHABILIDADESCARGOSService;

	@Inject
	private TbHABILIDADESCULTCARGOSService tbHABILIDADESCULTCARGOSService;
	
	@Inject
	private TbDEPTOService tbDEPTOSerivce;
	
	@Inject 
	private TbNOService tbNOService;
	
	@Inject
	private TbGRADECARGOSService tbGRADESCARGOSService;
	
	@Inject
	private TbPERFILCARGOSService tbPERFILCARGOSService;
	
	@Inject 
	private TbMOTIVADORESCARGOSService tbMOTIVADORESCARGOSService;


	protected List<TbHABILIDADESCARGOSEntity> tbHABILIDADESCARGOSs;

	protected List<TbHABILIDADESCULTCARGOSEntity> tbHABILIDADESCULTCARGOSs;

	protected List<TbESTILOLIDERANCACARGOSEntity> tbESTILOLIDERANCAs;

	protected List<TbESTILOPENSAMENTOCARGOSEntity> tbESTILOPENSAMENTOs;

	protected List<TbCOMPETENCIASEMCARGOSEntity> tbCOMPETENCIASEMs;

	protected List<TbCOMPETENCIASCARGOSEntity> tbCOMPETENCIASCARGOSs;

	protected List<TbCONHECIMENTOSBASCARGOSEntity> tbCONHECIMENTOSBASICOSCARGOSs;

	protected List<TbCONHECIMENTOSESPCARGOSEntity>  tbCONHECIMENTOSESPCARGOSs;
	
	protected List<TbGRADECARGOSEntity> tbGRADESCARGOSs;
	
	protected List<TbPERFILCARGOSEntity> tbPERFILCARGOSs;
	
	protected List<TbMOTIVADORESCARGOSEntity> tbMOTIVADORESCARGOSs;

	private List<TbESTATUARIOEntity> allIdESTsList;
	
	private List<TbDEPTOEntity> allIdDEPTOsList;
	
	private List<TbNOEntity> allIdNOsList;

	private List<TbCURSOSEntity> allIdCURSOSsList;

	private List<TbDIRETORIAEntity> allIdDIRETORIAsList;

	private List<TbHABILIDADESCULTURAISEntity> allIdHABCULTCARsList = new ArrayList<TbHABILIDADESCULTURAISEntity>();

	private List<TbCARGOSEntity> allIdCARGOSsList = new ArrayList<TbCARGOSEntity>();

	protected TbCARGOSEntity selectedCargo;

	private String dialogHeader;

	private String detalhesComportamento;

	private TbCARGOSEntity cargoDe;

	private TbCARGOSEntity cargoPara;

	private BigDecimal gapDePara;

	private Double gapVar=7.5;

	private Double aderencia;

	List<TbCOMPETENCIASCARGOSEntity> listGap = new ArrayList<TbCOMPETENCIASCARGOSEntity>();

	private BigDecimal gapDeParaGR;

	private Double gapVarGR = 25.0; 

	private Double aderenciaGR;

	List<TbGRADECARGOSEntity> listGapGR = new ArrayList<TbGRADECARGOSEntity>();
	
	private BigDecimal gapDeParaMO;
	
	private Double gapVarMO;
	
	private Double aderenciaMO;
	
	List<TbMOTIVADORESCARGOSEntity> listGapMO = new ArrayList<TbMOTIVADORESCARGOSEntity>();
	
	private BigDecimal gapDeParaPE;
	
	private Double gapVarPE = 10.0;
	
	private Double aderenciaPE;
	
	List<TbPERFILCARGOSEntity> listGapPE = new ArrayList<TbPERFILCARGOSEntity>();
	
	private BigDecimal gapDeParaCB;
	
	private Double gapVarCB = 2.0;
	
	private Double aderenciaCB;
	
	List<TbCONHECIMENTOSBASCARGOSEntity> listGapCB = new ArrayList<TbCONHECIMENTOSBASCARGOSEntity>();
	
	private BigDecimal gapDeParaCE;
	
	private Double gapVarCE = 5.0;
	
	private Double aderenciaCE;
	
	List<TbCONHECIMENTOSESPCARGOSEntity> listGapCE = new ArrayList<TbCONHECIMENTOSESPCARGOSEntity>();
	
	
	
	
	private Double aderenciaFinal;
	

	private String avisoMovimentacao;
	

	public String getDetalhesComportamento() {
		return detalhesComportamento;
	}

	public void setDetalhesComportamento(String detalhesComportamento) {
		this.detalhesComportamento = detalhesComportamento;
	}

	public void setDialogHeader(final String dialogHeader) { 
		this.dialogHeader = dialogHeader;
	}

	public String getDialogHeader() {
		return dialogHeader;
	}

	public void changeHeaderCadastrar() {
		setDialogHeader("Cadastrar Cargos");
	}

	public void changeHeaderEditar() {
		setDialogHeader("Editar Cargos");
	}


	private int flagCultura = 0;



	public void InicializaCargos(TbCARGOSEntity tbCARGOS) {

		setFlagCultura(1);
		flagCultura = 1;
		this.tbCARGOS = tbCARGOS;
		tbCARGOSListFiltrada.clear();
		tbCARGOSListFiltrada.add(this.tbCARGOS);
	}

	public void prepareNewTbCARGOS() {
		reset();
		changeHeaderCadastrar();
		this.tbCARGOS = new TbCARGOSEntity();
	}


	public List<TbHABILIDADESCULTCARGOSEntity> InicializaTabelasAuxiliares(TbCARGOSEntity tbCARGOS){
		this.tbCARGOS = tbCARGOS;

		tbHABILIDADESCULTCARGOSs = tbHABILIDADESCULTCARGOSService
				.findTbHABILIDADESCULTCARGOSsByIdCARGOS(this.tbCARGOS);


		return tbHABILIDADESCULTCARGOSs;
	}



	public List<TbHABILIDADESCARGOSEntity> InicializaTabelasAuxiliaresHABAPR(TbCARGOSEntity tbCARGOS){
		this.tbCARGOS = tbCARGOS;
		tbHABILIDADESCARGOSs = tbHABILIDADESCARGOSService.findTbHABILIDADESCARGOSsByIdCARGOS(this.tbCARGOS);

		return tbHABILIDADESCARGOSs;
	}

	


	public List<TbESTILOLIDERANCACARGOSEntity> InicializaTabelasAuxiliaresEST(TbCARGOSEntity tbCARGOS){
		this.tbCARGOS = tbCARGOS;
		tbESTILOLIDERANCAs = tbESTILOLIDERANCACARGOSService.findTbESTILOLIDERANCACARGOSsByIdCARGOS(this.tbCARGOS);

		return tbESTILOLIDERANCAs;
	}

	public List<TbESTILOPENSAMENTOCARGOSEntity> InicializaTabelasAuxiliaresELI(TbCARGOSEntity tbCARGOS){
		this.tbCARGOS = tbCARGOS;
		tbESTILOPENSAMENTOs = tbESTILOPENSAMENTOCARGOSService.findTbESTILOPENSAMENTOCARGOSsByIdCARGOS(this.tbCARGOS);

		return tbESTILOPENSAMENTOs;
	}

	public List<TbCOMPETENCIASEMCARGOSEntity> InicializaTabelasAuxiliaresEM(TbCARGOSEntity tbCARGOS){
		this.tbCARGOS = tbCARGOS;
		tbCOMPETENCIASEMs = tbCOMPETENCIASEMCARGOSService.findTbCOMPETENCIASEMCARGOSsByIdCARGOS(this.tbCARGOS);

		return tbCOMPETENCIASEMs;
	}

	public List<TbCOMPETENCIASCARGOSEntity> InicializaTabelasAuxiliaresCO(TbCARGOSEntity tbCARGOS){
		this.tbCARGOS = tbCARGOS;
		tbCOMPETENCIASCARGOSs= tbCOMPETENCIASCARGOSService.findTbCOMPETENCIASCARGOSsByIdCARGOS(this.tbCARGOS);
		tbCOMPETENCIASCARGOSs = ordenaListas(tbCOMPETENCIASCARGOSs);
		return tbCOMPETENCIASCARGOSs;
	}

	public List<TbGRADECARGOSEntity> InicializaTabelasAuxiliaresGR(TbCARGOSEntity tbCARGOS){
		this.tbCARGOS = tbCARGOS;
		tbGRADESCARGOSs= tbGRADESCARGOSService.findTbGRADECARGOSsByIdCARGOS(this.tbCARGOS);

		return tbGRADESCARGOSs;
	}

	public List<TbPERFILCARGOSEntity> InicializaTabelasAuxiliaresPE(TbCARGOSEntity tbCARGOS){
		this.tbCARGOS = tbCARGOS;
		tbPERFILCARGOSs= tbPERFILCARGOSService.findTbPERFILCARGOSsByIdCARGOS(this.tbCARGOS);

		return tbPERFILCARGOSs;
	}
	
	public List<TbMOTIVADORESCARGOSEntity> InicializaTabelasAuxiliaresMO(TbCARGOSEntity tbCARGOS){
		this.tbCARGOS = tbCARGOS;
		tbMOTIVADORESCARGOSs= tbMOTIVADORESCARGOSService.findTbMOTIVADORESCARGOSsByIdCARGOS(this.tbCARGOS);

		return tbMOTIVADORESCARGOSs;
	}

	public List<TbCONHECIMENTOSBASCARGOSEntity> InicializaTabelasAuxiliaresCB(TbCARGOSEntity tbCARGOS){
		this.tbCARGOS = tbCARGOS;
		tbCONHECIMENTOSBASICOSCARGOSs= tbCONHECIMENTOSBASCARGOSService.findTbCONHECIMENTOSBASCARGOSsByIdCARGOS(this.tbCARGOS);

		return tbCONHECIMENTOSBASICOSCARGOSs;
	}
	

	public List<TbCONHECIMENTOSESPCARGOSEntity> InicializaTabelasAuxiliaresCE(TbCARGOSEntity tbCARGOS){
		this.tbCARGOS = tbCARGOS;
		tbCONHECIMENTOSESPCARGOSs= tbCONHECIMENTOSESPCARGOSService.findTbCONHECIMENTOSESPCARGOSsByIdCARGOS(this.tbCARGOS);

		return tbCONHECIMENTOSESPCARGOSs;
	}

	
	public List<TbCOMPETENCIASCARGOSEntity> ordenaListas(List<TbCOMPETENCIASCARGOSEntity> list){
		Collections.sort(list, new Comparator<TbCOMPETENCIASCARGOSEntity>() {
		    @Override
		    public int compare(TbCOMPETENCIASCARGOSEntity b1, TbCOMPETENCIASCARGOSEntity b2) {
		       int x =  b1.getIdCOMPETENCIAS().getDeSCCOMPETENCIA().compareTo(b2.getIdCOMPETENCIAS().getDeSCCOMPETENCIA());
		       return x;
		    }
		});
		return list;
	}
	
	public List<TbGRADECARGOSEntity> ordenaListasGR(List<TbGRADECARGOSEntity> list){
		Collections.sort(list, new Comparator<TbGRADECARGOSEntity>() {
		    @Override
		    public int compare(TbGRADECARGOSEntity b1, TbGRADECARGOSEntity b2) {
		       int x =  b1.getIdGRADE().getDeSCGRADE().compareTo(b2.getIdGRADE().getDeSCGRADE());
		       return x;
		    }
		});
		return list;
	}
	

	public List<TbMOTIVADORESCARGOSEntity> ordenaListasMO(List<TbMOTIVADORESCARGOSEntity> list){
		Collections.sort(list, new Comparator<TbMOTIVADORESCARGOSEntity>() {
		    @Override
		    public int compare(TbMOTIVADORESCARGOSEntity b1, TbMOTIVADORESCARGOSEntity b2) {
		       int x =  b1.getIdMOTIVADORES().getDeSCMOTIVADORES().compareTo(b2.getIdMOTIVADORES().getDeSCMOTIVADORES());
		       return x;
		    }
		});
		return list;
	}
	
	public List<TbPERFILCARGOSEntity> ordenaListasPE(List<TbPERFILCARGOSEntity> list){
		Collections.sort(list, new Comparator<TbPERFILCARGOSEntity>() {
		    @Override
		    public int compare(TbPERFILCARGOSEntity b1, TbPERFILCARGOSEntity b2) {
		       int x =  b1.getIdPERFIL().getDeSCPERFIL().compareTo(b2.getIdPERFIL().getDeSCPERFIL());
		       return x;
		    }
		});
		return list;
	}
	
	public List<TbCONHECIMENTOSBASCARGOSEntity> ordenaListasCB(List<TbCONHECIMENTOSBASCARGOSEntity> list){
		Collections.sort(list, new Comparator<TbCONHECIMENTOSBASCARGOSEntity>() {
		    @Override
		    public int compare(TbCONHECIMENTOSBASCARGOSEntity b1, TbCONHECIMENTOSBASCARGOSEntity b2) {
		       int x =  b1.getIdCONHECBAS().getDeSCCONHECIMENTOSBASICOS().compareTo(
		    		    b2.getIdCONHECBAS().getDeSCCONHECIMENTOSBASICOS());
		       return x;
		    }
		});
		return list;
	}
	
	public List<TbCONHECIMENTOSESPCARGOSEntity> ordenaListasCE(List<TbCONHECIMENTOSESPCARGOSEntity> list){
		Collections.sort(list, new Comparator<TbCONHECIMENTOSESPCARGOSEntity>() {
		    @Override
		    public int compare(TbCONHECIMENTOSESPCARGOSEntity b1, TbCONHECIMENTOSESPCARGOSEntity b2) {
		       int x =  b1.getIdCONHECESP().getDeSCCONHECIMENTOSESPECIFICOS().compareTo(
		    		    b2.getIdCONHECESP().getDeSCCONHECIMENTOSESPECIFICOS());
		       return x;
		    }
		});
		return list;
	}
	
	
	//100%  - ((Cargo Para - Cargo De) * Variação no GAP)
			public void calculaConhecimentosEspecificosDEPARA(TbCARGOSEntity tbCARGOSDe,
					TbCARGOSEntity tbCARGOSPara, double gapVar) {
				gapVar = gapVar/100;
				List<TbCONHECIMENTOSESPCARGOSEntity> listCARGOSDe = InicializaTabelasAuxiliaresCE(tbCARGOSDe);
				List<TbCONHECIMENTOSESPCARGOSEntity> listCARGOSPara = InicializaTabelasAuxiliaresCE(tbCARGOSPara);
				listCARGOSDe = ordenaListasCE(listCARGOSDe);
				listCARGOSPara = ordenaListasCE(listCARGOSPara);
				
				
				listGapCE.clear();
				Double somaListaGap =0.0;
				flagCultura = 1;
				try {
					for(int i=0; i<listCARGOSDe.size();i++) {
						TbCONHECIMENTOSESPCARGOSEntity gap = new TbCONHECIMENTOSESPCARGOSEntity();
						//Calcula a subtração entre cada pontuação de cada competência
						if(listCARGOSDe.get(i).getIdCONHECESP().getDeSCCONHECIMENTOSESPECIFICOS().equals(
								listCARGOSPara.get(i).getIdCONHECESP().getDeSCCONHECIMENTOSESPECIFICOS())) {
							gapDeParaCE= listCARGOSPara.get(i).getPoNTUACAOCONESP().
									subtract(listCARGOSDe.get(i).getPoNTUACAOCONESP());

							
							//Se "(Cargo Para - Cargo De)" < 0 então  "(Cargo Para - Cargo De)" = 0
							if(gapDeParaCE.compareTo(BigDecimal.ZERO)<0) {
								gapDeParaCE = BigDecimal.ZERO;
							}
							
							gap.setPoNTUACAOCONESP(gapDeParaCE);
							//Adiciona cada valor à lista
							listGapCE.add(gap);
						}
					}
					//Soma todos os gaps
					for(int i2=0;i2<listGapCE.size();i2++) {
						somaListaGap += listGapCE.get(i2).getPoNTUACAOCONESP().doubleValue();
					}
					//calculo de aderência
					aderenciaCE =  1 -((somaListaGap*gapVar));
					aderenciaCE = round(aderenciaCE,2);
					
					//Se aderencia maior que 100%, força o valor
					if(aderenciaCE > 1) {
						aderenciaCE =1.0;
					}
					//Se aderência menor que 0%, força o valor 
					else if (aderenciaCE <0) {
						aderenciaCE =0.0;
					}
				}catch(IndexOutOfBoundsException ex) {

				}
			}
	
	

	//100%  - ((Cargo Para - Cargo De) * Variação no GAP)
		public void calculaConhecimentosBasicosDEPARA(TbCARGOSEntity tbCARGOSDe,
				TbCARGOSEntity tbCARGOSPara, double gapVar) {
			gapVar = gapVar/100;
			List<TbCONHECIMENTOSBASCARGOSEntity> listCARGOSDe = InicializaTabelasAuxiliaresCB(tbCARGOSDe);
			List<TbCONHECIMENTOSBASCARGOSEntity> listCARGOSPara = InicializaTabelasAuxiliaresCB(tbCARGOSPara);
			listCARGOSDe = ordenaListasCB(listCARGOSDe);
			listCARGOSPara = ordenaListasCB(listCARGOSPara);
			
			
			listGapCB.clear();
			Double somaListaGap =0.0;
			flagCultura = 1;
			try {
				for(int i=0; i<listCARGOSDe.size();i++) {
					TbCONHECIMENTOSBASCARGOSEntity gap = new TbCONHECIMENTOSBASCARGOSEntity();
					//Calcula a subtração entre cada pontuação de cada competência
					if(listCARGOSDe.get(i).getIdCONHECBAS().getDeSCCONHECIMENTOSBASICOS().equals(
							listCARGOSPara.get(i).getIdCONHECBAS().getDeSCCONHECIMENTOSBASICOS())) {
						gapDeParaCB= listCARGOSPara.get(i).getPoNTUACAOCONBAS().
								subtract(listCARGOSDe.get(i).getPoNTUACAOCONBAS());

						
						//Se "(Cargo Para - Cargo De)" < 0 então  "(Cargo Para - Cargo De)" = 0
						if(gapDeParaCB.compareTo(BigDecimal.ZERO)<0) {
							gapDeParaCB = BigDecimal.ZERO;
						}
						
						gap.setPoNTUACAOCONBAS(gapDeParaCB);
						//Adiciona cada valor à lista
						listGapCB.add(gap);
					}
				}
				//Soma todos os gaps
				for(int i2=0;i2<listGapCB.size();i2++) {
					somaListaGap += listGapCB.get(i2).getPoNTUACAOCONBAS().doubleValue();
				}
				//calculo de aderência
				aderenciaCB =  1 -((somaListaGap*gapVar));
				aderenciaCB = round(aderenciaCB,2);
				
				//Se aderencia maior que 100%, força o valor
				if(aderenciaCB > 1) {
					aderenciaCB =1.0;
				}
				//Se aderência menor que 0%, força o valor 
				else if (aderenciaCB <0) {
					aderenciaCB =0.0;
				}
			}catch(IndexOutOfBoundsException ex) {

			}
		}
	
	
	
	
	
	//100%  - ((Cargo Para - Cargo De) * Variação no GAP)
	public void calculaCompetenciasDEPARA(TbCARGOSEntity tbCARGOSDe,
			TbCARGOSEntity tbCARGOSPara, double gapVar) {
		gapVar = gapVar/100;
		List<TbCOMPETENCIASCARGOSEntity> listCARGOSDe = InicializaTabelasAuxiliaresCO(tbCARGOSDe);
		List<TbCOMPETENCIASCARGOSEntity> listCARGOSPara = InicializaTabelasAuxiliaresCO(tbCARGOSPara);
		listCARGOSDe = ordenaListas(listCARGOSDe);
		listCARGOSPara = ordenaListas(listCARGOSPara);
		
		
		listGap.clear();
		Double somaListaGap =0.0;
		flagCultura = 1;
		try {
			for(int i=0; i<listCARGOSDe.size();i++) {
				TbCOMPETENCIASCARGOSEntity gap = new TbCOMPETENCIASCARGOSEntity();
				//Calcula a subtração entre cada pontuação de cada competência
				if(listCARGOSDe.get(i).getIdCOMPETENCIAS().getDeSCCOMPETENCIA().equals(listCARGOSPara.get(i).getIdCOMPETENCIAS().getDeSCCOMPETENCIA())) {
					gapDePara= listCARGOSPara.get(i).getPoNTUACAOCOMPETENCIA().
							subtract(listCARGOSDe.get(i).getPoNTUACAOCOMPETENCIA());

					
					//Se "(Cargo Para - Cargo De)" < 0 então  "(Cargo Para - Cargo De)" = "(Cargo Para - Cargo De) / 2"
					if(gapDePara.compareTo(BigDecimal.ZERO)<0) {
						gapDePara = gapDePara.divide(BigDecimal.valueOf(2));
					}
					
					gap.setPoNTUACAOCOMPETENCIA(gapDePara);
					//Adiciona cada valor à lista
					listGap.add(gap);
				}
			}
			//Soma todos os gaps
			for(int i2=0;i2<listGap.size();i2++) {
				somaListaGap += listGap.get(i2).getPoNTUACAOCOMPETENCIA().doubleValue();
			}
			//calculo de aderência
			aderencia =  1 -((somaListaGap*gapVar));
			aderencia = round(aderencia,2);
			
			//Se aderencia maior que 100%, força o valor
			if(aderencia > 1) {
				aderencia =1.0;
			}
			//Se aderência menor que 0%, força o valor 
			else if (aderencia <0) {
				aderencia =0.0;
			}
		}catch(IndexOutOfBoundsException ex) {

		}
	}

	 public void onSlideEnd(SlideEndEvent event) {
	    gapVar =  event.getValue();
	    //System.out.println(gapVar);
	    
	    calculaCompetenciasDEPARA(cargoDe,cargoPara,gapVar);
	       
	    } 
	 
	 
	 public void onSlideEndGR(SlideEndEvent event) {
		    gapVarGR =  event.getValue();
		    System.out.println(gapVarGR);
		    
		    calculaGRADESDEPARA(cargoDe,cargoPara,gapVarGR);
		       
		    } 
	 
	 public void onSlideEndCB(SlideEndEvent event) {
		    gapVarCB =  event.getValue();
		    System.out.println(gapVarCB);
		    
		    calculaConhecimentosBasicosDEPARA(cargoDe,cargoPara, gapVarCB);
		       
		    } 

	 public void onSlideEndPE(SlideEndEvent event) {
		    gapVarPE =  event.getValue();
		    System.out.println(gapVarPE);
		    
		    calculaPerfilDEPARA(cargoDe,cargoPara, gapVarPE);
		       
		    } 
	 
	 public void onSlideEndCE(SlideEndEvent event) {
		    gapVarCE =  event.getValue();
		    System.out.println(gapVarCE);
		    
		    calculaConhecimentosEspecificosDEPARA(cargoDe,cargoPara, gapVarCE);
		       
		    } 
	
	
	/*100%  - (((módulo(Cargo Para - Cargo De)) -4) * Variação no GAP)
		public void calculaMotivadoresDEPARA(TbCARGOSEntity tbCARGOSDe,
				TbCARGOSEntity tbCARGOSPara, double gapVar) {
			gapVar = gapVar/100;
			List<TbMOTIVADORESCARGOSEntity> listCARGOSDe = InicializaTabelasAuxiliaresMO(tbCARGOSDe);
			List<TbMOTIVADORESCARGOSEntity> listCARGOSPara = InicializaTabelasAuxiliaresMO(tbCARGOSPara);
			listCARGOSDe = ordenaListasMO(listCARGOSDe);
			listCARGOSPara = ordenaListasMO(listCARGOSPara);
			
			
			listGapMO.clear();
			Double somaListaGapMO =0.0;
			flagCultura = 1;
			try {
				for(int i=0; i<listCARGOSDe.size();i++) {
					TbMOTIVADORESCARGOSEntity gap = new TbMOTIVADORESCARGOSEntity();
					//Calcula a subtração entre cada pontuação de cada motivador
					if(listCARGOSDe.get(i).getIdMOTIVADORES().getDeSCMOTIVADORES().equals(listCARGOSPara.get(i).getIdMOTIVADORES().getDeSCMOTIVADORES())) {
						gapDeParaMO= listCARGOSPara.get(i).getPoNTUACAOMOTIVADORES().
								subtract(listCARGOSDe.get(i).getPoNTUACAOMOTIVADORES());

						gapDeParaMO = gapDeParaMO.abs().subtract(BigDecimal.valueOf(4));
						//Se "(módulo(Cargo Para - Cargo De)) -4)" < 0 então "(módulo(Cargo Para - Cargo De)) -4)" = 0
						if(gapDeParaMO.compareTo(BigDecimal.ZERO)<0) {
							gapDeParaMO = BigDecimal.ZERO;
						}
						
						gap.setPoNTUACAOMOTIVADORES(gapDeParaMO);
						//Adiciona cada valor à lista
						listGapMO.add(gap);
					}
				}
				//Soma todos os gaps
				for(int i2=0;i2<listGapMO.size();i2++) {
					somaListaGapMO += listGapMO.get(i2).getPoNTUACAOMOTIVADORES().doubleValue();
				}
				//calculo de aderência
				aderenciaMO =  1 -((somaListaGapMO*gapVar));
				aderenciaMO = round(aderenciaMO,2);
				
				//Se aderencia maior que 100%, força o valor
				if(aderenciaMO > 1) {
					aderenciaMO =1.0;
				}
				//Se aderência menor que 0%, força o valor 
				else if (aderenciaMO <0) {
					aderenciaMO =0.0;
				}
			}catch(IndexOutOfBoundsException ex) {

			}
		}*/
	
		//100%  - (((módulo(Cargo Para - Cargo De)) -4) * Variação no GAP)
				public void calculaPerfilDEPARA(TbCARGOSEntity tbCARGOSDe,
						TbCARGOSEntity tbCARGOSPara, double gapVar) {
					gapVar = gapVar/100;
					List<TbPERFILCARGOSEntity> listCARGOSDe = InicializaTabelasAuxiliaresPE(tbCARGOSDe);
					List<TbPERFILCARGOSEntity> listCARGOSPara = InicializaTabelasAuxiliaresPE(tbCARGOSPara);
					listCARGOSDe = ordenaListasPE(listCARGOSDe);
					listCARGOSPara = ordenaListasPE(listCARGOSPara);
					
					
					listGapPE.clear();
					Double somaListaGapPE =0.0;
					flagCultura = 1;
					try {
						for(int i=0; i<listCARGOSDe.size();i++) {
							TbPERFILCARGOSEntity gap = new TbPERFILCARGOSEntity();
							//Calcula a subtração entre cada pontuação de cada perfil
							if(listCARGOSDe.get(i).getIdPERFIL().getDeSCPERFIL().equals(listCARGOSPara.get(i).getIdPERFIL().getDeSCPERFIL())) {
								gapDeParaPE= listCARGOSPara.get(i).getPoNTUACAOPERFIL().
										subtract(listCARGOSDe.get(i).getPoNTUACAOPERFIL());

								
								gap.setPoNTUACAOPERFIL(gapDeParaPE);
								//Adiciona cada valor à lista
								listGapPE.add(gap);
							}
						}
						//Soma todos os gaps
						for(int i2=0;i2<listGapPE.size();i2++) {
							somaListaGapPE += listGapPE.get(i2).getPoNTUACAOPERFIL().doubleValue();
							

						}
						somaListaGapPE = Math.abs(somaListaGapPE) - 4;
						//Se "(módulo(Cargo Para - Cargo De)) -4)" < 0 então "(módulo(Cargo Para - Cargo De)) -4)" = 0
						if(somaListaGapPE <0) {
							somaListaGapPE = 0.0;
						}
						//calculo de aderência
						aderenciaPE =  1 -((somaListaGapPE*gapVar));
						aderenciaPE = round(aderenciaPE,2);
						
						//Se aderencia maior que 100%, força o valor
						if(aderenciaPE > 1) {
							aderenciaPE =1.0;
						}
						//Se aderência menor que 0%, força o valor 
						else if (aderenciaPE <0) {
							aderenciaPE =0.0;
						}
					}catch(IndexOutOfBoundsException ex) {

					}
				}
			
	
	
	public void calculaGRADESDEPARA(TbCARGOSEntity tbCARGOSDe,
			TbCARGOSEntity tbCARGOSPara, double gapVar) {
		gapVar = gapVar/100;
		aderenciaGR = 0.0;
		List<TbGRADECARGOSEntity> listCARGOSDe = InicializaTabelasAuxiliaresGR(tbCARGOSDe);
		List<TbGRADECARGOSEntity> listCARGOSPara = InicializaTabelasAuxiliaresGR(tbCARGOSPara);
		listCARGOSDe = ordenaListasGR(listCARGOSDe);
		listCARGOSPara = ordenaListasGR(listCARGOSPara);
		
		
		listGapGR.clear();
		Double somaListaGapGR =0.0;
		flagCultura = 1;
		try {
			for(int i=0; i<listCARGOSDe.size();i++) {
				TbGRADECARGOSEntity gap = new TbGRADECARGOSEntity();
				//Calcula a subtração entre cada pontuação de cada competência
				if(listCARGOSDe.get(i).getIdGRADE().getDeSCGRADE().equals(listCARGOSPara.get(i).getIdGRADE().getDeSCGRADE())) {
					gapDeParaGR= listCARGOSPara.get(i).getPoNTUACAOGRADE().
							subtract(listCARGOSDe.get(i).getPoNTUACAOGRADE());
					
					
					
					
					gap.setPoNTUACAOGRADE(gapDeParaGR);
					//Adiciona cada valor à lista
					listGapGR.add(gap);
				}
			}
			//Soma todos os gaps
			for(int i2=0;i2<listGapGR.size();i2++) {
				somaListaGapGR += listGapGR.get(i2).getPoNTUACAOGRADE().doubleValue();
			}
			//calculo de aderência
			aderenciaGR =  1 -((somaListaGapGR*gapVar));
			aderenciaGR = round(aderenciaGR,2);
			
			//Se aderencia maior que 100%, força o valor
			if(aderenciaGR > 1) {
				aderenciaGR =1.0;
			}
			//Se aderência menor que 0%, força o valor 
			else if (aderenciaGR <0) {
				aderenciaGR =0.0;
				
				
				
			}
		}catch(IndexOutOfBoundsException ex) {

		}
	}
	
	
	public void calculaAderenciasParciais(TbCARGOSEntity tbCARGOSDe,
			TbCARGOSEntity tbCARGOSPara, double gapVarCO, double gapVarNH, double gapVarPE,
										double gapVarCB, double gapVarCE) {
		
		calculaGRADESDEPARA(tbCARGOSDe, tbCARGOSPara, gapVarNH);
		calculaCompetenciasDEPARA(tbCARGOSDe, tbCARGOSPara, gapVarCO);
		calculaPerfilDEPARA(tbCARGOSDe, tbCARGOSPara, gapVarPE);
		calculaConhecimentosBasicosDEPARA(tbCARGOSDe, tbCARGOSPara, gapVarCB);
		calculaConhecimentosEspecificosDEPARA(tbCARGOSDe, tbCARGOSPara, gapVarCE);
		
		
		calculaAderenciaFinal(tbCARGOSDe, tbCARGOSPara);
		
	}

	
	
	public void calculaAderenciaFinal(TbCARGOSEntity tbCARGOSDe,
			TbCARGOSEntity tbCARGOSPara) {
		double pesoCO = 0.3;
		double pesoNH = 0.2;
		double pesoPE = 0.3;
		double pesoCB = 0.0;
		double pesoCE = 0.0;
		
		aderenciaFinal = (pesoCO * aderencia) + (pesoNH * aderenciaGR) + (pesoPE * aderenciaPE) + 
						(pesoCB * aderenciaCB) + (pesoCE *aderenciaCE);
		
		if(aderenciaGR ==0.0) {
		
			avisoMovimentacao = "Movimentação entre cargos impossível!";
		}
	}
	
	
	
	public static double round(double value, int places) {
		if (places < 0) throw new IllegalArgumentException();

		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	





	public void onDialogOpen(TbCARGOSEntity tbCARGOS) {
		reset();
		changeHeaderEditar();
		this.tbCARGOS = tbCARGOS;
		this.tbCARGOS.setReSPONSABILIDADES(this.tbCARGOS.getReSPONSABILIDADES().replace("<br>", "\n"));
	}


	public String detalhesComportamentos(BigDecimal poNTUACAOHABCARGOS) {
		for(int i=0; i<tbHABILIDADESCARGOSs.size();i++) {
			if(poNTUACAOHABCARGOS.toString().equals("3")) {
				setDetalhesComportamento("Hibrido");
			}else if(poNTUACAOHABCARGOS.toString().equals("4")) {
				setDetalhesComportamento("Amplitude");
			}
		}
		return detalhesComportamento;
	}

	public String persist() {

		String message;

		try {

			if (tbCARGOS.getId() != null) {
				tbCARGOS = tbCARGOSService.update(tbCARGOS);
				message = "message_successfully_updated";
			} else {
				tbCARGOS = tbCARGOSService.save(tbCARGOS);
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

		tbCARGOSList = null;

		FacesMessage facesMessage = MessageFactory.getMessage(message);
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);

		return null;
	}

	public String delete() {

		String message;

		try {
			tbCARGOSService.delete(tbCARGOS);
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



	public void reset() {
		tbCARGOS = null;
		tbCARGOSList = null;
		tbHABILIDADESCULTCARGOSs = null;
		allIdESTsList = null;
		allIdCURSOSsList = null;
		allIdDEPTOsList = null;
		allIdNOsList = null;

	}

	// Get a List of all idEST
	public List<TbESTATUARIOEntity> getIdESTs() {
		if (this.allIdESTsList == null) {
			this.allIdESTsList = tbESTATUARIOService.findAllTbESTATUARIOEntities();

		}
		return this.allIdESTsList;
	}

	// Get a List of all idDEPTO
		public List<TbDEPTOEntity> getIdDEPTOs() {
			if (this.allIdDEPTOsList == null) {
				this.allIdDEPTOsList = tbDEPTOSerivce.findAllTbDEPTOEntities();

			}
			return this.allIdDEPTOsList;
		}
		
	// Get a List of all idNO
		public List<TbNOEntity> getIdNOs() {
			if (this.allIdNOsList == null) {
					this.allIdNOsList = tbNOService.findAllTbNOEntities();
					}
			return this.allIdNOsList;
		}		


	// Get a List of all idDIRETORIAs
	public List<TbDIRETORIAEntity> getIdDiretoria() {
		if (this.allIdDIRETORIAsList == null) {
			this.allIdDIRETORIAsList = tbDIRETORIAService.findAllTbDIRETORIAEntities();
		}
		return this.allIdDIRETORIAsList;
	}




	public void updateIdDEPTO(TbDEPTOEntity tbDEPTO) {
		this.tbCARGOS.setIdDEPTO(tbDEPTO);
		allIdESTsList = null;
	}
	
	public void updateIdNO(TbNOEntity tbNO) {
		this.tbCARGOS.setIdNO(tbNO);
	}

	// Get a List of all idCURSOS
	public List<TbCURSOSEntity> getIdCURSOSs() {
		if (this.allIdCURSOSsList == null) {
			this.allIdCURSOSsList = tbCURSOSService.findAllTbCURSOSEntities();
		}
		return this.allIdCURSOSsList;
	}





	public TbCARGOSEntity getTbCARGOS() {
		if (this.tbCARGOS == null) {
			prepareNewTbCARGOS();
		}
		return this.tbCARGOS;
	}

	public void setTbCARGOS(TbCARGOSEntity tbCARGOS) {
		this.tbCARGOS = tbCARGOS;
	}

	public List<TbCARGOSEntity> getTbCARGOSList() {
		if (tbCARGOSList == null) {
			tbCARGOSList = tbCARGOSService.findAllTbCARGOSEntities();
		}
		return tbCARGOSList;
	}

	public void setTbCARGOSList(List<TbCARGOSEntity> tbCARGOSList) {
		this.tbCARGOSList = tbCARGOSList;
	}

	public boolean isPermitted(String permission) {
		return SecurityWrapper.isPermitted(permission);
	}

	public boolean isPermitted(TbCARGOSEntity tbCARGOS, String permission) {

		return SecurityWrapper.isPermitted(permission);

	}

	public TbCARGOSEntity getSelectedCargo() {
		return selectedCargo;
	}

	public void setSelectedCargo(TbCARGOSEntity selectedCargo) {
		this.selectedCargo = selectedCargo;
	}

	public List<TbHABILIDADESCULTCARGOSEntity> getTbHABILIDADESCULTCARGOSs() {
		return tbHABILIDADESCULTCARGOSs;
	}

	public void setTbHABILIDADESCULTCARGOSs(List<TbHABILIDADESCULTCARGOSEntity> tbHABILIDADESCULTCARGOSs) {
		this.tbHABILIDADESCULTCARGOSs = tbHABILIDADESCULTCARGOSs;
	}

	public int getFlagCultura() {
		return flagCultura;
	}

	public void setFlagCultura(int flagCultura) {
		this.flagCultura = flagCultura;
	}

	public List<TbCARGOSEntity> getTbCARGOSListFiltrada() {
		return tbCARGOSListFiltrada;
	}

	public void setTbCARGOSListFiltrada(List<TbCARGOSEntity> tbCARGOSListFiltrada) {
		this.tbCARGOSListFiltrada = tbCARGOSListFiltrada;
	}


	public List<TbDIRETORIAEntity> getAllIdDIRETORIAsList() {
		return allIdDIRETORIAsList;
	}

	public void setAllIdDIRETORIAsList(List<TbDIRETORIAEntity> allIdDIRETORIAsList) {
		this.allIdDIRETORIAsList = allIdDIRETORIAsList;
	}

	public List<TbHABILIDADESCULTURAISEntity> getAllIdHABCULTCARsList() {
		return allIdHABCULTCARsList;
	}

	public void setAllIdHABCULTCARsList(List<TbHABILIDADESCULTURAISEntity> allIdHABCULTCARsList) {
		this.allIdHABCULTCARsList = allIdHABCULTCARsList;
	}

	public List<TbCARGOSEntity> getAllIdCARGOSsList() {
		return allIdCARGOSsList;
	}

	public void setAllIdCARGOSsList(List<TbCARGOSEntity> allIdCARGOSsList) {
		this.allIdCARGOSsList = allIdCARGOSsList;
	}

	public List<TbHABILIDADESCARGOSEntity> getTbHABILIDADESCARGOSs() {
		return tbHABILIDADESCARGOSs;
	}

	public void setTbHABILIDADESCARGOSs(List<TbHABILIDADESCARGOSEntity> tbHABILIDADESCARGOSs) {
		this.tbHABILIDADESCARGOSs = tbHABILIDADESCARGOSs;
	}

	public List<TbESTILOLIDERANCACARGOSEntity> getTbESTILOLIDERANCAs() {
		return tbESTILOLIDERANCAs;
	}

	public void setTbESTILOLIDERANCAs(List<TbESTILOLIDERANCACARGOSEntity> tbESTILOLIDERANCAs) {
		this.tbESTILOLIDERANCAs = tbESTILOLIDERANCAs;
	}

	public List<TbESTILOPENSAMENTOCARGOSEntity> getTbESTILOPENSAMENTOs() {
		return tbESTILOPENSAMENTOs;
	}

	public void setTbESTILOPENSAMENTOs(List<TbESTILOPENSAMENTOCARGOSEntity> tbESTILOPENSAMENTOs) {
		this.tbESTILOPENSAMENTOs = tbESTILOPENSAMENTOs;
	}

	public List<TbCOMPETENCIASEMCARGOSEntity> getTbCOMPETENCIASEMs() {
		return tbCOMPETENCIASEMs;
	}

	public void setTbCOMPETENCIASEMs(List<TbCOMPETENCIASEMCARGOSEntity> tbCOMPETENCIASEMs) {
		this.tbCOMPETENCIASEMs = tbCOMPETENCIASEMs;
	}

	public List<TbCOMPETENCIASCARGOSEntity> getTbCOMPETENCIASCARGOSs() {
		return tbCOMPETENCIASCARGOSs;
	}

	public void setTbCOMPETENCIASCARGOSs(List<TbCOMPETENCIASCARGOSEntity> tbCOMPETENCIASCARGOSs) {
		this.tbCOMPETENCIASCARGOSs = tbCOMPETENCIASCARGOSs;
	}

	public List<TbCONHECIMENTOSBASCARGOSEntity> getTbCONHECIMENTOSBASICOSCARGOSs() {
		return tbCONHECIMENTOSBASICOSCARGOSs;
	}

	public void setTbCONHECIMENTOSBASICOSCARGOSs(List<TbCONHECIMENTOSBASCARGOSEntity> tbCONHECIMENTOSBASICOSCARGOSs) {
		this.tbCONHECIMENTOSBASICOSCARGOSs = tbCONHECIMENTOSBASICOSCARGOSs;
	}

	public List<TbCONHECIMENTOSESPCARGOSEntity> getTbCONHECIMENTOSESPCARGOSs() {
		return tbCONHECIMENTOSESPCARGOSs;
	}

	public void setTbCONHECIMENTOSESPCARGOSs(List<TbCONHECIMENTOSESPCARGOSEntity> tbCONHECIMENTOSESPCARGOSs) {
		this.tbCONHECIMENTOSESPCARGOSs = tbCONHECIMENTOSESPCARGOSs;
	}

	public TbCARGOSEntity getCargoDe() {
		return cargoDe;
	}

	public void setCargoDe(TbCARGOSEntity cargoDe) {
		this.cargoDe = cargoDe;
	}

	public TbCARGOSEntity getCargoPara() {
		return cargoPara;
	}

	public void setCargoPara(TbCARGOSEntity cargoPara) {
		this.cargoPara = cargoPara;
	}

	public BigDecimal getGapDePara() {
		return gapDePara;
	}

	public void setGapDePara(BigDecimal gapDePara) {
		this.gapDePara = gapDePara;
	}

	public Double getGapVar() {
		return gapVar;
	}

	public void setGapVar(Double gapVar) {
		this.gapVar = gapVar;
	}

	public List<TbCOMPETENCIASCARGOSEntity> getListGap() {
		return listGap;
	}

	public void setListGap(List<TbCOMPETENCIASCARGOSEntity> listGap) {
		this.listGap = listGap;
	}

	public Double getAderencia() {
		return aderencia;
	}

	public void setAderencia(Double aderencia) {
		this.aderencia = aderencia;
	}



	
	public List<TbGRADECARGOSEntity> getTbGRADESCARGOSs() {
		return tbGRADESCARGOSs;
	}

	public void setTbGRADESCARGOSs(List<TbGRADECARGOSEntity> tbGRADESCARGOSs) {
		this.tbGRADESCARGOSs = tbGRADESCARGOSs;
	}

	public List<TbPERFILCARGOSEntity> getTbPERFILCARGOSs() {
		return tbPERFILCARGOSs;
	}

	public void setTbPERFILCARGOSs(List<TbPERFILCARGOSEntity> tbPERFILCARGOSs) {
		this.tbPERFILCARGOSs = tbPERFILCARGOSs;
	}

	public List<TbMOTIVADORESCARGOSEntity> getTbMOTIVADORESCARGOSs() {
		return tbMOTIVADORESCARGOSs;
	}

	public void setTbMOTIVADORESCARGOSs(List<TbMOTIVADORESCARGOSEntity> tbMOTIVADORESCARGOSs) {
		this.tbMOTIVADORESCARGOSs = tbMOTIVADORESCARGOSs;
	}

	public BigDecimal getGapDeParaGR() {
		return gapDeParaGR;
	}

	public void setGapDeParaGR(BigDecimal gapDeParaGR) {
		this.gapDeParaGR = gapDeParaGR;
	}

	public Double getGapVarGR() {
		return gapVarGR;
	}

	public void setGapVarGR(Double gapVarGR) {
		this.gapVarGR = gapVarGR;
	}

	public Double getAderenciaGR() {
		return aderenciaGR;
	}

	public void setAderenciaGR(Double aderenciaGR) {
		this.aderenciaGR = aderenciaGR;
	}

	public List<TbGRADECARGOSEntity> getListGapGR() {
		return listGapGR;
	}

	public void setListGapGR(List<TbGRADECARGOSEntity> listGapGR) {
		this.listGapGR = listGapGR;
	}

	public String getAvisoMovimentacao() {
		return avisoMovimentacao;
	}

	public void setAvisoMovimentacao(String avisoMovimentacao) {
		this.avisoMovimentacao = avisoMovimentacao;
	}

	public BigDecimal getGapDeParaMO() {
		return gapDeParaMO;
	}

	public void setGapDeParaMO(BigDecimal gapDeParaMO) {
		this.gapDeParaMO = gapDeParaMO;
	}

	public Double getGapVarMO() {
		return gapVarMO;
	}

	public void setGapVarMO(Double gapVarMO) {
		this.gapVarMO = gapVarMO;
	}

	public Double getAderenciaMO() {
		return aderenciaMO;
	}

	public void setAderenciaMO(Double aderenciaMO) {
		this.aderenciaMO = aderenciaMO;
	}

	public List<TbMOTIVADORESCARGOSEntity> getListGapMO() {
		return listGapMO;
	}

	public void setListGapMO(List<TbMOTIVADORESCARGOSEntity> listGapMO) {
		this.listGapMO = listGapMO;
	}

	public BigDecimal getGapDeParaPE() {
		return gapDeParaPE;
	}

	public void setGapDeParaPE(BigDecimal gapDeParaPE) {
		this.gapDeParaPE = gapDeParaPE;
	}

	public Double getGapVarPE() {
		return gapVarPE;
	}

	public void setGapVarPE(Double gapVarPE) {
		this.gapVarPE = gapVarPE;
	}

	public Double getAderenciaPE() {
		return aderenciaPE;
	}

	public void setAderenciaPE(Double aderenciaPE) {
		this.aderenciaPE = aderenciaPE;
	}

	public List<TbPERFILCARGOSEntity> getListGapPE() {
		return listGapPE;
	}

	public void setListGapPE(List<TbPERFILCARGOSEntity> listGapPE) {
		this.listGapPE = listGapPE;
	}

	public Double getAderenciaFinal() {
		return aderenciaFinal;
	}

	public void setAderenciaFinal(Double aderenciaFinal) {
		this.aderenciaFinal = aderenciaFinal;
	}

	public BigDecimal getGapDeParaCB() {
		return gapDeParaCB;
	}

	public void setGapDeParaCB(BigDecimal gapDeParaCB) {
		this.gapDeParaCB = gapDeParaCB;
	}

	public Double getGapVarCB() {
		return gapVarCB;
	}

	public void setGapVarCB(Double gapVarCB) {
		this.gapVarCB = gapVarCB;
	}

	public Double getAderenciaCB() {
		return aderenciaCB;
	}

	public void setAderenciaCB(Double aderenciaCB) {
		this.aderenciaCB = aderenciaCB;
	}

	public List<TbCONHECIMENTOSBASCARGOSEntity> getListGapCB() {
		return listGapCB;
	}

	public void setListGapCB(List<TbCONHECIMENTOSBASCARGOSEntity> listGapCB) {
		this.listGapCB = listGapCB;
	}

	public BigDecimal getGapDeParaCE() {
		return gapDeParaCE;
	}

	public void setGapDeParaCE(BigDecimal gapDeParaCE) {
		this.gapDeParaCE = gapDeParaCE;
	}

	public Double getGapVarCE() {
		return gapVarCE;
	}

	public void setGapVarCE(Double gapVarCE) {
		this.gapVarCE = gapVarCE;
	}

	public Double getAderenciaCE() {
		return aderenciaCE;
	}

	public void setAderenciaCE(Double aderenciaCE) {
		this.aderenciaCE = aderenciaCE;
	}

	public List<TbCONHECIMENTOSESPCARGOSEntity> getListGapCE() {
		return listGapCE;
	}

	public void setListGapCE(List<TbCONHECIMENTOSESPCARGOSEntity> listGapCE) {
		this.listGapCE = listGapCE;
	}


	




}
