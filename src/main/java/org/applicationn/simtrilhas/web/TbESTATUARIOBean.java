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

import org.applicationn.simtrilhas.domain.TbDIRETORIAEntity;
import org.applicationn.simtrilhas.domain.TbESTATUARIOEntity;
import org.applicationn.simtrilhas.service.TbDIRETORIAService;
import org.applicationn.simtrilhas.service.TbESTATUARIOService;
import org.applicationn.simtrilhas.service.security.SecurityWrapper;
import org.applicationn.simtrilhas.web.util.MessageFactory;

@Named("tbESTATUARIOBean")
@ViewScoped
public class TbESTATUARIOBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(TbESTATUARIOBean.class.getName());
    
    private List<TbESTATUARIOEntity> tbESTATUARIOList;

    private TbESTATUARIOEntity tbESTATUARIO;
    
    @Inject
    private TbESTATUARIOService tbESTATUARIOService;
    
    @Inject
    private TbDIRETORIAService tbDIRETORIAService;
    
    private List<TbDIRETORIAEntity> allIdDIRETORIAsList;
    
    public void prepareNewTbESTATUARIO() {
        reset();
        this.tbESTATUARIO = new TbESTATUARIOEntity();
        // set any default values now, if you need
        // Example: this.tbESTATUARIO.setAnything("test");
    }

    public String persist() {

        if (tbESTATUARIO.getId() == null && !isPermitted("tbESTATUARIO:create")) {
            return "accessDenied";
        } else if (tbESTATUARIO.getId() != null && !isPermitted(tbESTATUARIO, "tbESTATUARIO:update")) {
            return "accessDenied";
        }
        
        String message;
        
        try {
            
            if (tbESTATUARIO.getId() != null) {
                tbESTATUARIO = tbESTATUARIOService.update(tbESTATUARIO);
                message = "message_successfully_updated";
            } else {
                tbESTATUARIO = tbESTATUARIOService.save(tbESTATUARIO);
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
        
        tbESTATUARIOList = null;

        FacesMessage facesMessage = MessageFactory.getMessage(message);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        
        return null;
    }
    
    public String delete() {
        
        if (!isPermitted(tbESTATUARIO, "tbESTATUARIO:delete")) {
            return "accessDenied";
        }
        
        String message;
        
        try {
            tbESTATUARIOService.delete(tbESTATUARIO);
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
    
    public void onDialogOpen(TbESTATUARIOEntity tbESTATUARIO) {
        reset();
        this.tbESTATUARIO = tbESTATUARIO;
    }
    
    public void reset() {
        tbESTATUARIO = null;
        tbESTATUARIOList = null;
        
        allIdDIRETORIAsList = null;
        
    }

    // Get a List of all idDIRETORIA
    public List<TbDIRETORIAEntity> getIdDIRETORIAs() {
        if (this.allIdDIRETORIAsList == null) {
            this.allIdDIRETORIAsList = tbDIRETORIAService.findAllTbDIRETORIAEntities();
        }
        return this.allIdDIRETORIAsList;
    }
    
    // Update idDIRETORIA of the current tbESTATUARIO
    public void updateIdDIRETORIA(TbDIRETORIAEntity tbDIRETORIA) {
        this.tbESTATUARIO.setIdDIRETORIA(tbDIRETORIA);
        // Maybe we just created and assigned a new tbDIRETORIA. So reset the allIdDIRETORIAList.
        allIdDIRETORIAsList = null;
    }
    
    public TbESTATUARIOEntity getTbESTATUARIO() {
        if (this.tbESTATUARIO == null) {
            prepareNewTbESTATUARIO();
        }
        return this.tbESTATUARIO;
    }
    
    public void setTbESTATUARIO(TbESTATUARIOEntity tbESTATUARIO) {
        this.tbESTATUARIO = tbESTATUARIO;
    }
    
    public List<TbESTATUARIOEntity> getTbESTATUARIOList() {
        if (tbESTATUARIOList == null) {
            tbESTATUARIOList = tbESTATUARIOService.findAllTbESTATUARIOEntities();
        }
        return tbESTATUARIOList;
    }

    public void setTbESTATUARIOList(List<TbESTATUARIOEntity> tbESTATUARIOList) {
        this.tbESTATUARIOList = tbESTATUARIOList;
    }
    
    public boolean isPermitted(String permission) {
        return SecurityWrapper.isPermitted(permission);
    }

    public boolean isPermitted(TbESTATUARIOEntity tbESTATUARIO, String permission) {
        
        return SecurityWrapper.isPermitted(permission);
        
    }
    
}
