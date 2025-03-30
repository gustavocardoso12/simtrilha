package org.applicationn.pesquisa.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.Query;

import org.applicationn.pesquisa.domain.TbPesquisa;
import org.applicationn.pesquisa.vo.EmpresaVisiveisVO;
import org.applicationn.pesquisa.vo.MediasVO;
import org.applicationn.simtrilhas.domain.security.UserEntity;
import org.applicationn.simtrilhas.service.BaseService;
import org.applicationn.simtrilhas.service.security.SecurityWrapper;
import org.applicationn.simtrilhas.service.security.UserService;

@Named
@TransactionManagement(TransactionManagementType.BEAN)
public class TbPesquisaServiceNew extends BaseService<TbPesquisa> implements Serializable {

	private static final long serialVersionUID = 1L;

	public TbPesquisaServiceNew(){
		super(TbPesquisa.class);
	}
	 
		@Inject
		protected UserService userService;
		@Inject
		private TbEmpresaService tbEmpresaService;
	@SuppressWarnings("unchecked")
	public List<MediasVO> findMediaSuaEmpresa(String nmFamilia, String nmSubFamilia,
	        String nmCargo, String mercGeral,
	        Integer gradeMinimo, Integer gradeMaximo,
	        String nomeEmpresa, Integer maiorGradeEmpresa,
	        EntityManager em) {
		String username = SecurityWrapper.getUsername();
        UserEntity user = userService.findUserByUsername(username);
        EmpresaVisiveisVO empresaVis = tbEmpresaService.empresasVisiveisPorUsuario(user.getUsername());
        long visivel = empresaVis.getVisivel();
        StringBuilder consultaSQL = new StringBuilder();
        consultaSQL.append("WITH baseData AS (\n")
            .append("  SELECT \n")
            .append("    tp.nome_cargo,\n")
            .append("    tp.nome_empresa,\n")
            .append("    tp.grade,\n")
            .append("    tp.freq_fun,\n")
            .append("    tp.media_sb,\n")
            .append("    tp.mediana_cp_aliv,\n")
            .append("    tp.media_tda,\n")
            .append("    tp.mediana_cp_pg,\n")
            .append("    tp.media_tb,\n")
            .append("    tp.mediana_ilp,\n")
            .append("    tp.media_rda,\n")
            .append("    tp.media_rd,\n")
            .append("    CASE WHEN tp.nome_empresa = :nomeEmpresa THEN 1 ELSE 0 END AS empresa_referencia, EV.visivel  \n") // Identificador da empresa de referência
            .append("  FROM tb_pesquisa tp\n")
            .append("    INNER JOIN TB_PESQUISA_MERCADO tpm ON tp.codigo_empresa = tpm.cd_empresa\n")
            .append("    INNER JOIN tb_mercado tm ON tpm.cd_mercado = tm.id\n")
            .append("    LEFT JOIN TB_EMPRESA_VISIBILIDADE ev ON ev.nome_empresa = tp.nome_empresa\n") // LEFT JOIN para incluir todas as empresas
            .append("  WHERE \n")
            .append("    nm_familia    = :nmFamilia\n")
            .append("    AND nm_subfamilia = :nmSubFamilia\n")
            .append("    AND nome_cargo    = :nmCargo\n")
            .append("    AND tm.dsMercado  = :nmMercado\n")
            .append("    AND tp.grade BETWEEN :gradeMinimo AND :gradeMaximo\n")
            .append("    ") // Sempre inclui a empresa de referência
            .append("),\n")
            .append("maxGrades AS (\n")
            .append("  SELECT \n")
            .append("    nome_empresa, \n")
            .append("    MAX(grade) AS max_grade\n")
            .append("  FROM baseData\n")
            .append("  GROUP BY nome_empresa\n")
            .append("),\n")
            .append("filteredData AS (\n")
            .append("  SELECT b.*\n")
            .append("  FROM baseData b\n")
            .append("  INNER JOIN maxGrades mg ON b.nome_empresa = mg.nome_empresa\n")
            .append("  WHERE b.grade = mg.max_grade\n")
            .append("),\n")
            .append("Agg AS (\n")
            .append("  SELECT \n")
            .append("    f.nome_cargo,\n")
            .append("    v.metric_type,\n")
            .append("    v.metric_value,\n")
            .append("    f.freq_fun\n")
            .append("  FROM filteredData f\n")
            .append("  CROSS APPLY ( VALUES\n")
            .append("      ('SBM - Salário Base Mensal', CASE WHEN f.media_sb IS NOT NULL THEN f.media_sb / 13.33 ELSE NULL END),\n")
            .append("      ('SBA - Salário Base Anual',   f.media_sb),\n")
            .append("      ('ICPA - Incentivo de Curto Prazo Alvo (Bônus + PLR)', f.mediana_cp_aliv),\n")
            .append("      ('TDA - Total em Dinheiro Alvo', f.media_tda),\n")
            .append("      ('ICP - Incentivo de Curto Prazo Pago (Bônus + PLR)', f.mediana_cp_pg),\n")
            .append("      ('TD - Total em Dinheiro', f.media_tb),\n")
            .append("      ('ILP - Incentivos de Longo Prazo', f.mediana_ilp),\n")
            .append("      ('RDA - Remuneração Direta Alvo', f.media_rda),\n")
            .append("      ('RD - Remuneração Direta', f.media_rd)\n")
            .append("  ) AS v(metric_type, metric_value)\n")
            .append("  WHERE v.metric_value IS NOT NULL\n")
            .append("    AND f.VISIVEL = 1 \n") // Exclui a empresa de referência das estatísticas do mercado
            .append("),\n")
            .append("Aggregated AS (\n")
            .append("  SELECT\n")
            .append("    metric_type AS desc_renum,\n")
            .append("    nome_cargo,\n")
            .append("    CAST(ROUND(AVG(metric_value), 0) AS INT) AS media,\n")
            .append("    COUNT(*) AS qtd_empresas,\n")
            .append("    SUM(freq_fun) AS num_participantes\n")
            .append("  FROM Agg\n")
            .append("  GROUP BY metric_type, nome_cargo\n")
            .append("),\n")
            .append("Pct AS (\n")
            .append("  SELECT\n")
            .append("    bd.nome_cargo,\n")
            .append("    v.metric_type,\n")
            .append("    PERCENTILE_CONT(0.90) WITHIN GROUP (ORDER BY v.metric_value) OVER (PARTITION BY bd.nome_cargo, v.metric_type) AS p90,\n")
            .append("    PERCENTILE_CONT(0.75) WITHIN GROUP (ORDER BY v.metric_value) OVER (PARTITION BY bd.nome_cargo, v.metric_type) AS p75,\n")
            .append("    PERCENTILE_CONT(0.50) WITHIN GROUP (ORDER BY v.metric_value) OVER (PARTITION BY bd.nome_cargo, v.metric_type) AS p50,\n")
            .append("    PERCENTILE_CONT(0.25) WITHIN GROUP (ORDER BY v.metric_value) OVER (PARTITION BY bd.nome_cargo, v.metric_type) AS p25,\n")
            .append("    PERCENTILE_CONT(0.10) WITHIN GROUP (ORDER BY v.metric_value) OVER (PARTITION BY bd.nome_cargo, v.metric_type) AS p10\n")
            .append("  FROM baseData bd\n")
            .append("  CROSS APPLY ( VALUES\n")
            .append("      ('SBM - Salário Base Mensal', CASE WHEN bd.media_sb IS NOT NULL THEN bd.media_sb / 13.33 ELSE NULL END),\n")
            .append("      ('SBA - Salário Base Anual',   bd.media_sb),\n")
            .append("      ('ICPA - Incentivo de Curto Prazo Alvo (Bônus + PLR)', bd.mediana_cp_aliv),\n")
            .append("      ('TDA - Total em Dinheiro Alvo', bd.media_tda),\n")
            .append("      ('ICP - Incentivo de Curto Prazo Pago (Bônus + PLR)', bd.mediana_cp_pg),\n")
            .append("      ('TD - Total em Dinheiro', bd.media_tb),\n")
            .append("      ('ILP - Incentivos de Longo Prazo', bd.mediana_ilp),\n")
            .append("      ('RDA - Remuneração Direta Alvo', bd.media_rda),\n")
            .append("      ('RD - Remuneração Direta', bd.media_rd)\n")
            .append("  ) AS v(metric_type, metric_value)\n")
            .append("  WHERE v.metric_value IS NOT NULL\n")
            .append("    AND bd.visivel = 1\n") // Exclui a empresa de referência dos percentis
            .append("),\n")
            .append("Company AS (\n")
            .append("  SELECT\n")
            .append("    bd.nome_cargo,\n")
            .append("    v.metric_type,\n")
            .append("    CAST(ROUND(AVG(v.metric_value), 0) AS INT) AS sua_empresa\n")
            .append("  FROM baseData bd\n")
            .append("  CROSS APPLY ( VALUES\n")
            .append("      ('SBM - Salário Base Mensal', CASE WHEN bd.media_sb IS NOT NULL THEN bd.media_sb / 13.33 ELSE NULL END),\n")
            .append("      ('SBA - Salário Base Anual',   bd.media_sb),\n")
            .append("      ('ICPA - Incentivo de Curto Prazo Alvo (Bônus + PLR)', bd.mediana_cp_aliv),\n")
            .append("      ('TDA - Total em Dinheiro Alvo', bd.media_tda),\n")
            .append("      ('ICP - Incentivo de Curto Prazo Pago (Bônus + PLR)', bd.mediana_cp_pg),\n")
            .append("      ('TD - Total em Dinheiro', bd.media_tb),\n")
            .append("      ('ILP - Incentivos de Longo Prazo', bd.mediana_ilp),\n")
            .append("      ('RDA - Remuneração Direta Alvo', bd.media_rda),\n")
            .append("      ('RD - Remuneração Direta', bd.media_rd)\n")
            .append("  ) AS v(metric_type, metric_value)\n")
            .append("  WHERE bd.grade = :maiorGrade\n")
            .append("    AND bd.nome_empresa = :nomeEmpresa\n") // Sempre pega apenas a empresa referência
            .append("    AND bd.empresa_referencia = 1\n") // Garante que só pega a empresa de referência
            .append("    AND v.metric_value IS NOT NULL\n")
            .append("  GROUP BY bd.nome_cargo, v.metric_type\n")
            .append(")\n")
            .append("\n")
            .append("SELECT\n")
            .append("  a.desc_renum,\n")
            .append("  a.nome_cargo,\n")
            .append("  a.media,\n")
            .append("  CAST(ROUND(p.p90, 0) AS INT) AS p90,\n")
            .append("  CAST(ROUND(p.p75, 0) AS INT) AS p75,\n")
            .append("  CAST(ROUND(p.p50, 0) AS INT) AS p50,\n")
            .append("  CAST(ROUND(p.p25, 0) AS INT) AS p25,\n")
            .append("  CAST(ROUND(p.p10, 0) AS INT) AS p10,\n")
            .append("  a.qtd_empresas,\n")
            .append("  a.num_participantes,\n")
            .append("  c.sua_empresa\n") // Removido o ISNULL para depuração
            .append("FROM Aggregated a\n")
            .append("  JOIN (\n")
            .append("    SELECT DISTINCT \n")
            .append("      nome_cargo, \n")
            .append("      metric_type, \n")
            .append("      p90, p75, p50, p25, p10\n")
            .append("    FROM Pct\n")
            .append("  ) p ON a.nome_cargo = p.nome_cargo AND a.desc_renum = p.metric_type\n")
            .append("  LEFT JOIN Company c ON a.nome_cargo = c.nome_cargo AND a.desc_renum = c.metric_type\n")
            .append("ORDER BY \n")
            .append("  CASE a.desc_renum\n")
            .append("    WHEN 'SBM - Salário Base Mensal' THEN 1\n")
            .append("    WHEN 'SBA - Salário Base Anual' THEN 2\n")
            .append("    WHEN 'ICPA - Incentivo de Curto Prazo Alvo (Bônus + PLR)' THEN 3\n")
            .append("    WHEN 'TDA - Total em Dinheiro Alvo' THEN 4\n")
            .append("    WHEN 'ICP - Incentivo de Curto Prazo Pago (Bônus + PLR)' THEN 5\n")
            .append("    WHEN 'TD - Total em Dinheiro' THEN 6\n")
            .append("    WHEN 'ILP - Incentivos de Longo Prazo' THEN 7\n")
            .append("    WHEN 'RDA - Remuneração Direta Alvo' THEN 8\n")
            .append("    WHEN 'RD - Remuneração Direta' THEN 9\n")
            .append("    ELSE 10\n")
            .append("  END");

	    // Cria a query nativa e seta os parâmetros (cada parâmetro apenas uma vez)
	    Query query = getEntityManagerMatriz().createNativeQuery(consultaSQL.toString());
	    query.setFlushMode(FlushModeType.COMMIT);
	    query.setHint("org.hibernate.cacheable", Boolean.TRUE);
	    query.setParameter("nmFamilia", nmFamilia);
	    query.setParameter("nmSubFamilia", nmSubFamilia);
	    query.setParameter("nmCargo", nmCargo);
	    query.setParameter("nmMercado", mercGeral);
	    query.setParameter("gradeMinimo", gradeMinimo);
	    query.setParameter("gradeMaximo", gradeMaximo);
	    query.setParameter("maiorGrade", maiorGradeEmpresa);
	    query.setParameter("nomeEmpresa", nomeEmpresa);

	    // Executa a query e itera sobre os resultados
	    List<Object[]> resultList = query.getResultList();
	    List<MediasVO> mediasFinal = new ArrayList<>();
	    for (Object[] row : resultList) {
	        MediasVO medias = new MediasVO();
	        medias.setDescRenum((String) row[0]);
	        medias.setNomeCargo((String) row[1]);
	        medias.setMedia((int) row[2]);
	        medias.setP90((int) row[3]);
	        medias.setP75((int) row[4]);
	        medias.setP50((int) row[5]);
	        medias.setP25((int) row[6]);
	        medias.setP10((int) row[7]);
	        medias.setQtd_empresas((int) row[8]);
	        medias.setNum_participantes((int) row[9]);
	        medias.setSua_empresa(row[10] == null ? 0 : (int) row[10]);
	        mediasFinal.add(medias);
	    }

	    // Aplica as regras de negócio para ajustar os percentis e média
	    for (MediasVO media : mediasFinal) {
	        if ("SBA - Salário Base Anual".equals(media.getDescRenum()) && media.getNum_participantes() < 3) {
	            return null;
	        }
	        if (media.getQtd_empresas() < 3) {
	            media.setP10(0);
	            media.setP25(0);
	            media.setP50(0);
	            media.setP75(0);
	            media.setP90(0);
	            media.setMedia(0);
	            media.setNum_participantes(media.getQtd_empresas());
	        } else if (media.getQtd_empresas() >= 3 && media.getQtd_empresas() < 4) {
	            media.setP10(0);
	            media.setP25(0);
	            media.setP50(0);
	            media.setP75(0);
	            media.setP90(0);
	        } else if (media.getQtd_empresas() >= 4 && media.getQtd_empresas() < 6) {
	            media.setP10(0);
	            media.setP25(0);
	            media.setP75(0);
	            media.setP90(0);
	        } else if (media.getQtd_empresas() >= 6 && media.getQtd_empresas() < 10) {
	            media.setP10(0);
	            media.setP90(0);
	        }
	    }
	    
	    return mediasFinal;
	}
}
