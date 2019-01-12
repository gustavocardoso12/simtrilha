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
import org.applicationn.simtrilhas.domain.TbCONHECIMENTOSBASCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbCONHECIMENTOSBASICOSEntity;
import org.applicationn.simtrilhas.service.TbCARGOSService;
import org.applicationn.simtrilhas.service.TbCONHECIMENTOSBASCARGOSService;
import org.applicationn.simtrilhas.service.TbCONHECIMENTOSBASICOSService;
import org.applicationn.simtrilhas.service.security.SecurityWrapper;
import org.applicationn.simtrilhas.web.util.MessageFactory;
import org.primefaces.context.RequestContext;

@Named("tbCONHECIMENTOSBASCARGOSBean")
@ViewScoped
public class TbCONHECIMENTOSBASCARGOSBean extends TbCARGOSBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(TbCONHECIMENTOSBASCARGOSBean.class.getName());
    
    private List<TbCONHECIMENTOSBASCARGOSEntity> tbCONHECIMENTOSBASCARGOSList;

    private TbCONHECIMENTOSBASCARGOSEntity tbCONHECIMENTOSBASCARGOS;
    
    @Inject
    private TbCONHECIMENTOSBASCARGOSService tbCONHECIMENTOSBASCARGOSService;
    
    @Inject
    private TbCARGOSService tbCARGOSService;
    
    @Inject
    private TbCONHECIMENTOSBASICOSService tbCONHECIMENTOSBASICOSService;
    
    private List<TbCARGOSEntity> allIdCARGOSsList;
    
    private List<TbCONHECIMENTOSBASICOSEntity> allIdCONHECBASsList;
    
    private String dialogHeader;
    
    

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
    
    public void prepareNewTbCONHECIMENTOSBASCARGOS(TbCARGOSEntity tbCARGOS) {
        reset();
        changeHeaderCadastrar();
        this.tbCONHECIMENTOSBASCARGOS = new TbCONHECIMENTOSBASCARGOSEntity();
        filtraListas(tbCARGOS); 
    }
    
 
    
    public void onDialogOpen(TbCONHECIMENTOSBASCARGOSEntity tbCONHECIMENTOSBASCARGOS) {
        reset();
        changeHeaderEditar();
        this.tbCONHECIMENTOSBASCARGOS = tbCONHECIMENTOSBASCARGOS;
    }
    
    
    public void filtraListas(TbCARGOSEntity tbCARGOS) {

    	tbCONHECIMENTOSBASICOSCARGOSs = InicializaTabelasAuxiliaresCB(tbCARGOS);
		allIdCONHECBASsList= tbCONHECIMENTOSBASICOSService.findAllTbCONHECIMENTOSBASICOSEntities();
		for(int i=0;i<this.getTbCONHECIMENTOSBASICOSCARGOSs().size();i++) {
			for(int j=0;j<allIdCONHECBASsList.size();j++) {
				if(tbCONHECIMENTOSBASICOSCARGOSs.get(i).getIdCONHECBAS().getDeSCCONHECIMENTOSBASICOS().
						equals(allIdCONHECBASsList.get(j).getDeSCCONHECIMENTOSBASICOS())) {
					allIdCONHECBASsList.remove(allIdCONHECBASsList.get(j));
				}
			}
		}
		
		allIdCARGOSsList = tbCARGOSService.findAllTbCARGOSEntities();

			for(int j2=0;j2<allIdCARGOSsList.size();j2++) {
				if(!tbCARGOS.getId().equals(allIdCARGOSsList.get(j2).getId())) {
					allIdCARGOSsList.remove(allIdCARGOSsList.remove(j2));
				}
			
		}
		if(allIdCONHECBASsList.size()==0) {
			FacesContext.getCurrentInstance().validationFailed();
			FacesMessage message = MessageFactory.getMessage("label_listaCultvazia");
			message.setDetail(MessageFactory.getMessageString("label_listaCBvazia_message" ));
			RequestContext.getCurrentInstance().showMessageInDialog(message);
			return;
		
		}
	}

    public String persist() {

        String message;
        
        try {
            
            if (tbCONHECIMENTOSBASCARGOS.getId() != null) {
                tbCONHECIMENTOSBASCARGOS = tbCONHECIMENTOSBASCARGOSService.update(tbCONHECIMENTOSBASCARGOS);
                message = "message_successfully_updated";
            } else {
                tbCONHECIMENTOSBASCARGOS = tbCONHECIMENTOSBASCARGOSService.save(tbCONHECIMENTOSBASCARGOS);
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
        
        tbCONHECIMENTOSBASCARGOSList = null;

        FacesMessage facesMessage = MessageFactory.getMessage(message);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        
        return null;
    }
    
    public String delete() {
        
        String message;
        
        try {
            tbCONHECIMENTOSBASCARGOSService.delete(tbCONHECIMENTOSBASCARGOS);
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
        tbCONHECIMENTOSBASCARGOS = null;
        tbCONHECIMENTOSBASCARGOSList = null;
        
        allIdCARGOSsList = null;
        
        allIdCONHECBASsList = null;
        
    }

    // Get a List of all idCARGOS
    public List<TbCARGOSEntity> getIdCARGOSs() {
        if (this.allIdCARGOSsList == null) {
            this.allIdCARGOSsList = tbCARGOSService.findAllTbCARGOSEntities();
        }
        return this.allIdCARGOSsList;
    }
    
    // Update idCARGOS of the current tbCONHECIMENTOSBASCARGOS
    public void updateIdCARGOS(TbCARGOSEntity tbCARGOS) {
        this.tbCONHECIMENTOSBASCARGOS.setIdCARGOS(tbCARGOS);
        // Maybe we just created and assigned a new tbCARGOS. So reset the allIdCARGOSList.
        allIdCARGOSsList = null;
    }
    
    // Get a List of all idCONHECBAS
    public List<TbCONHECIMENTOSBASICOSEntity> getIdCONHECBASs() {
        if (this.allIdCONHECBASsList == null) {
            this.allIdCONHECBASsList = tbCONHECIMENTOSBASICOSService.findAllTbCONHECIMENTOSBASICOSEntities();
        }
        return this.allIdCONHECBASsList;
    }
    
    // Update idCONHECBAS of the current tbCONHECIMENTOSBASCARGOS
    public void updateIdCONHECBAS(TbCONHECIMENTOSBASICOSEntity tbCONHECIMENTOSBASICOS) {
        this.tbCONHECIMENTOSBASCARGOS.setIdCONHECBAS(tbCONHECIMENTOSBASICOS);
        // Maybe we just created and assigned a new tbCONHECIMENTOSBASICOS. So reset the allIdCONHECBASList.
        allIdCONHECBASsList = null;
    }
    
    public TbCONHECIMENTOSBASCARGOSEntity getTbCONHECIMENTOSBASCARGOS() {

        return this.tbCONHECIMENTOSBASCARGOS;
    }
    
    public void setTbCONHECIMENTOSBASCARGOS(TbCONHECIMENTOSBASCARGOSEntity tbCONHECIMENTOSBASCARGOS) {
        this.tbCONHECIMENTOSBASCARGOS = tbCONHECIMENTOSBASCARGOS;
    }
    
    public List<TbCONHECIMENTOSBASCARGOSEntity> getTbCONHECIMENTOSBASCARGOSList() {
        if (tbCONHECIMENTOSBASCARGOSList == null) {
            tbCONHECIMENTOSBASCARGOSList = tbCONHECIMENTOSBASCARGOSService.findAllTbCONHECIMENTOSBASCARGOSEntities();
        }
        return tbCONHECIMENTOSBASCARGOSList;
    }

    public void setTbCONHECIMENTOSBASCARGOSList(List<TbCONHECIMENTOSBASCARGOSEntity> tbCONHECIMENTOSBASCARGOSList) {
        this.tbCONHECIMENTOSBASCARGOSList = tbCONHECIMENTOSBASCARGOSList;
    }
    
    public boolean isPermitted(String permission) {
        return SecurityWrapper.isPermitted(permission);
    }

    public boolean isPermitted(TbCONHECIMENTOSBASCARGOSEntity tbCONHECIMENTOSBASCARGOS, String permission) {
        
        return SecurityWrapper.isPermitted(permission);
        
    }
    
}
