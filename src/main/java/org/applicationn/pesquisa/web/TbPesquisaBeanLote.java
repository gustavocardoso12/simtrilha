package org.applicationn.pesquisa.web;


import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.StringJoiner;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;


import org.applicationn.pesquisa.domain.TbPesquisa;
import org.applicationn.pesquisa.service.TbPesquisaService;
import org.applicationn.pesquisa.vo.GradeVO;
import org.applicationn.pesquisa.vo.MediasVO;
import org.applicationn.simtrilhas.domain.security.UserEntity;
import org.applicationn.simtrilhas.service.security.UserService;
import org.applicationn.simtrilhas.web.util.MessageFactory;
import org.primefaces.component.export.CSVOptions;
import org.primefaces.component.export.ExporterOptions;
import org.primefaces.event.SlideEndEvent;
@Named("tbPesquisaBeanLote")
@ViewScoped
public class TbPesquisaBeanLote implements Serializable {


	private static final long serialVersionUID = 1L;

	@Inject
	private TbPesquisaService tbPesquisaService;

	private List<TbPesquisa> tbPesquisaList;

	private String familiaEscolhida;
	private String subFamiliaEscolhida;
	private String cargoEscolhido;
	
	private String[] familiasEscolhidas;
	private String[] subFamiliasEscolhidas;
	private String[] cargosEscolhidas;
	
	private String mercadoEscolhido= "Mercado AptaXR";
	private int gradeEscolhida;

	private boolean mostrarembarras =false;

	private Integer gradeMinimo = (Integer) 19;
	private Integer gradeMaximo = (Integer) 40;


	private Integer gradeMinimoPadrao = (Integer) 19;
	private Integer gradeMaximoPadrao = (Integer) 40;

	private Integer gradeMinimoEmpresa = (Integer) 0;
	private Integer gradeMaximoEmpresa = (Integer) 0;

	private Integer gradeSlide = null;

	private List<String> distinctFamilia;
	private List<String> distinctSubFamilia;
	private List<String> distinctCargos;
	private List<String> distinctMercado;
	private List<Integer> distinctGrade;

	private List<MediasVO> listaDeMedias;


	private String classe="";

	@Inject
	protected UserService userService;

	protected UserEntity user;

	private String descEmpresaExibir;

	public void prepareNewTbAREA() {
		reset();
	}

	public void ExportarExcel() {
		if(listaDeMedias==null) {
			
		}else {
			if(listaDeMedias.size()==0) {

			}else {
			
			}
		
		}
	}




	public ExporterOptions getCsvOptions() {
		return CSVOptions.EXCEL_NORTHERN_EUROPE;
	}


	@PostConstruct
	public void init() {
		try {

			this.distinctFamilia = tbPesquisaService.findDistinctTbPesquisaNMFamilia();
			user = userService.getCurrentUser();
		}catch (Exception e) {
		}

	}


	

