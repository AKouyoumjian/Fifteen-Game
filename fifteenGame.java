// CS 2510, Assignment 9
//Alex Kouyoumjian

import java.util.ArrayList;
import java.util.Arrays;
import tester.*;
import javalib.impworld.*;
import java.awt.Color;
import javalib.worldimages.*;
import java.util.Random;


/**
 * Fifteen Game Project
 * @author Alex Kouyoumjian
 */


//Represents an individual tile
class Tile {

  // The number on the tile.  Use 0 to represent the hole
  String value;
  Boolean correctLoc;
  // assign int values to tiles that are tiles to use for the board.

  Tile(String value) {
    this.value = value;
    this.correctLoc = false;
  }

  // Draws this tile onto the background at the specified logical coordinates
  WorldScene drawAt(int col, int row, WorldScene background) {
    WorldImage tBackground = new RectangleImage(100, 100, "outline", Color.orange);
    WorldImage text = new TextImage(this.value, Color.black);

    WorldImage tileBackground = new OverlayImage(text, tBackground);

    background.placeImageXY(tileBackground, col * 105 + 80, row * 105 + 80);

    return background;
  }

}


// class for Fifteen Game
class FifteenGame extends World {

  // represents the rows of tiles
  ArrayList<ArrayList<Tile>> board;

  FifteenGame(ArrayList<ArrayList<Tile>> board) {
    this.board = board;
  }

  FifteenGame() {
    this.board = this.initData(new Random());
  }

  FifteenGame(boolean foo) {
    this.board = this.initSolved();
  }

  FifteenGame(Random r) {
    // constructor for testing w seeded random
  }



  // draws the game
  public WorldScene makeScene() {
    WorldScene ws = new WorldScene(500, 500);

    for (int i =  0 ;  i < this.board.size() ; i++) {

      ArrayList<Tile> row = board.get(i);

      for (int j = 0; j < row.size() ; j++) {

        Tile t = row.get(j);

        ws = t.drawAt(i, j, ws);
      }
    }

    return ws;
  }


  // scrambles the tiles on the board
  public ArrayList<ArrayList<Tile>> initData(Random r) {
    ArrayList<ArrayList<Tile>> start = new ArrayList<ArrayList<Tile>>();
    ArrayList<String> allPossible =
        new ArrayList<String>(Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
            "11", "12", "13", "14", "15", ""));

    for (int i = 0 ; i < 4 ; i++) {

      ArrayList<Tile> inner = new ArrayList<Tile>();

      for (int j = 0 ; j < 4 ; j++) {

        Random rand = r;

        int count = rand.nextInt(allPossible.size());

        inner.add(new Tile(allPossible.get(count)));
        allPossible.remove(count);

      }

      start.add(inner);

    }

