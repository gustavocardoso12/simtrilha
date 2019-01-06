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

import org.applicationn.simtrilhas.domain.TbESTILOPENSAMENTOCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbESTILOPENSAMENTOEntity;
import org.applicationn.simtrilhas.service.TbESTILOPENSAMENTOCARGOSService;
import org.applicationn.simtrilhas.service.TbESTILOPENSAMENTOService;
import org.applicationn.simtrilhas.service.security.SecurityWrapper;
import org.applicationn.simtrilhas.web.util.MessageFactory;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;

@Named("tbESTILOPENSAMENTOBean")
@ViewScoped
public class TbESTILOPENSAMENTOBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(TbESTILOPENSAMENTOBean.class.getName());
    
    private List<TbESTILOPENSAMENTOEntity> tbESTILOPENSAMENTOList;

    private TbESTILOPENSAMENTOEntity tbESTILOPENSAMENTO;
    
    @Inject
    private TbESTILOPENSAMENTOService tbESTILOPENSAMENTOService;
    
    @Inject
    private TbESTILOPENSAMENTOCARGOSService tbESTILOPENSAMENTOCARGOSService;
    
    private DualListModel<TbESTILOPENSAMENTOCARGOSEntity> tbESTILOPENSAMENTOCARGOSs;
    private List<String> transferedTbESTILOPENSAMENTOCARGOSIDs;
    private List<String> removedTbESTILOPENSAMENTOCARGOSIDs;
    
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
    
    
    public void prepareNewTbESTILOPENSAMENTO() {
        reset();
        changeHeaderCadastrar();
        this.tbESTILOPENSAMENTO = new TbESTILOPENSAMENTOEntity();
        // set any default values now, if you need
        // Example: this.tbESTILOPENSAMENTO.setAnything("test");
    }

    public String persist() {

        if (tbESTILOPENSAMENTO.getId() == null && !isPermitted("tbESTILOPENSAMENTO:create")) {
            return "accessDenied";
        } else if (tbESTILOPENSAMENTO.getId() != null && !isPermitted(tbESTILOPENSAMENTO, "tbESTILOPENSAMENTO:update")) {
            return "accessDenied";
        }
        
        String message;
        
        try {
            
            if (tbESTILOPENSAMENTO.getId() != null) {
                tbESTILOPENSAMENTO = tbESTILOPENSAMENTOService.update(tbESTILOPENSAMENTO);
                message = "message_successfully_updated";
            } else {
                tbESTILOPENSAMENTO = tbESTILOPENSAMENTOService.save(tbESTILOPENSAMENTO);
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
        
        tbESTILOPENSAMENTOList = null;

        FacesMessage facesMessage = MessageFactory.getMessage(message);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        
        return null;
    }
    
    public String delete() {
        
        if (!isPermitted(tbESTILOPENSAMENTO, "tbESTILOPENSAMENTO:delete")) {
            return "accessDenied";
        }
        
        String message;
        
        try {
            tbESTILOPENSAMENTOService.delete(tbESTILOPENSAMENTO);
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
    
    public void onDialogOpen(TbESTILOPENSAMENTOEntity tbESTILOPENSAMENTO) {
        reset();
        changeHeaderEditar();
        this.tbESTILOPENSAMENTO = tbESTILOPENSAMENTO;
    }
    
    public void reset() {
        tbESTILOPENSAMENTO = null;
        tbESTILOPENSAMENTOList = null;
        
        tbESTILOPENSAMENTOCARGOSs = null;
        transferedTbESTILOPENSAMENTOCARGOSIDs = null;
        removedTbESTILOPENSAMENTOCARGOSIDs = null;
        
    }

    public DualListModel<TbESTILOPENSAMENTOCARGOSEntity> getTbESTILOPENSAMENTOCARGOSs() {
        return tbESTILOPENSAMENTOCARGOSs;
    }

    public void setTbESTILOPENSAMENTOCARGOSs(DualListModel<TbESTILOPENSAMENTOCARGOSEntity> tbESTILOPENSAMENTOCARGOSs) {
        this.tbESTILOPENSAMENTOCARGOSs = tbESTILOPENSAMENTOCARGOSs;
    }
    
    public List<TbESTILOPENSAMENTOCARGOSEntity> getFullTbESTILOPENSAMENTOCARGOSsList() {
        List<TbESTILOPENSAMENTOCARGOSEntity> allList = new ArrayList<>();
        allList.addAll(tbESTILOPENSAMENTOCARGOSs.getSource());
        allList.addAll(tbESTILOPENSAMENTOCARGOSs.getTarget());
        return allList;
    }
    
    public void onTbESTILOPENSAMENTOCARGOSsDialog(TbESTILOPENSAMENTOEntity tbESTILOPENSAMENTO) {
        // Prepare the tbESTILOPENSAMENTOCARGOS PickList
        this.tbESTILOPENSAMENTO = tbESTILOPENSAMENTO;
        List<TbESTILOPENSAMENTOCARGOSEntity> selectedTbESTILOPENSAMENTOCARGOSsFromDB = tbESTILOPENSAMENTOCARGOSService
                .findTbESTILOPENSAMENTOCARGOSsByIdESTPENSAMENTO(this.tbESTILOPENSAMENTO);
        List<TbESTILOPENSAMENTOCARGOSEntity> availableTbESTILOPENSAMENTOCARGOSsFromDB = tbESTILOPENSAMENTOCARGOSService
                .findAvailableTbESTILOPENSAMENTOCARGOSs(this.tbESTILOPENSAMENTO);
        this.tbESTILOPENSAMENTOCARGOSs = new DualListModel<>(availableTbESTILOPENSAMENTOCARGOSsFromDB, selectedTbESTILOPENSAMENTOCARGOSsFromDB);
        
        transferedTbESTILOPENSAMENTOCARGOSIDs = new ArrayList<>();
        removedTbESTILOPENSAMENTOCARGOSIDs = new ArrayList<>();
    }
    
    public void onTbESTILOPENSAMENTOCARGOSsPickListTransfer(TransferEvent event) {
        // If a tbESTILOPENSAMENTOCARGOS is transferred within the PickList, we just transfer it in this
        // bean scope. We do not change anything it the database, yet.
        for (Object item : event.getItems()) {
            String id = ((TbESTILOPENSAMENTOCARGOSEntity) item).getId().toString();
            if (event.isAdd()) {
                transferedTbESTILOPENSAMENTOCARGOSIDs.add(id);
                removedTbESTILOPENSAMENTOCARGOSIDs.remove(id);
            } else if (event.isRemove()) {
                removedTbESTILOPENSAMENTOCARGOSIDs.add(id);
                transferedTbESTILOPENSAMENTOCARGOSIDs.remove(id);
            }
        }
        
    }
    
    public void updateTbESTILOPENSAMENTOCARGOS(TbESTILOPENSAMENTOCARGOSEntity tbESTILOPENSAMENTOCARGOS) {
        // If a new tbESTILOPENSAMENTOCARGOS is created, we persist it to the database,
        // but we do not assign it to this tbESTILOPENSAMENTO in the database, yet.
        tbESTILOPENSAMENTOCARGOSs.getTarget().add(tbESTILOPENSAMENTOCARGOS);
        transferedTbESTILOPENSAMENTOCARGOSIDs.add(tbESTILOPENSAMENTOCARGOS.getId().toString());
    }
    
    public void onTbESTILOPENSAMENTOCARGOSsSubmit() {
        // Now we save the changed of the PickList to the database.
        try {
            List<TbESTILOPENSAMENTOCARGOSEntity> selectedTbESTILOPENSAMENTOCARGOSsFromDB = tbESTILOPENSAMENTOCARGOSService
                    .findTbESTILOPENSAMENTOCARGOSsByIdESTPENSAMENTO(this.tbESTILOPENSAMENTO);
            List<TbESTILOPENSAMENTOCARGOSEntity> availableTbESTILOPENSAMENTOCARGOSsFromDB = tbESTILOPENSAMENTOCARGOSService
                    .findAvailableTbESTILOPENSAMENTOCARGOSs(this.tbESTILOPENSAMENTO);
            
            for (TbESTILOPENSAMENTOCARGOSEntity tbESTILOPENSAMENTOCARGOS : selectedTbESTILOPENSAMENTOCARGOSsFromDB) {
                if (removedTbESTILOPENSAMENTOCARGOSIDs.contains(tbESTILOPENSAMENTOCARGOS.getId().toString())) {
                    tbESTILOPENSAMENTOCARGOS.setIdESTPENSAMENTO(null);
                    tbESTILOPENSAMENTOCARGOSService.update(tbESTILOPENSAMENTOCARGOS);
                }
            }
    
            for (TbESTILOPENSAMENTOCARGOSEntity tbESTILOPENSAMENTOCARGOS : availableTbESTILOPENSAMENTOCARGOSsFromDB) {
                if (transferedTbESTILOPENSAMENTOCARGOSIDs.contains(tbESTILOPENSAMENTOCARGOS.getId().toString())) {
                    tbESTILOPENSAMENTOCARGOS.setIdESTPENSAMENTO(tbESTILOPENSAMENTO);
                    tbESTILOPENSAMENTOCARGOSService.update(tbESTILOPENSAMENTOCARGOS);
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
    
    public TbESTILOPENSAMENTOEntity getTbESTILOPENSAMENTO() {
        if (this.tbESTILOPENSAMENTO == null) {
            prepareNewTbESTILOPENSAMENTO();
        }
        return this.tbESTILOPENSAMENTO;
    }
    
    public void setTbESTILOPENSAMENTO(TbESTILOPENSAMENTOEntity tbESTILOPENSAMENTO) {
        this.tbESTILOPENSAMENTO = tbESTILOPENSAMENTO;
    }
    
    public List<TbESTILOPENSAMENTOEntity> getTbESTILOPENSAMENTOList() {
        if (tbESTILOPENSAMENTOList == null) {
            tbESTILOPENSAMENTOList = tbESTILOPENSAMENTOService.findAllTbESTILOPENSAMENTOEntities();
        }
        return tbESTILOPENSAMENTOList;
    }

    public void setTbESTILOPENSAMENTOList(List<TbESTILOPENSAMENTOEntity> tbESTILOPENSAMENTOList) {
        this.tbESTILOPENSAMENTOList = tbESTILOPENSAMENTOList;
    }
    
    public boolean isPermitted(String permission) {
        return SecurityWrapper.isPermitted(permission);
    }

    public boolean isPermitted(TbESTILOPENSAMENTOEntity tbESTILOPENSAMENTO, String permission) {
        
        return SecurityWrapper.isPermitted(permission);
        
    }
    
}
