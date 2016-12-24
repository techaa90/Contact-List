package main;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.xmlbeans.StringEnumAbstractBase.Table;

public class ContactListMain extends JFrame implements ActionListener {
	static int tableCount=0;
	JTable table;
	JFrame New_open;
	String[] columnNames;
	int value = 0;
	JButton go;
	int j = 5;
	int i = 0;
	JButton Edit;
	int rowCnt = 0;
	ContactList con;
	int row;
	int column;
	int data;
	JButton add1;
	FileWriter fw;
	BufferedWriter bw;
	File file;
	JFileChooser fileChooser;
	String filename="";
	JDialog jd=null;
	String name;
	TableRowSorter<MyTableModel> serchh;
	final static String ADDACTION="1", DELETEACTION="2",EDITACTION="3";
	String latestAction="";
	ArrayList<String> mycontactBackup = new ArrayList<String>();
	static Locale currentLocale=new Locale("en","US");
	ResourceBundle message=null;
	String [] fontsArray=new String[]{"San serifs","Verdana"};
	Font currentFont=null;
	private JComboBox<String> jcb=null;
	private JSpinner js=null;
	public void undoAdd(){
		mycontactBackup.add(mycontact.get(mycontact.size()-1));
		mycontact.remove(mycontact.size()-1);
		resetTable();
	}
	public void redoAdd(){
		mycontact.add(mycontactBackup.get(0));
		mycontactBackup.remove(0);
		resetTable();
		
	}
	public void undoDelete(){
		mycontact.add(mycontactBackup.get(0));
		resetTable();
	}
	public void redoDelete(int row){
		String [] contacts=mycontact.get(table.getSelectedRow()).split(",");
		mycontactBackup.remove(0);
		mycontactBackup.add(contacts[0]+","+contacts[1]+","+contacts[2]+","+contacts[3]+","+contacts[4]);
		mycontact.remove(row);
		resetTable();
	}
	public void resetTable(){
		for(int r=0;r<table.getRowCount();r++){
			for(int c=0;c<table.getColumnCount();c++){
				table.setValueAt("", r, c);
				
			}
			if(r<mycontact.size()){
				String [] line=mycontact.get(r).split(",");
				for(int c=0;c<line.length;c++){
					table.setValueAt(line[c], r, c);
				}
			}
		}		
	}
	public void undoEdit(int row){
		String [] contacts=mycontactBackup.get(0).split(",");
		mycontactBackup.set(0, mycontact.get(row));
		table.setValueAt(contacts[0], row, 0);
		table.setValueAt(contacts[1], row, 1);
		table.setValueAt(contacts[2], row, 2);
		table.setValueAt(contacts[3], row, 3);
		table.setValueAt(contacts[4], row, 4);
		mycontact.set(row, contacts[0]+","+contacts[1]+","+contacts[2]+","+contacts[3]+","+contacts[4]);
	}
	
	public void redoEdit(int row){
		String [] contacts=mycontactBackup.get(0).split(",");
		mycontactBackup.set(0, mycontact.get(row));
		table.setValueAt(contacts[0], row, 0);
		table.setValueAt(contacts[1], row, 1);
		table.setValueAt(contacts[2], row, 2);
		table.setValueAt(contacts[3], row, 3);
		table.setValueAt(contacts[4], row, 4);
		mycontact.set(row, contacts[0]+","+contacts[1]+","+contacts[2]+","+contacts[3]+","+contacts[4]);
	}
	// Creating an array list to store all the contact info and to help to store
	// them in a file.
	ArrayList<String> mycontact = new ArrayList<String>();

	// Creating Container to contain AWT components.
	Container input = getContentPane();
	public static final int WIDTH = 600;
	public static final int HEIGHT = 280;
	// Declaring the JTextField variables.
	private JTextField firstname1, lastname1, address1, city1, state1, zip1, phone1, email1, search1;
	private File fi;
	private boolean dirty = false;
	private String add;
	private String delete;
	private String edit;
	private String selectedFont="";
	private int selectedFontSize=0;
	private JMenuBar menubar=null;
	JButton AddEntry =null;
	JButton EditEntry=null;
	JButton DeleteEntry=null;
	JButton exit =null;
	public ContactListMain() {
		if(currentLocale.toString().equals("ar_DZ")){
			message=ResourceBundle.getBundle("main.ResourceBundle_ar_DZ",currentLocale);	
		}else if(currentLocale.toString().equals("en_US")){
			
			message=ResourceBundle.getBundle("main.ResourceBundle_en_US",currentLocale);
		}
				

		// Setting size for the window.
		setSize(WIDTH, HEIGHT);

		// Setting Title for the window.
		setTitle(message.getString("title"));

		// calling menu bar function.
		createMenuBar();

		// Setting color background.
		input.setBackground(Color.LIGHT_GRAY);

		// Setting flow layout to arrange components
		input.setLayout(new FlowLayout());

		// Create a Search JLable with text field and Go button.
		JLabel search = new JLabel(message.getString("search"));
		input.add(search);
		search1 = new JTextField(25);
		input.add(search1);
		 go = new JButton(message.getString("go"));
		go.addActionListener(this);
		input.add(go);

		// Create the table with the Dimension
		table = new JTable(new MyTableModel());
		table.setPreferredScrollableViewportSize(new Dimension(500, 120));

		// Create the scroll pane and add the table to it.
		JScrollPane scrollPane = new JScrollPane(table);

		// Add the scroll pane to this panel.
		input.add(scrollPane);

		// Create AddEntry to insert a new contact.
		AddEntry = new JButton(message.getString("addentry"));
		AddEntry.addActionListener(this);
		input.add(AddEntry);
		
		
		// Create EditEntry to update a contact.
		EditEntry = new JButton(message.getString("editentry"));
		EditEntry.addActionListener(this);
		input.add(EditEntry);
		
		
		// Create DeleteEntry to delete contact from table.
		DeleteEntry = new JButton(message.getString("deleteentry"));
		DeleteEntry.addActionListener(this);
		input.add(DeleteEntry);

		// The Exit is to exit :)
		exit = new JButton(message.getString("exit"));
		exit.addActionListener(this);
		input.add(exit);

	}

