package org.applicationn.simtrilhas.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Named;
import javax.transaction.Transactional;

import org.applicationn.simtrilhas.domain.TbCARGOSEntity;
import org.applicationn.simtrilhas.domain.TbCURSOSEntity;
import org.applicationn.simtrilhas.domain.TbESTATUARIOEntity;

@Named
public class TbCARGOSService extends BaseService<TbCARGOSEntity> implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public TbCARGOSService(){
        super(TbCARGOSEntity.class);
    }
    
    @Transactional
    public List<TbCARGOSEntity> findAllTbCARGOSEntities() {
        
        return entityManager.createQuery("SELECT o FROM TbCARGOS o ", TbCARGOSEntity.class).getResultList();
    }
    
    @Override
    @Transactional
    public long countAllEntries() {
        return entityManager.createQuery("SELECT COUNT(o) FROM TbCARGOS o", Long.class).getSingleResult();
    }
    
    @Override
    protected void handleDependenciesBeforeDelete(TbCARGOSEntity tbCARGOS) {

        /* This is called before a TbCARGOS is deleted. Place here all the
           steps to cut dependencies to other entities */
        
        this.cutAllIdCARGOSTbCOMPETENCIASCARGOSsAssignments(tbCARGOS);
        
        this.cutAllIdCARGOSTbCOMPETENCIASEMCARGOSsAssignments(tbCARGOS);
        
        this.cutAllIdCARGOSTbCONHECIMENTOSBASCARGOSsAssignments(tbCARGOS);
        
        this.cutAllIdCARGOSTbCONHECIMENTOSESPCARGOSsAssignments(tbCARGOS);
        
        this.cutAllIdCARGOSTbESTILOLIDERANCACARGOSsAssignments(tbCARGOS);
        
        this.cutAllIdCARGOSTbESTILOPENSAMENTOCARGOSsAssignments(tbCARGOS);
        
        this.cutAllIdCARGOSTbHABILIDADESCARGOSsAssignments(tbCARGOS);
        
        this.cutAllIdCARGOSTbHABILIDADESCULTCARGOSsAssignments(tbCARGOS);
        
    }

    // Remove all assignments from all tbCOMPETENCIASCARGOS a tbCARGOS. Called before delete a tbCARGOS.
    @Transactional
    private void cutAllIdCARGOSTbCOMPETENCIASCARGOSsAssignments(TbCARGOSEntity tbCARGOS) {
        entityManager
                .createQuery("UPDATE TbCOMPETENCIASCARGOS c SET c.idCARGOS = NULL WHERE c.idCARGOS = :p")
                .setParameter("p", tbCARGOS).executeUpdate();
    }
    
    // Remove all assignments from all tbCOMPETENCIASEMCARGOS a tbCARGOS. Called before delete a tbCARGOS.
    @Transactional
    private void cutAllIdCARGOSTbCOMPETENCIASEMCARGOSsAssignments(TbCARGOSEntity tbCARGOS) {
        entityManager
                .createQuery("UPDATE TbCOMPETENCIASEMCARGOS c SET c.idCARGOS = NULL WHERE c.idCARGOS = :p")
                .setParameter("p", tbCARGOS).executeUpdate();
    }
    
    // Remove all assignments from all tbCONHECIMENTOSBASCARGOS a tbCARGOS. Called before delete a tbCARGOS.
    @Transactional
    private void cutAllIdCARGOSTbCONHECIMENTOSBASCARGOSsAssignments(TbCARGOSEntity tbCARGOS) {
        entityManager
                .createQuery("UPDATE TbCONHECIMENTOSBASCARGOS c SET c.idCARGOS = NULL WHERE c.idCARGOS = :p")
                .setParameter("p", tbCARGOS).executeUpdate();
    }
    
    // Remove all assignments from all tbCONHECIMENTOSESPCARGOS a tbCARGOS. Called before delete a tbCARGOS.
    @Transactional
    private void cutAllIdCARGOSTbCONHECIMENTOSESPCARGOSsAssignments(TbCARGOSEntity tbCARGOS) {
        entityManager
                .createQuery("UPDATE TbCONHECIMENTOSESPCARGOS c SET c.idCARGOS = NULL WHERE c.idCARGOS = :p")
                .setParameter("p", tbCARGOS).executeUpdate();
    }
    
    // Remove all assignments from all tbESTILOLIDERANCACARGOS a tbCARGOS. Called before delete a tbCARGOS.
    @Transactional
    private void cutAllIdCARGOSTbESTILOLIDERANCACARGOSsAssignments(TbCARGOSEntity tbCARGOS) {
        entityManager
                .createQuery("UPDATE TbESTILOLIDERANCACARGOS c SET c.idCARGOS = NULL WHERE c.idCARGOS = :p")
                .setParameter("p", tbCARGOS).executeUpdate();
    }
    
    // Remove all assignments from all tbESTILOPENSAMENTOCARGOS a tbCARGOS. Called before delete a tbCARGOS.
    @Transactional
    private void cutAllIdCARGOSTbESTILOPENSAMENTOCARGOSsAssignments(TbCARGOSEntity tbCARGOS) {
        entityManager
                .createQuery("UPDATE TbESTILOPENSAMENTOCARGOS c SET c.idCARGOS = NULL WHERE c.idCARGOS = :p")
                .setParameter("p", tbCARGOS).executeUpdate();
    }
    
    // Remove all assignments from all tbHABILIDADESCARGOS a tbCARGOS. Called before delete a tbCARGOS.
    @Transactional
    private void cutAllIdCARGOSTbHABILIDADESCARGOSsAssignments(TbCARGOSEntity tbCARGOS) {
        entityManager
                .createQuery("UPDATE TbHABILIDADESCARGOS c SET c.idCARGOS = NULL WHERE c.idCARGOS = :p")
                .setParameter("p", tbCARGOS).executeUpdate();
    }
    
    // Remove all assignments from all tbHABILIDADESCULTCARGOS a tbCARGOS. Called before delete a tbCARGOS.
    @Transactional
    private void cutAllIdCARGOSTbHABILIDADESCULTCARGOSsAssignments(TbCARGOSEntity tbCARGOS) {
        entityManager
                .createQuery("UPDATE TbHABILIDADESCULTCARGOS c SET c.idCARGOS = NULL WHERE c.idCARGOS = :p")
                .setParameter("p", tbCARGOS).executeUpdate();
    }
    
    @Transactional
    public List<TbCARGOSEntity> findAvailableTbCARGOSs(TbESTATUARIOEntity tbESTATUARIO) {
        return entityManager.createQuery("SELECT o FROM TbCARGOS o WHERE o.idEST IS NULL", TbCARGOSEntity.class).getResultList();
    }

    @Transactional
    public List<TbCARGOSEntity> findTbCARGOSsByIdEST(TbESTATUARIOEntity tbESTATUARIO) {
        return entityManager.createQuery("SELECT o FROM TbCARGOS o WHERE o.idEST = :tbESTATUARIO", TbCARGOSEntity.class).setParameter("tbESTATUARIO", tbESTATUARIO).getResultList();
    }

    @Transactional
    public List<TbCARGOSEntity> findAvailableTbCARGOSs(TbCURSOSEntity tbCURSOS) {
        return entityManager.createQuery("SELECT o FROM TbCARGOS o WHERE o.idCURSOS IS NULL", TbCARGOSEntity.class).getResultList();
    }

    @Transactional
    public List<TbCARGOSEntity> findTbCARGOSsByIdCURSOS(TbCURSOSEntity tbCURSOS) {
        return entityManager.createQuery("SELECT o FROM TbCARGOS o WHERE o.idCURSOS = :tbCURSOS", TbCARGOSEntity.class).setParameter("tbCURSOS", tbCURSOS).getResultList();
    }

}
