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

import org.applicationn.simtrilhas.domain.TbVICEPRESIDENCIAEntity;
import org.applicationn.simtrilhas.service.TbVICEPRESIDENCIAService;
import org.applicationn.simtrilhas.service.security.SecurityWrapper;
import org.applicationn.simtrilhas.web.util.MessageFactory;

@Named("tbVICEPRESIDENCIABean")
@ViewScoped
public class TbVICEPRESIDENCIABean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(TbVICEPRESIDENCIABean.class.getName());
    
    private List<TbVICEPRESIDENCIAEntity> tbVICEPRESIDENCIAList;

    private TbVICEPRESIDENCIAEntity tbVICEPRESIDENCIA;
    
    @Inject
    private TbVICEPRESIDENCIAService tbVICEPRESIDENCIAService;
    
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
    
    public void prepareNewTbVICEPRESIDENCIA() {
        reset();
        changeHeaderCadastrar();
        this.tbVICEPRESIDENCIA = new TbVICEPRESIDENCIAEntity();
        // set any default values now, if you need
        // Example: this.tbVICEPRESIDENCIA.setAnything("test");
    }

    public String persist() {

        if (tbVICEPRESIDENCIA.getId() == null && !isPermitted("tbVICEPRESIDENCIA:create")) {
            return "accessDenied";
        } else if (tbVICEPRESIDENCIA.getId() != null && !isPermitted(tbVICEPRESIDENCIA, "tbVICEPRESIDENCIA:update")) {
            return "accessDenied";
        }
        
        String message;
        
        try {
            
            if (tbVICEPRESIDENCIA.getId() != null) {
                tbVICEPRESIDENCIA = tbVICEPRESIDENCIAService.update(tbVICEPRESIDENCIA);
                message = "message_successfully_updated";
            } else {
                tbVICEPRESIDENCIA = tbVICEPRESIDENCIAService.save(tbVICEPRESIDENCIA);
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
        
        tbVICEPRESIDENCIAList = null;

        FacesMessage facesMessage = MessageFactory.getMessage(message);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        
        return null;
    }
    
    public String delete() {
        
        if (!isPermitted(tbVICEPRESIDENCIA, "tbVICEPRESIDENCIA:delete")) {
            return "accessDenied";
        }
        
        String message;
        
        try {
            tbVICEPRESIDENCIAService.delete(tbVICEPRESIDENCIA);
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
    
    public void onDialogOpen(TbVICEPRESIDENCIAEntity tbVICEPRESIDENCIA) {
        reset();
        changeHeaderEditar();
        this.tbVICEPRESIDENCIA = tbVICEPRESIDENCIA;
    }
    
    public void reset() {
        tbVICEPRESIDENCIA = null;
        tbVICEPRESIDENCIAList = null;
        
    }

    public TbVICEPRESIDENCIAEntity getTbVICEPRESIDENCIA() {
        if (this.tbVICEPRESIDENCIA == null) {
            prepareNewTbVICEPRESIDENCIA();
        }
        return this.tbVICEPRESIDENCIA;
    }
    
    public void setTbVICEPRESIDENCIA(TbVICEPRESIDENCIAEntity tbVICEPRESIDENCIA) {
        this.tbVICEPRESIDENCIA = tbVICEPRESIDENCIA;
    }
    
    public List<TbVICEPRESIDENCIAEntity> getTbVICEPRESIDENCIAList() {
        if (tbVICEPRESIDENCIAList == null) {
            tbVICEPRESIDENCIAList = tbVICEPRESIDENCIAService.findAllTbVICEPRESIDENCIAEntities();
        }
        return tbVICEPRESIDENCIAList;
    }

    public void setTbVICEPRESIDENCIAList(List<TbVICEPRESIDENCIAEntity> tbVICEPRESIDENCIAList) {
        this.tbVICEPRESIDENCIAList = tbVICEPRESIDENCIAList;
    }
    
    public boolean isPermitted(String permission) {
        return SecurityWrapper.isPermitted(permission);
    }

    public boolean isPermitted(TbVICEPRESIDENCIAEntity tbVICEPRESIDENCIA, String permission) {
        
        return SecurityWrapper.isPermitted(permission);
        
    }
    
}
