package proyecto_1;

public class Writer extends Thread {
    Drive drive;
    int daysPerWriter;
    int secondsPerDay;

    public Writer(int daysPerWriter, Studio studio) {
        this.drive = studio.getDrive();
        this.daysPerWriter = daysPerWriter;
        this.secondsPerDay = studio.config.secondsPerDay;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                // Sleep while producing a Writer
                Thread.sleep(secondsPerDay * 1000 * this.daysPerWriter);
                drive.addWriters();
            }
        } catch (InterruptedException e) {
            // Thread killed
        }
    }
}
