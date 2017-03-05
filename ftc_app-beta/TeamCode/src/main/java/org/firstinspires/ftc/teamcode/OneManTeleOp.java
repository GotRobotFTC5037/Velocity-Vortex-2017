package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "One Man TeleOp")
public class OneManTeleOp extends Archimedes
{
    @Override
    public void runOpMode() throws InterruptedException
    {
        double rightMotorPower;
        double leftMotorPower;

        boolean isDriveControlsInReverse = false;

        initializeArchimedes();
        waitForStart();
        startArchimedes();

        while (opModeIsActive())
        {
            boolean isRobotTurning = (getLeftDriveMotor().getPower() > 0 &&
                    getRightDriveMotor().getPower() <= 0) ||
                    (getRightDriveMotor().getPower() > 0 && getLeftDriveMotor
                            ().getPower() <= 0);

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
            if (gamepad1.right_trigger > 0 && rightMotorPower == 0 &&
                    leftMotorPower
                            == 0)
                liftBallDeployer();
            else
                dropBallDeployer();

            if (gamepad1.left_trigger > 0)
                startBallLauncherAtHighPower();
            else
                stopBallLauncher();

            if (gamepad1.left_bumper)
                startBallCollector();
            else if (gamepad1.right_bumper)
                reverseBallCollector();
            else
                stopBallCollector();

            if (gamepad1.dpad_up)
                setLiftPower(1);
            else if (gamepad1.dpad_down)
                setLiftPower(-1);
            else
                setLiftPower(0);

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
