package jp.nyatla.manaclipper.ctrl;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import jp.nyatla.manaclipper.ctrl.Export_jinfagang_keras_frcnn_TrainTextDlg.ImageCopyMode;

import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

public class Export_jinfagang_keras_frcnn_TrainTextDlg extends JDialog {
	public enum ImageCopyMode {
	    NO_COPY,			//コピー操作を行わなない
	    COPY_IMAGE,			//対象画像をコピーする
	    MAKE_PATCH,			//パッチを生成してコピーする
	    MAKE_RESIZED_PATCH,	//リサイズしたパッチをコピーする
	}
	public class Result{
		final public String output_xml_path;
		final public List<String> filter;
		final public ImageCopyMode makeImageMode;
		final public int width;
		final public int height;
		private Result(AppImpl _impl) {
			this.output_xml_path=_impl._output_text.getText();
			String s=_impl._filter_text.getText();
			this.filter=Arrays.asList(s.trim().length()==0?new String[0]:_impl._filter_text.getText().split(","));
			this.makeImageMode=(ImageCopyMode) _impl._mode.getSelectedItem();
			switch(this.makeImageMode) {
			case MAKE_RESIZED_PATCH:
				this.width=Integer.parseInt(_impl._resize_x.getText());
				this.height=Integer.parseInt(_impl._resize_y.getText());
				break;
			default:
				this.width=this.height=0;
				break;
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
	public Result doModal(String default_file_path)
	{
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this._impl._default_file_path=default_file_path;
		if(_impl._output_text.getText().isEmpty()) {
			_impl._output_text.setText(default_file_path);
		}
		
		this.setVisible(true);
		this.dispose();
		return result;
	}

	
	AppImpl _impl=new AppImpl();
	/**
	 * Create the dialog.
	 */
	public Export_jinfagang_keras_frcnn_TrainTextDlg() {
		setResizable(false);
		setModal(true);
		setTitle("Export jinfagang_keras_frcnn text");
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
		
		resizeYText = new JTextField();
		resizeYText.setBounds(129, 156, 58, 19);
		contentPanel.add(resizeYText);
		resizeYText.setColumns(10);
		
		resizeXText = new JTextField();
		resizeXText.setColumns(10);
		resizeXText.setBounds(29, 155, 58, 19);
		contentPanel.add(resizeXText);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						//チェ�?ク
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
		
		JLabel lblX = new JLabel("X");
		lblX.setBounds(12, 161, 22, 13);
		contentPanel.add(lblX);
		
		JLabel lblY = new JLabel("Y");
		lblY.setBounds(99, 159, 22, 13);
		contentPanel.add(lblY);
		
		JComboBox comboBox = new JComboBox();
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				_impl.updateResizeCtrlEnable();
			}
		});
		comboBox.setModel(new DefaultComboBoxModel(ImageCopyMode.values()));
		comboBox.setBounds(12, 131, 202, 19);
		contentPanel.add(comboBox);
		
		JLabel lblPatchGenerateMode = new JLabel("Image generate mode");
		lblPatchGenerateMode.setBounds(12, 109, 140, 13);
		contentPanel.add(lblPatchGenerateMode);
		this._impl._resize_x=resizeXText;
		this._impl._resize_y=resizeYText;
		this._impl._output_text=outputDirText;
		this._impl._filter_text=textField_1;
		this._impl._mode=comboBox;

		this._impl.frame=this;
		this._impl.updateResizeCtrlEnable();
	}
	static class AppImpl{
		public String _default_file_path;
		JDialog frame;
		JTextField _resize_x;
		JTextField _resize_y;
		JComboBox _mode;
		JTextField _output_text;
		JTextField _filter_text;
		public void updateResizeCtrlEnable()
		{
			Object obj= this._mode.getSelectedItem();
			if(obj==null) {
				return;
			}
			ImageCopyMode mode=(ImageCopyMode)obj;
			boolean f=false;
			switch(mode) {
			case MAKE_RESIZED_PATCH:
				f=true;
				break;
			default:
				break;			
			}
			this._resize_x.setEnabled(f);
			this._resize_y.setEnabled(f);
		}
		/**
		 * 入力状態が正しいか確認
		 */
		public boolean isValid()
		{
			//
			if(this._output_text.getText().isEmpty()) {
				JOptionPane.showConfirmDialog(this.frame, "Fill output file name.","ERROR", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
				return false;
			}
			File f=new File(this._output_text.getText());
			if(!f.getParentFile().exists() && !f.getParentFile().getParentFile().exists()) {
				JOptionPane.showConfirmDialog(this.frame, "Fix output file path","ERROR", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
				return false;			
			}

			//数値のチェック
			
			if(this._resize_x.isEnabled()) {
				try{
					int i=Integer.parseInt(_resize_x.getText());
					if(i<0 || i>10000) {
						throw new NumberFormatException();
					}
				}catch(NumberFormatException e) {
					JOptionPane.showConfirmDialog(this.frame, "Fix resize parameter X","ERROR", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
					return false;
				}
			}
			if(this._resize_y.isEnabled()) {
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
			File fn=new File(this._default_file_path);
			filechooser.setSelectedFile(fn);
			if(filechooser.showOpenDialog(this.frame)!=JFileChooser.APPROVE_OPTION) {
				return;
			}
			File f=filechooser.getSelectedFile();
			if(f.isDirectory()) {
				f=new File(f.getPath(),fn.getName());
			}
			this._output_text.setText(f.getPath());
		}
	}
}
