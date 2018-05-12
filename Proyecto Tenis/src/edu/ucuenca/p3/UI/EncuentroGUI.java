/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ucuenca.p3.UI;

import edu.ucuenca.p3.Modulos.Encuentro;
import edu.ucuenca.p3.Modulos.Equipo;
import edu.ucuenca.p3.Modulos.Inscripcion;
import edu.ucuenca.p3.Modulos.Jugador;
import edu.ucuenca.p3.Modulos.Participante;
import edu.ucuenca.p3.Modulos.Torneo;
import edu.ucuenca.p3.Modulos.Torneo_Etapa;
import edu.ucuenca.p3.SRV.EncuentroSRV;
import edu.ucuenca.p3.SRV.TorneoSRV;
import edu.ucuenca.p3.SRV.exceptions.EncuentroExcepcion;
import edu.ucuenca.p3.SRV.exceptions.EtapaFinalizadaException;
import edu.ucuenca.p3.SRV.exceptions.TorneoFinalizadoException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author andre
 */
public class EncuentroGUI extends javax.swing.JPanel {

    public JScrollPane jspContenedor;
    /**
     * Creates new form EncuentroGUI
     */
    int contador_etapas = 1;
    int contador_encuentros = 1;

    List<Encuentro> lista_encuentros = null;
    List<Participante> lista_participante = null;
    List<Participante> lista_ganadores = null;
    List<Torneo_Etapa> lista_torneo_etapa = null;

