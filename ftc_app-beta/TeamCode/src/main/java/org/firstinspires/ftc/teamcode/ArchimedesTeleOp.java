package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "TeleOp")
public class ArchimedesTeleOp extends Archimedes
{
    @Override
    public void runOpMode() throws InterruptedException
    {
        final double MAXIMUM_SLOW_POWER_TURN = 0.4;
        final double MAXIMUM_SLOW_POWER_DRIVE = 0.25;

        double rightMotorPower;
        double leftMotorPower;

        boolean isDriveControlsInReverse = false;
        boolean hasCapBallGrabberBeenDeployed = false;

        initializeArchimedes();
        waitForStart();
        startArchimedes();

        while (opModeIsActive())
        {
            boolean isRobotTurning = (getLeftDriveMotor().getPower() > 0 &&
                    getRightDriveMotor().getPower() <= 0) ||
                    (getRightDriveMotor().getPower() > 0 && getLeftDriveMotor
                            ().getPower() <= 0);

            if (gamepad1.right_bumper)
            {
                rightMotorPower = isRobotTurning ?
                        ((1 - MINIMUM_TURN_POWER) -
                                (1 - MAXIMUM_SLOW_POWER_TURN)) *
                                Math.pow(gamepad1.right_stick_y, 2) +
                                MINIMUM_TURN_POWER :
                        ((1 - MINIMUM_DRIVE_POWER) -
                                (1 - MAXIMUM_SLOW_POWER_DRIVE)) *
                                Math.pow(gamepad1.right_stick_y, 2) +
                                MINIMUM_DRIVE_POWER;

                leftMotorPower = isRobotTurning ?
                        ((1 - MINIMUM_DRIVE_POWER) -
                                (1 - MAXIMUM_SLOW_POWER_DRIVE)) *
                                Math.pow(gamepad1.left_stick_y, 2) +
                                MINIMUM_TURN_POWER :
                        ((1 - MINIMUM_DRIVE_POWER) -
                                (1 - MAXIMUM_SLOW_POWER_DRIVE)) *
                                Math.pow(gamepad1
                                        .left_stick_y, 2) +
                                MINIMUM_DRIVE_POWER;

            }
            else
            {
                rightMotorPower = isRobotTurning ?
                        (1 - MINIMUM_TURN_POWER) *
                                Math.pow(gamepad1.right_stick_y, 2) +
                                MINIMUM_TURN_POWER :
                        (1 - MINIMUM_DRIVE_POWER) * Math.pow(gamepad1
                                .right_stick_y, 2) +
                                MINIMUM_DRIVE_POWER;

                leftMotorPower = isRobotTurning ?
                        (1 - MINIMUM_TURN_POWER) *
                                Math.pow(gamepad1.left_stick_y, 2) +
                                MINIMUM_TURN_POWER :
                        (1 - MINIMUM_DRIVE_POWER) * Math.pow(gamepad1
                                .left_stick_y, 2) +
                                MINIMUM_DRIVE_POWER;
            }

            if (gamepad1.right_stick_y == 0)
                rightMotorPower = 0;
            else if (gamepad1.right_stick_y < 0)
                rightMotorPower = rightMotorPower * -1;

            if (gamepad1.left_stick_y == 0)
                leftMotorPower = 0;
            else if (gamepad1.left_stick_y < 0)
                leftMotorPower = leftMotorPower * -1;

            if (gamepad1.start)
            {
                isDriveControlsInReverse = !isDriveControlsInReverse;
                while (gamepad1.start)
                {
                    idle();
                }
            }

            // Gunner Controls
            if (gamepad2.right_trigger > 0 && rightMotorPower == 0 && leftMotorPower == 0)
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

            if (hasCapBallGrabberBeenDeployed ? (gamepad2.dpad_up &&
                    rightMotorPower == 0 && leftMotorPower
                    == 0) : gamepad1.x && gamepad2.dpad_up)
            {
                hasCapBallGrabberBeenDeployed = true;
                liftCapBallGrabber();
            }

            if ((gamepad2.dpad_left || gamepad2.dpad_right) && hasCapBallGrabberBeenDeployed)
                clampCapBallGrabber();

            if (gamepad2.dpad_down)
                dropCapBallGrabber();

            if (gamepad2.b)
                turnButtonPusherRight();

            if (gamepad2.x)
                turnButtonPusherLeft();

            if (gamepad2.a || gamepad2.y)
                setButtonPusherToNeutral();

            setLiftPower(gamepad2.right_stick_y);

            if (isDriveControlsInReverse)
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
