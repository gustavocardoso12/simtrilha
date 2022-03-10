package org.applicationn.simtrilhas.web.security;


import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.MessagingException;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceException;

import org.applicationn.simtrilhas.domain.security.UserEntity;
import org.applicationn.simtrilhas.domain.security.UserRole;
import org.applicationn.simtrilhas.domain.security.UserStatus;
import org.applicationn.simtrilhas.service.security.UserService;
import org.applicationn.simtrilhas.web.util.MessageFactory;

@Named("ControleUsuariosBean")
@ViewScoped
public class ControleUsuariosBean implements Serializable {

	private static final long serialVersionUID = 1L;

	protected List<UserEntity> tbUSUARIOSList;

	@Inject
	protected UserService userService;

	protected UserEntity user;

	private List<String> themesList = null;

	private List<String> BDList = null;
	private List<UserRole> rolesList = new ArrayList<UserRole>();

	private String username;
	private String senha;
	private String email;
	private String tema;
	private String bancoDados;
	private String flagPessoa;
	private String flagGrade;
	private String nivelAcesso;
	
	private UserEntity usuarioEdicao;


	private int id;

	public String AdicionarUsuarios() {
		reset();
		this.user = new UserEntity();
		return "/trilhas/usuario/AdicionarUsuario.xhtml?faces-redirect=true";
	}

	public String submit(UserEntity user) {
		id = user.getId().intValue();

		return "/trilhas/usuario/EditarUsuario.xhtml?faces-redirect=true&includeViewParams=true";
	}
	
	

	public String persistEdicao(UserEntity tbUSEREdicao) {
		if(nivelAcesso.toUpperCase().equals("COLABORADOR")) {
			tbUSEREdicao.setRoles(Arrays.asList(new UserRole[]{UserRole.Colaborador}));
		}
		
		if(nivelAcesso.toUpperCase().equals("ADMINISTRATOR")) {
			tbUSEREdicao.setRoles(Arrays.asList(new UserRole[]{UserRole.Administrator}));
		}
		
		
		String message="";
		try {


			if (tbUSEREdicao.getId() != null) {
				tbUSEREdicao = userService.updateMatriz(tbUSEREdicao);
				message = "message_successfully_updated";
				this.usuarioEdicao = userService.findMatriz((long) id);

			} else {
				tbUSEREdicao = userService.save(tbUSEREdicao);
				message = "message_successfully_created";
			}

		} catch (OptimisticLockException e) {
			message = "message_optimistic_locking_exception";
			// Set validationFailed to keep the dialog open
			FacesContext.getCurrentInstance().validationFailed();
		} catch (PersistenceException e) {
			message = "message_save_exception";
			// Set validationFailed to keep the dialog open
			FacesContext.getCurrentInstance().validationFailed();
		}

		tbUSUARIOSList = null;
		FacesMessage facesMessage = MessageFactory.getMessage(message);
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);

