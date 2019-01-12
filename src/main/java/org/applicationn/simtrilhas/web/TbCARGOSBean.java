package org.applicationn.simtrilhas.web;

import java.io.Serializable;
import java.math.BigDecimal;
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
import org.applicationn.simtrilhas.domain.TbCOMPETENCIASEntity;
import org.applicationn.simtrilhas.domain.TbCONHECIMENTOSBASCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbCONHECIMENTOSESPCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbCURSOSEntity;
import org.applicationn.simtrilhas.domain.TbDIRETORIAEntity;
import org.applicationn.simtrilhas.domain.TbESTATUARIOEntity;
import org.applicationn.simtrilhas.domain.TbESTILOLIDERANCACARGOSEntity;
import org.applicationn.simtrilhas.domain.TbESTILOLIDERANCAEntity;
import org.applicationn.simtrilhas.domain.TbESTILOPENSAMENTOCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbHABILIDADESCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbHABILIDADESCULTCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbHABILIDADESCULTURAISEntity;
import org.applicationn.simtrilhas.service.TbCARGOSService;
import org.applicationn.simtrilhas.service.TbCOMPETENCIASCARGOSService;
import org.applicationn.simtrilhas.service.TbCOMPETENCIASEMCARGOSService;
import org.applicationn.simtrilhas.service.TbCONHECIMENTOSBASCARGOSService;
import org.applicationn.simtrilhas.service.TbCONHECIMENTOSESPCARGOSService;
import org.applicationn.simtrilhas.service.TbCURSOSService;
import org.applicationn.simtrilhas.service.TbDIRETORIAService;
import org.applicationn.simtrilhas.service.TbESTATUARIOService;
import org.applicationn.simtrilhas.service.TbESTILOLIDERANCACARGOSService;
import org.applicationn.simtrilhas.service.TbESTILOLIDERANCAService;
import org.applicationn.simtrilhas.service.TbESTILOPENSAMENTOCARGOSService;
import org.applicationn.simtrilhas.service.TbHABILIDADESCARGOSService;
import org.applicationn.simtrilhas.service.TbHABILIDADESCULTCARGOSService;
import org.applicationn.simtrilhas.service.TbHABILIDADESCULTURAISService;
import org.applicationn.simtrilhas.service.security.SecurityWrapper;
import org.applicationn.simtrilhas.web.util.MessageFactory;
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
	

	protected List<TbHABILIDADESCARGOSEntity> tbHABILIDADESCARGOSs;

	protected List<TbHABILIDADESCULTCARGOSEntity> tbHABILIDADESCULTCARGOSs;
	
	protected List<TbESTILOLIDERANCACARGOSEntity> tbESTILOLIDERANCAs;
	
	protected List<TbESTILOPENSAMENTOCARGOSEntity> tbESTILOPENSAMENTOs;
	
	protected List<TbCOMPETENCIASEMCARGOSEntity> tbCOMPETENCIASEMs;
	
	protected List<TbCOMPETENCIASCARGOSEntity> tbCOMPETENCIASCARGOSs;
	
	protected List<TbCONHECIMENTOSBASCARGOSEntity> tbCONHECIMENTOSBASICOSCARGOSs;
	
	protected List<TbCONHECIMENTOSESPCARGOSEntity>  tbCONHECIMENTOSESPCARGOSs;

	private List<TbESTATUARIOEntity> allIdESTsList;

	private List<TbCURSOSEntity> allIdCURSOSsList;

	private List<TbDIRETORIAEntity> allIdDIRETORIAsList;

	private List<TbHABILIDADESCULTURAISEntity> allIdHABCULTCARsList = new ArrayList<TbHABILIDADESCULTURAISEntity>();

	private List<TbCARGOSEntity> allIdCARGOSsList = new ArrayList<TbCARGOSEntity>();

	protected TbCARGOSEntity selectedCargo;

	private String dialogHeader;

	private String detalhesComportamento;


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

		return tbCOMPETENCIASCARGOSs;
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

	}

	// Get a List of all idEST
	public List<TbESTATUARIOEntity> getIdESTs() {
		if (this.allIdESTsList == null) {
			this.allIdESTsList = tbESTATUARIOService.findAllTbESTATUARIOEntities();

		}
		return this.allIdESTsList;
	}


	// Get a List of all idDIRETORIAs
	public List<TbDIRETORIAEntity> getIdDiretoria() {
		if (this.allIdDIRETORIAsList == null) {
			this.allIdDIRETORIAsList = tbDIRETORIAService.findAllTbDIRETORIAEntities();
		}
		return this.allIdDIRETORIAsList;
	}



	// Update idEST of the current tbCARGOS
	public void updateIdEST(TbESTATUARIOEntity tbESTATUARIO) {
		this.tbCARGOS.setIdEST(tbESTATUARIO);
		// Maybe we just created and assigned a new tbESTATUARIO. So reset the allIdESTList.
		allIdESTsList = null;
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






}
