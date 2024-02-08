package proyecto_1;

public class Assemblers extends Thread {
    Drive drive;
    int secondsPerDay;

    public Assemblers(Studio studio) {
        this.drive = studio.getDrive();
        this.secondsPerDay = studio.config.secondsPerDay;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                drive.getResources();
                // Sleep while making a game
                Thread.sleep(secondsPerDay * 1000);
                drive.addEpisode();
            }
        } catch (InterruptedException e) {
            // Thread killed
        }
    }
}
