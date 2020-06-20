// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2018T2, Assignment 4
 * Name: Matthew Corfiatis
 * Username: CorfiaMatt
 * ID: 300447277
 */

import ecs100.*;
import java.awt.Color;
import java.util.*;
import java.io.*;


/**
 * A Decision Tree is a structure for getting to an answer by following
 * through a tree of yes/no questions.
 * Each internal node has a question and two children.
 * - the Yes child is the decision tree to follow if the answer to the question is yes
 * - the No child is the decision tree to follow if the answer to the question is no
 * Leaves of the tree do not have a question, but have an Answer.
 *  They should not have any children.
 * A leaf node can be turned into an internal node by giving it a question and two children.
*/

public class DTNode{


    private boolean isQuestionNode;
    private Map<String, DTNode> answers = new HashMap<>();
    private String text;
    public String parentAnswer = ""; //Answer of the parent node, used when drawing the tree
    


    /**
     * Construct a new leaf node with an answer.
     */
    public DTNode(String ans, boolean isQuestionNode){
        this.isQuestionNode = isQuestionNode;
        text = ans;
    }


    /** Getters */
    public boolean isQuestion(){return isQuestionNode;}
    public String getText(){return text;}
    public Map<String, DTNode> getAnswers(){return Collections.unmodifiableMap(answers);}

    /**
     * Change a leaf node into an internal question node
     * by providing a question and two subtrees
     */
    public void setQuestion(String question){
        if(isQuestionNode){
            throw new RuntimeException("Node is already a question node");
        }
        isQuestionNode = true;
        text = question;
    }

    /**
     * Add an answer and a resulting value as a child
     * @param answer Key in the map
     * @param node Value in the map
     * @return
     */
    public DTNode addAnswer(String answer, DTNode node)
    {
        if(!isQuestionNode){
            throw new RuntimeException("Node is not a question node");
        }
        answers.put(answer, node); //Add the answer and result
        node.parentAnswer = answer; //Set the answer on the child
        return this;
    }

    /** Return a String describing the node */
    public String toString(){
        if (isQuestionNode){
            return ("Question: "+text);
        }
        else {
            return ("Decision: "+text);
        }
    }

    public static final int WIDTH = 80;
    public static final int HEIGHT = 15;

    /**
     * Draw the node (as a box with the text) on the graphics pane
     * (x,y) is the center of the box
     * The box should be WIDTH wide, and HEIGHT high, and
     * the text should be truncated to fit.
     */
    public void draw(double x, double y){
        double left = x-WIDTH/2;
        String toDraw = text.substring(0, Math.min(text.length(),15));
        
        UI.eraseRect(left, y-HEIGHT/2, WIDTH, HEIGHT);  // to clear anything behind it
        UI.drawRect(left, y-HEIGHT/2, WIDTH, HEIGHT);
        UI.drawString(toDraw, left+2, y+6);             // assume height of characters = 12
    }
}
