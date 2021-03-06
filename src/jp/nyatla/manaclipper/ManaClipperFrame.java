package jp.nyatla.manaclipper;

import java.awt.EventQueue;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JButton;

import jp.nyatla.manaclipper.ctrl.DirectoryFileList;
import jp.nyatla.manaclipper.ctrl.ExportDlibDlg;
import jp.nyatla.manaclipper.ctrl.Export_jinfagang_keras_frcnn_TrainTextDlg;
import jp.nyatla.manaclipper.ctrl.PreviewPanel;
import jp.nyatla.manaclipper.ctrl.RectList;
import jp.nyatla.manaclipper.ctrl.RegisteredCombobox;
import jp.nyatla.manaclipper.io.DlibXml;
import jp.nyatla.manaclipper.io.JkfRcnLebelText;
import jp.nyatla.manaclipper.io.ManaClipperXml;
import jp.nyatla.manaclipper.io.Utils;
import jp.nyatla.manaclipper.type.ClipDataset;
import jp.nyatla.manaclipper.type.ClipDataset.BoxInfo;

import javax.swing.JPanel;
import javax.swing.JMenuBar;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JMenu;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.imageio.ImageIO;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.awt.Toolkit;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelListener;
import java.awt.image.AreaAveragingScaleFilter;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.event.MouseWheelEvent;
import javax.swing.DefaultComboBoxModel;
import java.awt.Font;
import java.awt.Canvas;
import java.awt.event.InputMethodListener;
import java.awt.event.InputMethodEvent;

public class ManaClipperFrame {

