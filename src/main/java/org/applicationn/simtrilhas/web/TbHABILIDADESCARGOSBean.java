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
import org.applicationn.simtrilhas.domain.TbHABILIDADESCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbHABILIDADESEntity;
import org.applicationn.simtrilhas.service.TbCARGOSService;
import org.applicationn.simtrilhas.service.TbHABILIDADESCARGOSService;
import org.applicationn.simtrilhas.service.TbHABILIDADESService;
import org.applicationn.simtrilhas.service.security.SecurityWrapper;
import org.applicationn.simtrilhas.web.util.MessageFactory;

@Named("tbHABILIDADESCARGOSBean")
@ViewScoped
public class TbHABILIDADESCARGOSBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(TbHABILIDADESCARGOSBean.class.getName());
    
    private List<TbHABILIDADESCARGOSEntity> tbHABILIDADESCARGOSList;

    private TbHABILIDADESCARGOSEntity tbHABILIDADESCARGOS;
    
    @Inject
    private TbHABILIDADESCARGOSService tbHABILIDADESCARGOSService;
    
    @Inject
    private TbCARGOSService tbCARGOSService;
    
    @Inject
    private TbHABILIDADESService tbHABILIDADESService;
    
    private List<TbCARGOSEntity> allIdCARGOSsList;
    
    private List<TbHABILIDADESEntity> allIdHABCARGOSsList;
    
    public void prepareNewTbHABILIDADESCARGOS() {
        reset();
        this.tbHABILIDADESCARGOS = new TbHABILIDADESCARGOSEntity();
        // set any default values now, if you need
        // Example: this.tbHABILIDADESCARGOS.setAnything("test");
    }

    public String persist() {

        String message;
        
        try {
            
            if (tbHABILIDADESCARGOS.getId() != null) {
                tbHABILIDADESCARGOS = tbHABILIDADESCARGOSService.update(tbHABILIDADESCARGOS);
                message = "message_successfully_updated";
            } else {
                tbHABILIDADESCARGOS = tbHABILIDADESCARGOSService.save(tbHABILIDADESCARGOS);
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
        
        tbHABILIDADESCARGOSList = null;

        FacesMessage facesMessage = MessageFactory.getMessage(message);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        
        return null;
    }
    
    public String delete() {
        
        String message;
        
        try {
            tbHABILIDADESCARGOSService.delete(tbHABILIDADESCARGOS);
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
    
    public void onDialogOpen(TbHABILIDADESCARGOSEntity tbHABILIDADESCARGOS) {
        reset();
        this.tbHABILIDADESCARGOS = tbHABILIDADESCARGOS;
    }
    
    public void reset() {
        tbHABILIDADESCARGOS = null;
        tbHABILIDADESCARGOSList = null;
        
        allIdCARGOSsList = null;
        
        allIdHABCARGOSsList = null;
        
    }

    // Get a List of all idCARGOS
    public List<TbCARGOSEntity> getIdCARGOSs() {
        if (this.allIdCARGOSsList == null) {
            this.allIdCARGOSsList = tbCARGOSService.findAllTbCARGOSEntities();
        }
        return this.allIdCARGOSsList;
    }
    
    // Update idCARGOS of the current tbHABILIDADESCARGOS
    public void updateIdCARGOS(TbCARGOSEntity tbCARGOS) {
        this.tbHABILIDADESCARGOS.setIdCARGOS(tbCARGOS);
        // Maybe we just created and assigned a new tbCARGOS. So reset the allIdCARGOSList.
        allIdCARGOSsList = null;
    }
    
    // Get a List of all idHABCARGOS
    public List<TbHABILIDADESEntity> getIdHABCARGOSs() {
        if (this.allIdHABCARGOSsList == null) {
            this.allIdHABCARGOSsList = tbHABILIDADESService.findAllTbHABILIDADESEntities();
        }
        return this.allIdHABCARGOSsList;
    }
    
    // Update idHABCARGOS of the current tbHABILIDADESCARGOS
    public void updateIdHABCARGOS(TbHABILIDADESEntity tbHABILIDADES) {
        this.tbHABILIDADESCARGOS.setIdHABCARGOS(tbHABILIDADES);
        // Maybe we just created and assigned a new tbHABILIDADES. So reset the allIdHABCARGOSList.
        allIdHABCARGOSsList = null;
    }
    
    public TbHABILIDADESCARGOSEntity getTbHABILIDADESCARGOS() {
        if (this.tbHABILIDADESCARGOS == null) {
            prepareNewTbHABILIDADESCARGOS();
        }
        return this.tbHABILIDADESCARGOS;
    }
    
    public void setTbHABILIDADESCARGOS(TbHABILIDADESCARGOSEntity tbHABILIDADESCARGOS) {
        this.tbHABILIDADESCARGOS = tbHABILIDADESCARGOS;
    }
    
    public List<TbHABILIDADESCARGOSEntity> getTbHABILIDADESCARGOSList() {
        if (tbHABILIDADESCARGOSList == null) {
            tbHABILIDADESCARGOSList = tbHABILIDADESCARGOSService.findAllTbHABILIDADESCARGOSEntities();
        }
        return tbHABILIDADESCARGOSList;
    }

    public void setTbHABILIDADESCARGOSList(List<TbHABILIDADESCARGOSEntity> tbHABILIDADESCARGOSList) {
        this.tbHABILIDADESCARGOSList = tbHABILIDADESCARGOSList;
    }
    
    public boolean isPermitted(String permission) {
        return SecurityWrapper.isPermitted(permission);
    }

    public boolean isPermitted(TbHABILIDADESCARGOSEntity tbHABILIDADESCARGOS, String permission) {
        
        return SecurityWrapper.isPermitted(permission);
        
    }
    
}
