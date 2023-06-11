package frames;

import classes.Connections;
import classes.MainClass;
import java.awt.Color;
import javax.swing.JOptionPane;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class DetailsSale extends javax.swing.JDialog {

    boolean banNext = true;
    JButton x;
    JTable table;
    SimpleDateFormat format = new SimpleDateFormat("dd-MM-YY");
    DecimalFormat df = new DecimalFormat("#.00");
    String noArt, subTotal, dto, dtoG, total, customer, time, idUser;

    public DetailsSale(java.awt.Frame parent, boolean modal, String noArt, String subTotal, String dto, String dtoG,
            String total, String customer, String time, String idUser, JButton x, JTable table) {
        super(parent, modal);
        initComponents();
        this.noArt = noArt;
        this.subTotal = subTotal;
        this.dto = dto;
        this.dtoG = dtoG;
        this.total = total;
        this.customer = customer;
        this.time = time;
        this.idUser = idUser;
        this.x = x;
        this.table = table;
        data();
        setTitle("Detalles de la venta");
    }

    private void data() {
        jButton3.setBackground(Color.decode("#" + MainClass.systemColor));
        btnPay.setBackground(Color.decode("#" + MainClass.systemColor));
        lbCustomer.setForeground(Color.decode("#" + MainClass.systemColor));
        txtNoProduct.setText(noArt);
        txtSubtotal.setText("$" + subTotal);
        txtDiscount.setText("$" + dto);
        txtDiscountG.setText("$" + dtoG);
        txtTotal.setText("$" + total);
        lbCustomer.setText(customer);
    }

    public int week() {
        GregorianCalendar time = new GregorianCalendar();
        int x = time.get(Calendar.WEEK_OF_YEAR);
        return x;
    }

    public String date() {
        java.util.Date date = new java.util.Date();
        return format.format(date);
    }

    private int noSale() {
        int x = 0;
        try {
            try ( Connection cn = Connections.connect()) {
                PreparedStatement pst = cn.prepareStatement(""
                        + "SELECT noVenta "
                        + "FROM venta "
                        + "WHERE noVenta = (SELECT MAX(noVenta) FROM venta)");
                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    x = rs.getInt(1);
                }
                cn.close();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "ERROR DE CÓDIGO\n\n"
                    + "Es posible que haya ocurrido un error en el código.\n"
                    + "Si el problema persiste, contactar con el desarrollador.\n", "ERROR X", JOptionPane.ERROR_MESSAGE);
        }
        return x;
    }

    String one, two, three, four, five;

    private void replace() {
        one = txtSubtotal.getText();
        one = one.replace("$", "");
        two = txtDiscount.getText();
        two = two.replace("$", "");
        three = txtDiscountG.getText();
        three = three.replace("$", "");
        four = txtTotal.getText();
        four = four.replace("$", "");
        five = txtChange.getText();
        five = five.replace("$", "");
    }

    private void insertSale() {
        replace();
        try {
            Connection cn = Connections.connect();
            PreparedStatement pst = cn.prepareStatement("INSERT INTO venta VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            pst.setInt(1, 0);
            pst.setInt(2, week());
            pst.setString(3, date());
            pst.setString(4, time);
            pst.setString(5, customer);
            pst.setString(6, idUser);
            pst.setInt(7, Integer.parseInt(txtNoProduct.getText()));
            pst.setFloat(8, Float.parseFloat(one));
            pst.setFloat(9, Float.parseFloat(two));
            pst.setFloat(10, Float.parseFloat(three));
            pst.setFloat(11, Float.parseFloat(four));
            pst.setFloat(12, Float.parseFloat(txtReceived.getText().trim()));
            pst.setFloat(13, Float.parseFloat(five));
            pst.executeUpdate();
            cn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "ERROR DE CÓDIGO\n\n"
                    + "Es posible que haya ocurrido un error en el código.\n"
                    + "Si el problema persiste, contactar con el desarrollador.\n" + e, "ERROR X", JOptionPane.ERROR_MESSAGE);
            banNext = false;
        }
    }

    private void insertDetailSale() {
        DefaultTableModel dtm = (DefaultTableModel) table.getModel();
        for (int i = 0; i < dtm.getRowCount(); i++) {
            int cant = Integer.parseInt(table.getValueAt(i, 0).toString());
            String cod = table.getValueAt(i, 1).toString();
            String name = table.getValueAt(i, 2).toString();
            String mark = table.getValueAt(i, 3).toString();
            String price = table.getValueAt(i, 4).toString();
            String discountP = table.getValueAt(i, 5).toString();
            String fina = table.getValueAt(i, 6).toString();
            try {
                Connection cn = Connections.connect();
                PreparedStatement pst = cn.prepareStatement("INSERT INTO detalleVenta VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
                pst.setInt(1, 0);
                pst.setInt(2, noSale());
                pst.setInt(3, cant);
                pst.setString(4, cod);
                pst.setString(5, name);
                pst.setString(6, mark);
                pst.setFloat(7, Float.parseFloat(price));
                pst.setFloat(8, Float.parseFloat(discountP));
                pst.setFloat(9, Float.parseFloat(fina));
                pst.executeUpdate();
                cn.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "ERROR DE CÓDIGO\n\n"
                        + "Es posible que haya ocurrido un error en el código.\n"
                        + "Si el problema persiste, contactar con el desarrollador.\n" + e, "ERROR X", JOptionPane.ERROR_MESSAGE);
            }
            try {
                Connection cn = Connections.connect();
                PreparedStatement pst = cn.prepareStatement("select stock from producto where codigoBarra ='" + cod + "'");
                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    int cantN = rs.getInt(1) - cant;
                    try {
                        PreparedStatement pss = Connections.connect().prepareStatement("update producto set stock = '" + cantN
                                + "' where codigoBarra ='" + cod + "'");
                        pss.executeUpdate();
                    } catch (SQLException e) {
                        JOptionPane.showMessageDialog(null, "ERROR DE CÓDIGO\n\n"
                                + "Es posible que haya ocurrido un error en el código.\n"
                                + "Si el problema persiste, contactar con el desarrollador.\n" + e, "ERROR X", JOptionPane.ERROR_MESSAGE);
                    }
                }
                cn.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "ERROR DE CÓDIGO\n\n"
                        + "Es posible que haya ocurrido un error en el código.\n"
                        + "Si el problema persiste, contactar con el desarrollador.\n" + e, "ERROR X", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        lbCustomer = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtChange = new javax.swing.JTextField();
        txtSubtotal = new javax.swing.JTextField();
        txtDiscount = new javax.swing.JTextField();
        txtDiscountG = new javax.swing.JTextField();
        txtTotal = new javax.swing.JTextField();
        txtReceived = new javax.swing.JTextField();
        btnPay = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        txtNoProduct = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbCustomer.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lbCustomer.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbCustomer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/user_24px.png"))); // NOI18N
        jPanel1.add(lbCustomer, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 300, 30));

        jLabel2.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Subtotal:");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 70, 110, 30));

        jLabel3.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Descuento:");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 110, 110, 30));

        jLabel4.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Dto. General:");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 150, 110, 30));

        jLabel5.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Total a pagar");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 210, 300, 20));

        jLabel6.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Pago con:");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 310, 90, 30));

        jLabel7.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Cambio:");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 350, 70, 30));

        txtChange.setEditable(false);
        txtChange.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtChange.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jPanel1.add(txtChange, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 350, 100, 30));

        txtSubtotal.setEditable(false);
        txtSubtotal.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtSubtotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jPanel1.add(txtSubtotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 70, 100, 30));

        txtDiscount.setEditable(false);
        txtDiscount.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtDiscount.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jPanel1.add(txtDiscount, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 110, 100, 30));

        txtDiscountG.setEditable(false);
        txtDiscountG.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtDiscountG.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jPanel1.add(txtDiscountG, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 150, 100, 30));

        txtTotal.setEditable(false);
        txtTotal.setBackground(new java.awt.Color(255, 255, 255));
        txtTotal.setFont(new java.awt.Font("Arial", 1, 28)); // NOI18N
        txtTotal.setForeground(new java.awt.Color(51, 204, 0));
        txtTotal.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtTotal.setText("0");
        txtTotal.setBorder(null);
        jPanel1.add(txtTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 240, 300, 40));

        txtReceived.setBackground(new java.awt.Color(240, 240, 240));
        txtReceived.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtReceived.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtReceived.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtReceivedKeyReleased(evt);
            }
        });
        jPanel1.add(txtReceived, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 310, 100, 30));

        btnPay.setBackground(new java.awt.Color(128, 128, 128));
        btnPay.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnPay.setForeground(new java.awt.Color(255, 255, 255));
        btnPay.setText("EFECTIVO");
        btnPay.setBorderPainted(false);
        btnPay.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnPay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPayActionPerformed(evt);
            }
        });
        jPanel1.add(btnPay, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 410, 140, 40));

        jButton3.setBackground(new java.awt.Color(128, 128, 128));
        jButton3.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("TARJETA");
        jButton3.setBorderPainted(false);
        jButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel1.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 410, 140, 40));

        jLabel1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("No. Art:");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 50, 30));

        txtNoProduct.setEditable(false);
        txtNoProduct.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtNoProduct.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jPanel1.add(txtNoProduct, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 70, 40, 30));
        jPanel1.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 200, 300, 10));
        jPanel1.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 300, 10));
        jPanel1.add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 280, 300, 10));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 350, 500));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnPayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPayActionPerformed
        if ("".equals(txtReceived.getText().trim())) {
            JOptionPane.showMessageDialog(null, "CAMPOS VACIOS\n\n"
                    + "Es necesario ingresar la cantidad recibida por el cliente.", "ADVERTENCIA",
                    JOptionPane.WARNING_MESSAGE);
        } else {
            insertSale();
            if (banNext == false) {
                JOptionPane.showMessageDialog(null, "VENTA INTERRUMPIDA\n\n"
                        + "Es posible que la venta no se haya realizado correctamente.", "ADVERTENCIA",
                        JOptionPane.WARNING_MESSAGE);
                setVisible(false);
                dispose();
            } else {
                insertDetailSale();
                JOptionPane.showMessageDialog(null, "VENTA EXITOSA\n\n"
                        + "Se ha registrado con exito la venta", "INFORMACIÓN",
                        JOptionPane.INFORMATION_MESSAGE);
                x.doClick();
                setVisible(false);
                dispose();
            }
        }
    }//GEN-LAST:event_btnPayActionPerformed

    private void txtReceivedKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtReceivedKeyReleased
        if ("0".equals(txtReceived.getText().trim()) || "".equals(txtReceived.getText().trim())) {
            txtReceived.setText("");
            txtChange.setText("0");
        } else {
            String r = txtTotal.getText();
            r = r.replace("$", "");
            double x = Double.parseDouble(txtReceived.getText().trim());
            double y = Double.parseDouble(r);
            double change = Double.parseDouble(df.format(x - y));
            txtChange.setText(String.valueOf(change));
        }
    }//GEN-LAST:event_txtReceivedKeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnPay;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JLabel lbCustomer;
    private javax.swing.JTextField txtChange;
    private javax.swing.JTextField txtDiscount;
    private javax.swing.JTextField txtDiscountG;
    private javax.swing.JTextField txtNoProduct;
    private javax.swing.JTextField txtReceived;
    private javax.swing.JTextField txtSubtotal;
    private javax.swing.JTextField txtTotal;
    // End of variables declaration//GEN-END:variables
}
