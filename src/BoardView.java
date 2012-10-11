/*
 * John Brodie
 * jdb356@drexel.edu
 * CS338:GUI, Assignment 3
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

public class BoardView extends JFrame implements Serializable {
	
	private static final long serialVersionUID = -5749346013237583250L;
	Board controller;
	
	/* Menu */
	protected JMenuBar menuBar = new JMenuBar();
	private JMenu fileMenu = new JMenu("File");
	private JMenu editMenu = new JMenu("Edit");
	private JMenu helpMenu = new JMenu("Help");
	protected JMenuItem addCardItem = new JMenuItem("Add Card", KeyEvent.VK_A);
	protected JMenuItem removeCardItem = new JMenuItem("Remove Card", KeyEvent.VK_R);
	protected JMenuItem editCardItem = new JMenuItem("Edit Card", KeyEvent.VK_E);
	protected JMenuItem addColumnItem = new JMenuItem("Add Column", KeyEvent.VK_C);
	protected JMenuItem editColumnItem = new JMenuItem("Edit Column", KeyEvent.VK_D);
	protected JMenuItem removeColumnItem = new JMenuItem("Remove Column", KeyEvent.VK_L);
	protected JMenuItem statsItem = new JMenuItem("Stats", KeyEvent.VK_T);
	protected JMenuItem saveItem = new JMenuItem("Save", KeyEvent.VK_S);
	protected JMenuItem loadItem = new JMenuItem("Load", KeyEvent.VK_L);
	protected JMenuItem exitItem = new JMenuItem("Exit", KeyEvent.VK_X);
	protected JMenuItem helpItem = new JMenuItem("Help", KeyEvent.VK_H);
	protected JMenuItem aboutItem = new JMenuItem("About", KeyEvent.VK_A);
	protected JMenuItem renameBoardItem = new JMenuItem("Rename Board");
	
	/* Panels */
	private JPanel surround = new JPanel(new BorderLayout());
	private JPanel titlePanel = new JPanel();
	JPanel columnPanel = new JPanel(new FlowLayout());
	private JPanel bottomPanel = new JPanel();
	private JPanel leftPanel = new JPanel();
	private JPanel rightPanel = new JPanel();
	private JScrollPane columnScroll = new JScrollPane(columnPanel);
	
	/* Components */
		/* Main screen components */
	JLabel boardNameLabel = new JLabel("Default Name");
	public JButton saveButton= new JButton("Save");
	public JButton exitButton = new JButton("Exit");
	
		/* Add card components */
	public JPanel addCardPanel = new JPanel();
	protected JTextField addCardTitleField = new JTextField("Default Title", 20);
	protected JTextArea descriptionArea = new JTextArea("Default Description");
	protected JTextField creatorField = new JTextField("Default Creator", 25);
	protected JTextField assignedField = new JTextField("Default Assigned", 20);
	private String[] priorityOptions = {"Red", "Green", "Blue", "Yellow"};
	protected JComboBox priorityBox = new JComboBox(priorityOptions);
	DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
	protected JLabel createdField = new JLabel(df.format(new Date()));
	
		/* Add column components */
	protected JPanel addColumnPanel = new JPanel();
	protected JTextField addColumnTitleField = new JTextField("Default Title", 10);
	protected JTextField addColumnWipField = new JFormattedTextField(NumberFormat.getInstance());
	protected JTextField addColumnPosField = new JTextField();
	
		/* Remove column components */
	protected JPanel removeColumnPanel = new JPanel();
	protected JComboBox removeColumnBox = new JComboBox();
	
		/* Rename board components */
	protected JPanel renameBoardPanel = new JPanel();
	protected JTextField renameField = new JTextField();
	
	/**
	 * Constructor for board view
	 * @param board - The controller
	 */
	public BoardView(Board board) {
		this.controller = board;
		initComponents();
	}
	
	/**
	 * Set needed attributes for components
	 */
	public void initComponents() {
		this.setTitle("Visual KanBan Version 1.0");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		/* public domain icon from http://bit.ly/NRxwCf */
		this.setIconImage(new ImageIcon(getClass().getClassLoader().getResource("k.png")).getImage());
		this.setLayout(new GridBagLayout());
		
		columnScroll.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		
		boardNameLabel.setFont(boardNameLabel.getFont().deriveFont(32.0f));
		boardNameLabel.setFont(boardNameLabel.getFont().deriveFont(Font.BOLD));
		titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.LINE_AXIS));
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.LINE_AXIS));
		addCardPanel.setLayout(new BoxLayout(addCardPanel, BoxLayout.PAGE_AXIS));
		addColumnPanel.setLayout(new BoxLayout(addColumnPanel, BoxLayout.PAGE_AXIS));
		
		descriptionArea.setBackground(new Color(252, 244, 170));
		
		priorityBox.setMaximumSize(new Dimension(90, 20));
		priorityBox.setPreferredSize(new Dimension(90, 20));
		creatorField.setPreferredSize(new Dimension(75, 20));
		creatorField.setMaximumSize(new Dimension(75, 20));
		addCardPanel.setMinimumSize(new Dimension(300, 150));
		addCardPanel.setPreferredSize(new Dimension(300, 150));
		addCardPanel.setMaximumSize(new Dimension(300, 150));
		
		descriptionArea.setLineWrap(true);
		
		/* Menu */
		editCardItem.setEnabled(false);
		removeCardItem.setEnabled(false);
		fileMenu.setMnemonic(KeyEvent.VK_F);
		editMenu.setMnemonic(KeyEvent.VK_E);
		helpMenu.setMnemonic(KeyEvent.VK_H);
		exitItem.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_X, ActionEvent.ALT_MASK));
		addCardItem.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_A, ActionEvent.ALT_MASK));
		editCardItem.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_E, ActionEvent.ALT_MASK));
		removeCardItem.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_R, ActionEvent.ALT_MASK));
		statsItem.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_T, ActionEvent.ALT_MASK));
		saveItem.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_S, ActionEvent.ALT_MASK));
		loadItem.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_L, ActionEvent.ALT_MASK));
		addColumnItem.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_C, ActionEvent.ALT_MASK));
		editColumnItem.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_D, ActionEvent.ALT_MASK));
		removeColumnItem.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_L, ActionEvent.ALT_MASK));
		helpItem.setAccelerator(KeyStroke.getKeyStroke("F1"));
		fileMenu.add(statsItem);
		fileMenu.addSeparator();
		fileMenu.add(loadItem);
		fileMenu.add(saveItem);
		fileMenu.add(exitItem);
		
		editMenu.add(addCardItem);
		editMenu.add(editCardItem);
		editMenu.add(removeCardItem);
		editMenu.addSeparator();
		editMenu.add(addColumnItem);
		editMenu.add(editColumnItem);
		editMenu.add(removeColumnItem);
		editMenu.addSeparator();
		editMenu.add(renameBoardItem);
		
		helpMenu.add(helpItem);
		helpMenu.add(aboutItem);
		
		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		menuBar.add(helpMenu);
	}
	
	/**
	 * Layout and display main board
	 */
	public void CreateGUI() {
		controller.update();
		Container pane = getContentPane();
		pane.setLayout(new BorderLayout());
		Dimension buttonDim = new Dimension(8, 0);
		Dimension spacerDim = new Dimension(12, 35);
		
		/* Menu */
		this.setJMenuBar(menuBar);
		
		/* Title (Header) Panel */
		titlePanel.add(Box.createHorizontalGlue());
		titlePanel.add(boardNameLabel);
		titlePanel.add(Box.createHorizontalGlue());
		surround.add(titlePanel, BorderLayout.NORTH);
		
		/* Bottom Panel */
		bottomPanel.add(Box.createHorizontalGlue());
		bottomPanel.add(saveButton);
		bottomPanel.add(Box.createRigidArea(buttonDim));
		bottomPanel.add(exitButton);
		bottomPanel.add(Box.createRigidArea(spacerDim));
		surround.add(bottomPanel, BorderLayout.SOUTH);
		
		/* Add Card Panel */ 
		Dimension addCardDim = new Dimension(10, 8);
		Dimension addSpaceDim = new Dimension(300, 20);
		JPanel tempTitle = new JPanel();
		tempTitle.setPreferredSize(addSpaceDim);
		tempTitle.setMinimumSize(addSpaceDim);
		tempTitle.setMaximumSize(addSpaceDim);
		tempTitle.setLayout(new BoxLayout(tempTitle, BoxLayout.LINE_AXIS));
		tempTitle.add(Box.createHorizontalGlue());
		tempTitle.add(new JLabel("Title:    "));
		tempTitle.add(addCardTitleField);
		tempTitle.add(Box.createHorizontalGlue());
		addCardPanel.add(tempTitle);
		addCardPanel.add(Box.createRigidArea(addCardDim));
		
		addCardPanel.add(descriptionArea);
		addCardPanel.add(Box.createRigidArea(addSpaceDim));
		JPanel tempCreator = new JPanel();
		tempCreator.setPreferredSize(addSpaceDim);
		tempCreator.setMinimumSize(addSpaceDim);
		tempCreator.setMaximumSize(addSpaceDim);
		tempCreator.setLayout(new BoxLayout(tempCreator, BoxLayout.LINE_AXIS));
		tempCreator.add(new JLabel("Creator:  "));
		tempCreator.add(creatorField);
		tempCreator.add(Box.createRigidArea(new Dimension(15, 1)));
		tempCreator.add(new JLabel("Assigned:  "));
		tempCreator.add(assignedField);
		addCardPanel.add(tempCreator);
		addCardPanel.add(Box.createRigidArea(addCardDim));
		
		JPanel tempPriority = new JPanel();
		tempPriority.setPreferredSize(addSpaceDim);
		tempPriority.setMinimumSize(addSpaceDim);
		tempPriority.setMaximumSize(addSpaceDim);
		tempPriority.setLayout(new BoxLayout(tempPriority, BoxLayout.LINE_AXIS));
		tempPriority.add(new JLabel("Priority:   "));
		tempPriority.add(priorityBox);
		tempPriority.add(Box.createRigidArea(new Dimension(20, 1)));
		tempPriority.add(new JLabel("Created:    "));
		tempPriority.add(new JLabel(df.format(new Date())));
		addCardPanel.add(tempPriority);
		addCardPanel.add(Box.createRigidArea(addCardDim));
		
		/* Add Column Panel */
		JPanel tempColTitle = new JPanel();
		tempColTitle.setPreferredSize(addSpaceDim);
		tempColTitle.setMinimumSize(addSpaceDim);
		tempColTitle.setMaximumSize(addSpaceDim);
		tempColTitle.setLayout(new BoxLayout(tempColTitle, BoxLayout.LINE_AXIS));
		tempColTitle.add(Box.createHorizontalGlue());
		tempColTitle.add(new JLabel("Title:           "));
		tempColTitle.add(addColumnTitleField);
		tempColTitle.add(Box.createHorizontalGlue());
		addColumnPanel.add(tempColTitle);
		addColumnPanel.add(Box.createRigidArea(addCardDim));
		
		JPanel tempColNum = new JPanel();
		tempColNum.setPreferredSize(addSpaceDim);
		tempColNum.setMinimumSize(addSpaceDim);
		tempColNum.setMaximumSize(addSpaceDim);
		tempColNum.setLayout(new BoxLayout(tempColNum, BoxLayout.LINE_AXIS));
		tempColNum.add(new JLabel("WIP Limit: "));
		tempColNum.add(addColumnWipField);
		tempColNum.add(Box.createHorizontalGlue());
		addColumnPanel.add(tempColNum);
		addColumnPanel.add(Box.createRigidArea(addCardDim));
		
		JPanel tempColPos = new JPanel();
		tempColPos.setPreferredSize(addSpaceDim);
		tempColPos.setMinimumSize(addSpaceDim);
		tempColPos.setMaximumSize(addSpaceDim);
		tempColPos.setLayout(new BoxLayout(tempColPos, BoxLayout.LINE_AXIS));
		tempColPos.add(new JLabel("Position:    "));
		tempColPos.add(addColumnPosField);
		tempColPos.add(Box.createHorizontalGlue());
		addColumnPanel.add(tempColPos);
		addColumnPanel.add(Box.createRigidArea(addCardDim));
		
		/* Remove Column Panel */
		JPanel tempColRem = new JPanel();
		tempColRem.setPreferredSize(addSpaceDim);
		tempColRem.setMinimumSize(addSpaceDim);
		tempColRem.setMaximumSize(addSpaceDim);
		tempColRem.setLayout(new BoxLayout(tempColRem, BoxLayout.LINE_AXIS));
		tempColRem.add(Box.createHorizontalGlue());
		tempColRem.add(new JLabel("Column: "));
		tempColRem.add(removeColumnBox);
		removeColumnPanel.add(tempColRem);
		
		/* Rename Board Panel */
		JPanel tempBorPan = new JPanel();
		tempBorPan.setPreferredSize(addSpaceDim);
		tempBorPan.setMinimumSize(addSpaceDim);
		tempBorPan.setMaximumSize(addSpaceDim);
		tempBorPan.setLayout(new BoxLayout(tempBorPan, BoxLayout.LINE_AXIS));
		tempBorPan.add(Box.createHorizontalGlue());
		tempBorPan.add(new JLabel("New board name: "));
		tempBorPan.add(renameField);
		tempBorPan.add(Box.createHorizontalGlue());
		renameBoardPanel.add(tempBorPan);
		
		/* Left and Right (Spacer) Panels */
		surround.add(rightPanel, BorderLayout.EAST);
		surround.add(leftPanel, BorderLayout.WEST);
		
		surround.add(columnScroll, BorderLayout.CENTER);
		
		pane.add(surround, BorderLayout.CENTER);
		pack();
		this.setExtendedState(MAXIMIZED_BOTH);
	}
	
	/**
	 * Override processWindowEvent so we can catch a user closing
	 * the window and display "are you sure" prompt
	 */
    protected void processWindowEvent(WindowEvent e) {

        if (e.getID() == WindowEvent.WINDOW_CLOSING)
        	controller.showExitDialog();
        else
            super.processWindowEvent(e);
    }
}