	// Creating table of 5 columns and 500 row
	// Creating getColumnCount, getRowCount, getColumnName, getValueAt,
	// setValueAt
	// in order to handle the inputs with the table.
	class MyTableModel extends AbstractTableModel {
		String[] columnNames = {message.getString("firstname"), message.getString("lastname"), message.getString("phoneno"), message.getString("email"),message.getString("address") };

		Object[][] data = new Object[500][5];

		public int getColumnCount() {
			return columnNames.length;
		}

		public int getRowCount() {
			return data.length;
		}

		public String getColumnName(int col) {
			return columnNames[col];
		}

		public Object getValueAt(int row, int col) {
			return data[row][col];
		}

		public void setValueAt(Object value, int row, int column) {
			data[row][column] = value;
			fireTableCellUpdated(row, column);
		}

	}

	// This method helps to create a menu bar at the top of the window.
	// This method should do five functions which is New to generate a new
	// table.
	// Opening a contact list file. Saving a file either new or existed file
	// with new changes.
	// Saving as to save a new file in a different place even the old is already
	// exist.
	// About to show a message about the developer or the program.
	JMenu jmFile=null;
	JMenuItem New=null;
	JMenuItem Open=null;
	JMenuItem Save =null;
	JMenuItem SaveAs =null;
	JMenuItem About =null;
	JMenuItem importContacts =null;
	JMenuItem exportContacts =null;
	JMenu jmEdit=null;
	JMenuItem undo=null;
	JMenuItem redo=null;
	JMenu language=null;
	JMenuItem arabic=null;
	JMenuItem english=null;
	JMenu settings=null;
	JMenuItem background=null;
	JMenuItem font=null;
	JMenuItem sort=null;
	private Properties properties=new Properties();
	
