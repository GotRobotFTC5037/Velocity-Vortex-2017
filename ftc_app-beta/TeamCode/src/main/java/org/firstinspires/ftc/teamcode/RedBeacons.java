package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.enums.BeaconColor;
import org.firstinspires.ftc.teamcode.enums.ButtonPusherPosition;
import org.firstinspires.ftc.teamcode.enums.ColorSensorPosition;

@Autonomous(name = "Red: Beacons", group = "Red")
public class RedBeacons extends Archimedes
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
        startBallLauncher();
        drive(1.00, 300, 300);
        launchBalls();
        stopBallLauncher();

        // Turn toward the beacon line, drive to it and then turn into it.
        turn(1.00, -37);
        driveToLine(1.0, 1175, 1000);
        turn(1.00, -53);

        // Press the beacon. Follows line up to wall, detects the color and presses correct button.
        pressBeacon();

        // Turn toward the second line, drive towards it and turn into the line.
        drive(0.50, -125, 50);
        turn(1.00, 90);
        driveToLine(1.00, 1175, 1000);
        turn(1.00, -90);

        // Press the beacon. Follows line up to wall, detects the color and presses correct button.
        pressBeacon();

        // Back away from the beacon as a way to show that everything has completed.
        drive(0.50, -650, 25);
        turn(1.0, 45);
        drive(1, -600, 0);
        timeDrive(-0.5, 5000);

        stop();
    }



    private void pressBeacon()
    {
        followLineToWall: while (opModeIsActive())
        {
            // Follow the line up to the beacon.
            followLineToWall(0.05, DEFAULT_WALL_DISTANCE);
            correctHeading(0.6);

            // Detect if the robot is lined up with the beacon.
            if (isAlignedWithBeacon())
            {
                final BeaconColor leftDetectedColor;
                final BeaconColor rightDetectedColor;

                // Turn the button pusher left to expose the right color sensor.
                setButtonPusherPosition(ButtonPusherPosition.LEFT_POSITION);
                rightDetectedColor = getDetectedColor(ColorSensorPosition.RIGHT_SENSOR);

                // Turn the button pusher right to expose the left color sensor.
                setButtonPusherPosition(ButtonPusherPosition.RIGHT_POSITION);
                leftDetectedColor = getDetectedColor(ColorSensorPosition.LEFT_SENSOR);

                // Use both sensors to determine the current state of the beacon and from there
                // chose a side to press.
                if (leftDetectedColor == BeaconColor.COLOR_RED
                        && rightDetectedColor != BeaconColor.COLOR_RED)
                {
                    pressBeaconButton(0.6, 500, ButtonPusherPosition.LEFT_POSITION);
                }
                else if (rightDetectedColor == BeaconColor.COLOR_RED
                        && leftDetectedColor != BeaconColor.COLOR_RED)
                {
                    pressBeaconButton(0.6, 500, ButtonPusherPosition.RIGHT_POSITION);
                }
                else if (rightDetectedColor == BeaconColor.COLOR_BLUE
                        && leftDetectedColor == BeaconColor.COLOR_BLUE)
                {
                    pressBeaconButton(0.6, 500, ButtonPusherPosition.NEUTRAL_POSITION);
                }
                else if (rightDetectedColor == BeaconColor.COLOR_RED)
                {
                    setButtonPusherPosition(ButtonPusherPosition.NEUTRAL_POSITION);
                    return;
                }
                else if (rightDetectedColor == BeaconColor.COLOR_BLUE)
                {
                    pressBeaconButton(0.6, 500, ButtonPusherPosition.LEFT_POSITION);
                }
                else if (leftDetectedColor == BeaconColor.COLOR_BLUE)
                {
                    pressBeaconButton(0.6, 500, ButtonPusherPosition.RIGHT_POSITION);
                }
                else
                {
                    withdrawFromBeacon();
                    continue;
                }

                while (opModeIsActive())
                {
                    final BeaconColor leftDetectedColorCheck;
                    final BeaconColor rightDetectedColorCheck;

                    rightDetectedColorCheck = getDetectedColor(ColorSensorPosition.RIGHT_SENSOR);

                    // Using the fastest method possible, check and make sure that the beacon is
                    // in the correct position after pressing the beacon.
                    if(rightDetectedColorCheck == rightDetectedColor
                            || rightDetectedColorCheck == BeaconColor.UNKNOWN
                            || rightDetectedColorCheck == BeaconColor.ERROR)
                    {
                        setButtonPusherPosition(ButtonPusherPosition.RIGHT_POSITION);
                        leftDetectedColorCheck = getDetectedColor(ColorSensorPosition.LEFT_SENSOR);

                        switch (rightDetectedColorCheck)
                        {
                            case COLOR_RED:
                                switch (leftDetectedColorCheck)
                                {
                                    case COLOR_BLUE:
                                        pressBeaconButton(0.65, 500, ButtonPusherPosition.RIGHT_POSITION);
                                        break;

                                    default:
                                        setButtonPusherPosition(ButtonPusherPosition.NEUTRAL_POSITION);
                                        return;
                                }
                                break;

                            case COLOR_BLUE:
                                switch (leftDetectedColorCheck)
                                {
                                    case COLOR_RED:
                                        pressBeaconButton(0.65, 500, ButtonPusherPosition.LEFT_POSITION);
                                        break;

                                    default:
                                        correctBeacon();
                                        break;
                                }
                                break;

                            default:
                                switch (leftDetectedColorCheck)
                                {
                                    case COLOR_RED:
                                        setButtonPusherPosition(ButtonPusherPosition.NEUTRAL_POSITION);
                                        return;

                                    case COLOR_BLUE:
                                        correctBeacon();
                                        break;

                                    default:
                                        withdrawFromBeacon();
                                        continue followLineToWall;
                                }
                                break;
                        }
                    }
                    else
                    {
                        switch(rightDetectedColorCheck)
                        {
                            case COLOR_RED:
                                setButtonPusherPosition(ButtonPusherPosition.NEUTRAL_POSITION);
                                return;

                            default:
                                correctBeacon();
                                break ;
                        }
                    }
                }
            }
            else
            {
                withdrawFromBeacon();
            }
        }
    }
}
