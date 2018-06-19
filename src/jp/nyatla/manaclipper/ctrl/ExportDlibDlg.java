package jp.nyatla.manaclipper.ctrl;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;


import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.awt.event.ActionEvent;

public class ExportDlibDlg extends JDialog {

	public class Result{
		final public String output_xml_path;
		final public List<String> filter;
		final public boolean makeImage;
		final public boolean makeResize;
		final public int width;
		final public int height;
		private Result(AppImpl _impl) {
			this.output_xml_path=_impl._output_text.getText();
			String s=_impl._filter_text.getText();
			this.filter=Arrays.asList(s.trim().length()==0?new String[0]:_impl._filter_text.getText().split(","));
			this.makeImage=_impl._patch_checkbox.isSelected();
			if(this.makeImage) {
				this.makeResize=_impl._resize_checkbox.isSelected();
				if(this.makeResize) {
					this.width=Integer.parseInt(_impl._resize_x.getText());
					this.height=Integer.parseInt(_impl._resize_y.getText());
				}else {
					this.width=this.height=0;
				}
			}else {
				this.makeResize=false;
				this.width=this.height=0;
			}
		}
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 5757690842398536125L;
	private final JPanel contentPanel = new JPanel();
	private JTextField outputDirText;
	private JTextField textField_1;
	private JTextField resizeYText;
	private JTextField resizeXText;

	
	private Result result=null;
	public Result doModal()
	{
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setVisible(true);
		this.dispose();
		return result;
	}

	
	AppImpl _impl=new AppImpl();
	/**
	 * Create the dialog.
	 */
	public ExportDlibDlg() {
		setResizable(false);
		setModal(true);
		setTitle("Export dlib compatible XML");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			outputDirText = new JTextField();
			outputDirText.setBounds(12, 28, 332, 19);
			contentPanel.add(outputDirText);
			outputDirText.setColumns(10);
		}
		
		JButton btnNewButton = new JButton("Open");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				_impl.handleOpenFile();
			}
		});
		btnNewButton.setBounds(356, 27, 66, 21);
		contentPanel.add(btnNewButton);
		
		JCheckBox createPatchChecked = new JCheckBox("Create patch images");
		createPatchChecked.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				_impl.updateResizeCtrlEnable();
			}
		});
		createPatchChecked.setBounds(8, 107, 175, 21);
		contentPanel.add(createPatchChecked);
		
		JLabel lblNewLabel = new JLabel("Output file name");
		lblNewLabel.setBounds(12, 10, 115, 13);
		contentPanel.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Tag filter(csv)");
		lblNewLabel_1.setBounds(12, 57, 140, 13);
		contentPanel.add(lblNewLabel_1);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(12, 80, 332, 19);
		contentPanel.add(textField_1);
		
		JCheckBox resizeChecked = new JCheckBox("Resize");
		resizeChecked.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				_impl.updateResizeCtrlEnable();
			}
		});
		resizeChecked.setBounds(8, 128, 76, 21);
		contentPanel.add(resizeChecked);
		
		resizeYText = new JTextField();
		resizeYText.setBounds(222, 129, 58, 19);
		contentPanel.add(resizeYText);
		resizeYText.setColumns(10);
		
		resizeXText = new JTextField();
		resizeXText.setColumns(10);
		resizeXText.setBounds(122, 128, 58, 19);
		contentPanel.add(resizeXText);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						//
						if(_impl.isValid()) {
							setVisible(false);
							result=new Result(_impl);							
						}
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						setVisible(false);
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		this._impl._resize_x=resizeXText;
		this._impl._resize_y=resizeYText;
		this._impl._resize_checkbox=resizeChecked;
		this._impl._patch_checkbox=createPatchChecked;
		this._impl._output_text=outputDirText;
		this._impl._filter_text=textField_1;
		
		JLabel lblX = new JLabel("X");
		lblX.setBounds(105, 134, 22, 13);
		contentPanel.add(lblX);
		
		JLabel lblY = new JLabel("Y");
		lblY.setBounds(192, 132, 22, 13);
		contentPanel.add(lblY);
		this._impl.frame=this;
		this._impl.updateResizeCtrlEnable();
	}
	static class AppImpl{
		JDialog frame;
		JTextField _resize_x;
		JTextField _resize_y;
		JCheckBox _resize_checkbox;
		JCheckBox _patch_checkbox;
		JTextField _output_text;
		JTextField _filter_text;
		public void updateResizeCtrlEnable()
		{
			boolean f=_patch_checkbox.isSelected();
			this._resize_checkbox.setEnabled(f);
			f=f && _resize_checkbox.isSelected();
			this._resize_x.setEnabled(f);
			this._resize_y.setEnabled(f);
		}

		public boolean isValid()
		{

			if(this._output_text.getText().isEmpty()) {
				JOptionPane.showConfirmDialog(this.frame, "Fill output file name.","ERROR", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
				return false;
			}
			File f=new File(this._output_text.getText());
			if(!f.getParentFile().exists() && !f.getParentFile().getParentFile().exists()) {
				JOptionPane.showConfirmDialog(this.frame, "Fix output file path","ERROR", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
				return false;			
			}

			if(this._patch_checkbox.isSelected() && this._resize_checkbox.isSelected()) {
				try{
					int i=Integer.parseInt(_resize_x.getText());
					if(i<0 || i>10000) {
						throw new NumberFormatException();
					}
				}catch(NumberFormatException e) {
					JOptionPane.showConfirmDialog(this.frame, "Fix resize parameter X","ERROR", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
					return false;
				}
				try{
					int i=Integer.parseInt(_resize_y.getText());
					if(i<0 || i>10000) {
						throw new NumberFormatException();
					}
				}catch(NumberFormatException e) {
					JOptionPane.showConfirmDialog(this.frame, "Fix resize parameter Y","ERROR", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
					return false;
				}
			}
			return true;	
		}
		public void handleOpenFile()
		{
			JFileChooser filechooser = new JFileChooser();
			filechooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			filechooser.setDialogTitle("Load from ManaClipperXML");
			filechooser.setSelectedFile(new File("untitled.dlib.xml"));
			if(filechooser.showOpenDialog(this.frame)!=JFileChooser.APPROVE_OPTION) {
				return;
			}
			File f=filechooser.getSelectedFile();
			if(f.isDirectory()) {
				f=new File(f.getPath(),"untitled.dlib.xml");
			}
			this._output_text.setText(f.getPath());
		}
	}
}