	private AppImpl _impl=new AppImpl();
	private JFrame frmManaclipper;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ManaClipperFrame window = new ManaClipperFrame();
					window.frmManaclipper.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ManaClipperFrame() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmManaclipper = new JFrame();
		frmManaclipper.setTitle("ManaClipper");
		frmManaclipper.setBounds(100, 100, 845, 723);
		frmManaclipper.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		PreviewPanel panel = new PreviewPanel();
		panel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				_impl.handleAddClip();
			}
		});
		panel.addMouseWheelListener(new MouseWheelListener() {
			private long last_wheeled=0;
			private int speed=1;
			public void mouseWheelMoved(MouseWheelEvent arg0) {
				if(System.currentTimeMillis()-last_wheeled<100) {
					speed+=1;
				}else {
					speed=1;
				}
				_impl.handleCursolWheel(arg0.getWheelRotation()*speed);
				last_wheeled=System.currentTimeMillis();
			}
		});
		panel.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent arg0) {
				_impl.handleCursorRect(arg0.getX(),arg0.getY());
			}
		});
		
		JPanel panel_1 = new JPanel();
		GroupLayout groupLayout = new GroupLayout(frmManaclipper.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(panel, GroupLayout.DEFAULT_SIZE, 538, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 251, GroupLayout.PREFERRED_SIZE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(panel, GroupLayout.DEFAULT_SIZE, 532, Short.MAX_VALUE)
				.addComponent(panel_1, GroupLayout.DEFAULT_SIZE, 532, Short.MAX_VALUE)
		);
		
		JButton btnAdd = new JButton("Add");
		btnAdd.setEnabled(false);
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				_impl.handleAddClip();
			}
		});
		
		JButton btnRemove = new JButton("Remove");
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				_impl.handleDeleteClip();
			}
		});
		
		RegisteredCombobox tagNameComboBox = new RegisteredCombobox();
		tagNameComboBox.addInputMethodListener(new InputMethodListener() {
			public void caretPositionChanged(InputMethodEvent arg0) {
			}
			public void inputMethodTextChanged(InputMethodEvent event) {
			}
		});
		tagNameComboBox.setFont(new Font("ＭＳ Ｐゴシック", Font.PLAIN, 12));
		tagNameComboBox.setEditable(true);
		tagNameComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				_impl.handleTagChanged();
			}
		});
		
		JLabel lblNewLabel = new JLabel("File list");
		
		JScrollPane scrollPane = new JScrollPane();
		
		JScrollPane scrollPane_1 = new JScrollPane();
		
		JComboBox modeComboBox = new JComboBox();
		modeComboBox.setEnabled(false);
		modeComboBox.setModel(new DefaultComboBoxModel(new String[] {"One Click Square box", "------"}));
		
		JLabel lblBoxList = new JLabel("Box list");
		
		JLabel lblTag = new JLabel("Tag text");
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
						.addComponent(lblNewLabel)
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 239, Short.MAX_VALUE)
						.addGroup(gl_panel_1.createSequentialGroup()
							.addContainerGap()
							.addComponent(btnAdd)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnRemove))
						.addComponent(scrollPane_1, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 239, Short.MAX_VALUE)
						.addComponent(tagNameComboBox, 0, 239, Short.MAX_VALUE)
						.addComponent(lblBoxList, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
						.addComponent(modeComboBox, Alignment.TRAILING, 0, 239, Short.MAX_VALUE)
						.addComponent(lblTag))
					.addContainerGap())
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addGap(11)
					.addComponent(modeComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(14)
					.addComponent(lblTag)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(tagNameComboBox, GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE)
					.addGap(8)
					.addComponent(lblBoxList)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 113, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnAdd)
						.addComponent(btnRemove))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblNewLabel)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 389, Short.MAX_VALUE))
		);
		
		RectList rectList = new RectList();
		rectList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				_impl.handleSelectBoxList();
			}
		});
		rectList.setFont(new Font("ＭＳ Ｐゴシック", Font.PLAIN, 12));
		scrollPane_1.setViewportView(rectList);
		DirectoryFileList fileList = new DirectoryFileList();
		fileList.setFont(new Font("ＭＳ Ｐゴシック", Font.PLAIN, 12));
		scrollPane.setViewportView(fileList);
		fileList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				_impl.handleSelectFile();
			}
		});
		fileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this._impl.filelist=fileList;
		panel_1.setLayout(gl_panel_1);
		frmManaclipper.getContentPane().setLayout(groupLayout);
		this._impl.frame=frmManaclipper;
		this._impl.preview=panel;
		
		Canvas canvas = new Canvas();
		panel.add(canvas);
		this._impl.tagname=tagNameComboBox;
		this._impl.rectlist=rectList;
		
		JMenuBar menuBar = new JMenuBar();
		frmManaclipper.setJMenuBar(menuBar);
		
		JMenu mnNewMenu = new JMenu("File");
		menuBar.add(mnNewMenu);
		
		JMenu mnImageDirectory = new JMenu("Image Directory");
		mnNewMenu.add(mnImageDirectory);
		
		JMenuItem mntmNewMenuItem = new JMenuItem("Select new directory");
		mnImageDirectory.add(mntmNewMenuItem);
		
		JMenuItem mntmNewMenuItem_2 = new JMenuItem("Append directory");
		mntmNewMenuItem_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				_impl.handleAppendDirectory();
			}
		});
		mnImageDirectory.add(mntmNewMenuItem_2);
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				_impl.handleSelectNewDirectory();
			}
		});
		mnNewMenu.addSeparator();
		JMenuItem mntmLoadClipperxml = new JMenuItem("Save to ClipperXML");
		mntmLoadClipperxml.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				_impl.handleSaveToFile();
			}
		});
		mnNewMenu.add(mntmLoadClipperxml);
		
		JMenuItem mntmSaveClipperxml = new JMenuItem("Load from ClipperXML");
		mntmSaveClipperxml.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				_impl.handleLoadFromFile();
			}
		});
		mnNewMenu.add(mntmSaveClipperxml);
		
		JMenu mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);
		
		JMenuItem mntmRemoveUnexistFiles = new JMenuItem("Cleanup unexistent files");
		mntmRemoveUnexistFiles.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				_impl.handleDeleteUnexistFiles();
			}
		});
		mnEdit.add(mntmRemoveUnexistFiles);
		
		JMenu mnGenerate = new JMenu("Generate");
		menuBar.add(mnGenerate);
		
		JMenuItem mntmNewMenuItem_1 = new JMenuItem("dlib XML");
		mntmNewMenuItem_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				_impl.handleDlibExport();
			}
		});
		mnGenerate.add(mntmNewMenuItem_1);
		
		JMenuItem mntmNewMenuItem_4 = new JMenuItem("kfRcnTextFile");
		mntmNewMenuItem_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				_impl.handleJkfRcnExport();
			}
		});
		mnGenerate.add(mntmNewMenuItem_4);
		
		JMenuItem mntmNewMenuItem_3 = new JMenuItem("About");
		mntmNewMenuItem_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(frmManaclipper,String.format("ManaClipper/0.0.4\n (c)2018 nyatla.jp"));
			}
		});
		menuBar.add(mntmNewMenuItem_3);
		//this._impl.loadDirectory(new File("D:\\media.files\\temp\\pya"));
	}
}