    public EncuentroGUI(String codigo_torneo, JScrollPane jspContenedor2) {
        try {
            initComponents();
            jTextField_Puntaje1.setEnabled(false);
            jTextField_Puntaje2.setEnabled(false);//bloquea los textbox de los jugadores
            
            jspContenedor = jspContenedor2;
            jspContenedor.setLayout(null);//centra el jPanel

            TorneoSRV torneoSrv = new TorneoSRV();
            EncuentroSRV encuentroSrv = new EncuentroSRV();

//            Torneo torneo = torneoSrv.obtenerTorneo(codigo_torneo);
            Torneo torneo = torneoSrv.obtenerTorneoArchivos(codigo_torneo);
            jLabel_Codigo_Torneo.setText(torneo.getCodigo());
            jLabel_Nombre_Categoria.setText(torneo.getTorneo_categorías().getCategoria().getNombre());
            jLabel_Nombre_Etapa.setText(String.valueOf(contador_etapas));
            jLabel_Modalidad_torneo.setText(torneo.getTipo_juego());

            //implementeado
            lista_torneo_etapa = new ArrayList<>();
            lista_participante = new ArrayList<>();
            lista_ganadores = new ArrayList<>();
            lista_encuentros = new LinkedList<>();

            String nombre_categoria = jLabel_Nombre_Categoria.getText();
            String etapa = jLabel_Nombre_Etapa.getText();
            String modalidad = jLabel_Modalidad_torneo.getText();

            List<Inscripcion> listaInscritos = torneo.getTorneo_categorías().getInscripciones();
            if (encuentroSrv.validarListaInscritos(listaInscritos)) {
                JOptionPane.showMessageDialog(null, "No existen inscritos en el torneo !");
                this.hide();
            } else {
                if (encuentroSrv.validarEtapa(codigo_torneo)) {
                    lista_encuentros = encuentroSrv.encuentrosEtapa(codigo_torneo);
                    System.out.println("Aqui llega si no está vacía");
                } else {
                    lista_encuentros = encuentroSrv.lista_Encuentros(codigo_torneo, nombre_categoria, etapa, modalidad);
                    System.out.println("Aqui llega si está vacía la lista de encuentros");                    
                }

                limpiar_Tabla(jTable_Lista_Encuentros);
                tabla_Encuentros(lista_encuentros);
            }
        } catch (TorneoFinalizadoException ex) {
            Logger.getLogger(EncuentroGUI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (EncuentroExcepcion ex) {
            Logger.getLogger(EncuentroGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void tabla_Encuentros(List<Encuentro> lista_encuentro) {

        String modalidad = jLabel_Modalidad_torneo.getText();
        DefaultTableModel modelo = (DefaultTableModel) jTable_Lista_Encuentros.getModel();
        Object[] fila = new Object[modelo.getColumnCount()];
        if (modalidad.equalsIgnoreCase("Simple")) {
            for (int i = 0; i < lista_encuentro.size(); i++) {
                Participante participante1 = lista_encuentro.get(i).getParticipante_1();
                Participante participante2 = lista_encuentro.get(i).getParticipante_2();
                Jugador jugador1 = (Jugador) participante1;
                Jugador jugador2 = (Jugador) participante2;

//                fila[0] = jLabel_Nombre_Categoria.getText();//
                fila[0] = lista_encuentro.get(i).getCodigo();
                fila[1] = jugador1.getNombre() + " " + jugador1.getApellido();//
                fila[2] = "vs";//
                fila[3] = jugador2.getNombre() + " " + jugador2.getApellido();//
                modelo.addRow(fila);

            }
        } else {
            for (int i = 0; i < lista_encuentro.size(); i++) {
                Participante participante1 = lista_encuentro.get(i).getParticipante_1();
                Participante participante2 = lista_encuentro.get(i).getParticipante_2();
                Equipo equipo1 = (Equipo) participante1;
                Equipo equipo2 = (Equipo) participante2;

//                fila[0] = jLabel_Nombre_Categoria.getText();//
                fila[0] = lista_encuentro.get(i).getCodigo();
                fila[1] = equipo1.getCodigo();//
                fila[2] = "vs";//
                fila[3] = equipo2.getCodigo();//
                modelo.addRow(fila);
            }
        }
    }

    private void limpiar_Tabla(JTable jtableTorneos) {
        DefaultTableModel modeloInscritos = (DefaultTableModel) jtableTorneos.getModel();
        for (int i = 0; i < jtableTorneos.getRowCount(); i++) {
            modeloInscritos.removeRow(i);
            i -= 1;
        }
    }

    public void cargarCampos(List<Encuentro> lista_encuentro, String modalidad, int contador) {

        Participante participante1 = lista_encuentro.get(contador).getParticipante_1();
        Participante participante2 = lista_encuentro.get(contador).getParticipante_2();
        if (modalidad.equalsIgnoreCase("simple")) {
            Jugador jugador1 = (Jugador) participante1;
            Jugador jugador2 = (Jugador) participante2;
            jTextField_Nombre_Jugador_1.setText(jugador1.getNombre() + " " + jugador1.getApellido());
            jTextField_Nombre_Jugador2.setText(jugador2.getNombre() + " " + jugador2.getApellido());
        } else {
            Equipo equipo1 = (Equipo) participante1;
            Equipo equipo2 = (Equipo) participante2;
            jTextField_Nombre_Jugador_1.setText(equipo1.getJugador1().getApellido() + " - " + equipo1.getJugador2().getApellido());
            jTextField_Nombre_Jugador2.setText(equipo2.getJugador1().getApellido() + " - " + equipo2.getJugador2().getApellido());
        }

    }

    public void limpiarCampos() {
        jTextField_Nombre_Jugador_1.setText("");
        jTextField_Nombre_Jugador2.setText("");
        jTextField_Puntaje1.setText("");
        jTextField_Puntaje2.setText("");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable_Lista_Encuentros = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel_Codigo_Torneo = new javax.swing.JLabel();
        jLabel_Nombre_Categoria = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel_Nombre_Etapa = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jTextField_Nombre_Jugador_1 = new javax.swing.JTextField();
        jTextField_Nombre_Jugador2 = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jTextField_Puntaje1 = new javax.swing.JTextField();
        jTextField_Puntaje2 = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jButton_Ingresar_Resultado_Set = new javax.swing.JButton();
        jButton_Ingresar_Falta = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel11 = new javax.swing.JLabel();
        jLabel_Modalidad_torneo = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        setMaximumSize(new java.awt.Dimension(957, 563));
        setMinimumSize(new java.awt.Dimension(957, 563));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Lista de Encuentros"));

        jTable_Lista_Encuentros.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "# Encuentro", "Participante 1", "vs", "Participante 2"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable_Lista_Encuentros.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable_Lista_EncuentrosMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTable_Lista_EncuentrosMousePressed(evt);
            }
        });
        jScrollPane2.setViewportView(jTable_Lista_Encuentros);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 470, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 430, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 120, 500, -1));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Torneo:");
        add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 50, 107, 33));

        jLabel_Codigo_Torneo.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel_Codigo_Torneo.setForeground(new java.awt.Color(255, 255, 255));
        jLabel_Codigo_Torneo.setText("Código:");
        add(jLabel_Codigo_Torneo, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 50, 79, 33));

        jLabel_Nombre_Categoria.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel_Nombre_Categoria.setForeground(new java.awt.Color(255, 255, 255));
        jLabel_Nombre_Categoria.setText("Categoría");
        add(jLabel_Nombre_Categoria, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 50, 105, 33));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Etapa:");
        add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 50, 95, 33));

        jLabel_Nombre_Etapa.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel_Nombre_Etapa.setForeground(new java.awt.Color(255, 255, 255));
        jLabel_Nombre_Etapa.setText("1");
        add(jLabel_Nombre_Etapa, new org.netbeans.lib.awtextra.AbsoluteConstraints(1070, 50, 72, 33));

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Encuentro"));

        jLabel7.setText("Participante 1");

        jLabel8.setText("Participante 2");

        jTextField_Nombre_Jugador_1.setEditable(false);

        jTextField_Nombre_Jugador2.setEditable(false);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Set"));

        jLabel9.setText("Puntaje ");

        jLabel10.setText("Puntaje ");

        jButton_Ingresar_Resultado_Set.setText("Ingresar Resultado de Set");
        jButton_Ingresar_Resultado_Set.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_Ingresar_Resultado_SetActionPerformed(evt);
            }
        });

        jButton_Ingresar_Falta.setText("Ingresar Falta");
        jButton_Ingresar_Falta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_Ingresar_FaltaActionPerformed(evt);
            }
        });

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "No se presenta", "Abandona" }));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jButton_Ingresar_Falta, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 157, Short.MAX_VALUE)
                                .addComponent(jButton_Ingresar_Resultado_Set))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE)
                                    .addComponent(jTextField_Puntaje1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jTextField_Puntaje2, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(23, 23, 23))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField_Puntaje1, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField_Puntaje2, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 48, Short.MAX_VALUE)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton_Ingresar_Resultado_Set, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_Ingresar_Falta, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34))
        );

        jLabel11.setFont(new java.awt.Font("Tahoma", 3, 12)); // NOI18N
        jLabel11.setText("VS");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(329, 329, 329)
                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jTextField_Nombre_Jugador_1, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(94, 94, 94)
                                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(95, 95, 95)
                                .addComponent(jTextField_Nombre_Jugador2, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(39, 39, 39)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField_Nombre_Jugador_1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel11))
                    .addComponent(jTextField_Nombre_Jugador2, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(43, 43, 43)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 120, -1, -1));

        jLabel_Modalidad_torneo.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel_Modalidad_torneo.setForeground(new java.awt.Color(255, 255, 255));
        jLabel_Modalidad_torneo.setText("Modalidad:");
        add(jLabel_Modalidad_torneo, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 50, 133, 33));
        add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ucuenca/p3/Iconos/fondo8.jpg"))); // NOI18N
        add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1190, 660));
    }// </editor-fold>//GEN-END:initComponents

    private void jButton_Ingresar_Resultado_SetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_Ingresar_Resultado_SetActionPerformed
        EncuentroSRV encuentroSrv = new EncuentroSRV();
        String codigo_torneo = jLabel_Codigo_Torneo.getText();
        String nombre_categoria = jLabel_Nombre_Categoria.getText();
        String modalidad = jLabel_Modalidad_torneo.getText();
        String sede = "Cuenca";
        String nombre_jugador1 = jTextField_Nombre_Jugador_1.getText();
        String nombre_jugador2 = jTextField_Nombre_Jugador2.getText();
        int puntaje1;
        int puntaje2;
        String p1 = jTextField_Puntaje1.getText();
        String p2 = jTextField_Puntaje2.getText();

        int numero_encuentro = jTable_Lista_Encuentros.getSelectedRow();
        String etapa = String.valueOf(contador_etapas);

        try {
            if (!p1.equals("") && !p2.equals("")) {
                puntaje1 = Integer.parseInt(p1);
                puntaje2 = Integer.parseInt(p2);
                if ((puntaje1 == 0 && puntaje2 == 2) || (puntaje1 == 1 && puntaje2 == 2) || (puntaje1 == 2 && puntaje2 == 0) || (puntaje1 == 2 && puntaje2 == 1)) {
                    if (numero_encuentro > -1) {
                        encuentroSrv.jugarEncuentro(codigo_torneo, numero_encuentro, 
                                nombre_jugador1, nombre_jugador2, puntaje1, puntaje2);
                        
                        if(encuentroSrv.encuentrosJugados(codigo_torneo)){
                            contador_etapas++;
                            etapa = String.valueOf(contador_etapas);
                            lista_encuentros = encuentroSrv.lista_Encuentros(codigo_torneo, nombre_categoria, etapa, modalidad);
                            JOptionPane.showMessageDialog(null, "Etapa terminada !", "Información sobre el torneo", JOptionPane.INFORMATION_MESSAGE);

                            limpiar_Tabla(jTable_Lista_Encuentros);
                            tabla_Encuentros(lista_encuentros);
                            limpiarCampos();

                        }

                    } else {
                        JOptionPane.showMessageDialog(null, "Elija un Encuentro a jugar por favor!", "Atención", JOptionPane.WARNING_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Por regla los puntajes deben ser iguales a:\n"
                            + " 2 - 0\n"
                            + " 2 - 1\n"
                            + " 0 - 2\n"
                            + " 1 - 2");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Ingrese los puntajes para ambos jugadores.");
            }
            limpiarCampos();
        } catch (EncuentroExcepcion ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        } catch (TorneoFinalizadoException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Ingrese los puntajes para ambos jugadores.");
        }
    }//GEN-LAST:event_jButton_Ingresar_Resultado_SetActionPerformed

    private void jButton_Ingresar_FaltaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_Ingresar_FaltaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton_Ingresar_FaltaActionPerformed

    private void jTable_Lista_EncuentrosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable_Lista_EncuentrosMouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_jTable_Lista_EncuentrosMouseClicked

    private void jTable_Lista_EncuentrosMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable_Lista_EncuentrosMousePressed
        // TODO add your handling code here:
        jTextField_Puntaje1.setEnabled(true);
        jTextField_Puntaje2.setEnabled(true);
        if (evt.getClickCount() > 1) {
            String modalidad = jLabel_Modalidad_torneo.getText();
            int contador = jTable_Lista_Encuentros.getSelectedRow();
            cargarCampos(lista_encuentros, modalidad, contador);
        }
    }//GEN-LAST:event_jTable_Lista_EncuentrosMousePressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton_Ingresar_Falta;
    private javax.swing.JButton jButton_Ingresar_Resultado_Set;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel_Codigo_Torneo;
    private javax.swing.JLabel jLabel_Modalidad_torneo;
    private javax.swing.JLabel jLabel_Nombre_Categoria;
    private javax.swing.JLabel jLabel_Nombre_Etapa;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable_Lista_Encuentros;
    private javax.swing.JTextField jTextField_Nombre_Jugador2;
    private javax.swing.JTextField jTextField_Nombre_Jugador_1;
    private javax.swing.JTextField jTextField_Puntaje1;
    private javax.swing.JTextField jTextField_Puntaje2;
    // End of variables declaration//GEN-END:variables
}
