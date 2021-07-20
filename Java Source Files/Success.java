import java.awt.Image;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.awt.event.ActionEvent;
import javax.imageio.ImageIO;
import javax.sql.rowset.serial.SerialBlob;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Blob;
import javax.swing.SwingConstants;

public class Success extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table;
	JLabel lblName,lblEmail,lblPhone,lblClass,lblNoOfStudents;
	public String name,email,phone,cls;
	public int sts=0; boolean isCountChanged=false;
	private static String column_names[]= {"Serial ID","Name","Email","Phone Number","SEX","D.O.B","Courses"};
	private static String[] buttons = { "The whole Table", "Just the Selected Rows", "Cancel"};    
	final JLabel imgLabel = new JLabel("Picture");
	
	private void refresh() throws Exception{
		DefaultTableModel tbm=(DefaultTableModel)table.getModel();
		tbm.setRowCount(0);
		Class.forName("com.mysql.cj.jdbc.Driver");
		String query="SELECT *from Students WHERE Class='"+cls+"';";
        Connection con= DriverManager.getConnection(ConnDATA.url,ConnDATA.uname,ConnDATA.password);
        Statement st=con.createStatement();
        ResultSet rs=st.executeQuery(query);
        while(rs.next()){
        	String values[]= {rs.getString("SerialID"),rs.getString("Name"),rs.getString("Email"),rs.getString("Phone"),rs.getString("SEX"),rs.getString("DOB"),rs.getString("Course")};
        	tbm.addRow(values);
        }
        if(isCountChanged) {
        	Statement st2=con.createStatement();
            ResultSet rs2=st2.executeQuery("SELECT Students from Teacher_LOGIN WHERE Email='"+email+"';");
            rs2.next(); sts=rs2.getInt("Students");
            lblNoOfStudents.setText("Student count : "+Integer.toString(sts));
            rs2.close(); st2.close();
            isCountChanged=false;
            lblNoOfStudents.setText("Student count : "+Integer.toString(sts));
        }	st.close(); rs.close(); con.close();
	}
	
	public void SetImg() {
		try {
			String query="Select Image from Teacher_LOGIN where Email='"+email+"';";
			Class.forName("com.mysql.cj.jdbc.Driver");
	        Connection con= DriverManager.getConnection(ConnDATA.url,ConnDATA.uname,ConnDATA.password);
	        Statement st=con.createStatement();
	        ResultSet rs=st.executeQuery(query);
	        rs.next();
	        if(rs.getRow()!=0) {
		        Blob x=rs.getBlob(1);
		        if(x==null){
		        	BufferedImage image = ImageIO.read(getClass().getClassLoader().getResource("user-icon.png"));
		        	Image BarImg = image.getScaledInstance(imgLabel.getWidth(), imgLabel.getHeight(),Image.SCALE_SMOOTH);
			        imgLabel.setIcon(new ImageIcon(BarImg));
		        }
		        else {
		        	InputStream in =x.getBinaryStream();
			        BufferedImage image = ImageIO.read(in);
			        Image BarImg = image.getScaledInstance(imgLabel.getWidth(), imgLabel.getHeight(),Image.SCALE_SMOOTH);
			        imgLabel.setIcon(new ImageIcon(BarImg));
		        }
	        }
	        else {
	        	BufferedImage image = ImageIO.read(getClass().getClassLoader().getResource("user-icon.png"));
	        	Image BarImg = image.getScaledInstance(imgLabel.getWidth(), imgLabel.getHeight(),Image.SCALE_SMOOTH);
		        imgLabel.setIcon(new ImageIcon(BarImg));
	        }
	        st.close(); rs.close();
		}catch(Exception err) {
			err.printStackTrace();
		}
	}
	public Success(){
		setTitle("Teacher's Section");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 642, 471);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		imgLabel.setHorizontalAlignment(SwingConstants.CENTER);
		imgLabel.setBounds(466, 6, 127, 105);
		contentPane.add(imgLabel);
		
		JButton btnClickMe = new JButton("Upload Photo");
		btnClickMe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					JFileChooser fileChooser = new JFileChooser();
					fileChooser.setDialogTitle("Select Profile Image");
					fileChooser.setMultiSelectionEnabled(false);
					
					FileFilter imgFilter = new FileTypeFilter(".png","Portable Network Graphics");
					FileFilter imgFilter2 = new FileTypeFilter(".jpg", "Joint Photographic Experts Group");
					fileChooser.setAcceptAllFileFilterUsed(false);
					fileChooser.addChoosableFileFilter(imgFilter);
					fileChooser.addChoosableFileFilter(imgFilter2);
					int check=fileChooser.showOpenDialog(Success.this);
					if(check==0) {
						File outputfile = fileChooser.getSelectedFile();
						BufferedImage x= ImageIO.read(new FileInputStream(outputfile.getAbsolutePath()));
						Image BarImg = x.getScaledInstance(imgLabel.getWidth(), imgLabel.getHeight(),Image.SCALE_SMOOTH);
				        imgLabel.setIcon(new ImageIcon(BarImg));
				        ByteArrayOutputStream os = new ByteArrayOutputStream();
				        ImageIO.write(x, "png", os);
				        Blob blob=new SerialBlob(os.toByteArray());
				        Class.forName("com.mysql.cj.jdbc.Driver");
			            Connection con= DriverManager.getConnection(ConnDATA.url,ConnDATA.uname,ConnDATA.password);
			            PreparedStatement stmt = con.prepareStatement("UPDATE Teacher_LOGIN SET Image=? WHERE Email='"+email+"';");
			            stmt.setBlob(1,blob.getBinaryStream());
			            stmt.executeUpdate();
			            stmt.close(); con.close();
					}
				}catch(Exception err) {
					err.printStackTrace();
				}
			}
		});
		btnClickMe.setBounds(434, 112, 189, 35);
		contentPane.add(btnClickMe);
		
		lblName = new JLabel("Name : ");
		lblName.setBounds(19, 6, 194, 15);
		contentPane.add(lblName);
		
		lblEmail = new JLabel("Email : ");
		lblEmail.setBounds(19, 25, 266, 15);
		contentPane.add(lblEmail);
		
		lblPhone = new JLabel("Phone : ");
		lblPhone.setBounds(19, 42, 217, 15);
		contentPane.add(lblPhone);
		
		lblClass = new JLabel("Class : ");
		lblClass.setBounds(19, 57, 109, 15);
		contentPane.add(lblClass);
		
		lblNoOfStudents = new JLabel("Student count : ");
		lblNoOfStudents.setBounds(19, 77, 149, 15);
		contentPane.add(lblNoOfStudents);
		
		JButton btnInsert = new JButton("INSERT");
		btnInsert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				INSERT x=new INSERT(Success.this,"New Student Enrollment","INSERT");
				x.TEML=email; x.sts=sts; x.Cls=cls; x.setVisible(true);
				x.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosed(WindowEvent e) {
					    try {
					    	isCountChanged=true;
					    	refresh();
					    }catch(Exception err) {
					    	err.printStackTrace();
					    }
					}
				});	
			}
		});
		btnInsert.setBounds(19, 386, 106, 35);
		contentPane.add(btnInsert);
		
		JButton btnUpdate = new JButton("UPDATE");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				int pos=table.getSelectedRow();
				if(pos!=-1) {
					String S_ID=table.getModel().getValueAt(pos,0).toString();
					String Name=table.getModel().getValueAt(pos,1).toString();
					String Email=table.getModel().getValueAt(pos,2).toString();
					String Phone=table.getModel().getValueAt(pos,3).toString();
					String Sex=table.getModel().getValueAt(pos,4).toString();
					String DOB=table.getModel().getValueAt(pos,5).toString();
					String Course=table.getModel().getValueAt(pos,6).toString();
					INSERT x=new INSERT(Success.this,"Update details of an existing Student","UPDATE");
					x.Name=Name; x.Email=Email; x.Phone=Phone; x.S_ID=S_ID; x.Course=Course; x.sex=Sex; x.DOB=DOB; x.Cls=cls;
					x.UpdateDetails(); x.setVisible(true);
					x.addWindowListener(new WindowAdapter() {
						@Override
						public void windowClosed(WindowEvent e) {
						    try {
						    	refresh();
						    }catch(Exception err) {
						    	err.printStackTrace();
						    }
						}
					});
			}}
		});
		btnUpdate.setBounds(266, 386, 106, 35);
		contentPane.add(btnUpdate);
		
		JButton btnDelete = new JButton("DELETE");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try {
					int pos[]=table.getSelectedRows();
					if(pos.length!=0) {
						int x=JOptionPane.showConfirmDialog(Success.this, "Are you sure you want to delete the selected records?", "Delete Confirmation", 2);
						if(x==0) {
							Class.forName("com.mysql.cj.jdbc.Driver");
							Connection con= DriverManager.getConnection(ConnDATA.url,ConnDATA.uname,ConnDATA.password);
					        Statement st=con.createStatement();
							for(int i=0;i<pos.length;i++) {
								String S_ID=table.getModel().getValueAt(pos[i],0).toString();
								st.executeUpdate("DELETE from Students where SerialID='"+S_ID+"';");
							}	sts-=pos.length;
							st.executeUpdate("UPDATE Teacher_LOGIN SET Students="+Integer.toString(sts)+" WHERE Email='"+email+"';");
					        con.close(); st.close(); refresh(); 
							lblNoOfStudents.setText("Student count : "+Integer.toString(sts));
						}
					}
					
				}catch(Exception err) {
					err.printStackTrace();
				}
			}
		});
		btnDelete.setBounds(517, 386, 106, 35);
		contentPane.add(btnDelete);
		
		JButton btnExport = new JButton("EXPORT");
		btnExport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try {
					int x = JOptionPane.showOptionDialog(Success.this, "Do you want to Export the Entire table or just the selected Rows", "Narrative",0, 3, null, buttons, null);
					boolean isexp=false;
					Workbook wbk=new XSSFWorkbook();
					if(x==1) {
						int pos[]=table.getSelectedRows();
						if(pos.length==0) JOptionPane.showMessageDialog(Success.this,"No rows selected","Couldn't Export Data",0);
						else {
							Sheet sh=wbk.createSheet("Student's Register");
							Font fnt=wbk.createFont();
							fnt.setBold(true);
							fnt.setColor(IndexedColors.AQUA.index);
							fnt.setFontHeightInPoints((short)14);
							Font fnt2=wbk.createFont();
							fnt2.setItalic(true);
							fnt2.setColor(IndexedColors.WHITE.index);
							fnt2.setFontHeightInPoints((short)13);
							CellStyle stlhead= wbk.createCellStyle();
							stlhead.setFont(fnt);
							stlhead.setAlignment(HorizontalAlignment.CENTER);
							stlhead.setFillBackgroundColor(IndexedColors.BLUE.index);
							stlhead.setFillPattern(FillPatternType.THICK_VERT_BANDS);
							CellStyle stldata= wbk.createCellStyle();
							stldata.setFont(fnt2);
							stldata.setFillBackgroundColor(IndexedColors.GREEN.index);
							stldata.setFillPattern(FillPatternType.THICK_VERT_BANDS);
							CellStyle stldataCenter= wbk.createCellStyle();
							stldataCenter.setAlignment(HorizontalAlignment.CENTER);
							stldataCenter.setFont(fnt2);
							stldataCenter.setFillBackgroundColor(IndexedColors.GREEN.index);
							stldataCenter.setFillPattern(FillPatternType.THICK_VERT_BANDS);
							CellStyle stldataRight= wbk.createCellStyle();
							stldataRight.setAlignment(HorizontalAlignment.RIGHT);
							stldataRight.setFont(fnt2);
							stldataRight.setFillBackgroundColor(IndexedColors.GREEN.index);
							stldataRight.setFillPattern(FillPatternType.THICK_VERT_BANDS);
							Row rh=sh.createRow(0);
							CellStyle date=wbk.createCellStyle();
							date.setFont(fnt2);
							date.setAlignment(HorizontalAlignment.CENTER);
							date.setFillBackgroundColor(IndexedColors.GREEN.index);
							date.setFillPattern(FillPatternType.THICK_VERT_BANDS);
							date.setDataFormat(wbk.getCreationHelper().createDataFormat().getFormat("dd/MM/YYYY"));
							for(int i=0;i<column_names.length;i++) {
								Cell cell=rh.createCell(i);
								cell.setCellValue(column_names[i]);
								cell.setCellStyle(stlhead);
							} sh.createFreezePane(0,1);
							for(int i=0;i<pos.length;i++) {
								Row row=sh.createRow(i+1);
								Cell c0=row.createCell(0); c0.setCellValue(table.getValueAt(pos[i],0).toString()); c0.setCellStyle(stldata);
								Cell c1=row.createCell(1); c1.setCellValue(table.getValueAt(pos[i],1).toString()); c1.setCellStyle(stldata);
								Cell c2=row.createCell(2); c2.setCellValue(table.getValueAt(pos[i],2).toString()); c2.setCellStyle(stldata);
								Cell c3=row.createCell(3); c3.setCellValue(table.getValueAt(pos[i],3).toString()); c3.setCellStyle(stldataRight);
								Cell c4=row.createCell(4); c4.setCellValue(table.getValueAt(pos[i],4).toString()); c4.setCellStyle(stldataCenter);
								Cell dtr=row.createCell(5);	dtr.setCellStyle(date); dtr.setCellValue(table.getValueAt(pos[i],5).toString());
								Cell c6=row.createCell(6); c6.setCellValue(table.getValueAt(pos[i],6).toString()); c6.setCellStyle(stldataCenter);
							} isexp=true;
							for(int i=0;i<column_names.length;i++) sh.autoSizeColumn(i);
						}
					}
					else if(x==0) {
						Sheet sh=wbk.createSheet("Student's Register");
						Font fnt=wbk.createFont();
						fnt.setBold(true);
						fnt.setColor(IndexedColors.AQUA.index);
						fnt.setFontHeightInPoints((short)14);
						Font fnt2=wbk.createFont();
						fnt2.setItalic(true);
						fnt2.setColor(IndexedColors.WHITE.index);
						fnt2.setFontHeightInPoints((short)13);
						CellStyle stlhead= wbk.createCellStyle();
						stlhead.setFont(fnt);
						stlhead.setAlignment(HorizontalAlignment.CENTER);
						stlhead.setFillBackgroundColor(IndexedColors.BLUE.index);
						stlhead.setFillPattern(FillPatternType.THICK_VERT_BANDS);
						CellStyle stldata= wbk.createCellStyle();
						stldata.setFont(fnt2);
						stldata.setFillBackgroundColor(IndexedColors.GREEN.index);
						stldata.setFillPattern(FillPatternType.THICK_VERT_BANDS);
						CellStyle stldataCenter= wbk.createCellStyle();
						stldataCenter.setAlignment(HorizontalAlignment.CENTER);
						stldataCenter.setFont(fnt2);
						stldataCenter.setFillBackgroundColor(IndexedColors.GREEN.index);
						stldataCenter.setFillPattern(FillPatternType.THICK_VERT_BANDS);
						CellStyle stldataRight= wbk.createCellStyle();
						stldataRight.setAlignment(HorizontalAlignment.RIGHT);
						stldataRight.setFont(fnt2);
						stldataRight.setFillBackgroundColor(IndexedColors.GREEN.index);
						stldataRight.setFillPattern(FillPatternType.THICK_VERT_BANDS);
						Row rh=sh.createRow(0);
						CellStyle date=wbk.createCellStyle();
						date.setFont(fnt2);
						date.setAlignment(HorizontalAlignment.CENTER);
						date.setFillBackgroundColor(IndexedColors.GREEN.index);
						date.setFillPattern(FillPatternType.THICK_VERT_BANDS);
						date.setDataFormat(wbk.getCreationHelper().createDataFormat().getFormat("dd/MM/YYYY"));
						for(int i=0;i<column_names.length;i++) {
							Cell cell=rh.createCell(i);
							cell.setCellValue(column_names[i]);
							cell.setCellStyle(stlhead);
						} sh.createFreezePane(0,1);
						for(int i=0;i<table.getRowCount();i++) {
							Row row=sh.createRow(i+1);
							Cell c0=row.createCell(0); c0.setCellValue(table.getValueAt(i,0).toString()); c0.setCellStyle(stldata);
							Cell c1=row.createCell(1); c1.setCellValue(table.getValueAt(i,1).toString()); c1.setCellStyle(stldata);
							Cell c2=row.createCell(2); c2.setCellValue(table.getValueAt(i,2).toString()); c2.setCellStyle(stldata);
							Cell c3=row.createCell(3); c3.setCellValue(table.getValueAt(i,3).toString()); c3.setCellStyle(stldataRight);
							Cell c4=row.createCell(4); c4.setCellValue(table.getValueAt(i,4).toString()); c4.setCellStyle(stldataCenter);
							Cell dtr=row.createCell(5);	dtr.setCellStyle(date); dtr.setCellValue(table.getValueAt(i,5).toString());
							Cell c6=row.createCell(6); c6.setCellValue(table.getValueAt(i,6).toString()); c6.setCellStyle(stldataCenter);
						} isexp=true;
						for(int i=0;i<column_names.length;i++) sh.autoSizeColumn(i);
					}
					if(isexp){
						JFileChooser fileChooser = new JFileChooser();
						fileChooser.setAcceptAllFileFilterUsed(false);
						fileChooser.setDialogTitle("Export to Spreadsheet");
						fileChooser.setMultiSelectionEnabled(false);
						FileFilter imgFilter = new FileTypeFilter(".xlsx","Microsoft Excel 2013-16");
						fileChooser.addChoosableFileFilter(imgFilter);
						int chk=fileChooser.showSaveDialog(Success.this);
						if(chk==JFileChooser.APPROVE_OPTION) {
							String path=fileChooser.getSelectedFile().getAbsolutePath();
							if(path.indexOf('.')!=-1) path=path.substring(0,path.indexOf('.'));
							FileOutputStream fsout=new FileOutputStream(path+".xlsx");
							wbk.write(fsout); fsout.close();
							JOptionPane.showMessageDialog(Success.this,"Data Exported Successfully","SUCCESS",1);
						}
					} wbk.close();
				}catch(Exception err) {
					err.printStackTrace();
				}
			}
		});
		btnExport.setBounds(19, 112, 121, 35);
		contentPane.add(btnExport);
	}
	public void Init() {
		try {
			lblName.setText("Name : "+name);
			lblEmail.setText("Email : "+email);
			lblPhone.setText("Phone : "+phone);
			lblClass.setText("Class : "+cls);
			lblNoOfStudents.setText("Student count : "+Integer.toString(sts));
			Class.forName("com.mysql.cj.jdbc.Driver");
			String query="SELECT *from Students WHERE Class='"+cls+"';";
	        Connection con= DriverManager.getConnection(ConnDATA.url,ConnDATA.uname,ConnDATA.password);
	        Statement st=con.createStatement();
	        ResultSet rs=st.executeQuery(query);
	        DefaultTableModel table_model=new DefaultTableModel(column_names,0);
	        while(rs.next()){
	        	String values[]= {rs.getString("SerialID"),rs.getString("Name"),rs.getString("Email"),rs.getString("Phone"),rs.getString("SEX"),rs.getString("DOB"),rs.getString("Course")};
	        	table_model.addRow(values);
	        }
	        st.close(); rs.close(); con.close();
	        table=new JTable(table_model) {
				private static final long serialVersionUID = 1L;  //Don't know what the hell is this
				public boolean isCellEditable(int row, int column) {                
	                return false;               
				};
			};
			table.getTableHeader().setReorderingAllowed(false);
			JScrollPane SP= new JScrollPane(table,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			SP.setBounds(19, 159, 604, 215);
			contentPane.add(SP); SetImg();
		}catch(Exception err) {
			err.printStackTrace();
		}
	}
}