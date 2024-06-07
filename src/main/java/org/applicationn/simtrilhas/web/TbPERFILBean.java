package org.applicationn.simtrilhas.web;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.Flags.Flag;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceException;

import org.applicationn.simtrilhas.domain.TbCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbCOMPETENCIASEntity;
import org.applicationn.simtrilhas.domain.TbMASCARAEntity;
import org.applicationn.simtrilhas.domain.TbPERFILCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbPERFILEntity;
import org.applicationn.simtrilhas.domain.TbPONTCARGOSEntity;
import org.applicationn.simtrilhas.service.TbMASCARAService;
import org.applicationn.simtrilhas.service.TbPERFILCARGOSService;
import org.applicationn.simtrilhas.service.TbPERFILService;
import org.applicationn.simtrilhas.service.TbPONTCARGOSService;
import org.applicationn.simtrilhas.service.security.SecurityWrapper;
import org.applicationn.simtrilhas.web.util.MessageFactory;
import org.primefaces.event.SlideEndEvent;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;

import com.lowagie.text.html.simpleparser.FactoryProperties;

@Named("tbPERFILBean")
@ViewScoped
public class TbPERFILBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = Logger.getLogger(TbPERFILBean.class.getName());

	private List<TbPERFILEntity> tbPERFILList;

	private TbPERFILEntity tbPERFIL;

	private TbPONTCARGOSEntity tbPONTCARGOSEntity;

	@Inject
	private TbPERFILService tbPERFILService;

	@Inject
	private TbPONTCARGOSService tbPONTCARGOSService;

	@Inject
	private TbPERFILCARGOSService tbPERFILCARGOSService;

	@Inject
	private TbMASCARAService tbMASCARAService;

	private DualListModel<TbPERFILCARGOSEntity> tbPERFILCARGOSs;
	private List<String> transferedTbPERFILCARGOSIDs;
	private List<String> removedTbPERFILCARGOSIDs;

	private String dialogHeader;

	private double pontuacaoOriginal;

	private double gapPE;

	private double g;

	public double getG() {
		return g;
	}


	public void setG(double g) {
		this.g = g;
	}


	public double getGapPE() {
		this.tbPONTCARGOSEntity = tbPONTCARGOSService.findPONTCARGOSByRequisito("PERFIL");
		gapPE = tbPONTCARGOSEntity.getPoNTUACAOORIGINAL();
		return gapPE;
	}


	public void setGapPE(double gapPE) {
		this.gapPE = gapPE;
	}


	private boolean flagBloqueio;

	private boolean flagCustom;

	private boolean flagEdit;

	private List<TbMASCARAEntity> allIdMASCARAsList;


	public List<TbMASCARAEntity> getAllIdMASCARAsList() {
		if (this.allIdMASCARAsList == null) {
			this.allIdMASCARAsList = tbMASCARAService.findAllTbMASCARAEntities();
		}
		return allIdMASCARAsList;
	}


	public void setAllIdMASCARAsList(List<TbMASCARAEntity> allIdMASCARAsList) {
		this.allIdMASCARAsList = allIdMASCARAsList;
	}


	public void updateIdMASCARA(TbMASCARAEntity tbMASCARA) {
		this.tbPERFIL.setTbMascara(tbMASCARA);
		allIdMASCARAsList = null;
	}


	public void teste(String t) {
	}

	public void onSelect() {

		flagEdit = false;


		for (TbPERFILEntity tbPERFILEntity : tbPERFILList) {

			if(tbPERFILEntity.getConhecPerfilCustom()==null) {
				tbPERFILEntity.setPenalidadeConhecPerfil(gapPE);
				persist(tbPERFILEntity);

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
		setDialogHeader("Cadastrar Perfil");
	}

	public void changeHeaderEditar() {
		setDialogHeader("Editar Perfil");
	}


	public void prepareNewTbPERFIL() {
		changeHeaderCadastrar();
		this.tbPERFIL = new TbPERFILEntity();

	}

	public void reset() {

	}

	public String persist(TbPERFILEntity tbPERFIL) {

		if (tbPERFIL.getId() == null && !isPermitted("tbPERFIL:create")) {
			return "accessDenied";
		} else if (tbPERFIL.getId() != null && !isPermitted(tbPERFIL, "tbPERFIL:update")) {
			return "accessDenied";
		}

		String message;

		try {



			if (tbPERFIL.getId() != null) {

				if(tbPERFIL.getPenalidadeConhecPerfil()==0) {

				}else {

					if(flagEdit==false){
						tbPONTCARGOSEntity.setPoNTUACAOORIGINAL((double) tbPERFIL.getPenalidadeConhecPerfil());
						tbPERFIL.setConhecPerfilCustom(null);

					}else {
						if(tbPERFIL.getPenalidadeConhecPerfil()==tbPONTCARGOSEntity.getPoNTUACAOORIGINAL()) {
							tbPERFIL.setConhecPerfilCustom(null);
						}else {
							tbPERFIL.setConhecPerfilCustom("S");
						}
					}
				}

				tbPERFIL = tbPERFILService.update(tbPERFIL);
				tbPONTCARGOSEntity = tbPONTCARGOSService.update(tbPONTCARGOSEntity);
				message = "message_successfully_updated";
			} else {

				String duplicado="";
				String perfil="";

				perfil = tbPERFIL.getDeSCPERFIL();

				tbPERFIL.setDeSCPERFIL(removerAcentos(tbPERFIL.getDeSCPERFIL()));
				tbPERFIL.setDeSCPERFIL(tbPERFIL.getDeSCPERFIL().trim().toUpperCase());

				tbPERFILList = tbPERFILService.findAllTbPERFILEntities();
				for (int i =0; i<tbPERFILList.size();i++) {
					tbPERFILList.get(i).setDeSCPERFIL(removerAcentos(tbPERFILList.get(i).getDeSCPERFIL()));
					tbPERFILList.get(i).setDeSCPERFIL((tbPERFILList.get(i).getDeSCPERFIL().trim().toUpperCase()));
					if(tbPERFIL.getDeSCPERFIL().equals(tbPERFILList.get(i).getDeSCPERFIL())) {
						duplicado = "S";
						this.tbPERFIL =null;
						break;
					}
					else {
						duplicado="N";
					}
				}

				if((duplicado.equals("S"))){
					message = "messageduplicado";
				}
				else {
					tbPERFIL.setDeSCPERFIL(perfil);
					tbPERFIL = tbPERFILService.save(tbPERFIL);
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

		tbPERFILList = null;

		FacesMessage facesMessage = MessageFactory.getMessage(message);
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);

		return null;
	}

	public static String removerAcentos(String str) {
		return Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
	}

	public void persist() {
		persist(tbPERFIL);
	}

	public String delete() {

		if (!isPermitted(tbPERFIL, "tbPERFIL:delete")) {
			return "accessDenied";
		}

		String message;

		try {
			tbPERFILService.delete(tbPERFIL);
			message = "message_successfully_deleted";
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error occured", e);
			message = "message_delete_exception";
			// Set validationFailed to keep the dialog open
			FacesContext.getCurrentInstance().validationFailed();
		}

		tbPERFILList = null;

		FacesContext.getCurrentInstance().addMessage(null, MessageFactory.getMessage(message));

		return null;
	}

	public void onDialogOpen(TbPERFILEntity tbPERFIL) {
		changeHeaderEditar();
		this.tbPERFIL = tbPERFIL;
		pontuacaoOriginal = tbPERFIL.getPenalidadeConhecPerfil();
		this.tbPONTCARGOSEntity = tbPONTCARGOSService.findPONTCARGOSByRequisito("PERFIL");
		flagEdit = true;
		if(pontuacaoOriginal == tbPONTCARGOSEntity.getPoNTUACAOORIGINAL()) {
			flagCustom = false;

		}else {
			flagCustom = true;
		}
	}



	public DualListModel<TbPERFILCARGOSEntity> getTbPERFILCARGOSs() {
		return tbPERFILCARGOSs;
	}

	public void setTbPERFILCARGOSs(DualListModel<TbPERFILCARGOSEntity> tbPERFILCARGOSs) {
		this.tbPERFILCARGOSs = tbPERFILCARGOSs;
	}

	public List<TbPERFILCARGOSEntity> getFullTbPERFILCARGOSsList() {
		List<TbPERFILCARGOSEntity> allList = new ArrayList<>();
		allList.addAll(tbPERFILCARGOSs.getSource());
		allList.addAll(tbPERFILCARGOSs.getTarget());
		return allList;
	}

	public void onTbPERFILCARGOSsDialog(TbPERFILEntity tbPERFIL) {
		// Prepare the tbPERFILCARGOS PickList
		this.tbPERFIL = tbPERFIL;
		List<TbPERFILCARGOSEntity> selectedTbPERFILCARGOSsFromDB = tbPERFILCARGOSService
				.findTbPERFILCARGOSsByIdPERFIL(this.tbPERFIL);
		List<TbPERFILCARGOSEntity> availableTbPERFILCARGOSsFromDB = tbPERFILCARGOSService
				.findAvailableTbPERFILCARGOSs(this.tbPERFIL);
		this.tbPERFILCARGOSs = new DualListModel<>(availableTbPERFILCARGOSsFromDB, selectedTbPERFILCARGOSsFromDB);

		transferedTbPERFILCARGOSIDs = new ArrayList<>();
		removedTbPERFILCARGOSIDs = new ArrayList<>();
	}

	public void onTbPERFILCARGOSsPickListTransfer(TransferEvent event) {
		// If a tbPERFILCARGOS is transferred within the PickList, we just transfer it in this
		// bean scope. We do not change anything it the database, yet.
		for (Object item : event.getItems()) {
			String id = ((TbPERFILCARGOSEntity) item).getId().toString();
			if (event.isAdd()) {
				transferedTbPERFILCARGOSIDs.add(id);
				removedTbPERFILCARGOSIDs.remove(id);
			} else if (event.isRemove()) {
				removedTbPERFILCARGOSIDs.add(id);
				transferedTbPERFILCARGOSIDs.remove(id);
			}
		}

	}

	public void updateTbPERFILCARGOS(TbPERFILCARGOSEntity tbPERFILCARGOS) {
		// If a new tbPERFILCARGOS is created, we persist it to the database,
		// but we do not assign it to this tbPERFIL in the database, yet.
		tbPERFILCARGOSs.getTarget().add(tbPERFILCARGOS);
		transferedTbPERFILCARGOSIDs.add(tbPERFILCARGOS.getId().toString());
	}

	public void onTbPERFILCARGOSsSubmit() {
		// Now we save the changed of the PickList to the database.
		try {
			List<TbPERFILCARGOSEntity> selectedTbPERFILCARGOSsFromDB = tbPERFILCARGOSService
					.findTbPERFILCARGOSsByIdPERFIL(this.tbPERFIL);
			List<TbPERFILCARGOSEntity> availableTbPERFILCARGOSsFromDB = tbPERFILCARGOSService
					.findAvailableTbPERFILCARGOSs(this.tbPERFIL);

			for (TbPERFILCARGOSEntity tbPERFILCARGOS : selectedTbPERFILCARGOSsFromDB) {
				if (removedTbPERFILCARGOSIDs.contains(tbPERFILCARGOS.getId().toString())) {
					tbPERFILCARGOS.setIdPERFIL(null);
					tbPERFILCARGOSService.update(tbPERFILCARGOS);
				}
			}

			for (TbPERFILCARGOSEntity tbPERFILCARGOS : availableTbPERFILCARGOSsFromDB) {
				if (transferedTbPERFILCARGOSIDs.contains(tbPERFILCARGOS.getId().toString())) {
					tbPERFILCARGOS.setIdPERFIL(tbPERFIL);
					tbPERFILCARGOSService.update(tbPERFILCARGOS);
				}
			}

			FacesMessage facesMessage = MessageFactory.getMessage(
					"message_changes_saved");
			FacesContext.getCurrentInstance().addMessage(null, facesMessage);


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

	public TbPERFILEntity getTbPERFIL() {
		if (this.tbPERFIL == null) {
			prepareNewTbPERFIL();
		}
		return this.tbPERFIL;
	}

	public void setTbPERFIL(TbPERFILEntity tbPERFIL) {
		this.tbPERFIL = tbPERFIL;
	}

	public List<TbPERFILEntity> getTbPERFILList() {
		if (tbPERFILList == null) {
			tbPERFILList = tbPERFILService.findAllTbPERFILEntities();
		}
		return tbPERFILList;
	}

	public void setTbPERFILList(List<TbPERFILEntity> tbPERFILList) {
		this.tbPERFILList = tbPERFILList;
	}

	public boolean isPermitted(String permission) {
		return SecurityWrapper.isPermitted(permission);
	}

	public boolean isPermitted(TbPERFILEntity tbPERFIL, String permission) {

		return SecurityWrapper.isPermitted(permission);

	}

	public double getPontuacaoOriginal() {
		return pontuacaoOriginal;
	}

	public void setPontuacaoOriginal(double pontuacaoOriginal) {
		this.pontuacaoOriginal = pontuacaoOriginal;
	}



	public boolean isFlagBloqueio() {
		if(tbPERFIL==null) {

		}else {
			if(tbPERFIL.getBloqueiaMovConhecPerfil()==null) {

			}else {

				if(tbPERFIL.getBloqueiaMovConhecPerfil().equals("SIM")) {
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
			tbPERFIL.setBloqueiaMovConhecPerfil("SIM");
		} else {
			tbPERFIL.setBloqueiaMovConhecPerfil("");
		}
	}

	public boolean isFlagCustom() {
		return flagCustom;
	}

	public void setFlagCustom(boolean flagCustom) {
		this.flagCustom = flagCustom;
		if(flagCustom==false) {
			tbPERFIL.setPenalidadeConhecPerfil(tbPONTCARGOSEntity.getPoNTUACAOORIGINAL());
		}
	}

	public boolean isFlagEdit() {
		return flagEdit;
	}

	public void setFlagEdit(boolean flagEdit) {
		this.flagEdit = flagEdit;
	}




}
