package com.snobot.simulator.jni.module_wrapper;



import org.junit.Assert;
import org.junit.Test;

import com.snobot.simulator.motor_sim.SimpleMotorSimulationConfig;
import com.snobot.simulator.wrapper_accessors.DataAccessorFactory;
import com.snobot.test.utilities.BaseSimulatorTest;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;

public class TestEncoderJni extends BaseSimulatorTest
{
    @Test
    public void testCreateEncoder()
    {
        Assert.assertEquals(0, EncoderWrapperJni.getPortList().length);

        new Encoder(0, 1);
        Assert.assertEquals(1, EncoderWrapperJni.getPortList().length);
        Assert.assertEquals("Encoder (0, 0)", EncoderWrapperJni.getName(0));

        new Encoder(2, 3);
        Assert.assertEquals(2, EncoderWrapperJni.getPortList().length);
        // Assert.assertEquals("Encoder (0, 0)", EncoderWrapperJni.getName(1));
    }

    @Test(expected = RuntimeException.class)
    public void testReusePort()
    {
        Assert.assertEquals(0, EncoderWrapperJni.getPortList().length);

        new Encoder(0, 1);
        Assert.assertEquals(1, EncoderWrapperJni.getPortList().length);

        new Encoder(1, 2);
    }

    @Test
    public void testSpeedControllerFeedback()
    {
        SpeedController sc = new Talon(0);
        Encoder encoder = new Encoder(1, 2);

        DataAccessorFactory.getInstance().getEncoderAccessor().connectSpeedController(0, 0);
        DataAccessorFactory.getInstance().getSimulatorDataAccessor().setSpeedControllerModel_Simple(0, new SimpleMotorSimulationConfig(12));

        for (int i = 0; i < 50; ++i)
        {
            sc.set(1);
            DataAccessorFactory.getInstance().getSimulatorDataAccessor().updateSimulatorComponents(.02);
        }

        // Assert.assertEquals(12.0, encoder.getDistance(), DOUBLE_EPSILON);
        Assert.assertEquals(12.0, EncoderWrapperJni.getDistance(0), DOUBLE_EPSILON);
    }
}