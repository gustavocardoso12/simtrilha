package org.applicationn.simtrilhas.web;

import java.io.Serializable;

import java.util.ArrayList;

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
import org.applicationn.simtrilhas.domain.TbCONHECIMENTOSBASCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbCONHECIMENTOSESPCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbCURSOSEntity;
import org.applicationn.simtrilhas.domain.TbDEPTOEntity;
import org.applicationn.simtrilhas.domain.TbNOEntity;
import org.applicationn.simtrilhas.domain.TbNivelEscolaridadeEntity;
import org.applicationn.simtrilhas.domain.TbDIRETORIAEntity;
import org.applicationn.simtrilhas.domain.TbESTATUARIOEntity;
import org.applicationn.simtrilhas.domain.TbESTILOLIDERANCACARGOSEntity;
import org.applicationn.simtrilhas.domain.TbESTILOPENSAMENTOCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbGRADECARGOSEntity;
import org.applicationn.simtrilhas.domain.TbHABILIDADESCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbHABILIDADESCULTCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbHABILIDADESCULTURAISEntity;
import org.applicationn.simtrilhas.domain.TbMATRIZCARGOSEntityTrilhas;
import org.applicationn.simtrilhas.domain.TbMOTIVADORESCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbPERFILCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbPONTCARGOSEntity;
import org.applicationn.simtrilhas.domain.security.UserEntity;
import org.applicationn.simtrilhas.service.TbAREAService;
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
import org.applicationn.simtrilhas.service.TbESTILOPENSAMENTOCARGOSService;
import org.applicationn.simtrilhas.service.TbGRADECARGOSService;
import org.applicationn.simtrilhas.service.TbHABILIDADESCARGOSService;
import org.applicationn.simtrilhas.service.TbHABILIDADESCULTCARGOSService;
import org.applicationn.simtrilhas.service.TbHABILIDADESCULTURAISService;
import org.applicationn.simtrilhas.service.TbMATRIZCARGOSService;
import org.applicationn.simtrilhas.service.TbMOTIVADORESCARGOSService;
import org.applicationn.simtrilhas.service.TbNIVELESCOLARIDADEService;
import org.applicationn.simtrilhas.service.TbPERFILCARGOSService;
import org.applicationn.simtrilhas.service.TbPONTCARGOSService;
import org.applicationn.simtrilhas.service.security.SecurityWrapper;
import org.applicationn.simtrilhas.service.security.UserService;
import org.applicationn.simtrilhas.web.util.MessageFactory;
import org.primefaces.PrimeFaces;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

@Named("tbCARGOSBean")
@ViewScoped
public class TbCARGOSBean implements Serializable {

	protected static final long serialVersionUID = 1L;

	protected UserEntity user;

	protected static final Logger logger = Logger.getLogger(TbCARGOSBean.class.getName());

	protected List<TbCARGOSEntity> tbCARGOSList;
	
	protected List<TbCARGOSEntity> tbPESSOASList;



	protected TbCARGOSEntity tbCARGOSEdicao;

	protected List<TbCARGOSEntity> tbCARGOSListFiltrada = new ArrayList<TbCARGOSEntity>();

	protected TbCARGOSEntity tbCARGOS;

	@Inject
	protected TbMATRIZCARGOSService tbMATRIZCARGOSService;

	@Inject
	protected TbCARGOSService tbCARGOSService;

	@Inject
	protected TbESTATUARIOService tbESTATUARIOService;

	@Inject
	protected TbCURSOSService tbCURSOSService;

	@Inject
	protected TbDIRETORIAService tbDIRETORIAService;

	@Inject
	protected TbPONTCARGOSService tbPONTCARGOSService;

	@Inject
	protected TbCOMPETENCIASCARGOSService tbCOMPETENCIASCARGOSService;

	@Inject
	protected TbCOMPETENCIASEMCARGOSService tbCOMPETENCIASEMCARGOSService;

	@Inject
	protected TbHABILIDADESCULTURAISService tbHABILIDADESCULTURAISService;

	@Inject
	protected TbCONHECIMENTOSBASCARGOSService tbCONHECIMENTOSBASCARGOSService;

	@Inject
	protected TbCONHECIMENTOSESPCARGOSService tbCONHECIMENTOSESPCARGOSService;

	@Inject
	protected TbESTILOLIDERANCACARGOSService tbESTILOLIDERANCACARGOSService;

	@Inject
	protected TbESTILOPENSAMENTOCARGOSService tbESTILOPENSAMENTOCARGOSService;

	@Inject
	protected TbHABILIDADESCARGOSService tbHABILIDADESCARGOSService;

	@Inject
	protected TbHABILIDADESCULTCARGOSService tbHABILIDADESCULTCARGOSService;

	@Inject
	protected TbDEPTOService tbDEPTOSerivce;

	@Inject
	protected TbAREAService tbAREAService;

	@Inject
	protected TbNOService tbNOService;

	@Inject
	protected TbGRADECARGOSService tbGRADESCARGOSService;

	@Inject
	protected TbPERFILCARGOSService tbPERFILCARGOSService;

	@Inject
	protected TbMOTIVADORESCARGOSService tbMOTIVADORESCARGOSService;

	@Inject
	protected UserService userService;
	
	@Inject
	protected TbNIVELESCOLARIDADEService tbNIVELESCOLARIDADEService;

	protected List<TbHABILIDADESCARGOSEntity> tbHABILIDADESCARGOSs;

	protected List<TbHABILIDADESCULTCARGOSEntity> tbHABILIDADESCULTCARGOSs;

	protected List<TbESTILOLIDERANCACARGOSEntity> tbESTILOLIDERANCAs;

	protected List<TbESTILOPENSAMENTOCARGOSEntity> tbESTILOPENSAMENTOs;

	protected List<TbCOMPETENCIASEMCARGOSEntity> tbCOMPETENCIASEMs;

	protected List<TbCOMPETENCIASCARGOSEntity> tbCOMPETENCIASCARGOSs;

	protected List<TbCONHECIMENTOSBASCARGOSEntity> tbCONHECIMENTOSBASICOSCARGOSs;

	protected List<TbCONHECIMENTOSESPCARGOSEntity> tbCONHECIMENTOSESPCARGOSs;

	protected List<TbGRADECARGOSEntity> tbGRADESCARGOSs;

	protected List<TbPERFILCARGOSEntity> tbPERFILCARGOSs;

	protected List<TbMOTIVADORESCARGOSEntity> tbMOTIVADORESCARGOSs;

	protected List<TbESTATUARIOEntity> allIdESTsList;

	protected List<String> distinctArea;

	protected List<String> distinctDepto;

	protected List<TbCARGOSEntity> distinctCargos;

	protected String areaEscolhida;

	protected String departamentoEscolhido;

	protected List<String> distinctAreaPara;

	protected List<String> distinctDeptoPara;

	protected List<TbCARGOSEntity> distinctCargosPara;

	protected String areaEscolhidaPara;

	protected String departamentoEscolhidoPara;

	protected List<TbDEPTOEntity> allIdDEPTOsList;

	protected List<TbNOEntity> allIdNOsList;

	protected List<TbCURSOSEntity> allIdCURSOSsList;

	protected List<TbDIRETORIAEntity> allIdDIRETORIAsList;

	protected List<TbHABILIDADESCULTURAISEntity> allIdHABCULTCARsList = new ArrayList<TbHABILIDADESCULTURAISEntity>();

	protected List<TbCARGOSEntity> allIdCARGOSsList = new ArrayList<TbCARGOSEntity>();

	protected TbCARGOSEntity selectedCargo;

	protected String dialogHeader;

	protected String detalhesComportamento;

	protected TbCARGOSEntity cargoDe;

	protected TbCARGOSEntity cargoPara;

	protected Double gapDePara;

	protected int gapVar = 30;

	protected Double aderencia;

	List<TbCOMPETENCIASCARGOSEntity> listGap = new ArrayList<TbCOMPETENCIASCARGOSEntity>();

	protected double gapDeParaGR;

	protected int gapVarGR = 25;

	protected Double aderenciaGR;

	List<TbGRADECARGOSEntity> listGapGR = new ArrayList<TbGRADECARGOSEntity>();

	protected int gapDeParaMO;

	protected int gapVarMO;

	protected Double aderenciaMO;

	List<TbMOTIVADORESCARGOSEntity> listGapMO = new ArrayList<TbMOTIVADORESCARGOSEntity>();

	protected double gapDeParaPE;

	protected int gapVarPE = 10;

	protected Double aderenciaPE;

	List<TbPERFILCARGOSEntity> listGapPE = new ArrayList<TbPERFILCARGOSEntity>();

	protected double gapDeParaCB;

	protected int gapVarCB = 2;

	protected Double aderenciaCB;

	List<TbCONHECIMENTOSBASCARGOSEntity> listGapCB = new ArrayList<TbCONHECIMENTOSBASCARGOSEntity>();

	protected double gapDeParaCE;

	protected int gapVarCE = 5;

	protected double aderenciaMinima = 30.0;

	protected Double aderenciaCE;

	List<TbCONHECIMENTOSESPCARGOSEntity> listGapCE = new ArrayList<TbCONHECIMENTOSESPCARGOSEntity>();


