package jp.nyatla.manacliper.ctrl;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.JPanel;

import jp.nyatla.manacliper.type.ClipDataset;
import jp.nyatla.manacliper.type.ClipDataset.BoxInfo;

public class PreviewPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	public Image _img=null;
	private List<BoxInfo> _box=null;
	final private Rectangle _client_cursor=new Rectangle();
	public void setImage(Image i_img, List<ClipDataset.BoxInfo> i_box)
	{
		this._img=i_img;
    	this._box=i_box;
    	this.setOpaque(true);
		this.repaint();
		this.setOpaque(false);
	}
	
	private double _getZoom()
	{
        if(this._img==null){
        	return 1;
        }
        double iw = this._img.getWidth(null);
        double ih = this._img.getHeight(null);
        double pw = this.getWidth();
        double ph = this.getHeight();
        double ppx=pw/iw;
        double ppy=ph/ih;
        return ppx<ppy?ppx:ppy;
	}
	public boolean isInnerCursol() {
        if(this._img==null){
        	return false;
        }
		double zoom=this._getZoom();
        double iw = this._img.getWidth(null);
        double ih = this._img.getHeight(null);
		return (this._client_cursor.x+this._client_cursor.width)/zoom<iw && (this._client_cursor.y+this._client_cursor.height)/zoom<ih;
	}
	public int[] getImageCursor()
	{
		double zoom=this._getZoom();
		return new int[] {(int)(this._client_cursor.x/zoom),(int)(this._client_cursor.y/zoom),(int)(this._client_cursor.width/zoom),(int)(this._client_cursor.height/zoom)};
	}

	double _last_zoom=0;
	BufferedImage _tmp_img=null;
    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
        if(this._img==null){
        	g2D.setColor(Color.BLACK);
        	g2D.setStroke(new BasicStroke(5));
        	g2D.drawRect(0,0,this.getWidth()-1,this.getHeight()-1);
        }else{
        	
	        double zoom=this._getZoom();
        	if(_last_zoom!=zoom) {
        		this._tmp_img=new BufferedImage((int)(zoom*this._img.getWidth(null)),(int)(zoom*this._img.getHeight(null)),BufferedImage.TYPE_INT_RGB);
    	        AffineTransform af = AffineTransform.getScaleInstance(zoom,zoom);
    	        ((Graphics2D)(this._tmp_img.getGraphics())).drawImage(this._img, af,null);
        	}
	        g2D.drawImage(this._tmp_img,0,0,this);
	        //
	        if(this._box!=null) {
		    	g2D.setColor(new Color(0,0,255,255));
		        for(ClipDataset.BoxInfo i:this._box) {
		        	g2D.drawString(i.tag,(int)(i.l*zoom+2),(int)(i.t*zoom+20));
		        	g2D.setStroke(new BasicStroke(1));
		        	g2D.drawRect((int)(i.l*zoom),(int)(i.t*zoom),(int)(i.w*zoom),(int)(i.h*zoom));
		        }
	        }
	        //カーソルの描画
	        if(this.isInnerCursol()) {
		    	g2D.setColor(new Color(0,255,0,64));
	        }else {
		    	g2D.setColor(new Color(255,0,0,64));
	        }
	    	g2D.setStroke(new BasicStroke(1));
	    	g2D.fillRect(this._client_cursor.x,this._client_cursor.y,this._client_cursor.width,this._client_cursor.height);

        }
    	return;        
    }


    public void setClientCursor(int ix,int iy,int iw,int ih){
    	if(this._img==null){
    		return;
    	}
    	this._client_cursor.x=(int)(ix);
    	this._client_cursor.y=(int)(iy);
    	this._client_cursor.width=(int)(iw);
    	this._client_cursor.height=(int)(ih);
    	
    	this.repaint();
    }
}