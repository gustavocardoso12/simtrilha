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

import org.applicationn.simtrilhas.domain.TbNOEntity;
import org.applicationn.simtrilhas.service.TbNOService;
import org.applicationn.simtrilhas.service.security.SecurityWrapper;
import org.applicationn.simtrilhas.web.util.MessageFactory;

@Named("tbNOBean")
@ViewScoped
public class TbNOBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(TbNOBean.class.getName());
    
    private List<TbNOEntity> tbNOList;

    private TbNOEntity tbNO;
    
    @Inject
    private TbNOService tbNOService;
    
    private String dialogHeader;

    public void setDialogHeader(final String dialogHeader) { 
        this.dialogHeader = dialogHeader;
    }

    public String getDialogHeader() {
        return dialogHeader;
    }

    public void changeHeaderCadastrar() {
        setDialogHeader("Cadastrar Nível Organizacional");
    }
    
    public void changeHeaderEditar() {
        setDialogHeader("Editar Nível Organizacional");
    }
    
    
    public void prepareNewTbNO() {
        reset();
        changeHeaderCadastrar();
        this.tbNO = new TbNOEntity();
        // set any default values now, if you need
        // Example: this.tbNO.setAnything("test");
    }

    public String persist() {

        if (tbNO.getId() == null && !isPermitted("tbNO:create")) {
            return "accessDenied";
        } else if (tbNO.getId() != null && !isPermitted(tbNO, "tbNO:update")) {
            return "accessDenied";
        }
        
        String message;
        
        try {
            
            if (tbNO.getId() != null) {
                tbNO = tbNOService.update(tbNO);
                message = "message_successfully_updated";
            } else {
                tbNO = tbNOService.save(tbNO);
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
        
        tbNOList = null;

        FacesMessage facesMessage = MessageFactory.getMessage(message);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        
        return null;
    }
    
    public String delete() {
        
        if (!isPermitted(tbNO, "tbNO:delete")) {
            return "accessDenied";
        }
        
        String message;
        
        try {
            tbNOService.delete(tbNO);
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
    
    public void onDialogOpen(TbNOEntity tbNO) {
        reset();
        changeHeaderEditar();
        this.tbNO = tbNO;
    }
    
    public void reset() {
        tbNO = null;
        tbNOList = null;
        
    }

    public TbNOEntity getTbNO() {
        if (this.tbNO == null) {
            prepareNewTbNO();
        }
        return this.tbNO;
    }
    
    public void setTbNO(TbNOEntity tbNO) {
        this.tbNO = tbNO;
    }
    
    public List<TbNOEntity> getTbNOList() {
        if (tbNOList == null) {
            tbNOList = tbNOService.findAllTbNOEntities();
        }
        return tbNOList;
    }

    public void setTbNOList(List<TbNOEntity> tbNOList) {
        this.tbNOList = tbNOList;
    }
    
    public boolean isPermitted(String permission) {
        return SecurityWrapper.isPermitted(permission);
    }

    public boolean isPermitted(TbNOEntity tbNO, String permission) {
        
        return SecurityWrapper.isPermitted(permission);
        
    }
    
}
