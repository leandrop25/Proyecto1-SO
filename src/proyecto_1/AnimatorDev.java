package proyecto_1;

public class AnimatorDev extends Thread {
    Drive drive;
    int animatorsPerDay;
    int secondsPerDay;

    public AnimatorDev(int animatorsPerDay, Studio studio) {
        this.drive = studio.getDrive();
        this.animatorsPerDay = animatorsPerDay;
        this.secondsPerDay = studio.config.secondsPerDay;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                // Sleep while producing a narrative
                Thread.sleep(secondsPerDay * 1000);
                drive.addAnimators(animatorsPerDay);
            }
        } catch (InterruptedException e) {
            // Thread killed
        }
    }
}
