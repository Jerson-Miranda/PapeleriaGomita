package frames;

import classes.Connections;
import classes.MainClass;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.RowFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class Home extends javax.swing.JFrame {

    DecimalFormat df = new DecimalFormat("#.00");
    private final Timer timer;
    private final TableRowSorter<DefaultTableModel> sorterProduct;
    private final TableRowSorter<DefaultTableModel> sorterCustomer;
    private final TableRowSorter<DefaultTableModel> sorterSupplier;
    private final TableRowSorter<DefaultTableModel> sorterStaff;
    private final TableRowSorter<DefaultTableModel> sorterJob;
    boolean banDetailsSession = false;
    boolean banFavoriteProduct = false;
    String codeProduct = "", nameCustomer = "", idSupplier = "", idStaff = "", nameJob = "", codeSale = "";
    double totalSale = 0, subTotalSale = 0, discountSale = 0;
    int contadorSale = 0;

    double priceSale = 0, importSale = 0;
    int cantSale = 0, filaSale = 0;

    public Home() {
        initComponents();
        setTitle("Papelería Gomita");
        setVisible();
        dataHome();
        timer = new javax.swing.Timer(1000, new time());
        timer.start();
        //---------------------------------------------------------------------------
        DefaultTableModel dtm = (DefaultTableModel) tProduct.getModel();
        sorterProduct = new TableRowSorter<>(dtm);
        tProduct.setRowSorter(sorterProduct);
        DefaultTableModel dtm2 = (DefaultTableModel) tCustomer.getModel();
        sorterCustomer = new TableRowSorter<>(dtm2);
        tCustomer.setRowSorter(sorterCustomer);
        DefaultTableModel dtm3 = (DefaultTableModel) tSupplier.getModel();
        sorterSupplier = new TableRowSorter<>(dtm3);
        tSupplier.setRowSorter(sorterSupplier);
        DefaultTableModel dtm4 = (DefaultTableModel) tStaff.getModel();
        sorterStaff = new TableRowSorter<>(dtm4);
        tStaff.setRowSorter(sorterStaff);
        DefaultTableModel dtm5 = (DefaultTableModel) tJob.getModel();
        sorterJob = new TableRowSorter<>(dtm5);
        tJob.setRowSorter(sorterJob);
        //-------------------------------------------------------------------------
        mouseClickProduct();
        mouseClickCustomer();
        mouseClickSupplier();
        mouseClickStaff();
        mouseClickJob();
        mouseClickSale();
    }

    //PRINCIPAL
    private String date() {
        SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd");
        Date date = new Date();
        return format.format(date);
    }

    private class time implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent evt) {
            GregorianCalendar time = new GregorianCalendar();
            java.util.Date date = new java.util.Date();
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
            int h = time.get(Calendar.HOUR);
            int m = time.get(Calendar.MINUTE);
            int s = time.get(Calendar.SECOND);
            int ampm = time.get(Calendar.AM_PM);
            String x = h + ":" + m + ":" + s;
            x = sdf.format(date);
            if (ampm == 0) {
                lbTime.setText(x + " am");
            } else if (ampm == 1) {
                lbTime.setText(x + " pm");
            }
        }
    }

    private void setVisible() {
        pHome.setVisible(false);
        pProduct.setVisible(false);
        pCustomer.setVisible(false);
        pSupplier.setVisible(false);
        pStaff.setVisible(false);
        pJob.setVisible(false);
        pSale.setVisible(false);
        pSetting.setVisible(false);
    }

    private void setForeground() {
        btnHome.setForeground(Color.WHITE);
        btnProduct.setForeground(Color.WHITE);
        btnSale.setForeground(Color.WHITE);
        btnBuy.setForeground(Color.WHITE);
        btnCustomer.setForeground(Color.WHITE);
        btnSupplier.setForeground(Color.WHITE);
        btnStaff.setForeground(Color.WHITE);
        btnStatistic.setForeground(Color.WHITE);
        btnCancel.setForeground(Color.WHITE);
        btnJob.setForeground(Color.WHITE);
        btnSetting.setForeground(Color.WHITE);
    }

    public FileInputStream insertPhoto(String ruta) {
        FileInputStream fi = null;
        try {
            File file = new File(ruta);
            fi = new FileInputStream(file);
        } catch (FileNotFoundException e) {
        }
        return fi;
    }

    public void file(JLabel photo, JLabel ruta) {
        String system = System.getProperty("user.home");
        JFileChooser jfc = new JFileChooser(system + "/Pictures/");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG, PNG & GIF", "jpg", "png", "gif");
        jfc.setFileFilter(filter);
        int answer = jfc.showOpenDialog(this);
        if (answer == JFileChooser.APPROVE_OPTION) {
            String ruta1 = jfc.getSelectedFile().getPath();
            Image img = new ImageIcon(ruta1).getImage();
            ImageIcon icon = new ImageIcon(img.getScaledInstance(photo.getWidth(), photo.getHeight(), Image.SCALE_SMOOTH));
            photo.setIcon(icon);
            ruta.setText(ruta1);
        }
    }

    private void line() {
        try {
            Connection cn = Connections.connect();
            CallableStatement call = cn.prepareCall("{call UPDATEUSER(?, ?)}");
            call.setString(1, Login.user);
            call.setString(2, "OFFLINE");
            call.execute();
            cn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "ERROR DE CÓDIGO\n\n"
                    + "Es posible que haya ocurrido un error en el código.\n"
                    + "Si el problema persiste, contactar con el desarrollador.\n", "ERROR X", JOptionPane.ERROR_MESSAGE);
        }
    }

    //DATA
    private void dataHome() {
        ImageIcon icon0 = new ImageIcon(MainClass.img3.getScaledInstance(lbPattern.getWidth(), lbPattern.getHeight(), Image.SCALE_SMOOTH));
        lbPattern.setIcon(icon0);
        ImageIcon icon01 = new ImageIcon(MainClass.img4.getScaledInstance(lbBannerSale.getWidth(), lbBannerSale.getHeight(), Image.SCALE_SMOOTH));
        lbBannerSale.setIcon(icon01);
        lbIdUser.setText(Login.id);
        ImageIcon icon = new ImageIcon(Login.photo.getScaledInstance(lbPhotoUser.getWidth(), lbPhotoUser.getHeight(), Image.SCALE_SMOOTH));
        lbPhotoUser.setIcon(icon);
        lbNameUser.setText(Login.name + " " + Login.ap + " " + Login.am);
        lbJobUser.setText(Login.job);
        pDetailsSession.setVisible(false);
        lbDate.setText(date());
        ImageIcon icon2 = new ImageIcon(MainClass.img2.getScaledInstance(lbLogoMenu.getWidth(), lbLogoMenu.getHeight(), Image.SCALE_SMOOTH));
        lbLogoMenu.setIcon(icon2);
        ImageIcon icon3 = new ImageIcon(MainClass.img.getScaledInstance(lbLogoHome.getWidth(), lbLogoHome.getHeight(), Image.SCALE_SMOOTH));
        lbLogoHome.setIcon(icon3);
        lbVersion.setText(MainClass.version);
        lbStreet.setText(MainClass.street);
        lbColony.setText(MainClass.colony);
        lbLocation.setText(MainClass.locate);
        lbPhoneEstableciment.setText(MainClass.phone);
        lbIdEstableciment.setText(MainClass.id);
        pMenu.setBackground(Color.decode("#" + MainClass.systemColor));
        lbFrankSale.setBackground(Color.decode("#" + MainClass.systemColor));
        lbTotalSale.setBackground(Color.decode("#" + MainClass.systemColor));
        //TITLE
        lbTitleSale.setForeground(Color.decode("#" + MainClass.systemColor));
        lbTitleProduct.setForeground(Color.decode("#" + MainClass.systemColor));
        lbTitleSupplier.setForeground(Color.decode("#" + MainClass.systemColor));
        lbTitleStaff.setForeground(Color.decode("#" + MainClass.systemColor));
        lbTitleCustomer.setForeground(Color.decode("#" + MainClass.systemColor));
        lbTitleJob.setForeground(Color.decode("#" + MainClass.systemColor));
        lbTitleSetting.setForeground(Color.decode("#" + MainClass.systemColor));
        //BUTTONS
        btnAddProduct.setBackground(Color.decode("#" + MainClass.systemColor));
        btnSaveProduct.setBackground(Color.decode("#" + MainClass.systemColor));
        btnEditProduct.setBackground(Color.decode("#" + MainClass.systemColor));
        btnDeleteProduct.setBackground(Color.decode("#" + MainClass.systemColor));
        btnAddSupplier.setBackground(Color.decode("#" + MainClass.systemColor));
        btnSaveSupplier.setBackground(Color.decode("#" + MainClass.systemColor));
        btnEditSupplier.setBackground(Color.decode("#" + MainClass.systemColor));
        btnDeleteSupplier.setBackground(Color.decode("#" + MainClass.systemColor));
        btnAddStaff.setBackground(Color.decode("#" + MainClass.systemColor));
        btnSaveStaff.setBackground(Color.decode("#" + MainClass.systemColor));
        btnEditStaff.setBackground(Color.decode("#" + MainClass.systemColor));
        btnDeleteStaff.setBackground(Color.decode("#" + MainClass.systemColor));
        btnAddCustomer.setBackground(Color.decode("#" + MainClass.systemColor));
        btnSaveCustomer.setBackground(Color.decode("#" + MainClass.systemColor));
        btnEditCustomer.setBackground(Color.decode("#" + MainClass.systemColor));
        btnDeleteCustomer.setBackground(Color.decode("#" + MainClass.systemColor));
        btnAddJob.setBackground(Color.decode("#" + MainClass.systemColor));
        btnSaveJob.setBackground(Color.decode("#" + MainClass.systemColor));
        btnEditJob.setBackground(Color.decode("#" + MainClass.systemColor));
        btnDeleteJob.setBackground(Color.decode("#" + MainClass.systemColor));
        btnTool11.setBackground(Color.decode("#" + MainClass.systemColor));
        btnTool12.setBackground(Color.decode("#" + MainClass.systemColor));
        btnTool13.setBackground(Color.decode("#" + MainClass.systemColor));
        btnTool21.setBackground(Color.decode("#" + MainClass.systemColor));
        btnTool22.setBackground(Color.decode("#" + MainClass.systemColor));
        btnTool23.setBackground(Color.decode("#" + MainClass.systemColor));
        btnTool51.setBackground(Color.decode("#" + MainClass.systemColor));
        btnTool52.setBackground(Color.decode("#" + MainClass.systemColor));
        //BSTORAGE
        lbCountProduct.setForeground(Color.decode("#" + MainClass.systemColor));
        bStorageProduct.setForeground(Color.decode("#" + MainClass.systemColor));
        lbCountSupplier.setForeground(Color.decode("#" + MainClass.systemColor));
        bStorageSupplier.setForeground(Color.decode("#" + MainClass.systemColor));
        lbCountStaff.setForeground(Color.decode("#" + MainClass.systemColor));
        bStorageStaff.setForeground(Color.decode("#" + MainClass.systemColor));
        lbCountCustomer.setForeground(Color.decode("#" + MainClass.systemColor));
        bStorageCustomer.setForeground(Color.decode("#" + MainClass.systemColor));
        lbCountJob.setForeground(Color.decode("#" + MainClass.systemColor));
        bStorageJob.setForeground(Color.decode("#" + MainClass.systemColor));
        //TABLESELECTED
        tProduct.setSelectionBackground(Color.decode("#" + MainClass.systemColor));
        tSale.setSelectionBackground(Color.decode("#" + MainClass.systemColor));
        tCustomer.setSelectionBackground(Color.decode("#" + MainClass.systemColor));
        tSupplier.setSelectionBackground(Color.decode("#" + MainClass.systemColor));
        tStaff.setSelectionBackground(Color.decode("#" + MainClass.systemColor));
        tJob.setSelectionBackground(Color.decode("#" + MainClass.systemColor));
    }

    private void dataProduct() throws IOException {
        viewProduct();
        countProduct();
        statisticProduct();
        cbDepartment();
        clearProduct();
        codeProduct = null;
    }

    private void dataCustomer() {
        viewCustomer();
        countCustomer();
        statisticCustomer();
        clearCustomer();
        nameCustomer = null;
    }

    private void dataSupplier() throws IOException {
        viewSupplier();
        countSupplier();
        statisticSupplier();
        clearSupplier();
        idSupplier = null;
    }

    private void dataStaff() throws IOException {
        viewStaff();
        countStaff();
        statisticStaff();
        cbJob();
        clearStaff();
        idStaff = null;
    }

    private void dataJob() {
        viewJob();
        countJob();
        clearJob();
        nameJob = null;
    }

    private void dataSale() {
        noSale();
        clearSale();
    }

    private void dataSetting() {
        ImageIcon icon0 = new ImageIcon(MainClass.img4.getScaledInstance(lbPhotoBannerSetting1.getWidth(), lbPhotoBannerSetting1.getHeight(), Image.SCALE_SMOOTH));
        lbPhotoBannerSetting1.setIcon(icon0);
        ImageIcon icon1 = new ImageIcon(MainClass.img.getScaledInstance(lbPhotoPlantSetting.getWidth(), lbPhotoPlantSetting.getHeight(), Image.SCALE_SMOOTH));
        lbPhotoPlantSetting.setIcon(icon1);
        ImageIcon icon2 = new ImageIcon(MainClass.img2.getScaledInstance(lbPhotoPlantSetting1.getWidth(), lbPhotoPlantSetting1.getHeight(), Image.SCALE_SMOOTH));
        lbPhotoPlantSetting1.setIcon(icon2);
        viewSetting();
        setVisibleSetting();
        boldLetter();
        btnTool1.setFont(new Font("Arial", 1, 14));
        pTool1.setVisible(true);
    }

    //SALE
    public void noSale() {
        try {
            try ( Connection cn = Connections.connect()) {
                PreparedStatement pst = cn.prepareStatement(""
                        + "SELECT noVenta "
                        + "FROM venta "
                        + "WHERE noVenta = (SELECT MAX(noVenta) FROM venta)");
                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    int x = rs.getInt(1);
                    x = x + 1;
                    txtNoSale.setText(String.valueOf(x));
                }
                cn.close();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "ERROR DE CÓDIGO\n\n"
                    + "Es posible que haya ocurrido un error en el código.\n"
                    + "Si el problema persiste, contactar con el desarrollador.\n", "ERROR X", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mouseClickSale() {
        DefaultTableModel dtm = (DefaultTableModel) tSale.getModel();
        tSale.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                filaSale = tSale.rowAtPoint(e.getPoint());
                if (filaSale > -1) {
                    priceSale = Double.parseDouble(dtm.getValueAt(filaSale, 4).toString());
                    importSale = Double.parseDouble(dtm.getValueAt(filaSale, 6).toString());
                    cantSale = Integer.parseInt(dtm.getValueAt(filaSale, 0).toString());
                    codeSale = dtm.getValueAt(filaSale, 1).toString();
                }
                try {
                    Connection cn = Connections.connect();
                    PreparedStatement pst = cn.prepareStatement("select foto from producto where codigoBarra = '" + codeSale + "'");
                    ResultSet rs = pst.executeQuery();
                    if (rs.next()) {
                        Blob blob = rs.getBlob(1);
                        byte[] data = blob.getBytes(1, (int) blob.length());
                        BufferedImage img = null;
                        try {
                            img = ImageIO.read(new ByteArrayInputStream(data));
                        } catch (IOException ex) {
                        }
                        ImageIcon icon = new ImageIcon(img.getScaledInstance(lbPhotoSale.getWidth(), lbPhotoSale.getHeight(), Image.SCALE_SMOOTH));
                        lbPhotoSale.setIcon(icon);
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "ERROR DE CÓDIGO\n\n"
                            + "Es posible que haya ocurrido un error en el código.\n"
                            + "Si el problema persiste, contactar con el desarrollador.\n", "ERROR X", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void insertSale() {
        DetailsSale ds = new DetailsSale(this, true, String.valueOf(contadorSale), lbSubtotalSale.getText(), lbDiscountSale.getText(),
                txtDiscountSale.getText(), lbTotalSale.getText(), txtCustomerSale.getText(), lbTime.getText(),
                lbIdUser.getText(), btnSale, tSale);
        ds.setVisible(true);
    }

    private void deleteSale() {
        if (tSale.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(null, "NADA SELECCIONADO\n\n"
                    + "Seleccione de la tabla el producto que desea eliminar de la venta", "ADVERTENCIA",
                    JOptionPane.WARNING_MESSAGE);
        } else {
            if (JOptionPane.showConfirmDialog(null, "¿Esta seguro de eliminar este producto de la venta?", "PREGUNTA",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                try {
                    Connection cn = Connections.connect();
                    PreparedStatement pst = cn.prepareStatement(""
                            + "SELECT venta, descuento "
                            + "FROM producto "
                            + "WHERE codigoBarra = '" + codeSale + "'");
                    ResultSet rs = pst.executeQuery();
                    if (rs.next()) {
                        double dis = (rs.getDouble(1) * rs.getDouble(2)) / 100;
                        DefaultTableModel dtm = (DefaultTableModel) tSale.getModel();
                        subTotalSale = subTotalSale - (priceSale * cantSale);
                        discountSale = discountSale - (dis * cantSale);
                        totalSale = totalSale - importSale;
                        contadorSale = contadorSale - cantSale;
                        lbSubtotalSale.setText(df.format(subTotalSale));
                        lbDiscountSale.setText(df.format(discountSale));
                        lbTotalSale.setText(df.format(totalSale));
                        dtm.removeRow(filaSale);
                        lbPhotoSale.setIcon(null);
                        if (dtm.getRowCount() == 0) {
                            btnSaveSale.setEnabled(false);
                        }
                    }
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "ERROR DE CÓDIGO\n\n"
                            + "Es posible que haya ocurrido un error en el código.\n"
                            + "Si el problema persiste, contactar con el desarrollador.\n", "ERROR X", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void clearSale() {
        txtCodeSale.setText("");
        txtCustomerSale.setText("PÚBLICO GENERAL");
        txtDiscountSale.setText("0");
        lbPhotoSale.setIcon(null);
        DefaultTableModel dtm = (DefaultTableModel) tSale.getModel();
        dtm.setRowCount(0);
        lbTotalSale.setText("0.0");
        lbDiscountSale.setText("0.0");
        lbSubtotalSale.setText("0.0");
        btnSaveSale.setEnabled(false);
        subTotalSale = 0;
        discountSale = 0;
        totalSale = 0;
        contadorSale = 0;
    }

    //PRODUCTS
    private void countProduct() {
        try {
            Connection cn = Connections.connect();
            PreparedStatement pst = cn.prepareStatement(""
                    + "SELECT COUNT(codigoBarra) "
                    + "FROM producto");
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                lbCountProduct.setText(rs.getString(1));
                bStorageProduct.setValue(rs.getInt(1));
                lbStorageProduct.setText(rs.getString(1) + "/1000");
            } else {
                lbCountProduct.setText("0");
                bStorageProduct.setValue(0);
                lbStorageProduct.setText("0/1000");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "ERROR DE CÓDIGO\n\n"
                    + "Es posible que haya ocurrido un error en el código.\n"
                    + "Si el problema persiste, contactar con el desarrollador.\n", "ERROR X", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void statisticProduct() {
        taDepartment.setText("");
        taCantDepartment.setText("");
        try {
            Connection cn = Connections.connect();
            PreparedStatement pst = cn.prepareStatement(""
                    + "SELECT nombreDepartamento, COUNT(codigoBarra) "
                    + "FROM producto "
                    + "GROUP BY nombreDepartamento "
                    + "ORDER BY nombreDepartamento");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String x = taDepartment.getText();
                String y = taCantDepartment.getText();
                taDepartment.setText(x + "\n" + rs.getString(1));
                taCantDepartment.setText(y + "\n" + rs.getString(2));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "ERROR DE CÓDIGO\n\n"
                    + "Es posible que haya ocurrido un error en el código.\n"
                    + "Si el problema persiste, contactar con el desarrollador.\n", "ERROR X", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cbDepartment() {
        cbDepartmentProduct.removeAllItems();
        try {
            Connection cn = Connections.connect();
            PreparedStatement pst = cn.prepareStatement(""
                    + "SELECT nombreDepartamento "
                    + "FROM departamento "
                    + "ORDER BY nombreDepartamento");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                cbDepartmentProduct.addItem(rs.getString(1));
            }
            cn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "ERROR DE CÓDIGO\n\n"
                    + "Es posible que haya ocurrido un error en el código.\n"
                    + "Si el problema persiste, contactar con el desarrollador.\n", "ERROR X", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void viewProduct() {
        DefaultTableModel dtm = (DefaultTableModel) tProduct.getModel();
        dtm.setRowCount(0);
        try {
            Connection cn = Connections.connect();
            PreparedStatement pst = cn.prepareStatement(""
                    + "SELECT codigoBarra, stock, compra, venta, nombre, marca, nombreDepartamento, descuento, estado "
                    + "FROM producto "
                    + "ORDER BY nombre");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getString(1));
                v.add(rs.getString(2));
                v.add(rs.getString(3));
                v.add(rs.getString(4));
                v.add(rs.getString(5));
                v.add(rs.getString(6));
                v.add(rs.getString(7));
                v.add(rs.getString(8));
                v.add(rs.getString(9));
                dtm.addRow(v);
            }
            tProduct.setModel(dtm);
            cn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "ERROR DE CÓDIGO\n\n"
                    + "Es posible que haya ocurrido un error en el código.\n"
                    + "Si el problema persiste, contactar con el desarrollador.\n", "ERROR X", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mouseClickProduct() {
        lbRutaProduct.setText("");
        DefaultTableModel dtm = (DefaultTableModel) tProduct.getModel();
        tProduct.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila_point = tProduct.rowAtPoint(e.getPoint());
                if (fila_point > -1) {
                    codeProduct = dtm.getValueAt(fila_point, 0).toString();
                }
                try {
                    Connection cn = Connections.connect();
                    PreparedStatement pst = cn.prepareStatement("select * from producto where codigoBarra = '" + codeProduct + "'");
                    ResultSet rs = pst.executeQuery();
                    if (rs.next()) {
                        txtCodeProduct.setText(rs.getString(1));
                        txtNameProduct.setText(rs.getString(2));
                        txtBrendProduct.setText(rs.getString(3));
                        cbDepartmentProduct.setSelectedItem(rs.getString(4));
                        txtBuyProduct.setText(rs.getString(5));
                        txtGainProduct.setText(rs.getString(6));
                        txtDiscountProduct.setText(rs.getString(7));
                        txtSaleProduct.setText(rs.getString(8));
                        txtStockProduct.setText(rs.getString(9));
                        txtStockMaxProduct.setText(rs.getString(10));
                        txtStockMinProduct.setText(rs.getString(11));
                        if ("SI".equals(rs.getString(12))) {
                            btnFavorite.setIcon(new ImageIcon(getClass().getResource("/icons/star_filled_30px.png")));
                            banFavoriteProduct = true;
                        } else {
                            btnFavorite.setIcon(new ImageIcon(getClass().getResource("/icons/star_30px.png")));
                            banFavoriteProduct = false;
                        }
                        if ("ACTIVO".equals(rs.getString(13))) {
                            rbActiveProduct.setSelected(true);
                        } else {
                            rbInactiveProduct.setSelected(true);
                        }
                        Blob blob = rs.getBlob(14);
                        byte[] data = blob.getBytes(1, (int) blob.length());
                        BufferedImage img = null;
                        try {
                            img = ImageIO.read(new ByteArrayInputStream(data));
                            ImageIcon icon = new ImageIcon(img.getScaledInstance(lbPhotoProduct.getWidth(), lbPhotoProduct.getHeight(), Image.SCALE_SMOOTH));
                            lbPhotoProduct.setIcon(icon);
                        } catch (IOException ex) {
                        }
                    }
                    cn.close();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "ERROR DE CÓDIGO\n\n"
                            + "Es posible que haya ocurrido un error en el código.\n"
                            + "Si el problema persiste, contactar con el desarrollador.\n", "ERROR X", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void insertProduct() throws IOException {
        if ("".equals(txtCodeProduct.getText()) || "".equals(txtNameProduct.getText()) || "".equals(txtBrendProduct.getText()) || "".equals(txtBuyProduct.getText())
                || "".equals(txtGainProduct.getText()) || "".equals(txtDiscountProduct.getText()) || "".equals(txtSaleProduct.getText()) || "".equals(txtStockProduct.getText())
                || "".equals(txtStockMaxProduct.getText()) || "".equals(txtStockMinProduct.getText()) || "".equals(lbRutaProduct.getText())) {
            JOptionPane.showMessageDialog(null, "CAMPOS VACIOS\n\n"
                    + "Es necesario llenar todos los campos.", "ADVERTENCIA",
                    JOptionPane.WARNING_MESSAGE);
        } else {
            try {
                Connection cn = Connections.connect();
                PreparedStatement pst = cn.prepareStatement("select codigoBarra from producto where codigoBarra = '" + txtCodeProduct.getText() + "'");
                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    JOptionPane.showMessageDialog(null, "REGISTRO EXISTENTE\n\n"
                            + "Producto " + rs.getString(1) + " actualmente registrado.", "ADVERTENCIA",
                            JOptionPane.WARNING_MESSAGE);
                    cn.close();
                } else {
                    cn.close();
                    try {
                        double buy = Double.parseDouble(txtBuyProduct.getText().trim());
                        double sale = Double.parseDouble(txtSaleProduct.getText().trim());
                        Connection cn2 = Connections.connect();
                        PreparedStatement pst2 = cn2.prepareStatement("insert into producto values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                        pst2.setString(1, txtCodeProduct.getText().trim());
                        pst2.setString(2, txtNameProduct.getText().trim());
                        pst2.setString(3, txtBrendProduct.getText().trim());
                        pst2.setString(4, cbDepartmentProduct.getSelectedItem().toString());
                        pst2.setDouble(5, Double.parseDouble(df.format(buy)));
                        pst2.setInt(6, Integer.parseInt(txtGainProduct.getText().trim()));
                        pst2.setInt(7, Integer.parseInt(txtDiscountProduct.getText().trim()));
                        pst2.setDouble(8, Double.parseDouble(df.format(sale)));
                        pst2.setInt(9, Integer.parseInt(txtStockProduct.getText().trim()));
                        pst2.setInt(10, Integer.parseInt(txtStockMaxProduct.getText().trim()));
                        pst2.setInt(11, Integer.parseInt(txtStockMinProduct.getText().trim()));
                        if (banFavoriteProduct == true) {
                            pst2.setString(12, "SI");
                        } else {
                            pst2.setString(12, "NO");
                        }
                        if (rbActiveProduct.isSelected()) {
                            pst2.setString(13, "ACTIVO");
                        } else if (rbInactiveProduct.isSelected()) {
                            pst2.setString(13, "INACTIVO");
                        }
                        pst2.setBlob(14, insertPhoto(lbRutaProduct.getText()));
                        pst2.executeUpdate();
                        JOptionPane.showMessageDialog(null, "REGISTRO EXITOSO\n\n"
                                + "Se ha registrado con exito el producto con codigo de barras: " + txtCodeProduct.getText().trim(), "INFORMACIÓN",
                                JOptionPane.INFORMATION_MESSAGE);
                        viewProduct();
                        clearProduct();
                        statisticProduct();
                        countProduct();
                        cn2.close();
                    } catch (SQLException e) {
                        JOptionPane.showMessageDialog(null, "ERROR DE CÓDIGO\n\n"
                                + "Es posible que haya ocurrido un error en el código.\n"
                                + "Si el problema persiste, contactar con el desarrollador.\n", "ERROR X", JOptionPane.ERROR_MESSAGE);
                    }
                }
                cn.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "ERROR DE CÓDIGO\n\n"
                        + "Es posible que haya ocurrido un error en el código.\n"
                        + "Si el problema persiste, contactar con el desarrollador.\n", "ERROR X", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateProduct() throws IOException {
        if ("".equals(txtCodeProduct.getText()) || "".equals(txtNameProduct.getText()) || "".equals(txtBrendProduct.getText()) || "".equals(txtBuyProduct.getText())
                || "".equals(txtGainProduct.getText()) || "".equals(txtDiscountProduct.getText()) || "".equals(txtSaleProduct.getText()) || "".equals(txtStockProduct.getText())
                || "".equals(txtStockMaxProduct.getText()) || "".equals(txtStockMinProduct.getText())) {
            JOptionPane.showMessageDialog(null, "CAMPOS VACIOS\n\n"
                    + "Es necesario llenar todos los campos.", "ADVERTENCIA",
                    JOptionPane.WARNING_MESSAGE);
        } else {
            try {
                double buy = Double.parseDouble(txtBuyProduct.getText().trim());
                double sale = Double.parseDouble(txtSaleProduct.getText().trim());
                Connection cn = Connections.connect();
                CallableStatement call = cn.prepareCall("{call UPDATEPRODUCT(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
                call.setString(1, txtCodeProduct.getText().trim());
                call.setString(2, txtNameProduct.getText().trim());
                call.setString(3, txtBrendProduct.getText().trim());
                call.setString(4, cbDepartmentProduct.getSelectedItem().toString());
                call.setDouble(5, Double.parseDouble(df.format(buy)));
                call.setInt(6, Integer.parseInt(txtGainProduct.getText().trim()));
                call.setInt(7, Integer.parseInt(txtDiscountProduct.getText().trim()));
                call.setDouble(8, Double.parseDouble(df.format(sale)));
                call.setInt(9, Integer.parseInt(txtStockProduct.getText().trim()));
                call.setInt(10, Integer.parseInt(txtStockMaxProduct.getText().trim()));
                call.setInt(11, Integer.parseInt(txtStockMinProduct.getText().trim()));
                if (banFavoriteProduct == true) {
                    call.setString(12, "SI");
                } else {
                    call.setString(12, "NO");
                }
                if (rbActiveProduct.isSelected()) {
                    call.setString(13, "ACTIVO");
                } else if (rbInactiveProduct.isSelected()) {
                    call.setString(13, "INACTIVO");
                }
                call.execute();
                JOptionPane.showMessageDialog(null, "ACTUALIZACIÓN EXITOSA\n\n"
                        + "Se ha actualizado con exito el producto con código de barras: " + txtCodeProduct.getText().trim(), "INFORMACIÓN",
                        JOptionPane.INFORMATION_MESSAGE);
                viewProduct();
                clearProduct();
                statisticProduct();
                countProduct();
                cn.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "ERROR DE CÓDIGO\n\n"
                        + "Es posible que haya ocurrido un error en el código.\n"
                        + "Si el problema persiste, contactar con el desarrollador.\n", "ERROR X", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteProduct() throws IOException {
        if (tProduct.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(null, "NADA SELECCIONADO\n\n"
                    + "Seleccione de la tabla el producto que desea eliminar", "ADVERTENCIA",
                    JOptionPane.WARNING_MESSAGE);
        } else {
            if (JOptionPane.showConfirmDialog(null, "¿DESEA ELIMINAR EL PRODUCTO?\n\n"
                    + "Esta apunto de eliminar el producto con id: " + codeProduct + ",\n"
                    + "porfavor confirme para continuar.", "ELIMINACIÓN", JOptionPane.YES_NO_OPTION) == 0) {
                try {
                    Connection cn = Connections.connect();
                    PreparedStatement pst = cn.prepareStatement("delete from producto where codigoBarra = '" + codeProduct + "'");
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "ELIMINACIÓN EXITOSA\n\n"
                            + "Se ha eliminado con exito el producto con código de barras: " + codeProduct, "INFORMACIÓN",
                            JOptionPane.INFORMATION_MESSAGE);
                    viewProduct();
                    clearProduct();
                    countProduct();
                    statisticProduct();
                    cn.close();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "ERROR DE CÓDIGO\n\n"
                            + "Es posible que haya ocurrido un error en el código.\n"
                            + "Si el problema persiste, contactar con el desarrollador.\n", "ERROR X", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void clearProduct() throws IOException {
        txtCodeProduct.setText("");
        txtNameProduct.setText("");
        txtBrendProduct.setText("");
        cbDepartmentProduct.setSelectedIndex(0);
        txtBuyProduct.setText("0");
        txtGainProduct.setText("0");
        txtDiscountProduct.setText("0");
        txtSaleProduct.setText("0.0");
        txtStockProduct.setText("");
        txtStockMaxProduct.setText("");
        txtStockMinProduct.setText("");
        btnFavorite.setIcon(new ImageIcon(getClass().getResource("/icons/star_30px.png")));
        banFavoriteProduct = false;
        rbActiveProduct.setSelected(true);
        BufferedImage imgUno = ImageIO.read(getClass().getResource("/icons/emptyImage.png"));
        ImageIcon img1 = new ImageIcon(imgUno.getScaledInstance(lbPhotoProduct.getWidth(), lbPhotoProduct.getHeight(), Image.SCALE_SMOOTH));
        lbPhotoProduct.setIcon(img1);
        lbRutaProduct.setText("");
    }

    //CUSTOMERS
    private void countCustomer() {
        try {
            Connection cn = Connections.connect();
            PreparedStatement pst = cn.prepareStatement(""
                    + "SELECT COUNT(nombreCliente) "
                    + "FROM cliente");
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                lbCountCustomer.setText(rs.getString(1));
                bStorageCustomer.setValue(rs.getInt(1));
                lbStorageCustomer.setText(rs.getString(1) + "/500");
            } else {
                lbCountCustomer.setText("0");
                bStorageCustomer.setValue(0);
                lbStorageCustomer.setText("0/500");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "ERROR DE CÓDIGO\n\n"
                    + "Es posible que haya ocurrido un error en el código.\n"
                    + "Si el problema persiste, contactar con el desarrollador.\n", "ERROR X", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void statisticCustomer() {
        taCustomer.setText("");
        try {
            Connection cn = Connections.connect();
            PreparedStatement pst = cn.prepareStatement(""
                    + "SELECT nombreCliente, count(nombreCliente) "
                    + "FROM venta "
                    + "GROUP BY nombreCliente");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String x = taCustomer.getText();
                taCustomer.setText(x + "\n" + rs.getString(1));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "ERROR DE CÓDIGO\n\n"
                    + "Es posible que haya ocurrido un error en el código.\n"
                    + "Si el problema persiste, contactar con el desarrollador.\n", "ERROR X", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void viewCustomer() {
        DefaultTableModel dtm = (DefaultTableModel) tCustomer.getModel();
        dtm.setRowCount(0);
        try {
            Connection cn = Connections.connect();
            PreparedStatement pst = cn.prepareStatement(""
                    + "SELECT * "
                    + "FROM cliente "
                    + "ORDER BY nombreCliente");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getString(1));
                v.add(rs.getString(2));
                v.add(rs.getString(3));
                dtm.addRow(v);
            }
            tCustomer.setModel(dtm);
            cn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "ERROR DE CÓDIGO\n\n"
                    + "Es posible que haya ocurrido un error en el código.\n"
                    + "Si el problema persiste, contactar con el desarrollador.\n", "ERROR X", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mouseClickCustomer() {
        DefaultTableModel dtm = (DefaultTableModel) tCustomer.getModel();
        tCustomer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila_point = tProduct.rowAtPoint(e.getPoint());
                if (fila_point > -1) {
                    nameCustomer = dtm.getValueAt(fila_point, 0).toString();
                }
                try {
                    Connection cn = Connections.connect();
                    PreparedStatement pst = cn.prepareStatement("select * from cliente where nombreCliente = '" + nameCustomer + "'");
                    ResultSet rs = pst.executeQuery();
                    if (rs.next()) {
                        txtNameCustomer.setText(rs.getString(1));
                        txtPhoneCustomer.setText(rs.getString(2));
                        txtDiscountCustomer.setText(rs.getString(3));
                    }
                    cn.close();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "ERROR DE CÓDIGO\n\n"
                            + "Es posible que haya ocurrido un error en el código.\n"
                            + "Si el problema persiste, contactar con el desarrollador.\n", "ERROR X", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void insertCustomer() {
        if ("".equals(txtNameCustomer.getText()) || "".equals(txtPhoneCustomer.getText()) || "".equals(txtDiscountCustomer.getText())) {
            JOptionPane.showMessageDialog(null, "CAMPOS VACIOS\n\n"
                    + "Es necesario llenar todos los campos.", "ADVERTENCIA",
                    JOptionPane.WARNING_MESSAGE);
        } else {
            try {
                Connection cn = Connections.connect();
                PreparedStatement pst = cn.prepareStatement("select nombreCliente from cliente where nombreCliente = '" + txtNameCustomer.getText() + "'");
                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    JOptionPane.showMessageDialog(null, "REGISTRO EXISTENTE\n\n"
                            + "Cliente " + rs.getString(1) + " actualmente registrado.", "ADVERTENCIA",
                            JOptionPane.WARNING_MESSAGE);
                    cn.close();
                } else {
                    cn.close();
                    try {
                        Connection cn2 = Connections.connect();
                        PreparedStatement pst2 = cn2.prepareStatement("insert into cliente values (?, ?, ?)");
                        pst2.setString(1, txtNameCustomer.getText().trim());
                        pst2.setString(2, txtPhoneCustomer.getText().trim());
                        pst2.setInt(3, Integer.parseInt(txtDiscountCustomer.getText().trim()));
                        pst2.executeUpdate();
                        JOptionPane.showMessageDialog(null, "REGISTRO EXITOSO\n\n"
                                + "Se ha registrado con exito el cliente con nombre: " + txtNameCustomer.getText().trim(), "INFORMACIÓN",
                                JOptionPane.INFORMATION_MESSAGE);
                        viewCustomer();
                        clearCustomer();
                        statisticCustomer();
                        countCustomer();
                        cn2.close();
                    } catch (SQLException e) {
                        JOptionPane.showMessageDialog(null, "ERROR DE CÓDIGO\n\n"
                                + "Es posible que haya ocurrido un error en el código.\n"
                                + "Si el problema persiste, contactar con el desarrollador.\n", "ERROR X", JOptionPane.ERROR_MESSAGE);
                    }
                }
                cn.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "ERROR DE CÓDIGO\n\n"
                        + "Es posible que haya ocurrido un error en el código.\n"
                        + "Si el problema persiste, contactar con el desarrollador.\n", "ERROR X", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateCustomer() {
        if ("".equals(txtNameCustomer.getText()) || "".equals(txtPhoneCustomer.getText()) || "".equals(txtDiscountCustomer.getText())) {
            JOptionPane.showMessageDialog(null, "CAMPOS VACIOS\n\n"
                    + "Es necesario llenar todos los campos.", "ADVERTENCIA",
                    JOptionPane.WARNING_MESSAGE);
        } else {
            try {
                Connection cn = Connections.connect();
                CallableStatement call = cn.prepareCall("{call UPDATECUSTOMER(?, ?, ?)}");
                call.setString(1, txtNameCustomer.getText().trim());
                call.setString(2, txtPhoneCustomer.getText().trim());
                call.setInt(3, Integer.parseInt(txtDiscountCustomer.getText().trim()));
                call.execute();
                JOptionPane.showMessageDialog(null, "ACTUALIZACIÓN EXITOSA\n\n"
                        + "Se ha actualizado con exito el cliente con nombre: " + txtNameCustomer.getText().trim(), "INFORMACIÓN",
                        JOptionPane.INFORMATION_MESSAGE);
                viewCustomer();
                clearCustomer();
                statisticCustomer();
                countCustomer();
                cn.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "ERROR DE CÓDIGO\n\n"
                        + "Es posible que haya ocurrido un error en el código.\n"
                        + "Si el problema persiste, contactar con el desarrollador.\n", "ERROR X", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteCustomer() {
        if (tCustomer.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(null, "NADA SELECCIONADO\n\n"
                    + "Seleccione de la tabla el cliente que desea eliminar", "ADVERTENCIA",
                    JOptionPane.WARNING_MESSAGE);
        } else {
            if (JOptionPane.showConfirmDialog(null, "¿DESEA ELIMINAR EL CLIENTE?\n\n"
                    + "Esta apunto de eliminar el cliente con nombre: " + nameCustomer + ",\n"
                    + "porfavor confirme para continuar.", "ELIMINACIÓN", JOptionPane.YES_NO_OPTION) == 0) {
                try {
                    Connection cn = Connections.connect();
                    PreparedStatement pst = cn.prepareStatement("delete from cliente where nombreCliente= '" + nameCustomer + "'");
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "ELIMINACIÓN EXITOSA\n\n"
                            + "Se ha eliminado con exito el cliente con nombre: " + nameCustomer, "INFORMACIÓN",
                            JOptionPane.INFORMATION_MESSAGE);
                    viewCustomer();
                    clearCustomer();
                    countCustomer();
                    statisticCustomer();
                    cn.close();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "ERROR DE CÓDIGO\n\n"
                            + "Es posible que haya ocurrido un error en el código.\n"
                            + "Si el problema persiste, contactar con el desarrollador.\n", "ERROR X", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void clearCustomer() {
        txtNameCustomer.setText("");
        txtPhoneCustomer.setText("");
        txtDiscountCustomer.setText("");
    }

    //SUPPLIER
    private void countSupplier() {
        try {
            Connection cn = Connections.connect();
            PreparedStatement pst = cn.prepareStatement(""
                    + "SELECT COUNT(idProveedor) "
                    + "FROM proveedor");
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                lbCountSupplier.setText(rs.getString(1));
                bStorageSupplier.setValue(rs.getInt(1));
                lbStorageSupplier.setText(rs.getString(1) + "/500");
            } else {
                lbCountSupplier.setText("0");
                bStorageSupplier.setValue(0);
                lbStorageSupplier.setText("0/500");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "ERROR DE CÓDIGO\n\n"
                    + "Es posible que haya ocurrido un error en el código.\n"
                    + "Si el problema persiste, contactar con el desarrollador.\n", "ERROR X", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void statisticSupplier() {
        taTopSupplier.setText("");
        try {
            Connection cn = Connections.connect();
            PreparedStatement pst = cn.prepareStatement(""
                    + "SELECT idProveedor, COUNT(idProveedor) "
                    + "FROM compra "
                    + "GROUP BY idProveedor "
                    + "ORDER BY idProveedor");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String x = taTopSupplier.getText();
                taTopSupplier.setText(x + "\n" + rs.getString(1));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "ERROR DE CÓDIGO\n\n"
                    + "Es posible que haya ocurrido un error en el código.\n"
                    + "Si el problema persiste, contactar con el desarrollador.\n", "ERROR X", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void viewSupplier() {
        DefaultTableModel dtm = (DefaultTableModel) tSupplier.getModel();
        dtm.setRowCount(0);
        try {
            Connection cn = Connections.connect();
            PreparedStatement pst = cn.prepareStatement(""
                    + "SELECT idProveedor, nombre, descripcion, calle, colonia, localidad, telefono, estado "
                    + "FROM proveedor "
                    + "ORDER BY nombre");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getString(1));
                v.add(rs.getString(2));
                v.add(rs.getString(3));
                v.add(rs.getString(4));
                v.add(rs.getString(5));
                v.add(rs.getString(6));
                v.add(rs.getString(7));
                v.add(rs.getString(8));
                dtm.addRow(v);
            }
            tSupplier.setModel(dtm);
            cn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "ERROR DE CÓDIGO\n\n"
                    + "Es posible que haya ocurrido un error en el código.\n"
                    + "Si el problema persiste, contactar con el desarrollador.\n", "ERROR X", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mouseClickSupplier() {
        lbRutaSupplier.setText("");
        DefaultTableModel dtm = (DefaultTableModel) tSupplier.getModel();
        tSupplier.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila_point = tSupplier.rowAtPoint(e.getPoint());
                if (fila_point > -1) {
                    idSupplier = dtm.getValueAt(fila_point, 0).toString();
                }
                try {
                    Connection cn = Connections.connect();
                    PreparedStatement pst = cn.prepareStatement("select * from proveedor where idProveedor = '" + idSupplier + "'");
                    ResultSet rs = pst.executeQuery();
                    if (rs.next()) {
                        txtIdSupplier.setText(rs.getString(1));
                        txtNameSupplier.setText(rs.getString(2));
                        txtDescriptionSuppllier.setText(rs.getString(3));
                        txtStreetSupplier.setText(rs.getString(4));
                        txtColonySupplier.setText(rs.getString(5));
                        txtLocateSupplier.setText(rs.getString(6));
                        txtPhoneSupplier.setText(rs.getString(7));
                        txtEmailSupplier.setText(rs.getString(8));
                        if ("ACTIVO".equals(rs.getString(9))) {
                            rbActiveSupplier.setSelected(true);
                        } else {
                            rbInactiveSupplier.setSelected(true);
                        }
                        Blob blob = rs.getBlob(10);
                        byte[] data = blob.getBytes(1, (int) blob.length());
                        BufferedImage img = null;
                        try {
                            img = ImageIO.read(new ByteArrayInputStream(data));
                            ImageIcon icon = new ImageIcon(img.getScaledInstance(lbPhotoSupplier.getWidth(), lbPhotoSupplier.getHeight(), Image.SCALE_SMOOTH));
                            lbPhotoSupplier.setIcon(icon);
                        } catch (IOException ex) {
                        }
                    }
                    cn.close();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "ERROR DE CÓDIGO\n\n"
                            + "Es posible que haya ocurrido un error en el código.\n"
                            + "Si el problema persiste, contactar con el desarrollador.\n", "ERROR X", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void insertSupplier() throws IOException {
        if ("".equals(txtIdSupplier.getText()) || "".equals(txtNameSupplier.getText()) || "".equals(txtDescriptionSuppllier.getText()) || "".equals(txtStreetSupplier.getText())
                || "".equals(txtColonySupplier.getText()) || "".equals(txtLocateSupplier.getText()) || "".equals(txtPhoneSupplier.getText())
                || "".equals(txtEmailSupplier.getText()) || "".equals(lbRutaSupplier.getText())) {
            JOptionPane.showMessageDialog(null, "CAMPOS VACIOS\n\n"
                    + "Es necesario llenar todos los campos.", "ADVERTENCIA",
                    JOptionPane.WARNING_MESSAGE);
        } else {
            try {
                Connection cn = Connections.connect();
                PreparedStatement pst = cn.prepareStatement("select idProveedor from proveedor where idProveedor = '" + txtIdSupplier.getText() + "'");
                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    JOptionPane.showMessageDialog(null, "REGISTRO EXISTENTE\n\n"
                            + "Proveedor " + rs.getString(1) + " actualmente registrado.", "ADVERTENCIA",
                            JOptionPane.WARNING_MESSAGE);
                    cn.close();
                } else {
                    cn.close();
                    try {
                        Connection cn2 = Connections.connect();
                        PreparedStatement pst2 = cn2.prepareStatement("insert into proveedor values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                        pst2.setString(1, txtIdSupplier.getText().trim());
                        pst2.setString(2, txtNameSupplier.getText().trim());
                        pst2.setString(3, txtDescriptionSuppllier.getText().trim());
                        pst2.setString(4, txtStreetSupplier.getText().trim());
                        pst2.setString(5, txtColonySupplier.getText().trim());
                        pst2.setString(6, txtLocateSupplier.getText().trim());
                        pst2.setString(7, txtPhoneSupplier.getText().trim());
                        pst2.setString(8, txtEmailSupplier.getText().trim());
                        if (rbActiveSupplier.isSelected()) {
                            pst2.setString(9, "ACTIVO");
                        } else if (rbInactiveSupplier.isSelected()) {
                            pst2.setString(9, "INACTIVO");
                        }
                        pst2.setBlob(10, insertPhoto(lbRutaSupplier.getText()));
                        pst2.executeUpdate();
                        JOptionPane.showMessageDialog(null, "REGISTRO EXITOSO\n\n"
                                + "Se ha registrado con exito el proveedor con id: " + txtIdSupplier.getText().trim(), "INFORMACIÓN",
                                JOptionPane.INFORMATION_MESSAGE);
                        viewSupplier();
                        clearSupplier();
                        statisticSupplier();
                        countSupplier();
                        cn2.close();
                    } catch (SQLException e) {
                        JOptionPane.showMessageDialog(null, "ERROR DE CÓDIGO\n\n"
                                + "Es posible que haya ocurrido un error en el código.\n"
                                + "Si el problema persiste, contactar con el desarrollador.\n", "ERROR X", JOptionPane.ERROR_MESSAGE);
                    }
                }
                cn.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "ERROR DE CÓDIGO\n\n"
                        + "Es posible que haya ocurrido un error en el código.\n"
                        + "Si el problema persiste, contactar con el desarrollador.\n", "ERROR X", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateSupplier() throws IOException {
        if ("".equals(txtIdSupplier.getText()) || "".equals(txtNameSupplier.getText()) || "".equals(txtDescriptionSuppllier.getText()) || "".equals(txtStreetSupplier.getText())
                || "".equals(txtColonySupplier.getText()) || "".equals(txtLocateSupplier.getText()) || "".equals(txtPhoneSupplier.getText()) || "".equals(txtEmailSupplier.getText())) {
            JOptionPane.showMessageDialog(null, "CAMPOS VACIOS\n\n"
                    + "Es necesario llenar todos los campos.", "ADVERTENCIA",
                    JOptionPane.WARNING_MESSAGE);
        } else {
            try {
                Connection cn = Connections.connect();
                CallableStatement call = cn.prepareCall("{call UPDATESUPPLIER(?, ?, ?, ?, ?, ?, ?, ?, ?)}");
                call.setString(1, txtIdSupplier.getText().trim());
                call.setString(2, txtNameSupplier.getText().trim());
                call.setString(3, txtDescriptionSuppllier.getText().trim());
                call.setString(4, txtStreetSupplier.getText().trim());
                call.setString(5, txtColonySupplier.getText().trim());
                call.setString(6, txtLocateSupplier.getText().trim());
                call.setString(7, txtPhoneSupplier.getText().trim());
                call.setString(8, txtEmailSupplier.getText().trim());
                if (rbActiveSupplier.isSelected()) {
                    call.setString(9, "ACTIVO");
                } else if (rbInactiveSupplier.isSelected()) {
                    call.setString(9, "INACTIVO");
                }
                call.execute();
                JOptionPane.showMessageDialog(null, "ACTUALIZACIÓN EXITOSA\n\n"
                        + "Se ha actualizado con exito el proveedor con id: " + txtIdSupplier.getText().trim(), "INFORMACIÓN",
                        JOptionPane.INFORMATION_MESSAGE);
                viewSupplier();
                clearSupplier();
                statisticSupplier();
                countSupplier();
                cn.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "ERROR DE CÓDIGO\n\n"
                        + "Es posible que haya ocurrido un error en el código.\n"
                        + "Si el problema persiste, contactar con el desarrollador.\n", "ERROR X", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteSupplier() throws IOException {
        if (tSupplier.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(null, "NADA SELECCIONADO\n\n"
                    + "Seleccione de la tabla el proveedor que desea eliminar", "ADVERTENCIA",
                    JOptionPane.WARNING_MESSAGE);
        } else {
            if (JOptionPane.showConfirmDialog(null, "¿DESEA ELIMINAR EL PROVEEDOR?\n\n"
                    + "Esta apunto de eliminar el proveedor con id: " + idSupplier + ",\n"
                    + "porfavor confirme para continuar.", "ELIMINACIÓN", JOptionPane.YES_NO_OPTION) == 0) {
                try {
                    Connection cn = Connections.connect();
                    PreparedStatement pst = cn.prepareStatement("delete from proveedor where idProveedor = '" + idSupplier + "'");
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "ELIMINACIÓN EXITOSA\n\n"
                            + "Se ha eliminado con exito el proveedor con id: " + idSupplier, "INFORMACIÓN",
                            JOptionPane.INFORMATION_MESSAGE);
                    viewSupplier();
                    clearSupplier();
                    countSupplier();
                    statisticSupplier();
                    cn.close();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "ERROR DE CÓDIGO\n\n"
                            + "Es posible que haya ocurrido un error en el código.\n"
                            + "Si el problema persiste, contactar con el desarrollador.\n", "ERROR X", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void clearSupplier() throws IOException {
        txtIdSupplier.setText("");
        txtNameSupplier.setText("");
        txtDescriptionSuppllier.setText("");
        txtStreetSupplier.setText("");
        txtColonySupplier.setText("");
        txtLocateSupplier.setText("");
        txtPhoneSupplier.setText("");
        txtEmailSupplier.setText("");
        rbActiveSupplier.setSelected(true);
        BufferedImage imgUno = ImageIO.read(getClass().getResource("/icons/emptyImage.png"));
        ImageIcon img1 = new ImageIcon(imgUno.getScaledInstance(lbPhotoSupplier.getWidth(), lbPhotoSupplier.getHeight(), Image.SCALE_SMOOTH));
        lbPhotoSupplier.setIcon(img1);
        lbRutaSupplier.setText("");
    }

    //STAFF
    private void countStaff() {
        try {
            Connection cn = Connections.connect();
            PreparedStatement pst = cn.prepareStatement(""
                    + "SELECT COUNT(idUsuario) "
                    + "FROM usuario");
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                lbCountStaff.setText(rs.getString(1));
                bStorageStaff.setValue(rs.getInt(1));
                lbStorageStaff.setText(rs.getString(1) + "/100");
            } else {
                lbCountStaff.setText("");
                bStorageStaff.setValue(0);
                lbStorageStaff.setText("0/100");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "ERROR DE CÓDIGO\n\n"
                    + "Es posible que haya ocurrido un error en el código.\n"
                    + "Si el problema persiste, contactar con el desarrollador.\n", "ERROR X", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void statisticStaff() {
        taJobStaff.setText("");
        taCantJobStaff.setText("");
        try {
            Connection cn = Connections.connect();
            PreparedStatement pst = cn.prepareStatement(""
                    + "SELECT nombreCargo, COUNT(idUsuario) "
                    + "FROM usuario "
                    + "GROUP BY nombreCargo "
                    + "ORDER BY nombreCargo");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String x = taJobStaff.getText();
                String y = taCantJobStaff.getText();
                taJobStaff.setText(x + "\n" + rs.getString(1));
                taCantJobStaff.setText(y + "\n" + rs.getString(2));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "ERROR DE CÓDIGO\n\n"
                    + "Es posible que haya ocurrido un error en el código.\n"
                    + "Si el problema persiste, contactar con el desarrollador.\n", "ERROR X", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cbJob() {
        cbJobStaff.removeAllItems();
        try {
            Connection cn = Connections.connect();
            PreparedStatement pst = cn.prepareStatement(""
                    + "SELECT nombreCargo "
                    + "FROM cargo "
                    + "ORDER BY nombreCargo");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                cbJobStaff.addItem(rs.getString(1));
            }
            cn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "ERROR DE CÓDIGO\n\n"
                    + "Es posible que haya ocurrido un error en el código.\n"
                    + "Si el problema persiste, contactar con el desarrollador.\n", "ERROR X", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void viewStaff() {
        DefaultTableModel dtm = (DefaultTableModel) tStaff.getModel();
        dtm.setRowCount(0);
        try {
            Connection cn = Connections.connect();
            PreparedStatement pst = cn.prepareStatement(""
                    + "SELECT idUsuario, nombre, apellidoP, apellidoM, nombreCargo, usuario, estado "
                    + "FROM usuario "
                    + "ORDER BY nombre");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getString(1));
                v.add(rs.getString(2));
                v.add(rs.getString(3));
                v.add(rs.getString(4));
                v.add(rs.getString(5));
                v.add(rs.getString(6));
                v.add(rs.getString(7));
                dtm.addRow(v);
            }
            tStaff.setModel(dtm);
            cn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "ERROR DE CÓDIGO\n\n"
                    + "Es posible que haya ocurrido un error en el código.\n"
                    + "Si el problema persiste, contactar con el desarrollador.\n", "ERROR X", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mouseClickStaff() {
        lbRutaStaff.setText("");
        DefaultTableModel dtm = (DefaultTableModel) tStaff.getModel();
        tStaff.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila_point = tStaff.rowAtPoint(e.getPoint());
                if (fila_point > -1) {
                    idStaff = dtm.getValueAt(fila_point, 0).toString();
                }
                try {
                    Connection cn = Connections.connect();
                    PreparedStatement pst = cn.prepareStatement("select * from usuario where idUsuario = '" + idStaff + "'");
                    ResultSet rs = pst.executeQuery();
                    if (rs.next()) {
                        txtIdUser.setText(rs.getString(1));
                        txtNameStaff.setText(rs.getString(2));
                        txtAPStaff.setText(rs.getString(3));
                        txtAMStaff.setText(rs.getString(4));
                        cbJobStaff.setSelectedItem(rs.getString(5));
                        txtPhoneStaff.setText(rs.getString(6));
                        txtEmailStaff.setText(rs.getString(7));
                        txtUserStaff.setText(rs.getString(8));
                        txtPasswordStaff.setText(rs.getString(9));
                        if ("ACTIVO".equals(rs.getString(10))) {
                            rbActiveStaff.setSelected(true);
                        } else {
                            rbInactiveStaff.setSelected(true);
                        }
                        Blob blob = rs.getBlob(12);
                        byte[] data = blob.getBytes(1, (int) blob.length());
                        BufferedImage img = null;
                        try {
                            img = ImageIO.read(new ByteArrayInputStream(data));
                            ImageIcon icon = new ImageIcon(img.getScaledInstance(lbPhotoStaff.getWidth(), lbPhotoStaff.getHeight(), Image.SCALE_SMOOTH));
                            lbPhotoStaff.setIcon(icon);
                        } catch (IOException ex) {
                        }
                    }
                    cn.close();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "ERROR DE CÓDIGO\n\n"
                            + "Es posible que haya ocurrido un error en el código.\n"
                            + "Si el problema persiste, contactar con el desarrollador.\n", "ERROR X", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void insertStaff() throws IOException {
        if ("".equals(txtIdUser.getText()) || "".equals(txtNameStaff.getText()) || "".equals(txtAPStaff.getText()) || "".equals(txtAMStaff.getText())
                || "".equals(txtPhoneStaff.getText()) || "".equals(txtEmailStaff.getText()) || "".equals(txtUserStaff.getText()) || "".equals(txtPasswordStaff.getText())
                || "".equals(lbRutaStaff.getText().trim())) {
            JOptionPane.showMessageDialog(null, "CAMPOS VACIOS\n\n"
                    + "Es necesario llenar todos los campos.", "ADVERTENCIA",
                    JOptionPane.WARNING_MESSAGE);
        } else {
            try {
                Connection cn = Connections.connect();
                PreparedStatement pst = cn.prepareStatement("select idUsuario from usuario where idUsuario = '" + txtIdUser.getText() + "'");
                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    JOptionPane.showMessageDialog(null, "REGISTRO EXISTENTE\n\n"
                            + "Usuario " + rs.getString(1) + " actualmente registrado.", "ADVERTENCIA",
                            JOptionPane.WARNING_MESSAGE);
                    cn.close();
                } else {
                    cn.close();
                    try {
                        Connection cn2 = Connections.connect();
                        PreparedStatement pst2 = cn2.prepareStatement("insert into usuario values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                        pst2.setString(1, txtIdUser.getText().trim());
                        pst2.setString(2, txtNameStaff.getText().trim());
                        pst2.setString(3, txtAPStaff.getText().trim());
                        pst2.setString(4, txtAMStaff.getText().trim());
                        pst2.setString(5, cbJobStaff.getSelectedItem().toString());
                        pst2.setString(6, txtPhoneStaff.getText().trim());
                        pst2.setString(7, txtEmailStaff.getText().trim());
                        pst2.setString(8, txtUserStaff.getText().trim());
                        pst2.setString(9, txtPasswordStaff.getText().trim());
                        if (rbActiveStaff.isSelected()) {
                            pst2.setString(10, "ACTIVO");
                        } else if (rbInactiveStaff.isSelected()) {
                            pst2.setString(10, "INACTIVO");
                        }
                        pst2.setString(11, "OFFLINE");
                        pst2.setBlob(12, insertPhoto(lbRutaStaff.getText()));
                        pst2.executeUpdate();
                        JOptionPane.showMessageDialog(null, "REGISTRO EXITOSO\n\n"
                                + "Se ha registrado con exito el usuario con id: " + txtIdUser.getText().trim(), "INFORMACIÓN",
                                JOptionPane.INFORMATION_MESSAGE);
                        viewStaff();
                        clearStaff();
                        statisticStaff();
                        countStaff();
                        cn2.close();
                    } catch (SQLException e) {
                        JOptionPane.showMessageDialog(null, "ERROR DE CÓDIGO\n\n"
                                + "Es posible que haya ocurrido un error en el código.\n"
                                + "Si el problema persiste, contactar con el desarrollador.\n", "ERROR X", JOptionPane.ERROR_MESSAGE);
                    }
                }
                cn.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "ERROR DE CÓDIGO\n\n"
                        + "Es posible que haya ocurrido un error en el código.\n"
                        + "Si el problema persiste, contactar con el desarrollador.\n", "ERROR X", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateStaff() throws IOException {
        if ("".equals(txtIdUser.getText()) || "".equals(txtNameStaff.getText()) || "".equals(txtAPStaff.getText()) || "".equals(txtAMStaff.getText())
                || "".equals(txtPhoneStaff.getText()) || "".equals(txtEmailStaff.getText()) || "".equals(txtUserStaff.getText()) || "".equals(txtPasswordStaff.getText())) {
            JOptionPane.showMessageDialog(null, "CAMPOS VACIOS\n\n"
                    + "Es necesario llenar todos los campos.", "ADVERTENCIA",
                    JOptionPane.WARNING_MESSAGE);
        } else {
            try {
                Connection cn = Connections.connect();
                CallableStatement call = cn.prepareCall("{call UPDATESTAFF(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
                call.setString(1, txtIdUser.getText().trim());
                call.setString(2, txtNameStaff.getText().trim());
                call.setString(3, txtAPStaff.getText().trim());
                call.setString(4, txtAMStaff.getText().trim());
                call.setString(5, cbJobStaff.getSelectedItem().toString());
                call.setString(6, txtPhoneStaff.getText().trim());
                call.setString(7, txtEmailStaff.getText().trim());
                call.setString(8, txtUserStaff.getText().trim());
                call.setString(9, txtPasswordStaff.getText().trim());
                if (rbActiveStaff.isSelected()) {
                    call.setString(10, "ACTIVO");
                } else if (rbInactiveStaff.isSelected()) {
                    call.setString(10, "INACTIVO");
                }
                call.setString(11, "OFFLINE");
                call.execute();
                JOptionPane.showMessageDialog(null, "ACTUALIZACIÓN EXITOSA\n\n"
                        + "Se ha actualizado con exito el usuario con id: " + txtIdUser.getText().trim(), "INFORMACIÓN",
                        JOptionPane.INFORMATION_MESSAGE);
                viewStaff();
                clearStaff();
                statisticStaff();
                countStaff();
                cn.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "ERROR DE CÓDIGO\n\n"
                        + "Es posible que haya ocurrido un error en el código.\n"
                        + "Si el problema persiste, contactar con el desarrollador.\n", "ERROR X", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteStaff() throws IOException {
        if (tStaff.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(null, "NADA SELECCIONADO\n\n"
                    + "Seleccione de la tabla el usuario que desea eliminar", "ADVERTENCIA",
                    JOptionPane.WARNING_MESSAGE);
        } else {
            if (JOptionPane.showConfirmDialog(null, "¿DESEA ELIMINAR EL USUARIO?\n\n"
                    + "Esta apunto de eliminar el usuario con id: " + idStaff + ",\n"
                    + "porfavor confirme para continuar.", "ELIMINACIÓN", JOptionPane.YES_NO_OPTION) == 0) {
                try {
                    Connection cn = Connections.connect();
                    PreparedStatement pst = cn.prepareStatement("delete from usuario where idUsuario = '" + idStaff + "'");
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "ELIMINACIÓN EXITOSA\n\n"
                            + "Se ha eliminado con exito el usuario con id: " + idStaff, "INFORMACIÓN",
                            JOptionPane.INFORMATION_MESSAGE);
                    viewStaff();
                    clearStaff();
                    countStaff();
                    statisticStaff();
                    cn.close();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "ERROR DE CÓDIGO\n\n"
                            + "Es posible que haya ocurrido un error en el código.\n"
                            + "Si el problema persiste, contactar con el desarrollador.\n", "ERROR X", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void clearStaff() throws IOException {
        txtIdUser.setText("");
        txtNameStaff.setText("");
        txtAPStaff.setText("");
        txtAMStaff.setText("");
        cbJobStaff.setSelectedIndex(0);
        txtPhoneStaff.setText("");
        txtEmailStaff.setText("");
        txtUserStaff.setText("");
        txtPasswordStaff.setText("");
        rbActiveStaff.setSelected(true);
        BufferedImage imgUno = ImageIO.read(getClass().getResource("/icons/emptyImage.png"));
        ImageIcon img1 = new ImageIcon(imgUno.getScaledInstance(lbPhotoStaff.getWidth(), lbPhotoStaff.getHeight(), Image.SCALE_SMOOTH));
        lbPhotoStaff.setIcon(img1);
        lbRutaStaff.setText("");
    }

    //JOB
    private void countJob() {
        try {
            Connection cn = Connections.connect();
            PreparedStatement pst = cn.prepareStatement(""
                    + "SELECT COUNT(nombreCargo) "
                    + "FROM cargo");
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                lbCountJob.setText(rs.getString(1));
                bStorageJob.setValue(rs.getInt(1));
                lbStorageJob.setText(rs.getString(1) + "/20");
            } else {
                lbCountJob.setText("0");
                bStorageJob.setValue(0);
                lbStorageJob.setText("0/20");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "ERROR DE CÓDIGO\n\n"
                    + "Es posible que haya ocurrido un error en el código.\n"
                    + "Si el problema persiste, contactar con el desarrollador.\n", "ERROR X", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void viewJob() {
        DefaultTableModel dtm = (DefaultTableModel) tJob.getModel();
        dtm.setRowCount(0);
        try {
            Connection cn = Connections.connect();
            PreparedStatement pst = cn.prepareStatement(""
                    + "SELECT * "
                    + "FROM cargo "
                    + "ORDER BY nombreCargo");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getString(1));
                v.add(rs.getString(2));
                v.add(rs.getString(3));
                v.add(rs.getString(4));
                v.add(rs.getString(5));
                v.add(rs.getString(6));
                v.add(rs.getString(7));
                v.add(rs.getString(8));
                v.add(rs.getString(9));
                v.add(rs.getString(10));
                v.add(rs.getString(11));
                v.add(rs.getString(12));
                v.add(rs.getString(13));
                v.add(rs.getString(14));
                dtm.addRow(v);
            }
            tJob.setModel(dtm);
            cn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "ERROR DE CÓDIGO\n\n"
                    + "Es posible que haya ocurrido un error en el código.\n"
                    + "Si el problema persiste, contactar con el desarrollador.\n", "ERROR X", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mouseClickJob() {
        DefaultTableModel dtm = (DefaultTableModel) tJob.getModel();
        tJob.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila_point = tJob.rowAtPoint(e.getPoint());
                if (fila_point > -1) {
                    nameJob = dtm.getValueAt(fila_point, 0).toString();
                }
                try {
                    Connection cn = Connections.connect();
                    PreparedStatement pst = cn.prepareStatement("select * from cargo where nombreCargo = '" + nameJob + "'");
                    ResultSet rs = pst.executeQuery();
                    if (rs.next()) {
                        clearJob();
                        txtNameJob.setText(rs.getString(1));
                        if ("SI".equals(rs.getString(2))) {
                            rbRegProduct.setSelected(true);
                        }
                        if ("SI".equals(rs.getString(3))) {
                            rbRegSale.setSelected(true);
                        }
                        if ("SI".equals(rs.getString(4))) {
                            rbRegBuy.setSelected(true);
                        }
                        if ("SI".equals(rs.getString(5))) {
                            rbRegCustomer.setSelected(true);
                        }
                        if ("SI".equals(rs.getString(6))) {
                            rbRegSupplier.setSelected(true);
                        }
                        if ("SI".equals(rs.getString(7))) {
                            rbRegStaff.setSelected(true);
                        }
                        if ("SI".equals(rs.getString(8))) {
                            rbConStatistic.setSelected(true);
                        }
                        if ("SI".equals(rs.getString(9))) {
                            rbConSale.setSelected(true);
                        }
                        if ("SI".equals(rs.getString(10))) {
                            rbConBuy.setSelected(true);
                        }
                        if ("SI".equals(rs.getString(11))) {
                            rbCanSale.setSelected(true);
                        }
                        if ("SI".equals(rs.getString(12))) {
                            rbCanBuy.setSelected(true);
                        }
                        if ("SI".equals(rs.getString(13))) {
                            rbRegJob.setSelected(true);
                        }
                        if ("SI".equals(rs.getString(14))) {
                            rbSettingJob.setSelected(true);
                        }
                    }
                    cn.close();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "ERROR DE CÓDIGO\n\n"
                            + "Es posible que haya ocurrido un error en el código.\n"
                            + "Si el problema persiste, contactar con el desarrollador.\n", "ERROR X", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void insertJob() {
        if ("".equals(txtNameJob.getText())) {
            JOptionPane.showMessageDialog(null, "CAMPOS VACIOS\n\n"
                    + "Es necesario llenar todos los campos.", "ADVERTENCIA",
                    JOptionPane.WARNING_MESSAGE);
        } else {
            try {
                Connection cn = Connections.connect();
                PreparedStatement pst = cn.prepareStatement("select nombreCargo from cargo where nombreCargo = '" + txtNameJob.getText() + "'");
                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    JOptionPane.showMessageDialog(null, "REGISTRO EXISTENTE\n\n"
                            + "Cargo " + rs.getString(1) + " actualmente registrado.", "ADVERTENCIA",
                            JOptionPane.WARNING_MESSAGE);
                    cn.close();
                } else {
                    cn.close();
                    String regProduct, regSale, regBuy, regCustomer, regSupplier, regStaff, conStatistic, conSale, conBuy, canSale, canBuy, regJob, setting;
                    if (rbRegProduct.isSelected()) {
                        regProduct = "SI";
                    } else {
                        regProduct = "NO";
                    }
                    if (rbRegSale.isSelected()) {
                        regSale = "SI";
                    } else {
                        regSale = "NO";
                    }
                    if (rbRegBuy.isSelected()) {
                        regBuy = "SI";
                    } else {
                        regBuy = "NO";
                    }
                    if (rbRegCustomer.isSelected()) {
                        regCustomer = "SI";
                    } else {
                        regCustomer = "NO";
                    }
                    if (rbRegSupplier.isSelected()) {
                        regSupplier = "SI";
                    } else {
                        regSupplier = "NO";
                    }
                    if (rbRegStaff.isSelected()) {
                        regStaff = "SI";
                    } else {
                        regStaff = "NO";
                    }
                    if (rbConStatistic.isSelected()) {
                        conStatistic = "SI";
                    } else {
                        conStatistic = "NO";
                    }
                    if (rbConSale.isSelected()) {
                        conSale = "SI";
                    } else {
                        conSale = "NO";
                    }
                    if (rbConBuy.isSelected()) {
                        conBuy = "SI";
                    } else {
                        conBuy = "NO";
                    }
                    if (rbCanSale.isSelected()) {
                        canSale = "SI";
                    } else {
                        canSale = "NO";
                    }
                    if (rbCanBuy.isSelected()) {
                        canBuy = "SI";
                    } else {
                        canBuy = "NO";
                    }
                    if (rbRegJob.isSelected()) {
                        regJob = "SI";
                    } else {
                        regJob = "NO";
                    }
                    if (rbSettingJob.isSelected()) {
                        setting = "SI";
                    } else {
                        setting = "NO";
                    }
                    try {
                        Connection cn2 = Connections.connect();
                        PreparedStatement pst2 = cn2.prepareStatement("insert into cargo values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                        pst2.setString(1, txtNameJob.getText().trim());
                        pst2.setString(2, regProduct);
                        pst2.setString(3, regSale);
                        pst2.setString(4, regBuy);
                        pst2.setString(5, regCustomer);
                        pst2.setString(6, regSupplier);
                        pst2.setString(7, regStaff);
                        pst2.setString(8, conStatistic);
                        pst2.setString(9, conSale);
                        pst2.setString(10, conBuy);
                        pst2.setString(11, canSale);
                        pst2.setString(12, canBuy);
                        pst2.setString(13, regJob);
                        pst2.setString(14, setting);
                        pst2.executeUpdate();
                        JOptionPane.showMessageDialog(null, "REGISTRO EXITOSO\n\n"
                                + "Se ha registrado con exito el cargo nombrado: " + txtNameJob.getText().trim(), "INFORMACIÓN",
                                JOptionPane.INFORMATION_MESSAGE);
                        viewJob();
                        clearJob();
                        countJob();
                        cn2.close();
                    } catch (SQLException e) {
                        JOptionPane.showMessageDialog(null, "ERROR DE CÓDIGO\n\n"
                                + "Es posible que haya ocurrido un error en el código.\n"
                                + "Si el problema persiste, contactar con el desarrollador.\n", "ERROR X", JOptionPane.ERROR_MESSAGE);
                    }
                }
                cn.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "ERROR DE CÓDIGO\n\n"
                        + "Es posible que haya ocurrido un error en el código.\n"
                        + "Si el problema persiste, contactar con el desarrollador.\n", "ERROR X", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateJob() {
        if ("".equals(txtNameJob.getText())) {
            JOptionPane.showMessageDialog(null, "CAMPOS VACIOS\n\n"
                    + "Es necesario llenar todos los campos.", "ADVERTENCIA",
                    JOptionPane.WARNING_MESSAGE);
        } else {
            String regProduct, regSale, regBuy, regCustomer, regSupplier, regStaff, conStatistic, conSale, conBuy, canSale, canBuy, regJob, setting;
            if (rbRegProduct.isSelected()) {
                regProduct = "SI";
            } else {
                regProduct = "NO";
            }
            if (rbRegSale.isSelected()) {
                regSale = "SI";
            } else {
                regSale = "NO";
            }
            if (rbRegBuy.isSelected()) {
                regBuy = "SI";
            } else {
                regBuy = "NO";
            }
            if (rbRegCustomer.isSelected()) {
                regCustomer = "SI";
            } else {
                regCustomer = "NO";
            }
            if (rbRegSupplier.isSelected()) {
                regSupplier = "SI";
            } else {
                regSupplier = "NO";
            }
            if (rbRegStaff.isSelected()) {
                regStaff = "SI";
            } else {
                regStaff = "NO";
            }
            if (rbConStatistic.isSelected()) {
                conStatistic = "SI";
            } else {
                conStatistic = "NO";
            }
            if (rbConSale.isSelected()) {
                conSale = "SI";
            } else {
                conSale = "NO";
            }
            if (rbConBuy.isSelected()) {
                conBuy = "SI";
            } else {
                conBuy = "NO";
            }
            if (rbCanSale.isSelected()) {
                canSale = "SI";
            } else {
                canSale = "NO";
            }
            if (rbCanBuy.isSelected()) {
                canBuy = "SI";
            } else {
                canBuy = "NO";
            }
            if (rbRegJob.isSelected()) {
                regJob = "SI";
            } else {
                regJob = "NO";
            }
            if (rbSettingJob.isSelected()) {
                setting = "SI";
            } else {
                setting = "NO";
            }
            try {
                Connection cn = Connections.connect();
                CallableStatement call = cn.prepareCall("{call UPDATEJOB(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
                call.setString(1, txtNameJob.getText().trim());
                call.setString(2, regProduct);
                call.setString(3, regSale);
                call.setString(4, regBuy);
                call.setString(5, regCustomer);
                call.setString(6, regSupplier);
                call.setString(7, regStaff);
                call.setString(8, conStatistic);
                call.setString(9, conSale);
                call.setString(10, conBuy);
                call.setString(11, canSale);
                call.setString(12, canBuy);
                call.setString(13, regJob);
                call.setString(14, setting);
                call.execute();
                JOptionPane.showMessageDialog(null, "ACTUALIZACIÓN EXITOSA\n\n"
                        + "Se ha actualizado con exito el cargo nombrado: " + txtNameJob.getText().trim(), "INFORMACIÓN",
                        JOptionPane.INFORMATION_MESSAGE);
                viewJob();
                clearJob();
                countJob();
                cn.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "ERROR DE CÓDIGO\n\n"
                        + "Es posible que haya ocurrido un error en el código.\n"
                        + "Si el problema persiste, contactar con el desarrollador.\n", "ERROR X", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteJob() {
        if (tJob.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(null, "NADA SELECCIONADO\n\n"
                    + "Seleccione de la tabla el cargo que desea eliminar", "ADVERTENCIA",
                    JOptionPane.WARNING_MESSAGE);
        } else {
            if (JOptionPane.showConfirmDialog(null, "¿DESEA ELIMINAR EL CARGO?\n\n"
                    + "Esta apunto de eliminar el cargo nombrado: " + nameJob + ",\n"
                    + "porfavor confirme para continuar.", "ELIMINACIÓN", JOptionPane.YES_NO_OPTION) == 0) {
                try {
                    Connection cn = Connections.connect();
                    PreparedStatement pst = cn.prepareStatement("delete from cargo where nombreCargo = '" + nameJob + "'");
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "ELIMINACIÓN EXITOSA\n\n"
                            + "Se ha eliminado con exito el cargo nombrado: " + nameJob, "INFORMACIÓN",
                            JOptionPane.INFORMATION_MESSAGE);
                    viewJob();
                    clearJob();
                    countJob();
                    cn.close();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "ERROR DE CÓDIGO\n\n"
                            + "Es posible que haya ocurrido un error en el código.\n"
                            + "Si el problema persiste, contactar con el desarrollador.\n", "ERROR X", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void clearJob() {
        txtNameJob.setText("");
        rbRegSale.setSelected(false);
        rbRegBuy.setSelected(false);
        rbRegProduct.setSelected(false);
        rbRegCustomer.setSelected(false);
        rbRegSupplier.setSelected(false);
        rbRegStaff.setSelected(false);
        rbRegJob.setSelected(false);
        rbConSale.setSelected(false);
        rbConBuy.setSelected(false);
        rbCanSale.setSelected(false);
        rbCanBuy.setSelected(false);
        rbSettingJob.setSelected(false);
        rbConStatistic.setSelected(false);
    }

    //SETTINGS
    private void boldLetter() {
        btnTool2.setFont(new Font("Arial", 0, 14));
        btnTool1.setFont(new Font("Arial", 0, 14));
        btnTool5.setFont(new Font("Arial", 0, 14));
        btnTool3.setFont(new Font("Arial", 0, 14));
        btnTool4.setFont(new Font("Arial", 0, 14));
    }

    private void setVisibleSetting() {
        pTool2.setVisible(false);
        pTool1.setVisible(false);
        pTool5.setVisible(false);
    }

    private void viewSetting() {
        try {
            Connection cn = Connections.connect();
            PreparedStatement pst = cn.prepareStatement(""
                    + "SELECT * "
                    + "FROM establecimiento ");
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                txtNamePlantSetting.setText(rs.getString(2));
                txtDescriptionSetting.setText(rs.getString(3));
                txtStreetPlantSetting.setText(rs.getString(4));
                txtColonyPlantSetting.setText(rs.getString(5));
                txtLocatePlantSetting.setText(rs.getString(6));
                txtPhonePlantSetting.setText(rs.getString(8));
                txtEmailPlantSetting.setText(rs.getString(7));
            }
            cn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "ERROR DE CÓDIGO\n\n"
                    + "Es posible que haya ocurrido un error en el código.\n"
                    + "Si el problema persiste, contactar con el desarrollador.\n", "ERROR X", JOptionPane.ERROR_MESSAGE);
        }
        
    }

    private void updateColor() {
        try {
            Connection cn = Connections.connect();
            PreparedStatement pst = cn.prepareStatement("update configuracion set color = '" + txtColorSetting.getText().trim() + "'");
            pst.execute();
            JOptionPane.showMessageDialog(null, "ACTUALIZACIÓN EXITOSA\n\n"
                    + "Se ha actualizado con exito.", "INFORMACIÓN",
                    JOptionPane.INFORMATION_MESSAGE);
            cn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "ERROR DE CÓDIGO\n\n"
                    + "Es posible que haya ocurrido un error en el código.\n"
                    + "Si el problema persiste, contactar con el desarrollador.\n", "ERROR X", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updatePlant() {
        try {
            Connection cn = Connections.connect();
            PreparedStatement pst = cn.prepareStatement("update establecimiento set nombre = '" + txtNamePlantSetting.getText().trim()
                    + "', descripcion = '" + txtDescriptionSetting.getText().trim()
                    + "', calle = '" + txtStreetPlantSetting.getText().trim()
                    + "', colonia = '" + txtColonyPlantSetting.getText().trim()
                    + "', localidad = '" + txtLocatePlantSetting.getText().trim()
                    + "', email = '" + txtEmailPlantSetting.getText().trim()
                    + "', telefono = '" + txtPhonePlantSetting.getText().trim() + "'");
            pst.execute();
            JOptionPane.showMessageDialog(null, "ACTUALIZACIÓN EXITOSA\n\n"
                    + "Se ha actualizado con exito.", "INFORMACIÓN",
                    JOptionPane.INFORMATION_MESSAGE);
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

        searchProduct = new javax.swing.ButtonGroup();
        statusProduct = new javax.swing.ButtonGroup();
        searchSupplier = new javax.swing.ButtonGroup();
        statusSupplier = new javax.swing.ButtonGroup();
        searchStaff = new javax.swing.ButtonGroup();
        statusStaff = new javax.swing.ButtonGroup();
        stylesSetting = new javax.swing.ButtonGroup();
        pMenu = new javax.swing.JPanel();
        btnSetting = new javax.swing.JButton();
        btnHome = new javax.swing.JButton();
        btnProduct = new javax.swing.JButton();
        btnSale = new javax.swing.JButton();
        btnBuy = new javax.swing.JButton();
        btnCustomer = new javax.swing.JButton();
        btnSupplier = new javax.swing.JButton();
        btnStaff = new javax.swing.JButton();
        btnStatistic = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        btnJob = new javax.swing.JButton();
        lbLogoMenu = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        pSale = new javax.swing.JPanel();
        jPanel23 = new javax.swing.JPanel();
        lbTitleSale = new javax.swing.JLabel();
        lbIdUser6 = new javax.swing.JLabel();
        btnExit7 = new javax.swing.JButton();
        jPanel24 = new javax.swing.JPanel();
        lbPhotoSale = new javax.swing.JLabel();
        jLabel62 = new javax.swing.JLabel();
        txtNoSale = new javax.swing.JTextField();
        jLabel63 = new javax.swing.JLabel();
        txtCodeSale = new javax.swing.JTextField();
        jLabel66 = new javax.swing.JLabel();
        txtCustomerSale = new javax.swing.JTextField();
        jLabel67 = new javax.swing.JLabel();
        txtDiscountSale = new javax.swing.JTextField();
        btnSearchCustomerSale = new javax.swing.JButton();
        btnSearchProductSale = new javax.swing.JButton();
        btnInputSale = new javax.swing.JButton();
        btnOutputSale = new javax.swing.JButton();
        jLabel69 = new javax.swing.JLabel();
        btnPriceSale = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        tSale = new javax.swing.JTable();
        pBannerSale = new javax.swing.JPanel();
        lbBannerSale = new javax.swing.JLabel();
        lbSubtotalSale = new javax.swing.JLabel();
        lbFrankSale = new javax.swing.JLabel();
        jLabel61 = new javax.swing.JLabel();
        lbTotalSale = new javax.swing.JLabel();
        lbDiscountSale = new javax.swing.JLabel();
        jLabel64 = new javax.swing.JLabel();
        jLabel65 = new javax.swing.JLabel();
        btnAddSale = new javax.swing.JButton();
        btnSaveSale = new javax.swing.JButton();
        btnDeleteSale = new javax.swing.JButton();
        jSeparator5 = new javax.swing.JSeparator();
        jLabel68 = new javax.swing.JLabel();
        jLabel70 = new javax.swing.JLabel();
        jLabel71 = new javax.swing.JLabel();
        pProduct = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        lbTitleProduct = new javax.swing.JLabel();
        lbIdUser1 = new javax.swing.JLabel();
        btnExit1 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        txtSearchProduct = new javax.swing.JTextField();
        rbDepartmentProduct = new javax.swing.JRadioButton();
        rbCodeProduct = new javax.swing.JRadioButton();
        rbNameProduct = new javax.swing.JRadioButton();
        rbBrendProduct = new javax.swing.JRadioButton();
        jPanel1 = new javax.swing.JPanel();
        lbCountProduct = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        taCantDepartment = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        taDepartment = new javax.swing.JTextArea();
        bStorageProduct = new javax.swing.JProgressBar();
        lbStorageProduct = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tProduct = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        btnFavorite = new javax.swing.JButton();
        lbPhotoProduct = new javax.swing.JLabel();
        lbRutaProduct = new javax.swing.JLabel();
        btnDeleteProduct = new javax.swing.JButton();
        btnAddProduct = new javax.swing.JButton();
        btnEditProduct = new javax.swing.JButton();
        btnSaveProduct = new javax.swing.JButton();
        jPanel25 = new javax.swing.JPanel();
        txtStockProduct = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        txtStockMaxProduct = new javax.swing.JTextField();
        txtStockMinProduct = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        rbInactiveProduct = new javax.swing.JRadioButton();
        rbActiveProduct = new javax.swing.JRadioButton();
        jLabel17 = new javax.swing.JLabel();
        jPanel26 = new javax.swing.JPanel();
        txtCodeProduct = new javax.swing.JTextField();
        txtNameProduct = new javax.swing.JTextField();
        txtBrendProduct = new javax.swing.JTextField();
        cbDepartmentProduct = new javax.swing.JComboBox<>();
        jLabel14 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jPanel27 = new javax.swing.JPanel();
        txtBuyProduct = new javax.swing.JTextField();
        txtGainProduct = new javax.swing.JTextField();
        txtDiscountProduct = new javax.swing.JTextField();
        txtSaleProduct = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jSeparator7 = new javax.swing.JSeparator();
        jSeparator6 = new javax.swing.JSeparator();
        pJob = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        lbTitleJob = new javax.swing.JLabel();
        lbIdUser5 = new javax.swing.JLabel();
        btnExit6 = new javax.swing.JButton();
        jLabel55 = new javax.swing.JLabel();
        txtSearchJob = new javax.swing.JTextField();
        rbNameJob = new javax.swing.JRadioButton();
        jPanel20 = new javax.swing.JPanel();
        lbCountJob = new javax.swing.JLabel();
        bStorageJob = new javax.swing.JProgressBar();
        lbStorageJob = new javax.swing.JLabel();
        jLabel57 = new javax.swing.JLabel();
        jTabbedPane5 = new javax.swing.JTabbedPane();
        jPanel21 = new javax.swing.JPanel();
        jScrollPane12 = new javax.swing.JScrollPane();
        tJob = new javax.swing.JTable();
        jPanel22 = new javax.swing.JPanel();
        jLabel58 = new javax.swing.JLabel();
        txtNameJob = new javax.swing.JTextField();
        btnDeleteJob = new javax.swing.JButton();
        btnAddJob = new javax.swing.JButton();
        btnEditJob = new javax.swing.JButton();
        btnSaveJob = new javax.swing.JButton();
        jPanel35 = new javax.swing.JPanel();
        rbRegSale = new javax.swing.JRadioButton();
        rbRegBuy = new javax.swing.JRadioButton();
        rbRegProduct = new javax.swing.JRadioButton();
        rbRegCustomer = new javax.swing.JRadioButton();
        rbRegSupplier = new javax.swing.JRadioButton();
        rbRegStaff = new javax.swing.JRadioButton();
        rbRegJob = new javax.swing.JRadioButton();
        jPanel36 = new javax.swing.JPanel();
        rbConSale = new javax.swing.JRadioButton();
        rbConBuy = new javax.swing.JRadioButton();
        rbConStatistic = new javax.swing.JRadioButton();
        jPanel37 = new javax.swing.JPanel();
        rbCanBuy = new javax.swing.JRadioButton();
        rbCanSale = new javax.swing.JRadioButton();
        jPanel38 = new javax.swing.JPanel();
        rbSettingJob = new javax.swing.JRadioButton();
        jSeparator15 = new javax.swing.JSeparator();
        jSeparator14 = new javax.swing.JSeparator();
        pSupplier = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        lbTitleSupplier = new javax.swing.JLabel();
        lbIdUser3 = new javax.swing.JLabel();
        btnExit4 = new javax.swing.JButton();
        jLabel29 = new javax.swing.JLabel();
        txtSearchSupplier = new javax.swing.JTextField();
        rbPhoneSupplier = new javax.swing.JRadioButton();
        rbIdSupplier = new javax.swing.JRadioButton();
        rbNameSupplier = new javax.swing.JRadioButton();
        rbLocationSupplier = new javax.swing.JRadioButton();
        jPanel12 = new javax.swing.JPanel();
        lbCountSupplier = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        taTopSupplier = new javax.swing.JTextArea();
        bStorageSupplier = new javax.swing.JProgressBar();
        lbStorageSupplier = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jTabbedPane3 = new javax.swing.JTabbedPane();
        jPanel13 = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        tSupplier = new javax.swing.JTable();
        jPanel14 = new javax.swing.JPanel();
        jLabel39 = new javax.swing.JLabel();
        rbInactiveSupplier = new javax.swing.JRadioButton();
        rbActiveSupplier = new javax.swing.JRadioButton();
        lbPhotoSupplier = new javax.swing.JLabel();
        lbRutaSupplier = new javax.swing.JLabel();
        btnDeleteSupplier = new javax.swing.JButton();
        btnAddSupplier = new javax.swing.JButton();
        btnEditSupplier = new javax.swing.JButton();
        btnSaveSupplier = new javax.swing.JButton();
        jPanel28 = new javax.swing.JPanel();
        txtStreetSupplier = new javax.swing.JTextField();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        txtColonySupplier = new javax.swing.JTextField();
        txtLocateSupplier = new javax.swing.JTextField();
        jLabel38 = new javax.swing.JLabel();
        jPanel29 = new javax.swing.JPanel();
        txtIdSupplier = new javax.swing.JTextField();
        txtNameSupplier = new javax.swing.JTextField();
        txtDescriptionSuppllier = new javax.swing.JTextField();
        jLabel34 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jPanel30 = new javax.swing.JPanel();
        jLabel43 = new javax.swing.JLabel();
        txtPhoneSupplier = new javax.swing.JTextField();
        jLabel56 = new javax.swing.JLabel();
        txtEmailSupplier = new javax.swing.JTextField();
        jSeparator8 = new javax.swing.JSeparator();
        jSeparator9 = new javax.swing.JSeparator();
        pStaff = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        lbTitleStaff = new javax.swing.JLabel();
        lbIdUser4 = new javax.swing.JLabel();
        btnExit5 = new javax.swing.JButton();
        jLabel40 = new javax.swing.JLabel();
        txtSearchStaff = new javax.swing.JTextField();
        rbUserStaff = new javax.swing.JRadioButton();
        rbIdStaff = new javax.swing.JRadioButton();
        rbNameStaff = new javax.swing.JRadioButton();
        rbJobStaff = new javax.swing.JRadioButton();
        jPanel16 = new javax.swing.JPanel();
        lbCountStaff = new javax.swing.JLabel();
        jScrollPane9 = new javax.swing.JScrollPane();
        taCantJobStaff = new javax.swing.JTextArea();
        bStorageStaff = new javax.swing.JProgressBar();
        lbStorageStaff = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jScrollPane11 = new javax.swing.JScrollPane();
        taJobStaff = new javax.swing.JTextArea();
        jTabbedPane4 = new javax.swing.JTabbedPane();
        jPanel17 = new javax.swing.JPanel();
        jScrollPane10 = new javax.swing.JScrollPane();
        tStaff = new javax.swing.JTable();
        jPanel18 = new javax.swing.JPanel();
        jLabel51 = new javax.swing.JLabel();
        rbInactiveStaff = new javax.swing.JRadioButton();
        rbActiveStaff = new javax.swing.JRadioButton();
        lbPhotoStaff = new javax.swing.JLabel();
        lbRutaStaff = new javax.swing.JLabel();
        btnDeleteStaff = new javax.swing.JButton();
        btnAddStaff = new javax.swing.JButton();
        btnEditStaff = new javax.swing.JButton();
        btnSaveStaff = new javax.swing.JButton();
        jSeparator11 = new javax.swing.JSeparator();
        jPanel31 = new javax.swing.JPanel();
        txtIdUser = new javax.swing.JTextField();
        jLabel42 = new javax.swing.JLabel();
        txtNameStaff = new javax.swing.JTextField();
        txtAPStaff = new javax.swing.JTextField();
        jLabel47 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        txtAMStaff = new javax.swing.JTextField();
        jLabel46 = new javax.swing.JLabel();
        cbJobStaff = new javax.swing.JComboBox<>();
        jLabel49 = new javax.swing.JLabel();
        jPanel32 = new javax.swing.JPanel();
        jLabel50 = new javax.swing.JLabel();
        txtPhoneStaff = new javax.swing.JTextField();
        txtEmailStaff = new javax.swing.JTextField();
        jLabel53 = new javax.swing.JLabel();
        jPanel33 = new javax.swing.JPanel();
        txtUserStaff = new javax.swing.JTextField();
        jLabel54 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        txtPasswordStaff = new javax.swing.JPasswordField();
        jSeparator10 = new javax.swing.JSeparator();
        pCustomer = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        lbTitleCustomer = new javax.swing.JLabel();
        lbIdUser2 = new javax.swing.JLabel();
        btnExit3 = new javax.swing.JButton();
        jLabel23 = new javax.swing.JLabel();
        txtSearchCustomer = new javax.swing.JTextField();
        rbNameCustomer = new javax.swing.JRadioButton();
        jPanel8 = new javax.swing.JPanel();
        lbCountCustomer = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        bStorageCustomer = new javax.swing.JProgressBar();
        lbStorageCustomer = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        taCustomer = new javax.swing.JTextArea();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tCustomer = new javax.swing.JTable();
        jPanel10 = new javax.swing.JPanel();
        btnDeleteCustomer = new javax.swing.JButton();
        btnAddCustomer = new javax.swing.JButton();
        btnEditCustomer = new javax.swing.JButton();
        btnSaveCustomer = new javax.swing.JButton();
        jPanel34 = new javax.swing.JPanel();
        txtNameCustomer = new javax.swing.JTextField();
        txtPhoneCustomer = new javax.swing.JTextField();
        txtDiscountCustomer = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jSeparator13 = new javax.swing.JSeparator();
        jSeparator12 = new javax.swing.JSeparator();
        pSetting = new javax.swing.JPanel();
        jPanel39 = new javax.swing.JPanel();
        lbTitleSetting = new javax.swing.JLabel();
        lbIdUser7 = new javax.swing.JLabel();
        btnExit8 = new javax.swing.JButton();
        jSeparator17 = new javax.swing.JSeparator();
        jPanel40 = new javax.swing.JPanel();
        btnTool5 = new javax.swing.JButton();
        btnTool2 = new javax.swing.JButton();
        btnTool1 = new javax.swing.JButton();
        btnTool3 = new javax.swing.JButton();
        btnTool4 = new javax.swing.JButton();
        pTool2 = new javax.swing.JScrollPane();
        jPanel41 = new javax.swing.JPanel();
        jSeparator16 = new javax.swing.JSeparator();
        jRadioButton3 = new javax.swing.JRadioButton();
        rbColor1 = new javax.swing.JRadioButton();
        jLabel8 = new javax.swing.JLabel();
        btnTool11 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jRadioButton7 = new javax.swing.JRadioButton();
        jRadioButton6 = new javax.swing.JRadioButton();
        jRadioButton4 = new javax.swing.JRadioButton();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton5 = new javax.swing.JRadioButton();
        jScrollPane14 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        jSeparator18 = new javax.swing.JSeparator();
        jLabel41 = new javax.swing.JLabel();
        jLabel59 = new javax.swing.JLabel();
        btnTool12 = new javax.swing.JButton();
        lbRutaBannerSettings = new javax.swing.JLabel();
        lbPhotoBannerSetting2 = new javax.swing.JLabel();
        lbPhotoBannerSetting1 = new javax.swing.JLabel();
        jScrollPane15 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jLabel73 = new javax.swing.JLabel();
        jSeparator20 = new javax.swing.JSeparator();
        btnTool13 = new javax.swing.JButton();
        jSeparator21 = new javax.swing.JSeparator();
        jSeparator22 = new javax.swing.JSeparator();
        lbColorSettings1 = new javax.swing.JLabel();
        txtColorSetting = new javax.swing.JTextField();
        jLabel60 = new javax.swing.JLabel();
        pTool1 = new javax.swing.JScrollPane();
        jPanel42 = new javax.swing.JPanel();
        jSeparator19 = new javax.swing.JSeparator();
        jLabel72 = new javax.swing.JLabel();
        jLabel74 = new javax.swing.JLabel();
        jSeparator23 = new javax.swing.JSeparator();
        jLabel76 = new javax.swing.JLabel();
        jLabel77 = new javax.swing.JLabel();
        jScrollPane17 = new javax.swing.JScrollPane();
        jTextArea4 = new javax.swing.JTextArea();
        jLabel78 = new javax.swing.JLabel();
        jSeparator24 = new javax.swing.JSeparator();
        btnTool22 = new javax.swing.JButton();
        txtNamePlantSetting = new javax.swing.JTextField();
        jLabel75 = new javax.swing.JLabel();
        txtDescriptionSetting = new javax.swing.JTextField();
        jLabel80 = new javax.swing.JLabel();
        jLabel81 = new javax.swing.JLabel();
        jLabel82 = new javax.swing.JLabel();
        txtStreetPlantSetting = new javax.swing.JTextField();
        txtColonyPlantSetting = new javax.swing.JTextField();
        jLabel83 = new javax.swing.JLabel();
        jLabel84 = new javax.swing.JLabel();
        txtLocatePlantSetting = new javax.swing.JTextField();
        jLabel85 = new javax.swing.JLabel();
        jSeparator25 = new javax.swing.JSeparator();
        jLabel86 = new javax.swing.JLabel();
        jLabel87 = new javax.swing.JLabel();
        txtPhonePlantSetting = new javax.swing.JTextField();
        txtEmailPlantSetting = new javax.swing.JTextField();
        jLabel88 = new javax.swing.JLabel();
        jScrollPane18 = new javax.swing.JScrollPane();
        jTextArea5 = new javax.swing.JTextArea();
        jLabel89 = new javax.swing.JLabel();
        jScrollPane19 = new javax.swing.JScrollPane();
        jTextArea6 = new javax.swing.JTextArea();
        jLabel91 = new javax.swing.JLabel();
        jSeparator26 = new javax.swing.JSeparator();
        btnTool21 = new javax.swing.JButton();
        lbPhotoPlantSetting = new javax.swing.JLabel();
        jSeparator27 = new javax.swing.JSeparator();
        lbRutaPlantSetting = new javax.swing.JLabel();
        lbPhotoPlantSetting1 = new javax.swing.JLabel();
        btnTool23 = new javax.swing.JButton();
        lbRutaPlantSetting1 = new javax.swing.JLabel();
        jSeparator28 = new javax.swing.JSeparator();
        jLabel93 = new javax.swing.JLabel();
        jLabel94 = new javax.swing.JLabel();
        jLabel79 = new javax.swing.JLabel();
        jLabel95 = new javax.swing.JLabel();
        pTool5 = new javax.swing.JScrollPane();
        jPanel43 = new javax.swing.JPanel();
        jSeparator29 = new javax.swing.JSeparator();
        jLabel90 = new javax.swing.JLabel();
        jLabel92 = new javax.swing.JLabel();
        jSeparator30 = new javax.swing.JSeparator();
        jLabel98 = new javax.swing.JLabel();
        btnTool51 = new javax.swing.JButton();
        lbRutaBackupSetting = new javax.swing.JLabel();
        jSeparator31 = new javax.swing.JSeparator();
        btnTool52 = new javax.swing.JButton();
        pHome = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        lbPhotoUser = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lbVersion = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        lbIdUser = new javax.swing.JLabel();
        btnDetailsSession = new javax.swing.JButton();
        btnExit = new javax.swing.JButton();
        btnNotifications = new javax.swing.JButton();
        btnMinimize = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        pDetailsSession = new javax.swing.JPanel();
        btnExit2 = new javax.swing.JButton();
        btnCloseSession = new javax.swing.JButton();
        lbJobUser = new javax.swing.JLabel();
        lbIdEstableciment = new javax.swing.JLabel();
        lbNameUser = new javax.swing.JLabel();
        lbStreet = new javax.swing.JLabel();
        lbColony = new javax.swing.JLabel();
        lbLocation = new javax.swing.JLabel();
        lbPhoneEstableciment = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        jPanel3 = new javax.swing.JPanel();
        lbDate = new javax.swing.JLabel();
        lbTime = new javax.swing.JLabel();
        lbTime1 = new javax.swing.JLabel();
        lbLogoHome = new javax.swing.JLabel();
        shadowHome = new javax.swing.JLabel();
        lbBackgroundHome = new javax.swing.JLabel();
        lbPattern = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pMenu.setBackground(new java.awt.Color(128, 128, 128));
        pMenu.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnSetting.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnSetting.setForeground(new java.awt.Color(255, 255, 255));
        btnSetting.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/settings_30px.png"))); // NOI18N
        btnSetting.setText("AJUSTES");
        btnSetting.setBorderPainted(false);
        btnSetting.setContentAreaFilled(false);
        btnSetting.setEnabled(false);
        btnSetting.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnSetting.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnSetting.setIconTextGap(10);
        btnSetting.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSettingActionPerformed(evt);
            }
        });
        pMenu.add(btnSetting, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 700, 150, 30));

        btnHome.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnHome.setForeground(new java.awt.Color(255, 255, 255));
        btnHome.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/home_30px.png"))); // NOI18N
        btnHome.setText("INICIO");
        btnHome.setBorderPainted(false);
        btnHome.setContentAreaFilled(false);
        btnHome.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnHome.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnHome.setIconTextGap(10);
        btnHome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHomeActionPerformed(evt);
            }
        });
        pMenu.add(btnHome, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, 160, 30));

        btnProduct.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnProduct.setForeground(new java.awt.Color(255, 255, 255));
        btnProduct.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/product_30px.png"))); // NOI18N
        btnProduct.setText("PRODUCTOS");
        btnProduct.setBorderPainted(false);
        btnProduct.setContentAreaFilled(false);
        btnProduct.setEnabled(false);
        btnProduct.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnProduct.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnProduct.setIconTextGap(10);
        btnProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProductActionPerformed(evt);
            }
        });
        pMenu.add(btnProduct, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 150, 160, 30));

        btnSale.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnSale.setForeground(new java.awt.Color(255, 255, 255));
        btnSale.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/sale_30px.png"))); // NOI18N
        btnSale.setText("VENTA");
        btnSale.setBorderPainted(false);
        btnSale.setContentAreaFilled(false);
        btnSale.setEnabled(false);
        btnSale.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnSale.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnSale.setIconTextGap(10);
        btnSale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaleActionPerformed(evt);
            }
        });
        pMenu.add(btnSale, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 190, 160, 30));

        btnBuy.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnBuy.setForeground(new java.awt.Color(255, 255, 255));
        btnBuy.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/buy_30px.png"))); // NOI18N
        btnBuy.setText("COMPRA");
        btnBuy.setBorderPainted(false);
        btnBuy.setContentAreaFilled(false);
        btnBuy.setEnabled(false);
        btnBuy.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnBuy.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnBuy.setIconTextGap(10);
        btnBuy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuyActionPerformed(evt);
            }
        });
        pMenu.add(btnBuy, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 230, 160, 30));

        btnCustomer.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnCustomer.setForeground(new java.awt.Color(255, 255, 255));
        btnCustomer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/customer_30px.png"))); // NOI18N
        btnCustomer.setText("CLIENTES");
        btnCustomer.setBorderPainted(false);
        btnCustomer.setContentAreaFilled(false);
        btnCustomer.setEnabled(false);
        btnCustomer.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnCustomer.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnCustomer.setIconTextGap(10);
        btnCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCustomerActionPerformed(evt);
            }
        });
        pMenu.add(btnCustomer, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 270, 160, 30));

        btnSupplier.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnSupplier.setForeground(new java.awt.Color(255, 255, 255));
        btnSupplier.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/supplier_30px.png"))); // NOI18N
        btnSupplier.setText("PROVEEDORES");
        btnSupplier.setBorderPainted(false);
        btnSupplier.setContentAreaFilled(false);
        btnSupplier.setEnabled(false);
        btnSupplier.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnSupplier.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnSupplier.setIconTextGap(10);
        btnSupplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSupplierActionPerformed(evt);
            }
        });
        pMenu.add(btnSupplier, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 310, 160, 30));

        btnStaff.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnStaff.setForeground(new java.awt.Color(255, 255, 255));
        btnStaff.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/staff_30px.png"))); // NOI18N
        btnStaff.setText("PERSONAL");
        btnStaff.setBorderPainted(false);
        btnStaff.setContentAreaFilled(false);
        btnStaff.setEnabled(false);
        btnStaff.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnStaff.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnStaff.setIconTextGap(10);
        btnStaff.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStaffActionPerformed(evt);
            }
        });
        pMenu.add(btnStaff, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 350, 160, 30));

        btnStatistic.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnStatistic.setForeground(new java.awt.Color(255, 255, 255));
        btnStatistic.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/statistic_30px.png"))); // NOI18N
        btnStatistic.setText("ESTADISTICAS");
        btnStatistic.setBorderPainted(false);
        btnStatistic.setContentAreaFilled(false);
        btnStatistic.setEnabled(false);
        btnStatistic.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnStatistic.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnStatistic.setIconTextGap(10);
        btnStatistic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStatisticActionPerformed(evt);
            }
        });
        pMenu.add(btnStatistic, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 390, 160, 30));

        btnCancel.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnCancel.setForeground(new java.awt.Color(255, 255, 255));
        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/cancel_30px.png"))); // NOI18N
        btnCancel.setText("CANCELACIÓN");
        btnCancel.setBorderPainted(false);
        btnCancel.setContentAreaFilled(false);
        btnCancel.setEnabled(false);
        btnCancel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnCancel.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnCancel.setIconTextGap(10);
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        pMenu.add(btnCancel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 430, 160, 30));

        btnJob.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnJob.setForeground(new java.awt.Color(255, 255, 255));
        btnJob.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/job_30px.png"))); // NOI18N
        btnJob.setText("CARGOS");
        btnJob.setBorderPainted(false);
        btnJob.setContentAreaFilled(false);
        btnJob.setEnabled(false);
        btnJob.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnJob.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnJob.setIconTextGap(10);
        btnJob.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnJobActionPerformed(evt);
            }
        });
        pMenu.add(btnJob, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 470, 160, 30));
        pMenu.add(lbLogoMenu, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 133, 69));

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/shadowMenu.png"))); // NOI18N
        pMenu.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 200, 768));

        getContentPane().add(pMenu, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 200, 768));

        pSale.setBackground(new java.awt.Color(255, 255, 255));
        pSale.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel23.setBackground(new java.awt.Color(255, 255, 255));
        jPanel23.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbTitleSale.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        lbTitleSale.setForeground(new java.awt.Color(128, 128, 128));
        lbTitleSale.setText("GENERACIÓN DE VENTAS");
        jPanel23.add(lbTitleSale, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, -1, -1));
        jPanel23.add(lbIdUser6, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 10, 130, 20));

        btnExit7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/exit_30px.png"))); // NOI18N
        btnExit7.setBorderPainted(false);
        btnExit7.setContentAreaFilled(false);
        btnExit7.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnExit7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExit7ActionPerformed(evt);
            }
        });
        jPanel23.add(btnExit7, new org.netbeans.lib.awtextra.AbsoluteConstraints(1100, 20, 30, 30));

        pSale.add(jPanel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1146, 70));

        jPanel24.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbPhotoSale.setBackground(new java.awt.Color(255, 255, 255));
        lbPhotoSale.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        lbPhotoSale.setOpaque(true);
        jPanel24.add(lbPhotoSale, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 160, 160));

        jLabel62.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel62.setText("%");
        jPanel24.add(jLabel62, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 140, 20, 30));

        txtNoSale.setEditable(false);
        txtNoSale.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jPanel24.add(txtNoSale, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 20, 120, 30));

        jLabel63.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel63.setText("No. Venta:");
        jPanel24.add(jLabel63, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 20, 70, 30));

        txtCodeSale.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtCodeSale.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCodeSaleKeyReleased(evt);
            }
        });
        jPanel24.add(txtCodeSale, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 60, 230, 30));

        jLabel66.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel66.setText("Código:");
        jPanel24.add(jLabel66, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 60, 50, 30));

        txtCustomerSale.setEditable(false);
        txtCustomerSale.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtCustomerSale.setText("PÚBLICO GENERAL");
        jPanel24.add(txtCustomerSale, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 100, 230, 30));

        jLabel67.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel67.setText("Cliente:");
        jPanel24.add(jLabel67, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 100, 50, 30));

        txtDiscountSale.setEditable(false);
        txtDiscountSale.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtDiscountSale.setText("0");
        jPanel24.add(txtDiscountSale, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 140, 40, 30));

        btnSearchCustomerSale.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        btnSearchCustomerSale.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/search_30px.png"))); // NOI18N
        btnSearchCustomerSale.setBorderPainted(false);
        btnSearchCustomerSale.setContentAreaFilled(false);
        btnSearchCustomerSale.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSearchCustomerSale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchCustomerSaleActionPerformed(evt);
            }
        });
        jPanel24.add(btnSearchCustomerSale, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 100, 30, 30));

        btnSearchProductSale.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        btnSearchProductSale.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/search_30px.png"))); // NOI18N
        btnSearchProductSale.setBorderPainted(false);
        btnSearchProductSale.setContentAreaFilled(false);
        btnSearchProductSale.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSearchProductSale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchProductSaleActionPerformed(evt);
            }
        });
        jPanel24.add(btnSearchProductSale, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 60, 30, 30));

        btnInputSale.setBackground(new java.awt.Color(204, 204, 204));
        btnInputSale.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnInputSale.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/input_30px.png"))); // NOI18N
        btnInputSale.setText("ENTRADA");
        btnInputSale.setBorderPainted(false);
        btnInputSale.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnInputSale.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnInputSale.setIconTextGap(10);
        jPanel24.add(btnInputSale, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 20, 150, 40));

        btnOutputSale.setBackground(new java.awt.Color(204, 204, 204));
        btnOutputSale.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnOutputSale.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/output_30px.png"))); // NOI18N
        btnOutputSale.setText("SALIDA");
        btnOutputSale.setBorderPainted(false);
        btnOutputSale.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnOutputSale.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnOutputSale.setIconTextGap(10);
        jPanel24.add(btnOutputSale, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 70, 150, 40));

        jLabel69.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel69.setText("Descuento:");
        jPanel24.add(jLabel69, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 140, 80, 30));

        btnPriceSale.setBackground(new java.awt.Color(204, 204, 204));
        btnPriceSale.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnPriceSale.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/barcode_30px.png"))); // NOI18N
        btnPriceSale.setText("VERIFICADOR");
        btnPriceSale.setBorderPainted(false);
        btnPriceSale.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnPriceSale.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnPriceSale.setIconTextGap(10);
        jPanel24.add(btnPriceSale, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 120, 150, 40));

        pSale.add(jPanel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 90, 790, 180));

        tSale.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        tSale.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "CANT", "CÓDIGO BARRA", "PRODUCTO", "MARCA", "PRECIO", "DTO", "IMPORTE"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tSale.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tSale.setRowHeight(26);
        jScrollPane5.setViewportView(tSale);
        if (tSale.getColumnModel().getColumnCount() > 0) {
            tSale.getColumnModel().getColumn(0).setResizable(false);
            tSale.getColumnModel().getColumn(0).setPreferredWidth(40);
            tSale.getColumnModel().getColumn(1).setResizable(false);
            tSale.getColumnModel().getColumn(1).setPreferredWidth(120);
            tSale.getColumnModel().getColumn(2).setResizable(false);
            tSale.getColumnModel().getColumn(2).setPreferredWidth(230);
            tSale.getColumnModel().getColumn(3).setResizable(false);
            tSale.getColumnModel().getColumn(3).setPreferredWidth(200);
            tSale.getColumnModel().getColumn(4).setResizable(false);
            tSale.getColumnModel().getColumn(5).setResizable(false);
            tSale.getColumnModel().getColumn(5).setPreferredWidth(60);
            tSale.getColumnModel().getColumn(6).setResizable(false);
            tSale.getColumnModel().getColumn(6).setPreferredWidth(80);
        }

        pSale.add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 280, 800, 320));

        pBannerSale.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbBannerSale.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 240, 240)));
        pBannerSale.add(lbBannerSale, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 290, 648));

        pSale.add(pBannerSale, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 90, 290, 648));

        lbSubtotalSale.setBackground(new java.awt.Color(204, 255, 255));
        lbSubtotalSale.setFont(new java.awt.Font("Arial", 1, 20)); // NOI18N
        lbSubtotalSale.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbSubtotalSale.setText("0.0");
        lbSubtotalSale.setOpaque(true);
        pSale.add(lbSubtotalSale, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 650, 175, 70));

        lbFrankSale.setBackground(new java.awt.Color(128, 128, 128));
        lbFrankSale.setOpaque(true);
        pSale.add(lbFrankSale, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 10, 180));

        jLabel61.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel61.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel61.setText("Cancelar");
        pSale.add(jLabel61, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 690, 60, 20));

        lbTotalSale.setBackground(new java.awt.Color(128, 128, 128));
        lbTotalSale.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        lbTotalSale.setForeground(new java.awt.Color(255, 255, 255));
        lbTotalSale.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbTotalSale.setText("0.0");
        lbTotalSale.setOpaque(true);
        pSale.add(lbTotalSale, new org.netbeans.lib.awtextra.AbsoluteConstraints(635, 650, 185, 70));

        lbDiscountSale.setBackground(new java.awt.Color(204, 255, 255));
        lbDiscountSale.setFont(new java.awt.Font("Arial", 1, 20)); // NOI18N
        lbDiscountSale.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbDiscountSale.setText("0.0");
        lbDiscountSale.setOpaque(true);
        pSale.add(lbDiscountSale, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 650, 140, 70));

        jLabel64.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel64.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel64.setText("Total a pagar");
        pSale.add(jLabel64, new org.netbeans.lib.awtextra.AbsoluteConstraints(635, 622, 180, 30));

        jLabel65.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel65.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel65.setText("Descuento");
        pSale.add(jLabel65, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 622, 140, 30));

        btnAddSale.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/delete_40px.png"))); // NOI18N
        btnAddSale.setBorderPainted(false);
        btnAddSale.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAddSale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddSaleActionPerformed(evt);
            }
        });
        pSale.add(btnAddSale, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 630, 60, 50));

        btnSaveSale.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/save_40px.png"))); // NOI18N
        btnSaveSale.setBorderPainted(false);
        btnSaveSale.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSaveSale.setEnabled(false);
        btnSaveSale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveSaleActionPerformed(evt);
            }
        });
        pSale.add(btnSaveSale, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 630, 60, 50));

        btnDeleteSale.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/add_40px.png"))); // NOI18N
        btnDeleteSale.setBorderPainted(false);
        btnDeleteSale.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDeleteSale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteSaleActionPerformed(evt);
            }
        });
        pSale.add(btnDeleteSale, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 630, 60, 50));
        pSale.add(jSeparator5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 1120, 10));

        jLabel68.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel68.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel68.setText("Subtotal");
        pSale.add(jLabel68, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 622, 175, 30));

        jLabel70.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel70.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel70.setText("Generar");
        pSale.add(jLabel70, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 690, 60, 20));

        jLabel71.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel71.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel71.setText("Eliminar");
        pSale.add(jLabel71, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 690, 60, 20));

        getContentPane().add(pSale, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 10, 1146, 748));

        pProduct.setBackground(new java.awt.Color(255, 255, 255));
        pProduct.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbTitleProduct.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        lbTitleProduct.setForeground(new java.awt.Color(128, 128, 128));
        lbTitleProduct.setText("LISTA DE PRODUCTOS");
        jPanel4.add(lbTitleProduct, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 340, -1));
        jPanel4.add(lbIdUser1, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 10, 130, 20));

        btnExit1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/exit_30px.png"))); // NOI18N
        btnExit1.setBorderPainted(false);
        btnExit1.setContentAreaFilled(false);
        btnExit1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnExit1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExit1ActionPerformed(evt);
            }
        });
        jPanel4.add(btnExit1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1100, 20, 30, 30));

        pProduct.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1146, 70));

        jLabel7.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/search_30px.png"))); // NOI18N
        jLabel7.setText("BUSCAR:");
        pProduct.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 710, -1, 30));

        txtSearchProduct.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtSearchProduct.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchProductKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSearchProductKeyTyped(evt);
            }
        });
        pProduct.add(txtSearchProduct, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 710, 270, 30));

        rbDepartmentProduct.setBackground(new java.awt.Color(255, 255, 255));
        searchProduct.add(rbDepartmentProduct);
        rbDepartmentProduct.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        rbDepartmentProduct.setText("Departamento");
        rbDepartmentProduct.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pProduct.add(rbDepartmentProduct, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 710, -1, 30));

        rbCodeProduct.setBackground(new java.awt.Color(255, 255, 255));
        searchProduct.add(rbCodeProduct);
        rbCodeProduct.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        rbCodeProduct.setSelected(true);
        rbCodeProduct.setText("Código");
        rbCodeProduct.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pProduct.add(rbCodeProduct, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 710, -1, 30));

        rbNameProduct.setBackground(new java.awt.Color(255, 255, 255));
        searchProduct.add(rbNameProduct);
        rbNameProduct.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        rbNameProduct.setText("Nombre");
        rbNameProduct.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pProduct.add(rbNameProduct, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 710, -1, 30));

        rbBrendProduct.setBackground(new java.awt.Color(255, 255, 255));
        searchProduct.add(rbBrendProduct);
        rbBrendProduct.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        rbBrendProduct.setText("Marca");
        rbBrendProduct.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pProduct.add(rbBrendProduct, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 710, -1, 30));

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbCountProduct.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        lbCountProduct.setForeground(new java.awt.Color(128, 128, 128));
        lbCountProduct.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbCountProduct.setText("0");
        jPanel1.add(lbCountProduct, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 210, 30));

        jLabel10.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("Productos registrados");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(21, 18, 210, 20));

        jScrollPane2.setBorder(null);
        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        taCantDepartment.setEditable(false);
        taCantDepartment.setBackground(new java.awt.Color(240, 240, 240));
        taCantDepartment.setColumns(20);
        taCantDepartment.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        taCantDepartment.setRows(5);
        taCantDepartment.setBorder(null);
        jScrollPane2.setViewportView(taCantDepartment);

        jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 70, 50, 500));

        jScrollPane3.setBorder(null);
        jScrollPane3.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane3.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        taDepartment.setEditable(false);
        taDepartment.setBackground(new java.awt.Color(240, 240, 240));
        taDepartment.setColumns(20);
        taDepartment.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        taDepartment.setRows(5);
        taDepartment.setBorder(null);
        jScrollPane3.setViewportView(taDepartment);

        jPanel1.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 150, 500));

        bStorageProduct.setMaximum(1000);
        jPanel1.add(bStorageProduct, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 590, 210, 20));

        lbStorageProduct.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lbStorageProduct.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1.add(lbStorageProduct, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 610, 210, 20));

        pProduct.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 90, 250, 648));

        jTabbedPane1.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N

        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tProduct.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        tProduct.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "CÓDIGO BARRA", "STOCK", "COMPRA", "VENTA", "NOMBRE", "MARCA", "DEPARTAMENTO", "DESCUENTO", "ESTADO"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tProduct.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tProduct.setRowHeight(24);
        jScrollPane1.setViewportView(tProduct);
        if (tProduct.getColumnModel().getColumnCount() > 0) {
            tProduct.getColumnModel().getColumn(0).setResizable(false);
            tProduct.getColumnModel().getColumn(0).setPreferredWidth(120);
            tProduct.getColumnModel().getColumn(1).setResizable(false);
            tProduct.getColumnModel().getColumn(1).setPreferredWidth(60);
            tProduct.getColumnModel().getColumn(2).setResizable(false);
            tProduct.getColumnModel().getColumn(2).setPreferredWidth(70);
            tProduct.getColumnModel().getColumn(3).setResizable(false);
            tProduct.getColumnModel().getColumn(3).setPreferredWidth(70);
            tProduct.getColumnModel().getColumn(4).setResizable(false);
            tProduct.getColumnModel().getColumn(4).setPreferredWidth(250);
            tProduct.getColumnModel().getColumn(5).setResizable(false);
            tProduct.getColumnModel().getColumn(5).setPreferredWidth(200);
            tProduct.getColumnModel().getColumn(6).setResizable(false);
            tProduct.getColumnModel().getColumn(6).setPreferredWidth(250);
            tProduct.getColumnModel().getColumn(7).setResizable(false);
            tProduct.getColumnModel().getColumn(7).setPreferredWidth(70);
            tProduct.getColumnModel().getColumn(8).setResizable(false);
            tProduct.getColumnModel().getColumn(8).setPreferredWidth(70);
        }

        jPanel5.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 850, 585));

        jTabbedPane1.addTab("Productos registrados", jPanel5);

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnFavorite.setBorderPainted(false);
        btnFavorite.setContentAreaFilled(false);
        btnFavorite.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnFavorite.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFavoriteActionPerformed(evt);
            }
        });
        jPanel6.add(btnFavorite, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 30, 40, 40));

        lbPhotoProduct.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        lbPhotoProduct.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lbPhotoProduct.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbPhotoProductMouseClicked(evt);
            }
        });
        jPanel6.add(lbPhotoProduct, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 30, 190, 190));

        lbRutaProduct.setForeground(new java.awt.Color(255, 255, 255));
        jPanel6.add(lbRutaProduct, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 500, 180, 30));

        btnDeleteProduct.setBackground(new java.awt.Color(128, 128, 128));
        btnDeleteProduct.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnDeleteProduct.setForeground(new java.awt.Color(255, 255, 255));
        btnDeleteProduct.setText("ELIMINAR");
        btnDeleteProduct.setBorderPainted(false);
        btnDeleteProduct.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDeleteProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteProductActionPerformed(evt);
            }
        });
        jPanel6.add(btnDeleteProduct, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 500, 130, 40));

        btnAddProduct.setBackground(new java.awt.Color(128, 128, 128));
        btnAddProduct.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnAddProduct.setForeground(new java.awt.Color(255, 255, 255));
        btnAddProduct.setText("AGREGAR");
        btnAddProduct.setBorderPainted(false);
        btnAddProduct.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAddProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddProductActionPerformed(evt);
            }
        });
        jPanel6.add(btnAddProduct, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 500, 130, 40));

        btnEditProduct.setBackground(new java.awt.Color(128, 128, 128));
        btnEditProduct.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnEditProduct.setForeground(new java.awt.Color(255, 255, 255));
        btnEditProduct.setText("EDITAR");
        btnEditProduct.setBorderPainted(false);
        btnEditProduct.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEditProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditProductActionPerformed(evt);
            }
        });
        jPanel6.add(btnEditProduct, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 500, 130, 40));

        btnSaveProduct.setBackground(new java.awt.Color(128, 128, 128));
        btnSaveProduct.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnSaveProduct.setForeground(new java.awt.Color(255, 255, 255));
        btnSaveProduct.setText("GUARDAR");
        btnSaveProduct.setBorderPainted(false);
        btnSaveProduct.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSaveProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveProductActionPerformed(evt);
            }
        });
        jPanel6.add(btnSaveProduct, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 500, 130, 40));

        jPanel25.setBackground(new java.awt.Color(255, 255, 255));
        jPanel25.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)), "Inventario", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 14))); // NOI18N
        jPanel25.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtStockProduct.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtStockProduct.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        txtStockProduct.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtStockProductKeyTyped(evt);
            }
        });
        jPanel25.add(txtStockProduct, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 40, 130, 30));

        jLabel15.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel15.setText("Stock:");
        jPanel25.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 40, 40, 30));

        jLabel16.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel16.setText("Stock max:");
        jPanel25.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, -1, 30));

        txtStockMaxProduct.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtStockMaxProduct.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        txtStockMaxProduct.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtStockMaxProductKeyTyped(evt);
            }
        });
        jPanel25.add(txtStockMaxProduct, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 80, 130, 30));

        txtStockMinProduct.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtStockMinProduct.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        txtStockMinProduct.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtStockMinProductKeyTyped(evt);
            }
        });
        jPanel25.add(txtStockMinProduct, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 120, 130, 30));

        jLabel21.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel21.setText("Stock min:");
        jPanel25.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 120, 70, 30));

        rbInactiveProduct.setBackground(new java.awt.Color(255, 255, 255));
        statusProduct.add(rbInactiveProduct);
        rbInactiveProduct.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        rbInactiveProduct.setText("Inactivo");
        rbInactiveProduct.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel25.add(rbInactiveProduct, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 120, 80, 30));

        rbActiveProduct.setBackground(new java.awt.Color(255, 255, 255));
        statusProduct.add(rbActiveProduct);
        rbActiveProduct.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        rbActiveProduct.setSelected(true);
        rbActiveProduct.setText("Activo");
        rbActiveProduct.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel25.add(rbActiveProduct, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 80, 70, 30));

        jLabel17.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel17.setText("Estado:");
        jPanel25.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 40, 60, 30));

        jPanel6.add(jPanel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 250, 490, 210));

        jPanel26.setBackground(new java.awt.Color(255, 255, 255));
        jPanel26.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)), "Datos del producto", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 14))); // NOI18N
        jPanel26.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtCodeProduct.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtCodeProduct.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        txtCodeProduct.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCodeProductKeyTyped(evt);
            }
        });
        jPanel26.add(txtCodeProduct, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 30, 190, 30));

        txtNameProduct.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtNameProduct.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        txtNameProduct.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNameProductKeyTyped(evt);
            }
        });
        jPanel26.add(txtNameProduct, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 70, 400, 30));

        txtBrendProduct.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtBrendProduct.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        txtBrendProduct.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtBrendProductKeyTyped(evt);
            }
        });
        jPanel26.add(txtBrendProduct, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 110, 340, 30));

        cbDepartmentProduct.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        cbDepartmentProduct.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cbDepartmentProductMouseClicked(evt);
            }
        });
        jPanel26.add(cbDepartmentProduct, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 150, 280, 30));

        jLabel14.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel14.setText("Departamento:");
        jPanel26.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 150, 100, 30));

        jLabel13.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setText("Marca:");
        jPanel26.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 110, 50, 30));

        jLabel12.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("Nombre:");
        jPanel26.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 70, 60, 30));

        jLabel11.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("Código de Barra:");
        jPanel26.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, -1, 30));

        jPanel6.add(jPanel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 580, 210));

        jPanel27.setBackground(new java.awt.Color(255, 255, 255));
        jPanel27.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)), "Contabilidad", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 14))); // NOI18N
        jPanel27.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtBuyProduct.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtBuyProduct.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtBuyProduct.setText("0");
        txtBuyProduct.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        txtBuyProduct.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuyProductKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtBuyProductKeyTyped(evt);
            }
        });
        jPanel27.add(txtBuyProduct, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 30, 120, 30));

        txtGainProduct.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtGainProduct.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtGainProduct.setText("0");
        txtGainProduct.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        txtGainProduct.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtGainProductKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtGainProductKeyTyped(evt);
            }
        });
        jPanel27.add(txtGainProduct, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 70, 60, 30));

        txtDiscountProduct.setEditable(false);
        txtDiscountProduct.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtDiscountProduct.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtDiscountProduct.setText("0");
        txtDiscountProduct.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        txtDiscountProduct.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtDiscountProductKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDiscountProductKeyTyped(evt);
            }
        });
        jPanel27.add(txtDiscountProduct, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 110, 60, 30));

        txtSaleProduct.setEditable(false);
        txtSaleProduct.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtSaleProduct.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtSaleProduct.setText("0.0");
        txtSaleProduct.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanel27.add(txtSaleProduct, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 150, 120, 30));

        jLabel18.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel18.setText("Venta: $");
        jPanel27.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 150, 70, 30));

        jLabel20.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel20.setText("%");
        jPanel27.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 70, 20, 30));

        jLabel19.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel19.setText("Ganancia:");
        jPanel27.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 90, 30));

        jLabel9.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("Compra: $");
        jPanel27.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 90, 30));

        jLabel22.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel22.setText("Descuento:");
        jPanel27.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, 90, 30));

        jLabel25.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel25.setText("%");
        jPanel27.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 110, 20, 30));

        jPanel6.add(jPanel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 250, 290, 210));

        jSeparator7.setForeground(new java.awt.Color(204, 204, 204));
        jPanel6.add(jSeparator7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 480, 800, 10));

        jTabbedPane1.addTab("Agregar / Editar", jPanel6);

        pProduct.add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 78, 850, 620));
        jTabbedPane1.getAccessibleContext().setAccessibleName("Productos registrados");

        pProduct.add(jSeparator6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 1110, 10));

        getContentPane().add(pProduct, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 10, 1146, 748));

        pJob.setBackground(new java.awt.Color(255, 255, 255));
        pJob.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel19.setBackground(new java.awt.Color(255, 255, 255));
        jPanel19.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbTitleJob.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        lbTitleJob.setForeground(new java.awt.Color(128, 128, 128));
        lbTitleJob.setText("LISTA DE CARGOS");
        jPanel19.add(lbTitleJob, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 340, -1));
        jPanel19.add(lbIdUser5, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 10, 130, 20));

        btnExit6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/exit_30px.png"))); // NOI18N
        btnExit6.setBorderPainted(false);
        btnExit6.setContentAreaFilled(false);
        btnExit6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnExit6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExit6ActionPerformed(evt);
            }
        });
        jPanel19.add(btnExit6, new org.netbeans.lib.awtextra.AbsoluteConstraints(1100, 20, 30, 30));

        pJob.add(jPanel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1146, 70));

        jLabel55.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel55.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/search_30px.png"))); // NOI18N
        jLabel55.setText("BUSCAR:");
        pJob.add(jLabel55, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 710, 100, 30));

        txtSearchJob.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtSearchJob.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchJobKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSearchJobKeyTyped(evt);
            }
        });
        pJob.add(txtSearchJob, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 710, 270, 30));

        rbNameJob.setBackground(new java.awt.Color(255, 255, 255));
        rbNameJob.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        rbNameJob.setSelected(true);
        rbNameJob.setText("Nombre");
        rbNameJob.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pJob.add(rbNameJob, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 710, 80, 30));

        jPanel20.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbCountJob.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        lbCountJob.setForeground(new java.awt.Color(128, 128, 128));
        lbCountJob.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbCountJob.setText("0");
        jPanel20.add(lbCountJob, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 210, 30));

        bStorageJob.setMaximum(500);
        jPanel20.add(bStorageJob, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 590, 210, 20));

        lbStorageJob.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lbStorageJob.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel20.add(lbStorageJob, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 610, 210, 20));

        jLabel57.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel57.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel57.setText("Cargos registrados");
        jPanel20.add(jLabel57, new org.netbeans.lib.awtextra.AbsoluteConstraints(21, 18, 210, 20));

        pJob.add(jPanel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 90, 250, 648));

        jTabbedPane5.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jTabbedPane5.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N

        jPanel21.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tJob.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        tJob.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "NOMBRE", "REG. PRODUCTO", "REG. VENTA", "REG. COMPRA", "REG. CLIENTE", "REG. PROVEEDOR", "REG. PERSONAL", "CON. ESTAD.", "CON. VENTA", "CON. COMPRA", "CAN. VENTA", "CAN. COMPRA", "REG. CARGO", "AJUSTES"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tJob.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tJob.setRowHeight(24);
        jScrollPane12.setViewportView(tJob);
        if (tJob.getColumnModel().getColumnCount() > 0) {
            tJob.getColumnModel().getColumn(0).setResizable(false);
            tJob.getColumnModel().getColumn(0).setPreferredWidth(250);
            tJob.getColumnModel().getColumn(1).setResizable(false);
            tJob.getColumnModel().getColumn(1).setPreferredWidth(100);
            tJob.getColumnModel().getColumn(2).setResizable(false);
            tJob.getColumnModel().getColumn(2).setPreferredWidth(100);
            tJob.getColumnModel().getColumn(3).setResizable(false);
            tJob.getColumnModel().getColumn(3).setPreferredWidth(100);
            tJob.getColumnModel().getColumn(4).setResizable(false);
            tJob.getColumnModel().getColumn(4).setPreferredWidth(100);
            tJob.getColumnModel().getColumn(5).setResizable(false);
            tJob.getColumnModel().getColumn(5).setPreferredWidth(100);
            tJob.getColumnModel().getColumn(6).setResizable(false);
            tJob.getColumnModel().getColumn(6).setPreferredWidth(100);
            tJob.getColumnModel().getColumn(7).setResizable(false);
            tJob.getColumnModel().getColumn(7).setPreferredWidth(100);
            tJob.getColumnModel().getColumn(8).setResizable(false);
            tJob.getColumnModel().getColumn(8).setPreferredWidth(100);
            tJob.getColumnModel().getColumn(9).setResizable(false);
            tJob.getColumnModel().getColumn(9).setPreferredWidth(100);
            tJob.getColumnModel().getColumn(10).setResizable(false);
            tJob.getColumnModel().getColumn(10).setPreferredWidth(100);
            tJob.getColumnModel().getColumn(11).setResizable(false);
            tJob.getColumnModel().getColumn(11).setPreferredWidth(100);
            tJob.getColumnModel().getColumn(12).setResizable(false);
            tJob.getColumnModel().getColumn(12).setPreferredWidth(100);
            tJob.getColumnModel().getColumn(13).setResizable(false);
            tJob.getColumnModel().getColumn(13).setPreferredWidth(100);
        }

        jPanel21.add(jScrollPane12, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 850, 585));

        jTabbedPane5.addTab("Cargos registrados", jPanel21);

        jPanel22.setBackground(new java.awt.Color(255, 255, 255));
        jPanel22.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel58.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel58.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel58.setText("Nombre:");
        jPanel22.add(jLabel58, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, 60, 30));

        txtNameJob.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtNameJob.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNameJobKeyTyped(evt);
            }
        });
        jPanel22.add(txtNameJob, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 30, 410, 30));

        btnDeleteJob.setBackground(new java.awt.Color(128, 128, 128));
        btnDeleteJob.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnDeleteJob.setForeground(new java.awt.Color(255, 255, 255));
        btnDeleteJob.setText("ELIMINAR");
        btnDeleteJob.setBorderPainted(false);
        btnDeleteJob.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteJobActionPerformed(evt);
            }
        });
        jPanel22.add(btnDeleteJob, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 500, 130, 40));

        btnAddJob.setBackground(new java.awt.Color(128, 128, 128));
        btnAddJob.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnAddJob.setForeground(new java.awt.Color(255, 255, 255));
        btnAddJob.setText("AGREGAR");
        btnAddJob.setBorderPainted(false);
        btnAddJob.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddJobActionPerformed(evt);
            }
        });
        jPanel22.add(btnAddJob, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 500, 130, 40));

        btnEditJob.setBackground(new java.awt.Color(128, 128, 128));
        btnEditJob.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnEditJob.setForeground(new java.awt.Color(255, 255, 255));
        btnEditJob.setText("EDITAR");
        btnEditJob.setBorderPainted(false);
        btnEditJob.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditJobActionPerformed(evt);
            }
        });
        jPanel22.add(btnEditJob, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 500, 130, 40));

        btnSaveJob.setBackground(new java.awt.Color(128, 128, 128));
        btnSaveJob.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnSaveJob.setForeground(new java.awt.Color(255, 255, 255));
        btnSaveJob.setText("GUARDAR");
        btnSaveJob.setBorderPainted(false);
        btnSaveJob.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveJobActionPerformed(evt);
            }
        });
        jPanel22.add(btnSaveJob, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 500, 130, 40));

        jPanel35.setBackground(new java.awt.Color(255, 255, 255));
        jPanel35.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)), "Registro", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 14))); // NOI18N
        jPanel35.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        rbRegSale.setBackground(new java.awt.Color(255, 255, 255));
        rbRegSale.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        rbRegSale.setText("Registrar ventas");
        rbRegSale.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel35.add(rbRegSale, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, 140, 30));

        rbRegBuy.setBackground(new java.awt.Color(255, 255, 255));
        rbRegBuy.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        rbRegBuy.setText("Registrar compras");
        rbRegBuy.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel35.add(rbRegBuy, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 150, 30));

        rbRegProduct.setBackground(new java.awt.Color(255, 255, 255));
        rbRegProduct.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        rbRegProduct.setText("Registrar productos");
        rbRegProduct.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel35.add(rbRegProduct, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 160, 30));

        rbRegCustomer.setBackground(new java.awt.Color(255, 255, 255));
        rbRegCustomer.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        rbRegCustomer.setText("Registrar cliente");
        rbRegCustomer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel35.add(rbRegCustomer, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 150, 130, 30));

        rbRegSupplier.setBackground(new java.awt.Color(255, 255, 255));
        rbRegSupplier.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        rbRegSupplier.setText("Registrar proveedor");
        rbRegSupplier.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel35.add(rbRegSupplier, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 190, 160, 30));

        rbRegStaff.setBackground(new java.awt.Color(255, 255, 255));
        rbRegStaff.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        rbRegStaff.setText("Registrar usuario");
        rbRegStaff.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel35.add(rbRegStaff, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 230, 140, 30));

        rbRegJob.setBackground(new java.awt.Color(255, 255, 255));
        rbRegJob.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        rbRegJob.setText("Registrar cargo");
        rbRegJob.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel35.add(rbRegJob, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 270, 130, 30));

        jPanel22.add(jPanel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 200, 340));

        jPanel36.setBackground(new java.awt.Color(255, 255, 255));
        jPanel36.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)), "Consulta", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 14))); // NOI18N
        jPanel36.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        rbConSale.setBackground(new java.awt.Color(255, 255, 255));
        rbConSale.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        rbConSale.setText("Ventas");
        rbConSale.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel36.add(rbConSale, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 140, 30));

        rbConBuy.setBackground(new java.awt.Color(255, 255, 255));
        rbConBuy.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        rbConBuy.setText("Compras");
        rbConBuy.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel36.add(rbConBuy, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, 150, 30));

        rbConStatistic.setBackground(new java.awt.Color(255, 255, 255));
        rbConStatistic.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        rbConStatistic.setText("Estadisticas");
        rbConStatistic.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel36.add(rbConStatistic, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 140, 30));

        jPanel22.add(jPanel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 80, 190, 340));

        jPanel37.setBackground(new java.awt.Color(255, 255, 255));
        jPanel37.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)), "Cancelación", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 14))); // NOI18N
        jPanel37.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        rbCanBuy.setBackground(new java.awt.Color(255, 255, 255));
        rbCanBuy.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        rbCanBuy.setText("Compras");
        rbCanBuy.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel37.add(rbCanBuy, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 150, 30));

        rbCanSale.setBackground(new java.awt.Color(255, 255, 255));
        rbCanSale.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        rbCanSale.setText("Ventas");
        rbCanSale.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel37.add(rbCanSale, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 130, 30));

        jPanel22.add(jPanel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 80, 190, 340));

        jPanel38.setBackground(new java.awt.Color(255, 255, 255));
        jPanel38.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)), "Sistema", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 14))); // NOI18N
        jPanel38.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        rbSettingJob.setBackground(new java.awt.Color(255, 255, 255));
        rbSettingJob.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        rbSettingJob.setText("Configuración");
        rbSettingJob.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel38.add(rbSettingJob, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 120, 30));

        jPanel22.add(jPanel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 80, 190, 340));

        jSeparator15.setForeground(new java.awt.Color(204, 204, 204));
        jPanel22.add(jSeparator15, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 480, 810, 20));

        jTabbedPane5.addTab("Agregar / Editar", jPanel22);

        pJob.add(jTabbedPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 78, 850, 620));
        pJob.add(jSeparator14, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 1110, 10));

        getContentPane().add(pJob, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 10, 1146, 748));

        pSupplier.setBackground(new java.awt.Color(255, 255, 255));
        pSupplier.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));
        jPanel11.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbTitleSupplier.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        lbTitleSupplier.setForeground(new java.awt.Color(128, 128, 128));
        lbTitleSupplier.setText("LISTA DE PROVEEDORES");
        jPanel11.add(lbTitleSupplier, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 370, 30));
        jPanel11.add(lbIdUser3, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 10, 130, 20));

        btnExit4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/exit_30px.png"))); // NOI18N
        btnExit4.setBorderPainted(false);
        btnExit4.setContentAreaFilled(false);
        btnExit4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnExit4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExit4ActionPerformed(evt);
            }
        });
        jPanel11.add(btnExit4, new org.netbeans.lib.awtextra.AbsoluteConstraints(1100, 20, 30, 30));

        pSupplier.add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1146, 70));

        jLabel29.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel29.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/search_30px.png"))); // NOI18N
        jLabel29.setText("BUSCAR:");
        pSupplier.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 710, 100, 30));

        txtSearchSupplier.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtSearchSupplier.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchSupplierKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSearchSupplierKeyTyped(evt);
            }
        });
        pSupplier.add(txtSearchSupplier, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 710, 270, 30));

        rbPhoneSupplier.setBackground(new java.awt.Color(255, 255, 255));
        searchSupplier.add(rbPhoneSupplier);
        rbPhoneSupplier.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        rbPhoneSupplier.setText("Telefono");
        rbPhoneSupplier.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pSupplier.add(rbPhoneSupplier, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 710, 90, 30));

        rbIdSupplier.setBackground(new java.awt.Color(255, 255, 255));
        searchSupplier.add(rbIdSupplier);
        rbIdSupplier.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        rbIdSupplier.setSelected(true);
        rbIdSupplier.setText("ID Proveedor");
        rbIdSupplier.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pSupplier.add(rbIdSupplier, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 710, 110, 30));

        rbNameSupplier.setBackground(new java.awt.Color(255, 255, 255));
        searchSupplier.add(rbNameSupplier);
        rbNameSupplier.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        rbNameSupplier.setText("Nombre");
        rbNameSupplier.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pSupplier.add(rbNameSupplier, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 710, 80, 30));

        rbLocationSupplier.setBackground(new java.awt.Color(255, 255, 255));
        searchSupplier.add(rbLocationSupplier);
        rbLocationSupplier.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        rbLocationSupplier.setText("Localidad");
        rbLocationSupplier.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pSupplier.add(rbLocationSupplier, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 710, 90, 30));

        jPanel12.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbCountSupplier.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        lbCountSupplier.setForeground(new java.awt.Color(128, 128, 128));
        lbCountSupplier.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbCountSupplier.setText("0");
        jPanel12.add(lbCountSupplier, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, 200, 30));

        jLabel30.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel30.setText("Proveedores frecuentes");
        jPanel12.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 210, -1));

        jScrollPane7.setBorder(null);
        jScrollPane7.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane7.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        taTopSupplier.setEditable(false);
        taTopSupplier.setColumns(20);
        taTopSupplier.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        taTopSupplier.setRows(5);
        taTopSupplier.setBorder(null);
        taTopSupplier.setOpaque(false);
        jScrollPane7.setViewportView(taTopSupplier);

        jPanel12.add(jScrollPane7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, 210, 470));

        bStorageSupplier.setMaximum(500);
        jPanel12.add(bStorageSupplier, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 590, 210, 20));

        lbStorageSupplier.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lbStorageSupplier.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel12.add(lbStorageSupplier, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 610, 210, 20));

        jLabel44.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel44.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel44.setText("Proveedores registrados");
        jPanel12.add(jLabel44, new org.netbeans.lib.awtextra.AbsoluteConstraints(21, 18, 210, 20));

        pSupplier.add(jPanel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 90, 250, 648));

        jTabbedPane3.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N

        jPanel13.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tSupplier.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        tSupplier.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "DNI", "NOMBRE", "DESCRIPCIÓN", "CALLE", "COLONIA", "LOCALIDAD", "TELEFONO", "ESTADO"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tSupplier.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tSupplier.setRowHeight(24);
        jScrollPane8.setViewportView(tSupplier);
        if (tSupplier.getColumnModel().getColumnCount() > 0) {
            tSupplier.getColumnModel().getColumn(0).setResizable(false);
            tSupplier.getColumnModel().getColumn(0).setPreferredWidth(120);
            tSupplier.getColumnModel().getColumn(1).setResizable(false);
            tSupplier.getColumnModel().getColumn(1).setPreferredWidth(250);
            tSupplier.getColumnModel().getColumn(2).setResizable(false);
            tSupplier.getColumnModel().getColumn(2).setPreferredWidth(300);
            tSupplier.getColumnModel().getColumn(3).setResizable(false);
            tSupplier.getColumnModel().getColumn(3).setPreferredWidth(150);
            tSupplier.getColumnModel().getColumn(4).setResizable(false);
            tSupplier.getColumnModel().getColumn(4).setPreferredWidth(150);
            tSupplier.getColumnModel().getColumn(5).setResizable(false);
            tSupplier.getColumnModel().getColumn(5).setPreferredWidth(200);
            tSupplier.getColumnModel().getColumn(6).setResizable(false);
            tSupplier.getColumnModel().getColumn(6).setPreferredWidth(150);
            tSupplier.getColumnModel().getColumn(7).setResizable(false);
            tSupplier.getColumnModel().getColumn(7).setPreferredWidth(80);
        }

        jPanel13.add(jScrollPane8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 850, 585));

        jTabbedPane3.addTab("Proveedores registrados", jPanel13);

        jPanel14.setBackground(new java.awt.Color(255, 255, 255));
        jPanel14.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel39.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel39.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel39.setText("Estado:");
        jPanel14.add(jLabel39, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 430, 60, 30));

        rbInactiveSupplier.setBackground(new java.awt.Color(255, 255, 255));
        statusSupplier.add(rbInactiveSupplier);
        rbInactiveSupplier.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        rbInactiveSupplier.setText("Inactivo");
        rbInactiveSupplier.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel14.add(rbInactiveSupplier, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 430, 80, 30));

        rbActiveSupplier.setBackground(new java.awt.Color(255, 255, 255));
        statusSupplier.add(rbActiveSupplier);
        rbActiveSupplier.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        rbActiveSupplier.setSelected(true);
        rbActiveSupplier.setText("Activo");
        rbActiveSupplier.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel14.add(rbActiveSupplier, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 430, 80, 30));

        lbPhotoSupplier.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        lbPhotoSupplier.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lbPhotoSupplier.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbPhotoSupplierMouseClicked(evt);
            }
        });
        jPanel14.add(lbPhotoSupplier, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 30, 190, 190));

        lbRutaSupplier.setForeground(new java.awt.Color(255, 255, 255));
        jPanel14.add(lbRutaSupplier, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 430, 180, 30));

        btnDeleteSupplier.setBackground(new java.awt.Color(128, 128, 128));
        btnDeleteSupplier.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnDeleteSupplier.setForeground(new java.awt.Color(255, 255, 255));
        btnDeleteSupplier.setText("ELIMINAR");
        btnDeleteSupplier.setBorderPainted(false);
        btnDeleteSupplier.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDeleteSupplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteSupplierActionPerformed(evt);
            }
        });
        jPanel14.add(btnDeleteSupplier, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 500, 130, 40));

        btnAddSupplier.setBackground(new java.awt.Color(128, 128, 128));
        btnAddSupplier.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnAddSupplier.setForeground(new java.awt.Color(255, 255, 255));
        btnAddSupplier.setText("AGREGAR");
        btnAddSupplier.setBorderPainted(false);
        btnAddSupplier.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAddSupplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddSupplierActionPerformed(evt);
            }
        });
        jPanel14.add(btnAddSupplier, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 500, 130, 40));

        btnEditSupplier.setBackground(new java.awt.Color(128, 128, 128));
        btnEditSupplier.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnEditSupplier.setForeground(new java.awt.Color(255, 255, 255));
        btnEditSupplier.setText("EDITAR");
        btnEditSupplier.setBorderPainted(false);
        btnEditSupplier.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEditSupplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditSupplierActionPerformed(evt);
            }
        });
        jPanel14.add(btnEditSupplier, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 500, 130, 40));

        btnSaveSupplier.setBackground(new java.awt.Color(128, 128, 128));
        btnSaveSupplier.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnSaveSupplier.setForeground(new java.awt.Color(255, 255, 255));
        btnSaveSupplier.setText("GUARDAR");
        btnSaveSupplier.setBorderPainted(false);
        btnSaveSupplier.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSaveSupplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveSupplierActionPerformed(evt);
            }
        });
        jPanel14.add(btnSaveSupplier, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 500, 130, 40));

        jPanel28.setBackground(new java.awt.Color(255, 255, 255));
        jPanel28.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)), "Ubicación", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 14))); // NOI18N
        jPanel28.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtStreetSupplier.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtStreetSupplier.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtStreetSupplierKeyTyped(evt);
            }
        });
        jPanel28.add(txtStreetSupplier, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 30, 260, 30));

        jLabel35.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel35.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel35.setText("Calle:");
        jPanel28.add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 30, 40, 30));

        jLabel36.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel36.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel36.setText("Colonia:");
        jPanel28.add(jLabel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 70, 60, 30));

        txtColonySupplier.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtColonySupplier.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtColonySupplierKeyTyped(evt);
            }
        });
        jPanel28.add(txtColonySupplier, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 70, 260, 30));

        txtLocateSupplier.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtLocateSupplier.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtLocateSupplierKeyTyped(evt);
            }
        });
        jPanel28.add(txtLocateSupplier, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 110, 260, 30));

        jLabel38.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel38.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel38.setText("Localidad:");
        jPanel28.add(jLabel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 110, 70, 30));

        jPanel14.add(jPanel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 230, 410, 170));

        jPanel29.setBackground(new java.awt.Color(255, 255, 255));
        jPanel29.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)), "Datos de proveedor", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 14))); // NOI18N
        jPanel29.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtIdSupplier.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtIdSupplier.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtIdSupplierKeyTyped(evt);
            }
        });
        jPanel29.add(txtIdSupplier, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 30, 150, 30));

        txtNameSupplier.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtNameSupplier.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNameSupplierKeyTyped(evt);
            }
        });
        jPanel29.add(txtNameSupplier, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 70, 420, 30));

        txtDescriptionSuppllier.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtDescriptionSuppllier.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDescriptionSuppllierKeyTyped(evt);
            }
        });
        jPanel29.add(txtDescriptionSuppllier, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 110, 420, 60));

        jLabel34.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel34.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel34.setText("Descripción:");
        jPanel29.add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 110, 80, 30));

        jLabel33.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel33.setText("Nombre:");
        jPanel29.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 70, 60, 30));

        jLabel32.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel32.setText("DNI:");
        jPanel29.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 90, 30));

        jPanel14.add(jPanel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 590, 190));

        jPanel30.setBackground(new java.awt.Color(255, 255, 255));
        jPanel30.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)), "Contacto", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 14))); // NOI18N
        jPanel30.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel43.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel43.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel43.setText("Correo:");
        jPanel30.add(jLabel43, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 70, 30));

        txtPhoneSupplier.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPhoneSupplier.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPhoneSupplierKeyTyped(evt);
            }
        });
        jPanel30.add(txtPhoneSupplier, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 30, 170, 30));

        jLabel56.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel56.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel56.setText("Teléfono:");
        jPanel30.add(jLabel56, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 70, 30));

        txtEmailSupplier.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtEmailSupplier.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtEmailSupplierKeyTyped(evt);
            }
        });
        jPanel30.add(txtEmailSupplier, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 70, 250, 30));

        jPanel14.add(jPanel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 230, 380, 170));

        jSeparator8.setForeground(new java.awt.Color(204, 204, 204));
        jPanel14.add(jSeparator8, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 480, 800, 10));

        jTabbedPane3.addTab("Agregar / Editar", jPanel14);

        pSupplier.add(jTabbedPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 78, 850, 620));
        pSupplier.add(jSeparator9, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 1110, 10));

        getContentPane().add(pSupplier, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 10, 1146, 748));

        pStaff.setBackground(new java.awt.Color(255, 255, 255));
        pStaff.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel15.setBackground(new java.awt.Color(255, 255, 255));
        jPanel15.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbTitleStaff.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        lbTitleStaff.setForeground(new java.awt.Color(128, 128, 128));
        lbTitleStaff.setText("LISTA DE PERSONAL REGISTRADO");
        jPanel15.add(lbTitleStaff, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 430, -1));
        jPanel15.add(lbIdUser4, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 10, 130, 20));

        btnExit5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/exit_30px.png"))); // NOI18N
        btnExit5.setToolTipText("");
        btnExit5.setBorderPainted(false);
        btnExit5.setContentAreaFilled(false);
        btnExit5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnExit5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExit5ActionPerformed(evt);
            }
        });
        jPanel15.add(btnExit5, new org.netbeans.lib.awtextra.AbsoluteConstraints(1100, 20, 30, 30));

        pStaff.add(jPanel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1146, 70));

        jLabel40.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel40.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/search_30px.png"))); // NOI18N
        jLabel40.setText("BUSCAR:");
        pStaff.add(jLabel40, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 710, 100, 30));

        txtSearchStaff.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtSearchStaff.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchStaffKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSearchStaffKeyTyped(evt);
            }
        });
        pStaff.add(txtSearchStaff, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 710, 270, 30));

        rbUserStaff.setBackground(new java.awt.Color(255, 255, 255));
        searchStaff.add(rbUserStaff);
        rbUserStaff.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        rbUserStaff.setText("Usuario");
        rbUserStaff.setToolTipText("");
        rbUserStaff.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pStaff.add(rbUserStaff, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 710, 80, 30));

        rbIdStaff.setBackground(new java.awt.Color(255, 255, 255));
        searchStaff.add(rbIdStaff);
        rbIdStaff.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        rbIdStaff.setSelected(true);
        rbIdStaff.setText("ID Usuario");
        rbIdStaff.setToolTipText("");
        rbIdStaff.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pStaff.add(rbIdStaff, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 710, 100, 30));

        rbNameStaff.setBackground(new java.awt.Color(255, 255, 255));
        searchStaff.add(rbNameStaff);
        rbNameStaff.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        rbNameStaff.setText("Nombre");
        rbNameStaff.setToolTipText("");
        rbNameStaff.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pStaff.add(rbNameStaff, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 710, 80, 30));

        rbJobStaff.setBackground(new java.awt.Color(255, 255, 255));
        searchStaff.add(rbJobStaff);
        rbJobStaff.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        rbJobStaff.setText("Cargo");
        rbJobStaff.setToolTipText("");
        rbJobStaff.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pStaff.add(rbJobStaff, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 710, 70, 30));

        jPanel16.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbCountStaff.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        lbCountStaff.setForeground(new java.awt.Color(128, 128, 128));
        lbCountStaff.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbCountStaff.setText("0");
        jPanel16.add(lbCountStaff, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 210, 30));

        jScrollPane9.setBorder(null);
        jScrollPane9.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane9.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        jScrollPane9.setOpaque(false);

        taCantJobStaff.setEditable(false);
        taCantJobStaff.setBackground(new java.awt.Color(240, 240, 240));
        taCantJobStaff.setColumns(20);
        taCantJobStaff.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        taCantJobStaff.setRows(5);
        taCantJobStaff.setBorder(null);
        jScrollPane9.setViewportView(taCantJobStaff);

        jPanel16.add(jScrollPane9, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 70, 60, 500));
        jPanel16.add(bStorageStaff, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 590, 210, 20));

        lbStorageStaff.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lbStorageStaff.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel16.add(lbStorageStaff, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 610, 210, 20));

        jLabel45.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel45.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel45.setText("Personal registrado");
        jPanel16.add(jLabel45, new org.netbeans.lib.awtextra.AbsoluteConstraints(21, 18, 210, 20));

        jScrollPane11.setBorder(null);
        jScrollPane11.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane11.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        jScrollPane11.setOpaque(false);

        taJobStaff.setEditable(false);
        taJobStaff.setBackground(new java.awt.Color(240, 240, 240));
        taJobStaff.setColumns(20);
        taJobStaff.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        taJobStaff.setRows(5);
        taJobStaff.setBorder(null);
        jScrollPane11.setViewportView(taJobStaff);

        jPanel16.add(jScrollPane11, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 140, 500));

        pStaff.add(jPanel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 90, 250, 648));

        jTabbedPane4.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N

        jPanel17.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tStaff.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        tStaff.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "RFC", "NOMBRE", "APELLIDO PATERNO", "APELLIDO MATERNO", "CARGO", "USUARIO", "ESTADO"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tStaff.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tStaff.setRowHeight(24);
        jScrollPane10.setViewportView(tStaff);
        if (tStaff.getColumnModel().getColumnCount() > 0) {
            tStaff.getColumnModel().getColumn(0).setResizable(false);
            tStaff.getColumnModel().getColumn(0).setPreferredWidth(100);
            tStaff.getColumnModel().getColumn(1).setResizable(false);
            tStaff.getColumnModel().getColumn(1).setPreferredWidth(200);
            tStaff.getColumnModel().getColumn(2).setResizable(false);
            tStaff.getColumnModel().getColumn(2).setPreferredWidth(200);
            tStaff.getColumnModel().getColumn(3).setResizable(false);
            tStaff.getColumnModel().getColumn(3).setPreferredWidth(200);
            tStaff.getColumnModel().getColumn(4).setResizable(false);
            tStaff.getColumnModel().getColumn(4).setPreferredWidth(250);
            tStaff.getColumnModel().getColumn(5).setResizable(false);
            tStaff.getColumnModel().getColumn(5).setPreferredWidth(250);
            tStaff.getColumnModel().getColumn(6).setResizable(false);
            tStaff.getColumnModel().getColumn(6).setPreferredWidth(100);
        }

        jPanel17.add(jScrollPane10, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 850, 585));

        jTabbedPane4.addTab("Personal registrado", jPanel17);

        jPanel18.setBackground(new java.awt.Color(255, 255, 255));
        jPanel18.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel51.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel51.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel51.setText("Estado:");
        jPanel18.add(jLabel51, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 420, 60, 30));

        rbInactiveStaff.setBackground(new java.awt.Color(255, 255, 255));
        statusStaff.add(rbInactiveStaff);
        rbInactiveStaff.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        rbInactiveStaff.setText("Inactivo");
        rbInactiveStaff.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel18.add(rbInactiveStaff, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 420, 80, 30));

        rbActiveStaff.setBackground(new java.awt.Color(255, 255, 255));
        statusStaff.add(rbActiveStaff);
        rbActiveStaff.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        rbActiveStaff.setSelected(true);
        rbActiveStaff.setText("Activo");
        rbActiveStaff.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel18.add(rbActiveStaff, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 420, 80, 30));

        lbPhotoStaff.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        lbPhotoStaff.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lbPhotoStaff.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbPhotoStaffMouseClicked(evt);
            }
        });
        jPanel18.add(lbPhotoStaff, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 30, 190, 190));

        lbRutaStaff.setForeground(new java.awt.Color(255, 255, 255));
        jPanel18.add(lbRutaStaff, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 420, 180, 30));

        btnDeleteStaff.setBackground(new java.awt.Color(128, 128, 128));
        btnDeleteStaff.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnDeleteStaff.setForeground(new java.awt.Color(255, 255, 255));
        btnDeleteStaff.setText("ELIMINAR");
        btnDeleteStaff.setBorderPainted(false);
        btnDeleteStaff.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDeleteStaff.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteStaffActionPerformed(evt);
            }
        });
        jPanel18.add(btnDeleteStaff, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 500, 130, 40));

        btnAddStaff.setBackground(new java.awt.Color(128, 128, 128));
        btnAddStaff.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnAddStaff.setForeground(new java.awt.Color(255, 255, 255));
        btnAddStaff.setText("AGREGAR");
        btnAddStaff.setBorderPainted(false);
        btnAddStaff.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAddStaff.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddStaffActionPerformed(evt);
            }
        });
        jPanel18.add(btnAddStaff, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 500, 130, 40));

        btnEditStaff.setBackground(new java.awt.Color(128, 128, 128));
        btnEditStaff.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnEditStaff.setForeground(new java.awt.Color(255, 255, 255));
        btnEditStaff.setText("EDITAR");
        btnEditStaff.setBorderPainted(false);
        btnEditStaff.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEditStaff.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditStaffActionPerformed(evt);
            }
        });
        jPanel18.add(btnEditStaff, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 500, 130, 40));

        btnSaveStaff.setBackground(new java.awt.Color(128, 128, 128));
        btnSaveStaff.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnSaveStaff.setForeground(new java.awt.Color(255, 255, 255));
        btnSaveStaff.setText("GUARDAR");
        btnSaveStaff.setBorderPainted(false);
        btnSaveStaff.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSaveStaff.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveStaffActionPerformed(evt);
            }
        });
        jPanel18.add(btnSaveStaff, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 500, 130, 40));

        jSeparator11.setForeground(new java.awt.Color(204, 204, 204));
        jPanel18.add(jSeparator11, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 480, 800, 10));

        jPanel31.setBackground(new java.awt.Color(255, 255, 255));
        jPanel31.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)), "Datos del personal", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 14))); // NOI18N
        jPanel31.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtIdUser.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtIdUser.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        txtIdUser.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtIdUserKeyTyped(evt);
            }
        });
        jPanel31.add(txtIdUser, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 30, 150, 30));

        jLabel42.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel42.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel42.setText("RFC:");
        jPanel31.add(jLabel42, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 30, 80, 30));

        txtNameStaff.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtNameStaff.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        txtNameStaff.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNameStaffKeyTyped(evt);
            }
        });
        jPanel31.add(txtNameStaff, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 70, 390, 30));

        txtAPStaff.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtAPStaff.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        txtAPStaff.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtAPStaffKeyTyped(evt);
            }
        });
        jPanel31.add(txtAPStaff, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 110, 260, 30));

        jLabel47.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel47.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel47.setText("Apellido Paterno:");
        jPanel31.add(jLabel47, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 120, 30));

        jLabel48.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel48.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel48.setText("Apellido Materno:");
        jPanel31.add(jLabel48, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 150, 120, 30));

        txtAMStaff.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtAMStaff.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        txtAMStaff.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtAMStaffKeyTyped(evt);
            }
        });
        jPanel31.add(txtAMStaff, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 150, 260, 30));

        jLabel46.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel46.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel46.setText("Nombre:");
        jPanel31.add(jLabel46, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 70, 60, 30));

        cbJobStaff.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        cbJobStaff.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cbJobStaffMouseClicked(evt);
            }
        });
        jPanel31.add(cbJobStaff, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 190, 260, 30));

        jLabel49.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel49.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel49.setText("Cargo:");
        jPanel31.add(jLabel49, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 190, 50, 30));

        jPanel18.add(jPanel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 590, 240));

        jPanel32.setBackground(new java.awt.Color(255, 255, 255));
        jPanel32.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)), "Contacto", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 14))); // NOI18N
        jPanel32.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel50.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel50.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel50.setText("Telefono:");
        jPanel32.add(jLabel50, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 30, 70, 30));

        txtPhoneStaff.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPhoneStaff.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        txtPhoneStaff.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPhoneStaffKeyTyped(evt);
            }
        });
        jPanel32.add(txtPhoneStaff, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 30, 190, 30));

        txtEmailStaff.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtEmailStaff.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        txtEmailStaff.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtEmailStaffKeyTyped(evt);
            }
        });
        jPanel32.add(txtEmailStaff, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 70, 290, 30));

        jLabel53.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel53.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel53.setText("Correo:");
        jPanel32.add(jLabel53, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 70, 60, 30));

        jPanel18.add(jPanel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 270, 460, 130));

        jPanel33.setBackground(new java.awt.Color(255, 255, 255));
        jPanel33.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)), "Inicio de sesión", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 14))); // NOI18N
        jPanel33.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtUserStaff.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtUserStaff.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        txtUserStaff.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtUserStaffKeyTyped(evt);
            }
        });
        jPanel33.add(txtUserStaff, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 30, 190, 30));

        jLabel54.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel54.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel54.setText("Usuario:");
        jPanel33.add(jLabel54, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, 70, 30));

        jLabel52.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel52.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel52.setText("Contraseña:");
        jPanel33.add(jLabel52, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 90, 30));

        txtPasswordStaff.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPasswordStaff.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        txtPasswordStaff.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPasswordStaffKeyTyped(evt);
            }
        });
        jPanel33.add(txtPasswordStaff, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 70, 190, 30));

        jPanel18.add(jPanel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 270, 330, 130));

        jTabbedPane4.addTab("Agregar / Editar", jPanel18);

        pStaff.add(jTabbedPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 78, 850, 620));
        pStaff.add(jSeparator10, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 1110, 10));

        getContentPane().add(pStaff, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 10, 1146, 748));

        pCustomer.setBackground(new java.awt.Color(255, 255, 255));
        pCustomer.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbTitleCustomer.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        lbTitleCustomer.setForeground(new java.awt.Color(128, 128, 128));
        lbTitleCustomer.setText("LISTA DE CLIENTES");
        jPanel7.add(lbTitleCustomer, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 280, -1));
        jPanel7.add(lbIdUser2, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 10, 130, 20));

        btnExit3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/exit_30px.png"))); // NOI18N
        btnExit3.setBorderPainted(false);
        btnExit3.setContentAreaFilled(false);
        btnExit3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnExit3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExit3ActionPerformed(evt);
            }
        });
        jPanel7.add(btnExit3, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 20, 30, 30));

        pCustomer.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 860, 70));

        jLabel23.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/search_30px.png"))); // NOI18N
        jLabel23.setText("BUSCAR:");
        pCustomer.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 710, 100, 30));

        txtSearchCustomer.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtSearchCustomer.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchCustomerKeyReleased(evt);
            }
        });
        pCustomer.add(txtSearchCustomer, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 710, 270, 30));

        rbNameCustomer.setBackground(new java.awt.Color(255, 255, 255));
        rbNameCustomer.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        rbNameCustomer.setSelected(true);
        rbNameCustomer.setText("Nombre");
        rbNameCustomer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pCustomer.add(rbNameCustomer, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 710, 80, 30));

        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbCountCustomer.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        lbCountCustomer.setForeground(new java.awt.Color(128, 128, 128));
        lbCountCustomer.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbCountCustomer.setText("0");
        jPanel8.add(lbCountCustomer, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 210, 30));

        jLabel24.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel24.setText("Clientes más frecuentes");
        jPanel8.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 210, 20));

        bStorageCustomer.setMaximum(500);
        jPanel8.add(bStorageCustomer, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 590, 210, 20));

        lbStorageCustomer.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lbStorageCustomer.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel8.add(lbStorageCustomer, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 610, 210, 20));

        jLabel37.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel37.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel37.setText("Clientes registrados");
        jPanel8.add(jLabel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(21, 18, 210, 20));

        jScrollPane4.setBorder(null);
        jScrollPane4.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane4.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        jScrollPane4.setOpaque(false);

        taCustomer.setEditable(false);
        taCustomer.setBackground(new java.awt.Color(240, 240, 240));
        taCustomer.setColumns(20);
        taCustomer.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        taCustomer.setRows(5);
        taCustomer.setBorder(null);
        jScrollPane4.setViewportView(taCustomer);

        jPanel8.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, 210, 470));

        pCustomer.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 90, 250, 648));

        jTabbedPane2.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N

        jPanel9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tCustomer.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        tCustomer.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "NOMBRE CLIENTE", "TELEFONO", "DESCUENTO"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tCustomer.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tCustomer.setRowHeight(24);
        jScrollPane6.setViewportView(tCustomer);
        if (tCustomer.getColumnModel().getColumnCount() > 0) {
            tCustomer.getColumnModel().getColumn(0).setResizable(false);
            tCustomer.getColumnModel().getColumn(0).setPreferredWidth(300);
            tCustomer.getColumnModel().getColumn(1).setResizable(false);
            tCustomer.getColumnModel().getColumn(1).setPreferredWidth(150);
            tCustomer.getColumnModel().getColumn(2).setResizable(false);
            tCustomer.getColumnModel().getColumn(2).setPreferredWidth(100);
        }

        jPanel9.add(jScrollPane6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 550, 585));

        jTabbedPane2.addTab("Clientes registrados", jPanel9);

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));
        jPanel10.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnDeleteCustomer.setBackground(new java.awt.Color(128, 128, 128));
        btnDeleteCustomer.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnDeleteCustomer.setForeground(new java.awt.Color(255, 255, 255));
        btnDeleteCustomer.setText("ELIMINAR");
        btnDeleteCustomer.setBorderPainted(false);
        btnDeleteCustomer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDeleteCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteCustomerActionPerformed(evt);
            }
        });
        jPanel10.add(btnDeleteCustomer, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 500, 110, 40));

        btnAddCustomer.setBackground(new java.awt.Color(128, 128, 128));
        btnAddCustomer.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnAddCustomer.setForeground(new java.awt.Color(255, 255, 255));
        btnAddCustomer.setText("AGREGAR");
        btnAddCustomer.setBorderPainted(false);
        btnAddCustomer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAddCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddCustomerActionPerformed(evt);
            }
        });
        jPanel10.add(btnAddCustomer, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 500, 110, 40));

        btnEditCustomer.setBackground(new java.awt.Color(128, 128, 128));
        btnEditCustomer.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnEditCustomer.setForeground(new java.awt.Color(255, 255, 255));
        btnEditCustomer.setText("EDITAR");
        btnEditCustomer.setBorderPainted(false);
        btnEditCustomer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEditCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditCustomerActionPerformed(evt);
            }
        });
        jPanel10.add(btnEditCustomer, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 500, 110, 40));

        btnSaveCustomer.setBackground(new java.awt.Color(128, 128, 128));
        btnSaveCustomer.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnSaveCustomer.setForeground(new java.awt.Color(255, 255, 255));
        btnSaveCustomer.setText("GUARDAR");
        btnSaveCustomer.setBorderPainted(false);
        btnSaveCustomer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSaveCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveCustomerActionPerformed(evt);
            }
        });
        jPanel10.add(btnSaveCustomer, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 500, 110, 40));

        jPanel34.setBackground(new java.awt.Color(255, 255, 255));
        jPanel34.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)), "Datos del cliente", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 14))); // NOI18N
        jPanel34.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtNameCustomer.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtNameCustomer.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNameCustomerKeyTyped(evt);
            }
        });
        jPanel34.add(txtNameCustomer, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 30, 340, 30));

        txtPhoneCustomer.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPhoneCustomer.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPhoneCustomerKeyTyped(evt);
            }
        });
        jPanel34.add(txtPhoneCustomer, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 70, 160, 30));

        txtDiscountCustomer.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtDiscountCustomer.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDiscountCustomerKeyTyped(evt);
            }
        });
        jPanel34.add(txtDiscountCustomer, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 110, 60, 30));

        jLabel28.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel28.setText("%");
        jPanel34.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 110, 20, 30));

        jLabel27.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel27.setText("Telefono:");
        jPanel34.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 70, 70, 30));

        jLabel26.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel26.setText("Nombre:");
        jPanel34.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 30, 60, 30));

        jLabel31.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel31.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel31.setText("Descuento:");
        jPanel34.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 110, 80, 30));

        jPanel10.add(jPanel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 500, 170));
        jPanel10.add(jSeparator13, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 480, 500, 10));

        jTabbedPane2.addTab("Agregar / Editar", jPanel10);

        pCustomer.add(jTabbedPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 78, 550, 620));
        pCustomer.add(jSeparator12, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 820, 10));

        getContentPane().add(pCustomer, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 10, 860, 748));

        pSetting.setBackground(new java.awt.Color(255, 255, 255));
        pSetting.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel39.setBackground(new java.awt.Color(255, 255, 255));
        jPanel39.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbTitleSetting.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        lbTitleSetting.setForeground(new java.awt.Color(128, 128, 128));
        lbTitleSetting.setText("CONFIGURACIONES DEL SISTEMA");
        jPanel39.add(lbTitleSetting, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 450, -1));
        jPanel39.add(lbIdUser7, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 10, 130, 20));

        btnExit8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/exit_30px.png"))); // NOI18N
        btnExit8.setBorderPainted(false);
        btnExit8.setContentAreaFilled(false);
        btnExit8.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnExit8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExit8ActionPerformed(evt);
            }
        });
        jPanel39.add(btnExit8, new org.netbeans.lib.awtextra.AbsoluteConstraints(1100, 20, 30, 30));

        pSetting.add(jPanel39, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1146, 70));
        pSetting.add(jSeparator17, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 1110, 10));

        jPanel40.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        pSetting.add(jPanel40, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 90, 10, 638));

        btnTool5.setBackground(new java.awt.Color(204, 204, 204));
        btnTool5.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        btnTool5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/tool3_24px.png"))); // NOI18N
        btnTool5.setText("Base de datos");
        btnTool5.setBorderPainted(false);
        btnTool5.setContentAreaFilled(false);
        btnTool5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnTool5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnTool5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTool5ActionPerformed(evt);
            }
        });
        pSetting.add(btnTool5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 250, 180, 30));

        btnTool2.setBackground(new java.awt.Color(204, 204, 204));
        btnTool2.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        btnTool2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/tool1_24px.png"))); // NOI18N
        btnTool2.setText("Personalización");
        btnTool2.setBorderPainted(false);
        btnTool2.setContentAreaFilled(false);
        btnTool2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnTool2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnTool2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTool2ActionPerformed(evt);
            }
        });
        pSetting.add(btnTool2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, 180, -1));

        btnTool1.setBackground(new java.awt.Color(204, 204, 204));
        btnTool1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnTool1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/tool2_24px.png"))); // NOI18N
        btnTool1.setText("Establecimiento");
        btnTool1.setBorderPainted(false);
        btnTool1.setContentAreaFilled(false);
        btnTool1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnTool1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnTool1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTool1ActionPerformed(evt);
            }
        });
        pSetting.add(btnTool1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 180, 30));

        btnTool3.setBackground(new java.awt.Color(204, 204, 204));
        btnTool3.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        btnTool3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/ticket_20px.png"))); // NOI18N
        btnTool3.setText("Ticket");
        btnTool3.setBorderPainted(false);
        btnTool3.setContentAreaFilled(false);
        btnTool3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnTool3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        pSetting.add(btnTool3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, 180, 30));

        btnTool4.setBackground(new java.awt.Color(204, 204, 204));
        btnTool4.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        btnTool4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/report_20px.png"))); // NOI18N
        btnTool4.setText("Reportes");
        btnTool4.setBorderPainted(false);
        btnTool4.setContentAreaFilled(false);
        btnTool4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnTool4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        pSetting.add(btnTool4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 210, 180, 30));

        pTool2.setBorder(null);
        pTool2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        jPanel41.setBackground(new java.awt.Color(255, 255, 255));
        jPanel41.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel41.add(jSeparator16, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 850, 10));

        jRadioButton3.setBackground(new java.awt.Color(153, 0, 0));
        stylesSetting.add(jRadioButton3);
        jRadioButton3.setBorder(null);
        jRadioButton3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jRadioButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton3ActionPerformed(evt);
            }
        });
        jPanel41.add(jRadioButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 90, 50, 50));

        rbColor1.setBackground(new java.awt.Color(55, 87, 164));
        stylesSetting.add(rbColor1);
        rbColor1.setBorder(null);
        rbColor1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbColor1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbColor1ActionPerformed(evt);
            }
        });
        jPanel41.add(rbColor1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 50, 50));

        jLabel8.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(102, 102, 102));
        jLabel8.setText("Seleccione el estilo que desea aplicar al sistema.");
        jPanel41.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 390, 30));

        btnTool11.setBackground(new java.awt.Color(128, 128, 128));
        btnTool11.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnTool11.setForeground(new java.awt.Color(255, 255, 255));
        btnTool11.setText("PERSONALIZADO");
        btnTool11.setBorderPainted(false);
        btnTool11.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnTool11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTool11ActionPerformed(evt);
            }
        });
        jPanel41.add(btnTool11, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 150, 150, 30));

        jLabel6.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel6.setText("Estilo");
        jPanel41.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 120, 30));

        jRadioButton7.setBackground(new java.awt.Color(0, 51, 102));
        stylesSetting.add(jRadioButton7);
        jRadioButton7.setForeground(new java.awt.Color(0, 51, 102));
        jRadioButton7.setBorder(null);
        jRadioButton7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jRadioButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton7ActionPerformed(evt);
            }
        });
        jPanel41.add(jRadioButton7, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 90, 50, 50));

        jRadioButton6.setBackground(new java.awt.Color(0, 102, 102));
        stylesSetting.add(jRadioButton6);
        jRadioButton6.setBorder(null);
        jRadioButton6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jRadioButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton6ActionPerformed(evt);
            }
        });
        jPanel41.add(jRadioButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 90, 50, 50));

        jRadioButton4.setBackground(new java.awt.Color(51, 102, 0));
        stylesSetting.add(jRadioButton4);
        jRadioButton4.setBorder(null);
        jRadioButton4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jRadioButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton4ActionPerformed(evt);
            }
        });
        jPanel41.add(jRadioButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 90, 50, 50));

        jRadioButton1.setBackground(new java.awt.Color(102, 0, 102));
        stylesSetting.add(jRadioButton1);
        jRadioButton1.setBorder(null);
        jRadioButton1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton1ActionPerformed(evt);
            }
        });
        jPanel41.add(jRadioButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 90, 50, 50));

        jRadioButton5.setBackground(new java.awt.Color(102, 102, 0));
        stylesSetting.add(jRadioButton5);
        jRadioButton5.setBorder(null);
        jRadioButton5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jRadioButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton5ActionPerformed(evt);
            }
        });
        jPanel41.add(jRadioButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 90, 50, 50));

        jScrollPane14.setBorder(null);
        jScrollPane14.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane14.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jTextArea1.setForeground(new java.awt.Color(102, 102, 102));
        jTextArea1.setRows(5);
        jTextArea1.setText("Se recomienda cargar imagenes que contengan\nuna resolución mínima de 290x648 pixeles o una\nresolución a escala.");
        jTextArea1.setBorder(null);
        jScrollPane14.setViewportView(jTextArea1);

        jPanel41.add(jScrollPane14, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 270, 330, 50));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/warning_20px.png"))); // NOI18N
        jPanel41.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 270, 20, 20));
        jPanel41.add(jSeparator18, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 600, 840, 10));

        jLabel41.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel41.setText("Baner publicitario");
        jPanel41.add(jLabel41, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 200, 140, 30));

        jLabel59.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel59.setForeground(new java.awt.Color(102, 102, 102));
        jLabel59.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/warning_20px.png"))); // NOI18N
        jLabel59.setText("Para visualizar los cambios realizados es necesario reiniciar el sistema.");
        jPanel41.add(jLabel59, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 620, 540, 30));

        btnTool12.setBackground(new java.awt.Color(128, 128, 128));
        btnTool12.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnTool12.setForeground(new java.awt.Color(255, 255, 255));
        btnTool12.setText("EXAMINAR");
        btnTool12.setBorderPainted(false);
        btnTool12.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnTool12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTool12ActionPerformed(evt);
            }
        });
        jPanel41.add(btnTool12, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 330, 150, 30));

        lbRutaBannerSettings.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jPanel41.add(lbRutaBannerSettings, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 370, 360, 30));

        lbPhotoBannerSetting2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 240, 240)));
        jPanel41.add(lbPhotoBannerSetting2, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 280, 135, 300));

        lbPhotoBannerSetting1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 240, 240)));
        jPanel41.add(lbPhotoBannerSetting1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 280, 135, 300));

        jScrollPane15.setBorder(null);
        jScrollPane15.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane15.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        jTextArea2.setEditable(false);
        jTextArea2.setColumns(20);
        jTextArea2.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jTextArea2.setForeground(new java.awt.Color(102, 102, 102));
        jTextArea2.setRows(5);
        jTextArea2.setText("Se recomienda aplicar estilos obscuros para tener\nuna visualización correcta de todos los elementos \nde la aplicación.");
        jTextArea2.setBorder(null);
        jScrollPane15.setViewportView(jTextArea2);

        jPanel41.add(jScrollPane15, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 90, 330, 50));

        jLabel73.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/warning_20px.png"))); // NOI18N
        jPanel41.add(jLabel73, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 90, 20, 20));
        jPanel41.add(jSeparator20, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 230, 850, 10));

        btnTool13.setBackground(new java.awt.Color(128, 128, 128));
        btnTool13.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnTool13.setForeground(new java.awt.Color(255, 255, 255));
        btnTool13.setText("GUARDAR");
        btnTool13.setBorderPainted(false);
        btnTool13.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnTool13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTool13ActionPerformed(evt);
            }
        });
        jPanel41.add(btnTool13, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 620, 150, 30));
        jPanel41.add(jSeparator21, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 400, 360, 10));
        jPanel41.add(jSeparator22, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 180, 130, 10));

        lbColorSettings1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lbColorSettings1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbColorSettings1.setText("#");
        jPanel41.add(lbColorSettings1, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 150, 20, 30));

        txtColorSetting.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtColorSetting.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtColorSetting.setText("3757A4");
        txtColorSetting.setBorder(null);
        txtColorSetting.setEnabled(false);
        txtColorSetting.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtColorSettingKeyTyped(evt);
            }
        });
        jPanel41.add(txtColorSetting, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 150, 130, 30));

        jLabel60.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel60.setForeground(new java.awt.Color(102, 102, 102));
        jLabel60.setText("Cargue una imagén para mostrar en el baner publicitario.");
        jPanel41.add(jLabel60, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 240, 390, 30));

        pTool2.setViewportView(jPanel41);

        pSetting.add(pTool2, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 90, 900, 630));

        pTool1.setBorder(null);
        pTool1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        jPanel42.setBackground(new java.awt.Color(255, 255, 255));
        jPanel42.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel42.add(jSeparator19, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 850, 10));

        jLabel72.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel72.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel72.setText("Nombre:");
        jPanel42.add(jLabel72, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 110, 30));

        jLabel74.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel74.setText("Datos del establecimiento");
        jPanel42.add(jLabel74, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 240, 30));
        jPanel42.add(jSeparator23, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1040, 840, 10));

        jLabel76.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel76.setText("Datos de ubicación");
        jPanel42.add(jLabel76, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 200, 140, 30));

        jLabel77.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel77.setForeground(new java.awt.Color(102, 102, 102));
        jLabel77.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/warning_20px.png"))); // NOI18N
        jLabel77.setText("280x145px");
        jPanel42.add(jLabel77, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 900, 120, 30));

        jScrollPane17.setBorder(null);
        jScrollPane17.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane17.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        jTextArea4.setEditable(false);
        jTextArea4.setColumns(20);
        jTextArea4.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jTextArea4.setForeground(new java.awt.Color(102, 102, 102));
        jTextArea4.setRows(5);
        jTextArea4.setText("Al actualizar los datos del \nestablecimiento los datos del ticket\nserán alterados.");
        jTextArea4.setBorder(null);
        jScrollPane17.setViewportView(jTextArea4);

        jPanel42.add(jScrollPane17, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 90, 260, 90));

        jLabel78.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/warning_20px.png"))); // NOI18N
        jPanel42.add(jLabel78, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 90, 20, 20));
        jPanel42.add(jSeparator24, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 230, 850, 10));

        btnTool22.setBackground(new java.awt.Color(128, 128, 128));
        btnTool22.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnTool22.setForeground(new java.awt.Color(255, 255, 255));
        btnTool22.setText("GUARDAR");
        btnTool22.setBorderPainted(false);
        btnTool22.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnTool22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTool22ActionPerformed(evt);
            }
        });
        jPanel42.add(btnTool22, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 1060, 150, 30));

        txtNamePlantSetting.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtNamePlantSetting.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtNamePlantSetting.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        txtNamePlantSetting.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNamePlantSettingKeyTyped(evt);
            }
        });
        jPanel42.add(txtNamePlantSetting, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 90, 390, 30));

        jLabel75.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel75.setForeground(new java.awt.Color(102, 102, 102));
        jLabel75.setText("Complete los campos con la información de localización que se le solicita.");
        jPanel42.add(jLabel75, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 240, 510, 30));

        txtDescriptionSetting.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtDescriptionSetting.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtDescriptionSetting.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        txtDescriptionSetting.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDescriptionSettingKeyTyped(evt);
            }
        });
        jPanel42.add(txtDescriptionSetting, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 130, 390, 30));

        jLabel80.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel80.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel80.setText("Descripción");
        jPanel42.add(jLabel80, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, 110, 30));

        jLabel81.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel81.setForeground(new java.awt.Color(102, 102, 102));
        jLabel81.setText("Complete los campos con la información del establecimiento que se le solicita.");
        jPanel42.add(jLabel81, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 520, 30));

        jLabel82.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel82.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel82.setText("Calle:");
        jPanel42.add(jLabel82, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 280, 110, 30));

        txtStreetPlantSetting.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtStreetPlantSetting.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtStreetPlantSetting.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        txtStreetPlantSetting.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtStreetPlantSettingKeyTyped(evt);
            }
        });
        jPanel42.add(txtStreetPlantSetting, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 280, 390, 30));

        txtColonyPlantSetting.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtColonyPlantSetting.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtColonyPlantSetting.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        txtColonyPlantSetting.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtColonyPlantSettingKeyTyped(evt);
            }
        });
        jPanel42.add(txtColonyPlantSetting, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 320, 390, 30));

        jLabel83.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel83.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel83.setText("Colonia:");
        jPanel42.add(jLabel83, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 320, 110, 30));

        jLabel84.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel84.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel84.setText("Localidad:");
        jPanel42.add(jLabel84, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 360, 110, 30));

        txtLocatePlantSetting.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtLocatePlantSetting.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtLocatePlantSetting.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        txtLocatePlantSetting.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtLocatePlantSettingKeyTyped(evt);
            }
        });
        jPanel42.add(txtLocatePlantSetting, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 360, 390, 30));

        jLabel85.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel85.setText("Datos de contacto");
        jPanel42.add(jLabel85, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 410, 140, 30));
        jPanel42.add(jSeparator25, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 440, 850, 10));

        jLabel86.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel86.setForeground(new java.awt.Color(102, 102, 102));
        jLabel86.setText("Complete los campos con la información de contacto que se le solicita.");
        jPanel42.add(jLabel86, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 450, 510, 30));

        jLabel87.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel87.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel87.setText("Teléfono:");
        jPanel42.add(jLabel87, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 490, 110, 30));

        txtPhonePlantSetting.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPhonePlantSetting.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtPhonePlantSetting.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        txtPhonePlantSetting.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPhonePlantSettingKeyTyped(evt);
            }
        });
        jPanel42.add(txtPhonePlantSetting, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 490, 200, 30));

        txtEmailPlantSetting.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtEmailPlantSetting.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtEmailPlantSetting.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        txtEmailPlantSetting.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtEmailPlantSettingKeyTyped(evt);
            }
        });
        jPanel42.add(txtEmailPlantSetting, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 530, 390, 30));

        jLabel88.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel88.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel88.setText("Correo:");
        jPanel42.add(jLabel88, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 530, 110, 30));

        jScrollPane18.setBorder(null);
        jScrollPane18.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane18.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        jTextArea5.setEditable(false);
        jTextArea5.setColumns(20);
        jTextArea5.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jTextArea5.setForeground(new java.awt.Color(102, 102, 102));
        jTextArea5.setRows(5);
        jTextArea5.setText("Al actualizar los datos de ubicación\nlos datos del ticket serán alterados.");
        jTextArea5.setBorder(null);
        jScrollPane18.setViewportView(jTextArea5);

        jPanel42.add(jScrollPane18, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 280, 260, 40));

        jLabel89.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/warning_20px.png"))); // NOI18N
        jPanel42.add(jLabel89, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 280, 20, 20));

        jScrollPane19.setBorder(null);
        jScrollPane19.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane19.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        jTextArea6.setEditable(false);
        jTextArea6.setColumns(20);
        jTextArea6.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jTextArea6.setForeground(new java.awt.Color(102, 102, 102));
        jTextArea6.setRows(5);
        jTextArea6.setText("Al actualizar los datos de contacto\nlos datos del ticket serán alterados.");
        jTextArea6.setBorder(null);
        jScrollPane19.setViewportView(jTextArea6);

        jPanel42.add(jScrollPane19, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 490, 260, 40));

        jLabel91.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel91.setText("Logo");
        jPanel42.add(jLabel91, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 580, 140, 30));
        jPanel42.add(jSeparator26, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 610, 850, 10));

        btnTool21.setBackground(new java.awt.Color(128, 128, 128));
        btnTool21.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnTool21.setForeground(new java.awt.Color(255, 255, 255));
        btnTool21.setText("EXAMINAR");
        btnTool21.setBorderPainted(false);
        btnTool21.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnTool21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTool21ActionPerformed(evt);
            }
        });
        jPanel42.add(btnTool21, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 660, 150, 30));

        lbPhotoPlantSetting.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanel42.add(lbPhotoPlantSetting, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 750, 240, 240));
        jPanel42.add(jSeparator27, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 730, 390, 10));

        lbRutaPlantSetting.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jPanel42.add(lbRutaPlantSetting, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 700, 380, 30));

        lbPhotoPlantSetting1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanel42.add(lbPhotoPlantSetting1, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 750, 280, 145));

        btnTool23.setBackground(new java.awt.Color(128, 128, 128));
        btnTool23.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnTool23.setForeground(new java.awt.Color(255, 255, 255));
        btnTool23.setText("EXAMINAR");
        btnTool23.setBorderPainted(false);
        btnTool23.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnTool23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTool23ActionPerformed(evt);
            }
        });
        jPanel42.add(btnTool23, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 660, 150, 30));

        lbRutaPlantSetting1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jPanel42.add(lbRutaPlantSetting1, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 700, 390, 30));
        jPanel42.add(jSeparator28, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 730, 380, 10));

        jLabel93.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/warning_20px.png"))); // NOI18N
        jPanel42.add(jLabel93, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 490, 20, 20));

        jLabel94.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel94.setForeground(new java.awt.Color(102, 102, 102));
        jLabel94.setText("Cargue una imagén para mostrar el logo del establecimiento.");
        jPanel42.add(jLabel94, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 620, 510, 30));

        jLabel79.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel79.setForeground(new java.awt.Color(102, 102, 102));
        jLabel79.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/warning_20px.png"))); // NOI18N
        jLabel79.setText("Para visualizar los cambios realizados es necesario reiniciar el sistema.");
        jPanel42.add(jLabel79, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 1060, 540, 30));

        jLabel95.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel95.setForeground(new java.awt.Color(102, 102, 102));
        jLabel95.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/warning_20px.png"))); // NOI18N
        jLabel95.setText("1000x1000px");
        jPanel42.add(jLabel95, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 1000, 120, 30));

        pTool1.setViewportView(jPanel42);

        pSetting.add(pTool1, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 90, 900, 630));

        pTool5.setBorder(null);
        pTool5.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        jPanel43.setBackground(new java.awt.Color(255, 255, 255));
        jPanel43.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel43.add(jSeparator29, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 850, 10));

        jLabel90.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel90.setForeground(new java.awt.Color(102, 102, 102));
        jLabel90.setText("Seleccione la ruta a la que desea crear una copia de seguridad de la base de datos.");
        jPanel43.add(jLabel90, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 600, 30));

        jLabel92.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel92.setText("Copia de seguridad");
        jPanel43.add(jLabel92, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 190, 30));
        jPanel43.add(jSeparator30, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 560, 840, 10));

        jLabel98.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel98.setForeground(new java.awt.Color(102, 102, 102));
        jLabel98.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/warning_20px.png"))); // NOI18N
        jLabel98.setText("La restauración de su copia de seguridad solo es posible contactando al desarrollador.");
        jPanel43.add(jLabel98, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 580, 610, 30));

        btnTool51.setBackground(new java.awt.Color(128, 128, 128));
        btnTool51.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnTool51.setForeground(new java.awt.Color(255, 255, 255));
        btnTool51.setText("EXAMINAR");
        btnTool51.setBorderPainted(false);
        btnTool51.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnTool51.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTool51ActionPerformed(evt);
            }
        });
        jPanel43.add(btnTool51, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 150, 30));

        lbRutaBackupSetting.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jPanel43.add(lbRutaBackupSetting, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 90, 370, 30));
        jPanel43.add(jSeparator31, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 120, 370, 10));

        btnTool52.setBackground(new java.awt.Color(128, 128, 128));
        btnTool52.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnTool52.setForeground(new java.awt.Color(255, 255, 255));
        btnTool52.setText("GUARDAR");
        btnTool52.setBorderPainted(false);
        btnTool52.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnTool52.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTool52ActionPerformed(evt);
            }
        });
        jPanel43.add(btnTool52, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 580, 150, 30));

        pTool5.setViewportView(jPanel43);

        pSetting.add(pTool5, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 90, 900, 630));

        getContentPane().add(pSetting, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 10, 1146, 748));

        pHome.setBackground(new java.awt.Color(255, 255, 255));
        pHome.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel2.add(lbPhotoUser, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 10, 50, 50));

        jLabel2.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(128, 128, 128));
        jLabel2.setText("PUNTO DE VENTA");
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 14, 120, 20));

        lbVersion.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lbVersion.setForeground(new java.awt.Color(128, 128, 128));
        jPanel2.add(lbVersion, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 34, 120, 20));

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/JEY_50px.png"))); // NOI18N
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 50, 50));

        jLabel5.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/online_24px.png"))); // NOI18N
        jLabel5.setText("EN LINEA");
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 40, 100, 24));

        lbIdUser.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        lbIdUser.setText("MIDJ010422");
        jPanel2.add(lbIdUser, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 10, 130, 30));

        btnDetailsSession.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/arrow1_24px.png"))); // NOI18N
        btnDetailsSession.setBorderPainted(false);
        btnDetailsSession.setContentAreaFilled(false);
        btnDetailsSession.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDetailsSession.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDetailsSessionActionPerformed(evt);
            }
        });
        jPanel2.add(btnDetailsSession, new org.netbeans.lib.awtextra.AbsoluteConstraints(920, 40, 24, 20));

        btnExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/exit_30px.png"))); // NOI18N
        btnExit.setBorderPainted(false);
        btnExit.setContentAreaFilled(false);
        btnExit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitActionPerformed(evt);
            }
        });
        jPanel2.add(btnExit, new org.netbeans.lib.awtextra.AbsoluteConstraints(1090, 20, 30, 30));

        btnNotifications.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/notification_30px.png"))); // NOI18N
        btnNotifications.setBorderPainted(false);
        btnNotifications.setContentAreaFilled(false);
        btnNotifications.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnNotifications.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNotificationsActionPerformed(evt);
            }
        });
        jPanel2.add(btnNotifications, new org.netbeans.lib.awtextra.AbsoluteConstraints(990, 20, 30, 30));

        btnMinimize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/minimize_30px.png"))); // NOI18N
        btnMinimize.setBorderPainted(false);
        btnMinimize.setContentAreaFilled(false);
        btnMinimize.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnMinimize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMinimizeActionPerformed(evt);
            }
        });
        jPanel2.add(btnMinimize, new org.netbeans.lib.awtextra.AbsoluteConstraints(1050, 20, 30, 30));

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jPanel2.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 10, 10, 50));

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jPanel2.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 10, 10, 50));

        jSeparator3.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jPanel2.add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 10, 10, 50));

        pHome.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1146, 70));

        pDetailsSession.setBackground(new java.awt.Color(255, 255, 255));
        pDetailsSession.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 240, 240), 2));
        pDetailsSession.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnExit2.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnExit2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/close_24px.png"))); // NOI18N
        btnExit2.setText("SALIR");
        btnExit2.setBorderPainted(false);
        btnExit2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnExit2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnExit2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnExit2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExit2ActionPerformed(evt);
            }
        });
        pDetailsSession.add(btnExit2, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 20, 100, 60));

        btnCloseSession.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnCloseSession.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/out_24px.png"))); // NOI18N
        btnCloseSession.setText("SESIÓN");
        btnCloseSession.setBorderPainted(false);
        btnCloseSession.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCloseSession.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCloseSession.setVerifyInputWhenFocusTarget(false);
        btnCloseSession.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnCloseSession.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseSessionActionPerformed(evt);
            }
        });
        pDetailsSession.add(btnCloseSession, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 100, 60));

        lbJobUser.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lbJobUser.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        pDetailsSession.add(lbJobUser, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, 210, 20));

        lbIdEstableciment.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        pDetailsSession.add(lbIdEstableciment, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 250, 210, 20));

        lbNameUser.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        lbNameUser.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        pDetailsSession.add(lbNameUser, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 210, 20));

        lbStreet.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        pDetailsSession.add(lbStreet, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 150, 210, 20));

        lbColony.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        pDetailsSession.add(lbColony, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, 210, 20));

        lbLocation.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        pDetailsSession.add(lbLocation, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 190, 210, 20));

        lbPhoneEstableciment.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        pDetailsSession.add(lbPhoneEstableciment, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 220, 210, 20));
        pDetailsSession.add(jSeparator4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, 210, 10));

        pHome.add(pDetailsSession, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 70, 250, 290));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbDate.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lbDate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/calendar_30px.png"))); // NOI18N
        jPanel3.add(lbDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 130, 30));

        lbTime.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lbTime.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbTime.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/time_30px.png"))); // NOI18N
        jPanel3.add(lbTime, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 20, 170, 30));

        lbTime1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lbTime1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbTime1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/pc_30px.png"))); // NOI18N
        lbTime1.setText("PC: 01");
        lbTime1.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        lbTime1.setIconTextGap(10);
        jPanel3.add(lbTime1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, 20, 130, 30));

        pHome.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 678, 1146, 70));
        pHome.add(lbLogoHome, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 150, 450, 450));

        shadowHome.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/shadow_1146px.png"))); // NOI18N
        pHome.add(shadowHome, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 1146, 608));
        pHome.add(lbBackgroundHome, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 1146, 608));

        getContentPane().add(pHome, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 10, 1146, 748));
        getContentPane().add(lbPattern, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 0, 1166, 768));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitActionPerformed
        if (JOptionPane.showConfirmDialog(null, "¿DESEA SALIR?\n\n"
                + "Esta apunto de cerrar el sistema, \n"
                + "porfavor confirme para continuar.", "SALIR", JOptionPane.YES_NO_OPTION) == 0) {
            line();
            System.exit(0);
        }
    }//GEN-LAST:event_btnExitActionPerformed

    private void btnMinimizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMinimizeActionPerformed
        this.setState(Home.ICONIFIED);
    }//GEN-LAST:event_btnMinimizeActionPerformed

    private void btnNotificationsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNotificationsActionPerformed
        Noticifation notification = new Noticifation(this, true);
        notification.setVisible(true);
    }//GEN-LAST:event_btnNotificationsActionPerformed

    private void btnDetailsSessionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDetailsSessionActionPerformed
        if (!banDetailsSession) {
            banDetailsSession = true;
            pDetailsSession.setVisible(true);
            btnDetailsSession.setIcon(new ImageIcon(getClass().getResource("/icons/arrow2_24px.png")));
        } else {
            banDetailsSession = false;
            pDetailsSession.setVisible(false);
            btnDetailsSession.setIcon(new ImageIcon(getClass().getResource("/icons/arrow1_24px.png")));
        }
    }//GEN-LAST:event_btnDetailsSessionActionPerformed

    private void btnCloseSessionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseSessionActionPerformed
        if (JOptionPane.showConfirmDialog(null, "¿DESEA CERRAR SESIÓN?\n\n"
                + "Esta apunto de cerrar sesión, \n"
                + "porfavor confirme para continuar.", "SALIR", JOptionPane.YES_NO_OPTION) == 0) {
            line();
            this.dispose();
            Login login = new Login();
            login.setBackground(new Color(0, 0, 0, 0));
            login.setVisible(true);
        }
    }//GEN-LAST:event_btnCloseSessionActionPerformed

    private void btnExit2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExit2ActionPerformed
        if (JOptionPane.showConfirmDialog(null, "¿DESEA SALIR?\n\n"
                + "Esta apunto de cerrar el sistema, \n"
                + "porfavor confirme para continuar.", "SALIR", JOptionPane.YES_NO_OPTION) == 0) {
            line();
            System.exit(0);
        }
    }//GEN-LAST:event_btnExit2ActionPerformed

    private void btnHomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHomeActionPerformed
        setVisible();
        pHome.setVisible(true);
        dataHome();
        setForeground();
        btnHome.setForeground(Color.YELLOW);
    }//GEN-LAST:event_btnHomeActionPerformed

    private void btnProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProductActionPerformed
        setVisible();
        pProduct.setVisible(true);
        try {
            dataProduct();
        } catch (IOException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
        setForeground();
        btnProduct.setForeground(Color.YELLOW);
    }//GEN-LAST:event_btnProductActionPerformed

    private void btnSaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaleActionPerformed
        setVisible();
        pSale.setVisible(true);
        dataSale();
        setForeground();
        btnSale.setForeground(Color.YELLOW);
    }//GEN-LAST:event_btnSaleActionPerformed

    private void btnBuyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuyActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnBuyActionPerformed

    private void btnCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCustomerActionPerformed
        setVisible();
        pCustomer.setVisible(true);
        dataCustomer();
        setForeground();
        btnCustomer.setForeground(Color.YELLOW);
    }//GEN-LAST:event_btnCustomerActionPerformed

    private void btnSupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSupplierActionPerformed
        setVisible();
        pSupplier.setVisible(true);
        try {
            dataSupplier();
        } catch (IOException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
        setForeground();
        btnSupplier.setForeground(Color.YELLOW);
    }//GEN-LAST:event_btnSupplierActionPerformed

    private void btnStaffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStaffActionPerformed
        setVisible();
        pStaff.setVisible(true);
        try {
            dataStaff();
        } catch (IOException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
        setForeground();
        btnStaff.setForeground(Color.YELLOW);
    }//GEN-LAST:event_btnStaffActionPerformed

    private void btnStatisticActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStatisticActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnStatisticActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnJobActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnJobActionPerformed
        setVisible();
        pJob.setVisible(true);
        dataJob();
        setForeground();
        btnJob.setForeground(Color.YELLOW);
    }//GEN-LAST:event_btnJobActionPerformed

    private void btnSettingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSettingActionPerformed
        setVisible();
        pSetting.setVisible(true);
        dataSetting();
        setForeground();
        btnSetting.setForeground(Color.YELLOW);
    }//GEN-LAST:event_btnSettingActionPerformed

    private void btnExit1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExit1ActionPerformed
        pProduct.setVisible(false);
    }//GEN-LAST:event_btnExit1ActionPerformed

    private void lbPhotoProductMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbPhotoProductMouseClicked
        file(lbPhotoProduct, lbRutaProduct);
    }//GEN-LAST:event_lbPhotoProductMouseClicked

    private void btnFavoriteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFavoriteActionPerformed
        if (!banFavoriteProduct) {
            banFavoriteProduct = true;
            btnFavorite.setIcon(new ImageIcon(getClass().getResource("/icons/star_filled_30px.png")));
        } else {
            banFavoriteProduct = false;
            btnFavorite.setIcon(new ImageIcon(getClass().getResource("/icons/star_30px.png")));
        }
    }//GEN-LAST:event_btnFavoriteActionPerformed

    private void btnAddProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddProductActionPerformed
        try {
            clearProduct();
        } catch (IOException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnAddProductActionPerformed

    private void btnSaveProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveProductActionPerformed
        try {
            insertProduct();
        } catch (IOException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnSaveProductActionPerformed

    private void btnEditProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditProductActionPerformed
        try {
            updateProduct();
        } catch (IOException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnEditProductActionPerformed

    private void btnDeleteProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteProductActionPerformed
        try {
            deleteProduct();
        } catch (IOException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnDeleteProductActionPerformed

    private void txtSearchProductKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchProductKeyReleased
        if (rbCodeProduct.isSelected()) {
            try {
                sorterProduct.setRowFilter(RowFilter.regexFilter(txtSearchProduct.getText(), 0));
            } catch (Exception e) {
            }
        } else if (rbNameProduct.isSelected()) {
            try {
                sorterProduct.setRowFilter(RowFilter.regexFilter(txtSearchProduct.getText(), 4));
            } catch (Exception e) {
            }
        } else if (rbBrendProduct.isSelected()) {
            try {
                sorterProduct.setRowFilter(RowFilter.regexFilter(txtSearchProduct.getText(), 5));
            } catch (Exception e) {
            }
        } else if (rbDepartmentProduct.isSelected()) {
            try {
                sorterProduct.setRowFilter(RowFilter.regexFilter(txtSearchProduct.getText(), 6));
            } catch (Exception e) {
            }
        }
    }//GEN-LAST:event_txtSearchProductKeyReleased

    private void cbDepartmentProductMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cbDepartmentProductMouseClicked
        cbDepartment();
    }//GEN-LAST:event_cbDepartmentProductMouseClicked

    private void btnExit3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExit3ActionPerformed
        pCustomer.setVisible(false);
    }//GEN-LAST:event_btnExit3ActionPerformed

    private void txtSearchCustomerKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchCustomerKeyReleased
        try {
            sorterCustomer.setRowFilter(RowFilter.regexFilter(txtSearchCustomer.getText(), 0));
        } catch (Exception e) {
        }
    }//GEN-LAST:event_txtSearchCustomerKeyReleased

    private void btnDeleteCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteCustomerActionPerformed
        deleteCustomer();
    }//GEN-LAST:event_btnDeleteCustomerActionPerformed

    private void btnAddCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddCustomerActionPerformed
        clearCustomer();
    }//GEN-LAST:event_btnAddCustomerActionPerformed

    private void btnEditCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditCustomerActionPerformed
        updateCustomer();
    }//GEN-LAST:event_btnEditCustomerActionPerformed

    private void btnSaveCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveCustomerActionPerformed
        insertCustomer();
    }//GEN-LAST:event_btnSaveCustomerActionPerformed

    private void btnExit4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExit4ActionPerformed
        pSupplier.setVisible(false);
    }//GEN-LAST:event_btnExit4ActionPerformed

    private void txtSearchSupplierKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchSupplierKeyReleased
        if (rbIdSupplier.isSelected()) {
            try {
                sorterSupplier.setRowFilter(RowFilter.regexFilter(txtSearchSupplier.getText(), 0));
            } catch (Exception e) {
            }
        } else if (rbNameSupplier.isSelected()) {
            try {
                sorterSupplier.setRowFilter(RowFilter.regexFilter(txtSearchSupplier.getText(), 1));
            } catch (Exception e) {
            }
        } else if (rbLocationSupplier.isSelected()) {
            try {
                sorterSupplier.setRowFilter(RowFilter.regexFilter(txtSearchSupplier.getText(), 5));
            } catch (Exception e) {
            }
        } else if (rbPhoneSupplier.isSelected()) {
            try {
                sorterSupplier.setRowFilter(RowFilter.regexFilter(txtSearchSupplier.getText(), 6));
            } catch (Exception e) {
            }
        }
    }//GEN-LAST:event_txtSearchSupplierKeyReleased

    private void lbPhotoSupplierMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbPhotoSupplierMouseClicked
        file(lbPhotoSupplier, lbRutaSupplier);
    }//GEN-LAST:event_lbPhotoSupplierMouseClicked

    private void btnDeleteSupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteSupplierActionPerformed
        try {
            deleteSupplier();
        } catch (IOException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnDeleteSupplierActionPerformed

    private void btnAddSupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddSupplierActionPerformed
        try {
            clearSupplier();
        } catch (IOException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnAddSupplierActionPerformed

    private void btnEditSupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditSupplierActionPerformed
        try {
            updateSupplier();
        } catch (IOException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnEditSupplierActionPerformed

    private void btnSaveSupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveSupplierActionPerformed
        try {
            insertSupplier();
        } catch (IOException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnSaveSupplierActionPerformed

    private void btnExit5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExit5ActionPerformed
        pStaff.setVisible(false);
    }//GEN-LAST:event_btnExit5ActionPerformed

    private void txtSearchStaffKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchStaffKeyReleased
        if (rbIdStaff.isSelected()) {
            try {
                sorterStaff.setRowFilter(RowFilter.regexFilter(txtSearchStaff.getText(), 0));
            } catch (Exception e) {
            }
        } else if (rbNameStaff.isSelected()) {
            try {
                sorterStaff.setRowFilter(RowFilter.regexFilter(txtSearchStaff.getText(), 1));
            } catch (Exception e) {
            }
        } else if (rbJobStaff.isSelected()) {
            try {
                sorterStaff.setRowFilter(RowFilter.regexFilter(txtSearchStaff.getText(), 4));
            } catch (Exception e) {
            }
        } else if (rbUserStaff.isSelected()) {
            try {
                sorterStaff.setRowFilter(RowFilter.regexFilter(txtSearchStaff.getText(), 5));
            } catch (Exception e) {
            }
        }
    }//GEN-LAST:event_txtSearchStaffKeyReleased

    private void lbPhotoStaffMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbPhotoStaffMouseClicked
        file(lbPhotoStaff, lbRutaStaff);
    }//GEN-LAST:event_lbPhotoStaffMouseClicked

    private void btnDeleteStaffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteStaffActionPerformed
        try {
            deleteStaff();
        } catch (IOException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnDeleteStaffActionPerformed

    private void btnAddStaffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddStaffActionPerformed
        try {
            clearStaff();
        } catch (IOException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnAddStaffActionPerformed

    private void btnEditStaffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditStaffActionPerformed
        try {
            updateStaff();
        } catch (IOException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnEditStaffActionPerformed

    private void btnSaveStaffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveStaffActionPerformed
        try {
            insertStaff();
        } catch (IOException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnSaveStaffActionPerformed

    private void cbJobStaffMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cbJobStaffMouseClicked
        cbJob();
    }//GEN-LAST:event_cbJobStaffMouseClicked

    private void btnExit6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExit6ActionPerformed
        pJob.setVisible(false);
    }//GEN-LAST:event_btnExit6ActionPerformed

    private void txtSearchJobKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchJobKeyReleased
        try {
            sorterJob.setRowFilter(RowFilter.regexFilter(txtSearchJob.getText(), 0));
        } catch (Exception e) {
        }
    }//GEN-LAST:event_txtSearchJobKeyReleased

    private void btnDeleteJobActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteJobActionPerformed
        deleteJob();
    }//GEN-LAST:event_btnDeleteJobActionPerformed

    private void btnAddJobActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddJobActionPerformed
        clearJob();
    }//GEN-LAST:event_btnAddJobActionPerformed

    private void btnEditJobActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditJobActionPerformed
        updateJob();
    }//GEN-LAST:event_btnEditJobActionPerformed

    private void btnSaveJobActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveJobActionPerformed
        insertJob();
    }//GEN-LAST:event_btnSaveJobActionPerformed

    private void btnExit7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExit7ActionPerformed
        pSale.setVisible(false);
    }//GEN-LAST:event_btnExit7ActionPerformed

    private void btnSearchProductSaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchProductSaleActionPerformed
        searchProduct sp = new searchProduct(this, true, txtCodeSale);
        sp.setVisible(true);
    }//GEN-LAST:event_btnSearchProductSaleActionPerformed

    private void btnSearchCustomerSaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchCustomerSaleActionPerformed
        searchCustomer sc = new searchCustomer(this, true, txtCustomerSale, txtDiscountSale);
        sc.setVisible(true);
    }//GEN-LAST:event_btnSearchCustomerSaleActionPerformed

    private void btnSaveSaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveSaleActionPerformed
        insertSale();
    }//GEN-LAST:event_btnSaveSaleActionPerformed

    private void btnDeleteSaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteSaleActionPerformed
        deleteSale();
    }//GEN-LAST:event_btnDeleteSaleActionPerformed

    private void btnAddSaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddSaleActionPerformed
        clearSale();
    }//GEN-LAST:event_btnAddSaleActionPerformed

    private void txtCodeSaleKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodeSaleKeyReleased
        DefaultTableModel dtm = (DefaultTableModel) tSale.getModel();
        String cod = txtCodeSale.getText().trim();
        int x = dtm.getRowCount();
        try {
            Connection cn = Connections.connect();
            PreparedStatement pst = cn.prepareStatement(""
                    + "SELECT codigoBarra, nombre, marca, venta, descuento, estado, foto "
                    + "FROM producto "
                    + "WHERE codigoBarra = '" + cod + "'");
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                String c = rs.getString(1);
                String name = rs.getString(2);
                String mark = rs.getString(3);
                double sale = rs.getDouble(4);
                int discount = rs.getInt(5);
                String status = rs.getString(6);
                Blob blob = rs.getBlob(7);
                byte[] data = blob.getBytes(1, (int) blob.length());
                BufferedImage img = null;
                try {
                    img = ImageIO.read(new ByteArrayInputStream(data));
                } catch (IOException ex) {
                }
                ImageIcon icon = new ImageIcon(img.getScaledInstance(lbPhotoSale.getWidth(), lbPhotoSale.getHeight(), Image.SCALE_SMOOTH));
                lbPhotoSale.setIcon(icon);
                double dis = (sale * discount) / 100;
                double importe = sale - dis;
                if ("INACTIVO".equals(status)) {
                    lbPhotoSale.setIcon(null);
                } else {
                    boolean a = true;
                    if (x > 0) {
                        for (int i = 0; i < x; i++) {
                            if (cod.equals(tSale.getValueAt(i, 1).toString())) {
                                //CANT
                                int cant = Integer.parseInt(tSale.getValueAt(i, 0).toString());
                                tSale.setValueAt(cant + 1, i, 0);
                                //IMPORTE
                                double importeT = Double.parseDouble(tSale.getValueAt(i, 6).toString());
                                tSale.setValueAt(df.format(importeT + importe), i, 6);
                                subTotalSale = subTotalSale + sale;
                                discountSale = discountSale + dis;
                                totalSale = totalSale + importe;
                                i = 1000;
                                a = false;
                            }
                        }
                        if (a == true) {
                            Vector v = new Vector();
                            v.add(0, 1);
                            v.add(1, c);
                            v.add(2, name);
                            v.add(3, mark);
                            v.add(4, sale);
                            v.add(5, discount);
                            v.add(6, importe);
                            dtm.addRow(v);
                            tSale.setModel(dtm);
                            subTotalSale = subTotalSale + sale;
                            discountSale = discountSale + dis;
                            totalSale = totalSale + importe;
                            cn.close();
                        }
                    } else {
                        Vector v = new Vector();;
                        v.add(0, 1);
                        v.add(1, c);
                        v.add(2, name);
                        v.add(3, mark);
                        v.add(4, sale);
                        v.add(5, discount);
                        v.add(6, importe);
                        dtm.addRow(v);
                        tSale.setModel(dtm);
                        subTotalSale = subTotalSale + sale;
                        discountSale = discountSale + dis;
                        totalSale = totalSale + importe;
                        cn.close();
                        btnSaveSale.setEnabled(true);
                    }
                    contadorSale = contadorSale + 1;
                    lbSubtotalSale.setText(String.valueOf(df.format(subTotalSale)));
                    lbDiscountSale.setText(String.valueOf(df.format(discountSale)));
                    lbTotalSale.setText(String.valueOf(df.format(totalSale)));
                    txtCodeSale.setText("");
                }
            }
        } catch (SQLException e) {
        }
    }//GEN-LAST:event_txtCodeSaleKeyReleased

    private void txtBuyProductKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuyProductKeyReleased
        if ("".equals(txtBuyProduct.getText().trim())) {
            JOptionPane.showMessageDialog(null, "CANTIDAD INVALIDA\n\n"
                    + "Ingrese una cantidad no nula", "ADVERTENCIA",
                    JOptionPane.WARNING_MESSAGE);
            txtBuyProduct.setText("0");
        } else {
            double buy = Double.parseDouble(txtBuyProduct.getText().trim());
            int gain = Integer.parseInt(txtGainProduct.getText().trim());
            double discount = Double.parseDouble(txtDiscountProduct.getText().trim());
            double buyGain = buy + ((buy * gain) / 100);
            double conDiscount = buyGain - ((buyGain * discount) / 100);
            txtSaleProduct.setText(df.format(conDiscount));
        }
    }//GEN-LAST:event_txtBuyProductKeyReleased

    private void txtGainProductKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGainProductKeyReleased
        if ("".equals(txtGainProduct.getText().trim())) {
            JOptionPane.showMessageDialog(null, "CANTIDAD INVALIDA\n\n"
                    + "Ingrese una cantidad no nula", "ADVERTENCIA",
                    JOptionPane.WARNING_MESSAGE);
            txtGainProduct.setText("0");
        } else {
            double buy = Double.parseDouble(txtBuyProduct.getText().trim());
            int gain = Integer.parseInt(txtGainProduct.getText().trim());
            double discount = Double.parseDouble(txtDiscountProduct.getText().trim());
            double buyGain = buy + ((buy * gain) / 100);
            double conDiscount = buyGain - ((buyGain * discount) / 100);
            txtSaleProduct.setText(df.format(conDiscount));
        }
    }//GEN-LAST:event_txtGainProductKeyReleased

    private void txtDiscountProductKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDiscountProductKeyReleased
