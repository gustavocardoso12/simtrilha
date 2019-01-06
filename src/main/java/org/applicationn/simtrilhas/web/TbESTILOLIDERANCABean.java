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

import org.applicationn.simtrilhas.domain.TbESTILOLIDERANCACARGOSEntity;
import org.applicationn.simtrilhas.domain.TbESTILOLIDERANCAEntity;
import org.applicationn.simtrilhas.service.TbESTILOLIDERANCACARGOSService;
import org.applicationn.simtrilhas.service.TbESTILOLIDERANCAService;
import org.applicationn.simtrilhas.service.security.SecurityWrapper;
import org.applicationn.simtrilhas.web.util.MessageFactory;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;

@Named("tbESTILOLIDERANCABean")
@ViewScoped
public class TbESTILOLIDERANCABean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(TbESTILOLIDERANCABean.class.getName());
    
    private List<TbESTILOLIDERANCAEntity> tbESTILOLIDERANCAList;

    private TbESTILOLIDERANCAEntity tbESTILOLIDERANCA;
    
    @Inject
    private TbESTILOLIDERANCAService tbESTILOLIDERANCAService;
    
    @Inject
    private TbESTILOLIDERANCACARGOSService tbESTILOLIDERANCACARGOSService;
    
    private DualListModel<TbESTILOLIDERANCACARGOSEntity> tbESTILOLIDERANCACARGOSs;
    private List<String> transferedTbESTILOLIDERANCACARGOSIDs;
    private List<String> removedTbESTILOLIDERANCACARGOSIDs;
    
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
    
    
    public void prepareNewTbESTILOLIDERANCA() {
        reset();
        changeHeaderCadastrar();
        this.tbESTILOLIDERANCA = new TbESTILOLIDERANCAEntity();
        // set any default values now, if you need
        // Example: this.tbESTILOLIDERANCA.setAnything("test");
    }

    public String persist() {

        if (tbESTILOLIDERANCA.getId() == null && !isPermitted("tbESTILOLIDERANCA:create")) {
            return "accessDenied";
        } else if (tbESTILOLIDERANCA.getId() != null && !isPermitted(tbESTILOLIDERANCA, "tbESTILOLIDERANCA:update")) {
            return "accessDenied";
        }
        
        String message;
        
        try {
            
            if (tbESTILOLIDERANCA.getId() != null) {
                tbESTILOLIDERANCA = tbESTILOLIDERANCAService.update(tbESTILOLIDERANCA);
                message = "message_successfully_updated";
            } else {
                tbESTILOLIDERANCA = tbESTILOLIDERANCAService.save(tbESTILOLIDERANCA);
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
        
        tbESTILOLIDERANCAList = null;

        FacesMessage facesMessage = MessageFactory.getMessage(message);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        
        return null;
    }
    
    public String delete() {
        
        if (!isPermitted(tbESTILOLIDERANCA, "tbESTILOLIDERANCA:delete")) {
            return "accessDenied";
        }
        
        String message;
        
        try {
            tbESTILOLIDERANCAService.delete(tbESTILOLIDERANCA);
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
    
    public void onDialogOpen(TbESTILOLIDERANCAEntity tbESTILOLIDERANCA) {
        reset();
        changeHeaderEditar();
        this.tbESTILOLIDERANCA = tbESTILOLIDERANCA;
    }
    
    public void reset() {
        tbESTILOLIDERANCA = null;
        tbESTILOLIDERANCAList = null;
        
        tbESTILOLIDERANCACARGOSs = null;
        transferedTbESTILOLIDERANCACARGOSIDs = null;
        removedTbESTILOLIDERANCACARGOSIDs = null;
        
    }

    public DualListModel<TbESTILOLIDERANCACARGOSEntity> getTbESTILOLIDERANCACARGOSs() {
        return tbESTILOLIDERANCACARGOSs;
    }

    public void setTbESTILOLIDERANCACARGOSs(DualListModel<TbESTILOLIDERANCACARGOSEntity> tbESTILOLIDERANCACARGOSs) {
        this.tbESTILOLIDERANCACARGOSs = tbESTILOLIDERANCACARGOSs;
    }
    
    public List<TbESTILOLIDERANCACARGOSEntity> getFullTbESTILOLIDERANCACARGOSsList() {
        List<TbESTILOLIDERANCACARGOSEntity> allList = new ArrayList<>();
        allList.addAll(tbESTILOLIDERANCACARGOSs.getSource());
        allList.addAll(tbESTILOLIDERANCACARGOSs.getTarget());
        return allList;
    }
    
    public void onTbESTILOLIDERANCACARGOSsDialog(TbESTILOLIDERANCAEntity tbESTILOLIDERANCA) {
        // Prepare the tbESTILOLIDERANCACARGOS PickList
        this.tbESTILOLIDERANCA = tbESTILOLIDERANCA;
        List<TbESTILOLIDERANCACARGOSEntity> selectedTbESTILOLIDERANCACARGOSsFromDB = tbESTILOLIDERANCACARGOSService
                .findTbESTILOLIDERANCACARGOSsByIdESTLIDER(this.tbESTILOLIDERANCA);
        List<TbESTILOLIDERANCACARGOSEntity> availableTbESTILOLIDERANCACARGOSsFromDB = tbESTILOLIDERANCACARGOSService
                .findAvailableTbESTILOLIDERANCACARGOSs(this.tbESTILOLIDERANCA);
        this.tbESTILOLIDERANCACARGOSs = new DualListModel<>(availableTbESTILOLIDERANCACARGOSsFromDB, selectedTbESTILOLIDERANCACARGOSsFromDB);
        
        transferedTbESTILOLIDERANCACARGOSIDs = new ArrayList<>();
        removedTbESTILOLIDERANCACARGOSIDs = new ArrayList<>();
    }
    
    public void onTbESTILOLIDERANCACARGOSsPickListTransfer(TransferEvent event) {
        // If a tbESTILOLIDERANCACARGOS is transferred within the PickList, we just transfer it in this
        // bean scope. We do not change anything it the database, yet.
        for (Object item : event.getItems()) {
            String id = ((TbESTILOLIDERANCACARGOSEntity) item).getId().toString();
            if (event.isAdd()) {
                transferedTbESTILOLIDERANCACARGOSIDs.add(id);
                removedTbESTILOLIDERANCACARGOSIDs.remove(id);
            } else if (event.isRemove()) {
                removedTbESTILOLIDERANCACARGOSIDs.add(id);
                transferedTbESTILOLIDERANCACARGOSIDs.remove(id);
            }
        }
        
    }
    
    public void updateTbESTILOLIDERANCACARGOS(TbESTILOLIDERANCACARGOSEntity tbESTILOLIDERANCACARGOS) {
        // If a new tbESTILOLIDERANCACARGOS is created, we persist it to the database,
        // but we do not assign it to this tbESTILOLIDERANCA in the database, yet.
        tbESTILOLIDERANCACARGOSs.getTarget().add(tbESTILOLIDERANCACARGOS);
        transferedTbESTILOLIDERANCACARGOSIDs.add(tbESTILOLIDERANCACARGOS.getId().toString());
    }
    
    public void onTbESTILOLIDERANCACARGOSsSubmit() {
        // Now we save the changed of the PickList to the database.
        try {
            List<TbESTILOLIDERANCACARGOSEntity> selectedTbESTILOLIDERANCACARGOSsFromDB = tbESTILOLIDERANCACARGOSService
                    .findTbESTILOLIDERANCACARGOSsByIdESTLIDER(this.tbESTILOLIDERANCA);
            List<TbESTILOLIDERANCACARGOSEntity> availableTbESTILOLIDERANCACARGOSsFromDB = tbESTILOLIDERANCACARGOSService
                    .findAvailableTbESTILOLIDERANCACARGOSs(this.tbESTILOLIDERANCA);
            
            for (TbESTILOLIDERANCACARGOSEntity tbESTILOLIDERANCACARGOS : selectedTbESTILOLIDERANCACARGOSsFromDB) {
                if (removedTbESTILOLIDERANCACARGOSIDs.contains(tbESTILOLIDERANCACARGOS.getId().toString())) {
                    tbESTILOLIDERANCACARGOS.setIdESTLIDER(null);
                    tbESTILOLIDERANCACARGOSService.update(tbESTILOLIDERANCACARGOS);
                }
            }
    
            for (TbESTILOLIDERANCACARGOSEntity tbESTILOLIDERANCACARGOS : availableTbESTILOLIDERANCACARGOSsFromDB) {
                if (transferedTbESTILOLIDERANCACARGOSIDs.contains(tbESTILOLIDERANCACARGOS.getId().toString())) {
                    tbESTILOLIDERANCACARGOS.setIdESTLIDER(tbESTILOLIDERANCA);
                    tbESTILOLIDERANCACARGOSService.update(tbESTILOLIDERANCACARGOS);
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
    
    public TbESTILOLIDERANCAEntity getTbESTILOLIDERANCA() {
        if (this.tbESTILOLIDERANCA == null) {
            prepareNewTbESTILOLIDERANCA();
        }
        return this.tbESTILOLIDERANCA;
    }
    
    public void setTbESTILOLIDERANCA(TbESTILOLIDERANCAEntity tbESTILOLIDERANCA) {
        this.tbESTILOLIDERANCA = tbESTILOLIDERANCA;
    }
    
    public List<TbESTILOLIDERANCAEntity> getTbESTILOLIDERANCAList() {
        if (tbESTILOLIDERANCAList == null) {
            tbESTILOLIDERANCAList = tbESTILOLIDERANCAService.findAllTbESTILOLIDERANCAEntities();
        }
        return tbESTILOLIDERANCAList;
    }

    public void setTbESTILOLIDERANCAList(List<TbESTILOLIDERANCAEntity> tbESTILOLIDERANCAList) {
        this.tbESTILOLIDERANCAList = tbESTILOLIDERANCAList;
    }
    
    public boolean isPermitted(String permission) {
        return SecurityWrapper.isPermitted(permission);
    }

    public boolean isPermitted(TbESTILOLIDERANCAEntity tbESTILOLIDERANCA, String permission) {
        
        return SecurityWrapper.isPermitted(permission);
        
    }
    
}
