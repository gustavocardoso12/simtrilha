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

import org.applicationn.simtrilhas.domain.TbFORMACAOEntity;
import org.applicationn.simtrilhas.service.TbFORMACAOService;
import org.applicationn.simtrilhas.service.security.SecurityWrapper;
import org.applicationn.simtrilhas.web.util.MessageFactory;

@Named("tbFORMACAOBean")
@ViewScoped
public class TbFORMACAOBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(TbFORMACAOBean.class.getName());
    
    private List<TbFORMACAOEntity> tbFORMACAOList;

    private TbFORMACAOEntity tbFORMACAO;
    
    @Inject
    private TbFORMACAOService tbFORMACAOService;
    
    public void prepareNewTbFORMACAO() {
        reset();
        this.tbFORMACAO = new TbFORMACAOEntity();
        // set any default values now, if you need
        // Example: this.tbFORMACAO.setAnything("test");
    }

    public String persist() {

        if (tbFORMACAO.getId() == null && !isPermitted("tbFORMACAO:create")) {
            return "accessDenied";
        } else if (tbFORMACAO.getId() != null && !isPermitted(tbFORMACAO, "tbFORMACAO:update")) {
            return "accessDenied";
        }
        
        String message;
        
        try {
            
            if (tbFORMACAO.getId() != null) {
                tbFORMACAO = tbFORMACAOService.update(tbFORMACAO);
                message = "message_successfully_updated";
            } else {
                tbFORMACAO = tbFORMACAOService.save(tbFORMACAO);
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
        
        tbFORMACAOList = null;

        FacesMessage facesMessage = MessageFactory.getMessage(message);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        
        return null;
    }
    
    public String delete() {
        
        if (!isPermitted(tbFORMACAO, "tbFORMACAO:delete")) {
            return "accessDenied";
        }
        
        String message;
        
        try {
            tbFORMACAOService.delete(tbFORMACAO);
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
    
    public void onDialogOpen(TbFORMACAOEntity tbFORMACAO) {
        reset();
        this.tbFORMACAO = tbFORMACAO;
    }
    
    public void reset() {
        tbFORMACAO = null;
        tbFORMACAOList = null;
        
    }

    public TbFORMACAOEntity getTbFORMACAO() {
        if (this.tbFORMACAO == null) {
            prepareNewTbFORMACAO();
        }
        return this.tbFORMACAO;
    }
    
    public void setTbFORMACAO(TbFORMACAOEntity tbFORMACAO) {
        this.tbFORMACAO = tbFORMACAO;
    }
    
    public List<TbFORMACAOEntity> getTbFORMACAOList() {
        if (tbFORMACAOList == null) {
            tbFORMACAOList = tbFORMACAOService.findAllTbFORMACAOEntities();
        }
        return tbFORMACAOList;
    }

    public void setTbFORMACAOList(List<TbFORMACAOEntity> tbFORMACAOList) {
        this.tbFORMACAOList = tbFORMACAOList;
    }
    
    public boolean isPermitted(String permission) {
        return SecurityWrapper.isPermitted(permission);
    }

    public boolean isPermitted(TbFORMACAOEntity tbFORMACAO, String permission) {
        
        return SecurityWrapper.isPermitted(permission);
        
    }
    
}
