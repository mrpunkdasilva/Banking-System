package org.example.capstone.controller.emprestimo;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.capstone.model.entities.Cliente;
import org.example.capstone.service.emprestimo.SimuladorEmprestimoService;


public class SolicitacaoEmprestimoController {

    @FXML private ComboBox<String> tipoEmprestimo;
    @FXML private TextField valorSolicitado;
    @FXML private ComboBox<Integer> prazoParcelas;
    @FXML private ComboBox<String> finalidade;
    @FXML private Label taxaMensal, cet, parcelaMensal, mensagemResultado;

    private final SimuladorEmprestimoService simulador = new SimuladorEmprestimoService();

    @FXML
    public void initialize() {
        tipoEmprestimo.getItems().addAll("Pessoal", "Consignado", "Estudantil" , "Imobiliário");
        finalidade.getItems().addAll( "Entrada", "Reforma", "Viagem", "Estudos", "Outros");
        for (int i = 6; i <= 240; i += 6) prazoParcelas.getItems().add(i);
    }

    public void simularEmprestimo() {
        try {
            double valor = Double.parseDouble(valorSolicitado.getText());
            int prazo = prazoParcelas.getValue();
            double taxa = simulador.obterTaxa(tipoEmprestimo.getValue());

            double parcela = simulador.calcularParcela(valor, prazo, taxa);
            double cetAno = simulador.calcularCETAnual(taxa);

            taxaMensal.setText("Taxa Mensal: " + String.format("%.2f%%", taxa * 100));
            cet.setText("CET Anual: " + String.format("%.2f%%", cetAno * 100));
            parcelaMensal.setText("Parcela: R$" + String.format("%.2f", parcela));
            mensagemResultado.setText("");

        } catch (Exception e) {
            mensagemResultado.setText("Erro na simulação. Verifique os dados.");
        }
    }

    public void solicitarEmprestimo() {
        Cliente cliente = new Cliente("Errisu", 900, 100000); // Exemplo temporário!!!

        double valor = Double.parseDouble(valorSolicitado.getText());
        int prazo = prazoParcelas.getValue();
        String tipo = tipoEmprestimo.getValue(); // Ex: "Pessoal", "Estudantil" etc.

        String resposta = simulador.analisarSolicitacao(cliente, valor, prazo, tipo);
        mensagemResultado.setText(resposta);
    }

}
