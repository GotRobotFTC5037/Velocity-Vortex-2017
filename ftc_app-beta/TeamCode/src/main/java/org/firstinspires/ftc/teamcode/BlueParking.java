package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Blue: Parking", group = "Blue")
public class BlueParking extends Archimedes
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
            launchBalls(2);
            stopBallLauncher();
            drive(1.00, 500, 500);
            turn(1.00, 175);
            timeDrive(-0.65, 1500);
        }
    }
}
