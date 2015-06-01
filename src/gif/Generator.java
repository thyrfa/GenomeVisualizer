package gif;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.filechooser.FileNameExtensionFilter;
public class Generator implements ActionListener {
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
	public File saver;
	JTextField acolor;
	JTextField ccolor;
	JTextField tcolor;
	JTextField gcolor;
	int sqrt;
	int numchars=0;
	JTextField xsize;
	JTextField ysize;
	int squareheight;
	int squarewidth;
	boolean pair=false;
	ArrayList<ColorCounter> list;
	int vert=0;
	int labx;
	int laby;
	Dimension dim;
	BufferedImage image;
	int counter=0;
	int kone=0;
	int ktwo=0;
	int kthree=1; //these are the constants that determine how much darker each square is made relative to the mode
	int kfour=2;
	int kfive=3;
	int ksix=4;
	boolean uncompressed=false;
	File saveloc;
	JTextField imglngth;
	ZoomPanel zPanel;
	JTextField startIndex;
	JTextField endIndex;
	
	static HashMap<Character, Color> colorstat= new HashMap<Character, Color>();
    static {
		colorstat.put('a', Color.red);
		colorstat.put('g', Color.green);
		colorstat.put('c', Color.blue);
		colorstat.put('t', Color.yellow);
		colorstat.put('n', Color.BLACK);
    }
    /*DONE-----base pairs--GA, TC, etc. 
      * //Spiral pattern 
      * //DONE-----control size of squares 
      * //DONE-----print number of letters shown 
      * //DONE-----Clickable image //DONE-----Snake 
      * //Differences 
      * //Customizable Colors- Still need to do in pair and total 
      * //DONE-----Finish pair parsing- something weird at the end 
      * //DONE-----Index on click menu 
      * //DONE-----Fix base numbers 
      * //DONE-----Bring back image writing 
      * //Option to represent purely with numbers*/
	public Generator(String pre){
		fileprefix = pre;
		colors.put('a', Color.red);
		colors.put('g', Color.green);
		colors.put('c', Color.blue);
		colors.put('t', Color.yellow);
		colors.put('n', Color.BLACK);
		basetobase.put('a', "Adenine");
		basetobase.put('g', "Guanine");
		basetobase.put('t', "Thymine");
		basetobase.put('n', "Unknown");
		basetobase.put('c', "Cytosine");
		File f=new File("C:/Users/"+System.getProperty("user.name")+fileprefix+"Desktop"+fileprefix+"NewGeneImage.gif");
		//finds place to write image
		int i=0;
		while (f.exists()){
			f=new File("C:/Users/"+System.getProperty("user.name")+fileprefix+"Desktop"+fileprefix+"NewGeneImage"+i+".gif");
			i++;
		}
		saver=f;
	}
	//Takes the given string and converts it to .cfa, adds on the stuff at the end with z's seperating, writes .counter for easy check on compression.
	public void makeList(String s, File f, boolean done){
		s=s.toLowerCase();
		File one=new File(f.getParentFile().getAbsolutePath()+fileprefix+f.getName().substring(0, f.getName().indexOf("."))+".cfa");
		System.out.println(one.toString());
		char lastchar='y';
		int count=1;
		list= new ArrayList<ColorCounter>();
		for (int i=s.indexOf("\n"); i<s.length();i++){
			if (s.charAt(i)!=' '&&!s.substring(i, i+1).equals("\n")){
				if (s.charAt(i)==lastchar){
					count++;
				}
				else{
					if (lastchar!='y'){
						list.add(new ColorCounter(count, lastchar));
						if(mode.containsKey(count)&&lastchar!='n'){
							mode.put(count, mode.get(count)+1);
						}
						else if (lastchar!='n'){
							mode.put(count, 1);
						}
					}
					lastchar=s.charAt(i);
					if (count>max&&lastchar!='n'){
						max=count;
					}
					count=1;
				}				
			}
			if ((i+1)==s.length()){
				if (lastchar!='y'){
					list.add(new ColorCounter(count, lastchar));
					if (lastchar!='n'){
						if(mode.containsKey(count)){
							mode.put(count, mode.get(count)+1);
						}
						else{
							mode.put(count, 1);
						}
					}
				}
			}
		}

		try{
			FileWriter writeone= new FileWriter(one, true);
			PrintWriter writerone= new PrintWriter(new BufferedWriter(writeone));
			list.trimToSize();
			for (int i=0; i<list.size(); i++){
				writerone.write(Integer.toString(list.get(i).getNumber(), 2).replace('1', list.get(i).getCharacter()));
				numchars+=list.get(i).getNumber();
				counter+=1;
			}
			writerone.flush();
			writerone.close();
			writeone.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		if (done){
			int modehelper=0;
			int modehelpertwo=0;
			Object[] ar=mode.keySet().toArray();
			for(int i=0;i<mode.size();i++){
				if(mode.get((Integer)ar[i])>modehelper){
					modetwo=modenum;
					modenum=(Integer)ar[i];
					modehelpertwo=modehelper;
					modehelper=mode.get((Integer)ar[i]);
				}
				else if(mode.get((Integer)ar[i])>modehelpertwo){
					modetwo=(Integer)ar[i];
					modehelpertwo=mode.get((Integer)ar[i]);
				}
			}
			if (modenum==0||modenum==1){
				modenum=modetwo;
			}
			try{
				FileWriter writeone= new FileWriter(one, true);
				PrintWriter writerone= new PrintWriter(new BufferedWriter(writeone));
				System.out.println(writeone.getEncoding());
				File two=new File(f.getParentFile().getAbsolutePath()+fileprefix+f.getName().substring(0, f.getName().indexOf("."))+".counter");
				System.out.println('z'+max+'z'+modenum+"z"+counter+"z"+numchars);
				writerone.write("z"+max+"z"+modenum+"z"+counter+"z"+numchars);
				writerone.flush();
				writerone.close();
				writeone.close();
				FileWriter writetwo= new FileWriter(two, true);
				PrintWriter writertwo= new PrintWriter(new BufferedWriter(writetwo));
				writertwo.write(counter+" to "+numchars);
				writertwo.close();
				writetwo.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
			System.out.println(counter);
		}
		//System.out.println(list.get(0).getCharacter());
		//System.out.println(modenum);

	}
	//parses .cfa for individual compressed display
	public void readList(File f, int start, int end){
		try{
			saver.createNewFile();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		//System.out.println(Runtime.getRuntime().maxMemory());
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
		int y = start;
		try{
			FileReader ist = new FileReader(f);
			BufferedReader in = new BufferedReader(ist);
			while(t==true){
				c=in.read();
				if (ch=='z'){
					inlast=true;
					s=new StringBuilder();
				}
				//System.out.println("Dots: "+dots);
				if (c==-1){
					numchars=Integer.parseInt(s.toString());
					t=false;
					return;
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
						s=new StringBuilder();
						ranthree=true;
					}
				}
				else if ((char)c!=ch&&(char)c!='0'&&ch!='@'){
					n=Integer.parseInt(s.toString().replace(ch, '1'), 2);
					y -= n;
					if (y<=0){
						if (Math.abs(y) < n)
							n = Math.abs(y);
						total+=n;
						counter++;
						list.add(new ColorCounter(n, ch, total));
					}
					else if (y!=0){
						total+=n;
						counter++;
						list.add(new ColorCounter(n, ch, total));
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
			list.trimToSize();
			if (end > 0){
				for (int i = list.size(); list.get(i).getIndex() > end && i >= 1; i-- ){
					if (list.get(i-1).getIndex() < end){
						ColorCounter h = list.remove(i);
						list.add(new ColorCounter(h.getIndex() - end, h.getCharacter(), end));
					}
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
			System.out.println(e.getCause());
		}
		
	}
	//called to choose what type of image to display.
	public void display(int t){
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		width = screenSize.getWidth();
		height = screenSize.getHeight();
		System.out.println(width);
		if (t==0){
			makeImage();
		}
		else if (t==-1){
			makeSnakeImage();
		}
	}
	//horizontal left to right image creator
	public void makeImage(){
		System.out.println(xsize.getText());
		System.out.println(ysize.getText());
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
		//sqrt is the number of squares per row
		System.out.println("sqrt: "+sqrt);
		System.out.println(width);
		System.out.println(squarewidth);
		image = new BufferedImage(sqrt*squarewidth, (int)(squareheight*Math.ceil(list.size()/(double)sqrt)), BufferedImage.TYPE_INT_RGB);
		dim= new Dimension(image.getWidth(), image.getHeight());
		Color c;
		Graphics g = image.getGraphics(); 
		int k;
		int q=0;
		//q is the index of the list that is currently being rendered
		for (int i=0; i<(int)Math.ceil(list.size() / sqrt)+1;i++){
			if (i==Math.ceil(list.size()/sqrt)){
				for (int n=0; n<list.size() % sqrt; n++){
					c= colors.get(list.get(q).getCharacter());
					if (list.get(q).getNumber()==max){
						k=kone;
					}
					else if (list.get(q).getNumber()>((max-modenum)/2)+modenum){
						k=ktwo;
					}
					else if(list.get(q).getNumber()>modenum){
						k=kthree;
					}
					else if (list.get(q).getNumber()==modenum){
						k=kfour;
					}
					else if (list.get(q).getNumber()==1){
						k=ksix;
					}
					else{
						k=kfive;
					}
					for (int p=0; p<k;p++){
						c=c.darker();
					}
					q++;
					g.setColor(c);
					g.fillRect((n*squarewidth), i*squareheight, squarewidth, squareheight);
					g.setColor(Color.black);
				}
			}
			else{
			for (int n=0; n<sqrt; n++){
				c= colors.get(list.get(q).getCharacter());
				if (list.get(q).getNumber()==max){
					k=kone;
				}
				else if (list.get(q).getNumber()>((max-modenum)/2)+modenum){
					k=ktwo;
				}
				else if(list.get(q).getNumber()>modenum){
					k=kthree;
				}
				else if (list.get(q).getNumber()==modenum){
					k=kfour;
				}
				else if (list.get(q).getNumber()==1){
					k=ksix;
				}
				else{
					k=kfive;
				}
				for (int p=0; p<k;p++){
					c=c.darker();
				}
				q++;
				g.setColor(c);
				g.fillRect((n*squarewidth), i*squareheight, squarewidth, squareheight);
				g.setColor(Color.black);
				if (squarewidth>2){
					//makes the border on the right and left of the boxes
					g.fillRect((n*squarewidth), i*squareheight, 1, squareheight);
				}
			}
			}
			if (squareheight>2){
				//makes the border on the top and bottom of the boxes
				g.fillRect(0, i*squareheight, squarewidth*sqrt, 1);
			}
		}
		g.setColor(Color.black);
		//makes a border at the left of the frame
		g.fillRect((int)width-1, 0, 1, (int)height);
		//the jframe is set up here
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
		//System.out.println(scroll.getSize());
		//System.out.println(label.getSize());
		//System.out.println(panel.getContentPane().getSize());
	}
	public void makeSnakeImage(){
		System.out.println(sqrt+" "+squarewidth+" "+squareheight);
		image = new BufferedImage(sqrt*squarewidth, (int)(squareheight*Math.ceil(list.size()/(double)sqrt)), BufferedImage.TYPE_INT_RGB);
		dim= new Dimension(image.getWidth(), image.getHeight());
		Color c;
		Graphics g = image.getGraphics(); 
		int k;
		int q=0;
		for (int i=0; i<(int)Math.ceil(list.size() / sqrt)+1;i++){
			g.setColor(Color.black);
			//if its the last row and the last row is even, render with this
			if (i==Math.ceil(list.size()/sqrt)&&Math.ceil(list.size()/sqrt)%2==0){
				for (int n=0; n<list.size() % sqrt; n++){
					c= colors.get(list.get(q).getCharacter());
					if (list.get(q).getNumber()==max){
						k=kone;
					}
					else if (list.get(q).getNumber()>((max-modenum)/2)+modenum){
						k=ktwo;
					}
					else if(list.get(q).getNumber()>modenum){
						k=kthree;
					}
					else if (list.get(q).getNumber()==modenum){
						k=kfour;
					}
					else if (list.get(q).getNumber()==1){
						k=ksix;
					}
					else{
						k=kfive;
					}
					for (int p=0; p<k;p++){
						c=c.darker();
					}
					q++;
					g.setColor(c);
					g.fillRect((n*squarewidth), i*squareheight, squarewidth, squareheight);
					g.setColor(Color.black);
					if (squarewidth>1){
						g.fillRect((n*squarewidth)-squarewidth, i*squareheight, 1, squareheight);
					}
				}
			}
			//last row and its odd, render with this
			else if (i==Math.ceil(list.size()/sqrt)&&Math.ceil(list.size()/sqrt)%2==1){
				for (int n=(list.size()%sqrt)-1; n>-1; n--){
					//System.out.println((i*sqrt)+n);
					//System.out.println(Math.ceil(list.size()/20.0));
					c= colors.get(list.get(q).getCharacter());
					//System.out.println(c);
					//System.out.println(list.get((i*sqrt)+n).getCharacter());
					if (list.get(q).getNumber()==max){
						k=kone;
					}
					else if (list.get(q).getNumber()>((max-modenum)/2)+modenum){
						k=ktwo;
					}
					else if(list.get(q).getNumber()>modenum){
						k=kthree;
					}
					else if (list.get(q).getNumber()==modenum){
						k=kfour;
					}
					else if (list.get(q).getNumber()==1){
						k=ksix;
					}
					else{
						k=kfive;
					}
					for (int p=0; p<k;p++){
						c=c.darker();
					}
					q++;
					//System.out.println(c);
					g.setColor(c);
					g.fillRect((n*squarewidth)-squarewidth, i*squareheight, squarewidth, squareheight);
					g.setColor(Color.black);
					if (squarewidth>1){
						g.fillRect((n*squarewidth)-squarewidth, i*squareheight, 1, squareheight);
					}
				}

			}
			//render for a normal line that is even
			else if(i%2==0){
			for (int n=0; n<sqrt; n++){
				c= colors.get(list.get(q).getCharacter());
				if (list.get(q).getNumber()==max){
					k=kone;
				}
				else if (list.get(q).getNumber()>((max-modenum)/2)+modenum){
					k=ktwo;
				}
				else if(list.get(q).getNumber()>modenum){
					k=kthree;
				}
				else if (list.get(q).getNumber()==modenum){
					k=kfour;
				}
				else if (list.get(q).getNumber()==1){
					k=ksix;
				}
				else{
					k=kfive;
				}
				for (int p=0; p<k;p++){
					c=c.darker();
				}
				q++;
				g.setColor(c);
				g.fillRect((n*squarewidth), i*squareheight, squarewidth, squareheight);
				g.setColor(Color.black);
				if (squarewidth>1){
					g.fillRect((n*squarewidth), i*squareheight, 1, squareheight);
				}
			}
			}
			//render for normal line that is odd
			else{
				for (int n=sqrt-1; n>-1; n--){
					c= colors.get(list.get(q).getCharacter());
					if (list.get(q).getNumber()==max){
						k=kone;
					}
					else if (list.get(q).getNumber()>((max-modenum)/2)+modenum){
						k=ktwo;
					}
					else if(list.get(q).getNumber()>modenum){
						k=kthree;
					}
					else if (list.get(q).getNumber()==modenum){
						k=kfour;
					}
					else if (list.get(q).getNumber()==1){
						k=ksix;
					}
					else{
						k=kfive;
					}
					for (int p=0; p<k;p++){
						c=c.darker();
					}
					q++;
					g.setColor(c);
					g.fillRect((n*squarewidth), i*squareheight, squarewidth, squareheight);
					g.setColor(Color.black);
					if (squarewidth>1){
						g.fillRect((n*squarewidth), i*squareheight, 1, squareheight);
					}
				}
			}
			if (squareheight>1){
				g.fillRect(0, i*squareheight, squarewidth*sqrt, 1);
			}
		}
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
		//System.out.println(scroll.getSize());
		//System.out.println(label.getSize());
		//System.out.println(panel.getContentPane().getSize());
	}
	//The menu to determine what kind of generation the user wants--Color selection does NOT work for uncompressed or pairs, vertical doesnt work with uncompressed
	public void ask(File f){
		File g = new File(fileprefix);
		JFileChooser fileChooser = new JFileChooser();
		if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
		  g = fileChooser.getSelectedFile();
		}
		else{
			System.exit(0);
		}
		saveloc = g;
		saver = g;
		a=f;
		panel=new JFrame();
		panel.setLayout(new GridBagLayout());
		GridBagConstraints c= new GridBagConstraints();
		panel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JLabel question=new JLabel("What type of generation would you like?");
		JButton make=new JButton("Horizontal");
		JButton snake= new JButton("Snaked Horizontal");
		snake.setActionCommand("snake");
		snake.addActionListener(this);
		make.setActionCommand("horiz");
		make.addActionListener(this);
		JButton colorz= new JButton("Customize Colors");
		colorz.setActionCommand("colors");
		colorz.addActionListener(this);
	    ButtonGroup bG = new ButtonGroup();
		JRadioButton single= new JRadioButton("Compressed");
		bG.add(single);
		JRadioButton uncompress= new JRadioButton("Uncompressed");
		uncompress.addActionListener(this);
		uncompress.setActionCommand("uncompress");
		bG.add(uncompress);
		single.addActionListener(this);
		single.setActionCommand("sing");
		c.gridy=0;
		c.gridx=0;
		panel.add(new JLabel("X"), c);
		c.gridx = 1;
		panel.add(new JLabel("Size of the boxes in pixels, leave blank for default size"), c);
		c.gridx = 2;
		panel.add(new JLabel("Y"), c);
		xsize=new JTextField(4);
		ysize=new JTextField(4);
		c.gridy=1;
		c.gridx=0;
		panel.add(xsize, c);
		c.gridx=2;
		panel.add(ysize, c);
		c.gridy=2;
		c.gridx=1;
		panel.add(new JLabel("Would you like compressed or uncompressed bases?"), c);
		c.gridx=0;
		c.gridy=3;
		panel.add(single, c);
		c.gridx=2;
		panel.add(uncompress, c);
		c.gridy=4;
		c.gridx=1;
		panel.add(new JLabel("Number of squares lengthwise?"), c);
		c.gridy = 5;
		imglngth = new JTextField(6);
		panel.add(imglngth, c);
		c.gridy = 6;
		panel.add(new JLabel("   "), c);
		c.gridy = 7;
		c.gridx = 0;
		panel.add(new JLabel("Start"), c);
		c.gridx = 1;
		panel.add(new JLabel("Start and end index of the sequence"), c);
		c.gridx = 2;
		panel.add(new JLabel("End"), c);
		c.gridy = 8;
		c.gridx = 0;
		startIndex = new JTextField(10);
		endIndex = new JTextField(10);
		panel.add(startIndex, c);
		c.gridx = 2;
		panel.add(endIndex, c);
		c.gridy = 9;
		c.gridx = 1;
		panel.add(new JLabel("   "), c);
		panel.add(question, c);
		c.gridx = 2;
		c.gridy = 11;
		panel.add(new JLabel("    "), c);
		c.gridy=12;
		panel.add(snake, c);
		c.gridx=0;
		panel.add(make, c);
		c.gridy=13;
		c.gridx=1;
		panel.add(new JLabel("    "), c);
		c.gridy++;
		panel.add(colorz, c);
		panel.pack();
		panel.setVisible(true);
	}
	//The color picker menu
	public void pickColors(){
		frame=new JFrame();
		frame.setLayout(new GridBagLayout());
		GridBagConstraints c= new GridBagConstraints();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		acolor= new JTextField(Integer.toHexString(colors.get('a').getRGB()).substring(2));
		ccolor= new JTextField(Integer.toHexString(colors.get('c').getRGB()).substring(2));
		tcolor= new JTextField(Integer.toHexString(colors.get('t').getRGB()).substring(2));
		gcolor= new JTextField(Integer.toHexString(colors.get('g').getRGB()).substring(2));
		JButton updater= new JButton("See selected colors");
		updater.setActionCommand("showchange");
		updater.addActionListener(this);
		JButton submitter= new JButton("Submit changes");
		submitter.setActionCommand("submitcolor");
		submitter.addActionListener(this);
		BufferedImage ai = new BufferedImage(40, 25, BufferedImage.TYPE_INT_RGB);
		BufferedImage ci = new BufferedImage(40, 25, BufferedImage.TYPE_INT_RGB);
		BufferedImage gi = new BufferedImage(40, 25, BufferedImage.TYPE_INT_RGB);
		BufferedImage ti = new BufferedImage(40, 25, BufferedImage.TYPE_INT_RGB);
		Graphics gg = gi.getGraphics();
		gg.setColor(colors.get('g'));
		gg.fillRect(0, 0, 40, 25);
		Graphics ag= ai.getGraphics();
		ag.setColor(colors.get('a'));
		ag.fillRect(0, 0, 40, 25);
		Graphics cg= ci.getGraphics();
		cg.setColor(colors.get('c'));
		cg.fillRect(0, 0, 40, 25);
		Graphics tg= ti.getGraphics();
		tg.setColor(colors.get('t'));
		tg.fillRect(0, 0, 40, 25);
		c.gridx=1;
		c.gridy=0;
		frame.add(new JLabel("A"), c);
		c.gridx--;
		c.gridy++;
		frame.add(acolor, c);
		c.gridx+=2;
		frame.add(new JLabel(new ImageIcon(ai)), c);
		c.gridx=1;
		c.gridy=2;
		frame.add(new JLabel("C"), c);
		c.gridx--;
		c.gridy++;
		frame.add(ccolor, c);
		c.gridx+=2;
		frame.add(new JLabel(new ImageIcon(ci)), c);
		c.gridx=6;
		c.gridy=0;
		frame.add(new JLabel("G"), c);
		c.gridx--;
		c.gridy++;
		frame.add(gcolor, c);
		c.gridx+=2;
		frame.add(new JLabel(new ImageIcon(gi)), c);
		c.gridx=6;
		c.gridy=2;
		frame.add(new JLabel("T"), c);
		c.gridx--;
		c.gridy++;
		frame.add(tcolor, c);
		c.gridx+=2;
		frame.add(new JLabel(new ImageIcon(ti)), c);
		c.gridy=5;
		c.gridx=3;
		frame.add(updater, c);
		c.gridx++;
		frame.add(submitter, c);
		frame.setSize(600, 300);
		frame.setVisible(true);
		ag.dispose();
		cg.dispose();
		tg.dispose();
		gg.dispose();
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		int start = -1;
		int end = -1;
		if (startIndex.getText().equals("")){
			start = 0;
		}
		else {
			start = Integer.parseInt(startIndex.getText());
		}
		if (endIndex.getText().equals("")){
			end = 0;
		}
		else {
			end = Integer.parseInt(endIndex.getText());
		}
		//load color selection
		if (e.getActionCommand().equals("colors")){
			pickColors();
		}
		else if (e.getActionCommand().equals("uncompress")){
			uncompressed=true;
		}
		//accept changes to the colors
		else if (e.getActionCommand().equals("submitcolor")){
			colors=new HashMap<Character, Color>();
			boolean error=false;
			try{
			colors.put('a', new Color(Integer.parseInt(acolor.getText(), 16)));
			colors.put('c', new Color(Integer.parseInt(ccolor.getText(), 16)));
			colors.put('g', new Color(Integer.parseInt(gcolor.getText(), 16)));
			colors.put('t', new Color(Integer.parseInt(tcolor.getText(), 16)));
			}
			catch(Exception ex){
				error=true;
				JOptionPane.showMessageDialog(frame, "Invalid color code");
				colors=colorstat;
			}
			if (!error){
				frame.dispose();
			}
		}
		//show the new colors
		else if(e.getActionCommand().equals("showchange")){
			colors=new HashMap<Character, Color>();
			boolean error=false;
			try{
			colors.put('a', new Color(Integer.parseInt(acolor.getText(), 16)));
			colors.put('c', new Color(Integer.parseInt(ccolor.getText(), 16)));
			colors.put('g', new Color(Integer.parseInt(gcolor.getText(), 16)));
			colors.put('t', new Color(Integer.parseInt(tcolor.getText(), 16)));
			}
			catch(Exception ex){
				error=true;
				JOptionPane.showMessageDialog(frame, "Invalid color code");
				colors=colorstat;
			}
			if (!error){
				frame.dispose();
				pickColors();
			}
		}
		//render in snake
		else if(e.getActionCommand().equals("snake")){
			vert=-1;
			panel.dispose();
			if (uncompressed){
				new TotalGenerator(saveloc, fileprefix, dimensions(a)).readList(a, -1, start, end);
			}
			else{
				dimensions(a);
				panel=new JFrame();
				panel.setLayout(new BorderLayout());
				readList(a, start, end);
				display(-1);
			}
		}
		//render horizontally
		else if(e.getActionCommand().equals("horiz")){
			vert=0;
			panel.dispose();
			if (uncompressed){
				new TotalGenerator(saveloc, fileprefix, dimensions(a)).readList(a, 1, start, end);
			}
			else{
				dimensions(a);
				panel=new JFrame();
				panel.setLayout(new BorderLayout());
				readList(a, start, end);
				display(0);
			}
			
		}
		
	}
	public int[] dimensions(File f){
		try{
			FileReader ist = new FileReader(f.getParentFile()+fileprefix+f.getName().substring(0, f.getName().indexOf("."))+".counter");
			BufferedReader in = new BufferedReader(ist);
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			width = screenSize.getWidth();
			height = screenSize.getHeight();
			String s = in.readLine();
			int listsize = Integer.parseInt(s.substring(s.indexOf("to")+3));
			System.out.println("Listsize: "+listsize);
			if (!startIndex.getText().equals("")||!endIndex.getText().equals("")){
				int st = 0;
				int e = listsize;
				if (!endIndex.getText().equals("")){
					e = Integer.parseInt(endIndex.getText());
				}
				if (!startIndex.getText().equals("")){
					st = Integer.parseInt(startIndex.getText());
				}
				listsize = e - st;
			}
			sqrt=(int) Math.sqrt(listsize);
			squarewidth=(int)(width/sqrt);
			sqrt=(int) Math.sqrt(listsize);
			squareheight=(int)(height/Math.ceil(listsize / (double)sqrt));
			if (squarewidth<=0 ||squareheight<=0){
				squarewidth=1;
				squareheight=1;
				sqrt=(int) Math.sqrt(listsize);
			}
			System.out.println("sqrt: "+sqrt);
			if (!xsize.getText().equals("")&&!ysize.getText().equals("")){
				try{
					squarewidth=Integer.parseInt(xsize.getText());
					squareheight=Integer.parseInt(ysize.getText());
					sqrt=(int)(width/squarewidth);
					System.out.println("sqrt1: "+sqrt);
				}
				catch(Exception e){
					squarewidth=(int)(width/sqrt);
					sqrt=(int) Math.sqrt(listsize);
					squareheight=(int)(height/Math.ceil(listsize / (double)sqrt));
					System.out.println("sqrt2: "+sqrt);
				}
			}
			System.out.println("SW: "+squarewidth);
			System.out.println("SH: "+squareheight);
			System.out.println("sqrt: "+sqrt);
			if (!imglngth.getText().equals("")){
				System.out.println("sqrt yes");
				try{
					sqrt = Integer.parseInt(imglngth.getText());
				}catch(Exception e){}
			}
			System.out.println("sqrt: "+sqrt);
			in.close();
			ist.close();
		}catch(Exception e){}
		return new int[]{squarewidth, squareheight, sqrt};
	}
	//Mouse event for a compressed single pair frame
	public void mouseClicked(MouseEvent e){
		//System.out.println("Local Clicked")
		JPopupMenu shower=new JPopupMenu();
		JViewport viewport = zPanel.scroll.getViewport();
		int xdif=viewport.getSize().width-zPanel.panel.getPreferredSize().width;
		int ydif=viewport.getSize().height-zPanel.panel.getPreferredSize().height;
		xdif=xdif/2;
		ydif=ydif/2; //ydif and xdif are the difference in location between the edge of the scrollframe and the picture, can find what pixel of the IMAGE was clicked on.
		if (xdif<0){
			xdif=0;
		}
		if (ydif<0){
			ydif=0;
		}
		
        Point h=viewport.getViewPosition();        
        xdif+=(zPanel.panel.scale*squarewidth)/5;
        ydif+=(zPanel.panel.scale*squareheight)/5;
        //h is how much the user has scrolled, corrects for that.
		int x=e.getX()+h.x-xdif;
		int y=e.getY()+h.y-ydif;
		int yrow=(int) Math.floor(y/(zPanel.panel.scale*squareheight));
		int xcol=(int) Math.floor(x/(zPanel.panel.scale*squarewidth));
		if (yrow<0||xcol<0){
			return;
		}
		//regular horizontal tooltip
		if (vert==0){
			if (yrow*sqrt+xcol<list.size()){
				shower.add(new JLabel("Base is "+basetobase.get(list.get(yrow*sqrt+xcol).getCharacter())));
				shower.add(new JLabel("Amount represented: "+list.get(yrow*sqrt+xcol).getNumber()));
				shower.add(new JLabel("Position: "+list.get(yrow*sqrt+xcol).getIndex()));
				shower.show(e.getComponent(), x+xdif-h.x, y+ydif-h.y);
			}
		}
		//snaked tooltip
		else if (vert==-1){
			//clicker if last line is odd
			if ((int)Math.floor(dim.height/(double)squareheight)==yrow && yrow%2==1){
				shower.add(new JLabel("Base is "+basetobase.get(list.get((yrow*list.size()%sqrt+(list.size()%sqrt-xcol))-1).getCharacter())));
				shower.add(new JLabel("Amount represented: "+list.get((yrow*list.size()%sqrt+(list.size()%sqrt-xcol))-1).getNumber()));
				shower.add(new JLabel("Position: "+list.get((yrow*list.size()%sqrt+(list.size()%sqrt-xcol))-1).getIndex()));
				shower.add(new JLabel("Going Left"));
				shower.show(e.getComponent(), x+xdif-h.x, y+ydif-h.y);
			}
			//clicker for everything else
			else if (yrow*sqrt+xcol<list.size()){
				if (yrow%2==1){
					shower.add(new JLabel("Base is "+basetobase.get(list.get((yrow*sqrt+(sqrt-xcol))-1).getCharacter())));
					shower.add(new JLabel("Amount represented: "+list.get((yrow*sqrt+(sqrt-xcol))-1).getNumber()));
					shower.add(new JLabel("Position: "+list.get((yrow*sqrt+(sqrt-xcol))-1).getIndex()));
					shower.add(new JLabel("Going left"));
					shower.show(e.getComponent(), x+xdif-h.x, y+ydif-h.y);
				}
				else{
					shower.add(new JLabel("Base is "+basetobase.get(list.get(yrow*sqrt+xcol).getCharacter())));
					shower.add(new JLabel("Amount represented: "+list.get(yrow*sqrt+xcol).getNumber()));
					shower.add(new JLabel("Position: "+list.get(yrow*sqrt+xcol).getIndex()));
					shower.add(new JLabel("Going right"));
					shower.show(e.getComponent(), x+xdif-h.x, y+ydif-h.y);
				}
			}
		}
	}
	public static void main(String[] args) {
		String fileprefix = "";
		if (System.getProperty("os.name").toString().equals("Linux")){
			fileprefix = "/";
		}
		else{
			fileprefix = "\\";
		}
		JFrame panel= new JFrame();
		JFileChooser fc = new JFileChooser(new File("C:\\Users\\"+System.getProperty("user.name")+"\\Downloads"));
	    FileNameExtensionFilter filter = new FileNameExtensionFilter(
	            "txt", "fa", "cfa");
	    fc.setFileFilter(filter);
	    int returnVal = fc.showOpenDialog(panel);
	    File f;
	    if (fc.getSelectedFile()==null){
	    	System.exit(0);
	    }
	    if(returnVal == JFileChooser.APPROVE_OPTION){
	    	panel.dispose();
	    	f=fc.getSelectedFile();
	    	System.out.println(f.toString());
	    	Generator g=new Generator(fileprefix);
	    	if (f.getParentFile().isDirectory()){
	    		for (File i : f.getParentFile().listFiles()){
	    			if ((f.getName().substring(0, f.getName().indexOf("."))+".cfa").equals(i.getName())){
	    				f=i;
	    			}
	    		}
	    	}
	    	if (f.getAbsolutePath().contains(".cfa")){
	    		try{
	    			g.ask(f);
	    		}
	    		catch(Throwable e){
	    			e.printStackTrace();
	    		}
	    	}
	    	else{
	    		try {
	    			long startTime = System.currentTimeMillis();
	    			BufferedReader filer= new BufferedReader(new FileReader(f));
	    			StringBuilder s=new StringBuilder();
	    			boolean t=true;
	    			int c;
	    			int a=0;
	    			int count=0;
	    			while(t==true){
	    				c=filer.read();
	    				a++;
	    				if (c==-1){
	    					t=false;
	    					g.makeList(s.toString(), f, true);
	    				}
	    				else{
	    					s.append((char)c);
	    				}
	    				if (a==15000000){ //feeds the .cfa creator 15000000 at a time, lower memory overhead.
	    					count++;
	    					g.makeList(s.toString(), f, false);
	    					System.out.println(15000000*count);
	    					s=new StringBuilder();
	    					a=0;
	    				}
	    			}
	    			filer.close();
	    			//System.out.println(s.toString());
	    			long stopTime = System.currentTimeMillis();
	    			System.out.println("Elapsed time was " + (stopTime - startTime) + " miliseconds.");
	    		} catch (FileNotFoundException e) {
	    			// TODO Auto-generated catch block
	    			e.printStackTrace();
	    		} catch (IOException e) {
	    			// TODO Auto-generated catch block
	    			e.printStackTrace();
	    		}
	    	}
	    }
	    else{
	    	System.exit(0);
	    }
		
	}
    public static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }

	
}
