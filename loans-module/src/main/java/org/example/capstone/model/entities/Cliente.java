package org.example.capstone.model.entities;

public class Cliente {
    private String nome;
    private int score;
    private double rendaMensal;

    public Cliente(String nome, int score, double rendaMensal) {
        this.nome = nome;
        this.score = score;
        this.rendaMensal = rendaMensal;
    }

    public int getScore() { return score; }
    public double getRendaMensal() { return rendaMensal; }
}
