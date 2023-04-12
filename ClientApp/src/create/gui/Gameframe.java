package create.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.Random;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.Timer;

import java.awt.Font;
import java.awt.Image;

import javax.swing.JLabel;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Choice;
import javax.swing.JList;
import java.awt.List;
import java.awt.ComponentOrientation;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Point;
import General.GameData;
import Messaging.Message;
import main.ClueLessClient;
import main.game.ClientGameActions;
import main.network.ClientData;
import main.network.ClientMessager;
import java.io.IOException;
import java.util.Scanner;
import java.awt.Dimension;
import java.awt.Component;
import java.awt.CardLayout;
import javax.swing.JComboBox;
import javax.swing.Box;
import javax.swing.JTextArea;
import java.awt.Rectangle;
import java.awt.TextArea;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
public class Gameframe extends JFrame {

	private JPanel gamePanel;
	public boolean gameStart = false;
	public static String prevMsg = "";
	public static String outputMsg;
	public static String inputMsg;
	private Timer timer;
	private JTextField inputField;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Gameframe frame = new Gameframe();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	
	public Gameframe() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 912, 536);
		JPanel gamePanel = new JPanel();
		gamePanel.setAlignmentX(Component.RIGHT_ALIGNMENT);
		gamePanel.setFont(new Font("Tahoma", Font.PLAIN, 8));
		gamePanel.setBackground(new Color(192, 192, 192));
		gamePanel.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(gamePanel);
		gamePanel.setLayout(null);
		
		JButton startBttn = new JButton("Start Game");
		startBttn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startBttn.setVisible(false);
				gameStart = true;
				ClueLessClient clientmssg = new ClueLessClient();
				clientmssg.start();
				timer.start();
			}
		});
		
		startBttn.setBounds(10, 25, 102, 38);
		gamePanel.add(startBttn);
		
		JLabel lblNewLabel = new JLabel("Clue Game");
		lblNewLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 18));
		lblNewLabel.setForeground(new Color(255, 128, 64));
		lblNewLabel.setBounds(232, 25, 102, 44);
		gamePanel.add(lblNewLabel);
		

		ImageIcon boardIcon = new ImageIcon("C:\\Users\\jason\\Downloads\\asahi_biru_fall22-origin-Josh (1)\\asahi_biru_fall22-origin-Josh\\ClientApp\\src\\Icons\\Board Final.png");

		
		int boardWidth = boardIcon.getIconWidth() - 150   ;
		int boardHeight = boardIcon.getIconHeight()   - 150;
		Image scaled = scaleImage(boardIcon.getImage(),boardWidth,boardHeight);
		
		ImageIcon scaledIcon = new ImageIcon(scaled);		
		JPanel boardpanel = new JPanel();
		boardpanel.setBounds(139, 65, 400, 360);
		boardpanel.setLayout(new CardLayout(0, 0));
		
		//contentPane.add(boardpanel);
		JLabel boardLabel = new JLabel(scaledIcon);
		boardLabel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		boardLabel.setBounds(139, 65, boardWidth, boardHeight);
		boardpanel.add(boardLabel, "name_2682798538306900");
		gamePanel.add(boardpanel);
		
		JTextArea outMsg = new JTextArea();
		
		JScrollPane scroll = new JScrollPane(outMsg,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.setBounds(563, 95, 314, 167);
		timer = new Timer(10000,new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (outputMsg != "") {
					
					outMsg.setText(outputMsg);
					//outMsg.append(outputMsg);
					//System.out.println(outputMsg);
				}
			}
		});
		//saveoldMsg(outMsg.getText());
		
		//outMsg.action
		
		//outMsg.setText(outputMsg);
		//outMsg.setText();
		gamePanel.add(scroll);
		
		JLabel chatTitle = new JLabel("Game Interface Window");
		chatTitle.setBounds(637, 71, 131, 13);
		gamePanel.add(chatTitle);
		
		Panel inputPanel = new Panel();
		inputPanel.setBounds(637, 289, 171, 95);
		GridBagLayout gbl_inputPanel = new GridBagLayout();
		gbl_inputPanel.columnWidths = new int[]{51, 7, 57, 0};
		gbl_inputPanel.rowHeights = new int[]{21, 0, 0, 0};
		gbl_inputPanel.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_inputPanel.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		inputPanel.setLayout(gbl_inputPanel);
		
		gamePanel.add(inputPanel);
		
		inputField = new JTextField();
		GridBagConstraints gbc_inputField = new GridBagConstraints();
		gbc_inputField.gridwidth = 3;
		gbc_inputField.insets = new Insets(0, 0, 5, 5);
		gbc_inputField.fill = GridBagConstraints.HORIZONTAL;
		gbc_inputField.gridx = 0;
		gbc_inputField.gridy = 0;
		inputPanel.add(inputField, gbc_inputField);
		inputField.setColumns(10);
		
		JButton inputmsgBttn = new JButton("Enter");
		inputmsgBttn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//inputMsg = inputField.getText();
				inputMsg = sendtoBox(inputField.getText());
			}
		});
		GridBagConstraints gbc_inputmsgBttn = new GridBagConstraints();
		gbc_inputmsgBttn.gridwidth = 2;
		gbc_inputmsgBttn.insets = new Insets(0, 0, 0, 5);
		gbc_inputmsgBttn.anchor = GridBagConstraints.NORTHWEST;
		gbc_inputmsgBttn.gridx = 1;
		gbc_inputmsgBttn.gridy = 2;
		inputPanel.add(inputmsgBttn, gbc_inputmsgBttn);
		/*Box vBox = Box.createVerticalBox();
		vBox.setBounds(600, 331, 88, -74);
		v
		gamePanel.add(vBox);
		*/

		//boardpanel.add(new JLabel(new ImageIcon("ClientApp/src/Icons/clueboard.jpg")));
		
		
		
	}
/*	public void gameMessager() {
		if (gameStart = true) {
			ClueLessClient clientmssg = new ClueLessClient();
			clientmssg.clientMessager();
			
		}
	}*/
	/*private void saveoldMsg(String msg) {
		prevMsg = msgArea.getText();
	}*/
	public static String sendtoBox (String message) {
		return message;
	}
	public static void writetoBox (String message) {
		String newmsg;
		if(message != null) {
			newmsg = outputMsg + "\n" + message;
			outputMsg = newmsg;
			//prevMsg = message;
			//System.out.println(newmsg);
		}
		else
			outputMsg = "";
		
	
	}
	
	private Image scaleImage(Image image, int w, int h) {
			Image scaled = image.getScaledInstance(w,h,Image.SCALE_SMOOTH);
			
			return scaled;
	}
}
