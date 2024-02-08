package proyecto_1;

public class DirectorCheck extends Thread {
    Studio studio;
    boolean caughtPM;

    public DirectorCheck(Studio studio) {
        this.studio = studio;
        this.caughtPM = false;
    }

    @Override
    public void run() {
        while (!isInterrupted() && !caughtPM) {
            if (studio.PMWatchingAnimes) {
                studio.caughtPM();
                caughtPM = true;
            }
        }
    }
}