	List<TbCONHECIMENTOSBASCARGOSEntity> listCBCARGOSPara = new ArrayList<TbCONHECIMENTOSBASCARGOSEntity>();

	List<TbCONHECIMENTOSBASCARGOSEntity> listCBCARGOSDe = new ArrayList<TbCONHECIMENTOSBASCARGOSEntity>();


	List<TbCONHECIMENTOSESPCARGOSEntity> listCECARGOSPara = new ArrayList<TbCONHECIMENTOSESPCARGOSEntity>();

	List<TbCONHECIMENTOSESPCARGOSEntity> listCECARGOSDe = new ArrayList<TbCONHECIMENTOSESPCARGOSEntity>();

	List<TbCOMPETENCIASCARGOSEntity> listCOCARGOSPara = new ArrayList<TbCOMPETENCIASCARGOSEntity>();

	List<TbCOMPETENCIASCARGOSEntity> listCOCARGOSDe = new ArrayList<TbCOMPETENCIASCARGOSEntity>();

	List<TbGRADECARGOSEntity> listGRCARGOSPara = new ArrayList<TbGRADECARGOSEntity>();

	List<TbGRADECARGOSEntity> listGRCARGOSDe = new ArrayList<TbGRADECARGOSEntity>();

	List<TbPERFILCARGOSEntity> listPECARGOSPara = new ArrayList<TbPERFILCARGOSEntity>();

	List<TbPERFILCARGOSEntity> listPECARGOSDe = new ArrayList<TbPERFILCARGOSEntity>();
	
	protected String flagEstaganado ="false";

	protected boolean flagDowngrade = false;

	protected String msgMovimentacaoFalha;

	protected Double aderenciaFinal;



	protected String avisoDowngrade;

	protected String corBarraAderenciaFinal;

	protected String corFinal;

	protected double pesoCO = 0.0;
	protected double pesoNH = 0.0;
	protected double pesoPE = 0.0;
	protected double pesoCB = 0.0;
	protected double pesoCE = 0.0;

	protected List<TbPONTCARGOSEntity> tbPesos = new ArrayList<TbPONTCARGOSEntity>();

	private List<TbMATRIZCARGOSEntityTrilhas> listaTrilhas = new ArrayList<TbMATRIZCARGOSEntityTrilhas>();

	private List<TbMATRIZCARGOSEntityTrilhas> listaTrilhasn2 = new ArrayList<TbMATRIZCARGOSEntityTrilhas>();

	private List<TbMATRIZCARGOSEntityTrilhas> listaTrilhasn2_per = new ArrayList<TbMATRIZCARGOSEntityTrilhas>();

	private List<TbMATRIZCARGOSEntityTrilhas> listaTrilhasn3_per = new ArrayList<TbMATRIZCARGOSEntityTrilhas>();

	private List<TbMATRIZCARGOSEntityTrilhas> listaTrilhasn3 = new ArrayList<TbMATRIZCARGOSEntityTrilhas>();

	private List<TbMATRIZCARGOSEntityTrilhas> listaTrilhasn4 = new ArrayList<TbMATRIZCARGOSEntityTrilhas>();

	private List<TbCARGOSEntity> listaCargosTrilhas = new ArrayList<TbCARGOSEntity>();

	private TbMATRIZCARGOSEntityTrilhas selectedTrilha;

	private TreeNode root;

	private boolean flagBloqueio;

	private int id;
	
	protected boolean selector;

	public String submit(TbCARGOSEntity cargo) {
		id = cargo.getId().intValue();

		return "/trilhas/Cargos/EditarCargos.xhtml?faces-redirect=true&includeViewParams=true";
	}
	
	
	public String submitPessoas(TbCARGOSEntity cargo) {
		id = cargo.getId().intValue();

		return "/trilhas/Pessoas/EditarPessoas.xhtml?faces-redirect=true&includeViewParams=true";
	}


	public void InicializaCargos(int id) {

		setFlagCultura(1);
		flagCultura = 1;
		System.out.println(id);

	}

	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public TreeNode getRoot() {

		return root;
	}

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

	protected int flagCultura = 0;

	@PostConstruct
	public void init() {
		try {
		user = userService.getCurrentUser();
		tbPesos = tbPONTCARGOSService.findAllTbPONTCARGOSEntities();



		pesoNH = tbPesos.get(0).getPeso();

		pesoPE = tbPesos.get(1).getPeso();

		pesoCO = tbPesos.get(2).getPeso();

		pesoCB = tbPesos.get(3).getPeso();

		pesoCE = tbPesos.get(4).getPeso();
		
       selector = true;
		}catch (Exception e) {
			// TODO: handle exception
		}

	}

	public void InicializaCargos(TbCARGOSEntity tbCARGOS) {
		if (tbCARGOS == null) {

		} else {
			setFlagCultura(1);
			flagCultura = 1;
			this.tbCARGOS = tbCARGOS;
			tbCARGOSListFiltrada.clear();
			tbCARGOSListFiltrada.add(this.tbCARGOS);

			PrimeFaces.current().scrollTo("paneCargos");

		}
	}

	public void autoTrilhas(TbCARGOSEntity tbCARGOS) {
		if (tbCARGOS == null) {
			FacesMessage message = MessageFactory.getMessage(
					"simulacao_vazia");
			FacesContext.getCurrentInstance().addMessage(null, message);
			return;
		} else {
			try {
			setFlagCultura(1);
			flagCultura = 1;
			this.tbCARGOS = tbCARGOS;
			tbCARGOSListFiltrada.clear();
			tbCARGOSListFiltrada.add(this.tbCARGOS);
			listaTrilhas.clear();
			listaTrilhasn2.clear();
			listaTrilhasn2_per.clear();
			listaTrilhasn3.clear();
			listaTrilhasn3_per.clear();
			listaTrilhasn4.clear();

			// raiz
			root = new DefaultTreeNode(tbCARGOSListFiltrada.get(0).getDeSCCARGO(), null);

			// nivel 1
			listaTrilhas = tbMATRIZCARGOSService.findTrilhas(aderenciaMinima, tbCARGOS);
			TreeNode[] nodes = new TreeNode[listaTrilhas.size()];
			for (int i = 0; i < listaTrilhas.size(); i++) {
				nodes[i] = new DefaultTreeNode(listaTrilhas.get(i).getIdCARGOPARA().getDeSCCARGO() + " "
						+ listaTrilhas.get(i).getAdERENCIAFINAL().intValue() + "%", root);
			}

			// nivel 2
			
			List<TreeNode> nodesn2_per = new ArrayList<TreeNode>();

			for (int i = 0; i < listaTrilhas.size(); i++) {
				listaTrilhasn2 = tbMATRIZCARGOSService.findTrilhas(aderenciaMinima,
						listaTrilhas.get(i).getIdCARGOPARA());
				listaTrilhasn2_per.addAll(listaTrilhasn2);
				System.out.println(listaTrilhasn2_per.size());
				TreeNode[] nodesn2 = new TreeNode[listaTrilhasn2_per.size()];
				for (int j = 0; j < listaTrilhasn2.size(); j++) {
					nodesn2[j] = new DefaultTreeNode(listaTrilhasn2.get(j).getIdCARGOPARA().getDeSCCARGO() + " "
							+ listaTrilhasn2.get(j).getAdERENCIAFINAL().intValue() + "%", nodes[i]);
					nodesn2_per.add(nodesn2[j]);
				}

			}

			// nível 3
			
			List<TreeNode> nodesn3_per = new ArrayList<TreeNode>();
			for (int i = 0; i < listaTrilhasn2_per.size(); i++) {
				listaTrilhasn3 = tbMATRIZCARGOSService.findTrilhas(aderenciaMinima,
						listaTrilhasn2_per.get(i).getIdCARGOPARA());
				listaTrilhasn3_per.addAll(listaTrilhasn3);
				TreeNode[] nodesn3 = new TreeNode[listaTrilhasn3_per.size()];
				for (int j = 0; j < listaTrilhasn3.size(); j++) {
					nodesn3[j] = new DefaultTreeNode(listaTrilhasn3.get(j).getIdCARGOPARA().getDeSCCARGO() + " "
							+ listaTrilhasn3.get(j).getAdERENCIAFINAL().intValue() + "%", nodesn2_per.get(i));
					nodesn3_per.add(nodesn3[j]);
				}

			}
			// nivel 4
			TreeNode[] nodesn4 = new TreeNode[listaTrilhasn3_per.size() * listaTrilhasn3_per.size()];

			for (int i = 0; i < listaTrilhasn3_per.size(); i++) {
				listaTrilhasn4 = tbMATRIZCARGOSService.findTrilhas(aderenciaMinima,
						listaTrilhasn3_per.get(i).getIdCARGOPARA());
				for (int j = 0; j < listaTrilhasn4.size(); j++) {
					nodesn4[j] = new DefaultTreeNode(listaTrilhasn4.get(j).getIdCARGOPARA().getDeSCCARGO() + " "
							+ listaTrilhasn4.get(j).getAdERENCIAFINAL().intValue() + "%", nodesn3_per.get(i));
				}

			}

		
		}catch(IndexOutOfBoundsException ex) {
			System.out.println(ex.getMessage());
		}
		}
	}

