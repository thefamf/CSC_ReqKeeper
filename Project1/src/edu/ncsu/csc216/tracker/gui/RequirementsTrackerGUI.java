package edu.ncsu.csc216.tracker.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;

import edu.ncsu.csc216.tracker.model.RequirementsTrackerModel;
import edu.ncsu.csc216.tracker.requirement.Command;
import edu.ncsu.csc216.tracker.requirement.Requirement;
import edu.ncsu.csc216.tracker.requirement.enums.CommandValue;
import edu.ncsu.csc216.tracker.requirement.enums.Rejection;

/**
 * Container for the RequirementsTracker that has the menu options for new  
 * requirements tracking files, loading existing files, saving files and quitting.
 * Depending on user actions, other {@link JPanel}s are loaded for the
 * different ways users interact with the UI.
 * 
 * @author Dr. Sarah Heckman (heckman@csc.ncsu.edu)
 */
public class RequirementsTrackerGUI extends JFrame implements ActionListener {
	
	/** ID number used for object serialization. */
	private static final long serialVersionUID = 1L;
	/** Title for top of GUI. */
	private static final String APP_TITLE = "ReqKeeper";
	/** Text for the File Menu. */
	private static final String FILE_MENU_TITLE = "File";
	/** Text for the New Req XML menu item. */
	private static final String NEW_XML_TITLE = "New";
	/** Text for the Load Req XML menu item. */
	private static final String LOAD_XML_TITLE = "Load";
	/** Text for the Save menu item. */
	private static final String SAVE_XML_TITLE = "Save";
	/** Text for the Quit menu item. */
	private static final String QUIT_TITLE = "Quit";
	/** Field for no priority */
	private static final int NO_PRIORITY = 0;
	/** Menu bar for the GUI that contains Menus. */
	private JMenuBar menuBar;
	/** Menu for the GUI. */
	private JMenu menu;
	/** Menu item for creating a new file containing {@link Req}s. */
	private JMenuItem itemNewReqXML;
	/** Menu item for loading a file containing {@link Req}s. */
	private JMenuItem itemLoadReqXML;
	/** Menu item for saving the requirements list. */
	private JMenuItem itemSaveReqXML;
	/** Menu item for quitting the program. */
	private JMenuItem itemQuit;
	/** Panel that will contain different views for the application. */
	private JPanel panel;
	/** Constant to identify RequriementListPanel for {@link CardLayout}. */
	private static final String REQ_LIST_PANEL = "RequriementListPanel";
	/** Constant to identify SubmittedPanel for {@link CardLayout}. */
	private static final String SUBMITTED_PANEL = "SubmittedPanel";
	/** Constant to identify AcceptedPanel for {@link CardLayout}. */
	private static final String ACCEPTED_PANEL = "AcceptedPanel";
	/** Constant to identify WorkingPanel for {@link CardLayout}. */
	private static final String WORKING_PANEL = "WorkingPanel";
	/** Constant to identify CompletedPanel for {@link CardLayout}. */
	private static final String COMPLETED_PANEL = "CompletedPanel";
	/** Constant to identify VerifiedPanel for {@link CardLayout}. */
	private static final String VERIFIED_PANEL = "VerifiedPanel";
	/** Constant to identify RejectedPanel for {@link CardLayout}. */
	private static final String REJECTED_PANEL = "RejectedPanel";
	/** Constant to identify CreateReqPanel for {@link CardLayout}. */
	private static final String CREATE_REQ_PANEL = "CreateReqPanel";
	/** Req List panel - we only need one instance, so it's final. */
	private final ReqListPanel pnlReqList = new ReqListPanel();
	/** Submitted panel - we only need one instance, so it's final. */
	private final SubmittedPanel pnlSubmitted = new SubmittedPanel();
	/** Accepted panel - we only need one instance, so it's final. */
	private final AcceptedPanel pnlAccepted = new AcceptedPanel();
	/** Working panel - we only need one instance, so it's final. */
	private final WorkingPanel pnlWorking = new WorkingPanel();
	/** Completed panel - we only need one instance, so it's final. */
	private final CompletedPanel pnlCompleted = new CompletedPanel();
	/** Verified panel - we only need one instance, so it's final. */
	private final VerifiedPanel pnlVerified = new VerifiedPanel();
	/** Rejected panel - we only need one instance, so it's final. */
	private final RejectedPanel pnlRejected = new RejectedPanel();
	/** Add Req panel - we only need one instance, so it's final. */
	private final CreateReqPanel pnlCreateReq = new CreateReqPanel();
	/** Reference to {@link CardLayout} for panel.  Stacks all of the panels. */
	private CardLayout cardLayout;
	/** List of rejection reasons */
	private static String [] rejections = {"Duplicate", "Infeasible", "Too Large", "Out of Scope", "Inappropriate"};
	
	
	/**
	 * Constructs a {@link RequirementsTrackerGUI} object that will contain a {@link JMenuBar} and a
	 * {@link JPanel} that will hold different possible views of the data in
	 * the {@link RequirementsTrackerModel}.
	 */
	public RequirementsTrackerGUI() {
		super();
		
		//Set up general GUI info
		setSize(500, 700);
		setLocation(50, 50);
		setTitle(APP_TITLE);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setUpMenuBar();
		
		//Create JPanel that will hold rest of GUI information.
		//The JPanel utilizes a CardLayout, which stack several different
		//JPanels.  User actions lead to switching which "Card" is visible.
		panel = new JPanel();
		cardLayout = new CardLayout();
		panel.setLayout(cardLayout);
		panel.add(pnlReqList, REQ_LIST_PANEL);
		panel.add(pnlSubmitted, SUBMITTED_PANEL);
		panel.add(pnlAccepted, ACCEPTED_PANEL);
		panel.add(pnlWorking, WORKING_PANEL);
		panel.add(pnlCompleted, COMPLETED_PANEL);
		panel.add(pnlVerified, VERIFIED_PANEL);
		panel.add(pnlRejected, REJECTED_PANEL);
		panel.add(pnlCreateReq, CREATE_REQ_PANEL);
		cardLayout.show(panel, REQ_LIST_PANEL);
		
		//Add panel to the container
		Container c = getContentPane();
		c.add(panel, BorderLayout.CENTER);
		
		//Set the GUI visible
		setVisible(true);
	}
	
