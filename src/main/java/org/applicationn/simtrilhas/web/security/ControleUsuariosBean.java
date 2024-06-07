package org.applicationn.simtrilhas.web.security;


import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.MessagingException;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceException;

import org.applicationn.pesquisa.domain.TbEmpresa;

import org.applicationn.pesquisa.service.TbDetalheAcessoService;
import org.applicationn.pesquisa.service.TbEmpresaService;
import org.applicationn.pesquisa.vo.TbDetalhesAcessoVO;
import org.applicationn.simtrilhas.domain.security.UserEntity;
import org.applicationn.simtrilhas.domain.security.UserRole;
import org.applicationn.simtrilhas.domain.security.UserStatus;
import org.applicationn.simtrilhas.service.security.UserService;
import org.applicationn.simtrilhas.web.util.MessageFactory;

@Named("ControleUsuariosBean")
@ViewScoped
public class ControleUsuariosBean implements Serializable {

	public static int idUsuario;
	private static String nomeUsuario;
	private static String emailUsuario;
	private static UserEntity nomeExcluirUsuario;
	private static final long serialVersionUID = 1L;

	protected List<UserEntity> tbUSUARIOSList;
	
	protected List<UserEntity> tbRelatorioList;
	
	private List<String>uniqueMonths;

	@Inject
	protected UserService userService;

	@Inject
	protected TbEmpresaService tbEmpresaService;
	
	@Inject 
	protected TbDetalheAcessoService tbDetalheAcessoService;

	protected UserEntity user;

	private List<String> themesList = null;

	private List<String> BDList = null;
	private List<String> privilegiosList = new ArrayList<String>();
	private List<UserRole> rolesList = new ArrayList<UserRole>();
	private List<TbEmpresa> empresaList = new ArrayList<TbEmpresa>();
	private List<String> mercadoList = new ArrayList<String>();
	private String username;
	private String privilegio_acesso;
	private String senha;
	private String email;
	private String tema;
	private String bancoDados;
	private String flagPessoa;
	private String flagGrade;
	private String nivelAcesso;
	private String sistema;
	private String mercado;
	private TbEmpresa tbEmpresa;
	
	
	private List<TbDetalhesAcessoVO> accessDataList;


	public TbEmpresa getTbEmpresa() {
		return tbEmpresa;
	}

	public void setTbEmpresa(TbEmpresa tbEmpresa) {
		this.tbEmpresa = tbEmpresa;
	}

	private UserEntity usuarioEdicao;




	private int id;
	private String sistemaAtual;

	@PostConstruct
	public void Init() {
		privilegiosList.add("Basic");
		privilegiosList.add("Premium");
		empresaList = tbEmpresaService.findAllTbEmpresa();
		mercadoList = tbEmpresaService.empresasDisponiveis();
		nomeUsuario=null;
		emailUsuario=null;
	}
	
	

	/*public void ExecutaList(TbEmpresa tbEmpresa) {
		mercadoList = tbEmpresaService.empresasDisponiveis(tbEmpresa.getDescEmpresa());	
	}

	public void ExecutaListEdicao(TbEmpresa tbEmpresa) {
		mercadoList = tbEmpresaService.empresasDisponiveis(usuarioEdicao.getIdEmpresa().getDescEmpresa());
	}*/

	public String AdicionarUsuarios(String sistema) {
		reset();
		this.user = new UserEntity();
		this.sistema=sistema;
		if(sistema.equals("Simulador")) {
			return "/trilhas/usuario/AdicionarUsuario.xhtml?faces-redirect=true";
		}else {
			return "/pesquisas/pesquisas/usuarios/AdicionarUsuario.xhtml?faces-redirect=true";
		}

	}


	public int getAccessByMonth(String mesAno, String username) {
	
		UserEntity user = userService.findUserByUsername(username);
		int result = tbDetalheAcessoService.findDistinctMeses(mesAno, String.valueOf(user.getId()));
		return result;
		
		
	}