	public void prepareNewTbCARGOS() {
		reset();
		changeHeaderCadastrar();
		this.tbCARGOS = new TbCARGOSEntity();
	}

	public String AdicionarCargos() {
		reset();
		changeHeaderCadastrar();
		this.tbCARGOS = new TbCARGOSEntity();
		return "/trilhas/Cargos/AdicionarCargo.xhtml?faces-redirect=true";
	}
	
	
	public String AdicionarPessoas() {
		reset();
		changeHeaderCadastrar();
		this.tbCARGOS = new TbCARGOSEntity();
		return "/trilhas/Pessoas/AdicionarPessoas.xhtml?faces-redirect=true";
	}

	public void SelecionaPessoasAT() {
		selector = false; 
		
		this.distinctArea = tbAREAService.findDistinctTbAREAATEntitiesDe();
		areaEscolhida=null;
		distinctDepto=null;
		distinctCargos=null;
		departamentoEscolhido=null;
		cargoDe = null;
		
	}
	
	public void SelecionaPessoasComp() {
		selector = false; 
		
		this.distinctArea = tbAREAService.findDistinctTbAREAATEntitiesDe();
		areaEscolhida=null;
		distinctDepto=null;
		distinctCargos=null;
		departamentoEscolhido=null;
		cargoDe = null;
		
		areaEscolhidaPara=null;
		distinctDeptoPara=null;
		distinctCargosPara=null;
		departamentoEscolhidoPara=null;
		cargoPara=null;
		
	}
	
	public void SelecionaCargosComp() {
		selector = true; 
		this.distinctArea = tbAREAService.findDistinctTbAREAEntitiesDe(user.getFlag_pessoa());
		areaEscolhida=null;
		distinctDepto=null;
		distinctCargos=null;
		departamentoEscolhido=null;
		cargoDe = null;
		
		areaEscolhidaPara=null;
		distinctDeptoPara=null;
		distinctCargosPara=null;
		departamentoEscolhidoPara=null;
		cargoPara=null;
	}
	
	
	public void SelecionaCargosAT() {
		selector = true; 
		this.distinctArea = tbAREAService.findDistinctTbAREAEntitiesDe(user.getFlag_pessoa());
		areaEscolhida=null;
		distinctDepto=null;
		distinctCargos=null;
		departamentoEscolhido=null;
		cargoDe = null;
	}

	public List<TbHABILIDADESCULTCARGOSEntity> InicializaTabelasAuxiliares(TbCARGOSEntity tbCARGOS) {
		this.tbCARGOS = tbCARGOS;

		tbHABILIDADESCULTCARGOSs = tbHABILIDADESCULTCARGOSService.findTbHABILIDADESCULTCARGOSsByIdCARGOS(this.tbCARGOS);

		return tbHABILIDADESCULTCARGOSs;
	}



	public List<TbCOMPETENCIASCARGOSEntity> InicializaTabelasAuxiliaresCO(TbCARGOSEntity tbCARGOS) {
		this.tbCARGOS = tbCARGOS;
		tbCOMPETENCIASCARGOSs = tbCOMPETENCIASCARGOSService.findTbCOMPETENCIASCARGOSsByIdCARGOS(this.tbCARGOS);
		return tbCOMPETENCIASCARGOSs;
	}
	
	public List<TbCOMPETENCIASCARGOSEntity> InicializaTabelasAuxiliaresCO_De(TbCARGOSEntity tbCARGOS, TbCARGOSEntity tbCARGOSPARA) {
		this.tbCARGOS = tbCARGOS;
		tbCOMPETENCIASCARGOSs = tbCOMPETENCIASCARGOSService
				.findTbCOMPETENCIASCARGOSsByIdCARGOS(tbCARGOS,tbCARGOSPARA );
		return tbCOMPETENCIASCARGOSs;
	}
	
	
	public List<TbCOMPETENCIASCARGOSEntity> InicializaTabelasAuxiliaresCO_para(TbCARGOSEntity tbCARGOS, TbCARGOSEntity tbCARGOSPARA) {
		this.tbCARGOS = tbCARGOS;
		List<TbCOMPETENCIASCARGOSEntity> tbCOMPETENCIASCARGOS = tbCOMPETENCIASCARGOSService
				.findTbCOMPETENCIASCARGOSsByIdCARGOSPARA(tbCARGOS,tbCARGOSPARA );
		return tbCOMPETENCIASCARGOS;
	}
	

	public List<TbCOMPETENCIASCARGOSEntity> InicializaTabelasAuxiliaresCO_ED(TbCARGOSEntity tbCARGOS) {
		if(tbCARGOS.getId()==null) {

		}else {
			this.tbCARGOS = tbCARGOS;
			tbCOMPETENCIASCARGOSs = tbCOMPETENCIASCARGOSService.findTbCOMPETENCIASCARGOSsByIdCARGOSOrder(this.tbCARGOS);
		}

		return tbCOMPETENCIASCARGOSs;
	}

	public List<TbGRADECARGOSEntity> InicializaTabelasAuxiliaresGR(TbCARGOSEntity tbCARGOS) {
		this.tbCARGOS = tbCARGOS;
		tbGRADESCARGOSs = tbGRADESCARGOSService.findTbGRADECARGOSsByIdCARGOS(this.tbCARGOS);
		return tbGRADESCARGOSs;
	}
	
	public List<TbGRADECARGOSEntity> InicializaTabelasAuxiliaresGR_De(TbCARGOSEntity tbCARGOS, TbCARGOSEntity tbCARGOSPARA) {
		this.tbCARGOS = tbCARGOS;
		tbGRADESCARGOSs = tbGRADESCARGOSService
				.findTbGRADECARGOSsByIdCARGOS(tbCARGOS,tbCARGOSPARA );
		return tbGRADESCARGOSs;
	}
	
	public List<TbGRADECARGOSEntity> InicializaTabelasAuxiliaresGR_para(TbCARGOSEntity tbCARGOS, TbCARGOSEntity tbCARGOSPARA) {
		this.tbCARGOS = tbCARGOS;
		List<TbGRADECARGOSEntity> tbGRADECARGOS = tbGRADESCARGOSService
				.findTbGRADECARGOSsByIdCARGOSPARA(tbCARGOS,tbCARGOSPARA );
		return tbGRADECARGOS;
	}
	
	
	

	public List<TbGRADECARGOSEntity> InicializaTabelasAuxiliaresGR_ED(TbCARGOSEntity tbCARGOS) {
		if(tbCARGOS.getId()==null) {

		}else {
			this.tbCARGOS = tbCARGOS;
			tbGRADESCARGOSs = tbGRADESCARGOSService.findTbGRADECARGOSsByIdCARGOSOrder(this.tbCARGOS);
		}
		return tbGRADESCARGOSs;
	}


	public List<TbPERFILCARGOSEntity> InicializaTabelasAuxiliaresPE(TbCARGOSEntity tbCARGOS) {
		this.tbCARGOS = tbCARGOS;
		tbPERFILCARGOSs = tbPERFILCARGOSService.findTbPERFILCARGOSsByIdCARGOS(this.tbCARGOS);
		return tbPERFILCARGOSs;
	}
	
	
	public List<TbPERFILCARGOSEntity> InicializaTabelasAuxiliaresPE_De(TbCARGOSEntity tbCARGOS, TbCARGOSEntity tbCARGOSPARA) {
		this.tbCARGOS = tbCARGOS;
		tbPERFILCARGOSs = tbPERFILCARGOSService
				.findTbPERFILCARGOSsByIdCARGOS(tbCARGOS,tbCARGOSPARA );
		return tbPERFILCARGOSs;
	}
	
	public List<TbPERFILCARGOSEntity> InicializaTabelasAuxiliaresPE_para(TbCARGOSEntity tbCARGOS, TbCARGOSEntity tbCARGOSPARA) {
		this.tbCARGOS = tbCARGOS;
		List<TbPERFILCARGOSEntity> tbPERFILCARGOS = tbPERFILCARGOSService
				.findTbPERFILCARGOSsByIdCARGOSPARA(tbCARGOS,tbCARGOSPARA );
		return tbPERFILCARGOS;
	}
	


	public List<TbPERFILCARGOSEntity> InicializaTabelasAuxiliaresPE_ED(TbCARGOSEntity tbCARGOS) {
		if(tbCARGOS.getId()==null) {

		}else {
			this.tbCARGOS = tbCARGOS;
			tbPERFILCARGOSs = tbPERFILCARGOSService.findTbPERFILCARGOSsByIdCARGOSOrder(this.tbCARGOS);
		}
		return tbPERFILCARGOSs;
	}
	

	public List<TbCONHECIMENTOSBASCARGOSEntity> InicializaTabelasAuxiliaresCB(TbCARGOSEntity tbCARGOS) {
		this.tbCARGOS = tbCARGOS;
		tbCONHECIMENTOSBASICOSCARGOSs = tbCONHECIMENTOSBASCARGOSService.findTbCONHECIMENTOSBASCARGOSsByIdCARGOS(tbCARGOS);
		return tbCONHECIMENTOSBASICOSCARGOSs;
	}