class AppImpl{
	public RegisteredCombobox tagname;
	public JFrame frame;
	public DirectoryFileList filelist;
	public PreviewPanel preview;
	public RectList rectlist;
	private int cursor_size=100;
	private int cursor_x=0;
	private int cursor_y=0;
	File lastDirectory=new File( System.getProperty("user.dir"));


	/**
	 * ディレクトリをセットする。
	 */
	public void handleSelectNewDirectory()
	{
		JFileChooser filechooser = new JFileChooser();
		filechooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		filechooser.setDialogTitle("Select Images Directory");
		filechooser.setCurrentDirectory(lastDirectory);
		if(filechooser.showOpenDialog(this.frame)!=JFileChooser.APPROVE_OPTION) {
			return;
		}
		lastDirectory=filechooser.getSelectedFile().getParentFile();
		//最終確認
	    int option = JOptionPane.showConfirmDialog(this.frame, "Are you sure to override current data?","Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
	    if(option != JOptionPane.YES_OPTION){
	    	return;
	    }
		File f=filechooser.getSelectedFile();
		this.loadDirectory(f);
	}
	/** TagComboboxの値を選択されているボックスのものに変更する*/
	public void handleSelectBoxList() {
		int findex=this.filelist.getSelectedIndex();
		if(findex<0) {
			return;
		}
		int rindex=this.rectlist.getSelectedIndex();
		if(rindex<0) {
			return;
		}
		ClipDataset cds=this.filelist.getAttached();
		BoxInfo box=cds.get(findex).getBoxInfo().get(rindex);
		this.tagname.getEditor().setItem(box.tag);
	}
	/** Tagの値で選択されているボックスを更新する。*/
	public void handleTagChanged() {
		//矩形リストが選択されていたら更新する
		int findex=this.filelist.getSelectedIndex();
		if(findex<0) {
			return;
		}
		int rindex=this.rectlist.getSelectedIndex();
		if(rindex<0) {
			return;
		}
		Object s=this.tagname.getEditor().getItem();
		String str=s==null?"":s.toString();
		ClipDataset cds=this.filelist.getAttached();
		BoxInfo box=cds.get(findex).getBoxInfo().get(rindex);
		if(str.compareTo(box.tag)==0) {
			return;
		}
		cds.get(findex).getBoxInfo().set(rindex,new BoxInfo(box.l,box.t,box.w,box.h,str));

		this.rectlist.repaint();
		this.preview.repaint();
	}
	public void handleOpenCvTrainText() {
		
	}



	private boolean confirmNotEmptyDir() {
		//空ではないディレクトリの場合
	    int option = JOptionPane.showConfirmDialog(this.frame, 
	    		  "The directory is not empty. All some name file will be overwritten. "
	    		+ "Do you want to continue?","Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
	    if(option != JOptionPane.YES_OPTION){
	    	return false;
	    }
		return true;
	}
	/** ファイルが既に存在する場合に上書きチェック*/
	private boolean confirmFileAlreadyExist(File f) {
		if(!f.exists()) {
			return true;
		}
		//空ではないディレクトリの場合
	    int option = JOptionPane.showConfirmDialog(this.frame, 
	    		  "The file is already exist. "
	    		+ "Do you want to override and continue?","Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
	    if(option != JOptionPane.YES_OPTION){
	    	return false;
	    }
		return true;
	}

	private boolean confirmMakeDir() {
		//ディレクトリがない場合
	    int option = JOptionPane.showConfirmDialog(this.frame, 
	    		  "Do you want to make directory?","Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
	    if(option != JOptionPane.YES_OPTION){
	    	return false;
	    }
	    return true;
	}
	private File prepareParentDir(File ftxt)
	{
		File fdir=ftxt.getParentFile();
		if(fdir.exists() && fdir.isDirectory() && fdir.list().length>0) {
			if(!confirmNotEmptyDir()) {
				return null;
			}
		}else if(!fdir.exists()) {
			if(!confirmMakeDir()) {
				return null;
			}
		    if(!fdir.mkdirs()) {
			    JOptionPane.showConfirmDialog(this.frame,"Can not make the directory.","Confirm", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
			    return null;
		    }
		}
		return fdir;
	}
	public void handleJkfRcnExport() {
		Export_jinfagang_keras_frcnn_TrainTextDlg dlg=new Export_jinfagang_keras_frcnn_TrainTextDlg();
		Export_jinfagang_keras_frcnn_TrainTextDlg.Result r=dlg.doModal("rcnn_labels.txt");
		if(r==null) {
			return;
		}
		
		File ftxt=new File(r.output_xml_path);
		if(!confirmFileAlreadyExist(ftxt)) {
			return;
		}
		try {
			JkfRcnLebelText labelfile=new JkfRcnLebelText();
			ClipDataset subset=filelist.getAttached().getSubset(r.filter);
			switch(r.makeImageMode) {
			case NO_COPY:
				for(ClipDataset.Item i:subset)
				{
					for(BoxInfo b:i.getBoxInfo()) {
						labelfile.add(new JkfRcnLebelText.Item(i.path,b));
					}
				}
				break;
			case MAKE_PATCH:
				{
					File fdir=this.prepareParentDir(ftxt);
					int filenumber=0;
					for(ClipDataset.Item i:subset) {
						BufferedImage im=ImageIO.read(new File(i.path));
						for(BoxInfo b:i.getBoxInfo()) {
							BufferedImage subimg = im.getSubimage(b.l,b.t,b.w,b.h);
							String imagename=String.format("%05d.jpg", filenumber);
							ImageIO.write(subimg,"jpg",new File(fdir,imagename));
							labelfile.add(new JkfRcnLebelText.Item(imagename,new BoxInfo(0,0,b.w,b.h,b.tag)));
							filenumber++;
						}
					}
				}
				break;
			case MAKE_RESIZED_PATCH:
				{
					File fdir=this.prepareParentDir(ftxt);
					int filenumber=0;
					for(ClipDataset.Item i:subset) {
						BufferedImage im=ImageIO.read(new File(i.path));
						for(BoxInfo b:i.getBoxInfo()) {
							BufferedImage subimg = im.getSubimage(b.l,b.t,b.w,b.h);
							String imagename=String.format("%05d.png", filenumber);
							ImageIO.write(resizeImage(subimg,r.width,r.height),"png",new File(fdir,imagename));	
							labelfile.add(new JkfRcnLebelText.Item(imagename,new BoxInfo(0,0,r.width,r.height,b.tag)));
							filenumber++;
						}
					}			
				}
				break;
			case COPY_IMAGE:
			{
				File fdir=this.prepareParentDir(ftxt);
				int filenumber=0;
				for(ClipDataset.Item i:subset) {
					String imagename=String.format("%05d.png", filenumber);
					Files.copy(new File(i.path).toPath(),new File(fdir,imagename).toPath(), StandardCopyOption.REPLACE_EXISTING);
					filenumber++;
					for(BoxInfo b:i.getBoxInfo()) {
						labelfile.add(new JkfRcnLebelText.Item(imagename,new BoxInfo(0,0,b.w,b.h,b.tag)));
					}
				}			
			}
			break;				
			default:
				throw new RuntimeException("Invalid case!");
			}
			//ファイルの保�?
			JkfRcnLebelText.saveToFile(labelfile,ftxt);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}	
	
	
	
	
	public void handleDlibExport() {
		ExportDlibDlg dlg=new ExportDlibDlg();
		ExportDlibDlg.Result r=dlg.doModal();
		if(r==null) {
			return;
		}
		//
		File fxml=new File(r.output_xml_path);
		if(!r.makeImage) {
			//
			if(!confirmFileAlreadyExist(fxml)) {
				return;
			}
			DlibXml xml=new DlibXml();
			for(ClipDataset.Item i:filelist.getAttached()) {
				//
				File image_file=new File(i.path);
				if(!image_file.exists()) {
					continue;
				}
				//
				if(!i.getBoxInfo().isEmpty()) {
					DlibXml.Image xim=new DlibXml.Image();
					xim.file=i.path;
					for(BoxInfo b:i.getBoxInfo()) {
						if(!r.filter.isEmpty() && !r.filter.contains(b.tag)) {
							continue;
						}
						xim.boxes.add(new DlibXml.Box(b));
					}
					if(xim.boxes.isEmpty()) {
						continue;
					}
					xml.images.add(xim);
				}
				DlibXml.saveToFile(xml,fxml);
			}			
		}else {
			//
			File fdir=fxml.getParentFile();
			if(fdir.exists() && fdir.isDirectory() && fdir.list().length>0) {
				//
				if(!confirmNotEmptyDir()) {
					return;
				}
			}else if(!fdir.exists()) {
				if(!confirmMakeDir()) {
					return;
				}				
				//
			    if(!fdir.mkdirs()) {
				    JOptionPane.showConfirmDialog(this.frame,"Can not make the directory.","Confirm", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
				    return;
			    }
			}
			int filenumber=0;
			DlibXml xml=new DlibXml();
			//XML
			for(ClipDataset.Item i:filelist.getAttached()) {
				//
				File image_file=new File(i.path);
				if(!image_file.exists()) {
					continue;
				}
				//
				if(i.getBoxInfo().isEmpty()) {
					continue;
				}
				//
				try {
					BufferedImage im=ImageIO.read(image_file);
					for(BoxInfo b:i.getBoxInfo()) {
						if(!r.filter.isEmpty() && !r.filter.contains(b.tag)) {
							continue;
						}
						BufferedImage subimg = im.getSubimage(b.l,b.t,b.w,b.h);
						String imagename=String.format("%05d.png", filenumber);
						DlibXml.Image xim=new DlibXml.Image();
						xim.file=imagename;
						//
						if(!r.makeResize) {
							ImageIO.write(subimg,"png",new File(fdir,imagename));
							xim.boxes.add(new DlibXml.Box(0,0,b.w,b.h,b.tag));
							
						}else {
							ImageIO.write(resizeImage(subimg,r.width,r.height),"png",new File(fdir,imagename));	
							xim.boxes.add(new DlibXml.Box(0,0,r.width,r.height,b.tag));
						}
						xml.images.add(xim);
						filenumber++;
					}
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
			DlibXml.saveToFile(xml,fxml);
		}
	}
	/**
	 * 
	 */
	public void handleDeleteUnexistFiles() {
		//
	    int option = JOptionPane.showConfirmDialog(this.frame, "Are you sure to be cleanup the filelist?","Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
	    if(option != JOptionPane.YES_OPTION){
	    	return;
	    }
	    int n=0;
	    ClipDataset current=filelist.getAttached();
	    for(int i=current.size()-1;i>=0;i--) {
	    	if(new File(current.get(i).path).exists()) {
	    		continue;
	    	}
	    	n++;
	    	current.remove(i);
	    }
		JOptionPane.showMessageDialog(frame,String.format("remove %d/ skip %d",n,current.size()-n));
		loadCds(current);
	}
	/**
	 * 
	 */
	public void handleAppendDirectory() {
		JFileChooser filechooser = new JFileChooser();
		filechooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		filechooser.setDialogTitle("Select Appended Images Directory");
		filechooser.setCurrentDirectory(lastDirectory);
		if(filechooser.showOpenDialog(this.frame)!=JFileChooser.APPROVE_OPTION) {
			return;
		}
		lastDirectory=filechooser.getSelectedFile().getParentFile();
		
	    int option = JOptionPane.showConfirmDialog(this.frame, "Are you sure to append data to the filelist?","Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
	    if(option != JOptionPane.YES_OPTION){
	    	return;
	    }
		File f=filechooser.getSelectedFile();
		
		ClipDataset current=filelist.getAttached();
		int n=0;
		ClipDataset append=Utils.createFromDir(f);
		for(ClipDataset.Item i:append) {
			if(current.add(i.path)) {
				n++;
			};
		}
		JOptionPane.showMessageDialog(frame,String.format("add %d/ skip %d",n,append.size()-n));
		loadCds(current);
	}
	public void handleLoadFromFile() {
		JFileChooser filechooser = new JFileChooser();
		filechooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		filechooser.setDialogTitle("Load from ManaClipperXML");
		filechooser.setCurrentDirectory(lastDirectory);
		filechooser.setSelectedFile(new File("untitled.manaclipper.xml"));
		if(filechooser.showOpenDialog(this.frame)!=JFileChooser.APPROVE_OPTION) {
			return;
		}
		lastDirectory=filechooser.getSelectedFile().getParentFile();
		//
	    int option = JOptionPane.showConfirmDialog(this.frame, "Are you sure override current data?","Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
	    if(option != JOptionPane.YES_OPTION){
	    	return;
	    }
		
		ManaClipperXml xml=ManaClipperXml.loadFromFile(filechooser.getSelectedFile());
		loadCds(xml.toClipData());
	}
	/**
	 * XML
	 */
	public void handleSaveToFile() {
		JFileChooser filechooser = new JFileChooser();
		filechooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		filechooser.setDialogTitle("Save to ManaClipperXML");
		filechooser.setCurrentDirectory(lastDirectory);
		filechooser.setSelectedFile(new File("untitled.manaclipper.xml"));
		if(filechooser.showOpenDialog(this.frame)!=JFileChooser.APPROVE_OPTION) {
			return;
		}
		lastDirectory=filechooser.getSelectedFile().getParentFile();
		ManaClipperXml.saveToFile(new ManaClipperXml(this.filelist.getAttached()),filechooser.getSelectedFile());
	}
	public void handleDeleteClip() {
		rectlist.deleteSelected();
		this.preview.repaint();
	}
	/**
	 * 
	 */
	public void handleAddClip()
	{
		//ComboBox
		tagname.addCurrentTextIfNot();

		int index=this.filelist.getSelectedIndex();
		if(index<0) {
			return;
		}
		if(!this.preview.isInnerCursol()) {
			return;
		}
		//
		Object s=this.tagname.getEditor().getItem();
		int[] ltwh=this.preview.getImageCursor();
		rectlist.addRect(ltwh[0],ltwh[1],ltwh[2],ltwh[3],s==null?"":s.toString());
		this.filelist.repaint();
		this.preview.repaint();
	}
	public void handleCursolWheel(int i)
	{
		this.cursor_size+=i;
		this.cursor_size=Math.max(5,this.cursor_size);
		this.cursor_size=Math.min(1000,this.cursor_size);
		this.preview.setClientCursor(this.cursor_x,this.cursor_y,this.cursor_size,this.cursor_size);
	}
	public void handleCursorRect(int ix, int iy) {
		this.cursor_x=ix;
		this.cursor_y=iy;
		this.preview.setClientCursor(this.cursor_x,this.cursor_y,this.cursor_size,this.cursor_size);
	}
	public void handleSelectFile()
	{
		//
		int i_index=this.filelist.getSelectedIndex();
		if(i_index<0) {
			this.preview.setImage(null,null);
			this.rectlist.detache();
			return;
		}
		ClipDataset.Item item=this.filelist.getDataSetItem(i_index);
		File image_file=new File(item.path);
		if(!image_file.exists()) {
			//
			this.preview.setImage(null,null);
			this.rectlist.attach(item);
		}else {
			try {
				this.preview.setImage(ImageIO.read(image_file),item.getBoxInfo());
				this.rectlist.attach(item);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
	public void handleModeSelect()
	{

	}
	/**
	 * 
	 * @param org
	 * @param w
	 * @param h
	 * @return
	 * @throws IOException
	 */
    public static BufferedImage resizeImage(BufferedImage org, int w,int h) throws IOException {
        ImageFilter filter = new AreaAveragingScaleFilter((w),(h));
        ImageProducer p = new FilteredImageSource(org.getSource(), filter);
        java.awt.Image dstImage = Toolkit.getDefaultToolkit().createImage(p);
        BufferedImage dst = new BufferedImage(dstImage.getWidth(null), dstImage.getHeight(null), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = dst.createGraphics();
        g.drawImage(dstImage, 0, 0, null);
        g.dispose();
        return dst;
    }
	
	/**
	 * 
	 * @param i_path
	 */
	public void loadDirectory(File i_path)
	{
		this.loadCds(Utils.createFromDir(i_path));
	}
	public void loadCds(ClipDataset i_cds)
	{
		//
		this.tagname.resetTags(i_cds);
		this.rectlist.detache();
		this.filelist.attach(i_cds);
	}
	
}