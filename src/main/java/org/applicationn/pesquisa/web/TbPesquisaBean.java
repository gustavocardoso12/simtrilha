package org.applicationn.pesquisa.web;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.codec.binary.StringUtils;
import org.apache.poi.util.StringUtil;
import org.applicationn.pesquisa.domain.TbDetalhesAcesso;
import org.applicationn.pesquisa.domain.TbPesquisa;
import org.applicationn.pesquisa.service.TbDetalheAcessoService;
import org.applicationn.pesquisa.service.TbPesquisaService;
import org.applicationn.pesquisa.vo.EmpresasDetalheVO;
import org.applicationn.pesquisa.vo.FiltroVO;
import org.applicationn.pesquisa.vo.GradeVO;
import org.applicationn.pesquisa.vo.MediasVO;
import org.applicationn.simtrilhas.domain.TbCARGOSEntity;
import org.applicationn.simtrilhas.domain.security.UserEntity;
import org.applicationn.simtrilhas.service.security.SecurityWrapper;
import org.applicationn.simtrilhas.service.security.UserService;
import org.applicationn.simtrilhas.web.util.MessageFactory;
import org.primefaces.PrimeFaces;
import org.primefaces.component.export.CSVOptions;
import org.primefaces.component.export.ExporterOptions;
import org.primefaces.event.SlideEndEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LegendPlacement;

@Named("tbPesquisaBean")
@ViewScoped
public class TbPesquisaBean implements Serializable {
	@PersistenceContext(unitName = "Moura")
	private  EntityManager entityManager;
	private FiltroVO filtroVO = new FiltroVO();
	 private List<String> selectedFilters = new ArrayList<>();
	private static final long serialVersionUID = 1L;

	private final String URLpesquisa = "https://www.figma.com/proto/cD4OIlgiMRPPK77bGJzWSz/Apresenta%C3%A7%C3%A3o---Apta-XR-2023?page-id=41%3A60&type=design&node-id=3233-6671&viewport=7014%2C-21526%2C0.68&t=tZPQku4VytbW1f52-1&scaling=scale-down-width&starting-point-node-id=3233%3A6671";

	@Inject
	private TbPesquisaService tbPesquisaService;

	@Inject
	private TbDetalheAcessoService tbDetalheAcessoService;
	
	private List<TbPesquisa> tbPesquisaList;

	private String familiaEscolhida;
	private String subFamiliaEscolhida;
	private String empresaEscolhida;
	private String cargoEscolhido;
	private String mercadoEscolhido= "Mercado AptaXR";
	private int gradeEscolhida;
	private int anoEscolhido = 2021;
	 private String buttonTextSumario = "-";
	 private boolean panelCollapsedSumario = false;
	 public void togglePanel() {
		 panelCollapsedSumario = !panelCollapsedSumario;
	        buttonTextSumario = panelCollapsedSumario ? "+" : "-";
	    }
	 private static int SBSuaEmpresa =0;
	 private static double ICPSuaEmpresa =0;
	 private static double ICPASuaEmpresa =0;
	 private static double ILPSuaEmpresa=0;
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
	private List<String> distinctCargosEmpresa;
	private List<String> distinctMercado;
	private List<Integer> distinctGrade;
	
	private String empresaInsuficiente = "0";
	
	private String paddingBottom = "14px";

	public String getPaddingBottom() {
		return paddingBottom;
	}

	public void setPaddingBottom(String paddingBottom) {
		this.paddingBottom = paddingBottom;
	}

	private List<MediasVO> listaDeMedias;

	private BarChartModel barModel;
	private BarChartModel barModelILP;
	private BarChartModel  barModelCPA;
	private BarChartModel barModelLongo;

	private StreamedContent file;
	private static String OS="";
	private String classe="";

	double p25 = 0;
	double mediana = 0;
	double SuaEmpresa=0;
	double p75 = 0;

	double p25Outros= 0;
	double medianaOutros = 0;
	double SuaEmpresaOutros = 0;
	double p75Outros = 0;

	double MaximoGraficoSB = 0;
	double MaximoGraficosCategorias = 0;

	private boolean RenderizaSB = false;
	private boolean RenderizaICP = false;
	private boolean RenderizaICPA = false;
	private boolean RenderizaILP = false;
	private int exportOption=0;
	
	private String gradeXR;
	
	public String getGradeXR() {
		return gradeXR;
	}

	public void setGradeXR(String gradeXR) {
		this.gradeXR = gradeXR;
	}

	@Inject
	protected UserService userService;

	protected UserEntity user;

	private String descEmpresaExibir;
	
	
	public String getEmpresaInsuficiente() {
		return empresaInsuficiente;
	}

	public void setEmpresaInsuficiente(String empresaInsuficiente) {
		this.empresaInsuficiente = empresaInsuficiente;
	}