	public List<TbCONHECIMENTOSBASCARGOSEntity> InicializaTabelasAuxiliaresCB_De(TbCARGOSEntity tbCARGOS, TbCARGOSEntity tbCARGOSPARA) {
		this.tbCARGOS = tbCARGOS;
		tbCONHECIMENTOSBASICOSCARGOSs = tbCONHECIMENTOSBASCARGOSService
				.findTbCONHECIMENTOSBASCARGOSsByIdCARGOS(tbCARGOS,tbCARGOSPARA );
		return tbCONHECIMENTOSBASICOSCARGOSs;
	}
	
	public List<TbCONHECIMENTOSBASCARGOSEntity> InicializaTabelasAuxiliaresCB_para(TbCARGOSEntity tbCARGOS, TbCARGOSEntity tbCARGOSPARA) {
		this.tbCARGOS = tbCARGOS;
		List<TbCONHECIMENTOSBASCARGOSEntity> tbCONHECIMENTOSBASICOSCARGOS = tbCONHECIMENTOSBASCARGOSService
				.findTbCONHECIMENTOSBASCARGOSsByIdCARGOSPARA(tbCARGOS,tbCARGOSPARA );
		return tbCONHECIMENTOSBASICOSCARGOS;
	}

	public List<TbCONHECIMENTOSBASCARGOSEntity> InicializaTabelasAuxiliaresCB_ED(TbCARGOSEntity tbCARGOS) {
		if(tbCARGOS.getId()==null) {

		}else {
			this.tbCARGOS = tbCARGOS;
			tbCONHECIMENTOSBASICOSCARGOSs = tbCONHECIMENTOSBASCARGOSService
					.findTbCONHECIMENTOSBASCARGOSsByIdCARGOSOrder(this.tbCARGOS);
		}
		return tbCONHECIMENTOSBASICOSCARGOSs;
	}

	public List<TbCONHECIMENTOSESPCARGOSEntity> InicializaTabelasAuxiliaresCE(TbCARGOSEntity tbCARGOS) {
		this.tbCARGOS = tbCARGOS;
		tbCONHECIMENTOSESPCARGOSs = tbCONHECIMENTOSESPCARGOSService
				.findTbCONHECIMENTOSESPCARGOSsByIdCARGOS(tbCARGOS);
		return tbCONHECIMENTOSESPCARGOSs;
	}


	public List<TbCONHECIMENTOSESPCARGOSEntity> InicializaTabelasAuxiliaresCE_De(TbCARGOSEntity tbCARGOS, TbCARGOSEntity tbCARGOSPARA) {
		this.tbCARGOS = tbCARGOS;
		tbCONHECIMENTOSESPCARGOSs = tbCONHECIMENTOSESPCARGOSService
				.findTbCONHECIMENTOSESPCARGOSsByIdCARGOS(tbCARGOS,tbCARGOSPARA );
		return tbCONHECIMENTOSESPCARGOSs;
	}
	
	public List<TbCONHECIMENTOSESPCARGOSEntity> InicializaTabelasAuxiliaresCE_PARA(TbCARGOSEntity tbCARGOS, TbCARGOSEntity tbCARGOSPARA) {
		this.tbCARGOS = tbCARGOS;
		List<TbCONHECIMENTOSESPCARGOSEntity>tbCONHECIMENTOSESPCARGOS = tbCONHECIMENTOSESPCARGOSService
				.findTbCONHECIMENTOSESPCARGOSsByIdCARGOSPARA(tbCARGOS,tbCARGOSPARA );
		return tbCONHECIMENTOSESPCARGOS;
	}

	public List<TbCONHECIMENTOSESPCARGOSEntity> InicializaTabelasAuxiliaresCE_ED(TbCARGOSEntity tbCARGOS) {
		if(tbCARGOS.getId()==null) {

		}else {
			this.tbCARGOS = tbCARGOS;
			tbCONHECIMENTOSESPCARGOSs = tbCONHECIMENTOSESPCARGOSService
					.findTbCONHECIMENTOSESPCARGOSsByIdCARGOSOrder(this.tbCARGOS);
		}
		return tbCONHECIMENTOSESPCARGOSs;
	}

	// 100% - ((Cargo Para - Cargo De) * Variação no GAP)
	public void calculaConhecimentosEspecificosDEPARA(TbCARGOSEntity tbCARGOSDe, TbCARGOSEntity tbCARGOSPara) {
		List<TbNivelEscolaridadeEntity> listaFormacao = new ArrayList<TbNivelEscolaridadeEntity>();
		listCECARGOSDe = InicializaTabelasAuxiliaresCE_De(tbCARGOSDe,tbCARGOSPara);
		listCECARGOSPara = InicializaTabelasAuxiliaresCE_PARA(tbCARGOSDe, tbCARGOSPara);
		
	
		
		
		listGapCE.clear();
		Double somaListaGap = 0.0;
		flagCultura = 1;
		int i =0;
		try {
			for (i = 0; i < listCECARGOSPara.size(); i++) {
				
				if(listCECARGOSDe.get(i).getPoNTUACAOCONESP() ==null) {
					listCECARGOSDe.get(i).setPoNTUACAOCONESP(0.0);
				}
				
				if(listCECARGOSPara.get(i).getPoNTUACAOCONESP() ==null) {
					listCECARGOSPara.get(i).setPoNTUACAOCONESP(0.0);
				}
					else {
						TbCONHECIMENTOSESPCARGOSEntity gap = new TbCONHECIMENTOSESPCARGOSEntity();					
						double gapdePARA = 0;

						if(listCECARGOSPara.get(i).getIdCONHECESP().getBloqueiaMovConhecEsp()==null) {
							
						}else {
						
						
						if(listCECARGOSPara.get(i).getIdCONHECESP().getBloqueiaMovConhecEsp().equals("SIM") &&
								(listCECARGOSPara.get(i).getPoNTUACAOCONESP() >=1) && 
								(listCECARGOSDe.get(i).getPoNTUACAOCONESP()==0)) {
							flagDowngrade = true;
						}
						}

							if(listCECARGOSPara.get(i).getIdCONHECESP().getDeSCCONHECIMENTOSESPECIFICOS().equals(
									listCECARGOSDe.get(i).getIdCONHECESP().getDeSCCONHECIMENTOSESPECIFICOS())) {

								gapDeParaCE = listCECARGOSPara.get(i).getPoNTUACAOCONESP()
										- listCECARGOSDe.get(i).getPoNTUACAOCONESP();	

								if(gapDeParaCE<0) {
									gapDeParaCE =0;
								}
								if(gapDeParaCE==0) {
							
								}else {
									gapdePARA = gapDeParaCE * listCECARGOSDe.get(i).getIdCONHECESP().getPenalidadeConhecBas();	
								}
								gap.setPoNTUACAOCONESP(gapdePARA);
								listGapCE.add(gap);
							}
						
						somaListaGap += gapdePARA;

					}

				}
				
		

			// calculo de aderência
			aderenciaCE = 100 - somaListaGap;
			// aderenciaCE = round(aderenciaCE,2);

			// Se aderencia maior que 100%, força o valor
			if (aderenciaCE > 100) {
				aderenciaCE = 100.0;
			}
			// Se aderência menor que 0%, força o valor
			else if (aderenciaCE < -100) {
				aderenciaCE = -100.0;
			}
			
			
			listaFormacao = tbNIVELESCOLARIDADEService.findAlltbNIVELESCOLARIDADEEntities();
			
			for(int i2 =0; i2<listCECARGOSDe.size();i2++) {
				
				if(listCECARGOSDe.get(i2).getIdCONHECESP().getGrupo()==null) {
					
				}else {
					
					for (int j = 0; j<listaFormacao.size();j++) {
						if(listCECARGOSDe.get(i2).getIdCONHECESP().getGrupo().equals(listaFormacao.get(j).getGrupo())	
						&& (listCECARGOSDe.get(i2).getPoNTUACAOCONESP().intValue()==listaFormacao.get(j).getIdFormacao())){
								
								listCECARGOSDe.get(i2).setMascara(listaFormacao.get(j).getDescFormacao());
								
							
							
						}
					}
				}
				
				}
			
			
				for(int i2 =0; i2<listCECARGOSPara.size();i2++) {
				
				if(listCECARGOSPara.get(i2).getIdCONHECESP().getGrupo()==null) {
					
				}else {
					
					for (int j = 0; j<listaFormacao.size();j++) {
						if(listCECARGOSPara.get(i2).getIdCONHECESP().getGrupo().equals(listaFormacao.get(j).getGrupo())	
						&& (listCECARGOSPara.get(i2).getPoNTUACAOCONESP().intValue()==listaFormacao.get(j).getIdFormacao())){
								
								listCECARGOSPara.get(i2).setMascara(listaFormacao.get(j).getDescFormacao());
								
							
							
						}
					}
				}
				
				}
			
			
		} catch (IndexOutOfBoundsException ex) {

		}
	}

