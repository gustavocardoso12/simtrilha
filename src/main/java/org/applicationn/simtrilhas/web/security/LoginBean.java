package org.applicationn.simtrilhas.web.security;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import org.applicationn.pesquisa.domain.TbDetalhesAcesso;
import org.applicationn.pesquisa.service.TbDetalheAcessoService;
import org.applicationn.simtrilhas.domain.security.UserEntity;
import org.applicationn.simtrilhas.service.BaseService;
import org.applicationn.simtrilhas.service.security.SecurityWrapper;
import org.applicationn.simtrilhas.service.security.UserService;
import org.applicationn.simtrilhas.web.util.MessageFactory;

@Named
@SessionScoped
public class LoginBean extends BaseService<UserEntity> implements Serializable {

	private static final long serialVersionUID = 1L;
	private  String username;
	public static List<String> usuario =  new ArrayList<String>();
	private String password;
	private boolean remember;
	private boolean logged = false;
	List<String> usuarios = new ArrayList<String>();

	@Inject
	private UserService userService;

	@Inject
	private TbDetalheAcessoService tbDetalheAcessoService;

	public  List<String> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<String> usuarios) {
		this.usuarios = usuarios;
	}


	public String findBrowserRenderMode() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();
		String userAgent = externalContext.getRequestHeaderMap().get("User-Agent");
		return userAgent.toLowerCase().contains("mobile") ? "MOBILE" : "DESKTOP";
	}




	@Transactional
	public String login() throws IOException {
		UserEntity user = userService.findUserByUsername(username);
		if(user==null) {
			FacesMessage message = MessageFactory.getMessage(
					"usuario_inexistente");
			FacesContext.getCurrentInstance().addMessage(null, message);
			return null;
		}else {
			if(user.getAtivo().equals("SIM")) {

				if(tbDetalheAcessoService.findAcessoSimultaneo().equals("true")) {
					if (!SecurityWrapper.login(username, password, remember)) {
						FacesMessage message = MessageFactory.getMessage(
								"authentication_exception");
						FacesContext.getCurrentInstance().addMessage(null, message);
						return null;
					}
				}else {
					FacesMessage message = MessageFactory.getMessage(
							"login_sessaoativa");
					FacesContext.getCurrentInstance().addMessage(null, message);
					return null;	
				}


			}else {

				if (!SecurityWrapper.login(username, password, remember)) {
					FacesMessage message = MessageFactory.getMessage(
							"authentication_exception");
					FacesContext.getCurrentInstance().addMessage(null, message);
					return null;
				}


				flag_ativo();


				logged = true;

				usuario.add(username);


			}
		}

		String sistema ="";

		if(user.getSistema()==null) {
			sistema= "/trilhas/Aderencias/Comparacao?faces-redirect=true";
		}else {
			if(user.getSistema().equals("Simulador")){
				sistema= "/trilhas/Aderencias/Comparacao?faces-redirect=true";
			}else if (user.getSistema().equals("Pesquisa")){
				sistema= "/pesquisas/pesquisas/Comparacao?faces-redirect=true";
			}
		}


		return sistema;
	}


	@Transactional
	public void flag_ativo() {
		UserEntity user = userService.findUserByUsername(username);
		user.setAtivo("SIM");
		user.setModifiedAt(new Date());
		getEntityManagerMatriz().merge(user);
		getEntityManagerMatriz().flush();

		if(user.getSistema()==null) {

		}else {

			TbDetalhesAcesso vo = new TbDetalhesAcesso();
			vo.setDataAcesso(new Date());
			vo.setId_user(user.getId());

			SimpleDateFormat formato = new SimpleDateFormat("yyyy/MM");
			String dataFormatada = formato.format(new Date());

			vo.setMesAno(dataFormatada);
			if(user.getSistema().equals("Simulador")) {
				vo.setTipoDeAtividade("ACESSO SIMULADOR");
			}else if (user.getSistema().equals("Pesquisa")) {
				vo.setTipoDeAtividade("ACESSO DASHBOARD");
			}

			getEntityManagerMatriz().persist(vo);
			getEntityManagerMatriz().flush();
		}
	}

	@Transactional
	public void logout() throws IOException {

		String username = SecurityWrapper.getUsername();
		UserEntity user = userService.findUserByUsername(username);
		user.setAtivo("NAO");
		getEntityManagerMatriz().merge(user);
		getEntityManagerMatriz().flush();
		logged=false;
		for(int i=0;i<usuario.size();i++) {
			if(username.equals(usuario.get(i))) {
				usuario.remove(i);
			}
		}

		SecurityWrapper.logout();
		String path = FacesContext.getCurrentInstance().getExternalContext().getApplicationContextPath() + "/trilhas/main.xhtml";
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		FacesContext.getCurrentInstance().getExternalContext().redirect(path);
	}

	@Transactional
	public String logout1() throws IOException {
		String username = SecurityWrapper.getUsername();
		UserEntity user = userService.findUserByUsername(username);
		user.setAtivo("NAO");
		getEntityManagerMatriz().merge(user);
		getEntityManagerMatriz().flush();
		logged=false;
		for(int i=0;i<usuario.size();i++) {
			if(username.equals(usuario.get(i))) {
				usuario.remove(i);
			}
		}
		SecurityWrapper.logout();
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		return "/trilhas/main.xhtml?faces-redirect=true";
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
