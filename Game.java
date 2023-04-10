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
        Room eventCtr, theater, cafe, testingCtr, ctrBldg, BuildW, staircase, collegeCtr, whitmanBldg, huntHall, prkLot1, lot2, lot3, lot4, busStop, planetarium, gameLab, entrance, limits;
      
        // create the rooms
            eventCtr = new Room("inside the main eventCtr of the university");  //event center
            theater = new Room("in the commons theater"); //theatre
            testingCtr = new Room("in the testing center"); //Somerset Test Center

            ctrBldg = new Room("in the College Center building"); //College Center
            whitmanBldg = new Room ("you are in the Whitman Science Building"); // Whitman Science Center
            huntHall = new Room ("you are in the Hunterdon Hall Building"); //Hunterdon Hall
                planetarium = new Room ("at the Planetarium/Observatory"); //at the Planetarium

            
            collegeCtr = new Room ("you are at the College Center Courtyard"); //center of the campus (outdoor)
            staircase = new Room ("Stairwell to Cafe"); //College Center Stairwell
            cafe = new Room("in the campus cafe"); //College Center cafe
            
            
            BuildW = new Room ("in the West Building"); //West Building
                gameLab = new Room ("Game Lab in West Building"); //W113
            
            entrance = new Room ("in the main entrance off Rt. 22"); //Main two lane service road.
                prkLot = new Room ("in the parking lot"); //parking lot
                busStop = new Room ("at the CAT and NJ Transit bus stops"); //in front of theater
                
            lot2 = new Room ("in the parking lot");
            lot3 = new Room ("in the parking lot");
            lot4 = new Room ("in the parking lot");

                
            limits = new Room ("you have reached the boundaries of RVCC!");
                
        // initialise room exits
        eventCtr.setExit("north", collegeCtr);
        eventCtr.setExit("south", prkLot1);
        eventCtr.setExit("east", testingCtr);
        eventCtr.setExit("west", theater);

        theater.setExit("north", limits);
        theater.setExit("south", busStop);
        theater.setExit("east", limits);
        theater.setExit("west", eventCtr); 

        cafe.setExit ("north", limits);
        cafe.setExit ("south", limits);
        cafe.setExit ("east", limits);
        cafe.setExit("west", staircase);

        testingCtr.setExit("north", limits);
        testingCtr.setExit("south", lot2);
        testingCtr.setExit("east", collegeCtr);
        testingCtr.setExit("west", limits);
        
        ctrBldg.setExit("north", staircase);
        ctrBldg.setExit("south", collegeCtr);
        ctrBldg.setExit("east", huntHall);
        ctrBldg.setExit("west", BuildW);
        
        BuildW.setExit ("north", whitmanBldg);
        BuildW.setExit ("south", gameLab);
        BuildW.setExit ("east", collegeCtr);
        BuildW.setExit ("west", lot3);

        prkLot1.setExit ("north", eventCtr );
        prkLot1.setExit ("south", limits);
        prkLot1.setExit ("east", busStop);
        prkLot1.setExit ("west", lot2);
        
        lot2.setExit("north", testingCtr);
        lot2.setExit("south", limits);
        lot2.setExit("east", prkLot1);
        lot2.setExit("west", limits);
        
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
