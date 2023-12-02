package com.cbozan.view.record;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import com.cbozan.dao.PriceDAO;
import com.cbozan.entity.Price;
import com.cbozan.exception.EntityException;
import com.cbozan.util.LayoutMetrics;
import com.cbozan.view.component.RecordTextField;
import com.cbozan.view.helper.Control;
import com.cbozan.view.helper.Observer;

public class PricePanel extends JPanel implements Observer, Serializable, ActionListener{
	
	private static final long serialVersionUID = 1L;

	private final List<Observer> observers;
	
	private JLabel imageLabel;
	private JLabel fulltimeLabel, halftimeLabel, overtimeLabel;
	private RecordTextField fulltimeTextField, halftimeTextField, overtimeTextField;
	private JButton saveButton;
	
	private Color defaultColor;
	private final LayoutMetrics layoutMetrics;
	
	public PricePanel() {
		
		super();
		layoutMetrics = new LayoutMetrics(230, 450, 60, 25, 140, 25, 50, 30, 0, 0, 0);
		setLayout(null);
		
		observers = new ArrayList<>();
		subscribe(this);
		
		imageLabel = new JLabel(new ImageIcon("src\\icon\\new_price.png"));
		imageLabel.setBounds(layoutMetrics.getLabelPositionX() + 12, 50, 128, 128);
		this.add(imageLabel);
		
		defaultColor = imageLabel.getForeground();
		
		fulltimeLabel = new JLabel("Fulltime (TL)");
		fulltimeLabel.setBounds(layoutMetrics.getLabelPositionX(), layoutMetrics.getLabelPositionY() + layoutMetrics.getLabelTextFieldVerticalSpace(), layoutMetrics.getLabelWidth(), layoutMetrics.getLabelHeight());
		fulltimeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		
		fulltimeTextField = new RecordTextField(RecordTextField.DECIMAL_NUMBER_TEXT + RecordTextField.REQUIRED_TEXT);
		fulltimeTextField.setHorizontalAlignment(SwingConstants.CENTER);
		fulltimeTextField.setBounds(layoutMetrics.getLabelPositionX() + (fulltimeLabel.getWidth() / 2) - (layoutMetrics.getTextFieldWidth() / 2), layoutMetrics.getLabelPositionY(), layoutMetrics.getTextFieldWidth(), layoutMetrics.getTextFieldHeight());
		fulltimeTextField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(Control.decimalControl(fulltimeTextField.getText())) {
					halftimeTextField.requestFocus();
				}
					
			}
		});
		
		add(fulltimeTextField);
		add(fulltimeLabel);
		
		halftimeLabel = new JLabel("Halftime (TL)");
		halftimeLabel.setBounds(layoutMetrics.getLabelPositionX(), fulltimeLabel.getY() + layoutMetrics.getLabelVerticalSpace() + layoutMetrics.getLabelTextFieldVerticalSpace(), layoutMetrics.getLabelWidth(), layoutMetrics.getLabelHeight());
		halftimeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		halftimeTextField = new RecordTextField(RecordTextField.DECIMAL_NUMBER_TEXT + RecordTextField.REQUIRED_TEXT);
		halftimeTextField.setHorizontalAlignment(SwingConstants.CENTER);
		halftimeTextField.setBounds(layoutMetrics.getLabelPositionX() + (halftimeLabel.getWidth() / 2) - (layoutMetrics.getTextFieldWidth() / 2), halftimeLabel.getY() - layoutMetrics.getLabelTextFieldVerticalSpace(), layoutMetrics.getTextFieldWidth(), layoutMetrics.getTextFieldHeight());
		halftimeTextField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(Control.decimalControl(halftimeTextField.getText())) {
					overtimeTextField.requestFocus();
				}
			}
		});
		add(halftimeTextField);
		add(halftimeLabel);
		
		overtimeLabel = new JLabel("Overtime (TL)");
		overtimeLabel.setBounds(layoutMetrics.getLabelPositionX(), halftimeLabel.getY() + layoutMetrics.getLabelVerticalSpace() + layoutMetrics.getLabelTextFieldVerticalSpace(), layoutMetrics.getLabelWidth(), layoutMetrics.getLabelHeight());
		overtimeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		overtimeTextField = new RecordTextField(RecordTextField.DECIMAL_NUMBER_TEXT + RecordTextField.REQUIRED_TEXT);
		overtimeTextField.setHorizontalAlignment(SwingConstants.CENTER);
		overtimeTextField.setBounds(layoutMetrics.getLabelPositionX() + (overtimeLabel.getWidth() / 2) - (layoutMetrics.getTextFieldWidth() / 2), overtimeLabel.getY() - layoutMetrics.getLabelTextFieldVerticalSpace(), layoutMetrics.getTextFieldWidth(), layoutMetrics.getTextFieldHeight());
		overtimeTextField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(Control.decimalControl(halftimeTextField.getText())) {
					saveButton.doClick();
				}
			}
		});
		add(overtimeTextField);
		add(overtimeLabel);
		
		saveButton = new JButton("SAVE");
		saveButton.setBounds(overtimeLabel.getX() - 70, overtimeLabel.getY() + 50, overtimeLabel.getWidth() + 120, 30);
		//save_button.setContentAreaFilled(false);
		saveButton.setFocusPainted(false);
		saveButton.addActionListener(this);
		add(saveButton);
		
	}

		
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == saveButton) {
			
			String fulltime, halftime, overtime;
			
			fulltime = fulltimeTextField.getText().replaceAll("\\s+", "");
			halftime = halftimeTextField.getText().replaceAll("\\s+", "");
			overtime = overtimeTextField.getText().replaceAll("\\s+", "");
			
			if(!Control.decimalControl(fulltime, halftime, overtime)) {
				
				String message = "Please fill in the required fields or enter correctly format (max 2 floatpoint)";
				JOptionPane.showMessageDialog(this, message, "ERROR", JOptionPane.ERROR_MESSAGE);
				
			} else {
				
				JTextArea fulltimeTextArea, halftimeTextArea, overtimeTextArea;
				
				fulltimeTextArea = new JTextArea(fulltime + " ₺");
				fulltimeTextArea.setEditable(false);
				
				halftimeTextArea = new JTextArea(halftime + " ₺");
				halftimeTextArea.setEditable(false);
				
				overtimeTextArea = new JTextArea(overtime + " ₺");
				overtimeTextArea.setEditable(false);
				
				Object[] pane = {
						new JLabel("Fulltime Price"),
						fulltimeTextArea,
						new JLabel("Halftime Price"),
						halftimeTextArea,
						new JLabel("Overtime Price"),
						overtimeTextArea
				};
				
				
		
				int result = JOptionPane.showOptionDialog(this, pane, "Confirmation", 1, 1, 
						new ImageIcon("src\\icon\\accounting_icon_1_32.png"), new Object[] {"SAVE", "CANCEL"}, "CANCEL");
				
				
				// System.out.println(result);
				// 0 -> SAVE
				// 1 -> CANCEL
				
				if(result == 0) {
					
					Price.PriceBuilder builder = new Price.PriceBuilder();
					builder.setId(Integer.MAX_VALUE);
					builder.setFulltime(new BigDecimal(fulltime));
					builder.setHalftime(new BigDecimal(halftime));
					builder.setOvertime(new BigDecimal(overtime));
					
					Price price = null;
					try {
						price = builder.build();
					} catch (EntityException e1) {
						System.out.println(e1.getMessage());
					}
					
					if(PriceDAO.getInstance().create(price)) { 
						JOptionPane.showMessageDialog(this, "Registration Successful");
						notifyAllObservers();
					} else {
						JOptionPane.showMessageDialog(this, "Not saved", "DataBase error", JOptionPane.ERROR_MESSAGE);
					}
				}
				
			}
			
			
		}
		
	}
	
	private void clearPanel() {
		
		fulltimeTextField.setText("");
		halftimeTextField.setText("");
		overtimeTextField.setText("");
		
		fulltimeTextField.setBorder(new LineBorder(Color.white));
		halftimeTextField.setBorder(new LineBorder(Color.white));
		overtimeTextField.setBorder(new LineBorder(Color.white));
		
		fulltimeLabel.setForeground(defaultColor);
		halftimeLabel.setForeground(defaultColor);
		overtimeLabel.setForeground(defaultColor);
		
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