	List<TbPesquisa> listSumario = new ArrayList<TbPesquisa>();

	public void prepareNewTbAREA() {
		reset();
	}

	public void ExportarExcel(int exportOption) {
		PrimeFaces current = PrimeFaces.current();
		current.executeScript("PF('statusDialog').show();");
		
		if(exportOption==3 || exportOption==4 || exportOption == 2 || exportOption== 1 ) {
			
			/*List<EmpresasDetalheVO> listDetalhes =tbPesquisaService.
					findDistinctTbPesquisaNMEmpresa(user.getIdEmpresa().getDescEmpresa());
			List<MediasVO> lista = new ArrayList<MediasVO>();
			Exportar.exportarPlanilhav3(lista, user.getIdEmpresa().getDescEmpresa(), cargoEscolhido,
					familiaEscolhida, subFamiliaEscolhida, exportOption,
					distinctCargos, user, tbPesquisaService,listDetalhes,
					mercadoEscolhido,gradeMinimoPadrao,gradeMaximoPadrao,entityManager);*/
			filtroVO.setSalarioBase(selectedFilters.contains("salarioBase"));
	        filtroVO.setTotalEmDinheiro(selectedFilters.contains("totalEmDinheiro"));
	        filtroVO.setTotalemDinheiroAlvo(selectedFilters.contains("totalemDinheiroAlvo"));
	        filtroVO.setRenumeracaoDireta(selectedFilters.contains("renumeracaoDireta"));
	        filtroVO.setRenumeracaoDiretaAlvo(selectedFilters.contains("renumeracaoDiretaAlvo"));
	        filtroVO.setIncentivoCurtoPrazo(selectedFilters.contains("incentivoCurtoPrazo"));
	        filtroVO.setIncentivoCurtoPrazoAlvo(selectedFilters.contains("incentivoCurtoPrazoAlvo"));
	        filtroVO.setIncentivoLongoPrazo(selectedFilters.contains("incentivoLongoPrazo"));
	        filtroVO.setP25(selectedFilters.contains("p25"));
	        filtroVO.setP50(selectedFilters.contains("p50"));
	        filtroVO.setP75(selectedFilters.contains("p75"));
	        filtroVO.setMedia(selectedFilters.contains("media"));
	        filtroVO.setP10(selectedFilters.contains("p10"));
	        filtroVO.setP90(selectedFilters.contains("p90"));
			
	        int existeEmpresaPres = tbPesquisaService
					.findCounEmpresaPres(user.getIdEmpresa().getDescEmpresa().toUpperCase());
	        
	        
			ExportarNew.exportarPlanilhav4(listaDeMedias, 
					user.getIdEmpresa().getDescEmpresa(), cargoEscolhido,
					familiaEscolhida, subFamiliaEscolhida,exportOption, distinctCargos,
					mercadoEscolhido,gradeMinimoPadrao,gradeMaximoPadrao,user,
					tbPesquisaService,entityManager,filtroVO,existeEmpresaPres);
			
				return;
			}
		if(listaDeMedias==null) {

		}else {
			if(listaDeMedias.size()==0) {
			
			}else {
						Exportar.exportarPlanilhav2(listaDeMedias, 
								descEmpresaExibir, cargoEscolhido,
								familiaEscolhida, subFamiliaEscolhida,exportOption, distinctCargos,
								mercadoEscolhido,gradeMinimoPadrao,gradeMaximoPadrao,user,
								tbPesquisaService,entityManager);
			}

		}
		
		String username = SecurityWrapper.getUsername();
		UserEntity user = userService.findUserByUsername(username);
		TbDetalhesAcesso vo = new TbDetalhesAcesso();
		vo.setDataAcesso(new Date());
		vo.setId_user(user.getId());
		SimpleDateFormat formato = new SimpleDateFormat("yyyy/MM");
        String dataFormatada = formato.format(new Date());
		
		vo.setMesAno(dataFormatada);
		if(exportOption==1) {
			vo.setTipoDeAtividade("ACESSO EXCEL CARGO");
		}
		if(exportOption==2) {
			vo.setTipoDeAtividade("ACESSO EXCEL SUBFAMILIA");
		}
		if (exportOption==3) {
			vo.setTipoDeAtividade("ACESSO EXCEL EMPRESA");
		}
		if (exportOption==4) {
			vo.setTipoDeAtividade("ACESSO EXCEL MERCADO");
		}
		
		tbDetalheAcessoService.save(vo);
		current.executeScript("PF('statusDialog').hide();");
	}

	public static boolean isWindows() {
		return OS.contains("win");
	}

	public static boolean isMac() {
		return OS.contains("mac");
	}

	public static boolean isUnix() {
		return (OS.contains("nix") || OS.contains("nux") || OS.contains("aix"));
	}

