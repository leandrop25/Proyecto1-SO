package proyecto_1;

public class Specifications {
    int writers;
    int stages;
    int animators;
    int actors;
    int plotTwists;
    int episodesBeforePlotTwists;
    int episodeProfit;
    int episodeWithPlotTwistsProfit;

    public Specifications(int writers, int stages, int animators, int actors, int plotTwists,
            int episodesBeforePlotTwists, int episodeProfit, int episodeWithPlotTwistsProfit) {
        this.writers = writers;
        this.stages = stages;
        this.animators = animators;
        this.actors = actors;
        this.episodesBeforePlotTwists = episodesBeforePlotTwists;
        this.plotTwists = plotTwists;
        this.episodeProfit = episodeProfit;
        this.episodeWithPlotTwistsProfit = episodeWithPlotTwistsProfit;
    }
}
