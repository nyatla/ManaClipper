package jp.nyatla.manacliper.ctrl;


import javax.swing.AbstractListModel;
import javax.swing.JList;

import jp.nyatla.manacliper.type.ClipDataset;
import jp.nyatla.manacliper.type.ClipDataset.Item;

public class DirectoryFileList extends JList<String>{
	private static final long serialVersionUID = 8388693799051674714L;
	private ClipDataset _attached = new ClipDataset();
	static private class Model extends AbstractListModel<String>{
		private static final long serialVersionUID = -4007171834286505432L;
		private ClipDataset _ref_l;
		public Model(ClipDataset boxlist) {
			super();
			this._ref_l=boxlist;
		}
		@Override
		public String getElementAt(int arg0) {
			ClipDataset.Item b=this._ref_l.get(arg0);
			return String.format("[%02d]%s",b.getBoxInfo().size(),b.path);
		}
		@Override
		public int getSize() {
			return this._ref_l.size();
		}
	}	
	public ClipDataset attach(ClipDataset item){
		ClipDataset old=this._attached;
		this.setSelectedIndex(-1);
		this.setModel(new Model(item));
		_attached=item;
		return old;
	}
	public Item getDataSetItem(int i_index) {
		return this._attached.get(i_index);
	}
	public ClipDataset getAttached()
	{
		return this._attached;
	}
}