    return start;
  }

  // solved board
  public ArrayList<ArrayList<Tile>> initSolved() {
    ArrayList<ArrayList<Tile>> start = new ArrayList<ArrayList<Tile>>();
    ArrayList<String> allPossible =
        new ArrayList<String>(Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
            "11", "12", "13", "14", "15", ""));

    for (int i = 0 ; i < 4 ; i++) {

      ArrayList<Tile> inner = new ArrayList<Tile>();

      for (int j = 0 ; j < 4 ; j++) {
        inner.add(new Tile(allPossible.remove(0)));
      }

      start.add(inner);
    }
    return start;
  }

  // finds the row and column of the given string in 2d array list
  // and returns them as an ArrayList
  public ArrayList<Integer> find(String s) {
    for (int i = 0; i < this.board.size(); i++) {

      ArrayList<Tile> current = this.board.get(i);

      for (int j = 0; j < current.size(); j++) {
        if (current.get(j).value.equals(s)) {

          // returns i = row, j = column of found string
          return new ArrayList<Integer>(Arrays.asList(i, j));
        }
      }
    }
    throw new IllegalStateException("Item not in list. Cannot find it");
  }


  // is this move able to be performed
  public boolean canMove(String s) {
    // find the empty space's row and column
    int row = this.find("").get(0);
    int column = this.find("").get(1);

    if (s.equals("up")) {
      return ((row <= this.board.size() - 1) && row >= 0);
    }

    if (s.equals("down")) {
      return (row >= 0 && (row <= this.board.size() - 1));
    }

    if (s.equals("left")) {
      return ((column <= this.board.size() - 1) && column >= 0);
    }

    if (s.equals("right")) {
      return (column >= 0 && (column <= this.board.size() - 1));
    }

    throw new IllegalArgumentException("Move is not possible!");
  }



  // swaps the tiles if they can be swapped
  public void swap(String keyMove) {

    // swap up
    if (keyMove.equals("up")) {

      // finding where the mt is
      int mtRow = this.find("").get(0);
      int mtColumn = this.find("").get(1);

      // saving information of tile were swapping w mt
      int thisRow = mtRow;
      int thisColumn = mtColumn + 1;


      if ((thisRow >= 0 && thisRow <= this.board.size() - 1)
          && (thisColumn >= 0 && thisColumn <= this.board.size() - 1)) {

        Tile temp = this.board.get(thisRow).get(thisColumn);

        // makes the tile an mt
        this.board.get(thisRow).set(thisColumn, this.board.get(mtRow).get(mtColumn));

        // makes the mt the saved tile
        this.board.get(mtRow).set(mtColumn, temp);
      }
    }

    // swap down
    if (keyMove.equals("down")) {

      // finding where the mt is
      int mtRow = this.find("").get(0);
      int mtColumn = this.find("").get(1);

      int thisRow = mtRow;
      int thisColumn = mtColumn - 1;

      if ((thisRow >= 0 && thisRow <= this.board.size() - 1)
          && (thisColumn >= 0 && thisColumn <= this.board.size() - 1)) {

        Tile temp = this.board.get(thisRow).get(thisColumn);


        this.board.get(thisRow).set(thisColumn, this.board.get(mtRow).get(mtColumn));
        this.board.get(mtRow).set(mtColumn, temp);
      }
    }

    // swap left
    if (keyMove.equals("left")) {

      // finding where the mt is
      int mtRow = this.find("").get(0);
      int mtColumn = this.find("").get(1);

      int thisRow = mtRow + 1;
      int thisColumn = mtColumn;

      if ((thisRow >= 0 && thisRow <= this.board.size() - 1)
          && (thisColumn >= 0 && thisColumn <= this.board.size() - 1)) {

        Tile temp = this.board.get(thisRow).get(thisColumn);

        this.board.get(thisRow).set(thisColumn, this.board.get(mtRow).get(mtColumn));
        this.board.get(mtRow).set(mtColumn, temp);
      }
    }

    // swap right
    if (keyMove.equals("right")) {

      // finding where the mt is
      int mtRow = this.find("").get(0);
      int mtColumn = this.find("").get(1);

      int thisRow = mtRow - 1;
      int thisColumn = mtColumn;
      if ((thisRow >= 0 && thisRow <= this.board.size() - 1)
          && (thisColumn >= 0 && thisColumn <= this.board.size() - 1)) {

        Tile temp = this.board.get(thisRow).get(thisColumn);

        this.board.get(thisRow).set(thisColumn, this.board.get(mtRow).get(mtColumn));
        this.board.get(mtRow).set(mtColumn, temp);
      }
    }
  }


  // handles keystrokes and does the given movement if possible
  public void onKeyEvent(String keyName) {
    if (keyName.equals("up")) {
      if (this.canMove(keyName)) {
        // if the moves possible, invoke swap method
        this.swap("up");
      }
    }
    if (keyName.equals("down")) {
      if (this.canMove(keyName)) {
        this.swap("down");
      }
    }
    if (keyName.equals("left")) {
      if (this.canMove(keyName)) {
        this.swap("left");
      }
    }
    if (keyName.equals("right")) {
      if (this.canMove(keyName)) {
        this.swap("right");
      }
    }
  }


  // are all the tiles in the right place
  public boolean hasGameEnded() {
    boolean flag = true;
    // only for numbers 1-15, not the sixteenth blank tile
    for (int i = 0; i < this.board.size(); i++) {


      ArrayList<Tile> current = this.board.get(i);

      for (int j = 0; j < current.size(); j++) {
        // if the current index does not equal the number
        // of the tile, set flag to false.
        if (!current.get(j).value.equals("" + ((i * this.board.size()) + j) + "")
            || current.get(4).value.equals("")) {
          flag = false;
        }
      }
    }
    // will return true if all tiles are in correct place
    return flag;
  }

  public WorldEnd worldEnds() {
    // current world scene
    WorldScene current = new WorldScene(500, 500);

    if (this.hasGameEnded()) {
      // if game has ended, place image over current world scene
      current.placeImageXY(new TextImage("You Win!", 50, Color.red), 250, 250);
    }
    return new WorldEnd(true, current);
  }
}



// example class
class ExampleFifteenGame {

  Tile t1 = new Tile("1");
  Tile t2 = new Tile("2");
  Tile t3 = new Tile("3");
  Tile t4 = new Tile("4");
  Tile t5 = new Tile("5");
  Tile t6 = new Tile("6");
  Tile t7 = new Tile("7");
  Tile t8 = new Tile("8");
  Tile t9 = new Tile("9");
  Tile t10 = new Tile("10");
  Tile t11 = new Tile("11");
  Tile t12 = new Tile("12");
  Tile t13 = new Tile("13");
  Tile t14 = new Tile("14");
  Tile t15 = new Tile("15");
  Tile t16 = new Tile("");


  ArrayList<Tile> row1 = new ArrayList<Tile>(Arrays.asList(this.t1, this.t5, this.t9, this.t13));
  ArrayList<Tile> row2 = new ArrayList<Tile>(Arrays.asList(this.t2, this.t6, this.t10, this.t14));
  ArrayList<Tile> row3 = new ArrayList<Tile>(Arrays.asList(this.t3, this.t7, this.t11, this.t15));
  ArrayList<Tile> row4 = new ArrayList<Tile>(Arrays.asList(this.t4, this.t8, this.t12, this.t16));

