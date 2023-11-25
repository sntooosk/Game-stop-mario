package com.stopmario.view;

import com.stopmario.DAO.PartidaDAO;
import com.stopmario.model.Partida;
import java.awt.Image;
import static java.lang.Thread.sleep;
import lib.som.modelSom;

import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import javax.swing.ImageIcon;

public class TelaTabela extends javax.swing.JFrame {

    /**
     * Tela Jogo
     *
     * @author Juliano
     * @version 1.1
     */
    private int numeroSorteado;
    private final DefaultTableModel modeloTabela;
    private final DefaultTableModel modeloPlacar;
    private int tempo = 1;
    private int pontos = 0;
    private final PartidaDAO partidaDAO;
    private final modelSom som;

    public TelaTabela() {
        initComponents();
        // Associa modelo de tabela à tbStop
        modeloTabela = (DefaultTableModel) tbStop.getModel();

        // Associa modelo de tabela à tbPlacar
        modeloPlacar = (DefaultTableModel) tbPlacar.getModel();
        modeloPlacar.addRow(new Object[]{lblNome.getText(), 0, 0});

        // Inicializa instâncias para manipulação de partidas e som
        partidaDAO = new PartidaDAO();
        som = new modelSom();

        // Carrega o placar na tabela
        partidaDAO.carregarPlacar();
    }

    // Método para sortear um número com base na dificuldade
    private void sortearNumero() {
        int minSorteio = 10;
        int maxSorteio = 50;

        if ("Medio".equals(lblDifi.getText())) {
            minSorteio = 51;
            maxSorteio = 100;
        } else if ("Dificil".equals(lblDifi.getText())) {
            minSorteio = 101;
            maxSorteio = 200;
        }

        numeroSorteado = new Random().nextInt(maxSorteio - minSorteio + 1) + minSorteio;
        txtNumeroSorteado.setText(String.format("%02d", numeroSorteado));
    }

    // Método para iniciar o temporizador
    private void iniciarTempo() {
        atualizarTempo();
    }

    // Método para atualizar o tempo do jogo
    private void atualizarTempo() {
        lblTime.setText(String.format("%02d:%02d", tempo / 60, tempo % 60));

        if (tempo <= 0) {
            encerrarJogo();
        } else {
            tempo++;
            agendarAtualizacaoTempo();
        }
    }

    // Método para encerrar o jogo
    private void encerrarJogo() {
        adicionarNovoJogadorEPontos();
        ocultarComponentes(false);
        som.pararComeco();
        som.somFim();
        partidaDAO.fecharConexao();
    }

    private void agendarAtualizacaoTempo() {
        new Thread(() -> {
            try {
                sleep(1000);
                atualizarTempo();
            } catch (InterruptedException e) {
            }
        }).start();
    }

    private void ocultarComponentes(boolean ocultar) {
        btnValidar.setVisible(ocultar);
        lblDifi.setVisible(ocultar);
        lbPontos.setVisible(ocultar);
        lblNome.setVisible(ocultar);
        lblTime.setVisible(ocultar);
        lb1.setVisible(ocultar);
        lb2.setVisible(ocultar);
        lb3.setVisible(ocultar);
        lb4.setVisible(ocultar);
        lb6.setVisible(ocultar);
        txtNumeroSorteado.setVisible(ocultar);
        tbbStop.setVisible(ocultar);
        tbbPlacar.setVisible(ocultar);
    }

    private int[] calcularResultados() {
        int resultado1 = numeroSorteado * 2;
        int resultado2 = numeroSorteado + 15;
        int resultado3 = numeroSorteado - 10;
        int resultado4 = numeroSorteado * 3;
        int resultado5 = numeroSorteado - 1;
        int resultado6 = numeroSorteado + 5;
        int resultado7 = numeroSorteado - 8;
        int resultado8 = numeroSorteado + 1;
        int resultado9 = numeroSorteado * 4;
        int total = resultado1 + resultado2 + resultado3 + resultado4 + resultado5 + resultado6 + resultado7 + resultado8 + resultado9;

        return new int[]{resultado1, resultado2, resultado3, resultado4, resultado5, resultado6, resultado7, resultado8, resultado9, total};
    }

    private void verificarMeta() {
        int[] resultado = calcularResultados();
        int ultimaLinha = modeloTabela.getRowCount() - 1;

        List<Integer> colunasIncorretas = new ArrayList<>();

        for (int j = 0; j < 10; j++) {
            Object valorCelulaObj = modeloTabela.getValueAt(ultimaLinha, j);
            int valorCelula = (valorCelulaObj != null) ? Integer.parseInt(valorCelulaObj.toString()) : 0;

            if (valorCelula != resultado[j]) {
                colunasIncorretas.add(j);
            }
        }

        if (!colunasIncorretas.isEmpty()) {
            limparColunasIncorretas(colunasIncorretas);
            mostrarMensagem(false, colunasIncorretas);
        } else {
            calcularPontos();
            mostrarMensagem(true, colunasIncorretas);
        }
    }

