import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

public class TestConnection {
    public static void main(String[] args) {
        String url  = "jdbc:postgresql://ep-small-meadow-aclg1sp4-pooler.sa-east-1.aws.neon.tech/neondb?sslmode=require";
        String user = "neondb_owner";
        String pass = "npg_8IWeu2akiUHX";

        System.out.println("Conectando a Neon PostgreSQL...");
        try {
            Class.forName("org.postgresql.Driver");
            long start = System.currentTimeMillis();
            Connection conn = DriverManager.getConnection(url, user, pass);
            long elapsed = System.currentTimeMillis() - start;

            ResultSet rs = conn.createStatement().executeQuery("SELECT version()");
            if (rs.next()) {
                System.out.println("✅ ¡CONECTADO! (" + elapsed + " ms)");
                System.out.println("   Versión: " + rs.getString(1));
            }
            conn.close();
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
