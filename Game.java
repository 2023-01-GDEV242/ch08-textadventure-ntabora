/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game. 
 * 
 * 
 * @author  Nathaniel Tabora
 * @version 2023.03.23
 */

public class Game 
{
    private Parser parser;
    private Room currentRoom;
        
    /**
     * Create the game and initialise its internal map.
     */
    
    public Game() 
    {
        createRooms();
        parser = new Parser();
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        Room eventCtr, theater, cafe, testingCtr, ctrBldg, BuildW, staircase, collegeCtr, whitmanBldg, huntHall, prkLot;
      
        // create the rooms
        eventCtr = new Room("inside the main eventCtr of the university");  //event center
            theater = new Room("in the commons theater"); //theatre
            testingCtr = new Room("in the testing center"); //Somerset Test Center

        ctrBldg = new Room("in the College Center building"); //College Center
                
        collegeCtr = new Room ("you are at the College Center Courtyard"); //center of the campus (outdoor)
            staircase = new Room ("Stairwell to Cafe"); //College Center Stairwell
            cafe = new Room("in the campus cafe"); //College Center cafe
            
        whitmanBldg = new Room ("you are in the Whitman Science Building"); // Whitman Science Center
        huntHall = new Room ("you are in the Hunterdon Hall Building"); //Hunterdon Hall
        BuildW = new Room ("in the West Building"); //West Building
        prkLot = new Room ("in the parking lot"); //parking lot

        
        
        // initialise room exits
        eventCtr.setExit("east", theater);
        eventCtr.setExit("west", testingCtr);
        eventCtr.setExit("north", collegeCtr);

        theater.setExit("south", eventCtr);

        cafe.setExit("east", staircase);

        testingCtr.setExit("south", eventCtr);
        testingCtr.setExit("east", collegeCtr);

        ctrBldg.setExit("east", huntHall);
        ctrBldg.setExit("north", staircase);
        ctrBldg.setExit("west", collegeCtr);
        ctrBldg.setExit("south", collegeCtr);
        
        BuildW.setExit ("north", prkLot);
        BuildW.setExit ("south", collegeCtr);
        
        

        currentRoom = eventCtr;  // start game inside the main building
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
                
        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to the World of Zuul!");
        System.out.println("World of Zuul is a new, incredibly [insert verb] adventure game.");
        System.out.println("Type '" + CommandWord.HELP + "' if you need help.");
        System.out.println();
        System.out.println(currentRoom.getLongDescription());
    }

    
    private void lookAround()
    {
        System.out.println("North is: " + getNorth());
        System.out.println("South is: " + getSouth());    
        System.out.println("East is: " + getEast());    
        System.out.println("West is: " + getWest());                              
    }
    
    
    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        CommandWord commandWord = command.getCommandWord();

        switch (commandWord) {
            case UNKNOWN:
                System.out.println("I don't know what you mean...");
                break;

            case HELP:
                printHelp();
                break;

            case GO:
                goRoom(command);
                break;

            case QUIT:
                wantToQuit = quit(command);
                break;
                
            case LOOK:
                lookAround();
                break;
        }
        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around at the university.");
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
    }

    /** 
     * Try to go in one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        else {
            currentRoom = nextRoom;
            System.out.println(currentRoom.getLongDescription());
        }
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }
    
    /**
     * Allows the game to be started up without the need of the BlueJ integrator/IDE
     */
        public void main ()
    {
        createRooms();
        play();
    }
}
