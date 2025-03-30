package org.applicationn.pesquisa.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.QueryHint;
import javax.transaction.TransactionScoped;
import javax.transaction.Transactional;

import org.applicationn.pesquisa.domain.TbPesquisa;
import org.applicationn.pesquisa.vo.EmpresaVisiveisVO;
import org.applicationn.pesquisa.vo.EmpresasDetalheVO;
import org.applicationn.pesquisa.vo.GradeVO;
import org.applicationn.pesquisa.vo.MediasDetalheVO;
import org.applicationn.pesquisa.vo.MediasNovaEmpresaVO;
import org.applicationn.pesquisa.vo.MediasVO;
import org.applicationn.pesquisa.vo.RelatorioUsuariosVO;
import org.applicationn.simtrilhas.domain.security.UserEntity;
import org.applicationn.simtrilhas.service.BaseService;
import org.applicationn.simtrilhas.service.security.SecurityWrapper;
import org.applicationn.simtrilhas.service.security.UserService;
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
	
	private Query queryFindMediaEmpresa;
	
	


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
	public List<EmpresasDetalheVO> findDistinctTbPesquisaNMEmpresa(String nomeEmpresa) {
		
		List<EmpresasDetalheVO> mediasFinal = new ArrayList<EmpresasDetalheVO>();
		List<Object> lista = getEntityManagerMatriz().createNativeQuery("SELECT DISTINCT nm_familia,nm_subfamilia,nome_cargo   FROM TB_PESQUISA o\n"
				+ "				 WHERE o.nome_empresa =:nomeEmpresa\n"
				+ "				order by o.nome_cargo ").
				setParameter("nomeEmpresa",nomeEmpresa).
				getResultList();
		for(Object record: lista) {
			Object[] linha = (Object[]) record; 
			EmpresasDetalheVO medias = new EmpresasDetalheVO();
			medias.setNmFamilia((String) linha[0]);
			medias.setNmSubFam((String) linha[1]);
			medias.setNomeCargoEmpresa((String) linha[2]);
			mediasFinal.add(medias);
		}
		return mediasFinal;
	}
	
	@SuppressWarnings("unchecked")
    @Transactional
    public List<MediasNovaEmpresaVO> findExtracaoEmpresaMercado(String nomeEmpresa, String nomeCargo, String mercado ) {
        List<MediasNovaEmpresaVO> extracoes = new ArrayList<>();
        
        StringBuilder query = new StringBuilder(
                " SELECT * \n"
                + "FROM (\n"
                + "    SELECT \n"
                + "        grade,\n"
                + "        mercado,\n"
                + "        nm_familia,\n"
                + "        nm_subfamilia,\n"
                + "        nome_cargo_xr,\n"
                + "        desc_renum_new,\n"
                + "        avg_p10,\n"
                + "        avg_p25,\n"
                + "        avg_p50,\n"
                + "        avg_p75,\n"
                + "        avg_p90,\n"
                + "        avg_media,\n"
                + "        total_qtd_empresas, codigo_cargo\n"
                + "    FROM tb_pesquisa_extracao_mercado\n"
                + ") AS combined_results\n"
                + "WHERE 1=1 "
        );
        
        // Verifica se o parâmetro nomeCargo foi passado
        if (nomeCargo != null && !nomeCargo.isEmpty()) {
            query.append(" AND nome_cargo_xr = :nomeCargo");
        }
        
        query.append(" AND mercado = :mercado");
        
        query.append(
                " ORDER BY \n"
                + "    nm_familia, \n"
                + "    nm_subfamilia,\n"
                + "    nome_cargo_xr,\n"
                + "    grade,\n"
                + "    CASE \n"
                + "        WHEN desc_renum_new = 'SBM - Salário Base Mensal' THEN 1\n"
                + "        WHEN desc_renum_new = 'SBA - Salário Base Anual' THEN 2\n"
                + "        WHEN desc_renum_new = 'ICPA - Incentivo de Curto Prazo Alvo (Bônus + PLR)' THEN 3\n"
                + "        WHEN desc_renum_new = 'TDA - Total em Dinheiro Alvo' THEN 4\n"
                + "        WHEN desc_renum_new = 'ICP - Incentivo de Curto Prazo Pago (Bônus + PLR)' THEN 5\n"
                + "        WHEN desc_renum_new = 'TD - Total em Dinheiro' THEN 6\n"
                + "        WHEN desc_renum_new = 'ILP - Incentivos de Longo Prazo' THEN 7\n"
                + "        WHEN desc_renum_new = 'RDA - Remuneração Direta Alvo' THEN 8\n"
                + "        WHEN desc_renum_new = 'RD - Remuneração Direta' THEN 9\n"
                + "        ELSE 10 \n"
                + "    END"
        );

        // Cria a query e define os parâmetros
        Query nativeQuery = getEntityManagerMatriz().createNativeQuery(query.toString());
        System.out.println(query.toString());
        if (nomeCargo != null && !nomeCargo.isEmpty()) {
            nativeQuery.setParameter("nomeCargo", nomeCargo);
        }
        
        nativeQuery.setParameter("mercado", mercado);
        
        List<Object> resultList = nativeQuery.getResultList();

        for (Object record : resultList) {
            Object[] linha = (Object[]) record;
            MediasNovaEmpresaVO extracao = new MediasNovaEmpresaVO();
           
            extracao.setGrade((int) linha[0]);
            extracao.setMercado((String) linha[1]);
            extracao.setNmFamilia((String)linha[2]);
            extracao.setNmSubFamilia((String) linha[3]);
            extracao.setNomeCargoXr((String) linha[4]);
            extracao.setDescRenum((String) linha[5]);
            extracao.setP10((int) linha[6]);
            extracao.setP25((int) linha[7]);
            extracao.setP50((int) linha[8]);
            extracao.setP75((int) linha[9]);
            extracao.setP90((int) linha[10]);
            extracao.setMedia((int) linha[11]);
            extracao.setQtdEmpresas((int) linha[12]);
            extracao.setCodigoCargo((String) linha[13]);
            extracoes.add(extracao);
        }
        for(int i=0; i<extracoes.size();i++) {

			if(extracoes.get(i).getDescRenum().equals("SBA - Salário Base Anual")) {
				if(extracoes.get(i).getNumParticipantes()<3) {

				}
			}

			if(extracoes.get(i).getQtdEmpresas()<3) {
				extracoes.get(i).setP10(0);
				extracoes.get(i).setP25(0);
				extracoes.get(i).setP50(0);
				extracoes.get(i).setP75(0);
				extracoes.get(i).setP90(0);
				extracoes.get(i).setMedia(0);
				extracoes.get(i).setQtdEmpresas(extracoes.get(i).getQtdEmpresas());

			}
			//Informações do cargo em 3 empresas: o sistema calcula somente a média
			else if (extracoes.get(i).getQtdEmpresas()>=3 && 
					extracoes.get(i).getQtdEmpresas()<4) {
				extracoes.get(i).setP10(0);
				extracoes.get(i).setP25(0);
				extracoes.get(i).setP50(0);
				extracoes.get(i).setP75(0);
				extracoes.get(i).setP90(0);

			}
			//Informações do cargo entre 4 empresa e 5 empresas: o sistema calcula a média e o P50
			else if (extracoes.get(i).getQtdEmpresas()>=4 && 
					extracoes.get(i).getQtdEmpresas()<6) {
				extracoes.get(i).setP10(0);
				extracoes.get(i).setP25(0);
				extracoes.get(i).setP75(0);
				extracoes.get(i).setP90(0);

			}
			//Informações do cargo entre 6 empresas e 7 empresas: o sistema calcula a média, o P50 e P25 e P75.
			else if (
					extracoes.get(i).getQtdEmpresas()>=6
					&& 
					extracoes.get(i).getQtdEmpresas()<10) {
				extracoes.get(i).setP10(0);
				extracoes.get(i).setP90(0);	
			}
			//Informações do cargo em 8 ou mais empresas: o sistema calcula a média, o P50 e P25 e P75, P90 e P10
			//else if (mediasFinal.get(i).getQtd_empresas()>=10) {
			//}

		}

        return extracoes;
    }
	
	
    @Transactional
    public int findGradeInicioMenu(String nomeEmpresa,String nomeCargo) {
		int grade =  (int) getEntityManagerMatriz().createNativeQuery("select COALESCE (MIN(grade),0) GRADE   from tb_pesquisa "
				+ " where nome_empresa=:nomeEmpresa\n"
				+ " and nome_cargo=:nomeCargo")
				.setParameter("nomeEmpresa", nomeEmpresa)
				.setParameter("nomeCargo", nomeCargo)
				.getSingleResult();
		return grade;
		
	}
	
    
    
	@Inject
	protected UserService userService;
	@Inject
	private TbEmpresaService tbEmpresaService;
	@SuppressWarnings("unchecked")
    @Transactional
    public List<MediasNovaEmpresaVO> findExtracaoEmpresaPorNome(String nomeEmpresa, String nomeFamilia, String nomeSubfamilia,
    		String nomeCargo, String mercado) {
        List<MediasNovaEmpresaVO> extracoes = new ArrayList<>();
        
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT tpd.id,\n")
            .append("    tee.nome_cargo_xr,\n")
            .append("    tee.nome_cargo,\n")
            .append("    CASE\n")
            .append("        WHEN desc_renum = '1 - SBM - Salário Base Mensal' THEN 'SBM - Salário Base Mensal'\n")
            .append("        WHEN desc_renum = '2 - SBM - Salário Base Anual' THEN 'SBA - Salário Base Anual'\n")
            .append("        WHEN desc_renum = '3 ICPA - Incentivo de Curto Prazo Alvo (Bônus + PLR)' THEN 'ICPA - Incentivo de Curto Prazo Alvo (Bônus + PLR)'\n")
            .append("        WHEN desc_renum = '4 TDA - Total em Dinheiro Alvo' THEN 'TDA - Total em Dinheiro Alvo'\n")
            .append("        WHEN desc_renum = '5 - ICP - Incentivo de Curto Prazo Pago (Bônus + PLR)' THEN 'ICP - Incentivo de Curto Prazo Pago (Bônus + PLR)'\n")
            .append("        WHEN desc_renum = '6 - TD - Total em Dinheiro' THEN 'TD - Total em Dinheiro'\n")
            .append("        WHEN desc_renum = '7 - ILP - Incentivos de Longo Prazo' THEN 'ILP - Incentivos de Longo Prazo'\n")
            .append("        WHEN desc_renum = '8 - RDA - Remuneração Direta Alvo' THEN 'RDA - Remuneração Direta Alvo'\n")
            .append("        WHEN desc_renum = '9 - RD - Remuneração Direta' THEN 'RD - Remuneração Direta'\n")
            .append("        ELSE desc_renum\n")
            .append("    END AS desc_renum_new,\n")
            .append("    (CASE  \n"
            		+ "					    WHEN desc_renum = '1 - SBM - Salário Base Mensal' THEN cast(tpd.valor_sb /13.33 as int)\n"
            		+ "					    WHEN desc_renum = '2 - SBM - Salário Base Anual' THEN cast(tpd.valor_sb as int)\n"
            		+ "					    WHEN desc_renum = '3 ICPA - Incentivo de Curto Prazo Alvo (Bônus + PLR)'  THEN cast(tpd.valor_cp_aliv  as int)\n"
            		+ "					    WHEN desc_renum = '4 TDA - Total em Dinheiro Alvo' THEN cast(tpd.valor_tda  as int)\n"
            		+ "					    WHEN desc_renum = '5 - ICP - Incentivo de Curto Prazo Pago (Bônus + PLR)' THEN cast(tpd.valor_cp_pg  as int)\n"
            		+ "					    WHEN desc_renum = '6 - TD - Total em Dinheiro' THEN cast(tpd.valor_td  as int)\n"
            		+ "					    WHEN desc_renum = '7 - ILP - Incentivos de Longo Prazo' THEN cast(tpd.valor_ilp  as int)\n"
            		+ "					    WHEN desc_renum = '8 - RDA - Remuneração Direta Alvo' THEN cast(tpd.valor_rda  as int)\n"
            		+ "					    WHEN desc_renum = '9 - RD - Remuneração Direta'THEN cast(tpd.valor_rd  as int)\n"
            		+ "					    ELSE NULL \n"
            		+ "					END) AS sua_empresa ,\n")
            .append("    p10,\n")
            .append("    P25,\n")
            .append("    P50,\n")
            .append("    P75,\n")
            .append("    P90,\n")
            .append("    media,\n")
            .append("    qtd_empresas,\n")
            .append("    num_participantes,\n")
            .append("    tee.nome_empresa,\n")
            .append("    tee.grade,\n")
            .append("    tp.nm_familia,\n")
            .append("    tp.nm_subfamilia,\n")
            .append("    tpd.matricula,\n")
            .append("    tpd.grade AS grade_empresa,\n")
            .append("    tpd.codigo_cargo, \n")
            .append("    tm.dsMercado AS mercado, desc_renum desc_renum_remuneracao, tp.nome_empresa nome_empresa_ordenacao \n")
            .append("FROM \n")
            .append("    TB_EXTRACAO_EMPRESA tee\n")
            .append("INNER JOIN \n")
            .append("    tb_pesquisa tp \n")
            .append("    ON tp.codigo_cargo = tee.codigo_cargo\n")
            .append("    AND tp.grade = tee.grade\n")
            .append("    AND tp.nome_empresa = tee.nome_empresa\n")
            .append("     INNER JOIN TB_PESQUISA_MERCADO tpm ON (tp.codigo_empresa = tpm.cd_empresa) "
            		+ "   INNER JOIN tb_mercado tm ON (tpm.cd_mercado = tm.id) "
            		+ "    INNER JOIN TB_EMPRESA_VISIBILIDADE ev \n"
            		+ "    ON ev.nome_empresa = tp.nome_empresa \n"
            		+ "    AND ev.visivel = 1 ")
            .append("INNER JOIN \n")
            .append("    TB_PESQUISA_DETALHE tpd \n")
            .append("    ON tee.codigo_cargo = tpd.codigo_cargo \n")
            .append("    AND tee.nome_empresa = tpd.nome_empresa \n")
            .append("    AND tp.grade = tpd.grade_xr   \n")
            .append("    AND tp.nome_cargo = tpd.nome_cargo_XR\n")
            .append("    AND TEE.nome_cargo = TPD.nome_cargo_empresa \n")
            .append("WHERE \n")
            .append("    tp.nome_empresa = :nomeEmpresa \n");

        if (nomeFamilia != null && !nomeFamilia.isEmpty()) {
            queryBuilder.append("AND tp.nm_familia = :nomeFamilia\n");
        }
        if (nomeSubfamilia != null && !nomeSubfamilia.isEmpty()) {
            queryBuilder.append("AND tp.nm_subfamilia = :nomeSubfamilia\n");
        }
        
        if (nomeCargo != null && !nomeCargo.isEmpty()) {
            queryBuilder.append("AND tp.nome_cargo = :nomeCargo\n");
        }
        if (mercado != null && !mercado.isEmpty()) {
            queryBuilder.append("AND (tm.dsMercado = :mercado and  tp.nome_empresa =:nomeEmpresa)\n");
        }
        

        queryBuilder.append("ORDER BY TPD.ID,\n")
        .append("    grade, \n")
        .append("    matricula, \n")
        .append("    tp.nome_empresa, \n")
        .append("    nome_cargo, \n")
        .append("    desc_renum, mercado");
        
      

        Query query = getEntityManagerMatriz().createNativeQuery(queryBuilder.toString())
                .setParameter("nomeEmpresa", nomeEmpresa);

        if (nomeFamilia != null && !nomeFamilia.isEmpty()) {
            query.setParameter("nomeFamilia", nomeFamilia);
        }
        if (nomeSubfamilia != null && !nomeSubfamilia.isEmpty()) {
            query.setParameter("nomeSubfamilia", nomeSubfamilia);
        }
        if (nomeCargo != null && !nomeCargo.isEmpty()) {
        	 query.setParameter("nomeCargo", nomeCargo);
        }
        if (mercado != null && !mercado.isEmpty()) {
        	 query.setParameter("mercado", mercado);
        }
        query.setHint("org.hibernate.fetchSize", 100000);
        List<Object> resultList = query.getResultList();

        
        for (Object record : resultList) {
            Object[] linha = (Object[]) record;
            MediasNovaEmpresaVO extracao = new MediasNovaEmpresaVO();
            extracao.setId((BigInteger) linha[0]);
            extracao.setNomeCargoXr((String) linha[1]);
            extracao.setNomeCargo((String) linha[2]);
            extracao.setDescRenum((String) linha[3]);

            if (linha[4] == null) {
                extracao.setSuaEmpresa(0);
            } else {
                extracao.setSuaEmpresa((int) linha[4]);
            }

            extracao.setP10((int) linha[5]);
            extracao.setP25((int) linha[6]);
            extracao.setP50((int) linha[7]);
            extracao.setP75((int) linha[8]);
            extracao.setP90((int) linha[9]);
            extracao.setMedia((int) linha[10]);
            extracao.setQtdEmpresas((int) linha[11]);
            extracao.setNumParticipantes((int) linha[12]);
            extracao.setNomeEmpresa((String) linha[13]);
            extracao.setGrade((int) linha[14]);
            extracao.setNmFamilia((String) linha[15]);
            extracao.setNmSubFamilia((String) linha[16]);
            extracao.setMatricula((String) linha[17]);
            extracao.setGradeEmpresa((String) linha[18]);
            extracao.setCodigoCargo((String) linha[19]);
            extracao.setMercado((String) linha[20]);
            extracao.setDesc_renum_ordenacao((String) linha[21]);
            extracao.setNome_empresa_ordenacao((String) linha[22]);
            extracoes.add(extracao);
        }
        
        
       
        
        for(int i=0; i<extracoes.size();i++) {

			if(extracoes.get(i).getDescRenum().equals("SBA - Salário Base Anual")) {
				if(extracoes.get(i).getNumParticipantes()<3) {

				}
			}

			if(extracoes.get(i).getQtdEmpresas()<3) {
				extracoes.get(i).setP10(0);
				extracoes.get(i).setP25(0);
				extracoes.get(i).setP50(0);
				extracoes.get(i).setP75(0);
				extracoes.get(i).setP90(0);
				extracoes.get(i).setMedia(0);
				extracoes.get(i).setQtdEmpresas(extracoes.get(i).getQtdEmpresas());

			}
			//Informações do cargo em 3 empresas: o sistema calcula somente a média
			else if (extracoes.get(i).getQtdEmpresas()>=3 && 
					extracoes.get(i).getQtdEmpresas()<4) {
				extracoes.get(i).setP10(0);
				extracoes.get(i).setP25(0);
				extracoes.get(i).setP50(0);
				extracoes.get(i).setP75(0);
				extracoes.get(i).setP90(0);

			}
			//Informações do cargo entre 4 empresa e 5 empresas: o sistema calcula a média e o P50
			else if (extracoes.get(i).getQtdEmpresas()>=4 && 
					extracoes.get(i).getQtdEmpresas()<6) {
				extracoes.get(i).setP10(0);
				extracoes.get(i).setP25(0);
				extracoes.get(i).setP75(0);
				extracoes.get(i).setP90(0);

			}
			//Informações do cargo entre 6 empresas e 7 empresas: o sistema calcula a média, o P50 e P25 e P75.
			else if (
					extracoes.get(i).getQtdEmpresas()>=6
					&& 
					extracoes.get(i).getQtdEmpresas()<10) {
				extracoes.get(i).setP10(0);
				extracoes.get(i).setP90(0);	
			}
			//Informações do cargo em 8 ou mais empresas: o sistema calcula a média, o P50 e P25 e P75, P90 e P10
			//else if (mediasFinal.get(i).getQtd_empresas()>=10) {
			//}

		}

        return extracoes;
    }
	
	@SuppressWarnings("unchecked")
    @Transactional
    public List<MediasNovaEmpresaVO> findExtracaoEmpresaPorNomeMercadoSelecionado(String nomeEmpresa, String nomeFamilia, String nomeSubfamilia,
    		String nomeCargo, String mercado) {
        List<MediasNovaEmpresaVO> extracoes = new ArrayList<>();
        String username = SecurityWrapper.getUsername();
        UserEntity user = userService.findUserByUsername(username);
        EmpresaVisiveisVO empresaVis = tbEmpresaService.empresasVisiveisPorUsuario(user.getUsername());
        long visivel = empresaVis.getVisivel();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("  WITH EmpresaSelecionada2 AS (\n"
        		+ "    SELECT DISTINCT \n"
        		+ "        desc_renum,\n"
        		+ "        p90,\n"
        		+ "        p75,\n"
        		+ "        p50,\n"
        		+ "        p25,\n"
        		+ "        p10,\n"
        		+ "        media,\n"
        		+ "        qtd_empresas,\n"
        		+ "        num_participantes,\n"
        		+ "        grade, \n"
        		+ "        nome_cargo_xr,\n"
        		+ "        codigo_cargo,\n"
        		+ "        mercado,\n"
        		+ "       :nomeEmpresa AS nome_empresa\n"
        		+ "    FROM SimTrilhas.dbo.TB_EXTRACAO_EMPRESA\n"
        		+ "    WHERE mercado = :mercado\n"
        		+ ")\n"
        		+ "SELECT \n"
        		+ "    tpd.id,\n"
        		+ "    tee.nome_cargo_xr,\n"
        		+ "    tpd.nome_cargo_empresa AS nome_cargo,\n"
        		+ "    CASE tee.desc_renum\n"
        		+ "        WHEN '1 - SBM - Salário Base Mensal' THEN 'SBM - Salário Base Mensal'\n"
        		+ "        WHEN '2 - SBM - Salário Base Anual' THEN 'SBA - Salário Base Anual'\n"
        		+ "        WHEN '3 ICPA - Incentivo de Curto Prazo Alvo (Bônus + PLR)' THEN 'ICPA - Incentivo de Curto Prazo Alvo (Bônus + PLR)'\n"
        		+ "        WHEN '4 TDA - Total em Dinheiro Alvo' THEN 'TDA - Total em Dinheiro Alvo'\n"
        		+ "        WHEN '5 - ICP - Incentivo de Curto Prazo Pago (Bônus + PLR)' THEN 'ICP - Incentivo de Curto Prazo Pago (Bônus + PLR)'\n"
        		+ "        WHEN '6 - TD - Total em Dinheiro' THEN 'TD - Total em Dinheiro'\n"
        		+ "        WHEN '7 - ILP - Incentivos de Longo Prazo' THEN 'ILP - Incentivos de Longo Prazo'\n"
        		+ "        WHEN '8 - RDA - Remuneração Direta Alvo' THEN 'RDA - Remuneração Direta Alvo'\n"
        		+ "        WHEN '9 - RD - Remuneração Direta' THEN 'RD - Remuneração Direta'\n"
        		+ "        ELSE tee.desc_renum\n"
        		+ "    END AS desc_renum_new,\n"
        		+ "    CASE tee.desc_renum\n"
        		+ "        WHEN '1 - SBM - Salário Base Mensal' THEN CAST(tpd.valor_sb / 13.33 AS INT)\n"
        		+ "        WHEN '2 - SBM - Salário Base Anual' THEN CAST(tpd.valor_sb AS INT)\n"
        		+ "        WHEN '3 ICPA - Incentivo de Curto Prazo Alvo (Bônus + PLR)' THEN CAST(tpd.valor_cp_aliv AS INT)\n"
        		+ "        WHEN '4 TDA - Total em Dinheiro Alvo' THEN CAST(tpd.valor_tda AS INT)\n"
        		+ "        WHEN '5 - ICP - Incentivo de Curto Prazo Pago (Bônus + PLR)' THEN CAST(tpd.valor_cp_pg AS INT)\n"
        		+ "        WHEN '6 - TD - Total em Dinheiro' THEN CAST(tpd.valor_td AS INT)\n"
        		+ "        WHEN '7 - ILP - Incentivos de Longo Prazo' THEN CAST(tpd.valor_ilp AS INT)\n"
        		+ "        WHEN '8 - RDA - Remuneração Direta Alvo' THEN CAST(tpd.valor_rda AS INT)\n"
        		+ "        WHEN '9 - RD - Remuneração Direta' THEN CAST(tpd.valor_rd AS INT)\n"
        		+ "    END AS sua_empresa,\n"
        		+ "    tee.p10,\n"
        		+ "    tee.p25,\n"
        		+ "    tee.p50,\n"
        		+ "    tee.p75,\n"
        		+ "    tee.p90,\n"
        		+ "    tee.media,\n"
        		+ "    tee.qtd_empresas,\n"
        		+ "    tee.num_participantes,\n"
        		+ "    tpd.nome_empresa,\n"
        		+ "    tee.grade,\n"
        		+ "    tp.nm_familia,\n"
        		+ "    tp.nm_subfamilia,\n"
        		+ "    tpd.matricula,\n"
        		+ "    tpd.grade AS grade_empresa,\n"
        		+ "    tpd.codigo_cargo,\n"
        		+ "    tee.mercado AS mercado\n"
        		+ "FROM EmpresaSelecionada2 tee\n"
        		+ "INNER JOIN tb_pesquisa tp \n"
        		+ "    ON tp.codigo_cargo = tee.codigo_cargo\n"
        		+ "    AND tp.grade = tee.grade\n"
        		+ "    AND tp.nome_empresa = tee.nome_empresa\n"
        		+ "INNER JOIN TB_PESQUISA_DETALHE tpd\n"
        		+ "    ON tee.codigo_cargo = tpd.codigo_cargo\n"
        		+ "    AND tp.grade = tpd.grade_xr\n"
        		+ "    AND tp.codigo_cargo = tpd.codigo_cargo\n"
        		+ "    AND tpd.nome_empresa = tp.nome_empresa ");

        Query query = getEntityManagerMatriz().createNativeQuery(queryBuilder.toString())
                .setParameter("nomeEmpresa", nomeEmpresa);

        if (nomeFamilia != null && !nomeFamilia.isEmpty()) {
            query.setParameter("nomeFamilia", nomeFamilia);
        }
        if (nomeSubfamilia != null && !nomeSubfamilia.isEmpty()) {
            query.setParameter("nomeSubfamilia", nomeSubfamilia);
        }
        if (nomeCargo != null && !nomeCargo.isEmpty()) {
        	 query.setParameter("nomeCargo", nomeCargo);
        }
        if (mercado != null && !mercado.isEmpty()) {
        	 query.setParameter("mercado", mercado);
        }
        query.setHint("org.hibernate.fetchSize", 100000);
        List<Object> resultList = query.getResultList();

        for (Object record : resultList) {
            Object[] linha = (Object[]) record;
            MediasNovaEmpresaVO extracao = new MediasNovaEmpresaVO();
            extracao.setId((BigInteger) linha[0]);
            extracao.setNomeCargoXr((String) linha[1]);
            extracao.setNomeCargo((String) linha[2]);
            extracao.setDescRenum((String) linha[3]);

            if (linha[4] == null) {
                extracao.setSuaEmpresa(0);
            } else {
                extracao.setSuaEmpresa((int) linha[4]);
            }

            extracao.setP10((int) linha[5]);
            extracao.setP25((int) linha[6]);
            extracao.setP50((int) linha[7]);
            extracao.setP75((int) linha[8]);
            extracao.setP90((int) linha[9]);
            extracao.setMedia((int) linha[10]);
            extracao.setQtdEmpresas((int) linha[11]);
            extracao.setNumParticipantes((int) linha[12]);
            extracao.setNomeEmpresa((String) linha[13]);
            extracao.setGrade((int) linha[14]);
            extracao.setNmFamilia((String) linha[15]);
            extracao.setNmSubFamilia((String) linha[16]);
            extracao.setMatricula((String) linha[17]);
            extracao.setGradeEmpresa((String) linha[18]);
            extracao.setCodigoCargo((String) linha[19]);
            extracao.setMercado((String) linha[20]);
            extracoes.add(extracao);
        }
        for(int i=0; i<extracoes.size();i++) {

			if(extracoes.get(i).getDescRenum().equals("SBA - Salário Base Anual")) {
				if(extracoes.get(i).getNumParticipantes()<3) {

				}
			}

			if(extracoes.get(i).getQtdEmpresas()<3) {
				extracoes.get(i).setP10(0);
				extracoes.get(i).setP25(0);
				extracoes.get(i).setP50(0);
				extracoes.get(i).setP75(0);
				extracoes.get(i).setP90(0);
				extracoes.get(i).setMedia(0);
				extracoes.get(i).setQtdEmpresas(extracoes.get(i).getQtdEmpresas());

			}
			//Informações do cargo em 3 empresas: o sistema calcula somente a média
			else if (extracoes.get(i).getQtdEmpresas()>=3 && 
					extracoes.get(i).getQtdEmpresas()<4) {
				extracoes.get(i).setP10(0);
				extracoes.get(i).setP25(0);
				extracoes.get(i).setP50(0);
				extracoes.get(i).setP75(0);
				extracoes.get(i).setP90(0);

			}
			//Informações do cargo entre 4 empresa e 5 empresas: o sistema calcula a média e o P50
			else if (extracoes.get(i).getQtdEmpresas()>=4 && 
					extracoes.get(i).getQtdEmpresas()<6) {
				extracoes.get(i).setP10(0);
				extracoes.get(i).setP25(0);
				extracoes.get(i).setP75(0);
				extracoes.get(i).setP90(0);

			}
			//Informações do cargo entre 6 empresas e 7 empresas: o sistema calcula a média, o P50 e P25 e P75.
			else if (
					extracoes.get(i).getQtdEmpresas()>=6
					&& 
					extracoes.get(i).getQtdEmpresas()<10) {
				extracoes.get(i).setP10(0);
				extracoes.get(i).setP90(0);	
			}
			//Informações do cargo em 8 ou mais empresas: o sistema calcula a média, o P50 e P25 e P75, P90 e P10
			//else if (mediasFinal.get(i).getQtd_empresas()>=10) {
			//}

		}

        return extracoes;
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
	public String findGradeXR(String nomeEmpresa, String nomeCargo, 
			int gradeMinimo, int gradeMaximo) {

		List<Object> lista = getEntityManagerMatriz().
				createNativeQuery("select grade_xr from TB_PESQUISA tp "
						+ "  where 1=1 "
						+ "  AND nome_cargo = :nomeCargo "
						+ "  AND nome_empresa = :nomeEmpresa "
						+  " and GRADE  >= :gradeMinimo "
						+ "  and  GRADE <= :gradeMaximo"
						+ "  and grade_xr is not null"
						+ " order by grade_xr ").
				setParameter("nomeEmpresa", nomeEmpresa).
				setParameter("nomeCargo", nomeCargo).
				setParameter("gradeMinimo", gradeMinimo).
				setParameter("gradeMaximo", gradeMaximo)
				.getResultList();

		StringBuilder sb = new StringBuilder();
		for (Object valor : lista) {
		    sb.append(valor).append(" / ");
		}

		String resultado = sb.toString();
		if (resultado.endsWith(" / ")) {
		    resultado = resultado.substring(0, resultado.length() - 3);
		}
		return resultado ;
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
	public Integer findCounEmpresaPres(String nomeEmpresa) {
		Integer quantidade = (Integer) getEntityManagerMatriz().createNativeQuery(" select COUNT(1) as quantidade "
				+ " from TB_PESQUISA tp"
				+ " where nome_cargo in('PRESIDÊNCIA SUBSIDIÁRIA / CONTROLADA', 'PRESIDÊNCIA (CEO)', 'PRESIDENTE / CEO') "
				+ "and nome_empresa = :nomeEmpresa ").
				setParameter("nomeEmpresa", nomeEmpresa)
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
	public List<MediasVO> findMediaSuaEmpresaNew(String nmFamilia, String nmSubFamilia,
			String nmCargo, String mercGeral,
			Integer gradeMinimo, Integer gradeMaximo, String nomeEmpresa, Integer maiorGradeEmpresa,EntityManager em) {

		List<MediasVO> mediasFinal = new ArrayList<MediasVO>();
		List<Object> lista = new ArrayList<Object>();
		long startTime = System.nanoTime();
		queryFindMediaEmpresa = getEntityManagerMatriz().createNativeQuery(""
				+ "SELECT \n"
				+ "total.desc_renum,\n"
				+ "total.nome_cargo nome_cargo,\n"
				+ "total.media,\n"
				+ "total.p90,\n"
				+ "total.p75,\n"
				+ "total.p50,\n"
				+ "total.p25,\n"
				+ "total.p10,\n"
				+ "total.qtd_empresas,\n"
				+ "total.num_participantes,\n"
				+ "cast(inn.valor_sb as int) sua_empresa, inn.nome_cargo_empresa \n"
				+ "FROM ("
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
				+ "on(consulta.nome_cargo = sua_empresa.nome_cargo)) TOTAL \n"
				+ "				INNER JOIN (SELECT DISTINCT \n"
				+ "    existe, \n"
				+ "    DESC_RENUM,\n"
				+ "    CAST(ROUND(AVG(VALOR_SB), 0) AS INT) AS VALOR_SB, \n"
				+ "    NOME_CARGO_EMPRESA FROM (\n"
				+ "			SELECT 1 as existe,'SBM - Salário Base Mensal' as desc_renum , cast(round(valor_sb / 13.33, 0) as int) valor_sb,nome_cargo_empresa  FROM TB_PESQUISA_DETALHE tpd \n"
				+ "				WHERE  TPD.NOME_EMPRESA =:nomeEmpresa\n"
				+ "				and nome_cargo_XR =:nmCargo\n"
				+ "				UNION ALL \n"
				+ "			SELECT 2 as existe,'SBA - Salário Base Anual' as desc_renum , valor_sb,nome_cargo_empresa  FROM TB_PESQUISA_DETALHE tpd \n"
				+ "				WHERE  TPD.NOME_EMPRESA =:nomeEmpresa\n"
				+ "				and nome_cargo_XR =:nmCargo\n"
				+ "			UNION ALL	\n"
				+ "			SELECT 3 as existe,'ICPA - Incentivo de Curto Prazo Alvo (Bônus + PLR)' as desc_renum , valor_cp_aliv valor_sb,nome_cargo_empresa FROM TB_PESQUISA_DETALHE tpd \n"
				+ "				WHERE  TPD.NOME_EMPRESA =:nomeEmpresa\n"
				+ "				and nome_cargo_XR =:nmCargo\n"
				+ "			union all\n"
				+ "				SELECT 5 as existe, 'ICP - Incentivo de Curto Prazo Pago (Bônus + PLR)' as desc_renum , valor_cp_pg valor_sb,nome_cargo_empresa FROM TB_PESQUISA_DETALHE tpd \n"
				+ "				WHERE  TPD.NOME_EMPRESA =:nomeEmpresa\n"
				+ "				and nome_cargo_XR =:nmCargo\n"
				+ "			union all\n"
				+ "				SELECT 4 as existe, 'TDA - Total em Dinheiro Alvo' as desc_renum , valor_tda valor_sb,nome_cargo_empresa FROM TB_PESQUISA_DETALHE tpd \n"
				+ "				WHERE  TPD.NOME_EMPRESA =:nomeEmpresa\n"
				+ "				and nome_cargo_XR =:nmCargo\n"
				+ "			union all\n"
				+ "				SELECT 6 as existe, 'TD - Total em Dinheiro' as desc_renum , valor_td valor_sb,nome_cargo_empresa FROM TB_PESQUISA_DETALHE tpd \n"
				+ "				WHERE  TPD.NOME_EMPRESA =:nomeEmpresa\n"
				+ "				and nome_cargo_XR =:nmCargo	\n"
				+ "			union all\n"
				+ "				SELECT 7 as existe, 'ILP - Incentivos de Longo Prazo'  as desc_renum , valor_ilp valor_sb,nome_cargo_empresa FROM TB_PESQUISA_DETALHE tpd \n"
				+ "				WHERE  TPD.NOME_EMPRESA =:nomeEmpresa\n"
				+ "				and nome_cargo_XR =:nmCargo	\n"
				+ "			union all\n"
				+ "				SELECT 8 as existe, 'RDA - Remuneração Direta Alvo'  as desc_renum , valor_rda valor_sb,nome_cargo_empresa FROM TB_PESQUISA_DETALHE tpd \n"
				+ "				WHERE  TPD.NOME_EMPRESA = :nomeEmpresa\n"
				+ "				and nome_cargo_XR =:nmCargo	\n"
				+ "			union all	\n"
				+ "			SELECT 9 as existe, 'RD - Remuneração Direta'  as desc_renum , valor_rd valor_sb,nome_cargo_empresa FROM TB_PESQUISA_DETALHE tpd \n"
				+ "				WHERE  TPD.NOME_EMPRESA =:nomeEmpresa\n"
				+ "				and nome_cargo_XR =:nmCargo	"
				+ "				) DES GROUP BY existe, DESC_RENUM, NOME_CARGO_EMPRESA) AS INN ON INN.DESC_RENUM = TOTAL.DESC_RENUM");

			queryFindMediaEmpresa.setFlushMode(FlushModeType.COMMIT);
			queryFindMediaEmpresa.setHint("org.hibernate.cacheable", Boolean.TRUE);

		
		//SBM
		lista = queryFindMediaEmpresa.
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
		
		
		setParameter("nomeEmpresa",nomeEmpresa).
		setParameter("nmCargo",nmCargo).
		
		setParameter("nomeEmpresa",nomeEmpresa).
		setParameter("nmCargo",nmCargo).
		
		setParameter("nomeEmpresa",nomeEmpresa).
		setParameter("nmCargo",nmCargo).
		
		setParameter("nomeEmpresa",nomeEmpresa).
		setParameter("nmCargo",nmCargo).
		
		setParameter("nomeEmpresa",nomeEmpresa).
		setParameter("nmCargo",nmCargo).
		
		setParameter("nomeEmpresa",nomeEmpresa).
		setParameter("nmCargo",nmCargo).
		
		setParameter("nomeEmpresa",nomeEmpresa).
		setParameter("nmCargo",nmCargo).
		
		setParameter("nomeEmpresa",nomeEmpresa).
		setParameter("nmCargo",nmCargo).
		
		setParameter("nomeEmpresa",nomeEmpresa).
		setParameter("nmCargo",nmCargo)
		
		.getResultList();
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
			medias.setNomeCargoEmpresa((String) linha[11]);
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
			//Informações do cargo entre 4 empresa e 5 empresas: o sistema calcula a média e o P50
			else if (mediasFinal.get(i).getQtd_empresas()>=4 && 
					mediasFinal.get(i).getQtd_empresas()<6) {
				mediasFinal.get(i).setP10(0);
				mediasFinal.get(i).setP25(0);
				mediasFinal.get(i).setP75(0);
				mediasFinal.get(i).setP90(0);

			}
			//Informações do cargo entre 6 empresas e 7 empresas: o sistema calcula a média, o P50 e P25 e P75.
			else if (
					mediasFinal.get(i).getQtd_empresas()>=6
					&& 
					mediasFinal.get(i).getQtd_empresas()<10) {
				mediasFinal.get(i).setP10(0);
				mediasFinal.get(i).setP90(0);	
			}
			//Informações do cargo em 8 ou mais empresas: o sistema calcula a média, o P50 e P25 e P75, P90 e P10
			//else if (mediasFinal.get(i).getQtd_empresas()>=10) {
			//}

		}
		long stopTime = System.nanoTime();
		System.out.println((stopTime - startTime) / 1000000);
		return mediasFinal;

	}
	


	@SuppressWarnings("unchecked")
	public List<MediasVO> findMediaSuaEmpresa(String nmFamilia, String nmSubFamilia,
			String nmCargo, String mercGeral,
			Integer gradeMinimo, Integer gradeMaximo, String nomeEmpresa, Integer maiorGradeEmpresa,EntityManager em) {

		List<MediasVO> mediasFinal = new ArrayList<MediasVO>();
		List<Object> lista = new ArrayList<Object>();
		queryFindMediaEmpresa = getEntityManagerMatriz().createNativeQuery(""
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
				+ "on(consulta.nome_cargo = sua_empresa.nome_cargo)");

			queryFindMediaEmpresa.setFlushMode(FlushModeType.COMMIT);
			queryFindMediaEmpresa.setHint("org.hibernate.cacheable", Boolean.TRUE);

		
		//SBM
		lista = queryFindMediaEmpresa.
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
		setParameter("nomeEmpresa",nomeEmpresa)
		.getResultList();
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
			//Informações do cargo entre 4 empresa e 5 empresas: o sistema calcula a média e o P50
			else if (mediasFinal.get(i).getQtd_empresas()>=4 && 
					mediasFinal.get(i).getQtd_empresas()<6) {
				mediasFinal.get(i).setP10(0);
				mediasFinal.get(i).setP25(0);
				mediasFinal.get(i).setP75(0);
				mediasFinal.get(i).setP90(0);

			}
			//Informações do cargo entre 6 empresas e 7 empresas: o sistema calcula a média, o P50 e P25 e P75.
			else if (
					mediasFinal.get(i).getQtd_empresas()>=6
					&& 
					mediasFinal.get(i).getQtd_empresas()<10) {
				mediasFinal.get(i).setP10(0);
				mediasFinal.get(i).setP90(0);	
			}
			//Informações do cargo em 8 ou mais empresas: o sistema calcula a média, o P50 e P25 e P75, P90 e P10
			//else if (mediasFinal.get(i).getQtd_empresas()>=10) {
			//}

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
		getgetEntityManagerMatriz()()
		.createQuery("DELETE FROM TbDEPTO c WHERE c.idAREA = :p")
		.setParameter("p", tbAREA).executeUpdate();
	}

	@Transactional
	private void cutAllIdAREATbCARGOAssignments(TbAREAEntity tbAREA) {
		List<TbDEPTOEntity> result = new ArrayList<TbDEPTOEntity>();
		result = tbDEPTOService.findTbDEPTOsByIdAREA(tbAREA);
		for(int i=0; i<result.size();i++) {
			getgetEntityManagerMatriz()()
			.createQuery("DELETE FROM TbCARGOS c WHERE c.idDEPTO = :p")
			.setParameter("p", result.get(i)).executeUpdate();
		}

	}*/







}
