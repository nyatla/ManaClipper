package jp.nyatla.manacliper.io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import jp.nyatla.manacliper.type.ClipDataset;

/**
 * 
 * https://github.com/jinfagang/keras_frcnn
 * keras_frcnn特徴画像定義ファイル形式
 *
 */
public class JkfRcnLebelText extends ArrayList<JkfRcnLebelText.Item>{
	private static final long serialVersionUID = 948002611831593056L;
	static public class Item
	{
		final String path;
		final ClipDataset.BoxInfo box;
		public Item(String path,ClipDataset.BoxInfo box) {
			this.path=path;
			this.box=box;
		}
	}
	static public void saveToFile(JkfRcnLebelText i_object,File i_file) throws IOException
	{
		  FileWriter filewriter = new FileWriter(i_file);
		  for(Item i:i_object) {
			  String s=String.format("%s,%d,%d,%d,%d,%s\n",i.path,i.box.l,i.box.t,i.box.w,i.box.h,i.box.tag);
			  filewriter.write(s);
		  }
		  filewriter.close();

	}
}
