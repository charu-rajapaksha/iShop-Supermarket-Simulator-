package iShop;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


public class IShop {

	private final int maxCustomersSliderMax = 30;
	private final int genSliderMax = 50;
	
	private String testCaseBtnText;
	
	private JFrame frame;
	private JPanel controllerPanel;
	
	private JPanel testCasePanel;
	private OutputPanel outputPanel = OutputPanel.outputPanel();
	private JScrollBar scrollBar;
	private JButton test1Button;
	private JButton test2Button;
	private JButton test3Button;
	private JButton test4Button;
	
	SimulatorController simCont;
	JButton startStopButton;
	boolean started = false;
	private JCheckBox cbAIn;
	private JCheckBox cbBIn;
	private JCheckBox cbCIn;
	private JCheckBox cbDIn;
	private JButton customTestButton;
	private JLabel startingShoppingPathLbl;
	private Component verticalStrut1;
	private Component verticalStrut2;
	private Component verticalStrut3;
	private JLabel endingShoppingPathLbl;
	private JCheckBox cbAOut;
	private JCheckBox cbBOut;
	private JCheckBox cbCOut;
	private JCheckBox cbDOut;
	private Component verticalStrut4;
	private JLabel genLbl;
	private JSlider genSlider;
	private Component verticalStrut5;
	private JLabel maxCustomersLbl;
	private JSlider maxCustomersSlider;