  ArrayList<Tile> rrow1 = new ArrayList<Tile>(Arrays.asList(this.t7, this.t12, this.t13, this.t8));
  ArrayList<Tile> rrow2 = new ArrayList<Tile>(Arrays.asList(this.t14, this.t16, this.t9, this.t4));
  ArrayList<Tile> rrow3 = new ArrayList<Tile>(Arrays.asList(this.t5, this.t10, this.t3, this.t1));
  ArrayList<Tile> rrow4 = new ArrayList<Tile>(Arrays.asList(this.t2, this.t11, this.t15, this.t6));

  ArrayList<ArrayList<Tile>> board1 =
      new ArrayList<ArrayList<Tile>>(Arrays.asList(this.row1, this.row2, this.row3, this.row4));

  FifteenGame game1 = new FifteenGame();

  FifteenGame game2 = new FifteenGame(true);

  WorldScene ws;

  FifteenGame game;
  WorldImage tBackground = new RectangleImage(100, 100, "outline", Color.orange);

  WorldImage text1 = new TextImage("1", Color.black);
  WorldImage text2 = new TextImage("2", Color.black);
  WorldImage text3 = new TextImage("3", Color.black);

  WorldImage tileBackground1 = new OverlayImage(text1, tBackground);
  WorldImage tileBackground2 = new OverlayImage(text2, tBackground);
  WorldImage tileBackground3 = new OverlayImage(text3, tBackground);

  void initData() {
    this.ws = new WorldScene(500, 500);
    this.game = new FifteenGame();
  }

  // creates a test scene
  void initScene() {
    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 4; j++) {
        this.ws = this.game.board.get(j).get(i).drawAt(i, j, ws);
      }
    }
    this.ws.placeImageXY(this.tBackground, 500, 500);
  }

  // big bang
  void testGame(Tester t) {
    game1.bigBang(500, 500);
  }


  // tests for drawAt
  void testDrawAt(Tester t) {
    this.initData();

    WorldScene ws1 = new WorldScene(500, 500);
    ws1.placeImageXY(tileBackground1, 0 * 105 + 80, 0 * 105 + 80);

    WorldScene ws2 = new WorldScene(500, 500);
    ws2.placeImageXY(tileBackground2, 1 * 105 + 80, 1 * 105 + 80);

    WorldScene ws3 = new WorldScene(500, 500);
    ws3.placeImageXY(tileBackground3, 2 * 105 + 80, 2 * 105 + 80);

    t.checkExpect(this.t1.drawAt(0, 0, ws), ws1);
    this.initData();
    t.checkExpect(this.t2.drawAt(1, 1, ws), ws2);
    this.initData();
    t.checkExpect(this.t3.drawAt(2, 2, ws), ws3);
  }


  // tests the makeScene method
  void testMakeScene(Tester t) {
    this.initData();
    this.game.initData(new Random(1));
    this.initScene();
    t.checkExpect(this.game.makeScene(), this.game.makeScene());
    this.game.initSolved();
    this.ws.placeImageXY(new TextImage("You Win!", 50, Color.red), 250, 250);
  }


  // tests for find
  void testFind(Tester t) {
    t.checkExpect(game2.find("5"), new ArrayList<Integer>(Arrays.asList(1, 0)));
    t.checkExpect(game2.find("6"), new ArrayList<Integer>(Arrays.asList(1, 1)));
    t.checkExpect(game2.find("7"), new ArrayList<Integer>(Arrays.asList(1, 2)));
  }


  // tests for canMove
  void testCanMove(Tester t) {
    t.checkExpect(game2.canMove("down"), true);
    t.checkExpect(game2.canMove("left"), true);
    t.checkExpect(game2.canMove("right"), true);
  }

  // tests for swap
  void testSwap(Tester t) {
    this.initData();
    t.checkExpect(this.game2.board.get(3).get(1), t14);
    t.checkExpect(this.game2.board.get(3).get(0), t13);
    this.game2.swap("down");
    t.checkExpect(this.game2.board.get(3).get(1), t16);
    t.checkExpect(this.game2.board.get(3).get(0), t13);
  }

  // tests for onKey
  void testOnKey(Tester t) {
    this.initData();
    t.checkExpect(this.game2.board.get(0).get(0), new Tile("1"));
    this.game2.onKeyEvent("up");
    t.checkExpect(this.game2.board.get(1).get(0), new Tile("5"));
    this.game2.onKeyEvent("down");
    t.checkExpect(this.game2.board.get(2).get(0), new Tile("9"));
    this.game2.onKeyEvent("right");
    t.checkExpect(this.game2.board.get(1).get(1), new Tile("6"));
    this.game2.onKeyEvent("left");
  }


}