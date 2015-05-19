package gif;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Zoomable extends JPanel implements Runnable {

AffineTransform tx = new AffineTransform();


public Zoomable() {
this.addMouseWheelListener(new ZoomHandler());
}

@Override
public void paint(Graphics g) {
super.paint(g);
}

private class ZoomHandler implements MouseWheelListener {

double scale = 1.0;

public void mouseWheelMoved(MouseWheelEvent e) {
	if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
		Point2D p1 = e.getPoint();
		Point2D p2 = null;
		try {
			p2 = tx.inverseTransform(p1, null);
		} catch (NoninvertibleTransformException ex) {
			ex.printStackTrace();
			return;
		}
		scale -= (0.1 * e.getWheelRotation());
		scale = Math.max(0.1, scale);
		tx.setToIdentity();
		tx.translate(p1.getX(), p1.getY());
		tx.scale(scale, scale);
		tx.translate(-p2.getX(), -p2.getY());
		Zoomable.this.revalidate();
		Zoomable.this.repaint();
		}
	}
}

	public void run() {
		JFrame f = new JFrame("Zoom Demo");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.getContentPane().add(this);
		f.setSize(600, 600);
		f.setLocationRelativeTo(null);
		f.setVisible(true);
	}
}