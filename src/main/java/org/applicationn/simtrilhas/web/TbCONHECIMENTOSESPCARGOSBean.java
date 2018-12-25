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
import org.applicationn.simtrilhas.domain.TbCONHECIMENTOSESPCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbCONHECIMENTOSESPECIFICOSEntity;
import org.applicationn.simtrilhas.service.TbCARGOSService;
import org.applicationn.simtrilhas.service.TbCONHECIMENTOSESPCARGOSService;
import org.applicationn.simtrilhas.service.TbCONHECIMENTOSESPECIFICOSService;
import org.applicationn.simtrilhas.service.security.SecurityWrapper;
import org.applicationn.simtrilhas.web.util.MessageFactory;

@Named("tbCONHECIMENTOSESPCARGOSBean")
@ViewScoped
public class TbCONHECIMENTOSESPCARGOSBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(TbCONHECIMENTOSESPCARGOSBean.class.getName());
    
    private List<TbCONHECIMENTOSESPCARGOSEntity> tbCONHECIMENTOSESPCARGOSList;

    private TbCONHECIMENTOSESPCARGOSEntity tbCONHECIMENTOSESPCARGOS;
    
    @Inject
    private TbCONHECIMENTOSESPCARGOSService tbCONHECIMENTOSESPCARGOSService;
    
    @Inject
    private TbCARGOSService tbCARGOSService;
    
    @Inject
    private TbCONHECIMENTOSESPECIFICOSService tbCONHECIMENTOSESPECIFICOSService;
    
    private List<TbCARGOSEntity> allIdCARGOSsList;
    
    private List<TbCONHECIMENTOSESPECIFICOSEntity> allIdCONHECESPsList;
    
    public void prepareNewTbCONHECIMENTOSESPCARGOS() {
        reset();
        this.tbCONHECIMENTOSESPCARGOS = new TbCONHECIMENTOSESPCARGOSEntity();
        // set any default values now, if you need
        // Example: this.tbCONHECIMENTOSESPCARGOS.setAnything("test");
    }

    public String persist() {

        String message;
        
        try {
            
            if (tbCONHECIMENTOSESPCARGOS.getId() != null) {
                tbCONHECIMENTOSESPCARGOS = tbCONHECIMENTOSESPCARGOSService.update(tbCONHECIMENTOSESPCARGOS);
                message = "message_successfully_updated";
            } else {
                tbCONHECIMENTOSESPCARGOS = tbCONHECIMENTOSESPCARGOSService.save(tbCONHECIMENTOSESPCARGOS);
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
        
        tbCONHECIMENTOSESPCARGOSList = null;

        FacesMessage facesMessage = MessageFactory.getMessage(message);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        
        return null;
    }
    
    public String delete() {
        
        String message;
        
        try {
            tbCONHECIMENTOSESPCARGOSService.delete(tbCONHECIMENTOSESPCARGOS);
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
    
    public void onDialogOpen(TbCONHECIMENTOSESPCARGOSEntity tbCONHECIMENTOSESPCARGOS) {
        reset();
        this.tbCONHECIMENTOSESPCARGOS = tbCONHECIMENTOSESPCARGOS;
    }
    
    public void reset() {
        tbCONHECIMENTOSESPCARGOS = null;
        tbCONHECIMENTOSESPCARGOSList = null;
        
        allIdCARGOSsList = null;
        
        allIdCONHECESPsList = null;
        
    }

    // Get a List of all idCARGOS
    public List<TbCARGOSEntity> getIdCARGOSs() {
        if (this.allIdCARGOSsList == null) {
            this.allIdCARGOSsList = tbCARGOSService.findAllTbCARGOSEntities();
        }
        return this.allIdCARGOSsList;
    }
    
    // Update idCARGOS of the current tbCONHECIMENTOSESPCARGOS
    public void updateIdCARGOS(TbCARGOSEntity tbCARGOS) {
        this.tbCONHECIMENTOSESPCARGOS.setIdCARGOS(tbCARGOS);
        // Maybe we just created and assigned a new tbCARGOS. So reset the allIdCARGOSList.
        allIdCARGOSsList = null;
    }
    
    // Get a List of all idCONHECESP
    public List<TbCONHECIMENTOSESPECIFICOSEntity> getIdCONHECESPs() {
        if (this.allIdCONHECESPsList == null) {
            this.allIdCONHECESPsList = tbCONHECIMENTOSESPECIFICOSService.findAllTbCONHECIMENTOSESPECIFICOSEntities();
        }
        return this.allIdCONHECESPsList;
    }
    
    // Update idCONHECESP of the current tbCONHECIMENTOSESPCARGOS
    public void updateIdCONHECESP(TbCONHECIMENTOSESPECIFICOSEntity tbCONHECIMENTOSESPECIFICOS) {
        this.tbCONHECIMENTOSESPCARGOS.setIdCONHECESP(tbCONHECIMENTOSESPECIFICOS);
        // Maybe we just created and assigned a new tbCONHECIMENTOSESPECIFICOS. So reset the allIdCONHECESPList.
        allIdCONHECESPsList = null;
    }
    
    public TbCONHECIMENTOSESPCARGOSEntity getTbCONHECIMENTOSESPCARGOS() {
        if (this.tbCONHECIMENTOSESPCARGOS == null) {
            prepareNewTbCONHECIMENTOSESPCARGOS();
        }
        return this.tbCONHECIMENTOSESPCARGOS;
    }
    
    public void setTbCONHECIMENTOSESPCARGOS(TbCONHECIMENTOSESPCARGOSEntity tbCONHECIMENTOSESPCARGOS) {
        this.tbCONHECIMENTOSESPCARGOS = tbCONHECIMENTOSESPCARGOS;
    }
    
    public List<TbCONHECIMENTOSESPCARGOSEntity> getTbCONHECIMENTOSESPCARGOSList() {
        if (tbCONHECIMENTOSESPCARGOSList == null) {
            tbCONHECIMENTOSESPCARGOSList = tbCONHECIMENTOSESPCARGOSService.findAllTbCONHECIMENTOSESPCARGOSEntities();
        }
        return tbCONHECIMENTOSESPCARGOSList;
    }

    public void setTbCONHECIMENTOSESPCARGOSList(List<TbCONHECIMENTOSESPCARGOSEntity> tbCONHECIMENTOSESPCARGOSList) {
        this.tbCONHECIMENTOSESPCARGOSList = tbCONHECIMENTOSESPCARGOSList;
    }
    
    public boolean isPermitted(String permission) {
        return SecurityWrapper.isPermitted(permission);
    }

    public boolean isPermitted(TbCONHECIMENTOSESPCARGOSEntity tbCONHECIMENTOSESPCARGOS, String permission) {
        
        return SecurityWrapper.isPermitted(permission);
        
    }
    
}
