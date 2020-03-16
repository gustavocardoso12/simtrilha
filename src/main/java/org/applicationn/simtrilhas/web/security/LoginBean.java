package org.applicationn.simtrilhas.web.security;

import java.io.IOException;
import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.applicationn.simtrilhas.service.security.SecurityWrapper;
import org.applicationn.simtrilhas.web.util.MessageFactory;

@Named
@RequestScoped
public class LoginBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private String username;
    private String password;
    private boolean remember;
    private boolean logged = false;

    public String login() throws IOException {
        
        if (!SecurityWrapper.login(username, password, remember)) {
            FacesMessage message = MessageFactory.getMessage(
                    "authentication_exception");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return null;
        }
        
        SavedRequest savedRequest = WebUtils
                .getAndClearSavedRequest((HttpServletRequest) FacesContext
                        .getCurrentInstance().getExternalContext()
                        .getRequest());
        
        if (savedRequest != null) {
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect(savedRequest.getRequestUrl());
            return null;
        } else {
        	logged = true;
            return "/trilhas/Aderencias/Comparacao?faces-redirect=true";
        }
    }

    public void logout() throws IOException {
    	logged=false;
        SecurityWrapper.logout();
        String path = FacesContext.getCurrentInstance().getExternalContext().getApplicationContextPath() + "/trilhas/main.xhtml";
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        FacesContext.getCurrentInstance().getExternalContext().redirect(path);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isRemember() {
        return remember;
    }

    public void setRemember(boolean remember) {
        this.remember = remember;
    }

	public boolean isLogged() {
		return logged;
	}

	public void setLogged(boolean logged) {
		this.logged = logged;
	}
}
