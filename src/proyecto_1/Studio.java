package proyecto_1;

import org.jfree.data.xy.XYSeries;

public class Studio extends Thread {
    int id;
    Drive drive;

    LinkedList<Writer> writersDevs = new LinkedList<>();;
    LinkedList<StageDev> stagesDevs = new LinkedList<>();;
    LinkedList<AnimatorDev> animatorsDevs = new LinkedList<>();;
    LinkedList<ActorsDev> actorsDevs = new LinkedList<>();;
    LinkedList<PlotTwistDev> plotTwistDevs = new LinkedList<>();;
    LinkedList<Assemblers> Assemblers = new LinkedList<>();
    ProjectManager PM;
    Director director;

    Configuration config;
    Proyecto1GUI GUI;

    int daysForWriter, daysForStage, animatorsPerDay, actorsPerDay, daysPerPlotTwist;
    int rawProfits, operativeCosts, utility, deductedFromPM;
    int pmFaults;
    int daysPassed = 0;

    int currentDaysUntilDeadline;

    boolean isRunning;
    boolean PMWatchingAnimes;

    XYSeries series;

    public Studio(int id, String name, int carnetNumber, Specifications specs, Configuration config, Proyecto1GUI GUI) {
        this.id = id;
        this.drive = new Drive(id, specs, GUI);
        this.config = config;
        this.GUI = GUI;

        // Set daysForWriter, daysPerstage and animatorsPerDay
        if (carnetNumber >= 0 && carnetNumber < 3) {
            daysForWriter = 2;
            daysForStage = 2;
            animatorsPerDay = 3;
        } else if (carnetNumber >= 3 && carnetNumber < 6) {
            daysForWriter = 3;
            daysForStage = 3;
            animatorsPerDay = 2;
        } else if (carnetNumber >= 6 && carnetNumber <= 9) {
            daysForWriter = 4;
            daysForStage = 4;
            animatorsPerDay = 1;
        }

        // Set ActorsPerDay and daysPerPlotTwist
        if (carnetNumber >= 0 && carnetNumber < 5) {
            actorsPerDay = 3;
            daysPerPlotTwist = 3;
        } else if (carnetNumber >= 5 && carnetNumber <= 9) {
            actorsPerDay = 5;
            daysPerPlotTwist = 2;
        }

        rawProfits = 0;
        operativeCosts = 0;
        utility = 0;

        currentDaysUntilDeadline = config.daysUntilDeadlineInit;

        series = new XYSeries(name);
        series.add(0, 0);
    }

    public boolean simulationRunning() {
        return isRunning;
    }

    public Drive getDrive() {
        return drive;
    }

    public boolean deadlineZero() {
        return currentDaysUntilDeadline == 0;
    }

    public int calculateDayCosts() {
        return config.nWriterDevs * 20 * 24 + config.nStageDevs * 26 * 24 + config.nAnimatorDevs * 40 * 24
                + config.nActorDevs * 16 * 24 + config.nPlotTwistDevs * 34 * 24 + config.nAssemblers * 50 * 24 +
                40 * 24 + 60 * 24;
    }

    public void changeDeadline(String action) {
        if (action.equals("reduce")) {
            if (currentDaysUntilDeadline > 0) {
                currentDaysUntilDeadline--;
                daysPassed++;
                operativeCosts += calculateDayCosts();
                GUI.modCosts(id, operativeCosts);
                utility = rawProfits - operativeCosts;
                GUI.modUtilities(id, utility);
                series.add(daysPassed, utility);
            }
        } else if (action.equals("reset")) {
            currentDaysUntilDeadline = config.daysUntilDeadlineInit;
        }
        GUI.modDeadline(id, currentDaysUntilDeadline);
    }

    public void changePMActvity(boolean isWatchingAnimes) {
        PMWatchingAnimes = isWatchingAnimes;
        if (isWatchingAnimes) {
            GUI.modPmActivity(id, "viendo anime");
        } else {
            GUI.modPmActivity(id, "trabajando");
        }
    }

    public void caughtPM() {
        pmFaults++;
        deductedFromPM += 100;
        operativeCosts -= 100;
        GUI.modCosts(id, operativeCosts);
        GUI.modPmFaults(id, pmFaults, deductedFromPM);
    }

    public void deliverEpisodes() {
        rawProfits += drive.getEpisodes();
        GUI.modProfits(id, rawProfits);
    }

    public void changeDirectorActivity(String activity) {
        GUI.modDirectorActivity(id, activity);
    }

    public void addWriterDev() {
        if (isRunning && config.currentEmployees + 1 <= config.maxEmployees) {
            Writer newDev = new Writer(daysForWriter, this);
            writersDevs.append(newDev);
            newDev.start();
            config.nWriterDevs++;
            config.currentEmployees++;
            GUI.modWritersDev(id, config.nWriterDevs);
        }
    }

    public void removeWriterDev() {
        if (isRunning && config.nWriterDevs != 1) {
            Writer dev = writersDevs.pop();
            dev.interrupt();
            config.nWriterDevs--;
            config.currentEmployees--;
            GUI.modWritersDev(id, config.nWriterDevs);
        }
    }

