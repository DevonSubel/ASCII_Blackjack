public class Card {
	private int val;
	private String name;
	
	public Card(int val)
	{
		if(val <= 10 && val >= 2) /*This is meant for non-royal cards*/
		{
			this.val = val;
			name = Integer.toString(val);
		}
		else if(val == 1) /*A separate method will decide if the value is 1 or 11*/
		{
			this.val = 11;
			name = "A";
		}
		else if(val == 11)
		{
			this.val = 10;
			name = "J";
		}
		else if(val == 12)
		{
			this.val = 10;
			name = "Q";	
		}
		else if(val == 13)
		{
			this.val = 10;
			name = "K";	
		}
		
	}
	
	public int getVal()
	{
		return val;	
	}
	
	public String getName()
	{
		return "[" + name + "]";
	}
	
	public boolean isRoyal()
	{
		return name.equals("A") || name.equals("K") || name.equals("Q") || name.equals("J");
	}
	
	public void ace()
	{
		val = 1;
	}
}
