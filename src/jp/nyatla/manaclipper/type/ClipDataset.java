package jp.nyatla.manaclipper.type;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jp.nyatla.manaclipper.io.JkfRcnLebelText;
import jp.nyatla.manaclipper.type.ClipDataset.BoxInfo;


/**
 * アノテーションデータの格納先
 *
 */
public class ClipDataset implements Iterable<ClipDataset.Item>{
	static public class BoxInfo{
		final public int l;
		final public int t;
		final public int w;
		final public int h;
		final public String tag;
		public BoxInfo(int il,int it,int iw,int ih,String itag)
		{
			this.l=il;
			this.t=it;
			this.w=iw;
			this.h=ih;
			this.tag=itag;
		}
	}
	public class Item{
		public final String path;
		public Item(String i_path) {
			this.path=i_path;
		}
		final private List<BoxInfo> _boxinfo=new ArrayList<BoxInfo>();
		public List<BoxInfo> getBoxInfo(){
			return this._boxinfo;
		}
		/**
		 * Boxを追加します。
		 */
		public void addBox(BoxInfo box) {
			this._boxinfo.add(box);
		}
	}
	private final Map<String,Integer> _map=new HashMap<String,Integer>();
	private final List<Item> _items=new ArrayList<Item>();
	public ClipDataset() {
	}
	/**
	 * フィルタに合致するboxを含むサブセットを得る。
	 * @param target_tags
	 * @return
	 */
	public ClipDataset getSubset(List<String> target_tags)
	{
		ClipDataset ret=new ClipDataset();
		for(ClipDataset.Item i:this._items)
		{
			//ファイルは存在する？
			File image_file=new File(i.path);
			if(!image_file.exists()) {
				continue;
			}
			//boxはある？
			if(i.getBoxInfo().isEmpty()) {
				continue;
			}
			ClipDataset.Item item=new ClipDataset.Item(i.path);
			for(BoxInfo b:i.getBoxInfo()) {
				if(!target_tags.isEmpty() && !target_tags.contains(b.tag)) {
					continue;
				}
				if(b.tag.isEmpty()) {
					continue;
				}
				item.addBox(b);
			}
			if(item.getBoxInfo().isEmpty()) {
				continue;
			}
			ret._items.add(item);
		}
		return ret;
	}
	/**
	 * ファイルを追加する
	 */
	public boolean add(String i_file_path) {
		//
		Integer idx=this._map.get(i_file_path);
		if(idx!=null) {
			return false;
		}
		if(!this._items.add(new Item(i_file_path))) {
			return false;
		}
		this._map.put(i_file_path,this._items.size()-1);
		return true;
	};
	
	public Item indexOf(String i_path) {
		Integer idx=this._map.get(i_path);
		if(idx==null) {
			return null;
		}
		return this._items.get(idx);
	};
	public int size() {
		return this._items.size();
	}
	public Item get(int i_index) {
		return this._items.get(i_index);
	}
	public void clear() {
		this._map.clear();
		this._items.clear();
	}
	@Override
	public Iterator<Item> iterator() {
		return this._items.iterator();
	}
	public void remove(int i) {
		this._map.remove(this._items.get(i).path);
		this._items.remove(i);
	}
}
