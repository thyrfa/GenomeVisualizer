package gif;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.JViewport;

//Generate uncompressed singles
public class TotalGenerator {
	public HashMap<Character, Color> colors=new HashMap<Character, Color>();
	public final String fileprefix;
	JFrame frame;
	public int max=0;
	public int modenum=0;
	public int modetwo=0;
	public HashMap<Integer, Integer> mode= new HashMap<Integer, Integer>();
	public File a;
	public HashMap<Character, String> basetobase= new HashMap<Character, String>();
	JFrame panel;
	double width;
	double height;
	JTextField acolor;
	JTextField ccolor;
	JTextField tcolor;
	JTextField gcolor;
	int sqrt;
	int numchars;
	int squareheight;
	int squarewidth;
	boolean pair=false;
	ArrayList<Character> list;
	int vert=0;
	int labx;
	int laby;
	Dimension dim;
	BufferedImage image;
	int counter;
	File saver;
	ZoomPanel zPanel;
	
	public TotalGenerator(File save, String pre, int[] dim) {
		fileprefix = pre;
		colors.put('a', Color.red.darker());
		colors.put('g', Color.green.darker());
		colors.put('c', Color.blue.darker());
		colors.put('t', Color.yellow.darker());
		colors.put('n', Color.BLACK);
		basetobase.put('a', "Adenine");
		basetobase.put('g', "Guanine");
		basetobase.put('t', "Thymine");
		basetobase.put('n', "Unknown");
		basetobase.put('c', "Cytosine");
		squarewidth = dim[0];
		squareheight = dim[1];
		sqrt = dim[2];
		saver=save;
	}
	public void readList(File f, int q){
		vert=q;
		try{
			saver.createNewFile();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		boolean t=true;
		StringBuilder s=new StringBuilder();
		int c;
		int n;
		char ch='@';
		int total=0;
		int dots=0;
		boolean inlast=false;
		boolean ranone=false;
		boolean rantwo=false;
		boolean ranthree=false;
		try{
			FileReader countz = new FileReader(new File(f.getParentFile().getAbsolutePath()+fileprefix+f.getName().substring(0, f.getName().indexOf("."))+".counter"));
			BufferedReader cin = new BufferedReader(countz);
			String counts = cin.readLine();
			int num = Integer.parseInt(counts.substring(counts.indexOf("to")+3));
			cin.close();
			countz.close();
			list = new ArrayList<Character>(num);
			FileReader ist = new FileReader(f);
			BufferedReader in = new BufferedReader(ist);
			while(t==true){
				c=in.read();
				if (ch=='z'){
					inlast=true;
					s=new StringBuilder();
				}
				if (c==-1){
					numchars=Integer.parseInt(s.toString());
					t=false;
				}
				else if (inlast){
					ch='@';
					if ((char)c=='z'){
						dots++;
					}
					else{
						s.append((char)c);
					}
					if (dots==1&&!ranone){
						max=Integer.parseInt(s.toString());
						s=new StringBuilder();
						ranone=true;
					}
					else if (dots==2&&!rantwo){
						modenum=Integer.parseInt(s.toString());
						s=new StringBuilder();
						rantwo=true;
					}
					else if (dots==3&&!ranthree){
						counter=Integer.parseInt(s.toString());
						s=new StringBuilder();
						ranthree=true;
					}
				}
				else if ((char)c!=ch&&(char)c!='0'&&ch!='@'){
					n=Integer.parseInt(s.toString().replace(ch, '1'), 2);
					if (n>20000){
						n=20000;
					}
					total+=n;
					counter++;
					for (int z = 0; z<n; z++){
						list.add(ch);
					}
					s= new StringBuilder();
					ch=(char)c;
					s.append((char)c);
				}
				else if ((char)c!=ch&&(char)c!='0'){
					ch=(char)c;
					s.append((char)c);
				}
				else{
					s.append((char)c);
				}				
			}
			in.close();
			ist.close();
		}
		catch(Throwable e){
			e.printStackTrace();
			System.out.println(e.getCause());
		}
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		list.trimToSize();
		width = screenSize.getWidth();
		height = screenSize.getHeight();
		numchars=total;
		System.out.println("End 1");
		if (q==1){
			makeImage();
		}
		else if (q==-1){
			makeSnakeImage();
		}
	}
	public void makeImage(){
		//System.out.println(xsize.getText());
		//System.out.println(ysize.getText());
		panel=new JFrame("Horizontal");
		panel.setLayout(new BorderLayout());
		int k=0;
		int lastindex=list.size()-1;
		image = new BufferedImage(sqrt*squarewidth, (int)(squareheight*Math.ceil(lastindex/(double)sqrt)), BufferedImage.TYPE_INT_RGB);
		dim= new Dimension(image.getWidth(), image.getHeight());
		Color c;
		Graphics g = image.getGraphics();
		int listindex=0;
		for (int i=0; k<lastindex;i++){
			if (lastindex-k<=lastindex%sqrt){
				for (int n=0; k<lastindex; n++){
					c= colors.get(list.get(listindex));
					k++;
					g.setColor(c);
					g.fillRect((n*squarewidth), i*squareheight, squarewidth, squareheight);
					g.setColor(Color.black);
					if (squarewidth>1){
						g.fillRect((n*squarewidth), i*squareheight, 1, squareheight);
					}
					listindex++;
				}

			}
			else{
				for (int n=0; n<sqrt; n++){
					c= colors.get(list.get(listindex));
					k++;
					g.setColor(c);
					g.fillRect((n*squarewidth), i*squareheight, squarewidth, squareheight);
					g.setColor(Color.black);
					if (squarewidth>1){
						g.fillRect((n*squarewidth), i*squareheight, 1, squareheight);
					}
					listindex++;
				}
			}
			if (squareheight>1){
				g.fillRect(0, i*squareheight, squarewidth*sqrt, 1);
			}
		}
		//System.out.println(list.size());
		g.setColor(Color.black);
		g.fillRect((int)width-1, 0, 1, (int)height);
		panel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		try {  
			ImageIO.write(image, "gif", saver); 
			
		} catch (IOException e) {  
		 e.printStackTrace(); 
		}
		panel.dispose();
		zPanel = new ZoomPanel(new MouseWatcher(this), image);
		//System.out.println(scroll.getSize());
		g.dispose();

	}
	public void makeSnakeImage(){
		panel=new JFrame("Snake");
		panel.setLayout(new BorderLayout());
		//System.out.println(xsize.getText());
		//System.out.println(ysize.getText());
		int k=0;
		int listindex=0;
		int lastindex=list.size()-1;
		image = new BufferedImage(sqrt*squarewidth, (int)(squareheight*Math.ceil(lastindex/(double)sqrt)), BufferedImage.TYPE_INT_RGB);
		dim= new Dimension(image.getWidth(), image.getHeight());
		Color c;
		Graphics g = image.getGraphics(); 
		for (int i=0; k<lastindex-1;i++){
			g.setColor(Color.black);
			if (lastindex-k<=sqrt&&Math.ceil(lastindex/sqrt)%2==0){
				for (int n=0; k<lastindex; n++){
					//System.out.println((i*sqrt)+n);
					//System.out.println(Math.ceil(list.size()/20.0));
					c= colors.get(list.get(listindex));
					k++;
					//System.out.println(c);
					//System.out.println(list.get((i*sqrt)+n).getCharacter());
					//System.out.println(c);
					g.setColor(c);
					g.fillRect((n*squarewidth), i*squareheight, squarewidth, squareheight);
					g.setColor(Color.black);
					listindex++;
					if (squarewidth>1){
						g.fillRect((n*squarewidth)-squarewidth, i*squareheight, 1, squareheight);
					}
				}
			}
			else if (lastindex-k<=sqrt&&Math.ceil(lastindex/sqrt)%2==1){
				for (int n=(lastindex%sqrt); k<lastindex; n--){
					c= colors.get(list.get(listindex));
					k++;
					g.setColor(c);
					g.fillRect((n*squarewidth)-squarewidth, i*squareheight, squarewidth, squareheight);
					g.setColor(Color.black);
					if (squarewidth>1){
						g.fillRect((n*squarewidth)-squarewidth, i*squareheight, 1, squareheight);
					}
					listindex++;
				}
			}
			else if(i%2==0){
			//System.out.println(i);
			for (int n=0; n<sqrt; n++){
				c= colors.get(list.get(listindex));
				k++;
				//System.out.println(list.get((i*20)+n).getCharacter());
				//System.out.println(c);
				g.setColor(c);
				g.fillRect((n*squarewidth), i*squareheight, squarewidth, squareheight);
				g.setColor(Color.black);
				listindex++;
				if (squarewidth>1){
					g.fillRect((n*squarewidth), i*squareheight, 1, squareheight);
				}
			}
			}
			else{
				//System.out.println(i);
				for (int n=sqrt-1; n>-1; n--){
					//System.out.println(Math.ceil(list.size()/20.0));
					c= colors.get(list.get(listindex));
					k++;
					//System.out.println(list.get((i*20)+n).getCharacter());
					//System.out.println(c);
					g.setColor(c);
					g.fillRect((n*squarewidth), i*squareheight, squarewidth, squareheight);
					g.setColor(Color.black);
					if (squarewidth>1){
						g.fillRect((n*squarewidth), i*squareheight, 1, squareheight);
					}
					listindex++;
				}
			}
			if (squareheight>1){
				g.fillRect(0, i*squareheight, squarewidth*sqrt, 1);
			}
		}
		//System.out.println(list.size());
		g.setColor(Color.black);
		g.fillRect((int)width-1, 0, 1, (int)height);
		panel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		try {  
			ImageIO.write(image, "gif", saver); 
			
		} catch (IOException e) {  
		 e.printStackTrace(); 
		}
		panel.dispose();
		zPanel = new ZoomPanel(new MouseWatcher(this), image);
		//System.out.println(scroll.getSize());
		g.dispose();
	}
	public void mouseClicked(MouseEvent e){
		JPopupMenu shower=new JPopupMenu();
		JViewport viewport = zPanel.scroll.getViewport();
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
		int yrow=(int) Math.floor(y/(zPanel.panel.scale*squareheight));
		int xcol=(int) Math.floor(x/(zPanel.panel.scale*squarewidth));
		if (yrow<0||xcol<0){
			return;
		}
		//regular horizontal tooltip
		System.out.println(vert);
		if (vert==0){
			System.out.println(squareheight+", "+squarewidth);
			System.out.println(x+", "+y+"||"+xcol+", "+yrow);
			if (yrow*sqrt+xcol<list.size()){
				shower.add(new JLabel("Base is "+basetobase.get(list.get(yrow*sqrt+xcol))));
				shower.add(new JLabel("Position: "+((yrow*sqrt+xcol)+1)));
				shower.show(e.getComponent(), x+xdif-h.x, y+ydif-h.y);
			}
		}
		//snaked tooltip
		else if (vert==-1){
			//clicker if last line is odd
			if ((int)Math.floor(dim.height/(double)squareheight)==yrow && yrow%2==1){
				shower.add(new JLabel("Base is "+basetobase.get(list.get((yrow*list.size()%sqrt+(list.size()%sqrt-xcol))-1))));
				shower.add(new JLabel("Position: "+((yrow*list.size()%sqrt+(list.size()%sqrt-xcol))-1)));
				shower.add(new JLabel("Going Left"));
				shower.show(e.getComponent(), x+xdif-h.x, y+ydif-h.y);
			}
			//clicker for everything else
			else if (yrow*sqrt+xcol<list.size()){
				if (yrow%2==1){
					shower.add(new JLabel("Base is "+basetobase.get(list.get((yrow*sqrt+(sqrt-xcol))-1))));
					shower.add(new JLabel("Position: "+((yrow*sqrt+(sqrt-xcol))-1)));
					shower.add(new JLabel("Going left"));
					shower.show(e.getComponent(), x+xdif-h.x, y+ydif-h.y);
				}
				else{
					shower.add(new JLabel("Base is "+basetobase.get(list.get(yrow*sqrt+xcol))));
					shower.add(new JLabel("Position: "+(yrow*sqrt+xcol)));
					shower.add(new JLabel("Going right"));
					shower.show(e.getComponent(), x+xdif-h.x, y+ydif-h.y);
				}
			}
		}		
	}
}
