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
import org.applicationn.simtrilhas.domain.TbCOMPETENCIASCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbCOMPETENCIASEntity;
import org.applicationn.simtrilhas.service.TbCARGOSService;
import org.applicationn.simtrilhas.service.TbCOMPETENCIASCARGOSService;
import org.applicationn.simtrilhas.service.TbCOMPETENCIASService;
import org.applicationn.simtrilhas.service.security.SecurityWrapper;
import org.applicationn.simtrilhas.web.util.MessageFactory;
//import org.primefaces.context.RequestContext;

@Named("tbCOMPETENCIASCARGOSBean")
@ViewScoped
public class TbCOMPETENCIASCARGOSBean extends TbCARGOSBean  implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(TbCOMPETENCIASCARGOSBean.class.getName());
    
    private List<TbCOMPETENCIASCARGOSEntity> tbCOMPETENCIASCARGOSList;

    private TbCOMPETENCIASCARGOSEntity tbCOMPETENCIASCARGOS;
    
    @Inject
    private TbCOMPETENCIASCARGOSService tbCOMPETENCIASCARGOSService;
    
    @Inject
    private TbCOMPETENCIASService tbCOMPETENCIASService;
    
    @Inject
    private TbCARGOSService tbCARGOSService;
    
    private List<TbCOMPETENCIASEntity> allIdCOMPETENCIASsList;
    
    private List<TbCARGOSEntity> allIdCARGOSsList;
    
    private List<TbCARGOSEntity> ListaCargos;
    
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
    
    
    public void prepareNewTbCOMPETENCIASCARGOS(TbCARGOSEntity tbCARGOS) {
        reset();
        changeHeaderCadastrar();
        
        this.tbCOMPETENCIASCARGOS = new TbCOMPETENCIASCARGOSEntity();
        this.tbCOMPETENCIASCARGOS.setIdCARGOS(tbCARGOS);
        filtraListas(tbCARGOS); 
    }
    
    
    
    
    public String preencherCompetenciasZeros(TbCARGOSEntity tbCARGOS) {
    	String message ="";
    	filtraListas(tbCARGOS);
    	
    	for(TbCOMPETENCIASEntity listaComp: allIdCOMPETENCIASsList ) {
    		
    		TbCOMPETENCIASCARGOSEntity cargos = new TbCOMPETENCIASCARGOSEntity();
    		
    		cargos.setIdCARGOS(tbCARGOS);
    		cargos.setIdCOMPETENCIAS(listaComp);
    		cargos.setPoNTUACAOCOMPETENCIA(0);
    		
    		tbCOMPETENCIASCARGOSService.save(cargos);
    	
    		
    		
    	}
    	message = "message_successfully_created";
    	 FacesMessage facesMessage = MessageFactory.getMessage(message);
         FacesContext.getCurrentInstance().addMessage(null, facesMessage);
    	return null;
        
    }
    
    
    public String DeletarCompetencias(TbCARGOSEntity tbCARGOS) {
    	String message ="";
    	tbCOMPETENCIASCARGOSs = InicializaTabelasAuxiliaresCO(tbCARGOS);
    	
    	for(TbCOMPETENCIASCARGOSEntity cargos: tbCOMPETENCIASCARGOSs ) {
    		
    		tbCOMPETENCIASCARGOSService.delete(cargos);
    		
    	}
    	message = "message_successfully_deleted";
    	 FacesMessage facesMessage = MessageFactory.getMessage(message);
         FacesContext.getCurrentInstance().addMessage(null, facesMessage);
    	return null;
        
    }
    
    
    public void filtraListas(TbCARGOSEntity tbCARGOS) {

    	tbCOMPETENCIASCARGOSs = InicializaTabelasAuxiliaresCO(tbCARGOS);
		allIdCOMPETENCIASsList= tbCOMPETENCIASService.findAllTbCOMPETENCIASEntities();
		for(int i=0;i<this.getTbCOMPETENCIASCARGOSs().size();i++) {
			for(int j=0;j<allIdCOMPETENCIASsList.size();j++) {
				if(tbCOMPETENCIASCARGOSs.get(i).getIdCOMPETENCIAS().getDeSCCOMPETENCIA().
						equals(allIdCOMPETENCIASsList.get(j).getDeSCCOMPETENCIA())) {
					allIdCOMPETENCIASsList.remove(allIdCOMPETENCIASsList.get(j));
				}
			}
		}
		
		allIdCARGOSsList = tbCARGOSService.findAllTbCARGOSEntities();

			for(int j2=0;j2<allIdCARGOSsList.size();j2++) {
				if(!tbCARGOS.getId().equals(allIdCARGOSsList.get(j2).getId())) {
					allIdCARGOSsList.remove(allIdCARGOSsList.remove(j2));
				}
			
		}
		if(allIdCOMPETENCIASsList.size()==0) {
			FacesContext.getCurrentInstance().validationFailed();
			FacesMessage message = MessageFactory.getMessage("label_listaCultvazia");
			message.setDetail(MessageFactory.getMessageString("label_listaCOvazia_message" ));
			//RequestContext.getCurrentInstance().showMessageInDialog(message);
			return;
		
		}
	}
    
    public String persist(TbCOMPETENCIASCARGOSEntity tbCOMPETENCIASCARGOS) {

        String message;
        
        try {
            
            if (tbCOMPETENCIASCARGOS.getId() != null) {
                tbCOMPETENCIASCARGOS = tbCOMPETENCIASCARGOSService.update(tbCOMPETENCIASCARGOS);
                message = "message_successfully_updated";
            } else {
                tbCOMPETENCIASCARGOS = tbCOMPETENCIASCARGOSService.save(tbCOMPETENCIASCARGOS);
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
        
        tbCOMPETENCIASCARGOSList = null;

        FacesMessage facesMessage = MessageFactory.getMessage(message);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        
        return null;
    }
    

    public String persist() {

        String message;
        
        try {
            
            if (tbCOMPETENCIASCARGOS.getId() != null) {
                tbCOMPETENCIASCARGOS = tbCOMPETENCIASCARGOSService.update(tbCOMPETENCIASCARGOS);
                message = "message_successfully_updated";
            } else {
                tbCOMPETENCIASCARGOS = tbCOMPETENCIASCARGOSService.save(tbCOMPETENCIASCARGOS);
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
        
        tbCOMPETENCIASCARGOSList = null;

        FacesMessage facesMessage = MessageFactory.getMessage(message);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        
        return null;
    }
    
    public String delete() {
        
        String message;
        
        try {
            tbCOMPETENCIASCARGOSService.delete(tbCOMPETENCIASCARGOS);
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
    
    public void onDialogOpen(TbCOMPETENCIASCARGOSEntity tbCOMPETENCIASCARGOS) {
        reset();
        changeHeaderEditar();
        this.tbCOMPETENCIASCARGOS = tbCOMPETENCIASCARGOS;
    }
    
    public void reset() {
        tbCOMPETENCIASCARGOS = null;
        tbCOMPETENCIASCARGOSList = null;
        
        allIdCOMPETENCIASsList = null;
        
        allIdCARGOSsList = null;
        
    }

    // Get a List of all idCOMPETENCIAS
    public List<TbCOMPETENCIASEntity> getIdCOMPETENCIASs() {
        if (this.allIdCOMPETENCIASsList == null) {
            this.allIdCOMPETENCIASsList = tbCOMPETENCIASService.findAllTbCOMPETENCIASEntities();
        }
        return this.allIdCOMPETENCIASsList;
    }
    
    // Update idCOMPETENCIAS of the current tbCOMPETENCIASCARGOS
    public void updateIdCOMPETENCIAS(TbCOMPETENCIASEntity tbCOMPETENCIAS) {
        this.tbCOMPETENCIASCARGOS.setIdCOMPETENCIAS(tbCOMPETENCIAS);
        // Maybe we just created and assigned a new tbCOMPETENCIAS. So reset the allIdCOMPETENCIASList.
        allIdCOMPETENCIASsList = null;
    }
    
    // Get a List of all idCARGOS
    public List<TbCARGOSEntity> getIdCARGOSs() {
        if (this.allIdCARGOSsList == null) {
            this.allIdCARGOSsList = tbCARGOSService.findAllTbCARGOSEntities();
        }
        return this.allIdCARGOSsList;
    }
    
    // Update idCARGOS of the current tbCOMPETENCIASCARGOS
    public void updateIdCARGOS(TbCARGOSEntity tbCARGOS) {
        this.tbCOMPETENCIASCARGOS.setIdCARGOS(tbCARGOS);
        // Maybe we just created and assigned a new tbCARGOS. So reset the allIdCARGOSList.
        allIdCARGOSsList = null;
    }
    
    public TbCOMPETENCIASCARGOSEntity getTbCOMPETENCIASCARGOS() {
        return this.tbCOMPETENCIASCARGOS;
    }
    
    public void setTbCOMPETENCIASCARGOS(TbCOMPETENCIASCARGOSEntity tbCOMPETENCIASCARGOS) {
        this.tbCOMPETENCIASCARGOS = tbCOMPETENCIASCARGOS;
    }
    
    public List<TbCOMPETENCIASCARGOSEntity> getTbCOMPETENCIASCARGOSList() {
        if (tbCOMPETENCIASCARGOSList == null) {
            tbCOMPETENCIASCARGOSList = tbCOMPETENCIASCARGOSService.findAllTbCOMPETENCIASCARGOSEntities();
        }
        return tbCOMPETENCIASCARGOSList;
    }

    public void setTbCOMPETENCIASCARGOSList(List<TbCOMPETENCIASCARGOSEntity> tbCOMPETENCIASCARGOSList) {
        this.tbCOMPETENCIASCARGOSList = tbCOMPETENCIASCARGOSList;
    }
    
    public boolean isPermitted(String permission) {
        return SecurityWrapper.isPermitted(permission);
    }

    public boolean isPermitted(TbCOMPETENCIASCARGOSEntity tbCOMPETENCIASCARGOS, String permission) {
        
        return SecurityWrapper.isPermitted(permission);
        
    }

	public List<TbCARGOSEntity> getListaCargos() {
		return ListaCargos;
	}

	public void setListaCargos(List<TbCARGOSEntity> listaCargos) {
		ListaCargos = listaCargos;
	}
    
}
