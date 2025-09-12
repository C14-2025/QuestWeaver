package br.dev.projetoc14.quest;

/*

    Missão de matar uma certa quantidade de um certo mob

 */
public class KillQuest extends Quest{
    private String targetMob;
    private int targetCount;
    private int currentCount;

    public KillQuest(String id, String name, String description, int experienceReward, String targetMob, int targetCount, int currentCount) {
        super(id, name, description, experienceReward);
        this.targetMob = targetMob;
        this.targetCount = targetCount;
        this.currentCount = currentCount;
    }

    @Override
    public boolean checkCompletion() {
        /*
        Condição de conclusão para essa missão é matar o número pedido de
        mobs, então se a contagem atual bater o quanto é definido
        */
        return currentCount >= targetCount;
    }

    @Override
    public void updateProgress(Object... params) {
        /*

         */
        if (params[0] instanceof String && params[0].equals(targetMob)) {
            currentCount++;
            completed = checkCompletion();
        }
    }

}
