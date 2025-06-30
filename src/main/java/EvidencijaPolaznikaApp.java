import com.microsoft.sqlserver.jdbc.SQLServerDataSource;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Scanner;

public class EvidencijaPolaznikaApp {

    public static void main(String[] args) {

        DataSource dataSource = createDataSource();
        try (Connection connection = dataSource.getConnection()) {
            System.out.println("Spojeni ste na bazu podataka!");

            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("\nIzbornik:");
                System.out.println("1. Unos novog polaznika");
                System.out.println("2. Unos novog programa obrazovanja");
                System.out.println("3. Upis polaznika na program obrazovanja");
                System.out.println("4. Prebaci polaznika iz jednog u drugi program obrazovanja");
                System.out.println("5. Ispiši polaznike za zadani program obrazovanja");
                System.out.println("6. Izlaz");
                System.out.print("Odaberite opciju iz izbornika: ");

                int izbor = scanner.nextInt();
                scanner.nextLine();

                switch (izbor) {
                    case 1 -> unosNovogPolaznika(connection, scanner);
                    case 2 -> unosNovogProgramaObrazovanja(connection, scanner);
                    case 3 -> upisPolaznika(connection, scanner);
                    case 4 -> transferPolaznika(connection, scanner);
                    case 5 -> ispisPolaznikaZaProgram(connection, scanner);
                    case 6 -> {
                        return;
                    }
                    default -> System.out.println("Greška, pokušajte ponovno!");
                }
            }
        } catch (SQLException e) {
            System.err.println("Greška s bazom:");
            e.printStackTrace();
        }
    }

    private static void unosNovogPolaznika(Connection connection, Scanner scanner) throws SQLException {

        System.out.print("Unesite ime polaznika: ");
        String ime = scanner.nextLine();
        System.out.print("Unesite prezime polaznika: ");
        String prezime = scanner.nextLine();

        String query = "INSERT INTO Polaznik (Ime, Prezime) VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, ime);
            ps.setString(2, prezime);
            ps.executeUpdate();
            System.out.println("Polaznik je dodan!");
        }
    }

    private static void unosNovogProgramaObrazovanja(Connection connection, Scanner scanner) throws SQLException {

        System.out.print("Unesite naziv programa obrazovanja: ");
        String imePrograma = scanner.nextLine();
        System.out.print("Unesite broj CSVET bodova: ");
        int csvetBodovi = scanner.nextInt();

        String query = "INSERT INTO ProgramObrazovanja (Naziv, CSVET) VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, imePrograma);
            ps.setInt(2, csvetBodovi);
            ps.executeUpdate();
            System.out.println("Program obrazovanja je dodan!");
        }
    }

    private static void upisPolaznika(Connection connection, Scanner scanner) throws SQLException {

        System.out.print("Unesite ID polaznika: ");
        int idPolaznika = scanner.nextInt();
        System.out.print("Unesite ID programa obrazovanja: ");
        int idPrograma = scanner.nextInt();

        String query = "INSERT INTO Upis (IDPolaznik, IDProgramObrazovanja) VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, idPolaznika);
            ps.setInt(2, idPrograma);
            ps.executeUpdate();
            System.out.println("Polaznik je upisan u program!");
        }
    }

    private static void transferPolaznika(Connection connection, Scanner scanner) throws SQLException {

        System.out.print("Unesite ID polaznika: ");
        int idPolaznika = scanner.nextInt();
        System.out.print("Unesite ID aktualnog programa: ");
        int idAktualnogPrograma = scanner.nextInt();
        System.out.print("Unesite ID od novog programa: ");
        int idNovogPrograma = scanner.nextInt();

        connection.setAutoCommit(false); // Start transaction
        try {
            String deleteQuery = "DELETE FROM Upis WHERE IDPolaznik = ? AND IDProgramObrazovanja = ?";
            try (PreparedStatement deletePs = connection.prepareStatement(deleteQuery)) {
                deletePs.setInt(1, idPolaznika);
                deletePs.setInt(2, idAktualnogPrograma);
                deletePs.executeUpdate();
            }

            String insertQuery = "INSERT INTO Upis (IDPolaznik, IDProgramObrazovanja) VALUES (?, ?)";
            try (PreparedStatement insertPs = connection.prepareStatement(insertQuery)) {
                insertPs.setInt(1, idPolaznika);
                insertPs.setInt(2, idNovogPrograma);
                insertPs.executeUpdate();
            }

            connection.commit();
            System.out.println("Polaznik je prebačen!");
        } catch (SQLException e) {
            connection.rollback();
            System.err.println("Greška za vrijeme transfera, transakcija vraćena u prethodno stanje.");
            e.printStackTrace();
        } finally {
            connection.setAutoCommit(true);
        }
    }

    private static void ispisPolaznikaZaProgram(Connection connection, Scanner scanner) throws SQLException {

        System.out.print("Unesite ID programa obrazovanja: ");
        int programId = scanner.nextInt();

        String query = """
                SELECT p.Ime, p.Prezime, po.Naziv, po.CSVET
                FROM Upis u
                JOIN Polaznik p ON u.IDPolaznik = p.PolaznikID
                JOIN ProgramObrazovanja po ON u.IDProgramObrazovanja = po.ProgramObrazovanjaId
                WHERE po.ProgramObrazovanjaId = ?""";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, programId);
            try (ResultSet rs = ps.executeQuery()) {
                System.out.println("Polaznici tog programa:");
                while (rs.next()) {
                    String ime = rs.getString("Ime");
                    String prezime = rs.getString("Prezime");
                    String nazivPrograma = rs.getString("Naziv");
                    int csvetBodovi = rs.getInt("CSVET");
                    System.out.printf("Naziv: %s %s, Program: %s, CSVET: %d%n", ime, prezime, nazivPrograma, csvetBodovi);
                }
            }
        }
    }

    private static DataSource createDataSource() {

        SQLServerDataSource dataSource = new SQLServerDataSource();
        dataSource.setServerName("localhost");
        dataSource.setDatabaseName("JavaAdv");
        dataSource.setUser("sa");
        dataSource.setPassword("SQL");
        dataSource.setEncrypt("false");
        return dataSource;
    }
}
