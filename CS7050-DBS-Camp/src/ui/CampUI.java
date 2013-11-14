package ui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.GregorianCalendar;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JButton;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;

import controllers.ControllerBookingStatus;
import controllers.ControllerBookings;
import controllers.ControllerCancellations;
import controllers.ControllerFlightSearch;
import controllers.ControllerHelicopterConnection;

public class CampUI {

	private JFrame frame;
	private JTextField textFlightID;
	private JTextField textCustomerID;
	private JTextField textCustomerName;
	private JTextField textReturnFlightID;
	private JTextField textBookingReference;
	private JList flightList;
	private JButton bSearch;
	private JButton bBooking;
	private JButton bCheckStatus;
	private JButton bCancellation;
	private JButton bConnectHelicopter;
	private JTextArea notificationPanel;
	private JTextField textHelicopterIP;
	private JTextField textHelicopterPort;
	private JCheckBox chckbxReturn;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CampUI window = new CampUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public CampUI() {
		initialize();
	}
	
    public JTextArea getNotificationPanel() {
        return notificationPanel;
    }
    
    public JList getFlightList() {
    	return flightList;
    }
    
    public JButton getButtonSearch(){
    	return bSearch;
    }
    
    public JButton getButtonBooking(){
    	return bBooking;
    }
    
    public JButton getButtonCancellation(){
    	return bCancellation;
    }
    
    public JButton getButtonCheckStatus(){
    	return bCheckStatus;
    }
    
    public JTextField getTextBookingReference(){
    	return textBookingReference;
    }
    
    public JTextField getTextHeliIP(){
    	return textHelicopterIP;
    }
    
    public JTextField getTextHeliPort(){
    	return textHelicopterPort;
    }
    
    public JButton getButtonHeliConnection(){
    	return bConnectHelicopter;
    }
    
    public JTextField getTextFlightID(){
    	return textFlightID;
    }
    
    public JTextField getTextCustomerID(){
    	return textCustomerID;
    }
    
    public JTextField getTextCustomerName(){
    	return textCustomerName;
    }
    
    public JTextField getTextReturnFlightID(){
    	return textReturnFlightID;
    }
    
