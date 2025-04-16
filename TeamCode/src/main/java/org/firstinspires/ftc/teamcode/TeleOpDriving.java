package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class TeleOpDriving extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {

        // Declare our motors
        // Make sure your ID's match your configuration
        DcMotor frontLeftMotor = hardwareMap.dcMotor.get("frontLeftMotor");
        DcMotor backLeftMotor = hardwareMap.dcMotor.get("backLeftMotor");
        DcMotor frontRightMotor = hardwareMap.dcMotor.get("frontRightMotor");
        DcMotor backRightMotor = hardwareMap.dcMotor.get("backRightMotor");
        DcMotor leftSlide = hardwareMap.dcMotor.get("leftSlide");
        DcMotor rightSlide = hardwareMap.dcMotor.get("rightSlide");
        DcMotor leftHang = hardwareMap.dcMotor.get("leftHang");
        DcMotor rightHang = hardwareMap.dcMotor.get("rightHang");

        Servo leftExtension = hardwareMap.get(Servo.class, "servoName"); //Todo fix servo id
        Servo rightExtension = hardwareMap.get(Servo.class, "servoName"); //Todo fix servo id
        Servo extensionClaw = hardwareMap.get(Servo.class, "servoName"); //Todo fix servo id
        Servo verticalClaw = hardwareMap.get(Servo.class, "servoName"); //Todo fix servo id
        Servo extensionLeftPivot = hardwareMap.get(Servo.class, "servoName"); //Todo fix servo id
        Servo extensionRightPivot = hardwareMap.get(Servo.class, "servoName"); //Todo fix servo id
        Servo extensionPivotClaw = hardwareMap.get(Servo.class, "servoName"); //Todo fix servo id
        Servo verticalPivotClaw = hardwareMap.get(Servo.class, "servoName"); //Todo fix servo id
        Servo verticalClawAngle = hardwareMap.get(Servo.class, "servoName"); //Todo fix servo id
        Servo leftArm = hardwareMap.get(Servo.class, "servoName"); //Todo fix servo id
        Servo rightArm = hardwareMap.get(Servo.class, "servoName"); //Todo fix servo id
        Servo barTouch = hardwareMap.get(Servo.class, "servoName"); //Todo fix servo id

        String currentGrabPosition = "Wall";
        String grabSpecimen = "False";

        // Reverse the right side motors. This may be wrong for your setup.
        // If your robot moves backwards when commanded to go forwards,
        // reverse the left side instead.
        // See the note about this earlier on this page.
        frontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        frontLeftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        backLeftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        leftSlide.setDirection(DcMotorSimple.Direction.REVERSE);
        rightSlide.setDirection(DcMotorSimple.Direction.FORWARD);
        leftHang.setDirection(DcMotorSimple.Direction.REVERSE);
        rightHang.setDirection(DcMotorSimple.Direction.FORWARD);

        //Todo - Tune Values (Starting Position)
        leftExtension.setPosition(0); //Retracted
        rightExtension.setPosition(0); //Retracted
        extensionClaw.setPosition(0); //Closed
        extensionPivotClaw.setPosition(0); //Straight
        extensionLeftPivot.setPosition(0); //Faced Up
        extensionRightPivot.setPosition(0); //Faced Up
        leftArm.setPosition(0); //Transfer Prep Position
        rightArm.setPosition(0); //Transfer Prep Position
        verticalClawAngle.setPosition(0); //Faced Down
        verticalPivotClaw.setPosition(0); //Straight
        verticalClaw.setPosition(0); //Open

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            double y = -gamepad1.left_stick_y; // Remember, Y stick value is reversed
            double x = gamepad1.left_stick_x * 1.1; // Counteract imperfect strafing
            double rx = gamepad1.right_stick_x;

            // Denominator is the largest motor power (absolute value) or 1
            // This ensures all the powers maintain the same ratio,
            // but only if at least one is out of the range [-1, 1]
            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double frontLeftPower = (y + x + rx) / denominator;
            double backLeftPower = (y - x + rx) / denominator;
            double frontRightPower = (y - x - rx) / denominator;
            double backRightPower = (y + x - rx) / denominator;

            frontLeftMotor.setPower(frontLeftPower);
            backLeftMotor.setPower(backLeftPower);
            frontRightMotor.setPower(frontRightPower);
            backRightMotor.setPower(backRightPower);

            leftSlide.setPower(gamepad2.left_stick_y);
            rightSlide.setPower(gamepad2.left_stick_y);

            //Todo - Verify Values are Correct
            if (gamepad2.left_bumper){ //Release Winch
                leftHang.setPower(-1);
                rightHang.setPower(-1);
            } else if (gamepad2.right_bumper) { //Retract Winch
                leftHang.setPower(1);
                rightHang.setPower(1);
            } else { //Stationary Winch
                //Todo - Lock Motors for during match
                leftHang.setPower(0);
                rightHang.setPower(0);
            }

            if (gamepad1.dpad_up) {
                currentGrabPosition = "Submersible";
                leftExtension.setPosition(1); //Todo - Tune value (Horizontal Slides out to Sub)
                //rightExtension.setPosition(0); //Todo - Tune value (Horizontal Slides out to Sub)
                //extensionLeftPivot.setPosition(0.5); //Todo - Tune value (Sub Clear Position)
                //extensionRightPivot.setPosition(0.5); //Todo - Tune value (Sub Clear Position)
                //extensionPivotClaw.setPosition(0.5); //Todo - Tune value (Straight Position)
            }
            else if (gamepad1.dpad_down) {
                leftExtension.setPosition(0.5); //Todo - Tune value (Return Horizontal Slides to Robot)
                rightExtension.setPosition(0.5); //Todo - Tune value (Return Horizontal Slides to Robot)
                extensionLeftPivot.setPosition(0.5); //Todo - Tune value (Sub Clear Position)
                extensionRightPivot.setPosition(0.5); //Todo - Tune value (Sub Clear Position)
                extensionPivotClaw.setPosition(0.5); //Todo - Tune value (Straight Position)
            }

            if (gamepad1.dpad_left) {
                extensionLeftPivot.setPosition(1); //Todo - Tune value (Transfer Position)
                extensionRightPivot.setPosition(0); //Todo - Tune value (Transfer Position)
            }
            else if (gamepad1.dpad_right) {
                extensionLeftPivot.setPosition(0); //Todo - Tune value (Prep-Grab Position)
                extensionRightPivot.setPosition(1); //Todo - Tune value (Prep-Grab Position)
            }

            if (gamepad1.left_bumper) {
                extensionPivotClaw.setPosition(extensionPivotClaw.getPosition()+0.1); //Todo - Tune Value (45 degree turns)
            }
            else if (gamepad1.right_bumper) {
                extensionPivotClaw.setPosition(extensionPivotClaw.getPosition()-0.1); //Todo - Tune Value (45 degree turns)
            }

            if (gamepad1.a){ //Down and Grab
                if (currentGrabPosition == "Submersible") { //Down and Grab
                    extensionRightPivot.setPosition(1); //Todo - Tune Value (Grab Position)
                    extensionLeftPivot.setPosition(0); //Todo - Tune Value (Grab Position)
                    sleep(200); //Todo - Tune Value
                    extensionClaw.setPosition(1); //Todo - Tune Value (Close Claw)
                } else if (currentGrabPosition == "Wall") { //Grab off Wall
                    verticalClaw.setPosition(0); //Todo - Tune Value (Close Claw)
                    grabSpecimen = "True";
                }
            } else if (gamepad1.b){
                if (currentGrabPosition == "Submersible"){ //Return to Grabbing Position and drop block
                    extensionLeftPivot.setPosition(0); //Todo - Tune Value (Prep-Grab Position)
                    extensionRightPivot.setPosition(1); //Todo - Tune Value (Prep-Grab Position)
                    extensionClaw.setPosition(0); //Todo - Tune Value (Release Sample)
                } else if (currentGrabPosition == "Wall"){ //Open Claw
                    grabSpecimen = "False";
                    verticalClaw.setPosition(1); //Todo - Tune Value (Open Claw)
                }
            }

            if (gamepad1.y) { //Transfer and placing position for bucket
                currentGrabPosition = "Submersible";
                verticalPivotClaw.setPosition(0); //Todo - Tune Value (Prep Transfer)
                extensionPivotClaw.setPosition(0); //Todo - Tune Value (Prep Transfer)
                extensionLeftPivot.setPosition(1); //Todo - Tune value (Transfer Position)
                extensionRightPivot.setPosition(0); //Todo - Tune value (Transfer Position)
                sleep(200);
                leftArm.setPosition(1); //Todo - Tune Value (Transfer Position)
                rightArm.setPosition(0); //Todo - Tune Value (Transfer Position)
                sleep(200); // Todo - Tune Value
                verticalClaw.setPosition(0); //Todo - Tune Value (Grab Claw)
                extensionClaw.setPosition(0); //Todo - Tune Value (Release Claw)
                sleep(200); //Todo - Tune Value
                leftArm.setPosition(0); //Todo - Tune Value (Placing)
                rightArm.setPosition(1); //Todo - Tune Value (Placing)
                sleep(200);
                verticalClawAngle.setPosition(0); //Todo - Tune Value (Straight Up)
                verticalPivotClaw.setPosition(0); //Todo - Tune Value (Flip 180 degrees)
            }

            if (gamepad1.x) {
                if (grabSpecimen == "False") {
                    currentGrabPosition = "Wall";
                    leftArm.setPosition(0); //Todo - Tune Value (Grab off Wall)
                    rightArm.setPosition(0); //Todo - Tune Value (Grab off Wall)
                    verticalClawAngle.setPosition(0); //Todo - Tune Value (Grab off Wall)
                    verticalPivotClaw.setPosition(0); //Todo - Tune Value (Grab off Wall)
                } else if (grabSpecimen == "True"){
                    currentGrabPosition = "Wall";
                    leftArm.setPosition(0); //Todo - Tune Value (Vertical)
                    rightArm.setPosition(1); //Todo - Tune Value (Vertical)
                    verticalClawAngle.setPosition(0); //Todo - Tune Value (Face Clip Bar)
                    verticalPivotClaw.setPosition(1); //Todo - Tune Value (Flip 180 Degrees)
                }
            }

            }
        }
    }