	public String getProperty(String property){
		try{
			FileInputStream fis=new FileInputStream(new File("data.properties"));
			properties.load(fis);
			return properties.getProperty(property);
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
		return "";
	}
	private void createMenuBar() {
		
		menubar = new JMenuBar();
		jmFile = new JMenu(message.getString("file"));
		jmFile.setMnemonic(KeyEvent.VK_F);
		New = new JMenuItem(message.getString("new"));
		New.setActionCommand("New");
		New.addActionListener(this);
		
		input.add(New);

		Open = new JMenuItem(message.getString("open"));
		Open.setActionCommand("Open");
		Open.addActionListener(this);
		input.add(Open);

		Save = new JMenuItem(message.getString("save"));
		Save.setActionCommand("Save");
		Save.addActionListener(this);
		input.add(Save);

		SaveAs = new JMenuItem(message.getString("saveas"));
		SaveAs.setActionCommand("Save As");
		SaveAs.addActionListener(this);
		input.add(SaveAs);

		About = new JMenuItem(message.getString("about"));
		About.setActionCommand("About");
		About.addActionListener(this);
		input.add(About);
		importContacts = new JMenuItem(message.getString("importcontacts"));
		importContacts.setActionCommand("Import Contacts");
		importContacts.addActionListener(this);
		
		exportContacts = new JMenuItem(message.getString("exportcontacts"));
		exportContacts.setActionCommand("Export");
		exportContacts.addActionListener(this);


	
		jmFile.add(New);
		jmFile.add(Open);
		jmFile.add(Save);
		jmFile.add(SaveAs);
		jmFile.add(importContacts);
		jmFile.add(exportContacts);
		jmFile.add(About);
		menubar.add(jmFile);
		jmEdit=new JMenu(message.getString("edit"));
		sort=new JMenuItem(message.getString("sort"));
		sort.setActionCommand("sort");
		sort.addActionListener(this);
		undo=new JMenuItem(message.getString("undo"));
		undo.setActionCommand("Undo");
		undo.addActionListener(this);
		redo=new JMenuItem(message.getString("redo"));
		redo.setActionCommand("Redo");
		redo.addActionListener(this);
		
		jmEdit.add(sort);
		jmEdit.add(undo);
		jmEdit.add(redo);
		menubar.add(jmEdit);
		
		language=new JMenu(message.getString("language"));
		
		arabic=new JMenuItem(message.getString("arabic"));
		arabic.setActionCommand("Arabic");
		arabic.addActionListener(this);
		english=new JMenuItem(message.getString("english"));
		english.setActionCommand("English");
		english.addActionListener(this);
		language.add(arabic);
		language.add(english);
		
		menubar.add(language);
		
		settings=new JMenu(message.getString("settings"));
		background=new JMenuItem(message.getString("background"));
		background.setActionCommand("Background");
		background.addActionListener(this);
		font=new JMenuItem(message.getString("font"));
		font.setActionCommand("Font");
		font.addActionListener(this);
		
		settings.add(background);
		settings.add(font);
		menubar.add(settings);
		setJMenuBar(menubar);
	}
	public void setFont(){
		table.setFont(currentFont);
		table.repaint();
		jmFile.setFont(currentFont);
		jmFile.repaint();
		New.setFont(currentFont);
		Open.setFont(currentFont);
		Save.setFont(currentFont);
		SaveAs.setFont(currentFont);
		importContacts.setFont(currentFont);
		exportContacts.setFont(currentFont);
		About.setFont(currentFont);
		jmEdit.setFont(currentFont);
		undo.setFont(currentFont);
		redo.setFont(currentFont);
		language.setFont(currentFont);
		english.setFont(currentFont);
		arabic.setFont(currentFont);
		settings.setFont(currentFont);
		background.setFont(currentFont);
		font.setFont(currentFont);
		AddEntry.setFont(currentFont);
		EditEntry.setFont(currentFont);
		DeleteEntry.setFont(currentFont);
		exit.setFont(currentFont);
	}
	private void logging(String logg) {

		try {

			LogManager logmanager = LogManager.getLogManager();
			Logger logger = Logger.getLogger("");
			Logger log;

			log = Logger.getLogger("");

			logmanager.addLogger(log);
			log.setLevel(Level.INFO);
			log.log(Level.INFO, logg);
			//
			// FileHandler fh;
			// // This block configure the logger with handler and formatter
			// fh = new FileHandler("logging-file.txt");
			// logger.addHandler(fh);
			// logger.setLevel(Level.ALL);
			// logger.log(Level.INFO, logg);
			// SimpleFormatter formatter = new SimpleFormatter();
			// fh.setFormatter(formatter);

		} catch (SecurityException e) {
			e.printStackTrace();
			// } catch (IOException e) {
			// e.printStackTrace();
		}

	}

	// The listener interface to implement action events.
	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
		// here is the buttons that it appears in table.
		// AddEntry to input a new contact information "first name, last name,
		// phone number and email" in table.
		if (e.getSource() == AddEntry ) {
			// Setting a new window for entering inputs to the table.
			New_open = new JFrame();
			New_open.setSize(WIDTH, HEIGHT);
			New_open.setBackground(Color.LIGHT_GRAY);
			New_open.setLayout(new GridLayout(6, 2));

			JLabel firstname = new JLabel(message.getString("firstname"));
			New_open.add(firstname);
			firstname1 = new JTextField(25);
			New_open.add(firstname1);

			JLabel lastname = new JLabel(message.getString("lastname"));
			New_open.add(lastname);
			lastname1 = new JTextField(25);
			New_open.add(lastname1);

			JLabel phone = new JLabel(message.getString("phoneno"));
			New_open.add(phone);
			phone1 = new JTextField(25);
			New_open.add(phone1);

			JLabel email = new JLabel(message.getString("email"));
			New_open.add(email);
			email1 = new JTextField(25);
			New_open.add(email1);

			JLabel address = new JLabel(message.getString("address"));
			New_open.add(address);
			address1 = new JTextField(25);
			New_open.add(address1);
			
			add1 = new JButton(message.getString("Add"));
			add1.addActionListener(this);
			New_open.add(add1);

			JButton cancel = new JButton(message.getString("Cancel"));
			cancel.addActionListener(this);
			New_open.add(cancel);

			// To show the window.
			New_open.setVisible(true);
			// To center the window.
			New_open.setLocationRelativeTo(null);

		}
		// here when the user press on add to add the new contact to table list.
		else if (e.getSource() == add1) {

			if (phone1.getText().matches("-?\\d+(\\,\\d+)?") && email1.getText().matches(
					"(?:(?:\\r\\n)?[ \\t])*(?:(?:(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*))*@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*|(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)*\\<(?:(?:\\r\\n)?[ \\t])*(?:@(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*(?:,@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*)*:(?:(?:\\r\\n)?[ \\t])*)?(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*))*@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*\\>(?:(?:\\r\\n)?[ \\t])*)|(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)*:(?:(?:\\r\\n)?[ \\t])*(?:(?:(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*))*@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*|(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)*\\<(?:(?:\\r\\n)?[ \\t])*(?:@(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*(?:,@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*)*:(?:(?:\\r\\n)?[ \\t])*)?(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*))*@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*\\>(?:(?:\\r\\n)?[ \\t])*)(?:,\\s*(?:(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*))*@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*|(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)*\\<(?:(?:\\r\\n)?[ \\t])*(?:@(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*(?:,@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*)*:(?:(?:\\r\\n)?[ \\t])*)?(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*))*@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*\\>(?:(?:\\r\\n)?[ \\t])*))*)?;\\s*)")) {

				ContactList con = new ContactList(firstname1.getText(), lastname1.getText(), phone1.getText(),
						email1.getText(), address1.getText());
				mycontact.add(firstname1.getText() + "," + lastname1.getText() + "," + phone1.getText() + ","
						+ email1.getText() + "," + address1.getText());
				// Setting the contact info into exact row and column.
				
				table.setValueAt(con.getFirstname1(), rowCnt, 0);
				table.setValueAt(con.getLastname1(), rowCnt, 1);
				table.setValueAt(con.getPhone1(), rowCnt, 2);
				table.setValueAt(con.getEmail1(), rowCnt, 3);
				table.setValueAt(con.getAddress1(), rowCnt, 4);
				table.setVisible(true);
				New_open.setLocationRelativeTo(null);

				rowCnt++;

				dirty = true;

				firstname1.setText("");
				lastname1.setText("");
				phone1.setText("");
				email1.setText("");
				address1.setText("");

				logging(message.getString("Add"));
				table.repaint();
				latestAction=ADDACTION;
			} else {
				// Error message dialog.
				JOptionPane.showMessageDialog(New_open, message.getString("massage1"), message.getString("error"),
						JOptionPane.ERROR_MESSAGE);

			}

		}
		// EditEntry is button generates a new Jframe to update existed contact
		// by selecting a specific row or contact.
		else if (e.getSource() == EditEntry) {
			row = table.getSelectedRow();
			if (row != -1 && (String) table.getValueAt(row, 0) != null) {
				New_open = new JFrame();
				New_open.setSize(WIDTH, HEIGHT);
				New_open.setBackground(Color.LIGHT_GRAY);
				New_open.setLayout(new GridLayout(6, 2));

				JLabel firstname = new JLabel(message.getString("firstname"));
				New_open.add(firstname);

				firstname1 = new JTextField(25);
				name = (String) table.getValueAt(row, 0);
				firstname1.setText((String) table.getValueAt(row, 0));
				New_open.add(firstname1);

				JLabel lastname = new JLabel(message.getString("lastname"));
				New_open.add(lastname);
				lastname1 = new JTextField(25);
				lastname1.setText((String) table.getValueAt(row, 1));
				New_open.add(lastname1);

				JLabel phone = new JLabel(message.getString("phoneno"));
				New_open.add(phone);
				phone1 = new JTextField(25);
				phone1.setText((String) table.getValueAt(row, 2));
				New_open.add(phone1);

				JLabel email = new JLabel(message.getString("email"));
				New_open.add(email);
				email1 = new JTextField(25);
				email1.setText((String) table.getValueAt(row, 3));
				New_open.add(email1);

				JLabel address = new JLabel(message.getString("address"));
				New_open.add(address);
				address1 = new JTextField(25);
				address1.setText((String) table.getValueAt(row, 4));
				New_open.add(address1);
				
				Edit = new JButton(message.getString("edit"));
				Edit.addActionListener(this);
				New_open.add(Edit);

				JButton cancel = new JButton(message.getString("Cancel"));
				cancel.addActionListener(this);
				New_open.add(cancel);
				New_open.setVisible(true);
				New_open.setLocationRelativeTo(null);
				mycontactBackup.removeAll(mycontactBackup);
				mycontactBackup.add((String) table.getValueAt(row, 0)+","+(String) table.getValueAt(row, 1)+","+(String) table.getValueAt(row, 2)+","+(String) table.getValueAt(row, 3)+","+(String) table.getValueAt(row, 4));
			} else {
				// Error message dialog.
				JOptionPane.showMessageDialog(New_open,
						message.getString("massage3"), message.getString("error"),
						JOptionPane.ERROR_MESSAGE);
			}

		}
		// When the user press on Edit button to edit an existed contact info.
		else if (e.getSource() == Edit) {
			if (phone1.getText().matches("-?\\d+(\\,\\d+)?") && email1.getText().matches(
					"(?:(?:\\r\\n)?[ \\t])*(?:(?:(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*))*@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*|(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)*\\<(?:(?:\\r\\n)?[ \\t])*(?:@(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*(?:,@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*)*:(?:(?:\\r\\n)?[ \\t])*)?(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*))*@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*\\>(?:(?:\\r\\n)?[ \\t])*)|(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)*:(?:(?:\\r\\n)?[ \\t])*(?:(?:(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*))*@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*|(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)*\\<(?:(?:\\r\\n)?[ \\t])*(?:@(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*(?:,@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*)*:(?:(?:\\r\\n)?[ \\t])*)?(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*))*@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*\\>(?:(?:\\r\\n)?[ \\t])*)(?:,\\s*(?:(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*))*@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*|(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)*\\<(?:(?:\\r\\n)?[ \\t])*(?:@(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*(?:,@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*)*:(?:(?:\\r\\n)?[ \\t])*)?(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*))*@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*\\>(?:(?:\\r\\n)?[ \\t])*))*)?;\\s*)")) {

				

				ContactList con = new ContactList(firstname1.getText(), lastname1.getText(), phone1.getText(),
						email1.getText(), address1.getText());
				// mycontact.add(firstname1.getText() +","+
				// lastname1.getText()+","+phone1.getText()+","+
				// email1.getText()+","+address1.getText());

				// for(int i =0; i<mycontact.get(table.getSelectedRow()); i++){
				//
				// }

				// Setting the updated info into exact row and column.
				
				mycontact.set(row,con.getFirstname1() + "," + con.getLastname1() + "," + con.getPhone1() + ","
						+ con.getEmail1() + "," + con.getAddress1());
				table.setValueAt(con.getFirstname1(), row, 0);
				table.setValueAt(con.getLastname1(), row, 1);
				table.setValueAt(con.getPhone1(), row, 2);
				table.setValueAt(con.getEmail1(), row, 3);
				table.setValueAt(con.getAddress1(), row, 4);
				table.setVisible(true);

				rowCnt--;
				dirty = true;
				firstname1.setText("");
				lastname1.setText("");
				phone1.setText("");
				email1.setText("");
				address1.setText("");
				
				logging(message.getString("edit"));
				latestAction=EDITACTION;
			} else {
				// Error message dialog.
				JOptionPane.showMessageDialog(New_open, message.getString("massage1"), message.getString("error"),
						JOptionPane.ERROR_MESSAGE);

			}

		}
		// first ask the user when it click on the butn delete Onclick.
		// here is to delete a contact from the whole table.
		else if (e.getSource() == DeleteEntry) {
			if (row != -1 && (String) table.getValueAt(row, 0) != null) {
				// Confirmation message dialog by yes or no.
				if (JOptionPane.showConfirmDialog(New_open, message.getString("massage2"),message.getString("title1"),
						0) == 0) {
					String [] contacts=mycontact.get(table.getSelectedRow()).split(",");
					if(mycontactBackup.size()>0)mycontactBackup.remove(0);
					mycontactBackup.add(contacts[0]+","+contacts[1]+","+contacts[2]+","+contacts[3]+","+contacts[4]);
					mycontact.remove(table.getSelectedRow());
					// To delete the selected row or contact.
					// Setting that in a loop to organize and avoid space in
					// table.
					for(int r=0;r<table.getRowCount();r++){
						for(int c=0;c<table.getColumnCount();c++){
							table.setValueAt("", r, c);
							
						}
						if(r<mycontact.size()){
							String [] line=mycontact.get(r).split(",");
							for(int c=0;c<line.length;c++){
								table.setValueAt(line[c], r, c);
							}
						}
					}
//					for (int i = table.getSelectedRow(); i < rowCnt; i++) {
//						table.setValueAt(table.getValueAt(i + 1, 0), i, 0);
//						table.setValueAt(table.getValueAt(i + 1, 1), i, 1);
//						table.setValueAt(table.getValueAt(i + 1, 2), i, 2);
//						table.setValueAt(table.getValueAt(i + 1, 3), i, 3);
//						table.setValueAt(table.getValueAt(i + 1, 4), i, 4);
//
//					}
					rowCnt--;
					logging(message.getString("delete"));
					latestAction=DELETEACTION;
					dirty = true;
				}
			} else {
				// Error message dialog.
				JOptionPane.showMessageDialog(New_open,  message.getString("massage3"),
						 message.getString("error"), JOptionPane.ERROR_MESSAGE);
			}
		}
		// If the user wants to search on the list of contacts by the first
		// name.
		else if (e.getSource() == go) {
//			for (int i = 0; i < table.getRowCount(); i++) {
//				if (search1.getText().equals(table.getValueAt(i, 0))) {
//					// if(search1.getText().startsWith(table.getValueAt(i,0).toString())){
//					table.setRowSelectionInterval(i, i);
//				}
//			}
			TableRowSorter<MyTableModel> serchh = new TableRowSorter<MyTableModel>(((MyTableModel) table.getModel()));
			serchh.setRowFilter(RowFilter.regexFilter(search1.getText().substring(i)));
			table.setRowSorter(serchh);
			// Dispose the specific Jframe.
		}else if (e.getSource() == sort) {
			serchh = new TableRowSorter<MyTableModel>(((MyTableModel) table.getModel()));
			table.setRowSorter(serchh);
			ArrayList<RowSorter.SortKey> sort2 =new ArrayList<>();
			int dd=0;
			sort2.add(new RowSorter.SortKey(dd, SortOrder.DESCENDING));
			serchh.setSortKeys(sort2);
		}
		
		else if (actionCommand.equals("Cancel")) {
			New_open.dispose();

		}
		else if (actionCommand.equals("elgaa")) {
			New_open.dispose();

		}
		// New function is to generate a new contact list.
		else if (actionCommand.equals("New")) {

			// The program should ask the user if he wants to save the contact
			// file before creating a new one.
			if (dirty == true) {
				if (JOptionPane.showConfirmDialog(New_open, message.getString("massage4"), message.getString("title2"),
						0) == 0) {
					JFileChooser jf = new JFileChooser();
					int reslt = jf.showSaveDialog(New_open);
					if (reslt == JFileChooser.APPROVE_OPTION) {
						try {
							File fi = jf.getSelectedFile();
							fw = new FileWriter(fi.getAbsoluteFile());
							bw = new BufferedWriter(fw);
							for (int i = 0; i < mycontact.size(); i++) {
								bw.append(mycontact.get(i));
								/*
								 * for (int j = 0; j < table.getColumnCount();
								 * j++) {
								 * bw.write(table.getModel().getValueAt(row,
								 * j)+","); }
								 */
								bw.append("\n");
								bw.flush();
							}
							bw.close();
							fw.close();
							/////////
							/////////
							/////////
							JOptionPane.showMessageDialog(New_open,  message.getString("massage5"));
						} catch (IOException e1) {
							JOptionPane.showMessageDialog(New_open, message.getString("massage6"), message.getString("error"),
									JOptionPane.ERROR_MESSAGE);
						}
					}
					for (int i = 0; i < table.getRowCount(); i++) {
						table.setValueAt(null, i, 0);
						table.setValueAt(null, i, 1);
						table.setValueAt(null, i, 2);
						table.setValueAt(null, i, 3);
						table.setValueAt(null, i, 4);
						for (int j = 0; j < rowCnt; j++) {
							rowCnt--;

						}
					}

				} else {
					for (int i = 0; i < table.getRowCount(); i++) {
						table.setValueAt(null, i, 0);
						table.setValueAt(null, i, 1);
						table.setValueAt(null, i, 2);
						table.setValueAt(null, i, 3);
						table.setValueAt(null, i, 4);
						for (int j = 0; j < rowCnt; j++) {
							rowCnt--;

						}

					}
				}
			}
		}
		// opening an existed contact list file.
		else if (actionCommand.equals("Open")) {
			// JFileChooser is to provide a simple mechanism for the user to
			// choose a file.
			JFileChooser jf = new JFileChooser(new File("c:\\"));
			jf.setDialogTitle("Open a File");
			jf.setFileFilter(new FileNameExtensionFilter("*.txt", "txt"));
			int result = jf.showOpenDialog(null);
			if (result == JFileChooser.APPROVE_OPTION) {
				File fi = jf.getSelectedFile();
				if(tableCount==0){
					try {
						
						// to read the file
						filename = fi.getPath();
						BufferedReader br = new BufferedReader(new FileReader(fi.getPath()));
						String line = "";
						String[] s;
						int row = 0;
						while ((line = br.readLine()) != null) {
							mycontact.add(line);
							s = line.split(",");
							for (int i = 0; i < s.length; i++) {
								table.setValueAt(s[i], row, i);
							}
							row++;
						}

						br.close();
					} catch (Exception e2) {
						// throw an exception.
						e2.printStackTrace();
						JOptionPane.showMessageDialog(null, e2.getMessage());

					}
				}
				else{
					ContactListMain gui = new ContactListMain();
					gui.setVisible(true);
					gui.setLocation(0,0);
					gui.openTable(fi);
				}
				tableCount++;
			}

		}
		// Saving a file either new or existed file with new changes.
		else if (actionCommand.equals("Save")) {
					File file=new File(filename);
					if(!file.exists()){
						JFileChooser jf = new JFileChooser();
						int reslt = jf.showSaveDialog(New_open);
						if (reslt == JFileChooser.APPROVE_OPTION) {
							try {
								File fi = jf.getSelectedFile();
								fw = new FileWriter(fi.getAbsoluteFile());
								filename=fi.getAbsolutePath();
								bw = new BufferedWriter(fw);
								for (int i = 0; i < mycontact.size(); i++) {
									bw.append(mycontact.get(i));
									/*
									 * for (int j = 0; j < table.getColumnCount(); j++) {
									 * bw.write(table.getModel().getValueAt(row, j)+","); }
									 */
									bw.append("\n");
									bw.flush();
								}
								bw.close();
								fw.close();
								
								JOptionPane.showMessageDialog(New_open, message.getString("massage5"));
							} catch (IOException e1) {
								JOptionPane.showMessageDialog(New_open, message.getString("massage6"), message.getString("error"),
										JOptionPane.ERROR_MESSAGE);
							}
						}

					}else{
						try {
							File fi =file;
							fw = new FileWriter(fi.getAbsoluteFile());
							filename=fi.getAbsolutePath();
							bw = new BufferedWriter(fw);
							for (int i = 0; i < mycontact.size(); i++) {
								bw.append(mycontact.get(i));
								/*
								 * for (int j = 0; j < table.getColumnCount(); j++) {
								 * bw.write(table.getModel().getValueAt(row, j)+","); }
								 */
								bw.append("\n");
								bw.flush();
							}
							bw.close();
							fw.close();
							
							JOptionPane.showMessageDialog(New_open, message.getString("massage5"));
						} catch (IOException e1) {
							JOptionPane.showMessageDialog(New_open,message.getString("massage6"), message.getString("error"),
									JOptionPane.ERROR_MESSAGE);
						}
						
					}
		}

		// Saving as to save a new file in a different place even the old is
		// already exist.
		else if (actionCommand.equals("Save As")) {

			JFileChooser jf = new JFileChooser();
			int reslt = jf.showSaveDialog(New_open);
			if (reslt == JFileChooser.APPROVE_OPTION) {
				try {
					File fi = jf.getSelectedFile();
					fw = new FileWriter(fi.getAbsoluteFile());
					filename=fi.getAbsolutePath();
					bw = new BufferedWriter(fw);
					for (int i = 0; i < mycontact.size(); i++) {
						bw.append(mycontact.get(i));
						/*
						 * for (int j = 0; j < table.getColumnCount(); j++) {
						 * bw.write(table.getModel().getValueAt(row, j)+","); }
						 */
						bw.append("\n");
						bw.flush();
					}
					bw.close();
					fw.close();
					
					JOptionPane.showMessageDialog(New_open, message.getString("massage5"));
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(New_open,  message.getString("massage6"),  message.getString("error"),
							JOptionPane.ERROR_MESSAGE);
				}
			}
		}else if(actionCommand.equals("Undo")){
			
			switch(latestAction){
			case DELETEACTION:
				undoDelete();
				break;
			case EDITACTION:
				undoEdit(row);
				break;
			case ADDACTION:
				undoAdd();
				break;
			}
		}else if(actionCommand.equals("Redo")){
			switch(latestAction){
			case DELETEACTION:
				redoDelete(table.getSelectedRow());
				break;
			case EDITACTION:
				redoEdit(table.getSelectedRow());
				break;
			case ADDACTION:
				redoAdd();
				break;
			}
			
		}else if(actionCommand.equals("Background")){
			Color color=input.getBackground();
			Color background=JColorChooser.showDialog(this, "JColorChooserSample", color);
			if(background!=null){
				input.setBackground(background);
			}
			
		}else if(actionCommand.equals("Font")){
			 jd=new JDialog(this);
			 JPanel jp=new JPanel();
			 jp.setPreferredSize(new Dimension(300,400));
			 jp.setLayout(new FlowLayout(FlowLayout.CENTER,10,10));

			 JLabel jl=new JLabel("Font : ");
			 jl.setPreferredSize(new Dimension(280,30));
			 jp.add(jl);
			 jcb=new JComboBox<String>(fontsArray);
			 jcb.setPreferredSize(new Dimension(240,30));
			 jp.add(jcb);
			 jl=new JLabel("Font Size : ");
			 jl.setPreferredSize(new Dimension(240,30));
			 js=new JSpinner();
			 js.setPreferredSize(new Dimension(240,30));
			 
			 jp.add(jl);
			 jp.add(js);
			 JButton jb=new JButton("Set Font");
			 jb.addActionListener(this);
			 jb.setPreferredSize(new Dimension(240,30));
			 jp.add(jb);
			 jd.setSize(300,400);
			 
			 jd.setLocationRelativeTo(this);
			 jd.add(jp);
			 jd.setVisible(true);
			 
		}else if(actionCommand.equals("Set Font")){
			selectedFont=jcb.getSelectedItem().toString();
			selectedFontSize=(int) js.getValue();
			currentFont=new Font(selectedFont,Font.PLAIN,selectedFontSize);
			this.setFont();
			
			jd.dispose();
			
		}else if(actionCommand.equals("English")){
			currentLocale=new Locale("en","US");
			this.dispose();
			ContactListMain gui = new ContactListMain();
			gui.setVisible(true);
			gui.setLocationRelativeTo(null);

		}else if(actionCommand.equals("Arabic")){
			currentLocale=new Locale("ar","DZ");
			this.dispose();
			ContactListMain gui = new ContactListMain();
			gui.setVisible(true);
			gui.setLocationRelativeTo(null);
			

		}else if(actionCommand.equals("Import")){
			
		}else if(actionCommand.equals("Export")){
			JFileChooser jf = new JFileChooser();
			int reslt = jf.showSaveDialog(New_open);
			if (reslt == JFileChooser.APPROVE_OPTION) {
				try {
					File fi = jf.getSelectedFile();
					XSSFWorkbook workbook=new XSSFWorkbook();
					XSSFSheet sheet=workbook.createSheet("Contacts");
					Row row=null;
					for (int i = 0; i < mycontact.size(); i++) {
						row=sheet.createRow(i);
						String [] data=mycontact.get(i).split(",");
						Cell cell=null;
						for(int c=0;c<data.length;c++){
							cell=row.createCell(c);
							cell.setCellValue(data[c]);
						}
						/*
						 * for (int j = 0; j < table.getColumnCount(); j++) {
						 * bw.write(table.getModel().getValueAt(row, j)+","); }
						 */
						
					}
					FileOutputStream fos=new FileOutputStream(fi);
					workbook.write(fos);
					fos.close();
					JOptionPane.showMessageDialog(New_open,  message.getString("massage5"));
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(New_open,  message.getString("massage6"),  message.getString("error"),
							JOptionPane.ERROR_MESSAGE);
				}
			}
		}else if(actionCommand.equals("Import Contacts")){
			JFileChooser jf = new JFileChooser(new File("c:\\"));
			jf.setDialogTitle("Open a File");
			int result = jf.showOpenDialog(null);
			if (result == JFileChooser.APPROVE_OPTION) {
				File fi = jf.getSelectedFile();
				FileInputStream fis=null;
				try {
					fis = new FileInputStream(fi);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}						
				if(tableCount==0){
					try {
						XSSFWorkbook workbook=new XSSFWorkbook(fis);
						XSSFSheet sheet=workbook.getSheetAt(0);
						Iterator<Row> rowIterator=sheet.iterator();
						// to read the file
						Row row=null;
						int r=0;
						while (rowIterator.hasNext()) {
							row=rowIterator.next();
							Iterator<Cell> cellIterator=row.cellIterator();
							Cell cell=null;
							String line="";
							int c=0;
							while(cellIterator.hasNext()){
								cell=cellIterator.next();
								line+=cell.getStringCellValue()+",";
								table.setValueAt(cell.getStringCellValue(), r,c);
								c++;
							}
							
							mycontact.add(line);
							r++;
						}

					} catch (Exception e2) {
						// throw an exception.
						e2.printStackTrace();
						JOptionPane.showMessageDialog(null, e2.getMessage());

					}
				}
				else{
					ContactListMain gui = new ContactListMain();
					gui.setVisible(true);
					gui.setLocation(0,0);
					gui.importTable(fis);
				}
				tableCount++;
			}
			

		}
		// Providing message dialog with the developer name and ID.
		else if (actionCommand.equals("About")) {
			JOptionPane.showMessageDialog(New_open, "Ahmad Alghamdi s1051443", message.getString("about"), 1);
		} else
			System.exit(0);
			
		
		
	}

