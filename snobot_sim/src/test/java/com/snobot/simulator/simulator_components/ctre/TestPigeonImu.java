package com.snobot.simulator.simulator_components.ctre;

import com.snobot.simulator.module_wrapper.interfaces.IGyroWrapper;
import com.snobot.simulator.wrapper_accessors.AccelerometerWrapperAccessor;
import com.snobot.simulator.wrapper_accessors.GyroWrapperAccessor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.sensors.PigeonIMU;
import com.ctre.phoenix.sensors.PigeonIMU.FusionStatus;
import com.snobot.simulator.wrapper_accessors.DataAccessorFactory;
import com.snobot.test.utilities.BaseSimulatorJniTest;

@Tag("CTRE")
public class TestPigeonImu extends BaseSimulatorJniTest
{
    @ParameterizedTest
    @ArgumentsSource(GetCtreTestIds.GetCtreTestIdsFeedbackDevice.class)
    public void testPigeonInSeries(int aDeviceId)
    {
        PigeonIMU imu = new PigeonIMU(aDeviceId);
        testImu(imu, aDeviceId);
    }

    @ParameterizedTest
    @ArgumentsSource(GetCtreTestIds.GetCtreTestIdsFeedbackDevice.class)
    public void testPigeonInTalon(int aDeviceId)
    {
        TalonSRX talon = new TalonSRX(aDeviceId);
        PigeonIMU imu = new PigeonIMU(talon);
        testImu(imu, aDeviceId);
    }

    private void testImu(PigeonIMU aImu, int aDeviceId)
    {
        GyroWrapperAccessor gyroAccessor = DataAccessorFactory.getInstance().getGyroAccessor();
        AccelerometerWrapperAccessor accelerometerAccessor = DataAccessorFactory.getInstance().getAccelerometerAccessor();
        final double ANGLE_EPSILON = 1 / 16.0;

        int basePort = 400 + aDeviceId * 3;

        int yawPort = basePort + 0;
        int pitchPort = basePort + 1;
        int rollPort = basePort + 2;
        int xPort = basePort + 0;
        int yPort = basePort + 1;
        int zPort = basePort + 2;

        Assertions.assertEquals(3, gyroAccessor.getPortList().size());
        Assertions.assertEquals(3, accelerometerAccessor.getPortList().size());
        Assertions.assertTrue(gyroAccessor.getPortList().contains(yawPort));
        Assertions.assertTrue(gyroAccessor.getPortList().contains(pitchPort));
        Assertions.assertTrue(gyroAccessor.getPortList().contains(rollPort));
        Assertions.assertTrue(accelerometerAccessor.getPortList().contains(xPort));
        Assertions.assertTrue(accelerometerAccessor.getPortList().contains(yPort));
        Assertions.assertTrue(accelerometerAccessor.getPortList().contains(zPort));

        double[] rawAngles = new double[3];
        double[] yawPitchRollAngles = new double[3];
        FusionStatus fusionStatus = new FusionStatus();

        IGyroWrapper yawWrapper = gyroAccessor.getWrapper(yawPort);
        IGyroWrapper pitchWrapper = gyroAccessor.getWrapper(pitchPort);
        IGyroWrapper rollWrapper = gyroAccessor.getWrapper(rollPort);

        aImu.getRawGyro(rawAngles);
        aImu.getFusedHeading(fusionStatus);
        aImu.getYawPitchRoll(yawPitchRollAngles);
        Assertions.assertEquals(0, fusionStatus.heading, ANGLE_EPSILON);
        Assertions.assertEquals(0, rawAngles[0], ANGLE_EPSILON);
        Assertions.assertEquals(0, rawAngles[1], ANGLE_EPSILON);
        Assertions.assertEquals(0, rawAngles[2], ANGLE_EPSILON);
        Assertions.assertEquals(0, yawPitchRollAngles[0], ANGLE_EPSILON);
        Assertions.assertEquals(0, yawPitchRollAngles[1], ANGLE_EPSILON);
        Assertions.assertEquals(0, yawPitchRollAngles[2], ANGLE_EPSILON);
        Assertions.assertEquals(0, yawWrapper.getAngle(), ANGLE_EPSILON);
        Assertions.assertEquals(0, pitchWrapper.getAngle(), ANGLE_EPSILON);
        Assertions.assertEquals(0, rollWrapper.getAngle(), ANGLE_EPSILON);

        yawWrapper.setAngle(47);
        pitchWrapper.setAngle(-98);
        rollWrapper.setAngle(24);

        aImu.getRawGyro(rawAngles);
        aImu.getFusedHeading(fusionStatus);
        aImu.getYawPitchRoll(yawPitchRollAngles);
        Assertions.assertEquals(47, fusionStatus.heading, ANGLE_EPSILON);
        Assertions.assertEquals(47, rawAngles[0], ANGLE_EPSILON);
        Assertions.assertEquals(-98, rawAngles[1], ANGLE_EPSILON);
        Assertions.assertEquals(24, rawAngles[2], ANGLE_EPSILON);
        Assertions.assertEquals(47, yawPitchRollAngles[0], ANGLE_EPSILON);
        Assertions.assertEquals(-98, yawPitchRollAngles[1], ANGLE_EPSILON);
        Assertions.assertEquals(24, yawPitchRollAngles[2], ANGLE_EPSILON);
        Assertions.assertEquals(47, yawWrapper.getAngle(), ANGLE_EPSILON);
        Assertions.assertEquals(-98, pitchWrapper.getAngle(), ANGLE_EPSILON);
        Assertions.assertEquals(24, rollWrapper.getAngle(), ANGLE_EPSILON);
    }
}
