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

import org.applicationn.simtrilhas.domain.TbCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbCOMPETENCIASCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbCOMPETENCIASEMCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbCONHECIMENTOSBASCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbCONHECIMENTOSESPCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbCURSOSEntity;
import org.applicationn.simtrilhas.domain.TbESTATUARIOEntity;
import org.applicationn.simtrilhas.domain.TbESTILOLIDERANCACARGOSEntity;
import org.applicationn.simtrilhas.domain.TbESTILOPENSAMENTOCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbHABILIDADESCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbHABILIDADESCULTCARGOSEntity;
import org.applicationn.simtrilhas.service.TbCARGOSService;
import org.applicationn.simtrilhas.service.TbCOMPETENCIASCARGOSService;
import org.applicationn.simtrilhas.service.TbCOMPETENCIASEMCARGOSService;
import org.applicationn.simtrilhas.service.TbCONHECIMENTOSBASCARGOSService;
import org.applicationn.simtrilhas.service.TbCONHECIMENTOSESPCARGOSService;
import org.applicationn.simtrilhas.service.TbCURSOSService;
import org.applicationn.simtrilhas.service.TbESTATUARIOService;
import org.applicationn.simtrilhas.service.TbESTILOLIDERANCACARGOSService;
import org.applicationn.simtrilhas.service.TbESTILOPENSAMENTOCARGOSService;
import org.applicationn.simtrilhas.service.TbHABILIDADESCARGOSService;
import org.applicationn.simtrilhas.service.TbHABILIDADESCULTCARGOSService;
import org.applicationn.simtrilhas.service.security.SecurityWrapper;
import org.applicationn.simtrilhas.web.util.MessageFactory;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;

