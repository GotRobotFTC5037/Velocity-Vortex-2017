package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="Fly Wheel Dec", group="Tests")
public class FWDC extends Archimedes
{
    @Override
    public void runOpMode() throws InterruptedException
    {
        initializeArchimedes();
        waitForGyroCalibration();
        waitForStart();
        startArchimedes();

        final double[] speedList = {};
        double averageDeceleration = 0;

        startBallLauncherAtLowPower();
        wait(5000);
        stopBallLauncher();

        while (opModeIsActive())
        {
            final long initialPosition = getBallLauncher().getCurrentPosition();
            sleep(50);
            final long finalPosition = getBallLauncher().getCurrentPosition();
            final double speed = ((finalPosition - initialPosition) / .05) / 1440;
            speedList[speedList.length] = speed;

            if (speed == 0)
            {
                break;
            }
        }

        for (int i = 1; i < speedList.length; i++)
        {
            averageDeceleration += speedList[i] - speedList[i - 1];
        }

        averageDeceleration /= speedList.length - 1;

        telemetry.addData("Average Deceleration", averageDeceleration);
        telemetry.update();
    }
}