	// 100% - ((Cargo Para - Cargo De) * Variação no GAP)
	public void calculaConhecimentosBasicosDEPARA(TbCARGOSEntity tbCARGOSDe, TbCARGOSEntity tbCARGOSPara) {

		@SuppressWarnings("unused")
		List<TbCONHECIMENTOSBASCARGOSEntity> listzeros = new ArrayList<TbCONHECIMENTOSBASCARGOSEntity>();
		List<TbNivelEscolaridadeEntity> listaFormacao = new ArrayList<TbNivelEscolaridadeEntity>();
		listCBCARGOSDe = InicializaTabelasAuxiliaresCB_De(tbCARGOSDe,tbCARGOSPara);
		listCBCARGOSPara = InicializaTabelasAuxiliaresCB_para(tbCARGOSDe, tbCARGOSPara);
		listaFormacao = tbNIVELESCOLARIDADEService.findAlltbNIVELESCOLARIDADEEntities();
		
		
		

		listGapCB.clear();
		Double somaListaGap = 0.0;
		flagCultura = 1;
		int i =0; 
		try {
			for (i = 0; i < listCBCARGOSPara.size(); i++) {
				
						if(listCBCARGOSDe.get(i).getPoNTUACAOCONBAS() ==null) {
							listCBCARGOSDe.get(i).setPoNTUACAOCONBAS(0.0);
						}
						
						if(listCBCARGOSPara.get(i).getPoNTUACAOCONBAS() ==null) {
							listCBCARGOSPara.get(i).setPoNTUACAOCONBAS(0.0);
						}

						TbCONHECIMENTOSBASCARGOSEntity gap = new TbCONHECIMENTOSBASCARGOSEntity();

						double gapdePARA = 0;
						
						if(listCBCARGOSPara.get(i).getIdCONHECBAS().getBloqueiaMovConhecBas()==null) {
							
						}else {
						
						if(listCBCARGOSPara.get(i).getIdCONHECBAS().getBloqueiaMovConhecBas().equals("SIM") &&
								(listCBCARGOSPara.get(i).getPoNTUACAOCONBAS() >=1) && 
								(listCBCARGOSDe.get(i).getPoNTUACAOCONBAS()==0)) {
							flagDowngrade = true;
						}
						}
						

							if(listCBCARGOSPara.get(i).getIdCONHECBAS().getDeSCCONHECIMENTOSBASICOS().equals(listCBCARGOSDe.get(i).getIdCONHECBAS().getDeSCCONHECIMENTOSBASICOS())) {
								gapDeParaCB = listCBCARGOSPara.get(i).getPoNTUACAOCONBAS()
										- listCBCARGOSDe.get(i).getPoNTUACAOCONBAS();
								if (gapDeParaCB < 0) {
									gapDeParaCB = 0;
								}else {

									if (gapDeParaCB == 0) {
									} else {
										gapdePARA = gapDeParaCB * listCBCARGOSDe.get(i).getIdCONHECBAS().getPenalidadeConhecBas();
									}
								}
								gap.setPoNTUACAOCONBAS(gapdePARA);
								listGapCB.add(gap);
							}
						
						somaListaGap += gapdePARA;	
					}
			// calculo de aderência
			aderenciaCB = 100 - (somaListaGap);
			// aderenciaCB = round(aderenciaCB,2);

			// Se aderencia maior que 100%, força o valor
			if (aderenciaCB > 100) {
				aderenciaCB = 100.0;
			}
			// Se aderência menor que 0%, força o valor
			else if (aderenciaCB < -100) {
				aderenciaCB = -100.0;
			}
			
			for(int i2 =0; i2<listCBCARGOSDe.size();i2++) {
				
				if(listCBCARGOSDe.get(i2).getIdCONHECBAS().getGrupo()==null) {
					
				}else {
					
					for (int j = 0; j<listaFormacao.size();j++) {
						if(listCBCARGOSDe.get(i2).getIdCONHECBAS().getGrupo().equals(listaFormacao.get(j).getGrupo())	
						&& (listCBCARGOSDe.get(i2).getPoNTUACAOCONBAS().intValue()==listaFormacao.get(j).getIdFormacao())){
								
								listCBCARGOSDe.get(i2).setMascara(listaFormacao.get(j).getDescFormacao());
								
							
							
						}
					}
				}
				
				}
			
			
			
			for(int i2 =0; i2<listCBCARGOSPara.size();i2++) {
				
				if(listCBCARGOSPara.get(i2).getIdCONHECBAS().getGrupo()==null) {
					
				}else {
					
					for (int j = 0; j<listaFormacao.size();j++) {
						if(listCBCARGOSPara.get(i2).getIdCONHECBAS().getGrupo().equals(listaFormacao.get(j).getGrupo())	
						&& (listCBCARGOSPara.get(i2).getPoNTUACAOCONBAS().intValue()==listaFormacao.get(j).getIdFormacao())){
								
								listCBCARGOSPara.get(i2).setMascara(listaFormacao.get(j).getDescFormacao());
								
							
							
						}
					}
				}
				
				}
			
			
			
		} catch (IndexOutOfBoundsException ex) {

		}
	}

	// 100% - ((Cargo Para - Cargo De) * Variação no GAP)
	public void calculaCompetenciasDEPARA(TbCARGOSEntity tbCARGOSDe, TbCARGOSEntity tbCARGOSPara) {

		listCOCARGOSDe = InicializaTabelasAuxiliaresCO_De(tbCARGOSDe,tbCARGOSPara);
		listCOCARGOSPara =  InicializaTabelasAuxiliaresCO_para(tbCARGOSDe, tbCARGOSPara);
		listGap.clear();
		Double somaListaGap = 0.0;
		flagCultura = 1;
		int i =0;
		try {

			for (i=0; i < listCOCARGOSPara.size(); i++) {

				if(listCOCARGOSDe.get(i).getPoNTUACAOCOMPETENCIA() ==null) {
					listCOCARGOSDe.get(i).setPoNTUACAOCOMPETENCIA(0.0);
				}
				
				if(listCOCARGOSPara.get(i).getPoNTUACAOCOMPETENCIA() ==null) {
					listCOCARGOSPara.get(i).setPoNTUACAOCOMPETENCIA(0.0);
				}
				else {
						TbCOMPETENCIASCARGOSEntity gap = new TbCOMPETENCIASCARGOSEntity();
						double gapdePARA = 0;
						
						if(listCOCARGOSPara.get(i).getIdCOMPETENCIAS().getBloqueiaMovCompetencias()==null) {
							
						}else {
						
						if(listCOCARGOSPara.get(i).getIdCOMPETENCIAS().getBloqueiaMovCompetencias().equals("SIM") &&
								(listCOCARGOSPara.get(i).getPoNTUACAOCOMPETENCIA() >=1) && 
								(listCOCARGOSDe.get(i).getPoNTUACAOCOMPETENCIA()==0)) {
							flagDowngrade = true;
						}
						}
						


							if(listCOCARGOSPara.get(i).getIdCOMPETENCIAS().getDeSCCOMPETENCIA().equals(
									listCOCARGOSDe.get(i).getIdCOMPETENCIAS().getDeSCCOMPETENCIA())) {

								gapDePara = listCOCARGOSPara.get(i).getPoNTUACAOCOMPETENCIA()
										- listCOCARGOSDe.get(i).getPoNTUACAOCOMPETENCIA();	

								if(gapDePara<0.0) {
									gapDePara =0.0;
								}
								if(gapDePara==0.0) {

								}else {
									gapdePARA = gapDePara * listCOCARGOSDe.get(i).getIdCOMPETENCIAS().getPenalidadeCompetencias();	
								}
								gap.setPoNTUACAOCOMPETENCIA(gapdePARA);
								listGap.add(gap);
							}
						
						somaListaGap += gapdePARA;

					}

				}


			// calculo de aderência
			aderencia = 100 - somaListaGap;
			// aderencia = round(aderencia,2);

			// Se aderencia maior que 100%, força o valor
			if (aderencia > 100) {
				aderencia = 100.0;
			}
			// Se aderência menor que 0%, força o valor
			else if (aderencia < -100) {
				aderencia = -100.0;
			}
		} catch (IndexOutOfBoundsException ex) {

		}
	}

