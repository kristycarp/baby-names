//Kristy Carpenter, Computer Science II, Fall 2014, Section C (4th period)
//Assignment 6--Baby Names Option
//
//This program takes a name input from the user and searches through data from the Social Security
//Administration to find its popularity ranks throughout decades and its meaning. If the name was
//found, it prints this information to the console and then creates a graph in a drawing panel.
//This graph has the meaning at the top, year labels at the bottom, and bars showing a visual
//depiction of the name's popularity (the bigger the bar, the more popular the name). Class
//constants control the starting year as well as the dimensions of important aspects of the graph.
//The width of the graph stays constant at 780 pixels, regardless of the amount of the name's data.


import java.util.*; //for scanner
import java.io.*; //for file input
import java.awt.*; //for drawing panel

public class BabyNames
{
   /**
     *This constant indicates the starting decade of the data
     */
   public static final int STARTING_YEAR = 1890;
   
   /**
     *This constant controls the width of each decade in the graph. The width of each bar is half
     *this value.
     */
   public static final int WIDTH = 60;
   
   /**
     *This constant controls the height of the gray boxes at the top and the bottom of the drawing
     *panel graphic. It also adjusts the size of the drawing panel to accomidate the box size.
     */
   public static final int HEIGHT = 30;
   
   /**
     *This constant controls the height of the area where the bars of the graph are drawn.
     */
   public static final int GRAPH_HEIGHT = 500;
   
   /**
     *this is the main method, where the program begins
     *
     *@param args
     */
   public static void main(String[]args)
   {
      Scanner dataScan = makeAScanner("names.txt");
      Scanner meaningsScan = makeAScanner("meanings.txt");
      
      if (dataScan != null && meaningsScan != null)
      {
         String name = intro();
         String nameData = getName(dataScan, name);
         String nameMeaning = getName(meaningsScan, name);
         if (nameData != null && meaningsScan != null)
         {
            DrawingPanel panel = new DrawingPanel(780, GRAPH_HEIGHT + 2 * HEIGHT);
            Graphics g = panel.getGraphics();
            drawSetShapes(g);
            int nDecades = findDecades(nameData);
            if (nDecades != 0)
            {
               drawUniqueShapes(g, nameMeaning, nameData, nDecades);
            }
            else
            {
               System.out.println("Error: line containing " + name + "does not have adequate data");
            }
         }
         else
         {
            System.out.println("\"" + name + "\" not found.");
         }
      }
      else
      {
         System.out.println("An error ocurred. One of the files could not be opened.");
      }
   }
   
   /**
     *This method creates a scanner that can read an input file, then returns it to the main method
     *
     *@param filename - the name/location of the file to read in
     *@return fileScan - the scanner which can read the given file
     */
   public static Scanner makeAScanner(String filename)
   {
      Scanner fileScan = null;
      File file = null;
      try
      {
         file = new File(filename);
         fileScan = new Scanner(file);
      }
      catch (FileNotFoundException e)
      {
         System.out.println("Error opening file: " + filename);
         return null;
      }
      return fileScan;
   }
   
   /**
     *This method prints the introduction to the program, then prompts the user for a name. It
     *returns the name typed by the user.
     *
     *@return name - the name typed by the user
     */
   public static String intro()
   {
      System.out.println("This program allows you to search through the");
      System.out.println("data from the Social Security Administration");
      System.out.println("to see how popular a particular name has been");
      System.out.println("since 1890.");
      System.out.println("");
      System.out.print("Name: ");
      Scanner inputScan = new Scanner(System.in);
      String name = inputScan.next();
      return name;
   }
   
   /**
     *This method goes through the given file, checking each line to see if one starts with the
     *name given by the user. If one does, it takes the whole line (containing the name and its
     *data), prints it, and returns it to be used elsewhere in the program. If no such line was
     *found, it returns null, and an error message is be printed later.
     *
     *@param fileScan - the scanner that can read in the desired file
     *@param name - the name typed in by the user
     *@return line - the line of data starting with the name the user typed in
     */
   public static String getName(Scanner fileScan, String name)
   {
      boolean nameFound = false;
      while (fileScan.hasNextLine() && !nameFound)
      {
         String line = fileScan.nextLine();
         Scanner lineScan = new Scanner(line);
         String nameInCurrentLine = lineScan.next();
         if (name.equalsIgnoreCase(nameInCurrentLine))
         {
            nameFound = true;
            System.out.println(line);
            return line;
         }
      }
      return null;
   }
   
