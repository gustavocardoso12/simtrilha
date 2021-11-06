package org.applicationn.simtrilhas.web.security;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import org.applicationn.simtrilhas.domain.security.UserEntity;
import org.applicationn.simtrilhas.service.BaseService;
import org.applicationn.simtrilhas.service.security.SecurityWrapper;
import org.applicationn.simtrilhas.service.security.UserService;


import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import  org.applicationn.simtrilhas.domain.Sessoes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named("sessionWatchdog")
@SessionScoped
public class SessionScopeListener extends BaseService<UserEntity> implements Serializable, HttpSessionListener {
	
	private String  userName;
	
	 @Inject
	 private UserService userService;
	 
	 static List<Sessoes> sessoesGravadas = new ArrayList<Sessoes>();

	private HttpSessionEvent event;
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(SessionScopeListener.class);
	
	private static String user;
	
	public void sessionCreated(HttpSessionEvent event) {
		logger.debug("starting session {}", event.getSession().getId());
		this.event = event;
	}
	



    @Transactional
	public void sessionDestroyed(HttpSessionEvent event) {
		logger.debug("stopping session {}", event.getSession().getId());
			
				try {
				
				String username = SecurityWrapper.getUsername();
				UserEntity user = userService.findUserByUsername(username);
				user.setAtivo("NAO");
				getEntityManagerMatriz().merge(user);
				getEntityManagerMatriz().flush();
				for(int i=0;i<LoginBean.usuario.size();i++) {
					if(username.equals(LoginBean.usuario.get(i))) {
						LoginBean.usuario.remove(i);
					}
				}
				} catch (RuntimeException ex) {
					for(int i=0;i<LoginBean.usuario.size();i++) {
						String username = LoginBean.usuario.get(i);
						UserEntity user = userService.findUserByUsername(username);
						user.setAtivo("NAO");
						getEntityManagerMatriz().merge(user);
						getEntityManagerMatriz().flush();
					}
				}
				
			
				

}



	public String getUserName() {
		return userName;
	}



	public void setUserName(String userName) {
		this.userName = userName;
	}


	public String getUser() {
		return user;
	}


	@SuppressWarnings("static-access")
	public void setUser(String user) {
		this.user = user;
	}

	public HttpSessionEvent getEvent() {
		return event;
	}

	public void setEvent(HttpSessionEvent event) {
		this.event = event;
	}
}