	// 100% - (((módulo(Cargo Para - Cargo De)) -4) * Variação no GAP)
	public void calculaPerfilDEPARA(TbCARGOSEntity tbCARGOSDe, TbCARGOSEntity tbCARGOSPara) {
		List<TbNivelEscolaridadeEntity> listaFormacao = new ArrayList<TbNivelEscolaridadeEntity>();
		listPECARGOSDe = InicializaTabelasAuxiliaresPE_De(tbCARGOSDe,tbCARGOSPara);
		listPECARGOSPara = InicializaTabelasAuxiliaresPE_para(tbCARGOSDe, tbCARGOSPara);
		
		
	
		
		
		
		listGapPE.clear();
		Double somaListaGapPE = 0.0;
		flagCultura = 1;
		int i =0;

		try {
			for (i = 0; i < listPECARGOSPara.size(); i++) {

				if(listPECARGOSDe.get(i).getPoNTUACAOPERFIL() ==null) {
					listPECARGOSDe.get(i).setPoNTUACAOPERFIL(0.0);
				}
				
				if(listPECARGOSPara.get(i).getPoNTUACAOPERFIL() ==null) {
					listPECARGOSPara.get(i).setPoNTUACAOPERFIL(0.0);
				}
				
				else {
						TbPERFILCARGOSEntity gap = new TbPERFILCARGOSEntity();
						double gapdePARA = 0;

						
						if(listPECARGOSPara.get(i).getIdPERFIL().getBloqueiaMovConhecPerfil()==null) {
							
						}else {
						
						if(listPECARGOSPara.get(i).getIdPERFIL().getBloqueiaMovConhecPerfil().equals("SIM") &&
								(listPECARGOSPara.get(i).getPoNTUACAOPERFIL() >=1) && 
								(listPECARGOSDe.get(i).getPoNTUACAOPERFIL()==0)
								
								) {
							flagDowngrade = true;
						}
						}
							if(listPECARGOSPara.get(i).getIdPERFIL().getDeSCPERFIL().equals(listPECARGOSDe.get(i).getIdPERFIL().getDeSCPERFIL())) {

								gapDeParaPE = listPECARGOSPara.get(i).getPoNTUACAOPERFIL() - listPECARGOSDe.get(i).getPoNTUACAOPERFIL();
								gapDeParaPE = Math.abs(gapDeParaPE);


								if(gapDeParaPE==0) {

								}else {
									gapdePARA = gapDeParaPE * listPECARGOSDe.get(i).getIdPERFIL().getPenalidadeConhecPerfil();
								}
								gap.setPoNTUACAOPERFIL(gapdePARA);
								listGapPE.add(gap);
								somaListaGapPE+=gapdePARA;
							}
						}
						
					}
				

			// calculo de aderência
			aderenciaPE = 100 - somaListaGapPE;
			// aderenciaPE = round(aderenciaPE,2);

			// Se aderencia maior que 100%, força o valor
			if (aderenciaPE > 100) {
				aderenciaPE = 100.0;
			}
			// Se aderência menor que 0%, força o valor
			else if (aderenciaPE < -100) {
				aderenciaPE = -100.0;
			}
			
			listaFormacao = tbNIVELESCOLARIDADEService.findAlltbNIVELESCOLARIDADEEntities();
			
			for(int i2 =0; i2<listPECARGOSDe.size();i2++) {
				
				if(listPECARGOSDe.get(i2).getIdPERFIL().getGrupo()==null) {
					
				}else {
					
					for (int j = 0; j<listaFormacao.size();j++) {
						if(listPECARGOSDe.get(i2).getIdPERFIL().getGrupo().equals(listaFormacao.get(j).getGrupo())	
						&& (listPECARGOSDe.get(i2).getPoNTUACAOPERFIL().intValue()==listaFormacao.get(j).getIdFormacao())){
								
								listPECARGOSDe.get(i2).setMascara(listaFormacao.get(j).getDescFormacao());
								
							
							
						}
					}
				}
				
				}
			
			
			
			for(int i2 =0; i2<listPECARGOSPara.size();i2++) {
				
				if(listPECARGOSPara.get(i2).getIdPERFIL().getGrupo()==null) {
					
				}else {
					
					for (int j = 0; j<listaFormacao.size();j++) {
						if(listPECARGOSPara.get(i2).getIdPERFIL().getGrupo().equals(listaFormacao.get(j).getGrupo())	
						&& (listPECARGOSPara.get(i2).getPoNTUACAOPERFIL().intValue()==listaFormacao.get(j).getIdFormacao())){
								
								listPECARGOSPara.get(i2).setMascara(listaFormacao.get(j).getDescFormacao());
								
							
							
						}
					}
				}
				
				}
			
			
		} catch (IndexOutOfBoundsException ex) {

		}
	}

