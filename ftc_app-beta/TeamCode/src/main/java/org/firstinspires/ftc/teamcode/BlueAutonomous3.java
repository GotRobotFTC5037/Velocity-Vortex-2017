package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Blue: Parking @ Angle", group = "Blue")
public class BlueAutonomous3 extends Archimedes
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
            drive(1.00, 650, 650);
            sleep(1500);
            launchBall(1000);
            sleep(1500);
            launchBall(1000);
            sleep(1500);
            stopBallLauncher();

            // Drive towards the center vortex, knock off the cap ball, turn
            // around and park.
            drive(1.00, 300, 300);
            turn(1.00, 160);
            timeDrive(-0.65, 1500);
        }
    }
}
