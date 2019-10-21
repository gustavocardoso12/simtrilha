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
import org.applicationn.simtrilhas.domain.TbCOMPETENCIASEMCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbCOMPETENCIASEMOCIONAISEntity;
import org.applicationn.simtrilhas.service.TbCARGOSService;
import org.applicationn.simtrilhas.service.TbCOMPETENCIASEMCARGOSService;
import org.applicationn.simtrilhas.service.TbCOMPETENCIASEMOCIONAISService;
import org.applicationn.simtrilhas.service.security.SecurityWrapper;
import org.applicationn.simtrilhas.web.util.MessageFactory;
import org.primefaces.context.PrimeRequestContext;

@Named("tbCOMPETENCIASEMCARGOSBean")
@ViewScoped
public class TbCOMPETENCIASEMCARGOSBean extends TbCARGOSBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(TbCOMPETENCIASEMCARGOSBean.class.getName());
    
    private List<TbCOMPETENCIASEMCARGOSEntity> tbCOMPETENCIASEMCARGOSList;

    private TbCOMPETENCIASEMCARGOSEntity tbCOMPETENCIASEMCARGOS;
    
    @Inject
    private TbCOMPETENCIASEMCARGOSService tbCOMPETENCIASEMCARGOSService;
    
    @Inject
    private TbCARGOSService tbCARGOSService;
    
    @Inject
    private TbCOMPETENCIASEMOCIONAISService tbCOMPETENCIASEMOCIONAISService;
    
    private List<TbCARGOSEntity> allIdCARGOSsList;
    
    private List<TbCOMPETENCIASEMOCIONAISEntity> allIdCOMPEMsList;
    
    
    private String dialogHeader;
    
    

   	public void setDialogHeader(final String dialogHeader) { 
   		this.dialogHeader = dialogHeader;
   	}

   	public String getDialogHeader() {
   		return dialogHeader;
   	}

   	public void changeHeaderCadastrar() {
   		setDialogHeader("Cadastrar Competências emocionais");
   	}

   	public void changeHeaderEditar() {
   		setDialogHeader("Editar Competências emocionais");
   	}
    
    public void prepareNewTbCOMPETENCIASEMCARGOS(TbCARGOSEntity tbCARGOS) {
        reset();
        changeHeaderCadastrar();
        this.tbCOMPETENCIASEMCARGOS = new TbCOMPETENCIASEMCARGOSEntity();
        //filtraListas(tbCARGOS);
    }
    
    public void onDialogOpen(TbCOMPETENCIASEMCARGOSEntity tbCOMPETENCIASEMCARGOS) {
        reset();
        changeHeaderEditar();
        this.tbCOMPETENCIASEMCARGOS = tbCOMPETENCIASEMCARGOS;
    }
    
   /* public void filtraListas(TbCARGOSEntity tbCARGOS) {

    	tbCOMPETENCIASEMs = InicializaTabelasAuxiliaresEM(tbCARGOS);
		allIdCOMPEMsList= tbCOMPETENCIASEMOCIONAISService.findAllTbCOMPETENCIASEMOCIONAISEntities();
		for(int i=0;i<this.getTbCOMPETENCIASEMs().size();i++) {
			for(int j=0;j<allIdCOMPEMsList.size();j++) {
				if(tbCOMPETENCIASEMs.get(i).getIdCOMPEM().getDeSCCOMPEMOCIONAIS().
						equals(allIdCOMPEMsList.get(j).getDeSCCOMPEMOCIONAIS())) {
					allIdCOMPEMsList.remove(allIdCOMPEMsList.get(j));
				}
			}
		}
		
		allIdCARGOSsList = tbCARGOSService.findAllTbCARGOSEntities();

			for(int j2=0;j2<allIdCARGOSsList.size();j2++) {
				if(!tbCARGOS.getId().equals(allIdCARGOSsList.get(j2).getId())) {
					allIdCARGOSsList.remove(allIdCARGOSsList.remove(j2));
				}
			
		}
		if(allIdCOMPEMsList.size()==0) {
			FacesContext.getCurrentInstance().validationFailed();
			FacesMessage message = MessageFactory.getMessage("label_listaCultvazia");
			message.setDetail(MessageFactory.getMessageString("label_listaELIvazia_message" ));
		
			return;
		
		}
	}*/

    public String persist() {

        String message;
        
        try {
            
            if (tbCOMPETENCIASEMCARGOS.getId() != null) {
                tbCOMPETENCIASEMCARGOS = tbCOMPETENCIASEMCARGOSService.update(tbCOMPETENCIASEMCARGOS);
                message = "message_successfully_updated";
            } else {
                tbCOMPETENCIASEMCARGOS = tbCOMPETENCIASEMCARGOSService.save(tbCOMPETENCIASEMCARGOS);
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
        
        tbCOMPETENCIASEMCARGOSList = null;

        FacesMessage facesMessage = MessageFactory.getMessage(message);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        
        return null;
    }
    
    public String delete() {
        
        String message;
        
        try {
            tbCOMPETENCIASEMCARGOSService.delete(tbCOMPETENCIASEMCARGOS);
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
        tbCOMPETENCIASEMCARGOS = null;
        tbCOMPETENCIASEMCARGOSList = null;
        
        allIdCARGOSsList = null;
        
        allIdCOMPEMsList = null;
        
    }

    // Get a List of all idCARGOS
    public List<TbCARGOSEntity> getIdCARGOSs() {
        if (this.allIdCARGOSsList == null) {
            this.allIdCARGOSsList = tbCARGOSService.findAllTbCARGOSEntities();
        }
        return this.allIdCARGOSsList;
    }
    
    // Update idCARGOS of the current tbCOMPETENCIASEMCARGOS
    public void updateIdCARGOS(TbCARGOSEntity tbCARGOS) {
        this.tbCOMPETENCIASEMCARGOS.setIdCARGOS(tbCARGOS);
        // Maybe we just created and assigned a new tbCARGOS. So reset the allIdCARGOSList.
        allIdCARGOSsList = null;
    }
    
    // Get a List of all idCOMPEM
    public List<TbCOMPETENCIASEMOCIONAISEntity> getIdCOMPEMs() {
        if (this.allIdCOMPEMsList == null) {
            this.allIdCOMPEMsList = tbCOMPETENCIASEMOCIONAISService.findAllTbCOMPETENCIASEMOCIONAISEntities();
        }
        return this.allIdCOMPEMsList;
    }
    
    // Update idCOMPEM of the current tbCOMPETENCIASEMCARGOS
    public void updateIdCOMPEM(TbCOMPETENCIASEMOCIONAISEntity tbCOMPETENCIASEMOCIONAIS) {
        this.tbCOMPETENCIASEMCARGOS.setIdCOMPEM(tbCOMPETENCIASEMOCIONAIS);
        // Maybe we just created and assigned a new tbCOMPETENCIASEMOCIONAIS. So reset the allIdCOMPEMList.
        allIdCOMPEMsList = null;
    }
    
    public TbCOMPETENCIASEMCARGOSEntity getTbCOMPETENCIASEMCARGOS() {
        return this.tbCOMPETENCIASEMCARGOS;
    }
    
    public void setTbCOMPETENCIASEMCARGOS(TbCOMPETENCIASEMCARGOSEntity tbCOMPETENCIASEMCARGOS) {
        this.tbCOMPETENCIASEMCARGOS = tbCOMPETENCIASEMCARGOS;
    }
    
    public List<TbCOMPETENCIASEMCARGOSEntity> getTbCOMPETENCIASEMCARGOSList() {
        if (tbCOMPETENCIASEMCARGOSList == null) {
            tbCOMPETENCIASEMCARGOSList = tbCOMPETENCIASEMCARGOSService.findAllTbCOMPETENCIASEMCARGOSEntities();
        }
        return tbCOMPETENCIASEMCARGOSList;
    }

    public void setTbCOMPETENCIASEMCARGOSList(List<TbCOMPETENCIASEMCARGOSEntity> tbCOMPETENCIASEMCARGOSList) {
        this.tbCOMPETENCIASEMCARGOSList = tbCOMPETENCIASEMCARGOSList;
    }
    
    public boolean isPermitted(String permission) {
        return SecurityWrapper.isPermitted(permission);
    }

    public boolean isPermitted(TbCOMPETENCIASEMCARGOSEntity tbCOMPETENCIASEMCARGOS, String permission) {
        
        return SecurityWrapper.isPermitted(permission);
        
    }
    
}
