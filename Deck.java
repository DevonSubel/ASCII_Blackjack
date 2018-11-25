import java.util.Random;

public class Deck {
	private int cardNum = 0; /*The index of the next card to be pulled*/
	private Card[] deck = new Card[52];
	
	public Deck()
	{
		int index = 0;
		for(int i = 0; i < 4; i++)
		{
			for(int j = 1; j <= 13; j++)
			{
				deck[index] = new Card(j);
				index++;
			}
		}
		deck = shuffle();
		deck[0] = new Card(7);
		deck[2] = new Card(7);
		cardNum = -1;
	}
	
	public Card[] shuffle()
	{
		int randPos;
		Card temp;
		Random rand = new Random();
		
		for(int i = 0; i < 52; i++)
		{
			randPos = rand.nextInt(52);
			temp = deck[i];
			deck[i] = deck[randPos];
			deck[randPos] = temp;
		}
		cardNum = -1;
		return deck;
	}
	
	public Card draw()
	{
		return deck[++cardNum];
	}
}
