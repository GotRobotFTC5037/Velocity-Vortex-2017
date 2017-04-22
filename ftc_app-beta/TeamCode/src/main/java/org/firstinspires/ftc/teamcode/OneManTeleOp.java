package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.enums.ButtonPusherPosition;

@TeleOp(name = "One Man TeleOp")
public class OneManTeleOp extends Archimedes
{
    private Thread automaticBallLaunchThread;



    @Override
    public void runOpMode() throws InterruptedException
    {
        boolean areControlsReversed = false;

        initializeArchimedes();
        waitForStart();
        startArchimedes();
        startBallLauncher();

        while (opModeIsActive())
        {
            double rightMotorPower;
            double leftMotorPower;

            final boolean isTurningLeft = (getLeftDriveMotor().getPower() > 0
                    && getRightDriveMotor().getPower() <= 0);
            final boolean isTurningRight = (getRightDriveMotor().getPower() > 0
                    && getLeftDriveMotor().getPower() <= 0);
            final boolean isTurning = isTurningLeft || isTurningRight;

            final double maximumDriveSpeed = (1 - MAXIMUM_SLOW_POWER_DRIVE) *
                    Math.pow(1 - (getLiftPosition() / MAXIMUM_LIFT_HEIGHT), 2) +
                    MAXIMUM_SLOW_POWER_DRIVE;

            final double maximumTurnSpeed = (1 - MAXIMUM_SLOW_POWER_TURN) *
                    Math.pow(1 - (getLiftPosition() / MAXIMUM_LIFT_HEIGHT), 2) +
                    MAXIMUM_SLOW_POWER_TURN;

            if (gamepad1.right_bumper)
            {
                rightMotorPower = isTurning ?
                        ((1 - MINIMUM_TURN_POWER) - (1 - MAXIMUM_SLOW_POWER_TURN)) *
                                Math.pow(gamepad1.right_stick_y, 2) + MINIMUM_TURN_POWER :
                        ((1 - MINIMUM_DRIVE_POWER) - (1 - MAXIMUM_SLOW_POWER_DRIVE)) *
                                Math.pow(gamepad1.right_stick_y, 2) + MINIMUM_DRIVE_POWER;

                leftMotorPower = isTurning ?
                        ((1 - MINIMUM_DRIVE_POWER) - (1 - MAXIMUM_SLOW_POWER_DRIVE)) *
                                Math.pow(gamepad1.left_stick_y, 2) + MINIMUM_TURN_POWER :
                        ((1 - MINIMUM_DRIVE_POWER) - (1 - MAXIMUM_SLOW_POWER_DRIVE)) *
                                Math.pow(gamepad1.left_stick_y, 2) + MINIMUM_DRIVE_POWER;

            }
            else
            {
                rightMotorPower = isTurning ?
                        ((1 - MINIMUM_TURN_POWER) - (1 - maximumTurnSpeed)) *
                                Math.pow(gamepad1.right_stick_y, 2) + MINIMUM_TURN_POWER :
                        ((1 - MINIMUM_DRIVE_POWER) - (1 - maximumDriveSpeed)) *
                                Math.pow(gamepad1.right_stick_y, 2) + MINIMUM_DRIVE_POWER;

                leftMotorPower = isTurning ?
                        ((1 - MINIMUM_TURN_POWER) - (1 - maximumTurnSpeed)) *
                                Math.pow(gamepad1.left_stick_y, 2) + MINIMUM_TURN_POWER :
                        ((1 - MINIMUM_DRIVE_POWER) - (1 - maximumDriveSpeed)) *
                                Math.pow(gamepad1.left_stick_y, 2) + MINIMUM_DRIVE_POWER;
            }

            if (gamepad1.right_stick_y == 0)
                rightMotorPower = 0;
            else if (gamepad1.right_stick_y > 0)
                rightMotorPower *= -1;

            if (gamepad1.left_stick_y == 0)
                leftMotorPower = 0;
            else if (gamepad1.left_stick_y > 0)
                leftMotorPower *= -1;

            if (gamepad1.start)
            {
                areControlsReversed = !areControlsReversed;
                while (gamepad1.start)
                {
                    idle();
                }
            }

            // Gunner Controls
            if (gamepad1.right_trigger == 1.0)
            {
                if (automaticBallLaunchThread == null)
                {
                    automaticBallLaunchThread = new Thread(new Runnable()
                    {
                        @Override public void run()
                        {
                            BallLauncherSpeedControlThread.setPriority(Thread.NORM_PRIORITY + 2);
                            shouldRecordBallLauncherIntergal = false;
                            while (opModeIsActive())
                            {
                                liftBallDeployer();
                                try
                                {
                                    Thread.sleep(BALL_LAUNCH_WAIT_TIME);
                                }
                                catch (InterruptedException e)
                                {
                                    break;
                                }

                                dropBallDeployer();
                                try
                                {
                                    Thread.sleep(POST_LAUNCH_WAIT_TIME);
                                }
                                catch (InterruptedException e)
                                {
                                    break;
                                }
                            }

                            shouldRecordBallLauncherIntergal = true;
                            BallLauncherSpeedControlThread.setPriority(Thread.NORM_PRIORITY + 1);
                        }
                    });
                }

                if (!automaticBallLaunchThread.isAlive())
                {
                    automaticBallLaunchThread.setPriority(Thread.NORM_PRIORITY + 1);
                    automaticBallLaunchThread.setName("Automatic Ball Launch Thread");
                    automaticBallLaunchThread.start();
                }
            }
            else if (gamepad1.right_trigger != 1.0 && gamepad1.right_trigger != 0.0)
            {
                if (automaticBallLaunchThread != null && automaticBallLaunchThread.isAlive())
                {
                    automaticBallLaunchThread.interrupt();
                    automaticBallLaunchThread.join();
                    automaticBallLaunchThread = null;
                }

                liftBallDeployer();
            }
            else
            {
                if (automaticBallLaunchThread != null && automaticBallLaunchThread.isAlive())
                {
                    automaticBallLaunchThread.interrupt();
                    automaticBallLaunchThread.join();
                    automaticBallLaunchThread = null;
                }
                dropBallDeployer();
            }

            if (gamepad1.left_bumper)
                startBallCollector();
            else
                stopBallCollector();

            if (gamepad1.b)
                setButtonPusherPosition(ButtonPusherPosition.RIGHT_POSITION);

            if (gamepad1.x)
                setButtonPusherPosition(ButtonPusherPosition.LEFT_POSITION);

            if (gamepad1.a || gamepad1.y)
                setButtonPusherPosition(ButtonPusherPosition.NEUTRAL_POSITION);

            if (areControlsReversed)
            {
                getRightDriveMotor().setPower(-rightMotorPower);
                getLeftDriveMotor().setPower(-leftMotorPower);
            }
            else
            {
                getRightDriveMotor().setPower(leftMotorPower);
                getLeftDriveMotor().setPower(rightMotorPower);
            }
        }

        BallLauncherSpeedControlThread.interrupt();
        BallLauncherSpeedControlThread.join();
    }
}
