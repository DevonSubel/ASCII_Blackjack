import java.util.Scanner;
import java.util.ArrayList;

public class Game {
	public static void setPlayers(ArrayList<Player> players)
	{
		double money = -1;
		short numPlayers = 0;
		Scanner reader = new Scanner(System.in);
	
		do 
		{
			System.out.print("How much money does each player start with: ");
			if(reader.hasNextDouble())
				money = reader.nextDouble();
			else /*Clears buffer in case of non-int input*/
				reader.next();
		}while(money <= 0); /*Makes sure entered input is an int greater than 0*/
		
		do 
		{
			System.out.print("How many players do you want (1-5): ");
			if(reader.hasNextShort())
				numPlayers = reader.nextShort();
			else /*Clears buffer in case of non-int input*/
				reader.next();
		}while(!(numPlayers > 0 && numPlayers <= 5)); /*Make sure entered input is an int b/w 1 and 5*/
		
		for(int i = 0; i < numPlayers; i++)
		{
			System.out.print("What is your name: ");
			players.add(new Player(reader.next(),money,false));
		}
	}
	
	public static void makeBets(Player p)
	{
		int bet = -1;
		Scanner reader = new Scanner(System.in);
		do 
		{
			System.out.print(p.getName() + " how much do you want to bet: ");
			if(reader.hasNextDouble()) /*Ensures that the user entered proper input*/
				bet = reader.nextInt();
			else
				reader.next(); /*Clears buffer in case of non-int input*/
		}while(bet <= 0 || bet > p.getMoney()); /*Makes sure bet is a positive number that is not more than the total money of a player*/
		p.setBet(bet);
	}
	
	public static void dealCards(ArrayList<Player> players, Player dealer, Deck d)
	{
		/*Gets first card for all players & dealer*/
		for(int i = 0; i < players.size(); i++)
		{
			players.get(i).addToHand(d.draw());
		}
		dealer.addToHand(d.draw());
		/*Gets second card for all players & dealer*/
		for(int i = 0; i < players.size(); i++)
		{
			players.get(i).addToHand(d.draw());
		}
		dealer.addToHand(d.draw());
	}
	
	public static void printHands(ArrayList<Player> players, Player dealer)
	{
		for(int i = 0; i < players.size(); i++)
		{
			players.get(i).printHand(0);
		}
		dealer.printHand(1); /*The int is meant to know if this is the first time the dealer's hand is being "shown"*/
	}

	public static void checkInsurance(ArrayList<Player> players, Player dealer)
	{
		for(int i = 0; i < players.size(); i++)
		{
			if(dealer.handTotal() == 21) /*When the dealer has a blackjack the program checks to see who actually took the bet */
			{
				System.out.println("The dealer got a blackjack. Hope you took out insurance");
				if(players.get(i).getTookInsur())
				{
					players.get(i).wonInsur();
				}
				players.get(i).setBust();
			}
		}
	}
	
	public static void split(Player p, Deck d)
	{
		String input = "";
		Scanner reader = new Scanner(System.in);
		/*Sees if player wants to split */
		do
		{
			System.out.print(p.getName() + " would you like to split your cards ('y' or 'n'): ");
			input = reader.next();
		}while(!(input.equals("y") || input.equals("n")));
		
		/*Enters here is the user wants to split */
		if(input.equals("y"))
		{
			p.split();
			/*Hit and stay for the split hand */
			while(!input.equals("s") && p.getNumCards_Split() < 5 && inGame_Split(p))
			{
				System.out.println("\n"); /*Helps with UI to show where each turn ends*/
				p.printHand_Split();
				System.out.print("Press 'h' to hit or 's' to stay (MAX 5 cards): ");
				input = reader.next();
				if(input.equals("h"))
				{
					p.addToHand_Split(d.draw());
					p.printHand_Split();
					if(p.handTotal_Split() > 21) /*Sets Ace to 1 to prevents bust*/
						checkAce_Split(p);
					if(p.handTotal_Split() > 21) /*Sees if player is truely busted*/
					{
						System.out.println(p.getName() + "'s SPLIT HAND BUSTED");
						p.setBust_Split();
						input = "s";
					}
				}
			}
		}
		else if(p.getHand()[1].getVal() == 11) /*If the player doesn't split and they have two aces prevents auto-busting*/
		{
			p.getHand()[1].ace();
		}
	}
	
