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

import org.applicationn.simtrilhas.domain.TbGRADECARGOSEntity;
import org.applicationn.simtrilhas.domain.TbGRADEEntity;
import org.applicationn.simtrilhas.service.TbGRADECARGOSService;
import org.applicationn.simtrilhas.service.TbGRADEService;
import org.applicationn.simtrilhas.service.security.SecurityWrapper;
import org.applicationn.simtrilhas.web.util.MessageFactory;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;

@Named("tbGRADEBean")
@ViewScoped
public class TbGRADEBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(TbGRADEBean.class.getName());
    
    private List<TbGRADEEntity> tbGRADEList;

    private TbGRADEEntity tbGRADE;
    
    @Inject
    private TbGRADEService tbGRADEService;
    
    @Inject
    private TbGRADECARGOSService tbGRADECARGOSService;
    
    private DualListModel<TbGRADECARGOSEntity> tbGRADECARGOSs;
    private List<String> transferedTbGRADECARGOSIDs;
    private List<String> removedTbGRADECARGOSIDs;
    
    
    private String dialogHeader;

    public void setDialogHeader(final String dialogHeader) { 
        this.dialogHeader = dialogHeader;
    }

    public String getDialogHeader() {
        return dialogHeader;
    }

    public void changeHeaderCadastrar() {
        setDialogHeader("Cadastrar Vice presidência");
    }
    
    public void changeHeaderEditar() {
        setDialogHeader("Editar Vice presidência");
    }
    
    public void prepareNewTbGRADE() {
        reset();
        this.tbGRADE = new TbGRADEEntity();
        // set any default values now, if you need
        // Example: this.tbGRADE.setAnything("test");
    }

    public String persist() {

        if (tbGRADE.getId() == null && !isPermitted("tbGRADE:create")) {
            return "accessDenied";
        } else if (tbGRADE.getId() != null && !isPermitted(tbGRADE, "tbGRADE:update")) {
            return "accessDenied";
        }
        
        String message;
        
        try {
            
            if (tbGRADE.getId() != null) {
                tbGRADE = tbGRADEService.update(tbGRADE);
                message = "message_successfully_updated";
            } else {
                tbGRADE = tbGRADEService.save(tbGRADE);
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
        
        tbGRADEList = null;

        FacesMessage facesMessage = MessageFactory.getMessage(message);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        
        return null;
    }
    
    public String delete() {
        
        if (!isPermitted(tbGRADE, "tbGRADE:delete")) {
            return "accessDenied";
        }
        
        String message;
        
        try {
            tbGRADEService.delete(tbGRADE);
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
    
    public void onDialogOpen(TbGRADEEntity tbGRADE) {
        reset();
        this.tbGRADE = tbGRADE;
    }
    
    public void reset() {
        tbGRADE = null;
        tbGRADEList = null;
        
        tbGRADECARGOSs = null;
        transferedTbGRADECARGOSIDs = null;
        removedTbGRADECARGOSIDs = null;
        
    }

    public DualListModel<TbGRADECARGOSEntity> getTbGRADECARGOSs() {
        return tbGRADECARGOSs;
    }

    public void setTbGRADECARGOSs(DualListModel<TbGRADECARGOSEntity> tbGRADECARGOSs) {
        this.tbGRADECARGOSs = tbGRADECARGOSs;
    }
    
    public List<TbGRADECARGOSEntity> getFullTbGRADECARGOSsList() {
        List<TbGRADECARGOSEntity> allList = new ArrayList<>();
        allList.addAll(tbGRADECARGOSs.getSource());
        allList.addAll(tbGRADECARGOSs.getTarget());
        return allList;
    }
    
    public void onTbGRADECARGOSsDialog(TbGRADEEntity tbGRADE) {
        // Prepare the tbGRADECARGOS PickList
        this.tbGRADE = tbGRADE;
        List<TbGRADECARGOSEntity> selectedTbGRADECARGOSsFromDB = tbGRADECARGOSService
                .findTbGRADECARGOSsByIdGRADE(this.tbGRADE);
        List<TbGRADECARGOSEntity> availableTbGRADECARGOSsFromDB = tbGRADECARGOSService
                .findAvailableTbGRADECARGOSs(this.tbGRADE);
        this.tbGRADECARGOSs = new DualListModel<>(availableTbGRADECARGOSsFromDB, selectedTbGRADECARGOSsFromDB);
        
        transferedTbGRADECARGOSIDs = new ArrayList<>();
        removedTbGRADECARGOSIDs = new ArrayList<>();
    }
    
    public void onTbGRADECARGOSsPickListTransfer(TransferEvent event) {
        // If a tbGRADECARGOS is transferred within the PickList, we just transfer it in this
        // bean scope. We do not change anything it the database, yet.
        for (Object item : event.getItems()) {
            String id = ((TbGRADECARGOSEntity) item).getId().toString();
            if (event.isAdd()) {
                transferedTbGRADECARGOSIDs.add(id);
                removedTbGRADECARGOSIDs.remove(id);
            } else if (event.isRemove()) {
                removedTbGRADECARGOSIDs.add(id);
                transferedTbGRADECARGOSIDs.remove(id);
            }
        }
        
    }
    
    public void updateTbGRADECARGOS(TbGRADECARGOSEntity tbGRADECARGOS) {
        // If a new tbGRADECARGOS is created, we persist it to the database,
        // but we do not assign it to this tbGRADE in the database, yet.
        tbGRADECARGOSs.getTarget().add(tbGRADECARGOS);
        transferedTbGRADECARGOSIDs.add(tbGRADECARGOS.getId().toString());
    }
    
    public void onTbGRADECARGOSsSubmit() {
        // Now we save the changed of the PickList to the database.
        try {
            List<TbGRADECARGOSEntity> selectedTbGRADECARGOSsFromDB = tbGRADECARGOSService
                    .findTbGRADECARGOSsByIdGRADE(this.tbGRADE);
            List<TbGRADECARGOSEntity> availableTbGRADECARGOSsFromDB = tbGRADECARGOSService
                    .findAvailableTbGRADECARGOSs(this.tbGRADE);
            
            for (TbGRADECARGOSEntity tbGRADECARGOS : selectedTbGRADECARGOSsFromDB) {
                if (removedTbGRADECARGOSIDs.contains(tbGRADECARGOS.getId().toString())) {
                    tbGRADECARGOS.setIdGRADE(null);
                    tbGRADECARGOSService.update(tbGRADECARGOS);
                }
            }
    
            for (TbGRADECARGOSEntity tbGRADECARGOS : availableTbGRADECARGOSsFromDB) {
                if (transferedTbGRADECARGOSIDs.contains(tbGRADECARGOS.getId().toString())) {
                    tbGRADECARGOS.setIdGRADE(tbGRADE);
                    tbGRADECARGOSService.update(tbGRADECARGOS);
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
    
    public TbGRADEEntity getTbGRADE() {
        if (this.tbGRADE == null) {
            prepareNewTbGRADE();
        }
        return this.tbGRADE;
    }
    
    public void setTbGRADE(TbGRADEEntity tbGRADE) {
        this.tbGRADE = tbGRADE;
    }
    
    public List<TbGRADEEntity> getTbGRADEList() {
        if (tbGRADEList == null) {
            tbGRADEList = tbGRADEService.findAllTbGRADEEntities();
        }
        return tbGRADEList;
    }

    public void setTbGRADEList(List<TbGRADEEntity> tbGRADEList) {
        this.tbGRADEList = tbGRADEList;
    }
    
    public boolean isPermitted(String permission) {
        return SecurityWrapper.isPermitted(permission);
    }

    public boolean isPermitted(TbGRADEEntity tbGRADE, String permission) {
        
        return SecurityWrapper.isPermitted(permission);
        
    }
    
}
