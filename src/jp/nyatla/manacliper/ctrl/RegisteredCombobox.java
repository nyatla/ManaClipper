package jp.nyatla.manacliper.ctrl;

import java.util.HashSet;
import java.util.Set;

import javax.swing.JComboBox;

import jp.nyatla.manacliper.type.ClipDataset;

public class RegisteredCombobox  extends JComboBox<String>{
	private static final long serialVersionUID = 8102181317257264168L;
	private Set<String> list=new HashSet<String>();

	public void addCurrentTextIfNot() {
		Object s=this.getEditor().getItem();
//		Object s=this.getSelectedItem();
		if(s==null) {
			return;
		}
		this.addTextIfNot(s.toString());
	}
	public void addTextIfNot(String s) {
		if(list.contains(s.toString())) {
			return;
		}
		list.add(s.toString());
		super.addItem(s.toString());
		this.getEditor().setItem(s);
	}	
	public void resetTags(ClipDataset ds )
	{
		this.removeAllItems();
		for(ClipDataset.Item i:ds) {
			for(ClipDataset.BoxInfo b:i.getBoxInfo()) {
				if(!b.tag.trim().isEmpty()) {
					this.addTextIfNot(b.tag);
				}
			}
		}
	}
}
