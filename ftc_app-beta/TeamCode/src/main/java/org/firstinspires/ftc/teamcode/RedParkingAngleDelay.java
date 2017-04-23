package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Red: Parking @ Angle w/ Delay", group = "Red")
public class RedParkingAngleDelay extends Archimedes
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
            startBallLauncher();
            interruptibleSleep(5000);
            drive(1.00, 675, 675);
            launchBalls();
            stopBallLauncher();
            drive(1.00, 675, 675);
            turn(1.00, -115);
            timeDrive(-0.65, 1500);
        }
    }
}
