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
import org.applicationn.simtrilhas.domain.TbGRADECARGOSEntity;
import org.applicationn.simtrilhas.domain.TbGRADEEntity;
import org.applicationn.simtrilhas.service.TbCARGOSService;
import org.applicationn.simtrilhas.service.TbGRADECARGOSService;
import org.applicationn.simtrilhas.service.TbGRADEService;
import org.applicationn.simtrilhas.service.security.SecurityWrapper;
import org.applicationn.simtrilhas.web.util.MessageFactory;
import org.primefaces.PrimeFaces;
@Named("tbGRADECARGOSBean")
@ViewScoped
public class TbGRADECARGOSBean extends TbCARGOSBean  implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(TbGRADECARGOSBean.class.getName());
    
    private List<TbGRADECARGOSEntity> tbGRADECARGOSList;

    private TbGRADECARGOSEntity tbGRADECARGOS;
    
    @Inject
    private TbGRADECARGOSService tbGRADECARGOSService;
    
    @Inject
    private TbCARGOSService tbCARGOSService;
    
    @Inject
    private TbGRADEService tbGRADEService;
    
    private List<TbCARGOSEntity> allIdCARGOSsList;
    
    private List<TbGRADEEntity> allIdGRADEsList;
    
    
    private String dialogHeader;

    public void setDialogHeader(final String dialogHeader) { 
        this.dialogHeader = dialogHeader;
    }

    public String getDialogHeader() {
        return dialogHeader;
    }

    public void changeHeaderCadastrar() {
        setDialogHeader("Cadastrar Competências");
    }
    
    public void changeHeaderEditar() {
        setDialogHeader("Editar Competências");
    }
    
    
    
    public void prepareNewTbGRADESCARGOS(TbCARGOSEntity tbCARGOS) {
        reset();
        changeHeaderCadastrar();
        this.tbGRADECARGOS = new TbGRADECARGOSEntity();
        filtraListas(tbCARGOS); 
    }
    
    
    public void filtraListas(TbCARGOSEntity tbCARGOS) {

    	tbGRADESCARGOSs = InicializaTabelasAuxiliaresGR(tbCARGOS);
		allIdGRADEsList= tbGRADEService.findAllTbGRADEEntities();
		for(int i=0;i<this.getTbGRADESCARGOSs().size();i++) {
			for(int j=0;j<allIdGRADEsList.size();j++) {
				if(tbGRADESCARGOSs.get(i).getIdGRADE().getDeSCGRADE().
						equals(allIdGRADEsList.get(j).getDeSCGRADE())) {
					allIdGRADEsList.remove(allIdGRADEsList.get(j));
				}
			}
		}
		
		allIdCARGOSsList = tbCARGOSService.findAllTbCARGOSEntities();

			for(int j2=0;j2<allIdCARGOSsList.size();j2++) {
				if(!tbCARGOS.getId().equals(allIdCARGOSsList.get(j2).getId())) {
					allIdCARGOSsList.remove(allIdCARGOSsList.remove(j2));
				}
			
		}
		if(allIdGRADEsList.size()==0) {
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
            
            if (tbGRADECARGOS.getId() != null) {
                tbGRADECARGOS = tbGRADECARGOSService.update(tbGRADECARGOS);
                message = "message_successfully_updated";
            } else {
                tbGRADECARGOS = tbGRADECARGOSService.save(tbGRADECARGOS);
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
        
        tbGRADECARGOSList = null;

        FacesMessage facesMessage = MessageFactory.getMessage(message);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        
        return null;
    }
    
    public String delete() {
        
        String message;
        
        try {
            tbGRADECARGOSService.delete(tbGRADECARGOS);
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
    
    public void onDialogOpen(TbGRADECARGOSEntity tbGRADECARGOS) {
        reset();
        changeHeaderEditar();
        this.tbGRADECARGOS = tbGRADECARGOS;
    }
    
    public void reset() {
        tbGRADECARGOS = null;
        tbGRADECARGOSList = null;
        
        allIdCARGOSsList = null;
        
        allIdGRADEsList = null;
        
    }

    // Get a List of all idCARGOS
    public List<TbCARGOSEntity> getIdCARGOSs() {
        if (this.allIdCARGOSsList == null) {
            this.allIdCARGOSsList = tbCARGOSService.findAllTbCARGOSEntities();
        }
        return this.allIdCARGOSsList;
    }
    
    // Update idCARGOS of the current tbGRADECARGOS
    public void updateIdCARGOS(TbCARGOSEntity tbCARGOS) {
        this.tbGRADECARGOS.setIdCARGOS(tbCARGOS);
        // Maybe we just created and assigned a new tbCARGOS. So reset the allIdCARGOSList.
        allIdCARGOSsList = null;
    }
    
    // Get a List of all idGRADE
    public List<TbGRADEEntity> getIdGRADEs() {
        if (this.allIdGRADEsList == null) {
            this.allIdGRADEsList = tbGRADEService.findAllTbGRADEEntities();
        }
        return this.allIdGRADEsList;
    }
    
    // Update idGRADE of the current tbGRADECARGOS
    public void updateIdGRADE(TbGRADEEntity tbGRADE) {
        this.tbGRADECARGOS.setIdGRADE(tbGRADE);
        // Maybe we just created and assigned a new tbGRADE. So reset the allIdGRADEList.
        allIdGRADEsList = null;
    }
    
    public TbGRADECARGOSEntity getTbGRADECARGOS() {
        return this.tbGRADECARGOS;
    }
    
    public void setTbGRADECARGOS(TbGRADECARGOSEntity tbGRADECARGOS) {
        this.tbGRADECARGOS = tbGRADECARGOS;
    }
    
    public List<TbGRADECARGOSEntity> getTbGRADECARGOSList() {
        if (tbGRADECARGOSList == null) {
            tbGRADECARGOSList = tbGRADECARGOSService.findAllTbGRADECARGOSEntities();
        }
        return tbGRADECARGOSList;
    }

    public void setTbGRADECARGOSList(List<TbGRADECARGOSEntity> tbGRADECARGOSList) {
        this.tbGRADECARGOSList = tbGRADECARGOSList;
    }
    
    public boolean isPermitted(String permission) {
        return SecurityWrapper.isPermitted(permission);
    }

    public boolean isPermitted(TbGRADECARGOSEntity tbGRADECARGOS, String permission) {
        
        return SecurityWrapper.isPermitted(permission);
        
    }
    
}
