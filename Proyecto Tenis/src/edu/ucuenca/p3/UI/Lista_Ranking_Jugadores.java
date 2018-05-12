/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ucuenca.p3.UI;

import edu.ucuenca.p3.Modulos.MezclaDirecta;
import edu.ucuenca.p3.Modulos.Jugador;
import edu.ucuenca.p3.Modulos.Participante;
import edu.ucuenca.p3.SRV.ParticipantesSRV;
import edu.ucuenca.p3.SRV.ServicioIntercalacion;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.util.HashSet;

/**
 *
 * @author andre
 */
public class Lista_Ranking_Jugadores extends javax.swing.JPanel {

    DefaultTableModel modeloTabla;
    public static ArrayList<Participante> arrayUsuarios;

    /**
     * Creates new form Lista_Ranking_Jugadores
     */
    public Lista_Ranking_Jugadores() {
        initComponents();
        //cargarRankings();
        //clear_Table(jTable_Lista_Ranking);
        tabla_jugadores_Archivos();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable_Lista_Ranking = new javax.swing.JTable();
        jLabel12 = new javax.swing.JLabel();
        cbxEnOrden = new javax.swing.JComboBox();
        jLabel11 = new javax.swing.JLabel();
        cbxOrdenarPor = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();

        setLayout(null);

        jLabel1.setFont(new java.awt.Font("Impact", 0, 30)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Lista de Ranking de Jugadores");
        add(jLabel1);
        jLabel1.setBounds(560, 40, 390, 25);

        jTable_Lista_Ranking.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Nombre", "Apellido", "Edad", "Ranking"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable_Lista_Ranking);

        add(jScrollPane1);
        jScrollPane1.setBounds(410, 200, 687, 360);

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel12.setText("EN ORDEN:");
        add(jLabel12);
        jLabel12.setBounds(810, 120, 82, 29);

        cbxEnOrden.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ascendente", "Descendente" }));
        cbxEnOrden.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxEnOrdenActionPerformed(evt);
            }
        });
        add(cbxEnOrden);
        cbxEnOrden.setBounds(940, 120, 162, 29);

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel11.setText("ORDENAR POR:");
        add(jLabel11);
        jLabel11.setBounds(410, 120, 121, 30);

        cbxOrdenarPor.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Apellido", "Ranking" }));
        cbxOrdenarPor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxOrdenarPorActionPerformed(evt);
            }
        });
        add(cbxOrdenarPor);
        cbxOrdenarPor.setBounds(550, 120, 140, 30);

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ucuenca/p3/Iconos/fondo3.jpg"))); // NOI18N
        jLabel2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        add(jLabel2);
        jLabel2.setBounds(0, 0, 1190, 656);
    }// </editor-fold>//GEN-END:initComponents

    private void cbxEnOrdenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxEnOrdenActionPerformed
        // TODO add your handling code here:
        String ordenarPor = cbxOrdenarPor.getSelectedItem().toString();
        String enOrden = cbxEnOrden.getSelectedItem().toString();
        
        ParticipantesSRV p = new ParticipantesSRV();
        ArrayList<Participante> listaSinDuplicarOrdenada = p.getListaParticipantesOrdenadaArchivos(ordenarPor, enOrden);
        
        modeloTabla = (DefaultTableModel) jTable_Lista_Ranking.getModel();
        int tamJtable = modeloTabla.getRowCount();
        for (int j = 0; j < tamJtable; j++) {
            modeloTabla.removeRow(0);
        }
        jTable_Lista_Ranking.setModel(modeloTabla);
        //this.iniciar();
        cargarRankings(listaSinDuplicarOrdenada, enOrden);
    }//GEN-LAST:event_cbxEnOrdenActionPerformed

    private void cbxOrdenarPorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxOrdenarPorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxOrdenarPorActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cbxEnOrden;
    private javax.swing.JComboBox cbxOrdenarPor;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable_Lista_Ranking;
    // End of variables declaration//GEN-END:variables

    private void cargarRankings(ArrayList<Participante> listaParticipantes, String enOrden) {
        DefaultTableModel modeloVigia = (DefaultTableModel) jTable_Lista_Ranking.getModel();

        Object[] fila = new Object[modeloVigia.getColumnCount()];
        if (enOrden.equalsIgnoreCase("Ascendente")) {
            for (int i = 0; i < listaParticipantes.size(); i++) {
                Jugador jugador = (Jugador) listaParticipantes.get(i);

                fila[0] = jugador.getCodigo();
                fila[1] = jugador.getNombre().toUpperCase();
                fila[2] = jugador.getApellido().toUpperCase();
                fila[3] = jugador.getEdad();
                fila[4] = jugador.getRanking().getPuntos();

                modeloVigia.addRow(fila);
            }
        } else if (enOrden.equalsIgnoreCase("Descendente")) {
            for (int i = listaParticipantes.size() - 1; i >= 0; i--) {
                Jugador jugador = (Jugador) listaParticipantes.get(i);

                fila[0] = jugador.getCodigo();
                fila[1] = jugador.getNombre().toUpperCase();
                fila[2] = jugador.getApellido().toUpperCase();
                fila[3] = jugador.getEdad();
                fila[4] = jugador.getRanking().getPuntos();

                modeloVigia.addRow(fila);
            }
        }
    }

    public void iniciar() {
        ParticipantesSRV participanteService = new ParticipantesSRV();

        arrayUsuarios = participanteService.listarParticpantesArchivos();
        for (Participante o : arrayUsuarios) {
            Jugador j = (Jugador) o;
            Object[] datosFila
                    = {
                        j.getCodigo(),
                        j.getNombre(),
                        j.getApellido(),
                        j.getEdad(),
                        j.getRanking().getPuntos()
                    };
            modeloTabla = (DefaultTableModel) jTable_Lista_Ranking.getModel();
            modeloTabla.addRow(datosFila);
            jTable_Lista_Ranking.setModel(modeloTabla);
        }
    }

    private void tabla_jugadores_Archivos() {
        DefaultTableModel modeloVigia = (DefaultTableModel) jTable_Lista_Ranking.getModel();
        ParticipantesSRV participantesrv = new ParticipantesSRV();
//        List<Participante> listaParticipantes = participantesrv.listaParticipantes();

        ArrayList<Participante> listaParticipantes = participantesrv.listarParticpantesArchivos();
        Object[] fila = new Object[modeloVigia.getColumnCount()];
        for (int i = 0; i < listaParticipantes.size(); i++) {
            Jugador jugador = (Jugador) listaParticipantes.get(i);

            fila[0] = jugador.getCodigo();
            fila[1] = jugador.getNombre().toUpperCase();
            fila[2] = jugador.getApellido().toUpperCase();
            fila[3] = jugador.getEdad();
            fila[4] = jugador.getRanking().getPuntos();//error aqui

            modeloVigia.addRow(fila);
        }
    }

    private void clear_Table(JTable jTableVigilante) {
        DefaultTableModel modeloVigia = (DefaultTableModel) jTableVigilante.getModel();
        for (int i = 0; i < jTableVigilante.getRowCount(); i++) {
            modeloVigia.removeRow(i);
            i -= 1;
        }
    }
    
    
}