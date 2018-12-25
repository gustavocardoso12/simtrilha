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

@Named("tbCONHECIMENTOSBASCARGOSBean")
@ViewScoped
public class TbCONHECIMENTOSBASCARGOSBean implements Serializable {

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
    
    public void prepareNewTbCONHECIMENTOSBASCARGOS() {
        reset();
        this.tbCONHECIMENTOSBASCARGOS = new TbCONHECIMENTOSBASCARGOSEntity();
        // set any default values now, if you need
        // Example: this.tbCONHECIMENTOSBASCARGOS.setAnything("test");
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
    
    public void onDialogOpen(TbCONHECIMENTOSBASCARGOSEntity tbCONHECIMENTOSBASCARGOS) {
        reset();
        this.tbCONHECIMENTOSBASCARGOS = tbCONHECIMENTOSBASCARGOS;
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
        if (this.tbCONHECIMENTOSBASCARGOS == null) {
            prepareNewTbCONHECIMENTOSBASCARGOS();
        }
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
