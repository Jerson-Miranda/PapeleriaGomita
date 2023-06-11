package classes;
import java.sql.*;
import javax.swing.JOptionPane;

public class Connections {
    public static Connection connect() {
        try {
            Connection cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bd_gomita", "root", "");
            return cn;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "PERDIDA DE CONEXION CON LA BD\n\n"
                    + "Es posible que haya ocurrido un problema al intentar\n"
                    + "conectar con la base de datos, porfavor intente de nuevo.\n\n"
                    + "Si el problema permanece, favor de comunicarse con el\n"
                    + "desarrollador.\n", "ERROR 001", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }
}
