package com.cbozan.view.record;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JViewport;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import com.cbozan.dao.EmployerDAO;
import com.cbozan.entity.Employer;
import com.cbozan.exception.EntityException;
import com.cbozan.util.LayoutMetrics;
import com.cbozan.view.component.RecordTextField;
import com.cbozan.view.helper.Control;
import com.cbozan.view.helper.Observer;

public class EmployerPanel extends JPanel implements Observer, ActionListener, Serializable{
	
	private static final long serialVersionUID = -5013773197522265980L;
	private final List<Observer> observers;
	
	private JLabel imageLabel;
	private JLabel fnameLabel, lnameLabel, phoneNumberLabel, descriptionLabel;
	private RecordTextField fnameTextField, lnameTextField, phoneNumberTextField;
	private JScrollPane descriptionScrollPane;
	private JButton saveButton;
	private final LayoutMetrics layoutMetrics;
	
	public EmployerPanel() {
		
		super();
		layoutMetrics = new LayoutMetrics(250, 330, 190, 25, 95, 25, 40, 0, 30, 80, 30);
		setLayout(null);
		
		observers = new ArrayList<>();
		subscribe(this);
		
		GUI();
		
	}

	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == saveButton)
			save(e);
	}
	
	private void GUI() {
		
		imageLabel = new JLabel();
		imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
		imageLabel.setIcon(new ImageIcon("src\\icon\\new_employer.png"));
		imageLabel.setBounds(layoutMetrics.getLabelPositionX() + 155, 40, 128, 130);
		add(imageLabel);

		fnameLabel = new JLabel("Name");
		fnameLabel.setBounds(layoutMetrics.getLabelPositionX(), layoutMetrics.getLabelPositionY(), layoutMetrics.getLabelWidth(), layoutMetrics.getLabelHeight());
		add(fnameLabel);
		
		fnameTextField = new RecordTextField(RecordTextField.REQUIRED_TEXT);
		fnameTextField.setBounds(fnameLabel.getX() + layoutMetrics.getLabelWidth() + layoutMetrics.getLabelHorizontalSpace(), fnameLabel.getY(), layoutMetrics.getTextFieldWidth(), layoutMetrics.getTextFieldHeight());
		fnameTextField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!fnameTextField.getText().replaceAll("\\s+", "").equals("")) {
					lnameTextField.requestFocus();
				}
			}
		});
		add(fnameTextField);
		
		
		lnameLabel = new JLabel("Surname");
		lnameLabel.setBounds(fnameLabel.getX(), fnameLabel.getY() + layoutMetrics.getLabelVerticalSpace(), layoutMetrics.getLabelWidth(), layoutMetrics.getLabelHeight());
		add(lnameLabel);
		
		lnameTextField = new RecordTextField(RecordTextField.REQUIRED_TEXT);
		lnameTextField.setBounds(lnameLabel.getX() + layoutMetrics.getLabelWidth() + layoutMetrics.getLabelHorizontalSpace(), lnameLabel.getY(), layoutMetrics.getTextFieldWidth(), layoutMetrics.getTextFieldHeight());
		lnameTextField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!lnameTextField.getText().replaceAll("\\s+", "").equals("")) {
					phoneNumberTextField.requestFocus();
				}
			}
		});
		add(lnameTextField);
		
		
		phoneNumberLabel = new JLabel("Phone Nu.");
		phoneNumberLabel.setBounds(lnameLabel.getX(), lnameLabel.getY() + layoutMetrics.getLabelVerticalSpace(), layoutMetrics.getLabelWidth(), layoutMetrics.getLabelHeight());
		add(phoneNumberLabel);
		
		phoneNumberTextField= new RecordTextField(RecordTextField.PHONE_NUMBER_TEXT + RecordTextField.REQUIRED_TEXT);
		phoneNumberTextField.setBounds(phoneNumberLabel.getX() + layoutMetrics.getLabelWidth() + layoutMetrics.getLabelHorizontalSpace(), phoneNumberLabel.getY(), layoutMetrics.getTextFieldWidth(), layoutMetrics.getTextFieldHeight());
		phoneNumberTextField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(Control.phoneNumberControl(phoneNumberTextField.getText())) {
					((JTextArea)((JViewport)descriptionScrollPane.getComponent(0)).getComponent(0)).requestFocus();
				}
			}
		});
		add(phoneNumberTextField);
		
		
		descriptionLabel= new JLabel("Description");
		descriptionLabel.setBounds(phoneNumberLabel.getX(), phoneNumberLabel.getY() + layoutMetrics.getLabelVerticalSpace(), layoutMetrics.getLabelWidth(), layoutMetrics.getLabelHeight());
		add(descriptionLabel);
		
		descriptionScrollPane = new JScrollPane(new JTextArea());
		descriptionScrollPane.setBounds(descriptionLabel.getX() + layoutMetrics.getLabelWidth() + layoutMetrics.getLabelHorizontalSpace(), descriptionLabel.getY(), layoutMetrics.getTextFieldWidth(), layoutMetrics.getTextFieldHeight() * 3);
		add(descriptionScrollPane);
		
		saveButton = new JButton("SAVE");
		saveButton.setBounds(descriptionScrollPane.getX() + ((layoutMetrics.getTextFieldWidth() - layoutMetrics.getButtonWidth()) / 2), descriptionScrollPane.getY() + descriptionScrollPane.getHeight() + 20, layoutMetrics.getButtonWidth(), layoutMetrics.getButtonHeight());
		saveButton.setFocusPainted(false);
		saveButton.addActionListener(this);
		add(saveButton);
		
	}
	
	
	private void save(ActionEvent e) {
		String fname, lname, phoneNumber, description;
		
		fname = fnameTextField.getText().trim().toUpperCase();
		lname = lnameTextField.getText().trim().toUpperCase();
		phoneNumber = phoneNumberTextField.getText().replaceAll("\\s+", "");
		description = ((JTextArea)((JViewport)descriptionScrollPane.getComponent(0)).getComponent(0)).getText().trim().toUpperCase();
		
		if( fname.equals("") || lname.equals("") || !Control.phoneNumberControl(phoneNumber) ) {
			
			String message = "Please fill in required fields or \\nEnter the Phone Number format correctly";
			JOptionPane.showMessageDialog(this, message, "ERROR", JOptionPane.ERROR_MESSAGE);
			
		} else {
			
			JTextArea fnameTextArea, lnameTextArea, phoneNumberTextArea, descriptionTextArea; 
			
			fnameTextArea = new JTextArea(fname);
			fnameTextArea.setEditable(false);
			
			lnameTextArea = new JTextArea(lname);
			lnameTextArea.setEditable(false);
			
			phoneNumberTextArea = new JTextArea(phoneNumber);
			phoneNumberTextArea.setEditable(false);
			
			descriptionTextArea = new JTextArea(description);
			descriptionTextArea.setEditable(false);
			
			Object[] pane = {
					new JLabel("Name"),
					fnameTextArea,
					new JLabel("Surname"),
					lnameTextArea,
					new JLabel("Phone Number"),
					phoneNumberTextArea,
					new JLabel("Description"),
					new JScrollPane(descriptionTextArea) {
						private static final long serialVersionUID = 1L;
						public Dimension getPreferredSize() {
							return new Dimension(200, layoutMetrics.getTextFieldHeight() * 3);
						}
					}
			};
			

			int result = JOptionPane.showOptionDialog(this, pane, "Confirmation", 1, 1, 
					new ImageIcon("src\\icon\\accounting_icon_1_32.png"), new Object[] {"SAVE", "CANCEL"}, "CANCEL");
			
			// System.out.println(result);
			// 0 -> SAVE
			// 1 -> CANCEL
					
			if(result == 0) {
				
				Employer.EmployerBuilder builder = new Employer.EmployerBuilder();
				builder.setId(Integer.MAX_VALUE);
				builder.setFname(fname);
				builder.setLname(lname);
				if(!phoneNumberTextField.getText().trim().equals("")) {
					builder.setTel(Arrays.asList(new String[] {phoneNumber}));
				}
				builder.setDescription(description);
				
				Employer employer = null;
				try {
					employer = builder.build();
				} catch (EntityException e1) {
					System.out.println(e1.getMessage());
				}
				
				if(EmployerDAO.getInstance().create(employer)) { 
					JOptionPane.showMessageDialog(this, "Registration Successful");
					notifyAllObservers();
				} else {
					JOptionPane.showMessageDialog(this, "Not saved", "DataBase error", JOptionPane.ERROR_MESSAGE);
				}
				
			}
			
		} 
	}
	
	private void clearPanel() {
		
		fnameTextField.setText("");
		lnameTextField.setText("");
		phoneNumberTextField.setText("");
		((JTextArea)((JViewport)descriptionScrollPane.getComponent(0)).getComponent(0)).setText("");
		
		fnameTextField.setBorder(new LineBorder(Color.white));
		lnameTextField.setBorder(new LineBorder(Color.white));
		phoneNumberTextField.setBorder(new LineBorder(Color.white));
		
	}
	
	public void subscribe(Observer observer) {
		observers.add(observer);
	}
	
	public void unsubscribe(Observer observer) {
		observers.remove(observer);
	}
	
	public void notifyAllObservers() {
		for(Observer observer : observers) {
			observer.update();
		}
	}
	
	
	@Override
	public void update() {
		clearPanel();
	}
	
}