	/**
	 * Makes the GUI Menu bar that contains options for loading a file
	 * containing requirements or for quitting the application.
	 */
	private void setUpMenuBar() {
		//Construct Menu items
		menuBar = new JMenuBar();
		menu = new JMenu(FILE_MENU_TITLE);
		itemNewReqXML = new JMenuItem(NEW_XML_TITLE);
		itemLoadReqXML = new JMenuItem(LOAD_XML_TITLE);
		itemSaveReqXML = new JMenuItem(SAVE_XML_TITLE);
		itemQuit = new JMenuItem(QUIT_TITLE);
		itemNewReqXML.addActionListener(this);
		itemLoadReqXML.addActionListener(this);
		itemSaveReqXML.addActionListener(this);
		itemQuit.addActionListener(this);
		
		//Start with save button disabled
		itemSaveReqXML.setEnabled(false);
		
		//Build Menu and add to GUI
		menu.add(itemNewReqXML);
		menu.add(itemLoadReqXML);
		menu.add(itemSaveReqXML);
		menu.add(itemQuit);
		menuBar.add(menu);
		this.setJMenuBar(menuBar);
	}
	
	/**
	 * Performs an action based on the given {@link ActionEvent}.
	 * @param e user event that triggers an action.
	 */
	public void actionPerformed(ActionEvent e) {
		//Use RequirementTracker's Model singleton to create/get the sole instance.
		RequirementsTrackerModel model = RequirementsTrackerModel.getInstance();
		if (e.getSource() == itemNewReqXML) {
			//Create a new requirements list list
			model.createNewRequirementsList();
			itemSaveReqXML.setEnabled(true);
			pnlReqList.updateTable();
			cardLayout.show(panel, REQ_LIST_PANEL);
			validate();
			repaint();			
		} else if (e.getSource() == itemLoadReqXML) {
			//Load an existing requirements list
			try {
				model.loadRequirementsFromFile(getFileName());
				itemSaveReqXML.setEnabled(true);
				pnlReqList.updateTable();
				cardLayout.show(panel, REQ_LIST_PANEL);
				validate();
				repaint();
			} catch (IllegalArgumentException exp) {
				JOptionPane.showMessageDialog(this, "Unable to load requirements file.");
			} catch (IllegalStateException exp) {
				//Don't do anything - user canceled (or error)
			}
		} else if (e.getSource() == itemSaveReqXML) {
			//Save current requirements list
			try {
				model.saveRequirementsToFile(getFileName());
			} catch (IllegalArgumentException exp) {
				JOptionPane.showMessageDialog(this, "Unable to save requirements file.");
			} catch (IllegalStateException exp) {
				//Don't do anything - user canceled (or error)
			}
		} else if (e.getSource() == itemQuit) {
			//Quit the program
			try {
				model.saveRequirementsToFile(getFileName());
				System.exit(0);  //Ignore FindBugs warning here - this is the only place to quit the program!
			} catch (IllegalArgumentException exp) {
				JOptionPane.showMessageDialog(this, "Unable to save req file.");
			} catch (IllegalStateException exp) {
				//Don't do anything - user canceled (or error)
			}
		}
	}
	
	/**
	 * Returns a file name generated through interactions with a {@link JFileChooser}
	 * object.
	 * @return the file name selected through {@link JFileChooser}
	 */
	private String getFileName() {
		JFileChooser fc = new JFileChooser("./");  //Open JFileChoose to current working directory
		fc.setDialogTitle("Requirements Tracker Load/Save");
		fc.setApproveButtonText("Select");
		int returnVal = fc.showOpenDialog(this);
		if (returnVal != JFileChooser.APPROVE_OPTION) {
			//Error or user canceled, either way no file name.
			throw new IllegalStateException();
		}
		File gameFile = fc.getSelectedFile();
		return gameFile.getAbsolutePath();
	}

	/**
	 * Starts the GUI for the RequirementsTracker application.
	 * @param args command line arguments
	 */
	public static void main(String [] args) {
		new RequirementsTrackerGUI();
	}
	
	/**
	 * Inner class that creates the look and behavior for the {@link JPanel} that 
	 * shows the list of requirements.
	 * 
	 * @author Dr. Sarah Heckman (heckman@csc.ncsu.edu)
	 */
	private class ReqListPanel extends JPanel implements ActionListener {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		/** Button for creating a new Requirement */
		private JButton btnAdd;
		/** Button for deleting the selected requirement in the list */
		private JButton btnDelete;
		/** Button for editing the selected requirement in the list */
		private JButton btnEdit;
		/** JTable for displaying the list of requirements */
		private JTable table;
		/** TableModel for Requirements */
		private ReqTableModel reqTableModel;
		
		/**
		 * Creates the requirements list.
		 */
		public ReqListPanel() {
			super(new BorderLayout());
			
			//Set up the JPanel that will hold action buttons		
			btnAdd = new JButton("Add New Requirement");
			btnAdd.addActionListener(this);
			btnDelete = new JButton("Delete Selected Requirement");
			btnDelete.addActionListener(this);
			btnEdit = new JButton("Edit Selected Requirement");
			btnEdit.addActionListener(this);
			
			JPanel pnlActions = new JPanel();
			pnlActions.setLayout(new GridLayout(2, 3));
			pnlActions.add(btnAdd);
			pnlActions.add(btnDelete);
			pnlActions.add(btnEdit);
						
			//Set up table
			reqTableModel = new ReqTableModel();
			table = new JTable(reqTableModel);
			table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			table.setPreferredScrollableViewportSize(new Dimension(500, 500));
			table.setFillsViewportHeight(true);
			
			JScrollPane listScrollPane = new JScrollPane(table);
			
			add(pnlActions, BorderLayout.NORTH);
			add(listScrollPane, BorderLayout.CENTER);
		}

