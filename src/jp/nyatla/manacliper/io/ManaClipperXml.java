package jp.nyatla.manacliper.io;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import jp.nyatla.manacliper.type.ClipDataset;
/**
 * 独自形式
 */
@XmlRootElement
public class ManaClipperXml {
	public String version="ManaClipperXml/0.1";
	@XmlType(name="image")
	static public class Image{
		@XmlAttribute
		public String filename="";
		@XmlElementWrapper(name = "boxes")
		@XmlElement(name="box")		
		public List<Box> boxes=new ArrayList<Box>();
	}
	@XmlType(name="box")
	static public class Box{
		@XmlAttribute
		public String tag="";
		@XmlAttribute
		public int left=0;
		@XmlAttribute
		public int top=0;
		@XmlAttribute
		public int width=0;
		@XmlAttribute
		public int height=0;
	}
	public String path;
	@XmlElementWrapper(name = "Images")
	@XmlElement(name="Image")		
	public List<Image> images=new ArrayList<Image>();
	
	public ClipDataset toClipData()
	{
		ClipDataset ci=new ClipDataset();
		for(Image i:this.images) {
			ci.add(i.filename);
			ClipDataset.Item item=ci.get(ci.size()-1);
			for(Box j:i.boxes) {
				item.getBoxInfo().add(new ClipDataset.BoxInfo(j.left,j.top,j.width,j.height,j.tag));
			}
		}
		return ci;
	}
	public ManaClipperXml() {
		
	}
	public ManaClipperXml(ClipDataset i_from) {
		for(ClipDataset.Item i:i_from) {
			Image im=new Image();
			im.filename=i.path;
			for(ClipDataset.BoxInfo j:i.getBoxInfo()) {
				Box box=new Box();
				box.left=j.l;
				box.top=j.t;
				box.width=j.w;
				box.height=j.h;
				box.tag=j.tag;
				im.boxes.add(box);
			}
			this.images.add(im);
		}
	}
	
	public static ManaClipperXml loadFromFile(File i_file)
	{
		JAXBContext context;
		try {
			context = JAXBContext.newInstance(ManaClipperXml.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			return (ManaClipperXml)unmarshaller.unmarshal(i_file);
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
	}
	public static void saveToFile(ManaClipperXml i_object,File i_file){
		JAXBContext context;
		try {
			context = JAXBContext.newInstance(ManaClipperXml.class);
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,true);

			marshaller.marshal(i_object,i_file);
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
	}		
}
