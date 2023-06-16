package org.applicationn.pesquisa.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;

import org.applicationn.pesquisa.domain.TbPesquisa;
import org.applicationn.pesquisa.vo.GradeVO;
import org.applicationn.pesquisa.vo.MediasVO;
import org.applicationn.simtrilhas.service.BaseService;
import org.applicationn.simtrilhas.service.security.SecurityWrapper;
import org.applicationn.simtrilhas.web.util.MessageFactory;
@Named
@TransactionManagement(TransactionManagementType.BEAN)
public class TbPesquisaService extends BaseService<TbPesquisa> implements Serializable {

	private static final long serialVersionUID = 1L;

	//@Inject
	//private TbDEPTOService tbDEPTOService;


	public TbPesquisaService(){
		super(TbPesquisa.class);
	}

	@Transactional
	public List<TbPesquisa> findAllTbPesquisa() {
		return getEntityManagerMatriz().createQuery("SELECT o FROM TbPesquisa o ",
				TbPesquisa.class).getResultList();
	}

	@Override
	@Transactional
	public long countAllEntries() {
		return getEntityManagerMatriz().createQuery("SELECT COUNT(o) FROM TbPesquisa o", Long.class).getSingleResult();
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public List<String> findDistinctTbPesquisaNMFamilia() {


		List<String> result = new ArrayList<>();
		result =  getEntityManagerMatriz().createNativeQuery(
				"select distinct(nm_familia) from TB_PESQUISA order by nm_familia").getResultList();
		return result;

	}

	@Transactional
	public List<String> findDistinctTbPesquisaNMSubFamilia(String NmFamilia) {

		return getEntityManagerMatriz().createQuery("SELECT DISTINCT(o.nmSubFamilia) FROM TbPesquisa "
				+ "o WHERE o.nmFamilia =:descFamilia order by o.nmSubFamilia", String.class).
				setParameter("descFamilia",NmFamilia).getResultList();
	}



	@Transactional
	public List<String> findDistinctTbPesquisaNMCargo(String nmFamilia, String nmSubFamilia) {

		return getEntityManagerMatriz().createQuery("SELECT DISTINCT(o.nomeCargo) FROM TbPesquisa "
				+ "o WHERE o.nmFamilia =:descFamilia "
				+ "AND o.nmSubFamilia =:descSubFamilia "
				+ "order by o.nomeCargo", String.class).
				setParameter("descFamilia",nmFamilia).
				setParameter("descSubFamilia",nmSubFamilia).
				getResultList();
	}




	@SuppressWarnings("unchecked")
	@Transactional
	public List<String> findDistinctTbPesquisaMercado(String nmFamilia, String nmSubFamilia,
			String nmCargo) {

		return getEntityManagerMatriz().createNativeQuery(""
				+ "select distinct(tm.dsMercado ) from tb_pesquisa tp "
				+ "inner join TB_PESQUISA_MERCADO tpm "
				+ "on (tp.codigo_empresa = tpm.cd_empresa) "
				+ "inner join tb_mercado tm "
				+ "on (tpm.cd_mercado = tm.id) "
				+ "where nm_familia =:descFamilia"
				+ "and nm_subfamilia =:descSubFamilia "
				+ "and nome_cargo = :descCargo"
				+ "order by tm.dsMercado ", String.class).
				setParameter("descFamilia",nmFamilia).
				setParameter("descSubFamilia",nmSubFamilia).
				setParameter("descCargo",nmCargo).
				getResultList();
	}


	@SuppressWarnings("unchecked")
	@Transactional
	public List<Integer> findDistinctTbPesquisaGrade(String nmFamilia, String nmSubFamilia,
			String nmCargo, String mercGeral) {

		return (List<Integer>) getEntityManagerMatriz().createNativeQuery("select distinct(tp.grade) from tb_pesquisa tp "
				+ "inner join TB_PESQUISA_MERCADO tpm "
				+ "on (tp.codigo_empresa = tpm.cd_empresa) "
				+ "inner join tb_mercado tm "
				+ "on (tpm.cd_mercado = tm.id) "
				+ "where nm_familia =:descFamilia "
				+ "and nm_subfamilia =:descSubFamilia "
				+ "and nome_cargo =:descCargo "
				+ "and tm.dsMercado =:mercGeral "
				+ "order by tp.grade ").
				setParameter("descFamilia",nmFamilia).
				setParameter("descSubFamilia",nmSubFamilia).
				setParameter("descCargo",nmCargo).
				setParameter("mercGeral",mercGeral).
				getResultList();
	}




	@Transactional
	public Integer findCountEmpresas(String nmFamilia, String nmSubFamilia,
			String nmCargo, String mercGeral,
			Integer gradeMinimo, Integer gradeMaximo) {

		return (Integer) getEntityManagerMatriz().createNativeQuery("select count(1) as quantidade from tb_pesquisa tp"
				+ "inner join TB_PESQUISA_MERCADO tpm "
				+ "on (tp.codigo_empresa = tpm.cd_empresa)"
				+ "inner join tb_mercado tm "
				+ "on (tpm.cd_mercado = tm.id)"
				+ "where nm_familia =:descFamilia "
				+ "and nm_subfamilia =:descSubFamilia "
				+ "and nome_cargo =:descCargo "
				+ "and tm.dsMercado =:mercGeral "
				+ "and tp.grade >= :gradeMinimo and tp.grade <= :gradeMaximo "
				+ "group by tp.nm_familia,tp.nm_subfamilia,tp.nome_cargo").
				setParameter("descFamilia",nmFamilia).
				setParameter("descSubFamilia",nmSubFamilia).
				setParameter("descCargo",nmCargo).
				setParameter("mercGeral",mercGeral).
				setParameter("gradeMinimo",gradeMinimo).
				setParameter("gradeMaximo",gradeMaximo).
				getFirstResult();
	}
	@SuppressWarnings("unchecked")
	@Transactional
	public List<TbPesquisa>  findDescricaoCargo(String nmFamilia, String nmSubFamilia,
			String nmCargo) {
		List<TbPesquisa> listaFinal = new ArrayList<TbPesquisa>();
		List<Object> lista =  getEntityManagerMatriz().
				createNativeQuery("select descricao_completa_cargos, nome_cargo, count(1) qtd"
						+ "  from tb_pesquisa \n"
						+ "where nm_familia =:descFamilia\n"
						+ "and nm_subfamilia =:descSubFamilia \n"
						+ "and nome_cargo =:descCargo \n"
						+ " group by descricao_completa_cargos, nome_cargo").
				setParameter("descFamilia",nmFamilia).
				setParameter("descSubFamilia",nmSubFamilia).
				setParameter("descCargo",nmCargo).getResultList();


		for(Object record: lista) {
			Object[] linha = (Object[]) record; 
			TbPesquisa pesquisa = new TbPesquisa();
			pesquisa.setNomeCargo((String) linha[1]);
			pesquisa.setDescricao_completa_cargos((String) linha[0]);

			listaFinal.add(pesquisa);
		}



		return listaFinal;
	}

	@Transactional
	@SuppressWarnings("unchecked")
	public List<GradeVO>  findGradeSuaEmpresaPres(String nomeEmpresa, String nomeCargo) {

		List<GradeVO> mediasFinal = new ArrayList<GradeVO>();
		List<Object> lista = getEntityManagerMatriz().
				createNativeQuery("select COALESCE (MIN(grade),0) minimo, COALESCE (MAX(grade),0) maximo from TB_PESQUISA tp "
						+ "where 1=1 "
						+ "AND nome_cargo = :nomeCargo "
						+ "AND nome_empresa = :nomeEmpresa ").
				setParameter("nomeEmpresa", nomeEmpresa).
				setParameter("nomeCargo", nomeCargo)
				.getResultList();

		for(Object record: lista) {
			Object[] linha = (Object[]) record; 
			GradeVO medias = new GradeVO();
			medias.setGradeMinimoEmpresa((int) linha[0]);
			medias.setGradeMaximoEmpresa((int) linha[1]);

			mediasFinal.add(medias);
		}


		return mediasFinal ;
	}



	@Transactional
	@SuppressWarnings("unchecked")
	public String findGradeXR(String nomeEmpresa, String nomeCargo, int gradeMaximo) {
		String gradeXR = "";
		List<Object> lista = getEntityManagerMatriz().
				createNativeQuery("select grade_xr from TB_PESQUISA tp "
						+ "  where 1=1 "
						+ "  AND nome_cargo = :nomeCargo "
						+ "  AND nome_empresa = :nomeEmpresa "
						+  " and GRADE = :gradeMaximo"
						+ "  and grade_xr is not null ").
				setParameter("nomeEmpresa", nomeEmpresa).
				setParameter("nomeCargo", nomeCargo).
				setParameter("gradeMaximo", gradeMaximo)
				.getResultList();

		for(Object record: lista) {

			 gradeXR = (String) record;

		}


		return gradeXR ;
	}

	@Transactional
	public Integer findCountSuaEmpresaPres(String nomeEmpresa, String nomeCargo) {
		Integer quantidade = (Integer) getEntityManagerMatriz().createNativeQuery(" select COUNT(1) as quantidade "
				+ " from TB_PESQUISA tp"
				+ " where nome_cargo =:nomeCargo "
				+ "and nome_empresa = :nomeEmpresa").
				setParameter("nomeEmpresa", nomeEmpresa).
				setParameter("nomeCargo", nomeCargo)
				.getSingleResult();
		System.out.println(quantidade);
		return quantidade ;
	}


	@Transactional
	public Integer findSuaEmpresaMaiorGrade(String nmFamilia, String nmSubFamilia,
			String nmCargo, String mercGeral,
			Integer gradeMinimo, Integer gradeMaximo, String nomeEmpresa) {

		Integer quantidade = 0;
		try{ quantidade= (Integer) getEntityManagerMatriz().createNativeQuery("select max(grade) max_grade  from tb_pesquisa tp \n"
				+ "inner join TB_PESQUISA_MERCADO tpm \n"
				+ "on (tp.codigo_empresa = tpm.cd_empresa) \n"
				+ "inner join tb_mercado tm \n"
				+ "on (tpm.cd_mercado = tm.id) \n"
				+ " where          1=1          and nm_familia = :nmFamilia\n"
				+ "						and nm_subfamilia =:nmSubFamilia\n"
				+ "						and nome_cargo = :nmCargo\n"
				+ "		 				and tp.nome_empresa = :nomeEmpresa\n"
				+ "                        and tp.grade >= :gradeMinimo\n"
				+ "                        and tp.grade <= :gradeMaximo \n"
				+ "and media_sb is not null \n"
				+ "group by nome_cargo,nome_empresa").
				setParameter("nmFamilia",nmFamilia).
				setParameter("nmSubFamilia",nmSubFamilia).
				setParameter("nmCargo",nmCargo).
				setParameter("gradeMinimo",gradeMinimo).
				setParameter("gradeMaximo",gradeMaximo).
				setParameter("nomeEmpresa",nomeEmpresa)
				.getSingleResult();} catch(NoResultException ex) {
					return 0;
				}
		return quantidade ;
	}




	@SuppressWarnings("unchecked")
	@Transactional
	public List<MediasVO> findMedia(String nmFamilia, String nmSubFamilia,
			String nmCargo, String mercGeral,
			Integer gradeMinimo, Integer gradeMaximo) {

		List<MediasVO> mediasFinal = new ArrayList<MediasVO>();

		List<Object> lista =  getEntityManagerMatriz().createNativeQuery(""
				+ " select\n"
				+ "   desc_renum,\n"
				+ "   media.nome_cargo,\n"
				+ "   cast(round(media_sb / 13.33, 0) as int) media,\n"
				+ "   cast(round(p90 / 13.33, 0) as int) as p90,\n"
				+ "   cast(round(p75 / 13.33, 0) as int) as p75,\n"
				+ "   cast(round(p50 / 13.33, 0) as int) as p50,\n"
				+ "   cast(round(p25 / 13.33, 0) as int) as p25,\n"
				+ "   cast(round(p10 / 13.33, 0) as int) as p10,\n"
				+ "   qtd_empresas,\n"
				+ "   num_participantes \n"
				+ "from\n"
				+ "   (\n"
				+ "      select\n"
				+ "         desc_renum,\n"
				+ "         nome_cargo,\n"
				+ "         avg(media_sb) media_sb,\n"
				+ "         count(1) qtd_empresas,\n"
				+ "         sum(freq_fun) num_participantes \n"
				+ "      from\n"
				+ "         (\n"
				+ "            select\n"
				+ "               query.nome_cargo,\n"
				+ "               query.desc_renum,\n"
				+ "               max_grade.max_grade,\n"
				+ "               query.grade,\n"
				+ "               query.freq_fun,\n"
				+ "               query.nome_empresa,\n"
				+ "               query.media_sb \n"
				+ "            from\n"
				+ "               (\n"
				+ "                  select\n"
				+ "                     tp.nome_cargo,\n"
				+ "                     avg(media_sb) media_sb,\n"
				+ "                     nome_empresa,\n"
				+ "                     sum(freq_fun ) freq_fun,\n"
				+ "                     grade,\n"
				+ "                     'SBM - Salário Base Mensal ' as desc_renum \n"
				+ "                  from\n"
				+ "                     tb_pesquisa tp \n"
				+ "                     inner join\n"
				+ "                        TB_PESQUISA_MERCADO tpm \n"
				+ "                        on (tp.codigo_empresa = tpm.cd_empresa) \n"
				+ "                     inner join\n"
				+ "                        tb_mercado tm \n"
				+ "                        on (tpm.cd_mercado = tm.id) \n"
				+ "                  where\n"
				+ "                     nm_familia = :nmFamilia \n"
				+ "                     and nm_subfamilia = :nmSubFamilia \n"
				+ "                     and nome_cargo = :nmCargo \n"
				+ "                     and tm.dsMercado = :nmMercado \n"
				+ "                     \n"
				+ "                     and tp.grade >= :gradeMinimo \n"
				+ "                     and tp.grade <= :gradeMaximo \n"
				+ "                     and media_sb is not null \n"
				+ "                  group by\n"
				+ "                     nome_cargo,\n"
				+ "                     nome_empresa,\n"
				+ "                     grade\n"
				+ "               )\n"
				+ "               query \n"
				+ "               inner join\n"
				+ "                  (\n"
				+ "                     SELECT\n"
				+ "                        MAX(grade) max_grade,\n"
				+ "                        nome_empresa \n"
				+ "                     FROM\n"
				+ "                        tb_pesquisa tp \n"
				+ "                        inner join\n"
				+ "                           TB_PESQUISA_MERCADO tpm \n"
				+ "                           on (tp.codigo_empresa = tpm.cd_empresa) \n"
				+ "                        inner join\n"
				+ "                           tb_mercado tm \n"
				+ "                           on (tpm.cd_mercado = tm.id) \n"
				+ "                     where\n"
				+ "                        nm_familia = :nmFamilia \n"
				+ "                        and nm_subfamilia = :nmSubFamilia \n"
				+ "                        and nome_cargo = :nmCargo \n"
				+ "                        and tm.dsMercado = :nmMercado \n"
				+ "                        \n"
				+ "                        and tp.grade >= :gradeMinimo \n"
				+ "                        and tp.grade <= :gradeMaximo \n"
				+ "                        and media_sb is not null \n"
				+ "                     group by\n"
				+ "                        nome_empresa\n"
				+ "                  )\n"
				+ "                  max_grade \n"
				+ "                  on (max_grade.nome_empresa = query.nome_empresa)\n"
				+ "         )\n"
				+ "         total \n"
				+ "      where\n"
				+ "         total.grade = total.max_grade \n"
				+ "      group by\n"
				+ "         desc_renum,\n"
				+ "         nome_cargo\n"
				+ "   )\n"
				+ "   media \n"
				+ "   inner join\n"
				+ "      (\n"
				+ "         SELECT DISTINCT\n"
				+ "            [nome_cargo],\n"
				+ "            P90 = PERCENTILE_CONT(0.90) WITHIN GROUP (\n"
				+ "         ORDER BY\n"
				+ "            media_sb) OVER (PARTITION BY [nome_cargo]),\n"
				+ "            P75 = PERCENTILE_CONT(0.75) WITHIN GROUP (\n"
				+ "         ORDER BY\n"
				+ "            media_sb) OVER (PARTITION BY [nome_cargo]),\n"
				+ "            P50 = PERCENTILE_CONT(0.50) WITHIN GROUP (\n"
				+ "         ORDER BY\n"
				+ "            media_sb) OVER (PARTITION BY [nome_cargo]),\n"
				+ "            P25 = PERCENTILE_CONT(0.25) WITHIN GROUP (\n"
				+ "         ORDER BY\n"
				+ "            media_sb) OVER (PARTITION BY [nome_cargo]),\n"
				+ "            P10 = PERCENTILE_CONT(0.10) WITHIN GROUP (\n"
				+ "         ORDER BY\n"
				+ "            media_sb) OVER (PARTITION BY [nome_cargo]) \n"
				+ "         FROM\n"
				+ "            tb_pesquisa tp \n"
				+ "            inner join\n"
				+ "               TB_PESQUISA_MERCADO tpm \n"
				+ "               on (tp.codigo_empresa = tpm.cd_empresa) \n"
				+ "            inner join\n"
				+ "               tb_mercado tm \n"
				+ "               on (tpm.cd_mercado = tm.id) \n"
				+ "         where\n"
				+ "            nm_familia = :nmFamilia \n"
				+ "            and nm_subfamilia = :nmSubFamilia \n"
				+ "            and nome_cargo = :nmCargo \n"
				+ "            and tm.dsMercado = :nmMercado \n"
				+ "            \n"
				+ "            and tp.grade >= :gradeMinimo \n"
				+ "            and tp.grade <= :gradeMaximo \n"
				+ "            and media_sb is not null \n"
				+ "      )\n"
				+ "      percentis \n"
				+ "      on(media.nome_cargo = percentis.nome_cargo) "
				+ ""
				+ " UNION ALL"
				+ ""
				+ ""
				+ " \n"
				+ "select \n"
				+ "desc_renum, \n"
				+ "media.nome_cargo, \n"
				+ " cast(round(media_sb,0) as int) media,\n"
				+ " cast(round(p90,0) as int) as p90, \n"
				+ " cast(round(p75,0) as int) as p75, \n"
				+ " cast(round(p50,0) as int) as p50, \n"
				+ " cast(round(p25,0) as int) as p25, \n"
				+ " cast(round(p10,0) as int) as p10, \n"
				+ "qtd_empresas,\n"
				+ "num_participantes\n"
				+ "from (\n"
				+ "select desc_renum,\n"
				+ "	   nome_cargo ,\n"
				+ "	   avg(media_sb) media_sb,\n"
				+ "	   count(1) qtd_empresas,\n"
				+ "	   sum(freq_fun) num_participantes\n"
				+ "        from (\n"
				+ "select query.nome_cargo, query.desc_renum, max_grade.max_grade, query.grade,query.freq_fun, query.nome_empresa,\n"
				+ "query.media_sb\n"
				+ "from (\n"
				+ "select tp.nome_cargo, avg(media_sb) media_sb,nome_empresa, sum(freq_fun ) freq_fun, grade,\n"
				+ "'SBA - Salário Base Anual' as desc_renum from tb_pesquisa tp \n"
				+ "inner join TB_PESQUISA_MERCADO tpm \n"
				+ "on (tp.codigo_empresa = tpm.cd_empresa) \n"
				+ "inner join tb_mercado tm \n"
				+ "on (tpm.cd_mercado = tm.id) \n"
				+ "where nm_familia = :nmFamilia   \n"
				+ "		 		and nm_subfamilia =:nmSubFamilia   \n"
				+ "		 		and nome_cargo = :nmCargo    \n"
				+ "		 		and tm.dsMercado =:nmMercado    \n"
				+ "		 		and tp.grade >=:gradeMinimo and tp.grade <=:gradeMaximo\n"
				+ "and media_sb is not null\n"
				+ "group by nome_cargo,nome_empresa,grade) query \n"
				+ "inner join (\n"
				+ "	SELECT MAX(grade) max_grade, nome_empresa \n"
				+ "       FROM  tb_pesquisa tp \n"
				+ "inner join TB_PESQUISA_MERCADO tpm \n"
				+ "on (tp.codigo_empresa = tpm.cd_empresa) \n"
				+ "inner join tb_mercado tm \n"
				+ "on (tpm.cd_mercado = tm.id) \n"
				+ "where nm_familia = :nmFamilia   \n"
				+ "		 		and nm_subfamilia =:nmSubFamilia   \n"
				+ "		 		and nome_cargo = :nmCargo    \n"
				+ "		 		and tm.dsMercado =:nmMercado    \n"
				+ "		 		and tp.grade >=:gradeMinimo and tp.grade <=:gradeMaximo\n"
				+ "and media_sb is not null\n"
				+ "group by nome_empresa) max_grade\n"
				+ "on (max_grade.nome_empresa = query.nome_empresa)) total \n"
				+ "where total.grade = total.max_grade\n"
				+ "group by desc_renum,nome_cargo) media\n"
				+ "inner join ( \n"
				+ "SELECT DISTINCT \n"
				+ "            [nome_cargo], \n"
				+ "            P90    = PERCENTILE_CONT(0.90) WITHIN GROUP (ORDER BY media_sb) OVER (PARTITION BY [nome_cargo]), \n"
				+ "            P75    = PERCENTILE_CONT(0.75) WITHIN GROUP (ORDER BY media_sb) OVER (PARTITION BY [nome_cargo]), \n"
				+ "            P50    = PERCENTILE_CONT(0.50) WITHIN GROUP (ORDER BY media_sb) OVER (PARTITION BY [nome_cargo]), \n"
				+ "            P25    = PERCENTILE_CONT(0.25) WITHIN GROUP (ORDER BY media_sb) OVER (PARTITION BY [nome_cargo]), \n"
				+ "            P10    = PERCENTILE_CONT(0.10) WITHIN GROUP (ORDER BY media_sb) OVER (PARTITION BY [nome_cargo])\n"
				+ "FROM        tb_pesquisa tp \n"
				+ "inner join TB_PESQUISA_MERCADO tpm \n"
				+ "on (tp.codigo_empresa = tpm.cd_empresa) \n"
				+ "inner join tb_mercado tm \n"
				+ "on (tpm.cd_mercado = tm.id) \n"
				+ "where  nm_familia = :nmFamilia   \n"
				+ "		 		and nm_subfamilia =:nmSubFamilia   \n"
				+ "		 		and nome_cargo = :nmCargo    \n"
				+ "		 		and tm.dsMercado =:nmMercado    \n"
				+ "		 		and tp.grade >=:gradeMinimo and tp.grade <=:gradeMaximo\n"
				+ "and media_sb is not null \n"
				+ ")  percentis \n"
				+ "on(media.nome_cargo = percentis.nome_cargo) "
				+ ""
				+ ""
				+ " UNION ALL "
				+ ""
				+ " \n"
				+ "select \n"
				+ "desc_renum, \n"
				+ "media.nome_cargo, \n"
				+ " cast(round(mediana_cp_aliv,0) as int) media,\n"
				+ " cast(round(p90,0) as int) as p90, \n"
				+ " cast(round(p75,0) as int) as p75, \n"
				+ " cast(round(p50,0) as int) as p50, \n"
				+ " cast(round(p25,0) as int) as p25, \n"
				+ " cast(round(p10,0) as int) as p10, \n"
				+ "qtd_empresas,\n"
				+ "num_participantes\n"
				+ "from (\n"
				+ "select desc_renum,\n"
				+ "	   nome_cargo ,\n"
				+ "	   avg(mediana_cp_aliv) mediana_cp_aliv,\n"
				+ "	   count(1) qtd_empresas,\n"
				+ "	   sum(freq_fun) num_participantes\n"
				+ "        from (\n"
				+ "select query.nome_cargo, query.desc_renum, max_grade.max_grade, query.grade,query.freq_fun, query.nome_empresa,\n"
				+ "query.mediana_cp_aliv\n"
				+ "from (\n"
				+ "select tp.nome_cargo, avg(mediana_cp_aliv) mediana_cp_aliv,nome_empresa, sum(freq_fun ) freq_fun, grade,\n"
				+ "'ICPA - Incentivo de Curto Prazo Alvo (Bônus + PLR) ' as desc_renum from tb_pesquisa tp \n"
				+ "inner join TB_PESQUISA_MERCADO tpm \n"
				+ "on (tp.codigo_empresa = tpm.cd_empresa) \n"
				+ "inner join tb_mercado tm \n"
				+ "on (tpm.cd_mercado = tm.id) \n"
				+ "where nm_familia = :nmFamilia   \n"
				+ "		 		and nm_subfamilia =:nmSubFamilia   \n"
				+ "		 		and nome_cargo = :nmCargo    \n"
				+ "		 		and tm.dsMercado =:nmMercado    \n"
				+ "		 		and tp.grade >=:gradeMinimo and tp.grade <=:gradeMaximo\n"
				+ "and mediana_cp_aliv is not null\n"
				+ "group by nome_cargo,nome_empresa,grade) query \n"
				+ "inner join (\n"
				+ "	SELECT MAX(grade) max_grade, nome_empresa \n"
				+ "       FROM  tb_pesquisa tp \n"
				+ "inner join TB_PESQUISA_MERCADO tpm \n"
				+ "on (tp.codigo_empresa = tpm.cd_empresa) \n"
				+ "inner join tb_mercado tm \n"
				+ "on (tpm.cd_mercado = tm.id) \n"
				+ "where nm_familia = :nmFamilia   \n"
				+ "		 		and nm_subfamilia =:nmSubFamilia   \n"
				+ "		 		and nome_cargo = :nmCargo    \n"
				+ "		 		and tm.dsMercado =:nmMercado    \n"
				+ "		 		and tp.grade >=:gradeMinimo and tp.grade <=:gradeMaximo\n"
				+ "and mediana_cp_aliv is not null\n"
				+ "group by nome_empresa) max_grade\n"
				+ "on (max_grade.nome_empresa = query.nome_empresa)) total \n"
				+ "where total.grade = total.max_grade\n"
				+ "group by desc_renum,nome_cargo) media\n"
				+ "inner join ( \n"
				+ "SELECT DISTINCT \n"
				+ "            [nome_cargo], \n"
				+ "            P90    = PERCENTILE_CONT(0.90) WITHIN GROUP (ORDER BY mediana_cp_aliv) OVER (PARTITION BY [nome_cargo]), \n"
				+ "            P75    = PERCENTILE_CONT(0.75) WITHIN GROUP (ORDER BY mediana_cp_aliv) OVER (PARTITION BY [nome_cargo]), \n"
				+ "            P50    = PERCENTILE_CONT(0.50) WITHIN GROUP (ORDER BY mediana_cp_aliv) OVER (PARTITION BY [nome_cargo]), \n"
				+ "            P25    = PERCENTILE_CONT(0.25) WITHIN GROUP (ORDER BY mediana_cp_aliv) OVER (PARTITION BY [nome_cargo]), \n"
				+ "            P10    = PERCENTILE_CONT(0.10) WITHIN GROUP (ORDER BY mediana_cp_aliv) OVER (PARTITION BY [nome_cargo])\n"
				+ "FROM        tb_pesquisa tp \n"
				+ "inner join TB_PESQUISA_MERCADO tpm \n"
				+ "on (tp.codigo_empresa = tpm.cd_empresa) \n"
				+ "inner join tb_mercado tm \n"
				+ "on (tpm.cd_mercado = tm.id) \n"
				+ "where  nm_familia = :nmFamilia   \n"
				+ "		 		and nm_subfamilia =:nmSubFamilia   \n"
				+ "		 		and nome_cargo = :nmCargo    \n"
				+ "		 		and tm.dsMercado =:nmMercado    \n"
				+ "		 		and tp.grade >=:gradeMinimo and tp.grade <=:gradeMaximo\n"
				+ "and mediana_cp_aliv is not null \n"
				+ ")  percentis \n"
				+ "on(media.nome_cargo = percentis.nome_cargo) \n"
				+ " UNION ALL "
				+ ""
				+ ""
				+ " \n"
				+ "select \n"
				+ "desc_renum, \n"
				+ "media.nome_cargo, \n"
				+ " cast(round(media_tda,0) as int) media,\n"
				+ " cast(round(p90,0) as int) as p90, \n"
				+ " cast(round(p75,0) as int) as p75, \n"
				+ " cast(round(p50,0) as int) as p50, \n"
				+ " cast(round(p25,0) as int) as p25, \n"
				+ " cast(round(p10,0) as int) as p10, \n"
				+ "qtd_empresas,\n"
				+ "num_participantes\n"
				+ "from (\n"
				+ "select desc_renum,\n"
				+ "	   nome_cargo ,\n"
				+ "	   avg(media_tda) media_tda,\n"
				+ "	   count(1) qtd_empresas,\n"
				+ "	   sum(freq_fun) num_participantes\n"
				+ "        from (\n"
				+ "select query.nome_cargo, query.desc_renum, max_grade.max_grade, query.grade,query.freq_fun, query.nome_empresa,\n"
				+ "query.media_tda\n"
				+ "from (\n"
				+ "select tp.nome_cargo, avg(media_tda) media_tda,nome_empresa, sum(freq_fun ) freq_fun, grade,\n"
				+ "'TDA - Total em Dinheiro Alvo ' as desc_renum from tb_pesquisa tp \n"
				+ "inner join TB_PESQUISA_MERCADO tpm \n"
				+ "on (tp.codigo_empresa = tpm.cd_empresa) \n"
				+ "inner join tb_mercado tm \n"
				+ "on (tpm.cd_mercado = tm.id) \n"
				+ "where nm_familia = :nmFamilia   \n"
				+ "		 		and nm_subfamilia =:nmSubFamilia   \n"
				+ "		 		and nome_cargo = :nmCargo    \n"
				+ "		 		and tm.dsMercado =:nmMercado    \n"
				+ "		 		and tp.grade >=:gradeMinimo and tp.grade <=:gradeMaximo\n"
				+ "and media_tda is not null\n"
				+ "group by nome_cargo,nome_empresa,grade) query \n"
				+ "inner join (\n"
				+ "	SELECT MAX(grade) max_grade, nome_empresa \n"
				+ "       FROM  tb_pesquisa tp \n"
				+ "inner join TB_PESQUISA_MERCADO tpm \n"
				+ "on (tp.codigo_empresa = tpm.cd_empresa) \n"
				+ "inner join tb_mercado tm \n"
				+ "on (tpm.cd_mercado = tm.id) \n"
				+ "where nm_familia = :nmFamilia   \n"
				+ "		 		and nm_subfamilia =:nmSubFamilia   \n"
				+ "		 		and nome_cargo = :nmCargo    \n"
				+ "		 		and tm.dsMercado =:nmMercado    \n"
				+ "		 		and tp.grade >=:gradeMinimo and tp.grade <=:gradeMaximo\n"
				+ "and media_tda is not null\n"
				+ "group by nome_empresa) max_grade\n"
				+ "on (max_grade.nome_empresa = query.nome_empresa)) total \n"
				+ "where total.grade = total.max_grade\n"
				+ "group by desc_renum,nome_cargo) media\n"
				+ "inner join ( \n"
				+ "SELECT DISTINCT \n"
				+ "            [nome_cargo], \n"
				+ "            P90    = PERCENTILE_CONT(0.90) WITHIN GROUP (ORDER BY media_tda) OVER (PARTITION BY [nome_cargo]), \n"
				+ "            P75    = PERCENTILE_CONT(0.75) WITHIN GROUP (ORDER BY media_tda) OVER (PARTITION BY [nome_cargo]), \n"
				+ "            P50    = PERCENTILE_CONT(0.50) WITHIN GROUP (ORDER BY media_tda) OVER (PARTITION BY [nome_cargo]), \n"
				+ "            P25    = PERCENTILE_CONT(0.25) WITHIN GROUP (ORDER BY media_tda) OVER (PARTITION BY [nome_cargo]), \n"
				+ "            P10    = PERCENTILE_CONT(0.10) WITHIN GROUP (ORDER BY media_tda) OVER (PARTITION BY [nome_cargo])\n"
				+ "FROM        tb_pesquisa tp \n"
				+ "inner join TB_PESQUISA_MERCADO tpm \n"
				+ "on (tp.codigo_empresa = tpm.cd_empresa) \n"
				+ "inner join tb_mercado tm \n"
				+ "on (tpm.cd_mercado = tm.id) \n"
				+ "where  nm_familia = :nmFamilia   \n"
				+ "		 		and nm_subfamilia =:nmSubFamilia   \n"
				+ "		 		and nome_cargo = :nmCargo    \n"
				+ "		 		and tm.dsMercado =:nmMercado    \n"
				+ "		 		and tp.grade >=:gradeMinimo and tp.grade <=:gradeMaximo\n"
				+ "and media_tda is not null \n"
				+ ")  percentis \n"
				+ "on(media.nome_cargo = percentis.nome_cargo) "
				+ ""
				+ " UNION ALL "
				+ ""
				+ ""
				+ " \n"
				+ "select \n"
				+ "desc_renum, \n"
				+ "media.nome_cargo, \n"
				+ " cast(round(mediana_cp_pg,0) as int) media,\n"
				+ " cast(round(p90,0) as int) as p90, \n"
				+ " cast(round(p75,0) as int) as p75, \n"
				+ " cast(round(p50,0) as int) as p50, \n"
				+ " cast(round(p25,0) as int) as p25, \n"
				+ " cast(round(p10,0) as int) as p10, \n"
				+ "qtd_empresas,\n"
				+ "num_participantes\n"
				+ "from (\n"
				+ "select desc_renum,\n"
				+ "	   nome_cargo ,\n"
				+ "	   avg(mediana_cp_pg) mediana_cp_pg,\n"
				+ "	   count(1) qtd_empresas,\n"
				+ "	   sum(freq_fun) num_participantes\n"
				+ "        from (\n"
				+ "select query.nome_cargo, query.desc_renum, max_grade.max_grade, query.grade,query.freq_fun, query.nome_empresa,\n"
				+ "query.mediana_cp_pg\n"
				+ "from (\n"
				+ "select tp.nome_cargo, avg(mediana_cp_pg) mediana_cp_pg,nome_empresa, sum(freq_fun ) freq_fun, grade,\n"
				+ "'ICP - Incentivo de Curto Prazo Pago (Bônus + PLR) ' as desc_renum from tb_pesquisa tp \n"
				+ "inner join TB_PESQUISA_MERCADO tpm \n"
				+ "on (tp.codigo_empresa = tpm.cd_empresa) \n"
				+ "inner join tb_mercado tm \n"
				+ "on (tpm.cd_mercado = tm.id) \n"
				+ "where nm_familia = :nmFamilia   \n"
				+ "		 		and nm_subfamilia =:nmSubFamilia   \n"
				+ "		 		and nome_cargo = :nmCargo    \n"
				+ "		 		and tm.dsMercado =:nmMercado    \n"
				+ "		 		and tp.grade >=:gradeMinimo and tp.grade <=:gradeMaximo\n"
				+ "and mediana_cp_pg is not null\n"
				+ "group by nome_cargo,nome_empresa,grade) query \n"
				+ "inner join (\n"
				+ "	SELECT MAX(grade) max_grade, nome_empresa \n"
				+ "       FROM  tb_pesquisa tp \n"
				+ "inner join TB_PESQUISA_MERCADO tpm \n"
				+ "on (tp.codigo_empresa = tpm.cd_empresa) \n"
				+ "inner join tb_mercado tm \n"
				+ "on (tpm.cd_mercado = tm.id) \n"
				+ "where nm_familia = :nmFamilia   \n"
				+ "		 		and nm_subfamilia =:nmSubFamilia   \n"
				+ "		 		and nome_cargo = :nmCargo    \n"
				+ "		 		and tm.dsMercado =:nmMercado    \n"
				+ "		 		and tp.grade >=:gradeMinimo and tp.grade <=:gradeMaximo\n"
				+ "and mediana_cp_pg is not null\n"
				+ "group by nome_empresa) max_grade\n"
				+ "on (max_grade.nome_empresa = query.nome_empresa)) total \n"
				+ "where total.grade = total.max_grade\n"
				+ "group by desc_renum,nome_cargo) media\n"
				+ "inner join ( \n"
				+ "SELECT DISTINCT \n"
				+ "            [nome_cargo], \n"
				+ "            P90    = PERCENTILE_CONT(0.90) WITHIN GROUP (ORDER BY mediana_cp_pg) OVER (PARTITION BY [nome_cargo]), \n"
				+ "            P75    = PERCENTILE_CONT(0.75) WITHIN GROUP (ORDER BY mediana_cp_pg) OVER (PARTITION BY [nome_cargo]), \n"
				+ "            P50    = PERCENTILE_CONT(0.50) WITHIN GROUP (ORDER BY mediana_cp_pg) OVER (PARTITION BY [nome_cargo]), \n"
				+ "            P25    = PERCENTILE_CONT(0.25) WITHIN GROUP (ORDER BY mediana_cp_pg) OVER (PARTITION BY [nome_cargo]), \n"
				+ "            P10    = PERCENTILE_CONT(0.10) WITHIN GROUP (ORDER BY mediana_cp_pg) OVER (PARTITION BY [nome_cargo])\n"
				+ "FROM        tb_pesquisa tp \n"
				+ "inner join TB_PESQUISA_MERCADO tpm \n"
				+ "on (tp.codigo_empresa = tpm.cd_empresa) \n"
				+ "inner join tb_mercado tm \n"
				+ "on (tpm.cd_mercado = tm.id) \n"
				+ "where  nm_familia = :nmFamilia   \n"
				+ "		 		and nm_subfamilia =:nmSubFamilia   \n"
				+ "		 		and nome_cargo = :nmCargo    \n"
				+ "		 		and tm.dsMercado =:nmMercado    \n"
				+ "		 		and tp.grade >=:gradeMinimo and tp.grade <=:gradeMaximo\n"
				+ "and mediana_cp_pg is not null \n"
				+ ")  percentis \n"
				+ "on(media.nome_cargo = percentis.nome_cargo) "
				+ ""
				+ ""
				+ " UNION ALL "
				+ ""
				+ " \n"
				+ "select \n"
				+ "desc_renum, \n"
				+ "media.nome_cargo, \n"
				+ " cast(round(media_tb,0) as int) media,\n"
				+ " cast(round(p90,0) as int) as p90, \n"
				+ " cast(round(p75,0) as int) as p75, \n"
				+ " cast(round(p50,0) as int) as p50, \n"
				+ " cast(round(p25,0) as int) as p25, \n"
				+ " cast(round(p10,0) as int) as p10, \n"
				+ "qtd_empresas,\n"
				+ "num_participantes\n"
				+ "from (\n"
				+ "select desc_renum,\n"
				+ "	   nome_cargo ,\n"
				+ "	   avg(media_tb) media_tb,\n"
				+ "	   count(1) qtd_empresas,\n"
				+ "	   sum(freq_fun) num_participantes\n"
				+ "        from (\n"
				+ "select query.nome_cargo, query.desc_renum, max_grade.max_grade, query.grade,query.freq_fun, query.nome_empresa,\n"
				+ "query.media_tb\n"
				+ "from (\n"
				+ "select tp.nome_cargo, avg(media_tb) media_tb,nome_empresa, sum(freq_fun ) freq_fun, grade,\n"
				+ "'TD - Total em Dinheiro ' as desc_renum from tb_pesquisa tp \n"
				+ "inner join TB_PESQUISA_MERCADO tpm \n"
				+ "on (tp.codigo_empresa = tpm.cd_empresa) \n"
				+ "inner join tb_mercado tm \n"
				+ "on (tpm.cd_mercado = tm.id) \n"
				+ "where nm_familia = :nmFamilia   \n"
				+ "		 		and nm_subfamilia =:nmSubFamilia   \n"
				+ "		 		and nome_cargo = :nmCargo    \n"
				+ "		 		and tm.dsMercado =:nmMercado    \n"
				+ "		 		and tp.grade >=:gradeMinimo and tp.grade <=:gradeMaximo\n"
				+ "and media_tb is not null\n"
				+ "group by nome_cargo,nome_empresa,grade) query \n"
				+ "inner join (\n"
				+ "	SELECT MAX(grade) max_grade, nome_empresa \n"
				+ "       FROM  tb_pesquisa tp \n"
				+ "inner join TB_PESQUISA_MERCADO tpm \n"
				+ "on (tp.codigo_empresa = tpm.cd_empresa) \n"
				+ "inner join tb_mercado tm \n"
				+ "on (tpm.cd_mercado = tm.id) \n"
				+ "where nm_familia = :nmFamilia   \n"
				+ "		 		and nm_subfamilia =:nmSubFamilia   \n"
				+ "		 		and nome_cargo = :nmCargo    \n"
				+ "		 		and tm.dsMercado =:nmMercado    \n"
				+ "		 		and tp.grade >=:gradeMinimo and tp.grade <=:gradeMaximo\n"
				+ "and media_tb is not null\n"
				+ "group by nome_empresa) max_grade\n"
				+ "on (max_grade.nome_empresa = query.nome_empresa)) total \n"
				+ "where total.grade = total.max_grade\n"
				+ "group by desc_renum,nome_cargo) media\n"
				+ "inner join ( \n"
				+ "SELECT DISTINCT \n"
				+ "            [nome_cargo], \n"
				+ "            P90    = PERCENTILE_CONT(0.90) WITHIN GROUP (ORDER BY media_tb) OVER (PARTITION BY [nome_cargo]), \n"
				+ "            P75    = PERCENTILE_CONT(0.75) WITHIN GROUP (ORDER BY media_tb) OVER (PARTITION BY [nome_cargo]), \n"
				+ "            P50    = PERCENTILE_CONT(0.50) WITHIN GROUP (ORDER BY media_tb) OVER (PARTITION BY [nome_cargo]), \n"
				+ "            P25    = PERCENTILE_CONT(0.25) WITHIN GROUP (ORDER BY media_tb) OVER (PARTITION BY [nome_cargo]), \n"
				+ "            P10    = PERCENTILE_CONT(0.10) WITHIN GROUP (ORDER BY media_tb) OVER (PARTITION BY [nome_cargo])\n"
				+ "FROM        tb_pesquisa tp \n"
				+ "inner join TB_PESQUISA_MERCADO tpm \n"
				+ "on (tp.codigo_empresa = tpm.cd_empresa) \n"
				+ "inner join tb_mercado tm \n"
				+ "on (tpm.cd_mercado = tm.id) \n"
				+ "where  nm_familia = :nmFamilia   \n"
				+ "		 		and nm_subfamilia =:nmSubFamilia   \n"
				+ "		 		and nome_cargo = :nmCargo    \n"
				+ "		 		and tm.dsMercado =:nmMercado    \n"
				+ "		 		and tp.grade >=:gradeMinimo and tp.grade <=:gradeMaximo\n"
				+ "and media_tb is not null \n"
				+ ")  percentis \n"
				+ "on(media.nome_cargo = percentis.nome_cargo) \n"
				+ ""
				+ ""
				+ " UNION ALL "
				+ ""
				+ " select \n"
				+ "desc_renum, \n"
				+ "media.nome_cargo, \n"
				+ " cast(round(mediana_ilp,0) as int) media,\n"
				+ " cast(round(p90,0) as int) as p90, \n"
				+ " cast(round(p75,0) as int) as p75, \n"
				+ " cast(round(p50,0) as int) as p50, \n"
				+ " cast(round(p25,0) as int) as p25, \n"
				+ " cast(round(p10,0) as int) as p10, \n"
				+ "qtd_empresas,\n"
				+ "num_participantes\n"
				+ "from (\n"
				+ "select desc_renum,\n"
				+ "	   nome_cargo ,\n"
				+ "	   avg(mediana_ilp) mediana_ilp,\n"
				+ "	   count(1) qtd_empresas,\n"
				+ "	   sum(freq_fun) num_participantes\n"
				+ "        from (\n"
				+ "select query.nome_cargo, query.desc_renum, max_grade.max_grade, query.grade,query.freq_fun, query.nome_empresa,\n"
				+ "query.mediana_ilp\n"
				+ "from (\n"
				+ "select tp.nome_cargo, avg(mediana_ilp) mediana_ilp,nome_empresa, sum(freq_fun ) freq_fun, grade,\n"
				+ "'ILP - Incentivos de Longo Prazo ' as desc_renum from tb_pesquisa tp \n"
				+ "inner join TB_PESQUISA_MERCADO tpm \n"
				+ "on (tp.codigo_empresa = tpm.cd_empresa) \n"
				+ "inner join tb_mercado tm \n"
				+ "on (tpm.cd_mercado = tm.id) \n"
				+ "where nm_familia = :nmFamilia   \n"
				+ "		 		and nm_subfamilia =:nmSubFamilia   \n"
				+ "		 		and nome_cargo = :nmCargo    \n"
				+ "		 		and tm.dsMercado =:nmMercado    \n"
				+ "		 		and tp.grade >=:gradeMinimo and tp.grade <=:gradeMaximo\n"
				+ "and mediana_ilp is not null\n"
				+ "group by nome_cargo,nome_empresa,grade) query \n"
				+ "inner join (\n"
				+ "	SELECT MAX(grade) max_grade, nome_empresa \n"
				+ "       FROM  tb_pesquisa tp \n"
				+ "inner join TB_PESQUISA_MERCADO tpm \n"
				+ "on (tp.codigo_empresa = tpm.cd_empresa) \n"
				+ "inner join tb_mercado tm \n"
				+ "on (tpm.cd_mercado = tm.id) \n"
				+ "where nm_familia = :nmFamilia   \n"
				+ "		 		and nm_subfamilia =:nmSubFamilia   \n"
				+ "		 		and nome_cargo = :nmCargo    \n"
				+ "		 		and tm.dsMercado =:nmMercado    \n"
				+ "		 		and tp.grade >=:gradeMinimo and tp.grade <=:gradeMaximo\n"
				+ "and mediana_ilp is not null\n"
				+ "group by nome_empresa) max_grade\n"
				+ "on (max_grade.nome_empresa = query.nome_empresa)) total \n"
				+ "where total.grade = total.max_grade\n"
				+ "group by desc_renum,nome_cargo) media\n"
				+ "inner join ( \n"
				+ "SELECT DISTINCT \n"
				+ "            [nome_cargo], \n"
				+ "            P90    = PERCENTILE_CONT(0.90) WITHIN GROUP (ORDER BY mediana_ilp) OVER (PARTITION BY [nome_cargo]), \n"
				+ "            P75    = PERCENTILE_CONT(0.75) WITHIN GROUP (ORDER BY mediana_ilp) OVER (PARTITION BY [nome_cargo]), \n"
				+ "            P50    = PERCENTILE_CONT(0.50) WITHIN GROUP (ORDER BY mediana_ilp) OVER (PARTITION BY [nome_cargo]), \n"
				+ "            P25    = PERCENTILE_CONT(0.25) WITHIN GROUP (ORDER BY mediana_ilp) OVER (PARTITION BY [nome_cargo]), \n"
				+ "            P10    = PERCENTILE_CONT(0.10) WITHIN GROUP (ORDER BY mediana_ilp) OVER (PARTITION BY [nome_cargo])\n"
				+ "FROM        tb_pesquisa tp \n"
				+ "inner join TB_PESQUISA_MERCADO tpm \n"
				+ "on (tp.codigo_empresa = tpm.cd_empresa) \n"
				+ "inner join tb_mercado tm \n"
				+ "on (tpm.cd_mercado = tm.id) \n"
				+ "where  nm_familia = :nmFamilia   \n"
				+ "		 		and nm_subfamilia =:nmSubFamilia   \n"
				+ "		 		and nome_cargo = :nmCargo    \n"
				+ "		 		and tm.dsMercado =:nmMercado    \n"
				+ "		 		and tp.grade >=:gradeMinimo and tp.grade <=:gradeMaximo\n"
				+ "and mediana_ilp is not null \n"
				+ ")  percentis \n"
				+ "on(media.nome_cargo = percentis.nome_cargo) "
				+ ""
				+ " UNION ALL "
				+ ""
				+ ""
				+ " select \n"
				+ "desc_renum, \n"
				+ "media.nome_cargo, \n"
				+ " cast(round(media_rda,0) as int) media,\n"
				+ " cast(round(p90,0) as int) as p90, \n"
				+ " cast(round(p75,0) as int) as p75, \n"
				+ " cast(round(p50,0) as int) as p50, \n"
				+ " cast(round(p25,0) as int) as p25, \n"
				+ " cast(round(p10,0) as int) as p10, \n"
				+ "qtd_empresas,\n"
				+ "num_participantes\n"
				+ "from (\n"
				+ "select desc_renum,\n"
				+ "	   nome_cargo ,\n"
				+ "	   avg(media_rda) media_rda,\n"
				+ "	   count(1) qtd_empresas,\n"
				+ "	   sum(freq_fun) num_participantes\n"
				+ "        from (\n"
				+ "select query.nome_cargo, query.desc_renum, max_grade.max_grade, query.grade,query.freq_fun, query.nome_empresa,\n"
				+ "query.media_rda\n"
				+ "from (\n"
				+ "select tp.nome_cargo, avg(media_rda) media_rda,nome_empresa, sum(freq_fun ) freq_fun, grade,\n"
				+ "'RDA - Remuneração Direta Alvo  ' as desc_renum from tb_pesquisa tp \n"
				+ "inner join TB_PESQUISA_MERCADO tpm \n"
				+ "on (tp.codigo_empresa = tpm.cd_empresa) \n"
				+ "inner join tb_mercado tm \n"
				+ "on (tpm.cd_mercado = tm.id) \n"
				+ "where nm_familia = :nmFamilia   \n"
				+ "		 		and nm_subfamilia =:nmSubFamilia   \n"
				+ "		 		and nome_cargo = :nmCargo    \n"
				+ "		 		and tm.dsMercado =:nmMercado    \n"
				+ "		 		and tp.grade >=:gradeMinimo and tp.grade <=:gradeMaximo\n"
				+ "and media_rda is not null\n"
				+ "group by nome_cargo,nome_empresa,grade) query \n"
				+ "inner join (\n"
				+ "	SELECT MAX(grade) max_grade, nome_empresa \n"
				+ "       FROM  tb_pesquisa tp \n"
				+ "inner join TB_PESQUISA_MERCADO tpm \n"
				+ "on (tp.codigo_empresa = tpm.cd_empresa) \n"
				+ "inner join tb_mercado tm \n"
				+ "on (tpm.cd_mercado = tm.id) \n"
				+ "where nm_familia = :nmFamilia   \n"
				+ "		 		and nm_subfamilia =:nmSubFamilia   \n"
				+ "		 		and nome_cargo = :nmCargo    \n"
				+ "		 		and tm.dsMercado =:nmMercado    \n"
				+ "		 		and tp.grade >=:gradeMinimo and tp.grade <=:gradeMaximo\n"
				+ "and media_rda is not null\n"
				+ "group by nome_empresa) max_grade\n"
				+ "on (max_grade.nome_empresa = query.nome_empresa)) total \n"
				+ "where total.grade = total.max_grade\n"
				+ "group by desc_renum,nome_cargo) media\n"
				+ "inner join ( \n"
				+ "SELECT DISTINCT \n"
				+ "            [nome_cargo], \n"
				+ "            P90    = PERCENTILE_CONT(0.90) WITHIN GROUP (ORDER BY media_rda) OVER (PARTITION BY [nome_cargo]), \n"
				+ "            P75    = PERCENTILE_CONT(0.75) WITHIN GROUP (ORDER BY media_rda) OVER (PARTITION BY [nome_cargo]), \n"
				+ "            P50    = PERCENTILE_CONT(0.50) WITHIN GROUP (ORDER BY media_rda) OVER (PARTITION BY [nome_cargo]), \n"
				+ "            P25    = PERCENTILE_CONT(0.25) WITHIN GROUP (ORDER BY media_rda) OVER (PARTITION BY [nome_cargo]), \n"
				+ "            P10    = PERCENTILE_CONT(0.10) WITHIN GROUP (ORDER BY media_rda) OVER (PARTITION BY [nome_cargo])\n"
				+ "FROM        tb_pesquisa tp \n"
				+ "inner join TB_PESQUISA_MERCADO tpm \n"
				+ "on (tp.codigo_empresa = tpm.cd_empresa) \n"
				+ "inner join tb_mercado tm \n"
				+ "on (tpm.cd_mercado = tm.id) \n"
				+ "where  nm_familia = :nmFamilia   \n"
				+ "		 		and nm_subfamilia =:nmSubFamilia   \n"
				+ "		 		and nome_cargo = :nmCargo    \n"
				+ "		 		and tm.dsMercado =:nmMercado    \n"
				+ "		 		and tp.grade >=:gradeMinimo and tp.grade <=:gradeMaximo\n"
				+ "and media_rda is not null \n"
				+ ")  percentis \n"
				+ "on(media.nome_cargo = percentis.nome_cargo) "
				+ ""
				+ " UNION ALL "
				+ ""
				+ ""
				+ "select \n"
				+ "desc_renum, \n"
				+ "media.nome_cargo, \n"
				+ " cast(round(media_rd,0) as int) media,\n"
				+ " cast(round(p90,0) as int) as p90, \n"
				+ " cast(round(p75,0) as int) as p75, \n"
				+ " cast(round(p50,0) as int) as p50, \n"
				+ " cast(round(p25,0) as int) as p25, \n"
				+ " cast(round(p10,0) as int) as p10, \n"
				+ "qtd_empresas,\n"
				+ "num_participantes\n"
				+ "from (\n"
				+ "select desc_renum,\n"
				+ "	   nome_cargo ,\n"
				+ "	   avg(media_rd) media_rd,\n"
				+ "	   count(1) qtd_empresas,\n"
				+ "	   sum(freq_fun) num_participantes\n"
				+ "        from (\n"
				+ "select query.nome_cargo, query.desc_renum, max_grade.max_grade, query.grade,query.freq_fun, query.nome_empresa,\n"
				+ "query.media_rd\n"
				+ "from (\n"
				+ "select tp.nome_cargo, avg(media_rd) media_rd,nome_empresa, sum(freq_fun ) freq_fun, grade,\n"
				+ "'RD - Remuneração Direta   ' as desc_renum from tb_pesquisa tp \n"
				+ "inner join TB_PESQUISA_MERCADO tpm \n"
				+ "on (tp.codigo_empresa = tpm.cd_empresa) \n"
				+ "inner join tb_mercado tm \n"
				+ "on (tpm.cd_mercado = tm.id) \n"
				+ "where nm_familia = :nmFamilia   \n"
				+ "		 		and nm_subfamilia =:nmSubFamilia   \n"
				+ "		 		and nome_cargo = :nmCargo    \n"
				+ "		 		and tm.dsMercado =:nmMercado    \n"
				+ "		 		and tp.grade >=:gradeMinimo and tp.grade <=:gradeMaximo\n"
				+ "and media_rd is not null\n"
				+ "group by nome_cargo,nome_empresa,grade) query \n"
				+ "inner join (\n"
				+ "	SELECT MAX(grade) max_grade, nome_empresa \n"
				+ "       FROM  tb_pesquisa tp \n"
				+ "inner join TB_PESQUISA_MERCADO tpm \n"
				+ "on (tp.codigo_empresa = tpm.cd_empresa) \n"
				+ "inner join tb_mercado tm \n"
				+ "on (tpm.cd_mercado = tm.id) \n"
				+ "where nm_familia = :nmFamilia   \n"
				+ "		 		and nm_subfamilia =:nmSubFamilia   \n"
				+ "		 		and nome_cargo = :nmCargo    \n"
				+ "		 		and tm.dsMercado =:nmMercado    \n"
				+ "		 		and tp.grade >=:gradeMinimo and tp.grade <=:gradeMaximo\n"
				+ "and media_rd is not null\n"
				+ "group by nome_empresa) max_grade\n"
				+ "on (max_grade.nome_empresa = query.nome_empresa)) total \n"
				+ "where total.grade = total.max_grade\n"
				+ "group by desc_renum,nome_cargo) media\n"
				+ "inner join ( \n"
				+ "SELECT DISTINCT \n"
				+ "            [nome_cargo], \n"
				+ "            P90    = PERCENTILE_CONT(0.90) WITHIN GROUP (ORDER BY media_rd) OVER (PARTITION BY [nome_cargo]), \n"
				+ "            P75    = PERCENTILE_CONT(0.75) WITHIN GROUP (ORDER BY media_rd) OVER (PARTITION BY [nome_cargo]), \n"
				+ "            P50    = PERCENTILE_CONT(0.50) WITHIN GROUP (ORDER BY media_rd) OVER (PARTITION BY [nome_cargo]), \n"
				+ "            P25    = PERCENTILE_CONT(0.25) WITHIN GROUP (ORDER BY media_rd) OVER (PARTITION BY [nome_cargo]), \n"
				+ "            P10    = PERCENTILE_CONT(0.10) WITHIN GROUP (ORDER BY media_rd) OVER (PARTITION BY [nome_cargo])\n"
				+ "FROM        tb_pesquisa tp \n"
				+ "inner join TB_PESQUISA_MERCADO tpm \n"
				+ "on (tp.codigo_empresa = tpm.cd_empresa) \n"
				+ "inner join tb_mercado tm \n"
				+ "on (tpm.cd_mercado = tm.id) \n"
				+ "where  nm_familia = :nmFamilia   \n"
				+ "		 		and nm_subfamilia =:nmSubFamilia   \n"
				+ "		 		and nome_cargo = :nmCargo    \n"
				+ "		 		and tm.dsMercado =:nmMercado    \n"
				+ "		 		and tp.grade >=:gradeMinimo and tp.grade <=:gradeMaximo\n"
				+ "and media_rd is not null \n"
				+ ")  percentis \n"
				+ "on(media.nome_cargo = percentis.nome_cargo)  ").

				//SBM
				setParameter("nmFamilia",nmFamilia).
				setParameter("nmSubFamilia",nmSubFamilia).
				setParameter("nmCargo",nmCargo).
				setParameter("nmMercado",mercGeral).
				setParameter("gradeMinimo",gradeMinimo).
				setParameter("gradeMaximo",gradeMaximo).

				setParameter("nmFamilia",nmFamilia).
				setParameter("nmSubFamilia",nmSubFamilia).
				setParameter("nmCargo",nmCargo).
				setParameter("nmMercado",mercGeral).
				setParameter("gradeMinimo",gradeMinimo).
				setParameter("gradeMaximo",gradeMaximo).

				setParameter("nmFamilia",nmFamilia).
				setParameter("nmSubFamilia",nmSubFamilia).
				setParameter("nmCargo",nmCargo).
				setParameter("nmMercado",mercGeral).
				setParameter("gradeMinimo",gradeMinimo).
				setParameter("gradeMaximo",gradeMaximo).


				//SBA
				setParameter("nmFamilia",nmFamilia).
				setParameter("nmSubFamilia",nmSubFamilia).
				setParameter("nmCargo",nmCargo).
				setParameter("nmMercado",mercGeral).
				setParameter("gradeMinimo",gradeMinimo).
				setParameter("gradeMaximo",gradeMaximo).

				setParameter("nmFamilia",nmFamilia).
				setParameter("nmSubFamilia",nmSubFamilia).
				setParameter("nmCargo",nmCargo).
				setParameter("nmMercado",mercGeral).
				setParameter("gradeMinimo",gradeMinimo).
				setParameter("gradeMaximo",gradeMaximo).

				setParameter("nmFamilia",nmFamilia).
				setParameter("nmSubFamilia",nmSubFamilia).
				setParameter("nmCargo",nmCargo).
				setParameter("nmMercado",mercGeral).
				setParameter("gradeMinimo",gradeMinimo).
				setParameter("gradeMaximo",gradeMaximo).

				//ICPA
				setParameter("nmFamilia",nmFamilia).
				setParameter("nmSubFamilia",nmSubFamilia).
				setParameter("nmCargo",nmCargo).
				setParameter("nmMercado",mercGeral).
				setParameter("gradeMinimo",gradeMinimo).
				setParameter("gradeMaximo",gradeMaximo).

				setParameter("nmFamilia",nmFamilia).
				setParameter("nmSubFamilia",nmSubFamilia).
				setParameter("nmCargo",nmCargo).
				setParameter("nmMercado",mercGeral).
				setParameter("gradeMinimo",gradeMinimo).
				setParameter("gradeMaximo",gradeMaximo).

				setParameter("nmFamilia",nmFamilia).
				setParameter("nmSubFamilia",nmSubFamilia).
				setParameter("nmCargo",nmCargo).
				setParameter("nmMercado",mercGeral).
				setParameter("gradeMinimo",gradeMinimo).
				setParameter("gradeMaximo",gradeMaximo).

				//TDA
				setParameter("nmFamilia",nmFamilia).
				setParameter("nmSubFamilia",nmSubFamilia).
				setParameter("nmCargo",nmCargo).
				setParameter("nmMercado",mercGeral).
				setParameter("gradeMinimo",gradeMinimo).
				setParameter("gradeMaximo",gradeMaximo).

				setParameter("nmFamilia",nmFamilia).
				setParameter("nmSubFamilia",nmSubFamilia).
				setParameter("nmCargo",nmCargo).
				setParameter("nmMercado",mercGeral).
				setParameter("gradeMinimo",gradeMinimo).
				setParameter("gradeMaximo",gradeMaximo).

				setParameter("nmFamilia",nmFamilia).
				setParameter("nmSubFamilia",nmSubFamilia).
				setParameter("nmCargo",nmCargo).
				setParameter("nmMercado",mercGeral).
				setParameter("gradeMinimo",gradeMinimo).
				setParameter("gradeMaximo",gradeMaximo).

				//TD
				setParameter("nmFamilia",nmFamilia).
				setParameter("nmSubFamilia",nmSubFamilia).
				setParameter("nmCargo",nmCargo).
				setParameter("nmMercado",mercGeral).
				setParameter("gradeMinimo",gradeMinimo).
				setParameter("gradeMaximo",gradeMaximo).

				setParameter("nmFamilia",nmFamilia).
				setParameter("nmSubFamilia",nmSubFamilia).
				setParameter("nmCargo",nmCargo).
				setParameter("nmMercado",mercGeral).
				setParameter("gradeMinimo",gradeMinimo).
				setParameter("gradeMaximo",gradeMaximo).

				setParameter("nmFamilia",nmFamilia).
				setParameter("nmSubFamilia",nmSubFamilia).
				setParameter("nmCargo",nmCargo).
				setParameter("nmMercado",mercGeral).
				setParameter("gradeMinimo",gradeMinimo).
				setParameter("gradeMaximo",gradeMaximo).

				//ILP
				setParameter("nmFamilia",nmFamilia).
				setParameter("nmSubFamilia",nmSubFamilia).
				setParameter("nmCargo",nmCargo).
				setParameter("nmMercado",mercGeral).
				setParameter("gradeMinimo",gradeMinimo).
				setParameter("gradeMaximo",gradeMaximo).

				setParameter("nmFamilia",nmFamilia).
				setParameter("nmSubFamilia",nmSubFamilia).
				setParameter("nmCargo",nmCargo).
				setParameter("nmMercado",mercGeral).
				setParameter("gradeMinimo",gradeMinimo).
				setParameter("gradeMaximo",gradeMaximo).

				setParameter("nmFamilia",nmFamilia).
				setParameter("nmSubFamilia",nmSubFamilia).
				setParameter("nmCargo",nmCargo).
				setParameter("nmMercado",mercGeral).
				setParameter("gradeMinimo",gradeMinimo).
				setParameter("gradeMaximo",gradeMaximo).


				//RDA
				setParameter("nmFamilia",nmFamilia).
				setParameter("nmSubFamilia",nmSubFamilia).
				setParameter("nmCargo",nmCargo).
				setParameter("nmMercado",mercGeral).
				setParameter("gradeMinimo",gradeMinimo).
				setParameter("gradeMaximo",gradeMaximo).

				setParameter("nmFamilia",nmFamilia).
				setParameter("nmSubFamilia",nmSubFamilia).
				setParameter("nmCargo",nmCargo).
				setParameter("nmMercado",mercGeral).
				setParameter("gradeMinimo",gradeMinimo).
				setParameter("gradeMaximo",gradeMaximo).

				setParameter("nmFamilia",nmFamilia).
				setParameter("nmSubFamilia",nmSubFamilia).
				setParameter("nmCargo",nmCargo).
				setParameter("nmMercado",mercGeral).
				setParameter("gradeMinimo",gradeMinimo).
				setParameter("gradeMaximo",gradeMaximo).

				//RD
				setParameter("nmFamilia",nmFamilia).
				setParameter("nmSubFamilia",nmSubFamilia).
				setParameter("nmCargo",nmCargo).
				setParameter("nmMercado",mercGeral).
				setParameter("gradeMinimo",gradeMinimo).
				setParameter("gradeMaximo",gradeMaximo).

				setParameter("nmFamilia",nmFamilia).
				setParameter("nmSubFamilia",nmSubFamilia).
				setParameter("nmCargo",nmCargo).
				setParameter("nmMercado",mercGeral).
				setParameter("gradeMinimo",gradeMinimo).
				setParameter("gradeMaximo",gradeMaximo).

				setParameter("nmFamilia",nmFamilia).
				setParameter("nmSubFamilia",nmSubFamilia).
				setParameter("nmCargo",nmCargo).
				setParameter("nmMercado",mercGeral).
				setParameter("gradeMinimo",gradeMinimo).
				setParameter("gradeMaximo",gradeMaximo).

				getResultList();

		for(Object record: lista) {
			Object[] linha = (Object[]) record; 
			MediasVO medias = new MediasVO();
			medias.setDescRenum((String) linha[0]);
			medias.setNomeCargo((String) linha[1]);
			medias.setMedia((int) linha[2]);
			medias.setP90((int) linha[3]);
			medias.setP75((int) linha[4]);
			medias.setP50((int) linha[5]);
			medias.setP25((int) linha[6]);
			medias.setP10((int) linha[7]);
			medias.setQtd_empresas((int) linha[8]);
			medias.setNum_participantes((int) linha[9]);
			mediasFinal.add(medias);
		}

		for(int i=0; i<mediasFinal.size();i++) {

			if(mediasFinal.get(i).getDescRenum().equals("SBA - Salário Base Anual")) {
				if(mediasFinal.get(i).getNum_participantes()<3) {

					return null;
				}
			}

			if(mediasFinal.get(i).getQtd_empresas()<3) {
				mediasFinal.get(i).setP10(0);
				mediasFinal.get(i).setP25(0);
				mediasFinal.get(i).setP50(0);
				mediasFinal.get(i).setP75(0);
				mediasFinal.get(i).setP90(0);
				mediasFinal.get(i).setMedia(0);
				mediasFinal.get(i).setNum_participantes(mediasFinal.get(i).getQtd_empresas());

			}
			//Informações do cargo em 3 empresas: o sistema calcula somente a média
			else if (mediasFinal.get(i).getQtd_empresas()>=3 && 
					mediasFinal.get(i).getQtd_empresas()<4) {
				mediasFinal.get(i).setP10(0);
				mediasFinal.get(i).setP25(0);
				mediasFinal.get(i).setP50(0);
				mediasFinal.get(i).setP75(0);
				mediasFinal.get(i).setP90(0);

			}
			//Informações do cargo em 4 empresas: o sistema calcula a média e o P50
			else if (mediasFinal.get(i).getQtd_empresas()>=4 && 
					mediasFinal.get(i).getQtd_empresas()<6) {
				mediasFinal.get(i).setP10(0);
				mediasFinal.get(i).setP25(0);
				mediasFinal.get(i).setP75(0);
				mediasFinal.get(i).setP90(0);

			}
			//Informações do cargo em 6 empresas: o sistema calcula a média, o P50 e P25 e P75.
			else if (
					mediasFinal.get(i).getQtd_empresas()>=6
					&& 
					mediasFinal.get(i).getQtd_empresas()<8) {
				mediasFinal.get(i).setP10(0);
				mediasFinal.get(i).setP90(0);	
			}
			//Informações do cargo em 8 empresas: o sistema calcula a média, o P50 e P25 e P75, P90 e P10
			else if (mediasFinal.get(i).getQtd_empresas()>=8) {
			}

		}

		return mediasFinal;

	}




	@SuppressWarnings("unchecked")
	@Transactional
	public List<MediasVO> findMediaSuaEmpresa(String nmFamilia, String nmSubFamilia,
			String nmCargo, String mercGeral,
			Integer gradeMinimo, Integer gradeMaximo, String nomeEmpresa, Integer maiorGradeEmpresa) {

		List<MediasVO> mediasFinal = new ArrayList<MediasVO>();


		List<Object> lista =  getEntityManagerMatriz().createNativeQuery(""
				+ "select \n"
				+ "consulta.desc_renum,\n"
				+ "consulta.nome_cargo,\n"
				+ "consulta.media,\n"
				+ "consulta.p90,\n"
				+ "consulta.p75,\n"
				+ "consulta.p50,\n"
				+ "consulta.p25,\n"
				+ "consulta.p10,\n"
				+ "consulta.qtd_empresas,\n"
				+ "consulta.num_participantes,\n"
				+ "cast(round(sua_empresa.media_sb/13.33,0) as int) sua_empresa\n"
				+ "from (\n"
				+ " select\n"
				+ "   desc_renum,\n"
				+ "   media.nome_cargo,\n"
				+ "   cast(round(media_sb / 13.33, 0) as int) media,\n"
				+ "   cast(round(p90 / 13.33, 0) as int) as p90,\n"
				+ "   cast(round(p75 / 13.33, 0) as int) as p75,\n"
				+ "   cast(round(p50 / 13.33, 0) as int) as p50,\n"
				+ "   cast(round(p25 / 13.33, 0) as int) as p25,\n"
				+ "   cast(round(p10 / 13.33, 0) as int) as p10,\n"
				+ "   qtd_empresas,\n"
				+ "   num_participantes \n"
				+ "from\n"
				+ "   (select desc_renum,nome_cargo,avg(media_sb) media_sb,count(1) qtd_empresas,sum(freq_fun) num_participantes from\n"
				+ "       (select query.nome_cargo, query.desc_renum, max_grade.max_grade,query.grade,query.freq_fun,query.nome_empresa,query.media_sb \n"
				+ "            from (\n"
				+ "                  select tp.nome_cargo, avg(media_sb) media_sb,nome_empresa,sum(freq_fun ) freq_fun,grade,\n"
				+ "                  'SBM - Salário Base Mensal' as desc_renum \n"
				+ "                  from tb_pesquisa tp inner join\n"
				+ "                        TB_PESQUISA_MERCADO tpm \n"
				+ "                        on (tp.codigo_empresa = tpm.cd_empresa) \n"
				+ "                     inner join\n"
				+ "                        tb_mercado tm \n"
				+ "                        on (tpm.cd_mercado = tm.id) \n"
				+ "                  where\n"
				+ "                        nm_familia = :nmFamilia \n"
				+ "                        and nm_subfamilia = :nmSubFamilia \n"
				+ "                        and nome_cargo = :nmCargo \n"
				+ "                        and tm.dsMercado = :nmMercado \n"
				+ "                        and tp.grade >= :gradeMinimo \n"
				+ "                        and tp.grade <= :gradeMaximo \n"
				+ "                     and media_sb is not null \n"
				+ "                  group by\n"
				+ "                     nome_cargo,nome_empresa,grade\n"
				+ "               )\n"
				+ "               query \n"
				+ "               inner join( SELECT MAX(grade) max_grade, nome_empresa  FROM tb_pesquisa tp \n"
				+ "                        inner join\n"
				+ "                           TB_PESQUISA_MERCADO tpm \n"
				+ "                           on (tp.codigo_empresa = tpm.cd_empresa) \n"
				+ "                        inner join\n"
				+ "                           tb_mercado tm \n"
				+ "                           on (tpm.cd_mercado = tm.id) \n"
				+ "                     where\n"
				+ "                        nm_familia = :nmFamilia \n"
				+ "                        and nm_subfamilia = :nmSubFamilia \n"
				+ "                        and nome_cargo = :nmCargo \n"
				+ "                        and tm.dsMercado = :nmMercado \n"
				+ "                        and tp.grade >= :gradeMinimo \n"
				+ "                        and tp.grade <= :gradeMaximo \n"
				+ "                        and media_sb is not null \n"
				+ "                     group by\n"
				+ "                        nome_empresa\n"
				+ "                  )\n"
				+ "                  max_grade \n"
				+ "                  on (max_grade.nome_empresa = query.nome_empresa)) total \n"
				+ "      				where total.grade = total.max_grade \n"
				+ "      group by desc_renum,nome_cargo )media \n"
				+ "   inner join\n"
				+ "      (\n"
				+ "         SELECT DISTINCT\n"
				+ "            [nome_cargo],\n"
				+ "            P90 = PERCENTILE_CONT(0.90) WITHIN GROUP ( ORDER BY media_sb) OVER (PARTITION BY [nome_cargo]),\n"
				+ "            P75 = PERCENTILE_CONT(0.75) WITHIN GROUP (ORDER BY media_sb) OVER (PARTITION BY [nome_cargo]),\n"
				+ "            P50 = PERCENTILE_CONT(0.50) WITHIN GROUP ( ORDER BY media_sb) OVER (PARTITION BY [nome_cargo]),\n"
				+ "            P25 = PERCENTILE_CONT(0.25) WITHIN GROUP (ORDER BY media_sb) OVER (PARTITION BY [nome_cargo]),\n"
				+ "            P10 = PERCENTILE_CONT(0.10) WITHIN GROUP ( ORDER BY media_sb) OVER (PARTITION BY [nome_cargo]) \n"
				+ "         FROM\n"
				+ "            tb_pesquisa tp \n"
				+ "            inner join\n"
				+ "               TB_PESQUISA_MERCADO tpm \n"
				+ "               on (tp.codigo_empresa = tpm.cd_empresa) \n"
				+ "            inner join\n"
				+ "               tb_mercado tm \n"
				+ "               on (tpm.cd_mercado = tm.id) \n"
				+ "         where\n"
				+ "              nm_familia = :nmFamilia \n"
				+ "                        and nm_subfamilia = :nmSubFamilia \n"
				+ "                        and nome_cargo = :nmCargo \n"
				+ "                        and tm.dsMercado = :nmMercado \n"
				+ "                        and tp.grade >= :gradeMinimo \n"
				+ "                        and tp.grade <= :gradeMaximo \n"
				+ "            and media_sb is not null \n"
				+ "      )\n"
				+ "      percentis \n"
				+ "      on(media.nome_cargo = percentis.nome_cargo)) consulta\n"
				+ "      LEFT OUTER JOIN (\n"
				+ "select round(avg(media_sb ),0) media_sb,nome_cargo from (\n"
				+ "select tp.nome_cargo, avg(media_sb) media_sb,nome_empresa  from tb_pesquisa tp \n"
				+ "inner join TB_PESQUISA_MERCADO tpm \n"
				+ "on (tp.codigo_empresa = tpm.cd_empresa) \n"
				+ "inner join tb_mercado tm \n"
				+ "on (tpm.cd_mercado = tm.id) \n"
				+ " where                  nm_familia = :nmFamilia \n"
				+ "                        and nm_subfamilia = :nmSubFamilia \n"
				+ "                        and nome_cargo = :nmCargo \n"
				+ "                        and tp.grade =:maiorGrade " 
				+ "					   and tp.nome_empresa = :nomeEmpresa\n"
				+ "and media_sb is not null \n"
				+ "group by nome_cargo,nome_empresa) d\n"
				+ "group by nome_cargo) sua_empresa\n"
				+ "on(consulta.nome_cargo = sua_empresa.nome_cargo)"
				+ ""
				+ ""
				+ " UNION ALL "
				+ ""
				+ ""
				+ " select \n"
				+ "consulta.desc_renum,\n"
				+ "consulta.nome_cargo,\n"
				+ "consulta.media,\n"
				+ "consulta.p90,\n"
				+ "consulta.p75,\n"
				+ "consulta.p50,\n"
				+ "consulta.p25,\n"
				+ "consulta.p10,\n"
				+ "consulta.qtd_empresas,\n"
				+ "consulta.num_participantes,\n"
				+ "cast(round(sua_empresa.media_sb,0) as int) sua_empresa\n"
				+ "from (\n"
				+ " select\n"
				+ "   desc_renum,\n"
				+ "   media.nome_cargo,\n"
				+ "   cast(round(media_sb, 0) as int) media,\n"
				+ "   cast(round(p90, 0) as int) as p90,\n"
				+ "   cast(round(p75, 0) as int) as p75,\n"
				+ "   cast(round(p50, 0) as int) as p50,\n"
				+ "   cast(round(p25, 0) as int) as p25,\n"
				+ "   cast(round(p10, 0) as int) as p10,\n"
				+ "   qtd_empresas,\n"
				+ "   num_participantes \n"
				+ "from\n"
				+ "   (select desc_renum,nome_cargo,avg(media_sb) media_sb,count(1) qtd_empresas,sum(freq_fun) num_participantes from\n"
				+ "       (select query.nome_cargo, query.desc_renum, max_grade.max_grade,query.grade,query.freq_fun,query.nome_empresa,query.media_sb \n"
				+ "            from (\n"
				+ "                  select tp.nome_cargo, avg(media_sb) media_sb,nome_empresa,sum(freq_fun ) freq_fun,grade,\n"
				+ "                  'SBA - Salário Base Anual' as desc_renum \n"
				+ "                  from tb_pesquisa tp inner join\n"
				+ "                        TB_PESQUISA_MERCADO tpm \n"
				+ "                        on (tp.codigo_empresa = tpm.cd_empresa) \n"
				+ "                     inner join\n"
				+ "                        tb_mercado tm \n"
				+ "                        on (tpm.cd_mercado = tm.id) \n"
				+ "                  where\n"
				+ "                        nm_familia = :nmFamilia \n"
				+ "                        and nm_subfamilia = :nmSubFamilia \n"
				+ "                        and nome_cargo = :nmCargo \n"
				+ "                        and tm.dsMercado = :nmMercado \n"
				+ "                        and tp.grade >= :gradeMinimo \n"
				+ "                        and tp.grade <= :gradeMaximo \n"
				+ "                     and media_sb is not null \n"
				+ "                  group by\n"
				+ "                     nome_cargo,nome_empresa,grade\n"
				+ "               )\n"
				+ "               query \n"
				+ "               inner join( SELECT MAX(grade) max_grade, nome_empresa  FROM tb_pesquisa tp \n"
				+ "                        inner join\n"
				+ "                           TB_PESQUISA_MERCADO tpm \n"
				+ "                           on (tp.codigo_empresa = tpm.cd_empresa) \n"
				+ "                        inner join\n"
				+ "                           tb_mercado tm \n"
				+ "                           on (tpm.cd_mercado = tm.id) \n"
				+ "                     where\n"
				+ "                        nm_familia = :nmFamilia \n"
				+ "                        and nm_subfamilia = :nmSubFamilia \n"
				+ "                        and nome_cargo = :nmCargo \n"
				+ "                        and tm.dsMercado = :nmMercado \n"
				+ "                        and tp.grade >= :gradeMinimo \n"
				+ "                        and tp.grade <= :gradeMaximo \n"
				+ "                        and media_sb is not null \n"
				+ "                     group by\n"
				+ "                        nome_empresa\n"
				+ "                  )\n"
				+ "                  max_grade \n"
				+ "                  on (max_grade.nome_empresa = query.nome_empresa)) total \n"
				+ "      				where total.grade = total.max_grade \n"
				+ "      group by desc_renum,nome_cargo )media \n"
				+ "   inner join\n"
				+ "      (\n"
				+ "         SELECT DISTINCT\n"
				+ "            [nome_cargo],\n"
				+ "            P90 = PERCENTILE_CONT(0.90) WITHIN GROUP ( ORDER BY media_sb) OVER (PARTITION BY [nome_cargo]),\n"
				+ "            P75 = PERCENTILE_CONT(0.75) WITHIN GROUP (ORDER BY media_sb) OVER (PARTITION BY [nome_cargo]),\n"
				+ "            P50 = PERCENTILE_CONT(0.50) WITHIN GROUP ( ORDER BY media_sb) OVER (PARTITION BY [nome_cargo]),\n"
				+ "            P25 = PERCENTILE_CONT(0.25) WITHIN GROUP (ORDER BY media_sb) OVER (PARTITION BY [nome_cargo]),\n"
				+ "            P10 = PERCENTILE_CONT(0.10) WITHIN GROUP ( ORDER BY media_sb) OVER (PARTITION BY [nome_cargo]) \n"
				+ "         FROM\n"
				+ "            tb_pesquisa tp \n"
				+ "            inner join\n"
				+ "               TB_PESQUISA_MERCADO tpm \n"
				+ "               on (tp.codigo_empresa = tpm.cd_empresa) \n"
				+ "            inner join\n"
				+ "               tb_mercado tm \n"
				+ "               on (tpm.cd_mercado = tm.id) \n"
				+ "         where\n"
				+ "              nm_familia = :nmFamilia \n"
				+ "                        and nm_subfamilia = :nmSubFamilia \n"
				+ "                        and nome_cargo = :nmCargo \n"
				+ "                        and tm.dsMercado = :nmMercado \n"
				+ "                        and tp.grade >= :gradeMinimo \n"
				+ "                        and tp.grade <= :gradeMaximo \n"
				+ "            and media_sb is not null \n"
				+ "      )\n"
				+ "      percentis \n"
				+ "      on(media.nome_cargo = percentis.nome_cargo)) consulta\n"
				+ "      LEFT OUTER JOIN (\n"
				+ "select round(avg(media_sb ),0) media_sb,nome_cargo from (\n"
				+ "select tp.nome_cargo, avg(media_sb) media_sb,nome_empresa  from tb_pesquisa tp \n"
				+ "inner join TB_PESQUISA_MERCADO tpm \n"
				+ "on (tp.codigo_empresa = tpm.cd_empresa) \n"
				+ "inner join tb_mercado tm \n"
				+ "on (tpm.cd_mercado = tm.id) \n"
				+ " where                  nm_familia = :nmFamilia \n"
				+ "                        and nm_subfamilia = :nmSubFamilia \n"
				+ "                        and nome_cargo = :nmCargo \n"
				+ "                        and tp.grade =:maiorGrade " 
				+ "					   and tp.nome_empresa = :nomeEmpresa\n"
				+ "and media_sb is not null \n"
				+ "group by nome_cargo,nome_empresa) d\n"
				+ "group by nome_cargo) sua_empresa\n"
				+ "on(consulta.nome_cargo = sua_empresa.nome_cargo)"
				+ ""
				+ ""
				+ " UNION ALL "
				+ ""
				+ ""
				+ " select \n"
				+ "consulta.desc_renum,\n"
				+ "consulta.nome_cargo,\n"
				+ "consulta.media,\n"
				+ "consulta.p90,\n"
				+ "consulta.p75,\n"
				+ "consulta.p50,\n"
				+ "consulta.p25,\n"
				+ "consulta.p10,\n"
				+ "consulta.qtd_empresas,\n"
				+ "consulta.num_participantes,\n"
				+ "cast(round(sua_empresa.mediana_cp_aliv,0) as int) sua_empresa\n"
				+ "from (\n"
				+ " select\n"
				+ "   desc_renum,\n"
				+ "   media.nome_cargo,\n"
				+ "   cast(round(mediana_cp_aliv, 0) as int) media,\n"
				+ "   cast(round(p90, 0) as int) as p90,\n"
				+ "   cast(round(p75, 0) as int) as p75,\n"
				+ "   cast(round(p50, 0) as int) as p50,\n"
				+ "   cast(round(p25, 0) as int) as p25,\n"
				+ "   cast(round(p10, 0) as int) as p10,\n"
				+ "   qtd_empresas,\n"
				+ "   num_participantes \n"
				+ "from\n"
				+ "   (select desc_renum,nome_cargo,avg(mediana_cp_aliv) mediana_cp_aliv,count(1) qtd_empresas,sum(freq_fun) num_participantes from\n"
				+ "       (select query.nome_cargo, query.desc_renum, max_grade.max_grade,query.grade,query.freq_fun,query.nome_empresa,query.mediana_cp_aliv \n"
				+ "            from (\n"
				+ "                  select tp.nome_cargo, avg(mediana_cp_aliv) mediana_cp_aliv,nome_empresa,sum(freq_fun ) freq_fun,grade,\n"
				+ "                  'ICPA - Incentivo de Curto Prazo Alvo (Bônus + PLR)' as desc_renum \n"
				+ "                  from tb_pesquisa tp inner join\n"
				+ "                        TB_PESQUISA_MERCADO tpm \n"
				+ "                        on (tp.codigo_empresa = tpm.cd_empresa) \n"
				+ "                     inner join\n"
				+ "                        tb_mercado tm \n"
				+ "                        on (tpm.cd_mercado = tm.id) \n"
				+ "                  where\n"
				+ "                        nm_familia = :nmFamilia \n"
				+ "                        and nm_subfamilia = :nmSubFamilia \n"
				+ "                        and nome_cargo = :nmCargo \n"
				+ "                        and tm.dsMercado = :nmMercado \n"
				+ "                        and tp.grade >= :gradeMinimo \n"
				+ "                        and tp.grade <= :gradeMaximo \n"
				+ "                     and mediana_cp_aliv is not null \n"
				+ "                  group by\n"
				+ "                     nome_cargo,nome_empresa,grade\n"
				+ "               )\n"
				+ "               query \n"
				+ "               inner join( SELECT MAX(grade) max_grade, nome_empresa  FROM tb_pesquisa tp \n"
				+ "                        inner join\n"
				+ "                           TB_PESQUISA_MERCADO tpm \n"
				+ "                           on (tp.codigo_empresa = tpm.cd_empresa) \n"
				+ "                        inner join\n"
				+ "                           tb_mercado tm \n"
				+ "                           on (tpm.cd_mercado = tm.id) \n"
				+ "                     where\n"
				+ "                        nm_familia = :nmFamilia \n"
				+ "                        and nm_subfamilia = :nmSubFamilia \n"
				+ "                        and nome_cargo = :nmCargo \n"
				+ "                        and tm.dsMercado = :nmMercado \n"
				+ "                        and tp.grade >= :gradeMinimo \n"
				+ "                        and tp.grade <= :gradeMaximo \n"
				+ "                        and mediana_cp_aliv is not null \n"
				+ "                     group by\n"
				+ "                        nome_empresa\n"
				+ "                  )\n"
				+ "                  max_grade \n"
				+ "                  on (max_grade.nome_empresa = query.nome_empresa)) total \n"
				+ "      				where total.grade = total.max_grade \n"
				+ "      group by desc_renum,nome_cargo )media \n"
				+ "   inner join\n"
				+ "      (\n"
				+ "         SELECT DISTINCT\n"
				+ "            [nome_cargo],\n"
				+ "            P90 = PERCENTILE_CONT(0.90) WITHIN GROUP ( ORDER BY mediana_cp_aliv) OVER (PARTITION BY [nome_cargo]),\n"
				+ "            P75 = PERCENTILE_CONT(0.75) WITHIN GROUP (ORDER BY mediana_cp_aliv) OVER (PARTITION BY [nome_cargo]),\n"
				+ "            P50 = PERCENTILE_CONT(0.50) WITHIN GROUP ( ORDER BY mediana_cp_aliv) OVER (PARTITION BY [nome_cargo]),\n"
				+ "            P25 = PERCENTILE_CONT(0.25) WITHIN GROUP (ORDER BY mediana_cp_aliv) OVER (PARTITION BY [nome_cargo]),\n"
				+ "            P10 = PERCENTILE_CONT(0.10) WITHIN GROUP ( ORDER BY mediana_cp_aliv) OVER (PARTITION BY [nome_cargo]) \n"
				+ "         FROM\n"
				+ "            tb_pesquisa tp \n"
				+ "            inner join\n"
				+ "               TB_PESQUISA_MERCADO tpm \n"
				+ "               on (tp.codigo_empresa = tpm.cd_empresa) \n"
				+ "            inner join\n"
				+ "               tb_mercado tm \n"
				+ "               on (tpm.cd_mercado = tm.id) \n"
				+ "         where\n"
				+ "              nm_familia = :nmFamilia \n"
				+ "                        and nm_subfamilia = :nmSubFamilia \n"
				+ "                        and nome_cargo = :nmCargo \n"
				+ "                        and tm.dsMercado = :nmMercado \n"
				+ "                        and tp.grade >= :gradeMinimo \n"
				+ "                        and tp.grade <= :gradeMaximo \n"
				+ "            and mediana_cp_aliv is not null \n"
				+ "      )\n"
				+ "      percentis \n"
				+ "      on(media.nome_cargo = percentis.nome_cargo)) consulta\n"
				+ "      LEFT OUTER JOIN (\n"
				+ "select round(avg(mediana_cp_aliv ),0) mediana_cp_aliv,nome_cargo from (\n"
				+ "select tp.nome_cargo, avg(mediana_cp_aliv) mediana_cp_aliv,nome_empresa  from tb_pesquisa tp \n"
				+ "inner join TB_PESQUISA_MERCADO tpm \n"
				+ "on (tp.codigo_empresa = tpm.cd_empresa) \n"
				+ "inner join tb_mercado tm \n"
				+ "on (tpm.cd_mercado = tm.id) \n"
				+ " where                  nm_familia = :nmFamilia \n"
				+ "                        and nm_subfamilia = :nmSubFamilia \n"
				+ "                        and nome_cargo = :nmCargo \n"
				+ "                        and tm.dsMercado = :nmMercado \n"
				+ "                        and tp.grade =:maiorGrade " 
				+ "					   and tp.nome_empresa = :nomeEmpresa\n"
				+ "and mediana_cp_aliv is not null \n"
				+ "group by nome_cargo,nome_empresa) d\n"
				+ "group by nome_cargo) sua_empresa\n"
				+ "on(consulta.nome_cargo = sua_empresa.nome_cargo)"
				+ ""
				+ ""
				+ " UNION ALL  "
				+ ""
				+ " select \n"
				+ "consulta.desc_renum,\n"
				+ "consulta.nome_cargo,\n"
				+ "consulta.media,\n"
				+ "consulta.p90,\n"
				+ "consulta.p75,\n"
				+ "consulta.p50,\n"
				+ "consulta.p25,\n"
				+ "consulta.p10,\n"
				+ "consulta.qtd_empresas,\n"
				+ "consulta.num_participantes,\n"
				+ "cast(round(sua_empresa.media_tda,0) as int) sua_empresa\n"
				+ "from (\n"
				+ " select\n"
				+ "   desc_renum,\n"
				+ "   media.nome_cargo,\n"
				+ "   cast(round(media_tda, 0) as int) media,\n"
				+ "   cast(round(p90, 0) as int) as p90,\n"
				+ "   cast(round(p75, 0) as int) as p75,\n"
				+ "   cast(round(p50, 0) as int) as p50,\n"
				+ "   cast(round(p25, 0) as int) as p25,\n"
				+ "   cast(round(p10, 0) as int) as p10,\n"
				+ "   qtd_empresas,\n"
				+ "   num_participantes \n"
				+ "from\n"
				+ "   (select desc_renum,nome_cargo,avg(media_tda) media_tda,count(1) qtd_empresas,sum(freq_fun) num_participantes from\n"
				+ "       (select query.nome_cargo, query.desc_renum, max_grade.max_grade,query.grade,query.freq_fun,query.nome_empresa,query.media_tda \n"
				+ "            from (\n"
				+ "                  select tp.nome_cargo, avg(media_tda) media_tda,nome_empresa,sum(freq_fun ) freq_fun,grade,\n"
				+ "                  'TDA - Total em Dinheiro Alvo' as desc_renum \n"
				+ "                  from tb_pesquisa tp inner join\n"
				+ "                        TB_PESQUISA_MERCADO tpm \n"
				+ "                        on (tp.codigo_empresa = tpm.cd_empresa) \n"
				+ "                     inner join\n"
				+ "                        tb_mercado tm \n"
				+ "                        on (tpm.cd_mercado = tm.id) \n"
				+ "                  where\n"
				+ "                        nm_familia = :nmFamilia \n"
				+ "                        and nm_subfamilia = :nmSubFamilia \n"
				+ "                        and nome_cargo = :nmCargo \n"
				+ "                        and tm.dsMercado = :nmMercado \n"
				+ "                        and tp.grade >= :gradeMinimo \n"
				+ "                        and tp.grade <= :gradeMaximo \n"
				+ "                     and media_tda is not null \n"
				+ "                  group by\n"
				+ "                     nome_cargo,nome_empresa,grade\n"
				+ "               )\n"
				+ "               query \n"
				+ "               inner join( SELECT MAX(grade) max_grade, nome_empresa  FROM tb_pesquisa tp \n"
				+ "                        inner join\n"
				+ "                           TB_PESQUISA_MERCADO tpm \n"
				+ "                           on (tp.codigo_empresa = tpm.cd_empresa) \n"
				+ "                        inner join\n"
				+ "                           tb_mercado tm \n"
				+ "                           on (tpm.cd_mercado = tm.id) \n"
				+ "                     where\n"
				+ "                        nm_familia = :nmFamilia \n"
				+ "                        and nm_subfamilia = :nmSubFamilia \n"
				+ "                        and nome_cargo = :nmCargo \n"
				+ "                        and tm.dsMercado = :nmMercado \n"
				+ "                        and tp.grade >= :gradeMinimo \n"
				+ "                        and tp.grade <= :gradeMaximo \n"
				+ "                        and media_tda is not null \n"
				+ "                     group by\n"
				+ "                        nome_empresa\n"
				+ "                  )\n"
				+ "                  max_grade \n"
				+ "                  on (max_grade.nome_empresa = query.nome_empresa)) total \n"
				+ "      				where total.grade = total.max_grade \n"
				+ "      group by desc_renum,nome_cargo )media \n"
				+ "   inner join\n"
				+ "      (\n"
				+ "         SELECT DISTINCT\n"
				+ "            [nome_cargo],\n"
				+ "            P90 = PERCENTILE_CONT(0.90) WITHIN GROUP ( ORDER BY media_tda) OVER (PARTITION BY [nome_cargo]),\n"
				+ "            P75 = PERCENTILE_CONT(0.75) WITHIN GROUP (ORDER BY media_tda) OVER (PARTITION BY [nome_cargo]),\n"
				+ "            P50 = PERCENTILE_CONT(0.50) WITHIN GROUP ( ORDER BY media_tda) OVER (PARTITION BY [nome_cargo]),\n"
				+ "            P25 = PERCENTILE_CONT(0.25) WITHIN GROUP (ORDER BY media_tda) OVER (PARTITION BY [nome_cargo]),\n"
				+ "            P10 = PERCENTILE_CONT(0.10) WITHIN GROUP ( ORDER BY media_tda) OVER (PARTITION BY [nome_cargo]) \n"
				+ "         FROM\n"
				+ "            tb_pesquisa tp \n"
				+ "            inner join\n"
				+ "               TB_PESQUISA_MERCADO tpm \n"
				+ "               on (tp.codigo_empresa = tpm.cd_empresa) \n"
				+ "            inner join\n"
				+ "               tb_mercado tm \n"
				+ "               on (tpm.cd_mercado = tm.id) \n"
				+ "         where\n"
				+ "              nm_familia = :nmFamilia \n"
				+ "                        and nm_subfamilia = :nmSubFamilia \n"
				+ "                        and nome_cargo = :nmCargo \n"
				+ "                        and tm.dsMercado = :nmMercado \n"
				+ "                        and tp.grade >= :gradeMinimo \n"
				+ "                        and tp.grade <= :gradeMaximo \n"
				+ "            and media_tda is not null \n"
				+ "      )\n"
				+ "      percentis \n"
				+ "      on(media.nome_cargo = percentis.nome_cargo)) consulta\n"
				+ "      LEFT OUTER JOIN (\n"
				+ "select round(avg(media_tda ),0) media_tda,nome_cargo from (\n"
				+ "select tp.nome_cargo, avg(media_tda) media_tda,nome_empresa  from tb_pesquisa tp \n"
				+ "inner join TB_PESQUISA_MERCADO tpm \n"
				+ "on (tp.codigo_empresa = tpm.cd_empresa) \n"
				+ "inner join tb_mercado tm \n"
				+ "on (tpm.cd_mercado = tm.id) \n"
				+ " where                  nm_familia = :nmFamilia \n"
				+ "                        and nm_subfamilia = :nmSubFamilia \n"
				+ "                        and nome_cargo = :nmCargo \n"
				+ "                        and tp.grade =:maiorGrade " 
				+ "					   and tp.nome_empresa = :nomeEmpresa\n"
				+ "and media_tda is not null \n"
				+ "group by nome_cargo,nome_empresa) d\n"
				+ "group by nome_cargo) sua_empresa\n"
				+ "on(consulta.nome_cargo = sua_empresa.nome_cargo) "
				+ ""
				+ ""
				+ " UNION ALL "
				+ ""
				+ ""
				+ " select \n"
				+ "consulta.desc_renum,\n"
				+ "consulta.nome_cargo,\n"
				+ "consulta.media,\n"
				+ "consulta.p90,\n"
				+ "consulta.p75,\n"
				+ "consulta.p50,\n"
				+ "consulta.p25,\n"
				+ "consulta.p10,\n"
				+ "consulta.qtd_empresas,\n"
				+ "consulta.num_participantes,\n"
				+ "cast(round(sua_empresa.mediana_cp_pg,0) as int) sua_empresa\n"
				+ "from (\n"
				+ " select\n"
				+ "   desc_renum,\n"
				+ "   media.nome_cargo,\n"
				+ "   cast(round(mediana_cp_pg, 0) as int) media,\n"
				+ "   cast(round(p90, 0) as int) as p90,\n"
				+ "   cast(round(p75, 0) as int) as p75,\n"
				+ "   cast(round(p50, 0) as int) as p50,\n"
				+ "   cast(round(p25, 0) as int) as p25,\n"
				+ "   cast(round(p10, 0) as int) as p10,\n"
				+ "   qtd_empresas,\n"
				+ "   num_participantes \n"
				+ "from\n"
				+ "   (select desc_renum,nome_cargo,avg(mediana_cp_pg) mediana_cp_pg,count(1) qtd_empresas,sum(freq_fun) num_participantes from\n"
				+ "       (select query.nome_cargo, query.desc_renum, max_grade.max_grade,query.grade,query.freq_fun,query.nome_empresa,query.mediana_cp_pg \n"
				+ "            from (\n"
				+ "                  select tp.nome_cargo, avg(mediana_cp_pg) mediana_cp_pg,nome_empresa,sum(freq_fun ) freq_fun,grade,\n"
				+ "                  'ICP - Incentivo de Curto Prazo Pago (Bônus + PLR)' as desc_renum \n"
				+ "                  from tb_pesquisa tp inner join\n"
				+ "                        TB_PESQUISA_MERCADO tpm \n"
				+ "                        on (tp.codigo_empresa = tpm.cd_empresa) \n"
				+ "                     inner join\n"
				+ "                        tb_mercado tm \n"
				+ "                        on (tpm.cd_mercado = tm.id) \n"
				+ "                  where\n"
				+ "                        nm_familia = :nmFamilia \n"
				+ "                        and nm_subfamilia = :nmSubFamilia \n"
				+ "                        and nome_cargo = :nmCargo \n"
				+ "                        and tm.dsMercado = :nmMercado \n"
				+ "                        and tp.grade >= :gradeMinimo \n"
				+ "                        and tp.grade <= :gradeMaximo \n"
				+ "                     and mediana_cp_pg is not null \n"
				+ "                  group by\n"
				+ "                     nome_cargo,nome_empresa,grade\n"
				+ "               )\n"
				+ "               query \n"
				+ "               inner join( SELECT MAX(grade) max_grade, nome_empresa  FROM tb_pesquisa tp \n"
				+ "                        inner join\n"
				+ "                           TB_PESQUISA_MERCADO tpm \n"
				+ "                           on (tp.codigo_empresa = tpm.cd_empresa) \n"
				+ "                        inner join\n"
				+ "                           tb_mercado tm \n"
				+ "                           on (tpm.cd_mercado = tm.id) \n"
				+ "                     where\n"
				+ "                        nm_familia = :nmFamilia \n"
				+ "                        and nm_subfamilia = :nmSubFamilia \n"
				+ "                        and nome_cargo = :nmCargo \n"
				+ "                        and tm.dsMercado = :nmMercado \n"
				+ "                        and tp.grade >= :gradeMinimo \n"
				+ "                        and tp.grade <= :gradeMaximo \n"
				+ "                        and mediana_cp_pg is not null \n"
				+ "                     group by\n"
				+ "                        nome_empresa\n"
				+ "                  )\n"
				+ "                  max_grade \n"
				+ "                  on (max_grade.nome_empresa = query.nome_empresa)) total \n"
				+ "      				where total.grade = total.max_grade \n"
				+ "      group by desc_renum,nome_cargo )media \n"
				+ "   inner join\n"
				+ "      (\n"
				+ "         SELECT DISTINCT\n"
				+ "            [nome_cargo],\n"
				+ "            P90 = PERCENTILE_CONT(0.90) WITHIN GROUP ( ORDER BY mediana_cp_pg) OVER (PARTITION BY [nome_cargo]),\n"
				+ "            P75 = PERCENTILE_CONT(0.75) WITHIN GROUP (ORDER BY mediana_cp_pg) OVER (PARTITION BY [nome_cargo]),\n"
				+ "            P50 = PERCENTILE_CONT(0.50) WITHIN GROUP ( ORDER BY mediana_cp_pg) OVER (PARTITION BY [nome_cargo]),\n"
				+ "            P25 = PERCENTILE_CONT(0.25) WITHIN GROUP (ORDER BY mediana_cp_pg) OVER (PARTITION BY [nome_cargo]),\n"
				+ "            P10 = PERCENTILE_CONT(0.10) WITHIN GROUP ( ORDER BY mediana_cp_pg) OVER (PARTITION BY [nome_cargo]) \n"
				+ "         FROM\n"
				+ "            tb_pesquisa tp \n"
				+ "            inner join\n"
				+ "               TB_PESQUISA_MERCADO tpm \n"
				+ "               on (tp.codigo_empresa = tpm.cd_empresa) \n"
				+ "            inner join\n"
				+ "               tb_mercado tm \n"
				+ "               on (tpm.cd_mercado = tm.id) \n"
				+ "         where\n"
				+ "              nm_familia = :nmFamilia \n"
				+ "                        and nm_subfamilia = :nmSubFamilia \n"
				+ "                        and nome_cargo = :nmCargo \n"
				+ "                        and tm.dsMercado = :nmMercado \n"
				+ "                        and tp.grade >= :gradeMinimo \n"
				+ "                        and tp.grade <= :gradeMaximo \n"
				+ "            and mediana_cp_pg is not null \n"
				+ "      )\n"
				+ "      percentis \n"
				+ "      on(media.nome_cargo = percentis.nome_cargo)) consulta\n"
				+ "      LEFT OUTER JOIN (\n"
				+ "select round(avg(mediana_cp_pg ),0) mediana_cp_pg,nome_cargo from (\n"
				+ "select tp.nome_cargo, avg(mediana_cp_pg) mediana_cp_pg,nome_empresa  from tb_pesquisa tp \n"
				+ "inner join TB_PESQUISA_MERCADO tpm \n"
				+ "on (tp.codigo_empresa = tpm.cd_empresa) \n"
				+ "inner join tb_mercado tm \n"
				+ "on (tpm.cd_mercado = tm.id) \n"
				+ " where                  nm_familia = :nmFamilia \n"
				+ "                        and nm_subfamilia = :nmSubFamilia \n"
				+ "                        and nome_cargo = :nmCargo \n"
				+ "                        and tp.grade =:maiorGrade " 
				+ "					   and tp.nome_empresa = :nomeEmpresa\n"
				+ "and mediana_cp_pg is not null \n"
				+ "group by nome_cargo,nome_empresa) d\n"
				+ "group by nome_cargo) sua_empresa\n"
				+ "on(consulta.nome_cargo = sua_empresa.nome_cargo)"
				+ ""
				+ " UNION ALL "
				+ ""
				+ ""
				+ ""
				+ " select \n"
				+ "consulta.desc_renum,\n"
				+ "consulta.nome_cargo,\n"
				+ "consulta.media,\n"
				+ "consulta.p90,\n"
				+ "consulta.p75,\n"
				+ "consulta.p50,\n"
				+ "consulta.p25,\n"
				+ "consulta.p10,\n"
				+ "consulta.qtd_empresas,\n"
				+ "consulta.num_participantes,\n"
				+ "cast(round(sua_empresa.media_tb,0) as int) sua_empresa\n"
				+ "from (\n"
				+ " select\n"
				+ "   desc_renum,\n"
				+ "   media.nome_cargo,\n"
				+ "   cast(round(media_tb, 0) as int) media,\n"
				+ "   cast(round(p90, 0) as int) as p90,\n"
				+ "   cast(round(p75, 0) as int) as p75,\n"
				+ "   cast(round(p50, 0) as int) as p50,\n"
				+ "   cast(round(p25, 0) as int) as p25,\n"
				+ "   cast(round(p10, 0) as int) as p10,\n"
				+ "   qtd_empresas,\n"
				+ "   num_participantes \n"
				+ "from\n"
				+ "   (select desc_renum,nome_cargo,avg(media_tb) media_tb,count(1) qtd_empresas,sum(freq_fun) num_participantes from\n"
				+ "       (select query.nome_cargo, query.desc_renum, max_grade.max_grade,query.grade,query.freq_fun,query.nome_empresa,query.media_tb \n"
				+ "            from (\n"
				+ "                  select tp.nome_cargo, avg(media_tb) media_tb,nome_empresa,sum(freq_fun ) freq_fun,grade,\n"
				+ "                  'TD - Total em Dinheiro' as desc_renum \n"
				+ "                  from tb_pesquisa tp inner join\n"
				+ "                        TB_PESQUISA_MERCADO tpm \n"
				+ "                        on (tp.codigo_empresa = tpm.cd_empresa) \n"
				+ "                     inner join\n"
				+ "                        tb_mercado tm \n"
				+ "                        on (tpm.cd_mercado = tm.id) \n"
				+ "                  where\n"
				+ "                        nm_familia = :nmFamilia \n"
				+ "                        and nm_subfamilia = :nmSubFamilia \n"
				+ "                        and nome_cargo = :nmCargo \n"
				+ "                        and tm.dsMercado = :nmMercado \n"
				+ "                        and tp.grade >= :gradeMinimo \n"
				+ "                        and tp.grade <= :gradeMaximo \n"
				+ "                     and media_tb is not null \n"
				+ "                  group by\n"
				+ "                     nome_cargo,nome_empresa,grade\n"
				+ "               )\n"
				+ "               query \n"
				+ "               inner join( SELECT MAX(grade) max_grade, nome_empresa  FROM tb_pesquisa tp \n"
				+ "                        inner join\n"
				+ "                           TB_PESQUISA_MERCADO tpm \n"
				+ "                           on (tp.codigo_empresa = tpm.cd_empresa) \n"
				+ "                        inner join\n"
				+ "                           tb_mercado tm \n"
				+ "                           on (tpm.cd_mercado = tm.id) \n"
				+ "                     where\n"
				+ "                        nm_familia = :nmFamilia \n"
				+ "                        and nm_subfamilia = :nmSubFamilia \n"
				+ "                        and nome_cargo = :nmCargo \n"
				+ "                        and tm.dsMercado = :nmMercado \n"
				+ "                        and tp.grade >= :gradeMinimo \n"
				+ "                        and tp.grade <= :gradeMaximo \n"
				+ "                        and media_tb is not null \n"
				+ "                     group by\n"
				+ "                        nome_empresa\n"
				+ "                  )\n"
				+ "                  max_grade \n"
				+ "                  on (max_grade.nome_empresa = query.nome_empresa)) total \n"
				+ "      				where total.grade = total.max_grade \n"
				+ "      group by desc_renum,nome_cargo )media \n"
				+ "   inner join\n"
				+ "      (\n"
				+ "         SELECT DISTINCT\n"
				+ "            [nome_cargo],\n"
				+ "            P90 = PERCENTILE_CONT(0.90) WITHIN GROUP ( ORDER BY media_tb) OVER (PARTITION BY [nome_cargo]),\n"
				+ "            P75 = PERCENTILE_CONT(0.75) WITHIN GROUP (ORDER BY media_tb) OVER (PARTITION BY [nome_cargo]),\n"
				+ "            P50 = PERCENTILE_CONT(0.50) WITHIN GROUP ( ORDER BY media_tb) OVER (PARTITION BY [nome_cargo]),\n"
				+ "            P25 = PERCENTILE_CONT(0.25) WITHIN GROUP (ORDER BY media_tb) OVER (PARTITION BY [nome_cargo]),\n"
				+ "            P10 = PERCENTILE_CONT(0.10) WITHIN GROUP ( ORDER BY media_tb) OVER (PARTITION BY [nome_cargo]) \n"
				+ "         FROM\n"
				+ "            tb_pesquisa tp \n"
				+ "            inner join\n"
				+ "               TB_PESQUISA_MERCADO tpm \n"
				+ "               on (tp.codigo_empresa = tpm.cd_empresa) \n"
				+ "            inner join\n"
				+ "               tb_mercado tm \n"
				+ "               on (tpm.cd_mercado = tm.id) \n"
				+ "         where\n"
				+ "              nm_familia = :nmFamilia \n"
				+ "                        and nm_subfamilia = :nmSubFamilia \n"
				+ "                        and nome_cargo = :nmCargo \n"
				+ "                        and tm.dsMercado = :nmMercado \n"
				+ "                        and tp.grade >= :gradeMinimo \n"
				+ "                        and tp.grade <= :gradeMaximo \n"
				+ "            and media_tb is not null \n"
				+ "      )\n"
				+ "      percentis \n"
				+ "      on(media.nome_cargo = percentis.nome_cargo)) consulta\n"
				+ "      LEFT OUTER JOIN (\n"
				+ "select round(avg(media_tb ),0) media_tb,nome_cargo from (\n"
				+ "select tp.nome_cargo, avg(media_tb) media_tb,nome_empresa  from tb_pesquisa tp \n"
				+ "inner join TB_PESQUISA_MERCADO tpm \n"
				+ "on (tp.codigo_empresa = tpm.cd_empresa) \n"
				+ "inner join tb_mercado tm \n"
				+ "on (tpm.cd_mercado = tm.id) \n"
				+ " where                  nm_familia = :nmFamilia \n"
				+ "                        and nm_subfamilia = :nmSubFamilia \n"
				+ "                        and nome_cargo = :nmCargo \n"
				+ "                        and tp.grade =:maiorGrade " 
				+ "					   and tp.nome_empresa = :nomeEmpresa\n"
				+ "and media_tb is not null \n"
				+ "group by nome_cargo,nome_empresa) d\n"
				+ "group by nome_cargo) sua_empresa\n"
				+ "on(consulta.nome_cargo = sua_empresa.nome_cargo)"
				+ ""
				+ " UNION ALL"
				+ ""
				+ ""
				+ " select \n"
				+ "consulta.desc_renum,\n"
				+ "consulta.nome_cargo,\n"
				+ "consulta.media,\n"
				+ "consulta.p90,\n"
				+ "consulta.p75,\n"
				+ "consulta.p50,\n"
				+ "consulta.p25,\n"
				+ "consulta.p10,\n"
				+ "consulta.qtd_empresas,\n"
				+ "consulta.num_participantes,\n"
				+ "cast(round(sua_empresa.mediana_ilp,0) as int) sua_empresa\n"
				+ "from (\n"
				+ " select\n"
				+ "   desc_renum,\n"
				+ "   media.nome_cargo,\n"
				+ "   cast(round(mediana_ilp, 0) as int) media,\n"
				+ "   cast(round(p90, 0) as int) as p90,\n"
				+ "   cast(round(p75, 0) as int) as p75,\n"
				+ "   cast(round(p50, 0) as int) as p50,\n"
				+ "   cast(round(p25, 0) as int) as p25,\n"
				+ "   cast(round(p10, 0) as int) as p10,\n"
				+ "   qtd_empresas,\n"
				+ "   num_participantes \n"
				+ "from\n"
				+ "   (select desc_renum,nome_cargo,avg(mediana_ilp) mediana_ilp,count(1) qtd_empresas,sum(freq_fun) num_participantes from\n"
				+ "       (select query.nome_cargo, query.desc_renum, max_grade.max_grade,query.grade,query.freq_fun,query.nome_empresa,query.mediana_ilp \n"
				+ "            from (\n"
				+ "                  select tp.nome_cargo, avg(mediana_ilp) mediana_ilp,nome_empresa,sum(freq_fun ) freq_fun,grade,\n"
				+ "                  'ILP - Incentivos de Longo Prazo' as desc_renum \n"
				+ "                  from tb_pesquisa tp inner join\n"
				+ "                        TB_PESQUISA_MERCADO tpm \n"
				+ "                        on (tp.codigo_empresa = tpm.cd_empresa) \n"
				+ "                     inner join\n"
				+ "                        tb_mercado tm \n"
				+ "                        on (tpm.cd_mercado = tm.id) \n"
				+ "                  where\n"
				+ "                        nm_familia = :nmFamilia \n"
				+ "                        and nm_subfamilia = :nmSubFamilia \n"
				+ "                        and nome_cargo = :nmCargo \n"
				+ "                        and tm.dsMercado = :nmMercado \n"
				+ "                        and tp.grade >= :gradeMinimo \n"
				+ "                        and tp.grade <= :gradeMaximo \n"
				+ "                     and mediana_ilp is not null \n"
				+ "                  group by\n"
				+ "                     nome_cargo,nome_empresa,grade\n"
				+ "               )\n"
				+ "               query \n"
				+ "               inner join( SELECT MAX(grade) max_grade, nome_empresa  FROM tb_pesquisa tp \n"
				+ "                        inner join\n"
				+ "                           TB_PESQUISA_MERCADO tpm \n"
				+ "                           on (tp.codigo_empresa = tpm.cd_empresa) \n"
				+ "                        inner join\n"
				+ "                           tb_mercado tm \n"
				+ "                           on (tpm.cd_mercado = tm.id) \n"
				+ "                     where\n"
				+ "                        nm_familia = :nmFamilia \n"
				+ "                        and nm_subfamilia = :nmSubFamilia \n"
				+ "                        and nome_cargo = :nmCargo \n"
				+ "                        and tm.dsMercado = :nmMercado \n"
				+ "                        and tp.grade >= :gradeMinimo \n"
				+ "                        and tp.grade <= :gradeMaximo \n"
				+ "                        and mediana_ilp is not null \n"
				+ "                     group by\n"
				+ "                        nome_empresa\n"
				+ "                  )\n"
				+ "                  max_grade \n"
				+ "                  on (max_grade.nome_empresa = query.nome_empresa)) total \n"
				+ "      				where total.grade = total.max_grade \n"
				+ "      group by desc_renum,nome_cargo )media \n"
				+ "   inner join\n"
				+ "      (\n"
				+ "         SELECT DISTINCT\n"
				+ "            [nome_cargo],\n"
				+ "            P90 = PERCENTILE_CONT(0.90) WITHIN GROUP ( ORDER BY mediana_ilp) OVER (PARTITION BY [nome_cargo]),\n"
				+ "            P75 = PERCENTILE_CONT(0.75) WITHIN GROUP (ORDER BY mediana_ilp) OVER (PARTITION BY [nome_cargo]),\n"
				+ "            P50 = PERCENTILE_CONT(0.50) WITHIN GROUP ( ORDER BY mediana_ilp) OVER (PARTITION BY [nome_cargo]),\n"
				+ "            P25 = PERCENTILE_CONT(0.25) WITHIN GROUP (ORDER BY mediana_ilp) OVER (PARTITION BY [nome_cargo]),\n"
				+ "            P10 = PERCENTILE_CONT(0.10) WITHIN GROUP ( ORDER BY mediana_ilp) OVER (PARTITION BY [nome_cargo]) \n"
				+ "         FROM\n"
				+ "            tb_pesquisa tp \n"
				+ "            inner join\n"
				+ "               TB_PESQUISA_MERCADO tpm \n"
				+ "               on (tp.codigo_empresa = tpm.cd_empresa) \n"
				+ "            inner join\n"
				+ "               tb_mercado tm \n"
				+ "               on (tpm.cd_mercado = tm.id) \n"
				+ "         where\n"
				+ "              nm_familia = :nmFamilia \n"
				+ "                        and nm_subfamilia = :nmSubFamilia \n"
				+ "                        and nome_cargo = :nmCargo \n"
				+ "                        and tm.dsMercado = :nmMercado \n"
				+ "                        and tp.grade >= :gradeMinimo \n"
				+ "                        and tp.grade <= :gradeMaximo \n"
				+ "            and mediana_ilp is not null \n"
				+ "      )\n"
				+ "      percentis \n"
				+ "      on(media.nome_cargo = percentis.nome_cargo)) consulta\n"
				+ "      LEFT OUTER JOIN (\n"
				+ "select round(avg(mediana_ilp ),0) mediana_ilp,nome_cargo from (\n"
				+ "select tp.nome_cargo, avg(mediana_ilp) mediana_ilp,nome_empresa  from tb_pesquisa tp \n"
				+ "inner join TB_PESQUISA_MERCADO tpm \n"
				+ "on (tp.codigo_empresa = tpm.cd_empresa) \n"
				+ "inner join tb_mercado tm \n"
				+ "on (tpm.cd_mercado = tm.id) \n"
				+ " where                  nm_familia = :nmFamilia \n"
				+ "                        and nm_subfamilia = :nmSubFamilia \n"
				+ "                        and nome_cargo = :nmCargo \n"
				+ "                        and tp.grade =:maiorGrade " 
				+ "					   and tp.nome_empresa = :nomeEmpresa\n"
				+ "and mediana_ilp is not null \n"
				+ "group by nome_cargo,nome_empresa) d\n"
				+ "group by nome_cargo) sua_empresa\n"
				+ "on(consulta.nome_cargo = sua_empresa.nome_cargo)"
				+ ""
				+ ""
				+ " UNION ALL "
				+ ""
				+ ""
				+ " select \n"
				+ "consulta.desc_renum,\n"
				+ "consulta.nome_cargo,\n"
				+ "consulta.media,\n"
				+ "consulta.p90,\n"
				+ "consulta.p75,\n"
				+ "consulta.p50,\n"
				+ "consulta.p25,\n"
				+ "consulta.p10,\n"
				+ "consulta.qtd_empresas,\n"
				+ "consulta.num_participantes,\n"
				+ "cast(round(sua_empresa.media_rda,0) as int) sua_empresa\n"
				+ "from (\n"
				+ " select\n"
				+ "   desc_renum,\n"
				+ "   media.nome_cargo,\n"
				+ "   cast(round(media_rda, 0) as int) media,\n"
				+ "   cast(round(p90, 0) as int) as p90,\n"
				+ "   cast(round(p75, 0) as int) as p75,\n"
				+ "   cast(round(p50, 0) as int) as p50,\n"
				+ "   cast(round(p25, 0) as int) as p25,\n"
				+ "   cast(round(p10, 0) as int) as p10,\n"
				+ "   qtd_empresas,\n"
				+ "   num_participantes \n"
				+ "from\n"
				+ "   (select desc_renum,nome_cargo,avg(media_rda) media_rda,count(1) qtd_empresas,sum(freq_fun) num_participantes from\n"
				+ "       (select query.nome_cargo, query.desc_renum, max_grade.max_grade,query.grade,query.freq_fun,query.nome_empresa,query.media_rda \n"
				+ "            from (\n"
				+ "                  select tp.nome_cargo, avg(media_rda) media_rda,nome_empresa,sum(freq_fun ) freq_fun,grade,\n"
				+ "                  'RDA - Remuneração Direta Alvo' as desc_renum \n"
				+ "                  from tb_pesquisa tp inner join\n"
				+ "                        TB_PESQUISA_MERCADO tpm \n"
				+ "                        on (tp.codigo_empresa = tpm.cd_empresa) \n"
				+ "                     inner join\n"
				+ "                        tb_mercado tm \n"
				+ "                        on (tpm.cd_mercado = tm.id) \n"
				+ "                  where\n"
				+ "                        nm_familia = :nmFamilia \n"
				+ "                        and nm_subfamilia = :nmSubFamilia \n"
				+ "                        and nome_cargo = :nmCargo \n"
				+ "                        and tm.dsMercado = :nmMercado \n"
				+ "                        and tp.grade >= :gradeMinimo \n"
				+ "                        and tp.grade <= :gradeMaximo \n"
				+ "                     and media_rda is not null \n"
				+ "                  group by\n"
				+ "                     nome_cargo,nome_empresa,grade\n"
				+ "               )\n"
				+ "               query \n"
				+ "               inner join( SELECT MAX(grade) max_grade, nome_empresa  FROM tb_pesquisa tp \n"
				+ "                        inner join\n"
				+ "                           TB_PESQUISA_MERCADO tpm \n"
				+ "                           on (tp.codigo_empresa = tpm.cd_empresa) \n"
				+ "                        inner join\n"
				+ "                           tb_mercado tm \n"
				+ "                           on (tpm.cd_mercado = tm.id) \n"
				+ "                     where\n"
				+ "                        nm_familia = :nmFamilia \n"
				+ "                        and nm_subfamilia = :nmSubFamilia \n"
				+ "                        and nome_cargo = :nmCargo \n"
				+ "                        and tm.dsMercado = :nmMercado \n"
				+ "                        and tp.grade >= :gradeMinimo \n"
				+ "                        and tp.grade <= :gradeMaximo \n"
				+ "                        and media_rda is not null \n"
				+ "                     group by\n"
				+ "                        nome_empresa\n"
				+ "                  )\n"
				+ "                  max_grade \n"
				+ "                  on (max_grade.nome_empresa = query.nome_empresa)) total \n"
				+ "      				where total.grade = total.max_grade \n"
				+ "      group by desc_renum,nome_cargo )media \n"
				+ "   inner join\n"
				+ "      (\n"
				+ "         SELECT DISTINCT\n"
				+ "            [nome_cargo],\n"
				+ "            P90 = PERCENTILE_CONT(0.90) WITHIN GROUP ( ORDER BY media_rda) OVER (PARTITION BY [nome_cargo]),\n"
				+ "            P75 = PERCENTILE_CONT(0.75) WITHIN GROUP (ORDER BY media_rda) OVER (PARTITION BY [nome_cargo]),\n"
				+ "            P50 = PERCENTILE_CONT(0.50) WITHIN GROUP ( ORDER BY media_rda) OVER (PARTITION BY [nome_cargo]),\n"
				+ "            P25 = PERCENTILE_CONT(0.25) WITHIN GROUP (ORDER BY media_rda) OVER (PARTITION BY [nome_cargo]),\n"
				+ "            P10 = PERCENTILE_CONT(0.10) WITHIN GROUP ( ORDER BY media_rda) OVER (PARTITION BY [nome_cargo]) \n"
				+ "         FROM\n"
				+ "            tb_pesquisa tp \n"
				+ "            inner join\n"
				+ "               TB_PESQUISA_MERCADO tpm \n"
				+ "               on (tp.codigo_empresa = tpm.cd_empresa) \n"
				+ "            inner join\n"
				+ "               tb_mercado tm \n"
				+ "               on (tpm.cd_mercado = tm.id) \n"
				+ "         where\n"
				+ "              nm_familia = :nmFamilia \n"
				+ "                        and nm_subfamilia = :nmSubFamilia \n"
				+ "                        and nome_cargo = :nmCargo \n"
				+ "                        and tm.dsMercado = :nmMercado \n"
				+ "                        and tp.grade >= :gradeMinimo \n"
				+ "                        and tp.grade <= :gradeMaximo \n"
				+ "            and media_rda is not null \n"
				+ "      )\n"
				+ "      percentis \n"
				+ "      on(media.nome_cargo = percentis.nome_cargo)) consulta\n"
				+ "      LEFT OUTER JOIN (\n"
				+ "select round(avg(media_rda ),0) media_rda,nome_cargo from (\n"
				+ "select tp.nome_cargo, avg(media_rda) media_rda,nome_empresa  from tb_pesquisa tp \n"
				+ "inner join TB_PESQUISA_MERCADO tpm \n"
				+ "on (tp.codigo_empresa = tpm.cd_empresa) \n"
				+ "inner join tb_mercado tm \n"
				+ "on (tpm.cd_mercado = tm.id) \n"
				+ " where                  nm_familia = :nmFamilia \n"
				+ "                        and nm_subfamilia = :nmSubFamilia \n"
				+ "                        and nome_cargo = :nmCargo \n"
				+ "                        and tp.grade =:maiorGrade " 
				+ "					   and tp.nome_empresa = :nomeEmpresa\n"
				+ "and media_rda is not null \n"
				+ "group by nome_cargo,nome_empresa) d\n"
				+ "group by nome_cargo) sua_empresa\n"
				+ "on(consulta.nome_cargo = sua_empresa.nome_cargo)"
				+ ""
				+ ""
				+ " UNION ALL "
				+ ""
				+ " select \n"
				+ "consulta.desc_renum,\n"
				+ "consulta.nome_cargo,\n"
				+ "consulta.media,\n"
				+ "consulta.p90,\n"
				+ "consulta.p75,\n"
				+ "consulta.p50,\n"
				+ "consulta.p25,\n"
				+ "consulta.p10,\n"
				+ "consulta.qtd_empresas,\n"
				+ "consulta.num_participantes,\n"
				+ "cast(round(sua_empresa.media_rd,0) as int) sua_empresa\n"
				+ "from (\n"
				+ " select\n"
				+ "   desc_renum,\n"
				+ "   media.nome_cargo,\n"
				+ "   cast(round(media_rd, 0) as int) media,\n"
				+ "   cast(round(p90, 0) as int) as p90,\n"
				+ "   cast(round(p75, 0) as int) as p75,\n"
				+ "   cast(round(p50, 0) as int) as p50,\n"
				+ "   cast(round(p25, 0) as int) as p25,\n"
				+ "   cast(round(p10, 0) as int) as p10,\n"
				+ "   qtd_empresas,\n"
				+ "   num_participantes \n"
				+ "from\n"
				+ "   (select desc_renum,nome_cargo,avg(media_rd) media_rd,count(1) qtd_empresas,sum(freq_fun) num_participantes from\n"
				+ "       (select query.nome_cargo, query.desc_renum, max_grade.max_grade,query.grade,query.freq_fun,query.nome_empresa,query.media_rd \n"
				+ "            from (\n"
				+ "                  select tp.nome_cargo, avg(media_rd) media_rd,nome_empresa,sum(freq_fun ) freq_fun,grade,\n"
				+ "                  'RD - Remuneração Direta' as desc_renum \n"
				+ "                  from tb_pesquisa tp inner join\n"
				+ "                        TB_PESQUISA_MERCADO tpm \n"
				+ "                        on (tp.codigo_empresa = tpm.cd_empresa) \n"
				+ "                     inner join\n"
				+ "                        tb_mercado tm \n"
				+ "                        on (tpm.cd_mercado = tm.id) \n"
				+ "                  where\n"
				+ "                        nm_familia = :nmFamilia \n"
				+ "                        and nm_subfamilia = :nmSubFamilia \n"
				+ "                        and nome_cargo = :nmCargo \n"
				+ "                        and tm.dsMercado = :nmMercado \n"
				+ "                        and tp.grade >= :gradeMinimo \n"
				+ "                        and tp.grade <= :gradeMaximo \n"
				+ "                     and media_rd is not null \n"
				+ "                  group by\n"
				+ "                     nome_cargo,nome_empresa,grade\n"
				+ "               )\n"
				+ "               query \n"
				+ "               inner join( SELECT MAX(grade) max_grade, nome_empresa  FROM tb_pesquisa tp \n"
				+ "                        inner join\n"
				+ "                           TB_PESQUISA_MERCADO tpm \n"
				+ "                           on (tp.codigo_empresa = tpm.cd_empresa) \n"
				+ "                        inner join\n"
				+ "                           tb_mercado tm \n"
				+ "                           on (tpm.cd_mercado = tm.id) \n"
				+ "                     where\n"
				+ "                        nm_familia = :nmFamilia \n"
				+ "                        and nm_subfamilia = :nmSubFamilia \n"
				+ "                        and nome_cargo = :nmCargo \n"
				+ "                        and tm.dsMercado = :nmMercado \n"
				+ "                        and tp.grade >= :gradeMinimo \n"
				+ "                        and tp.grade <= :gradeMaximo \n"
				+ "                        and media_rd is not null \n"
				+ "                     group by\n"
				+ "                        nome_empresa\n"
				+ "                  )\n"
				+ "                  max_grade \n"
				+ "                  on (max_grade.nome_empresa = query.nome_empresa)) total \n"
				+ "      				where total.grade = total.max_grade \n"
				+ "      group by desc_renum,nome_cargo )media \n"
				+ "   inner join\n"
				+ "      (\n"
				+ "         SELECT DISTINCT\n"
				+ "            [nome_cargo],\n"
				+ "            P90 = PERCENTILE_CONT(0.90) WITHIN GROUP ( ORDER BY media_rd) OVER (PARTITION BY [nome_cargo]),\n"
				+ "            P75 = PERCENTILE_CONT(0.75) WITHIN GROUP (ORDER BY media_rd) OVER (PARTITION BY [nome_cargo]),\n"
				+ "            P50 = PERCENTILE_CONT(0.50) WITHIN GROUP ( ORDER BY media_rd) OVER (PARTITION BY [nome_cargo]),\n"
				+ "            P25 = PERCENTILE_CONT(0.25) WITHIN GROUP (ORDER BY media_rd) OVER (PARTITION BY [nome_cargo]),\n"
				+ "            P10 = PERCENTILE_CONT(0.10) WITHIN GROUP ( ORDER BY media_rd) OVER (PARTITION BY [nome_cargo]) \n"
				+ "         FROM\n"
				+ "            tb_pesquisa tp \n"
				+ "            inner join\n"
				+ "               TB_PESQUISA_MERCADO tpm \n"
				+ "               on (tp.codigo_empresa = tpm.cd_empresa) \n"
				+ "            inner join\n"
				+ "               tb_mercado tm \n"
				+ "               on (tpm.cd_mercado = tm.id) \n"
				+ "         where\n"
				+ "              nm_familia = :nmFamilia \n"
				+ "                        and nm_subfamilia = :nmSubFamilia \n"
				+ "                        and nome_cargo = :nmCargo \n"
				+ "                        and tm.dsMercado = :nmMercado \n"
				+ "                        and tp.grade >= :gradeMinimo \n"
				+ "                        and tp.grade <= :gradeMaximo \n"
				+ "            and media_rd is not null \n"
				+ "      )\n"
				+ "      percentis \n"
				+ "      on(media.nome_cargo = percentis.nome_cargo)) consulta\n"
				+ "      LEFT OUTER JOIN (\n"
				+ "select round(avg(media_rd ),0) media_rd,nome_cargo from (\n"
				+ "select tp.nome_cargo, avg(media_rd) media_rd,nome_empresa  from tb_pesquisa tp \n"
				+ "inner join TB_PESQUISA_MERCADO tpm \n"
				+ "on (tp.codigo_empresa = tpm.cd_empresa) \n"
				+ "inner join tb_mercado tm \n"
				+ "on (tpm.cd_mercado = tm.id) \n"
				+ " where                  nm_familia = :nmFamilia \n"
				+ "                        and nm_subfamilia = :nmSubFamilia \n"
				+ "                        and nome_cargo = :nmCargo \n"
				+ "                        and tp.grade =:maiorGrade " 
				+ "					   and tp.nome_empresa = :nomeEmpresa\n"
				+ "and media_rd is not null \n"
				+ "group by nome_cargo,nome_empresa) d\n"
				+ "group by nome_cargo) sua_empresa\n"
				+ "on(consulta.nome_cargo = sua_empresa.nome_cargo)").

				//SBM
				setParameter("nmFamilia",nmFamilia).
				setParameter("nmSubFamilia",nmSubFamilia).
				setParameter("nmCargo",nmCargo).
				setParameter("nmMercado",mercGeral).
				setParameter("gradeMinimo",gradeMinimo).
				setParameter("gradeMaximo",gradeMaximo).

				setParameter("nmFamilia",nmFamilia).
				setParameter("nmSubFamilia",nmSubFamilia).
				setParameter("nmCargo",nmCargo).
				setParameter("nmMercado",mercGeral).
				setParameter("gradeMinimo",gradeMinimo).
				setParameter("gradeMaximo",gradeMaximo).

				setParameter("nmFamilia",nmFamilia).
				setParameter("nmSubFamilia",nmSubFamilia).
				setParameter("nmCargo",nmCargo).
				setParameter("nmMercado",mercGeral).
				setParameter("gradeMinimo",gradeMinimo).
				setParameter("gradeMaximo",gradeMaximo).

				setParameter("nmFamilia",nmFamilia).
				setParameter("nmSubFamilia",nmSubFamilia).
				setParameter("nmCargo",nmCargo).
				setParameter("maiorGrade",maiorGradeEmpresa).
				setParameter("nomeEmpresa",nomeEmpresa).

				//SBA
				setParameter("nmFamilia",nmFamilia).
				setParameter("nmSubFamilia",nmSubFamilia).
				setParameter("nmCargo",nmCargo).
				setParameter("nmMercado",mercGeral).
				setParameter("gradeMinimo",gradeMinimo).
				setParameter("gradeMaximo",gradeMaximo).

				setParameter("nmFamilia",nmFamilia).
				setParameter("nmSubFamilia",nmSubFamilia).
				setParameter("nmCargo",nmCargo).
				setParameter("nmMercado",mercGeral).
				setParameter("gradeMinimo",gradeMinimo).
				setParameter("gradeMaximo",gradeMaximo).

				setParameter("nmFamilia",nmFamilia).
				setParameter("nmSubFamilia",nmSubFamilia).
				setParameter("nmCargo",nmCargo).
				setParameter("nmMercado",mercGeral).
				setParameter("gradeMinimo",gradeMinimo).
				setParameter("gradeMaximo",gradeMaximo).



				setParameter("nmFamilia",nmFamilia).
				setParameter("nmSubFamilia",nmSubFamilia).
				setParameter("nmCargo",nmCargo).
				setParameter("maiorGrade",maiorGradeEmpresa).
				setParameter("nomeEmpresa",nomeEmpresa).

				//ICPA
				setParameter("nmFamilia",nmFamilia).
				setParameter("nmSubFamilia",nmSubFamilia).
				setParameter("nmCargo",nmCargo).
				setParameter("nmMercado",mercGeral).
				setParameter("gradeMinimo",gradeMinimo).
				setParameter("gradeMaximo",gradeMaximo).

				setParameter("nmFamilia",nmFamilia).
				setParameter("nmSubFamilia",nmSubFamilia).
				setParameter("nmCargo",nmCargo).
				setParameter("nmMercado",mercGeral).
				setParameter("gradeMinimo",gradeMinimo).
				setParameter("gradeMaximo",gradeMaximo).

				setParameter("nmFamilia",nmFamilia).
				setParameter("nmSubFamilia",nmSubFamilia).
				setParameter("nmCargo",nmCargo).
				setParameter("nmMercado",mercGeral).
				setParameter("gradeMinimo",gradeMinimo).
				setParameter("gradeMaximo",gradeMaximo).


				setParameter("nmFamilia",nmFamilia).
				setParameter("nmSubFamilia",nmSubFamilia).
				setParameter("nmCargo",nmCargo).
				setParameter("maiorGrade",maiorGradeEmpresa).
				setParameter("nomeEmpresa",nomeEmpresa).

				//TDA
				setParameter("nmFamilia",nmFamilia).
				setParameter("nmSubFamilia",nmSubFamilia).
				setParameter("nmCargo",nmCargo).
				setParameter("nmMercado",mercGeral).
				setParameter("gradeMinimo",gradeMinimo).
				setParameter("gradeMaximo",gradeMaximo).

				setParameter("nmFamilia",nmFamilia).
				setParameter("nmSubFamilia",nmSubFamilia).
				setParameter("nmCargo",nmCargo).
				setParameter("nmMercado",mercGeral).
				setParameter("gradeMinimo",gradeMinimo).
				setParameter("gradeMaximo",gradeMaximo).

				setParameter("nmFamilia",nmFamilia).
				setParameter("nmSubFamilia",nmSubFamilia).
				setParameter("nmCargo",nmCargo).
				setParameter("nmMercado",mercGeral).
				setParameter("gradeMinimo",gradeMinimo).
				setParameter("gradeMaximo",gradeMaximo).



				setParameter("nmFamilia",nmFamilia).
				setParameter("nmSubFamilia",nmSubFamilia).
				setParameter("nmCargo",nmCargo).
				setParameter("maiorGrade",maiorGradeEmpresa).
				setParameter("nomeEmpresa",nomeEmpresa).

				//TD
				setParameter("nmFamilia",nmFamilia).
				setParameter("nmSubFamilia",nmSubFamilia).
				setParameter("nmCargo",nmCargo).
				setParameter("nmMercado",mercGeral).
				setParameter("gradeMinimo",gradeMinimo).
				setParameter("gradeMaximo",gradeMaximo).

				setParameter("nmFamilia",nmFamilia).
				setParameter("nmSubFamilia",nmSubFamilia).
				setParameter("nmCargo",nmCargo).
				setParameter("nmMercado",mercGeral).
				setParameter("gradeMinimo",gradeMinimo).
				setParameter("gradeMaximo",gradeMaximo).

				setParameter("nmFamilia",nmFamilia).
				setParameter("nmSubFamilia",nmSubFamilia).
				setParameter("nmCargo",nmCargo).
				setParameter("nmMercado",mercGeral).
				setParameter("gradeMinimo",gradeMinimo).
				setParameter("gradeMaximo",gradeMaximo).



				setParameter("nmFamilia",nmFamilia).
				setParameter("nmSubFamilia",nmSubFamilia).
				setParameter("nmCargo",nmCargo).
				setParameter("maiorGrade",maiorGradeEmpresa).
				setParameter("nomeEmpresa",nomeEmpresa).

				//ILP
				setParameter("nmFamilia",nmFamilia).
				setParameter("nmSubFamilia",nmSubFamilia).
				setParameter("nmCargo",nmCargo).
				setParameter("nmMercado",mercGeral).
				setParameter("gradeMinimo",gradeMinimo).
				setParameter("gradeMaximo",gradeMaximo).

				setParameter("nmFamilia",nmFamilia).
				setParameter("nmSubFamilia",nmSubFamilia).
				setParameter("nmCargo",nmCargo).
				setParameter("nmMercado",mercGeral).
				setParameter("gradeMinimo",gradeMinimo).
				setParameter("gradeMaximo",gradeMaximo).

				setParameter("nmFamilia",nmFamilia).
				setParameter("nmSubFamilia",nmSubFamilia).
				setParameter("nmCargo",nmCargo).
				setParameter("nmMercado",mercGeral).
				setParameter("gradeMinimo",gradeMinimo).
				setParameter("gradeMaximo",gradeMaximo).



				setParameter("nmFamilia",nmFamilia).
				setParameter("nmSubFamilia",nmSubFamilia).
				setParameter("nmCargo",nmCargo).
				setParameter("maiorGrade",maiorGradeEmpresa).
				setParameter("nomeEmpresa",nomeEmpresa).


				//RDA
				setParameter("nmFamilia",nmFamilia).
				setParameter("nmSubFamilia",nmSubFamilia).
				setParameter("nmCargo",nmCargo).
				setParameter("nmMercado",mercGeral).
				setParameter("gradeMinimo",gradeMinimo).
				setParameter("gradeMaximo",gradeMaximo).

				setParameter("nmFamilia",nmFamilia).
				setParameter("nmSubFamilia",nmSubFamilia).
				setParameter("nmCargo",nmCargo).
				setParameter("nmMercado",mercGeral).
				setParameter("gradeMinimo",gradeMinimo).
				setParameter("gradeMaximo",gradeMaximo).

				setParameter("nmFamilia",nmFamilia).
				setParameter("nmSubFamilia",nmSubFamilia).
				setParameter("nmCargo",nmCargo).
				setParameter("nmMercado",mercGeral).
				setParameter("gradeMinimo",gradeMinimo).
				setParameter("gradeMaximo",gradeMaximo).


				setParameter("nmFamilia",nmFamilia).
				setParameter("nmSubFamilia",nmSubFamilia).
				setParameter("nmCargo",nmCargo).
				setParameter("maiorGrade",maiorGradeEmpresa).
				setParameter("nomeEmpresa",nomeEmpresa).
				//RD
				setParameter("nmFamilia",nmFamilia).
				setParameter("nmSubFamilia",nmSubFamilia).
				setParameter("nmCargo",nmCargo).
				setParameter("nmMercado",mercGeral).
				setParameter("gradeMinimo",gradeMinimo).
				setParameter("gradeMaximo",gradeMaximo).

				setParameter("nmFamilia",nmFamilia).
				setParameter("nmSubFamilia",nmSubFamilia).
				setParameter("nmCargo",nmCargo).
				setParameter("nmMercado",mercGeral).
				setParameter("gradeMinimo",gradeMinimo).
				setParameter("gradeMaximo",gradeMaximo).

				setParameter("nmFamilia",nmFamilia).
				setParameter("nmSubFamilia",nmSubFamilia).
				setParameter("nmCargo",nmCargo).
				setParameter("nmMercado",mercGeral).
				setParameter("gradeMinimo",gradeMinimo).
				setParameter("gradeMaximo",gradeMaximo).



				setParameter("nmFamilia",nmFamilia).
				setParameter("nmSubFamilia",nmSubFamilia).
				setParameter("nmCargo",nmCargo).
				setParameter("maiorGrade",maiorGradeEmpresa).
				setParameter("nomeEmpresa",nomeEmpresa).

				getResultList();

		for(Object record: lista) {
			Object[] linha = (Object[]) record; 
			MediasVO medias = new MediasVO();
			medias.setDescRenum((String) linha[0]);
			medias.setNomeCargo((String) linha[1]);
			medias.setMedia((int) linha[2]);
			medias.setP90((int) linha[3]);
			medias.setP75((int) linha[4]);
			medias.setP50((int) linha[5]);
			medias.setP25((int) linha[6]);
			medias.setP10((int) linha[7]);
			medias.setQtd_empresas((int) linha[8]);
			medias.setNum_participantes((int) linha[9]);

			medias.setSua_empresa( linha[10]== null ? (int) 0 :  (int) linha[10]);
			mediasFinal.add(medias);
		}

		for(int i=0; i<mediasFinal.size();i++) {

			if(mediasFinal.get(i).getDescRenum().equals("SBA - Salário Base Anual")) {
				if(mediasFinal.get(i).getNum_participantes()<3) {

					return null;
				}
			}

			if(mediasFinal.get(i).getQtd_empresas()<3) {
				mediasFinal.get(i).setP10(0);
				mediasFinal.get(i).setP25(0);
				mediasFinal.get(i).setP50(0);
				mediasFinal.get(i).setP75(0);
				mediasFinal.get(i).setP90(0);
				mediasFinal.get(i).setMedia(0);
				mediasFinal.get(i).setNum_participantes(mediasFinal.get(i).getQtd_empresas());

			}
			//Informações do cargo em 3 empresas: o sistema calcula somente a média
			else if (mediasFinal.get(i).getQtd_empresas()>=3 && 
					mediasFinal.get(i).getQtd_empresas()<4) {
				mediasFinal.get(i).setP10(0);
				mediasFinal.get(i).setP25(0);
				mediasFinal.get(i).setP50(0);
				mediasFinal.get(i).setP75(0);
				mediasFinal.get(i).setP90(0);

			}
			//Informações do cargo em 4 empresas: o sistema calcula a média e o P50
			else if (mediasFinal.get(i).getQtd_empresas()>=4 && 
					mediasFinal.get(i).getQtd_empresas()<6) {
				mediasFinal.get(i).setP10(0);
				mediasFinal.get(i).setP25(0);
				mediasFinal.get(i).setP75(0);
				mediasFinal.get(i).setP90(0);

			}
			//Informações do cargo em 6 empresas: o sistema calcula a média, o P50 e P25 e P75.
			else if (
					mediasFinal.get(i).getQtd_empresas()>=6
					&& 
					mediasFinal.get(i).getQtd_empresas()<8) {
				mediasFinal.get(i).setP10(0);
				mediasFinal.get(i).setP90(0);	
			}
			//Informações do cargo em 8 empresas: o sistema calcula a média, o P50 e P25 e P75, P90 e P10
			else if (mediasFinal.get(i).getQtd_empresas()>=8) {
			}

		}

		return mediasFinal;

	}





	@Override
	@Transactional
	public TbPesquisa save(TbPesquisa tbAREA) {
		String username = SecurityWrapper.getUsername();

		tbAREA.updateAuditInformation(username);
		return super.save(tbAREA);
	}

	@Override
	@Transactional
	public TbPesquisa update(TbPesquisa tbAREA) {
		String username = SecurityWrapper.getUsername();
		tbAREA.updateAuditInformation(username);
		return super.update(tbAREA);
	}

	@Override
	protected void handleDependenciesBeforeDelete(TbPesquisa tbAREA) {

		/* This is called before a TbAREA is deleted. Place here all the
           steps to cut dependencies to other entities */
		//this.cutAllIdAREATbCARGOAssignments(tbAREA);

		//this.cutAllIdAREATbDEPTOsAssignments(tbAREA);

	}



	/*// Remove all assignments from all tbDEPTO a tbAREA. Called before delete a tbAREA.
	@Transactional
	private void cutAllIdAREATbDEPTOsAssignments(TbAREAEntity tbAREA) {
		getEntityManager()
		.createQuery("DELETE FROM TbDEPTO c WHERE c.idAREA = :p")
		.setParameter("p", tbAREA).executeUpdate();
	}

	@Transactional
	private void cutAllIdAREATbCARGOAssignments(TbAREAEntity tbAREA) {
		List<TbDEPTOEntity> result = new ArrayList<TbDEPTOEntity>();
		result = tbDEPTOService.findTbDEPTOsByIdAREA(tbAREA);
		for(int i=0; i<result.size();i++) {
			getEntityManager()
			.createQuery("DELETE FROM TbCARGOS c WHERE c.idDEPTO = :p")
			.setParameter("p", result.get(i)).executeUpdate();
		}

	}*/







}