   /**
     *This method draws the shapes on the drawing panel that have no relation to the name's data.
     *These are the gray boxes at the top and the bottom (whose height is determined by the class
     *constant HEIGHT) and the black lines along their top or bottom.
     *
     *@param g - the graphics context
     */
   public static void drawSetShapes(Graphics g)
   {
      g.setColor(Color.LIGHT_GRAY);
      g.fillRect(0, 0, 780, HEIGHT);
      g.fillRect(0, GRAPH_HEIGHT + HEIGHT, 780, HEIGHT);
      g.setColor(Color.BLACK);
      g.drawLine(0, HEIGHT, 780, HEIGHT);
      g.drawLine(0, 500 + HEIGHT, 780, GRAPH_HEIGHT + HEIGHT);
   }
   
   /**
     *This method finds and returns the number of decades for which there are data, to be used
     *while drawing the graph.
     *
     *@param nameData - the line of data which has the ranks of the name throughout a certain
     *number of decades.
     *@return nDecades - the number of decades with data in the given line
     */
   public static int findDecades(String nameData)
   {
      Scanner lineScan = new Scanner(nameData);
      lineScan.next(); //discard the name
      lineScan.next(); //discard the gender
      int nDecades = 0;
      while (lineScan.hasNextInt()) //counts the number of integers following
      {
         lineScan.nextInt();
         nDecades++;
      }
      return nDecades;
   }
   
   /**
     *This method creates the graph of the data for the given name.
     *
     *@param g - the graphics context
     *@param nameMeaning - the line which contains the meaning of the given name
     *@param nameData - the line which contains the rankings of the given name
     *@param nDecades - the number of decades that have data for the given name
     */
   public static void drawUniqueShapes(Graphics g, String nameMeaning, String nameData, int nDecades)
   {
      g.drawString(nameMeaning, 0, (HEIGHT / 2) + 1);
      Scanner lineScan = new Scanner(nameData);
      lineScan.next(); //throw away name
      String genderString = lineScan.next(); //gender
      char gender = genderString.charAt(0);
      for(int barCounter = 1; barCounter <= nDecades; barCounter++)
      {
         int xCoordinate = (barCounter - 1) * WIDTH;
         int year = STARTING_YEAR + 10 * (barCounter - 1);
         String yearString = "" + year;
         g.drawString(yearString, xCoordinate, GRAPH_HEIGHT + (2 * HEIGHT) - 8);
         int currentRank = lineScan.nextInt();
         String currentRankString = "" + currentRank;
         if (currentRank == 0)
         {
            g.drawString(currentRankString, xCoordinate, GRAPH_HEIGHT + HEIGHT);
         }
         else
         {
            if (gender == 'm')
            {
               g.setColor(Color.BLUE);
            }
            else if (gender == 'f')
            {
               g.setColor(Color.PINK);
            }
            int yCoordinate = (WIDTH / 2) + (currentRank / 2);
            int height = GRAPH_HEIGHT + HEIGHT - yCoordinate;
            g.fillRect(xCoordinate, yCoordinate, (WIDTH / 2), height);
            g.setColor(Color.BLACK);
            g.drawString(currentRankString, xCoordinate, yCoordinate);
         }
      }
   }
}

/**Test Plan:
for input file names:
input:                                             expected output:
an existing file                                   the program will read in that file
a file not in this folder                          the program will print an error message to the user and end the program

for output file names:
input:                                             expected output:
an existing file                                   the existing file will be overwritten (not really what should be done)
a file not in this folder                          a new file will be created with the processed text


for eventual user input of name: (this is really the only one needed for the final program)
input:                                             expected output:
a name in the list exactly as it appears           the meaning and statistics from the list
a name in the list with different capitalizations  the meaning and statistics from the list
a name not in the list                             an error message telling the user the name wasn't found
**/