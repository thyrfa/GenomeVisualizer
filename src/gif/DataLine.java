package gif;

public class DataLine {
	public int start;
	public int end;
	public String protein;
	
	public DataLine(int start, int end, String protein){
		this.start = start;
		this.end = end;
		this.protein = protein;
	}
	
	public boolean isIn(int test){
		if(test >= start && test <= end)
			return true;
		return false;
	}
}
