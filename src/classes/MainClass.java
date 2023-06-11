package classes;

import frames.Installer;
import frames.Login;
import frames.Presentation;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import javax.swing.UIManager;
import java.sql.*;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class MainClass {

    public static boolean banInstaller = false;
    public static String systemColor, version, id, name, description, street, colony, locate, phone;
    public static BufferedImage img, img2, img3, img4;

    public static void main(String[] args) throws InterruptedException, IOException {
        installing();
        if (banInstaller == true) {
            Installer installer = new Installer();
            installer.setVisible(true);
        } else {
            config();
            data();
            Login login = new Login();
            login.setBackground(new Color(0, 0, 0, 0));
            Presentation presentation = new Presentation();
            presentation.setVisible(true);
            try {
                for (int i = 0; i <= 100; i++) {
                    Thread.sleep(10);
                    presentation.lbPercentage.setText(Integer.toString(i) + "%");
                    presentation.bProgress.setValue(i);
                    if (i == 0 || i == 30 || i == 60 || i == 90) {
                        presentation.lbLoading.setText("Cargando.");
                    }
                    if (i == 10 || i == 40 || i == 70) {
                        presentation.lbLoading.setText("Cargando..");
                    }
                    if (i == 20 || i == 50 || i == 80) {
                        presentation.lbLoading.setText("Cargando...");
                    }
                    if (i == 15) {
                        presentation.lbStatus.setText("Cargando texturas...");
                    }
                    if (i == 45) {
                        presentation.lbStatus.setText("Buscando ultimos registros...");
                    }
                    if (i == 75) {
                        presentation.lbStatus.setText("Construyendo vistas...");
                    }

                    if (i == 100) {
                        presentation.setVisible(false);
                        login.setVisible(true);
                    }
                }
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            } catch (ClassNotFoundException ex) {
                java.util.logging.Logger.getLogger(Presentation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (InstantiationException ex) {
                java.util.logging.Logger.getLogger(Presentation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                java.util.logging.Logger.getLogger(Presentation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (javax.swing.UnsupportedLookAndFeelException ex) {
                java.util.logging.Logger.getLogger(Presentation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
        }
    }

    private static void installing() throws IOException {
        try {
            Connection cn = Connections.connect();
            PreparedStatement pst = cn.prepareStatement(""
                    + "SELECT COUNT(idConfiguracion) "
                    + "FROM configuracion");
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                if ("1".equals(rs.getString(1))) {
                    banInstaller = true;
                } else {
                    banInstaller = false;
                }
            } else {
                System.exit(0);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "ERROR DE CÓDIGO\n\n"
                    + "Es posible que haya ocurrido un error en el código.\n"
                    + "Si el problema persiste, contactar con el desarrollador.\n", "ERROR X", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void config() throws IOException {
        try {
            Connection cn = Connections.connect();
            PreparedStatement pst = cn.prepareStatement(""
                    + "SELECT color, version, fotoPatron, fotoPublicidad "
                    + "FROM configuracion");
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                systemColor = rs.getString(1);
                version = "Version " + rs.getString(2);
                Blob blob = rs.getBlob(3);
                Blob blob2 = rs.getBlob(4);
                byte[] data = blob.getBytes(1, (int) blob.length());
                byte[] data2 = blob2.getBytes(1, (int) blob2.length());
                img3 = null;
                img4 = null;
                try {
                    img3 = ImageIO.read(new ByteArrayInputStream(data));
                    img4 = ImageIO.read(new ByteArrayInputStream(data2));
                } catch (IOException e) {
                }
            } else {
                systemColor = "000000";
                version = "0";
                img3 = ImageIO.read(new File("C:\\Program Files\\JEY-System V1.0\\LogoJEY_1000px.png"));
                img4 = ImageIO.read(new File("C:\\Program Files\\JEY-System V1.0\\LogoJEY_1000px.png"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "ERROR DE CÓDIGO\n\n"
                    + "Es posible que haya ocurrido un error en el código.\n"
                    + "Si el problema persiste, contactar con el desarrollador.\n", "ERROR X", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void data() throws IOException {
        try {
            Connection cn = Connections.connect();
            PreparedStatement pst = cn.prepareStatement(""
                    + "SELECT idEstablecimiento, nombre, descripcion, calle, colonia, localidad, telefono, foto, foto2 "
                    + "FROM establecimiento");
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                id = rs.getString(1);
                name = rs.getString(2);
                description = rs.getString(3);
                street = rs.getString(4);
                colony = rs.getString(5);
                locate = rs.getString(6);
                phone = rs.getString(7);
                Blob blob = rs.getBlob(8);
                Blob blob2 = rs.getBlob(9);
                byte[] data = blob.getBytes(1, (int) blob.length());
                byte[] data2 = blob2.getBytes(1, (int) blob2.length());
                img = null;
                img2 = null;
                try {
                    img = ImageIO.read(new ByteArrayInputStream(data));
                    img2 = ImageIO.read(new ByteArrayInputStream(data2));
                } catch (IOException e) {
                }
            } else {
                id = "INVITADO";
                name = "MODO INVITADO";
                description = "JEY-SYSTEM V1.0";
                street = "CALLE";
                colony = "COLONIA";
                locate = "LOCALIDAD";
                phone = "S/N";
                img = ImageIO.read(new File("C:\\Program Files\\JEY-System V1.0\\LogoJEY_1000px.png"));
                img2 = ImageIO.read(new File("C:\\Program Files\\JEY-System V1.0\\LogoJEY_1000px.png"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "ERROR DE CÓDIGO\n\n"
                    + "Es posible que haya ocurrido un error en el código.\n"
                    + "Si el problema persiste, contactar con el desarrollador.\n", "ERROR X", JOptionPane.ERROR_MESSAGE);
        }
    }
}
