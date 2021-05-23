package br.com.bandtec.avaliacaocontinuadatres.model;

import javax.persistence.*;

@Entity
public class Atleta {

    @Id
    private Integer id;

    private String nomeAtleta;

    private Integer treinoPorDia;

    private String tipoDieta;

    @ManyToOne
    private TipoCorredor tipoCorredor;

    @ManyToOne
    private TipoNadador tipoNadador;
//
//    public Atleta(Integer id, String nomeAtleta, Integer treinoPorDia, String tipoDieta, TipoCorredor tipoCorredor, TipoNadador tipoNadador) {
//        this.id = id;
//        this.nomeAtleta = nomeAtleta;
//        this.treinoPorDia = treinoPorDia;
//        this.tipoDieta = tipoDieta;
//        this.tipoCorredor = tipoCorredor;
//        this.tipoNadador = tipoNadador;
//    }
//
//    public Atleta(Integer id, String nomeAtleta, Integer treinoPorDia, String tipoDieta, String tipoCorredor, String tipoNadador) {
//
//    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNomeAtleta() {
        return nomeAtleta;
    }

    public void setNomeAtleta(String nomeAtleta) {
        this.nomeAtleta = nomeAtleta;
    }

    public Integer getTreinoPorDia() {
        return treinoPorDia;
    }

    public void setTreinoPorDia(Integer treinoPorDia) {
        this.treinoPorDia = treinoPorDia;
    }

    public String getTipoDieta() {
        return tipoDieta;
    }

    public void setTipoDieta(String tipoDieta) {
        this.tipoDieta = tipoDieta;
    }

    public TipoCorredor getTipoCorredor() {
        return tipoCorredor;
    }

    public void setTipoCorredor(TipoCorredor tipoCorredor) {
        this.tipoCorredor = tipoCorredor;
    }

    public TipoNadador getTipoNadador() {
        return tipoNadador;
    }

    public void setTipoNadador(TipoNadador tipoNadador) {
        this.tipoNadador = tipoNadador;
    }
}
