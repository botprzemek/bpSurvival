package pl.botprzemek.bpSurvival.SurvivalManager.Profile;

public class Profile {

    private int level;

    private int exp;

    private Settings settings;

    public Profile(int level, int exp, Settings settings) {

        this.level = level;

        this.exp = exp;

        this.settings = settings;

    }

    public void setSettings(Settings settings) {

        this.settings = settings;

    }

    public Settings getSettings() {

        return settings;

    }

    public void setLevel(int level) {

        this.level = level;

    }

    public int getLevel() {

        return level;

    }

    public int getExp() {

        return exp;

    }

    public void setExp(int exp) {

        this.exp = exp;

    }

}