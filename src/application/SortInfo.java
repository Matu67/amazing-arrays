package application;

public class SortInfo
{
	//Initialize all info variables
	private long time = -1;
	private int loopCounter = 0;
	private int comparisonCounter = 0;
	private int shiftCounter = 0;
	private int n;
	private int arrayID;
	private String sType;
	
	public SortInfo()
	{
		//Update the variables, getting them from the main class
		time = Main.time;
		loopCounter = Main.loopCounter;
		comparisonCounter = Main.comparisonCounter;
		shiftCounter = Main.shiftCounter;
		n = Main.n;
		arrayID = Main.arrayID;
		sType = Main.sType;
	}
	
	//Get methods
	public long getTime()
	{
		return time;
	}
	public int getComparisonCounter()
	{
		return comparisonCounter;
	}
	public int getLoopCounter()
	{
		return loopCounter;
	}
	public int getShiftCounter()
	{
		return shiftCounter;
	}
	public int getN()
	{
		return n;
	}
	public int getArrayID()
	{
		return arrayID;
	}
	public String getSType()
	{
		return sType;
	}
}
