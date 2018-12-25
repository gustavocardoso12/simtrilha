package org.applicationn.simtrilhas.service.security;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import org.applicationn.simtrilhas.domain.security.RolePermission;
import org.applicationn.simtrilhas.domain.security.UserRole;

@Singleton
@Startup
public class RolePermissionsPublisher {

    private static final Logger logger = Logger.getLogger(RolePermissionsPublisher.class.getName());
    
    @Inject
    private RolePermissionsService rolePermissionService;
    
    @PostConstruct
    public void postConstruct() {

        if (rolePermissionService.countAllEntries() == 0) {

            rolePermissionService.save(new RolePermission(UserRole.Administrator, "tbFORMACAO:create"));
            
            rolePermissionService.save(new RolePermission(UserRole.Administrator, "tbFORMACAO:update"));
            
            rolePermissionService.save(new RolePermission(UserRole.Administrator, "tbFORMACAO:delete"));
            
            rolePermissionService.save(new RolePermission(UserRole.Administrator, "tbCURSOS:create"));
            
            rolePermissionService.save(new RolePermission(UserRole.Administrator, "tbCURSOS:update"));
            
            rolePermissionService.save(new RolePermission(UserRole.Administrator, "tbCURSOS:delete"));
            
            rolePermissionService.save(new RolePermission(UserRole.Administrator, "tbVICEPRESIDENCIA:create"));
            
            rolePermissionService.save(new RolePermission(UserRole.Administrator, "tbVICEPRESIDENCIA:update"));
            
            rolePermissionService.save(new RolePermission(UserRole.Administrator, "tbVICEPRESIDENCIA:delete"));
            
            rolePermissionService.save(new RolePermission(UserRole.Administrator, "tbDIRETORIA:create"));
            
            rolePermissionService.save(new RolePermission(UserRole.Administrator, "tbDIRETORIA:update"));
            
            rolePermissionService.save(new RolePermission(UserRole.Administrator, "tbDIRETORIA:delete"));
            
            rolePermissionService.save(new RolePermission(UserRole.Administrator, "tbESTATUARIO:create"));
            
            rolePermissionService.save(new RolePermission(UserRole.Administrator, "tbESTATUARIO:update"));
            
            rolePermissionService.save(new RolePermission(UserRole.Administrator, "tbESTATUARIO:delete"));
            
            rolePermissionService.save(new RolePermission(UserRole.Administrator, "tbCOMPETENCIAS:create"));
            
            rolePermissionService.save(new RolePermission(UserRole.Administrator, "tbCOMPETENCIAS:update"));
            
            rolePermissionService.save(new RolePermission(UserRole.Administrator, "tbCOMPETENCIAS:delete"));
            
            rolePermissionService.save(new RolePermission(UserRole.Administrator, "tbCOMPETENCIASEMOCIONAIS:create"));
            
            rolePermissionService.save(new RolePermission(UserRole.Administrator, "tbCOMPETENCIASEMOCIONAIS:update"));
            
            rolePermissionService.save(new RolePermission(UserRole.Administrator, "tbCOMPETENCIASEMOCIONAIS:delete"));
            
            rolePermissionService.save(new RolePermission(UserRole.Administrator, "tbCONHECIMENTOSBASICOS:create"));
            
            rolePermissionService.save(new RolePermission(UserRole.Administrator, "tbCONHECIMENTOSBASICOS:update"));
            
            rolePermissionService.save(new RolePermission(UserRole.Administrator, "tbCONHECIMENTOSBASICOS:delete"));
            
            rolePermissionService.save(new RolePermission(UserRole.Administrator, "tbCONHECIMENTOSESPECIFICOS:create"));
            
            rolePermissionService.save(new RolePermission(UserRole.Administrator, "tbCONHECIMENTOSESPECIFICOS:update"));
            
            rolePermissionService.save(new RolePermission(UserRole.Administrator, "tbCONHECIMENTOSESPECIFICOS:delete"));
            
            rolePermissionService.save(new RolePermission(UserRole.Administrator, "tbESTILOLIDERANCA:create"));
            
            rolePermissionService.save(new RolePermission(UserRole.Administrator, "tbESTILOLIDERANCA:update"));
            
            rolePermissionService.save(new RolePermission(UserRole.Administrator, "tbESTILOLIDERANCA:delete"));
            
            rolePermissionService.save(new RolePermission(UserRole.Administrator, "tbESTILOPENSAMENTO:create"));
            
            rolePermissionService.save(new RolePermission(UserRole.Administrator, "tbESTILOPENSAMENTO:update"));
            
            rolePermissionService.save(new RolePermission(UserRole.Administrator, "tbESTILOPENSAMENTO:delete"));
            
            rolePermissionService.save(new RolePermission(UserRole.Administrator, "tbHABILIDADES:create"));
            
            rolePermissionService.save(new RolePermission(UserRole.Administrator, "tbHABILIDADES:update"));
            
            rolePermissionService.save(new RolePermission(UserRole.Administrator, "tbHABILIDADES:delete"));
            
            rolePermissionService.save(new RolePermission(UserRole.Administrator, "tbHABILIDADESCULTURAIS:create"));
            
            rolePermissionService.save(new RolePermission(UserRole.Administrator, "tbHABILIDADESCULTURAIS:update"));
            
            rolePermissionService.save(new RolePermission(UserRole.Administrator, "tbHABILIDADESCULTURAIS:delete"));
            
            rolePermissionService.save(new RolePermission(UserRole.Administrator, "user:*"));
            
            logger.info("Successfully created permissions for user roles.");
        }
    }
}