	public static boolean isSolaris() {
		return OS.contains("sunos");
	}

	public static String getOS(){
		if (isWindows()) {
			return "win";
		} else if (isMac()) {
			return "osx";
		} else if (isUnix()) {
			return "uni";
		} else if (isSolaris()) {
			return "sol";
		} else {
			return "err";
		}
	}

	public StreamedContent getFile() throws FileNotFoundException {
		OS = System.getProperty("os.name").toLowerCase();
		InputStream stream  = null;

		if (isWindows()) {
			System.out.println("This is Windows");
			stream = new FileInputStream("C:/metodologia.pdf");
		} else if (isMac()) {
		} else if (isUnix()) {
			stream = new FileInputStream("/home/Simtrilhas/metodologia.pdf");
		} else if (isSolaris()) {
		} else {
		}

		file = new DefaultStreamedContent(stream, "application/pdf", "Metodologia.pdf");
		return file;
	}


	public ExporterOptions getCsvOptions() {
		return CSVOptions.EXCEL_NORTHERN_EUROPE;
	}


	@PostConstruct
	public void init() {
		try {

			this.distinctFamilia = tbPesquisaService.findDistinctTbPesquisaNMFamilia();
			createBarModels();
			
			selectedFilters = new ArrayList<>();
		    selectedFilters.add("salarioBase");
		    selectedFilters.add("totalEmDinheiro");
		    selectedFilters.add("totalemDinheiroAlvo");
		    selectedFilters.add("renumeracaoDireta");
		    selectedFilters.add("renumeracaoDiretaAlvo");
		    selectedFilters.add("incentivoCurtoPrazo");
		    selectedFilters.add("incentivoCurtoPrazoAlvo");
		    selectedFilters.add("incentivoLongoPrazo");
		    selectedFilters.add("p25");
		    selectedFilters.add("p50");
		    selectedFilters.add("p75");
			user = userService.getCurrentUser();
		}catch (Exception e) {
		}

	}
	private void createBarModels() {
		createBarModel();
		createBarModelILP();

		Axis yAxis = barModelILP.getAxis(AxisType.Y);
		double quantidadeILP = (double) yAxis.getMax();


		createBarModelCPA();


		Axis yAxisICPA = barModelCPA.getAxis(AxisType.Y);
		double quantidadeICPA = (double) yAxisICPA.getMax();



		createBarModelLongo();
		Axis yAxisLongo = barModelLongo.getAxis(AxisType.Y);
		double quantidadeLongo = (double) yAxisLongo.getMax();



		double maximo = Math.max(quantidadeILP, Math.max(quantidadeICPA, quantidadeLongo));

		yAxisLongo.setMax(maximo);
		yAxisICPA.setMax(maximo);
		yAxis.setMax(maximo);
	}

	private BarChartModel initBarModel() {
		BarChartModel model = new BarChartModel();
		int p25 = 0;
		int mediana = 0;
		int sua_empresa = 0;
		int p75 = 0;
		if(listaDeMedias==null) {

		}else {
			for (int i=0; i<listaDeMedias.size();i++) {
				if(listaDeMedias.get(i).getDescRenum().contains("SBM - Salário Base Mensal")){
					p25 =  listaDeMedias.get(i).getP25();
					mediana = listaDeMedias.get(i).getP50();
					p75 = listaDeMedias.get(i).getP75();
					sua_empresa = listaDeMedias.get(i).getSua_empresa();
					if(SBSuaEmpresa==0) {
						SBSuaEmpresa =  listaDeMedias.get(i).getSua_empresa();
					}else {
						sua_empresa = SBSuaEmpresa;
					}
					
					break;
				}
			}
		}

		if(p25>=mediana) {
			MaximoGraficoSB = p25 *1.2;
		}else if(mediana>=p25) {
			MaximoGraficoSB = mediana *1.2;
		}

		if(sua_empresa>=MaximoGraficoSB) {
			MaximoGraficoSB = sua_empresa *1.2;
		}

		if(p25>0 && mediana>0 && p75>0 &&  MaximoGraficoSB>0) {
			RenderizaSB = true;
		}

		if(p25>0 && mediana>0 && p75>0 && MaximoGraficoSB>0) {
			RenderizaSB = true;
		}

		if(p25>=0 && mediana>0 && p75>0 && MaximoGraficoSB>0) {
			RenderizaSB = true;
		}

		if(p25>0 && mediana>=0 && p75>0 && MaximoGraficoSB>0) {
			RenderizaSB = true;
		}


		ChartSeries boys = new ChartSeries();
		boys.setLabel("Percentil 50 (Mediana)");
		boys.set("Salário Base Mensal", mediana);

		ChartSeries girls = new ChartSeries();
		girls.setLabel("Percentil 25 (P25)");
		girls.set("Salário Base Mensal", p25);     

		ChartSeries girls2 = new ChartSeries();
		girls2.setLabel("Sua empresa");
		girls2.set("Salário Base Mensal", sua_empresa);

		ChartSeries girls3 = new ChartSeries();
		girls3.setLabel("Percentil 75 (P75)");
		girls3.set("Salário Base Mensal",p75);


		model.addSeries(girls2);
		model.addSeries(girls);
		model.addSeries(boys);
		model.addSeries(girls3);

		return model;
	}


