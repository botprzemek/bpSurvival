package pl.botprzemek.bpSurvival.SurvivalManager.Config;

import pl.botprzemek.bpSurvival.BpSurvival;
import pl.botprzemek.bpSurvival.SurvivalManager.SurvivalManager;
import pl.botprzemek.bpSurvival.SurvivalManager.Utils.Config;

import java.util.ArrayList;
import java.util.List;

public class ConfigManager {

    private final List<Config> configs = new ArrayList<>();

    private final PluginConfig pluginConfig;

    private final MessageConfig messageConfig;

    private final ProfileConfig profileConfig;

    public ConfigManager(SurvivalManager survivalManager) {

        BpSurvival instance = survivalManager.getInstance();

        configs.add(this.pluginConfig = new PluginConfig(instance, "config.yml"));

        configs.add(this.messageConfig = new MessageConfig(instance, "messages.yml"));

        configs.add(this.profileConfig = new ProfileConfig(instance, "profiles.yml"));

        loadConfigs();

    }

    public void loadConfigs() {

        for (Config config : configs) {

            config.loadConfig();

        }

    }

    public void saveConfigs() {

        profileConfig.saveConfig();

    }

    public PluginConfig getPluginConfig() {

        return pluginConfig;

    }

    public MessageConfig getMessageConfig() {

        return messageConfig;

    }

    public ProfileConfig getProfileConfig() {

        return profileConfig;

    }

}