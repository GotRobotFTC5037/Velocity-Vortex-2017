package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "TeleOp")
public class ArchimedesTeleOp extends Archimedes
{
    @Override
    public void runOpMode() throws InterruptedException
    {
        boolean isControlsReversed = false;
        boolean isGrabberDeployed = false;

        initializeArchimedes();
        waitForStart();
        startArchimedes();

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
            else if (gamepad1.right_stick_y < 0)
                rightMotorPower *= -1;

            if (gamepad1.left_stick_y == 0)
                leftMotorPower = 0;
            else if (gamepad1.left_stick_y < 0)
                leftMotorPower *= -1;

            if (gamepad1.start)
            {
                isControlsReversed = !isControlsReversed;
                while (gamepad1.start)
                {
                    idle();
                }
            }

            // Gunner Controls
            if (gamepad2.right_trigger > 0 && rightMotorPower == 0 &&
                    leftMotorPower == 0)
                liftBallDeployer();
            else
                dropBallDeployer();

            if (gamepad2.left_trigger > 0)
                startBallLauncherAtHighPower();
            else
                stopBallLauncher();

            if (gamepad2.left_bumper)
                startBallCollector();
            else if (gamepad2.right_bumper)
                reverseBallCollector();
            else
                stopBallCollector();

            if (isGrabberDeployed ?
                    (gamepad2.dpad_up && rightMotorPower == 0 && leftMotorPower == 0) :
                    gamepad1.x && gamepad2.dpad_up)
            {
                isGrabberDeployed = true;
                liftCapBallGrabber();
            }

            if ((gamepad2.dpad_left || gamepad2.dpad_right) &&
                    isGrabberDeployed)
                clampCapBallGrabber();

            if (gamepad2.dpad_down)
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

            if (isControlsReversed)
            {
                getRightDriveMotor().setPower(rightMotorPower);
                getLeftDriveMotor().setPower(leftMotorPower);
            }
            else
            {
                getRightDriveMotor().setPower(-leftMotorPower);
                getLeftDriveMotor().setPower(-rightMotorPower);
            }
        }
    }
}
