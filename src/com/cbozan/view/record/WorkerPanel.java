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
import javax.swing.border.LineBorder;

import com.cbozan.dao.WorkerDAO;
import com.cbozan.entity.Worker;
import com.cbozan.exception.EntityException;
import com.cbozan.util.LayoutMetrics;
import com.cbozan.view.component.RecordTextField;
import com.cbozan.view.component.TextArea;
import com.cbozan.view.helper.Control;
import com.cbozan.view.helper.Observer;

public class WorkerPanel extends JPanel implements Observer, Serializable, ActionListener{

	private static final long serialVersionUID = 5821030218386153605L;
	private final List<Observer> observers;
	
	private JLabel imageLabel;
	private JLabel fnameLabel, lnameLabel, phoneNumberLabel, ibanLabel, descriptionLabel;
	private RecordTextField fnameTextField, lnameTextField, phoneNumberTextField, ibanTextField;
	private TextArea descriptionTextArea;
	private JButton saveButton;
	private final LayoutMetrics layoutMetrics;
	
	public WorkerPanel() {
		
		super();
		layoutMetrics = new LayoutMetrics(230, 330, 190, 25, 95, 25, 40, 0, 30, 80, 30);
		setLayout(null);
		
		observers = new ArrayList<>();
		subscribe(this);
		
		imageLabel = new JLabel(new ImageIcon("src\\icon\\new_worker.png"));
		imageLabel.setBounds(layoutMetrics.getLabelPositionX() + 157, 50, 128, 128);
		add(imageLabel);
		
		
		fnameLabel = new JLabel("Name");
		fnameLabel.setBounds(layoutMetrics.getLabelPositionX(), layoutMetrics.getLabelPositionY(), layoutMetrics.getLabelWidth(), layoutMetrics.getLabelHeight());
		add(fnameLabel);
		
		fnameTextField = new RecordTextField(RecordTextField.REQUIRED_TEXT);
		fnameTextField.setBounds(layoutMetrics.getLabelPositionX() + fnameLabel.getWidth() + layoutMetrics.getLabelHorizontalSpace(), fnameLabel.getY(), layoutMetrics.getTextFieldWidth(), layoutMetrics.getTextFieldHeight());
		fnameTextField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!fnameTextField.getText().replaceAll("\\s+", "").equals(""))
					lnameTextField.requestFocus();
			}
		});
		add(fnameTextField);
		
		
		lnameLabel = new JLabel("Surname");
		lnameLabel.setBounds(layoutMetrics.getLabelPositionX(), fnameLabel.getY() + layoutMetrics.getLabelVerticalSpace(), layoutMetrics.getLabelWidth(), layoutMetrics.getLabelHeight());
		add(lnameLabel);
		
		lnameTextField = new RecordTextField(RecordTextField.REQUIRED_TEXT);
		lnameTextField.setBounds(layoutMetrics.getLabelPositionX() + lnameLabel.getWidth() + layoutMetrics.getLabelHorizontalSpace(), lnameLabel.getY(), layoutMetrics.getTextFieldWidth(), layoutMetrics.getTextFieldHeight());
		lnameTextField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!lnameTextField.getText().replaceAll("\\s+", "").equals("")) {
					phoneNumberTextField.requestFocus();
				}
			}
		});
		add(lnameTextField);
		
		phoneNumberLabel = new JLabel("Phone Nu.");
		phoneNumberLabel.setBounds(layoutMetrics.getLabelPositionX(), lnameLabel.getY() + layoutMetrics.getLabelVerticalSpace(), layoutMetrics.getLabelWidth(), layoutMetrics.getLabelHeight());
		add(phoneNumberLabel);
		
		phoneNumberTextField = new RecordTextField(RecordTextField.PHONE_NUMBER_TEXT + RecordTextField.NON_REQUIRED_TEXT);
		phoneNumberTextField.setBounds(layoutMetrics.getLabelPositionX() + phoneNumberLabel.getWidth() + layoutMetrics.getLabelHorizontalSpace(), phoneNumberLabel.getY(), layoutMetrics.getTextFieldWidth(), layoutMetrics.getTextFieldHeight());
		phoneNumberTextField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(Control.phoneNumberControl(phoneNumberTextField.getText())) {
					ibanTextField.requestFocus();
				}
			}
		});
		add(phoneNumberTextField);
		
		ibanLabel = new JLabel("Iban");
		ibanLabel.setBounds(layoutMetrics.getLabelPositionX(), phoneNumberLabel.getY() + layoutMetrics.getLabelVerticalSpace(), layoutMetrics.getLabelWidth(), layoutMetrics.getLabelHeight());
		add(ibanLabel);
		
		ibanTextField = new RecordTextField(RecordTextField.IBAN_NUMBER_TEXT + RecordTextField.NON_REQUIRED_TEXT);
		ibanTextField.setBounds(layoutMetrics.getLabelPositionX() + ibanLabel.getWidth() + layoutMetrics.getLabelHorizontalSpace(), ibanLabel.getY(), layoutMetrics.getTextFieldWidth(), layoutMetrics.getTextFieldHeight());
		ibanTextField.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(Control.ibanControl(ibanTextField.getText())) {
					((JTextArea)((JViewport)descriptionTextArea.getComponent(0)).getComponent(0)).requestFocus();
				}
				
			}
		});
		add(ibanTextField);
		
		descriptionLabel= new JLabel("Description");
		descriptionLabel.setBounds(ibanLabel.getX(), ibanLabel.getY() + layoutMetrics.getLabelVerticalSpace(), layoutMetrics.getLabelWidth(), layoutMetrics.getLabelHeight());
		add(descriptionLabel);
		
		descriptionTextArea = new TextArea();
		descriptionTextArea.setBounds(descriptionLabel.getX() + layoutMetrics.getLabelWidth() + layoutMetrics.getLabelHorizontalSpace(), descriptionLabel.getY(), layoutMetrics.getTextFieldWidth(), layoutMetrics.getTextFieldHeight() * 3);
		add(descriptionTextArea);
		
		saveButton = new JButton("SAVE");
		saveButton.setBounds(descriptionTextArea.getX() + ((layoutMetrics.getTextFieldWidth() - layoutMetrics.getButtonWidth()) / 2), descriptionTextArea.getY() + descriptionTextArea.getHeight() + 20, layoutMetrics.getButtonWidth(), layoutMetrics.getButtonHeight());
		//save_button.setContentAreaFilled(false);
		saveButton.setFocusPainted(false);
		saveButton.addActionListener(this);
		add(saveButton);
		
		
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == saveButton) {
			
			String fname, lname, iban, phoneNumber, description;
			
			fname = fnameTextField.getText().trim().toUpperCase();
			lname = lnameTextField.getText().trim().toUpperCase();
			iban = ibanTextField.getText().replaceAll("\\s+",  "").toUpperCase();
			phoneNumber = phoneNumberTextField.getText().replaceAll("\\s+",  "");
			description = descriptionTextArea.getText().trim().toUpperCase();
			
			if( fname.equals("") || lname.equals("") || !Control.phoneNumberControl(phoneNumber) || !Control.ibanControl(iban)) {
				
				String message = "Please fill in required fields or \nEnter the Phone Nu. or Iban format correctly";
				JOptionPane.showMessageDialog(this, message, "ERROR", JOptionPane.ERROR_MESSAGE);
				
			} else {
				
				JTextArea fnameTextArea, lnameTextArea, phoneNumberTextArea, ibanTextArea, descriptionTextArea; 
				
				fnameTextArea = new JTextArea(fname);
				fnameTextArea.setEditable(false);
				
				lnameTextArea = new JTextArea(lname);
				lnameTextArea.setEditable(false);
				
				phoneNumberTextArea = new JTextArea(phoneNumber);
				phoneNumberTextArea.setEditable(false);
				
				ibanTextArea = new JTextArea(iban);
				ibanTextArea.setEditable(false);
				
				descriptionTextArea = new JTextArea(description);
				descriptionTextArea.setEditable(false);
				
				
				
				Object[] pane = {
						new JLabel("Name"),
						fnameTextArea,
						new JLabel("Surname"),
						lnameTextArea,
						new JLabel("Phone Number"),
						phoneNumberTextArea,
						new JLabel("Iban"),
						ibanTextArea,
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
					
					Worker.WorkerBuilder builder = new Worker.WorkerBuilder();
					builder.setId(Integer.MAX_VALUE);
					builder.setFname(fname);
					builder.setLname(lname);
					if(!phoneNumberTextField.getText().trim().equals("")) {
						builder.setTel(Arrays.asList(new String[] {phoneNumber}));
					}
					builder.setIban(iban);
					builder.setDescription(description);
					
					Worker worker = null;
					try {
						worker = builder.build();
					} catch (EntityException e1) {
						System.out.println(e1.getMessage());
					}
					
					
					
					if(WorkerDAO.getInstance().create(worker)) { 
						JOptionPane.showMessageDialog(this, "Registration successful");
						notifyAllObservers();
					} else {
						JOptionPane.showMessageDialog(this, "Not saved", "DataBase Error", JOptionPane.ERROR_MESSAGE);
					}
				}
				
			}
			
		}
		
	}
	
	
	private void clearPanel() {
		fnameTextField.setText("");
		lnameTextField.setText("");
		phoneNumberTextField.setText("");
		ibanTextField.setText("");
		((JTextArea)((JViewport)descriptionTextArea.getComponent(0)).getComponent(0)).setText("");
		
		fnameTextField.setBorder(new LineBorder(Color.white));
		lnameTextField.setBorder(new LineBorder(Color.white));
		phoneNumberTextField.setBorder(new LineBorder(Color.white));
		ibanTextField.setBorder(new LineBorder(Color.white));
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
