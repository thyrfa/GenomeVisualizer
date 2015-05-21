package gif;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
  
public class ZoomPanel
{
	double squareheight = 10;
	double squarewidth = 10;
	JScrollPane scroll;
	Dimension dim;
	ImagePanel panel;
	
	public ZoomPanel(MouseWatcher m, BufferedImage i){
        JFrame f = new JFrame();
		panel = new ImagePanel(i, f);
		dim = new Dimension(panel.image.getWidth(), panel.image.getHeight());
        ImageZoom zoom = new ImageZoom(panel);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.getContentPane().add(zoom.getUIPanel(), "North");
        scroll = new JScrollPane(panel);
        panel.setSBDimension(scroll.getHorizontalScrollBar().getPreferredSize(), scroll.getVerticalScrollBar().getPreferredSize());
        scroll.addMouseListener(m);
        f.getContentPane().add(scroll);
        f.setExtendedState(JFrame.MAXIMIZED_BOTH);
        f.setVisible(true);
	}
}

class ImagePanel extends JPanel
{
    BufferedImage image;
    double scale;
    JFrame cont;
    int spinHeight;
    Dimension vScroll;
    Dimension hScroll;
  
    public ImagePanel(BufferedImage i, JFrame f)
    {
    	cont = f;
    	image = i;
        scale = 1.0;
        setBackground(Color.black);
    }
    public void setSpinHeight(int i){
    	spinHeight = i;
    }
    public void setSBDimension(Dimension d1, Dimension d2){
    	 hScroll = d1;
    	 vScroll = d2;
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
        if (scale<1){
        	cont.setSize((int)(getPreferredSize().width - vScroll.getWidth()), (int)(getPreferredSize().height - spinHeight - hScroll.getHeight()));
        }
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
        SpinnerNumberModel model = new SpinnerNumberModel(1.0, 0.5, 2.0, .01);
        final JSpinner spinner = new JSpinner(model);
        spinner.setPreferredSize(new Dimension(45, spinner.getPreferredSize().height));
        imagePanel.setSpinHeight(spinner.getPreferredSize().height);
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
