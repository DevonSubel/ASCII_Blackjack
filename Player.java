public class Player {
	private double money;
	private double bet;
	private double bet_split;
	private double insur; 
	private int numCardsInHand = 0;
	private int numCardsInHand_Split = 0;
	private boolean bust = false; /*Sees if player busted */
	private boolean bust_split = false;
	private boolean dbl = false; /*Sees if player took a double*/
	private boolean tookInsur = false; /*Sees if player took insurance bet*/
	private boolean split = false; /*Sees if player split */
	private boolean isDealer;
	private String name;
	private Card[] hand = new Card[5];
	private Card[] hand_split = new Card[5];
	
	public Player(String name, double money, boolean isDealer)
	{
		this.name = name;
		this.money = money;
		this.isDealer = isDealer;
	}
	
	/*Getters*/
	public String getName()
	{
		return name;
	}
	
	public double getMoney()
	{
		return money;
	}
	
	public int getNumCards()
	{
		return numCardsInHand;
	}
	
	public int getNumCards_Split()
	{
		return numCardsInHand_Split;
	}
	
	public boolean getBust()
	{
		return bust;
	}
	
	public boolean getBust_Split()
	{
		return bust_split;
	}
	
	public double getBet()
	{
		return bet;
	}
	
	public Card[] getHand()
	{
		return hand;
	}
	
	public Card[] getHand_Split()
	{
		return hand_split;
	}
	
	public boolean getDbl()
	{
		return dbl;
	}
	
	public boolean getTookInsur()
	{
		return tookInsur;
	}
	
	public double getInsur()
	{
		return insur;
	}
	
	public boolean getSplit()
	{
		return split;
	}
	
	public double getSplitBet()
	{
		return bet_split;
	}
	
	/*Setter*/
	public void setBust()
	{
		bust = true;
	}
	
	public void setBust_Split()
	{
		bust_split = true;
	}
	
	public void setBet(int bet)
	{
		this.bet = bet;
		money -= bet;
	}
	
	/*Methods after hand totals compared (loss needed because money subtracted when player makes bet)*/
	public void win()
	{
		money += (bet*2);
	}
	
	public void tie()
	{
		money += bet;
	}
	
	/*Hand/Split methods*/
	public void addToHand(Card c)
	{
		hand[numCardsInHand] = c;
		numCardsInHand++;
	}
	
	public void addToHand_Split(Card c)
	{
		hand_split[numCardsInHand_Split] = c;
		numCardsInHand_Split++;
	}
	
	public int handTotal() /*Total value in hand*/
	{
		int ret = 0;
		for(int i = 0; i < numCardsInHand; i++)
		{
			ret += hand[i].getVal();
		}
		return ret;
	}
	
	public int handTotal_Split() /*Total value of cards in split hand*/
	{
		int ret = 0;
		for(int i = 0; i < numCardsInHand_Split; i++)
		{
			ret += hand_split[i].getVal();
		}
		
		return ret;
	}
	
	/*Special moves*/
	public void dbl(Card c)
	{
		money -= bet;
		bet *= 2;
		addToHand(c);
		dbl = true;
	}
	
	public void insurance()
	{
		tookInsur = true;
		insur = bet/2;
		money -= insur;
	}
	
	public void wonInsur()
	{
		money += (2*insur);
	}
	
	/*Resets vars for next round*/
	public void reset()
	{
		numCardsInHand = 0;
		numCardsInHand_Split = 0;
		insur = 0;
		bust = false;
		bust_split = false;
		dbl = false;
		tookInsur = false;
		split = false;
	}
	
	public void split()
	{
		split = true;
		numCardsInHand--;
		hand_split[0] = hand[1];
		numCardsInHand_Split++;
	}
	
	/*When a player gets a Blackjack*/
	public void blackjack()
	{
		money += bet;
		money += bet*1.25;
		bust = true; /*So he will not be compared to the dealer twice*/
	}
	
	public void printHand(int j) /*The int is meant to know if this is the first time the dealer's hand is being "shown"*/
	{
		if(isDealer == true && j == 1) /*Special case for when it is the first time showing the dealer's hand and you need to hide one of the cards*/
		{
			System.out.println("DEALERS HAND: [X] " + hand[1].getName() + " (TOTAL: XX)");
		}
		else
		{
			System.out.print(name + "'s hand: ");
			for(int i = 0; i < numCardsInHand; i++)
			{
				System.out.print(hand[i].getName() + " ");
			}
			System.out.println("(TOTAL: " + handTotal() + ")");
		}
	}
	
	public void printHand_Split() /*The dealer will never split so there is no need for a special case */
	{
		System.out.print(name + "'s split hand: ");
		for(int i = 0; i < numCardsInHand_Split; i++)
		{
			System.out.print(hand_split[i].getName() + " ");
		}
		System.out.println("(TOTAL: " + handTotal_Split() + ")");
	}
}
