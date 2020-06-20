// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2018T2, Assignment 4
  * Name: Matthew Corfiatis
 * Username: CorfiaMatt
 * ID: 300447277
 */

/**
 * Guess the Animal Game.
 * The program will play a "guess the animal" game and learn from its mistakes.
 * It has a decision tree for determining the player's animal.
 * When it guesses wrong, it asks the player of another question that would
 *  help it in the future, and adds it to the decision tree. 
 * The program can display the decision tree, and save the tree to a file and load it again,
 *
 * A decision tree is a tree in which all the internal modes have a question, 
 * The answer to the the question determines which way the program will
 *  proceed down the tree.  
 * All the leaf nodes have answers (animals in this case).
 * 
 */

import ecs100.*;

import java.util.*;
import java.awt.Color;

public class AnimalGame {

    public static final double MARGIN_LEFT = 50;
    public static final double X_SPACING = 200;
    public static final double HEIGHT = 700;

    public DTNode questionsTree;    // root of the decision tree;

    private List<List<DTNode>> depthList = new ArrayList<>();

    public AnimalGame(){
        setupGUI();
        resetTree();
    }

    /**
     * Set up the interface
     */
    public void setupGUI(){
        UI.addButton("Play", this::play);
        UI.addButton("Print Tree", this::printTree);
        UI.addButton("Draw Tree", this::drawTree);
        UI.addButton("Clear", UI::clearPanes);
        UI.addButton("Reset", this::resetTree);
        UI.addButton("Quit", UI::quit);
        UI.setDivider(0.6);
    }

    /**
     * Makes an initial tree with two question nodes and three leaf nodes.
     */
    public void resetTree(){
        questionsTree = new DTNode("has whiskers",              true).addAnswer(
                "yes", new DTNode("bigger than person",     true).addAnswer(
                        "yes",  new DTNode("pattern",             true).addAnswer(
                                "plain", new DTNode("lion",         false)).addAnswer(
                                "spots", new DTNode("cheetah",      false)).addAnswer(
                                "stripes", new DTNode("tiger",      false))).addAnswer(
                        "no",   new DTNode("cat",                 false))).addAnswer(
                "no", new DTNode("Trunk size",               true).addAnswer(
                        "long",   new DTNode("elephant",           false)).addAnswer(
                        "short",  new DTNode("elephant seal",               false)).addAnswer(
                        "none",   new DTNode("snake", false)
                ));
    }
                    

    /**
     * Play the game.
     * Starts at the top (questionsTree), and works its way down the tree
     *  until it finally gets to a leaf node (with an answer in it)
     * If the current node has a question, then it asks the question in the node,
     * and depending on the answer, goes to the "yes" child or the "no" child.
     * If the current node is a leaf it calls processLeaf on the node
     */
    public void play () {
        DTNode currentNode = questionsTree;
        while(true) //Game loop
        {
            if(currentNode.isQuestion())
            {
                boolean validAnswer = false;
                while(!validAnswer) { //Repeats question until the user types a valid answer
                    UI.println("Q: " + currentNode.getText() + "?");
                    UI.println("Valid answers: ");
                    for(String ans : currentNode.getAnswers().keySet())  //Show the user what answers are valid
                        UI.println("* " + ans);
                    String answer = UI.nextLine().toLowerCase();
                    if(currentNode.getAnswers().containsKey(answer)) { //Ensure the answer is valid
                        currentNode = currentNode.getAnswers().get(answer); //Shift to the next node
                        validAnswer = true;
                    }
                }
            }
            else{ //Questions ended
                processLeaf(currentNode);
                break; //Finish
            }
        }
     }
        
    
    /**
     * Process a leaf node (a node with an answer in it)
     * Tell the player what the answer is, and ask if it is correct.
     * If it is not correct, ask for the right answer, and a property to distinguish
     *  the guess from the right answer
     * Change the leaf node into a question node asking about that fact,
     *  adding two new leaf nodes to the node, with the guess and the right
     *  answer.
    */
    public void processLeaf(DTNode leaf){    
        //CurrentNode must be a leaf node (an answer node)
        if (leaf==null || leaf.isQuestion()) { return; }
        boolean validAnswer = false;
        while(!validAnswer) { //Repeats question until user types valid input
            UI.println("The answer is: " + leaf.getText());
            UI.println("Is this correct? (Y/N)");
            switch (UI.nextLine().toLowerCase()) {
                case "y":
                case "yes":
                    validAnswer = true;
                    break;
                case "n":
                case "no":
                    UI.println("Please enter a new question for this node: ");
                    String question = UI.nextLine();

                    //Remove trailing question mark(s) from the question
                    if(question.endsWith("?"))
                        question = question.substring(0, question.indexOf("?"));

                    leaf.setQuestion(question);

                    UI.println("Enter at least two new answers and results: (Empty answer to end)");
                    int count = 0; //Count of questions for this leaf
                    while(true)
                    {
                        UI.println("Enter an answer: ");
                        String ans = UI.nextLine().toLowerCase();

                        if(ans.equals(""))
                        {
                            if(count >= 2)
                                break;
                            else
                                UI.println("You need more answers and results!");
                        }
                        else
                        {
                            UI.println("Enter a resulting animal: ");
                            String res = UI.nextLine();
                            leaf.addAnswer(ans, new DTNode(res, false)); //Add new result node with the answer as the key
                            count++;
                        }
                    }
                    UI.println("Tree updated");
                    validAnswer = true;
                    break;
            }
        }
        UI.println("Game ended.");
    }       

