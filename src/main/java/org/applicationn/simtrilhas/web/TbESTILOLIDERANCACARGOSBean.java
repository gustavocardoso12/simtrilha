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

import org.applicationn.simtrilhas.domain.TbCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbESTILOLIDERANCACARGOSEntity;
import org.applicationn.simtrilhas.domain.TbESTILOLIDERANCAEntity;
import org.applicationn.simtrilhas.service.TbCARGOSService;
import org.applicationn.simtrilhas.service.TbESTILOLIDERANCACARGOSService;
import org.applicationn.simtrilhas.service.TbESTILOLIDERANCAService;
import org.applicationn.simtrilhas.service.security.SecurityWrapper;
import org.applicationn.simtrilhas.web.util.MessageFactory;
import org.primefaces.PrimeFaces;

@Named("tbESTILOLIDERANCACARGOSBean")
@ViewScoped
public class TbESTILOLIDERANCACARGOSBean extends TbCARGOSBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(TbESTILOLIDERANCACARGOSBean.class.getName());
    
    private List<TbESTILOLIDERANCACARGOSEntity> tbESTILOLIDERANCACARGOSList;

    private TbESTILOLIDERANCACARGOSEntity tbESTILOLIDERANCACARGOS;
    
    @Inject
    private TbESTILOLIDERANCACARGOSService tbESTILOLIDERANCACARGOSService;
    
    @Inject
    private TbCARGOSService tbCARGOSService;
    
    @Inject
    private TbESTILOLIDERANCAService tbESTILOLIDERANCAService;
    
    private List<TbCARGOSEntity> allIdCARGOSsList;
    
    private List<TbESTILOLIDERANCAEntity> allIdESTLIDERsList;
    
    
    private String dialogHeader;
    
    

	public void setDialogHeader(final String dialogHeader) { 
		this.dialogHeader = dialogHeader;
	}

	public String getDialogHeader() {
		return dialogHeader;
	}

	public void changeHeaderCadastrar() {
		setDialogHeader("Cadastrar Estilo de liderança");
	}

	public void changeHeaderEditar() {
		setDialogHeader("Editar Estilo de liderança");
	}
    
    public void prepareNewTbESTILOLIDERANCACARGOS(TbCARGOSEntity tbCARGOS) {
        this.tbESTILOLIDERANCACARGOS = new TbESTILOLIDERANCACARGOSEntity();
        changeHeaderCadastrar();
        filtraListas(tbCARGOS);
    }
    
    
    
    public void filtraListas(TbCARGOSEntity tbCARGOS) {

    	
		allIdESTLIDERsList= tbESTILOLIDERANCAService.findAllTbESTILOLIDERANCAEntities();
		for(int i=0;i<this.getTbESTILOLIDERANCAs().size();i++) {
			for(int j=0;j<allIdESTLIDERsList.size();j++) {
				if(tbESTILOLIDERANCAs.get(i).getIdESTLIDER().getDeSCESTILOLIDER().
						equals(allIdESTLIDERsList.get(j).getDeSCESTILOLIDER())) {
					allIdESTLIDERsList.remove(allIdESTLIDERsList.get(j));
				}
			}
		}
		
		allIdCARGOSsList = tbCARGOSService.findAllTbCARGOSEntities();
		for(int i2 = 0; i2<tbESTILOLIDERANCAs.size();i2++) {
			for(int j2=0;j2<allIdCARGOSsList.size();j2++) {
				if(!tbCARGOS.getId().equals(allIdCARGOSsList.get(j2).getId())) {
					allIdCARGOSsList.remove(allIdCARGOSsList.remove(j2));
				}
			}
		}
		if(allIdESTLIDERsList.size()==0) {
			FacesContext.getCurrentInstance().validationFailed();
			FacesMessage message = MessageFactory.getMessage("label_listaCultvazia");
			message.setDetail(MessageFactory.getMessageString("label_listaHabvazia_message" ));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			return;
		
		}
	}
    
    public void onDialogOpen(TbESTILOLIDERANCACARGOSEntity tbESTILOLIDERANCACARGOS) {
        reset();
        changeHeaderEditar();
        this.tbESTILOLIDERANCACARGOS = tbESTILOLIDERANCACARGOS;
    }

    public String persist() {

        String message;
        
        try {
            
            if (tbESTILOLIDERANCACARGOS.getId() != null) {
                tbESTILOLIDERANCACARGOS = tbESTILOLIDERANCACARGOSService.update(tbESTILOLIDERANCACARGOS);
                message = "message_successfully_updated";
            } else {
                tbESTILOLIDERANCACARGOS = tbESTILOLIDERANCACARGOSService.save(tbESTILOLIDERANCACARGOS);
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
        
        tbESTILOLIDERANCACARGOSList = null;

        FacesMessage facesMessage = MessageFactory.getMessage(message);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        
        return null;
    }
    
    public String delete() {
        
        String message;
        
        try {
            tbESTILOLIDERANCACARGOSService.delete(tbESTILOLIDERANCACARGOS);
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
    
   
    
    public void reset() {
        tbESTILOLIDERANCACARGOS = null;
        tbESTILOLIDERANCACARGOSList = null;
        
        allIdCARGOSsList = null;
        
        allIdESTLIDERsList = null;
        
    }

    // Get a List of all idCARGOS
    public List<TbCARGOSEntity> getIdCARGOSs() {
        if (this.allIdCARGOSsList == null) {
            this.allIdCARGOSsList = tbCARGOSService.findAllTbCARGOSEntities();
        }
        return this.allIdCARGOSsList;
    }
    
    // Update idCARGOS of the current tbESTILOLIDERANCACARGOS
    public void updateIdCARGOS(TbCARGOSEntity tbCARGOS) {
        this.tbESTILOLIDERANCACARGOS.setIdCARGOS(tbCARGOS);
        // Maybe we just created and assigned a new tbCARGOS. So reset the allIdCARGOSList.
        allIdCARGOSsList = null;
    }
    
    // Get a List of all idESTLIDER
    public List<TbESTILOLIDERANCAEntity> getIdESTLIDERs() {
        if (this.allIdESTLIDERsList == null) {
            this.allIdESTLIDERsList = tbESTILOLIDERANCAService.findAllTbESTILOLIDERANCAEntities();
        }
        return this.allIdESTLIDERsList;
    }
    
    // Update idESTLIDER of the current tbESTILOLIDERANCACARGOS
    public void updateIdESTLIDER(TbESTILOLIDERANCAEntity tbESTILOLIDERANCA) {
        this.tbESTILOLIDERANCACARGOS.setIdESTLIDER(tbESTILOLIDERANCA);
        // Maybe we just created and assigned a new tbESTILOLIDERANCA. So reset the allIdESTLIDERList.
        allIdESTLIDERsList = null;
    }
    
    public TbESTILOLIDERANCACARGOSEntity getTbESTILOLIDERANCACARGOS() {
        return this.tbESTILOLIDERANCACARGOS;
    }
    
    public void setTbESTILOLIDERANCACARGOS(TbESTILOLIDERANCACARGOSEntity tbESTILOLIDERANCACARGOS) {
        this.tbESTILOLIDERANCACARGOS = tbESTILOLIDERANCACARGOS;
    }
    
    public List<TbESTILOLIDERANCACARGOSEntity> getTbESTILOLIDERANCACARGOSList() {
        if (tbESTILOLIDERANCACARGOSList == null) {
            tbESTILOLIDERANCACARGOSList = tbESTILOLIDERANCACARGOSService.findAllTbESTILOLIDERANCACARGOSEntities();
        }
        return tbESTILOLIDERANCACARGOSList;
    }

    public void setTbESTILOLIDERANCACARGOSList(List<TbESTILOLIDERANCACARGOSEntity> tbESTILOLIDERANCACARGOSList) {
        this.tbESTILOLIDERANCACARGOSList = tbESTILOLIDERANCACARGOSList;
    }
    
    public boolean isPermitted(String permission) {
        return SecurityWrapper.isPermitted(permission);
    }

    public boolean isPermitted(TbESTILOLIDERANCACARGOSEntity tbESTILOLIDERANCACARGOS, String permission) {
        
        return SecurityWrapper.isPermitted(permission);
        
    }
    
}
