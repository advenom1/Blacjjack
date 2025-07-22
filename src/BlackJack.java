import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class BlackJack {
    private class Card {
        String value;
        String type;



        Card(String value, String type) {
            this.value = value;
            this.type = type;

        }
        
        public String toString() {
            return value + "-" + type;

        }
        public int getValue() {
            if("AJQK".contains(value)) {
                if (value == "A") {
                    return 11;
               } 
               return 10;
            }
            return Integer.parseInt(value);

        }

        public boolean isAce() {
            return value == "A";
        }
        public String getImagePath() {
            return "./cards/" + toString() + ".png";
         }  
    }

    ArrayList<Card> deck;
    Random random = new Random();

    Card hiddenCard;
    ArrayList<Card> dealerHand;
    int dealersum;
    int dealerAceCount;

    ArrayList<Card> playerHand;
    int playerSum;
    int PlayerAceCount;

    int boardWidth = 600;
    int boardHeight = boardWidth;

    int cardWidth = 110;
    int CardHeight = 155;

    JFrame frame = new JFrame("BlackJack");
    JPanel gamePanel = new JPanel() {
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            try {
                Image hiddenCardImg = new ImageIcon(getClass().getResource("./cards/BACK.png")).getImage();
                if (!standButton.isEnabled()) {
                    hiddenCardImg = new ImageIcon(getClass().getResource(hiddenCard.getImagePath())).getImage()
                    ;
                }
                g.drawImage(hiddenCardImg, 20,20, cardWidth, CardHeight, null);
                

                for (int i = 0; i < dealerHand.size(); i++ ) {
                    Card card = dealerHand.get(i);
                    Image cardImage = new ImageIcon(getClass().getResource(card.getImagePath())).getImage();
                    g.drawImage(cardImage, cardWidth + 25 + (cardWidth + 5)*i, 20, cardWidth, CardHeight, null);
                }
                
                for (int i = 0; i < playerHand.size(); i++ ) {
                  Card card = playerHand.get(i);  
                  Image cardImage = new ImageIcon(getClass().getResource(card.getImagePath())).getImage();
                  g.drawImage(cardImage, 20 + (cardWidth + 5)*i, 320, cardWidth, CardHeight, null);
                    
                }
                if (!standButton.isEnabled()) {
                    dealersum = reduceDealerAce();
                    playerSum = reducePlayerAce();
                    System.out.println("Stand");
                    System.out.println(dealersum);
                    System.out.println(playerSum);

                    String message = "";
                    if (playerSum > 21) {
                        message = "You loose!";
                    }
                    else if (dealersum >21) {
                        message = "You win!";

                    }
                    else if(dealersum <  playerSum ) {
                        message = "You win!";

                    }
                    else if(dealersum >  playerSum ) {
                        message = "You loose";

                    }
                    else if(dealersum ==  playerSum ) {
                        message = "Tie";

                    }

                    g.setFont(new Font("Arial", Font.BOLD, 30));
                    g.setColor(Color.BLACK);
                    g.drawString(message, 220, 250);

                }
            
            } catch (Exception e) {
                e.printStackTrace();
            }
        
        }
    };
    JPanel buttonPanel = new JPanel();
    JButton hitButton = new JButton("Hit");
    JButton standButton = new JButton("Stand");
    JButton newGameButton = new JButton("NewGame");
    JButton BetMoneyButton = new JButton("Bet");





    BlackJack() {
        startGame();
        newGameButton.setRolloverEnabled(false);

        frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false );
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        gamePanel.setLayout(new BorderLayout());
        gamePanel.setBackground(new Color(53,101,77));
        frame.add(gamePanel);

        hitButton.setFocusable(false);
        buttonPanel.add(hitButton);
        standButton.setFocusable(false);
        newGameButton.setFocusable(false);
        buttonPanel.add(standButton);
        buttonPanel.add(newGameButton);
        BetMoneyButton.setFocusable(false);
        buttonPanel.add(BetMoneyButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);



        hitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Card card = deck.remove(deck.size()-1);
                playerSum += card.getValue();
                PlayerAceCount += card.isAce()? 1 : 0;
                playerHand.add(card);
                if (reducePlayerAce() > 21) {
                    hitButton.setEnabled(false);
                }    
                gamePanel.repaint();

             
            }   
            
        });


        standButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              hitButton.setEnabled(false);
              standButton.setEnabled(false);
              newGameButton.setEnabled(true);
                while (dealersum < 17) {
                Card card = deck.remove(deck.size()-1);
                dealersum += card.getValue();
                dealerAceCount += card.isAce()? 1: 0;
                dealerHand.add(card);


                }
            
                gamePanel.repaint();
               

            }  
        });
        gamePanel.repaint();

        newGameButton.addActionListener(new ActionListener() {
            public void actionPerformed (ActionEvent e) {
                if (!standButton.isEnabled() && !standButton.isEnabled())  {
                    startGame();
                    hitButton.setEnabled(true);
                    standButton.setEnabled(true);
                    newGameButton.setEnabled(false);
                    gamePanel.repaint();
                    



                }
                else {
                    newGameButton.setEnabled(true);
                }
                

            }
            
        });


       // BetMoneyButton.addActionListener(new ActionListener() {
          //  public void actionPerformed (ActionEvent e) {
           //     if (); 
        //    }

        // });
    }

    public void startGame() {
        newGameButton.setEnabled(false);
        buildDeck();
        shuffleDeck();

        dealerHand = new ArrayList<Card>();
        dealersum= 0;
        dealerAceCount = 0;

        hiddenCard = deck.remove(deck.size()-1);
        dealersum += hiddenCard.getValue();
        dealerAceCount += hiddenCard.isAce() ? 1 : 0;


        Card card = deck.remove(deck.size()-1);
        dealersum += card.getValue();
        dealerAceCount += card.isAce() ? 1 : 0;
        dealerHand.add(card);


        System.out.println("Dealer:");
        System.out.println(hiddenCard);
        System.out.println(dealerHand);
        System.out.println(dealersum);
        System.out.println(dealerAceCount);


        
        playerHand = new ArrayList<Card>();
        playerSum = 0;
        PlayerAceCount = 0;


        for (int i= 0;i< 2; i++) {
            card = deck.remove(deck.size()-1);
            playerSum += card.getValue();
            PlayerAceCount += card.isAce() ? 1: 0;
            playerHand.add(card);

        }     

        System.out.println("Player:");
        System.out.println(playerHand);
        System.out.println(playerSum);
        System.out.println(PlayerAceCount);


    
    }

    public void buildDeck() {
        deck = new ArrayList<Card>();
        String[] values = {"A", "2", "3","4","5","6","7","8","9","10","J","Q","K"};
        String[] types = {"C","D","H","S"};

        for (int i =0; i < types.length; i++) {
            for (int j =0; j < values.length; j++) {
                Card card = new Card(values[j], types[i]);
                deck.add(card);
            }   
       
        } 


        System.out.println("Build Deck:");
        System.out.println(deck);
    }


    public void shuffleDeck() {
        for (int i = 0; i < deck.size(); i++) {
            int j = random.nextInt(deck.size());
            Card currCard = deck.get(i);
            Card randomCard = deck.get(j);
            deck.set(i, randomCard);
            deck.set(j, currCard);
        }

        System.out.println("After shuffle");
        System.out.println(deck);
    } 


    public int reducePlayerAce() {
        while (playerSum > 21 && PlayerAceCount > 0) {
            playerSum -= 10;
            PlayerAceCount -= 1;

        }
        return playerSum;
    }
    public int reduceDealerAce() {
        while (dealersum > 21 && dealerAceCount > 0) {
            dealersum -= 10;
            dealerAceCount -= 1;

        }
        return dealersum;
    }
     
}