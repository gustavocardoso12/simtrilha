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

import org.applicationn.simtrilhas.domain.TbAREAEntity;
import org.applicationn.simtrilhas.service.TbAREAService;
import org.applicationn.simtrilhas.service.security.SecurityWrapper;
import org.applicationn.simtrilhas.web.util.MessageFactory;

@Named("tbAREABean")
@ViewScoped
public class TbAREABean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(TbAREABean.class.getName());
    
    private List<TbAREAEntity> tbAREAList;

    private TbAREAEntity tbAREA;
    
    @Inject
    private TbAREAService tbAREAService;
    
    private String dialogHeader;

    public void setDialogHeader(final String dialogHeader) { 
        this.dialogHeader = dialogHeader;
    }

    public String getDialogHeader() {
        return dialogHeader;
    }

    public void changeHeaderCadastrar() {
        setDialogHeader("Cadastrar Área");
    }
    
    public void changeHeaderEditar() {
        setDialogHeader("Editar Área");
    }
    
    
    public void prepareNewTbAREA() {
        reset();
        this.tbAREA = new TbAREAEntity();
        // set any default values now, if you need
        // Example: this.tbAREA.setAnything("test");
    }

    public String persist() {

        if (tbAREA.getId() == null && !isPermitted("tbAREA:create")) {
            return "accessDenied";
        } else if (tbAREA.getId() != null && !isPermitted(tbAREA, "tbAREA:update")) {
            return "accessDenied";
        }
        
        String message;
        
        try {
            
            if (tbAREA.getId() != null) {
                tbAREA = tbAREAService.update(tbAREA);
                message = "message_successfully_updated";
            } else {
                tbAREA = tbAREAService.save(tbAREA);
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
        
        tbAREAList = null;

        FacesMessage facesMessage = MessageFactory.getMessage(message);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        
        return null;
    }
    
    public String delete() {
        
        if (!isPermitted(tbAREA, "tbAREA:delete")) {
            return "accessDenied";
        }
        
        String message;
        
        try {
            tbAREAService.delete(tbAREA);
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
    
    public void onDialogOpen(TbAREAEntity tbAREA) {
        reset();
        this.tbAREA = tbAREA;
    }
    
    public void reset() {
        tbAREA = null;
        tbAREAList = null;
        
    }

    public TbAREAEntity getTbAREA() {
        if (this.tbAREA == null) {
            prepareNewTbAREA();
        }
        return this.tbAREA;
    }
    
    public void setTbAREA(TbAREAEntity tbAREA) {
        this.tbAREA = tbAREA;
    }
    
    public List<TbAREAEntity> getTbAREAList() {
        if (tbAREAList == null) {
            tbAREAList = tbAREAService.findAllTbAREAEntities();
        }
        return tbAREAList;
    }

    public void setTbAREAList(List<TbAREAEntity> tbAREAList) {
        this.tbAREAList = tbAREAList;
    }
    
    public boolean isPermitted(String permission) {
        return SecurityWrapper.isPermitted(permission);
    }

    public boolean isPermitted(TbAREAEntity tbAREA, String permission) {
        
        return SecurityWrapper.isPermitted(permission);
        
    }
    
}
