package com.stopmario.DAO;

import com.stopmario.factory.ModuloConexao;
import com.stopmario.model.Partida;
import com.stopmario.view.TelaTabela;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 * Classe PartidaDao
 *
 * @author Juliano
 * @version 1.1
 */
public class PartidaDAO {

    private final Connection conexao;
    private PreparedStatement pst;
    private ResultSet rs;

    public PartidaDAO() {
        conexao = ModuloConexao.conectar();
    }

    public void adicionarPontos(Partida jogo) {
        if (jogadorExiste(jogo.getNome_jogador())) {
            atualizarPontos(jogo);
        } else {
            adicionarNovoJogador(jogo);
        }
    }

    private boolean jogadorExiste(String nomeJogador) {
        String sql = "SELECT COUNT(*) FROM tb01_partida WHERE tb01_nome_jogador = ?";

        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, nomeJogador);
            rs = pst.executeQuery();

            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao verificar jogador: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } finally {

        }

        return false;
    }

    private void atualizarPontos(Partida jogo) {
        String sql = "UPDATE tb01_partida SET tb01_pontos = tb01_pontos + ? WHERE tb01_nome_jogador = ?";

        try {
            pst = conexao.prepareStatement(sql);
            pst.setInt(1, jogo.getPontos());
            pst.setString(2, jogo.getNome_jogador());

            int atualizado = pst.executeUpdate();

            if (atualizado > 0) {

            } else {
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao atualizar pontos: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } finally {

        }
    }

    private void adicionarNovoJogador(Partida jogo) {
        String sql = "INSERT INTO tb01_partida (tb01_nome_jogador, tb01_pontos, tb01_dificuldade, tb01_tempo) VALUES (?, ?, ?, ?)";

        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, jogo.getNome_jogador());
            pst.setInt(2, jogo.getPontos());
            pst.setString(3, jogo.getDificuldade());
            pst.setString(4, jogo.getTempo());

            int adicionado = pst.executeUpdate();

            if (adicionado > 0) {
            } else {
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao adicionar novo jogador: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } finally {

        }
    }

    public void carregarPlacar() {
        String sql = "SELECT * FROM tb01_partida ORDER BY tb01_pontos DESC";

        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            DefaultTableModel tab = (DefaultTableModel) TelaTabela.tbPlacar.getModel();

            // Limpar a tabela antes de adicionar os novos dados
            tab.setRowCount(0);

            while (rs.next()) {
                tab.addRow(new Object[]{rs.getString("tb01_nome_jogador"), rs.getString("tb01_pontos"), rs.getString("tb01_tempo")});
            }
            TelaTabela.tbPlacar.setModel(tab);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao carregar placar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void fecharConexao() {
        try {
            if (pst != null) {
                pst.close();
            }
            if (conexao != null) {
                conexao.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao fechar conexão: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
