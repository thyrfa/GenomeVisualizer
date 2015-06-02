package gif;

public class ColorCounter {
	private int i;
	private char c;
	private String s;
	private int index;
	private int name;
	public ColorCounter(int n, char z, int in){
		i=n;
		c=z;
		index=in;
	}
	public ColorCounter(int n, char z){
		i=n;
		c=z;
	}
	public ColorCounter(int n, int z, int in){
		i=n;
		name=z;
		index=in;
	}
	public ColorCounter(int n, String z, int in){
		i=n;
		s=z;
		index=in;
	}
	public int getNumber(){
		return i;
	}
	public char getCharacter(){
		return c;
	}
	public String getStr(){
		return s;
	}
	public int getIndex(){
		return index;
	}
	public int getName(){
		return name;
	}
	public void fixIndex(int i){
		index +=i;
	}
}