	public void calculaGRADESDEPARA(TbCARGOSEntity tbCARGOSDe, TbCARGOSEntity tbCARGOSPara) {
		flagEstaganado = "false";
		aderenciaGR = 0.0;
		listGRCARGOSDe = InicializaTabelasAuxiliaresGR_De(tbCARGOSDe,tbCARGOSPara);
		listGRCARGOSPara = InicializaTabelasAuxiliaresGR_para(tbCARGOSDe,tbCARGOSPara);
		listGapGR.clear();
		Double somaListaGapGR = 0.0;
		flagDowngrade = false;
		flagCultura = 1;
		int i =0;
		try {
			for (i = 0; i < listGRCARGOSDe.size(); i++) {


				if(listGRCARGOSDe.get(i).getPoNTUACAOGRADE() ==null) {
					listGRCARGOSDe.get(i).setPoNTUACAOGRADE(0.0);
				}
				
				if(listGRCARGOSPara.get(i).getPoNTUACAOGRADE() ==null) {
					listGRCARGOSPara.get(i).setPoNTUACAOGRADE(0.0);
				}  
					else {
						TbGRADECARGOSEntity gap = new TbGRADECARGOSEntity();
						double gapdePARA = 0;
						if(listGRCARGOSPara.get(i).getIdGRADE().getDeSCGRADE().equals(listGRCARGOSDe.get(i).getIdGRADE().getDeSCGRADE())){

							gapDeParaGR = listGRCARGOSPara.get(i).getPoNTUACAOGRADE() - listGRCARGOSDe.get(i).getPoNTUACAOGRADE();

							if(gapDeParaGR <0) {
								flagDowngrade = true;
								aderenciaGR = 0.0;
								aderenciaFinal =0.0;
								aderencia = 0.0;
								aderenciaCB = 0.0;
								aderenciaCE =0.0;
								aderenciaGR =0.0;
								aderenciaPE= 0.0;
								gapDeParaGR=0.0;
								corFinal = "#F2F2F2";
								
							}
							if(gapDeParaGR==0) {
								flagEstaganado = "True";
							} else {
								gapdePARA = gapDeParaGR * listGRCARGOSDe.get(i).getIdGRADE().getPenalidadeConhecGrade();
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
			else if (aderenciaGR < -100) {
				aderenciaGR = -100.0;
			}
		} catch (IndexOutOfBoundsException ex) {

		}
	}

	public void calculaAderenciasParciais(TbCARGOSEntity tbCARGOSDe, TbCARGOSEntity tbCARGOSPara) {

		if ((tbCARGOSDe == null) || (tbCARGOSPara == null)) {

		} else {

			if (tbCARGOSDe.getId() == tbCARGOSPara.getId()) {
				corFinal = "#16600A";
			}

			calculaGRADESDEPARA(tbCARGOSDe, tbCARGOSPara);

			calculaCompetenciasDEPARA(tbCARGOSDe, tbCARGOSPara);

			calculaPerfilDEPARA(tbCARGOSDe, tbCARGOSPara);

			calculaConhecimentosBasicosDEPARA(tbCARGOSDe, tbCARGOSPara);

			calculaConhecimentosEspecificosDEPARA(tbCARGOSDe, tbCARGOSPara);

			

			if (flagDowngrade == true) {
				avisoDowngrade = "Downgrade";
				corBarraAderenciaFinal = "barraProgressoCinza";
				corFinal = "#F2F2F2";
				aderenciaFinal = 0.0;
				aderencia = 0.0;
				aderenciaCB = 0.0;
				aderenciaCE = 0.0;
				aderenciaGR = 0.0;
				aderenciaPE = 0.0;
			} else {
				calculaAderenciaFinal(tbCARGOSDe, tbCARGOSPara);
			}
		}
	}

	public void calculaAderenciaFinal(TbCARGOSEntity tbCARGOSDe, TbCARGOSEntity tbCARGOSPara) {
		if (flagDowngrade == true) {
			avisoDowngrade = "Bloqueado";
			corBarraAderenciaFinal = "barraProgressoCinza";
			corFinal = "#F2F2F2";
			aderenciaFinal = 0.0;
			aderencia = 0.0;
			aderenciaCB = 0.0;
			aderenciaCE = 0.0;
			aderenciaGR = 0.0;
			aderenciaPE = 0.0;
		} else {

			aderenciaFinal = ((pesoCO * aderencia) + (pesoNH * aderenciaGR) + (pesoPE * aderenciaPE)
					+ (pesoCB * aderenciaCB) + (pesoCE * aderenciaCE)) / 100;
			
			if(aderenciaFinal<0.0) {
				aderenciaFinal=0.0;
			}
			
			if (aderenciaFinal == 0.0) {
				corFinal = "#F2F2F2";
			} else {

				if ((aderenciaFinal > 0.0) && (aderenciaFinal <= 20.0)) {
					corBarraAderenciaFinal = "barraProgressoVermelha";
					corFinal = "#CC0D00";
				} else {
					if ((aderenciaFinal > 20.0) && (aderenciaFinal <= 40.0)) {
						corBarraAderenciaFinal = "barraProgressoAmarelaVermelha";
						corFinal = "#D83C11";
					} else {
						if ((aderenciaFinal > 40.0) && (aderenciaFinal <= 60.0)) {
							corBarraAderenciaFinal = "barraProgressoAmarela";
							corFinal = "#DD8C00";
						} else {
							if ((aderenciaFinal > 60.0) && (aderenciaFinal <= 80.0)) {
								corBarraAderenciaFinal = "barraProgressoAmarelaVerde";
								corFinal = "#869E00";
							} else if (aderenciaFinal > 80.0) {
								corBarraAderenciaFinal = "barraProgressoVerde";
								corFinal = "#16600A";
							}
						}
					}

				}
			}
		}
	}

	public void onDialogOpen(TbCARGOSEntity tbCARGOS) {
		reset();
		changeHeaderEditar();
		this.tbCARGOS = tbCARGOS;
		this.tbCARGOS.setReSPONSABILIDADES(this.tbCARGOS.getReSPONSABILIDADES().replace("<br>", "\n"));
	}

	public String detalhesComportamentos(Double poNTUACAOHABCARGOS) {
		for (int i = 0; i < tbHABILIDADESCARGOSs.size(); i++) {
			if (poNTUACAOHABCARGOS.toString().equals("3")) {
				setDetalhesComportamento("Hibrido");
			} else if (poNTUACAOHABCARGOS.toString().equals("4")) {
				setDetalhesComportamento("Amplitude");
			}
		}
		return detalhesComportamento;
	}

	public String persist() {

		tbCARGOS.setDeSCCARGO(tbCARGOS.getDeSCCARGO().replaceAll("[^\\p{ASCII}]", "").replaceAll("\\W", " ").trim().toUpperCase());
		String message="";
		String duplicado="";
		try {
			allIdCARGOSsList = tbCARGOSService.AllTbCARGOSEntities();
			for (int i =0; i<allIdCARGOSsList.size();i++) {
				if(tbCARGOS.getDeSCCARGO().equals(allIdCARGOSsList.get(i).getDeSCCARGO())) {
					duplicado = "S";
					this.tbCARGOS =null;
					break;
				}
				else {
					duplicado="N";
				}
			}

			if(duplicado.equals("S")) {

			}else {

				if (tbCARGOS.getId() != null) {
					tbCARGOS = tbCARGOSService.update(tbCARGOS);
					message = "message_successfully_updated";
				} else {
					tbCARGOS = tbCARGOSService.save(tbCARGOS);
					message = "message_successfully_created";
				}
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

		if(this.tbCARGOS==null) {
			message = "messageduplicado";
		}else {
			flagCultura = 1;
			setFlagCultura(1);
		}


		FacesMessage facesMessage = MessageFactory.getMessage(message);
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);

		return null;
	}


	public String persistEdicao(TbCARGOSEntity tbCARGOSEdicao) {

		tbCARGOSEdicao.setDeSCCARGO(tbCARGOSEdicao.getDeSCCARGO().replaceAll("[^\\p{ASCII}]", "").replaceAll("\\W", " ").trim().toUpperCase());
		String message="";
		try {


			if (tbCARGOSEdicao.getId() != null) {
				tbCARGOSEdicao = tbCARGOSService.update(tbCARGOSEdicao);
				message = "message_successfully_updated";
				this.tbCARGOSEdicao = tbCARGOSService.find((long) id);
				
			} else {
				tbCARGOSEdicao = tbCARGOSService.save(tbCARGOSEdicao);
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
		tbCARGOSList = null;
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
	
	
	public List<TbCARGOSEntity> getTbPESSOASList() {
		
		if (tbPESSOASList == null) {
			tbPESSOASList= tbCARGOSService.findAllTbPESSOASEntities();
		}
		return tbPESSOASList;
	}


	public void setTbPESSOASList(List<TbCARGOSEntity> tbPESSOASList) {
		this.tbPESSOASList = tbPESSOASList;
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

	public Double getGapDePara() {
		return gapDePara;
	}

	public void setGapDePara(Double gapDePara) {
		this.gapDePara = gapDePara;
	}

	public int getGapVar() {
		return gapVar;
	}

	public void setGapVar(int gapVar) {
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

	public double getGapDeParaGR() {
		return gapDeParaGR;
	}

	public void setGapDeParaGR(int gapDeParaGR) {
		this.gapDeParaGR = gapDeParaGR;
	}

	public int getGapVarGR() {
		return gapVarGR;
	}

	public void setGapVarGR(int gapVarGR) {
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

	public String getavisoDowngrade() {
		return avisoDowngrade;
	}

	public void setavisoDowngrade(String avisoDowngrade) {
		this.avisoDowngrade = avisoDowngrade;
	}

	public int getGapDeParaMO() {
		return gapDeParaMO;
	}

	public void setGapDeParaMO(int gapDeParaMO) {
		this.gapDeParaMO = gapDeParaMO;
	}

	public int getGapVarMO() {
		return gapVarMO;
	}

	public void setGapVarMO(int gapVarMO) {
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

	public double getGapDeParaPE() {
		return gapDeParaPE;
	}

	public void setGapDeParaPE(int gapDeParaPE) {
		this.gapDeParaPE = gapDeParaPE;
	}

	public int getGapVarPE() {
		return gapVarPE;
	}

	public void setGapVarPE(int gapVarPE) {
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

	public double getGapDeParaCB() {
		return gapDeParaCB;
	}

	public void setGapDeParaCB(int gapDeParaCB) {
		this.gapDeParaCB = gapDeParaCB;
	}

	public int getGapVarCB() {
		return gapVarCB;
	}

	public void setGapVarCB(int gapVarCB) {
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

	public double getGapDeParaCE() {
		return gapDeParaCE;
	}

	public void setGapDeParaCE(int gapDeParaCE) {
		this.gapDeParaCE = gapDeParaCE;
	}

	public int getGapVarCE() {
		return gapVarCE;
	}

	public void setGapVarCE(int gapVarCE) {
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

	public boolean isFlagDowngrade() {
		return flagDowngrade;
	}

	public void setFlagDowngrade(boolean flagDowngrade) {
		this.flagDowngrade = flagDowngrade;
	}

	public String getmsgMovimentacaoFalha() {
		return msgMovimentacaoFalha;
	}

	public void setmsgMovimentacaoFalha(String msgMovimentacaoFalha) {
		this.msgMovimentacaoFalha = msgMovimentacaoFalha;
	}

	 public List<String> getDistinctArea() {
		if(distinctArea==null) {
		this.distinctArea = tbAREAService.findDistinctTbAREAEntitiesDe(user.getFlag_pessoa());
		}
		return distinctArea;
	}

	public void setDistinctArea(List<String> distinctArea) {
		this.distinctArea = distinctArea;
	}

	public List<String> getDistinctDepto() {
		if (areaEscolhida != null) {
			this.distinctDepto = tbAREAService.findDistinctTbDEPTOEntities(areaEscolhida);

		}else {
			this.distinctDepto = null;
		}
		return distinctDepto;
	}

	public void setDistinctDepto(List<String> distinctDepto) {
		this.distinctDepto = distinctDepto;
	}

	public List<TbCARGOSEntity> getDistinctCargos() {
		if (departamentoEscolhido != null) {
			
			if(selector==true) {
				this.distinctCargos = tbAREAService.findDistinctTbCARGOSEntities(departamentoEscolhido, areaEscolhida, "NAO");

			}else {
				this.distinctCargos = tbAREAService.findDistinctTbCARGOSEntities(departamentoEscolhido, areaEscolhida, "SIM");
			}
			
			
		}else {
			this.distinctCargos =null;
		}
		return distinctCargos;
	}
	
	
	public List<TbCARGOSEntity> getDistinctCargosPara() {
		if (departamentoEscolhidoPara != null) {
			if(selector==true) {
			this.distinctCargosPara = tbAREAService.findDistinctTbCARGOSEntities(departamentoEscolhidoPara,
					areaEscolhidaPara,"NAO");
			}
			else {
				this.distinctCargosPara = tbAREAService.findDistinctTbCARGOSEntities(departamentoEscolhidoPara,
						areaEscolhidaPara,"NAO");
			}
			
		}
		return distinctCargosPara;
	}


	public void teste() {
		System.out.println(cargoDe.getDeSCCARGO());
		System.out.println(cargoPara.getDeSCCARGO());
	}

	public void setDistinctCargos(List<TbCARGOSEntity> distinctCargos) {
		this.distinctCargos = distinctCargos;
	}

	public String getAreaEscolhida() {
		return areaEscolhida;
	}

	public void setAreaEscolhida(String areaEscolhida) {
		this.areaEscolhida = areaEscolhida;
	}

	public String getDepartamentoEscolhido() {
		return departamentoEscolhido;
	}

	public void setDepartamentoEscolhido(String departamentoEscolhido) {
		this.departamentoEscolhido = departamentoEscolhido;
	}

	public List<String> getDistinctAreaPara() {

		this.distinctAreaPara = tbAREAService.findDistinctTbAREAEntitiesPara();

		return distinctAreaPara;
	}

	public void setDistinctAreaPara(List<String> distinctAreaPara) {
		this.distinctAreaPara = distinctAreaPara;
	}

	public List<String> getDistinctDeptoPara() {
		if (areaEscolhidaPara != null) {
			this.distinctDeptoPara = tbAREAService.findDistinctTbDEPTOEntities(areaEscolhidaPara);

		}
		return distinctDeptoPara;
	}

	public void setDistinctDeptoPara(List<String> distinctDeptoPara) {
		this.distinctDeptoPara = distinctDeptoPara;
	}

	
	public void setDistinctCargosPara(List<TbCARGOSEntity> distinctCargosPara) {
		this.distinctCargosPara = distinctCargosPara;
	}

	public String getAreaEscolhidaPara() {
		return areaEscolhidaPara;
	}

	public void setAreaEscolhidaPara(String areaEscolhidaPara) {
		this.areaEscolhidaPara = areaEscolhidaPara;
	}

	public String getDepartamentoEscolhidoPara() {
		return departamentoEscolhidoPara;
	}

	public void setDepartamentoEscolhidoPara(String departamentoEscolhidoPara) {
		this.departamentoEscolhidoPara = departamentoEscolhidoPara;
	}

	public String getCorBarraAderenciaFinal() {
		return corBarraAderenciaFinal;
	}

	public void setCorBarraAderenciaFinal(String corBarraAderenciaFinal) {
		this.corBarraAderenciaFinal = corBarraAderenciaFinal;
	}

	public String getCorFinal() {
		return corFinal;
	}

	public void setCorFinal(String corFinal) {
		this.corFinal = corFinal;
	}

	public double getPesoCO() {
		return pesoCO;
	}

	public void setPesoCO(double pesoCO) {
		this.pesoCO = pesoCO;
	}

	public double getPesoNH() {
		return pesoNH;
	}

	public void setPesoNH(double pesoNH) {
		this.pesoNH = pesoNH;
	}

	public double getPesoPE() {
		return pesoPE;
	}

	public void setPesoPE(double pesoPE) {
		this.pesoPE = pesoPE;
	}

	public double getPesoCB() {
		return pesoCB;
	}

	public void setPesoCB(double pesoCB) {
		this.pesoCB = pesoCB;
	}

	public double getPesoCE() {
		return pesoCE;
	}

	public void setPesoCE(double pesoCE) {
		this.pesoCE = pesoCE;
	}

	public List<TbPONTCARGOSEntity> getTbPesos() {

		return tbPesos;
	}

	public void setTbPesos(List<TbPONTCARGOSEntity> tbPesos) {
		this.tbPesos = tbPesos;
	}

	public List<TbMATRIZCARGOSEntityTrilhas> getListaTrilhas() {
		return listaTrilhas;
	}

	public void setListaTrilhas(List<TbMATRIZCARGOSEntityTrilhas> listaTrilhas) {
		this.listaTrilhas = listaTrilhas;
	}

	public List<TbCARGOSEntity> getListaCargosTrilhas() {
		return listaCargosTrilhas;
	}

	public void setListaCargosTrilhas(List<TbCARGOSEntity> listaCargosTrilhas) {
		this.listaCargosTrilhas = listaCargosTrilhas;
	}

	public double getAderenciaMinima() {
		return aderenciaMinima;
	}

	public void setAderenciaMinima(double aderenciaMinima) {
		this.aderenciaMinima = aderenciaMinima;
	}

	public TbMATRIZCARGOSEntityTrilhas getSelectedTrilha() {
		return selectedTrilha;
	}

	public void setSelectedTrilha(TbMATRIZCARGOSEntityTrilhas selectedTrilha) {
		this.selectedTrilha = selectedTrilha;
	}

	public List<TbMATRIZCARGOSEntityTrilhas> getListaTrilhasn2() {
		return listaTrilhasn2;
	}

	public void setListaTrilhasn2(List<TbMATRIZCARGOSEntityTrilhas> listaTrilhasn2) {
		this.listaTrilhasn2 = listaTrilhasn2;
	}

	public boolean isFlagBloqueio() {
		return flagBloqueio;
	}

	public void setFlagBloqueio(boolean flagBloqueio) {
		this.flagBloqueio = flagBloqueio;
	}

	public List<TbMATRIZCARGOSEntityTrilhas> getListaTrilhasn3() {
		return listaTrilhasn3;
	}

	public void setListaTrilhasn3(List<TbMATRIZCARGOSEntityTrilhas> listaTrilhasn3) {
		this.listaTrilhasn3 = listaTrilhasn3;
	}

	public List<TbMATRIZCARGOSEntityTrilhas> getListaTrilhasn2_per() {
		return listaTrilhasn2_per;
	}

	public void setListaTrilhasn2_per(List<TbMATRIZCARGOSEntityTrilhas> listaTrilhasn2_per) {
		this.listaTrilhasn2_per = listaTrilhasn2_per;
	}

	public List<TbMATRIZCARGOSEntityTrilhas> getListaTrilhasn3_per() {
		return listaTrilhasn3_per;
	}

	public void setListaTrilhasn3_per(List<TbMATRIZCARGOSEntityTrilhas> listaTrilhasn3_per) {
		this.listaTrilhasn3_per = listaTrilhasn3_per;
	}

	public List<TbMATRIZCARGOSEntityTrilhas> getListaTrilhasn4() {
		return listaTrilhasn4;
	}

	public void setListaTrilhasn4(List<TbMATRIZCARGOSEntityTrilhas> listaTrilhasn4) {
		this.listaTrilhasn4 = listaTrilhasn4;
	}


	public TbCARGOSEntity getTbCARGOSEdicao() {
		if(this.tbCARGOSEdicao==null) {
			setFlagCultura(1);
			flagCultura = 1;
			this.tbCARGOSEdicao = tbCARGOSService.find((long) id);
			System.out.println("ID do cargo" + id);
		}
		return this.tbCARGOSEdicao;
	}


	public void setTbCARGOSEdicao(TbCARGOSEntity tbCARGOSEdicao) {
		this.tbCARGOSEdicao = tbCARGOSEdicao;
	}


	public List<TbCONHECIMENTOSBASCARGOSEntity> getListCBCARGOSPara() {
		return listCBCARGOSPara;
	}


	public void setListCBCARGOSPara(List<TbCONHECIMENTOSBASCARGOSEntity> listCBCARGOSPara) {
		this.listCBCARGOSPara = listCBCARGOSPara;
	}


	public List<TbCONHECIMENTOSBASCARGOSEntity> getListCBCARGOSDe() {
		return listCBCARGOSDe;
	}


	public void setListCBCARGOSDe(List<TbCONHECIMENTOSBASCARGOSEntity> listCBCARGOSDe) {
		this.listCBCARGOSDe = listCBCARGOSDe;
	}


	public List<TbCONHECIMENTOSESPCARGOSEntity> getListCECARGOSPara() {
		return listCECARGOSPara;
	}


	public void setListCECARGOSPara(List<TbCONHECIMENTOSESPCARGOSEntity> listCECARGOSPara) {
		this.listCECARGOSPara = listCECARGOSPara;
	}


	public List<TbCONHECIMENTOSESPCARGOSEntity> getListCECARGOSDe() {
		return listCECARGOSDe;
	}


	public void setListCECARGOSDe(List<TbCONHECIMENTOSESPCARGOSEntity> listCECARGOSDe) {
		this.listCECARGOSDe = listCECARGOSDe;
	}


	public List<TbCOMPETENCIASCARGOSEntity> getListCOCARGOSPara() {
		return listCOCARGOSPara;
	}


	public void setListCOCARGOSPara(List<TbCOMPETENCIASCARGOSEntity> listCOCARGOSPara) {
		this.listCOCARGOSPara = listCOCARGOSPara;
	}


	public List<TbCOMPETENCIASCARGOSEntity> getListCOCARGOSDe() {
		return listCOCARGOSDe;
	}


	public void setListCOCARGOSDe(List<TbCOMPETENCIASCARGOSEntity> listCOCARGOSDe) {
		this.listCOCARGOSDe = listCOCARGOSDe;
	}


	public List<TbGRADECARGOSEntity> getListGRCARGOSPara() {
		return listGRCARGOSPara;
	}


	public void setListGRCARGOSPara(List<TbGRADECARGOSEntity> listGRCARGOSPara) {
		this.listGRCARGOSPara = listGRCARGOSPara;
	}


	public List<TbGRADECARGOSEntity> getListGRCARGOSDe() {
		return listGRCARGOSDe;
	}


	public void setListGRCARGOSDe(List<TbGRADECARGOSEntity> listGRCARGOSDe) {
		this.listGRCARGOSDe = listGRCARGOSDe;
	}


	public List<TbPERFILCARGOSEntity> getListPECARGOSPara() {
		return listPECARGOSPara;
	}


	public void setListPECARGOSPara(List<TbPERFILCARGOSEntity> listPECARGOSPara) {
		this.listPECARGOSPara = listPECARGOSPara;
	}


	public List<TbPERFILCARGOSEntity> getListPECARGOSDe() {
		return listPECARGOSDe;
	}


	public void setListPECARGOSDe(List<TbPERFILCARGOSEntity> listPECARGOSDe) {
		this.listPECARGOSDe = listPECARGOSDe;
	}


	public String getFlagEstaganado() {
		return flagEstaganado;
	}


	public void setFlagEstaganado(String flagEstaganado) {
		this.flagEstaganado = flagEstaganado;
	}


	public boolean getSelector() {
		return selector;
	}


	public void setSelector(boolean selector) {
		this.selector = selector;
	}

}