	private int mediasEMedianas(String SB, String outrosItens) {

		int encontrou = 1;
		if(listaDeMedias==null) {

		}else {
			for (int i=0; i<listaDeMedias.size();i++) {
				if(listaDeMedias.get(i).getDescRenum().contains(SB)){
					p25 =  listaDeMedias.get(i).getP25();
					mediana = listaDeMedias.get(i).getP50();
					SuaEmpresa = listaDeMedias.get(i).getSua_empresa();
					p75= listaDeMedias.get(i).getP75();
				}

				if(listaDeMedias.get(i).getDescRenum().contains(outrosItens)){
					p25Outros =  listaDeMedias.get(i).getP25();
					medianaOutros = listaDeMedias.get(i).getP50();
					SuaEmpresaOutros = listaDeMedias.get(i).getSua_empresa();
					p75Outros= listaDeMedias.get(i).getP75();
					encontrou = 1;
					return encontrou;
				}else {
					encontrou=0;
				}
			}
		}
		return encontrou;
	}

	public static double round(double value, int places) {
		if (places < 0) throw new IllegalArgumentException();

		BigDecimal bd = BigDecimal.valueOf(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}


	private BarChartModel initBarModelILP() {
		BarChartModel model = new BarChartModel();

		int flagEncontrou = mediasEMedianas("SBM - Salário Base Mensal","ICP - Incentivo de Curto Prazo Pago (Bônus + PLR)");

		if(ICPSuaEmpresa!=0) {
			flagEncontrou =1;
		}

		if(flagEncontrou==0.0) {
			medianaOutros=0.0;
			p25Outros=0.0;
			SuaEmpresaOutros=0.0;
			p75Outros=0.0;
		}

		ChartSeries boys = new ChartSeries();
		double medianaResultado = 0.0;
		double p25Resultado =0.0;
		double suaEmpresa= 0.0;
		double p75Resultado = 0.0;

		if(medianaOutros==0.0) {
			medianaResultado=0.0;
		}else {
			medianaResultado = round(medianaOutros / mediana,1);

		}

		if(p25Outros==0.0) {
			p25Resultado=0.0;
		}else {
			p25Resultado = round(p25Outros / p25,1);
		}

		if(SuaEmpresaOutros==0.0) {
			suaEmpresa=ICPSuaEmpresa;
		}else {
			if(ICPSuaEmpresa==0) {
				ICPSuaEmpresa =  round(SuaEmpresaOutros / SuaEmpresa,1);
				suaEmpresa=ICPSuaEmpresa;
			}else {
				suaEmpresa = ICPSuaEmpresa;
			}
		}

		if(p75Outros==0.0) {
			p75Resultado=0.0;
		}else {
			p75Resultado = round(p75Outros / p75,1);
		}


		if(p25Resultado>=medianaResultado) {
			MaximoGraficosCategorias = p25Resultado *1.2;
		}else if(medianaResultado>=p25Resultado) {
			MaximoGraficosCategorias = medianaResultado *1.2;
		}

		if(suaEmpresa>=MaximoGraficosCategorias) {
			MaximoGraficosCategorias = suaEmpresa *1.2;
		}

		if(p25Resultado>0 && medianaResultado>0 && MaximoGraficosCategorias>0) {
			RenderizaICP = true;
		}

		if(p25Resultado>=0 && medianaResultado>0 &&  MaximoGraficosCategorias>0) {
			RenderizaICP = true;
		}

		if(p25Resultado>0 && medianaResultado>=0 &&  MaximoGraficosCategorias>0) {
			RenderizaICP = true;
		}



		boys.setLabel("Percentil 50 (Mediana)");
		boys.set("Incentivo de Curto Prazo Pago",medianaResultado);

		ChartSeries girls = new ChartSeries();
		girls.setLabel("Percentil 25 (P25)");
		girls.set("Incentivo de Curto Prazo Pago", p25Resultado);


		ChartSeries girls2 = new ChartSeries();
		girls2.setLabel("Sua empresa");
		girls2.set("Incentivo de Curto Prazo Pago", suaEmpresa);

		ChartSeries girls3 = new ChartSeries();
		girls3.setLabel("Percentil 75 (P75)");
		girls3.set("Incentivo de Curto Prazo Pago", p75Resultado);

		model.addSeries(girls2);
		model.addSeries(girls);
		model.addSeries(boys);
		model.addSeries(girls3);
		return model;
	}


	private BarChartModel initBarModelCPA() {
		BarChartModel model = new BarChartModel();

		int flagEncontrou = mediasEMedianas("SBM - Salário Base Mensal","ICPA - Incentivo de Curto Prazo Alvo (Bônus + PLR)");
		
		if(ICPASuaEmpresa!=0) {
			flagEncontrou =1;
		}
		
		if(flagEncontrou==0.0) {
			medianaOutros=0.0;
			p25Outros=0.0;
			SuaEmpresaOutros=0.0;
			p75Outros=0.0;
		}

		ChartSeries boys = new ChartSeries();
		double medianaResultado = 0.0;
		double p25Resultado =0.0;
		double suaEmpresa= 0.0;
		double p75Resultado = 0.0;


		if(medianaOutros==0.0) {
			medianaResultado=0.0;
		}else {
			medianaResultado = round(medianaOutros / mediana,1);

		}

		if(p25Outros==0.0) {
			p25Resultado=0.0;
		}else {
			p25Resultado = round(p25Outros / p25,1);
		}


		if(SuaEmpresaOutros==0.0) {
			suaEmpresa=ICPASuaEmpresa;
		}else {
			if(ICPASuaEmpresa==0) {
				ICPASuaEmpresa =  round(SuaEmpresaOutros / SuaEmpresa,1);
				suaEmpresa=ICPASuaEmpresa;
			}else {
				suaEmpresa = ICPASuaEmpresa;
			}
		}

		if(p75Outros==0.0) {
			p75Resultado=0.0;
		}else {
			p75Resultado = round(p75Outros / p75,1);
		}


		if(p25Resultado>=medianaResultado) {
			MaximoGraficosCategorias = p25Resultado *1.2;
		}else if(medianaResultado>=p25Resultado) {
			MaximoGraficosCategorias = medianaResultado *1.2;
		}

		if(suaEmpresa>=MaximoGraficosCategorias) {
			MaximoGraficosCategorias = suaEmpresa *1.2;
		}

		if(p25Resultado>0 && medianaResultado>0 && MaximoGraficosCategorias>0) {
			RenderizaICPA = true;
		}

		if(p25Resultado>=0 && medianaResultado>0 &&  MaximoGraficosCategorias>0) {
			RenderizaICPA = true;
		}

		if(p25Resultado>0 && medianaResultado>=0 && MaximoGraficosCategorias>0) {
			RenderizaICPA = true;
		}


		boys.setLabel("Percentil 50 (Mediana)");
		boys.set("Incentivo de Curto Prazo Alvo",medianaResultado);

		ChartSeries girls = new ChartSeries();
		girls.setLabel("Percentil 25 (P25)");
		girls.set("Incentivo de Curto Prazo Alvo", p25Resultado);


		ChartSeries girls2 = new ChartSeries();
		girls2.setLabel("Sua empresa");
		girls2.set("Incentivo de Curto Prazo Alvo", suaEmpresa);

		ChartSeries girls3 = new ChartSeries();
		girls3.setLabel("Percentil 75 (P75)");
		girls3.set("Incentivo de Curto Prazo Alvo", p75Resultado);

		model.addSeries(girls2);
		model.addSeries(girls);
		model.addSeries(boys);

		model.addSeries(girls3);

		return model;
	}


	private BarChartModel initBarModelLongo() {
		BarChartModel model = new BarChartModel();

		int flagEncontrou = mediasEMedianas("SBM - Salário Base Mensal","ILP - Incentivos de Longo Prazo");
		
		if(ILPSuaEmpresa!=0) {
			flagEncontrou =1;
		}
		
		if(flagEncontrou==0.0) {
			medianaOutros=0.0;
			p25Outros=0.0;
			SuaEmpresaOutros=0.0;
			p75Outros=0.0;
		}

		ChartSeries boys = new ChartSeries();
		double medianaResultado = 0.0;
		double p25Resultado =0.0;
		double suaEmpresa= 0.0;
		double p75Resultado = 0.0;


		if(medianaOutros==0.0) {
			medianaResultado=0.0;
		}else {
			medianaResultado = round(medianaOutros / mediana,1);

		}

		if(p25Outros==0.0) {
			p25Resultado=0.0;
		}else {
			p25Resultado = round(p25Outros / p25,1);
		}

		if(SuaEmpresaOutros==0.0) {
			suaEmpresa=ILPSuaEmpresa;
		}else {
			if(ILPSuaEmpresa==0) {
				ILPSuaEmpresa =  round(SuaEmpresaOutros / SuaEmpresa,1);
				suaEmpresa=ILPSuaEmpresa;
			}else {
				suaEmpresa = ILPSuaEmpresa;
			}
		}

		if(p75Outros==0.0) {
			p75Resultado=0.0;
		}else {
			p75Resultado = round(p75Outros / p75,1);
		}


		if(p25Resultado>=medianaResultado) {
			MaximoGraficosCategorias = p25Resultado *1.2;
		}else if(medianaResultado>=p25Resultado) {
			MaximoGraficosCategorias = medianaResultado *1.2;
		}

		if(suaEmpresa>=MaximoGraficosCategorias) {
			MaximoGraficosCategorias = suaEmpresa *1.2;
		}

		if(p25Resultado>0 && medianaResultado>0 &&  MaximoGraficosCategorias>0) {
			RenderizaILP = true;
			classe="Derivado";
		}

		else if(p25Resultado>=0 && medianaResultado>0 &&  MaximoGraficosCategorias>0) {
			RenderizaILP = true;
			classe="Derivado";
		}

		else if(p25Resultado>0 && medianaResultado>=0 &&  MaximoGraficosCategorias>0) {
			RenderizaILP = true;
			classe="Derivado";
		}
		else {
			RenderizaILP = false;
			classe="ILP";
		}



		boys.setLabel("Percentil 50 (Mediana)");
		boys.set("Incentivo de Longo Prazo",medianaResultado);

		ChartSeries girls = new ChartSeries();
		girls.setLabel("Percentil 25 (P25)");
		girls.set("Incentivo de Longo Prazo", p25Resultado);


		ChartSeries girls2 = new ChartSeries();
		girls2.setLabel("Sua empresa");
		girls2.set("Incentivo de Longo Prazo", suaEmpresa);

		ChartSeries girls3 = new ChartSeries();
		girls3.setLabel("Percentil 75 (P75)");
		girls3.set("Incentivo de Longo Prazo", p75Resultado);

		model.addSeries(girls2);
		model.addSeries(girls);
		model.addSeries(boys);

		model.addSeries(girls3);


		return model;
	}


	private void createBarModel() {
		barModel = initBarModel();

		barModel.setTitle("");
		barModel.setLegendPosition("n");
		barModel.setLegendPlacement(LegendPlacement.OUTSIDEGRID);
		barModel.setLegendCols(4);
		barModel.setExtender("extndr");
		barModel.setSeriesColors("FF5250, AE78D6, 7030A0, 3C1A56");
		barModel.setDatatipFormat(getDatatipFormatX());
		Axis yAxis = barModel.getAxis(AxisType.Y);
		yAxis.setMin(0);
		yAxis.setMax(MaximoGraficoSB*1.30);
		yAxis.setLabel(" ");
	}

	private void createBarModelILP() {
		barModelILP = initBarModelILP();

		barModelILP.setTitle("");
		barModelILP.setLegendPosition("n");
		barModelILP.setLegendPlacement(LegendPlacement.OUTSIDEGRID);
		barModelILP.setLegendCols(4);
		barModelILP.setExtender("myExtender");
		barModelILP.setSeriesColors("FF5250, AE78D6, 7030A0, 3C1A56");
		barModelILP.setDatatipFormat(getDatatipFormatX());
		Axis yAxis = barModelILP.getAxis(AxisType.Y);
		yAxis.setMin(0);
		yAxis.setMax(MaximoGraficosCategorias*1.30);
		yAxis.setLabel(" ");
	}

	private void createBarModelCPA() {
		barModelCPA = initBarModelCPA();

		barModelCPA.setTitle("");
		barModelCPA.setLegendPosition("n");
		barModelCPA.setLegendPlacement(LegendPlacement.OUTSIDEGRID);
		barModelCPA.setLegendCols(4);
		barModelCPA.setExtender("myExtender");
		barModelCPA.setSeriesColors("FF5250, AE78D6, 7030A0, 3C1A56");
		barModelCPA.setDatatipFormat(getDatatipFormatX());
		Axis yAxis = barModelCPA.getAxis(AxisType.Y);
		yAxis.setMin(0);
		yAxis.setMax(MaximoGraficosCategorias*1.30);
		yAxis.setLabel(" ");
	}

	private void createBarModelLongo() {
		barModelLongo = initBarModelLongo();

		barModelLongo.setTitle("");
		barModelLongo.setLegendPosition("n");
		barModelLongo.setLegendPlacement(LegendPlacement.OUTSIDEGRID);
		barModelLongo.setLegendCols(4);
		barModelLongo.setExtender("myExtender");
		barModelLongo.setSeriesColors("FF5250, AE78D6, 7030A0, 3C1A56");
		barModelLongo.setDatatipFormat(getDatatipFormatX());
		Axis yAxis = barModelLongo.getAxis(AxisType.Y);
		yAxis.setMin(0);
		yAxis.setMax(MaximoGraficosCategorias*1.30);
		yAxis.setLabel(" ");
	}

	public String getDatatipFormatX() {
		return "R$s";
	}

	public void onSlideEnd(SlideEndEvent event) {
		this.gradeSlide = (int) event.getValue();
		getDistinctGrade(false,false);
	}


	public void reset() {


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

	public List<String> getDistinctSubFamilia() {

		if (familiaEscolhida != null) {
			this.distinctSubFamilia = tbPesquisaService.findDistinctTbPesquisaNMSubFamilia(familiaEscolhida);
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
	
	
	
	public List<Integer> getDistinctGrade(boolean UsaSlider, boolean primeiraExecucao) {
		String mensagem="";


		if ((familiaEscolhida != null) &&  (subFamiliaEscolhida != null)
				&& (cargoEscolhido!=null) && (mercadoEscolhido!=null)){
			this.distinctGrade = tbPesquisaService.
					findDistinctTbPesquisaGrade(familiaEscolhida, subFamiliaEscolhida, cargoEscolhido, mercadoEscolhido);

			if (this.distinctGrade!=null) {
				
				
				this.listSumario = tbPesquisaService.findDescricaoCargo(familiaEscolhida, subFamiliaEscolhida, cargoEscolhido);

				if(this.distinctGrade.size()==0) {
					mensagem="Filtro";
				}else {

					this.gradeMinimoPadrao = distinctGrade.get(0);
					this.gradeMaximoPadrao = (Integer) (distinctGrade.get(distinctGrade.size()-1));
				}
				if((UsaSlider==true) && (distinctGrade.size()>0)) {
					
					if(primeiraExecucao) {
						SBSuaEmpresa=0;
						ICPASuaEmpresa=0;
						ICPSuaEmpresa=0;
						ILPSuaEmpresa=0;
						//int grade  = tbPesquisaService.findGradeInicioMenu(user.getIdEmpresa().getDescEmpresa(),cargoEscolhido);
						/*if(grade==0) {
							this.gradeMinimo= distinctGrade.get(0);
							this.gradeMaximo= (Integer) (distinctGrade.get(distinctGrade.size()-1));
						}else {
							
						}*/
						
						this.gradeMinimo= distinctGrade.get(0);
						this.gradeMaximo= (Integer) (distinctGrade.get(distinctGrade.size()-1));
						
					}else {
						this.gradeMinimo= distinctGrade.get(0);
						this.gradeMaximo= (Integer) (distinctGrade.get(distinctGrade.size()-1));
					}
					
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
									cargoEscolhido, mercadoEscolhido, gradeMinimo, gradeMaximo,user.getIdEmpresa().getDescEmpresa().toUpperCase(),MaiorGradeSuaEmpresa,entityManager);

							List<GradeVO> listaGradesEmpresas = tbPesquisaService
									.findGradeSuaEmpresaPres(user.getIdEmpresa().getDescEmpresa().toUpperCase(), cargoEscolhido);
							
						
							if(listaGradesEmpresas.size()>0) {
								gradeMinimoEmpresa = listaGradesEmpresas.get(0).getGradeMinimoEmpresa();
								gradeMaximoEmpresa= listaGradesEmpresas.get(0).getGradeMaximoEmpresa();

								
								gradeXR = tbPesquisaService
										.findGradeXR(user.getIdEmpresa().getDescEmpresa().toUpperCase(),
												cargoEscolhido,
												gradeMinimoEmpresa,
												gradeMaximoEmpresa);
								
								
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
					createBarModels();
				}
				
				if(gradeMinimoEmpresa==0) {
					empresaInsuficiente = "1";
					paddingBottom= "0px";
				}else {
					empresaInsuficiente = "2";
					paddingBottom= "9px";
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

	public int getAnoEscolhido() {
		return anoEscolhido;
	}

	public void setAnoEscolhido(int anoEscolhido) {
		this.anoEscolhido = anoEscolhido;
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


	public BarChartModel getBarModel() {
		return barModel;
	}


	public void setBarModel(BarChartModel barModel) {
		this.barModel = barModel;
	}


	public BarChartModel getBarModelILP() {
		return barModelILP;
	}


	public void setBarModelILP(BarChartModel barModelILP) {
		this.barModelILP = barModelILP;
	}


	public BarChartModel getBarModelCPA() {
		return barModelCPA;
	}


	public void setBarModelCPA(BarChartModel barModelCPA) {
		this.barModelCPA = barModelCPA;
	}


	public Integer getGradeSlide() {
		return gradeSlide;
	}


	public void setGradeSlide(Integer gradeSlide) {
		this.gradeSlide = gradeSlide;
	}


	public BarChartModel getBarModelLongo() {
		return barModelLongo;
	}


	public void setBarModelLongo(BarChartModel barModelLongo) {
		this.barModelLongo = barModelLongo;
	}


	public List<Integer> getDistinctGrade() {
		return distinctGrade;
	}



	public boolean isRenderizaSB() {
		return RenderizaSB;
	}



	public void setRenderizaSB(boolean renderizaSB) {
		RenderizaSB = renderizaSB;
	}



	public boolean isRenderizaILP() {
		return RenderizaILP;
	}



	public void setRenderizaILP(boolean renderizaILP) {
		RenderizaILP = renderizaILP;
	}



	public boolean isRenderizaICPA() {
		return RenderizaICPA;
	}



	public void setRenderizaICPA(boolean renderizaICPA) {
		RenderizaICPA = renderizaICPA;
	}



	public boolean isRenderizaICP() {
		return RenderizaICP;
	}



	public void setRenderizaICP(boolean renderizaICP) {
		RenderizaICP = renderizaICP;
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



	public String getURLpesquisa() {
		return URLpesquisa;
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

	public double getP25() {
		return p25;
	}

	public void setP25(double p25) {
		this.p25 = p25;
	}

	public double getMediana() {
		return mediana;
	}

	public void setMediana(double mediana) {
		this.mediana = mediana;
	}

	public double getSuaEmpresa() {
		return SuaEmpresa;
	}

	public void setSuaEmpresa(double suaEmpresa) {
		SuaEmpresa = suaEmpresa;
	}

	public double getP75() {
		return p75;
	}

	public void setP75(double p75) {
		this.p75 = p75;
	}

	public double getP25Outros() {
		return p25Outros;
	}

	public void setP25Outros(double p25Outros) {
		this.p25Outros = p25Outros;
	}

	public double getMedianaOutros() {
		return medianaOutros;
	}

	public void setMedianaOutros(double medianaOutros) {
		this.medianaOutros = medianaOutros;
	}

	public double getSuaEmpresaOutros() {
		return SuaEmpresaOutros;
	}

	public void setSuaEmpresaOutros(double suaEmpresaOutros) {
		SuaEmpresaOutros = suaEmpresaOutros;
	}

	public double getP75Outros() {
		return p75Outros;
	}

	public void setP75Outros(double p75Outros) {
		this.p75Outros = p75Outros;
	}

	public double getMaximoGraficoSB() {
		return MaximoGraficoSB;
	}

	public void setMaximoGraficoSB(double maximoGraficoSB) {
		MaximoGraficoSB = maximoGraficoSB;
	}

	public double getMaximoGraficosCategorias() {
		return MaximoGraficosCategorias;
	}

	public void setMaximoGraficosCategorias(double maximoGraficosCategorias) {
		MaximoGraficosCategorias = maximoGraficosCategorias;
	}

	public int getExportOption() {
		return exportOption;
	}

	public void setExportOption(int exportOption) {
		this.exportOption = exportOption;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void setFile(StreamedContent file) {
		this.file = file;
	}

	public static void setOS(String oS) {
		OS = oS;
	}

	public List<TbPesquisa> getListSumario() {
		return listSumario;
	}

	public void setListSumario(List<TbPesquisa> listSumario) {
		this.listSumario = listSumario;
	}

	public String getButtonTextSumario() {
		return buttonTextSumario;
	}

	public void setButtonTextSumario(String buttonTextSumario) {
		this.buttonTextSumario = buttonTextSumario;
	}

	public boolean isPanelCollapsedSumario() {
		return panelCollapsedSumario;
	}

	public void setPanelCollapsedSumario(boolean panelCollapsedSumario) {
		this.panelCollapsedSumario = panelCollapsedSumario;
	}

	public TbDetalheAcessoService getTbDetalheAcessoService() {
		return tbDetalheAcessoService;
	}

	public void setTbDetalheAcessoService(TbDetalheAcessoService tbDetalheAcessoService) {
		this.tbDetalheAcessoService = tbDetalheAcessoService;
	}

	public String getEmpresaEscolhida() {
		return empresaEscolhida;
	}

	public void setEmpresaEscolhida(String empresaEscolhida) {
		this.empresaEscolhida = empresaEscolhida;
	}



	public void setDistinctCargosEmpresa(List<String> distinctCargosEmpresa) {
		this.distinctCargosEmpresa = distinctCargosEmpresa;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public FiltroVO getFiltroVO() {
		return filtroVO;
	}

	public void setFiltroVO(FiltroVO filtroVO) {
		this.filtroVO = filtroVO;
	}

	public List<String> getDistinctCargosEmpresa() {
		return distinctCargosEmpresa;
	}

	public List<String> getSelectedFilters() {
		return selectedFilters;
	}

	public void setSelectedFilters(List<String> selectedFilters) {
		this.selectedFilters = selectedFilters;
	}



}
