package jp.nyatla.manacliper.ctrl;

import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.DefaultListModel;
import javax.swing.JList;

import jp.nyatla.manacliper.type.ClipDataset;
import jp.nyatla.manacliper.type.ClipDataset.BoxInfo;

/**
 * 参照しているItemのBoxInfoを編集するJList.
 * アタッチしてる間はBoxInfoを占有させといて。
 *
 */
public class RectList extends JList<String>{
	private static final long serialVersionUID = 1820405942594655002L;

	static private class Model extends AbstractListModel<String>{
		private static final long serialVersionUID = 4268957789849735218L;
		private List<BoxInfo> _ref_l;
		public Model(List<BoxInfo> boxlist) {
			super();
			this._ref_l=boxlist;
		}
		@Override
		public String getElementAt(int arg0) {
			ClipDataset.BoxInfo b=this._ref_l.get(arg0);
			return String.format("(%d,%d-%dx%d) %s",b.l,b.t,b.w,b.h,b.tag);
		}
		@Override
		public int getSize() {
			return this._ref_l.size();
		}
		public void remove(int idx) {
			this._ref_l.remove(idx);
			this.fireIntervalRemoved(this,idx,idx);
		}
		public void add(int l,int t,int w,int h,String tag) {
			this._ref_l.add(new BoxInfo(l,t,w,h,tag));
			int last=this._ref_l.size()-1;
			this.fireIntervalAdded(this,last,last);
		}
	}
	public RectList() {}
	private ClipDataset.Item _attached=null;
	public ClipDataset.Item attach(ClipDataset.Item item){
		ClipDataset.Item old=this._attached;
		this.setModel(new Model(item.getBoxInfo()));
		_attached=item;
		return old;
	}
	public ClipDataset.Item detache(){
		ClipDataset.Item old=this._attached;
		this.setModel(new DefaultListModel<String>());
		_attached=null;
		return old;
	}	
	public void deleteSelected()
	{
		if(this._attached==null) {
			return;
		}
		int idx=this.getSelectedIndex();
		if(idx<0) {
			return;
		}
		Model m=(Model)this.getModel();
		m.remove(idx);
		if(m.getSize()-1>=idx) {
			this.setSelectedIndex(idx);
		}
		
	}
	public boolean addRect(int l,int t,int w,int h,String tag) {
		if(this._attached==null) {
			return false;
		}
		Model m=(Model)this.getModel();
		m.add(l,t,w,h,tag);
		this.setSelectedIndex(m.getSize()-1);
		return true;
	}
}
