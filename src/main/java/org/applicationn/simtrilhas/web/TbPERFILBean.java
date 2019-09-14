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

import org.applicationn.simtrilhas.domain.TbPERFILCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbPERFILEntity;
import org.applicationn.simtrilhas.service.TbPERFILCARGOSService;
import org.applicationn.simtrilhas.service.TbPERFILService;
import org.applicationn.simtrilhas.service.security.SecurityWrapper;
import org.applicationn.simtrilhas.web.util.MessageFactory;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;

@Named("tbPERFILBean")
@ViewScoped
public class TbPERFILBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(TbPERFILBean.class.getName());
    
    private List<TbPERFILEntity> tbPERFILList;

    private TbPERFILEntity tbPERFIL;
    
    @Inject
    private TbPERFILService tbPERFILService;
    
    @Inject
    private TbPERFILCARGOSService tbPERFILCARGOSService;
    
    private DualListModel<TbPERFILCARGOSEntity> tbPERFILCARGOSs;
    private List<String> transferedTbPERFILCARGOSIDs;
    private List<String> removedTbPERFILCARGOSIDs;
    
    private String dialogHeader;

    public void setDialogHeader(final String dialogHeader) { 
        this.dialogHeader = dialogHeader;
    }

    public String getDialogHeader() {
        return dialogHeader;
    }

    public void changeHeaderCadastrar() {
        setDialogHeader("Cadastrar Perfil");
    }
    
    public void changeHeaderEditar() {
        setDialogHeader("Editar Perfil");
    }
    
    
    public void prepareNewTbPERFIL() {
        reset();
        this.tbPERFIL = new TbPERFILEntity();
        // set any default values now, if you need
        // Example: this.tbPERFIL.setAnything("test");
    }

    public String persist() {

        if (tbPERFIL.getId() == null && !isPermitted("tbPERFIL:create")) {
            return "accessDenied";
        } else if (tbPERFIL.getId() != null && !isPermitted(tbPERFIL, "tbPERFIL:update")) {
            return "accessDenied";
        }
        
        String message;
        
        try {
            
            if (tbPERFIL.getId() != null) {
                tbPERFIL = tbPERFILService.update(tbPERFIL);
                message = "message_successfully_updated";
            } else {
                tbPERFIL = tbPERFILService.save(tbPERFIL);
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
        
        tbPERFILList = null;

        FacesMessage facesMessage = MessageFactory.getMessage(message);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        
        return null;
    }
    
    public String delete() {
        
        if (!isPermitted(tbPERFIL, "tbPERFIL:delete")) {
            return "accessDenied";
        }
        
        String message;
        
        try {
            tbPERFILService.delete(tbPERFIL);
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
    
    public void onDialogOpen(TbPERFILEntity tbPERFIL) {
        reset();
        this.tbPERFIL = tbPERFIL;
    }
    
    public void reset() {
        tbPERFIL = null;
        tbPERFILList = null;
        
        tbPERFILCARGOSs = null;
        transferedTbPERFILCARGOSIDs = null;
        removedTbPERFILCARGOSIDs = null;
        
    }

    public DualListModel<TbPERFILCARGOSEntity> getTbPERFILCARGOSs() {
        return tbPERFILCARGOSs;
    }

    public void setTbPERFILCARGOSs(DualListModel<TbPERFILCARGOSEntity> tbPERFILCARGOSs) {
        this.tbPERFILCARGOSs = tbPERFILCARGOSs;
    }
    
    public List<TbPERFILCARGOSEntity> getFullTbPERFILCARGOSsList() {
        List<TbPERFILCARGOSEntity> allList = new ArrayList<>();
        allList.addAll(tbPERFILCARGOSs.getSource());
        allList.addAll(tbPERFILCARGOSs.getTarget());
        return allList;
    }
    
    public void onTbPERFILCARGOSsDialog(TbPERFILEntity tbPERFIL) {
        // Prepare the tbPERFILCARGOS PickList
        this.tbPERFIL = tbPERFIL;
        List<TbPERFILCARGOSEntity> selectedTbPERFILCARGOSsFromDB = tbPERFILCARGOSService
                .findTbPERFILCARGOSsByIdPERFIL(this.tbPERFIL);
        List<TbPERFILCARGOSEntity> availableTbPERFILCARGOSsFromDB = tbPERFILCARGOSService
                .findAvailableTbPERFILCARGOSs(this.tbPERFIL);
        this.tbPERFILCARGOSs = new DualListModel<>(availableTbPERFILCARGOSsFromDB, selectedTbPERFILCARGOSsFromDB);
        
        transferedTbPERFILCARGOSIDs = new ArrayList<>();
        removedTbPERFILCARGOSIDs = new ArrayList<>();
    }
    
    public void onTbPERFILCARGOSsPickListTransfer(TransferEvent event) {
        // If a tbPERFILCARGOS is transferred within the PickList, we just transfer it in this
        // bean scope. We do not change anything it the database, yet.
        for (Object item : event.getItems()) {
            String id = ((TbPERFILCARGOSEntity) item).getId().toString();
            if (event.isAdd()) {
                transferedTbPERFILCARGOSIDs.add(id);
                removedTbPERFILCARGOSIDs.remove(id);
            } else if (event.isRemove()) {
                removedTbPERFILCARGOSIDs.add(id);
                transferedTbPERFILCARGOSIDs.remove(id);
            }
        }
        
    }
    
    public void updateTbPERFILCARGOS(TbPERFILCARGOSEntity tbPERFILCARGOS) {
        // If a new tbPERFILCARGOS is created, we persist it to the database,
        // but we do not assign it to this tbPERFIL in the database, yet.
        tbPERFILCARGOSs.getTarget().add(tbPERFILCARGOS);
        transferedTbPERFILCARGOSIDs.add(tbPERFILCARGOS.getId().toString());
    }
    
    public void onTbPERFILCARGOSsSubmit() {
        // Now we save the changed of the PickList to the database.
        try {
            List<TbPERFILCARGOSEntity> selectedTbPERFILCARGOSsFromDB = tbPERFILCARGOSService
                    .findTbPERFILCARGOSsByIdPERFIL(this.tbPERFIL);
            List<TbPERFILCARGOSEntity> availableTbPERFILCARGOSsFromDB = tbPERFILCARGOSService
                    .findAvailableTbPERFILCARGOSs(this.tbPERFIL);
            
            for (TbPERFILCARGOSEntity tbPERFILCARGOS : selectedTbPERFILCARGOSsFromDB) {
                if (removedTbPERFILCARGOSIDs.contains(tbPERFILCARGOS.getId().toString())) {
                    tbPERFILCARGOS.setIdPERFIL(null);
                    tbPERFILCARGOSService.update(tbPERFILCARGOS);
                }
            }
    
            for (TbPERFILCARGOSEntity tbPERFILCARGOS : availableTbPERFILCARGOSsFromDB) {
                if (transferedTbPERFILCARGOSIDs.contains(tbPERFILCARGOS.getId().toString())) {
                    tbPERFILCARGOS.setIdPERFIL(tbPERFIL);
                    tbPERFILCARGOSService.update(tbPERFILCARGOS);
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
    
    public TbPERFILEntity getTbPERFIL() {
        if (this.tbPERFIL == null) {
            prepareNewTbPERFIL();
        }
        return this.tbPERFIL;
    }
    
    public void setTbPERFIL(TbPERFILEntity tbPERFIL) {
        this.tbPERFIL = tbPERFIL;
    }
    
    public List<TbPERFILEntity> getTbPERFILList() {
        if (tbPERFILList == null) {
            tbPERFILList = tbPERFILService.findAllTbPERFILEntities();
        }
        return tbPERFILList;
    }

    public void setTbPERFILList(List<TbPERFILEntity> tbPERFILList) {
        this.tbPERFILList = tbPERFILList;
    }
    
    public boolean isPermitted(String permission) {
        return SecurityWrapper.isPermitted(permission);
    }

    public boolean isPermitted(TbPERFILEntity tbPERFIL, String permission) {
        
        return SecurityWrapper.isPermitted(permission);
        
    }
    
}
