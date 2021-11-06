package org.applicationn.simtrilhas.web;

import java.io.Serializable;
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
import org.applicationn.simtrilhas.domain.TbCONHECIMENTOSBASCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbCONHECIMENTOSBASICOSEntity;
import org.applicationn.simtrilhas.domain.TbMASCARAEntity;
import org.applicationn.simtrilhas.domain.TbPERFILEntity;
import org.applicationn.simtrilhas.domain.TbPONTCARGOSEntity;
import org.applicationn.simtrilhas.service.TbCONHECIMENTOSBASICOSService;
import org.applicationn.simtrilhas.service.TbMASCARAService;
import org.applicationn.simtrilhas.service.TbPONTCARGOSService;
import org.applicationn.simtrilhas.service.security.SecurityWrapper;
import org.applicationn.simtrilhas.web.util.MessageFactory;
import org.primefaces.event.SlideEndEvent;
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
    private TbMASCARAService tbMASCARAService;

	private TbPONTCARGOSEntity tbPONTCARGOSEntity;

	@Inject
	private TbPONTCARGOSService tbPONTCARGOSService;

	private DualListModel<TbCONHECIMENTOSBASCARGOSEntity> tbCONHECIMENTOSBASCARGOSs;

	private String dialogHeader;

	private double pontuacaoOriginal;

	private double gapVarCB;

	private boolean flagBloqueio;

	private boolean flagCustom;

	private boolean flagEdit;

	private boolean flagDelete=false;


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
        this.tbCONHECIMENTOSBASICOS.setTbMascara(tbMASCARA);
        allIdMASCARAsList = null;
    }
	

	public void onSelect() {

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
		setDialogHeader("Cadastrar Formação e Experiências");
	}

	public void changeHeaderEditar() {
		setDialogHeader("Editar Formação e Experiências");
	}

	public void prepareNewTbCONHECIMENTOSBASICOS() {
		
	
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

				if(tbCONHECIMENTOSBASICOS.getPenalidadeConhecBas()==0) {

				}else {

					if(flagEdit==false){
						tbPONTCARGOSEntity.setPoNTUACAOORIGINAL(tbCONHECIMENTOSBASICOS.getPenalidadeConhecBas());
						tbCONHECIMENTOSBASICOS.setConhecBasCustom(null);

					}else {
						if(tbCONHECIMENTOSBASICOS.getPenalidadeConhecBas()==tbPONTCARGOSEntity.getPoNTUACAOORIGINAL()) {
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
			flagDelete=true;
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error occured", e);
			message = "message_delete_exception";
			// Set validationFailed to keep the dialog open
			FacesContext.getCurrentInstance().validationFailed();
		}
		
		tbCONHECIMENTOSBASICOSList = null;
		
		FacesContext.getCurrentInstance().addMessage(null, MessageFactory.getMessage(message));

		return null;
	}

	public void onDialogOpen(TbCONHECIMENTOSBASICOSEntity tbCONHECIMENTOSBASICOS) {
		changeHeaderEditar();
		this.tbCONHECIMENTOSBASICOS = tbCONHECIMENTOSBASICOS;
		pontuacaoOriginal = tbCONHECIMENTOSBASICOS .getPenalidadeConhecBas();
		 if(this.tbPONTCARGOSEntity ==null) {
			 this.tbPONTCARGOSEntity = tbPONTCARGOSService.findPONTCARGOSByRequisito("CONHECBAS");
		 }
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
		if ((tbCONHECIMENTOSBASICOSList == null)|| (flagDelete==true)) {
			tbCONHECIMENTOSBASICOSList = tbCONHECIMENTOSBASICOSService.findAllTbCONHECIMENTOSBASICOSEntitiesCB();
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
		if(this.tbPONTCARGOSEntity ==null) {
		this.tbPONTCARGOSEntity = tbPONTCARGOSService.findPONTCARGOSByRequisito("CONHECBAS");
		gapVarCB = tbPONTCARGOSEntity.getPoNTUACAOORIGINAL();
		}
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
			tbCONHECIMENTOSBASICOS.setPenalidadeConhecBas(tbPONTCARGOSEntity.getPoNTUACAOORIGINAL());
		}
	}

	public boolean isFlagEdit() {
		return flagEdit;
	}

	public void setFlagEdit(boolean flagEdit) {
		this.flagEdit = flagEdit;
	}



	public boolean isFlagDelete() {
		return flagDelete;
	}



	public void setFlagDelete(boolean flagDelete) {
		this.flagDelete = flagDelete;
	}

}