	public String submit(UserEntity user, String sistema) {
		getTbUSUARIOSList();
		id = user.getId().intValue();
		sistema = user.getSistema();
		if(sistema.equals("Simulador")) {
			return "/trilhas/usuario/EditarUsuario.xhtml?faces-redirect=true&includeViewParams=true";
		}else {
			return "/pesquisas/pesquisas/usuarios/EditarUsuario.xhtml?faces-redirect=true&includeViewParams=true";
		}

	}

	public String qualSistema(UserEntity user) {
		sistemaAtual = user.getSistema();
		return "/trilhas/usuario/Controle_usuarios.xhtml?faces-redirect=true&includeViewParams=true";
	}

	public void VerificarSistema() {
		if(sistema==null) {

		}else {

			if (sistema.equals("Pesquisa")){
				bancoDados = "APTA";
				tema = "APTA";
				flagPessoa="NAO";
				flagGrade="NAO";
			}

		}


	}


	public void VerificarSistema(UserEntity tbUSEREdicao) {

	}

	public String persistEdicao(UserEntity tbUSEREdicao) {
		if(tbUSEREdicao.getPrivilegio_acesso()==null) {
			
		}else if(tbUSEREdicao.getPrivilegio_acesso().equals("Basic")) {
			tbUSEREdicao.setIdEmpresa(tbEmpresaService.findTbEmpresaModelo());
			nivelAcesso = "COLABORADOR";
		}else if (tbUSEREdicao.getPrivilegio_acesso().equals("Premium")) {
			if(tbUSEREdicao.getIdEmpresa()==null) {
				FacesMessage facesMessage = MessageFactory.getMessage(
						"erro_empresa_vazia");
				facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
				FacesContext.getCurrentInstance().addMessage(null, facesMessage);
				return "";
			}
		}
		
		
		if(nivelAcesso.toUpperCase().equals("COLABORADOR")) {
			tbUSEREdicao.setRoles(Arrays.asList(new UserRole[]{UserRole.Colaborador}));
		}

		if(nivelAcesso.toUpperCase().equals("ADMINISTRATOR")) {
			tbUSEREdicao.setRoles(Arrays.asList(new UserRole[]{UserRole.Administrator}));
		}

		if(tbUSEREdicao.getSistema()==null) {

		}else if (tbUSEREdicao.getSistema().equals("Pesquisa")){
			tbUSEREdicao.setBanco_dados("APTA");
			tbUSEREdicao.setTheme("APTA");
			tbUSEREdicao.setFlag_grade("NAO");
			tbUSEREdicao.setFlag_pessoa("NAO");
		}
		String message="";
		try {


			if (tbUSEREdicao.getId() != null) {

				if(tbUSEREdicao.getUsername().equals(nomeUsuario)) {

				}else {

					if (userService.findUserByUsername(tbUSEREdicao.getUsername()) != null) {
						FacesMessage facesMessage = MessageFactory.getMessage(
								"user_username_exists");
						facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
						FacesContext.getCurrentInstance().addMessage(null, facesMessage);

						if(!tbUSEREdicao.getSistema().equals("Pesquisa")){
							return "${facesContext.externalContext.request.contextPath}/trilhas/usuario/EditarUsuario.xhtml?id="+idUsuario+"?faces-redirect=true";
						}else {
							return "${facesContext.externalContext.request.contextPath}/pesquisas/pesquisas/usuarios/EditarUsuario.xhtml?id="+idUsuario+"?faces-redirect=true";
						}



					}
				}


				if(tbUSEREdicao.getEmail().equals(emailUsuario)) {

				}else {
					if (userService.findUserByEmail(tbUSEREdicao.getEmail()) != null) {
						FacesMessage facesMessage = MessageFactory.getMessage(
								"user_email_exists");
						facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
						FacesContext.getCurrentInstance().addMessage(null, facesMessage);
						if(!tbUSEREdicao.getSistema().equals("Pesquisa")){
							return "${facesContext.externalContext.request.contextPath}/trilhas/usuario/AdicionarUsuario.xhtml?faces-redirect=true";
						}else {
							return "${facesContext.externalContext.request.contextPath}/pesquisas/pesquisas/usuarios/AdicionarUsuario.xhtml?faces-redirect=true";
						}
					}
				}

				tbUSEREdicao = userService.updateMatriz(tbUSEREdicao);
				message = "message_successfully_updated";
				this.usuarioEdicao = userService.findMatriz((long) idUsuario);

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

	
	public String persistEdicaoSimulador(UserEntity tbUSEREdicao) {
		
		
		
		if(nivelAcesso.toUpperCase().equals("COLABORADOR")) {
			tbUSEREdicao.setRoles(Arrays.asList(new UserRole[]{UserRole.Colaborador}));
		}

		if(nivelAcesso.toUpperCase().equals("ADMINISTRATOR")) {
			tbUSEREdicao.setRoles(Arrays.asList(new UserRole[]{UserRole.Administrator}));
		}

		if(tbUSEREdicao.getSistema()==null) {

		}else if (tbUSEREdicao.getSistema().equals("Pesquisa")){
			tbUSEREdicao.setBanco_dados("APTA");
			tbUSEREdicao.setTheme("APTA");
			tbUSEREdicao.setFlag_grade("NAO");
			tbUSEREdicao.setFlag_pessoa("NAO");
		}
		String message="";
		try {


			if (tbUSEREdicao.getId() != null) {

				if(tbUSEREdicao.getUsername().equals(nomeUsuario)) {

				}else {

					if (userService.findUserByUsername(tbUSEREdicao.getUsername()) != null) {
						FacesMessage facesMessage = MessageFactory.getMessage(
								"user_username_exists");
						facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
						FacesContext.getCurrentInstance().addMessage(null, facesMessage);

						if(!tbUSEREdicao.getSistema().equals("Pesquisa")){
							return "${facesContext.externalContext.request.contextPath}/trilhas/usuario/EditarUsuario.xhtml?id="+idUsuario+"?faces-redirect=true";
						}else {
							return "${facesContext.externalContext.request.contextPath}/pesquisas/pesquisas/usuarios/EditarUsuario.xhtml?id="+idUsuario+"?faces-redirect=true";
						}



					}
				}


				if(tbUSEREdicao.getEmail().equals(emailUsuario)) {

				}else {
					if (userService.findUserByEmail(tbUSEREdicao.getEmail()) != null) {
						FacesMessage facesMessage = MessageFactory.getMessage(
								"user_email_exists");
						facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
						FacesContext.getCurrentInstance().addMessage(null, facesMessage);
						if(!tbUSEREdicao.getSistema().equals("Pesquisa")){
							return "${facesContext.externalContext.request.contextPath}/trilhas/usuario/AdicionarUsuario.xhtml?faces-redirect=true";
						}else {
							return "${facesContext.externalContext.request.contextPath}/pesquisas/pesquisas/usuarios/AdicionarUsuario.xhtml?faces-redirect=true";
						}
					}
				}

				tbUSEREdicao = userService.updateMatriz(tbUSEREdicao);
				message = "message_successfully_updated";
				this.usuarioEdicao = userService.findMatriz((long) idUsuario);

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

	public String delete() {

		String message;

		try {

			UserEntity userAtual = userService.getCurrentUser();
			usuarioEdicao = nomeExcluirUsuario;
			if(usuarioEdicao.getUsername().equals(userAtual.getUsername())) {
				message = "message_delete_current_user";
				FacesContext.getCurrentInstance().validationFailed();
				reset();
			}else {
				userService.delete(usuarioEdicao);
				message = "message_successfully_deleted";
				getTbUSUARIOSList();
			}
		} catch (Exception e) {
			message = "message_delete_exception";
			FacesContext.getCurrentInstance().validationFailed();
		}
		FacesContext.getCurrentInstance().addMessage(null, MessageFactory.getMessage(message));

		return null;
	}


	public UserEntity getUsuarioEdicao() {
		if(this.usuarioEdicao==null) {
			if(id!=0) {
				idUsuario = id;

			}
			this.usuarioEdicao = userService.findMatriz((long) idUsuario);

			if(nomeUsuario==null) {
				nomeUsuario = usuarioEdicao.getUsername();
			}
			if(emailUsuario==null) {
				emailUsuario= usuarioEdicao.getEmail();
			}
			this.nivelAcesso = this.usuarioEdicao.getRoles().get(0).name();
			if(usuarioEdicao.getIdEmpresa()!=null){
				mercadoList = tbEmpresaService.empresasDisponiveis(usuarioEdicao.getIdEmpresa().getDescEmpresa());	
			}

			System.out.println("ID do usuario" + idUsuario);
		}
		return usuarioEdicao;
	}

	public void setUsuarioEdicao(UserEntity usuarioEdicao) {
		this.usuarioEdicao = usuarioEdicao;
		nomeExcluirUsuario = usuarioEdicao;
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
		sistema=null;
		tbEmpresa=null;
	}


	public String Tryit() throws IOException {

		try {
			UserEntity user = new UserEntity();
			UserEntity userAtual = userService.getCurrentUser();
			sistema = userAtual.getSistema();

			if(sistema==null) {

			}else {

				if (sistema.equals("Pesquisa")){
					bancoDados ="APTA";
					tema ="APTA";
					flagGrade = "NAO";
					flagPessoa = "NAO";
				}
			}


			if(tbEmpresa==null) {

			}else {
				user.setIdEmpresa(tbEmpresa);
			}

			if(privilegio_acesso==null) {
				
			}else if(privilegio_acesso.equals("Basic")) {
				user.setIdEmpresa(tbEmpresaService.findTbEmpresaModelo());
				nivelAcesso = "COLABORADOR";
			}else if (privilegio_acesso.equals("Premium")) {
				if(tbEmpresa==null) {
					FacesMessage facesMessage = MessageFactory.getMessage(
							"erro_empresa_vazia");
					facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
					FacesContext.getCurrentInstance().addMessage(null, facesMessage);
					return "";
				}
			}
			
			
			user.setBanco_dados(bancoDados);
			user.setEmail(email);
			user.setFlag_grade(flagGrade);
			user.setFlag_pessoa(flagPessoa);
			user.setUsername(username);
			user.setTheme(tema);
			user.setPassword(senha);
			user.setSistema(sistema);
			user.setMercado(mercado);
			user.setPrivilegio_acesso(privilegio_acesso);


			if (userService.findUserByUsername(user.getUsername()) != null) {
				FacesMessage facesMessage = MessageFactory.getMessage(
						"user_username_exists");
				facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
				FacesContext.getCurrentInstance().addMessage(null, facesMessage);

				if(!user.getSistema().equals("Pesquisa")){
					return "${facesContext.externalContext.request.contextPath}/trilhas/usuario/AdicionarUsuario.xhtml?faces-redirect=true";
				}else {
					return "${facesContext.externalContext.request.contextPath}/pesquisas/pesquisas/usuarios/AdicionarUsuario.xhtml?faces-redirect=true";
				}


			}

			if (userService.findUserByEmail(user.getEmail()) != null) {
				FacesMessage facesMessage = MessageFactory.getMessage(
						"user_email_exists");
				facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
				FacesContext.getCurrentInstance().addMessage(null, facesMessage);
				if(!user.getSistema().equals("Pesquisa")){
					return "${facesContext.externalContext.request.contextPath}/trilhas/usuario/AdicionarUsuario.xhtml?faces-redirect=true";
				}else {
					return "${facesContext.externalContext.request.contextPath}/pesquisas/pesquisas/usuarios/AdicionarUsuario.xhtml?faces-redirect=true";
				}
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

			if(this.sistemaAtual==null) {
				UserEntity user = userService.getCurrentUser();
				tbUSUARIOSList = userService.findAllUserEntitiesBySistema(user.getSistema());
			}else {
				tbUSUARIOSList = userService.findAllUserEntitiesBySistema(sistemaAtual);
			}


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

	public String getSistema() {
		return sistema;
	}

	public void setSistema(String sistema) {
		this.sistema = sistema;
	}

	public String getMercado() {
		return mercado;
	}

	public void setMercado(String mercado) {
		this.mercado = mercado;
	}

	
	public List<TbEmpresa> getEmpresaList() {


		return empresaList;
	}


	public List<String> getMercadoList() {



		return mercadoList;
	}



	public void setMercadoList(List<String> mercadoList) {
		this.mercadoList = mercadoList;
	}


	public void setEmpresaList(List<TbEmpresa> empresaList) {
		this.empresaList = empresaList;
	}




	public String getSistemaAtual() {
		return sistemaAtual;
	}

	public void setSistemaAtual(String sistemaAtual) {
		this.sistemaAtual = sistemaAtual;
	}

	public List<String> getPrivilegiosList() {

		return privilegiosList;
	}

	public void setPrivilegiosList(List<String> privilegiosList) {
		this.privilegiosList = privilegiosList;
	}

	public static int getIdUsuario() {
		return idUsuario;
	}

	public static void setIdUsuario(int idUsuario) {
		ControleUsuariosBean.idUsuario = idUsuario;
	}

	public static String getNomeUsuario() {
		return nomeUsuario;
	}

	public static void setNomeUsuario(String nomeUsuario) {
		ControleUsuariosBean.nomeUsuario = nomeUsuario;
	}

	public static String getEmailUsuario() {
		return emailUsuario;
	}

	public static void setEmailUsuario(String emailUsuario) {
		ControleUsuariosBean.emailUsuario = emailUsuario;
	}

	public static UserEntity getNomeExcluirUsuario() {
		return nomeExcluirUsuario;
	}

	public static void setNomeExcluirUsuario(UserEntity nomeExcluirUsuario) {
		ControleUsuariosBean.nomeExcluirUsuario = nomeExcluirUsuario;
	}

	public TbEmpresaService getTbEmpresaService() {
		return tbEmpresaService;
	}

	public void setTbEmpresaService(TbEmpresaService tbEmpresaService) {
		this.tbEmpresaService = tbEmpresaService;
	}

	public String getPrivilegio_acesso() {
		return privilegio_acesso;
	}

	public void setPrivilegio_acesso(String privilegio_acesso) {
		this.privilegio_acesso = privilegio_acesso;
	}


	public List<TbDetalhesAcessoVO> getAccessDataList() {
		if (accessDataList == null) {
			accessDataList = tbDetalheAcessoService.findRelatorioUsuariosV2();
		}
		return accessDataList;
	}

	public void setAccessDataList(List<TbDetalhesAcessoVO> accessDataList) {
		this.accessDataList = accessDataList;
	}



	public List<String> getUniqueMonths() {
		
		if (uniqueMonths == null) {
			uniqueMonths = tbDetalheAcessoService.findDistinctMeses();
		}
		
		return uniqueMonths;
	}

	public void setUniqueMonths(List<String> uniqueMonths) {
		this.uniqueMonths = uniqueMonths;
	}

	public TbDetalheAcessoService getTbDetalheAcessoService() {
		return tbDetalheAcessoService;
	}

	public void setTbDetalheAcessoService(TbDetalheAcessoService tbDetalheAcessoService) {
		this.tbDetalheAcessoService = tbDetalheAcessoService;
	}

}