    private void limparColunasIncorretas(List<Integer> colunasIncorretas) {
        for (int coluna : colunasIncorretas) {
            for (int linha = 0; linha < modeloTabela.getRowCount(); linha++) {
                modeloTabela.setValueAt(0, linha, coluna);
            }
        }
    }

    private void mostrarMensagem(boolean metaAtingida, List<Integer> colunasIncorretas) {
        if (metaAtingida) {
            JOptionPane.showMessageDialog(null, "Você atingiu a meta de pontos! Uma nova linha foi adicionada.");
            adicionarNovoJogadorEPontos();
            partidaDAO.carregarPlacar();
            modeloTabela.addRow(new Object[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
            sortearNumero();
        } else {
            String mensagem = "Você não atingiu a meta de pontos!";
            adicionarNovoJogadorEPontos();
            partidaDAO.carregarPlacar();

            if (colunasIncorretas != null && !colunasIncorretas.isEmpty()) {
                mensagem += "\nColunas incorretas: " + colunasIncorretas.toString();
            }

            JOptionPane.showMessageDialog(null, mensagem);
        }
    }

    private void calcularPontos() {
        pontos += 1000;
        lbPontos.setText(String.valueOf(pontos));
    }

    public void adicionarNovoJogadorEPontos() {
        Partida jogo = new Partida(lblNome.getText(), pontos, lblDifi.getText(), lblTime.getText());
        partidaDAO.adicionarPontos(jogo);
        partidaDAO.carregarPlacar();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lb2 = new javax.swing.JLabel();
        tbbStop = new javax.swing.JScrollPane();
        tbStop = new javax.swing.JTable();
        lb4 = new javax.swing.JLabel();
        lblNome = new javax.swing.JLabel();
        lb3 = new javax.swing.JLabel();
        lblTime = new javax.swing.JLabel();
        btnEncerrar = new javax.swing.JButton();
        lb1 = new javax.swing.JLabel();
        lblDifi = new javax.swing.JLabel();
        lbPontos = new javax.swing.JLabel();
        lb6 = new javax.swing.JLabel();
        btnValidar = new javax.swing.JButton();
        tbbPlacar = new javax.swing.JScrollPane();
        tbPlacar = new javax.swing.JTable();
        txtNumeroSorteado = new javax.swing.JLabel();
        lbFundo = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lb2.setFont(new java.awt.Font("Gill Sans Ultra Bold", 0, 15)); // NOI18N
        lb2.setForeground(new java.awt.Color(255, 255, 255));
        lb2.setText("SORTEADO");
        getContentPane().add(lb2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, -1, 30));

        tbStop.setBackground(new java.awt.Color(92, 148, 252));
        tbStop.setFont(new java.awt.Font("Gill Sans Ultra Bold", 1, 10)); // NOI18N
        tbStop.setForeground(new java.awt.Color(255, 255, 255));
        tbStop.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "DOBRO", "+15", "-10", "*3", "Antessesor", "+5", "-8", "Sucessor", "*4", "Total"
            }
        ));
        tbStop.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbStopMouseClicked(evt);
            }
        });
        tbbStop.setViewportView(tbStop);

        getContentPane().add(tbbStop, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 200, 650, 190));

        lb4.setFont(new java.awt.Font("Gill Sans Ultra Bold", 0, 15)); // NOI18N
        lb4.setForeground(new java.awt.Color(255, 255, 255));
        lb4.setText("NOME:");
        getContentPane().add(lb4, new org.netbeans.lib.awtextra.AbsoluteConstraints(228, 70, 70, 30));

        lblNome.setFont(new java.awt.Font("Gill Sans Ultra Bold", 0, 15)); // NOI18N
        lblNome.setForeground(new java.awt.Color(255, 255, 255));
        getContentPane().add(lblNome, new org.netbeans.lib.awtextra.AbsoluteConstraints(318, 70, 90, 30));

        lb3.setFont(new java.awt.Font("Gill Sans Ultra Bold", 0, 15)); // NOI18N
        lb3.setForeground(new java.awt.Color(255, 255, 255));
        lb3.setText("TEMPO");
        getContentPane().add(lb3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, -1, 30));

        lblTime.setFont(new java.awt.Font("Gill Sans Ultra Bold", 0, 15)); // NOI18N
        lblTime.setForeground(new java.awt.Color(255, 255, 255));
        lblTime.setText("00:00");
        getContentPane().add(lblTime, new org.netbeans.lib.awtextra.AbsoluteConstraints(92, 140, 70, 30));

        btnEncerrar.setBackground(new java.awt.Color(207, 81, 12));
        btnEncerrar.setFont(new java.awt.Font("Gill Sans Ultra Bold", 1, 18)); // NOI18N
        btnEncerrar.setForeground(new java.awt.Color(255, 255, 255));
        btnEncerrar.setText("Encerrar");
        btnEncerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEncerrarActionPerformed(evt);
            }
        });
        getContentPane().add(btnEncerrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 440, 160, 50));

        lb1.setFont(new java.awt.Font("Gill Sans Ultra Bold", 0, 15)); // NOI18N
        lb1.setForeground(new java.awt.Color(255, 255, 255));
        lb1.setText("DIFICULDADE");
        getContentPane().add(lb1, new org.netbeans.lib.awtextra.AbsoluteConstraints(22, 32, -1, 30));

        lblDifi.setFont(new java.awt.Font("Gill Sans Ultra Bold", 0, 15)); // NOI18N
        lblDifi.setForeground(new java.awt.Color(255, 255, 255));
        getContentPane().add(lblDifi, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 30, 80, 30));

        lbPontos.setFont(new java.awt.Font("Gill Sans Ultra Bold", 0, 15)); // NOI18N
        lbPontos.setForeground(new java.awt.Color(255, 255, 255));
        getContentPane().add(lbPontos, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 10, 60, 20));

        lb6.setFont(new java.awt.Font("Gill Sans Ultra Bold", 0, 15)); // NOI18N
        lb6.setForeground(new java.awt.Color(255, 255, 255));
        lb6.setText("PONTOS:");
        getContentPane().add(lb6, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 10, 90, 20));

        btnValidar.setBackground(new java.awt.Color(92, 148, 252));
        btnValidar.setFont(new java.awt.Font("Gill Sans Ultra Bold", 1, 18)); // NOI18N
        btnValidar.setForeground(new java.awt.Color(255, 255, 255));
        btnValidar.setText("Validar");
        btnValidar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnValidarActionPerformed(evt);
            }
        });
        getContentPane().add(btnValidar, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 130, -1, 50));

        tbPlacar.setBackground(new java.awt.Color(92, 148, 252));
        tbPlacar.setFont(new java.awt.Font("Gill Sans Ultra Bold", 1, 10)); // NOI18N
        tbPlacar.setForeground(new java.awt.Color(255, 255, 255));
        tbPlacar.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Jogador", "Pontos", "Tempo"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbbPlacar.setViewportView(tbPlacar);

        getContentPane().add(tbbPlacar, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 30, 250, 160));

        txtNumeroSorteado.setFont(new java.awt.Font("Gill Sans Ultra Bold", 0, 15)); // NOI18N
        txtNumeroSorteado.setForeground(new java.awt.Color(255, 255, 255));
        getContentPane().add(txtNumeroSorteado, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 86, 70, 30));

        lbFundo.setFont(new java.awt.Font("Gill Sans Ultra Bold", 0, 15)); // NOI18N
        lbFundo.setForeground(new java.awt.Color(255, 255, 255));
        getContentPane().add(lbFundo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1100, 500));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnEncerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEncerrarActionPerformed
        // TODO add your handling code here:
        encerrarJogo();
    }//GEN-LAST:event_btnEncerrarActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened

        // Definir a imagem de ícone da aplicação
        this.setIconImage(new ImageIcon(getClass().getResource("/lib/img/Logo.png")).getImage());
        {
            ImageIcon imagem = new ImageIcon(TelaTabela.class.getResource("/lib/img/fundo.png"));
            Image imag = imagem.getImage().getScaledInstance(lbFundo.getWidth(), lbFundo.getHeight(), Image.SCALE_DEFAULT);
            lbFundo.setIcon(new ImageIcon(imag));
        }
        sortearNumero();
        iniciarTempo();
        // Sortear um número e iniciar o tempo
        modeloTabela.addRow(new Object[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
        som.somComeco();

    }//GEN-LAST:event_formWindowOpened

    private void btnValidarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnValidarActionPerformed
        verificarMeta();

    }//GEN-LAST:event_btnValidarActionPerformed

    private void tbStopMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbStopMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tbStopMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TelaTabela.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelaTabela.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelaTabela.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaTabela.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TelaTabela().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEncerrar;
    private javax.swing.JButton btnValidar;
    private javax.swing.JLabel lb1;
    private javax.swing.JLabel lb2;
    private javax.swing.JLabel lb3;
    private javax.swing.JLabel lb4;
    private javax.swing.JLabel lb6;
    private javax.swing.JLabel lbFundo;
    private javax.swing.JLabel lbPontos;
    public static javax.swing.JLabel lblDifi;
    public static javax.swing.JLabel lblNome;
    private javax.swing.JLabel lblTime;
    public static javax.swing.JTable tbPlacar;
    private javax.swing.JTable tbStop;
    private javax.swing.JScrollPane tbbPlacar;
    private javax.swing.JScrollPane tbbStop;
    private javax.swing.JLabel txtNumeroSorteado;
    // End of variables declaration//GEN-END:variables
}
