package frames;

import classes.MainClass;
import java.awt.Color;
import java.awt.Image;
import javax.swing.ImageIcon;

public class Presentation extends javax.swing.JFrame {

    public Presentation() {
        initComponents();
        setTitle("PAPELERÍA GOMITA");
        pBanner.setBackground(Color.decode("#" + MainClass.systemColor));
        s1.setForeground(Color.decode("#" + MainClass.systemColor));
        s2.setForeground(Color.decode("#" + MainClass.systemColor));
        s3.setForeground(Color.decode("#" + MainClass.systemColor));
        s4.setForeground(Color.decode("#" + MainClass.systemColor));
        s5.setForeground(Color.decode("#" + MainClass.systemColor));
        s6.setForeground(Color.decode("#" + MainClass.systemColor));
        bProgress.setForeground(Color.decode("#" + MainClass.systemColor));
        
        ImageIcon icon = new ImageIcon(MainClass.img.getScaledInstance(lbLogo.getWidth(), lbLogo.getHeight(), Image.SCALE_SMOOTH));
        lbLogo.setIcon(icon);
        lbName.setText(MainClass.name);
        lbDescription.setText(MainClass.description);
        lbVersion.setText(MainClass.version);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        lbLoading = new javax.swing.JLabel();
        bProgress = new javax.swing.JProgressBar();
        lbPercentage = new javax.swing.JLabel();
        lbName = new javax.swing.JLabel();
        lbDescription = new javax.swing.JLabel();
        lbLogo = new javax.swing.JLabel();
        s1 = new javax.swing.JSeparator();
        s2 = new javax.swing.JSeparator();
        s3 = new javax.swing.JSeparator();
        s4 = new javax.swing.JSeparator();
        s5 = new javax.swing.JSeparator();
        s6 = new javax.swing.JSeparator();
        pBanner = new javax.swing.JPanel();
        lbStatus = new javax.swing.JLabel();
        lbVersion = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbLoading.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jPanel1.add(lbLoading, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 240, 150, 20));

        bProgress.setBackground(new java.awt.Color(255, 255, 255));
        bProgress.setBorder(null);
        jPanel1.add(bProgress, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 265, 460, 5));

        lbPercentage.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lbPercentage.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jPanel1.add(lbPercentage, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 240, 40, 20));

        lbName.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        lbName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1.add(lbName, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 178, 460, 30));

        lbDescription.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lbDescription.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1.add(lbDescription, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 210, 460, 20));
        jPanel1.add(lbLogo, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 30, 140, 140));
        jPanel1.add(s1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 120, 100, 10));
        jPanel1.add(s2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 80, 100, 10));
        jPanel1.add(s3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 100, 100, 10));
        jPanel1.add(s4, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 80, 100, 10));
        jPanel1.add(s5, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 100, 100, 10));
        jPanel1.add(s6, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 120, 100, 10));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 500, 290));

        pBanner.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbStatus.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lbStatus.setForeground(new java.awt.Color(255, 255, 255));
        pBanner.add(lbStatus, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 350, 20));

        lbVersion.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lbVersion.setForeground(new java.awt.Color(255, 255, 255));
        lbVersion.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        pBanner.add(lbVersion, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 20, 100, 20));

        getContentPane().add(pBanner, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 290, 500, 60));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JProgressBar bProgress;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lbDescription;
    public javax.swing.JLabel lbLoading;
    private javax.swing.JLabel lbLogo;
    private javax.swing.JLabel lbName;
    public javax.swing.JLabel lbPercentage;
    public javax.swing.JLabel lbStatus;
    private javax.swing.JLabel lbVersion;
    private javax.swing.JPanel pBanner;
    private javax.swing.JSeparator s1;
    private javax.swing.JSeparator s2;
    private javax.swing.JSeparator s3;
    private javax.swing.JSeparator s4;
    private javax.swing.JSeparator s5;
    private javax.swing.JSeparator s6;
    // End of variables declaration//GEN-END:variables
}
