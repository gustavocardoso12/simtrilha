package org.applicationn.pesquisa.vo;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ProgressoImportacao {
    private String mercado;
    private String statusGeral;
    private String etapaAtual;
    private int percentualConcluido;
    private String mensagem;
    private LocalDateTime dataInicio;
    private LocalDateTime dataAtualizacao;

    public String getMercado() {
        return mercado;
    }

    public void setMercado(String mercado) {
        this.mercado = mercado;
    }

    public String getStatusGeral() {
        return statusGeral;
    }

    public void setStatusGeral(String statusGeral) {
        this.statusGeral = statusGeral;
    }

    public String getEtapaAtual() {
        return etapaAtual;
    }

    public void setEtapaAtual(String etapaAtual) {
        this.etapaAtual = etapaAtual;
    }

    public int getPercentualConcluido() {
        return percentualConcluido;
    }

    public void setPercentualConcluido(int percentualConcluido) {
        this.percentualConcluido = percentualConcluido;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public LocalDateTime getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDateTime dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProgressoImportacao that = (ProgressoImportacao) o;
        return percentualConcluido == that.percentualConcluido &&
                Objects.equals(mercado, that.mercado) &&
                Objects.equals(statusGeral, that.statusGeral) &&
                Objects.equals(etapaAtual, that.etapaAtual);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mercado, statusGeral, etapaAtual, percentualConcluido);
    }
}