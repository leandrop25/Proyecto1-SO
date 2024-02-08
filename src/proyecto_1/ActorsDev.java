package proyecto_1;

public class ActorsDev extends Thread {
    Drive drive;
    int actorsPerDay;
    int secondsPerDay;

    public ActorsDev(int actorsPerDay, Studio studio) {
        this.drive = studio.getDrive();
        this.actorsPerDay = actorsPerDay;
        this.secondsPerDay = studio.config.secondsPerDay;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                // Sleep while producing a narrative
                Thread.sleep(secondsPerDay * 1000);
                drive.addActors(actorsPerDay);
            }
        } catch (InterruptedException e) {
            // Thread killed
        }
    }
}