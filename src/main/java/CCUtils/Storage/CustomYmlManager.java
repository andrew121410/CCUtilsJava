package CCUtils.Storage;

import cc.summermc.bukkitYaml.file.FileConfiguration;
import cc.summermc.bukkitYaml.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class CustomYmlManager {

    // Files & File Configs Here.
    private FileConfiguration configcfg;
    private File configfile;
    private File dir;

    //Strings
    private String nameoffile;
    //...

    // --------------------------------------------------------------------------------------------------------
    public void setup(String nameoffile, File dir) {
        this.nameoffile = nameoffile;
        this.dir = dir;

        if (!this.dir.exists()) {
            this.dir.mkdir();
        }

        configfile = new File(dir, this.nameoffile);

        if (!configfile.exists()) {
            try {
                configfile.createNewFile();
                System.out.println("The " + this.nameoffile + " has been created.");
            } catch (IOException e) {
                System.out.println("The " + this.nameoffile + " could not make for some reason.");
            }
        }

        configcfg = YamlConfiguration.loadConfiguration(configfile);
    }

    public FileConfiguration getConfig() {
        return configcfg;
    }

    public void saveConfig() {
        try {
            configcfg.save(configfile);
            System.out.println("The " + this.nameoffile + " has been saved.");
        } catch (IOException e) {
            System.out.println("The " + this.nameoffile + " has been NOT SAVED!!!");
        }
    }

    public void saveConfigSilent() {
        try {
            configcfg.save(configfile);
        } catch (IOException e) {
            System.out.println("The " + this.nameoffile + " has been NOT SAVED!!!");
        }
    }

    public void reloadConfig() {
        configcfg = YamlConfiguration.loadConfiguration(configfile);
        System.out.println("The " + this.nameoffile + " has been reloaded.");
    }
}