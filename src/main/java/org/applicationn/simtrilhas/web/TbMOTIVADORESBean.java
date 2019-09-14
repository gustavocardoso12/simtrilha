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

import org.applicationn.simtrilhas.domain.TbMOTIVADORESCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbMOTIVADORESEntity;
import org.applicationn.simtrilhas.service.TbMOTIVADORESCARGOSService;
import org.applicationn.simtrilhas.service.TbMOTIVADORESService;
import org.applicationn.simtrilhas.service.security.SecurityWrapper;
import org.applicationn.simtrilhas.web.util.MessageFactory;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;

@Named("tbMOTIVADORESBean")
@ViewScoped
public class TbMOTIVADORESBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(TbMOTIVADORESBean.class.getName());
    
    private List<TbMOTIVADORESEntity> tbMOTIVADORESList;

    private TbMOTIVADORESEntity tbMOTIVADORES;
    
    @Inject
    private TbMOTIVADORESService tbMOTIVADORESService;
    
    @Inject
    private TbMOTIVADORESCARGOSService tbMOTIVADORESCARGOSService;
    
    private DualListModel<TbMOTIVADORESCARGOSEntity> tbMOTIVADORESCARGOSs;
    private List<String> transferedTbMOTIVADORESCARGOSIDs;
    private List<String> removedTbMOTIVADORESCARGOSIDs;
    
    private String dialogHeader;

    public void setDialogHeader(final String dialogHeader) { 
        this.dialogHeader = dialogHeader;
    }

    public String getDialogHeader() {
        return dialogHeader;
    }

    public void changeHeaderCadastrar() {
        setDialogHeader("Cadastrar Motivadores");
    }
    
    public void changeHeaderEditar() {
        setDialogHeader("Editar Motivadores");
    }
    
    public void prepareNewTbMOTIVADORES() {
        reset();
        this.tbMOTIVADORES = new TbMOTIVADORESEntity();
        // set any default values now, if you need
        // Example: this.tbMOTIVADORES.setAnything("test");
    }

    public String persist() {

        if (tbMOTIVADORES.getId() == null && !isPermitted("tbMOTIVADORES:create")) {
            return "accessDenied";
        } else if (tbMOTIVADORES.getId() != null && !isPermitted(tbMOTIVADORES, "tbMOTIVADORES:update")) {
            return "accessDenied";
        }
        
        String message;
        
        try {
            
            if (tbMOTIVADORES.getId() != null) {
                tbMOTIVADORES = tbMOTIVADORESService.update(tbMOTIVADORES);
                message = "message_successfully_updated";
            } else {
                tbMOTIVADORES = tbMOTIVADORESService.save(tbMOTIVADORES);
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
        
        tbMOTIVADORESList = null;

        FacesMessage facesMessage = MessageFactory.getMessage(message);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        
        return null;
    }
    
    public String delete() {
        
        if (!isPermitted(tbMOTIVADORES, "tbMOTIVADORES:delete")) {
            return "accessDenied";
        }
        
        String message;
        
        try {
            tbMOTIVADORESService.delete(tbMOTIVADORES);
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
    
    public void onDialogOpen(TbMOTIVADORESEntity tbMOTIVADORES) {
        reset();
        this.tbMOTIVADORES = tbMOTIVADORES;
    }
    
    public void reset() {
        tbMOTIVADORES = null;
        tbMOTIVADORESList = null;
        
        tbMOTIVADORESCARGOSs = null;
        transferedTbMOTIVADORESCARGOSIDs = null;
        removedTbMOTIVADORESCARGOSIDs = null;
        
    }

    public DualListModel<TbMOTIVADORESCARGOSEntity> getTbMOTIVADORESCARGOSs() {
        return tbMOTIVADORESCARGOSs;
    }

    public void setTbMOTIVADORESCARGOSs(DualListModel<TbMOTIVADORESCARGOSEntity> tbMOTIVADORESCARGOSs) {
        this.tbMOTIVADORESCARGOSs = tbMOTIVADORESCARGOSs;
    }
    
    public List<TbMOTIVADORESCARGOSEntity> getFullTbMOTIVADORESCARGOSsList() {
        List<TbMOTIVADORESCARGOSEntity> allList = new ArrayList<>();
        allList.addAll(tbMOTIVADORESCARGOSs.getSource());
        allList.addAll(tbMOTIVADORESCARGOSs.getTarget());
        return allList;
    }
    
    public void onTbMOTIVADORESCARGOSsDialog(TbMOTIVADORESEntity tbMOTIVADORES) {
        // Prepare the tbMOTIVADORESCARGOS PickList
        this.tbMOTIVADORES = tbMOTIVADORES;
        List<TbMOTIVADORESCARGOSEntity> selectedTbMOTIVADORESCARGOSsFromDB = tbMOTIVADORESCARGOSService
                .findTbMOTIVADORESCARGOSsByIdMOTIVADORES(this.tbMOTIVADORES);
        List<TbMOTIVADORESCARGOSEntity> availableTbMOTIVADORESCARGOSsFromDB = tbMOTIVADORESCARGOSService
                .findAvailableTbMOTIVADORESCARGOSs(this.tbMOTIVADORES);
        this.tbMOTIVADORESCARGOSs = new DualListModel<>(availableTbMOTIVADORESCARGOSsFromDB, selectedTbMOTIVADORESCARGOSsFromDB);
        
        transferedTbMOTIVADORESCARGOSIDs = new ArrayList<>();
        removedTbMOTIVADORESCARGOSIDs = new ArrayList<>();
    }
    
    public void onTbMOTIVADORESCARGOSsPickListTransfer(TransferEvent event) {
        // If a tbMOTIVADORESCARGOS is transferred within the PickList, we just transfer it in this
        // bean scope. We do not change anything it the database, yet.
        for (Object item : event.getItems()) {
            String id = ((TbMOTIVADORESCARGOSEntity) item).getId().toString();
            if (event.isAdd()) {
                transferedTbMOTIVADORESCARGOSIDs.add(id);
                removedTbMOTIVADORESCARGOSIDs.remove(id);
            } else if (event.isRemove()) {
                removedTbMOTIVADORESCARGOSIDs.add(id);
                transferedTbMOTIVADORESCARGOSIDs.remove(id);
            }
        }
        
    }
    
    public void updateTbMOTIVADORESCARGOS(TbMOTIVADORESCARGOSEntity tbMOTIVADORESCARGOS) {
        // If a new tbMOTIVADORESCARGOS is created, we persist it to the database,
        // but we do not assign it to this tbMOTIVADORES in the database, yet.
        tbMOTIVADORESCARGOSs.getTarget().add(tbMOTIVADORESCARGOS);
        transferedTbMOTIVADORESCARGOSIDs.add(tbMOTIVADORESCARGOS.getId().toString());
    }
    
    public void onTbMOTIVADORESCARGOSsSubmit() {
        // Now we save the changed of the PickList to the database.
        try {
            List<TbMOTIVADORESCARGOSEntity> selectedTbMOTIVADORESCARGOSsFromDB = tbMOTIVADORESCARGOSService
                    .findTbMOTIVADORESCARGOSsByIdMOTIVADORES(this.tbMOTIVADORES);
            List<TbMOTIVADORESCARGOSEntity> availableTbMOTIVADORESCARGOSsFromDB = tbMOTIVADORESCARGOSService
                    .findAvailableTbMOTIVADORESCARGOSs(this.tbMOTIVADORES);
            
            for (TbMOTIVADORESCARGOSEntity tbMOTIVADORESCARGOS : selectedTbMOTIVADORESCARGOSsFromDB) {
                if (removedTbMOTIVADORESCARGOSIDs.contains(tbMOTIVADORESCARGOS.getId().toString())) {
                    tbMOTIVADORESCARGOS.setIdMOTIVADORES(null);
                    tbMOTIVADORESCARGOSService.update(tbMOTIVADORESCARGOS);
                }
            }
    
            for (TbMOTIVADORESCARGOSEntity tbMOTIVADORESCARGOS : availableTbMOTIVADORESCARGOSsFromDB) {
                if (transferedTbMOTIVADORESCARGOSIDs.contains(tbMOTIVADORESCARGOS.getId().toString())) {
                    tbMOTIVADORESCARGOS.setIdMOTIVADORES(tbMOTIVADORES);
                    tbMOTIVADORESCARGOSService.update(tbMOTIVADORESCARGOS);
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
    
    public TbMOTIVADORESEntity getTbMOTIVADORES() {
        if (this.tbMOTIVADORES == null) {
            prepareNewTbMOTIVADORES();
        }
        return this.tbMOTIVADORES;
    }
    
    public void setTbMOTIVADORES(TbMOTIVADORESEntity tbMOTIVADORES) {
        this.tbMOTIVADORES = tbMOTIVADORES;
    }
    
    public List<TbMOTIVADORESEntity> getTbMOTIVADORESList() {
        if (tbMOTIVADORESList == null) {
            tbMOTIVADORESList = tbMOTIVADORESService.findAllTbMOTIVADORESEntities();
        }
        return tbMOTIVADORESList;
    }

    public void setTbMOTIVADORESList(List<TbMOTIVADORESEntity> tbMOTIVADORESList) {
        this.tbMOTIVADORESList = tbMOTIVADORESList;
    }
    
    public boolean isPermitted(String permission) {
        return SecurityWrapper.isPermitted(permission);
    }

    public boolean isPermitted(TbMOTIVADORESEntity tbMOTIVADORES, String permission) {
        
        return SecurityWrapper.isPermitted(permission);
        
    }
    
}
