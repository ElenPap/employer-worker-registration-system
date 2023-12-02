package com.cbozan.view.record;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JViewport;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import com.cbozan.dao.DB;
import com.cbozan.dao.EmployerDAO;
import com.cbozan.dao.JobDAO;
import com.cbozan.dao.PriceDAO;
import com.cbozan.entity.Employer;
import com.cbozan.entity.Job;
import com.cbozan.entity.Price;
import com.cbozan.exception.EntityException;
import com.cbozan.util.LayoutMetrics;
import com.cbozan.view.component.RecordTextField;
import com.cbozan.view.component.SearchBox;
import com.cbozan.view.component.TextArea;
import com.cbozan.view.helper.Observer;

public class JobPanel extends JPanel implements Observer, Serializable, ActionListener{

	private static final long serialVersionUID = 1L;
	
	private final List<Observer> observers;

	private JLabel imageLabel;
	private JLabel titleLabel, employerLabel, priceLabel, descriptionLabel;
	private RecordTextField titleTextField;
	private JComboBox<Price> priceComboBox;
	private TextArea descriptionTextArea;
	private JButton saveButton;
	
	private Employer selectedEmployer;
	private SearchBox employerSearchBox;
	private final LayoutMetrics layoutMetrics;
	
	public JobPanel() {
		
		super();
		layoutMetrics = new LayoutMetrics(230, 330, 190, 25, 95, 25, 40, 0, 30, 80, 30);
		setLayout(null);
		
		selectedEmployer = null;
		observers = new ArrayList<>();
		subscribe(this);
		
		imageLabel = new JLabel(new ImageIcon("src\\icon\\new_job.png"));
		imageLabel.setBounds(layoutMetrics.getLabelPositionX() + 157, 50, 128, 128);
		add(imageLabel);
		
		
		titleLabel = new JLabel("Job Title");
		titleLabel.setBounds(layoutMetrics.getLabelPositionX(), layoutMetrics.getLabelPositionY(), layoutMetrics.getLabelWidth(), layoutMetrics.getLabelHeight());
		add(titleLabel);
		
		titleTextField = new RecordTextField(RecordTextField.REQUIRED_TEXT);
		titleTextField.setBounds(layoutMetrics.getLabelPositionX() + titleLabel.getWidth() + layoutMetrics.getLabelHorizontalSpace(), titleLabel.getY(), layoutMetrics.getTextFieldWidth(), layoutMetrics.getTextFieldHeight());
		titleTextField.setHorizontalAlignment(SwingConstants.CENTER);
		titleTextField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!titleTextField.getText().replaceAll("\\s+", "").equals(""))
					employerSearchBox.requestFocus();
			}
		});
		add(titleTextField);
		
		
		employerLabel = new JLabel("Employer");
		employerLabel.setBounds(layoutMetrics.getLabelPositionX(), titleLabel.getY() + layoutMetrics.getLabelVerticalSpace(), layoutMetrics.getLabelWidth(), layoutMetrics.getLabelHeight());
		add(employerLabel);
		
		employerSearchBox = new SearchBox(EmployerDAO.getInstance().list(), new Dimension(layoutMetrics.getTextFieldWidth(), layoutMetrics.getTextFieldHeight())) {
			private static final long serialVersionUID = 685599997274436984L;
			@Override
			public void mouseAction(MouseEvent e, Object searchResultObject, int chooseIndex) {
				selectedEmployer = (Employer) searchResultObject;
				employerSearchBox.setText(selectedEmployer.toString());
				employerSearchBox.setEditable(false);
				priceComboBox.requestFocus();
				super.mouseAction(e, searchResultObject, chooseIndex);
			}
		};
		employerSearchBox.setBounds(layoutMetrics.getLabelPositionX() + employerLabel.getWidth() + layoutMetrics.getLabelHorizontalSpace(), employerLabel.getY(), layoutMetrics.getTextFieldWidth(), layoutMetrics.getTextFieldHeight());
		add(employerSearchBox);
		
		employerSearchBox.getPanel().setBounds(employerSearchBox.getX(), employerSearchBox.getY() + layoutMetrics.getTextFieldHeight(), layoutMetrics.getTextFieldWidth(), 0);
		add(employerSearchBox.getPanel());
		
		priceLabel = new JLabel("Ücretlendirme");
		priceLabel.setBounds(layoutMetrics.getLabelPositionX(), employerLabel.getY() + layoutMetrics.getLabelVerticalSpace(), layoutMetrics.getLabelWidth(), layoutMetrics.getLabelHeight());
		add(priceLabel);
		
		
		priceComboBox = new JComboBox<Price>(PriceDAO.getInstance().list().toArray(new Price[0]));
		priceComboBox.setBounds(layoutMetrics.getLabelPositionX() + priceLabel.getWidth() + layoutMetrics.getLabelHorizontalSpace(), priceLabel.getY(), layoutMetrics.getTextFieldWidth(), layoutMetrics.getTextFieldHeight());
		priceComboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				descriptionTextArea.getViewport().getComponent(0).requestFocus();
				
			}
		});
		add(priceComboBox);
		
		descriptionLabel = new JLabel("Description");
		descriptionLabel.setBounds(layoutMetrics.getLabelPositionX(), priceLabel.getY() + layoutMetrics.getLabelVerticalSpace(), layoutMetrics.getLabelWidth(), layoutMetrics.getLabelHeight());
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
		// başlık kontrolü
		
		if(e.getSource() == saveButton) {
			
			String title, description;
			Employer employer;
			Price price;
			
			
			title = titleTextField.getText().trim().toUpperCase();
			employer = selectedEmployer;
			price = (Price) priceComboBox.getSelectedItem();
			description = descriptionTextArea.getText().trim().toUpperCase();
			
			
			if( title.equals("") || employer == null || price == null) {
				
				String message = "Please fill in or select the required fields.";
				JOptionPane.showMessageDialog(this, message, "HATA", JOptionPane.ERROR_MESSAGE);
			}	
//			} else if(JobDAO.getInstance().isContainsTitle(title)){ // control
//				JOptionPane.showMessageDialog(this, "db error, same job title");
//			} 
			else {
				
				JTextArea titleTextArea, employerTextArea, priceTextArea, descriptionTextArea; 
				
				titleTextArea = new JTextArea(title);
				titleTextArea.setEditable(false);
				
				employerTextArea = new JTextArea(employer.toString());
				employerTextArea.setEditable(false);
				
				priceTextArea = new JTextArea(price.toString());
				priceTextArea.setEditable(false);
				
				descriptionTextArea = new JTextArea(description);
				descriptionTextArea.setEditable(false);
				
				
				
				Object[] pane = {
						new JLabel("Job Title"),
						titleTextArea,
						new JLabel("Employer"),
						employerTextArea,
						new JLabel("Price"),
						priceTextArea,
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
					
					Job.JobBuilder builder = new Job.JobBuilder();
					builder.setId(Integer.MAX_VALUE);
					builder.setTitle(title);
					builder.setEmployer(employer);
					builder.setPrice(price);
					builder.setDescription(description);
					
					Job job = null;
					try {
						job = builder.build();
					} catch (EntityException e1) {
						System.out.println(e1.getMessage());
					}
					
					
					if(JobDAO.getInstance().create(job)) { 
						JOptionPane.showMessageDialog(this, "Registration successful");
						notifyAllObservers();
					} else {
						JOptionPane.showMessageDialog(this, DB.ERROR_MESSAGE, "NOT SAVED", JOptionPane.ERROR_MESSAGE);
						titleTextField.setBorder(new LineBorder(Color.red));
					}
				}
				
			}
			
		}
		
	}
	
	
	private void clearPanel() {
		
		titleTextField.setText("");
		((JTextArea)((JViewport)descriptionTextArea.getComponent(0)).getComponent(0)).setText("");
		
		titleTextField.setBorder(new LineBorder(Color.white));
		
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
		
		priceComboBox.setModel(new DefaultComboBoxModel<>(PriceDAO.getInstance().list().toArray(new Price[0])));
		employerSearchBox.setObjectList(EmployerDAO.getInstance().list());
		
	}
	
}
