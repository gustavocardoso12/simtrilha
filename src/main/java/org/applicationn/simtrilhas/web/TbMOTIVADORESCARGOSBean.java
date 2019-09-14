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

import org.applicationn.simtrilhas.domain.TbCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbMOTIVADORESCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbMOTIVADORESEntity;
import org.applicationn.simtrilhas.service.TbCARGOSService;
import org.applicationn.simtrilhas.service.TbMOTIVADORESCARGOSService;
import org.applicationn.simtrilhas.service.TbMOTIVADORESService;
import org.applicationn.simtrilhas.service.security.SecurityWrapper;
import org.applicationn.simtrilhas.web.util.MessageFactory;
import org.primefaces.PrimeFaces;

@Named("tbMOTIVADORESCARGOSBean")
@ViewScoped
public class TbMOTIVADORESCARGOSBean extends TbCARGOSBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(TbMOTIVADORESCARGOSBean.class.getName());
    
    private List<TbMOTIVADORESCARGOSEntity> tbMOTIVADORESCARGOSList;

    private TbMOTIVADORESCARGOSEntity tbMOTIVADORESCARGOS;
    
    @Inject
    private TbMOTIVADORESCARGOSService tbMOTIVADORESCARGOSService;
    
    @Inject
    private TbCARGOSService tbCARGOSService;
    
    @Inject
    private TbMOTIVADORESService tbMOTIVADORESService;
    
    private List<TbCARGOSEntity> allIdCARGOSsList;
    
    private List<TbMOTIVADORESEntity> allIdMOTIVADORESsList;
    
    private String dialogHeader;

    public void setDialogHeader(final String dialogHeader) { 
        this.dialogHeader = dialogHeader;
    }

    public String getDialogHeader() {
        return dialogHeader;
    }

    public void changeHeaderCadastrar() {
        setDialogHeader("Cadastrar Motivadores");
    }
    
    public void changeHeaderEditar() {
        setDialogHeader("Editar Motivadores");
    }
    
    
    public void prepareNewTbMOTIVADORESCARGOS(TbCARGOSEntity tbCARGOS) {
        reset();
        changeHeaderCadastrar();
        this.tbMOTIVADORESCARGOS = new TbMOTIVADORESCARGOSEntity();
        filtraListas(tbCARGOS); 
    }

    public void filtraListas(TbCARGOSEntity tbCARGOS) {

    	tbMOTIVADORESCARGOSs = InicializaTabelasAuxiliaresMO(tbCARGOS);
		allIdMOTIVADORESsList= tbMOTIVADORESService.findAllTbMOTIVADORESEntities();
		for(int i=0;i<this.getTbMOTIVADORESCARGOSs().size();i++) {
			for(int j=0;j<allIdMOTIVADORESsList.size();j++) {
				if(tbMOTIVADORESCARGOSs.get(i).getIdMOTIVADORES().getDeSCMOTIVADORES().
						equals(allIdMOTIVADORESsList.get(j).getDeSCMOTIVADORES())) {
					allIdMOTIVADORESsList.remove(allIdMOTIVADORESsList.get(j));
				}
			}
		}
		
		allIdCARGOSsList = tbCARGOSService.findAllTbCARGOSEntities();

			for(int j2=0;j2<allIdCARGOSsList.size();j2++) {
				if(!tbCARGOS.getId().equals(allIdCARGOSsList.get(j2).getId())) {
					allIdCARGOSsList.remove(allIdCARGOSsList.remove(j2));
				}
			
		}
		if(allIdMOTIVADORESsList.size()==0) {
			FacesContext.getCurrentInstance().validationFailed();
			FacesMessage message = MessageFactory.getMessage("label_listaCultvazia");
			message.setDetail(MessageFactory.getMessageString("label_listaGRvazia_message" ));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			return;
		
		}
	}
    
    
    
    
    
    
    
    public String persist() {

        String message;
        
        try {
            
            if (tbMOTIVADORESCARGOS.getId() != null) {
                tbMOTIVADORESCARGOS = tbMOTIVADORESCARGOSService.update(tbMOTIVADORESCARGOS);
                message = "message_successfully_updated";
            } else {
                tbMOTIVADORESCARGOS = tbMOTIVADORESCARGOSService.save(tbMOTIVADORESCARGOS);
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
        
        tbMOTIVADORESCARGOSList = null;

        FacesMessage facesMessage = MessageFactory.getMessage(message);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        
        return null;
    }
    
    public String delete() {
        
        String message;
        
        try {
            tbMOTIVADORESCARGOSService.delete(tbMOTIVADORESCARGOS);
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
    
    public void onDialogOpen(TbMOTIVADORESCARGOSEntity tbMOTIVADORESCARGOS) {
        
    	reset();
    	changeHeaderEditar();
        this.tbMOTIVADORESCARGOS = tbMOTIVADORESCARGOS;
    }
    
    public void reset() {
        tbMOTIVADORESCARGOS = null;
        tbMOTIVADORESCARGOSList = null;
        
        allIdCARGOSsList = null;
        
        allIdMOTIVADORESsList = null;
        
    }

    // Get a List of all idCARGOS
    public List<TbCARGOSEntity> getIdCARGOSs() {
        if (this.allIdCARGOSsList == null) {
            this.allIdCARGOSsList = tbCARGOSService.findAllTbCARGOSEntities();
        }
        return this.allIdCARGOSsList;
    }
    
    // Update idCARGOS of the current tbMOTIVADORESCARGOS
    public void updateIdCARGOS(TbCARGOSEntity tbCARGOS) {
        this.tbMOTIVADORESCARGOS.setIdCARGOS(tbCARGOS);
        // Maybe we just created and assigned a new tbCARGOS. So reset the allIdCARGOSList.
        allIdCARGOSsList = null;
    }
    
    // Get a List of all idMOTIVADORES
    public List<TbMOTIVADORESEntity> getIdMOTIVADORESs() {
        if (this.allIdMOTIVADORESsList == null) {
            this.allIdMOTIVADORESsList = tbMOTIVADORESService.findAllTbMOTIVADORESEntities();
        }
        return this.allIdMOTIVADORESsList;
    }
    
    // Update idMOTIVADORES of the current tbMOTIVADORESCARGOS
    public void updateIdMOTIVADORES(TbMOTIVADORESEntity tbMOTIVADORES) {
        this.tbMOTIVADORESCARGOS.setIdMOTIVADORES(tbMOTIVADORES);
        // Maybe we just created and assigned a new tbMOTIVADORES. So reset the allIdMOTIVADORESList.
        allIdMOTIVADORESsList = null;
    }
    
    public TbMOTIVADORESCARGOSEntity getTbMOTIVADORESCARGOS() {

        return this.tbMOTIVADORESCARGOS;
    }
    
    public void setTbMOTIVADORESCARGOS(TbMOTIVADORESCARGOSEntity tbMOTIVADORESCARGOS) {
        this.tbMOTIVADORESCARGOS = tbMOTIVADORESCARGOS;
    }
    
    public List<TbMOTIVADORESCARGOSEntity> getTbMOTIVADORESCARGOSList() {
        if (tbMOTIVADORESCARGOSList == null) {
            tbMOTIVADORESCARGOSList = tbMOTIVADORESCARGOSService.findAllTbMOTIVADORESCARGOSEntities();
        }
        return tbMOTIVADORESCARGOSList;
    }

    public void setTbMOTIVADORESCARGOSList(List<TbMOTIVADORESCARGOSEntity> tbMOTIVADORESCARGOSList) {
        this.tbMOTIVADORESCARGOSList = tbMOTIVADORESCARGOSList;
    }
    
    public boolean isPermitted(String permission) {
        return SecurityWrapper.isPermitted(permission);
    }

    public boolean isPermitted(TbMOTIVADORESCARGOSEntity tbMOTIVADORESCARGOS, String permission) {
        
        return SecurityWrapper.isPermitted(permission);
        
    }
    
}
