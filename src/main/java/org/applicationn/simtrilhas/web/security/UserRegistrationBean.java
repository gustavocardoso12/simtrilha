package org.applicationn.simtrilhas.web.security;

import java.io.Serializable;
import java.util.Arrays;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.MessagingException;

import org.applicationn.simtrilhas.domain.security.UserEntity;
import org.applicationn.simtrilhas.domain.security.UserRole;
import org.applicationn.simtrilhas.domain.security.UserStatus;
import org.applicationn.simtrilhas.service.security.RegistrationMailSender;
import org.applicationn.simtrilhas.service.security.UserService;
import org.applicationn.simtrilhas.web.util.ApplicationBaseURLBuider;
import org.applicationn.simtrilhas.web.util.MessageFactory;

@Named
@RequestScoped
public class UserRegistrationBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(UserRegistrationBean.class.getName());

    private UserEntity user;

    @Inject
    private UserService userService;

    @PostConstruct
    public void init() {
        user = new UserEntity();
    }

    public String register() {
        try {
            // Check if a user with same username already exists
            if (userService.findUserByUsername(user.getUsername()) != null) {
                FacesMessage facesMessage = MessageFactory.getMessage(
                        "user_username_exists");
                facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
                FacesContext.getCurrentInstance().addMessage(null, facesMessage);
                return "${facesContext.externalContext.request.contextPath}/trilhas/user/register.xhtml";
            }
            // Check if a user with same email already exists            
            if (userService.findUserByEmail(user.getEmail()) != null) {
                FacesMessage facesMessage = MessageFactory.getMessage(
                        "user_email_exists");
                facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
                FacesContext.getCurrentInstance().addMessage(null, facesMessage);
                return "${facesContext.externalContext.request.contextPath}/trilhas/user/register.xhtml";
            }
            
            user.setRoles(Arrays.asList(new UserRole[]{UserRole.Colaborador}));
            user.setStatus(UserStatus.NotConfirmed);
            
            String emailConfirmationKey = UUID.randomUUID().toString();
            user.setEmailConfirmationKey(emailConfirmationKey);

            String confirmationURL = "simuladorapta.brazilsouth.cloudapp.azure.com"
                    + "${facesContext.externalContext.request.contextPath}/trilhas/user/activation.xhtml?key=" + emailConfirmationKey;

            RegistrationMailSender.sendRegistrationActivation(user.getEmail(), confirmationURL);
            
            userService.save(user);

            FacesMessage facesMessage = MessageFactory.getMessage("message_successfully_created",
                    "UserEntity");
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);

            return "${facesContext.externalContext.request.contextPath}/trilhas/userManagement/registerSuccess.xhtml?faces-redirect=true";
        }
        catch (RuntimeException e) {
            logger.log(Level.SEVERE, "Error registrando o usuário!", e);
            FacesMessage message;
            if(e.getCause() instanceof MessagingException) {
                message = MessageFactory.getMessage(
                        "sending_email_failed");

            } else {
                message = MessageFactory.getMessage(
                        "registration_exception");
            }
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage(null, message);
            return "${facesContext.externalContext.request.contextPath}/trilhas/user/register.xhtml";
        }
    }

    public UserEntity getUser() {
        return user;
    }

}
