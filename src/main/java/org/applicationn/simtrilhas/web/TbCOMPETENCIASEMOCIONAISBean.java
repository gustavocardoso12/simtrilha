package org.applicationn.simtrilhas.web;

import java.io.Serializable;
import java.util.ArrayList;
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

import org.applicationn.simtrilhas.domain.TbCOMPETENCIASEMCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbCOMPETENCIASEMOCIONAISEntity;
import org.applicationn.simtrilhas.service.TbCOMPETENCIASEMCARGOSService;
import org.applicationn.simtrilhas.service.TbCOMPETENCIASEMOCIONAISService;
import org.applicationn.simtrilhas.service.security.SecurityWrapper;
import org.applicationn.simtrilhas.web.util.MessageFactory;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;

@Named("tbCOMPETENCIASEMOCIONAISBean")
@ViewScoped
public class TbCOMPETENCIASEMOCIONAISBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(TbCOMPETENCIASEMOCIONAISBean.class.getName());
    
    private List<TbCOMPETENCIASEMOCIONAISEntity> tbCOMPETENCIASEMOCIONAISList;

    private TbCOMPETENCIASEMOCIONAISEntity tbCOMPETENCIASEMOCIONAIS;
    
    @Inject
    private TbCOMPETENCIASEMOCIONAISService tbCOMPETENCIASEMOCIONAISService;
    
    @Inject
    private TbCOMPETENCIASEMCARGOSService tbCOMPETENCIASEMCARGOSService;
    
    private DualListModel<TbCOMPETENCIASEMCARGOSEntity> tbCOMPETENCIASEMCARGOSs;
    private List<String> transferedTbCOMPETENCIASEMCARGOSIDs;
    private List<String> removedTbCOMPETENCIASEMCARGOSIDs;
    
    private String dialogHeader;

    public void setDialogHeader(final String dialogHeader) { 
        this.dialogHeader = dialogHeader;
    }

    public String getDialogHeader() {
        return dialogHeader;
    }

    public void changeHeaderCadastrar() {
        setDialogHeader("Cadastrar Competências");
    }
    
    public void changeHeaderEditar() {
        setDialogHeader("Editar Competências");
    }
    
    
    public void prepareNewTbCOMPETENCIASEMOCIONAIS() {
        reset();
        changeHeaderCadastrar();
        this.tbCOMPETENCIASEMOCIONAIS = new TbCOMPETENCIASEMOCIONAISEntity();
        // set any default values now, if you need
        // Example: this.tbCOMPETENCIASEMOCIONAIS.setAnything("test");
    }

    public String persist() {

        if (tbCOMPETENCIASEMOCIONAIS.getId() == null && !isPermitted("tbCOMPETENCIASEMOCIONAIS:create")) {
            return "accessDenied";
        } else if (tbCOMPETENCIASEMOCIONAIS.getId() != null && !isPermitted(tbCOMPETENCIASEMOCIONAIS, "tbCOMPETENCIASEMOCIONAIS:update")) {
            return "accessDenied";
        }
        
        String message;
        
        try {
            
            if (tbCOMPETENCIASEMOCIONAIS.getId() != null) {
                tbCOMPETENCIASEMOCIONAIS = tbCOMPETENCIASEMOCIONAISService.update(tbCOMPETENCIASEMOCIONAIS);
                message = "message_successfully_updated";
            } else {
                tbCOMPETENCIASEMOCIONAIS = tbCOMPETENCIASEMOCIONAISService.save(tbCOMPETENCIASEMOCIONAIS);
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
        
        tbCOMPETENCIASEMOCIONAISList = null;

        FacesMessage facesMessage = MessageFactory.getMessage(message);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        
        return null;
    }
    
    public String delete() {
        
        if (!isPermitted(tbCOMPETENCIASEMOCIONAIS, "tbCOMPETENCIASEMOCIONAIS:delete")) {
            return "accessDenied";
        }
        
        String message;
        
        try {
            tbCOMPETENCIASEMOCIONAISService.delete(tbCOMPETENCIASEMOCIONAIS);
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
    
    public void onDialogOpen(TbCOMPETENCIASEMOCIONAISEntity tbCOMPETENCIASEMOCIONAIS) {
        reset();
        changeHeaderEditar();
        this.tbCOMPETENCIASEMOCIONAIS = tbCOMPETENCIASEMOCIONAIS;
    }
    
    public void reset() {
        tbCOMPETENCIASEMOCIONAIS = null;
        tbCOMPETENCIASEMOCIONAISList = null;
        
        tbCOMPETENCIASEMCARGOSs = null;
        transferedTbCOMPETENCIASEMCARGOSIDs = null;
        removedTbCOMPETENCIASEMCARGOSIDs = null;
        
    }

    public DualListModel<TbCOMPETENCIASEMCARGOSEntity> getTbCOMPETENCIASEMCARGOSs() {
        return tbCOMPETENCIASEMCARGOSs;
    }

    public void setTbCOMPETENCIASEMCARGOSs(DualListModel<TbCOMPETENCIASEMCARGOSEntity> tbCOMPETENCIASEMCARGOSs) {
        this.tbCOMPETENCIASEMCARGOSs = tbCOMPETENCIASEMCARGOSs;
    }
    
    public List<TbCOMPETENCIASEMCARGOSEntity> getFullTbCOMPETENCIASEMCARGOSsList() {
        List<TbCOMPETENCIASEMCARGOSEntity> allList = new ArrayList<>();
        allList.addAll(tbCOMPETENCIASEMCARGOSs.getSource());
        allList.addAll(tbCOMPETENCIASEMCARGOSs.getTarget());
        return allList;
    }
    
    public void onTbCOMPETENCIASEMCARGOSsDialog(TbCOMPETENCIASEMOCIONAISEntity tbCOMPETENCIASEMOCIONAIS) {
        // Prepare the tbCOMPETENCIASEMCARGOS PickList
        this.tbCOMPETENCIASEMOCIONAIS = tbCOMPETENCIASEMOCIONAIS;
        List<TbCOMPETENCIASEMCARGOSEntity> selectedTbCOMPETENCIASEMCARGOSsFromDB = tbCOMPETENCIASEMCARGOSService
                .findTbCOMPETENCIASEMCARGOSsByIdCOMPEM(this.tbCOMPETENCIASEMOCIONAIS);
        List<TbCOMPETENCIASEMCARGOSEntity> availableTbCOMPETENCIASEMCARGOSsFromDB = tbCOMPETENCIASEMCARGOSService
                .findAvailableTbCOMPETENCIASEMCARGOSs(this.tbCOMPETENCIASEMOCIONAIS);
        this.tbCOMPETENCIASEMCARGOSs = new DualListModel<>(availableTbCOMPETENCIASEMCARGOSsFromDB, selectedTbCOMPETENCIASEMCARGOSsFromDB);
        
        transferedTbCOMPETENCIASEMCARGOSIDs = new ArrayList<>();
        removedTbCOMPETENCIASEMCARGOSIDs = new ArrayList<>();
    }
    
    public void onTbCOMPETENCIASEMCARGOSsPickListTransfer(TransferEvent event) {
        // If a tbCOMPETENCIASEMCARGOS is transferred within the PickList, we just transfer it in this
        // bean scope. We do not change anything it the database, yet.
        for (Object item : event.getItems()) {
            String id = ((TbCOMPETENCIASEMCARGOSEntity) item).getId().toString();
            if (event.isAdd()) {
                transferedTbCOMPETENCIASEMCARGOSIDs.add(id);
                removedTbCOMPETENCIASEMCARGOSIDs.remove(id);
            } else if (event.isRemove()) {
                removedTbCOMPETENCIASEMCARGOSIDs.add(id);
                transferedTbCOMPETENCIASEMCARGOSIDs.remove(id);
            }
        }
        
    }
    
    public void updateTbCOMPETENCIASEMCARGOS(TbCOMPETENCIASEMCARGOSEntity tbCOMPETENCIASEMCARGOS) {
        // If a new tbCOMPETENCIASEMCARGOS is created, we persist it to the database,
        // but we do not assign it to this tbCOMPETENCIASEMOCIONAIS in the database, yet.
        tbCOMPETENCIASEMCARGOSs.getTarget().add(tbCOMPETENCIASEMCARGOS);
        transferedTbCOMPETENCIASEMCARGOSIDs.add(tbCOMPETENCIASEMCARGOS.getId().toString());
    }
    
    public void onTbCOMPETENCIASEMCARGOSsSubmit() {
        // Now we save the changed of the PickList to the database.
        try {
            List<TbCOMPETENCIASEMCARGOSEntity> selectedTbCOMPETENCIASEMCARGOSsFromDB = tbCOMPETENCIASEMCARGOSService
                    .findTbCOMPETENCIASEMCARGOSsByIdCOMPEM(this.tbCOMPETENCIASEMOCIONAIS);
            List<TbCOMPETENCIASEMCARGOSEntity> availableTbCOMPETENCIASEMCARGOSsFromDB = tbCOMPETENCIASEMCARGOSService
                    .findAvailableTbCOMPETENCIASEMCARGOSs(this.tbCOMPETENCIASEMOCIONAIS);
            
            for (TbCOMPETENCIASEMCARGOSEntity tbCOMPETENCIASEMCARGOS : selectedTbCOMPETENCIASEMCARGOSsFromDB) {
                if (removedTbCOMPETENCIASEMCARGOSIDs.contains(tbCOMPETENCIASEMCARGOS.getId().toString())) {
                    tbCOMPETENCIASEMCARGOS.setIdCOMPEM(null);
                    tbCOMPETENCIASEMCARGOSService.update(tbCOMPETENCIASEMCARGOS);
                }
            }
    
            for (TbCOMPETENCIASEMCARGOSEntity tbCOMPETENCIASEMCARGOS : availableTbCOMPETENCIASEMCARGOSsFromDB) {
                if (transferedTbCOMPETENCIASEMCARGOSIDs.contains(tbCOMPETENCIASEMCARGOS.getId().toString())) {
                    tbCOMPETENCIASEMCARGOS.setIdCOMPEM(tbCOMPETENCIASEMOCIONAIS);
                    tbCOMPETENCIASEMCARGOSService.update(tbCOMPETENCIASEMCARGOS);
                }
            }
            
            FacesMessage facesMessage = MessageFactory.getMessage(
                    "message_changes_saved");
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            
            reset();

        } catch (OptimisticLockException e) {
            logger.log(Level.SEVERE, "Error occured", e);
            FacesMessage facesMessage = MessageFactory.getMessage(
                    "message_optimistic_locking_exception");
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            // Set validationFailed to keep the dialog open
            FacesContext.getCurrentInstance().validationFailed();
        } catch (PersistenceException e) {
            logger.log(Level.SEVERE, "Error occured", e);
            FacesMessage facesMessage = MessageFactory.getMessage(
                    "message_picklist_save_exception");
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            // Set validationFailed to keep the dialog open
            FacesContext.getCurrentInstance().validationFailed();
        }
    }
    
    public TbCOMPETENCIASEMOCIONAISEntity getTbCOMPETENCIASEMOCIONAIS() {
        if (this.tbCOMPETENCIASEMOCIONAIS == null) {
            prepareNewTbCOMPETENCIASEMOCIONAIS();
        }
        return this.tbCOMPETENCIASEMOCIONAIS;
    }
    
    public void setTbCOMPETENCIASEMOCIONAIS(TbCOMPETENCIASEMOCIONAISEntity tbCOMPETENCIASEMOCIONAIS) {
        this.tbCOMPETENCIASEMOCIONAIS = tbCOMPETENCIASEMOCIONAIS;
    }
    
    public List<TbCOMPETENCIASEMOCIONAISEntity> getTbCOMPETENCIASEMOCIONAISList() {
        if (tbCOMPETENCIASEMOCIONAISList == null) {
            tbCOMPETENCIASEMOCIONAISList = tbCOMPETENCIASEMOCIONAISService.findAllTbCOMPETENCIASEMOCIONAISEntities();
        }
        return tbCOMPETENCIASEMOCIONAISList;
    }

    public void setTbCOMPETENCIASEMOCIONAISList(List<TbCOMPETENCIASEMOCIONAISEntity> tbCOMPETENCIASEMOCIONAISList) {
        this.tbCOMPETENCIASEMOCIONAISList = tbCOMPETENCIASEMOCIONAISList;
    }
    
    public boolean isPermitted(String permission) {
        return SecurityWrapper.isPermitted(permission);
    }

    public boolean isPermitted(TbCOMPETENCIASEMOCIONAISEntity tbCOMPETENCIASEMOCIONAIS, String permission) {
        
        return SecurityWrapper.isPermitted(permission);
        
    }
    
}
