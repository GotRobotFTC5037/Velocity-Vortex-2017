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
        // Initialize the robot, wait for the gyro to calibrate and wait for the start of the match.
        initializeArchimedes();
        waitForGyroCalibration();
        waitForStart();
        startArchimedes();

        // Launch balls into center vortex.
        startBallLauncherAtLowPower();
        drive(1.00, 300, 75);
        sleep(1400);
        launchBall(750);
        sleep(1000);
        launchBall(750);
        stopBallLauncher();

        // Turn toward the beacon line, drive to it and then turn into it.
        turn(1.00, -34);
        driveToLine(1.00, 1175, 250, POST_LINE_DISTANCE_RED);
        turn(0.80, -56);

        // Press the beacon. Follows line up to wall, detects the color and presses correct button.
        pressBeacon();

        // Turn toward the second line, drive towards it and turn into the line.
        drive(0.50, -75, 25);
        turn(1.00, 90);
        driveToLine(1.00, 1175, 250, POST_LINE_DISTANCE_RED);
        turn(0.80, -90);

        // Press the beacon. Follows line up to wall, detects the color and presses correct button.
        pressBeacon();

        // Back away from the beacon as a way to show that everything has completed.
        drive(0.50, -75, 25);
    }



    private void pressBeacon()
    {
        while (opModeIsActive())
        {
            // Turn the button pusher left to expose the color sensor.
            setButtonPusherPosition(ButtonPusherPosition.LEFT_POSITION);

            // Follow the line up to the beacon.
            followLineToWall(0.2, DEFAULT_WALL_DISTANCE);
            correctHeading(0.35);

            // Detect if the robot is lined up with the beacon.
            if (isAlignedWithBeacon())
            {
                // Detect the color of the beacon. This could take up to one second. Anymore and
                // the robot with determine its not lined up properly or that there is an error.
                final BeaconColor detectedColor = getDetectedColorOnRight();

                //Turn the beacon button pusher to the appropriate location or backup and
                // re-approach.
                if (detectedColor == BeaconColor.COLOR_RED)
                {
                    setButtonPusherPosition(ButtonPusherPosition.RIGHT_POSITION);
                    sleep(300);
                }
                else if (detectedColor == BeaconColor.UNKNOWN ||detectedColor == BeaconColor.ERROR)
                {
                    setButtonPusherPosition(ButtonPusherPosition.NEUTRAL_POSITION);
                    drive(0.50, -270, 270);
                    continue;
                }

                // Press the button and drive back to where it just was.
                pressBeaconButton(0.5, 500);
                drive(0.50, -60, 60);

                // Check the color of the beacon, if it is not correct, wait 5 seconds and press
                // the beacon again.
                setButtonPusherPosition(ButtonPusherPosition.LEFT_POSITION);

                // Until the color is confirmed to be correct, continue to correct the beacon.
                while (getDetectedColorOnRight() == BeaconColor.COLOR_BLUE)
                {
                    // Change the position of the beacon pusher and wait for the beacon timer.
                    setButtonPusherPosition(ButtonPusherPosition.NEUTRAL_POSITION);
                    interruptibleWait(5000);

                    // Press the button and drive back to where it just was.
                    pressBeaconButton(0.5, 500);
                    drive(0.50, -60, 60);

                    // Setup to check for errors again
                    setButtonPusherPosition(ButtonPusherPosition.LEFT_POSITION);
                }

                // Return to neutral as a way to show the position in the program.
                setButtonPusherPosition(ButtonPusherPosition.NEUTRAL_POSITION);
                break;
            } // if (isAlignedWithBeacon())
            else
            {
                // Back up so that the robot can re-approach.
                drive(0.50, -270, 270);
            }
        }
    }
}
