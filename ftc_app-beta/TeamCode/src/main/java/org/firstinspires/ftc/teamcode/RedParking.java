package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Red: Parking", group = "Red")
public class RedParking extends Archimedes
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
            drive(1.00, 300, 300);
            launchBalls();
            stopBallLauncher();
            drive(1.00, 500, 500);
            turn(1.00, -135);
            timeDrive(-0.65, 1500);
        }
    }
}
