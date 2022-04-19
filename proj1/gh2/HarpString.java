package gh2;

import deque.ArrayDeque;
import deque.Deque;

import deque.LinkedListDeque;
import edu.princeton.cs.algs4.StdAudio;

//Note: This file will not compile until you complete the Deque implementations
public class HarpString {
    /** Constants. Do not change. In case you're curious, the keyword final
     * means the values cannot be changed at runtime. We'll discuss this and
     * other topics in lecture on Friday. */
    private static final int SR = 44100;      // Sampling Rate
    private static final double DECAY = .996; // energy decay factor

    /* Buffer for storing sound data. */
    private Deque<Double> buffer;

    /* Create a guitar string of the given frequency.  */
    public HarpString(double frequency) {
        int capacity = 2 * (int) Math.round(SR / frequency);
        buffer = new LinkedListDeque<>();
        for (int i = 0; i < capacity; i++) {
            buffer.addLast(0.0);
        }
    }


    /* Pluck the guitar string by replacing the buffer with white noise. */
    public void pluck() {
        int stringSize = buffer.size();
        while (buffer.size() > 0) {
            buffer.removeFirst();
        }
        for (int i = 0; i < stringSize; i++) {
            double r = Math.random() - 0.5;
            buffer.addLast(r);
        }
    }

    /* Advance the simulation one time step by performing one iteration of
     * the Karplus-Strong algorithm.
     */
    public void tic() {
        double dequeueSample = buffer.removeFirst();
        double enqueueSample = -0.5 * DECAY * (dequeueSample + buffer.get(0));
        buffer.addLast(enqueueSample);
    }

    /* Return the double at the front of the buffer. */
    public double sample() {
        return buffer.get(0);
    }

    public static void main(String[] args) {
    }
}
