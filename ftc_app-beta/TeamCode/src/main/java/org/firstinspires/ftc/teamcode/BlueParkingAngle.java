package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Blue: Parking @ Angle", group = "Blue")
public class BlueParkingAngle extends Archimedes
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
            drive(1.00, 600, 600);
            launchBalls();
            stopBallLauncher();
            drive(1.00, 300, 300);
            turn(1.00, 180);
            timeDrive(-0.65, 1500);
        }
    }
}
