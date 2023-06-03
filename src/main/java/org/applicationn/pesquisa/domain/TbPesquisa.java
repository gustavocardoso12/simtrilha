package org.applicationn.pesquisa.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Digits;

import org.applicationn.simtrilhas.domain.BaseEntity;

@Entity(name="TbPesquisa")
@Table(name="TB_PESQUISA")
public class TbPesquisa extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    
    
    @Column(name = "createdAt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name="ANO")
    private int ano;
    
    @Column(name="codigo_pes")
    private int codigoPes;
    
    @Column(name="nm_familia")
    private String nmFamilia;
    
    @Column(name="nm_subfamilia")
    private String nmSubFamilia;
    
    @Column(name="seq_cargo")
    private int seqCargo;
    
    @Column(name="codigo_cargo")
    private String codigoCargo;
    
    @Column(name="nome_cargo")
    private String nomeCargo;
    
    @Column(name="grade")
    private int grade;
    
    @Column(name="codigo_empresa")
    private int codigoEmpresa;
    
    @Column(name="nome_empresa")
    private String nomeEmpresa;
    
    @Column(name="freq_fun")
    private int freqFun;
    
    @Column(name="media_sb")
    @Digits(integer = 10, fraction = 2)
    private Double mediaSb;
    
    @Column(name="media_tb")
    @Digits(integer = 10, fraction = 2)
    private Double mediaTb;
    
    @Column(name="media_tda")
    @Digits(integer = 10, fraction = 2)
    private Double mediaTda;
    
    @Column(name="media_rd")
    @Digits(integer = 10, fraction = 2)
    private Double mediaRd;
    
    @Column(name="media_rda")
    @Digits(integer = 10, fraction = 2)
    private Double mediaRda;

    @Column(name="mediana_cp_aliv")
    @Digits(integer = 10, fraction = 2)
    private Double medianaCpAliv;
    
    @Column(name="mediana_cp_pg")
    @Digits(integer = 10, fraction = 2)
    private Double medianaCpPg;
    
    @Column(name="mediana_ilp")
    @Digits(integer = 10, fraction = 2)
    private Double medianailp;
    
    @Column(name="descricao_completa_cargos")
    private String descricao_completa_cargos;
    
    public void updateAuditInformation(String username) {
        if (this.getId() != null) {
        } else {
            createdAt = new Date();
        }
    }



	public Date getCreatedAt() {
		return createdAt;
	}



	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}



	public int getAno() {
		return ano;
	}



	public void setAno(int ano) {
		this.ano = ano;
	}



	public int getCodigoPes() {
		return codigoPes;
	}



	public void setCodigoPes(int codigoPes) {
		this.codigoPes = codigoPes;
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



	public int getSeqCargo() {
		return seqCargo;
	}



	public void setSeqCargo(int seqCargo) {
		this.seqCargo = seqCargo;
	}



	public String getCodigoCargo() {
		return codigoCargo;
	}



	public void setCodigoCargo(String codigoCargo) {
		this.codigoCargo = codigoCargo;
	}



	public String getNomeCargo() {
		return nomeCargo;
	}



	public void setNomeCargo(String nomeCargo) {
		this.nomeCargo = nomeCargo;
	}



	public int getGrade() {
		return grade;
	}



	public void setGrade(int grade) {
		this.grade = grade;
	}



	public int getCodigoEmpresa() {
		return codigoEmpresa;
	}



	public void setCodigoEmpresa(int codigoEmpresa) {
		this.codigoEmpresa = codigoEmpresa;
	}



	public String getNomeEmpresa() {
		return nomeEmpresa;
	}



	public void setNomeEmpresa(String nomeEmpresa) {
		this.nomeEmpresa = nomeEmpresa;
	}



	public int getFreqFun() {
		return freqFun;
	}



	public void setFreqFun(int freqFun) {
		this.freqFun = freqFun;
	}



	public Double getMediaSb() {
		return mediaSb;
	}



	public void setMediaSb(Double mediaSb) {
		this.mediaSb = mediaSb;
	}



	public Double getMediaTb() {
		return mediaTb;
	}



	public void setMediaTb(Double mediaTb) {
		this.mediaTb = mediaTb;
	}



	public Double getMediaTda() {
		return mediaTda;
	}



	public void setMediaTda(Double mediaTda) {
		this.mediaTda = mediaTda;
	}



	public Double getMediaRd() {
		return mediaRd;
	}



	public void setMediaRd(Double mediaRd) {
		this.mediaRd = mediaRd;
	}



	public Double getMediaRda() {
		return mediaRda;
	}



	public void setMediaRda(Double mediaRda) {
		this.mediaRda = mediaRda;
	}



	public Double getMedianaCpAliv() {
		return medianaCpAliv;
	}



	public void setMedianaCpAliv(Double medianaCpAliv) {
		this.medianaCpAliv = medianaCpAliv;
	}



	public Double getMedianaCpPg() {
		return medianaCpPg;
	}



	public void setMedianaCpPg(Double medianaCpPg) {
		this.medianaCpPg = medianaCpPg;
	}



	public Double getMedianailp() {
		return medianailp;
	}



	public void setMedianailp(Double medianailp) {
		this.medianailp = medianailp;
	}



	public static long getSerialversionuid() {
		return serialVersionUID;
	}



	public String getDescricao_completa_cargos() {
		return descricao_completa_cargos;
	}



	public void setDescricao_completa_cargos(String descricao_completa_cargos) {
		this.descricao_completa_cargos = descricao_completa_cargos;
	}

   

}
