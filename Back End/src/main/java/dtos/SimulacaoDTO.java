package dtos;

import entities.Variante;

import java.io.Serializable;

public class SimulacaoDTO implements Serializable {
    int nb, q;
    double LVao;
    int variantCode;

    public SimulacaoDTO(){}

    public SimulacaoDTO(int nb,double LVao,  int q,  int variantCode) {
        this.nb = nb;
        this.q = q;
        this.LVao = LVao;
        this.variantCode = variantCode;
    }


    public int getNb() {
        return nb;
    }

    public void setNb(int nb) {
        this.nb = nb;
    }

    public int getQ() {
        return q;
    }

    public void setQ(int q) {
        this.q = q;
    }

    public double getLVao() {
        return LVao;
    }

    public void setLVao(double LVao) {
        this.LVao = LVao;
    }

    public int getVariantCode() {
        return variantCode;
    }

    public void setVariantCode(int variantCode) {
        this.variantCode = variantCode;
    }
}
