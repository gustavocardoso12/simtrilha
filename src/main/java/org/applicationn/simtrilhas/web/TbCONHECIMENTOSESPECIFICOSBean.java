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

import org.applicationn.simtrilhas.domain.TbCONHECIMENTOSBASICOSEntity;
import org.applicationn.simtrilhas.domain.TbCONHECIMENTOSESPCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbCONHECIMENTOSESPECIFICOSEntity;
import org.applicationn.simtrilhas.domain.TbPONTCARGOSEntity;
import org.applicationn.simtrilhas.service.TbCONHECIMENTOSESPCARGOSService;
import org.applicationn.simtrilhas.service.TbCONHECIMENTOSESPECIFICOSService;
import org.applicationn.simtrilhas.service.TbPONTCARGOSService;
import org.applicationn.simtrilhas.service.security.SecurityWrapper;
import org.applicationn.simtrilhas.web.util.MessageFactory;
import org.primefaces.event.SlideEndEvent;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;

@Named("tbCONHECIMENTOSESPECIFICOSBean")
@ViewScoped
public class TbCONHECIMENTOSESPECIFICOSBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(TbCONHECIMENTOSESPECIFICOSBean.class.getName());
    
    private List<TbCONHECIMENTOSESPECIFICOSEntity> tbCONHECIMENTOSESPECIFICOSList;

    private TbCONHECIMENTOSESPECIFICOSEntity tbCONHECIMENTOSESPECIFICOS;
    
    @Inject
    private TbCONHECIMENTOSESPECIFICOSService tbCONHECIMENTOSESPECIFICOSService;
    
    @Inject
    private TbCONHECIMENTOSESPCARGOSService tbCONHECIMENTOSESPCARGOSService;
    
    private TbPONTCARGOSEntity tbPONTCARGOSEntity;

	@Inject
	private TbPONTCARGOSService tbPONTCARGOSService;
    
    private DualListModel<TbCONHECIMENTOSESPCARGOSEntity> tbCONHECIMENTOSESPCARGOSs;
    private List<String> transferedTbCONHECIMENTOSESPCARGOSIDs;
    private List<String> removedTbCONHECIMENTOSESPCARGOSIDs;
    
    private String dialogHeader;
    
    private double pontuacaoOriginal;

	private double gapVarCE;

	private boolean flagBloqueio;

	private boolean flagCustom;

	private boolean flagEdit;
	
	public void onSlideEndCE(SlideEndEvent event) {
		gapVarCE =  event.getValue();
		flagEdit = false;
		for (TbCONHECIMENTOSESPECIFICOSEntity tbCONHECIMENTOSESPECIFICOSEntity : tbCONHECIMENTOSESPECIFICOSList) {

			if(tbCONHECIMENTOSESPECIFICOSEntity.getConhecEspCustom()==null) {
				tbCONHECIMENTOSESPECIFICOSEntity.setPenalidadeConhecBas((int) gapVarCE);
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
        reset();
        changeHeaderCadastrar();
        this.tbCONHECIMENTOSESPECIFICOS = new TbCONHECIMENTOSESPECIFICOSEntity();

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
						tbPONTCARGOSEntity.setPoNTUACAOORIGINAL(tbCONHECIMENTOSESPECIFICOS.getPenalidadeConhecBas());
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
    
    public void onDialogOpen(TbCONHECIMENTOSESPECIFICOSEntity tbCONHECIMENTOSESPECIFICOS) {
        reset();
        changeHeaderEditar();
        this.tbCONHECIMENTOSESPECIFICOS = tbCONHECIMENTOSESPECIFICOS;
        pontuacaoOriginal = tbCONHECIMENTOSESPECIFICOS .getPenalidadeConhecBas();
		this.tbPONTCARGOSEntity = tbPONTCARGOSService.findPONTCARGOSByRequisito("CONHECESP");
		flagEdit = true;
		if(pontuacaoOriginal == tbPONTCARGOSEntity.getPoNTUACAOORIGINAL()) {
			flagCustom = false;

		}else {
			flagCustom = true;
		}
    }
    
    public void reset() {

        
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
    
    public void onTbCONHECIMENTOSESPCARGOSsDialog(TbCONHECIMENTOSESPECIFICOSEntity tbCONHECIMENTOSESPECIFICOS) {
        // Prepare the tbCONHECIMENTOSESPCARGOS PickList
        this.tbCONHECIMENTOSESPECIFICOS = tbCONHECIMENTOSESPECIFICOS;
        List<TbCONHECIMENTOSESPCARGOSEntity> selectedTbCONHECIMENTOSESPCARGOSsFromDB = tbCONHECIMENTOSESPCARGOSService
                .findTbCONHECIMENTOSESPCARGOSsByIdCONHECESP(this.tbCONHECIMENTOSESPECIFICOS);
        List<TbCONHECIMENTOSESPCARGOSEntity> availableTbCONHECIMENTOSESPCARGOSsFromDB = tbCONHECIMENTOSESPCARGOSService
                .findAvailableTbCONHECIMENTOSESPCARGOSs(this.tbCONHECIMENTOSESPECIFICOS);
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
        // but we do not assign it to this tbCONHECIMENTOSESPECIFICOS in the database, yet.
        tbCONHECIMENTOSESPCARGOSs.getTarget().add(tbCONHECIMENTOSESPCARGOS);
        transferedTbCONHECIMENTOSESPCARGOSIDs.add(tbCONHECIMENTOSESPCARGOS.getId().toString());
    }
    
    public void onTbCONHECIMENTOSESPCARGOSsSubmit() {
        // Now we save the changed of the PickList to the database.
        try {
            List<TbCONHECIMENTOSESPCARGOSEntity> selectedTbCONHECIMENTOSESPCARGOSsFromDB = tbCONHECIMENTOSESPCARGOSService
                    .findTbCONHECIMENTOSESPCARGOSsByIdCONHECESP(this.tbCONHECIMENTOSESPECIFICOS);
            List<TbCONHECIMENTOSESPCARGOSEntity> availableTbCONHECIMENTOSESPCARGOSsFromDB = tbCONHECIMENTOSESPCARGOSService
                    .findAvailableTbCONHECIMENTOSESPCARGOSs(this.tbCONHECIMENTOSESPECIFICOS);
            
            for (TbCONHECIMENTOSESPCARGOSEntity tbCONHECIMENTOSESPCARGOS : selectedTbCONHECIMENTOSESPCARGOSsFromDB) {
                if (removedTbCONHECIMENTOSESPCARGOSIDs.contains(tbCONHECIMENTOSESPCARGOS.getId().toString())) {
                    tbCONHECIMENTOSESPCARGOS.setIdCONHECESP(null);
                    tbCONHECIMENTOSESPCARGOSService.update(tbCONHECIMENTOSESPCARGOS);
                }
            }
    
            for (TbCONHECIMENTOSESPCARGOSEntity tbCONHECIMENTOSESPCARGOS : availableTbCONHECIMENTOSESPCARGOSsFromDB) {
                if (transferedTbCONHECIMENTOSESPCARGOSIDs.contains(tbCONHECIMENTOSESPCARGOS.getId().toString())) {
                    tbCONHECIMENTOSESPCARGOS.setIdCONHECESP(tbCONHECIMENTOSESPECIFICOS);
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
        if (tbCONHECIMENTOSESPECIFICOSList == null) {
            tbCONHECIMENTOSESPECIFICOSList = tbCONHECIMENTOSESPECIFICOSService.findAllTbCONHECIMENTOSESPECIFICOSEntities();
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
		this.tbPONTCARGOSEntity = tbPONTCARGOSService.findPONTCARGOSByRequisito("CONHECESP");
		gapVarCE = tbPONTCARGOSEntity.getPoNTUACAOORIGINAL();
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
    
}
