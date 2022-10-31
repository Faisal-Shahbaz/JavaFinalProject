import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.io.*;

class Main {
  public static void main(String[] args) {
    guiGame guiObject = new guiGame();
    guiObject.setSize(400, 400);
    guiObject.setVisible(true);
  }
}

class guiGame extends JFrame implements ActionListener {
  Random rand = new Random();
  String wordString;
  char[] letters, display, guesses;
  int letterGuessCount, wordGuessCount;
  boolean gameOver, win;
  ArrayList<String> wordsList = new ArrayList<String>();

  JLabel Lwordout = new JLabel("Word: ");
  JLabel Lguessletter = new JLabel("Guessed Letters: ");
  JLabel Lremainletter = new JLabel("Remaining Letter Guesses: ");
  JLabel Lremainword = new JLabel("Remaining Word Guesses: ");
  JLabel Lletter = new JLabel("Guess a letter: ");
  JLabel Lword = new JLabel("Guess a word: ");
  JTextField Outword = new JTextField(20);
  JTextField OutStatus = new JTextField(30);
  JTextField Outletter = new JTextField(20);
  JTextField Outlguess = new JTextField(5);
  JTextField Outwguess = new JTextField(5);
  JTextField Inletter = new JTextField(15);
  JTextField Inword = new JTextField(15);
  JButton startOverButton = new JButton("Start Over");
  JPanel Pwordout = new JPanel();
  JPanel Pstatusout = new JPanel();
  JPanel Pguessletter = new JPanel();
  JPanel Premainletter = new JPanel();
  JPanel Premainword = new JPanel();
  JPanel Pletter = new JPanel();
  JPanel Pword = new JPanel();
  JPanel PstartOver = new JPanel();

  guiGame() {
    setTitle("The Game of Definition");
    Outword.setEditable(false);
    OutStatus.setEditable(false);
    Outletter.setEditable(false);
    Outlguess.setEditable(false);
    Outwguess.setEditable(false);
    getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
    Pwordout.add(Lwordout);
    Pwordout.add(Outword);
    Pstatusout.add(OutStatus);
    Pguessletter.add(Lguessletter);
    Pguessletter.add(Outletter);
    Premainletter.add(Lremainletter);
    Premainletter.add(Outlguess);
    Premainword.add(Lremainword);
    Premainword.add(Outwguess);
    Pletter.add(Lletter);
    Pletter.add(Inletter);
    Pword.add(Lword);
    Pword.add(Inword);
    PstartOver.add(startOverButton);
    getContentPane().add(Pwordout);
    getContentPane().add(Pstatusout);
    getContentPane().add(Pguessletter);
    getContentPane().add(Premainletter);
    getContentPane().add(Premainword);
    getContentPane().add(Pletter);
    getContentPane().add(Pword);
    getContentPane().add(PstartOver);
    Inletter.addActionListener(this);
    Inword.addActionListener(this);
    startOverButton.addActionListener(this);
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    try {
      generatewordsList();
    } catch (IOException ex) {
    }
    initialize();
  }

  public void generatewordsList() throws IOException {
    File file = new File("words.txt");
    Scanner scan = new Scanner(file);
    while (scan.hasNext()) {
      wordsList.add(scan.next());
    }
  }

  public String generateWordString() {
    try {
      int tempIndex = rand.nextInt(wordsList.size());
      String tempString = wordsList.get(tempIndex);
      wordsList.remove(tempIndex);
      return tempString.toLowerCase();
    } catch (NullPointerException ex) {
      return "errorsomewhere";
    } catch (IllegalArgumentException ex) {
      return "you weren't supposed to QA this so much";
    }
  }

  public void initialize() {
    wordString = generateWordString();
    letters = wordString.toCharArray();
    display = new char[letters.length];
    for (int i = 0; i < letters.length; i++) {
      display[i] = '_';
    }
    guesses = new char[5];
    letterGuessCount = 5;
    wordGuessCount = 5;
    boolean gameOver = false;
    boolean win = false;
    printOut();
  }

  public void printOut() {
    String tempOut = "";
    for (int i = 0; i < display.length; i++) {
      tempOut += String.format("%c ", display[i]);
    }
    Outword.setText(tempOut);
    if (Arrays.equals(display, letters)) {
      OutStatus.setText("You Won!");
    } else if (letterGuessCount == 0 && wordGuessCount == 0) {
      OutStatus.setText("Game Over. Answer was: " + wordString);
    } else {
      OutStatus.setText("");
    }
    tempOut = "";
    for (int i = 0; i < letterGuessCount * -1 + 5; i++) {
      tempOut += String.format("%c ", guesses[i]);
    }
    Outletter.setText(tempOut);
    Outlguess.setText(letterGuessCount + "");
    Outwguess.setText(wordGuessCount + "");
    Inletter.setText("");
    Inword.setText("");
  }

  public void actionPerformed(ActionEvent evt) {
    if (evt.getSource().equals(Inletter) && letterGuessCount > 0 && !Inletter.getText().equals("")) {
      methodInletter();
    }
    if (evt.getSource().equals(Inword) && wordGuessCount > 0 && !Inword.getText().equals("")) {
      methodInword();
    }
    printOut();
    if (evt.getSource().equals(startOverButton)) {
      initialize();
    }
  }

  public void methodInletter() {
    boolean valid = true;
    char letterIn = Character.toLowerCase(Inletter.getText().charAt(0));
    if (!Character.isLowerCase(letterIn)) {
      valid = false;
    } else {
      for (int i = 0; i < guesses.length; i++) {
        if (letterIn == guesses[i]) {
          valid = false;
        }
      }
    }
    if (valid) {
      guesses[letterGuessCount * -1 + 5] = letterIn;
      letterGuessCount--;
      for (int i = 0; i < letters.length; i++) {
        if (letters[i] == letterIn) {
          display[i] = letters[i];
        }
      }
    }
  }

  public void methodInword() {
    if (Inword.getText().toLowerCase().equals(wordString)) {
      display = letters;
    }
    wordGuessCount--;
  }
}