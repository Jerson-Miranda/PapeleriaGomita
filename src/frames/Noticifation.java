package frames;

import classes.Connections;
import classes.MainClass;
import java.awt.Color;
import java.sql.*;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class Noticifation extends javax.swing.JDialog {

    public Noticifation(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setTitle("Notificaciones");
        jLabel3.setBackground(Color.decode("#" + MainClass.systemColor));
        table.setSelectionBackground(Color.decode("#" + MainClass.systemColor));
        data();
    }

    private void data() {
        DefaultTableModel dtm = (DefaultTableModel) table.getModel();
        dtm.setRowCount(0);
        try {
            Connection cn = Connections.connect();
            PreparedStatement pst = cn.prepareStatement(""
                    + "SELECT nombre, stock, stockmax, stockmin "
                    + "FROM producto");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String name = rs.getString(1);
                double stock = rs.getInt(2);
                double stockMax = rs.getInt(3);
                double stockMin = rs.getInt(4);
                if(stock < stockMin) {
                    Vector v = new Vector();
                    v.add(0, false);
                    v.add(1,"EL PRODUCTO " + name + " ESTA APUNTO DE ACABARSE");
                    v.add(2, "CANT: " + stock);
                dtm.addRow(v);
                }
                if(stock > stockMax) {
                    Vector v = new Vector();
                    v.add(0, false);
                    v.add(1,"EL PRODUCTO " + name + " SUPERA EL STOCK MÁXIMO");
                    v.add(2, "CANT: " + stock);
                dtm.addRow(v);
                }
            }
            table.setModel(dtm);
            cn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "ERROR DE CÓDIGO\n\n"
                    + "Es posible que haya ocurrido un error en el código.\n"
                    + "Si el problema persiste, contactar con el desarrollador.\n", "ERROR X", JOptionPane.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        jTabbedPane1 = new javax.swing.JTabbedPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jPanel1MouseDragged(evt);
            }
        });
        jPanel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanel1MousePressed(evt);
            }
        });
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setBackground(new java.awt.Color(128, 128, 128));
        jLabel3.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Notificaciones del sistema");
        jLabel3.setOpaque(true);
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 760, 30));

        table.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "", "CONCEPTO", ""
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        table.setRowHeight(30);
        table.setSelectionBackground(new java.awt.Color(255, 255, 0));
        table.setShowVerticalLines(false);
        jScrollPane1.setViewportView(table);
        if (table.getColumnModel().getColumnCount() > 0) {
            table.getColumnModel().getColumn(0).setResizable(false);
            table.getColumnModel().getColumn(0).setPreferredWidth(50);
            table.getColumnModel().getColumn(1).setResizable(false);
            table.getColumnModel().getColumn(1).setPreferredWidth(598);
            table.getColumnModel().getColumn(2).setResizable(false);
            table.getColumnModel().getColumn(2).setPreferredWidth(110);
        }

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 760, 470));
        jPanel1.add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, 620, 420));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 800, 540));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jPanel1MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseDragged

    }//GEN-LAST:event_jPanel1MouseDragged

    private void jPanel1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MousePressed

    }//GEN-LAST:event_jPanel1MousePressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables
}
