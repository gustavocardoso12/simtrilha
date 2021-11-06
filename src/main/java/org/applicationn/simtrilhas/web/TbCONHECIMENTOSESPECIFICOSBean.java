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


import org.applicationn.simtrilhas.domain.TbCONHECIMENTOSESPECIFICOSEntity;
import org.applicationn.simtrilhas.domain.TbMASCARAEntity;
import org.applicationn.simtrilhas.domain.TbPERFILEntity;
import org.applicationn.simtrilhas.domain.TbPONTCARGOSEntity;
import org.applicationn.simtrilhas.service.TbCONHECIMENTOSESPECIFICOSService;
import org.applicationn.simtrilhas.service.TbMASCARAService;
import org.applicationn.simtrilhas.service.TbPONTCARGOSService;
import org.applicationn.simtrilhas.service.security.SecurityWrapper;
import org.applicationn.simtrilhas.web.util.MessageFactory;
import org.primefaces.event.SlideEndEvent;
@Named("tbCONHECIMENTOSESPECIFICOSBean")
@ViewScoped
public class TbCONHECIMENTOSESPECIFICOSBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(TbCONHECIMENTOSESPECIFICOSBean.class.getName());
    
    private List<TbCONHECIMENTOSESPECIFICOSEntity> tbCONHECIMENTOSESPECIFICOSList;

    private TbCONHECIMENTOSESPECIFICOSEntity tbCONHECIMENTOSESPECIFICOS;
    
    @Inject
    private TbCONHECIMENTOSESPECIFICOSService tbCONHECIMENTOSESPECIFICOSService;

    
    private TbPONTCARGOSEntity tbPONTCARGOSEntity;

	@Inject
	private TbPONTCARGOSService tbPONTCARGOSService;
    

    @Inject
    private TbMASCARAService tbMASCARAService;
    
    private String dialogHeader;
    
    private double pontuacaoOriginal;

	private double gapVarCE;

	private boolean flagBloqueio;

	private boolean flagCustom;

	private boolean flagEdit;
	
	private boolean flagDelete=false;
	
	  private List<TbMASCARAEntity> allIdMASCARAsList;
		
		
		public List<TbMASCARAEntity> getAllIdMASCARAsList() {
			if (this.allIdMASCARAsList == null) {
	            this.allIdMASCARAsList = tbMASCARAService.findAllTbMASCARAEntities();
	        }
			return allIdMASCARAsList;
		}


		public void setAllIdMASCARAsList(List<TbMASCARAEntity> allIdMASCARAsList) {
			this.allIdMASCARAsList = allIdMASCARAsList;
		}

	    
	    public void updateIdMASCARA(TbMASCARAEntity tbMASCARA) {
	        this.tbCONHECIMENTOSESPECIFICOS.setTbMascara(tbMASCARA);
	        allIdMASCARAsList = null;
	    }
	
	
