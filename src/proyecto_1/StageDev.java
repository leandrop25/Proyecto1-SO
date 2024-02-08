package proyecto_1;

public class StageDev extends Thread {
    Drive drive;
    int daysPerStage;
    int secondsPerDay;

    public StageDev(int daysPerStage, Studio studio) {
        this.drive = studio.getDrive();
        this.daysPerStage = daysPerStage;
        this.secondsPerDay = studio.config.secondsPerDay;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                // Sleep while producing a narrative
                Thread.sleep(secondsPerDay * 1000 * this.daysPerStage);
                drive.addStages();
            }
        } catch (InterruptedException e) {
            // Thread killed
        }
    }
}