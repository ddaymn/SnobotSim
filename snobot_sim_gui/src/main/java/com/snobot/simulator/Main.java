package com.snobot.simulator;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collection;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

import com.snobot.simulator.wrapper_accessors.DataAccessorFactory;
import com.snobot.simulator.wrapper_accessors.SimulatorDataAccessor.SnobotLogLevel;

import edu.wpi.first.wpilibj.util.WPILibVersion;

public final class Main
{
    private static final String sUSER_CONFIG_DIR = "simulator_config/";

    private static final PrintStream VERSION_PRINTER = System.out;

    private Main()
    {

    }

    public static void main(String[] aArgs)
    {
        DefaultDataAccessorFactory.initalize();

        Collection<String> argList = Arrays.asList(aArgs);
        boolean useBuiltinDriverStation = true;

        if (argList.contains("version"))
        {
            printVersions();
        }

        if (argList.contains("disable_driver_station"))
        {
            useBuiltinDriverStation = false;
        }

        try
        {
            Simulator simulator = new Simulator(parseLogLevel(argList), sUSER_CONFIG_DIR, useBuiltinDriverStation);
            simulator.startSimulation();
        }
        catch (ClassNotFoundException e)
        {
            LogManager.getLogger().log(Level.FATAL, "Class not found exception.  You either have an error in your properties file, "
                    + "or the project is not set up to be able to find the robot project you are attempting to create"
                    +  "\nerror: " + e, e);

            System.exit(-1);
        }
        catch (UnsatisfiedLinkError e)
        {
            LogManager.getLogger().log(Level.FATAL, "Unsatisfied link error.  This likely means that there is a native "
                    + "call in WpiLib or the NetworkTables libraries.  Please tell PJ so he can mock it out.\n\nError Message: " + e, e);

            System.exit(-1);
        }
        catch (Exception e)
        {
            LogManager.getLogger().log(Level.ERROR, "Unknown exception...", e);
            System.exit(1);
        }
    }

    private static SnobotLogLevel parseLogLevel(Collection<String> aArgList)
    {
        int logLevel = 0;

        for (String arg : aArgList)
        {
            if (arg.startsWith("log_level="))
            {
                logLevel = Integer.parseInt(arg.substring("log_level=".length()));
            }
        }

        return SnobotLogLevel.values()[logLevel];
    }

    private static void printVersions()
    {
        VERSION_PRINTER.println("Versions:");
        VERSION_PRINTER.println("Wpilib Java    : " + WPILibVersion.Version);
        VERSION_PRINTER.println("SnobotSim HAL  : " + DataAccessorFactory.getInstance().getSimulatorDataAccessor().getNativeBuildVersion());
        VERSION_PRINTER.println("SnobotSim GUI  : " + SnobotSimGuiVersion.Version);
        VERSION_PRINTER.println("SnobotSim Type : " + DataAccessorFactory.getInstance().getAccessorType());
    }
}
