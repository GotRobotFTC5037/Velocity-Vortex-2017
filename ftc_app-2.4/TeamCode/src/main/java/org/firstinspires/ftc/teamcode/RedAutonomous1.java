package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Red Autonomous 1", group = "Red")
public class RedAutonomous1 extends Archimedes
{
	@Override
	public void runOpMode() throws InterruptedException
	{
		initializeArchimedes();
		waitForGyroCalibration();
		waitForStart();

		if (opModeIsActive())
		{
			telemetry.addData(">", "Archimedes running...");
			telemetry.update();

			// Launch balls into center vortex.
			startBallLauncherForAutonomous();
			drive(1, 300);
			sleep(1500);
			launchBall(1000);
			sleep(1500);
			launchBall(1000);
			stopBallLauncher();

			// Go towards the beacon line and turn onto it.
			turn(DEFAULT_TURN_SPEED, -41);
			// drive(1, 1000);
			driveToLine(0.15, 1300, 0.15, 75);
			turn(0.5, -49);

			// Follow the line up to the beacon, detect the color and press
			// the right button.
			turnButtonPusherLeft();
			followBeaconLineToWall(0.2, 300, 10);
			sleep(500);
			if(isDetectingRedOnRight())
				turnButtonPusherRight();
			sleep(750);
			timeDrive(.5, 500);
			drive(1, -125);

			// As a safety feature, check to see the color of the beacon, if it
			// is blue, wait 5 seconds and press the beacon again.
			turnButtonPusherLeft();
			sleep(500);
			if(isDetectingBlueOnRight())
			{
				setButtonPusherToNeutral();
				sleep(5000);
				timeDrive(.5, 750);
				drive(1, -125);
				stop();
			}

			/*
			turn(1, 90);
			drive(DEFAULT_DRIVE_SPEED, 1000);
			driveToLine(0.15, 200, 0.15, 75);

			turn(.5, -90);
			turnButtonPusherLeft();
			followBeaconLineToWall(0.25, 300, 10);
 			sleep(500);
			if(isDetectingRedOnRight())
			{
				turnButtonPusherRight();
			}
			sleep(750);
			timeDrive(DEFAULT_DRIVE_SPEED, 550);
			timeDrive(-DEFAULT_DRIVE_SPEED, 500);*/
		}
	}
}