//        if ("".equals(txtDiscountProduct.getText().trim())) {
//            JOptionPane.showMessageDialog(null, "CANTIDAD INVALIDA\n\n"
//                    + "Ingrese una cantidad no nula", "ADVERTENCIA",
//                    JOptionPane.WARNING_MESSAGE);
//            txtDiscountProduct.setText("0");
//        } else {
//            double buy = Double.parseDouble(txtBuyProduct.getText().trim());
//            int gain = Integer.parseInt(txtGainProduct.getText().trim());
//            double discount = Double.parseDouble(txtDiscountProduct.getText().trim());
//            double buyGain = buy + ((buy * gain) / 100);
//            double conDiscount = buyGain - ((buyGain * discount) / 100);
//            txtSaleProduct.setText(df.format(conDiscount));
//        }
    }//GEN-LAST:event_txtDiscountProductKeyReleased

    private void txtCodeProductKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodeProductKeyTyped
        char c = evt.getKeyChar();
        if (c < '0' || c > '9') {
            evt.consume();
        }
        if (txtCodeProduct.getText().length() >= 14) {
            evt.consume();
        }
    }//GEN-LAST:event_txtCodeProductKeyTyped

    private void txtNameProductKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNameProductKeyTyped
        char c = evt.getKeyChar();
        if (Character.isLowerCase(c)) {
            evt.setKeyChar(Character.toUpperCase(c));
        }
        if (txtNameProduct.getText().length() >= 50) {
            evt.consume();
        }
    }//GEN-LAST:event_txtNameProductKeyTyped

    private void txtBrendProductKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBrendProductKeyTyped
        char c = evt.getKeyChar();
        if (Character.isLowerCase(c)) {
            evt.setKeyChar(Character.toUpperCase(c));
        }
        if (txtBrendProduct.getText().length() >= 40) {
            evt.consume();
        }
    }//GEN-LAST:event_txtBrendProductKeyTyped

    private void txtStockProductKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtStockProductKeyTyped
        char c = evt.getKeyChar();
        if (c < '0' || c > '9') {
            evt.consume();
        }
        if (txtStockProduct.getText().length() >= 5) {
            evt.consume();
        }
    }//GEN-LAST:event_txtStockProductKeyTyped

    private void txtStockMaxProductKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtStockMaxProductKeyTyped
        char c = evt.getKeyChar();
        if (c < '0' || c > '9') {
            evt.consume();
        }
        if (txtStockMaxProduct.getText().length() >= 5) {
            evt.consume();
        }
    }//GEN-LAST:event_txtStockMaxProductKeyTyped

    private void txtStockMinProductKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtStockMinProductKeyTyped
        char c = evt.getKeyChar();
        if (c < '0' || c > '9') {
            evt.consume();
        }
        if (txtStockMinProduct.getText().length() >= 5) {
            evt.consume();
        }
    }//GEN-LAST:event_txtStockMinProductKeyTyped

    private void txtBuyProductKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuyProductKeyTyped
        char c = evt.getKeyChar();
        if ((c < '0' || c > '9') && (c != '.')) {
            evt.consume();
        } else {
            if (txtBuyProduct.getText().contains(".")) {
                if ((c < '0' || c > '9') && (c == '.')) {
                    evt.consume();
                }
            }
        }
    }//GEN-LAST:event_txtBuyProductKeyTyped

    private void txtGainProductKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGainProductKeyTyped
        char c = evt.getKeyChar();
        if (c < '0' || c > '9') {
            evt.consume();
        }
        if (txtGainProduct.getText().length() >= 2) {
            evt.consume();
        }
    }//GEN-LAST:event_txtGainProductKeyTyped

    private void txtDiscountProductKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDiscountProductKeyTyped
//        char c = evt.getKeyChar();
//        if (c < '0' || c > '9') {
//            evt.consume();
//        }
//        if (txtDiscountProduct.getText().length() >= 2) {
//            evt.consume();
//        }
    }//GEN-LAST:event_txtDiscountProductKeyTyped

    private void txtSearchProductKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchProductKeyTyped
        char c = evt.getKeyChar();
        if (Character.isLowerCase(c)) {
            evt.setKeyChar(Character.toUpperCase(c));
        }
    }//GEN-LAST:event_txtSearchProductKeyTyped

    private void txtSearchJobKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchJobKeyTyped
        char c = evt.getKeyChar();
        if (Character.isLowerCase(c)) {
            evt.setKeyChar(Character.toUpperCase(c));
        }
    }//GEN-LAST:event_txtSearchJobKeyTyped

    private void txtNameCustomerKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNameCustomerKeyTyped
        char c = evt.getKeyChar();
        if (Character.isLowerCase(c)) {
            evt.setKeyChar(Character.toUpperCase(c));
        }
        if (txtNameCustomer.getText().length() >= 70) {
            evt.consume();
        }
    }//GEN-LAST:event_txtNameCustomerKeyTyped

    private void txtPhoneCustomerKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPhoneCustomerKeyTyped
        char c = evt.getKeyChar();
        if (c < '0' || c > '9') {
            evt.consume();
        }
        if (txtPhoneCustomer.getText().length() >= 14) {
            evt.consume();
        }
    }//GEN-LAST:event_txtPhoneCustomerKeyTyped

    private void txtDiscountCustomerKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDiscountCustomerKeyTyped
        char c = evt.getKeyChar();
        if (c < '0' || c > '9') {
            evt.consume();
        }
        if (txtDiscountCustomer.getText().length() >= 2) {
            evt.consume();
        }
    }//GEN-LAST:event_txtDiscountCustomerKeyTyped

    private void txtSearchSupplierKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchSupplierKeyTyped
        char c = evt.getKeyChar();
        if (Character.isLowerCase(c)) {
            evt.setKeyChar(Character.toUpperCase(c));
        }
    }//GEN-LAST:event_txtSearchSupplierKeyTyped

    private void txtIdSupplierKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtIdSupplierKeyTyped
        char c = evt.getKeyChar();
        if (c < '0' || c > '9') {
            evt.consume();
        }
        if (txtIdSupplier.getText().length() >= 10) {
            evt.consume();
        }
    }//GEN-LAST:event_txtIdSupplierKeyTyped

    private void txtDescriptionSuppllierKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDescriptionSuppllierKeyTyped
        char c = evt.getKeyChar();
        if (Character.isLowerCase(c)) {
            evt.setKeyChar(Character.toUpperCase(c));
        }
        if (txtDescriptionSuppllier.getText().length() >= 50) {
            evt.consume();
        }
    }//GEN-LAST:event_txtDescriptionSuppllierKeyTyped

    private void txtColonySupplierKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtColonySupplierKeyTyped
        char c = evt.getKeyChar();
        if (Character.isLowerCase(c)) {
            evt.setKeyChar(Character.toUpperCase(c));
        }
        if (txtColonySupplier.getText().length() >= 30) {
            evt.consume();
        }
    }//GEN-LAST:event_txtColonySupplierKeyTyped

    private void txtLocateSupplierKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtLocateSupplierKeyTyped
        char c = evt.getKeyChar();
        if (Character.isLowerCase(c)) {
            evt.setKeyChar(Character.toUpperCase(c));
        }
        if (txtLocateSupplier.getText().length() >= 30) {
            evt.consume();
        }
    }//GEN-LAST:event_txtLocateSupplierKeyTyped

    private void txtPhoneSupplierKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPhoneSupplierKeyTyped
        char c = evt.getKeyChar();
        if (c < '0' || c > '9') {
            evt.consume();
        }
        if (txtPhoneSupplier.getText().length() >= 14) {
            evt.consume();
        }
    }//GEN-LAST:event_txtPhoneSupplierKeyTyped

    private void txtEmailSupplierKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmailSupplierKeyTyped
        char c = evt.getKeyChar();
        if (Character.isLowerCase(c)) {
            evt.setKeyChar(Character.toUpperCase(c));
        }
        if (txtEmailSupplier.getText().length() >= 70) {
            evt.consume();
        }
    }//GEN-LAST:event_txtEmailSupplierKeyTyped

    private void txtNameSupplierKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNameSupplierKeyTyped
        char c = evt.getKeyChar();
        if (Character.isLowerCase(c)) {
            evt.setKeyChar(Character.toUpperCase(c));
        }
        if (txtNameSupplier.getText().length() >= 50) {
            evt.consume();
        }
    }//GEN-LAST:event_txtNameSupplierKeyTyped

    private void txtStreetSupplierKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtStreetSupplierKeyTyped
        char c = evt.getKeyChar();
        if (Character.isLowerCase(c)) {
            evt.setKeyChar(Character.toUpperCase(c));
        }
        if (txtStreetSupplier.getText().length() >= 30) {
            evt.consume();
        }
    }//GEN-LAST:event_txtStreetSupplierKeyTyped

    private void txtSearchStaffKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchStaffKeyTyped
        char c = evt.getKeyChar();
        if (Character.isLowerCase(c)) {
            evt.setKeyChar(Character.toUpperCase(c));
        }
    }//GEN-LAST:event_txtSearchStaffKeyTyped

    private void txtIdUserKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtIdUserKeyTyped
        char c = evt.getKeyChar();
        if (Character.isLowerCase(c)) {
            evt.setKeyChar(Character.toUpperCase(c));
        }
        if (txtIdUser.getText().length() >= 10) {
            evt.consume();
        }
    }//GEN-LAST:event_txtIdUserKeyTyped

    private void txtNameStaffKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNameStaffKeyTyped
        char c = evt.getKeyChar();
        if (Character.isLowerCase(c)) {
            evt.setKeyChar(Character.toUpperCase(c));
        }
        if (txtNameStaff.getText().length() >= 30) {
            evt.consume();
        }
    }//GEN-LAST:event_txtNameStaffKeyTyped

    private void txtAPStaffKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAPStaffKeyTyped
        char c = evt.getKeyChar();
        if (Character.isLowerCase(c)) {
            evt.setKeyChar(Character.toUpperCase(c));
        }
        if (txtAPStaff.getText().length() >= 25) {
            evt.consume();
        }
    }//GEN-LAST:event_txtAPStaffKeyTyped

    private void txtAMStaffKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAMStaffKeyTyped
        char c = evt.getKeyChar();
        if (Character.isLowerCase(c)) {
            evt.setKeyChar(Character.toUpperCase(c));
        }
        if (txtAMStaff.getText().length() >= 25) {
            evt.consume();
        }
    }//GEN-LAST:event_txtAMStaffKeyTyped

    private void txtPhoneStaffKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPhoneStaffKeyTyped
        char c = evt.getKeyChar();
        if (c < '0' || c > '9') {
            evt.consume();
        }
        if (txtPhoneStaff.getText().length() >= 14) {
            evt.consume();
        }
    }//GEN-LAST:event_txtPhoneStaffKeyTyped

    private void txtEmailStaffKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmailStaffKeyTyped
        char c = evt.getKeyChar();
        if (Character.isLowerCase(c)) {
            evt.setKeyChar(Character.toUpperCase(c));
        }
        if (txtEmailStaff.getText().length() >= 70) {
            evt.consume();
        }
    }//GEN-LAST:event_txtEmailStaffKeyTyped

    private void txtUserStaffKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUserStaffKeyTyped
        char c = evt.getKeyChar();
        if (Character.isLowerCase(c)) {
            evt.setKeyChar(Character.toUpperCase(c));
        }
        if (txtUserStaff.getText().length() >= 50) {
            evt.consume();
        }
    }//GEN-LAST:event_txtUserStaffKeyTyped

    private void txtPasswordStaffKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPasswordStaffKeyTyped
        char c = evt.getKeyChar();
        if (Character.isLowerCase(c)) {
            evt.setKeyChar(Character.toUpperCase(c));
        }
        if (txtPasswordStaff.getText().length() >= 12) {
            evt.consume();
        }
    }//GEN-LAST:event_txtPasswordStaffKeyTyped

    private void txtNameJobKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNameJobKeyTyped
        char c = evt.getKeyChar();
        if (Character.isLowerCase(c)) {
            evt.setKeyChar(Character.toUpperCase(c));
        }
        if (txtNameJob.getText().length() >= 50) {
            evt.consume();
        }
    }//GEN-LAST:event_txtNameJobKeyTyped

    private void btnExit8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExit8ActionPerformed
        pSetting.setVisible(false);
    }//GEN-LAST:event_btnExit8ActionPerformed

    private void btnTool11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTool11ActionPerformed
        txtColorSetting.setEnabled(true);
    }//GEN-LAST:event_btnTool11ActionPerformed

    private void jRadioButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton7ActionPerformed
        txtColorSetting.setText("003366");
    }//GEN-LAST:event_jRadioButton7ActionPerformed

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed
        txtColorSetting.setText("660066");
    }//GEN-LAST:event_jRadioButton1ActionPerformed

    private void btnTool12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTool12ActionPerformed
        file(lbPhotoBannerSetting2, lbRutaBannerSettings);
    }//GEN-LAST:event_btnTool12ActionPerformed

    private void btnTool13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTool13ActionPerformed
        if ("".equals(txtColorSetting.getText().trim())) {
            JOptionPane.showMessageDialog(null, "CAMPOS VACIOS\n\n"
                    + "Es necesario llenar todos los campos.", "ADVERTENCIA",
                    JOptionPane.WARNING_MESSAGE);
        } else {
            updateColor();
            txtColorSetting.setText("3757A4");
            lbRutaBannerSettings.setText("");
            txtColorSetting.setEnabled(false);
            lbPhotoBannerSetting2.setIcon(null);
            rbColor1.setSelected(true);
        }
    }//GEN-LAST:event_btnTool13ActionPerformed

    private void btnTool2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTool2ActionPerformed
        setVisibleSetting();
        boldLetter();
        btnTool2.setFont(new Font("Arial", 1, 14));
        pTool2.setVisible(true);
    }//GEN-LAST:event_btnTool2ActionPerformed

    private void btnTool22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTool22ActionPerformed
        if ("".equals(txtNamePlantSetting.getText().trim()) || "".equals(txtDescriptionSetting.getText().trim()) || "".equals(txtStreetPlantSetting.getText().trim())
                || "".equals(txtColonyPlantSetting.getText().trim()) || "".equals(txtLocatePlantSetting.getText().trim()) || "".equals(txtPhonePlantSetting.getText().trim())
                || "".equals(txtEmailPlantSetting.getText().trim())) {
            JOptionPane.showMessageDialog(null, "CAMPOS VACIOS\n\n"
                    + "Es necesario llenar todos los campos.", "ADVERTENCIA",
                    JOptionPane.WARNING_MESSAGE);
        } else {
            updatePlant();
            lbRutaPlantSetting.setText("");
            lbRutaPlantSetting1.setText("");
        }
    }//GEN-LAST:event_btnTool22ActionPerformed

    private void jRadioButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton6ActionPerformed
        txtColorSetting.setText("006666");
    }//GEN-LAST:event_jRadioButton6ActionPerformed

    private void jRadioButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton5ActionPerformed
        txtColorSetting.setText("666600");
    }//GEN-LAST:event_jRadioButton5ActionPerformed

    private void jRadioButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton4ActionPerformed
        txtColorSetting.setText("336600");
    }//GEN-LAST:event_jRadioButton4ActionPerformed

    private void jRadioButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton3ActionPerformed
        txtColorSetting.setText("990000");
    }//GEN-LAST:event_jRadioButton3ActionPerformed

    private void rbColor1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbColor1ActionPerformed
        txtColorSetting.setText("3757A4");
    }//GEN-LAST:event_rbColor1ActionPerformed

    private void btnTool1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTool1ActionPerformed
        setVisibleSetting();
        boldLetter();
        btnTool1.setFont(new Font("Arial", 1, 14));
        pTool1.setVisible(true);
    }//GEN-LAST:event_btnTool1ActionPerformed

    private void btnTool21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTool21ActionPerformed
        file(lbPhotoPlantSetting, lbRutaPlantSetting);
    }//GEN-LAST:event_btnTool21ActionPerformed

    private void btnTool23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTool23ActionPerformed
        file(lbPhotoPlantSetting1, lbRutaPlantSetting1);
    }//GEN-LAST:event_btnTool23ActionPerformed

    private void txtNamePlantSettingKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNamePlantSettingKeyTyped
        char c = evt.getKeyChar();
        if (Character.isLowerCase(c)) {
            evt.setKeyChar(Character.toUpperCase(c));
        }
        if (txtNamePlantSetting.getText().length() >= 70) {
            evt.consume();
        }
    }//GEN-LAST:event_txtNamePlantSettingKeyTyped

    private void txtDescriptionSettingKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDescriptionSettingKeyTyped
        char c = evt.getKeyChar();
        if (Character.isLowerCase(c)) {
            evt.setKeyChar(Character.toUpperCase(c));
        }
        if (txtDescriptionSetting.getText().length() >= 70) {
            evt.consume();
        }
    }//GEN-LAST:event_txtDescriptionSettingKeyTyped

    private void txtStreetPlantSettingKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtStreetPlantSettingKeyTyped
        char c = evt.getKeyChar();
        if (Character.isLowerCase(c)) {
            evt.setKeyChar(Character.toUpperCase(c));
        }
        if (txtStreetPlantSetting.getText().length() >= 30) {
            evt.consume();
        }
    }//GEN-LAST:event_txtStreetPlantSettingKeyTyped

    private void txtColonyPlantSettingKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtColonyPlantSettingKeyTyped
        char c = evt.getKeyChar();
        if (Character.isLowerCase(c)) {
            evt.setKeyChar(Character.toUpperCase(c));
        }
        if (txtColonyPlantSetting.getText().length() >= 30) {
            evt.consume();
        }
    }//GEN-LAST:event_txtColonyPlantSettingKeyTyped

    private void txtLocatePlantSettingKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtLocatePlantSettingKeyTyped
        char c = evt.getKeyChar();
        if (Character.isLowerCase(c)) {
            evt.setKeyChar(Character.toUpperCase(c));
        }
        if (txtLocatePlantSetting.getText().length() >= 30) {
            evt.consume();
        }
    }//GEN-LAST:event_txtLocatePlantSettingKeyTyped

    private void txtPhonePlantSettingKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPhonePlantSettingKeyTyped
        char c = evt.getKeyChar();
        if (c < '0' || c > '9') {
            evt.consume();
        }
        if (txtPhonePlantSetting.getText().length() >= 14) {
            evt.consume();
        }
    }//GEN-LAST:event_txtPhonePlantSettingKeyTyped

    private void txtEmailPlantSettingKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmailPlantSettingKeyTyped
        char c = evt.getKeyChar();
        if (Character.isLowerCase(c)) {
            evt.setKeyChar(Character.toUpperCase(c));
        }
        if (txtEmailPlantSetting.getText().length() >= 70) {
            evt.consume();
        }
    }//GEN-LAST:event_txtEmailPlantSettingKeyTyped

    private void btnTool51ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTool51ActionPerformed
        file(lbPhotoPlantSetting, lbRutaBackupSetting);
    }//GEN-LAST:event_btnTool51ActionPerformed

    private void btnTool52ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTool52ActionPerformed
        lbRutaBackupSetting.setText("");
    }//GEN-LAST:event_btnTool52ActionPerformed

    private void btnTool5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTool5ActionPerformed
        setVisibleSetting();
        boldLetter();
        btnTool5.setFont(new Font("Arial", 1, 14));
        pTool5.setVisible(true);
    }//GEN-LAST:event_btnTool5ActionPerformed

    private void txtColorSettingKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtColorSettingKeyTyped
        char c = evt.getKeyChar();
        if (c < '0' || c > '9') {
            evt.consume();
        }
        if (txtColorSetting.getText().length() >= 6) {
            evt.consume();
        }
    }//GEN-LAST:event_txtColorSettingKeyTyped

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JProgressBar bStorageCustomer;
    private javax.swing.JProgressBar bStorageJob;
    private javax.swing.JProgressBar bStorageProduct;
    private javax.swing.JProgressBar bStorageStaff;
    private javax.swing.JProgressBar bStorageSupplier;
    private javax.swing.JButton btnAddCustomer;
    private javax.swing.JButton btnAddJob;
    private javax.swing.JButton btnAddProduct;
    private javax.swing.JButton btnAddSale;
    private javax.swing.JButton btnAddStaff;
    private javax.swing.JButton btnAddSupplier;
    public javax.swing.JButton btnBuy;
    public javax.swing.JButton btnCancel;
    private javax.swing.JButton btnCloseSession;
    public javax.swing.JButton btnCustomer;
    private javax.swing.JButton btnDeleteCustomer;
    private javax.swing.JButton btnDeleteJob;
    private javax.swing.JButton btnDeleteProduct;
    private javax.swing.JButton btnDeleteSale;
    private javax.swing.JButton btnDeleteStaff;
    private javax.swing.JButton btnDeleteSupplier;
    private javax.swing.JButton btnDetailsSession;
    private javax.swing.JButton btnEditCustomer;
    private javax.swing.JButton btnEditJob;
    private javax.swing.JButton btnEditProduct;
    private javax.swing.JButton btnEditStaff;
    private javax.swing.JButton btnEditSupplier;
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnExit1;
    private javax.swing.JButton btnExit2;
    private javax.swing.JButton btnExit3;
    private javax.swing.JButton btnExit4;
    private javax.swing.JButton btnExit5;
    private javax.swing.JButton btnExit6;
    private javax.swing.JButton btnExit7;
    private javax.swing.JButton btnExit8;
    private javax.swing.JButton btnFavorite;
    private javax.swing.JButton btnHome;
    private javax.swing.JButton btnInputSale;
    public javax.swing.JButton btnJob;
    private javax.swing.JButton btnMinimize;
    private javax.swing.JButton btnNotifications;
    private javax.swing.JButton btnOutputSale;
    private javax.swing.JButton btnPriceSale;
    public javax.swing.JButton btnProduct;
    public javax.swing.JButton btnSale;
    private javax.swing.JButton btnSaveCustomer;
    private javax.swing.JButton btnSaveJob;
    private javax.swing.JButton btnSaveProduct;
    private javax.swing.JButton btnSaveSale;
    private javax.swing.JButton btnSaveStaff;
    private javax.swing.JButton btnSaveSupplier;
    private javax.swing.JButton btnSearchCustomerSale;
    private javax.swing.JButton btnSearchProductSale;
    public javax.swing.JButton btnSetting;
    public javax.swing.JButton btnStaff;
    public javax.swing.JButton btnStatistic;
    public javax.swing.JButton btnSupplier;
    private javax.swing.JButton btnTool1;
    private javax.swing.JButton btnTool11;
    private javax.swing.JButton btnTool12;
    private javax.swing.JButton btnTool13;
    private javax.swing.JButton btnTool2;
    private javax.swing.JButton btnTool21;
    private javax.swing.JButton btnTool22;
    private javax.swing.JButton btnTool23;
    private javax.swing.JButton btnTool3;
    private javax.swing.JButton btnTool4;
    private javax.swing.JButton btnTool5;
    private javax.swing.JButton btnTool51;
    private javax.swing.JButton btnTool52;
    private javax.swing.JComboBox<String> cbDepartmentProduct;
    private javax.swing.JComboBox<String> cbJobStaff;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel77;
    private javax.swing.JLabel jLabel78;
    private javax.swing.JLabel jLabel79;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel80;
    private javax.swing.JLabel jLabel81;
    private javax.swing.JLabel jLabel82;
    private javax.swing.JLabel jLabel83;
    private javax.swing.JLabel jLabel84;
    private javax.swing.JLabel jLabel85;
    private javax.swing.JLabel jLabel86;
    private javax.swing.JLabel jLabel87;
    private javax.swing.JLabel jLabel88;
    private javax.swing.JLabel jLabel89;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel90;
    private javax.swing.JLabel jLabel91;
    private javax.swing.JLabel jLabel92;
    private javax.swing.JLabel jLabel93;
    private javax.swing.JLabel jLabel94;
    private javax.swing.JLabel jLabel95;
    private javax.swing.JLabel jLabel98;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel35;
    private javax.swing.JPanel jPanel36;
    private javax.swing.JPanel jPanel37;
    private javax.swing.JPanel jPanel38;
    private javax.swing.JPanel jPanel39;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel40;
    private javax.swing.JPanel jPanel41;
    private javax.swing.JPanel jPanel42;
    private javax.swing.JPanel jPanel43;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JRadioButton jRadioButton5;
    private javax.swing.JRadioButton jRadioButton6;
    private javax.swing.JRadioButton jRadioButton7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane17;
    private javax.swing.JScrollPane jScrollPane18;
    private javax.swing.JScrollPane jScrollPane19;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator11;
    private javax.swing.JSeparator jSeparator12;
    private javax.swing.JSeparator jSeparator13;
    private javax.swing.JSeparator jSeparator14;
    private javax.swing.JSeparator jSeparator15;
    private javax.swing.JSeparator jSeparator16;
    private javax.swing.JSeparator jSeparator17;
    private javax.swing.JSeparator jSeparator18;
    private javax.swing.JSeparator jSeparator19;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator20;
    private javax.swing.JSeparator jSeparator21;
    private javax.swing.JSeparator jSeparator22;
    private javax.swing.JSeparator jSeparator23;
    private javax.swing.JSeparator jSeparator24;
    private javax.swing.JSeparator jSeparator25;
    private javax.swing.JSeparator jSeparator26;
    private javax.swing.JSeparator jSeparator27;
    private javax.swing.JSeparator jSeparator28;
    private javax.swing.JSeparator jSeparator29;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator30;
    private javax.swing.JSeparator jSeparator31;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTabbedPane jTabbedPane3;
    private javax.swing.JTabbedPane jTabbedPane4;
    private javax.swing.JTabbedPane jTabbedPane5;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JTextArea jTextArea4;
    private javax.swing.JTextArea jTextArea5;
    private javax.swing.JTextArea jTextArea6;
    private javax.swing.JLabel lbBackgroundHome;
    private javax.swing.JLabel lbBannerSale;
    private javax.swing.JLabel lbColony;
    private javax.swing.JLabel lbColorSettings1;
    private javax.swing.JLabel lbCountCustomer;
    private javax.swing.JLabel lbCountJob;
    private javax.swing.JLabel lbCountProduct;
    private javax.swing.JLabel lbCountStaff;
    private javax.swing.JLabel lbCountSupplier;
    private javax.swing.JLabel lbDate;
    private javax.swing.JLabel lbDiscountSale;
    private javax.swing.JLabel lbFrankSale;
    private javax.swing.JLabel lbIdEstableciment;
    private javax.swing.JLabel lbIdUser;
    private javax.swing.JLabel lbIdUser1;
    private javax.swing.JLabel lbIdUser2;
    private javax.swing.JLabel lbIdUser3;
    private javax.swing.JLabel lbIdUser4;
    private javax.swing.JLabel lbIdUser5;
    private javax.swing.JLabel lbIdUser6;
    private javax.swing.JLabel lbIdUser7;
    private javax.swing.JLabel lbJobUser;
    private javax.swing.JLabel lbLocation;
    private javax.swing.JLabel lbLogoHome;
    private javax.swing.JLabel lbLogoMenu;
    private javax.swing.JLabel lbNameUser;
    private javax.swing.JLabel lbPattern;
    private javax.swing.JLabel lbPhoneEstableciment;
    private javax.swing.JLabel lbPhotoBannerSetting1;
    private javax.swing.JLabel lbPhotoBannerSetting2;
    private javax.swing.JLabel lbPhotoPlantSetting;
    private javax.swing.JLabel lbPhotoPlantSetting1;
    private javax.swing.JLabel lbPhotoProduct;
    private javax.swing.JLabel lbPhotoSale;
    private javax.swing.JLabel lbPhotoStaff;
    private javax.swing.JLabel lbPhotoSupplier;
    private javax.swing.JLabel lbPhotoUser;
    private javax.swing.JLabel lbRutaBackupSetting;
    private javax.swing.JLabel lbRutaBannerSettings;
    private javax.swing.JLabel lbRutaPlantSetting;
    private javax.swing.JLabel lbRutaPlantSetting1;
    private javax.swing.JLabel lbRutaProduct;
    private javax.swing.JLabel lbRutaStaff;
    private javax.swing.JLabel lbRutaSupplier;
    private javax.swing.JLabel lbStorageCustomer;
    private javax.swing.JLabel lbStorageJob;
    private javax.swing.JLabel lbStorageProduct;
    private javax.swing.JLabel lbStorageStaff;
    private javax.swing.JLabel lbStorageSupplier;
    private javax.swing.JLabel lbStreet;
    public javax.swing.JLabel lbSubtotalSale;
    private javax.swing.JLabel lbTime;
    private javax.swing.JLabel lbTime1;
    private javax.swing.JLabel lbTitleCustomer;
    private javax.swing.JLabel lbTitleJob;
    private javax.swing.JLabel lbTitleProduct;
    private javax.swing.JLabel lbTitleSale;
    private javax.swing.JLabel lbTitleSetting;
    private javax.swing.JLabel lbTitleStaff;
    private javax.swing.JLabel lbTitleSupplier;
    private javax.swing.JLabel lbTotalSale;
    private javax.swing.JLabel lbVersion;
    private javax.swing.JPanel pBannerSale;
    private javax.swing.JPanel pCustomer;
    private javax.swing.JPanel pDetailsSession;
    private javax.swing.JPanel pHome;
    private javax.swing.JPanel pJob;
    private javax.swing.JPanel pMenu;
    private javax.swing.JPanel pProduct;
    private javax.swing.JPanel pSale;
    private javax.swing.JPanel pSetting;
    private javax.swing.JPanel pStaff;
    private javax.swing.JPanel pSupplier;
    private javax.swing.JScrollPane pTool1;
    private javax.swing.JScrollPane pTool2;
    private javax.swing.JScrollPane pTool5;
    private javax.swing.JRadioButton rbActiveProduct;
    private javax.swing.JRadioButton rbActiveStaff;
    private javax.swing.JRadioButton rbActiveSupplier;
    private javax.swing.JRadioButton rbBrendProduct;
    private javax.swing.JRadioButton rbCanBuy;
    private javax.swing.JRadioButton rbCanSale;
    private javax.swing.JRadioButton rbCodeProduct;
    private javax.swing.JRadioButton rbColor1;
    private javax.swing.JRadioButton rbConBuy;
    private javax.swing.JRadioButton rbConSale;
    private javax.swing.JRadioButton rbConStatistic;
    private javax.swing.JRadioButton rbDepartmentProduct;
    private javax.swing.JRadioButton rbIdStaff;
    private javax.swing.JRadioButton rbIdSupplier;
    private javax.swing.JRadioButton rbInactiveProduct;
    private javax.swing.JRadioButton rbInactiveStaff;
    private javax.swing.JRadioButton rbInactiveSupplier;
    private javax.swing.JRadioButton rbJobStaff;
    private javax.swing.JRadioButton rbLocationSupplier;
    private javax.swing.JRadioButton rbNameCustomer;
    private javax.swing.JRadioButton rbNameJob;
    private javax.swing.JRadioButton rbNameProduct;
    private javax.swing.JRadioButton rbNameStaff;
    private javax.swing.JRadioButton rbNameSupplier;
    private javax.swing.JRadioButton rbPhoneSupplier;
    private javax.swing.JRadioButton rbRegBuy;
    private javax.swing.JRadioButton rbRegCustomer;
    private javax.swing.JRadioButton rbRegJob;
    private javax.swing.JRadioButton rbRegProduct;
    private javax.swing.JRadioButton rbRegSale;
    private javax.swing.JRadioButton rbRegStaff;
    private javax.swing.JRadioButton rbRegSupplier;
    private javax.swing.JRadioButton rbSettingJob;
    private javax.swing.JRadioButton rbUserStaff;
    private javax.swing.ButtonGroup searchProduct;
    private javax.swing.ButtonGroup searchStaff;
    private javax.swing.ButtonGroup searchSupplier;
    private javax.swing.JLabel shadowHome;
    private javax.swing.ButtonGroup statusProduct;
    private javax.swing.ButtonGroup statusStaff;
    private javax.swing.ButtonGroup statusSupplier;
    private javax.swing.ButtonGroup stylesSetting;
    private javax.swing.JTable tCustomer;
    private javax.swing.JTable tJob;
    private javax.swing.JTable tProduct;
    private javax.swing.JTable tSale;
    private javax.swing.JTable tStaff;
    private javax.swing.JTable tSupplier;
    private javax.swing.JTextArea taCantDepartment;
    private javax.swing.JTextArea taCantJobStaff;
    private javax.swing.JTextArea taCustomer;
    private javax.swing.JTextArea taDepartment;
    private javax.swing.JTextArea taJobStaff;
    private javax.swing.JTextArea taTopSupplier;
    private javax.swing.JTextField txtAMStaff;
    private javax.swing.JTextField txtAPStaff;
    private javax.swing.JTextField txtBrendProduct;
    private javax.swing.JTextField txtBuyProduct;
    private javax.swing.JTextField txtCodeProduct;
    private javax.swing.JTextField txtCodeSale;
    private javax.swing.JTextField txtColonyPlantSetting;
    private javax.swing.JTextField txtColonySupplier;
    private javax.swing.JTextField txtColorSetting;
    private javax.swing.JTextField txtCustomerSale;
    private javax.swing.JTextField txtDescriptionSetting;
    private javax.swing.JTextField txtDescriptionSuppllier;
    private javax.swing.JTextField txtDiscountCustomer;
    private javax.swing.JTextField txtDiscountProduct;
    private javax.swing.JTextField txtDiscountSale;
    private javax.swing.JTextField txtEmailPlantSetting;
    private javax.swing.JTextField txtEmailStaff;
    private javax.swing.JTextField txtEmailSupplier;
    private javax.swing.JTextField txtGainProduct;
    private javax.swing.JTextField txtIdSupplier;
    private javax.swing.JTextField txtIdUser;
    private javax.swing.JTextField txtLocatePlantSetting;
    private javax.swing.JTextField txtLocateSupplier;
    private javax.swing.JTextField txtNameCustomer;
    private javax.swing.JTextField txtNameJob;
    private javax.swing.JTextField txtNamePlantSetting;
    private javax.swing.JTextField txtNameProduct;
    private javax.swing.JTextField txtNameStaff;
    private javax.swing.JTextField txtNameSupplier;
    private javax.swing.JTextField txtNoSale;
    private javax.swing.JPasswordField txtPasswordStaff;
    private javax.swing.JTextField txtPhoneCustomer;
    private javax.swing.JTextField txtPhonePlantSetting;
    private javax.swing.JTextField txtPhoneStaff;
    private javax.swing.JTextField txtPhoneSupplier;
    private javax.swing.JTextField txtSaleProduct;
    private javax.swing.JTextField txtSearchCustomer;
    private javax.swing.JTextField txtSearchJob;
    private javax.swing.JTextField txtSearchProduct;
    private javax.swing.JTextField txtSearchStaff;
    private javax.swing.JTextField txtSearchSupplier;
    private javax.swing.JTextField txtStockMaxProduct;
    private javax.swing.JTextField txtStockMinProduct;
    private javax.swing.JTextField txtStockProduct;
    private javax.swing.JTextField txtStreetPlantSetting;
    private javax.swing.JTextField txtStreetSupplier;
    private javax.swing.JTextField txtUserStaff;
    // End of variables declaration//GEN-END:variables
}
