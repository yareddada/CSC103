import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class Runway {
    /**
     * Active planes
     */
    private Queue<Plane> takeoff = new LinkedBlockingQueue<Plane>();
    private Queue<Plane> landing = new LinkedBlockingQueue<Plane>();
    private Deque<Plane> crashes = new ArrayDeque<Plane>();
    Plane runway = null;
    /**
     * Simulation properties
     */
    private final int takeoffTime;
    private final int landingTime;
    private final int averageTakeoffArrival;
    private final int averageLandingArrival;
    private final int maximumLandingTime;
    private final BooleanSource takeoffProbability;
    private final BooleanSource landingProbability;
    /**
     * Simulation statistics
     */
    private int totalCrashed = 0;
    private int totalTakeoff = 0;
    private int totalLanded = 0;
    private Averager averageTakeoff = new Averager();
    private Averager averageLanding = new Averager();

    /**
     * Constructor.
     * 
     * @param takeoffTime Time for a plane to take off.
     * @param landingTime Time for a plane to land.
     * @param averageTakeoffArrival Probility for a plane to arrive for take
     *        off.
     * @param averageLandingArrival Probility for a plane to arrive for landing.
     * @param maximumLandingTime Maximum time a plane to land before it crashes.
     */
    public Runway(int takeoffTime, int landingTime, int averageTakeoffArrival, int averageLandingArrival, int maximumLandingTime) {
        this.takeoffTime = takeoffTime;
        this.landingTime = landingTime;
        this.averageTakeoffArrival = averageTakeoffArrival;
        this.averageLandingArrival = averageLandingArrival;
        this.maximumLandingTime = maximumLandingTime;
        this.takeoffProbability = new BooleanSource(1d / averageTakeoffArrival);
        this.landingProbability = new BooleanSource(1d / averageLandingArrival);
    }

    /**
     * Populates the landing and take off queues.
     * 
     * @param currentTime - The current time at which this is happening. This is
     *        used to date the planes arrival to the runway.
     * @postcondition The takeoff and arival queue both have a possibility for
     *                another plane to have been added.
     */
    public void populate(int currentTime) {
        /**
         * This block calculates if a plane is taking off or not. If a plane
         * is taking off, it is added to the takeoff queue.
         */
        if(takeoffProbability.query()) {
            Plane takeoffPlane = new Plane(Plane.Operation.TAKEING_OFF, currentTime);
            takeoff.add(takeoffPlane);
            System.out.println("Arrived for take off: " + takeoffPlane);
        } else {
            System.out.println("Arrived for take off: ");
        }
        /**
         * This block calculates if a plane is landing or not. If a plane is
         * landing, it is added to the landing queue.
         */
        if(landingProbability.query()) {
            Plane landingPlane = new Plane(Plane.Operation.LANDING, currentTime);
            landing.add(landingPlane);
            System.out.println("Arrived for landing : " + landingPlane);
        } else {
            System.out.println("Arrived for landing : ");
        }
    }

    /**
     * This processes what is currently on the runway.
     * 
     * @param currentTime - The current time at which this is happening. This is
     *        used to date the planes arrival to the runway.
     */
    public void process(int currentTime) {

    }

    /**
     * This outputs the crash and runtime statistics.
     */
    public void output() {
        System.out.println();
        System.out.println("Plane Crashes:");
        while(crashes.peek() != null) {
            Plane crashedPlane = crashes.pop();
            System.out.println(crashedPlane + " crashed " + (crashedPlane.getArrivalTime() + maximumLandingTime + 1) + " minutes in.");
        }
        System.out.println();
        System.out.println("# of planes that crashed : " + totalCrashed);
        System.out.println("# of planes that came to the runway for take off : " + totalTakeoff);
        System.out.println("# of planes that came to the runway for landing  : " + totalLanded);
        System.out.println("The average time a plane spent on the take off queue is : " + averageTakeoff.average());
        System.out.println("The average time a plane spent on the landing queue is  : " + averageLanding.average());
    }
}