	public static void checkSpecialHands(ArrayList<Player> players, Player dealer, Deck d)
	{
		int tot;
		Card[] c;
		String input = ".";
		Scanner reader = new Scanner(System.in);
		/*Checks for blackjack*/
		for(int i = 0; i < players.size(); i++)
		{
			tot = players.get(i).handTotal();
			c = players.get(i).getHand();
			
			if(tot == 21) /*Blackjack*/
			{
				System.out.println(players.get(i).getName() + " got a BLACKJACK!");
				if(dealer.handTotal() != 21) /*If the dealer also has a blackjack it is a tie */
					players.get(i).blackjack();
				else
					players.get(i).tie();
			}
			else if(dealer.getHand()[1].getName().equals("[A]")) /*Insurance*/
			{
				if(players.get(i).getMoney() > (players.get(i).getBet()/2)) /*Can the player afford to take the bet */
				{
					do /*Does the player want to take the insurance bet*/
					{
						System.out.print("The dealer might have a blackjack would you like to take out insurance ('y' or 'n'): ");
						input = reader.next();
					}while(!(input.equals("y") || input.equals("n")));
					if(input.equals("y"))
					{
						players.get(i).insurance();
					}
				}
				else
					System.out.println(players.get(i).getName() + " does not have enough money to make the insurance bet");
			}
			else if(tot <= 11 && tot >= 9 && players.get(i).getMoney() >= players.get(i).getBet()) /*Double*/
			{
				do /*Does the player want to double */
				{
					System.out.print(players.get(i).getName() + ", would you like to double your bet and get 1 more card: ");
					input = reader.next();
				}while(!(input.equals("y") || input.equals("n")));
				players.get(i).dbl(d.draw());
			}
			else if(c[0].getName().equals(c[1].getName())) /*Split*/
			{
				if(players.get(i).getMoney() >= players.get(i).getBet())
					split(players.get(i),d); /*sees if player wants to split and splits if they do*/
			}
			if(dealer.getHand()[1].getName().equals("[A]")) /*Every player needs to be asked if they want insurance before we can see if they won the bet*/
				checkInsurance(players, dealer);
		}
	}
	
	/*Swaps the value of an ace if it will stop the player from busting*/
	public static void checkAce(Player p)
	{
		int size = p.getNumCards();
		Card[] hand = p.getHand();

		for(int i = 0; i < size; i++)
		{
			if(hand[i].getName().equals("[A]"))
			{
				p.getHand()[i].ace();
			}
		}
	}
	
	public static void checkAce_Split(Player p)
	{
		int size = p.getNumCards_Split();
		Card[] hand = p.getHand_Split();

		for(int i = 0; i < size; i++)
		{
			if(hand[i].getName().equals("[A]"))
			{
				p.getHand()[i].ace();
			}
		}
	}
	

	public static boolean inGame(Player p)
	{
		return !p.getBust() && !p.getDbl(); /*Sees if players are still able to hit/stay */
	}
	
	public static boolean inGame_Split(Player p)
	{
		return !p.getBust_Split();
	}
	
	public static void hitOrStay(ArrayList<Player> players, Player dealer, Deck d)
	{
		String input = "g";
		Scanner reader = new Scanner(System.in);
		System.out.println("\n\n"); /*Helps with UI to show where each turn ends*/
		for(int i = 0; i < players.size(); i++)
		{
			while(!input.equals("s") && players.get(i).getNumCards() < 5 && inGame(players.get(i)))
			{
				System.out.println("\n"); /*Helps with UI to show where each turn ends*/
				players.get(i).printHand(0);
				System.out.print("Press 'h' to hit or 's' to stay (MAX 5 cards): ");
				input = reader.next();
				if(input.equals("h"))
				{
					players.get(i).addToHand(d.draw());
					if(players.get(i).handTotal() > 21) /*Sets Ace to 1 to prevents bust*/
						checkAce(players.get(i));
					if(players.get(i).handTotal() > 21) /*Sees if player is truely busted*/
					{
						System.out.println(players.get(i).getName() + " BUSTS");
						players.get(i).setBust();
						input = "s";
					}
				}
			}
			players.get(i).printHand(0);
			input = "g";
		}
		
		System.out.println("\n"); /*Helps with UI to show where each turn ends*/
		while(dealer.handTotal() <= 17)
		{
			dealer.printHand(0);
			dealer.addToHand(d.draw());
		}
		if(dealer.handTotal() > 21)
		{
			System.out.println("The Dealer BUSTED");
			dealer.setBust();
		}
		dealer.printHand(0);
	}
	
