package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Robot
{
	final double GYRO_DRIVE_COEFFICIENT = 0.015;
	final double TURN_COEFFICIENT = 0.15;
	final double DEFAULT_DRIVE_SPEED = .75;
	final double DEFAULT_TURN_SPEED = 0.6;
	final double MINIMUM_SPEED = .27;

	final int WALL_BUFFER = 500;

	private final int ENCODER_UNITS_PER_REVOLUTION = 1440;
	private final double ENCODER_WHEEL_DIAMETER = 50.2;
	final double ENCODER_UNITS_PER_MILLIMETER = (ENCODER_UNITS_PER_REVOLUTION / (ENCODER_WHEEL_DIAMETER * Math.PI));

	DcMotor leftMotor;
	DcMotor rightMotor;
	DcMotor ballLauncher;
	DcMotor ballCollector;
	Servo ballDeployer;
	GyroSensor gyroSensor;

//	VuforiaTrackables beacons;

	public void init(HardwareMap hardwareMap)
	{
		leftMotor = hardwareMap.dcMotor.get("left motor");
		leftMotor.setDirection(DcMotor.Direction.REVERSE);
		leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

		rightMotor = hardwareMap.dcMotor.get("right motor");
		rightMotor.setDirection(DcMotor.Direction.FORWARD);
		rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

		ballLauncher = hardwareMap.dcMotor.get("ball launcher");
		ballLauncher.setDirection(DcMotor.Direction.REVERSE);
		ballLauncher.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
		ballLauncher.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

		ballCollector = hardwareMap.dcMotor.get("ball collector");
		ballCollector.setDirection(DcMotor.Direction.REVERSE);
		ballCollector.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

		ballDeployer = hardwareMap.servo.get("ball deployer");
		ballDeployer.setDirection(Servo.Direction.FORWARD);
		ballDeployer.setPosition(1);

//		VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(R.id.cameraMonitorViewId);
//		parameters.vuforiaLicenseKey = "AVjSL4f/////AAAAGRrqOmjjwUvcra1uL+pn/W8AoLn03Yj7g6Aw+VGRAI+CkzXWFw/7FLW09TYRSzxCcQmlWovvlsq9k4DqqxDr+bnAVhsmk+MNEzKyMBqkwMM6BGjEL6ohtkGbMiE+sYL0aWgZ+ULu6pPJZQboiH/sEcH2jq8o5zAVe3lbP9E34gCELlHAIzgEta7lXabdjC86OixIDZbdEBpE5UTGPRFKTbYgKFVNoouFgUT4hs5MiqD21DwbubgmSe+WOVyi3G4WTkJowT9jx1XlOrUXwc6kfyArQ+DFQNXEghwAXhC9FOEWijQTKSG+TDq7XePfRICqLPEdl4aYUixHn6OCCPZ85o7bEaBYf74ZddKg7IBTCOsg";
//		parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
//		parameters.cameraMonitorFeedback = VuforiaLocalizer.Parameters.CameraMonitorFeedback.AXES;
//		Vuforia.setHint(HINT.HINT_MAX_SIMULTANEOUS_IMAGE_TARGETS, 1);
//		VuforiaLocalizer vuforia = ClassFactory.createVuforiaLocalizer(parameters);

//		beacons = vuforia.loadTrackablesFromAsset("FTC_2016-17");
//		beacons.get(0).setName("Wheels");
//		beacons.get(1).setName("Tools");
//		beacons.get(2).setName("Lego");
//		beacons.get(3).setName("Gears");
//		beacons.activate();

		gyroSensor = hardwareMap.gyroSensor.get("gyro");
		gyroSensor.calibrate();
	}

	DcMotor getEncoderWheel()
	{
		return rightMotor;
	}
}
