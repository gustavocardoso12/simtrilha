package org.applicationn.pesquisa.vo;

public class TbDetalhesAcessoVO {

	private String username;
    private String sistema;
    private String dataUltimoAcesso;
    private int qtdAcessosLegado;
    private int quantidadeAcessosCargo;
    private int quantidadeAcessosSubfamilia;
    private String mesAcesso;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getSistema() {
		return sistema;
	}
	public void setSistema(String sistema) {
		this.sistema = sistema;
	}
	public String getDataUltimoAcesso() {
		return dataUltimoAcesso;
	}
	public void setDataUltimoAcesso(String dataUltimoAcesso) {
		this.dataUltimoAcesso = dataUltimoAcesso;
	}
	public int getQtdAcessosLegado() {
		return qtdAcessosLegado;
	}
	public void setQtdAcessosLegado(int qtdAcessosLegado) {
		this.qtdAcessosLegado = qtdAcessosLegado;
	}

	public String getMesAcesso() {
		return mesAcesso;
	}
	public void setMesAcesso(String mesAcesso) {
		this.mesAcesso = mesAcesso;
	}
	public int getQuantidadeAcessosCargo() {
		return quantidadeAcessosCargo;
	}
	public void setQuantidadeAcessosCargo(int quantidadeAcessosCargo) {
		this.quantidadeAcessosCargo = quantidadeAcessosCargo;
	}
	public int getQuantidadeAcessosSubfamilia() {
		return quantidadeAcessosSubfamilia;
	}
	public void setQuantidadeAcessosSubfamilia(int quantidadeAcessosSubfamilia) {
		this.quantidadeAcessosSubfamilia = quantidadeAcessosSubfamilia;
	}
	
	
}
