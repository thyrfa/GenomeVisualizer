package gif;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MouseWatcher implements MouseListener {
	Generator g;
	PairGenerator p;
	TotalGenerator t;
	public MouseWatcher(Generator gs){
		g=gs;
	}
	public MouseWatcher(PairGenerator ps){
		p=ps;
	}
	public MouseWatcher(TotalGenerator ts){
		t=ts;
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		if (g==null&&t==null){
			p.mouseClicked(e);
		}
		else if (t==null&&p==null){
			g.mouseClicked(e);
		}
		else{
			t.mouseClicked(e);
		}
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
