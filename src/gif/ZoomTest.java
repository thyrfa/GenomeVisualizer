package gif;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JViewport;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
  
public class ZoomTest implements MouseListener
{
	double squareheight = 10;
	double squarewidth = 10;
	JScrollPane scroll;
	Dimension dim;
	ImagePanel panel;
	
	public ZoomTest(){
		panel = new ImagePanel();
		dim = new Dimension(panel.image.getWidth(), panel.image.getHeight());
        ImageZoom zoom = new ImageZoom(panel);
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.getContentPane().add(zoom.getUIPanel(), "North");
        scroll = new JScrollPane(panel);
        scroll.addMouseListener(this);
        f.getContentPane().add(scroll);
        f.setSize(400,400);
        f.setLocation(200,200);
        f.setVisible(true);
	}
    public static void main(String[] args)
    {
        new ZoomTest();
    }
	@Override
	public void mouseClicked(MouseEvent e) {
		JViewport viewport = scroll.getViewport();
		int xdif=viewport.getSize().width-dim.width;
		int ydif=viewport.getSize().height-dim.height;
		xdif=xdif/2;
		ydif=ydif/2; //ydif and xdif are the difference in location between the edge of the scrollframe and the picture, can find what pixel of the IMAGE was clicked on.
		if (xdif<0){
			xdif=0;
		}
		if (ydif<0){
			ydif=0;
		}
        Point h=viewport.getViewPosition();
        xdif+=2;
        ydif+=2;
        //h is how much the user has scrolled, corrects for that.
		int x=e.getX()+h.x-xdif;
		int y=e.getY()+h.y-ydif;
		int yrow=(int) Math.floor(y/(panel.scale*squareheight));
		int xcol=(int) Math.floor(x/(panel.scale*squarewidth));
		System.out.println(x+", "+y+"||"+xcol+", "+yrow);
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
  
class ImagePanel extends JPanel
{
    BufferedImage image;
    double scale;
  
    public ImagePanel()
    {
        loadImage();
        scale = 1.0;
        setBackground(Color.black);
    }
  
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                            RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        int w = getWidth();
        int h = getHeight();
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        double x = (w - scale * imageWidth)/2;
        double y = (h - scale * imageHeight)/2;
        AffineTransform at = AffineTransform.getTranslateInstance(x,y);
        at.scale(scale, scale);
        g2.drawRenderedImage(image, at);
    }
  
    /**
     * For the scroll pane.
     */
    public Dimension getPreferredSize()
    {
        int w = (int)(scale * image.getWidth());
        int h = (int)(scale * image.getHeight());
        return new Dimension(w, h);
    }
  
    public void setScale(double s)
    {
        scale = s;
        revalidate();      // update the scroll pane
        repaint();
    }
  
    private void loadImage()
    {
		JFileChooser fc = new JFileChooser(new File("C:\\Users\\"+System.getProperty("user.name")+"\\Downloads"));
		int returnVal = fc.showOpenDialog(new JFrame());
	    File f = new File("/");
	    if (returnVal == JFileChooser.APPROVE_OPTION){
	    	f = fc.getSelectedFile();
	    }
        try
        {
            image = ImageIO.read(f);
        }
        catch(MalformedURLException mue)
        {
            System.out.println("URL trouble: " + mue.getMessage());
        }
        catch(IOException ioe)
        {
            System.out.println("read trouble: " + ioe.getMessage());
        }
    }
}
  
class ImageZoom
{
    ImagePanel imagePanel;
  
    public ImageZoom(ImagePanel ip)
    {
        imagePanel = ip;
    }
  
    public JPanel getUIPanel()
    {
        SpinnerNumberModel model = new SpinnerNumberModel(1.0, 0.1, 1.5, .01);
        final JSpinner spinner = new JSpinner(model);
        spinner.setPreferredSize(new Dimension(45, spinner.getPreferredSize().height));
        spinner.addChangeListener(new ChangeListener()
        {
            public void stateChanged(ChangeEvent e)
            {
                float scale = ((Double)spinner.getValue()).floatValue();
                imagePanel.setScale(scale);
            }
        });
        JPanel panel = new JPanel();
        panel.add(new JLabel("scale"));
        panel.add(spinner);
        return panel;
    }
}
