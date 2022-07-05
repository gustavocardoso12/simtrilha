package org.applicationn.simtrilhas.web;

import java.io.Serializable;
import java.text.Normalizer;
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

import org.applicationn.simtrilhas.domain.TbAREAEntity;
import org.applicationn.simtrilhas.domain.TbDEPTOEntity;
import org.applicationn.simtrilhas.service.TbAREAService;
import org.applicationn.simtrilhas.service.TbDEPTOService;
import org.applicationn.simtrilhas.service.security.SecurityWrapper;
import org.applicationn.simtrilhas.web.util.MessageFactory;

@Named("tbDEPTOBean")
@ViewScoped
public class TbDEPTOBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = Logger.getLogger(TbDEPTOBean.class.getName());

	private List<TbDEPTOEntity> tbDEPTOList;

	private TbDEPTOEntity tbDEPTO;

	@Inject
	private TbDEPTOService tbDEPTOService;

	@Inject
	private TbAREAService tbAREAService;

	private List<TbAREAEntity> allIdAREAsList;

	private String dialogHeader;

	public void setDialogHeader(final String dialogHeader) { 
		this.dialogHeader = dialogHeader;
	}

	public String getDialogHeader() {
		return dialogHeader;
	}

	public void changeHeaderCadastrar() {
		setDialogHeader("Cadastrar Sub-Família");
	}

	public void changeHeaderEditar() {
		setDialogHeader("Editar Sub-família");
	}


	public void prepareNewTbDEPTO() {
		reset();
		this.tbDEPTO = new TbDEPTOEntity();
		changeHeaderCadastrar();
		// set any default values now, if you need
		// Example: this.tbDEPTO.setAnything("test");
	}

	public String persist() {

		if (tbDEPTO.getId() == null && !isPermitted("tbDEPTO:create")) {
			return "accessDenied";
		} else if (tbDEPTO.getId() != null && !isPermitted(tbDEPTO, "tbDEPTO:update")) {
			return "accessDenied";
		}

		String message;

		try {

			if (tbDEPTO.getId() != null) {
				tbDEPTO = tbDEPTOService.update(tbDEPTO);
				message = "message_successfully_updated";
			} else {
				
				String duplicado="";
				String departamento="";

				departamento = tbDEPTO.getDeSCDEPTO();

				tbDEPTO.setDeSCDEPTO(removerAcentos(tbDEPTO.getDeSCDEPTO()));
				tbDEPTO.setDeSCDEPTO(tbDEPTO.getDeSCDEPTO().trim().toUpperCase());

				tbDEPTOList = tbDEPTOService.findAllTbDEPTOEntities();
				for (int i =0; i<tbDEPTOList.size();i++) {
					tbDEPTOList.get(i).setDeSCDEPTO(removerAcentos(tbDEPTOList.get(i).getDeSCDEPTO()));
					tbDEPTOList.get(i).setDeSCDEPTO((tbDEPTOList.get(i).getDeSCDEPTO().trim().toUpperCase()));
					if(tbDEPTO.getDeSCDEPTO().equals(tbDEPTOList.get(i).getDeSCDEPTO())) {
						duplicado = "S";
						this.tbDEPTO =null;
						break;
					}
					else {
						duplicado="N";
					}
				}

				if((duplicado.equals("S"))){
					message = "messageduplicado";
				}else {
					tbDEPTO.setDeSCDEPTO(departamento);
					tbDEPTO = tbDEPTOService.save(tbDEPTO);
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

		tbDEPTOList = null;

		FacesMessage facesMessage = MessageFactory.getMessage(message);
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);

		return null;
	}

	public static String removerAcentos(String str) {
		return Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
	}
	
	public String delete() {

		if (!isPermitted(tbDEPTO, "tbDEPTO:delete")) {
			return "accessDenied";
		}

		String message;

		try {
			tbDEPTOService.delete(tbDEPTO);
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

	public void onDialogOpen(TbDEPTOEntity tbDEPTO) {
		reset();
		this.tbDEPTO = tbDEPTO;
		changeHeaderEditar();
	}

	public void reset() {
		tbDEPTO = null;
		tbDEPTOList = null;

		allIdAREAsList = null;

	}

	// Get a List of all idAREA
	public List<TbAREAEntity> getIdAREAs() {
		if (this.allIdAREAsList == null) {
			this.allIdAREAsList = tbAREAService.findAllTbAREAEntities();
		}
		return this.allIdAREAsList;
	}

	// Update idAREA of the current tbDEPTO
	public void updateIdAREA(TbAREAEntity tbAREA) {
		this.tbDEPTO.setIdAREA(tbAREA);
		// Maybe we just created and assigned a new tbAREA. So reset the allIdAREAList.
		allIdAREAsList = null;
	}

	public TbDEPTOEntity getTbDEPTO() {
		if (this.tbDEPTO == null) {
			prepareNewTbDEPTO();
		}
		return this.tbDEPTO;
	}

	public void setTbDEPTO(TbDEPTOEntity tbDEPTO) {
		this.tbDEPTO = tbDEPTO;
	}

	public List<TbDEPTOEntity> getTbDEPTOList() {
		if (tbDEPTOList == null) {
			tbDEPTOList = tbDEPTOService.findAllTbDEPTOEntities();
		}
		return tbDEPTOList;
	}

	public void setTbDEPTOList(List<TbDEPTOEntity> tbDEPTOList) {
		this.tbDEPTOList = tbDEPTOList;
	}

	public boolean isPermitted(String permission) {
		return SecurityWrapper.isPermitted(permission);
	}

	public boolean isPermitted(TbDEPTOEntity tbDEPTO, String permission) {

		return SecurityWrapper.isPermitted(permission);

	}

}
