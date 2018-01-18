package us.zonix.anticheat.commands;

import us.zonix.anticheat.LordMeme;

public class MemeCommand {

    public LordMeme main = LordMeme.getInstance();

    public MemeCommand() {
        main.getFramework().registerCommands(this);
    }


}
