package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="Fly Wheel Speed", group="Tests")
public class FWST extends Archimedes
{
    @Override public void runOpMode() throws InterruptedException
    {
        double maxSpeed = 0;
        double minSpeed = 0;

        initializeArchimedes();
        waitForGyroCalibration();
        waitForStart();
        startArchimedes();

        startBallLauncherAtLowPower();

        while (opModeIsActive())
        {
            final long initialPosition = getBallLauncher().getCurrentPosition();
            sleep(50);
            final long finalPosition = getBallLauncher().getCurrentPosition();
            final double speed = ((finalPosition - initialPosition) / .05) / 1440;

            if (gamepad1.a)
            {
                liftBallDeployer();

                if (speed > maxSpeed)
                {
                    maxSpeed = speed;
                }
                else if (speed < minSpeed || minSpeed == 0)
                {
                    minSpeed = speed;
                }

                telemetry.addData("Max Speed", maxSpeed);
                telemetry.addData("Min Speed", minSpeed);
            }
            else
            {
                maxSpeed = 0;
                minSpeed = 0;
            }

            telemetry.addData("Speed", speed);
            telemetry.addData("Pos", getBallLauncher().getCurrentPosition());
            telemetry.update();
        }
    }
}
