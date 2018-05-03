package com.snobot.simulator.jni;

import org.junit.Assert;
import org.junit.Test;

import com.snobot.simulator.SensorActuatorRegistry;
import com.snobot.test.utilities.BaseSimulatorJavaTest;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;

public class TestRegisterJavaCallback extends BaseSimulatorJavaTest
{
    private void checkSizes(int aAnalogSize, int aDigitalSize, int aEncoderSize, int aPcmSize, int aPdpSize, int aNumPwm)
    {
        Assert.assertEquals(aAnalogSize, SensorActuatorRegistry.get().getAnalog().size());
        Assert.assertEquals(aDigitalSize, SensorActuatorRegistry.get().getDigitalSources().size());
        Assert.assertEquals(aEncoderSize, SensorActuatorRegistry.get().getEncoders().size());
        Assert.assertEquals(aPcmSize, SensorActuatorRegistry.get().getSolenoids().size());
        Assert.assertEquals(aNumPwm, SensorActuatorRegistry.get().getSpeedControllers().size());
    }

    @Test
    public void testCallback()
    {
        RobotBase.initializeHardwareConfiguration();
        RegisterCallbacksJni.registerAllCallbacks();

        checkSizes(0, 0, 0, 0, 0, 0);

        SpeedController sc0 = new Talon(0);
        SpeedController sc1 = new Talon(1);
        SpeedController sc2 = new Talon(2);
        SpeedController sc3 = new Talon(3);
        checkSizes(0, 0, 0, 0, 0, 4);

        new AnalogInput(0);
        checkSizes(1, 0, 0, 0, 0, 4);

        new DigitalInput(0);
        checkSizes(1, 1, 0, 0, 0, 4);

        new Encoder(1, 2);
        checkSizes(1, 3, 1, 0, 0, 4);

        new Solenoid(0);
        checkSizes(1, 3, 1, 1, 0, 4);

    }

    // @Test
    // public void testUnsupportedOptions()
    // {
    // String doesntExistString = "DoesntExist";
    // AnalogCallbackJni.analogCallback(doesntExistString, 0, null);
    // AnalogGyroCallbackJni.analogGyroCallback(doesntExistString, 0, null);
    // DigitalCallbackJni.digitalCallback(doesntExistString, 0, null);
    // EncoderCallbackJni.encoderCallback(doesntExistString, 0, null);
    // I2CCallbackJni.i2cCallback(doesntExistString, 0, (HalCallbackValue)
    // null);
    // I2CCallbackJni.i2cCallback(doesntExistString, 0, ByteBuffer.allocate(5));
    // PcmCallbackJni.pcmCallback(doesntExistString, 0, null);
    // PdpCallbackJni.pdpCallback(doesntExistString, 0, null);
    // PwmCallbackJni.pwmCallback(doesntExistString, 0, null);
    // RelayCallbackJni.relayCallback(doesntExistString, 0, null);
    // SpiCallbackJni.spiCallback(doesntExistString, 0, (HalCallbackValue)
    // null);
    // SpiCallbackJni.spiCallback(doesntExistString, 0, ByteBuffer.allocate(5));
    // }
}
