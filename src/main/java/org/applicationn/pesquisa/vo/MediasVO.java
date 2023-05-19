package org.applicationn.pesquisa.vo;
import java.io.Serializable;


import org.applicationn.simtrilhas.domain.BaseEntity;

public class MediasVO extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;


    private String descRenum;
    private String nomeCargo;
    private int media;
    private int p90;
    private int p75;
    private int p50;
    private int p25;
    private int p10;
    private int qtd_empresas;
    private int num_participantes;
    private int sua_empresa;
    private String descricaoDoFiltro;
	public String getDescRenum() {
		return descRenum;
	}
	public void setDescRenum(String descRenum) {
		this.descRenum = descRenum;
	}
	public String getNomeCargo() {
		return nomeCargo;
	}
	public void setNomeCargo(String nomeCargo) {
		this.nomeCargo = nomeCargo;
	}
	public int getMedia() {
		return media;
	}
	public void setMedia(int media) {
		this.media = media;
	}
	public int getP90() {
		return p90;
	}
	public void setP90(int p90) {
		this.p90 = p90;
	}
	public int getP75() {
		return p75;
	}
	public void setP75(int p75) {
		this.p75 = p75;
	}
	public int getP50() {
		return p50;
	}
	public void setP50(int p50) {
		this.p50 = p50;
	}
	public int getP25() {
		return p25;
	}
	public void setP25(int p25) {
		this.p25 = p25;
	}
	public int getP10() {
		return p10;
	}
	public void setP10(int p10) {
		this.p10 = p10;
	}
	public int getQtd_empresas() {
		return qtd_empresas;
	}
	public void setQtd_empresas(int qtd_empresas) {
		this.qtd_empresas = qtd_empresas;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public int getNum_participantes() {
		return num_participantes;
	}
	public void setNum_participantes(int num_participantes) {
		this.num_participantes = num_participantes;
	}
	public int getSua_empresa() {
		return sua_empresa;
	}
	public void setSua_empresa(int sua_empresa) {
		this.sua_empresa = sua_empresa;
	}
	public String getDescricaoDoFiltro() {
		return descricaoDoFiltro;
	}
	public void setDescricaoDoFiltro(String descricaoDoFiltro) {
		this.descricaoDoFiltro = descricaoDoFiltro;
	}

    
    
}