    public void addStageDev() {
        if (isRunning && config.currentEmployees + 1 <= config.maxEmployees) {
            StageDev newDev = new StageDev(daysForStage, this);
            stagesDevs.append(newDev);
            newDev.start();
            config.nStageDevs++;
            config.currentEmployees++;
            GUI.modStageDev(id, config.nStageDevs);
        }
    }

    public void removeStageDev() {
        if (isRunning && config.nStageDevs != 1) {
            StageDev dev = stagesDevs.pop();
            dev.interrupt();
            config.nStageDevs--;
            config.currentEmployees--;
            GUI.modStageDev(id, config.nStageDevs);
        }
    }

    public void addAnimatorsDev() {
        if (isRunning && config.currentEmployees + 1 <= config.maxEmployees) {
            AnimatorDev newDev = new AnimatorDev(animatorsPerDay, this);
            animatorsDevs.append(newDev);
            newDev.start();
            config.nAnimatorDevs++;
            config.currentEmployees++;
            GUI.modAnimatorsDev(id, config.nAnimatorDevs);
        }
    }

    public void removeAnimatorsDev() {
        if (isRunning && config.nAnimatorDevs != 1) {
            AnimatorDev dev = animatorsDevs.pop();
            dev.interrupt();
            config.nAnimatorDevs--;
            config.currentEmployees--;
            GUI.modAnimatorsDev(id, config.nAnimatorDevs);
        }
    }

    public void addActorDev() {
        if (isRunning && config.currentEmployees + 1 <= config.maxEmployees) {
            ActorsDev newDev = new ActorsDev(actorsPerDay, this);
            actorsDevs.append(newDev);
            newDev.start();
            config.nActorDevs++;
            config.currentEmployees++;
            GUI.modActorsDev(id, config.nActorDevs);
        }
    }

    public void removeActorDev() {
        if (isRunning && config.nActorDevs != 1) {
            ActorsDev dev = actorsDevs.pop();
            dev.interrupt();
            config.nActorDevs--;
            config.currentEmployees--;
            GUI.modActorsDev(id, config.nActorDevs);
        }
    }

    public void addPlotTwistDev() {
        if (isRunning && config.currentEmployees + 1 <= config.maxEmployees) {
            PlotTwistDev newDev = new PlotTwistDev(daysPerPlotTwist, this);
            plotTwistDevs.append(newDev);
            newDev.start();
            config.nPlotTwistDevs++;
            config.currentEmployees++;
            GUI.modPlotTwistDev(id, config.nPlotTwistDevs);
        }
    }

    public void removePlotTwistDev() {
        if (isRunning && config.nPlotTwistDevs != 1) {
            PlotTwistDev dev = plotTwistDevs.pop();
            dev.interrupt();
            config.nPlotTwistDevs--;
            config.currentEmployees--;
            GUI.modPlotTwistDev(id, config.nPlotTwistDevs);
        }
    }

    public void addAssembler() {
        if (isRunning && config.currentEmployees + 1 <= config.maxEmployees) {
            Assemblers newAssembler = new Assemblers(this);
            Assemblers.append(newAssembler);
            newAssembler.start();
            config.nAssemblers++;
            config.currentEmployees++;
            GUI.modAssemblers(id, config.nAssemblers);
        }
    }

    public void removeAssembler() {
        if (isRunning && config.nAssemblers != 1) {
            Assemblers Assembler = Assemblers.pop();
            Assembler.interrupt();
            config.nAssemblers--;
            config.currentEmployees--;
            GUI.modAssemblers(id, config.nAssemblers);
        }
    }

    @Override
    public void run() {
        isRunning = true;

        for (int i = 0; i < config.nWriterDevs; i++)
            writersDevs.append(new Writer(daysForWriter, this));

        for (int i = 0; i < config.nStageDevs; i++)
            stagesDevs.append(new StageDev(daysForStage, this));

        for (int i = 0; i < config.nAnimatorDevs; i++)
            animatorsDevs.append(new AnimatorDev(animatorsPerDay, this));

        for (int i = 0; i < config.nActorDevs; i++)
            actorsDevs.append(new ActorsDev(actorsPerDay, this));

        for (int i = 0; i < config.nPlotTwistDevs; i++)
            plotTwistDevs.append(new PlotTwistDev(daysPerPlotTwist, this));

        for (int i = 0; i < config.nAssemblers; i++)
            Assemblers.append(new Assemblers(this));

        PM = new ProjectManager(this);
        director = new Director(this);

        for (int i = 0; i < config.nWriterDevs; i++)
            writersDevs.get(i).start();

        for (int i = 0; i < config.nStageDevs; i++)
            stagesDevs.get(i).start();

        for (int i = 0; i < config.nAnimatorDevs; i++)
            animatorsDevs.get(i).start();

        for (int i = 0; i < config.nActorDevs; i++)
            actorsDevs.get(i).start();

        for (int i = 0; i < config.nPlotTwistDevs; i++)
            plotTwistDevs.get(i).start();

        for (int i = 0; i < config.nAssemblers; i++)
            Assemblers.get(i).start();

        PM.start();
        director.start();

        GUI.initEmployeesPanel();
    }
}
