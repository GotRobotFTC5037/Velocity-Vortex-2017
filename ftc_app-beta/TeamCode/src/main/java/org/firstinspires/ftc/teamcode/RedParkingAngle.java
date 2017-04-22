package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Red: Parking @ Angle", group = "Red")
public class RedParkingAngle extends Archimedes
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
            drive(1.00, 675, 675);
            launchBalls(2);
            stopBallLauncher();
            drive(1.00, 675, 675);
            turn(1.00, -160);
            timeDrive(-0.65, 1500);
        }
    }
}
