package proyecto_1;

public class PlotTwistDev extends Thread {
    Drive drive;
    int daysPerPlotTwist;
    int secondsPerDay;

    public PlotTwistDev(int daysPerPlotTwist, Studio studio) {
        this.drive = studio.getDrive();
        this.daysPerPlotTwist = daysPerPlotTwist;
        this.secondsPerDay = studio.config.secondsPerDay;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                // Sleep while producing a narrative
                Thread.sleep(secondsPerDay * 1000 * this.daysPerPlotTwist);
                drive.addPlotTwists();
            }
        } catch (InterruptedException e) {
            // Thread killed
        }
    }
}
