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
import org.applicationn.simtrilhas.domain.TbPERFILCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbPERFILEntity;
import org.applicationn.simtrilhas.service.TbCARGOSService;
import org.applicationn.simtrilhas.service.TbPERFILCARGOSService;
import org.applicationn.simtrilhas.service.TbPERFILService;
import org.applicationn.simtrilhas.service.security.SecurityWrapper;
import org.applicationn.simtrilhas.web.util.MessageFactory;
import org.primefaces.PrimeFaces;

@Named("tbPERFILCARGOSBean")
@ViewScoped
public class TbPERFILCARGOSBean extends TbCARGOSBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(TbPERFILCARGOSBean.class.getName());
    
    private List<TbPERFILCARGOSEntity> tbPERFILCARGOSList;

    private TbPERFILCARGOSEntity tbPERFILCARGOS;
    
    @Inject
    private TbPERFILCARGOSService tbPERFILCARGOSService;
    
    @Inject
    private TbCARGOSService tbCARGOSService;
    
    @Inject
    private TbPERFILService tbPERFILService;
    
    private List<TbCARGOSEntity> allIdCARGOSsList;
    
    private List<TbPERFILEntity> allIdPERFILsList;
    
    private String dialogHeader;

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
    
    
    public void prepareNewTbPERFILCARGOS(TbCARGOSEntity tbCARGOS) {
        reset();
        changeHeaderCadastrar();
        this.tbPERFILCARGOS = new TbPERFILCARGOSEntity();
        filtraListas(tbCARGOS); 
    }
    
    
    public void filtraListas(TbCARGOSEntity tbCARGOS) {

    	tbPERFILCARGOSs = InicializaTabelasAuxiliaresPE(tbCARGOS);
		allIdPERFILsList= tbPERFILService.findAllTbPERFILEntities();
		for(int i=0;i<this.getTbPERFILCARGOSs().size();i++) {
			for(int j=0;j<allIdPERFILsList.size();j++) {
				if(tbPERFILCARGOSs.get(i).getIdPERFIL().getDeSCPERFIL().
						equals(allIdPERFILsList.get(j).getDeSCPERFIL())) {
					allIdPERFILsList.remove(allIdPERFILsList.get(j));
				}
			}
		}
		
		allIdCARGOSsList = tbCARGOSService.findAllTbCARGOSEntities();

			for(int j2=0;j2<allIdCARGOSsList.size();j2++) {
				if(!tbCARGOS.getId().equals(allIdCARGOSsList.get(j2).getId())) {
					allIdCARGOSsList.remove(allIdCARGOSsList.remove(j2));
				}
			
		}
		if(allIdPERFILsList.size()==0) {
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
            
            if (tbPERFILCARGOS.getId() != null) {
                tbPERFILCARGOS = tbPERFILCARGOSService.update(tbPERFILCARGOS);
                message = "message_successfully_updated";
            } else {
                tbPERFILCARGOS = tbPERFILCARGOSService.save(tbPERFILCARGOS);
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
        
        tbPERFILCARGOSList = null;

        FacesMessage facesMessage = MessageFactory.getMessage(message);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        
        return null;
    }
    
    public String delete() {
        
        String message;
        
        try {
            tbPERFILCARGOSService.delete(tbPERFILCARGOS);
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
    
    public void onDialogOpen(TbPERFILCARGOSEntity tbPERFILCARGOS) {
        reset();
        changeHeaderEditar();
        this.tbPERFILCARGOS = tbPERFILCARGOS;
    }
    
    public void reset() {
        tbPERFILCARGOS = null;
        tbPERFILCARGOSList = null;
        
        allIdCARGOSsList = null;
        
        allIdPERFILsList = null;
        
    }

    // Get a List of all idCARGOS
    public List<TbCARGOSEntity> getIdCARGOSs() {
        if (this.allIdCARGOSsList == null) {
            this.allIdCARGOSsList = tbCARGOSService.findAllTbCARGOSEntities();
        }
        return this.allIdCARGOSsList;
    }
    
    // Update idCARGOS of the current tbPERFILCARGOS
    public void updateIdCARGOS(TbCARGOSEntity tbCARGOS) {
        this.tbPERFILCARGOS.setIdCARGOS(tbCARGOS);
        // Maybe we just created and assigned a new tbCARGOS. So reset the allIdCARGOSList.
        allIdCARGOSsList = null;
    }
    
    // Get a List of all idPERFIL
    public List<TbPERFILEntity> getIdPERFILs() {
        if (this.allIdPERFILsList == null) {
            this.allIdPERFILsList = tbPERFILService.findAllTbPERFILEntities();
        }
        return this.allIdPERFILsList;
    }
    
    // Update idPERFIL of the current tbPERFILCARGOS
    public void updateIdPERFIL(TbPERFILEntity tbPERFIL) {
        this.tbPERFILCARGOS.setIdPERFIL(tbPERFIL);
        // Maybe we just created and assigned a new tbPERFIL. So reset the allIdPERFILList.
        allIdPERFILsList = null;
    }
    
    public TbPERFILCARGOSEntity getTbPERFILCARGOS() {
        return this.tbPERFILCARGOS;
    }
    
    public void setTbPERFILCARGOS(TbPERFILCARGOSEntity tbPERFILCARGOS) {
        this.tbPERFILCARGOS = tbPERFILCARGOS;
    }
    
    public List<TbPERFILCARGOSEntity> getTbPERFILCARGOSList() {
        if (tbPERFILCARGOSList == null) {
            tbPERFILCARGOSList = tbPERFILCARGOSService.findAllTbPERFILCARGOSEntities();
        }
        return tbPERFILCARGOSList;
    }

    public void setTbPERFILCARGOSList(List<TbPERFILCARGOSEntity> tbPERFILCARGOSList) {
        this.tbPERFILCARGOSList = tbPERFILCARGOSList;
    }
    
    public boolean isPermitted(String permission) {
        return SecurityWrapper.isPermitted(permission);
    }

    public boolean isPermitted(TbPERFILCARGOSEntity tbPERFILCARGOS, String permission) {
        
        return SecurityWrapper.isPermitted(permission);
        
    }
    
}