	private JButton manageInventory;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					IShop iShop = new IShop();
					iShop.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	}

	/**
	 * Create the application.
	 */
	public IShop() {
		initialize();
		simCont.setGenSliderMax(genSliderMax+1);
	}
	
	private void initializeControllerPanel() {
		outputPanel.getDocument().addDocumentListener(new TextAreaListener());
		simCont.setOutputPanel(outputPanel);
		JScrollPane scrollPane = new JScrollPane(outputPanel);
		scrollBar = scrollPane.getVerticalScrollBar();
		
		controllerPanel = new JPanel();
//		controllerPanel.setBackground(Color.gray);
                controllerPanel.setBackground(new Color(116, 35, 3));
		
		startStopButton = new JButton("Open Shop");
		startStopButton.setHorizontalAlignment(SwingConstants.LEFT);
		startStopButton.setEnabled(false);
		
		startStopButton.addActionListener(new StartStopListener());
		
		controllerPanel.add(startStopButton);
		controllerPanel.add(scrollPane);
		
		controllerPanel.setSize(300, 210);
		
	}
	
	private void initializeTestCasePanel() {
		testCasePanel = new JPanel();
		testCasePanel.setLayout(new BoxLayout(testCasePanel, BoxLayout.PAGE_AXIS));

		test1Button = new JButton("Test Case 1");
		test2Button = new JButton("Test Case 2");
		test3Button = new JButton("Test Case 3");
		test4Button = new JButton("Test Case 4");
		customTestButton = new JButton("Custom Test");

		manageInventory = new JButton("Manage Inventory");
		
		cbAIn = new JCheckBox("A");
		cbBIn = new JCheckBox("B");
		cbCIn = new JCheckBox("C");
		cbDIn = new JCheckBox("D");
		cbAOut = new JCheckBox("A");
		cbBOut = new JCheckBox("B");
		cbCOut = new JCheckBox("C");
		cbDOut = new JCheckBox("D");
		
		cbAIn.setSelected(true);
		cbBIn.setSelected(true);
		cbCIn.setSelected(true);
		cbDIn.setSelected(true);
		cbAOut.setSelected(true);
		cbBOut.setSelected(true);
		cbCOut.setSelected(true);
		cbDOut.setSelected(true);
		
		startingShoppingPathLbl = new JLabel("Start Shopping Paths");
		startingShoppingPathLbl.setFont(new Font("Tahoma", Font.BOLD, 14));
		startingShoppingPathLbl.setForeground(Color.WHITE);
		
		endingShoppingPathLbl = new JLabel("Ending Shopping Paths");
		endingShoppingPathLbl.setFont(new Font("Tahoma", Font.BOLD, 14));
		endingShoppingPathLbl.setForeground(Color.WHITE);
		
		genLbl = new JLabel("Generation Speed");
		genLbl.setFont(new Font("Tahoma", Font.BOLD, 14));
		genLbl.setForeground(Color.WHITE);

		maxCustomersLbl = new JLabel("Max Customers");
		maxCustomersLbl.setFont(new Font("Tahoma", Font.BOLD, 14));
		maxCustomersLbl.setForeground(Color.WHITE);
		
		
		verticalStrut1 = Box.createVerticalStrut(20);
		verticalStrut2 = Box.createVerticalStrut(10);
		verticalStrut3 = Box.createVerticalStrut(10);
		verticalStrut4 = Box.createVerticalStrut(10);
		verticalStrut5 = Box.createVerticalStrut(10);
		
		genSlider = new JSlider();
		genSlider.setMajorTickSpacing(20);
		genSlider.setPaintTicks(true);
		genSlider.setPaintLabels(true);
		genSlider.setMinimum(1);
		genSlider.setMaximum(genSliderMax-1);
		genSlider.setValue(genSliderMax/2);
		genSlider.setLabelTable(genSlider.createStandardLabels(10, 10));
		
		maxCustomersSlider = new JSlider();
		maxCustomersSlider.setMajorTickSpacing(5);
		maxCustomersSlider.setPaintTicks(true);
		maxCustomersSlider.setPaintLabels(true);
		maxCustomersSlider.setMinimum(1);
		maxCustomersSlider.setMaximum(maxCustomersSliderMax);
		maxCustomersSlider.setValue(maxCustomersSliderMax/2);
		maxCustomersSlider.setLabelTable(maxCustomersSlider.createStandardLabels(10, 10));
		
		
		test1Button.addActionListener(new testCaseListener());
		test2Button.addActionListener(new testCaseListener());
		test3Button.addActionListener(new testCaseListener());
		test4Button.addActionListener(new testCaseListener());
		customTestButton.addActionListener(new testCaseListener());

		manageInventory.addActionListener((new ManageInventory()));
		
//		testCasePanel.setBackground(new Color(209, 107, 67));
                testCasePanel.setBackground(new Color(116, 35, 3));
		
		testCasePanel.add(test1Button);
		testCasePanel.add(test2Button);
		testCasePanel.add(test3Button);
		testCasePanel.add(test4Button);
		testCasePanel.add(verticalStrut1);
		testCasePanel.add(customTestButton);
		testCasePanel.add(manageInventory);
		testCasePanel.add(verticalStrut2);
		testCasePanel.add(startingShoppingPathLbl);
		testCasePanel.add(cbAIn);
		testCasePanel.add(cbBIn);
		testCasePanel.add(cbCIn);
		testCasePanel.add(cbDIn);
		testCasePanel.add(verticalStrut3);
		testCasePanel.add(endingShoppingPathLbl);
		testCasePanel.add(cbAOut);
		testCasePanel.add(cbBOut);
		testCasePanel.add(cbCOut);
		testCasePanel.add(cbDOut);
		testCasePanel.add(verticalStrut4);
		testCasePanel.add(genLbl);
		testCasePanel.add(genSlider);
		testCasePanel.add(verticalStrut5);
		testCasePanel.add(maxCustomersLbl);
		testCasePanel.add(maxCustomersSlider);
		testCasePanel.setSize(280, 600);
		
		setIfCustomControlsEnabled(false);
	}

	
	private void initialize() {
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.getContentPane().setLayout(new BorderLayout());
	    frame.setTitle("ISHOP Simulator - Charuni");

		simCont = SimulatorController.simCont();
		
		initializeControllerPanel();
		initializeTestCasePanel();
		
		frame.getContentPane().add( simCont.getView(), BorderLayout.WEST);
	    frame.getContentPane().add( controllerPanel, BorderLayout.SOUTH );
		frame.getContentPane().add( testCasePanel, BorderLayout.EAST);
		
		System.out.println(simCont.getView().getWidth());
		
		frame.setSize((simCont.getView().getWidth() + testCasePanel.getWidth()), simCont.getView().getHeight() + controllerPanel.getHeight());

	    
	}
	
	public void appendOutputPanel(String text) {
		outputPanel.append("\n" + text);
		scrollBar.setValue(scrollBar.getMaximum());		
	}
	
	private void setIfCustomControlsEnabled(boolean enabled) {
		cbAIn.setEnabled(enabled);
		cbBIn.setEnabled(enabled);
		cbCIn.setEnabled(enabled);
		cbDIn.setEnabled(enabled);
		cbAOut.setEnabled(enabled);
		cbBOut.setEnabled(enabled);
		cbCOut.setEnabled(enabled);
		cbDOut.setEnabled(enabled);
		
		genSlider.setEnabled(enabled);
		maxCustomersSlider.setEnabled(enabled);
	}
	
		
	public class StartStopListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			
			if (started){
				appendOutputPanel("Simulation stopped\n");
				simCont.stopSim();
				startStopButton.setText("Open Shop");
			} else {
				
				if (testCaseBtnText == "Custom Test") {
					if (getStartingShoppingPath().isEmpty()) {
						appendOutputPanel("Please select at least 1 starting Shopping Path!");
						return;
					}
					
					if (getEndingShoppingPath().isEmpty()) {
						appendOutputPanel("Please select at least 1 ending Shopping Path!");
						return;
					}

					simCont.setStartingShoppingPath(getStartingShoppingPath());
					simCont.setEndingShoppingPath(getEndingShoppingPath());
					simCont.setGenSpeed(genSlider.getValue());
					simCont.setMaxCustomers(maxCustomersSlider.getValue());
					
					System.out.println();
				}
				appendOutputPanel("Simulation started:");
				simCont.startSim();
				startStopButton.setText("Close Shop");
			}
			
			started=!started;
		
		}
	
	}

	public class ManageInventory implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
                    InventoryView inventoryView = new InventoryView();
                    inventoryView.setVisible(true);
		}
	}
	
	public class testCaseListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			
			testCaseBtnText = ((JButton)e.getSource()).getText();
			
			switch (testCaseBtnText) {
			case "Test Case 1":
				simCont.selectTestCase(1);
				break;
			case "Test Case 2":
				simCont.selectTestCase(2);
				break;
			case "Test Case 3":
				simCont.selectTestCase(3);
				break;
			case "Test Case 4":
				simCont.selectTestCase(4);
				break;
			case "Custom Test":								
				simCont.selectTestCase(5);
				break;
			}
			
			if (testCaseBtnText == "Custom Test") {
				setIfCustomControlsEnabled(true);
			} else {
				setIfCustomControlsEnabled(false);
			}

			appendOutputPanel(testCaseBtnText + " Selected:");
			
			if (started) {
				simCont.stopSim();
			started = false;
			}
			
			startStopButton.setText("Open Shop");
			
			startStopButton.setEnabled(true);
		}
		
	}
	
	public class TextAreaListener implements DocumentListener{

		@Override
		public void changedUpdate(DocumentEvent e) {
			
		}

		@Override
		public void insertUpdate(DocumentEvent arg0) {
			outputPanel.setCaretPosition(outputPanel.getDocument().getLength());
		}

		@Override
		public void removeUpdate(DocumentEvent arg0) {
			
		}


		
	}
	
	public OutputPanel getOutputPanel() {return outputPanel;}
	
	public ArrayList<Customer.ShoppingPathLbl> getStartingShoppingPath() {
		ArrayList<Customer.ShoppingPathLbl> shoppingPath = new ArrayList<Customer.ShoppingPathLbl>();
		
		if (cbAIn.isSelected()) {
			shoppingPath.add(Customer.ShoppingPathLbl.a);
		}
		if (cbBIn.isSelected()) {
			shoppingPath.add(Customer.ShoppingPathLbl.b);
		}
		if (cbCIn.isSelected()) {
			shoppingPath.add(Customer.ShoppingPathLbl.c);
		}
		if (cbDIn.isSelected()) {
			shoppingPath.add(Customer.ShoppingPathLbl.d);
		}
		
		return shoppingPath;
	}
	
	public ArrayList<Customer.ShoppingPathLbl> getEndingShoppingPath() {
		ArrayList<Customer.ShoppingPathLbl> shoppingPath = new ArrayList<Customer.ShoppingPathLbl>();
		
		if (cbAOut.isSelected()) {
			shoppingPath.add(Customer.ShoppingPathLbl.a);
		}
		if (cbBOut.isSelected()) {
			shoppingPath.add(Customer.ShoppingPathLbl.c);
		}
		if (cbCOut.isSelected()) {
			shoppingPath.add(Customer.ShoppingPathLbl.c);
		}
		if (cbDOut.isSelected()) {
			shoppingPath.add(Customer.ShoppingPathLbl.d);
		}
		
		return shoppingPath;
	}

}