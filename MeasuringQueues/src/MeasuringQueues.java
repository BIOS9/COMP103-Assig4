// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2018T2, Assignment 4
 * Name: Matthew Corfiatis
 * Username: CorfiaMatt
 * ID: 300447277
 */

import java.util.*;


/** 
 *  Measures the performance of different ways of doing a priority queue of Patients
 *  Uses a cutdown version of Patient that has nothing in it but a priority (so it takes
 *   minimal time to construct a new Patient).
 *  The Patient constructor doesn't need any arguments
 *  Remember that small priority values are the highest priority - 1 is higher priority than 10.
 *  Algorithms to test and measure:
 *      Use a built-in PriorityQueue
 *      Use an ArrayList, with queue's head at 0,   sorting when you add an item.
 *      Use an ArrayList, with queue's head at end, sorting when you add an item.
 *  Each method should have an items parameter, which is a collection of Patients
 *    that should be initially added to the queue (eg  new PriorityQueue<Patient>(items); or
 *    new ArrayList<Patient>(items))
 *    It should then repeatedly dequeue a patient from the queue, and enqueue a new Patient(). 
 *    It should do this 100,000 times.
 *    (the number of times can be changed using the textField)
 *  To test your methods, you should have debugging statements such as UI.println(queue)
 *   in the loop to print out the state of the queue. You could comment them out before measuring.
 */

public class MeasuringQueues{

    private static final int TIMES=1000000;  //Number of times to repeatedly dequeue and enqueue item

    /**
    * Construct a new MeasuringQueues object
    */
    public MeasuringQueues(){
       measure();
    }

    /**
     * Create a priority queue using a PriorityQueue, 
     * adding all the patients in the collection to the queue.
     * (n will be the size of the the collection in the patients parameter).
     * Then, dequeue and enqueue TIMES times.
     */
    public long useQueuesPQ(Collection<Patient> patients){
        Queue<Patient> queue = new PriorityQueue<Patient>(patients); //Ignoring initialisation in timing
        long start = System.currentTimeMillis();
        for (int i=0; i<TIMES; i++){
            queue.poll();
            queue.offer(new Patient());
        }
        return System.currentTimeMillis() - start; //Calculate time taken
    }

    /**
     * Create a queue using an ArrayList with the head at the end.
     * Make a new ArrayList using all the patients in the collection,
     * and then sorting the list.
     * Then, dequeue and enqueue TIMES times.
     * (n will be the size of the the collection in the patients parameter).
     * Note: Head of queue is at the end of the list, 
     * so we need to sort in the reverse order of Patients (so the smallest value comes at the end)
     */
    public long useQueuesALEnd(Collection<Patient> patients){
        ArrayList<Patient> queue = new ArrayList<>(patients); //Ignoring initialisation in timing
        long start = System.currentTimeMillis(); 
        Collections.sort(queue, Collections.reverseOrder()); //Initial sorting of the list, uses reverse order comparator to sort backwards
        for (int i=0; i<TIMES; i++){
            queue.remove(queue.size() - 1); //Remove element from the end of the list
            queue.add(new Patient()); //Add patient to the end of the list
            Collections.sort(queue, Collections.reverseOrder());
        }
        return System.currentTimeMillis() - start;
    }

    /**
     * Create a queue using an ArrayList with the head at the start.
     * Head of queue is at the start of the list
     * Make a new ArrayList using all the patients in the collection,
     * and then sorting the list.
     * Then, dequeue and enqueue TIMES times.
     * (n will be the size of the the collection in the patients parameter).
     */
    public long useQueuesALStart(Collection<Patient> patients){
        ArrayList<Patient> queue = new ArrayList<>(patients); //Ignoring initialisation in timing
        long start = System.currentTimeMillis();
        Collections.sort(queue); //Initial sort will be bulk of the algorithm
        for (int i=0; i<TIMES; i++){
            queue.remove(0); //Remove from start of the list
            queue.add(new Patient()); //Add patient to end of list
            Collections.sort(queue);
        }
        return System.currentTimeMillis() - start;
    }



    /**
     * For a sequence of values of n, from 1000 to 1024000,
     *  - Construct a collection of n Patients (This step shouldn't be included in the time measurement)
     *  - call each of the methods, passing the collection, and measuring
     *    how long each method takes to dequeue and enqueue a Patient 100,000 times
     */
    public void measure(){
        int num = 1000;
        while(num <= 1024000) {

            System.out.println("\n\nItem count: " + num);
            Collection<Patient> items = new ArrayList<Patient>();
            for (int i = 0; i < num; i++) {
                items.add(new Patient());
            }

            System.out.println("======== Testing useQueuesPQ ============");
            System.out.println(useQueuesPQ(items));

            System.out.println("======== Testing useQueuesALEnd ============");
            System.out.println(useQueuesALEnd(items));

            System.out.println("======== Testing useQueuesALStart ============");
            System.out.println(useQueuesALStart(items));

            num*=2;
        }
    }

    public static void main(String[] arguments){
        new MeasuringQueues();
    }
}
