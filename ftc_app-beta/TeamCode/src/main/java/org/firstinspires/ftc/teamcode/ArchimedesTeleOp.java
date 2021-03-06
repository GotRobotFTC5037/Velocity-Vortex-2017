package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.enums.ButtonPusherPosition;

@TeleOp(name = "TeleOp")
public class ArchimedesTeleOp extends Archimedes
{
    private Thread automaticBallLaunchThread;



    @Override
    public void runOpMode() throws InterruptedException
    {
        boolean areControlsReversed = false;
        boolean isGrabberDeployed = false;

        initializeArchimedes();
        waitForStart();
        startArchimedes();
        startBallLauncher();

        while (opModeIsActive() && isGameActive())
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
            if (gamepad2.right_trigger == 1.0 && rightMotorPower == 0 && leftMotorPower == 0)
            {
                if (automaticBallLaunchThread == null)
                {
                    automaticBallLaunchThread = new Thread(new Runnable()
                    {
                        @Override public void run()
                        {
                            BallLauncherSpeedControlThread.setPriority(Thread.NORM_PRIORITY + 2);
                            shouldRecordBallLauncherIntegral = false;

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

                            shouldRecordBallLauncherIntegral = true;
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
            else if (gamepad2.right_trigger != 1.0 && gamepad2.right_trigger != 0.0 &&
                    rightMotorPower == 0 && leftMotorPower == 0)
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

            if (gamepad2.left_bumper || gamepad2.right_trigger != 0.0)
                startBallCollector();
            else if (gamepad2.right_bumper)
                reverseBallCollector();
            else
                stopBallCollector();

            if ((gamepad2.dpad_up && gamepad1.x) || (gamepad2.dpad_up && (isGrabberDeployed || isEndGameActive())))
            {
                isGrabberDeployed = true;
                liftCapBallGrabber();
            }
            else if ((gamepad2.dpad_left || gamepad2.dpad_right) && isGrabberDeployed)
                clampCapBallGrabber();
            else if (gamepad2.dpad_down)
                dropCapBallGrabber();

            if (gamepad2.b)
                setButtonPusherPosition(ButtonPusherPosition.RIGHT_POSITION);

            if (gamepad2.x)
                setButtonPusherPosition(ButtonPusherPosition.LEFT_POSITION);

            if (gamepad2.a || gamepad2.y)
                setButtonPusherPosition(ButtonPusherPosition.NEUTRAL_POSITION);

            if (isGrabberDeployed)
            {
                setLiftPower(-gamepad2.right_stick_y);
            }

            if (areControlsReversed && shouldRecordBallLauncherIntegral)
            {
                getRightDriveMotor().setPower(-rightMotorPower);
                getLeftDriveMotor().setPower(-leftMotorPower);
            }
            else if (!areControlsReversed && shouldRecordBallLauncherIntegral)
            {
                getRightDriveMotor().setPower(leftMotorPower);
                getLeftDriveMotor().setPower(rightMotorPower);
            }
            else if (!shouldRecordBallLauncherIntegral)
            {
                getRightDriveMotor().setPower(0.0);
                getLeftDriveMotor().setPower(0.0);
            }
        }

        liftCapBallGrabber();
        BallLauncherSpeedControlThread.interrupt();
        BallLauncherSpeedControlThread.join();
    }
}
