import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.SystemColor;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Font;
import java.awt.Frame;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import com.toedter.calendar.JDateChooser;

public class INSERT extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JDateChooser dateChooser = new JDateChooser();
	private final JRadioButton rdbtnMale = new JRadioButton("Male");
	private final JRadioButton rdbtnFemale = new JRadioButton("Female");
	private final JRadioButton rdbtnOther = new JRadioButton("Other");
	JComboBox<String> comboBox = new JComboBox<String>();
	public String TEML,Email,Name,Course,S_ID,DOB,Cls,sex,Phone;
	public int sts=0;
	
	public boolean Validate() throws Exception{
		if(textField.getText().equals("") || 
		   textField_1.getText().equals("") || 
		   textField_2.getText().equals("") || 
		   textField_3.getText().equals("") || 
		   comboBox.getSelectedIndex()==-1 ||
		   !(rdbtnMale.isSelected() || rdbtnFemale.isSelected() || rdbtnOther.isSelected()))
		throw new Exception("Some values are Empty");
		return true;
	}
	
	private String getdate() throws Exception{
		Date DATE=dateChooser.getDate();
		if(DATE==null) throw new Exception("Date is null");
		Calendar cal = Calendar.getInstance();
		cal.setTime(DATE);
		int year=cal.get(Calendar.YEAR);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int month = cal.get(Calendar.MONTH)+1;
		String date=Integer.toString(year)+"-"+Integer.toString(month)+"-"+Integer.toString(day);
		return date;
	}
	public void UpdateDetails() {
		try {
			textField.setText(Name);
			textField_1.setText(S_ID);
			textField_1.setEditable(false);
			textField_2.setText(Phone);
			textField_3.setText(Email);
			comboBox.setSelectedItem(Course);
			dateChooser.setDate(new SimpleDateFormat("yyyy-MM-dd").parse(DOB));
			if(sex.equals("Male")) rdbtnMale.setSelected(true);
			else if(sex.equals("Female")) rdbtnFemale.setSelected(true);
			else if(sex.equals("Other")) rdbtnOther.setSelected(true);
		}catch(Exception err) {
			err.printStackTrace();
		}
	}
	public INSERT(Frame owner, String title,String job) {
		super(owner, title,Dialog.ModalityType.DOCUMENT_MODAL);
		setResizable(false);
		setType(Type.UTILITY);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 352);
		contentPane = new JPanel();
		contentPane.setBackground(SystemColor.activeCaption);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblHighFrands = new JLabel("Name");
		lblHighFrands.setForeground(Color.WHITE);
		lblHighFrands.setFont(new Font("Dialog", Font.BOLD, 15));
		lblHighFrands.setBounds(12, 12, 103, 38);
		contentPane.add(lblHighFrands);
		
		JLabel lblSerianid = new JLabel("Serial-ID");
		lblSerianid.setForeground(Color.WHITE);
		lblSerianid.setFont(new Font("Dialog", Font.BOLD, 15));
		lblSerianid.setBounds(12, 49, 103, 38);
		contentPane.add(lblSerianid);
		
		JLabel lblPhone = new JLabel("Phone");
		lblPhone.setForeground(Color.WHITE);
		lblPhone.setFont(new Font("Dialog", Font.BOLD, 15));
		lblPhone.setBounds(12, 86, 103, 38);
		contentPane.add(lblPhone);
		
		JLabel lblEmail = new JLabel("Email");
		lblEmail.setForeground(Color.WHITE);
		lblEmail.setFont(new Font("Dialog", Font.BOLD, 15));
		lblEmail.setBounds(12, 124, 103, 38);
		contentPane.add(lblEmail);
		
		JLabel lblCourse = new JLabel("Course");
		lblCourse.setForeground(Color.WHITE);
		lblCourse.setFont(new Font("Dialog", Font.BOLD, 15));
		lblCourse.setBounds(12, 158, 103, 38);
		contentPane.add(lblCourse);
		
		JLabel lblDob = new JLabel("D.O.B");
		lblDob.setForeground(Color.WHITE);
		lblDob.setFont(new Font("Dialog", Font.BOLD, 15));
		lblDob.setBounds(12, 193, 103, 38);
		contentPane.add(lblDob);
		
		JLabel lblGender = new JLabel("Gender");
		lblGender.setForeground(Color.WHITE);
		lblGender.setFont(new Font("Dialog", Font.BOLD, 15));
		lblGender.setBounds(12, 232, 103, 38);
		contentPane.add(lblGender);
		
		JButton btnUpdate = new JButton(job);
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				String query="",sex="",Course="";
				try {
					if(!Validate() || textField_2.getText().length()!=10) JOptionPane.showMessageDialog(INSERT.this,"Invalid Phone Number","Phone Number can't be less or more than 10 digits",0);
					else {
						if(rdbtnMale.isSelected()) sex="Male";
						else if(rdbtnFemale.isSelected()) sex="Female";
						else if(rdbtnOther.isSelected()) sex="Other";
						Course=ConnDATA.courses[comboBox.getSelectedIndex()];
						Class.forName("com.mysql.cj.jdbc.Driver");
				        Connection conn2= DriverManager.getConnection(ConnDATA.url,ConnDATA.uname,ConnDATA.password);
						if(textField_1.isEditable()) {
					        Statement stmt1=conn2.createStatement();
					        Statement stmt2=conn2.createStatement();
					        ResultSet rs1=stmt1.executeQuery("SELECT *from Students where Email='"+textField_3.getText()+"';");
					        ResultSet rs2=stmt2.executeQuery("SELECT *from Students where SerialID='"+textField_1.getText()+"';");
					        rs1.next(); rs2.next();
					        if(rs1.getRow()==0 && rs2.getRow()==0) {
					        	query="INSERT into Students (Name,SerialID,Phone,Email,SEX,DOB,Class,Course) values ('"+textField.getText()+"','"+textField_1.getText()+"','"+textField_2.getText()+"','"+textField_3.getText()+"','"+sex+"','"+getdate()+"','"+Cls+"','"+Course+"');";
					        	Statement stmt3=conn2.createStatement();
					        	stmt3.executeUpdate(query); sts++;
					        	stmt3.executeUpdate("UPDATE Teacher_LOGIN SET Students="+Integer.toString(sts)+" WHERE Email='"+TEML+"';"); 
					        	stmt3.close();
					        	JOptionPane.showMessageDialog(INSERT.this,"Record Successfully Inserted","SUCCESS",1);
					        }
					        else {
					        	if(rs1.getRow()>0 && rs2.getRow()>0) JOptionPane.showMessageDialog(INSERT.this,"Email and Serial ID already exist","Record Duplicate Error",0);
					        	else if(rs1.getRow()>0) JOptionPane.showMessageDialog(INSERT.this,"Email already exists","Record Duplicate Error",0);
					        	else if(rs2.getRow()>0) JOptionPane.showMessageDialog(INSERT.this,"Serial ID already exists","Record Duplicate Error",0);
					        }
					        rs1.close(); rs2.close(); stmt1.close(); stmt2.close();
						}
						else {
							Statement stmt1=conn2.createStatement();
							ResultSet rs1=stmt1.executeQuery("SELECT SerialID from Students where Email='"+textField_3.getText()+"';"); rs1.next();
							if(rs1.getRow()!=0) {
								if(rs1.getString("SerialID").equals(S_ID)){
									Statement stmt2=conn2.createStatement();
									query="UPDATE Students set Name='"+textField.getText()+"',Phone='"+textField_2.getText()+"',Email='"+textField_3.getText()+"',SEX='"+sex+"',Course='"+Course+"' WHERE SerialID='"+textField_1.getText()+"';";
									stmt2.executeUpdate(query); stmt2.close();
									JOptionPane.showMessageDialog(INSERT.this,"Record Successfully Updated","SUCCESS",1);
								} else JOptionPane.showMessageDialog(INSERT.this,"Email already exists","Record Duplicate Error",0);
							}
							else {
								Statement stmt2=conn2.createStatement();
								query="UPDATE Students set Name='"+textField.getText()+"',Phone='"+textField_2.getText()+"',Email='"+textField_3.getText()+"',SEX='"+sex+"',Course='"+Course+"' WHERE SerialID='"+textField_1.getText()+"';";
								stmt2.executeUpdate(query); stmt2.close();
								JOptionPane.showMessageDialog(INSERT.this,"Record Successfully Updated","SUCCESS",1);
							} 
							rs1.close(); stmt1.close();
						}
					}
				}catch(Exception err){
					if(err.getMessage().equals("Some values are Empty")) JOptionPane.showMessageDialog(INSERT.this,"Please fill in the required details","Some values are Empty",0);
					else if(err.getMessage().equals("Date is null")) JOptionPane.showMessageDialog(INSERT.this,"Date of Birth can't be empty","Some values are Empty",0);
					else err.printStackTrace();
				}
			}
		});
		btnUpdate.setBounds(12, 270, 422, 38);
		contentPane.add(btnUpdate);
		
		textField = new JTextField();
		textField.setBounds(133, 22, 301, 28);
		contentPane.add(textField);
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setTransferHandler(null);
		textField_1.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent evt) {
				char ch=evt.getKeyChar();
				String str=textField_1.getText();
				int pos=textField_1.getCaretPosition();
				if((Character.isAlphabetic(ch) || Character.isDigit(ch)) && !Character.isSpaceChar(ch) && !(str.length()>=10)){
					if(pos==str.length()) textField_1.setText(str.toUpperCase()+Character.toUpperCase(ch));
					else {
						String id=str.substring(0,pos)+ch+str.substring(pos,str.length());
						textField_1.setText(id.toUpperCase());
						textField_1.setCaretPosition(pos+1);
					}
				}
				evt.consume();
			}
		});
		
		textField_1.setColumns(10);
		textField_1.setBounds(133, 55, 301, 28);
		contentPane.add(textField_1);
		
		
		textField_2 = new JTextField();
		textField_2.setTransferHandler(null);
		textField_2.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent evt) {
				char ch=evt.getKeyChar();
				String str=textField_2.getText();
				if(!(Character.isDigit(ch) && str.length()<10)) evt.consume();
			}
		});
		textField_2.setColumns(10);
		textField_2.setBounds(133, 92, 301, 28);
		contentPane.add(textField_2);
		
		textField_3 = new JTextField();
		textField_3.setColumns(10);
		textField_3.setBounds(133, 130, 301, 28);
		contentPane.add(textField_3);
		
		comboBox.setBounds(133, 165, 301, 24);
		for(int i=0;i<ConnDATA.courses.length;i++) comboBox.addItem(ConnDATA.courses[i]);
		comboBox.setSelectedIndex(-1);
		contentPane.add(comboBox);
		
		rdbtnOther.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(rdbtnOther.isSelected()) {
					rdbtnMale.setSelected(false);
					rdbtnFemale.setSelected(false);
				}
			}
		});
		rdbtnFemale.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(rdbtnFemale.isSelected()) {
					rdbtnMale.setSelected(false);
					rdbtnOther.setSelected(false);
				}
			}
		});
		rdbtnMale.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(rdbtnMale.isSelected()) {
					rdbtnFemale.setSelected(false);
					rdbtnOther.setSelected(false);
				}
			}
		});
		rdbtnMale.setForeground(SystemColor.info);
		rdbtnMale.setBackground(SystemColor.activeCaption);
		rdbtnMale.setBounds(132, 240, 72, 23);
		contentPane.add(rdbtnMale);
		
		rdbtnFemale.setForeground(SystemColor.info);
		rdbtnFemale.setBackground(SystemColor.activeCaption);
		rdbtnFemale.setBounds(208, 240, 93, 23);
		contentPane.add(rdbtnFemale);
		
		rdbtnOther.setForeground(SystemColor.info);
		rdbtnOther.setBackground(SystemColor.activeCaption);
		rdbtnOther.setBounds(305, 240, 72, 23);
		contentPane.add(rdbtnOther);

		dateChooser.setBounds(133, 200, 301, 24);
		contentPane.add(dateChooser);
	}
}