    /**  COMPLETION
     * Print out the contents of the decision tree in the text pane.
     * The root node should be at the top, followed by its "yes" subtree, and then
     * its "no" subtree.
     * Each node should be indented by how deep it is in the tree.
     */
    public void printTree(){
        UI.clearText();
        printSubTree(questionsTree, "");
    }

    /**
     * Recursively print a subtree, given the node at its root
     *  - print the text in the node with the given indentation
     *  - if it is a question node, then 
     *    print its two subtrees with increased indentation
    */
    public void printSubTree(DTNode node, String indent){
        UI.println(indent + node.toString()); //Prints question or decision depending on the node type
        if(node.isQuestion()) { //Recurse if there are child nodes
            for(DTNode n : node.getAnswers().values())
            printSubTree(n, indent + "  "); //Indent all further printing by 2 spaces
        }
    }

    /**  CHALLENGE
     * Draw the tree on the graphics pane as boxes, connected by lines.
     * To make the tree fit in a window, the tree should go from left to right
     * (ie, the root should be drawn at the left)
     * The lines should be drawn before the boxes that they are connecting
     */
    public void drawTree(){
        UI.clearGraphics();
        organiseDepth(questionsTree); //Get the depth structure

        for(int i = 0; i < depthList.size(); ++i) //Go from 0 depth to max depth
        {
            int childIndex = 0; //Index of the child in the current depth, used to positioning each child
            List<DTNode> nodeList = depthList.get(i);

            for(int j = 0; j < nodeList.size(); ++j) //Each node at this depth, NOT each child node
            {
                DTNode node = nodeList.get(j);
                double[] pos = calcNodePos(i, j); //Calculate position for this node but don't draw it yet

                for(DTNode childNode : node.getAnswers().values())
                {
                    double[] childPos = calcNodePos(i + 1, childIndex); //Calculate position of the child element so a line can be draw between them
                    childIndex++;
                    UI.drawLine(pos[0], pos[1], childPos[0], childPos[1]);

                    double midX = (pos[0] + childPos[0]) / 2;
                    double midY = (pos[1] + childPos[1]) / 2;

                    //Draw the answer string in the middle of the line
                    UI.setColor(Color.red.darker());
                    UI.setFontSize(17);
                    UI.drawString(childNode.parentAnswer, midX, midY);
                    UI.setFontSize(14);
                    UI.setColor(Color.black);
                }

                node.draw(pos[0], pos[1]);
            }
        }
    }

    /**
     * Organises a tree structure into a depth based list structure
     * @param node Root node to sort from
     */
    private void organiseDepth(DTNode node)
    {
        depthList = new ArrayList<>();
        convertTreeToLists(depthList, node, 0);
    }

    /**
     * Calculates the graphical position of a node given the depth and y index
     * @param depth Depth of the node in the tree
     * @param index Lateral index in the tree (If the tree is displayed with root at top branching down)
     * @return Double with x position at index 0, y position at index 1
     */
    private double[] calcNodePos(int depth, int index)
    {
        List<DTNode> nodeList = depthList.get(depth);
        double centreOffset = (HEIGHT / (2 * nodeList.size())); //Y offset to centre column
        double xPos = MARGIN_LEFT + (X_SPACING * depth);
        double yPos = (HEIGHT / nodeList.size() * index) + centreOffset; //Divide space evenly between all elements at current depth

        return new double[] {xPos, yPos};
    }

    /**
     * Converts a tree structure into a set of lists for each tree depth level
     * @param depthList List to put nodes in
     * @param node Current node
     * @param depth Current depth
     */
    private void convertTreeToLists(List<List<DTNode>> depthList, DTNode node, int depth)
    {
        //Ensure current depth has a valid list to put items in
        if(depthList.size() <= depth)
            depthList.add(new ArrayList<DTNode>());

        depthList.get(depth).add(node);

        for(DTNode child : node.getAnswers().values()) //Recurse on each of the node's children
            convertTreeToLists(depthList, child, depth + 1);
    }

    public static void main (String[] args) { 
        new AnimalGame();
    }
}
