package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp
public class Covenant_Quesobowl_TeleOp extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {

        // Declare our motors
        // Make sure your ID's match your configuration
        DcMotor frontLeftMotor = hardwareMap.dcMotor.get("frontLeft");
        DcMotor backLeftMotor = hardwareMap.dcMotor.get("backLeft");
        DcMotor frontRightMotor = hardwareMap.dcMotor.get("frontRight");
        DcMotor backRightMotor = hardwareMap.dcMotor.get("backRight");
        DcMotor leftSlide = hardwareMap.dcMotor.get("leftSlide");
        DcMotor rightSlide = hardwareMap.dcMotor.get("rightSlide");
        DcMotor leftHang = hardwareMap.dcMotor.get("lift1");
        DcMotor rightHang = hardwareMap.dcMotor.get("lift2");

        frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftHang.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightHang.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        Servo leftExtension = hardwareMap.get(Servo.class, "leftExtension");
        Servo rightExtension = hardwareMap.get(Servo.class, "rightExtension");
        Servo extensionClaw = hardwareMap.get(Servo.class, "extensionClaw");
        Servo verticalClaw = hardwareMap.get(Servo.class, "verticalClaw");
        Servo extensionLeftPivot = hardwareMap.get(Servo.class, "leftExtensionPivot");
        Servo extensionRightPivot = hardwareMap.get(Servo.class, "rightExtensionPivot");
        Servo extensionPivotClaw = hardwareMap.get(Servo.class, "extensionClawPivot");
        Servo verticalPivotClaw = hardwareMap.get(Servo.class, "verticalClawPivot");
        Servo verticalClawAngle = hardwareMap.get(Servo.class, "verticalClawAngle");
        Servo leftArm = hardwareMap.get(Servo.class, "leftArm");
        Servo rightArm = hardwareMap.get(Servo.class, "rightArm");
        Servo flag = hardwareMap.get(Servo.class, "flag");

        String currentGrabPosition = "slidesIn";
        String wallGrab = "True";
        String verticalGrab = "False";
        String wallCycling = "False";
        boolean lastLeftBumper = false;
        boolean lastRightBumper = false;
        ElapsedTime timer = new ElapsedTime();
        int step = 0;
        boolean isRunningSequence = false;

        frontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        frontLeftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        backLeftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        leftSlide.setDirection(DcMotorSimple.Direction.FORWARD);
        rightSlide.setDirection(DcMotorSimple.Direction.REVERSE);
        leftHang.setDirection(DcMotorSimple.Direction.REVERSE);
        rightHang.setDirection(DcMotorSimple.Direction.FORWARD);

        leftExtension.setPosition(0.3); //Retracted
        rightExtension.setPosition(1); //Retracted
        extensionClaw.setPosition(0.2); //Closed
        extensionPivotClaw.setPosition(0.73); //Straight
        extensionLeftPivot.setPosition(0); //Faced Up
        extensionRightPivot.setPosition(0.95); //Faced Up
        leftArm.setPosition(0.25); //Transfer Prep Position
        rightArm.setPosition(0.8); //Transfer Prep Position
        verticalClawAngle.setPosition(0.6); //Faced Down
        verticalPivotClaw.setPosition(0.25); //Straight
        verticalClaw.setPosition(0.65); //Open
        flag.setPosition(0.6); //Flyin

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            double y = -gamepad1.left_stick_y; // Remember, Y stick value is reversed
            double x = gamepad1.left_stick_x * 0.9;
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

            if (!isRunningSequence) {
                leftSlide.setPower(gamepad2.left_stick_y);
                rightSlide.setPower(gamepad2.left_stick_y);
            }

            if (gamepad1.dpad_up) {
                //Transfer Prep
                wallCycling = "False";
                leftArm.setPosition(0.25);
                rightArm.setPosition(0.8);
                verticalClaw.setPosition(0.65);
                verticalPivotClaw.setPosition(0.25);
                extensionPivotClaw.setPosition(0.73);
                extensionLeftPivot.setPosition(0);
                extensionRightPivot.setPosition(0.95);
                verticalClawAngle.setPosition(0.5);
                currentGrabPosition = "slidesOut";
                leftExtension.setPosition(0.6);
                rightExtension.setPosition(0.45);
                extensionLeftPivot.setPosition(0.5);
                extensionRightPivot.setPosition(0.6);
                extensionPivotClaw.setPosition(0.73);
            }
            else if (gamepad1.dpad_down) {
                //Transfer Prep
                wallCycling = "False";
                leftArm.setPosition(0.25);
                rightArm.setPosition(0.8);
                verticalClaw.setPosition(0.65);
                verticalPivotClaw.setPosition(0.25);
                extensionPivotClaw.setPosition(0.73);
                extensionLeftPivot.setPosition(0);
                extensionRightPivot.setPosition(0.95);
                verticalClawAngle.setPosition(0.5);
                currentGrabPosition = "slidesIn";
                leftExtension.setPosition(0.3);
                rightExtension.setPosition(1);
                extensionLeftPivot.setPosition(0.5);
                extensionRightPivot.setPosition(0.6);
                extensionPivotClaw.setPosition(0.73);
            }

