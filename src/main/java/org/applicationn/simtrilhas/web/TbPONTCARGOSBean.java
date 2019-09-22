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

import org.applicationn.simtrilhas.domain.TbCONHECIMENTOSESPECIFICOSEntity;
import org.applicationn.simtrilhas.domain.TbPONTCARGOSEntity;
import org.applicationn.simtrilhas.service.TbPONTCARGOSService;
import org.applicationn.simtrilhas.service.security.SecurityWrapper;
import org.applicationn.simtrilhas.web.util.MessageFactory;

@Named("tbPONTCARGOSBean")
@ViewScoped
public class TbPONTCARGOSBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(TbPONTCARGOSBean.class.getName());
    
    private List<TbPONTCARGOSEntity> tbPONTCARGOSList;

    private TbPONTCARGOSEntity tbPONTCARGOS;
    
    private int somaPeso = 0;

    private String dialogHeader;
    
    @Inject
    private TbPONTCARGOSService tbPONTCARGOSService;
    
    public void prepareNewTbPONTCARGOS() {
        reset();
        this.tbPONTCARGOS = new TbPONTCARGOSEntity();
    }
    
    public void setDialogHeader(final String dialogHeader) { 
        this.dialogHeader = dialogHeader;
    }

    public String getDialogHeader() {
        return dialogHeader;
    }

    public void changeHeaderCadastrar() {
        setDialogHeader("Cadastrar Pesos da Aderência final");
    }
    
    public void changeHeaderEditar() {
        setDialogHeader("Editar Pesos da Aderência final");
    }
    

    public String persist() {

        if (tbPONTCARGOS.getId() == null && !isPermitted("tbPONTCARGOS:create")) {
            return "accessDenied";
        } else if (tbPONTCARGOS.getId() != null && !isPermitted(tbPONTCARGOS, "tbPONTCARGOS:update")) {
            return "accessDenied";
        }
        
        String message;
        
        try {
        	
        
            if (tbPONTCARGOS.getId() != null) {
            	somaPeso = 0;
            	for (int i=0;i<tbPONTCARGOSList.size();i++) {

            		if(tbPONTCARGOSList.get(i).getDescricaoCompleta().equals(tbPONTCARGOS.getDescricaoCompleta())) {
            			somaPeso+= tbPONTCARGOS.getPeso();
            			tbPONTCARGOSList.get(i).setPeso(tbPONTCARGOS.getPeso());
            		}else {
            			somaPeso+=tbPONTCARGOSList.get(i).getPeso();
            		}
            		
            		
            	}
            	
           	 	message = "";
            	
            	
            	if(somaPeso!=100) {
        			
        			message ="label_aviso_pesos";
        		}else {
        			
        			for (int i=0;i<tbPONTCARGOSList.size();i++) {
        				if(tbPONTCARGOSList.get(i).getDescricaoCompleta().equals(tbPONTCARGOS.getDescricaoCompleta())) {
        					tbPONTCARGOS = tbPONTCARGOSService.update(tbPONTCARGOS);
        				}else {
        					tbPONTCARGOSService.update(tbPONTCARGOSList.get(i));
        				}
        			}
        			
        			 
        			message = "message_successfully_updated";
        		}

            
            } else {
                tbPONTCARGOS = tbPONTCARGOSService.save(tbPONTCARGOS);
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
        
        

        FacesMessage facesMessage = MessageFactory.getMessage(message);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        
        return null;
    }
    
    public String delete() {
        
        if (!isPermitted(tbPONTCARGOS, "tbPONTCARGOS:delete")) {
            return "accessDenied";
        }
        
        String message;
        
        try {
            tbPONTCARGOSService.delete(tbPONTCARGOS);
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
    
    public void onDialogOpen(TbPONTCARGOSEntity tbPONTCARGOS) {
        reset();
        changeHeaderEditar();
        this.tbPONTCARGOS = tbPONTCARGOS;
    }
    
    public void reset() {
        
    }

    public TbPONTCARGOSEntity getTbPONTCARGOS() {
        // Need to check for null, because some strange behaviour of f:viewParam
                // Sometimes it is setting to null
        if (this.tbPONTCARGOS == null) {
            prepareNewTbPONTCARGOS();
        }
        return this.tbPONTCARGOS;
    }
    
    public void setTbPONTCARGOS(TbPONTCARGOSEntity tbPONTCARGOS) {
            if (tbPONTCARGOS != null) {
        this.tbPONTCARGOS = tbPONTCARGOS;
            }
    }
    
    public List<TbPONTCARGOSEntity> getTbPONTCARGOSList() {
        if (tbPONTCARGOSList == null) {
            tbPONTCARGOSList = tbPONTCARGOSService.findAllTbPONTCARGOSEntities();
            
            somaPeso = 0;
            
            for (TbPONTCARGOSEntity listaPesos :  tbPONTCARGOSList) {
            		
            	somaPeso+=listaPesos.getPeso();
    		
    		}
        }
        return tbPONTCARGOSList;
    }

    public void setTbPONTCARGOSList(List<TbPONTCARGOSEntity> tbPONTCARGOSList) {
        this.tbPONTCARGOSList = tbPONTCARGOSList;
    }
    
    public boolean isPermitted(String permission) {
        return SecurityWrapper.isPermitted(permission);
    }

    public int getSomaPeso() {
    	
		return somaPeso;
	}

	public void setSomaPeso(int somaPeso) {
		this.somaPeso = somaPeso;
	}

	public boolean isPermitted(TbPONTCARGOSEntity tbPONTCARGOS, String permission) {
        
        return SecurityWrapper.isPermitted(permission);
        
    }
    
}
