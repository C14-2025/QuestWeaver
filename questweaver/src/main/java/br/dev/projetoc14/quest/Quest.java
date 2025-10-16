package br.dev.projetoc14.quest;

public abstract class Quest {
    protected String id;
    protected String name;
    protected String description;
    protected int experienceReward;
    protected boolean completed;

    public Quest(String id, String name, String description, int experienceReward) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.experienceReward = experienceReward;
        this.completed = false;//A quest, obrigatoriamente, começa não completada
    }

    //Para checar se a quest foi completada
    public abstract boolean checkCompletion();

    /*
    Para checar o progresso da missão.
    Como cada missão pode ter diferentes parâmetros dependendo de o que ela pedir,
    "Object... params" define um número "n" de parâmetros de um tipo genérico,
    então na hora de verificar, basta saber a ordem que os parâmetros estão sendo
    passados e iterar sobre ele como se fosse um vetor

    IMPORTANTE:
    - Como é um tipo genérico e a quantidade é variável, é importante sempre usar
    "instaceof" e iterar dentro dele para saber o tamanho dele
     */
    public abstract void updateProgress(Object... params);

    //Getters e Setters criados serão criados conforme necessidade


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isCompleted() {
        return completed;
    }

    public String getDescription() {
        return description;
    }

    public int getExperienceReward() {
        return experienceReward;
    }
}