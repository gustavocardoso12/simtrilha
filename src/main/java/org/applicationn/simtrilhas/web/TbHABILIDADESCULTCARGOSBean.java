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
import org.applicationn.simtrilhas.domain.TbHABILIDADESCULTCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbHABILIDADESCULTURAISEntity;
import org.applicationn.simtrilhas.service.TbCARGOSService;
import org.applicationn.simtrilhas.service.TbHABILIDADESCULTCARGOSService;
import org.applicationn.simtrilhas.service.TbHABILIDADESCULTURAISService;
import org.applicationn.simtrilhas.service.security.SecurityWrapper;
import org.applicationn.simtrilhas.web.util.MessageFactory;

@Named("tbHABILIDADESCULTCARGOSBean")
@ViewScoped
public class TbHABILIDADESCULTCARGOSBean implements Serializable {

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
    
    private List<TbHABILIDADESCULTURAISEntity> allIdHABCULTCARsList;
    
    public void prepareNewTbHABILIDADESCULTCARGOS() {
        reset();
        this.tbHABILIDADESCULTCARGOS = new TbHABILIDADESCULTCARGOSEntity();
        // set any default values now, if you need
        // Example: this.tbHABILIDADESCULTCARGOS.setAnything("test");
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
    
    public void onDialogOpen(TbHABILIDADESCULTCARGOSEntity tbHABILIDADESCULTCARGOS) {
        reset();
        this.tbHABILIDADESCULTCARGOS = tbHABILIDADESCULTCARGOS;
    }
    
    public void reset() {
        tbHABILIDADESCULTCARGOS = null;
        tbHABILIDADESCULTCARGOSList = null;
        
        allIdCARGOSsList = null;
        
        allIdHABCULTCARsList = null;
        
    }

    // Get a List of all idCARGOS
    public List<TbCARGOSEntity> getIdCARGOSs() {
        if (this.allIdCARGOSsList == null) {
            this.allIdCARGOSsList = tbCARGOSService.findAllTbCARGOSEntities();
        }
        return this.allIdCARGOSsList;
    }
    
    // Update idCARGOS of the current tbHABILIDADESCULTCARGOS
    public void updateIdCARGOS(TbCARGOSEntity tbCARGOS) {
        this.tbHABILIDADESCULTCARGOS.setIdCARGOS(tbCARGOS);
        // Maybe we just created and assigned a new tbCARGOS. So reset the allIdCARGOSList.
        allIdCARGOSsList = null;
    }
    
    // Get a List of all idHABCULTCAR
    public List<TbHABILIDADESCULTURAISEntity> getIdHABCULTCARs() {
        if (this.allIdHABCULTCARsList == null) {
            this.allIdHABCULTCARsList = tbHABILIDADESCULTURAISService.findAllTbHABILIDADESCULTURAISEntities();
        }
        return this.allIdHABCULTCARsList;
    }
    
    // Update idHABCULTCAR of the current tbHABILIDADESCULTCARGOS
    public void updateIdHABCULTCAR(TbHABILIDADESCULTURAISEntity tbHABILIDADESCULTURAIS) {
        this.tbHABILIDADESCULTCARGOS.setIdHABCULTCAR(tbHABILIDADESCULTURAIS);
        // Maybe we just created and assigned a new tbHABILIDADESCULTURAIS. So reset the allIdHABCULTCARList.
        allIdHABCULTCARsList = null;
    }
    
    public TbHABILIDADESCULTCARGOSEntity getTbHABILIDADESCULTCARGOS() {
        if (this.tbHABILIDADESCULTCARGOS == null) {
            prepareNewTbHABILIDADESCULTCARGOS();
        }
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
    
}
