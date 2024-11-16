package battleship;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        boolean player_main = true;
        System.out.println("Player 1, place your ships on the game field");
        GameBoard gameBoard_1 = new GameBoard();
        gameBoard_1.buildBoard();
        gameBoard_1.printBoard();
        gameBoard_1.builMask();
        gameBoard_1.buildBoard();

        BattleShip aircraftCarrier_1 = createShip(SHIPNAME.AC,gameBoard_1);
        BattleShip battleship_1 = createShip(SHIPNAME.BS,gameBoard_1);
        BattleShip submarine_1 = createShip(SHIPNAME.SB,gameBoard_1);
        BattleShip cruiser_1 = createShip(SHIPNAME.CR,gameBoard_1);
        BattleShip destroyer_1 = createShip(SHIPNAME.DR,gameBoard_1);

        player_main = enter(player_main);
        //System.out.print(player_main);

        System.out.println("Player 2, place your ships on the game field");
        GameBoard gameBoard_2 = new GameBoard();
        gameBoard_2.buildBoard();
        gameBoard_2.printBoard();
        gameBoard_2.builMask();
        gameBoard_2.buildBoard();

        BattleShip aircraftCarrier_2 = createShip(SHIPNAME.AC,gameBoard_2);
        BattleShip battleship_2 = createShip(SHIPNAME.BS,gameBoard_2);
        BattleShip submarine_2 = createShip(SHIPNAME.SB,gameBoard_2);
        BattleShip cruiser_2 = createShip(SHIPNAME.CR,gameBoard_2);
        BattleShip destroyer_2 = createShip(SHIPNAME.DR,gameBoard_2);

        player_main = enter(player_main);
        //System.out.print(player_main);

        while(gameBoard_1.hits<17 || gameBoard_2.hits<17) {

            if(player_main) {
                showBoard(player_main,gameBoard_1,gameBoard_2);
                System.out.println("Player 1, it's your turn:");
                Scanner scanner = new Scanner(System.in);
                String input = scanner.nextLine();
                if(!validateInput(input)){
                    System.out.println("Error! You entered the wrong coordinates! Try again:");
                    continue;
                }
                Shot(input, gameBoard_2, aircraftCarrier_2, battleship_2, submarine_2, cruiser_2, destroyer_2);//player 1 hits board 2
                //Shot(input, gameBoard_1, aircraftCarrier_1, battleship_1, submarine_1, cruiser_1, destroyer_1);//make a record on ownboard
            }else{
                showBoard(player_main,gameBoard_1,gameBoard_2);
                System.out.println("Player 2, it's your turn:");
                Scanner scanner = new Scanner(System.in);
                String input = scanner.nextLine();
                if(!validateInput(input)){
                    System.out.println("Error! You entered the wrong coordinates! Try again:");
                    continue;
                }
                Shot(input, gameBoard_1, aircraftCarrier_1, battleship_1, submarine_1, cruiser_1, destroyer_1);//player 2 hits board 1
                //Shot(input, gameBoard_2, aircraftCarrier_2, battleship_2, submarine_2, cruiser_2, destroyer_2);
            }
            player_main = enter(player_main);
        }

    }
    public static void showBoard(boolean player,GameBoard gameBoard_1, GameBoard gameBoard_2){
        if(player){
            gameBoard_2.printMask();
            System.out.println("---------------------");
            gameBoard_1.printBoard();
        }else{
            gameBoard_1.printMask();
            System.out.println("---------------------");
            gameBoard_2.printBoard();
        }
    }
    public static boolean enter(boolean player){
        System.out.println("Press Enter and pass the move to another player");
        Scanner scanner = new Scanner(System.in);
        String enter = scanner.nextLine();
        if(!enter.equals("")){
            System.out.println("Warnning");
            System.exit(0);
        }
        System.out.println();
        return !player;
    }
    public static void Shot(String input,GameBoard gameBoard, BattleShip aircraftCarrier, BattleShip battleship, BattleShip submarine, BattleShip cruiser, BattleShip destroyer){
        //again, less than 17 because the shot will come later in a method
            Shot shot = new Shot(input);
            String result = gameBoard.gotShot(shot, aircraftCarrier,battleship,submarine,cruiser,destroyer);
            //System.out.println(gameBoard.hits);
            if(gameBoard.hits!=17) {
                //gameBoard.printBoard();
                System.out.println(result);
                //gameBoard.printMask();
            }else{
                //gameBoard.printBoard();
                System.out.println(result);
            }

    }

    public static BattleShip whichShip(int x, int y, BattleShip aircraftCarrier, BattleShip battleShip, BattleShip submarine, BattleShip cruiser, BattleShip destroyer){
        String coordinate = indexToCoordinate(x,y);
        if(aircraftCarrier.shipOnGrid().contains(coordinate)){
            return aircraftCarrier;
        }
        else if(battleShip.shipOnGrid().contains(coordinate)){
            return battleShip;
        }
        else if(submarine.shipOnGrid().contains(coordinate)){
            return submarine;
        }
        else if(cruiser.shipOnGrid().contains(coordinate)){
            return cruiser;
        }
        else if(destroyer.shipOnGrid().contains(coordinate)){
            return destroyer;
        }
        else{
            return null;
        }
    }
    public static String indexToCoordinate(int x, int y){
        String coordinate = (char) ((int)('A' -1 + x)) + String.valueOf(y);
        return coordinate;
    }
    public static BattleShip createShip(SHIPNAME name, GameBoard gameBoard){

        System.out.printf("Enter the coordinates of the %s (%d cells)%n",name.getName(),name.getSize());
        while(true) {
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            String[] inputArr = input.split(" ");
            int rightLength = name.getSize();
            List<String> listOfShipCoordinate = listOfCoordinate(calculateLength(inputArr),inputArr[0],inputArr[1]);
            int inputLength= listOfShipCoordinate.size();
            if(rightLength!=inputLength){
                System.out.printf("Error! Wrong length of the %s! Try again:%n",name.getName());
                continue;
            }
            if(!validateInput(inputArr)) {
                System.out.println("Error! Wrong ship location! Try again:");
                continue;
            }
            if(!hasAdjacent(listOfShipCoordinate,gameBoard)){
                System.out.println("Error! You placed it too close to another one. Try again:");
                continue;
            }
            //need to check if length can instantiate the battleship
            if (!validateShipLocation(inputArr, gameBoard)) {
                System.out.println("Error! Wrong ship location! Try again:");
            } else {
                BattleShip battleship = new BattleShip(inputArr);
                gameBoard.updateBoard(battleship);
                gameBoard.printBoard();
                return battleship;
            }
        }
    }
    public static boolean hasAdjacent(List<String>listOfShipCoordinates, GameBoard gameBoard){
        for(int i=0;i<listOfShipCoordinates.size();i++){
            int[] boardCoordinate = coordinateConvert(listOfShipCoordinates.get(i));
            if(boardCoordinate[0]!=1 && boardCoordinate[0]!=10 && boardCoordinate[1]!=1 && boardCoordinate[1]!=10){
                if(gameBoard.boardState[boardCoordinate[0]+1][boardCoordinate[1]].equals("O")||
                        gameBoard.boardState[boardCoordinate[0]-1][boardCoordinate[1]].equals("O")||
                        gameBoard.boardState[boardCoordinate[0]][boardCoordinate[1]+1].equals("O")||
                        gameBoard.boardState[boardCoordinate[0]+1][boardCoordinate[1]-1].equals("O")) {
                    return false;
                }
            }
            else if (boardCoordinate[0]==1 && boardCoordinate[1]==1){
                if(gameBoard.boardState[boardCoordinate[0]+1][boardCoordinate[1]].equals("O")||
                        gameBoard.boardState[boardCoordinate[0]][boardCoordinate[1]+1].equals("O")) {
                    return false;
                }
            }
            else if (boardCoordinate[0]==10 && boardCoordinate[1]==10){
                if(gameBoard.boardState[boardCoordinate[0]-1][boardCoordinate[1]].equals("O")||
                        gameBoard.boardState[boardCoordinate[0]][boardCoordinate[1]-1].equals("O")) {
                    return false;
                }
            }
            else if (boardCoordinate[0]==1 && boardCoordinate[1]==10){
                if(gameBoard.boardState[boardCoordinate[0]+1][boardCoordinate[1]].equals("O")||
                        gameBoard.boardState[boardCoordinate[0]][boardCoordinate[1]-1].equals("O")) {

                    return false;
                }
            }
            else if (boardCoordinate[0]==10 && boardCoordinate[1]==1){
                if(gameBoard.boardState[boardCoordinate[0]-1][boardCoordinate[1]].equals("O")||
                        gameBoard.boardState[boardCoordinate[0]][boardCoordinate[1]+1].equals("O")) {
                    return false;
                }
            }
            else if (boardCoordinate[0]==1) {
                if(gameBoard.boardState[boardCoordinate[0]+1][boardCoordinate[1]].equals("O")||
                        gameBoard.boardState[boardCoordinate[0]][boardCoordinate[1]+1].equals("O")||
                        gameBoard.boardState[boardCoordinate[0]][boardCoordinate[1]-1].equals("O")) {
                    return false;
                }
            }
            else if (boardCoordinate[0]==10){
                if(gameBoard.boardState[boardCoordinate[0]-1][boardCoordinate[1]].equals("O")||
                        gameBoard.boardState[boardCoordinate[0]][boardCoordinate[1]-1].equals("O")||
                        gameBoard.boardState[boardCoordinate[0]][boardCoordinate[1]+1].equals("O")) {
                    return false;
                }
            }
            else if (boardCoordinate[1]==1){
                if(gameBoard.boardState[boardCoordinate[0]+1][boardCoordinate[1]].equals("O")||
                        gameBoard.boardState[boardCoordinate[0]-1][boardCoordinate[1]].equals("O")||
                        gameBoard.boardState[boardCoordinate[0]][boardCoordinate[1]+1].equals("O")) {
                    return false;
                }
            }
            else if (boardCoordinate[1]==10){
                if(gameBoard.boardState[boardCoordinate[0]-1][boardCoordinate[1]].equals("O")||
                        gameBoard.boardState[boardCoordinate[0]+1][boardCoordinate[1]].equals("O")||
                        gameBoard.boardState[boardCoordinate[0]][boardCoordinate[1]-1].equals("O")) {
                    return false;
                }
            }
        }
        return true;
    }
    public static List<String>listOfCoordinate(int length, String startCoordinate, String endCoordinate) {
        List<String> shipLocation = new ArrayList<>();
        for(int i =0; i<length;i++) {
            if (startCoordinate.charAt(0) == endCoordinate.charAt(0) && Integer.parseInt(startCoordinate.substring(1)) > Integer.parseInt(endCoordinate.substring(1))) {
                String x = startCoordinate.charAt(0) + String.valueOf(Integer.parseInt(startCoordinate.substring(1)) - i);
                //System.out.println(x);
                shipLocation.add(x);
            } else if (startCoordinate.charAt(0) == endCoordinate.charAt(0) && Integer.parseInt(startCoordinate.substring(1)) < Integer.parseInt(endCoordinate.substring(1))) {
                String x = startCoordinate.charAt(0) + String.valueOf(Integer.parseInt(startCoordinate.substring(1)) + i);
                //System.out.println(x);
                shipLocation.add(x);
            } else if ((int) startCoordinate.charAt(0) > (int) endCoordinate.charAt(0)) {
                String x = (char) ((int) startCoordinate.charAt(0) - i) + String.valueOf(Integer.parseInt(startCoordinate.substring(1)));
                //System.out.println(x);
                shipLocation.add(x);
            } else {
                String x = (char) ((int) startCoordinate.charAt(0) + i) + String.valueOf(Integer.parseInt(startCoordinate.substring(1)));
                //System.out.println(x);
                shipLocation.add(x);
            }
        }
        return shipLocation;
    }
    public static boolean validateShipLocation(String[] inputArr, GameBoard gameBoard){
        int length = calculateLength(inputArr);
        String startCoordinate = inputArr[0];
        String endCoordinate = inputArr[1];

        List<String> shipLocation = listOfCoordinate(length,startCoordinate,endCoordinate);

        for(int i=0;i<shipLocation.size();i++){
            int[] shipCoordinate =coordinateConvert(shipLocation.get(i));
            if(gameBoard.boardState[shipCoordinate[0]][shipCoordinate[1]].equals("O")){
                return false;
            }
        }
        return true;
    }
    public static boolean validateInput(String[] split){
        if(Integer.parseInt(split[0].substring(1))>10 || Integer.parseInt(split[1].substring(1))>10){
            return false;
        }
        if(split[0].charAt(0)!=split[1].charAt(0) && split[0].charAt(1)!=split[1].charAt(1)){
            return false;
        }
        return true;
    }
    public static boolean validateInput(String input){
        if(Integer.parseInt(input.substring(1))>10 || Integer.parseInt(input.substring(1))>10){
            return false;
        }
        if(input.charAt(0)<'A' || input.charAt(0)>'J'){
            return false;
        }
        return true;
    }
    public static int calculateLength(String[] split){
        if(split[0].charAt(0)==split[1].charAt(0)){
            return Math.abs(Integer.parseInt(split[0].substring(1)) - Integer.parseInt(split[1].substring(1)))+1;
        }else{
            return Math.abs((int)split[0].charAt(0) - (int)split[1].charAt(0))+1;
        }
    }
    public static int[] coordinateConvert(String input){
        int firstElement = input.charAt(0)- (int)'A'+1;
        int secondElement = Integer.parseInt(input.substring(1));
        int[] coordinate = {firstElement,secondElement};
        return coordinate;
    }
    public static class GameBoard {
        int hits = 0;
        String[][] boardState = new String[11][11];
        String[][] maskState = new String[11][11];
        public void buildBoard(){
            for (int i = 1; i < 11; i++) {
                this.boardState[0][i] = String.valueOf(i);
            }
            for (int j = 1; j < 11; j++) {
                this.boardState[j][0] = String.valueOf((char) ((int) 'A' + j - 1));
            }
            for (int i = 1; i<11;i++){
                for (int j =1; j<11;j++){
                    this.boardState[i][j]="~";
                }
            }
            boardState[0][0]=" ";
        }

        public void builMask(){
            for (int i = 1; i < 11; i++) {
                this.maskState[0][i] = String.valueOf(i);
            }
            for (int j = 1; j < 11; j++) {
                this.maskState[j][0] = String.valueOf((char) ((int) 'A' + j - 1));
            }
            for (int i = 1; i<11;i++){
                for (int j =1; j<11;j++){
                    this.maskState[i][j]="~";
                }
            }
            maskState[0][0]=" ";
        }


        String gotShot(Shot shot, BattleShip aircraftCarrier, BattleShip battleShip, BattleShip submarine, BattleShip cruiser, BattleShip destroyer){
            int x = shot.coordinate[0];
            int y = shot.coordinate[1];
            if(boardState[x][y].equals("O")){
                this.boardState[x][y] = "X";
                this.maskState[x][y] ="X";
                whichShip(x, y, aircraftCarrier,battleShip,submarine,cruiser,destroyer).gotHit();
                boolean sank = whichShip(x, y, aircraftCarrier,battleShip,submarine,cruiser,destroyer).gotSank();
                this.hits++;
                if(sank && hits!=17) { //use 17 instead of 18 because the shot is called in a different methods after the check, so it's one loop before 18
                    return "You sank a ship! Specify a new target:";
                } else if(sank && hits == 17){
                        return "You sank the last ship. You won. Congratulations!";
                }else{
                    return "You hit a ship!";
                }
            }else if(boardState[x][y].equals("~")){
                this.boardState[x][y] = "M";
                this.maskState[x][y]="M";
                return "You missed!";
            }else{
                return "You hit a ship!";
            }
        }
        public void printBoard(){
            for (String[] x:boardState){
                for (String y : x){
                    System.out.print(y + " ");
                }
                System.out.printf("%n");
            }
        }
        public void printMask(){
            for (String[] x:maskState){
                for (String y : x){
                    System.out.print(y + " ");
                }
                System.out.printf("%n");
            }
        }

        public void updateBoard(BattleShip battleShip){
            List<String> location = battleShip.shipOnGrid();
                for(int i=0;i<location.size();i++){
                    int[] coordinate = coordinateConvert(location.get(i));
                    boardState[coordinate[0]][coordinate[1]]="O";
                }
        }


    }
    public static class BattleShip{
        int hits = 0;
        int length;
        String startCoordinate;
        String endCoordinate;
        public BattleShip(String[] input){
            this.length=calculateLength(input);
            this.startCoordinate =input[0];
            this.endCoordinate = input[1];
        }
        public List<String> shipOnGrid(){
            List<String> shipLocation = listOfCoordinate(length,startCoordinate,endCoordinate);

            return shipLocation;
        }
        public void gotHit(){
            this.hits++;
        }
        public boolean gotSank(){
            if(hits == length){
                return true;
            }
            return false;
        }
    }
    enum SHIPNAME{
        AC (5, "Aircraft Carrier"),
        BS (4, "Battleship"),
        SB (3, "Submarine"),
        CR (3, "Cruiser"),
        DR  (2, "Destroyer");
        int size;
        String name;
        SHIPNAME(int size, String name){
            this.size = size;
            this.name = name;
        }
        public int getSize(){
            return size;
        }
        public String getName(){
            return name;
        }
    }
    public static class Shot{
        int[] coordinate;
        Shot(String input){
            int[] inputCoordinate = coordinateConvert(input);
            this.coordinate = inputCoordinate;
        }
    }


}


