package frames;

import classes.Connections;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.*;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import AppPackage.AnimationClass;
import classes.MainClass;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;

public class Login extends javax.swing.JFrame {

    public static String id, name, ap, am, job, status, user;
    public static BufferedImage photo;

    int moveX, moveY;
    String[] v = new String[14];

    public Login() {
        initComponents();
        setTitle("INICIAR SESIÓN");
        setSize(700, 500);
        data();
    }

    private void data() {
        pAside.setBackground(Color.decode("#" + MainClass.systemColor));
        btnInput.setBackground(Color.decode("#" + MainClass.systemColor));
        s1.setForeground(Color.decode("#" + MainClass.systemColor));
        s2.setForeground(Color.decode("#" + MainClass.systemColor));
        s3.setForeground(Color.decode("#" + MainClass.systemColor));
        lbTitle.setForeground(Color.decode("#" + MainClass.systemColor));
        lbSubtitle.setForeground(Color.decode("#" + MainClass.systemColor));

        ImageIcon icon = new ImageIcon(MainClass.img2.getScaledInstance(lbLogo.getWidth(), lbLogo.getHeight(), Image.SCALE_SMOOTH));
        lbLogo.setIcon(icon);
        lbVersion.setText(MainClass.version);
    }

    private void line() {
        try {
            Connection cn = Connections.connect();
            CallableStatement call = cn.prepareCall("{call UPDATEUSER(?, ?)}");
            call.setString(1, txtUser.getText().trim());
            call.setString(2, "ONLINE");
            call.execute();
            cn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "ERROR DE CÓDIGO\n\n"
                    + "Es posible que haya ocurrido un error en el código.\n"
                    + "Si el problema persiste, contactar con el desarrollador.\n", "ERROR X", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void permisions() {
        try {
            Connection cn = Connections.connect();
            PreparedStatement pst = cn.prepareStatement(""
                    + "SELECT * "
                    + "FROM cargo "
                    + "WHERE nombreCargo = '" + job + "'");
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                v[0] = rs.getString(1);
                v[1] = rs.getString(2);
                v[2] = rs.getString(3);
                v[3] = rs.getString(4);
                v[4] = rs.getString(5);
                v[5] = rs.getString(6);
                v[6] = rs.getString(7);
                v[7] = rs.getString(8);
                v[8] = rs.getString(9);
                v[9] = rs.getString(10);
                v[10] = rs.getString(11);
                v[11] = rs.getString(12);
                v[12] = rs.getString(13);
                v[13] = rs.getString(14);
            }
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
        jLabel1 = new javax.swing.JLabel();
        txtPassword = new javax.swing.JPasswordField();
        btnForget = new javax.swing.JButton();
        lbLogo = new javax.swing.JLabel();
        lbTitle = new javax.swing.JLabel();
        txtUser = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        btnInput = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        lbVersion = new javax.swing.JLabel();
        spAboutUs = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        s2 = new javax.swing.JSeparator();
        s3 = new javax.swing.JSeparator();
        s1 = new javax.swing.JSeparator();
        pAside = new javax.swing.JPanel();
        btnAboutUs = new javax.swing.JButton();
        btnExit = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        lbSubtitle = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
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

        jLabel1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel1.setText("Contraseña:");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(55, 250, 240, 20));

        txtPassword.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPassword.setText("22052001");
        txtPassword.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 240, 240)));
        txtPassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPasswordKeyTyped(evt);
            }
        });
        jPanel1.add(txtPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(55, 270, 240, 30));

        btnForget.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        btnForget.setForeground(new java.awt.Color(128, 128, 128));
        btnForget.setText("¿Olvidaste tu contraseña?");
        btnForget.setBorder(null);
        btnForget.setBorderPainted(false);
        btnForget.setContentAreaFilled(false);
        btnForget.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnForget.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnForgetActionPerformed(evt);
            }
        });
        jPanel1.add(btnForget, new org.netbeans.lib.awtextra.AbsoluteConstraints(55, 370, 240, 20));
        jPanel1.add(lbLogo, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 40, 133, 69));

        lbTitle.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        lbTitle.setForeground(new java.awt.Color(128, 128, 128));
        lbTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbTitle.setText("Iniciar Sesión");
        jPanel1.add(lbTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(55, 120, 240, 28));

        txtUser.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtUser.setText("JERSON MIRANDA");
        txtUser.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 240, 240)));
        txtUser.setDisabledTextColor(new java.awt.Color(240, 240, 240));
        txtUser.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtUserKeyTyped(evt);
            }
        });
        jPanel1.add(txtUser, new org.netbeans.lib.awtextra.AbsoluteConstraints(55, 200, 240, 30));

        jLabel4.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(128, 128, 128));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel4.setText("Mtz. Veracruz");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 445, 80, 15));

        btnInput.setBackground(new java.awt.Color(128, 128, 128));
        btnInput.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnInput.setForeground(new java.awt.Color(255, 255, 255));
        btnInput.setText("INGRESAR");
        btnInput.setBorder(null);
        btnInput.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInputActionPerformed(evt);
            }
        });
        jPanel1.add(btnInput, new org.netbeans.lib.awtextra.AbsoluteConstraints(55, 330, 240, 30));
        jPanel1.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(55, 410, 240, 10));

        jLabel5.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel5.setText("Usuario:");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(55, 180, 240, 20));

        jLabel6.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(128, 128, 128));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("@JEY-System 2022");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 445, 120, 15));

        jLabel7.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(128, 128, 128));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel7.setText("Copyright @ Papelería Gomita");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 430, 170, 15));

        lbVersion.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lbVersion.setForeground(new java.awt.Color(128, 128, 128));
        lbVersion.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jPanel1.add(lbVersion, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 430, 70, 15));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 350, 500));

        spAboutUs.setBorder(null);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel2.add(s2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 240, 40, 10));
        jPanel2.add(s3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 280, 40, 10));
        jPanel2.add(s1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 260, 40, 10));

        pAside.setBackground(new java.awt.Color(128, 128, 128));
        pAside.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnAboutUs.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/info_32px.png"))); // NOI18N
        btnAboutUs.setBorderPainted(false);
        btnAboutUs.setContentAreaFilled(false);
        btnAboutUs.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAboutUs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAboutUsActionPerformed(evt);
            }
        });
        pAside.add(btnAboutUs, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 460, 30, 30));

        btnExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/exit_32px.png"))); // NOI18N
        btnExit.setBorderPainted(false);
        btnExit.setContentAreaFilled(false);
        btnExit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitActionPerformed(evt);
            }
        });
        pAside.add(btnExit, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 30, 30));

        jPanel2.add(pAside, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 0, 50, 500));

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/shadowLogin.png"))); // NOI18N
        jPanel2.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 20, 500));

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/JEY_140px.png"))); // NOI18N
        jPanel2.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(75, 50, 140, 140));

        jLabel11.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(128, 128, 128));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel11.setText("Yiran Rodolfo Arcos García");
        jPanel2.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 270, 180, 16));

        lbSubtitle.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lbSubtitle.setForeground(new java.awt.Color(128, 128, 128));
        lbSubtitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbSubtitle.setText("Punto de Venta");
        jPanel2.add(lbSubtitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(75, 190, 140, 16));

        jLabel13.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(128, 128, 128));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Facebook_20px.png"))); // NOI18N
        jLabel13.setText("JEY-System");
        jLabel13.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jLabel13.setIconTextGap(10);
        jPanel2.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 380, 210, 20));

        jLabel14.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(128, 128, 128));
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel14.setText("Eimy Shirley Morales Garcia");
        jPanel2.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 250, 180, 16));

        jLabel15.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(128, 128, 128));
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel15.setText("Soporte: services@jeysystem.com.mx");
        jPanel2.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 430, 240, 15));

        jLabel16.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(128, 128, 128));
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel16.setText("Jerson Raí Miranda Diaz");
        jPanel2.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 230, 180, 16));

        jLabel17.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(128, 128, 128));
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/phone_20px.png"))); // NOI18N
        jLabel17.setText("2321164170");
        jLabel17.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jLabel17.setIconTextGap(10);
        jPanel2.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 340, 210, 20));

        jLabel18.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(128, 128, 128));
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Instagram_20px.png"))); // NOI18N
        jLabel18.setText("jey.system");
        jLabel18.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jLabel18.setIconTextGap(10);
        jPanel2.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 360, 210, 20));
        jPanel2.add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 410, 240, 10));

        spAboutUs.setViewportView(jPanel2);

        getContentPane().add(spAboutUs, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 0, 350, 500));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnForgetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnForgetActionPerformed
        JOptionPane.showMessageDialog(null, "CONTRASEÑA OLVIDADA\n\n"
                + "Contacte a un administrador", "ADVERTENCIA",
                JOptionPane.WARNING_MESSAGE);
    }//GEN-LAST:event_btnForgetActionPerformed

    private void btnAboutUsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAboutUsActionPerformed
        AnimationClass animation = new AnimationClass();
        animation.jTextAreaXRight(50, 350, 10, 5, spAboutUs);
        animation.jTextAreaXLeft(350, 50, 10, 5, spAboutUs);
    }//GEN-LAST:event_btnAboutUsActionPerformed

    private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitActionPerformed
        if (JOptionPane.showConfirmDialog(null, "¿DESEA SALIR?\n\n"
                + "Esta apunto de cerrar el sistema, \n"
                + "porfavor confirme para continuar.", "SALIR", JOptionPane.YES_NO_OPTION) == 0) {
            System.exit(0);
        }
    }//GEN-LAST:event_btnExitActionPerformed

    private void btnInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInputActionPerformed
        user = txtUser.getText().trim();
        String pass = txtPassword.getText().trim();
        if (user.compareTo("") == 0 && pass.compareTo("") == 0) {
            JOptionPane.showMessageDialog(this, "INGRESE SU USUARIO Y CONTRASEÑA", "ADVERTENCIA", JOptionPane.WARNING_MESSAGE);
        } else {
            if (user.compareTo(user) == 0 && pass.compareTo("") == 0) {
                JOptionPane.showMessageDialog(this, "INGRESE SU CONTRASEÑA", "ADVERTENCIA", JOptionPane.WARNING_MESSAGE);
            } else {
                if (user.compareTo("") == 0 && pass.compareTo(pass) == 0) {
                    JOptionPane.showMessageDialog(this, "INGRSE SU USUARIO", "ADVERTENCIA", JOptionPane.WARNING_MESSAGE);
                } else {
                    try {
                        Connection cn = Connections.connect();
                        PreparedStatement pst = cn.prepareStatement(""
                                + "SELECT idUsuario, nombre, apellidoP, apellidoM, nombreCargo, estado, sistema, foto "
                                + "FROM usuario "
                                + "WHERE usuario = '" + user + "' and contrasena = '" + pass + "'");
                        ResultSet rs = pst.executeQuery();
                        if (rs.next()) {
                            if ("ACTIVO".equals(rs.getString(6)) && "OFFLINE".equals(rs.getString(7))) {
                                id = rs.getString(1);
                                name = rs.getString(2);
                                ap = rs.getString(3);
                                am = rs.getString(4);
                                job = rs.getString(5);
                                status = rs.getString(6);
                                Blob blob = rs.getBlob(8);
                                byte[] data = blob.getBytes(1, (int) blob.length());
                                photo = null;
                                try {
                                    photo = ImageIO.read(new ByteArrayInputStream(data));
                                } catch (IOException e) {
                                }
                                permisions();
                                line();
                                this.dispose();
                                Home home = new Home();
                                if ("SI".equals(v[1])) {
                                    home.btnProduct.setEnabled(true);
                                }
                                if ("SI".equals(v[2])) {
                                    home.btnSale.setEnabled(true);
                                }
                                if ("SI".equals(v[3])) {
                                    home.btnBuy.setEnabled(true);
                                }
                                if ("SI".equals(v[4])) {
                                    home.btnCustomer.setEnabled(true);
                                }
                                if ("SI".equals(v[5])) {
                                    home.btnSupplier.setEnabled(true);
                                }
                                if ("SI".equals(v[6])) {
                                    home.btnStaff.setEnabled(true);
                                }
                                if ("SI".equals(v[7])) {
                                    home.btnStatistic.setEnabled(true);
                                }
                                if ("SI".equals(v[8]) || "SI".equals(v[9]) || "SI".equals(v[10]) || "SI".equals(v[11])) {
                                    home.btnCancel.setEnabled(true);
                                    if ("SI".equals(v[10])) {
                                        //btnCanSale
                                    }
                                    if ("SI".equals(v[11])) {
                                        //btnCanBuy
                                    }
                                }
                                if ("SI".equals(v[12])) {
                                    home.btnJob.setEnabled(true);
                                }
                                if ("SI".equals(v[13])) {
                                    home.btnSetting.setEnabled(true);
                                }
                                home.setVisible(true);
                            } else {
                                JOptionPane.showMessageDialog(null, "EL USUARIO SE ENCUENTRA INACTIVO O MANTIENE ABIERTA UNA SESIÓN EN OTRO DISPOSITIVO", "ERROR", JOptionPane.ERROR_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "DATOS DE ACCESO INCORRECTOS", "ERROR", JOptionPane.ERROR_MESSAGE);
                            txtUser.setText("");
                            txtPassword.setText("");
                        }
                        cn.close();
                    } catch (SQLException e) {
                        JOptionPane.showMessageDialog(null, "ERROR DE CÓDIGO\n\n"
                                + "Es posible que haya ocurrido un error en el código.\n"
                                + "Si el problema persiste, contactar con el desarrollador.\n", "ERROR X", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }//GEN-LAST:event_btnInputActionPerformed

    private void jPanel1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MousePressed
        moveX = evt.getX();
        moveY = evt.getY();
    }//GEN-LAST:event_jPanel1MousePressed

    private void jPanel1MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseDragged
        int x = evt.getXOnScreen();
        int y = evt.getYOnScreen();
        this.setLocation(x - moveX, y - moveY);
    }//GEN-LAST:event_jPanel1MouseDragged

    private void txtUserKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUserKeyTyped
        char c = evt.getKeyChar();
        if (Character.isLowerCase(c)) {
            evt.setKeyChar(Character.toUpperCase(c));
        }
        if (txtUser.getText().length() >= 50) {
            evt.consume();
        }
        if (c == KeyEvent.VK_ENTER) {
            btnInput.doClick();
        }
    }//GEN-LAST:event_txtUserKeyTyped

    private void txtPasswordKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPasswordKeyTyped
        char c = evt.getKeyChar();
        if (Character.isLowerCase(c)) {
            evt.setKeyChar(Character.toUpperCase(c));
        }
        if (txtPassword.getText().length() >= 12) {
            evt.consume();
        }
        if (c == KeyEvent.VK_ENTER) {
            btnInput.doClick();
        }
    }//GEN-LAST:event_txtPasswordKeyTyped

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAboutUs;
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnForget;
    private javax.swing.JButton btnInput;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JLabel lbLogo;
    private javax.swing.JLabel lbSubtitle;
    private javax.swing.JLabel lbTitle;
    private javax.swing.JLabel lbVersion;
    private javax.swing.JPanel pAside;
    private javax.swing.JSeparator s1;
    private javax.swing.JSeparator s2;
    private javax.swing.JSeparator s3;
    private javax.swing.JScrollPane spAboutUs;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtUser;
    // End of variables declaration//GEN-END:variables
}
