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
import org.applicationn.simtrilhas.domain.TbESTILOPENSAMENTOCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbESTILOPENSAMENTOEntity;
import org.applicationn.simtrilhas.service.TbCARGOSService;
import org.applicationn.simtrilhas.service.TbESTILOPENSAMENTOCARGOSService;
import org.applicationn.simtrilhas.service.TbESTILOPENSAMENTOService;
import org.applicationn.simtrilhas.service.security.SecurityWrapper;
import org.applicationn.simtrilhas.web.util.MessageFactory;

@Named("tbESTILOPENSAMENTOCARGOSBean")
@ViewScoped
public class TbESTILOPENSAMENTOCARGOSBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(TbESTILOPENSAMENTOCARGOSBean.class.getName());
    
    private List<TbESTILOPENSAMENTOCARGOSEntity> tbESTILOPENSAMENTOCARGOSList;

    private TbESTILOPENSAMENTOCARGOSEntity tbESTILOPENSAMENTOCARGOS;
    
    @Inject
    private TbESTILOPENSAMENTOCARGOSService tbESTILOPENSAMENTOCARGOSService;
    
    @Inject
    private TbCARGOSService tbCARGOSService;
    
    @Inject
    private TbESTILOPENSAMENTOService tbESTILOPENSAMENTOService;
    
    private List<TbCARGOSEntity> allIdCARGOSsList;
    
    private List<TbESTILOPENSAMENTOEntity> allIdESTPENSAMENTOsList;
    
    public void prepareNewTbESTILOPENSAMENTOCARGOS() {
        reset();
        this.tbESTILOPENSAMENTOCARGOS = new TbESTILOPENSAMENTOCARGOSEntity();
        // set any default values now, if you need
        // Example: this.tbESTILOPENSAMENTOCARGOS.setAnything("test");
    }

    public String persist() {

        String message;
        
        try {
            
            if (tbESTILOPENSAMENTOCARGOS.getId() != null) {
                tbESTILOPENSAMENTOCARGOS = tbESTILOPENSAMENTOCARGOSService.update(tbESTILOPENSAMENTOCARGOS);
                message = "message_successfully_updated";
            } else {
                tbESTILOPENSAMENTOCARGOS = tbESTILOPENSAMENTOCARGOSService.save(tbESTILOPENSAMENTOCARGOS);
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
        
        tbESTILOPENSAMENTOCARGOSList = null;

        FacesMessage facesMessage = MessageFactory.getMessage(message);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        
        return null;
    }
    
    public String delete() {
        
        String message;
        
        try {
            tbESTILOPENSAMENTOCARGOSService.delete(tbESTILOPENSAMENTOCARGOS);
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
    
    public void onDialogOpen(TbESTILOPENSAMENTOCARGOSEntity tbESTILOPENSAMENTOCARGOS) {
        reset();
        this.tbESTILOPENSAMENTOCARGOS = tbESTILOPENSAMENTOCARGOS;
    }
    
    public void reset() {
        tbESTILOPENSAMENTOCARGOS = null;
        tbESTILOPENSAMENTOCARGOSList = null;
        
        allIdCARGOSsList = null;
        
        allIdESTPENSAMENTOsList = null;
        
    }

    // Get a List of all idCARGOS
    public List<TbCARGOSEntity> getIdCARGOSs() {
        if (this.allIdCARGOSsList == null) {
            this.allIdCARGOSsList = tbCARGOSService.findAllTbCARGOSEntities();
        }
        return this.allIdCARGOSsList;
    }
    
    // Update idCARGOS of the current tbESTILOPENSAMENTOCARGOS
    public void updateIdCARGOS(TbCARGOSEntity tbCARGOS) {
        this.tbESTILOPENSAMENTOCARGOS.setIdCARGOS(tbCARGOS);
        // Maybe we just created and assigned a new tbCARGOS. So reset the allIdCARGOSList.
        allIdCARGOSsList = null;
    }
    
    // Get a List of all idESTPENSAMENTO
    public List<TbESTILOPENSAMENTOEntity> getIdESTPENSAMENTOs() {
        if (this.allIdESTPENSAMENTOsList == null) {
            this.allIdESTPENSAMENTOsList = tbESTILOPENSAMENTOService.findAllTbESTILOPENSAMENTOEntities();
        }
        return this.allIdESTPENSAMENTOsList;
    }
    
    // Update idESTPENSAMENTO of the current tbESTILOPENSAMENTOCARGOS
    public void updateIdESTPENSAMENTO(TbESTILOPENSAMENTOEntity tbESTILOPENSAMENTO) {
        this.tbESTILOPENSAMENTOCARGOS.setIdESTPENSAMENTO(tbESTILOPENSAMENTO);
        // Maybe we just created and assigned a new tbESTILOPENSAMENTO. So reset the allIdESTPENSAMENTOList.
        allIdESTPENSAMENTOsList = null;
    }
    
    public TbESTILOPENSAMENTOCARGOSEntity getTbESTILOPENSAMENTOCARGOS() {
        if (this.tbESTILOPENSAMENTOCARGOS == null) {
            prepareNewTbESTILOPENSAMENTOCARGOS();
        }
        return this.tbESTILOPENSAMENTOCARGOS;
    }
    
    public void setTbESTILOPENSAMENTOCARGOS(TbESTILOPENSAMENTOCARGOSEntity tbESTILOPENSAMENTOCARGOS) {
        this.tbESTILOPENSAMENTOCARGOS = tbESTILOPENSAMENTOCARGOS;
    }
    
    public List<TbESTILOPENSAMENTOCARGOSEntity> getTbESTILOPENSAMENTOCARGOSList() {
        if (tbESTILOPENSAMENTOCARGOSList == null) {
            tbESTILOPENSAMENTOCARGOSList = tbESTILOPENSAMENTOCARGOSService.findAllTbESTILOPENSAMENTOCARGOSEntities();
        }
        return tbESTILOPENSAMENTOCARGOSList;
    }

    public void setTbESTILOPENSAMENTOCARGOSList(List<TbESTILOPENSAMENTOCARGOSEntity> tbESTILOPENSAMENTOCARGOSList) {
        this.tbESTILOPENSAMENTOCARGOSList = tbESTILOPENSAMENTOCARGOSList;
    }
    
    public boolean isPermitted(String permission) {
        return SecurityWrapper.isPermitted(permission);
    }

    public boolean isPermitted(TbESTILOPENSAMENTOCARGOSEntity tbESTILOPENSAMENTOCARGOS, String permission) {
        
        return SecurityWrapper.isPermitted(permission);
        
    }
    
}