		/**
		 * Performs an action based on the given {@link ActionEvent}.
		 * @param e user event that triggers an action.
		 */
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == btnAdd) {
				//If the add button is pressed switch to the createRequirementPanel
				cardLayout.show(panel,  CREATE_REQ_PANEL);
			} else if (e.getSource() == btnDelete) {
				//If the delete button is pressed, delete the requirement
				int row = table.getSelectedRow();
				if (row == -1) {
					JOptionPane.showMessageDialog(RequirementsTrackerGUI.this, "No requirement selected");
				} else {
					try {
						int reqId = Integer.parseInt(reqTableModel.getValueAt(row, 0).toString());
						RequirementsTrackerModel.getInstance().deleteRequirementById(reqId);
					} catch (NumberFormatException nfe ) {
						JOptionPane.showMessageDialog(RequirementsTrackerGUI.this, "Invalid requirement id");
					}
				}
				updateTable();
			} else if (e.getSource() == btnEdit) {
				//If the edit button is pressed, switch panel based on state
				int row = table.getSelectedRow();
				if (row == -1) {
					JOptionPane.showMessageDialog(RequirementsTrackerGUI.this, "No requirement selected");
				} else {
					try {
						int reqId = Integer.parseInt(reqTableModel.getValueAt(row, 0).toString());
						String stateName = RequirementsTrackerModel.getInstance().getRequirementById(reqId).getState().getStateName();
						if (stateName.equals(Requirement.SUBMITTED_NAME)) {
							cardLayout.show(panel, SUBMITTED_PANEL);
							pnlSubmitted.setReqInfo(reqId);
						} 
						if (stateName.equals(Requirement.ACCEPTED_NAME)) {
							cardLayout.show(panel, ACCEPTED_PANEL);
							pnlAccepted.setReqInfo(reqId);
						} 
						if (stateName.equals(Requirement.WORKING_NAME)) {
							cardLayout.show(panel, WORKING_PANEL);
							pnlWorking.setReqInfo(reqId);
						} 
						if (stateName.equals(Requirement.COMPLETED_NAME)) {
							cardLayout.show(panel, COMPLETED_PANEL);
							pnlCompleted.setReqInfo(reqId);
						} 
						if (stateName.equals(Requirement.VERIFIED_NAME)) {
							cardLayout.show(panel, VERIFIED_PANEL);
							pnlVerified.setReqInfo(reqId);
						} 
						if (stateName.equals(Requirement.REJECTED_NAME)) {
							cardLayout.show(panel, REJECTED_PANEL);
							pnlRejected.setReqInfo(reqId);
						}
						
					} catch (NumberFormatException nfe) {
						JOptionPane.showMessageDialog(RequirementsTrackerGUI.this, "Invalid requirement id");
					} catch (NullPointerException npe) {
						JOptionPane.showMessageDialog(RequirementsTrackerGUI.this, "Invalid requirement id");
					}
				}
			}
			RequirementsTrackerGUI.this.repaint();
			RequirementsTrackerGUI.this.validate();
		}
		
		public void updateTable() {
			reqTableModel.updateReqData();
		}
		
		/**
		 * {@link ReqTableModel} is the object underlying the {@link JTable} object that displays
		 * the list of {@link Requirement}s to the user.
		 * @author Dr. Sarah Heckman (heckman@csc.ncsu.edu)
		 */
		private class ReqTableModel extends AbstractTableModel {
			
			/** ID number used for object serialization. */
			private static final long serialVersionUID = 1L;
			/** Column names for the table */
			private String [] columnNames = {"Req ID", "Req State", "Req Summary"};
			/** Data stored in the table */
			private Object [][] data;
			
			/**
			 * Constructs the {@link ReqTableModel} by requesting the latest information
			 * from the {@link RequirementTrackerModel}.
			 */
			public ReqTableModel() {
				updateReqData();
			}

			/**
			 * Returns the number of columns in the table.
			 * @return the number of columns in the table.
			 */
			public int getColumnCount() {
				return columnNames.length;
			}

			/**
			 * Returns the number of rows in the table.
			 * @return the number of rows in the table.
			 */
			public int getRowCount() {
				if (data == null) 
					return 0;
				return data.length;
			}
			
			/**
			 * Returns the column name at the given index.
			 * @return the column name at the given column.
			 */
			public String getColumnName(int col) {
				return columnNames[col];
			}

			/**
			 * Returns the data at the given {row, col} index.
			 * @return the data at the given location.
			 */
			public Object getValueAt(int row, int col) {
				if (data == null)
					return null;
				return data[row][col];
			}
			
			/**
			 * Sets the given value to the given {row, col} location.
			 * @param value Object to modify in the data.
			 * @param row location to modify the data.
			 * @param column location to modify the data.
			 */
			public void setValueAt(Object value, int row, int col) {
				data[row][col] = value;
				fireTableCellUpdated(row, col);
			}
			
			/**
			 * Updates the given model with {@link Requirement} information from the {@link RequirementsTrackerModel}.
			 */
			private void updateReqData() {
				RequirementsTrackerModel m = RequirementsTrackerModel.getInstance();
				data = m.getRequirementListAsArray();
			}
			
		}
	}
	
	/**
	 * Inner class that creates the look and behavior for the {@link JPanel} that 
	 * interacts with a submitted requirement.
	 * 
	 * @author Dr. Sarah Heckman (heckman@csc.ncsu.edu)
	 */
	private class SubmittedPanel extends JPanel implements ActionListener {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		/** {@link ReqInfoPanel} that presents the {@link Requirement}'s information to the user */
		private ReqInfoPanel pnlInfo;
		/** Label for priority */
		private JLabel lblPriority;
		/** Text field for priority */
		private JTextField txtPriority;
		/** Label for estimate */
		private JLabel lblEstimate;
		/** Text field for estimate */
		private JTextField txtEstimate;
		/** Label for rejections */
		private JLabel lblRejections;
		/** Drop down for rejection reasons */
		private JComboBox<String> cboRejections;
		/** Accept action */
		private JButton btnAccept;
		/** Reject action */
		private JButton btnReject;
		/** Cancel action */
		private JButton btnCancel;
		/** Current {@link Requirement}'s id */
		private int reqId;
		
		/**
		 * Constructs the JPanel for editing a {@link Requirement} in the UnconfirmedState.
		 */
		public SubmittedPanel() {
			pnlInfo = new ReqInfoPanel();
			lblPriority = new JLabel("Priority");
			txtPriority = new JTextField(20);
			lblEstimate = new JLabel("Estimate");
			txtEstimate = new JTextField(20);
			lblRejections = new JLabel("Rejection Reason");
			cboRejections = new JComboBox<String>(rejections);
			btnAccept = new JButton("Accept");
			btnReject = new JButton("Reject");
			btnCancel = new JButton("Cancel");
			
			btnAccept.addActionListener(this);
			btnReject.addActionListener(this);
			btnCancel.addActionListener(this);
			
			setLayout(new GridLayout(3, 1));
			add(pnlInfo);
			JPanel pnlData = new JPanel();
			pnlData.setLayout(new GridLayout(3, 2));
			pnlData.add(lblPriority);
			pnlData.add(txtPriority);
			pnlData.add(lblEstimate);
			pnlData.add(txtEstimate);
			pnlData.add(lblRejections);
			pnlData.add(cboRejections);
			add(pnlData);
			JPanel pnlBtnRow = new JPanel();
			pnlBtnRow.setLayout(new GridLayout(1, 3));
			pnlBtnRow.add(btnAccept);
			pnlBtnRow.add(btnReject);
			pnlBtnRow.add(btnCancel);
			add(pnlBtnRow);
			
		}
		
		/**
		 * Set the {@link ReqInfoPanel} with the given requirement data.
		 * @param reqId id of the requirement
		 */
		public void setReqInfo(int reqId) {
			this.reqId = reqId;
			pnlInfo.setReqInfo(this.reqId);
		}

		/**
		 * Performs an action based on the given {@link ActionEvent}.
		 * @param e user event that triggers an action.
		 */
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == btnAccept) {
				//Try a command.  If command fails, go back to requirement list.
				try {
					Command c = new Command(CommandValue.ACCEPT, null, null, Integer.parseInt(txtPriority.getText()), txtEstimate.getText(), null, null);
					RequirementsTrackerModel.getInstance().executeCommand(reqId, c);
				} catch (NumberFormatException nfe) {
					JOptionPane.showMessageDialog(RequirementsTrackerGUI.this, "Invalid priority");
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(RequirementsTrackerGUI.this, "Invalid command");
				} catch (UnsupportedOperationException uoe) {
					JOptionPane.showMessageDialog(RequirementsTrackerGUI.this, "Invalid state transition");
				} 
			} else if (e.getSource() == btnReject) {
				//Try a command.  If command fails, go back to requirement list.
				try {
					Command c = new Command(CommandValue.REJECT, null, null, NO_PRIORITY, null, null, getRejection(cboRejections.getSelectedIndex()));
					RequirementsTrackerModel.getInstance().executeCommand(reqId, c);
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(RequirementsTrackerGUI.this, "Invalid command");
				} catch (UnsupportedOperationException uoe) {
					JOptionPane.showMessageDialog(RequirementsTrackerGUI.this, "Invalid state transition");
				}
			} 
			//All buttons lead back to list
			cardLayout.show(panel, REQ_LIST_PANEL);
			pnlReqList.updateTable();
			RequirementsTrackerGUI.this.repaint();
			RequirementsTrackerGUI.this.validate();
			//Reset text fields
			txtEstimate.setText("");
			txtPriority.setText("");
		}
		
	}
	
	/**
	 * Inner class that creates the look and behavior for the {@link JPanel} that 
	 * interacts with an new requirement.
	 * 
	 * @author Dr. Sarah Heckman (heckman@csc.ncsu.edu)
	 */
	private class AcceptedPanel extends JPanel implements ActionListener {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		/** {@link ReqInfoPanel} that presents the {@link Requirement}'s information to the user */
		private ReqInfoPanel pnlInfo;
		/** Label for developer */
		private JLabel lblDeveloper;
		/** Text field for developer */
		private JTextField txtDeveloper;
		/** Label for rejections */
		private JLabel lblRejections;
		/** Drop down for rejection reasons */
		private JComboBox<String> cboRejections;
		/** Assign action */
		private JButton btnAssign;
		/** Reject action */
		private JButton btnReject;
		/** Cancel action */
		private JButton btnCancel;
		/** Current {@link Requirement}'s id */
		private int reqId;
		
		/**
		 * Constructs the JPanel for editing a {@link Requirement} in the NewState.
		 */
		public AcceptedPanel() {
			pnlInfo = new ReqInfoPanel();
			lblDeveloper = new JLabel("Developer");
			txtDeveloper = new JTextField(20);
			lblRejections = new JLabel("Rejection Reason");
			cboRejections = new JComboBox<String>(rejections);
			btnAssign = new JButton("Assign");
			btnReject = new JButton("Reject");
			btnCancel = new JButton("Cancel");
			
			btnAssign.addActionListener(this);
			btnReject.addActionListener(this);
			btnCancel.addActionListener(this);
			
			setLayout(new GridLayout(3, 1));
			add(pnlInfo);
			JPanel pnlData = new JPanel();
			pnlData.setLayout(new GridLayout(2, 2));
			pnlData.add(lblDeveloper);
			pnlData.add(txtDeveloper);
			pnlData.add(lblRejections);
			pnlData.add(cboRejections);
			add(pnlData);
			JPanel pnlBtnRow = new JPanel();
			pnlBtnRow.setLayout(new GridLayout(1, 3));
			pnlBtnRow.add(btnAssign);
			pnlBtnRow.add(btnReject);
			pnlBtnRow.add(btnCancel);
			add(pnlBtnRow);
		}
		
		/**
		 * Set the {@link ReqInfoPanel} with the given requirement data.
		 * @param reqId id of the requirement
		 */
		public void setReqInfo(int reqId) {
			this.reqId = reqId;
			pnlInfo.setReqInfo(this.reqId);
		}

		/**
		 * Performs an action based on the given {@link ActionEvent}.
		 * @param e user event that triggers an action.
		 */
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == btnAssign) {
				//Try a command.  If command fails, go back to requirement list.
				try {
					Command c = new Command(CommandValue.ASSIGN, null, null, NO_PRIORITY, null, txtDeveloper.getText(), null);
					RequirementsTrackerModel.getInstance().executeCommand(reqId, c);
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(RequirementsTrackerGUI.this, "Invalid command");
				} catch (UnsupportedOperationException uoe) {
					JOptionPane.showMessageDialog(RequirementsTrackerGUI.this, "Invalid state transition");
				}
			} else if (e.getSource() == btnReject) {
				//Try a command.  If command fails, go back to requirement list.
				try {
					Command c = new Command(CommandValue.REJECT, null, null, NO_PRIORITY, null, null, getRejection(cboRejections.getSelectedIndex()));
					RequirementsTrackerModel.getInstance().executeCommand(reqId, c);
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(RequirementsTrackerGUI.this, "Invalid command");
				} catch (UnsupportedOperationException uoe) {
					JOptionPane.showMessageDialog(RequirementsTrackerGUI.this, "Invalid state transition");
				}
			} 
			//All buttons lead back to list
			cardLayout.show(panel, REQ_LIST_PANEL);
			pnlReqList.updateTable();
			RequirementsTrackerGUI.this.repaint();
			RequirementsTrackerGUI.this.validate();
			//Reset text field
			txtDeveloper.setText("");
		}
		
	}
	
	/**
	 * Inner class that creates the look and behavior for the {@link JPanel} that 
	 * interacts with an working requirement.
	 * 
	 * @author Dr. Sarah Heckman (heckman@csc.ncsu.edu)
	 */
	private class WorkingPanel extends JPanel implements ActionListener {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		/** {@link ReqInfoPanel} that presents the {@link Requirement}'s information to the user */
		private ReqInfoPanel pnlInfo;
		/** Label for rejections */
		private JLabel lblRejections;
		/** Drop down for rejection reasons */
		private JComboBox<String> cboRejections;
		/** Complete action */
		private JButton btnComplete;
		/** Reject action */
		private JButton btnReject;
		/** Cancel action */
		private JButton btnCancel;
		/** Current {@link Requirement}'s id */
		private int reqId;
		
		/**
		 * Constructs the JPanel for editing a {@link Requirement} in the NewState.
		 */
		public WorkingPanel() {
			pnlInfo = new ReqInfoPanel();
			lblRejections = new JLabel("Rejection Reason");
			cboRejections = new JComboBox<String>(rejections);
			btnComplete = new JButton("Complete");
			btnReject = new JButton("Reject");
			btnCancel = new JButton("Cancel");
			
			btnComplete.addActionListener(this);
			btnReject.addActionListener(this);
			btnCancel.addActionListener(this);
			
			setLayout(new GridLayout(3, 1));
			add(pnlInfo);
			JPanel pnlData = new JPanel();
			pnlData.setLayout(new GridLayout(1, 2));
			pnlData.add(lblRejections);
			pnlData.add(cboRejections);
			add(pnlData);
			JPanel pnlBtnRow = new JPanel();
			pnlBtnRow.setLayout(new GridLayout(1, 3));
			pnlBtnRow.add(btnComplete);
			pnlBtnRow.add(btnReject);
			pnlBtnRow.add(btnCancel);
			add(pnlBtnRow);
		}
		
		/**
		 * Set the {@link ReqInfoPanel} with the given requirement data.
		 * @param reqId id of the requirement
		 */
		public void setReqInfo(int reqId) {
			this.reqId = reqId;
			pnlInfo.setReqInfo(this.reqId);
		}

		/**
		 * Performs an action based on the given {@link ActionEvent}.
		 * @param e user event that triggers an action.
		 */
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == btnComplete) {
				//Try a command.  If command fails, go back to requirement list.
				try {
					Command c = new Command(CommandValue.COMPLETE, null, null, NO_PRIORITY, null, null, null);
					RequirementsTrackerModel.getInstance().executeCommand(reqId, c);
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(RequirementsTrackerGUI.this, "Invalid command");
				} catch (UnsupportedOperationException uoe) {
					JOptionPane.showMessageDialog(RequirementsTrackerGUI.this, "Invalid state transition");
				}
			} else if (e.getSource() == btnReject) {
				//Try a command.  If command fails, go back to requirement list.
				try {
					Command c = new Command(CommandValue.REJECT, null, null, NO_PRIORITY, null, null, getRejection(cboRejections.getSelectedIndex()));
					RequirementsTrackerModel.getInstance().executeCommand(reqId, c);
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(RequirementsTrackerGUI.this, "Invalid command");
				} catch (UnsupportedOperationException uoe) {
					JOptionPane.showMessageDialog(RequirementsTrackerGUI.this, "Invalid state transition");
				}
			} 
			//All buttons lead back to list
			cardLayout.show(panel, REQ_LIST_PANEL);
			pnlReqList.updateTable();
			RequirementsTrackerGUI.this.repaint();
			RequirementsTrackerGUI.this.validate();
		}
	}
	
	/**
	 * Inner class that creates the look and behavior for the {@link JPanel} that 
	 * interacts with a completed requirement.
	 * 
	 * @author Dr. Sarah Heckman (heckman@csc.ncsu.edu)
	 */
	private class CompletedPanel extends JPanel implements ActionListener {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		/** {@link ReqInfoPanel} that presents the {@link Requirement}'s information to the user */
		private ReqInfoPanel pnlInfo;
		/** Label for developer */
		private JLabel lblDeveloper;
		/** Text field for developer */
		private JTextField txtDeveloper;
		/** Label for rejections */
		private JLabel lblRejections;
		/** Drop down for rejection reasons */
		private JComboBox<String> cboRejections;
		/** Pass action */
		private JButton btnPass;
		/** Fail action */
		private JButton btnFail;
		/** Assign action */
		private JButton btnAssign;
		/** Reject action */
		private JButton btnReject;
		/** Cancel action */
		private JButton btnCancel;
		/** Current {@link Requirement}'s id */
		private int reqId;
		
		/**
		 * Constructs the JPanel for editing a {@link Requirement} in the NewState.
		 */
		public CompletedPanel() {
			pnlInfo = new ReqInfoPanel();
			lblDeveloper = new JLabel("Developer");
			txtDeveloper = new JTextField(20);
			lblRejections = new JLabel("Rejection Reason");
			cboRejections = new JComboBox<String>(rejections);
			btnPass = new JButton("Pass");
			btnFail = new JButton("Fail");
			btnAssign = new JButton("Assign");
			btnReject = new JButton("Reject");
			btnCancel = new JButton("Cancel");
			
			btnPass.addActionListener(this);
			btnFail.addActionListener(this);
			btnAssign.addActionListener(this);
			btnReject.addActionListener(this);
			btnCancel.addActionListener(this);
			
			setLayout(new GridLayout(3, 1));
			add(pnlInfo);
			JPanel pnlData = new JPanel();
			pnlData.setLayout(new GridLayout(2, 2));
			pnlData.add(lblDeveloper);
			pnlData.add(txtDeveloper);
			pnlData.add(lblRejections);
			pnlData.add(cboRejections);
			add(pnlData);
			JPanel pnlBtnRow = new JPanel();
			pnlBtnRow.setLayout(new GridLayout(1, 5));
			pnlBtnRow.add(btnPass);
			pnlBtnRow.add(btnFail);
			pnlBtnRow.add(btnAssign);
			pnlBtnRow.add(btnReject);
			pnlBtnRow.add(btnCancel);
			add(pnlBtnRow);
		}
		
		/**
		 * Set the {@link ReqInfoPanel} with the given requirement data.
		 * @param reqId id of the requirement
		 */
		public void setReqInfo(int reqId) {
			this.reqId = reqId;
			pnlInfo.setReqInfo(this.reqId);
		}

		/**
		 * Performs an action based on the given {@link ActionEvent}.
		 * @param e user event that triggers an action.
		 */
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == btnPass) {
				//Try a command.  If command fails, go back to requirement list.
				try {
					Command c = new Command(CommandValue.PASS, null, null, NO_PRIORITY, null, null, null);
					RequirementsTrackerModel.getInstance().executeCommand(reqId, c);
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(RequirementsTrackerGUI.this, "Invalid command");
				} catch (UnsupportedOperationException uoe) {
					JOptionPane.showMessageDialog(RequirementsTrackerGUI.this, "Invalid state transition");
				}
			} else if (e.getSource() == btnFail) {
				//Try a command.  If command fails, go back to requirement list.
				try {
					Command c = new Command(CommandValue.FAIL, null, null, NO_PRIORITY, null, null, null);
					RequirementsTrackerModel.getInstance().executeCommand(reqId, c);
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(RequirementsTrackerGUI.this, "Invalid command");
				} catch (UnsupportedOperationException uoe) {
					JOptionPane.showMessageDialog(RequirementsTrackerGUI.this, "Invalid state transition");
				}
			} else if (e.getSource() == btnAssign) {
				try {
					Command c = new Command(CommandValue.ASSIGN, null, null, NO_PRIORITY, null, txtDeveloper.getText(), null);
					RequirementsTrackerModel.getInstance().executeCommand(reqId, c);
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(RequirementsTrackerGUI.this, "Invalid command");
				} catch (UnsupportedOperationException uoe) {
					JOptionPane.showMessageDialog(RequirementsTrackerGUI.this, "Invalid state transition");
				}
			} else if (e.getSource() == btnReject) {
				//Try a command.  If command fails, go back to requirement list.
				try {
					Command c = new Command(CommandValue.REJECT, null, null, NO_PRIORITY, null, null, getRejection(cboRejections.getSelectedIndex()));
					RequirementsTrackerModel.getInstance().executeCommand(reqId, c);
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(RequirementsTrackerGUI.this, "Invalid command");
				} catch (UnsupportedOperationException uoe) {
					JOptionPane.showMessageDialog(RequirementsTrackerGUI.this, "Invalid state transition");
				}
			} 
			//All buttons lead back to list
			cardLayout.show(panel, REQ_LIST_PANEL);
			pnlReqList.updateTable();
			RequirementsTrackerGUI.this.repaint();
			RequirementsTrackerGUI.this.validate();
			//Reset text field
			txtDeveloper.setText("");
		}
	}
	
	/**
	 * Inner class that creates the look and behavior for the {@link JPanel} that 
	 * interacts with a verified requirement.
	 * 
	 * @author Dr. Sarah Heckman (heckman@csc.ncsu.edu)
	 */
	private class VerifiedPanel extends JPanel implements ActionListener {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		/** {@link ReqInfoPanel} that presents the {@link Requirement}'s information to the user */
		private ReqInfoPanel pnlInfo;
		/** Label for developer */
		private JLabel lblDeveloper;
		/** Text field for developer */
		private JTextField txtDeveloper;
		/** Label for rejections */
		private JLabel lblRejections;
		/** Drop down for rejection reasons */
		private JComboBox<String> cboRejections;
		/** Assign action */
		private JButton btnAssign;
		/** Reject action */
		private JButton btnReject;
		/** Cancel action */
		private JButton btnCancel;
		/** Current {@link Requirement}'s id */
		private int reqId;
		
		/**
		 * Constructs the JPanel for editing a {@link Requirement} in the NewState.
		 */
		public VerifiedPanel() {
			pnlInfo = new ReqInfoPanel();
			lblDeveloper = new JLabel("Developer");
			txtDeveloper = new JTextField(20);
			lblRejections = new JLabel("Rejection Reason");
			cboRejections = new JComboBox<String>(rejections);
			btnAssign = new JButton("Assign");
			btnReject = new JButton("Reject");
			btnCancel = new JButton("Cancel");
			
			btnAssign.addActionListener(this);
			btnReject.addActionListener(this);
			btnCancel.addActionListener(this);
			
			setLayout(new GridLayout(3, 1));
			add(pnlInfo);
			JPanel pnlData = new JPanel();
			pnlData.setLayout(new GridLayout(2, 2));
			pnlData.add(lblDeveloper);
			pnlData.add(txtDeveloper);
			pnlData.add(lblRejections);
			pnlData.add(cboRejections);
			add(pnlData);
			JPanel pnlBtnRow = new JPanel();
			pnlBtnRow.setLayout(new GridLayout(1, 3));
			pnlBtnRow.add(btnAssign);
			pnlBtnRow.add(btnReject);
			pnlBtnRow.add(btnCancel);
			add(pnlBtnRow);
		}
		
		/**
		 * Set the {@link ReqInfoPanel} with the given requirement data.
		 * @param reqId id of the requirement
		 */
		public void setReqInfo(int reqId) {
			this.reqId = reqId;
			pnlInfo.setReqInfo(this.reqId);
		}

		/**
		 * Performs an action based on the given {@link ActionEvent}.
		 * @param e user event that triggers an action.
		 */
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == btnAssign) {
				//Try a command.  If command fails, go back to requirement list.
				try {
					Command c = new Command(CommandValue.ASSIGN, null, null, NO_PRIORITY, null, txtDeveloper.getText(), null);
					RequirementsTrackerModel.getInstance().executeCommand(reqId, c);
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(RequirementsTrackerGUI.this, "Invalid command");
				} catch (UnsupportedOperationException uoe) {
					JOptionPane.showMessageDialog(RequirementsTrackerGUI.this, "Invalid state transition");
				}
			} else if (e.getSource() == btnReject) {
				//Try a command.  If command fails, go back to requirement list.
				try {
					Command c = new Command(CommandValue.REJECT, null, null, NO_PRIORITY, null, null, getRejection(cboRejections.getSelectedIndex()));
					RequirementsTrackerModel.getInstance().executeCommand(reqId, c);
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(RequirementsTrackerGUI.this, "Invalid command");
				} catch (UnsupportedOperationException uoe) {
					JOptionPane.showMessageDialog(RequirementsTrackerGUI.this, "Invalid state transition");
				}
			} 
			//All buttons lead back to list
			cardLayout.show(panel, REQ_LIST_PANEL);
			pnlReqList.updateTable();
			RequirementsTrackerGUI.this.repaint();
			RequirementsTrackerGUI.this.validate();
			//Reset text field
			txtDeveloper.setText("");
		}
	}
	
	/**
	 * Inner class that creates the look and behavior for the {@link JPanel} that 
	 * interacts with a rejected requirement.
	 * 
	 * @author Dr. Sarah Heckman (heckman@csc.ncsu.edu)
	 */
	private class RejectedPanel extends JPanel implements ActionListener {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		/** {@link ReqInfoPanel} that presents the {@link Requirement}'s information to the user */
		private ReqInfoPanel pnlInfo;
		/** Label for summary */
		private JLabel lblSummary;
		/** Text field for summary */
		private JTextField txtSummary;
		/** Label for acceptanceTestId */
		private JLabel lblAcceptanceTestId;
		/** Text field for acceptanceTestId */
		private JTextField txtAcceptanceTestId;
		/** Revise action */
		private JButton btnRevise;
		/** Cancel action */
		private JButton btnCancel;
		/** Current {@link Requirement}'s id */
		private int reqId;
		
		/**
		 * Constructs the JPanel for editing a {@link Requirement} in the NewState.
		 */
		public RejectedPanel() {
			pnlInfo = new ReqInfoPanel();
			lblSummary = new JLabel("Summary");
			txtSummary = new JTextField(20);
			lblAcceptanceTestId = new JLabel("Acceptance Test ID");
			txtAcceptanceTestId = new JTextField(20);
			btnRevise = new JButton("Revise");
			btnCancel = new JButton("Cancel");
			
			btnRevise.addActionListener(this);
			btnCancel.addActionListener(this);
			
			setLayout(new GridLayout(3, 1));
			add(pnlInfo);
			JPanel pnlData = new JPanel();
			pnlData.setLayout(new GridLayout(2, 2));
			pnlData.add(lblSummary);
			pnlData.add(txtSummary);
			pnlData.add(lblAcceptanceTestId);
			pnlData.add(txtAcceptanceTestId);
			add(pnlData);
			JPanel pnlBtnRow = new JPanel();
			pnlBtnRow.setLayout(new GridLayout(1, 2));
			pnlBtnRow.add(btnRevise);
			pnlBtnRow.add(btnCancel);
			add(pnlBtnRow);
		}
		
		/**
		 * Set the {@link ReqInfoPanel} with the given requirement data.
		 * @param reqId id of the requirement
		 */
		public void setReqInfo(int reqId) {
			this.reqId = reqId;
			pnlInfo.setReqInfo(this.reqId);
			Requirement r = RequirementsTrackerModel.getInstance().getRequirementById(reqId);
			if (r == null) {
				txtSummary.setText("");
				txtAcceptanceTestId.setText("");
			} else {
				txtSummary.setText(r.getSummary());
				txtAcceptanceTestId.setText(r.getAcceptanceTestId());
			}
			RequirementsTrackerGUI.this.repaint();
			RequirementsTrackerGUI.this.validate();
		}

		/**
		 * Performs an action based on the given {@link ActionEvent}.
		 * @param e user event that triggers an action.
		 */
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == btnRevise) {
				//Try a command.  If command fails, go back to requirement list.
				try {
					Command c = new Command(CommandValue.REVISE, txtSummary.getText(), txtAcceptanceTestId.getText(), NO_PRIORITY, null, null, null);
					RequirementsTrackerModel.getInstance().executeCommand(reqId, c);
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(RequirementsTrackerGUI.this, "Invalid command");
				} catch (UnsupportedOperationException uoe) {
					JOptionPane.showMessageDialog(RequirementsTrackerGUI.this, "Invalid state transition");
				}
			}  
			//All buttons lead back to list
			cardLayout.show(panel, REQ_LIST_PANEL);
			pnlReqList.updateTable();
			RequirementsTrackerGUI.this.repaint();
			RequirementsTrackerGUI.this.validate();
			//Reset note
			txtSummary.setText("");
		}
		
	}
	
	/**
	 * Inner class that creates the look and behavior for the {@link JPanel} that 
	 * shows information about the requirement.
	 * 
	 * @author Dr. Sarah Heckman (heckman@csc.ncsu.edu)
	 */
	private class ReqInfoPanel extends JPanel {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		/** Label for id */
		private JLabel lblId;
		/** Field for id */
		private JTextField txtId;
		/** Label for state */
		private JLabel lblState;
		/** Field for state */
		private JTextField txtState;
		/** Label for summary */
		private JLabel lblSummary;
		/** Field for summary */
		private JTextArea txtSummary;
		/** Label for acceptanceTestId */
		private JLabel lblAcceptanceTestId;
		/** Field for acceptanceTestId */
		private JTextField txtAcceptanceTestId;
		/** Label for priority */
		private JLabel lblPriority;
		/** Field for priority */
		private JTextField txtPriority;
		/** Label for estimate */
		private JLabel lblEstimate;
		/** Field for estimate */
		private JTextField txtEstimate;
		/** Label for developer */
		private JLabel lblDeveloper;
		/** Field for developer */
		private JTextField txtDeveloper;
		/** Label for rejectionReason */
		private JLabel lblRejectionReason;
		/** Field for rejectionReason */
		private JTextField txtRejectionReason;
		
		/** 
		 * Construct the panel for the requirement information.
		 */
		public ReqInfoPanel() {
			super(new GridLayout(7, 1));
			
			lblId = new JLabel("Requirement Id");
			lblState = new JLabel("Requirement State");
			lblSummary = new JLabel("Requirement Summary");
			lblAcceptanceTestId = new JLabel("Acceptance Test ID");
			lblPriority = new JLabel("Priority");
			lblEstimate = new JLabel("Estimate");
			lblDeveloper = new JLabel("Developer");
			lblRejectionReason = new JLabel("Rejection Reason");
			
			txtId = new JTextField(15);
			txtState = new JTextField(15);
			txtSummary = new JTextArea(15, 3);
			txtAcceptanceTestId = new JTextField(15);
			txtPriority = new JTextField(15);
			txtEstimate = new JTextField(15);
			txtDeveloper = new JTextField(15);
			txtRejectionReason = new JTextField(15);
			
			txtId.setEditable(false);
			txtState.setEditable(false);
			txtSummary.setEditable(false);
			txtAcceptanceTestId.setEditable(false);
			txtPriority.setEditable(false);
			txtEstimate.setEditable(false);
			txtDeveloper.setEditable(false);
			txtRejectionReason.setEditable(false);
			
			JScrollPane summaryScrollPane = new JScrollPane(txtSummary);
			
			//Row 1 - ID and State
			JPanel row1 = new JPanel();
			row1.setLayout(new GridLayout(1, 4));
			row1.add(lblId);
			row1.add(txtId);
			row1.add(lblState);
			row1.add(txtState);
			add(row1);
			//Row 2 - Summary 
			add(lblSummary);
			//Row 3 - Summary text area
			add(summaryScrollPane);
			//Row 4 - Acceptance Test ID & Estimate
			JPanel row4 = new JPanel();
			row4.setLayout(new GridLayout(1, 2));
			row4.add(lblAcceptanceTestId);
			row4.add(txtAcceptanceTestId);
			add(row4);
			JPanel row5 = new JPanel();
			row5.setLayout(new GridLayout(1, 4));
			row5.add(lblPriority);
			row5.add(txtPriority);
			row5.add(lblEstimate);
			row5.add(txtEstimate);
			add(row5);
			//Row 5 - Developer & Rejection Reason
			JPanel row6 = new JPanel();
			row6.setLayout(new GridLayout(1, 4));
			row6.add(lblDeveloper);
			row6.add(txtDeveloper);
			row6.add(lblRejectionReason);
			row6.add(txtRejectionReason);
			add(row6);
		}
		
		/**
		 * Adds information about the requirement to the display.  
		 * @param reqId the id for the requirement to display information about.
		 */
		public void setReqInfo(int reqId) {
			//Get the requirement from the model
			Requirement r = RequirementsTrackerModel.getInstance().getRequirementById(reqId);
			if (r == null) {
				//If the requirement doesn't exist for the given id, show an error message
				JOptionPane.showMessageDialog(RequirementsTrackerGUI.this, "Invalid requirement id");
				cardLayout.show(panel, REQ_LIST_PANEL);
				RequirementsTrackerGUI.this.repaint();
				RequirementsTrackerGUI.this.validate();
			} else {
				//Otherwise, set all of the fields with the information
				txtId.setText("" + r.getRequirementId());
				txtState.setText(r.getState().getStateName());
				txtSummary.setText(r.getSummary());
				txtAcceptanceTestId.setText(r.getAcceptanceTestId());
				txtPriority.setText("" + r.getPriority());
				if (r.getEstimate() == null) {
					txtEstimate.setText("");
				} else {
					txtEstimate.setText(r.getEstimate());
				}
				if (r.getDeveloper() == null) {
					txtDeveloper.setText(""); 
				} else {
					txtDeveloper.setText(r.getDeveloper());
				}
				String resolutionString = r.getRejectionReasonString();
				if (resolutionString == null) {
					txtRejectionReason.setText("");
				} else {
					txtRejectionReason.setText("" + r.getRejectionReasonString());
				}
			}
		}
	}
	
	/**
	 * Inner class that creates the look and behavior for the {@link JPanel} that 
	 * allows for creation of a new requirement.
	 * 
	 * @author Dr. Sarah Heckman (heckman@csc.ncsu.edu)
	 */
	private class CreateReqPanel extends JPanel implements ActionListener {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		/** Label for identifying summary text field */
		private JLabel lblSummary;
		/** Text field for entering summary information */
		private JTextArea txtSummary;
		/** Label for identifying acceptanceTestId text field */
		private JLabel lblAcceptanceTestId;
		/** Text field for entering acceptanceTestId */
		private JTextField txtAcceptanceTestId;
		/** Button to add a requirement */
		private JButton btnAdd;
		/** Button for canceling add action */
		private JButton btnCancel;
		
		/**
		 * Creates the {@link JPanel} for adding new requirements to the 
		 * tracker.
		 */
		public CreateReqPanel() {
			super(new GridLayout(3, 1));  //Creates 3 row, 1 col grid
			
			//Construct widgets
			lblSummary = new JLabel("Requirement Summary");
			txtSummary = new JTextArea(5, 30);
			lblAcceptanceTestId = new JLabel("Requirement Acceptance Test Id");
			txtAcceptanceTestId = new JTextField(30);
			btnAdd = new JButton("Add Requirement to List");
			btnCancel = new JButton("Cancel");
			
			//Adds action listeners
			btnAdd.addActionListener(this);
			btnCancel.addActionListener(this);
			
			//Builds summary panel, which is a 2 row, 1 col grid
			JPanel pnlSummary = new JPanel();
			pnlSummary.setLayout(new GridLayout(2, 1));
			pnlSummary.add(lblSummary);
			pnlSummary.add(txtSummary);
			
			//Builds reporter panel, which is a 1 row, 2 col grid
			JPanel pnlReporter = new JPanel();
			pnlReporter.setLayout(new GridLayout(1, 2));
			pnlReporter.add(lblAcceptanceTestId);
			pnlReporter.add(txtAcceptanceTestId);
			
			//Build button panel, which is a 1 row, 2 col grid
			JPanel pnlButtons = new JPanel();
			pnlButtons.setLayout(new GridLayout(1, 2));
			pnlButtons.add(btnAdd);
			pnlButtons.add(btnCancel);
			
			//Adds all panels to main panel
			add(pnlSummary);
			add(pnlReporter);
			add(pnlButtons);
		}

		/**
		 * Performs an action based on the given {@link ActionEvent}.
		 * @param e user event that triggers an action.
		 */
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == btnAdd) {
				//Add requirement to the list
				String summary = txtSummary.getText();
				String reporter = txtAcceptanceTestId.getText();
				//Get instance of model and add requirement
				RequirementsTrackerModel.getInstance().addRequirement(summary, reporter);
			} 
			//All buttons lead to back requirement list
			cardLayout.show(panel, REQ_LIST_PANEL);
			pnlReqList.updateTable();
			RequirementsTrackerGUI.this.repaint();
			RequirementsTrackerGUI.this.validate();
			//Reset fields
			txtSummary.setText("");
			txtAcceptanceTestId.setText("");
		}
	}
	
	/**
	 * Returns the Rejection enum from the rejections array index
	 * @param idx in rejections array
	 * @return the Rejection enum value
	 */
	private Rejection getRejection(int idx) {
		switch (idx) {
			case 0: return Rejection.DUPLICATE;
			case 1: return Rejection.INFEASIBLE;
			case 2: return Rejection.TOO_LARGE;
			case 3: return Rejection.OUT_OF_SCOPE;
			case 4: return Rejection.INAPPROPRIATE;
			default: return null;	
		}
	}
}