            if (currentGrabPosition.equals("slidesIn")) {
                //Vertical Claw Grab
                if (gamepad1.a && verticalGrab.equals("False")) {
                    if (wallCycling.equals("True")){
                        verticalClaw.setPosition(0.85);
                        verticalGrab = "True";
                        long startTime = System.currentTimeMillis();
                        while (System.currentTimeMillis() - startTime < 250 && opModeIsActive()) {
                            // Could check gamepad or update telemetry here
                            idle();
                        }
                        //Go to Place on Bar
                        leftArm.setPosition(0.2);
                        rightArm.setPosition(0.85);
                        verticalClawAngle.setPosition(0.2);
                    }
                //Vertical Claw Release
                } else if (gamepad1.a && verticalGrab.equals("True"))
                    if (wallCycling.equals("True")) {
                    verticalClaw.setPosition(0.65);
                    wallCycling = "False";
                    verticalGrab = "False";
                    //Go to grab from Wall
                    leftArm.setPosition(0.55);
                    rightArm.setPosition(0.5);
                    verticalClawAngle.setPosition(0.25);
                    long startTime = System.currentTimeMillis();
                    while (System.currentTimeMillis() - startTime < 250 && opModeIsActive()) {
                        // Could check gamepad or update telemetry here
                        idle();
                    }
                    wallCycling = "True";
                    } else { //Wall Cycling = False
                    verticalClaw.setPosition(0.65);
                }

                if (gamepad2.y && !isRunningSequence) {
                    isRunningSequence = true;
                    step = 0;
                    timer.reset();
                }

                //Transfer and placing position for bucket
                if (isRunningSequence) {
                    switch (step) {
                        case 0:
                            // Set initial positions
                            verticalClaw.setPosition(0.65);
                            verticalPivotClaw.setPosition(0.25);
                            extensionPivotClaw.setPosition(0.73);
                            extensionLeftPivot.setPosition(0);
                            extensionRightPivot.setPosition(0.95);
                            verticalClawAngle.setPosition(0.57);
                            timer.reset();
                            step++;
                            break;

                        case 1:
                            if (timer.milliseconds() > 500) {
                                leftArm.setPosition(0.57);
                                rightArm.setPosition(0.47);
                                timer.reset();
                                step++;
                            }
                            break;

                        case 2:
                            if (timer.milliseconds() > 400) {
                                verticalClaw.setPosition(0.85);
                                extensionClaw.setPosition(0.2);
                                timer.reset();
                                step++;
                            }
                            break;

                        case 3:
                            if (timer.milliseconds() > 400) {
                                leftArm.setPosition(0.2);
                                rightArm.setPosition(0.85);
                                timer.reset();
                                step++;
                            }
                            break;

                        case 4:
                            if (timer.milliseconds() > 200) {
                                verticalClawAngle.setPosition(0.1);
                                verticalPivotClaw.setPosition(0.25);
                                verticalGrab = "True";
                                isRunningSequence = false;  // Done!
                            }
                            break;
                    }}

                if (gamepad2.left_bumper){ //Release Winch
                    leftHang.setPower(-1);
                    rightHang.setPower(-1);
                } else if (gamepad2.right_bumper) { //Retract Winch
                    leftHang.setPower(1);
                    rightHang.setPower(1);
                } else { //Stationary Winch
                    leftHang.setPower(0);
                    rightHang.setPower(0);
                }

                if (gamepad1.x && wallCycling.equals("False")){
                    wallCycling = "True";
                    verticalGrab = "False";
                    verticalClaw.setPosition(0.65);
                    leftArm.setPosition(0.6);
                    rightArm.setPosition(0.45);
                    verticalClawAngle.setPosition(0.2);
                }

            } else {
                //Pivot Horizontal Claw
                if (gamepad1.left_bumper && !lastLeftBumper) {
                    extensionPivotClaw.setPosition(Math.max(0.0, extensionPivotClaw.getPosition() - 0.2));
                }

                if (gamepad1.right_bumper && !lastRightBumper) {
                    extensionPivotClaw.setPosition(Math.min(1.0, extensionPivotClaw.getPosition() + 0.2));
                }

                lastLeftBumper = gamepad1.left_bumper;
                lastRightBumper = gamepad1.right_bumper;

                telemetry.addData("Servo Position", extensionPivotClaw.getPosition());
                telemetry.update(); //Telemetry for Testing

                //Grab Prep & Grab Positions for Horizontal Claw
                if (gamepad1.dpad_left) {
                    extensionLeftPivot.setPosition(0.65);
                    extensionRightPivot.setPosition(0.45);
                }
                else if (gamepad1.dpad_right) {
                    extensionLeftPivot.setPosition(0.75);
                    extensionRightPivot.setPosition(0.35);
                }

                //Horizontal Claw grab
                if (gamepad1.a) {
                    extensionClaw.setPosition(0.42);
                } else if (gamepad1.b){
                    extensionLeftPivot.setPosition(0.7);
                    extensionRightPivot.setPosition(0.4);
                    extensionClaw.setPosition(0.2);
                }
            }

            }
        }
    }
