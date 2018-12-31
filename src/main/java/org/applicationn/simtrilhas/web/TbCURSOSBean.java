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

import org.applicationn.simtrilhas.domain.TbCURSOSEntity;
import org.applicationn.simtrilhas.domain.TbFORMACAOEntity;
import org.applicationn.simtrilhas.service.TbCURSOSService;
import org.applicationn.simtrilhas.service.TbFORMACAOService;
import org.applicationn.simtrilhas.service.security.SecurityWrapper;
import org.applicationn.simtrilhas.web.util.MessageFactory;

@Named("tbCURSOSBean")
@ViewScoped
public class TbCURSOSBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(TbCURSOSBean.class.getName());
    
    private List<TbCURSOSEntity> tbCURSOSList;

    private TbCURSOSEntity tbCURSOS;
    
    @Inject
    private TbCURSOSService tbCURSOSService;
    
    @Inject
    private TbFORMACAOService tbFORMACAOService;
    
    private List<TbFORMACAOEntity> allIdFORMACAOsList;
    
    
    private String dialogHeader;

    public void setDialogHeader(final String dialogHeader) { 
        this.dialogHeader = dialogHeader;
    }

    public String getDialogHeader() {
        return dialogHeader;
    }

    public void changeHeaderCadastrar() {
        setDialogHeader("Cadastrar curso");
    }
    
    public void changeHeaderEditar() {
        setDialogHeader("Editar curso");
    }
    
    public void prepareNewTbCURSOS() {
        reset();
        changeHeaderCadastrar();
        this.tbCURSOS = new TbCURSOSEntity();
        // set any default values now, if you need
        // Example: this.tbCURSOS.setAnything("test");
    }

    public String persist() {

        if (tbCURSOS.getId() == null && !isPermitted("tbCURSOS:create")) {
            return "accessDenied";
        } else if (tbCURSOS.getId() != null && !isPermitted(tbCURSOS, "tbCURSOS:update")) {
            return "accessDenied";
        }
        
        String message;
        
        try {
            
            if (tbCURSOS.getId() != null) {
                tbCURSOS = tbCURSOSService.update(tbCURSOS);
                message = "message_successfully_updated";
            } else {
                tbCURSOS = tbCURSOSService.save(tbCURSOS);
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
        
        tbCURSOSList = null;

        FacesMessage facesMessage = MessageFactory.getMessage(message);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        
        return null;
    }
    
    public String delete() {
        
        if (!isPermitted(tbCURSOS, "tbCURSOS:delete")) {
            return "accessDenied";
        }
        
        String message;
        
        try {
            tbCURSOSService.delete(tbCURSOS);
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
    
    public void onDialogOpen(TbCURSOSEntity tbCURSOS) {
        reset();
        changeHeaderEditar();
        this.tbCURSOS = tbCURSOS;
    }
    
    public void reset() {
        tbCURSOS = null;
        tbCURSOSList = null;
        
        allIdFORMACAOsList = null;
        
    }

    // Get a List of all idFORMACAO
    public List<TbFORMACAOEntity> getIdFORMACAOs() {
        if (this.allIdFORMACAOsList == null) {
            this.allIdFORMACAOsList = tbFORMACAOService.findAllTbFORMACAOEntities();
        }
        return this.allIdFORMACAOsList;
    }
    
    // Update idFORMACAO of the current tbCURSOS
    public void updateIdFORMACAO(TbFORMACAOEntity tbFORMACAO) {
        this.tbCURSOS.setIdFORMACAO(tbFORMACAO);
        // Maybe we just created and assigned a new tbFORMACAO. So reset the allIdFORMACAOList.
        allIdFORMACAOsList = null;
    }
    
    public TbCURSOSEntity getTbCURSOS() {
        if (this.tbCURSOS == null) {
            prepareNewTbCURSOS();
        }
        return this.tbCURSOS;
    }
    
    public void setTbCURSOS(TbCURSOSEntity tbCURSOS) {
        this.tbCURSOS = tbCURSOS;
    }
    
    public List<TbCURSOSEntity> getTbCURSOSList() {
        if (tbCURSOSList == null) {
            tbCURSOSList = tbCURSOSService.findAllTbCURSOSEntities();
        }
        return tbCURSOSList;
    }

    public void setTbCURSOSList(List<TbCURSOSEntity> tbCURSOSList) {
        this.tbCURSOSList = tbCURSOSList;
    }
    
    public boolean isPermitted(String permission) {
        return SecurityWrapper.isPermitted(permission);
    }

    public boolean isPermitted(TbCURSOSEntity tbCURSOS, String permission) {
        
        return SecurityWrapper.isPermitted(permission);
        
    }
    
}
