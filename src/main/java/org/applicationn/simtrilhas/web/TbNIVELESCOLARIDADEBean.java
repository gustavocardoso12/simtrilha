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

import org.applicationn.simtrilhas.domain.TbNivelEscolaridadeEntity;
import org.applicationn.simtrilhas.service.TbNIVELESCOLARIDADEService;
import org.applicationn.simtrilhas.service.security.SecurityWrapper;
import org.applicationn.simtrilhas.web.util.MessageFactory;

@Named("tbNEBean")
@ViewScoped
public class TbNIVELESCOLARIDADEBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(TbNIVELESCOLARIDADEBean.class.getName());
    
    private List<TbNivelEscolaridadeEntity> tbNEList;
    
    private List<TbNivelEscolaridadeEntity> tbNEListGrupo2;
    
    private List<TbNivelEscolaridadeEntity> tbNEListGrupo3;
    
    private List<TbNivelEscolaridadeEntity> tbNEListGrupo4;
    
    private List<TbNivelEscolaridadeEntity> tbNEListGrupo5;
    
    private List<TbNivelEscolaridadeEntity> tbNEListGrupo6;

    private TbNivelEscolaridadeEntity tbNE;
    
    @Inject
    private TbNIVELESCOLARIDADEService tbNEService;
    
    private String dialogHeader;

    public void setDialogHeader(final String dialogHeader) { 
        this.dialogHeader = dialogHeader;
    }

    public String getDialogHeader() {
        return dialogHeader;
    }

    public void changeHeaderCadastrar() {
        setDialogHeader("Cadastrar");
    }
    
    public void changeHeaderEditar() {
        setDialogHeader("Editar");
    }
    
    
    public void prepareNewtbNE() {
        reset();
        changeHeaderCadastrar();
        this.tbNE = new TbNivelEscolaridadeEntity();
        // set any default values now, if you need
        // Example: this.tbNE.setAnything("test");
    }

    public String persist() {

        if (tbNE.getId() == null && !isPermitted("tbNO:create")) {
            return "accessDenied";
        } else if (tbNE.getId() != null && !isPermitted(tbNE, "tbNO:update")) {
            return "accessDenied";
        }
        
        String message;
        
        try {
        	   tbNE.setGrupo(1);
            if (tbNE.getId() != null) {
                tbNE = tbNEService.update(tbNE);
                message = "message_successfully_updated";
            } else {
                tbNE = tbNEService.save(tbNE);
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
        
        tbNEList = null;
        tbNEListGrupo2 = null;
        tbNEListGrupo3 = null;
        tbNEListGrupo4 = null;
        tbNEListGrupo5 = null;
        tbNEListGrupo6 = null;
        FacesMessage facesMessage = MessageFactory.getMessage(message);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        
        return null;
    }
    
    public String persistGRP2() {

        if (tbNE.getId() == null && !isPermitted("tbNO:create")) {
            return "accessDenied";
        } else if (tbNE.getId() != null && !isPermitted(tbNE, "tbNO:update")) {
            return "accessDenied";
        }
        
        String message;
        
        try {
        	   tbNE.setGrupo(2);
            if (tbNE.getId() != null) {
                tbNE = tbNEService.update(tbNE);
                message = "message_successfully_updated";
            } else {
                tbNE = tbNEService.save(tbNE);
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
        
        tbNEList = null;
        tbNEListGrupo2 = null;
        tbNEListGrupo3 = null;
        tbNEListGrupo4 = null;
        tbNEListGrupo5 = null;
        tbNEListGrupo6 = null;
        FacesMessage facesMessage = MessageFactory.getMessage(message);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        
        return null;
    }
    
    public String persistGRP3() {

        if (tbNE.getId() == null && !isPermitted("tbNO:create")) {
            return "accessDenied";
        } else if (tbNE.getId() != null && !isPermitted(tbNE, "tbNO:update")) {
            return "accessDenied";
        }
        
        String message;
        
        try {
            tbNE.setGrupo(3);
            if (tbNE.getId() != null) {
                tbNE = tbNEService.update(tbNE);
                message = "message_successfully_updated";
            } else {
                tbNE = tbNEService.save(tbNE);
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
        
        tbNEList = null;
        tbNEListGrupo2 = null;
        tbNEListGrupo3 = null;
        tbNEListGrupo4 = null;
        tbNEListGrupo5 = null;
        tbNEListGrupo6 = null;
        FacesMessage facesMessage = MessageFactory.getMessage(message);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        
        return null;
    }
    
    public String persistGRP4() {

        if (tbNE.getId() == null && !isPermitted("tbNO:create")) {
            return "accessDenied";
        } else if (tbNE.getId() != null && !isPermitted(tbNE, "tbNO:update")) {
            return "accessDenied";
        }
        
        String message;
        
        try {
            tbNE.setGrupo(4);
            if (tbNE.getId() != null) {
                tbNE = tbNEService.update(tbNE);
                message = "message_successfully_updated";
            } else {
                tbNE = tbNEService.save(tbNE);
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
        
        tbNEList = null;
        tbNEListGrupo2 = null;
        tbNEListGrupo3 = null;
        tbNEListGrupo4 = null;
        tbNEListGrupo5 = null;
        tbNEListGrupo6 = null;
        FacesMessage facesMessage = MessageFactory.getMessage(message);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        
        return null;
    }
    
    
    public String persistGRP5() {

        if (tbNE.getId() == null && !isPermitted("tbNO:create")) {
            return "accessDenied";
        } else if (tbNE.getId() != null && !isPermitted(tbNE, "tbNO:update")) {
            return "accessDenied";
        }
        
        String message;
        
        try {
            tbNE.setGrupo(5);
            if (tbNE.getId() != null) {
                tbNE = tbNEService.update(tbNE);
                message = "message_successfully_updated";
            } else {
                tbNE = tbNEService.save(tbNE);
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
        
        tbNEList = null;
        tbNEListGrupo2 = null;
        tbNEListGrupo3 = null;
        tbNEListGrupo4 = null;
        tbNEListGrupo5 = null;
        tbNEListGrupo6 = null;
        FacesMessage facesMessage = MessageFactory.getMessage(message);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        
        return null;
    }
    
    
    public String persistGRP6() {

        if (tbNE.getId() == null && !isPermitted("tbNO:create")) {
            return "accessDenied";
        } else if (tbNE.getId() != null && !isPermitted(tbNE, "tbNO:update")) {
            return "accessDenied";
        }
        
        String message;
        
        try {
            tbNE.setGrupo(6);
            if (tbNE.getId() != null) {
                tbNE = tbNEService.update(tbNE);
                message = "message_successfully_updated";
            } else {
                tbNE = tbNEService.save(tbNE);
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
        
        tbNEList = null;
        tbNEListGrupo2 = null;
        tbNEListGrupo3 = null;
        tbNEListGrupo4 = null;
        tbNEListGrupo5 = null;
        tbNEListGrupo6 = null;
        FacesMessage facesMessage = MessageFactory.getMessage(message);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        
        return null;
    }
    
    
    public String delete() {
        
        if (!isPermitted(tbNE, "tbNO:delete")) {
            return "accessDenied";
        }
        
        String message;
        
        try {
            tbNEService.delete(tbNE);
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
    
    public void onDialogOpen(TbNivelEscolaridadeEntity tbNE) {
        reset();
        changeHeaderEditar();
        this.tbNE = tbNE;
    }
    
    public void reset() {
        tbNE = null;
        tbNEList = null;
        tbNEListGrupo2 = null;
        tbNEListGrupo3 = null;
        tbNEListGrupo4 = null;
        tbNEListGrupo5 = null;
        tbNEListGrupo6 = null;
    }

    public TbNivelEscolaridadeEntity gettbNE() {
        if (this.tbNE == null) {
            prepareNewtbNE();
        }
        return this.tbNE;
    }
    
    public void settbNE(TbNivelEscolaridadeEntity tbNE) {
        this.tbNE = tbNE;
    }
    
    public List<TbNivelEscolaridadeEntity> gettbNEList() {
        if (tbNEList == null) {
            tbNEList = tbNEService.findAlltbNIVELESCOLARIDADEEntitiesGrupo1();
        }
        return tbNEList;
    }

    public void settbNEList(List<TbNivelEscolaridadeEntity> tbNEList) {
        this.tbNEList = tbNEList;
    }
    
    
    
   
    public List<TbNivelEscolaridadeEntity> getTbNEListGrupo2() {
    	 if (tbNEListGrupo2 == null) {
         	tbNEListGrupo2 = tbNEService.findAlltbNIVELESCOLARIDADEEntitiesGrupo2();
         }
		return tbNEListGrupo2;
	}
    
	

	public void setTbNEListGrupo2(List<TbNivelEscolaridadeEntity> tbNEListGrupo2) {
		this.tbNEListGrupo2 = tbNEListGrupo2;
	}
	
	public List<TbNivelEscolaridadeEntity> getTbNEListGrupo3() {
		 if (tbNEListGrupo3 == null) {
	         	tbNEListGrupo3 = tbNEService.findAlltbNIVELESCOLARIDADEEntitiesGrupo3();
	         }
		return tbNEListGrupo3;
	}

	public void setTbNEListGrupo3(List<TbNivelEscolaridadeEntity> tbNEListGrupo3) {
		this.tbNEListGrupo3 = tbNEListGrupo3;
	}

	public List<TbNivelEscolaridadeEntity> getTbNEListGrupo4() {
		 if (tbNEListGrupo4 == null) {
			 tbNEListGrupo4 = tbNEService.findAlltbNIVELESCOLARIDADEEntitiesGrupo4();
	         }
		return tbNEListGrupo4;
	}

	public void setTbNEListGrupo4(List<TbNivelEscolaridadeEntity> tbNEListGrupo4) {
		this.tbNEListGrupo4 = tbNEListGrupo4;
	}

	public List<TbNivelEscolaridadeEntity> getTbNEListGrupo5() {
		 if (tbNEListGrupo5 == null) {
			 tbNEListGrupo5 = tbNEService.findAlltbNIVELESCOLARIDADEEntitiesGrupo5();
	         }
		return tbNEListGrupo5;
	}

	public void setTbNEListGrupo5(List<TbNivelEscolaridadeEntity> tbNEListGrupo5) {
		this.tbNEListGrupo5 = tbNEListGrupo5;
	}

	public List<TbNivelEscolaridadeEntity> getTbNEListGrupo6() {
		 if (tbNEListGrupo6 == null) {
			 tbNEListGrupo6 = tbNEService.findAlltbNIVELESCOLARIDADEEntitiesGrupo6();
	         }
		return tbNEListGrupo6;
	}

	public void setTbNEListGrupo6(List<TbNivelEscolaridadeEntity> tbNEListGrupo6) {
		this.tbNEListGrupo6 = tbNEListGrupo6;
	}

	public boolean isPermitted(String permission) {
        return SecurityWrapper.isPermitted(permission);
    }

    public boolean isPermitted(TbNivelEscolaridadeEntity tbNE, String permission) {
        
        return SecurityWrapper.isPermitted(permission);
        
    }


    
}
