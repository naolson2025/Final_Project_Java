import javax.swing.*;

public class fraudReferralGUI extends JFrame{
    private JPanel mainPanel;
    private JTable fraudReferralJTable;
    private JTextField referralSourceTextField;
    private JLabel referralSouceLabel;
    private JTextField suspectEmployeeTextField;
    private JLabel suspectEmployeeLabel;
    private JTextField allegationDescriptionTextField;
    private JLabel allegationDescriptionLabel;
    private JButton addCaseButton;
    private JButton editCaseButton;
    private JButton deleteCaseButton;
    private JButton closeCaseButton;
    private JLabel allegationSeverityLabel;
    private JComboBox allegationSeverityComboBox;
    private JLabel susEmployeeMgrContLabel;
    private JCheckBox mgrContactedCheckBox;
    private JLabel caseDecisionLabel;
    private JComboBox caseDecisionComboBox;
    private JLabel correctiveActionComboBox;
    private JComboBox comboBox1;
    private JLabel highest;
    private JLabel lowest;
    private JLabel researchNotesLabel;
    private JTextField researchNotesTextField;
    private JButton newCaseButton;
    private JLabel caseIDLabel;
    private JLabel caseID;
    private JLabel dateCreatedLabel;
    private JLabel dateCreated;


    private DBConfig dbConfig = new DBConfig();

    public fraudReferralGUI(Referral referral){

        setContentPane(mainPanel);
        pack();
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        // Rows non-edit found on stack overflow
        fraudReferralJTable.setDefaultEditor(Object.class, null);
        // Single selection in JTable
        fraudReferralJTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    }
}