		return null;
	}

	
	

	public UserEntity getUsuarioEdicao() {
		if(this.usuarioEdicao==null) {
			this.usuarioEdicao = userService.findMatriz((long) id);
			this.nivelAcesso = this.usuarioEdicao.getRoles().get(0).name();
			System.out.println("ID do usuario" + id);
		}
		return usuarioEdicao;
	}

	public void setUsuarioEdicao(UserEntity usuarioEdicao) {
		this.usuarioEdicao = usuarioEdicao;
	}

	public void reset() {
		tbUSUARIOSList = null;
		username = null;
		senha = null;
		email = null;
		tema= null;
		bancoDados = null;
		flagPessoa = null;
		flagGrade=null;
		nivelAcesso = null;
		}


	public String Tryit() throws IOException {

		try {
			UserEntity user = new UserEntity();


			user.setBanco_dados(bancoDados);
			user.setEmail(email);
			user.setFlag_grade(flagGrade);
			user.setFlag_pessoa(flagPessoa);
			user.setUsername(username);
			user.setTheme(tema);
			user.setPassword(senha);

			if (userService.findUserByUsername(user.getUsername()) != null) {
				FacesMessage facesMessage = MessageFactory.getMessage(
						"user_username_exists");
				facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
				FacesContext.getCurrentInstance().addMessage(null, facesMessage);
				return "${facesContext.externalContext.request.contextPath}/trilhas/usuario/AdicionarUsuario.xhtml?faces-redirect=true";
			}

			if (userService.findUserByEmail(user.getEmail()) != null) {
				FacesMessage facesMessage = MessageFactory.getMessage(
						"user_email_exists");
				facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
				FacesContext.getCurrentInstance().addMessage(null, facesMessage);
				return "${facesContext.externalContext.request.contextPath}/trilhas/usuario/AdicionarUsuario.xhtml?faces-redirect=true";
			}
			
			if(nivelAcesso.toUpperCase().equals("COLABORADOR")) {
				user.setRoles(Arrays.asList(new UserRole[]{UserRole.Colaborador}));
			}
			
			if(nivelAcesso.toUpperCase().equals("ADMINISTRATOR")) {
				user.setRoles(Arrays.asList(new UserRole[]{UserRole.Administrator}));
			}
			
			
			user.setStatus(UserStatus.Active);
			user.setAtivo("NAO");

			userService.save(user);

			FacesMessage facesMessage = MessageFactory.getMessage("message_successfully_created",
					"UserEntity");
			FacesContext.getCurrentInstance().addMessage(null, facesMessage);
			reset();

			return "${facesContext.externalContext.request.contextPath}/trilhas/usuario/AdicionarUsuario.xhtml?faces-redirect=true";


		}catch(Exception e) {
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
			return "${facesContext.externalContext.request.contextPath}/trilhas/usuario/AdicionarUsuario.xhtml?faces-redirect=true";
		}


	}






	public List<UserEntity> getTbUSUARIOSList() {
		if (tbUSUARIOSList == null) {
			tbUSUARIOSList = userService.findAllUserEntities();
		}
		return tbUSUARIOSList;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setTbUSUARIOSList(List<UserEntity> tbUSUARIOSList) {
		this.tbUSUARIOSList = tbUSUARIOSList;
	}

	public List<String> getThemesList() {
		if (themesList == null) {
			themesList = userService.findAllThemes();
		}
		return themesList;
	}

	public void setThemesList(List<String> themesList) {

		this.themesList = themesList;
	}

	public List<String> getBDList() {
		if (BDList == null) {
			BDList = userService.findAllBD();
			for (int i=0;i<BDList.size();i++) {
				if(BDList.get(i).equals("Cocamar")) {
					BDList.set(i, "APTA");
				}
			}
		}
		return BDList;
	}

	public void setBDList(List<String> bDList) {
		BDList = bDList;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTema() {
		return tema;
	}

	public void setTema(String tema) {
		this.tema = tema;
	}

	public String getBancoDados() {
		return bancoDados;
	}

	public void setBancoDados(String bancoDados) {
		this.bancoDados = bancoDados;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getFlagPessoa() {
		return flagPessoa;
	}

	public void setFlagPessoa(String flagPessoa) {
		this.flagPessoa = flagPessoa;
	}

	public String getFlagGrade() {
		return flagGrade;
	}

	public void setFlagGrade(String flagGrade) {
		this.flagGrade = flagGrade;
	}

	public List<UserRole> getRolesList() {

		List<UserRole> lista1 = Arrays.asList(new UserRole[]{UserRole.Colaborador});
		List<UserRole> lista2 = Arrays.asList(new UserRole[]{UserRole.Administrator});


		if (rolesList.size()==0) {

			for(int i=0; i<lista1.size();i++) {
				rolesList.add(lista1.get(i));
			}

			for(int j=0; j<lista1.size();j++) {
				rolesList.add(lista2.get(j));
			}

		}

		return rolesList;
	}

	public void setRolesList(List<UserRole> rolesList) {
		this.rolesList = rolesList;
	}

	public String getNivelAcesso() {
		return nivelAcesso;
	}

	public void setNivelAcesso(String nivelAcesso) {
		this.nivelAcesso = nivelAcesso;
	}

}
