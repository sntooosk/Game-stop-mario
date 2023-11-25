/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stopmario.model;

/**
 * Classe Partida
 *
 * @author Juliano
 * @version 1.1
 */
public class Partida {

    private int id;
    private String nome_jogador;
    private int pontos;
    private String dificuldade;
    private String tempo;

    public Partida(String nome_jogador, int pontos, String dificuldade, String tempo) {
        this.nome_jogador = nome_jogador;
        this.pontos = pontos;
        this.dificuldade = dificuldade;
        this.tempo = tempo;
    }

    public String getTempo() {
        return tempo;
    }

    public void setTempo(String tempo) {
        this.tempo = tempo;
    }

    public String getDificuldade() {
        return dificuldade;
    }

    public void setDificuldade(String dificuldade) {
        this.dificuldade = dificuldade;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome_jogador() {
        return nome_jogador;
    }

    public void setNome_jogador(String nome_jogador) {
        this.nome_jogador = nome_jogador;
    }

    public int getPontos() {
        return pontos;
    }

    public void setPontos(int pontos) {
        this.pontos = pontos;
    }
}
