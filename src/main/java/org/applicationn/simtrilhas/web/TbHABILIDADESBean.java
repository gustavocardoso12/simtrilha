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

import org.applicationn.simtrilhas.domain.TbHABILIDADESCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbHABILIDADESEntity;
import org.applicationn.simtrilhas.service.TbHABILIDADESCARGOSService;
import org.applicationn.simtrilhas.service.TbHABILIDADESService;
import org.applicationn.simtrilhas.service.security.SecurityWrapper;
import org.applicationn.simtrilhas.web.util.MessageFactory;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;

@Named("tbHABILIDADESBean")
@ViewScoped
public class TbHABILIDADESBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(TbHABILIDADESBean.class.getName());
    
    private List<TbHABILIDADESEntity> tbHABILIDADESList;

    private TbHABILIDADESEntity tbHABILIDADES;
    
    @Inject
    private TbHABILIDADESService tbHABILIDADESService;
    
    @Inject
    private TbHABILIDADESCARGOSService tbHABILIDADESCARGOSService;
    
    private DualListModel<TbHABILIDADESCARGOSEntity> tbHABILIDADESCARGOSs;
    private List<String> transferedTbHABILIDADESCARGOSIDs;
    private List<String> removedTbHABILIDADESCARGOSIDs;
    
    public void prepareNewTbHABILIDADES() {
        reset();
        this.tbHABILIDADES = new TbHABILIDADESEntity();
        // set any default values now, if you need
        // Example: this.tbHABILIDADES.setAnything("test");
    }

    public String persist() {

        if (tbHABILIDADES.getId() == null && !isPermitted("tbHABILIDADES:create")) {
            return "accessDenied";
        } else if (tbHABILIDADES.getId() != null && !isPermitted(tbHABILIDADES, "tbHABILIDADES:update")) {
            return "accessDenied";
        }
        
        String message;
        
        try {
            
            if (tbHABILIDADES.getId() != null) {
                tbHABILIDADES = tbHABILIDADESService.update(tbHABILIDADES);
                message = "message_successfully_updated";
            } else {
                tbHABILIDADES = tbHABILIDADESService.save(tbHABILIDADES);
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
        
        tbHABILIDADESList = null;

        FacesMessage facesMessage = MessageFactory.getMessage(message);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        
        return null;
    }
    
    public String delete() {
        
        if (!isPermitted(tbHABILIDADES, "tbHABILIDADES:delete")) {
            return "accessDenied";
        }
        
        String message;
        
        try {
            tbHABILIDADESService.delete(tbHABILIDADES);
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
    
    public void onDialogOpen(TbHABILIDADESEntity tbHABILIDADES) {
        reset();
        this.tbHABILIDADES = tbHABILIDADES;
    }
    
    public void reset() {
        tbHABILIDADES = null;
        tbHABILIDADESList = null;
        
        tbHABILIDADESCARGOSs = null;
        transferedTbHABILIDADESCARGOSIDs = null;
        removedTbHABILIDADESCARGOSIDs = null;
        
    }

    public DualListModel<TbHABILIDADESCARGOSEntity> getTbHABILIDADESCARGOSs() {
        return tbHABILIDADESCARGOSs;
    }

    public void setTbHABILIDADESCARGOSs(DualListModel<TbHABILIDADESCARGOSEntity> tbHABILIDADESCARGOSs) {
        this.tbHABILIDADESCARGOSs = tbHABILIDADESCARGOSs;
    }
    
    public List<TbHABILIDADESCARGOSEntity> getFullTbHABILIDADESCARGOSsList() {
        List<TbHABILIDADESCARGOSEntity> allList = new ArrayList<>();
        allList.addAll(tbHABILIDADESCARGOSs.getSource());
        allList.addAll(tbHABILIDADESCARGOSs.getTarget());
        return allList;
    }
    
    public void onTbHABILIDADESCARGOSsDialog(TbHABILIDADESEntity tbHABILIDADES) {
        // Prepare the tbHABILIDADESCARGOS PickList
        this.tbHABILIDADES = tbHABILIDADES;
        List<TbHABILIDADESCARGOSEntity> selectedTbHABILIDADESCARGOSsFromDB = tbHABILIDADESCARGOSService
                .findTbHABILIDADESCARGOSsByIdHABCARGOS(this.tbHABILIDADES);
        List<TbHABILIDADESCARGOSEntity> availableTbHABILIDADESCARGOSsFromDB = tbHABILIDADESCARGOSService
                .findAvailableTbHABILIDADESCARGOSs(this.tbHABILIDADES);
        this.tbHABILIDADESCARGOSs = new DualListModel<>(availableTbHABILIDADESCARGOSsFromDB, selectedTbHABILIDADESCARGOSsFromDB);
        
        transferedTbHABILIDADESCARGOSIDs = new ArrayList<>();
        removedTbHABILIDADESCARGOSIDs = new ArrayList<>();
    }
    
    public void onTbHABILIDADESCARGOSsPickListTransfer(TransferEvent event) {
        // If a tbHABILIDADESCARGOS is transferred within the PickList, we just transfer it in this
        // bean scope. We do not change anything it the database, yet.
        for (Object item : event.getItems()) {
            String id = ((TbHABILIDADESCARGOSEntity) item).getId().toString();
            if (event.isAdd()) {
                transferedTbHABILIDADESCARGOSIDs.add(id);
                removedTbHABILIDADESCARGOSIDs.remove(id);
            } else if (event.isRemove()) {
                removedTbHABILIDADESCARGOSIDs.add(id);
                transferedTbHABILIDADESCARGOSIDs.remove(id);
            }
        }
        
    }
    
    public void updateTbHABILIDADESCARGOS(TbHABILIDADESCARGOSEntity tbHABILIDADESCARGOS) {
        // If a new tbHABILIDADESCARGOS is created, we persist it to the database,
        // but we do not assign it to this tbHABILIDADES in the database, yet.
        tbHABILIDADESCARGOSs.getTarget().add(tbHABILIDADESCARGOS);
        transferedTbHABILIDADESCARGOSIDs.add(tbHABILIDADESCARGOS.getId().toString());
    }
    
    public void onTbHABILIDADESCARGOSsSubmit() {
        // Now we save the changed of the PickList to the database.
        try {
            List<TbHABILIDADESCARGOSEntity> selectedTbHABILIDADESCARGOSsFromDB = tbHABILIDADESCARGOSService
                    .findTbHABILIDADESCARGOSsByIdHABCARGOS(this.tbHABILIDADES);
            List<TbHABILIDADESCARGOSEntity> availableTbHABILIDADESCARGOSsFromDB = tbHABILIDADESCARGOSService
                    .findAvailableTbHABILIDADESCARGOSs(this.tbHABILIDADES);
            
            for (TbHABILIDADESCARGOSEntity tbHABILIDADESCARGOS : selectedTbHABILIDADESCARGOSsFromDB) {
                if (removedTbHABILIDADESCARGOSIDs.contains(tbHABILIDADESCARGOS.getId().toString())) {
                    tbHABILIDADESCARGOS.setIdHABCARGOS(null);
                    tbHABILIDADESCARGOSService.update(tbHABILIDADESCARGOS);
                }
            }
    
            for (TbHABILIDADESCARGOSEntity tbHABILIDADESCARGOS : availableTbHABILIDADESCARGOSsFromDB) {
                if (transferedTbHABILIDADESCARGOSIDs.contains(tbHABILIDADESCARGOS.getId().toString())) {
                    tbHABILIDADESCARGOS.setIdHABCARGOS(tbHABILIDADES);
                    tbHABILIDADESCARGOSService.update(tbHABILIDADESCARGOS);
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
    
    public TbHABILIDADESEntity getTbHABILIDADES() {
        if (this.tbHABILIDADES == null) {
            prepareNewTbHABILIDADES();
        }
        return this.tbHABILIDADES;
    }
    
    public void setTbHABILIDADES(TbHABILIDADESEntity tbHABILIDADES) {
        this.tbHABILIDADES = tbHABILIDADES;
    }
    
    public List<TbHABILIDADESEntity> getTbHABILIDADESList() {
        if (tbHABILIDADESList == null) {
            tbHABILIDADESList = tbHABILIDADESService.findAllTbHABILIDADESEntities();
        }
        return tbHABILIDADESList;
    }

    public void setTbHABILIDADESList(List<TbHABILIDADESEntity> tbHABILIDADESList) {
        this.tbHABILIDADESList = tbHABILIDADESList;
    }
    
    public boolean isPermitted(String permission) {
        return SecurityWrapper.isPermitted(permission);
    }

    public boolean isPermitted(TbHABILIDADESEntity tbHABILIDADES, String permission) {
        
        return SecurityWrapper.isPermitted(permission);
        
    }
    
}
