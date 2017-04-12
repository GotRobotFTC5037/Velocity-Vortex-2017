package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

@Autonomous(name = "Red: Parking", group = "Red")
@Disabled
public class RedAutonomous2 extends Archimedes
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
            drive(1.00, 300, 300);
            sleep(1500);
            launchBall(1000);
            sleep(1500);
            launchBall(1000);
            sleep(1500);
            stopBallLauncher();

            // Drive towards the center vortex, knock off the cap ball, turn
            // around and park.
            drive(1.00, 500, 500);
            turn(1.00, -135);
            timeDrive(-0.65, 1500);
        }
    }
}
