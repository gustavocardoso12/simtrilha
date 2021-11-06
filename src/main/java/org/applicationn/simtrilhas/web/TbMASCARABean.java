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

import org.applicationn.simtrilhas.domain.TbMASCARAEntity;
import org.applicationn.simtrilhas.service.TbMASCARAService;
import org.applicationn.simtrilhas.service.security.SecurityWrapper;
import org.applicationn.simtrilhas.web.util.MessageFactory;

@Named("tbMASCARABean")
@ViewScoped
public class TbMASCARABean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(TbMASCARABean.class.getName());
    
    private List<TbMASCARAEntity> TbMASCARAList;

    private TbMASCARAEntity TbMASCARA;
    
    @Inject
    private TbMASCARAService TbMASCARAService;
    
    private String dialogHeader;

    public void setDialogHeader(final String dialogHeader) { 
        this.dialogHeader = dialogHeader;
    }

    public String getDialogHeader() {
        return dialogHeader;
    }

    public void changeHeaderCadastrar() {
        setDialogHeader("Cadastrar Rótulo");
    }
    
    public void changeHeaderEditar() {
        setDialogHeader("Editar Rótulo");
    }
    
    
    public void prepareNewTbMASCARA() {
        reset();
        this.TbMASCARA = new TbMASCARAEntity();
        // set any default values now, if you need
        // Example: this.TbMASCARA.setAnything("test");
    }

    public String persist() {

        if (TbMASCARA.getId() == null && !isPermitted("TbNO:create")) {
            return "accessDenied";
        } else if (TbMASCARA.getId() != null && !isPermitted(TbMASCARA, "TbNO:update")) {
            return "accessDenied";
        }
        
        String message;
        
        try {
            
            if (TbMASCARA.getId() != null) {
                TbMASCARA = TbMASCARAService.update(TbMASCARA);
                message = "message_successfully_updated";
            } else {
                TbMASCARA = TbMASCARAService.save(TbMASCARA);
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
        
        TbMASCARAList = null;

        FacesMessage facesMessage = MessageFactory.getMessage(message);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        
        return null;
    }
    
    public String delete() {
        
        if (!isPermitted(TbMASCARA, "TbNO:delete")) {
            return "accessDenied";
        }
        
        String message;
        
        try {
            TbMASCARAService.delete(TbMASCARA);
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
    
    public void onDialogOpen(TbMASCARAEntity TbMASCARA) {
        reset();
        this.TbMASCARA = TbMASCARA;
    }
    
    public void reset() {
        TbMASCARA = null;
        TbMASCARAList = null;
        
    }

    public TbMASCARAEntity getTbMASCARA() {
        if (this.TbMASCARA == null) {
            prepareNewTbMASCARA();
        }
        return this.TbMASCARA;
    }
    
    public void setTbMASCARA(TbMASCARAEntity TbMASCARA) {
        this.TbMASCARA = TbMASCARA;
    }
    
    public List<TbMASCARAEntity> getTbMASCARAList() {
        if (TbMASCARAList == null) {
            TbMASCARAList = TbMASCARAService.findAllTbMASCARAEntities();
        }
        return TbMASCARAList;
    }

    public void setTbMASCARAList(List<TbMASCARAEntity> TbMASCARAList) {
        this.TbMASCARAList = TbMASCARAList;
    }
    
    public boolean isPermitted(String permission) {
        return SecurityWrapper.isPermitted(permission);
    }

    public boolean isPermitted(TbMASCARAEntity TbMASCARA, String permission) {
        
        return SecurityWrapper.isPermitted(permission);
        
    }
    
}
