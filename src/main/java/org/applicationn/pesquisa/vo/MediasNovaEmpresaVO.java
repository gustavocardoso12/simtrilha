package org.applicationn.pesquisa.vo;

import java.math.BigInteger;
import java.util.Objects;

public class MediasNovaEmpresaVO {
	private BigInteger id;
	 private String nomeCargoXr;
	    private String nomeCargo;
	    private String descRenum;
	    private int suaEmpresa;
	    private int p10;
	    private int p25;
	    private int p50;
	    private int p75;
	    private int p90;
	    private int media;
	    private int qtdEmpresas;
	    private int numParticipantes;
	    private String nomeEmpresa;
	    private int grade;
	    private String nmFamilia;
	    private String nmSubFamilia;
	    private String matricula;
	    private String gradeEmpresa;
	    private String codigoCargo;
	    private String mercado;
	    private String desc_renum_ordenacao;
	    private String nome_empresa_ordenacao;
		public String getNomeCargoXr() {
			return nomeCargoXr;
		}
		public void setNomeCargoXr(String nomeCargoXr) {
			this.nomeCargoXr = nomeCargoXr;
		}
		public String getNomeCargo() {
			return nomeCargo;
		}
		public void setNomeCargo(String nomeCargo) {
			this.nomeCargo = nomeCargo;
		}
		public String getDescRenum() {
			return descRenum;
		}
		public void setDescRenum(String descRenum) {
			this.descRenum = descRenum;
		}
		public int getSuaEmpresa() {
			return suaEmpresa;
		}
		public void setSuaEmpresa(int suaEmpresa) {
			this.suaEmpresa = suaEmpresa;
		}
		public int getP10() {
			return p10;
		}
		public void setP10(int p10) {
			this.p10 = p10;
		}
		public int getP25() {
			return p25;
		}
		public void setP25(int p25) {
			this.p25 = p25;
		}
		public int getP50() {
			return p50;
		}
		public void setP50(int p50) {
			this.p50 = p50;
		}
		public int getP75() {
			return p75;
		}
		public void setP75(int p75) {
			this.p75 = p75;
		}
		public int getP90() {
			return p90;
		}
		public void setP90(int p90) {
			this.p90 = p90;
		}
		public int getMedia() {
			return media;
		}
		public void setMedia(int media) {
			this.media = media;
		}
		public int getQtdEmpresas() {
			return qtdEmpresas;
		}
		public void setQtdEmpresas(int qtdEmpresas) {
			this.qtdEmpresas = qtdEmpresas;
		}
		public int getNumParticipantes() {
			return numParticipantes;
		}
		public void setNumParticipantes(int numParticipantes) {
			this.numParticipantes = numParticipantes;
		}
		public String getNomeEmpresa() {
			return nomeEmpresa;
		}
		public void setNomeEmpresa(String nomeEmpresa) {
			this.nomeEmpresa = nomeEmpresa;
		}
		public int getGrade() {
			return grade;
		}
		public void setGrade(int grade) {
			this.grade = grade;
		}
		public String getNmFamilia() {
			return nmFamilia;
		}
		public void setNmFamilia(String nmFamilia) {
			this.nmFamilia = nmFamilia;
		}
		public String getNmSubFamilia() {
			return nmSubFamilia;
		}
		public void setNmSubFamilia(String nmSubFamilia) {
			this.nmSubFamilia = nmSubFamilia;
		}
		public String getMatricula() {
			return matricula;
		}
		public void setMatricula(String matricula) {
			this.matricula = matricula;
		}
		public String getGradeEmpresa() {
			return gradeEmpresa;
		}
		public void setGradeEmpresa(String gradeEmpresa) {
			this.gradeEmpresa = gradeEmpresa;
		}
		public String getCodigoCargo() {
			return codigoCargo;
		}
		public void setCodigoCargo(String codigoCargo) {
			this.codigoCargo = codigoCargo;
		}
		public String getMercado() {
			return mercado;
		}
		public void setMercado(String mercado) {
			this.mercado = mercado;
		}
		public BigInteger getId() {
			return id;
		}
		public void setId(BigInteger id) {
			this.id = id;
		}
		public String getDesc_renum_ordenacao() {
			return desc_renum_ordenacao;
		}
		public void setDesc_renum_ordenacao(String desc_renum_ordenacao) {
			this.desc_renum_ordenacao = desc_renum_ordenacao;
		}
		public String getNome_empresa_ordenacao() {
			return nome_empresa_ordenacao;
		}
		public void setNome_empresa_ordenacao(String nome_empresa_ordenacao) {
			this.nome_empresa_ordenacao = nome_empresa_ordenacao;
		}
		@Override
		public int hashCode() {
			return Objects.hash(codigoCargo, descRenum, desc_renum_ordenacao, grade, gradeEmpresa, id, matricula, media,
					mercado, nmFamilia, nmSubFamilia, nomeCargo, nomeCargoXr, nomeEmpresa, nome_empresa_ordenacao,
					numParticipantes, p10, p25, p50, p75, p90, qtdEmpresas, suaEmpresa);
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			MediasNovaEmpresaVO other = (MediasNovaEmpresaVO) obj;
			return Objects.equals(codigoCargo, other.codigoCargo) && Objects.equals(descRenum, other.descRenum)
					&& Objects.equals(desc_renum_ordenacao, other.desc_renum_ordenacao) && grade == other.grade
					&& Objects.equals(gradeEmpresa, other.gradeEmpresa) && Objects.equals(id, other.id)
					&& Objects.equals(matricula, other.matricula) && media == other.media
					&& Objects.equals(mercado, other.mercado) && Objects.equals(nmFamilia, other.nmFamilia)
					&& Objects.equals(nmSubFamilia, other.nmSubFamilia) && Objects.equals(nomeCargo, other.nomeCargo)
					&& Objects.equals(nomeCargoXr, other.nomeCargoXr) && Objects.equals(nomeEmpresa, other.nomeEmpresa)
					&& Objects.equals(nome_empresa_ordenacao, other.nome_empresa_ordenacao)
					&& numParticipantes == other.numParticipantes && p10 == other.p10 && p25 == other.p25
					&& p50 == other.p50 && p75 == other.p75 && p90 == other.p90 && qtdEmpresas == other.qtdEmpresas
					&& suaEmpresa == other.suaEmpresa;
		}
}
