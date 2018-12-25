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

@Named("tbCOMPETENCIASEMCARGOSBean")
@ViewScoped
public class TbCOMPETENCIASEMCARGOSBean implements Serializable {

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
    
    public void prepareNewTbCOMPETENCIASEMCARGOS() {
        reset();
        this.tbCOMPETENCIASEMCARGOS = new TbCOMPETENCIASEMCARGOSEntity();
        // set any default values now, if you need
        // Example: this.tbCOMPETENCIASEMCARGOS.setAnything("test");
    }

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
    
    public void onDialogOpen(TbCOMPETENCIASEMCARGOSEntity tbCOMPETENCIASEMCARGOS) {
        reset();
        this.tbCOMPETENCIASEMCARGOS = tbCOMPETENCIASEMCARGOS;
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
        if (this.tbCOMPETENCIASEMCARGOS == null) {
            prepareNewTbCOMPETENCIASEMCARGOS();
        }
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
