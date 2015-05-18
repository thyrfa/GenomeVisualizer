package gif;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class TotalGenerator {
	public HashMap<Character, Color> colors=new HashMap<Character, Color>();
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
	JTextField xsize;
	JTextField ysize;
	int squareheight;
	int squarewidth;
	boolean pair=false;
	ArrayList<ColorCounter> list;
	JScrollPane scroll;
	int vert=0;
	int labx;
	int laby;
	Dimension dim;
	BufferedImage image;
	int counter;
	File saver;
	
	public TotalGenerator(JTextField xs, JTextField ys) {
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
		xsize=xs;
		ysize=ys;
		File f=new File("C:/Users/"+System.getProperty("user.name")+"/Desktop/NewGeneImage.gif");
		int i=0;
		while (f.exists()){
			f=new File("C:/Users/"+System.getProperty("user.name")+"/Desktop/NewGeneImage"+i+".gif");
			i++;
		}
		saver=f;
	}
	public void readList(File f, int q){
		//System.out.println("Hi"+Runtime.getRuntime().maxMemory());
		try{
			saver.createNewFile();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		list=new ArrayList<ColorCounter>();
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
			FileReader ist = new FileReader(f);
			BufferedReader in = new BufferedReader(ist);
			while(t==true){
				//System.out.println(Runtime.getRuntime().freeMemory());
				c=in.read();
				//System.out.println((char)c);
				if (ch=='z'){
					inlast=true;
					s=new StringBuilder();
				}
				//System.out.println("Dots: "+dots);
				if (c==-1){
					numchars=Integer.parseInt(s.toString());
					t=false;
				}
				else if (inlast){
					//System.out.println((char)c);
					ch='@';
					if ((char)c=='z'){
						dots++;
					}
					else{
						s.append((char)c);
						//System.out.println(s.toString());
					}
					if (dots==1&&!ranone){
						//System.out.println("s:"+s.toString());
						max=Integer.parseInt(s.toString());
						s=new StringBuilder();
						ranone=true;
					}
					else if (dots==2&&!rantwo){
						//System.out.println("s:"+s.toString());
						modenum=Integer.parseInt(s.toString());
						s=new StringBuilder();
						rantwo=true;
					}
					else if (dots==3&&!ranthree){
						//System.out.println("s:"+s.toString());
						counter=Integer.parseInt(s.toString());
						s=new StringBuilder();
						ranthree=true;
					}
				}
				else if ((char)c!=ch&&(char)c!='0'&&ch!='@'){
					n=Integer.parseInt(s.toString().replace(ch, '1'), 2);
					//System.out.println(s.toString().replace(ch, '1'));
					//System.out.println(n);
					if (n>20000){
						n=20000;
					}
					total+=n;
					counter++;
					//System.out.println(total);
					//System.out.println(ch+", "+n);
					//System.out.println(total);
					list.add(new ColorCounter(n, ch, total));
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
			//list.trimToSize();
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
		sqrt=(int) Math.sqrt(numchars);
		//System.out.println(list.toString());
		squarewidth=(int)(width/sqrt);
		sqrt=(int)(sqrt+((width-(squarewidth*sqrt))/squarewidth));
		squareheight=(int)(height/Math.ceil(list.size() / (double)sqrt));
		if (squarewidth<=0 ||squareheight<=0){
			squarewidth=1;
			squareheight=1;
			sqrt=(int) Math.sqrt(list.size());
		}

		if (!xsize.getText().equals("")&&!ysize.getText().equals("")){
			boolean cat=false;
			try{
				squarewidth=Integer.parseInt(xsize.getText());
				squareheight=Integer.parseInt(ysize.getText());
				sqrt=(int) Math.sqrt(list.size());
			}
			catch(Exception e){
				e.printStackTrace();
				cat=true;
			}
			if (cat){
				squarewidth=(int)(width/sqrt);
				sqrt=(int)(sqrt+((width-(squarewidth*sqrt))/squarewidth));
				squareheight=(int)(height/Math.ceil(list.size() / (double)sqrt));
			}
		}
		if (((int)Math.ceil(list.size() / sqrt)+1)*squareheight>height){
			sqrt=(int)Math.floor(width/squarewidth);
		}
		/*System.out.println(height);
		System.out.println(width);
		System.out.println(squarewidth);
		System.out.println(squareheight);
		System.out.println(sqrt);
		System.out.println(list.size());
		System.out.println(list.size()%sqrt);*/
		int k=0;
		int listindex=0;
		int[] indexer= new int[list.size()];
		for (int i=0; i<list.size(); i++){
			indexer[i]=list.get(i).getIndex();
		}
		int lastindex=indexer[indexer.length-1];
		image = new BufferedImage(sqrt*squarewidth, (int)(squareheight*Math.ceil(lastindex/(double)sqrt)), BufferedImage.TYPE_INT_RGB);
		dim= new Dimension(image.getWidth(), image.getHeight());
		Color c;
		Graphics g = image.getGraphics(); 
		char ch;
		for (int i=0; k<indexer[indexer.length-1];i++){
			if (lastindex-k<=lastindex%sqrt){
				for (int n=0; k<lastindex; n++){
					//System.out.println((i*sqrt)+n);
					//System.out.println(Math.ceil(list.size()/20.0));
					//System.out.println(k);
					c= colors.get(list.get(listindex).getCharacter());
					k++;
					//System.out.println(k);
					//System.out.println(listindex);
					//System.out.println(indexer[listindex]);
					//System.out.println(c);
					//System.out.println(list.get((i*sqrt)+n).getCharacter());
					//System.out.println(c);
					g.setColor(c);
					g.fillRect((n*squarewidth), i*squareheight, squarewidth, squareheight);
					g.setColor(Color.black);
					if (squarewidth>1){
						g.fillRect((n*squarewidth), i*squareheight, 1, squareheight);
					}
					if (k>=indexer[listindex]){
						listindex++;
					}
				}

			}
			else{
			for (int n=0; n<sqrt; n++){
				//System.out.println(Math.ceil(list.size()/20.0));
				c= colors.get(list.get(listindex).getCharacter());
				k++;
				//System.out.println(k);
				//System.out.println(listindex);
				//System.out.println(indexer[listindex]);
				//System.out.println(list.get((i*20)+n).getCharacter());
				//System.out.println(c);
				g.setColor(c);
				g.fillRect((n*squarewidth), i*squareheight, squarewidth, squareheight);
				g.setColor(Color.black);
				if (squarewidth>1){
					g.fillRect((n*squarewidth), i*squareheight, 1, squareheight);
				}
				if (k>=indexer[listindex]){
					listindex++;
				}
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
		JLabel imagelabel=new JLabel(new ImageIcon(image));
		scroll = new JScrollPane (imagelabel, 
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.addMouseListener(new MouseWatcher(this));
		//System.out.println(scroll.getSize());
		panel.getContentPane().add(scroll);
		g.dispose();
		panel.pack();
		panel.setExtendedState(JFrame.MAXIMIZED_BOTH);
		//System.out.println(scroll.getSize());
		//System.out.println(label.getSize());
		//System.out.println(panel.getContentPane().getSize());
		panel.setVisible(true);
		try {  
			ImageIO.write(image, "gif", saver); 
			
		} catch (IOException e) {  
		 e.printStackTrace(); 
		}
	}
	public void makeSnakeImage(){
		panel=new JFrame("Snake");
		panel.setLayout(new BorderLayout());
		//System.out.println(xsize.getText());
		//System.out.println(ysize.getText());
		sqrt=(int) Math.sqrt(list.size());
		//System.out.println(list.toString());
		squarewidth=(int)(width/sqrt);
		sqrt=(int)(sqrt+((width-(squarewidth*sqrt))/squarewidth));
		squareheight=(int)(height/Math.ceil(list.size() / (double)sqrt));
		if (squarewidth<=0 ||squareheight<=0){
			squarewidth=1;
			squareheight=1;
			sqrt=(int) Math.sqrt(list.size());
		}
		/*System.out.println(height);
		System.out.println(width);
		System.out.println(squarewidth);
		System.out.println(squareheight);
		System.out.println(sqrt);
		System.out.println(list.size()%sqrt);*/
		System.out.println(list.size());
		if (!xsize.getText().equals("")&&!ysize.getText().equals("")){
			boolean cat=false;
			try{
				squarewidth=Integer.parseInt(xsize.getText());
				squareheight=Integer.parseInt(ysize.getText());
				sqrt=(int) Math.sqrt(list.size());

			}
			catch(Exception e){
				cat=true;
			}
			if (cat){
				squarewidth=(int)(width/sqrt);
				sqrt=(int)(sqrt+((width-(squarewidth*sqrt))/squarewidth));
				squareheight=(int)(height/Math.ceil(list.size() / (double)sqrt));
			}
		}
		if (((int)Math.ceil(list.size() / sqrt)+1)*squareheight>height){
			sqrt=(int)Math.floor(width/squarewidth);
		}
		int k=0;
		int listindex=0;
		int[] indexer= new int[list.size()];
		for (int i=0; i<list.size(); i++){
			indexer[i]=list.get(i).getIndex();
		}
		int lastindex=indexer[indexer.length-1];
		image = new BufferedImage(sqrt*squarewidth, (int)(squareheight*Math.ceil(lastindex/(double)sqrt)), BufferedImage.TYPE_INT_RGB);
		dim= new Dimension(image.getWidth(), image.getHeight());
		Color c;
		Graphics g = image.getGraphics(); 
		for (int i=0; k<lastindex-1;i++){
			g.setColor(Color.black);
			/*System.out.println(sqrt);
			System.out.println(lastindex);
			System.out.println(list.size());
			System.out.println(list.get(list.size()-1).getIndex());
			System.out.println("l-k: "+(lastindex-k));
			System.out.println("l%k: "+(lastindex%sqrt));
			System.out.println("l/s: "+(lastindex/sqrt));*/
			if (lastindex-k<=sqrt&&Math.ceil(lastindex/sqrt)%2==0){
				for (int n=0; k<lastindex; n++){
					//System.out.println((i*sqrt)+n);
					//System.out.println(Math.ceil(list.size()/20.0));
					c= colors.get(list.get(listindex).getCharacter());
					k++;
					//System.out.println(c);
					//System.out.println(list.get((i*sqrt)+n).getCharacter());
					//System.out.println(c);
					g.setColor(c);
					g.fillRect((n*squarewidth), i*squareheight, squarewidth, squareheight);
					g.setColor(Color.black);
					if (k>=indexer[listindex]&&listindex!=indexer.length-1){
						listindex++;
					}
					if (squarewidth>1){
						g.fillRect((n*squarewidth)-squarewidth, i*squareheight, 1, squareheight);
					}
				}
			}
			else if (lastindex-k<=sqrt&&Math.ceil(lastindex/sqrt)%2==1){
				for (int n=(lastindex%sqrt); k<lastindex; n--){
					//System.out.println((i*sqrt)+n);
					//System.out.println(Math.ceil(list.size()/20.0));
					/*System.out.println(lastindex);
					System.out.println(listindex);
					System.out.println(list.size());
					System.out.println(sqrt);
					System.out.println(lastindex%sqrt);*/
					c= colors.get(list.get(listindex).getCharacter());
					k++;
					//System.out.println(c);
					//System.out.println(list.get((i*sqrt)+n).getCharacter());
					//System.out.println(c);
					g.setColor(c);
					g.fillRect((n*squarewidth)-squarewidth, i*squareheight, squarewidth, squareheight);
					g.setColor(Color.black);
					if (squarewidth>1){
						g.fillRect((n*squarewidth)-squarewidth, i*squareheight, 1, squareheight);
					}
					if (k>=indexer[listindex]&&listindex!=indexer.length-1){
						listindex++;
					}
				}
			}
			else if(i%2==0){
			//System.out.println(i);
			for (int n=0; n<sqrt; n++){
				//System.out.println(Math.ceil(list.size()/20.0));
				//System.out.println("k: "+k);
				//System.out.println("listindex: "+listindex);
				c= colors.get(list.get(listindex).getCharacter());
				k++;
				//System.out.println(list.get((i*20)+n).getCharacter());
				//System.out.println(c);
				g.setColor(c);
				g.fillRect((n*squarewidth), i*squareheight, squarewidth, squareheight);
				g.setColor(Color.black);
				if (k>=indexer[listindex]){
					listindex++;
				}
				if (squarewidth>1){
					g.fillRect((n*squarewidth), i*squareheight, 1, squareheight);
				}
			}
			}
			else{
				//System.out.println(i);
				for (int n=sqrt-1; n>-1; n--){
					//System.out.println(Math.ceil(list.size()/20.0));
					c= colors.get(list.get(listindex).getCharacter());
					k++;
					//System.out.println(list.get((i*20)+n).getCharacter());
					//System.out.println(c);
					g.setColor(c);
					g.fillRect((n*squarewidth), i*squareheight, squarewidth, squareheight);
					g.setColor(Color.black);
					if (squarewidth>1){
						g.fillRect((n*squarewidth), i*squareheight, 1, squareheight);
					}
					if (k>=indexer[listindex]){
						listindex++;
					}
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
		JLabel imagelabel=new JLabel(new ImageIcon(image));
		scroll = new JScrollPane (imagelabel, 
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.addMouseListener(new MouseWatcher(this));
		g.dispose();
		//System.out.println(scroll.getSize());
		panel.getContentPane().add(scroll);
		panel.pack();
		panel.setExtendedState(JFrame.MAXIMIZED_BOTH);
		//System.out.println(scroll.getSize());
		//System.out.println(label.getSize());
		//System.out.println(panel.getContentPane().getSize());
		panel.setVisible(true);
		try {  
			ImageIO.write(image, "gif", saver); 
			
		} catch (IOException e) { 
		 e.printStackTrace(); 
		}
	}
	public void mouseClicked(MouseEvent e){
		
	}
}
