import java.sql.*;
import java.util.Date;
import java.util.Vector;

public class DBConfig {

    // Database connection path
    static String db_url = "jdbc:sqlite:fraudData.db";
    // SQL to pull all cases from the fraud_referrals table from DB
    private static final String GET_ALL_CASES = "SELECT * FROM fraud_referrals";

    Vector<Vector> getAllCases(){
        try (Connection connection = DriverManager.getConnection(db_url);
             Statement statement = connection.createStatement()){

            // Get all table data and put into ResultSet
            ResultSet rs = statement.executeQuery(GET_ALL_CASES);

            // Vector list to return
            Vector<Vector> caseVectors = new Vector<>();

            // Variables for each case
            int case_id;
            Date date_created;
            String referral_source;
            String suspect_employee;
            String allegation_description;
            String research_notes;
            int allegation_severity;
            boolean manager_contacted;
            String case_decision;
            String corrective_action;

            while (rs.next()){
                // put each column into a variable and add it to the caseVectors list
                case_id = rs.getInt("case_id");
                date_created = rs.getDate("date_created");
                referral_source = rs.getString("referral_source");
                suspect_employee = rs.getString("suspect_employee");
                allegation_description = rs.getString("allegation_description");
                research_notes = rs.getString("research_notes");
                allegation_severity = rs.getInt("allegation_severity");
                manager_contacted = rs.getBoolean("manager_contacted");
                case_decision = rs.getString("case_decision");
                corrective_action = rs.getString("corrective_action");

                // Create vector and add all variables
                Vector v = new Vector();
                v.add(case_id);
                v.add(date_created);
                v.add(referral_source);
                v.add(suspect_employee);
                v.add(allegation_description);
                v.add(research_notes);
                v.add(allegation_severity);
                v.add(manager_contacted);
                v.add(case_decision);
                v.add(corrective_action);

                // Add the vector to the caseVectors list
                caseVectors.add(v);
            }

            return caseVectors;

        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    String addRecord(String referral_source, String suspect_employee, String allegation_desc, String research_notes,
                     int allegation_severity, boolean manager_contacted, String case_decision, String corrective_action){
        // Get current date and time as SQL value
        long currentDateLong = System.currentTimeMillis();
        java.sql.Date currentDateSQL = new java.sql.Date(currentDateLong);

        String addRecordSQL = "INSERT INTO fraud_referrals (date_created," +
                                                            "referral_source," +
                                                            "suspect_employee," +
                                                            "allegation_description," +
                                                            "research_notes," +
                                                            "allegation_severity," +
                                                            "manager_contacted," +
                                                            "case_decision," +
                                                            "corrective_action) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try(Connection connection = DriverManager.getConnection(db_url);
        PreparedStatement ps = connection.prepareStatement(addRecordSQL)){
            // construct SQL statement
            ps.setDate(1, currentDateSQL);
            ps.setString(2, referral_source);
            ps.setString(3, suspect_employee);
            ps.setString(4, allegation_desc);
            ps.setString(5, research_notes);
            ps.setInt(6, allegation_severity);
            ps.setBoolean(7, manager_contacted);
            ps.setString(8, case_decision);
            ps.setString(9, corrective_action);

            // execute ps statement
            ps.execute();

            return "success";
        }catch (SQLException e){
            return "Failed to create record";
        }
    }
}