public void onSelect() {
		
	for (TbCONHECIMENTOSESPECIFICOSEntity tbCONHECIMENTOSESPECIFICOSEntity : tbCONHECIMENTOSESPECIFICOSList) {

		if(tbCONHECIMENTOSESPECIFICOSEntity.getConhecEspCustom()==null) {
			tbCONHECIMENTOSESPECIFICOSEntity.setPenalidadeConhecBas(gapVarCE);
			persist(tbCONHECIMENTOSESPECIFICOSEntity);
		}
	}
		
	}
	
	
	

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
    
    
    public void prepareNewTbCONHECIMENTOSESPECIFICOS() {
    	
    	if(this.tbCONHECIMENTOSESPECIFICOS ==null) {
        changeHeaderCadastrar();
        this.tbCONHECIMENTOSESPECIFICOS = new TbCONHECIMENTOSESPECIFICOSEntity();
    	}

    }

    public String persist(TbCONHECIMENTOSESPECIFICOSEntity tbCONHECIMENTOSESPECIFICOS) {

        if (tbCONHECIMENTOSESPECIFICOS.getId() == null && !isPermitted("tbCONHECIMENTOSESPECIFICOS:create")) {
            return "accessDenied";
        } else if (tbCONHECIMENTOSESPECIFICOS.getId() != null && !isPermitted(tbCONHECIMENTOSESPECIFICOS, "tbCONHECIMENTOSESPECIFICOS:update")) {
            return "accessDenied";
        }
        
        String message;
        
        try {
            
            if (tbCONHECIMENTOSESPECIFICOS.getId() != null) {
            	
            	if(tbCONHECIMENTOSESPECIFICOS.getPenalidadeConhecBas()==0) {

				}else {

					if(flagEdit==false){
						tbPONTCARGOSEntity.setPoNTUACAOORIGINAL((double) tbCONHECIMENTOSESPECIFICOS.getPenalidadeConhecBas());
						tbCONHECIMENTOSESPECIFICOS.setConhecEspCustom(null);

					}else {
						if(tbCONHECIMENTOSESPECIFICOS.getPenalidadeConhecBas()==tbPONTCARGOSEntity.getPoNTUACAOORIGINAL()) {
							tbCONHECIMENTOSESPECIFICOS.setConhecEspCustom(null);
						}else {
							tbCONHECIMENTOSESPECIFICOS.setConhecEspCustom("S");
						}
					}
				}					

                tbCONHECIMENTOSESPECIFICOS = tbCONHECIMENTOSESPECIFICOSService.update(tbCONHECIMENTOSESPECIFICOS);
                tbPONTCARGOSEntity = tbPONTCARGOSService.update(tbPONTCARGOSEntity);
                message = "message_successfully_updated";
            } else {
                tbCONHECIMENTOSESPECIFICOS = tbCONHECIMENTOSESPECIFICOSService.save(tbCONHECIMENTOSESPECIFICOS);
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
        
        tbCONHECIMENTOSESPECIFICOSList = null;

        FacesMessage facesMessage = MessageFactory.getMessage(message);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        
        return null;
    }
    
	public void persist() {
		persist(tbCONHECIMENTOSESPECIFICOS);
	}
    
    public String delete() {
        
        if (!isPermitted(tbCONHECIMENTOSESPECIFICOS, "tbCONHECIMENTOSESPECIFICOS:delete")) {
            return "accessDenied";
        }
        
        String message;
        
        try {
            tbCONHECIMENTOSESPECIFICOSService.delete(tbCONHECIMENTOSESPECIFICOS);
            message = "message_successfully_deleted";
            flagDelete=true;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error occured", e);
            message = "message_delete_exception";
            // Set validationFailed to keep the dialog open
            FacesContext.getCurrentInstance().validationFailed();
        }
        tbCONHECIMENTOSESPECIFICOSList = null;
        
        FacesContext.getCurrentInstance().addMessage(null, MessageFactory.getMessage(message));
        
        return null;
    }
    
    public void onDialogOpen(TbCONHECIMENTOSESPECIFICOSEntity tbCONHECIMENTOSESPECIFICOS) {
        reset();
        changeHeaderEditar();
        this.tbCONHECIMENTOSESPECIFICOS = tbCONHECIMENTOSESPECIFICOS;
        pontuacaoOriginal = tbCONHECIMENTOSESPECIFICOS .getPenalidadeConhecBas();
        if(this.tbPONTCARGOSEntity ==null) {
		this.tbPONTCARGOSEntity = tbPONTCARGOSService.findPONTCARGOSByRequisito("CONHECESP");
        }
		flagEdit = true;
		if(pontuacaoOriginal == tbPONTCARGOSEntity.getPoNTUACAOORIGINAL()) {
			flagCustom = false;

		}else {
			flagCustom = true;
		}
    }
    
    public void reset() {

        
    }


   
    
   
    public TbCONHECIMENTOSESPECIFICOSEntity getTbCONHECIMENTOSESPECIFICOS() {
        if (this.tbCONHECIMENTOSESPECIFICOS == null) {
            prepareNewTbCONHECIMENTOSESPECIFICOS();
        }
        return this.tbCONHECIMENTOSESPECIFICOS;
    }
    
    public void setTbCONHECIMENTOSESPECIFICOS(TbCONHECIMENTOSESPECIFICOSEntity tbCONHECIMENTOSESPECIFICOS) {
        this.tbCONHECIMENTOSESPECIFICOS = tbCONHECIMENTOSESPECIFICOS;
    }
    
    public List<TbCONHECIMENTOSESPECIFICOSEntity> getTbCONHECIMENTOSESPECIFICOSList() {
        if ((tbCONHECIMENTOSESPECIFICOSList == null) || (flagDelete==true)) {
            tbCONHECIMENTOSESPECIFICOSList = tbCONHECIMENTOSESPECIFICOSService.findAllTbCONHECIMENTOSESPECIFICOSEntitiesCE();
        }
        return tbCONHECIMENTOSESPECIFICOSList;
    }

    public void setTbCONHECIMENTOSESPECIFICOSList(List<TbCONHECIMENTOSESPECIFICOSEntity> tbCONHECIMENTOSESPECIFICOSList) {
        this.tbCONHECIMENTOSESPECIFICOSList = tbCONHECIMENTOSESPECIFICOSList;
    }
    
    public boolean isPermitted(String permission) {
        return SecurityWrapper.isPermitted(permission);
    }

    public boolean isPermitted(TbCONHECIMENTOSESPECIFICOSEntity tbCONHECIMENTOSESPECIFICOS, String permission) {
        
        return SecurityWrapper.isPermitted(permission);
        
    }

	public double getPontuacaoOriginal() {
		return pontuacaoOriginal;
	}

	public void setPontuacaoOriginal(double pontuacaoOriginal) {
		this.pontuacaoOriginal = pontuacaoOriginal;
	}

	public double getGapVarCE() {
		if(this.tbPONTCARGOSEntity ==null) {
		this.tbPONTCARGOSEntity = tbPONTCARGOSService.findPONTCARGOSByRequisito("CONHECESP");
		gapVarCE = tbPONTCARGOSEntity.getPoNTUACAOORIGINAL();
		}
		
		return gapVarCE;
	}

	public void setGapVarCE(double gapVarCE) {
		this.gapVarCE = gapVarCE;
	}

	public boolean isFlagBloqueio() {
		if(tbCONHECIMENTOSESPECIFICOS==null) {

		}else {
			if(tbCONHECIMENTOSESPECIFICOS.getBloqueiaMovConhecEsp()==null) {

			}else {
				if(tbCONHECIMENTOSESPECIFICOS.getBloqueiaMovConhecEsp().equals("SIM")) {
					flagBloqueio = true;
				}else {
					flagBloqueio = false;
				}
			}
		}
		return flagBloqueio;
	}

	public void setFlagBloqueio(boolean flagBloqueio) {
		this.flagBloqueio = flagBloqueio;
		if(flagBloqueio==true) {
			tbCONHECIMENTOSESPECIFICOS.setBloqueiaMovConhecEsp("SIM");
		} else {
			tbCONHECIMENTOSESPECIFICOS.setBloqueiaMovConhecEsp("");
		}
	}

	public boolean isFlagCustom() {
		return flagCustom;
	}

	public void setFlagCustom(boolean flagCustom) {
		this.flagCustom = flagCustom;
		if(flagCustom==false) {
			tbCONHECIMENTOSESPECIFICOS.setPenalidadeConhecBas(tbPONTCARGOSEntity.getPoNTUACAOORIGINAL());
		}
	}

	public boolean isFlagEdit() {
		return flagEdit;
	}

	public void setFlagEdit(boolean flagEdit) {
		this.flagEdit = flagEdit;
	}

	public boolean isFlagDelete() {
		return flagDelete;
	}

	public void setFlagDelete(boolean flagDelete) {
		this.flagDelete = flagDelete;
	}
    
}
