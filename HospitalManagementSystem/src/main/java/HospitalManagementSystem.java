import java.sql.*;
import java.util.Scanner;

public class HospitalManagementSystem {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/hospital_db";
    private static final String USER = "root"; // change to your DB username
    private static final String PASS = "";     // change to your DB password

    private static Connection conn;
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Connected to the database.");

            int choice;
            do {
                System.out.println("\n--- Hospital Management System ---");
                System.out.println("1. Add Patient");
                System.out.println("2. View Patients");
                System.out.println("3. Update Patient");
                System.out.println("4. Delete Patient");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");
                choice = scanner.nextInt();
                scanner.nextLine(); // consume newline

                switch (choice) {
                    case 1 -> addPatient();
                    case 2 -> viewPatients();
                    case 3 -> updatePatient();
                    case 4 -> deletePatient();
                    case 5 -> System.out.println("Exiting system...");
                    default -> System.out.println("Invalid choice.");
                }
            } while (choice != 5);

            conn.close();
        } catch (SQLException e) {
        }
    }

    private static void addPatient() throws SQLException {
        System.out.print("Enter Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Age: ");
        int age = scanner.nextInt();
        scanner.nextLine(); // consume newline
        System.out.print("Enter Gender: ");
        String gender = scanner.nextLine();
        System.out.print("Enter Diagnosis: ");
        String diagnosis = scanner.nextLine();

        String sql = "INSERT INTO patients (name, age, gender, diagnosis) VALUES (?, ?, ?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, name);
        pstmt.setInt(2, age);
        pstmt.setString(3, gender);
        pstmt.setString(4, diagnosis);
        pstmt.executeUpdate();

        System.out.println("Patient added successfully!");
    }

    private static void viewPatients() throws SQLException {
        String sql = "SELECT * FROM patients";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        System.out.println("\n--- Patient List ---");
        while (rs.next()) {
            System.out.println("ID: " + rs.getInt("id") +
                    ", Name: " + rs.getString("name") +
                    ", Age: " + rs.getInt("age") +
                    ", Gender: " + rs.getString("gender") +
                    ", Diagnosis: " + rs.getString("diagnosis"));
        }
    }

    private static void updatePatient() throws SQLException {
        System.out.print("Enter Patient ID to Update: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // consume newline

        System.out.print("Enter New Diagnosis: ");
        String diagnosis = scanner.nextLine();

        String sql = "UPDATE patients SET diagnosis = ? WHERE id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, diagnosis);
        pstmt.setInt(2, id);
        int rows = pstmt.executeUpdate();

        if (rows > 0) {
            System.out.println("Patient updated successfully.");
        } else {
            System.out.println("Patient not found.");
        }
    }

    private static void deletePatient() throws SQLException {
        System.out.print("Enter Patient ID to Delete: ");
        int id = scanner.nextInt();

        String sql = "DELETE FROM patients WHERE id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, id);
        int rows = pstmt.executeUpdate();

        if (rows > 0) {
            System.out.println("Patient deleted successfully.");
        } else {
            System.out.println("Patient not found.");
        }
    }
}
