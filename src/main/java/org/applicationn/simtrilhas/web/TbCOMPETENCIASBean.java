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

import org.applicationn.simtrilhas.domain.TbCOMPETENCIASCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbCOMPETENCIASEntity;
import org.applicationn.simtrilhas.service.TbCOMPETENCIASCARGOSService;
import org.applicationn.simtrilhas.service.TbCOMPETENCIASService;
import org.applicationn.simtrilhas.service.security.SecurityWrapper;
import org.applicationn.simtrilhas.web.util.MessageFactory;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;

@Named("tbCOMPETENCIASBean")
@ViewScoped
public class TbCOMPETENCIASBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(TbCOMPETENCIASBean.class.getName());
    
    private List<TbCOMPETENCIASEntity> tbCOMPETENCIASList;

    private TbCOMPETENCIASEntity tbCOMPETENCIAS;
    
    @Inject
    private TbCOMPETENCIASService tbCOMPETENCIASService;
    
    @Inject
    private TbCOMPETENCIASCARGOSService tbCOMPETENCIASCARGOSService;
    
    private DualListModel<TbCOMPETENCIASCARGOSEntity> tbCOMPETENCIASCARGOSs;
    private List<String> transferedTbCOMPETENCIASCARGOSIDs;
    private List<String> removedTbCOMPETENCIASCARGOSIDs;
    
    public void prepareNewTbCOMPETENCIAS() {
        reset();
        this.tbCOMPETENCIAS = new TbCOMPETENCIASEntity();
        // set any default values now, if you need
        // Example: this.tbCOMPETENCIAS.setAnything("test");
    }

    public String persist() {

        if (tbCOMPETENCIAS.getId() == null && !isPermitted("tbCOMPETENCIAS:create")) {
            return "accessDenied";
        } else if (tbCOMPETENCIAS.getId() != null && !isPermitted(tbCOMPETENCIAS, "tbCOMPETENCIAS:update")) {
            return "accessDenied";
        }
        
        String message;
        
        try {
            
            if (tbCOMPETENCIAS.getId() != null) {
                tbCOMPETENCIAS = tbCOMPETENCIASService.update(tbCOMPETENCIAS);
                message = "message_successfully_updated";
            } else {
                tbCOMPETENCIAS = tbCOMPETENCIASService.save(tbCOMPETENCIAS);
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
        
        tbCOMPETENCIASList = null;

        FacesMessage facesMessage = MessageFactory.getMessage(message);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        
        return null;
    }
    
    public String delete() {
        
        if (!isPermitted(tbCOMPETENCIAS, "tbCOMPETENCIAS:delete")) {
            return "accessDenied";
        }
        
        String message;
        
        try {
            tbCOMPETENCIASService.delete(tbCOMPETENCIAS);
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
    
    public void onDialogOpen(TbCOMPETENCIASEntity tbCOMPETENCIAS) {
        reset();
        this.tbCOMPETENCIAS = tbCOMPETENCIAS;
    }
    
    public void reset() {
        tbCOMPETENCIAS = null;
        tbCOMPETENCIASList = null;
        
        tbCOMPETENCIASCARGOSs = null;
        transferedTbCOMPETENCIASCARGOSIDs = null;
        removedTbCOMPETENCIASCARGOSIDs = null;
        
    }

    public DualListModel<TbCOMPETENCIASCARGOSEntity> getTbCOMPETENCIASCARGOSs() {
        return tbCOMPETENCIASCARGOSs;
    }

    public void setTbCOMPETENCIASCARGOSs(DualListModel<TbCOMPETENCIASCARGOSEntity> tbCOMPETENCIASCARGOSs) {
        this.tbCOMPETENCIASCARGOSs = tbCOMPETENCIASCARGOSs;
    }
    
    public List<TbCOMPETENCIASCARGOSEntity> getFullTbCOMPETENCIASCARGOSsList() {
        List<TbCOMPETENCIASCARGOSEntity> allList = new ArrayList<>();
        allList.addAll(tbCOMPETENCIASCARGOSs.getSource());
        allList.addAll(tbCOMPETENCIASCARGOSs.getTarget());
        return allList;
    }
    
    public void onTbCOMPETENCIASCARGOSsDialog(TbCOMPETENCIASEntity tbCOMPETENCIAS) {
        // Prepare the tbCOMPETENCIASCARGOS PickList
        this.tbCOMPETENCIAS = tbCOMPETENCIAS;
        List<TbCOMPETENCIASCARGOSEntity> selectedTbCOMPETENCIASCARGOSsFromDB = tbCOMPETENCIASCARGOSService
                .findTbCOMPETENCIASCARGOSsByIdCOMPETENCIAS(this.tbCOMPETENCIAS);
        List<TbCOMPETENCIASCARGOSEntity> availableTbCOMPETENCIASCARGOSsFromDB = tbCOMPETENCIASCARGOSService
                .findAvailableTbCOMPETENCIASCARGOSs(this.tbCOMPETENCIAS);
        this.tbCOMPETENCIASCARGOSs = new DualListModel<>(availableTbCOMPETENCIASCARGOSsFromDB, selectedTbCOMPETENCIASCARGOSsFromDB);
        
        transferedTbCOMPETENCIASCARGOSIDs = new ArrayList<>();
        removedTbCOMPETENCIASCARGOSIDs = new ArrayList<>();
    }
    
    public void onTbCOMPETENCIASCARGOSsPickListTransfer(TransferEvent event) {
        // If a tbCOMPETENCIASCARGOS is transferred within the PickList, we just transfer it in this
        // bean scope. We do not change anything it the database, yet.
        for (Object item : event.getItems()) {
            String id = ((TbCOMPETENCIASCARGOSEntity) item).getId().toString();
            if (event.isAdd()) {
                transferedTbCOMPETENCIASCARGOSIDs.add(id);
                removedTbCOMPETENCIASCARGOSIDs.remove(id);
            } else if (event.isRemove()) {
                removedTbCOMPETENCIASCARGOSIDs.add(id);
                transferedTbCOMPETENCIASCARGOSIDs.remove(id);
            }
        }
        
    }
    
    public void updateTbCOMPETENCIASCARGOS(TbCOMPETENCIASCARGOSEntity tbCOMPETENCIASCARGOS) {
        // If a new tbCOMPETENCIASCARGOS is created, we persist it to the database,
        // but we do not assign it to this tbCOMPETENCIAS in the database, yet.
        tbCOMPETENCIASCARGOSs.getTarget().add(tbCOMPETENCIASCARGOS);
        transferedTbCOMPETENCIASCARGOSIDs.add(tbCOMPETENCIASCARGOS.getId().toString());
    }
    
    public void onTbCOMPETENCIASCARGOSsSubmit() {
        // Now we save the changed of the PickList to the database.
        try {
            List<TbCOMPETENCIASCARGOSEntity> selectedTbCOMPETENCIASCARGOSsFromDB = tbCOMPETENCIASCARGOSService
                    .findTbCOMPETENCIASCARGOSsByIdCOMPETENCIAS(this.tbCOMPETENCIAS);
            List<TbCOMPETENCIASCARGOSEntity> availableTbCOMPETENCIASCARGOSsFromDB = tbCOMPETENCIASCARGOSService
                    .findAvailableTbCOMPETENCIASCARGOSs(this.tbCOMPETENCIAS);
            
            for (TbCOMPETENCIASCARGOSEntity tbCOMPETENCIASCARGOS : selectedTbCOMPETENCIASCARGOSsFromDB) {
                if (removedTbCOMPETENCIASCARGOSIDs.contains(tbCOMPETENCIASCARGOS.getId().toString())) {
                    tbCOMPETENCIASCARGOS.setIdCOMPETENCIAS(null);
                    tbCOMPETENCIASCARGOSService.update(tbCOMPETENCIASCARGOS);
                }
            }
    
            for (TbCOMPETENCIASCARGOSEntity tbCOMPETENCIASCARGOS : availableTbCOMPETENCIASCARGOSsFromDB) {
                if (transferedTbCOMPETENCIASCARGOSIDs.contains(tbCOMPETENCIASCARGOS.getId().toString())) {
                    tbCOMPETENCIASCARGOS.setIdCOMPETENCIAS(tbCOMPETENCIAS);
                    tbCOMPETENCIASCARGOSService.update(tbCOMPETENCIASCARGOS);
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
    
    public TbCOMPETENCIASEntity getTbCOMPETENCIAS() {
        if (this.tbCOMPETENCIAS == null) {
            prepareNewTbCOMPETENCIAS();
        }
        return this.tbCOMPETENCIAS;
    }
    
    public void setTbCOMPETENCIAS(TbCOMPETENCIASEntity tbCOMPETENCIAS) {
        this.tbCOMPETENCIAS = tbCOMPETENCIAS;
    }
    
    public List<TbCOMPETENCIASEntity> getTbCOMPETENCIASList() {
        if (tbCOMPETENCIASList == null) {
            tbCOMPETENCIASList = tbCOMPETENCIASService.findAllTbCOMPETENCIASEntities();
        }
        return tbCOMPETENCIASList;
    }

    public void setTbCOMPETENCIASList(List<TbCOMPETENCIASEntity> tbCOMPETENCIASList) {
        this.tbCOMPETENCIASList = tbCOMPETENCIASList;
    }
    
    public boolean isPermitted(String permission) {
        return SecurityWrapper.isPermitted(permission);
    }

    public boolean isPermitted(TbCOMPETENCIASEntity tbCOMPETENCIAS, String permission) {
        
        return SecurityWrapper.isPermitted(permission);
        
    }
    
}
