package jp.nyatla.manaclipper.io;

import java.io.File;
import java.util.regex.Pattern;

import jp.nyatla.manaclipper.type.ClipDataset;

public class Utils {
	/**
	 * @param i_directory_path
	 * @return
	 */
	public static ClipDataset createFromDir(File i_directory_path)
	{
		ClipDataset ret=new ClipDataset();
        Pattern p = Pattern.compile(".+\\.(jpe?g|png)$",Pattern.CASE_INSENSITIVE);
        //listFiles
        File[] list = i_directory_path.listFiles();
        for(File i:list) {
        	//
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
