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
import org.applicationn.simtrilhas.domain.TbCONHECIMENTOSBASCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbCONHECIMENTOSBASICOSEntity;
import org.applicationn.simtrilhas.domain.TbCONHECIMENTOSESPCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbCONHECIMENTOSESPECIFICOSEntity;
import org.applicationn.simtrilhas.service.TbCARGOSService;
import org.applicationn.simtrilhas.service.TbCONHECIMENTOSESPCARGOSService;
import org.applicationn.simtrilhas.service.TbCONHECIMENTOSESPECIFICOSService;
import org.applicationn.simtrilhas.service.security.SecurityWrapper;
import org.applicationn.simtrilhas.web.util.MessageFactory;
import org.primefaces.PrimeFaces;
import org.primefaces.context.PrimeFacesContext;

@Named("tbCONHECIMENTOSESPCARGOSBean")
@ViewScoped
public class TbCONHECIMENTOSESPCARGOSBean extends TbCARGOSBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(TbCONHECIMENTOSESPCARGOSBean.class.getName());
    
    private List<TbCONHECIMENTOSESPCARGOSEntity> tbCONHECIMENTOSESPCARGOSList;

    private TbCONHECIMENTOSESPCARGOSEntity tbCONHECIMENTOSESPCARGOS;
    
    @Inject
    private TbCONHECIMENTOSESPCARGOSService tbCONHECIMENTOSESPCARGOSService;
    
    @Inject
    private TbCARGOSService tbCARGOSService;
    
    @Inject
    private TbCONHECIMENTOSESPECIFICOSService tbCONHECIMENTOSESPECIFICOSService;
    
    private List<TbCARGOSEntity> allIdCARGOSsList;
    
    private List<TbCONHECIMENTOSESPECIFICOSEntity> allIdCONHECESPsList;
    

    private String dialogHeader;
    
    
 	public void setDialogHeader(final String dialogHeader) { 
   		this.dialogHeader = dialogHeader;
   	}

   	public String getDialogHeader() {
   		return dialogHeader;
   	}

   	public void changeHeaderCadastrar() {
   		setDialogHeader("Cadastrar Conhecimentos Específicos");
   	}

   	public void changeHeaderEditar() {
   		setDialogHeader("Editar Conhecimentos Específicos");
   	}
    
    
    
    public void prepareNewTbCONHECIMENTOSESPCARGOS(TbCARGOSEntity tbCARGOS) {
        reset();
        changeHeaderCadastrar();
        this.tbCONHECIMENTOSESPCARGOS = new TbCONHECIMENTOSESPCARGOSEntity();
        this.tbCONHECIMENTOSESPCARGOS.setIdCARGOS(tbCARGOS);
        filtraListas(tbCARGOS);
    }
    
    
    public String preencherCBZeros(TbCARGOSEntity tbCARGOS) {
    	String message ="";
    	filtraListas(tbCARGOS);
    	
    	for(TbCONHECIMENTOSESPECIFICOSEntity listaComp: allIdCONHECESPsList ) {
    		
    		TbCONHECIMENTOSESPCARGOSEntity cargos = new TbCONHECIMENTOSESPCARGOSEntity();
    		
    		cargos.setIdCARGOS(tbCARGOS);
    		cargos.setIdCONHECESP(listaComp);
    		cargos.setPoNTUACAOCONESP(0.0);
    		
    		tbCONHECIMENTOSESPCARGOSService.save(cargos);
    	
    		
    		
    	}
    	message = "message_successfully_created";
    	 FacesMessage facesMessage = MessageFactory.getMessage(message);
         FacesContext.getCurrentInstance().addMessage(null, facesMessage);
    	return null;
        
    }
    
    
    public String DeletarCB(TbCARGOSEntity tbCARGOS) {
    	String message ="";
    	tbCONHECIMENTOSESPCARGOSs = InicializaTabelasAuxiliaresCE(tbCARGOS);
    	
    	for(TbCONHECIMENTOSESPCARGOSEntity cargos: tbCONHECIMENTOSESPCARGOSs ) {
    		
    		tbCONHECIMENTOSESPCARGOSService.delete(cargos);
    		
    	}
    	message = "message_successfully_deleted";
    	 FacesMessage facesMessage = MessageFactory.getMessage(message);
         FacesContext.getCurrentInstance().addMessage(null, facesMessage);
    	return null;
        
    }
    
    public void onDialogOpen(TbCONHECIMENTOSESPCARGOSEntity tbCONHECIMENTOSESPCARGOS) {
        reset();
        changeHeaderEditar();
        this.tbCONHECIMENTOSESPCARGOS = tbCONHECIMENTOSESPCARGOS;
        
    }
    
    public void filtraListas(TbCARGOSEntity tbCARGOS) {

    	tbCONHECIMENTOSESPCARGOSs = InicializaTabelasAuxiliaresCE(tbCARGOS);
		allIdCONHECESPsList= tbCONHECIMENTOSESPECIFICOSService.findAllTbCONHECIMENTOSESPECIFICOSEntitiesAlfa();
		for(int i=0;i<this.getTbCONHECIMENTOSESPCARGOSs().size();i++) {
			for(int j=0;j<allIdCONHECESPsList.size();j++) {
				if(tbCONHECIMENTOSESPCARGOSs.get(i).getIdCONHECESP().getDeSCCONHECIMENTOSESPECIFICOS().
						equals(allIdCONHECESPsList.get(j).getDeSCCONHECIMENTOSESPECIFICOS())) {
					allIdCONHECESPsList.remove(allIdCONHECESPsList.get(j));
				}
			}
		}
		
		allIdCARGOSsList = tbCARGOSService.findAllTbCARGOSEntities();

			for(int j2=0;j2<allIdCARGOSsList.size();j2++) {
				if(!tbCARGOS.getId().equals(allIdCARGOSsList.get(j2).getId())) {
					allIdCARGOSsList.remove(allIdCARGOSsList.remove(j2));
				}
			
		}
		if(allIdCONHECESPsList.size()==0) {
			FacesContext.getCurrentInstance().validationFailed();
			FacesMessage message = MessageFactory.getMessage("label_listaCultvazia");
			message.setDetail(MessageFactory.getMessageString("label_listaCEvazia_message" ));
			
			
			PrimeFaces.current().dialog().showMessageDynamic(message);
			return;
		
		}
	}

    
    public String persist() {

        String message;
        
        try {
            
            if (tbCONHECIMENTOSESPCARGOS.getId() != null) {
                tbCONHECIMENTOSESPCARGOS = tbCONHECIMENTOSESPCARGOSService.update(tbCONHECIMENTOSESPCARGOS);
                message = "message_successfully_updated";
            } else {
                tbCONHECIMENTOSESPCARGOS = tbCONHECIMENTOSESPCARGOSService.save(tbCONHECIMENTOSESPCARGOS);
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
        
        tbCONHECIMENTOSESPCARGOSList = null;

        FacesMessage facesMessage = MessageFactory.getMessage(message);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        
        return null;
    }
    
    public String delete() {
        
        String message;
        
        try {
            tbCONHECIMENTOSESPCARGOSService.delete(tbCONHECIMENTOSESPCARGOS);
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
        tbCONHECIMENTOSESPCARGOS = null;
        tbCONHECIMENTOSESPCARGOSList = null;
        
        allIdCARGOSsList = null;
        
        allIdCONHECESPsList = null;
        
    }

    // Get a List of all idCARGOS
    public List<TbCARGOSEntity> getIdCARGOSs() {
        if (this.allIdCARGOSsList == null) {
            this.allIdCARGOSsList = tbCARGOSService.findAllTbCARGOSEntities();
        }
        return this.allIdCARGOSsList;
    }
    
    // Update idCARGOS of the current tbCONHECIMENTOSESPCARGOS
    public void updateIdCARGOS(TbCARGOSEntity tbCARGOS) {
        this.tbCONHECIMENTOSESPCARGOS.setIdCARGOS(tbCARGOS);
        // Maybe we just created and assigned a new tbCARGOS. So reset the allIdCARGOSList.
        allIdCARGOSsList = null;
    }
    
    // Get a List of all idCONHECESP
    public List<TbCONHECIMENTOSESPECIFICOSEntity> getIdCONHECESPs() {
        if (this.allIdCONHECESPsList == null) {
            this.allIdCONHECESPsList = tbCONHECIMENTOSESPECIFICOSService.findAllTbCONHECIMENTOSESPECIFICOSEntitiesAlfa();
        }
        return this.allIdCONHECESPsList;
    }
    
    // Update idCONHECESP of the current tbCONHECIMENTOSESPCARGOS
    public void updateIdCONHECESP(TbCONHECIMENTOSESPECIFICOSEntity tbCONHECIMENTOSESPECIFICOS) {
        this.tbCONHECIMENTOSESPCARGOS.setIdCONHECESP(tbCONHECIMENTOSESPECIFICOS);
        // Maybe we just created and assigned a new tbCONHECIMENTOSESPECIFICOS. So reset the allIdCONHECESPList.
        allIdCONHECESPsList = null;
    }
    
    public TbCONHECIMENTOSESPCARGOSEntity getTbCONHECIMENTOSESPCARGOS() {
        return this.tbCONHECIMENTOSESPCARGOS;
    }
    
    public void setTbCONHECIMENTOSESPCARGOS(TbCONHECIMENTOSESPCARGOSEntity tbCONHECIMENTOSESPCARGOS) {
        this.tbCONHECIMENTOSESPCARGOS = tbCONHECIMENTOSESPCARGOS;
    }
    
    public List<TbCONHECIMENTOSESPCARGOSEntity> getTbCONHECIMENTOSESPCARGOSList() {
        if (tbCONHECIMENTOSESPCARGOSList == null) {
            tbCONHECIMENTOSESPCARGOSList = tbCONHECIMENTOSESPCARGOSService.findAllTbCONHECIMENTOSESPCARGOSEntities();
        }
        return tbCONHECIMENTOSESPCARGOSList;
    }

    public void setTbCONHECIMENTOSESPCARGOSList(List<TbCONHECIMENTOSESPCARGOSEntity> tbCONHECIMENTOSESPCARGOSList) {
        this.tbCONHECIMENTOSESPCARGOSList = tbCONHECIMENTOSESPCARGOSList;
    }
    
    public boolean isPermitted(String permission) {
        return SecurityWrapper.isPermitted(permission);
    }

    public boolean isPermitted(TbCONHECIMENTOSESPCARGOSEntity tbCONHECIMENTOSESPCARGOS, String permission) {
        
        return SecurityWrapper.isPermitted(permission);
        
    }
    
}
