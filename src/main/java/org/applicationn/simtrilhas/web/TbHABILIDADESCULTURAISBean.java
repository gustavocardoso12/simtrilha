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

import org.applicationn.simtrilhas.domain.TbHABILIDADESCULTCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbHABILIDADESCULTURAISEntity;
import org.applicationn.simtrilhas.service.TbHABILIDADESCULTCARGOSService;
import org.applicationn.simtrilhas.service.TbHABILIDADESCULTURAISService;
import org.applicationn.simtrilhas.service.security.SecurityWrapper;
import org.applicationn.simtrilhas.web.util.MessageFactory;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;

@Named("tbHABILIDADESCULTURAISBean")
@ViewScoped
public class TbHABILIDADESCULTURAISBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(TbHABILIDADESCULTURAISBean.class.getName());
    
    private List<TbHABILIDADESCULTURAISEntity> tbHABILIDADESCULTURAISList;

    private TbHABILIDADESCULTURAISEntity tbHABILIDADESCULTURAIS;
    
    @Inject
    private TbHABILIDADESCULTURAISService tbHABILIDADESCULTURAISService;
    
    @Inject
    private TbHABILIDADESCULTCARGOSService tbHABILIDADESCULTCARGOSService;
    
    private DualListModel<TbHABILIDADESCULTCARGOSEntity> tbHABILIDADESCULTCARGOSs;
    private List<String> transferedTbHABILIDADESCULTCARGOSIDs;
    private List<String> removedTbHABILIDADESCULTCARGOSIDs;
    
    public void prepareNewTbHABILIDADESCULTURAIS() {
        reset();
        this.tbHABILIDADESCULTURAIS = new TbHABILIDADESCULTURAISEntity();
        // set any default values now, if you need
        // Example: this.tbHABILIDADESCULTURAIS.setAnything("test");
    }

    public String persist() {

        if (tbHABILIDADESCULTURAIS.getId() == null && !isPermitted("tbHABILIDADESCULTURAIS:create")) {
            return "accessDenied";
        } else if (tbHABILIDADESCULTURAIS.getId() != null && !isPermitted(tbHABILIDADESCULTURAIS, "tbHABILIDADESCULTURAIS:update")) {
            return "accessDenied";
        }
        
        String message;
        
        try {
            
            if (tbHABILIDADESCULTURAIS.getId() != null) {
                tbHABILIDADESCULTURAIS = tbHABILIDADESCULTURAISService.update(tbHABILIDADESCULTURAIS);
                message = "message_successfully_updated";
            } else {
                tbHABILIDADESCULTURAIS = tbHABILIDADESCULTURAISService.save(tbHABILIDADESCULTURAIS);
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
        
        tbHABILIDADESCULTURAISList = null;

        FacesMessage facesMessage = MessageFactory.getMessage(message);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        
        return null;
    }
    
    public String delete() {
        
        if (!isPermitted(tbHABILIDADESCULTURAIS, "tbHABILIDADESCULTURAIS:delete")) {
            return "accessDenied";
        }
        
        String message;
        
        try {
            tbHABILIDADESCULTURAISService.delete(tbHABILIDADESCULTURAIS);
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
    
    public void onDialogOpen(TbHABILIDADESCULTURAISEntity tbHABILIDADESCULTURAIS) {
        reset();
        this.tbHABILIDADESCULTURAIS = tbHABILIDADESCULTURAIS;
    }
    
    public void reset() {
        tbHABILIDADESCULTURAIS = null;
        tbHABILIDADESCULTURAISList = null;
        
        tbHABILIDADESCULTCARGOSs = null;
        transferedTbHABILIDADESCULTCARGOSIDs = null;
        removedTbHABILIDADESCULTCARGOSIDs = null;
        
    }

    public DualListModel<TbHABILIDADESCULTCARGOSEntity> getTbHABILIDADESCULTCARGOSs() {
        return tbHABILIDADESCULTCARGOSs;
    }

    public void setTbHABILIDADESCULTCARGOSs(DualListModel<TbHABILIDADESCULTCARGOSEntity> tbHABILIDADESCULTCARGOSs) {
        this.tbHABILIDADESCULTCARGOSs = tbHABILIDADESCULTCARGOSs;
    }
    
    public List<TbHABILIDADESCULTCARGOSEntity> getFullTbHABILIDADESCULTCARGOSsList() {
        List<TbHABILIDADESCULTCARGOSEntity> allList = new ArrayList<>();
        allList.addAll(tbHABILIDADESCULTCARGOSs.getSource());
        allList.addAll(tbHABILIDADESCULTCARGOSs.getTarget());
        return allList;
    }
    
    public void onTbHABILIDADESCULTCARGOSsDialog(TbHABILIDADESCULTURAISEntity tbHABILIDADESCULTURAIS) {
        // Prepare the tbHABILIDADESCULTCARGOS PickList
        this.tbHABILIDADESCULTURAIS = tbHABILIDADESCULTURAIS;
        List<TbHABILIDADESCULTCARGOSEntity> selectedTbHABILIDADESCULTCARGOSsFromDB = tbHABILIDADESCULTCARGOSService
                .findTbHABILIDADESCULTCARGOSsByIdHABCULTCAR(this.tbHABILIDADESCULTURAIS);
        List<TbHABILIDADESCULTCARGOSEntity> availableTbHABILIDADESCULTCARGOSsFromDB = tbHABILIDADESCULTCARGOSService
                .findAvailableTbHABILIDADESCULTCARGOSs(this.tbHABILIDADESCULTURAIS);
        this.tbHABILIDADESCULTCARGOSs = new DualListModel<>(availableTbHABILIDADESCULTCARGOSsFromDB, selectedTbHABILIDADESCULTCARGOSsFromDB);
        
        transferedTbHABILIDADESCULTCARGOSIDs = new ArrayList<>();
        removedTbHABILIDADESCULTCARGOSIDs = new ArrayList<>();
    }
    
    public void onTbHABILIDADESCULTCARGOSsPickListTransfer(TransferEvent event) {
        // If a tbHABILIDADESCULTCARGOS is transferred within the PickList, we just transfer it in this
        // bean scope. We do not change anything it the database, yet.
        for (Object item : event.getItems()) {
            String id = ((TbHABILIDADESCULTCARGOSEntity) item).getId().toString();
            if (event.isAdd()) {
                transferedTbHABILIDADESCULTCARGOSIDs.add(id);
                removedTbHABILIDADESCULTCARGOSIDs.remove(id);
            } else if (event.isRemove()) {
                removedTbHABILIDADESCULTCARGOSIDs.add(id);
                transferedTbHABILIDADESCULTCARGOSIDs.remove(id);
            }
        }
        
    }
    
    public void updateTbHABILIDADESCULTCARGOS(TbHABILIDADESCULTCARGOSEntity tbHABILIDADESCULTCARGOS) {
        // If a new tbHABILIDADESCULTCARGOS is created, we persist it to the database,
        // but we do not assign it to this tbHABILIDADESCULTURAIS in the database, yet.
        tbHABILIDADESCULTCARGOSs.getTarget().add(tbHABILIDADESCULTCARGOS);
        transferedTbHABILIDADESCULTCARGOSIDs.add(tbHABILIDADESCULTCARGOS.getId().toString());
    }
    
    public void onTbHABILIDADESCULTCARGOSsSubmit() {
        // Now we save the changed of the PickList to the database.
        try {
            List<TbHABILIDADESCULTCARGOSEntity> selectedTbHABILIDADESCULTCARGOSsFromDB = tbHABILIDADESCULTCARGOSService
                    .findTbHABILIDADESCULTCARGOSsByIdHABCULTCAR(this.tbHABILIDADESCULTURAIS);
            List<TbHABILIDADESCULTCARGOSEntity> availableTbHABILIDADESCULTCARGOSsFromDB = tbHABILIDADESCULTCARGOSService
                    .findAvailableTbHABILIDADESCULTCARGOSs(this.tbHABILIDADESCULTURAIS);
            
            for (TbHABILIDADESCULTCARGOSEntity tbHABILIDADESCULTCARGOS : selectedTbHABILIDADESCULTCARGOSsFromDB) {
                if (removedTbHABILIDADESCULTCARGOSIDs.contains(tbHABILIDADESCULTCARGOS.getId().toString())) {
                    tbHABILIDADESCULTCARGOS.setIdHABCULTCAR(null);
                    tbHABILIDADESCULTCARGOSService.update(tbHABILIDADESCULTCARGOS);
                }
            }
    
            for (TbHABILIDADESCULTCARGOSEntity tbHABILIDADESCULTCARGOS : availableTbHABILIDADESCULTCARGOSsFromDB) {
                if (transferedTbHABILIDADESCULTCARGOSIDs.contains(tbHABILIDADESCULTCARGOS.getId().toString())) {
                    tbHABILIDADESCULTCARGOS.setIdHABCULTCAR(tbHABILIDADESCULTURAIS);
                    tbHABILIDADESCULTCARGOSService.update(tbHABILIDADESCULTCARGOS);
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
    
    public TbHABILIDADESCULTURAISEntity getTbHABILIDADESCULTURAIS() {
        if (this.tbHABILIDADESCULTURAIS == null) {
            prepareNewTbHABILIDADESCULTURAIS();
        }
        return this.tbHABILIDADESCULTURAIS;
    }
    
    public void setTbHABILIDADESCULTURAIS(TbHABILIDADESCULTURAISEntity tbHABILIDADESCULTURAIS) {
        this.tbHABILIDADESCULTURAIS = tbHABILIDADESCULTURAIS;
    }
    
    public List<TbHABILIDADESCULTURAISEntity> getTbHABILIDADESCULTURAISList() {
        if (tbHABILIDADESCULTURAISList == null) {
            tbHABILIDADESCULTURAISList = tbHABILIDADESCULTURAISService.findAllTbHABILIDADESCULTURAISEntities();
        }
        return tbHABILIDADESCULTURAISList;
    }

    public void setTbHABILIDADESCULTURAISList(List<TbHABILIDADESCULTURAISEntity> tbHABILIDADESCULTURAISList) {
        this.tbHABILIDADESCULTURAISList = tbHABILIDADESCULTURAISList;
    }
    
    public boolean isPermitted(String permission) {
        return SecurityWrapper.isPermitted(permission);
    }

    public boolean isPermitted(TbHABILIDADESCULTURAISEntity tbHABILIDADESCULTURAIS, String permission) {
        
        return SecurityWrapper.isPermitted(permission);
        
    }
    
}
