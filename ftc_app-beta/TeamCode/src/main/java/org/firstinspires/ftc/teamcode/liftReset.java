package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Reset lIft")
public class liftReset extends Archimedes
{

    @Override public void runOpMode() throws InterruptedException
    {
        initializeArchimedes();
        waitForStart();
        startArchimedes();

        liftCapBallGrabber();

        while (isLiftElevated())
        {
            setLiftPower(-1.0);
        }

        dropCapBallGrabber();
    }
}
