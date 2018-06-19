package jp.nyatla.manacliper.io;

import java.io.File;
import java.util.regex.Pattern;

import jp.nyatla.manacliper.type.ClipDataset;

public class Utils {
	/**
	 * ディレクトリのパスを指定して生成。初期化ファイルは使わない。
	 * @param i_directory_path
	 * @return
	 */
	public static ClipDataset createFromDir(File i_directory_path)
	{
		ClipDataset ret=new ClipDataset();
        Pattern p = Pattern.compile(".+\\.(jpe?g|png)$",Pattern.CASE_INSENSITIVE);
        //listFilesメソッドを使用して一覧を得る。
        File[] list = i_directory_path.listFiles();
        for(File i:list) {
        	//ファイル名ではじく
            if(!p.matcher(i.getName()).matches()){
            	continue;
            }
        	if(!ret.add(i.getPath())) {
        		System.out.println("Already exist "+i.getPath());
        		continue;
        	}
        }
        return ret;
	}
}
