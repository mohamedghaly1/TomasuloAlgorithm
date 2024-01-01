package GUI;
import javax.swing.*;


import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.FileWriter;

import TomasuloAlgo.Processor;





public class TomasuloAlgorithmGUI extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField loadBufferSizeField, storeBufferSizeField, addSubBufferSizeField, mulDivBufferSizeField;
    private JTextField addDLatencyField, subDLatencyField, mulDLatencyField, dAddLatencyField,divDLatencyField;
    private JTextField subILatencyField, lDLatencyField, sDLatencyField;
    private JButton submitButton;
    private JPanel mainPanel;
    private JTextField floatingRegisterAddressField, floatingRegisterValueField;
    private JTextField integerRegisterAddressField, integerRegisterValueField;
    private JTextField cacheAddressField, cacheValueField;
    private JButton addButton, continueButton;
    private float[] frtemp= new float[32];
    private int[] irtemp = new int[32];
    private float[] cache = new float[2048];

    public TomasuloAlgorithmGUI() {
        setTitle("Tomasulo Algorithm GUI");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel to hold different sections
        mainPanel = new JPanel(new CardLayout());

        // First panel for initial inputs
        JPanel initialInputPanel = new JPanel();
        initialInputPanel.setLayout(new GridLayout(5, 2, 10, 10));

        initialInputPanel.add(createLabelWithTextField("Load Buffer Size:", loadBufferSizeField = new JTextField()));
        initialInputPanel.add(createLabelWithTextField("Store Buffer Size:", storeBufferSizeField = new JTextField()));
        initialInputPanel.add(createLabelWithTextField("Add/Sub Buffer Size:", addSubBufferSizeField = new JTextField()));
        initialInputPanel.add(createLabelWithTextField("Mul/Div Buffer Size:", mulDivBufferSizeField = new JTextField()));
       
        initialInputPanel.add(createLabelWithTextField("ADD.D Latency:", addDLatencyField = new JTextField()));
        initialInputPanel.add(createLabelWithTextField("SUB.D Latency:", subDLatencyField = new JTextField()));
        initialInputPanel.add(createLabelWithTextField("MUL.D Latency:", mulDLatencyField = new JTextField()));
        initialInputPanel.add(createLabelWithTextField("DIV.D Latency:", divDLatencyField = new JTextField()));
        initialInputPanel.add(createLabelWithTextField("DADD Latency:", dAddLatencyField = new JTextField()));
        initialInputPanel.add(createLabelWithTextField("SUBI Latency:", subILatencyField = new JTextField()));
        initialInputPanel.add(createLabelWithTextField("L.D Latency:", lDLatencyField = new JTextField()));
        initialInputPanel.add(createLabelWithTextField("S.D Latency:", sDLatencyField = new JTextField()));

        submitButton = new JButton("Submit");
        submitButton.setPreferredSize(new Dimension(80, 30));
        submitButton.addActionListener(new SubmitButtonListener());
        initialInputPanel.add(submitButton);

        // Adding new panel for additional inputs
        JPanel additionalInputPanel = new JPanel();
        additionalInputPanel.setLayout(new GridLayout(7, 2, 10, 10));

        additionalInputPanel.add(createLabelWithTextField("Floating Register Address:", floatingRegisterAddressField = new JTextField()));
        additionalInputPanel.add(createLabelWithTextField("Floating Register Value:", floatingRegisterValueField = new JTextField()));
        additionalInputPanel.add(createLabelWithTextField("Integer Register Address:", integerRegisterAddressField = new JTextField()));
        additionalInputPanel.add(createLabelWithTextField("Integer Register Value:", integerRegisterValueField = new JTextField()));
        additionalInputPanel.add(createLabelWithTextField("Cache Address:", cacheAddressField = new JTextField()));
        additionalInputPanel.add(createLabelWithTextField("Cache Value:", cacheValueField = new JTextField()));

        addButton = new JButton("Add");
        continueButton = new JButton("Continue");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(addButton);
        buttonPanel.add(continueButton);

        addButton.addActionListener(new AddButtonListener());
        continueButton.addActionListener(new ContinueButtonListener());

        additionalInputPanel.add(buttonPanel);
        
        mainPanel.add(initialInputPanel, "InitialInput");
        mainPanel.add(additionalInputPanel, "AdditionalInput"); // Add the new panel to the main panel

        add(mainPanel);
        setVisible(true);
    }

    private JPanel createLabelWithTextField(String labelText, JTextField textField) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(label);
        textField.setPreferredSize(new Dimension(100, 25));
        panel.add(textField);
        return panel;
    }
    
    private class SubmitButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (allFieldsFilled()) {
            	CardLayout cardLayout = (CardLayout) mainPanel.getLayout();
                cardLayout.show(mainPanel, "AdditionalInput"); // Show the additional input panel
				} else {
                JOptionPane.showMessageDialog(TomasuloAlgorithmGUI.this, "Please fill all the textboxes.");
            }
        }
    }
    
    private class AddButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String fraddress =  floatingRegisterAddressField.getText();
            String frvalue = floatingRegisterValueField.getText();
            String iraddress=integerRegisterAddressField.getText();
            String irvalue=integerRegisterValueField.getText();
            String cacheaddress=cacheAddressField.getText();
            String cachevalue=cacheValueField.getText();
            if(!fraddress.isEmpty() && !frvalue.isEmpty()) frtemp[Integer.parseInt(fraddress)]=Float.parseFloat(frvalue);
            if(!iraddress.isEmpty() && !irvalue.isEmpty()) irtemp[Integer.parseInt(iraddress)]=Integer.parseInt(irvalue);
            if(!cacheaddress.isEmpty() && !cachevalue.isEmpty()) cache[Integer.parseInt(cacheaddress)]=Float.parseFloat(cachevalue);
            
            floatingRegisterAddressField.setText("");
            floatingRegisterValueField.setText("");
            integerRegisterAddressField.setText("");
            integerRegisterValueField.setText("");
            cacheAddressField.setText("");
            cacheValueField.setText("");
            
        }
    }

    private class ContinueButtonListener implements ActionListener {
    	private int cycleCounter=0;
        public void actionPerformed(ActionEvent e) {
        	JPanel newPanel = new JPanel(new GridLayout(3, 2, 10, 10));

            // Clear the mainPanel before adding a new panel
            mainPanel.removeAll();
            mainPanel.revalidate();
            mainPanel.repaint();
            
            // Create and add the "Enter your program" label and text field
            JLabel enterProgramLabel = new JLabel("Enter your program:");
            JTextArea enterProgramField = new JTextArea();
            enterProgramField.setCaretPosition(0);
            enterProgramField.setEditable(true); // Allow editing
            newPanel.add(enterProgramLabel);
            newPanel.add(enterProgramField);

            // Create and add the "Output" label and non-editable text field
            JLabel outputLabel = new JLabel("Output:");
            
            JTextArea outputField = new JTextArea();
            outputField.setEditable(false); // Make it non-editable
            newPanel.add(outputLabel);
            newPanel.add(outputField);
            // Create a JScrollPane and add the outputField inside it
            JScrollPane scrollPane = new JScrollPane(outputField);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

            newPanel.add(outputLabel);
            newPanel.add(scrollPane); // Add the scroll pane instead of outputField directly

            JButton newButton = new JButton("cycle " + cycleCounter); // Initial label "cycle 0"
            newPanel.add(newButton);

            newButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                	  cycleCounter++; // Increment the cycle counter
                      newButton.setText("cycle " + cycleCounter); // Update button label
                      System.out.println("Button on the new panel clicked. Cycle: " + cycleCounter);
                      
                	try (BufferedWriter writer = new BufferedWriter(new FileWriter("program.txt"))) {
    		            writer.write(enterProgramField.getText());
    		            System.out.println("Successfully wrote to the file.");
    		        } catch (Exception e1) {
    		        	JOptionPane.showMessageDialog(null, "Please ensure accurate assembly code input. Assistance is available via the help button if needed.", "Error", JOptionPane.PLAIN_MESSAGE);
    		        }
                	
                	try {
    					Processor o = new Processor(Integer.parseInt(addDLatencyField.getText()),Integer.parseInt(subDLatencyField.getText()),Integer.parseInt(mulDLatencyField.getText()),Integer.parseInt(divDLatencyField.getText()),
                    		Integer.parseInt(dAddLatencyField.getText()),Integer.parseInt(subILatencyField.getText()),Integer.parseInt(lDLatencyField.getText()),
                    				Integer.parseInt(sDLatencyField.getText()),Integer.parseInt(addSubBufferSizeField.getText()),Integer.parseInt(mulDivBufferSizeField.getText()),
                    						Integer.parseInt(loadBufferSizeField.getText()),Integer.parseInt(storeBufferSizeField.getText()));
    					
    					//o.setFloatingRegister(frtemp);
    					//o.setIntegerRegister(irtemp);
    					//o.setcache(cache);
    				
    					//Add your preload registers and cache here:-
    					//o.loadMemory(2, 2);
    					o.loadRegisterR(1, 2);
    					//o.loadRegisterF(2, 2);
                		outputField.setText(o.ExecuteProgram(cycleCounter));
    				} catch (Exception e1) {
    					JOptionPane.showMessageDialog(null, "Please ensure accurate assembly code input. Assistance is available via the help button if needed.", "Error", JOptionPane.PLAIN_MESSAGE);
    					e1.printStackTrace();
    				}
                	
                  
                }
            });

            mainPanel.add(newPanel, "NewPanelName");

            CardLayout cardLayout = (CardLayout) mainPanel.getLayout();
            cardLayout.show(mainPanel, "NewPanelName");
        }
        }
   

   

    

    private boolean allFieldsFilled() {
        return !loadBufferSizeField.getText().isEmpty() &&
                !storeBufferSizeField.getText().isEmpty() &&
                !addSubBufferSizeField.getText().isEmpty() &&
                !mulDivBufferSizeField.getText().isEmpty()&&
                !subDLatencyField.getText().isEmpty() &&
                !mulDLatencyField.getText().isEmpty()&&
                !dAddLatencyField.getText().isEmpty()&&
                !subILatencyField.getText().isEmpty()&&
                !lDLatencyField.getText().isEmpty()&&
                !sDLatencyField.getText().isEmpty()&&
                !addDLatencyField.getText().isEmpty();
                
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new TomasuloAlgorithmGUI();
        });
    }
}
