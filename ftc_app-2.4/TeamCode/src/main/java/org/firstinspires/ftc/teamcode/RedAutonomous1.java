package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Delivers two balls into the center vortex, then presses one of the beacon
 * buttons. Is set up on the default red setup position.
 *
 * @author got robot?
 */
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
            drive(1.00, 300);
            sleep(1500);
            launchBall(1000);
            sleep(1500);
            launchBall(1000);
            stopBallLauncher();

            // Turn toward the beacon line, drive to it and then turn into it.
            turn(0.40, -42);
            driveToLine(0.85, DEFAULT_LINE_THRESHOLD, 1250, 65);
            turn(0.40, -48);

            // This is needed to expose the color sensor
            turnButtonPusherLeft();

            boolean isFirstBeaconPressed = false;
            while (!isFirstBeaconPressed && opModeIsActive())
            {
                // Follow the line up to the beacon.
                followLineToWall(0.2, 12);
                sleep(500);

                // Detect if the robot is lined up with the beacon, if it is
                // then detect what color is the red one and press it
                telemetry.addData("Left", leftOds.getLightDetected());
                telemetry.addData("Right", rightOds.getLightDetected());
                telemetry.update();
                if (isAlignedWithBeacon())
                {
                    if (isDetectingRedOnRight())
                    {
                        turnButtonPusherRight();
                        sleep(500);
                    }
                    timeDrive(0.50, 500);
                    drive(0.50, -120);

                    // As a safety feature, check the color of the beacon, if it is
                    // blue, wait 5 seconds and press the beacon again.
                    turnButtonPusherLeft();
                    sleep(1000);

                    if (isDetectingBlueOnRight())
                    {
                        setButtonPusherToNeutral();
                        sleep(5000);
                        timeDrive(0.50, 500);
                        drive(0.50, -120);
                        stop();
                    }

                    isFirstBeaconPressed = true;
                }
                else
                {
                    drive(0.50, -270);
                }
            }

            // Turn toward the second line, drive towards it and turn into
            // the line.
            turn(0.65, 90);
            driveToLine(0.85, DEFAULT_LINE_THRESHOLD, 1050, 100);
            turn(.65, -90);

            // This is needed to expose the color sensor
            turnButtonPusherLeft();

            boolean isSecondBeaconPressed = false;
            while (!isSecondBeaconPressed && opModeIsActive())
            {
                // Follow the line up to the beacon.
                followLineToWall(0.20, 12);

                // Detect if the robot is lined up with the beacon, if it is then
                // detect what color is the red one and press it
                if (isAlignedWithBeacon())
                {
                    sleep(500);
                    if (isDetectingRedOnRight())
                    {
                        turnButtonPusherRight();
                        sleep(500);
                    }
                    timeDrive(.5, 500);
                    drive(.5, -70);

                    // As a safety feature, check the color of the beacon, if it is
                    // blue, wait 5 seconds and press the beacon again.
                    turnButtonPusherLeft();
                    sleep(1000);

                    if (isDetectingBlueOnRight())
                    {
                        setButtonPusherToNeutral();
                        sleep(5000);
                        timeDrive(.5, 500);
                        drive(.5, -70);
                        stop();
                    }

                    isSecondBeaconPressed = true;
                }
                else
                {
                    drive(.5, -275);
                }
            }
        }
    }
}
