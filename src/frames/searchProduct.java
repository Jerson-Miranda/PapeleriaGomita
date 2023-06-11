package frames;

import classes.Connections;
import classes.MainClass;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class searchProduct extends java.awt.Dialog {

    int moveX, moveY;
    JTextField codeProduct;

    public searchProduct(java.awt.Frame parent, boolean modal, JTextField code) {
        super(parent, modal);
        this.codeProduct = code;
        initComponents();
        viewProduct(tProduct);
        mouseClickProduct();
        tProduct.setSelectionBackground(Color.decode("#" + MainClass.systemColor));
        jLabel3.setBackground(Color.decode("#" + MainClass.systemColor));
        setTitle("Busqueda de productos");
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bgSearch = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tProduct = new javax.swing.JTable();
        rbName = new javax.swing.JRadioButton();
        rbMark = new javax.swing.JRadioButton();
        txtSearch = new javax.swing.JTextField();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

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
        jLabel3.setText("Busquéda de productos registrados");
        jLabel3.setOpaque(true);
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 620, 30));
        jPanel1.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 620, 10));

        jLabel8.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/search_30px.png"))); // NOI18N
        jLabel8.setText("Buscar:");
        jLabel8.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 90, 30));

        tProduct.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        tProduct.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "CÓDIGO", "NOMBRE", "MARCA"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tProduct.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tProduct.setRowHeight(24);
        tProduct.setSelectionBackground(new java.awt.Color(255, 255, 0));
        jScrollPane1.setViewportView(tProduct);
        if (tProduct.getColumnModel().getColumnCount() > 0) {
            tProduct.getColumnModel().getColumn(0).setResizable(false);
            tProduct.getColumnModel().getColumn(0).setPreferredWidth(120);
            tProduct.getColumnModel().getColumn(1).setResizable(false);
            tProduct.getColumnModel().getColumn(1).setPreferredWidth(270);
            tProduct.getColumnModel().getColumn(2).setResizable(false);
            tProduct.getColumnModel().getColumn(2).setPreferredWidth(224);
        }

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, 620, 420));

        rbName.setBackground(new java.awt.Color(255, 255, 255));
        bgSearch.add(rbName);
        rbName.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        rbName.setSelected(true);
        rbName.setText("Nombre");
        jPanel1.add(rbName, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 50, 80, 30));

        rbMark.setBackground(new java.awt.Color(255, 255, 255));
        bgSearch.add(rbMark);
        rbMark.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        rbMark.setText("Marca");
        jPanel1.add(rbMark, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 50, 80, 30));

        txtSearch.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtSearch.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSearchKeyTyped(evt);
            }
        });
        jPanel1.add(txtSearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 50, 250, 30));

        add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 670, 540));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        setVisible(false);
        dispose();
    }//GEN-LAST:event_closeDialog

    private void jPanel1MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseDragged
        int x = evt.getXOnScreen();
        int y = evt.getYOnScreen();
        this.setLocation(x - moveX, y - moveY);
    }//GEN-LAST:event_jPanel1MouseDragged

    private void jPanel1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MousePressed
        moveX = evt.getX();
        moveY = evt.getY();
    }//GEN-LAST:event_jPanel1MousePressed

    private void txtSearchKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyTyped
        char c = evt.getKeyChar();
        if (Character.isLowerCase(c)) {
            evt.setKeyChar(Character.toUpperCase(c));
        }
    }//GEN-LAST:event_txtSearchKeyTyped

    private void txtSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyReleased
        if (txtSearch.getText().length() >= 40) {
            evt.consume();
        } else {
            searchProduct();
        }
    }//GEN-LAST:event_txtSearchKeyReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup bgSearch;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JRadioButton rbMark;
    private javax.swing.JRadioButton rbName;
    private javax.swing.JTable tProduct;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables

    public void viewProduct(JTable table) {
        DefaultTableModel dtm = (DefaultTableModel) table.getModel();
        dtm.setRowCount(0);
        try {
            Connection cn = Connections.connect();
            PreparedStatement pst = cn.prepareStatement("select * from producto order by marca");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getString(1));
                v.add(rs.getString(2));
                v.add(rs.getString(3));
                dtm.addRow(v);
            }
            table.setModel(dtm);
            cn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "OCURRIÓ UN ERROR DE CONSULTA\n\n"
                    + "Error al consultar los datos de los productos.", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void mouseClickProduct() {
        DefaultTableModel dtm = (DefaultTableModel) tProduct.getModel();
        tProduct.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila_point = tProduct.rowAtPoint(e.getPoint());
                if (fila_point > -1) {
                    String c = dtm.getValueAt(fila_point, 0).toString();
                    codeProduct.setText(c);
                    setVisible(false);
                    dispose();
                }
            }
        });
    }

    public void searchProduct() {
        int selected = 1;
        if (rbName.isSelected()) {
            selected = 1;
        } else if (rbMark.isSelected()) {
            selected = 2;
        }
        DefaultTableModel dtm = (DefaultTableModel) tProduct.getModel();
        dtm.setRowCount(0);
        try {
            Connection cn = Connections.connect();
            Statement declare = cn.createStatement();
            if (selected == 1) {
                ResultSet rs = declare.executeQuery("select * from producto where nombre LIKE '%" + txtSearch.getText() + "%'");
                while (rs.next()) {
                    Vector v = new Vector();
                    v.add(rs.getString(1));
                    v.add(rs.getString(2));
                    v.add(rs.getString(3));
                    dtm.addRow(v);
                }
                tProduct.setModel(dtm);
            } else if (selected == 2) {
                ResultSet rs = declare.executeQuery("select * from producto where marca LIKE '%" + txtSearch.getText() + "%'");
                while (rs.next()) {
                    Vector v = new Vector();
                    v.add(rs.getString(1));
                    v.add(rs.getString(2));
                    v.add(rs.getString(3));
                    dtm.addRow(v);
                }
                tProduct.setModel(dtm);
            } else if (txtSearch.getText().equals("")) {
                ResultSet rs = declare.executeQuery("select * from producto");
                while (rs.next()) {
                    Vector v = new Vector();
                    v.add(rs.getString(1));
                    v.add(rs.getString(2));
                    v.add(rs.getString(5));
                    v.add(rs.getString(7));
                    v.add(rs.getString(9));
                    v.add(rs.getString(3));
                    v.add(rs.getString(4));
                    dtm.addRow(v);
                }
                tProduct.setModel(dtm);
            }
            cn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "OCURRIÓ UN ERROR DE CONSULTA\n\n"
                    + "Error al consultar los datos del producto", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }
}
