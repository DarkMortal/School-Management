import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import java.awt.SystemColor;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class LoginPage extends JFrame {
	private static final long serialVersionUID = 1L;
	private JFrame frame;
	private JTextField textField;
	private JPasswordField textField_1;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				        if ("Nimbus".equals(info.getName())) {
				            UIManager.setLookAndFeel(info.getClassName());
				            break;
				        }
				    }
					LoginPage window = new LoginPage();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	

	public LoginPage(){
		frame = new JFrame();
		frame.setResizable(false);
		frame.getContentPane().setBackground(SystemColor.info);
		frame.setBackground(UIManager.getColor("Button.focus"));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(100, 100, 341, 216);
		frame.getContentPane().setLayout(null);
		
		textField = new JTextField();
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent evt) {
				char ch=evt.getKeyChar();
				if(Character.isSpaceChar(ch)) evt.consume();
			}
		});
		textField.setFont(new Font("DejaVu Sans", Font.BOLD, 13));
		textField.setToolTipText("Please enter your registered Email");
		textField.setBounds(16, 48, 304, 38);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		textField_1 = new JPasswordField();
		textField_1.setFont(new Font("DejaVu Sans", Font.BOLD, 13));
		textField_1.setToolTipText("Please Enter your Password");
		textField_1.setBounds(16, 85, 304, 38);
		frame.getContentPane().add(textField_1);
		textField_1.setColumns(10);
		
		JButton btnLogin = new JButton("LOGIN");
		btnLogin.setFont(new Font("DejaVu Sans", Font.BOLD, 12));
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try {
					String query="SELECT *from Teacher_LOGIN WHERE Email='"+textField.getText()+"' and Password='"+String.valueOf(textField_1.getPassword())+"';";
			        Class.forName("com.mysql.cj.jdbc.Driver");
			        Connection con= DriverManager.getConnection(ConnDATA.url,ConnDATA.uname,ConnDATA.password);
			        Statement st=con.createStatement();
			        ResultSet rs=st.executeQuery(query);
			        rs.next();
			        if(rs.getRow()>1 || rs.getRow()==1) {
			        	dispose();
			        	frame.setVisible(false);
			        	Success x=new Success();
			        	x.name=rs.getString("Name");
			        	x.email=textField.getText();
			        	x.sts=rs.getInt("Students");
			        	x.phone=rs.getString("Phone");
			        	x.cls=rs.getString("Class");
			        	x.Init();
			        	x.setVisible(true);
			        }
			        else JOptionPane.showMessageDialog(LoginPage.this,"Please check your Username & Password and try again","Invalid details",0);
			        st.close(); rs.close(); con.close();
				}catch(Exception err) {
					err.printStackTrace();
				}
			}
		});
		btnLogin.setBounds(16, 135, 304, 38);
		frame.getContentPane().add(btnLogin);
		
		JLabel lblTeacherLogin = new JLabel("Teacher Login");
		lblTeacherLogin.setFont(new Font("DejaVu Sans", Font.BOLD, 16));
		lblTeacherLogin.setBounds(101, 6, 142, 25);
		frame.getContentPane().add(lblTeacherLogin);
	}
}