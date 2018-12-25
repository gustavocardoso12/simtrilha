package org.applicationn.simtrilhas.web;

import java.io.Serializable;
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

import org.applicationn.simtrilhas.domain.TbDIRETORIAEntity;
import org.applicationn.simtrilhas.domain.TbVICEPRESIDENCIAEntity;
import org.applicationn.simtrilhas.service.TbDIRETORIAService;
import org.applicationn.simtrilhas.service.TbVICEPRESIDENCIAService;
import org.applicationn.simtrilhas.service.security.SecurityWrapper;
import org.applicationn.simtrilhas.web.util.MessageFactory;

@Named("tbDIRETORIABean")
@ViewScoped
public class TbDIRETORIABean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(TbDIRETORIABean.class.getName());
    
    private List<TbDIRETORIAEntity> tbDIRETORIAList;

    private TbDIRETORIAEntity tbDIRETORIA;
    
    @Inject
    private TbDIRETORIAService tbDIRETORIAService;
    
    @Inject
    private TbVICEPRESIDENCIAService tbVICEPRESIDENCIAService;
    
    private List<TbVICEPRESIDENCIAEntity> allIdVPsList;
    
    public void prepareNewTbDIRETORIA() {
        reset();
        this.tbDIRETORIA = new TbDIRETORIAEntity();
        // set any default values now, if you need
        // Example: this.tbDIRETORIA.setAnything("test");
    }

    public String persist() {

        if (tbDIRETORIA.getId() == null && !isPermitted("tbDIRETORIA:create")) {
            return "accessDenied";
        } else if (tbDIRETORIA.getId() != null && !isPermitted(tbDIRETORIA, "tbDIRETORIA:update")) {
            return "accessDenied";
        }
        
        String message;
        
        try {
            
            if (tbDIRETORIA.getId() != null) {
                tbDIRETORIA = tbDIRETORIAService.update(tbDIRETORIA);
                message = "message_successfully_updated";
            } else {
                tbDIRETORIA = tbDIRETORIAService.save(tbDIRETORIA);
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
        
        tbDIRETORIAList = null;

        FacesMessage facesMessage = MessageFactory.getMessage(message);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        
        return null;
    }
    
    public String delete() {
        
        if (!isPermitted(tbDIRETORIA, "tbDIRETORIA:delete")) {
            return "accessDenied";
        }
        
        String message;
        
        try {
            tbDIRETORIAService.delete(tbDIRETORIA);
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
    
    public void onDialogOpen(TbDIRETORIAEntity tbDIRETORIA) {
        reset();
        this.tbDIRETORIA = tbDIRETORIA;
    }
    
    public void reset() {
        tbDIRETORIA = null;
        tbDIRETORIAList = null;
        
        allIdVPsList = null;
        
    }

    // Get a List of all idVP
    public List<TbVICEPRESIDENCIAEntity> getIdVPs() {
        if (this.allIdVPsList == null) {
            this.allIdVPsList = tbVICEPRESIDENCIAService.findAllTbVICEPRESIDENCIAEntities();
        }
        return this.allIdVPsList;
    }
    
    // Update idVP of the current tbDIRETORIA
    public void updateIdVP(TbVICEPRESIDENCIAEntity tbVICEPRESIDENCIA) {
        this.tbDIRETORIA.setIdVP(tbVICEPRESIDENCIA);
        // Maybe we just created and assigned a new tbVICEPRESIDENCIA. So reset the allIdVPList.
        allIdVPsList = null;
    }
    
    public TbDIRETORIAEntity getTbDIRETORIA() {
        if (this.tbDIRETORIA == null) {
            prepareNewTbDIRETORIA();
        }
        return this.tbDIRETORIA;
    }
    
    public void setTbDIRETORIA(TbDIRETORIAEntity tbDIRETORIA) {
        this.tbDIRETORIA = tbDIRETORIA;
    }
    
    public List<TbDIRETORIAEntity> getTbDIRETORIAList() {
        if (tbDIRETORIAList == null) {
            tbDIRETORIAList = tbDIRETORIAService.findAllTbDIRETORIAEntities();
        }
        return tbDIRETORIAList;
    }

    public void setTbDIRETORIAList(List<TbDIRETORIAEntity> tbDIRETORIAList) {
        this.tbDIRETORIAList = tbDIRETORIAList;
    }
    
    public boolean isPermitted(String permission) {
        return SecurityWrapper.isPermitted(permission);
    }

    public boolean isPermitted(TbDIRETORIAEntity tbDIRETORIA, String permission) {
        
        return SecurityWrapper.isPermitted(permission);
        
    }
    
}
