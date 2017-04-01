package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Delivers two balls into the center vortex, then presses one of the beacon
 * buttons. Is set up on the default red setup position.
 *
 * @author got robot?
 */
@SuppressWarnings({"OverlyLongMethod", "OverlyComplexMethod", "MagicNumber"})
@Autonomous(name = "Red: Beacons", group = "Red")
public class RedAutonomous1 extends Archimedes
{
    @Override
    public void runOpMode() throws InterruptedException
    {
        initializeArchimedes();
        waitForGyroCalibration();
        waitForStart();
        startArchimedes();

        if (opModeIsActive())
        {
            // Launch balls into center vortex.
            startBallLauncherAtLowPower();
            drive(1.00, 300, 100);
            idle();
            sleep(1400);
            launchBall(750);
            idle();
            sleep(1000);
            launchBall(750);
            stopBallLauncher();
            idle();

            // Turn toward the beacon line, drive to it and then turn into it.
            turn(0.80, -34);
            idle();
            driveToLine(1.00, DEFAULT_LINE_THRESHOLD, 1175, 500, POST_LINE_DISTANCE_RED);
            idle();
            turn(0.50, -56);
            idle();

            // This is needed to expose the color sensor
            turnButtonPusherLeft();

            boolean isFirstBeaconPressed = false;
            while (!isFirstBeaconPressed && opModeIsActive())
            {
                // Follow the line up to the beacon.
                followLineToWall(0.2, DEFAULT_WALL_DISTANCE);
                idle();
                correctHeading(0.35, 6);
                idle();

                // Detect if the robot is lined up with the beacon
                if (isAlignedWithBeacon())
                {
                    // Detects what color is red
                    if (isDetectingRedOnRight())
                    {
                        turnButtonPusherRight();
                        sleep(500);
                    }
                    pressBeaconButton(0.5, 500);
                    drive(0.50, -60, 0);

                    // Check the color of the beacon, if it is blue, wait 5 seconds and press the
                    // beacon again.
                    turnButtonPusherLeft();
                    idle();
                    sleep(500);

                    while (isDetectingBlueOnRight())
                    {
                        // Wait and press button
                        setButtonPusherToNeutral();
                        idle();
                        sleep(5000);
                        pressBeaconButton(0.5, 500);
                        drive(0.50, -60, 0);

                        // Setup to check for errors again
                        turnButtonPusherLeft();
                        idle();
                        sleep(1000);
                    }

                    isFirstBeaconPressed = true;
                }
                else
                {
                    drive(0.50, -270, 270);
                }
            }

            // Turn toward the second line, drive towards it and turn into
            // the line.
            drive(0.50, -100, 50);
            turn(0.65, 90);
            driveToLine(1, DEFAULT_LINE_THRESHOLD, 1175, 500, POST_LINE_DISTANCE_RED);
            turn(0.50, -90);

            // This is needed to expose the color sensor
            turnButtonPusherLeft();

            boolean isSecondBeaconPressed = false;
            while (!isSecondBeaconPressed && opModeIsActive())
            {
                // Follow the line up to the beacon.
                followLineToWall(0.2, DEFAULT_WALL_DISTANCE);
                idle();
                correctHeading(0.35, 6);
                idle();

                // Detect if the robot is lined up with the beacon
                if (isAlignedWithBeacon())
                {
                    // Detects what color is red
                    if (isDetectingRedOnRight())
                    {
                        turnButtonPusherRight();
                        sleep(500);
                    }
                    pressBeaconButton(0.5, 500);
                    drive(0.50, -60, 0);

                    // Check the color of the beacon, if it is blue, wait 5 seconds and press the
                    // beacon again.
                    turnButtonPusherLeft();
                    idle();
                    sleep(500);

                    while (isDetectingBlueOnRight())
                    {
                        // Wait and press button
                        setButtonPusherToNeutral();
                        idle();
                        sleep(5000);
                        pressBeaconButton(0.5, 500);
                        drive(0.50, -60, 0);

                        // Setup to check for errors again
                        turnButtonPusherLeft();
                        idle();
                        sleep(1000);
                    }

                    isSecondBeaconPressed = true;
                }
                else
                {
                    drive(0.50, -270, 270);
                }
            }
            drive(0.50, -100, 50);
        }
    }
}
