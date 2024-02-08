package proyecto_1;

import java.util.concurrent.Semaphore;

public class Drive {
    int id;
    Specifications specs;
    Proyecto1GUI GUI;

    Semaphore semDrive = new Semaphore(1);

    Semaphore semCurrentWriters = new Semaphore(0);
    Semaphore semCurrentStages = new Semaphore(0);
    Semaphore semCurrentAnimators = new Semaphore(0);
    Semaphore semCurrentActors = new Semaphore(0);
    Semaphore semCurrentPlotTwists = new Semaphore(0);

    Semaphore semMaxWriters = new Semaphore(25);
    Semaphore semMaxStages = new Semaphore(20);
    Semaphore semMaxAnimators = new Semaphore(55);
    Semaphore semMaxActors = new Semaphore(35);
    Semaphore semMaxPlotTwists = new Semaphore(10);

    int writers, stages, animators, actors, plotTwists;
    int episodes, episodesWithPlotTwist, currentEpisodesBeforePlotTwist;

    public Drive(int id, Specifications specs, Proyecto1GUI GUI) {
        this.id = id;
        this.specs = specs;
        this.GUI = GUI;
        this.writers = 0;
        this.stages = 0;
        this.animators = 0;
        this.actors = 0;
        this.plotTwists = 0;
        this.episodes = 0;
        this.episodesWithPlotTwist = 0;
        this.currentEpisodesBeforePlotTwist = 0;
    }

    public void addWriters() {
        try {
            semMaxWriters.acquire();
            semDrive.acquire();
            writers++;
            GUI.modWriterAmount(id, writers);
            semDrive.release();
            semCurrentWriters.release();
        } catch (InterruptedException e) {
            System.out.println("InterruptedException caught");
        }
    }

    public void addStages() {
        try {
            semMaxStages.acquire();
            semDrive.acquire();
            stages++;
            GUI.modStagesAmount(id, stages);
            semDrive.release();
            semCurrentStages.release();
        } catch (InterruptedException e) {
            System.out.println("InterruptedException caught");
        }
    }

    public void addAnimators(int animatorsPerDay) {
        try {
            semMaxAnimators.acquire(animatorsPerDay);
            semDrive.acquire();
            animators += animatorsPerDay;
            GUI.modAnimatorsAmount(id, animators);
            semDrive.release();
            semCurrentAnimators.release(animatorsPerDay);
        } catch (InterruptedException e) {
            System.out.println("InterruptedException caught");
        }
    }

    public void addActors(int actorsPerDay) {
        try {
            semMaxActors.acquire(actorsPerDay);
            semDrive.acquire();
            actors += actorsPerDay;
            GUI.modActorsAmount(id, actors);
            semDrive.release();
            semCurrentActors.release(actorsPerDay);
        } catch (InterruptedException e) {
            System.out.println("InterruptedException caught");
        }
    }

    public void addPlotTwists() {
        try {
            semMaxPlotTwists.acquire();
            semDrive.acquire();
            plotTwists++;
            GUI.modPlotTwistAmount(id, plotTwists);
            semDrive.release();
            semCurrentPlotTwists.release();
        } catch (InterruptedException e) {
            System.out.println("InterruptedException caught");
        }
    }

    public void getResources() {
        try {
            semCurrentWriters.acquire(specs.writers);
            semCurrentStages.acquire(specs.stages);
            semCurrentAnimators.acquire(specs.animators);
            semCurrentActors.acquire(specs.actors);
            semDrive.acquire();
            writers -= specs.writers;
            stages -= specs.stages;
            animators -= specs.animators;
            actors -= specs.actors;
            GUI.modWriterAmount(id, writers);
            GUI.modStagesAmount(id, stages);
            GUI.modAnimatorsAmount(id, animators);
            GUI.modActorsAmount(id, actors);
            semDrive.release();

            semMaxWriters.release(specs.writers);
            semMaxStages.release(specs.stages);
            semMaxAnimators.release(specs.animators);
            semMaxActors.release(specs.actors);
        } catch (InterruptedException e) {
            System.out.println("InterruptedException caught");
        }
    }

    public void addEpisode() {
        try {
            semDrive.acquire();
            if (currentEpisodesBeforePlotTwist != 0
                    && currentEpisodesBeforePlotTwist % specs.episodesBeforePlotTwists == 0) {
                // Temporaly release the drive semaphore to wait for the plotTwists
                semDrive.release();
                semCurrentPlotTwists.acquire(specs.plotTwists);

                // Adquire the drive lock again before modifying the values
                semDrive.acquire();
                episodesWithPlotTwist++;
                plotTwists -= specs.plotTwists;
                currentEpisodesBeforePlotTwist = 0;

                semMaxPlotTwists.release(specs.plotTwists);

                GUI.modEpisodesPlotTwistAmount(id, episodesWithPlotTwist);
                GUI.modPlotTwistAmount(id, plotTwists);
            } else {
                episodes++;
                currentEpisodesBeforePlotTwist++;
                GUI.modEpisodesAmount(id, episodes);
            }

            semDrive.release();
        } catch (InterruptedException e) {
            System.out.println("InterruptedException caught");
        }
    }

    public int getEpisodes() {
        int localEpisodes = 0, localEpisodesWithPlotTwist = 0;

        try {
            semDrive.acquire();
            localEpisodes = episodes;
            localEpisodesWithPlotTwist = episodesWithPlotTwist;
            episodes = 0;
            episodesWithPlotTwist = 0;
            GUI.modEpisodesAmount(id, 0);
            GUI.modEpisodesPlotTwistAmount(id, 0);
            semDrive.release();
        } catch (InterruptedException e) {
            System.out.println("InterruptedException caught");
        }

        return localEpisodes * specs.episodeProfit + localEpisodesWithPlotTwist * specs.episodeWithPlotTwistsProfit;
    }
}