@Named("tbCARGOSBean")
@ViewScoped
public class TbCARGOSBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(TbCARGOSBean.class.getName());
    
    private List<TbCARGOSEntity> tbCARGOSList;

    private TbCARGOSEntity tbCARGOS;
    
    @Inject
    private TbCARGOSService tbCARGOSService;
    
    @Inject
    private TbESTATUARIOService tbESTATUARIOService;
    
    @Inject
    private TbCURSOSService tbCURSOSService;
    
    @Inject
    private TbCOMPETENCIASCARGOSService tbCOMPETENCIASCARGOSService;
    
    @Inject
    private TbCOMPETENCIASEMCARGOSService tbCOMPETENCIASEMCARGOSService;
    
    @Inject
    private TbCONHECIMENTOSBASCARGOSService tbCONHECIMENTOSBASCARGOSService;
    
    @Inject
    private TbCONHECIMENTOSESPCARGOSService tbCONHECIMENTOSESPCARGOSService;
    
    @Inject
    private TbESTILOLIDERANCACARGOSService tbESTILOLIDERANCACARGOSService;
    
    @Inject
    private TbESTILOPENSAMENTOCARGOSService tbESTILOPENSAMENTOCARGOSService;
    
    @Inject
    private TbHABILIDADESCARGOSService tbHABILIDADESCARGOSService;
    
    @Inject
    private TbHABILIDADESCULTCARGOSService tbHABILIDADESCULTCARGOSService;
    
    private DualListModel<TbCOMPETENCIASCARGOSEntity> tbCOMPETENCIASCARGOSs;
    private List<String> transferedTbCOMPETENCIASCARGOSIDs;
    private List<String> removedTbCOMPETENCIASCARGOSIDs;
    
    private DualListModel<TbCOMPETENCIASEMCARGOSEntity> tbCOMPETENCIASEMCARGOSs;
    private List<String> transferedTbCOMPETENCIASEMCARGOSIDs;
    private List<String> removedTbCOMPETENCIASEMCARGOSIDs;
    
    private DualListModel<TbCONHECIMENTOSBASCARGOSEntity> tbCONHECIMENTOSBASCARGOSs;
    private List<String> transferedTbCONHECIMENTOSBASCARGOSIDs;
    private List<String> removedTbCONHECIMENTOSBASCARGOSIDs;
    
    private DualListModel<TbCONHECIMENTOSESPCARGOSEntity> tbCONHECIMENTOSESPCARGOSs;
    private List<String> transferedTbCONHECIMENTOSESPCARGOSIDs;
    private List<String> removedTbCONHECIMENTOSESPCARGOSIDs;
    
    private DualListModel<TbESTILOLIDERANCACARGOSEntity> tbESTILOLIDERANCACARGOSs;
    private List<String> transferedTbESTILOLIDERANCACARGOSIDs;
    private List<String> removedTbESTILOLIDERANCACARGOSIDs;
    
    private DualListModel<TbESTILOPENSAMENTOCARGOSEntity> tbESTILOPENSAMENTOCARGOSs;
    private List<String> transferedTbESTILOPENSAMENTOCARGOSIDs;
    private List<String> removedTbESTILOPENSAMENTOCARGOSIDs;
    
    private DualListModel<TbHABILIDADESCARGOSEntity> tbHABILIDADESCARGOSs;
    private List<String> transferedTbHABILIDADESCARGOSIDs;
    private List<String> removedTbHABILIDADESCARGOSIDs;
    
    private DualListModel<TbHABILIDADESCULTCARGOSEntity> tbHABILIDADESCULTCARGOSs;
    private List<String> transferedTbHABILIDADESCULTCARGOSIDs;
    private List<String> removedTbHABILIDADESCULTCARGOSIDs;
    
    private List<TbESTATUARIOEntity> allIdESTsList;
    
    private List<TbCURSOSEntity> allIdCURSOSsList;
    
    public void prepareNewTbCARGOS() {
        reset();
        this.tbCARGOS = new TbCARGOSEntity();
        // set any default values now, if you need
        // Example: this.tbCARGOS.setAnything("test");
    }

    public String persist() {

        String message;
        
        try {
            
            if (tbCARGOS.getId() != null) {
                tbCARGOS = tbCARGOSService.update(tbCARGOS);
                message = "message_successfully_updated";
            } else {
                tbCARGOS = tbCARGOSService.save(tbCARGOS);
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
        
        tbCARGOSList = null;

        FacesMessage facesMessage = MessageFactory.getMessage(message);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        
        return null;
    }
    
    public String delete() {
        
        String message;
        
        try {
            tbCARGOSService.delete(tbCARGOS);
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
    
    public void onDialogOpen(TbCARGOSEntity tbCARGOS) {
        reset();
        this.tbCARGOS = tbCARGOS;
    }
    
    public void reset() {
        tbCARGOS = null;
        tbCARGOSList = null;
        
        tbCOMPETENCIASCARGOSs = null;
        transferedTbCOMPETENCIASCARGOSIDs = null;
        removedTbCOMPETENCIASCARGOSIDs = null;
        
        tbCOMPETENCIASEMCARGOSs = null;
        transferedTbCOMPETENCIASEMCARGOSIDs = null;
        removedTbCOMPETENCIASEMCARGOSIDs = null;
        
        tbCONHECIMENTOSBASCARGOSs = null;
        transferedTbCONHECIMENTOSBASCARGOSIDs = null;
        removedTbCONHECIMENTOSBASCARGOSIDs = null;
        
        tbCONHECIMENTOSESPCARGOSs = null;
        transferedTbCONHECIMENTOSESPCARGOSIDs = null;
        removedTbCONHECIMENTOSESPCARGOSIDs = null;
        
        tbESTILOLIDERANCACARGOSs = null;
        transferedTbESTILOLIDERANCACARGOSIDs = null;
        removedTbESTILOLIDERANCACARGOSIDs = null;
        
        tbESTILOPENSAMENTOCARGOSs = null;
        transferedTbESTILOPENSAMENTOCARGOSIDs = null;
        removedTbESTILOPENSAMENTOCARGOSIDs = null;
        
        tbHABILIDADESCARGOSs = null;
        transferedTbHABILIDADESCARGOSIDs = null;
        removedTbHABILIDADESCARGOSIDs = null;
        
        tbHABILIDADESCULTCARGOSs = null;
        transferedTbHABILIDADESCULTCARGOSIDs = null;
        removedTbHABILIDADESCULTCARGOSIDs = null;
        
        allIdESTsList = null;
        
        allIdCURSOSsList = null;
        
    }

    // Get a List of all idEST
    public List<TbESTATUARIOEntity> getIdESTs() {
        if (this.allIdESTsList == null) {
            this.allIdESTsList = tbESTATUARIOService.findAllTbESTATUARIOEntities();
        }
        return this.allIdESTsList;
    }
    
    // Update idEST of the current tbCARGOS
    public void updateIdEST(TbESTATUARIOEntity tbESTATUARIO) {
        this.tbCARGOS.setIdEST(tbESTATUARIO);
        // Maybe we just created and assigned a new tbESTATUARIO. So reset the allIdESTList.
        allIdESTsList = null;
    }
    
    // Get a List of all idCURSOS
    public List<TbCURSOSEntity> getIdCURSOSs() {
        if (this.allIdCURSOSsList == null) {
            this.allIdCURSOSsList = tbCURSOSService.findAllTbCURSOSEntities();
        }
        return this.allIdCURSOSsList;
    }
    
    // Update idCURSOS of the current tbCARGOS
    public void updateIdCURSOS(TbCURSOSEntity tbCURSOS) {
        this.tbCARGOS.setIdCURSOS(tbCURSOS);
        // Maybe we just created and assigned a new tbCURSOS. So reset the allIdCURSOSList.
        allIdCURSOSsList = null;
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
    
    public void onTbCOMPETENCIASCARGOSsDialog(TbCARGOSEntity tbCARGOS) {
        // Prepare the tbCOMPETENCIASCARGOS PickList
        this.tbCARGOS = tbCARGOS;
        List<TbCOMPETENCIASCARGOSEntity> selectedTbCOMPETENCIASCARGOSsFromDB = tbCOMPETENCIASCARGOSService
                .findTbCOMPETENCIASCARGOSsByIdCARGOS(this.tbCARGOS);
        List<TbCOMPETENCIASCARGOSEntity> availableTbCOMPETENCIASCARGOSsFromDB = tbCOMPETENCIASCARGOSService
                .findAvailableTbCOMPETENCIASCARGOSs(this.tbCARGOS);
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
        // but we do not assign it to this tbCARGOS in the database, yet.
        tbCOMPETENCIASCARGOSs.getTarget().add(tbCOMPETENCIASCARGOS);
        transferedTbCOMPETENCIASCARGOSIDs.add(tbCOMPETENCIASCARGOS.getId().toString());
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
    
    public void onTbCOMPETENCIASEMCARGOSsDialog(TbCARGOSEntity tbCARGOS) {
        // Prepare the tbCOMPETENCIASEMCARGOS PickList
        this.tbCARGOS = tbCARGOS;
        List<TbCOMPETENCIASEMCARGOSEntity> selectedTbCOMPETENCIASEMCARGOSsFromDB = tbCOMPETENCIASEMCARGOSService
                .findTbCOMPETENCIASEMCARGOSsByIdCARGOS(this.tbCARGOS);
        List<TbCOMPETENCIASEMCARGOSEntity> availableTbCOMPETENCIASEMCARGOSsFromDB = tbCOMPETENCIASEMCARGOSService
                .findAvailableTbCOMPETENCIASEMCARGOSs(this.tbCARGOS);
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
        // but we do not assign it to this tbCARGOS in the database, yet.
        tbCOMPETENCIASEMCARGOSs.getTarget().add(tbCOMPETENCIASEMCARGOS);
        transferedTbCOMPETENCIASEMCARGOSIDs.add(tbCOMPETENCIASEMCARGOS.getId().toString());
    }
    
    public DualListModel<TbCONHECIMENTOSBASCARGOSEntity> getTbCONHECIMENTOSBASCARGOSs() {
        return tbCONHECIMENTOSBASCARGOSs;
    }

    public void setTbCONHECIMENTOSBASCARGOSs(DualListModel<TbCONHECIMENTOSBASCARGOSEntity> tbCONHECIMENTOSBASCARGOSs) {
        this.tbCONHECIMENTOSBASCARGOSs = tbCONHECIMENTOSBASCARGOSs;
    }
    
    public List<TbCONHECIMENTOSBASCARGOSEntity> getFullTbCONHECIMENTOSBASCARGOSsList() {
        List<TbCONHECIMENTOSBASCARGOSEntity> allList = new ArrayList<>();
        allList.addAll(tbCONHECIMENTOSBASCARGOSs.getSource());
        allList.addAll(tbCONHECIMENTOSBASCARGOSs.getTarget());
        return allList;
    }
    
    public void onTbCONHECIMENTOSBASCARGOSsDialog(TbCARGOSEntity tbCARGOS) {
        // Prepare the tbCONHECIMENTOSBASCARGOS PickList
        this.tbCARGOS = tbCARGOS;
        List<TbCONHECIMENTOSBASCARGOSEntity> selectedTbCONHECIMENTOSBASCARGOSsFromDB = tbCONHECIMENTOSBASCARGOSService
                .findTbCONHECIMENTOSBASCARGOSsByIdCARGOS(this.tbCARGOS);
        List<TbCONHECIMENTOSBASCARGOSEntity> availableTbCONHECIMENTOSBASCARGOSsFromDB = tbCONHECIMENTOSBASCARGOSService
                .findAvailableTbCONHECIMENTOSBASCARGOSs(this.tbCARGOS);
        this.tbCONHECIMENTOSBASCARGOSs = new DualListModel<>(availableTbCONHECIMENTOSBASCARGOSsFromDB, selectedTbCONHECIMENTOSBASCARGOSsFromDB);
        
        transferedTbCONHECIMENTOSBASCARGOSIDs = new ArrayList<>();
        removedTbCONHECIMENTOSBASCARGOSIDs = new ArrayList<>();
    }
    
    public void onTbCONHECIMENTOSBASCARGOSsPickListTransfer(TransferEvent event) {
        // If a tbCONHECIMENTOSBASCARGOS is transferred within the PickList, we just transfer it in this
        // bean scope. We do not change anything it the database, yet.
        for (Object item : event.getItems()) {
            String id = ((TbCONHECIMENTOSBASCARGOSEntity) item).getId().toString();
            if (event.isAdd()) {
                transferedTbCONHECIMENTOSBASCARGOSIDs.add(id);
                removedTbCONHECIMENTOSBASCARGOSIDs.remove(id);
            } else if (event.isRemove()) {
                removedTbCONHECIMENTOSBASCARGOSIDs.add(id);
                transferedTbCONHECIMENTOSBASCARGOSIDs.remove(id);
            }
        }
        
    }
    
    public void updateTbCONHECIMENTOSBASCARGOS(TbCONHECIMENTOSBASCARGOSEntity tbCONHECIMENTOSBASCARGOS) {
        // If a new tbCONHECIMENTOSBASCARGOS is created, we persist it to the database,
        // but we do not assign it to this tbCARGOS in the database, yet.
        tbCONHECIMENTOSBASCARGOSs.getTarget().add(tbCONHECIMENTOSBASCARGOS);
        transferedTbCONHECIMENTOSBASCARGOSIDs.add(tbCONHECIMENTOSBASCARGOS.getId().toString());
    }
    
    public DualListModel<TbCONHECIMENTOSESPCARGOSEntity> getTbCONHECIMENTOSESPCARGOSs() {
        return tbCONHECIMENTOSESPCARGOSs;
    }

    public void setTbCONHECIMENTOSESPCARGOSs(DualListModel<TbCONHECIMENTOSESPCARGOSEntity> tbCONHECIMENTOSESPCARGOSs) {
        this.tbCONHECIMENTOSESPCARGOSs = tbCONHECIMENTOSESPCARGOSs;
    }
    
    public List<TbCONHECIMENTOSESPCARGOSEntity> getFullTbCONHECIMENTOSESPCARGOSsList() {
        List<TbCONHECIMENTOSESPCARGOSEntity> allList = new ArrayList<>();
        allList.addAll(tbCONHECIMENTOSESPCARGOSs.getSource());
        allList.addAll(tbCONHECIMENTOSESPCARGOSs.getTarget());
        return allList;
    }
    
    public void onTbCONHECIMENTOSESPCARGOSsDialog(TbCARGOSEntity tbCARGOS) {
        // Prepare the tbCONHECIMENTOSESPCARGOS PickList
        this.tbCARGOS = tbCARGOS;
        List<TbCONHECIMENTOSESPCARGOSEntity> selectedTbCONHECIMENTOSESPCARGOSsFromDB = tbCONHECIMENTOSESPCARGOSService
                .findTbCONHECIMENTOSESPCARGOSsByIdCARGOS(this.tbCARGOS);
        List<TbCONHECIMENTOSESPCARGOSEntity> availableTbCONHECIMENTOSESPCARGOSsFromDB = tbCONHECIMENTOSESPCARGOSService
                .findAvailableTbCONHECIMENTOSESPCARGOSs(this.tbCARGOS);
        this.tbCONHECIMENTOSESPCARGOSs = new DualListModel<>(availableTbCONHECIMENTOSESPCARGOSsFromDB, selectedTbCONHECIMENTOSESPCARGOSsFromDB);
        
        transferedTbCONHECIMENTOSESPCARGOSIDs = new ArrayList<>();
        removedTbCONHECIMENTOSESPCARGOSIDs = new ArrayList<>();
    }
    
    public void onTbCONHECIMENTOSESPCARGOSsPickListTransfer(TransferEvent event) {
        // If a tbCONHECIMENTOSESPCARGOS is transferred within the PickList, we just transfer it in this
        // bean scope. We do not change anything it the database, yet.
        for (Object item : event.getItems()) {
            String id = ((TbCONHECIMENTOSESPCARGOSEntity) item).getId().toString();
            if (event.isAdd()) {
                transferedTbCONHECIMENTOSESPCARGOSIDs.add(id);
                removedTbCONHECIMENTOSESPCARGOSIDs.remove(id);
            } else if (event.isRemove()) {
                removedTbCONHECIMENTOSESPCARGOSIDs.add(id);
                transferedTbCONHECIMENTOSESPCARGOSIDs.remove(id);
            }
        }
        
    }
    
    public void updateTbCONHECIMENTOSESPCARGOS(TbCONHECIMENTOSESPCARGOSEntity tbCONHECIMENTOSESPCARGOS) {
        // If a new tbCONHECIMENTOSESPCARGOS is created, we persist it to the database,
        // but we do not assign it to this tbCARGOS in the database, yet.
        tbCONHECIMENTOSESPCARGOSs.getTarget().add(tbCONHECIMENTOSESPCARGOS);
        transferedTbCONHECIMENTOSESPCARGOSIDs.add(tbCONHECIMENTOSESPCARGOS.getId().toString());
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
    
    public void onTbESTILOLIDERANCACARGOSsDialog(TbCARGOSEntity tbCARGOS) {
        // Prepare the tbESTILOLIDERANCACARGOS PickList
        this.tbCARGOS = tbCARGOS;
        List<TbESTILOLIDERANCACARGOSEntity> selectedTbESTILOLIDERANCACARGOSsFromDB = tbESTILOLIDERANCACARGOSService
                .findTbESTILOLIDERANCACARGOSsByIdCARGOS(this.tbCARGOS);
        List<TbESTILOLIDERANCACARGOSEntity> availableTbESTILOLIDERANCACARGOSsFromDB = tbESTILOLIDERANCACARGOSService
                .findAvailableTbESTILOLIDERANCACARGOSs(this.tbCARGOS);
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
        // but we do not assign it to this tbCARGOS in the database, yet.
        tbESTILOLIDERANCACARGOSs.getTarget().add(tbESTILOLIDERANCACARGOS);
        transferedTbESTILOLIDERANCACARGOSIDs.add(tbESTILOLIDERANCACARGOS.getId().toString());
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
    
    public void onTbESTILOPENSAMENTOCARGOSsDialog(TbCARGOSEntity tbCARGOS) {
        // Prepare the tbESTILOPENSAMENTOCARGOS PickList
        this.tbCARGOS = tbCARGOS;
        List<TbESTILOPENSAMENTOCARGOSEntity> selectedTbESTILOPENSAMENTOCARGOSsFromDB = tbESTILOPENSAMENTOCARGOSService
                .findTbESTILOPENSAMENTOCARGOSsByIdCARGOS(this.tbCARGOS);
        List<TbESTILOPENSAMENTOCARGOSEntity> availableTbESTILOPENSAMENTOCARGOSsFromDB = tbESTILOPENSAMENTOCARGOSService
                .findAvailableTbESTILOPENSAMENTOCARGOSs(this.tbCARGOS);
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
        // but we do not assign it to this tbCARGOS in the database, yet.
        tbESTILOPENSAMENTOCARGOSs.getTarget().add(tbESTILOPENSAMENTOCARGOS);
        transferedTbESTILOPENSAMENTOCARGOSIDs.add(tbESTILOPENSAMENTOCARGOS.getId().toString());
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
    
    public void onTbHABILIDADESCARGOSsDialog(TbCARGOSEntity tbCARGOS) {
        // Prepare the tbHABILIDADESCARGOS PickList
        this.tbCARGOS = tbCARGOS;
        List<TbHABILIDADESCARGOSEntity> selectedTbHABILIDADESCARGOSsFromDB = tbHABILIDADESCARGOSService
                .findTbHABILIDADESCARGOSsByIdCARGOS(this.tbCARGOS);
        List<TbHABILIDADESCARGOSEntity> availableTbHABILIDADESCARGOSsFromDB = tbHABILIDADESCARGOSService
                .findAvailableTbHABILIDADESCARGOSs(this.tbCARGOS);
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
        // but we do not assign it to this tbCARGOS in the database, yet.
        tbHABILIDADESCARGOSs.getTarget().add(tbHABILIDADESCARGOS);
        transferedTbHABILIDADESCARGOSIDs.add(tbHABILIDADESCARGOS.getId().toString());
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
    
    public void onTbHABILIDADESCULTCARGOSsDialog(TbCARGOSEntity tbCARGOS) {
        // Prepare the tbHABILIDADESCULTCARGOS PickList
        this.tbCARGOS = tbCARGOS;
        List<TbHABILIDADESCULTCARGOSEntity> selectedTbHABILIDADESCULTCARGOSsFromDB = tbHABILIDADESCULTCARGOSService
                .findTbHABILIDADESCULTCARGOSsByIdCARGOS(this.tbCARGOS);
        List<TbHABILIDADESCULTCARGOSEntity> availableTbHABILIDADESCULTCARGOSsFromDB = tbHABILIDADESCULTCARGOSService
                .findAvailableTbHABILIDADESCULTCARGOSs(this.tbCARGOS);
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
        // but we do not assign it to this tbCARGOS in the database, yet.
        tbHABILIDADESCULTCARGOSs.getTarget().add(tbHABILIDADESCULTCARGOS);
        transferedTbHABILIDADESCULTCARGOSIDs.add(tbHABILIDADESCULTCARGOS.getId().toString());
    }
    
    public void onTbCOMPETENCIASCARGOSsSubmit() {
        // Now we save the changed of the PickList to the database.
        try {
            List<TbCOMPETENCIASCARGOSEntity> selectedTbCOMPETENCIASCARGOSsFromDB = tbCOMPETENCIASCARGOSService
                    .findTbCOMPETENCIASCARGOSsByIdCARGOS(this.tbCARGOS);
            List<TbCOMPETENCIASCARGOSEntity> availableTbCOMPETENCIASCARGOSsFromDB = tbCOMPETENCIASCARGOSService
                    .findAvailableTbCOMPETENCIASCARGOSs(this.tbCARGOS);
            
            for (TbCOMPETENCIASCARGOSEntity tbCOMPETENCIASCARGOS : selectedTbCOMPETENCIASCARGOSsFromDB) {
                if (removedTbCOMPETENCIASCARGOSIDs.contains(tbCOMPETENCIASCARGOS.getId().toString())) {
                    tbCOMPETENCIASCARGOS.setIdCARGOS(null);
                    tbCOMPETENCIASCARGOSService.update(tbCOMPETENCIASCARGOS);
                }
            }
    
            for (TbCOMPETENCIASCARGOSEntity tbCOMPETENCIASCARGOS : availableTbCOMPETENCIASCARGOSsFromDB) {
                if (transferedTbCOMPETENCIASCARGOSIDs.contains(tbCOMPETENCIASCARGOS.getId().toString())) {
                    tbCOMPETENCIASCARGOS.setIdCARGOS(tbCARGOS);
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
    
    public void onTbCOMPETENCIASEMCARGOSsSubmit() {
        // Now we save the changed of the PickList to the database.
        try {
            List<TbCOMPETENCIASEMCARGOSEntity> selectedTbCOMPETENCIASEMCARGOSsFromDB = tbCOMPETENCIASEMCARGOSService
                    .findTbCOMPETENCIASEMCARGOSsByIdCARGOS(this.tbCARGOS);
            List<TbCOMPETENCIASEMCARGOSEntity> availableTbCOMPETENCIASEMCARGOSsFromDB = tbCOMPETENCIASEMCARGOSService
                    .findAvailableTbCOMPETENCIASEMCARGOSs(this.tbCARGOS);
            
            for (TbCOMPETENCIASEMCARGOSEntity tbCOMPETENCIASEMCARGOS : selectedTbCOMPETENCIASEMCARGOSsFromDB) {
                if (removedTbCOMPETENCIASEMCARGOSIDs.contains(tbCOMPETENCIASEMCARGOS.getId().toString())) {
                    tbCOMPETENCIASEMCARGOS.setIdCARGOS(null);
                    tbCOMPETENCIASEMCARGOSService.update(tbCOMPETENCIASEMCARGOS);
                }
            }
    
            for (TbCOMPETENCIASEMCARGOSEntity tbCOMPETENCIASEMCARGOS : availableTbCOMPETENCIASEMCARGOSsFromDB) {
                if (transferedTbCOMPETENCIASEMCARGOSIDs.contains(tbCOMPETENCIASEMCARGOS.getId().toString())) {
                    tbCOMPETENCIASEMCARGOS.setIdCARGOS(tbCARGOS);
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
    
    public void onTbCONHECIMENTOSBASCARGOSsSubmit() {
        // Now we save the changed of the PickList to the database.
        try {
            List<TbCONHECIMENTOSBASCARGOSEntity> selectedTbCONHECIMENTOSBASCARGOSsFromDB = tbCONHECIMENTOSBASCARGOSService
                    .findTbCONHECIMENTOSBASCARGOSsByIdCARGOS(this.tbCARGOS);
            List<TbCONHECIMENTOSBASCARGOSEntity> availableTbCONHECIMENTOSBASCARGOSsFromDB = tbCONHECIMENTOSBASCARGOSService
                    .findAvailableTbCONHECIMENTOSBASCARGOSs(this.tbCARGOS);
            
            for (TbCONHECIMENTOSBASCARGOSEntity tbCONHECIMENTOSBASCARGOS : selectedTbCONHECIMENTOSBASCARGOSsFromDB) {
                if (removedTbCONHECIMENTOSBASCARGOSIDs.contains(tbCONHECIMENTOSBASCARGOS.getId().toString())) {
                    tbCONHECIMENTOSBASCARGOS.setIdCARGOS(null);
                    tbCONHECIMENTOSBASCARGOSService.update(tbCONHECIMENTOSBASCARGOS);
                }
            }
    
            for (TbCONHECIMENTOSBASCARGOSEntity tbCONHECIMENTOSBASCARGOS : availableTbCONHECIMENTOSBASCARGOSsFromDB) {
                if (transferedTbCONHECIMENTOSBASCARGOSIDs.contains(tbCONHECIMENTOSBASCARGOS.getId().toString())) {
                    tbCONHECIMENTOSBASCARGOS.setIdCARGOS(tbCARGOS);
                    tbCONHECIMENTOSBASCARGOSService.update(tbCONHECIMENTOSBASCARGOS);
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
    
    public void onTbCONHECIMENTOSESPCARGOSsSubmit() {
        // Now we save the changed of the PickList to the database.
        try {
            List<TbCONHECIMENTOSESPCARGOSEntity> selectedTbCONHECIMENTOSESPCARGOSsFromDB = tbCONHECIMENTOSESPCARGOSService
                    .findTbCONHECIMENTOSESPCARGOSsByIdCARGOS(this.tbCARGOS);
            List<TbCONHECIMENTOSESPCARGOSEntity> availableTbCONHECIMENTOSESPCARGOSsFromDB = tbCONHECIMENTOSESPCARGOSService
                    .findAvailableTbCONHECIMENTOSESPCARGOSs(this.tbCARGOS);
            
            for (TbCONHECIMENTOSESPCARGOSEntity tbCONHECIMENTOSESPCARGOS : selectedTbCONHECIMENTOSESPCARGOSsFromDB) {
                if (removedTbCONHECIMENTOSESPCARGOSIDs.contains(tbCONHECIMENTOSESPCARGOS.getId().toString())) {
                    tbCONHECIMENTOSESPCARGOS.setIdCARGOS(null);
                    tbCONHECIMENTOSESPCARGOSService.update(tbCONHECIMENTOSESPCARGOS);
                }
            }
    
            for (TbCONHECIMENTOSESPCARGOSEntity tbCONHECIMENTOSESPCARGOS : availableTbCONHECIMENTOSESPCARGOSsFromDB) {
                if (transferedTbCONHECIMENTOSESPCARGOSIDs.contains(tbCONHECIMENTOSESPCARGOS.getId().toString())) {
                    tbCONHECIMENTOSESPCARGOS.setIdCARGOS(tbCARGOS);
                    tbCONHECIMENTOSESPCARGOSService.update(tbCONHECIMENTOSESPCARGOS);
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
    
    public void onTbESTILOLIDERANCACARGOSsSubmit() {
        // Now we save the changed of the PickList to the database.
        try {
            List<TbESTILOLIDERANCACARGOSEntity> selectedTbESTILOLIDERANCACARGOSsFromDB = tbESTILOLIDERANCACARGOSService
                    .findTbESTILOLIDERANCACARGOSsByIdCARGOS(this.tbCARGOS);
            List<TbESTILOLIDERANCACARGOSEntity> availableTbESTILOLIDERANCACARGOSsFromDB = tbESTILOLIDERANCACARGOSService
                    .findAvailableTbESTILOLIDERANCACARGOSs(this.tbCARGOS);
            
            for (TbESTILOLIDERANCACARGOSEntity tbESTILOLIDERANCACARGOS : selectedTbESTILOLIDERANCACARGOSsFromDB) {
                if (removedTbESTILOLIDERANCACARGOSIDs.contains(tbESTILOLIDERANCACARGOS.getId().toString())) {
                    tbESTILOLIDERANCACARGOS.setIdCARGOS(null);
                    tbESTILOLIDERANCACARGOSService.update(tbESTILOLIDERANCACARGOS);
                }
            }
    
            for (TbESTILOLIDERANCACARGOSEntity tbESTILOLIDERANCACARGOS : availableTbESTILOLIDERANCACARGOSsFromDB) {
                if (transferedTbESTILOLIDERANCACARGOSIDs.contains(tbESTILOLIDERANCACARGOS.getId().toString())) {
                    tbESTILOLIDERANCACARGOS.setIdCARGOS(tbCARGOS);
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
    
    public void onTbESTILOPENSAMENTOCARGOSsSubmit() {
        // Now we save the changed of the PickList to the database.
        try {
            List<TbESTILOPENSAMENTOCARGOSEntity> selectedTbESTILOPENSAMENTOCARGOSsFromDB = tbESTILOPENSAMENTOCARGOSService
                    .findTbESTILOPENSAMENTOCARGOSsByIdCARGOS(this.tbCARGOS);
            List<TbESTILOPENSAMENTOCARGOSEntity> availableTbESTILOPENSAMENTOCARGOSsFromDB = tbESTILOPENSAMENTOCARGOSService
                    .findAvailableTbESTILOPENSAMENTOCARGOSs(this.tbCARGOS);
            
            for (TbESTILOPENSAMENTOCARGOSEntity tbESTILOPENSAMENTOCARGOS : selectedTbESTILOPENSAMENTOCARGOSsFromDB) {
                if (removedTbESTILOPENSAMENTOCARGOSIDs.contains(tbESTILOPENSAMENTOCARGOS.getId().toString())) {
                    tbESTILOPENSAMENTOCARGOS.setIdCARGOS(null);
                    tbESTILOPENSAMENTOCARGOSService.update(tbESTILOPENSAMENTOCARGOS);
                }
            }
    
            for (TbESTILOPENSAMENTOCARGOSEntity tbESTILOPENSAMENTOCARGOS : availableTbESTILOPENSAMENTOCARGOSsFromDB) {
                if (transferedTbESTILOPENSAMENTOCARGOSIDs.contains(tbESTILOPENSAMENTOCARGOS.getId().toString())) {
                    tbESTILOPENSAMENTOCARGOS.setIdCARGOS(tbCARGOS);
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
    
    public void onTbHABILIDADESCARGOSsSubmit() {
        // Now we save the changed of the PickList to the database.
        try {
            List<TbHABILIDADESCARGOSEntity> selectedTbHABILIDADESCARGOSsFromDB = tbHABILIDADESCARGOSService
                    .findTbHABILIDADESCARGOSsByIdCARGOS(this.tbCARGOS);
            List<TbHABILIDADESCARGOSEntity> availableTbHABILIDADESCARGOSsFromDB = tbHABILIDADESCARGOSService
                    .findAvailableTbHABILIDADESCARGOSs(this.tbCARGOS);
            
            for (TbHABILIDADESCARGOSEntity tbHABILIDADESCARGOS : selectedTbHABILIDADESCARGOSsFromDB) {
                if (removedTbHABILIDADESCARGOSIDs.contains(tbHABILIDADESCARGOS.getId().toString())) {
                    tbHABILIDADESCARGOS.setIdCARGOS(null);
                    tbHABILIDADESCARGOSService.update(tbHABILIDADESCARGOS);
                }
            }
    
            for (TbHABILIDADESCARGOSEntity tbHABILIDADESCARGOS : availableTbHABILIDADESCARGOSsFromDB) {
                if (transferedTbHABILIDADESCARGOSIDs.contains(tbHABILIDADESCARGOS.getId().toString())) {
                    tbHABILIDADESCARGOS.setIdCARGOS(tbCARGOS);
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
    
    public void onTbHABILIDADESCULTCARGOSsSubmit() {
        // Now we save the changed of the PickList to the database.
        try {
            List<TbHABILIDADESCULTCARGOSEntity> selectedTbHABILIDADESCULTCARGOSsFromDB = tbHABILIDADESCULTCARGOSService
                    .findTbHABILIDADESCULTCARGOSsByIdCARGOS(this.tbCARGOS);
            List<TbHABILIDADESCULTCARGOSEntity> availableTbHABILIDADESCULTCARGOSsFromDB = tbHABILIDADESCULTCARGOSService
                    .findAvailableTbHABILIDADESCULTCARGOSs(this.tbCARGOS);
            
            for (TbHABILIDADESCULTCARGOSEntity tbHABILIDADESCULTCARGOS : selectedTbHABILIDADESCULTCARGOSsFromDB) {
                if (removedTbHABILIDADESCULTCARGOSIDs.contains(tbHABILIDADESCULTCARGOS.getId().toString())) {
                    tbHABILIDADESCULTCARGOS.setIdCARGOS(null);
                    tbHABILIDADESCULTCARGOSService.update(tbHABILIDADESCULTCARGOS);
                }
            }
    
            for (TbHABILIDADESCULTCARGOSEntity tbHABILIDADESCULTCARGOS : availableTbHABILIDADESCULTCARGOSsFromDB) {
                if (transferedTbHABILIDADESCULTCARGOSIDs.contains(tbHABILIDADESCULTCARGOS.getId().toString())) {
                    tbHABILIDADESCULTCARGOS.setIdCARGOS(tbCARGOS);
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
    
    public TbCARGOSEntity getTbCARGOS() {
        if (this.tbCARGOS == null) {
            prepareNewTbCARGOS();
        }
        return this.tbCARGOS;
    }
    
    public void setTbCARGOS(TbCARGOSEntity tbCARGOS) {
        this.tbCARGOS = tbCARGOS;
    }
    
    public List<TbCARGOSEntity> getTbCARGOSList() {
        if (tbCARGOSList == null) {
            tbCARGOSList = tbCARGOSService.findAllTbCARGOSEntities();
        }
        return tbCARGOSList;
    }

    public void setTbCARGOSList(List<TbCARGOSEntity> tbCARGOSList) {
        this.tbCARGOSList = tbCARGOSList;
    }
    
    public boolean isPermitted(String permission) {
        return SecurityWrapper.isPermitted(permission);
    }

    public boolean isPermitted(TbCARGOSEntity tbCARGOS, String permission) {
        
        return SecurityWrapper.isPermitted(permission);
        
    }
    
}