	public static void main(String[] args) {
		// Calling ContactListMain class.
		ContactListMain gui = new ContactListMain();
		gui.setVisible(true);
		gui.setLocationRelativeTo(null);
	}
	public void openTable(File file){
		try {
			File fi = file;
			// to read the file
			filename = fi.getPath();
			BufferedReader br = new BufferedReader(new FileReader(fi.getPath()));
			String line = "";
			String[] s;
			int row = 0;
			while ((line = br.readLine()) != null) {
				mycontact.add(line);
				s = line.split(",");
				for (int i = 0; i < s.length; i++) {
					table.setValueAt(s[i], row, i);
				}
				row++;
			}

			br.close();
		} catch (Exception e2) {
			// throw an exception.
			e2.printStackTrace();
			JOptionPane.showMessageDialog(null, e2.getMessage());

		}
		
	}
	public void importTable(FileInputStream fis){
		try {
			XSSFWorkbook workbook=new XSSFWorkbook(fis);
			XSSFSheet sheet=workbook.getSheetAt(0);
			Iterator<Row> rowIterator=sheet.iterator();
			// to read the file
			Row row=null;
			int r=0;
			while (rowIterator.hasNext()) {
				row=rowIterator.next();
				Iterator<Cell> cellIterator=row.cellIterator();
				Cell cell=null;
				String line="";
				int c=0;
				while(cellIterator.hasNext()){
					cell=cellIterator.next();
					line+=cell.getStringCellValue()+",";
					table.setValueAt(cell.getStringCellValue(), r,c);
					c++;
				}
				
				mycontact.add(line);
				r++;
			}

		} catch (Exception e2) {
			// throw an exception.
			e2.printStackTrace();
			JOptionPane.showMessageDialog(null, e2.getMessage());

		}
	}
}
