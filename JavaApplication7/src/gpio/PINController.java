package gpio;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.wiringpi.SoftPwm;
import constants.Constants;

/**
 *
 * @author Onur
 */
public class PINController implements IPINController {

    //<editor-fold desc="member">
    private GpioController gpio;
    private int speed = 25;
    private String previousCommand = null;

    private GpioPinDigitalOutput pinMotorRightEnable;
    private GpioPinDigitalOutput pinMotorLeftEnable;

    //</editor-fold>
    //sag alttan saymaya başla
    //8 - 9 (gpio4 ile gpio5) sağ motor bağlantısı
    //11 (gpio6) L293d enable giris rightMotor icin
    //7 - 8 (gpio2 ile gpio3) sol motor bağlantısı
    //4 (gpio0) L293d enable giris leftMotor icin
    //<editor-fold desc="constructor">
    public PINController() {
        gpio = GpioFactory.getInstance();
        SoftPwm.softPwmCreate(Constants.pinMotorRight1, 0, 100);
        SoftPwm.softPwmCreate(Constants.pinMotorRight2, 0, 100);
        SoftPwm.softPwmCreate(Constants.pinMotorLeft1, 0, 100);
        SoftPwm.softPwmCreate(Constants.pinMotorLeft2, 0, 100);

        pinMotorRightEnable = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_06);
        pinMotorLeftEnable = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_07);

    }
    //</editor-fold>

    //<editor-fold desc="properties">
    public int getSpeed() {
        return speed;
    }

    //<editor-fold desc="methods">
    @Override
    public void right() {
        pinMotorRightEnable.setState(PinState.HIGH);
        pinMotorLeftEnable.setState(PinState.HIGH);

        SoftPwm.softPwmWrite(Constants.pinMotorRight1, 0);
        SoftPwm.softPwmWrite(Constants.pinMotorRight2, 0);

        if (getPreviousCommand().equals(Constants.COMMAND_BACK)) {
            SoftPwm.softPwmWrite(Constants.pinMotorLeft1, 0);
            SoftPwm.softPwmWrite(Constants.pinMotorLeft2, speed);
        } else {
            SoftPwm.softPwmWrite(Constants.pinMotorLeft1, speed);
            SoftPwm.softPwmWrite(Constants.pinMotorLeft2, 0);
        }
    }

    @Override
    public void left() {
        pinMotorRightEnable.setState(PinState.HIGH);
        pinMotorLeftEnable.setState(PinState.HIGH);

        if (getPreviousCommand().equals(Constants.COMMAND_BACK)) {
            SoftPwm.softPwmWrite(Constants.pinMotorRight1, 0);
            SoftPwm.softPwmWrite(Constants.pinMotorRight2, speed);
        } else {
            SoftPwm.softPwmWrite(Constants.pinMotorRight1, speed);
            SoftPwm.softPwmWrite(Constants.pinMotorRight2, 0);
        }
        SoftPwm.softPwmWrite(Constants.pinMotorLeft1, 0);
        SoftPwm.softPwmWrite(Constants.pinMotorLeft2, 0);

    }

    @Override
    public void stop() {
        setPreviousCommand(Constants.COMMAND_STOP);
        pinMotorRightEnable.setState(PinState.LOW);
        pinMotorLeftEnable.setState(PinState.LOW);

        SoftPwm.softPwmWrite(Constants.pinMotorRight1, 0);
        SoftPwm.softPwmWrite(Constants.pinMotorRight2, 0);

        SoftPwm.softPwmWrite(Constants.pinMotorLeft1, 0);
        SoftPwm.softPwmWrite(Constants.pinMotorLeft2, 0);

    }

    @Override
    public void go() {

        setPreviousCommand(Constants.COMMAND_GO);
        pinMotorRightEnable.setState(PinState.HIGH);
        pinMotorLeftEnable.setState(PinState.HIGH);

        SoftPwm.softPwmWrite(Constants.pinMotorRight1, speed);
        SoftPwm.softPwmWrite(Constants.pinMotorRight2, 0);

        SoftPwm.softPwmWrite(Constants.pinMotorLeft1, speed);
        SoftPwm.softPwmWrite(Constants.pinMotorLeft2, 0);
    }

    @Override
    public void back() {
        setPreviousCommand(Constants.COMMAND_BACK);
        pinMotorRightEnable.setState(PinState.HIGH);
        pinMotorLeftEnable.setState(PinState.HIGH);

        SoftPwm.softPwmWrite(Constants.pinMotorRight1, 0);
        SoftPwm.softPwmWrite(Constants.pinMotorRight2, speed);

        SoftPwm.softPwmWrite(Constants.pinMotorLeft1, 0);
        SoftPwm.softPwmWrite(Constants.pinMotorLeft2, speed);
    }
    //</editor-fold>

    @Override
    public void setSpeed(int speed) {
        this.speed = speed;
        if (previousCommand.equals(Constants.COMMAND_GO)) {
            go();
        } else if (previousCommand.equals(Constants.COMMAND_BACK)) {
            back();
        } else if (previousCommand.equals(Constants.COMMAND_LEFT)) {
            left();
        } else if (previousCommand.equals(Constants.COMMAND_RIGHT)) {
            right();
        } else {
            stop();
        }
    }

    public String getPreviousCommand() {
        return previousCommand;
    }

    public void setPreviousCommand(String previousCommand) {
        this.previousCommand = previousCommand;
    }

}
