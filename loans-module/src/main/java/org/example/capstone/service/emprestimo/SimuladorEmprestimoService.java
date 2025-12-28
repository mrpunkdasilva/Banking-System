package org.example.capstone.service.emprestimo;


import org.example.capstone.model.entities.Cliente;

public class SimuladorEmprestimoService {

    public double obterTaxa(String tipo) {
        return switch (tipo) {
            case "Consignado" -> 0.015;
            case "Pessoal" -> 0.03;
            case "Estudantil" -> 0.01;
            case "Imobiliário" -> 0.009;
            default -> throw new IllegalArgumentException("Tipo de empréstimo inválido.");
        };
    }

    //Logica do max e minimo para emprestimo!!
    private record RegrasEmprestimo(double valorMin, double valorMax, int prazoMax, int scoreMin) {}

    private RegrasEmprestimo obterRegras(String tipo) {
        return switch (tipo) {
            case "Consignado" -> new RegrasEmprestimo(1000, 100000, 96, 500);
            case "Pessoal" -> new RegrasEmprestimo(1000, 30000, 48, 600);
            case "Estudantil" -> new RegrasEmprestimo(1000, 50000, 60, 400);
            case "Imobiliário" -> new RegrasEmprestimo(10000, 500000, 240, 650);
            default -> throw new IllegalArgumentException("Tipo de empréstimo inválido.");
        };
    }


    public double calcularParcela(double valor, int meses, double taxaMensal) {
        double fator = Math.pow(1 + taxaMensal, meses);
        return (valor * taxaMensal * fator) / (fator - 1);
    }

    public double calcularCETAnual(double taxaMensal) {
        return Math.pow(1 + taxaMensal, 12) - 1;
    }

    public String analisarSolicitacao(Cliente cliente, double valor, int prazo, String tipo) {
        try {
            RegrasEmprestimo regras = obterRegras(tipo);
            double taxa = obterTaxa(tipo);

            if (cliente.getScore() < regras.scoreMin())
                return "Solicitação negada: Score muito baixo para o tipo " + tipo + ".";

            if (valor < regras.valorMin() || valor > regras.valorMax())
                return "Valor fora do intervalo permitido para o tipo " + tipo + ".";

            if (prazo > regras.prazoMax())
                return "Prazo excede o máximo permitido para o tipo " + tipo + ".";

            double parcela = calcularParcela(valor, prazo, taxa);
            double maxParcela = cliente.getRendaMensal() * 0.30;

            if (parcela > maxParcela) {
                double valorMax = maxParcela * prazo;
                return "Valor indisponível. Seu limite máximo é de R$ " + String.format("%.2f", valorMax);
            }

            return "Pré-aprovação realizada com sucesso para " + tipo + "! Dinheiro em até 24h.";

        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }

}
