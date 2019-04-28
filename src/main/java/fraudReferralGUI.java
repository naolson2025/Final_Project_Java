import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class fraudReferralGUI extends JFrame{
    private JPanel mainPanel;
    private JTable fraudReferralJTable;
    private JTextField referralSourceTextField;
    private JLabel referralSouceLabel;
    private JTextField suspectEmployeeTextField;
    private JLabel suspectEmployeeLabel;
    private JTextField allegationDescriptionTextField;
    private JLabel allegationDescriptionLabel;
    private JButton saveCaseButton;
    private JButton deleteCaseButton;
    private JButton closeCaseButton;
    private JLabel allegationSeverityLabel;
    private JComboBox allegationSeverityComboBox;
    private JLabel susEmployeeMgrContLabel;
    private JCheckBox mgrContactedCheckBox;
    private JLabel caseDecisionLabel;
    private JComboBox caseDecisionComboBox;
    private JLabel correctiveActionLabel;
    private JComboBox correctiveActionComboBox;
    private JLabel highest;
    private JLabel lowest;
    private JLabel researchNotesLabel;
    private JTextField researchNotesTextField;
    private JButton newCaseButton;
    private JLabel caseIDLabel;
    private JLabel caseID;
    private JLabel dateCreatedLabel;
    private JLabel dateCreated;
    private JCheckBox caseClosedCheckBox;

    // Use of the DB config methods in the fraudReferralGui class
    private DBConfig dbConfig = new DBConfig();

    // List of severity levels
    private static String[] severityLevels = {"1", "2", "3", "4", "5"};

    // list of case decisions for combobox
    private static String[] caseDecisions = {"", "Agreed", "Disagreed"};

    // List of corrective actions
    private static String[] correctiveActions = {"", "Not Applicable", "Performance Coaching", "Informal Warning", "Formal Warning", "Termination"};

    public fraudReferralGUI(Referral referral){

        setContentPane(mainPanel);
        pack();
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        // Rows non-edit found on stack overflow
        fraudReferralJTable.setDefaultEditor(Object.class, null);
        // Single selection in JTable
        fraudReferralJTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // set severityLevel combobox values, found on stack overflow
        allegationSeverityComboBox.setModel(new DefaultComboBoxModel<>(severityLevels));
        // load case decision combobox
        caseDecisionComboBox.setModel(new DefaultComboBoxModel<>(caseDecisions));
        // load corrective action combobox
        correctiveActionComboBox.setModel(new DefaultComboBoxModel<>(correctiveActions));

        // configure the table model
        configureTable();

        try{
            // Default all fields to the first row, catch error if no first row
            fraudReferralJTable.setRowSelectionInterval(0, 0);
            setFields();
        }catch (NullPointerException e){
            System.out.println("No row 1 to set default values");
        }

        // Add listeners
        listeners();
    }

    private void configureTable(){
        // set columns and data in the table model, if there is no data in the table print note
        try{
            // Set columns and cases to variables
            Vector columnNames = getColumnNames();
            Vector<Vector> allCases = dbConfig.getAllCases();

            // Set table model
            DefaultTableModel tableModel = new DefaultTableModel(allCases, columnNames);
            fraudReferralJTable.setModel(tableModel);

        }catch (NullPointerException e){
            System.out.println("There is no data in the fraud referrals table");
        }
    }

    Vector getColumnNames(){
        // Set the column names
        Vector colNames = new Vector();
        colNames.add("Case ID");
        colNames.add("Date Created");
        colNames.add("Referral Source");
        colNames.add("Suspect Employee");
        colNames.add("Allegation Description");
        colNames.add("Research Notes");
        colNames.add("Allegation Severity");
        colNames.add("Manager Contacted");
        colNames.add("Case Decision");
        colNames.add("Corrective Action");
        colNames.add("Case Closed");

        return colNames;
    }

    protected void showAlert(String message){
        JOptionPane.showMessageDialog(this, message);
    }

    private void setFields(){
        // Get selected row
        int selectedRow = fraudReferralJTable.getSelectedRow();
        // selected row data will populate the fields
        try{
            caseID.setText(fraudReferralJTable.getModel().getValueAt(selectedRow, 0).toString());
            dateCreated.setText(fraudReferralJTable.getModel().getValueAt(selectedRow, 1).toString());
            referralSourceTextField.setText(fraudReferralJTable.getModel().getValueAt(selectedRow, 2).toString());
            suspectEmployeeTextField.setText(fraudReferralJTable.getModel().getValueAt(selectedRow, 3).toString());
            allegationDescriptionTextField.setText(fraudReferralJTable.getModel().getValueAt(selectedRow, 4).toString());
            researchNotesTextField.setText(fraudReferralJTable.getModel().getValueAt(selectedRow, 5).toString());
            allegationSeverityComboBox.setSelectedItem(fraudReferralJTable.getModel().getValueAt(selectedRow, 6));
            mgrContactedCheckBox.setSelected(Boolean.valueOf(fraudReferralJTable.getModel().getValueAt(selectedRow, 7).toString()));
            caseDecisionComboBox.setSelectedItem(fraudReferralJTable.getModel().getValueAt(selectedRow, 8));
            correctiveActionComboBox.setSelectedItem(fraudReferralJTable.getModel().getValueAt(selectedRow, 9));
            caseClosedCheckBox.setSelected(Boolean.valueOf(fraudReferralJTable.getModel().getValueAt(selectedRow, 10).toString()));
        }catch (IndexOutOfBoundsException e){
            System.out.println("No row was selected");
        }

    }

    private void listeners(){
        fraudReferralJTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                setFields();
            }
        });

        // new case button is clicked
        newCaseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // clear all as a blank case will be created
                clearAllFields();
                // create the blank case
                String result = dbConfig.addRecord(referralSourceTextField.getText(),
                        suspectEmployeeTextField.getText(),
                        allegationDescriptionTextField.getText(),
                        researchNotesTextField.getText(),
                        Integer.parseInt(String.valueOf(allegationSeverityComboBox.getSelectedItem())),
                        mgrContactedCheckBox.isSelected(),
                        String.valueOf(caseDecisionComboBox.getSelectedItem()),
                        String.valueOf(correctiveActionComboBox.getSelectedItem()),
                        caseClosedCheckBox.isSelected());

                // Will add the blank case to the JTable
                configureTable();
                // Set the JTable to have the new case selected, found on stack overflow
                int lastRow = fraudReferralJTable.getRowCount() - 1;
                fraudReferralJTable.setRowSelectionInterval(lastRow, lastRow);
                // Will update the case ID and date in the GUI
                setFields();
                // print case created if successful, show alert if not
                if (result.equals("success")){
                    System.out.println("Case created");
                }else {
                    showAlert("Failed to create case");
                }
            }
        });

        // updates an existing case
        saveCaseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String result = dbConfig.updateRecord(referralSourceTextField.getText(),
                        suspectEmployeeTextField.getText(),
                        allegationDescriptionTextField.getText(),
                        researchNotesTextField.getText(),
                        Integer.parseInt(String.valueOf(allegationSeverityComboBox.getSelectedItem())),
                        mgrContactedCheckBox.isSelected(),
                        String.valueOf(caseDecisionComboBox.getSelectedItem()),
                        String.valueOf(correctiveActionComboBox.getSelectedItem()),
                        caseClosedCheckBox.isSelected(),
                        Integer.parseInt(fraudReferralJTable.getModel().getValueAt(fraudReferralJTable.getSelectedRow(), 0).toString()));

                // refresh table
                configureTable();

                if (result.equals("success")){
                    System.out.println("Case updated");
                }else {
                    showAlert("Failed to update case");
                }
            }
        });

        // delete case button is clicked
        deleteCaseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int choice = JOptionPane.showConfirmDialog( null,"Confirm you want to delete:", null, JOptionPane.YES_NO_OPTION);

                if (choice == 0) {
                    dbConfig.deleteRecord(Integer.parseInt(fraudReferralJTable.getModel().getValueAt(fraudReferralJTable.getSelectedRow(), 0).toString()));
                    configureTable();
                    clearAllFields();
                } else {
                    System.out.println("case deletion cancelled");
                }
            }
        });
    }

    private void clearAllFields(){
        referralSourceTextField.setText("");
        suspectEmployeeTextField.setText("");
        allegationDescriptionTextField.setText("");
        researchNotesTextField.setText("");
        allegationSeverityComboBox.setSelectedItem("1");
        mgrContactedCheckBox.setSelected(false);
        caseDecisionComboBox.setSelectedItem("");
        correctiveActionComboBox.setSelectedItem("");
        caseClosedCheckBox.setSelected(false);
    }

}