    public JCheckBox getCheckBReturn(){
    	return chckbxReturn;
    }    

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 584, 709);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setTitle("CS7050-DBS Camp client");
		
		JScrollPane spList = new JScrollPane();
		spList.setBounds(22, 115, 548, 102);
		frame.getContentPane().add(spList);
		
		flightList = new JList();
        flightList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        flightList.setLayoutOrientation(JList.VERTICAL);
		spList.setViewportView(flightList);
		
		bSearch = new JButton("Search flights");
		bSearch.setBounds(56, 64, 132, 25);
        bSearch.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				search4Flights();	
			}
		});
		frame.getContentPane().add(bSearch);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(12, 234, 558, 2);
		frame.getContentPane().add(separator);
		
		textFlightID = new JTextField();
		textFlightID.setBounds(136, 248, 114, 19);
		frame.getContentPane().add(textFlightID);
		textFlightID.setColumns(10);
		
		JLabel lblFlightId = new JLabel("Flight ID");
		lblFlightId.setBounds(22, 248, 70, 15);
		frame.getContentPane().add(lblFlightId);
		
		JLabel lblUserId = new JLabel("User ID");
		lblUserId.setBounds(22, 276, 70, 15);
		frame.getContentPane().add(lblUserId);
		
		textCustomerID = new JTextField();
		textCustomerID.setBounds(136, 274, 114, 19);
		frame.getContentPane().add(textCustomerID);
		textCustomerID.setColumns(10);
		
		JLabel lblUserName = new JLabel("User Name");
		lblUserName.setBounds(22, 303, 84, 15);
		frame.getContentPane().add(lblUserName);
		
		textCustomerName = new JTextField();
		textCustomerName.setBounds(136, 301, 114, 19);
		frame.getContentPane().add(textCustomerName);
		textCustomerName.setColumns(10);
		
		JLabel lblFlightIdReturn = new JLabel("Flight ID Return");
		lblFlightIdReturn.setBounds(299, 248, 120, 15);
		frame.getContentPane().add(lblFlightIdReturn);
		
		textReturnFlightID = new JTextField();
		textReturnFlightID.setBounds(437, 248, 114, 19);
		frame.getContentPane().add(textReturnFlightID);
		textReturnFlightID.setColumns(10);
		
		chckbxReturn = new JCheckBox("Return");
		chckbxReturn.setBounds(299, 272, 129, 23);
		frame.getContentPane().add(chckbxReturn);
		
		bBooking = new JButton("Book");
		bBooking.setBounds(54, 340, 117, 25);
        bBooking.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				bookASeat();
				
			}
		});
		frame.getContentPane().add(bBooking);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(12, 377, 558, 2);
		frame.getContentPane().add(separator_1);
		
		textBookingReference = new JTextField();
		textBookingReference.setBounds(186, 391, 365, 19);
		frame.getContentPane().add(textBookingReference);
		textBookingReference.setColumns(36);
		
		JLabel lblBookingReference = new JLabel("Booking reference");
		lblBookingReference.setBounds(22, 393, 184, 15);
		frame.getContentPane().add(lblBookingReference);
		
		bCheckStatus = new JButton("Check booking");
		bCheckStatus.setBounds(54, 431, 163, 25);
        bCheckStatus.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				checkBookingStatus();
				
			}
		});
		frame.getContentPane().add(bCheckStatus);
		
		bCancellation = new JButton("Cancel booking");
		bCancellation.setBounds(245, 431, 174, 25);
		bCancellation.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				cancelABooking();
				
			}
		});
		frame.getContentPane().add(bCancellation);
		
		JSeparator separator_2 = new JSeparator();
		separator_2.setBounds(12, 483, 558, 2);
		frame.getContentPane().add(separator_2);
		
		JScrollPane spNotifications = new JScrollPane();
		spNotifications.setToolTipText("");
		spNotifications.setBounds(22, 499, 548, 158);
		frame.getContentPane().add(spNotifications);
		
		notificationPanel = new JTextArea();
		notificationPanel.setBorder(new TitledBorder(null, "Notifications", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		notificationPanel.setToolTipText("");
		notificationPanel.setEditable(false);
		notificationPanel.setEditable(false);
		spNotifications.setViewportView(notificationPanel);
		
		JLabel lblHelicopterIp = new JLabel("Helicopter IP");
		lblHelicopterIp.setBounds(22, 12, 114, 15);
		frame.getContentPane().add(lblHelicopterIp);
		
		textHelicopterIP = new JTextField();
		textHelicopterIP.setBounds(125, 10, 125, 19);
		textHelicopterIP.setText("localhost");
		frame.getContentPane().add(textHelicopterIP);
		textHelicopterIP.setColumns(10);
		
		JLabel lblHelicopterPort = new JLabel("Port");
		lblHelicopterPort.setBounds(268, 12, 70, 15);
		frame.getContentPane().add(lblHelicopterPort);
		
		textHelicopterPort = new JTextField();
		textHelicopterPort.setBounds(307, 10, 62, 19);
		textHelicopterPort.setText("2014");
		frame.getContentPane().add(textHelicopterPort);
		textHelicopterPort.setColumns(10);
		
		bConnectHelicopter = new JButton("Connect");
		bConnectHelicopter.setBounds(395, 7, 159, 25);
        bConnectHelicopter.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				connectToHelicopter();
				
			}
		});
		frame.getContentPane().add(bConnectHelicopter);
		
		JSeparator separator_3 = new JSeparator();
		separator_3.setBounds(22, 39, 548, 2);
		frame.getContentPane().add(separator_3);
	}
	
	// ########### listener actions #####################
	
   private void search4Flights(){	
    	new ControllerFlightSearch(this).start();
    }
    
    private void bookASeat() {
    	new ControllerBookings(this).start();
    }
    
    private void cancelABooking() {    	
    	new ControllerCancellations(this).start();
    }
    
    private void checkBookingStatus() {
    	new ControllerBookingStatus(this).start();
    }
    
	private void connectToHelicopter() {
		new ControllerHelicopterConnection(this).start();
	}
    
    public void writeNotification(String text){
        GregorianCalendar currentTime = new GregorianCalendar();
        int hour, min, sec;

        hour = currentTime.get(GregorianCalendar.HOUR_OF_DAY);
        min = currentTime.get(GregorianCalendar.MINUTE);
        sec= currentTime.get(GregorianCalendar.SECOND);

        getNotificationPanel().append("["+ hour +":"+ min +":"+ sec +
                "] " + text + "\n");        
    }
}



