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
import org.applicationn.simtrilhas.domain.TbESTILOLIDERANCACARGOSEntity;
import org.applicationn.simtrilhas.domain.TbESTILOLIDERANCAEntity;
import org.applicationn.simtrilhas.service.TbCARGOSService;
import org.applicationn.simtrilhas.service.TbESTILOLIDERANCACARGOSService;
import org.applicationn.simtrilhas.service.TbESTILOLIDERANCAService;
import org.applicationn.simtrilhas.service.security.SecurityWrapper;
import org.applicationn.simtrilhas.web.util.MessageFactory;

@Named("tbESTILOLIDERANCACARGOSBean")
@ViewScoped
public class TbESTILOLIDERANCACARGOSBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(TbESTILOLIDERANCACARGOSBean.class.getName());
    
    private List<TbESTILOLIDERANCACARGOSEntity> tbESTILOLIDERANCACARGOSList;

    private TbESTILOLIDERANCACARGOSEntity tbESTILOLIDERANCACARGOS;
    
    @Inject
    private TbESTILOLIDERANCACARGOSService tbESTILOLIDERANCACARGOSService;
    
    @Inject
    private TbCARGOSService tbCARGOSService;
    
    @Inject
    private TbESTILOLIDERANCAService tbESTILOLIDERANCAService;
    
    private List<TbCARGOSEntity> allIdCARGOSsList;
    
    private List<TbESTILOLIDERANCAEntity> allIdESTLIDERsList;
    
    public void prepareNewTbESTILOLIDERANCACARGOS() {
        reset();
        this.tbESTILOLIDERANCACARGOS = new TbESTILOLIDERANCACARGOSEntity();
        // set any default values now, if you need
        // Example: this.tbESTILOLIDERANCACARGOS.setAnything("test");
    }

    public String persist() {

        String message;
        
        try {
            
            if (tbESTILOLIDERANCACARGOS.getId() != null) {
                tbESTILOLIDERANCACARGOS = tbESTILOLIDERANCACARGOSService.update(tbESTILOLIDERANCACARGOS);
                message = "message_successfully_updated";
            } else {
                tbESTILOLIDERANCACARGOS = tbESTILOLIDERANCACARGOSService.save(tbESTILOLIDERANCACARGOS);
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
        
        tbESTILOLIDERANCACARGOSList = null;

        FacesMessage facesMessage = MessageFactory.getMessage(message);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        
        return null;
    }
    
    public String delete() {
        
        String message;
        
        try {
            tbESTILOLIDERANCACARGOSService.delete(tbESTILOLIDERANCACARGOS);
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
    
    public void onDialogOpen(TbESTILOLIDERANCACARGOSEntity tbESTILOLIDERANCACARGOS) {
        reset();
        this.tbESTILOLIDERANCACARGOS = tbESTILOLIDERANCACARGOS;
    }
    
    public void reset() {
        tbESTILOLIDERANCACARGOS = null;
        tbESTILOLIDERANCACARGOSList = null;
        
        allIdCARGOSsList = null;
        
        allIdESTLIDERsList = null;
        
    }

    // Get a List of all idCARGOS
    public List<TbCARGOSEntity> getIdCARGOSs() {
        if (this.allIdCARGOSsList == null) {
            this.allIdCARGOSsList = tbCARGOSService.findAllTbCARGOSEntities();
        }
        return this.allIdCARGOSsList;
    }
    
    // Update idCARGOS of the current tbESTILOLIDERANCACARGOS
    public void updateIdCARGOS(TbCARGOSEntity tbCARGOS) {
        this.tbESTILOLIDERANCACARGOS.setIdCARGOS(tbCARGOS);
        // Maybe we just created and assigned a new tbCARGOS. So reset the allIdCARGOSList.
        allIdCARGOSsList = null;
    }
    
    // Get a List of all idESTLIDER
    public List<TbESTILOLIDERANCAEntity> getIdESTLIDERs() {
        if (this.allIdESTLIDERsList == null) {
            this.allIdESTLIDERsList = tbESTILOLIDERANCAService.findAllTbESTILOLIDERANCAEntities();
        }
        return this.allIdESTLIDERsList;
    }
    
    // Update idESTLIDER of the current tbESTILOLIDERANCACARGOS
    public void updateIdESTLIDER(TbESTILOLIDERANCAEntity tbESTILOLIDERANCA) {
        this.tbESTILOLIDERANCACARGOS.setIdESTLIDER(tbESTILOLIDERANCA);
        // Maybe we just created and assigned a new tbESTILOLIDERANCA. So reset the allIdESTLIDERList.
        allIdESTLIDERsList = null;
    }
    
    public TbESTILOLIDERANCACARGOSEntity getTbESTILOLIDERANCACARGOS() {
        if (this.tbESTILOLIDERANCACARGOS == null) {
            prepareNewTbESTILOLIDERANCACARGOS();
        }
        return this.tbESTILOLIDERANCACARGOS;
    }
    
    public void setTbESTILOLIDERANCACARGOS(TbESTILOLIDERANCACARGOSEntity tbESTILOLIDERANCACARGOS) {
        this.tbESTILOLIDERANCACARGOS = tbESTILOLIDERANCACARGOS;
    }
    
    public List<TbESTILOLIDERANCACARGOSEntity> getTbESTILOLIDERANCACARGOSList() {
        if (tbESTILOLIDERANCACARGOSList == null) {
            tbESTILOLIDERANCACARGOSList = tbESTILOLIDERANCACARGOSService.findAllTbESTILOLIDERANCACARGOSEntities();
        }
        return tbESTILOLIDERANCACARGOSList;
    }

    public void setTbESTILOLIDERANCACARGOSList(List<TbESTILOLIDERANCACARGOSEntity> tbESTILOLIDERANCACARGOSList) {
        this.tbESTILOLIDERANCACARGOSList = tbESTILOLIDERANCACARGOSList;
    }
    
    public boolean isPermitted(String permission) {
        return SecurityWrapper.isPermitted(permission);
    }

    public boolean isPermitted(TbESTILOLIDERANCACARGOSEntity tbESTILOLIDERANCACARGOS, String permission) {
        
        return SecurityWrapper.isPermitted(permission);
        
    }
    
}
