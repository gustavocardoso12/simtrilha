package org.applicationn.simtrilhas.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceException;

import org.applicationn.simtrilhas.domain.TbCONHECIMENTOSBASCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbCONHECIMENTOSBASICOSEntity;
import org.applicationn.simtrilhas.domain.TbPONTCARGOSEntity;
import org.applicationn.simtrilhas.service.TbCONHECIMENTOSBASCARGOSService;
import org.applicationn.simtrilhas.service.TbCONHECIMENTOSBASICOSService;
import org.applicationn.simtrilhas.service.TbPONTCARGOSService;
import org.applicationn.simtrilhas.service.security.SecurityWrapper;
import org.applicationn.simtrilhas.web.util.MessageFactory;
import org.primefaces.event.SlideEndEvent;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;

@Named("tbCONHECIMENTOSBASICOSBean")
@ViewScoped
public class TbCONHECIMENTOSBASICOSBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = Logger.getLogger(TbCONHECIMENTOSBASICOSBean.class.getName());

	private List<TbCONHECIMENTOSBASICOSEntity> tbCONHECIMENTOSBASICOSList;

	private TbCONHECIMENTOSBASICOSEntity tbCONHECIMENTOSBASICOS;

	@Inject
	private TbCONHECIMENTOSBASICOSService tbCONHECIMENTOSBASICOSService;

	@Inject
	private TbCONHECIMENTOSBASCARGOSService tbCONHECIMENTOSBASCARGOSService;


	private TbPONTCARGOSEntity tbPONTCARGOSEntity;

	@Inject
	private TbPONTCARGOSService tbPONTCARGOSService;

	private DualListModel<TbCONHECIMENTOSBASCARGOSEntity> tbCONHECIMENTOSBASCARGOSs;
	private List<String> transferedTbCONHECIMENTOSBASCARGOSIDs;
	private List<String> removedTbCONHECIMENTOSBASCARGOSIDs;

	private String dialogHeader;

	private double pontuacaoOriginal;

	private double gapVarCB;

	private boolean flagBloqueio;

	private boolean flagCustom;

	private boolean flagEdit;


	public void onSlideEndCB(SlideEndEvent event) {
		gapVarCB =  event.getValue();
		flagEdit = false;
		for (TbCONHECIMENTOSBASICOSEntity tbCONHECIMENTOSBASICOSEntity : tbCONHECIMENTOSBASICOSList) {

			if(tbCONHECIMENTOSBASICOSEntity.getConhecBasCustom()==null) {
				tbCONHECIMENTOSBASICOSEntity.setPenalidadeConhecBas(gapVarCB);
				persist(tbCONHECIMENTOSBASICOSEntity);
			}
		}

	} 



	public void setDialogHeader(final String dialogHeader) { 
		this.dialogHeader = dialogHeader;
	}

	public String getDialogHeader() {
		return dialogHeader;
	}

	public void changeHeaderCadastrar() {
		setDialogHeader("Cadastrar Conhecimentos Básicos");
	}

	public void changeHeaderEditar() {
		setDialogHeader("Editar Conhecimentos Básicos");
	}

	public void prepareNewTbCONHECIMENTOSBASICOS() {
		reset();
		changeHeaderCadastrar();
		this.tbCONHECIMENTOSBASICOS = new TbCONHECIMENTOSBASICOSEntity();

	}

	public String persist(TbCONHECIMENTOSBASICOSEntity tbCONHECIMENTOSBASICOS ) {

		if (tbCONHECIMENTOSBASICOS.getId() == null && !isPermitted("tbCONHECIMENTOSBASICOS:create")) {
			return "accessDenied";
		} else if (tbCONHECIMENTOSBASICOS.getId() != null && !isPermitted(tbCONHECIMENTOSBASICOS, "tbCONHECIMENTOSBASICOS:update")) {
			return "accessDenied";
		}

		String message;

		try {

			if (tbCONHECIMENTOSBASICOS.getId() != null) {

				if(tbCONHECIMENTOSBASICOS.getPenalidadeConhecBas()==null) {

				}else {

					if(flagEdit==false){
						tbPONTCARGOSEntity.setPoNTUACAOORIGINAL(tbCONHECIMENTOSBASICOS.getPenalidadeConhecBas().intValue());
						tbCONHECIMENTOSBASICOS.setConhecBasCustom(null);

					}else {
						if(tbCONHECIMENTOSBASICOS.getPenalidadeConhecBas().equals(tbPONTCARGOSEntity.getPoNTUACAOORIGINAL().doubleValue())) {
							tbCONHECIMENTOSBASICOS.setConhecBasCustom(null);
						}else {
							tbCONHECIMENTOSBASICOS.setConhecBasCustom("S");
						}
					}
				}					



				tbCONHECIMENTOSBASICOS = tbCONHECIMENTOSBASICOSService.update(tbCONHECIMENTOSBASICOS);
				tbPONTCARGOSEntity = tbPONTCARGOSService.update(tbPONTCARGOSEntity);
				message = "message_successfully_updated";
			} else {
				tbCONHECIMENTOSBASICOS = tbCONHECIMENTOSBASICOSService.save(tbCONHECIMENTOSBASICOS);
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

		tbCONHECIMENTOSBASICOSList = null;

		FacesMessage facesMessage = MessageFactory.getMessage(message);
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);

		return null;
	}

	public String delete() {

		if (!isPermitted(tbCONHECIMENTOSBASICOS, "tbCONHECIMENTOSBASICOS:delete")) {
			return "accessDenied";
		}

		String message;

		try {
			tbCONHECIMENTOSBASICOSService.delete(tbCONHECIMENTOSBASICOS);
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

	public void onDialogOpen(TbCONHECIMENTOSBASICOSEntity tbCONHECIMENTOSBASICOS) {
		changeHeaderEditar();
		this.tbCONHECIMENTOSBASICOS = tbCONHECIMENTOSBASICOS;
		pontuacaoOriginal = tbCONHECIMENTOSBASICOS .getPenalidadeConhecBas();
		this.tbPONTCARGOSEntity = tbPONTCARGOSService.findPONTCARGOSByRequisito("CONHECBAS");
		flagEdit = true;
		if(pontuacaoOriginal == tbPONTCARGOSEntity.getPoNTUACAOORIGINAL()) {
			flagCustom = false;

		}else {
			flagCustom = true;
		}
	}
	
	public void persist() {
		persist(tbCONHECIMENTOSBASICOS);
	}

	public void reset() {


	}

	public DualListModel<TbCONHECIMENTOSBASCARGOSEntity> getTbCONHECIMENTOSBASCARGOSs() {
		return tbCONHECIMENTOSBASCARGOSs;
	}

	public void setTbCONHECIMENTOSBASCARGOSs(DualListModel<TbCONHECIMENTOSBASCARGOSEntity> tbCONHECIMENTOSBASCARGOSs) {
		this.tbCONHECIMENTOSBASCARGOSs = tbCONHECIMENTOSBASCARGOSs;
	}

	public List<TbCONHECIMENTOSBASCARGOSEntity> getFullTbCONHECIMENTOSBASCARGOSsList() {
		List<TbCONHECIMENTOSBASCARGOSEntity> allList = new ArrayList<>();
		allList.addAll(tbCONHECIMENTOSBASCARGOSs.getSource());
		allList.addAll(tbCONHECIMENTOSBASCARGOSs.getTarget());
		return allList;
	}

	public void onTbCONHECIMENTOSBASCARGOSsDialog(TbCONHECIMENTOSBASICOSEntity tbCONHECIMENTOSBASICOS) {
		// Prepare the tbCONHECIMENTOSBASCARGOS PickList
		this.tbCONHECIMENTOSBASICOS = tbCONHECIMENTOSBASICOS;
		List<TbCONHECIMENTOSBASCARGOSEntity> selectedTbCONHECIMENTOSBASCARGOSsFromDB = tbCONHECIMENTOSBASCARGOSService
				.findTbCONHECIMENTOSBASCARGOSsByIdCONHECBAS(this.tbCONHECIMENTOSBASICOS);
		List<TbCONHECIMENTOSBASCARGOSEntity> availableTbCONHECIMENTOSBASCARGOSsFromDB = tbCONHECIMENTOSBASCARGOSService
				.findAvailableTbCONHECIMENTOSBASCARGOSs(this.tbCONHECIMENTOSBASICOS);
		this.tbCONHECIMENTOSBASCARGOSs = new DualListModel<>(availableTbCONHECIMENTOSBASCARGOSsFromDB, selectedTbCONHECIMENTOSBASCARGOSsFromDB);

		transferedTbCONHECIMENTOSBASCARGOSIDs = new ArrayList<>();
		removedTbCONHECIMENTOSBASCARGOSIDs = new ArrayList<>();
	}

	public void onTbCONHECIMENTOSBASCARGOSsPickListTransfer(TransferEvent event) {
		// If a tbCONHECIMENTOSBASCARGOS is transferred within the PickList, we just transfer it in this
		// bean scope. We do not change anything it the database, yet.
		for (Object item : event.getItems()) {
			String id = ((TbCONHECIMENTOSBASCARGOSEntity) item).getId().toString();
			if (event.isAdd()) {
				transferedTbCONHECIMENTOSBASCARGOSIDs.add(id);
				removedTbCONHECIMENTOSBASCARGOSIDs.remove(id);
			} else if (event.isRemove()) {
				removedTbCONHECIMENTOSBASCARGOSIDs.add(id);
				transferedTbCONHECIMENTOSBASCARGOSIDs.remove(id);
			}
		}

	}

	public void updateTbCONHECIMENTOSBASCARGOS(TbCONHECIMENTOSBASCARGOSEntity tbCONHECIMENTOSBASCARGOS) {
		// If a new tbCONHECIMENTOSBASCARGOS is created, we persist it to the database,
		// but we do not assign it to this tbCONHECIMENTOSBASICOS in the database, yet.
		tbCONHECIMENTOSBASCARGOSs.getTarget().add(tbCONHECIMENTOSBASCARGOS);
		transferedTbCONHECIMENTOSBASCARGOSIDs.add(tbCONHECIMENTOSBASCARGOS.getId().toString());
	}

	public void onTbCONHECIMENTOSBASCARGOSsSubmit() {
		// Now we save the changed of the PickList to the database.
		try {
			List<TbCONHECIMENTOSBASCARGOSEntity> selectedTbCONHECIMENTOSBASCARGOSsFromDB = tbCONHECIMENTOSBASCARGOSService
					.findTbCONHECIMENTOSBASCARGOSsByIdCONHECBAS(this.tbCONHECIMENTOSBASICOS);
			List<TbCONHECIMENTOSBASCARGOSEntity> availableTbCONHECIMENTOSBASCARGOSsFromDB = tbCONHECIMENTOSBASCARGOSService
					.findAvailableTbCONHECIMENTOSBASCARGOSs(this.tbCONHECIMENTOSBASICOS);

			for (TbCONHECIMENTOSBASCARGOSEntity tbCONHECIMENTOSBASCARGOS : selectedTbCONHECIMENTOSBASCARGOSsFromDB) {
				if (removedTbCONHECIMENTOSBASCARGOSIDs.contains(tbCONHECIMENTOSBASCARGOS.getId().toString())) {
					tbCONHECIMENTOSBASCARGOS.setIdCONHECBAS(null);
					tbCONHECIMENTOSBASCARGOSService.update(tbCONHECIMENTOSBASCARGOS);
				}
			}

			for (TbCONHECIMENTOSBASCARGOSEntity tbCONHECIMENTOSBASCARGOS : availableTbCONHECIMENTOSBASCARGOSsFromDB) {
				if (transferedTbCONHECIMENTOSBASCARGOSIDs.contains(tbCONHECIMENTOSBASCARGOS.getId().toString())) {
					tbCONHECIMENTOSBASCARGOS.setIdCONHECBAS(tbCONHECIMENTOSBASICOS);
					tbCONHECIMENTOSBASCARGOSService.update(tbCONHECIMENTOSBASCARGOS);
				}
			}

			FacesMessage facesMessage = MessageFactory.getMessage(
					"message_changes_saved");
			FacesContext.getCurrentInstance().addMessage(null, facesMessage);

			reset();

		} catch (OptimisticLockException e) {
			logger.log(Level.SEVERE, "Error occured", e);
			FacesMessage facesMessage = MessageFactory.getMessage(
					"message_optimistic_locking_exception");
			FacesContext.getCurrentInstance().addMessage(null, facesMessage);
			// Set validationFailed to keep the dialog open
			FacesContext.getCurrentInstance().validationFailed();
		} catch (PersistenceException e) {
			logger.log(Level.SEVERE, "Error occured", e);
			FacesMessage facesMessage = MessageFactory.getMessage(
					"message_picklist_save_exception");
			FacesContext.getCurrentInstance().addMessage(null, facesMessage);
			// Set validationFailed to keep the dialog open
			FacesContext.getCurrentInstance().validationFailed();
		}
	}

	public TbCONHECIMENTOSBASICOSEntity getTbCONHECIMENTOSBASICOS() {
		if (this.tbCONHECIMENTOSBASICOS == null) {
			prepareNewTbCONHECIMENTOSBASICOS();
		}
		return this.tbCONHECIMENTOSBASICOS;
	}

	public void setTbCONHECIMENTOSBASICOS(TbCONHECIMENTOSBASICOSEntity tbCONHECIMENTOSBASICOS) {
		this.tbCONHECIMENTOSBASICOS = tbCONHECIMENTOSBASICOS;
	}

	public List<TbCONHECIMENTOSBASICOSEntity> getTbCONHECIMENTOSBASICOSList() {
		if (tbCONHECIMENTOSBASICOSList == null) {
			tbCONHECIMENTOSBASICOSList = tbCONHECIMENTOSBASICOSService.findAllTbCONHECIMENTOSBASICOSEntities();
		}
		return tbCONHECIMENTOSBASICOSList;
	}

	public void setTbCONHECIMENTOSBASICOSList(List<TbCONHECIMENTOSBASICOSEntity> tbCONHECIMENTOSBASICOSList) {
		this.tbCONHECIMENTOSBASICOSList = tbCONHECIMENTOSBASICOSList;
	}

	public boolean isPermitted(String permission) {
		return SecurityWrapper.isPermitted(permission);
	}

	public boolean isPermitted(TbCONHECIMENTOSBASICOSEntity tbCONHECIMENTOSBASICOS, String permission) {

		return SecurityWrapper.isPermitted(permission);

	}

	public double getPontuacaoOriginal() {
		return pontuacaoOriginal;
	}

	public void setPontuacaoOriginal(double pontuacaoOriginal) {
		this.pontuacaoOriginal = pontuacaoOriginal;
	}

	public double getGapVarCB() {
		this.tbPONTCARGOSEntity = tbPONTCARGOSService.findPONTCARGOSByRequisito("CONHECBAS");
		gapVarCB = tbPONTCARGOSEntity.getPoNTUACAOORIGINAL();
		return gapVarCB;
	}

	public void setGapVarCB(double gapVarCB) {
		this.gapVarCB = gapVarCB;
	}

	public boolean isFlagBloqueio() {

		if(tbCONHECIMENTOSBASICOS==null) {

		}else {
			if(tbCONHECIMENTOSBASICOS.getBloqueiaMovConhecBas()==null) {

			}else {
				if(tbCONHECIMENTOSBASICOS.getBloqueiaMovConhecBas().equals("SIM")) {
					flagBloqueio = true;
				}else {
					flagBloqueio = false;
				}
			}
		}
		return flagBloqueio;
	}

	public void setFlagBloqueio(boolean flagBloqueio) {
		this.flagBloqueio = flagBloqueio;
		if(flagBloqueio==true) {
			tbCONHECIMENTOSBASICOS.setBloqueiaMovConhecBas("SIM");
		} else {
			tbCONHECIMENTOSBASICOS.setBloqueiaMovConhecBas("");
		}
	}

	public boolean isFlagCustom() {

		return flagCustom;
	}

	public void setFlagCustom(boolean flagCustom) {
		this.flagCustom = flagCustom;
		if(flagCustom==false) {
			tbCONHECIMENTOSBASICOS.setPenalidadeConhecBas(tbPONTCARGOSEntity.getPoNTUACAOORIGINAL().doubleValue());
		}
	}

	public boolean isFlagEdit() {
		return flagEdit;
	}

	public void setFlagEdit(boolean flagEdit) {
		this.flagEdit = flagEdit;
	}

}
