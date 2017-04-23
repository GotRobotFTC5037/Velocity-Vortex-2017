package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Blue: Parking @ Angle w/ Delay", group = "Blue")
public class BlueParkingAngleDelay extends Archimedes
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
            drive(1.00, 650, 650);
            launchBalls();
            stopBallLauncher();
            drive(1.00, 300, 300);
            turn(1.00, 180);
            timeDrive(-0.65, 1500);
        }
    }
}