	public static double round(double value, int places) {
		if (places < 0) throw new IllegalArgumentException();

		BigDecimal bd = BigDecimal.valueOf(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}


	public String getDatatipFormatX() {
		return "R$s";
	}

	public void onSlideEnd(SlideEndEvent event) {
		this.gradeSlide = (int) event.getValue();
		getDistinctGrade(false);
	}


	public void reset() {


	}

	public String[] getFamiliasEscolhidas() {
		return familiasEscolhidas;
	}

	public void setFamiliasEscolhidas(String[] familiasEscolhidas) {
		this.familiasEscolhidas = familiasEscolhidas;
	}

	public String[] getSubFamiliasEscolhidas() {
		return subFamiliasEscolhidas;
	}

	public void setSubFamiliasEscolhidas(String[] subFamiliasEscolhidas) {
		this.subFamiliasEscolhidas = subFamiliasEscolhidas;
	}

	public String[] getCargosEscolhidas() {
		return cargosEscolhidas;
	}

	public void setCargosEscolhidas(String[] cargosEscolhidas) {
		this.cargosEscolhidas = cargosEscolhidas;
	}
	
	
	
	
	public List<String> getDistinctSubFamilia() {

		if (familiasEscolhidas != null) {
			
			StringJoiner sj = new StringJoiner(",", "'", "'");
			for (String familia : familiasEscolhidas) {
			    sj.add(familia);
			}
			String valores = sj.toString();
			
			valores = "'" + String.join("','", familiasEscolhidas) + "'";
			
		
		}else {
			this.distinctSubFamilia = null;
		}
		return distinctSubFamilia;
	}

	public List<String> getDistinctCargos() {
		if (subFamiliaEscolhida != null) {
			this.distinctCargos = tbPesquisaService.
					findDistinctTbPesquisaNMCargo(familiaEscolhida, subFamiliaEscolhida);

		}else {
			this.distinctCargos =null;
		}
		return distinctCargos;
	}

	public List<Integer> getDistinctGrade(boolean UsaSlider) {
		String mensagem="";
		
		
		if ((familiaEscolhida != null) &&  (subFamiliaEscolhida != null)
				&& (cargoEscolhido!=null) && (mercadoEscolhido!=null)){
			this.distinctGrade = tbPesquisaService.
					findDistinctTbPesquisaGrade(familiaEscolhida, subFamiliaEscolhida, cargoEscolhido, mercadoEscolhido);

			if (this.distinctGrade!=null) {

				if(this.distinctGrade.size()==0) {
					mensagem="Filtro";
				}else {

					this.gradeMinimoPadrao = distinctGrade.get(0);
					this.gradeMaximoPadrao = (Integer) (distinctGrade.get(distinctGrade.size()-1));
				}
				if((UsaSlider==true) && (distinctGrade.size()>0)) {

					this.gradeMinimo= distinctGrade.get(0);
					this.gradeMaximo= (Integer) (distinctGrade.get(distinctGrade.size()-1));
				}else {

				}

				if(user==null) {
					listaDeMedias = tbPesquisaService.findMedia(familiaEscolhida,
							subFamiliaEscolhida,
							cargoEscolhido, mercadoEscolhido, gradeMinimo, gradeMaximo);
				} else {
					if(user.getIdEmpresa()==null) {
						listaDeMedias = tbPesquisaService.findMedia(familiaEscolhida,
								subFamiliaEscolhida,
								cargoEscolhido, mercadoEscolhido, gradeMinimo, gradeMaximo);
					}else {

						Integer existeEmpresaPres = 1;

						if(cargoEscolhido.equals("PRESIDENTE / CEO") || cargoEscolhido.equals("PRESIDENTE DE SUBSIDIÁRIA")) {
							existeEmpresaPres = tbPesquisaService
									.findCountSuaEmpresaPres(user.getIdEmpresa().getDescEmpresa().toUpperCase()
											,cargoEscolhido);
						}

						if(existeEmpresaPres==1) {

							Integer MaiorGradeSuaEmpresa = tbPesquisaService.
									findSuaEmpresaMaiorGrade(familiaEscolhida, subFamiliaEscolhida, cargoEscolhido, 
											mercadoEscolhido, gradeMinimo, gradeMaximo, user.getIdEmpresa().getDescEmpresa().toUpperCase());



							listaDeMedias = tbPesquisaService.findMediaSuaEmpresa(familiaEscolhida,
									subFamiliaEscolhida,
									cargoEscolhido, mercadoEscolhido, gradeMinimo, gradeMaximo,user.getIdEmpresa().getDescEmpresa().toUpperCase(),MaiorGradeSuaEmpresa);

							List<GradeVO> listaGradesEmpresas = tbPesquisaService
									.findGradeSuaEmpresaPres(user.getIdEmpresa().getDescEmpresa().toUpperCase(), cargoEscolhido);
							if(listaGradesEmpresas.size()>0) {
								gradeMinimoEmpresa = listaGradesEmpresas.get(0).getGradeMinimoEmpresa();
								gradeMaximoEmpresa= listaGradesEmpresas.get(0).getGradeMaximoEmpresa();

								if (gradeMinimoEmpresa== gradeMaximoEmpresa) {
									mostrarembarras=false;
								}else {
									mostrarembarras = true;
								}
							}


							if(listaDeMedias==null) {

							}
							else {
								if(listaDeMedias.size()>=5) {
									listaDeMedias.get(0).setDescricaoDoFiltro("Familia: " + familiaEscolhida );
									listaDeMedias.get(1).setDescricaoDoFiltro("SubFamilia: " + subFamiliaEscolhida );
									listaDeMedias.get(2).setDescricaoDoFiltro("Cargo: " + cargoEscolhido );
									listaDeMedias.get(3).setDescricaoDoFiltro("Mercado: " + mercadoEscolhido );
									listaDeMedias.get(4).setDescricaoDoFiltro("Grade entre: " + gradeMinimo + " e " + gradeMaximo );

								}else {
									if(listaDeMedias.size()==4) {
										MediasVO media = new MediasVO();
										listaDeMedias.add(media);
										listaDeMedias.get(0).setDescricaoDoFiltro("Familia: " + familiaEscolhida );
										listaDeMedias.get(1).setDescricaoDoFiltro("SubFamilia: " + subFamiliaEscolhida );
										listaDeMedias.get(2).setDescricaoDoFiltro("Cargo: " + cargoEscolhido );
										listaDeMedias.get(3).setDescricaoDoFiltro("Mercado: " + mercadoEscolhido );
										listaDeMedias.get(4).setDescricaoDoFiltro("Grade entre: " + gradeMinimo + " e " + gradeMaximo );
									}
									if(listaDeMedias.size()==3) {
										MediasVO media = new MediasVO();
										listaDeMedias.add(media);
										listaDeMedias.add(media);
										listaDeMedias.get(0).setDescricaoDoFiltro("Familia: " + familiaEscolhida );
										listaDeMedias.get(1).setDescricaoDoFiltro("SubFamilia: " + subFamiliaEscolhida );
										listaDeMedias.get(2).setDescricaoDoFiltro("Cargo: " + cargoEscolhido );
										listaDeMedias.get(3).setDescricaoDoFiltro("Mercado: " + mercadoEscolhido );
										listaDeMedias.get(4).setDescricaoDoFiltro("Grade entre: " + gradeMinimo + " e " + gradeMaximo );
									}
								}
							}
						}else {
							FacesMessage message = MessageFactory.getMessage(
									"erro_dashboard_pres");
							FacesContext.getCurrentInstance().addMessage(null, message);
							this.distinctGrade =null;
							listaDeMedias = null;
							return null;
						}

						descEmpresaExibir = org.apache.commons
								.lang3.
								StringUtils.
								capitalize(user.getIdEmpresa().getDescEmpresa().toLowerCase());

					}
				}



				if(mensagem.equals("Filtro")) {
					FacesMessage message = MessageFactory.getMessage(
							"erro_dashboard_filtro");
					FacesContext.getCurrentInstance().addMessage(null, message);
					this.distinctGrade =null;
					listaDeMedias = null;
					return null;
				}
				if(listaDeMedias==null) {
					FacesMessage message = MessageFactory.getMessage(
							"erro_dashboard_empresas_insuficientes");
					FacesContext.getCurrentInstance().addMessage(null, message);
					this.distinctGrade =null;
					listaDeMedias = null;

					return null;
				}else {
				}

			}
		}else {
			this.distinctGrade =null;
		}
		return distinctGrade;
	}


	public void setDistinctSubFamilia(List<String> distinctSubFamilia) {
		this.distinctSubFamilia = distinctSubFamilia;
	}


	public void setDistinctCargos(List<String> distinctCargos) {
		this.distinctCargos = distinctCargos;
	}



	public TbPesquisaService getTbPesquisaService() {
		return tbPesquisaService;
	}

	public void setTbPesquisaService(TbPesquisaService tbPesquisaService) {
		this.tbPesquisaService = tbPesquisaService;
	}

	public String getMercadoEscolhido() {
		return mercadoEscolhido;
	}

	public void setMercadoEscolhido(String mercadoEscolhido) {
		this.mercadoEscolhido = mercadoEscolhido;
	}

	public int getGradeEscolhida() {
		return gradeEscolhida;
	}

	public void setGradeEscolhida(int gradeEscolhida) {
		this.gradeEscolhida = gradeEscolhida;
	}


	public List<String> getDistinctMercado() {
		return distinctMercado;
	}

	public void setDistinctMercado(List<String> distinctMercado) {
		this.distinctMercado = distinctMercado;
	}



	public void setDistinctGrade(List<Integer> distinctGrade) {
		this.distinctGrade = distinctGrade;
	}

	public Integer getGradeMinimo() {
		return gradeMinimo;
	}

	public void setGradeMinimo(Integer gradeMinimo) {
		this.gradeMinimo = gradeMinimo;
	}

	public Integer getGradeMaximo() {
		return gradeMaximo;
	}

	public void setGradeMaximo(Integer gradeMaximo) {
		this.gradeMaximo = gradeMaximo;
	}

	public List<MediasVO> getListaDeMedias() {
		return listaDeMedias;
	}

	public void setListaDeMedias(List<MediasVO> listaDeMedias) {
		this.listaDeMedias = listaDeMedias;
	}


	public Integer getGradeMinimoPadrao() {
		return gradeMinimoPadrao;
	}


	public void setGradeMinimoPadrao(Integer gradeMinimoPadrao) {
		this.gradeMinimoPadrao = gradeMinimoPadrao;
	}


	public Integer getGradeMaximoPadrao() {
		return gradeMaximoPadrao;
	}


	public void setGradeMaximoPadrao(Integer gradeMaximoPadrao) {
		this.gradeMaximoPadrao = gradeMaximoPadrao;
	}


	


	public Integer getGradeSlide() {
		return gradeSlide;
	}


	public void setGradeSlide(Integer gradeSlide) {
		this.gradeSlide = gradeSlide;
	}


	


	public List<Integer> getDistinctGrade() {
		return distinctGrade;
	}






	public String getClasse() {
		return classe;
	}



	public void setClasse(String classe) {
		this.classe = classe;
	}



	public UserEntity getUser() {
		return user;
	}



	public void setUser(UserEntity user) {
		this.user = user;
	}



	public String getDescEmpresaExibir() {
		return descEmpresaExibir;
	}



	public void setDescEmpresaExibir(String descEmpresaExibir) {
		this.descEmpresaExibir = descEmpresaExibir;
	}







	public Integer getGradeMinimoEmpresa() {
		return gradeMinimoEmpresa;
	}



	public void setGradeMinimoEmpresa(Integer gradeMinimoEmpresa) {
		this.gradeMinimoEmpresa = gradeMinimoEmpresa;
	}



	public Integer getGradeMaximoEmpresa() {
		return gradeMaximoEmpresa;
	}



	public void setGradeMaximoEmpresa(Integer gradeMaximoEmpresa) {
		this.gradeMaximoEmpresa = gradeMaximoEmpresa;
	}



	public boolean isMostrarembarras() {
		return mostrarembarras;
	}



	public void setMostrarembarras(boolean mostrarembarras) {
		this.mostrarembarras = mostrarembarras;
	}


	public List<TbPesquisa> getTbPesquisaList() {
		return tbPesquisaList;
	}

	public void setTbPesquisaList(List<TbPesquisa> tbPesquisaList) {
		this.tbPesquisaList = tbPesquisaList;
	}

	public String getFamiliaEscolhida() {
		return familiaEscolhida;
	}

	public void setFamiliaEscolhida(String familiaEscolhida) {
		this.familiaEscolhida = familiaEscolhida;
	}

	public String getSubFamiliaEscolhida() {
		return subFamiliaEscolhida;
	}

	public void setSubFamiliaEscolhida(String subFamiliaEscolhida) {
		this.subFamiliaEscolhida = subFamiliaEscolhida;
	}

	public String getCargoEscolhido() {

		return cargoEscolhido;
	}

	public void setCargoEscolhido(String cargoEscolhido) {
		this.cargoEscolhido = cargoEscolhido;
	}

	public static Long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<String> getDistinctFamilia() {
		return distinctFamilia;
	}

	public void setDistinctFamilia(List<String> distinctFamilia) {
		this.distinctFamilia = distinctFamilia;
	}

	

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

}