	public static void comp(Player p, Player dealer)
	{
		int dealerTotal = dealer.handTotal();
		boolean bust = dealer.getBust();
		if(!p.getBust()) /*if you bust then you don't need to do any checks*/
		{
			if(bust) /*Player didn't bust but dealer did*/
			{
				System.out.println(p.getName() + " won because the dealer busted!");
				p.win();
			}
			else if(p.handTotal() > dealerTotal) /*Neither busts and player did better then dealer*/
			{
				System.out.println(p.getName() + " beat the dealer");
				p.win();
			}
			else if(p.handTotal() == dealerTotal) /*Neither busts and player did equally as well as the dealer*/
			{
				System.out.println(p.getName() + " tied the dealer");
				p.tie();
			}
			else /*Neither busts and player did worse then dealer*/
			{
				System.out.println(p.getName() + " lost to the dealer");
			}
		}
		if(p.getSplit()) /*Sees if player has a split hand*/
		{
			if(!p.getBust_Split())
			{
				if(bust)
				{
					System.out.println(p.getName() + "'s split hand won because the dealer busted!");
					p.win();
				}
				else if(p.handTotal_Split() > dealerTotal)
				{
					System.out.println(p.getName() + "'s split hand beat the dealer");
					p.win();
				}
				else if(p.handTotal_Split() == dealerTotal)
				{
					System.out.println(p.getName() + "'s split hand tied the dealer");
					p.tie();
				}
				else
				{
					System.out.println(p.getName() + "'s split hand lost to the dealer");
				}
			}
		}
	}
	
	public static void compareToDealer(ArrayList<Player> players, Player dealer)
	{
		for(int i = 0; i < players.size(); i++)
		{
			comp(players.get(i), dealer);
		}
		
	}
	
	public static void stayInGame(ArrayList<Player> players)
	{
		String input;
		Scanner reader = new Scanner(System.in);
		for(int i = 0; i < players.size(); i++)
		{
			System.out.println("\n\n" + players.get(i).getName() + " has $" + players.get(i).getMoney()); /*Let's player know how much $ they have*/
			if(players.get(i).getMoney() == 0) /*Kicks out players who have no $*/
			{
				System.out.println(players.get(i).getName() + " ran out of money and was kicked from the table");
				players.remove(i);
				i--;
			}
			else
			{
				do /*Do players want to keep playing */
				{
					System.out.print("Do you want to stay in the game (Type 'y' or 'n'): ");
					input = reader.next();
				}while(!(input.equals("y") || input.equals("n")));
				if(input.equals("n"))
				{
					System.out.println(players.get(i).getName() + " left the game");
					players.remove(i);
					i--;
				}
			}
		}
	}
	
	public static void reset(ArrayList<Player> players, Player dealer, Deck d) /*Resets certain variables for players and dealer */
	{
		for(int i = 0; i < players.size(); i++)
		{
			players.get(i).reset();
		}
		d.shuffle();
		dealer.reset();
	}
	
	public static void runGame(Player dealer, ArrayList<Player> players)
	{
		Deck d = new Deck();
		while(players.size() > 0)
		{
			for(int i = 0; i < players.size(); i++) /*Runs through all players and makes them make a bet*/
			{
				makeBets(players.get(i));
			}
			dealCards(players, dealer, d);
			printHands(players,dealer);
			checkSpecialHands(players,dealer,d); /*Checks for Blackjack, insurance, doubles, or splits */
			hitOrStay(players, dealer, d); /*Lets players 'hit' or 'stay' on their hand*/
			compareToDealer(players,dealer); /*Sees who won/lost*/
			stayInGame(players); /*See which player wants to keep playing*/
			reset(players, dealer, d); /*Resets players so that they can play another game and re-shuffles the deck*/
		}
	}
	
	public static void main(String[] args)
	{
		Player dealer = new Player("Dealer",0, true);
		ArrayList<Player> players = new ArrayList<Player>();
		
		setPlayers(players); /*Initializes all players*/
		runGame(dealer,players); 
	}
}
