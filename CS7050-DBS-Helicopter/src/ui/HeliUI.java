package ui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JComboBox;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.GregorianCalendar;

import javax.swing.JLabel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;
import javax.swing.JScrollPane;

import net.ConnectionHandler;
import net.ConnectionManager;

import controllers.StateController;


public class HeliUI {
	
	private static final String CITY = "City";
	private static final String CAMP = "Camp";

	private JFrame frmHelicopterSimulator;
	private JComboBox cbState;
	private JTextArea notificationArea;
	
	private StateController stateController;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		if (args.length > 0) {
			// set server ip
			ConnectionHandler.serverAddress = args[0];
		}
		if (args.length > 1) {
			try {
				int port = Integer.parseInt(args[1]);
				// set server port
				ConnectionHandler.serverPort = port;
			}catch(NumberFormatException nfe) {
				System.out.println("The server port has to be a number");
				System.exit(-1);
			}
		}
		if (args.length > 2) {
			try {
				int port = Integer.parseInt(args[2]);
				// set helicopter port
				ConnectionManager.helicopter_port_number = port;
			}catch(NumberFormatException nfe) {
				System.out.println("The helicopter port has to be a number");
				System.exit(-1);
			}
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {

				try {
					HeliUI window = new HeliUI();
					window.frmHelicopterSimulator.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public HeliUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmHelicopterSimulator = new JFrame();
		frmHelicopterSimulator.setTitle("CS7050-DBS-Helicopter");
		frmHelicopterSimulator.setBounds(100, 100, 592, 501);
		frmHelicopterSimulator.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmHelicopterSimulator.getContentPane().setLayout(null);
		
		cbState = new JComboBox();
		cbState.setModel(new DefaultComboBoxModel(new String[] {CITY, CAMP}));
		cbState.setBounds(81, 12, 127, 24);
		cbState.setMinimumSize(new Dimension(10, 24));
		frmHelicopterSimulator.getContentPane().add(cbState);
		
		JLabel lblState = new JLabel("State:");
		lblState.setBounds(12, 17, 97, 15);
		frmHelicopterSimulator.getContentPane().add(lblState);
		
		JButton btnStartFlight = new JButton("Start flight");
		btnStartFlight.setBounds(253, 12, 117, 25);
		frmHelicopterSimulator.getContentPane().add(btnStartFlight);
		btnStartFlight.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				changeLocation();
			}
		});
		
		JScrollPane spNotifications = new JScrollPane();
		spNotifications.setBounds(12, 194, 566, 266);
		frmHelicopterSimulator.getContentPane().add(spNotifications);
		
		notificationArea = new JTextArea();
		spNotifications.setViewportView(notificationArea);
		notificationArea.setBorder(new TitledBorder(null, "Notifications", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		JButton btnSendInfo = new JButton("Exchange info");
		btnSendInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendInformation();
			}
		});
		btnSendInfo.setBounds(12, 74, 196, 25);
		frmHelicopterSimulator.getContentPane().add(btnSendInfo);
	
	
		stateController = new StateController(this);
		changeLocation();
	}
	
	private void changeLocation(){
		String selectedLocation = (String) cbState.getSelectedItem();
		int location = 0;
		if (selectedLocation.equals(CITY))
			location = StateController.LOCATION_CITY;
		else {
			location = StateController.LOCATION_CAMP;
		}
		stateController.changeLocation(location);
	}
	
	private void sendInformation() {
		stateController.sendInformationToCity();
	}
	
    public void writeNotification(String notification){
        GregorianCalendar currentTime=new GregorianCalendar();
        int h, min, secs;

        h = currentTime.get(GregorianCalendar.HOUR_OF_DAY);
        min = currentTime.get(GregorianCalendar.MINUTE);
        secs = currentTime.get(GregorianCalendar.SECOND);

       
        notificationArea.append("["+ h +":"+ min +":"+ secs +
                "] " + notification + "\n");        
    }
}
