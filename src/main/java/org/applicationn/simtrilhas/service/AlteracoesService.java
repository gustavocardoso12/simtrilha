package org.applicationn.simtrilhas.service;

import java.io.Serializable;
import java.util.Date;
import javax.inject.Named;
import javax.transaction.Transactional;

import org.applicationn.simtrilhas.domain.TbAREAEntity;

@Named
public class AlteracoesService extends BaseService<TbAREAEntity> implements Serializable {



	private static final long serialVersionUID = 1L;
    
	public AlteracoesService() {
		
	}
    
    
    @Transactional
    public Date findUltimaAlteracaoTbMatriz() {

    	Date result = new Date();
    	result = (Date)getEntityManager().createNativeQuery(
    					"select max(ENTRY_MODIFIED_AT ) from TB_MATRIZ_CARGOS").getSingleResult();
    		
		return result;

    }
    
    
    @Transactional
      public Date findUltimaAlteracaoTbMAREA() {

      	Date result = new Date();
      	result = (Date)getEntityManager().createNativeQuery(
      					"select max(ENTRY_MODIFIED_AT ) from TB_AREA").getSingleResult();
      		
  		return result;

      }

    
    @Transactional
      public Date findUltimaAlteracaoTbDEPTO() {

      	Date result = new Date();
      	result = (Date)getEntityManager().createNativeQuery(
      					"select max(ENTRY_MODIFIED_AT ) from TB_DEPTO").getSingleResult();
      		
  		return result;

      }
    
    
    
    @Transactional
      public Date findUltimaAlteracaoTbCARGO() {

      	Date result = new Date();
      	result = (Date)getEntityManager().createNativeQuery(
      					"select max(ENTRY_MODIFIED_AT ) from TBCARGOS").getSingleResult();
      		
  		return result;

      }
    
    
    
    @Transactional
      public Date findUltimaAlteracaoTbCOMPETENCIAS() {

      	Date result = new Date();
      	result = (Date)getEntityManager().createNativeQuery(
      					"select max(ENTRY_MODIFIED_AT ) from TBCOMPETENCIAS").getSingleResult();
      		
  		return result;

      }
    
    @Transactional
    public Date findUltimaAlteracaoTbGRADE() {

    	Date result = new Date();
    	result = (Date)getEntityManager().createNativeQuery(
    					"select max(ENTRY_MODIFIED_AT ) from TB_GRADE").getSingleResult();
    		
		return result;

    }
    
    
    @Transactional
    public Date findUltimaAlteracaoTbPERFIL() {

    	Date result = new Date();
    	result = (Date)getEntityManager().createNativeQuery(
    					"select max(ENTRY_MODIFIED_AT ) from TB_PERFIL").getSingleResult();
    		
		return result;

    }
    
    @Transactional
    public Date findUltimaAlteracaoTbCONHECBAS() {

    	Date result = new Date();
    	result = (Date)getEntityManager().createNativeQuery(
    					"select max(ENTRY_MODIFIED_AT ) from TBCONHECIMENTOSBASICOS").getSingleResult();
    		
		return result;

    }
    
    @Transactional
    public Date findUltimaAlteracaoTbCONHECESP() {

    	Date result = new Date();
    	result = (Date)getEntityManager().createNativeQuery(
    					"select max(ENTRY_MODIFIED_AT ) from TBCONHECIMENTOSESPECIFICOS").getSingleResult();
    		
		return result;

    }
    
    
    
    @Transactional
    public Date findUltimaAlteracaoTbCOMPETENCIASCARGOS() {

    	Date result = new Date();
    	result = (Date)getEntityManager().createNativeQuery(
    					"select max(ENTRY_MODIFIED_AT ) from TBCOMPETENCIASCARGOS").getSingleResult();
    		
		return result;

    }
    
    @Transactional
    public Date findUltimaAlteracaoTbGRADECARGOS() {

    	Date result = new Date();
    	result = (Date)getEntityManager().createNativeQuery(
    					"select max(ENTRY_MODIFIED_AT ) from TB_GRADES_CARGOS").getSingleResult();
    		
		return result;

    }
    
    @Transactional
    public Date findUltimaAlteracaoTbPERFILCARGOS() {

    	Date result = new Date();
    	result = (Date)getEntityManager().createNativeQuery(
    					"select max(ENTRY_MODIFIED_AT ) from TB_PERFIL_CARGOS").getSingleResult();
    		
		return result;

    }
    
    
    @Transactional
    public Date findUltimaAlteracaoTbCONHECBASCARGOS() {

    	Date result = new Date();
    	result = (Date)getEntityManager().createNativeQuery(
    					"select max(ENTRY_MODIFIED_AT ) from TBCONHECIMENTOSBASCARGOS").getSingleResult();
    		
		return result;

    }
    
    @Transactional
    public Date findUltimaAlteracaoTbCONHECESPCARGOS() {

    	Date result = new Date();
    	result = (Date)getEntityManager().createNativeQuery(
    					"select max(ENTRY_MODIFIED_AT ) from TBCONHECIMENTOSESPCARGOS").getSingleResult();
    		
		return result;

    }
    
}
