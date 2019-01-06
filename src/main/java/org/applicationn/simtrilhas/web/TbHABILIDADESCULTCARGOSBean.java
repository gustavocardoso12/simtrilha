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
import org.applicationn.simtrilhas.domain.TbHABILIDADESCULTCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbHABILIDADESCULTURAISEntity;
import org.applicationn.simtrilhas.service.TbCARGOSService;
import org.applicationn.simtrilhas.service.TbHABILIDADESCULTCARGOSService;
import org.applicationn.simtrilhas.service.TbHABILIDADESCULTURAISService;
import org.applicationn.simtrilhas.service.security.SecurityWrapper;
import org.applicationn.simtrilhas.web.util.MessageFactory;
import org.primefaces.context.RequestContext;

@Named("tbHABILIDADESCULTCARGOSBean")
@ViewScoped
public class TbHABILIDADESCULTCARGOSBean extends TbCARGOSBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = Logger.getLogger(TbHABILIDADESCULTCARGOSBean.class.getName());

	private List<TbHABILIDADESCULTCARGOSEntity> tbHABILIDADESCULTCARGOSList;

	private TbHABILIDADESCULTCARGOSEntity tbHABILIDADESCULTCARGOS;

	@Inject
	private TbHABILIDADESCULTCARGOSService tbHABILIDADESCULTCARGOSService;

	@Inject
	private TbCARGOSService tbCARGOSService;

	@Inject
	private TbHABILIDADESCULTURAISService tbHABILIDADESCULTURAISService;

	private List<TbCARGOSEntity> allIdCARGOSsList;

	private List<TbHABILIDADESCULTURAISEntity> allIdHABCULTCARsList = new ArrayList<TbHABILIDADESCULTURAISEntity>();


	private String dialogHeader;

	public void setDialogHeader(final String dialogHeader) { 
		this.dialogHeader = dialogHeader;
	}

	public String getDialogHeader() {
		return dialogHeader;
	}

	public void changeHeaderCadastrar() {
		setDialogHeader("Cadastrar Habilidades Culturais (Perfil)");
	}

	public void changeHeaderEditar() {
		setDialogHeader("Editar  Habilidades Culturais (Perfil)");
	}




	public TbHABILIDADESCULTCARGOSBean() {
		super();
	}


	public void prepareNewTbHABILIDADESCULTCARGOS(TbCARGOSEntity tbCARGOS) {
		this.tbHABILIDADESCULTCARGOS = new TbHABILIDADESCULTCARGOSEntity();
		changeHeaderCadastrar();
		filtraListas(tbCARGOS);

	}

	public void onDialogOpen(TbHABILIDADESCULTCARGOSEntity tbHABILIDADESCULTCARGOS,TbCARGOSEntity tbCARGOS ) {
		this.tbHABILIDADESCULTCARGOS = tbHABILIDADESCULTCARGOS;
		changeHeaderEditar();
		allIdHABCULTCARsList = tbHABILIDADESCULTURAISService.findAllTbHABILIDADESCULTURAISEntities();
		allIdCARGOSsList = tbCARGOSService.findAllTbCARGOSEntities();
	}


	public void filtraListas(TbCARGOSEntity tbCARGOS) {

		tbHABILIDADESCULTCARGOSs = InicializaTabelasAuxiliares(tbCARGOS);
		allIdHABCULTCARsList = tbHABILIDADESCULTURAISService.findAllTbHABILIDADESCULTURAISEntities();
		for(int i=0;i<this.getTbHABILIDADESCULTCARGOSs().size();i++) {
			for(int j=0;j<allIdHABCULTCARsList.size();j++) {
				if(tbHABILIDADESCULTCARGOSs.get(i).getIdHABCULTCAR().getDeSCHABCULT().
						equals(allIdHABCULTCARsList.get(j).getDeSCHABCULT())) {
					allIdHABCULTCARsList.remove(allIdHABCULTCARsList.get(j));
				}
			}
		}
		allIdCARGOSsList = tbCARGOSService.findAllTbCARGOSEntities();
		for(int i2 = 0; i2<tbHABILIDADESCULTCARGOSs.size();i2++) {
			for(int j2=0;j2<allIdCARGOSsList.size();j2++) {
				if(!tbCARGOS.getId().equals(allIdCARGOSsList.get(j2).getId())) {
					allIdCARGOSsList.remove(allIdCARGOSsList.remove(j2));
				}
			}
		}
		if(allIdHABCULTCARsList.size()==0) {
			FacesContext.getCurrentInstance().validationFailed();
			FacesMessage message = MessageFactory.getMessage("label_listaCultvazia");
			message.setDetail(MessageFactory.getMessageString("label_listaCultvazia_message" ));
			RequestContext.getCurrentInstance().showMessageInDialog(message);
			return;
		
		}
	}
	


	public String persist() {

		String message;

		try {

			if (tbHABILIDADESCULTCARGOS.getId() != null) {
				tbHABILIDADESCULTCARGOS = tbHABILIDADESCULTCARGOSService.update(tbHABILIDADESCULTCARGOS);
				message = "message_successfully_updated";
			} else {
				tbHABILIDADESCULTCARGOS = tbHABILIDADESCULTCARGOSService.save(tbHABILIDADESCULTCARGOS);
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

		tbHABILIDADESCULTCARGOSList = null;

		FacesMessage facesMessage = MessageFactory.getMessage(message);
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);

		return null;
	}

	public String delete() {

		String message;

		try {
			tbHABILIDADESCULTCARGOSService.delete(tbHABILIDADESCULTCARGOS);
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
		tbHABILIDADESCULTCARGOS = null;
		tbHABILIDADESCULTCARGOSList = null;

		allIdCARGOSsList = null;

		allIdHABCULTCARsList = null;

	}



	// Update idCARGOS of the current tbHABILIDADESCULTCARGOS
	public void updateIdCARGOS(TbCARGOSEntity tbCARGOS) {
		this.tbHABILIDADESCULTCARGOS.setIdCARGOS(tbCARGOS);
		// Maybe we just created and assigned a new tbCARGOS. So reset the allIdCARGOSList.
		allIdCARGOSsList = null;
	}


	// Update idHABCULTCAR of the current tbHABILIDADESCULTCARGOS
	public void updateIdHABCULTCAR(TbHABILIDADESCULTURAISEntity tbHABILIDADESCULTURAIS) {
		this.tbHABILIDADESCULTCARGOS.setIdHABCULTCAR(tbHABILIDADESCULTURAIS);
		// Maybe we just created and assigned a new tbHABILIDADESCULTURAIS. So reset the allIdHABCULTCARList.
		allIdHABCULTCARsList = null;
	}

	public TbHABILIDADESCULTCARGOSEntity getTbHABILIDADESCULTCARGOS() {
		return this.tbHABILIDADESCULTCARGOS;
	}

	public void setTbHABILIDADESCULTCARGOS(TbHABILIDADESCULTCARGOSEntity tbHABILIDADESCULTCARGOS) {
		this.tbHABILIDADESCULTCARGOS = tbHABILIDADESCULTCARGOS;
	}

	public List<TbHABILIDADESCULTCARGOSEntity> getTbHABILIDADESCULTCARGOSList() {
		if (tbHABILIDADESCULTCARGOSList == null) {
			tbHABILIDADESCULTCARGOSList = tbHABILIDADESCULTCARGOSService.findAllTbHABILIDADESCULTCARGOSEntities();

		}
		return tbHABILIDADESCULTCARGOSList;
	}

	public void setTbHABILIDADESCULTCARGOSList(List<TbHABILIDADESCULTCARGOSEntity> tbHABILIDADESCULTCARGOSList) {
		this.tbHABILIDADESCULTCARGOSList = tbHABILIDADESCULTCARGOSList;
	}

	public boolean isPermitted(String permission) {
		return SecurityWrapper.isPermitted(permission);
	}

	public boolean isPermitted(TbHABILIDADESCULTCARGOSEntity tbHABILIDADESCULTCARGOS, String permission) {

		return SecurityWrapper.isPermitted(permission);

	}


	public TbCARGOSEntity getSelectedCargo() {
		return selectedCargo;
	}

	public void setSelectedCargo(TbCARGOSEntity selectedCargo) {
		this.selectedCargo = selectedCargo;
	}

	public List<TbCARGOSEntity> getAllIdCARGOSsList() {
		return allIdCARGOSsList;
	}


	public void setAllIdCARGOSsList(List<TbCARGOSEntity> allIdCARGOSsList) {
		this.allIdCARGOSsList = allIdCARGOSsList;
	}


	public List<TbHABILIDADESCULTURAISEntity> getAllIdHABCULTCARsList() {
		return allIdHABCULTCARsList;
	}


	public void setAllIdHABCULTCARsList(List<TbHABILIDADESCULTURAISEntity> allIdHABCULTCARsList) {
		this.allIdHABCULTCARsList = allIdHABCULTCARsList;
	}

}
