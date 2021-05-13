package group01.smartcar.client.internal.speech.commands;

import group01.smartcar.client.SmartCar;
import group01.smartcar.client.internal.speech.VoiceControlCommand;

public class ForwardCommand implements VoiceControlCommand {

    @Override
    public String getName() {
        return "forward";
    }

    @Override
    public void execute(SmartCar smartCar, String... parameters) {
        smartCar.setSpeed(50);
    }
}