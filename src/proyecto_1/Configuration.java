package proyecto_1;

public class Configuration {
    int secondsPerDay;
    int daysUntilDeadlineInit;
    int nWriterDevs;
    int nStageDevs;
    int nAnimatorDevs;
    int nActorDevs;
    int nPlotTwistDevs;
    int nAssemblers;
    int maxEmployees;
    int currentEmployees;

    public Configuration(int secondsPerDay, int daysUntilDeadlineInit, int nWriterDevs, int nStageDevs,
            int nAnimatorDevs,
            int nActorDevs, int nPlotTwistDevs, int nAssemblers, int carnetNumber) {
        this.secondsPerDay = secondsPerDay;
        this.daysUntilDeadlineInit = daysUntilDeadlineInit;
        this.nWriterDevs = nWriterDevs;
        this.nStageDevs = nStageDevs;
        this.nAnimatorDevs = nAnimatorDevs;
        this.nActorDevs = nActorDevs;
        this.nPlotTwistDevs = nPlotTwistDevs;
        this.nAssemblers = nAssemblers;
        this.maxEmployees = carnetNumber + 12;
        this.currentEmployees = nWriterDevs + nStageDevs + nAnimatorDevs + nActorDevs + nPlotTwistDevs + nAssemblers;
    }
}
