package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Config
@Autonomous(name = "Covenant_Quesobowl_SpecimenAuto", group = "Autonomous")
public class Covenant_Quesobowl_SpecimenAuto extends LinearOpMode {
    public class verticalSlideExtension {
        private DcMotorEx leftSlide;
        private DcMotorEx rightSlide;

        public verticalSlideExtension(HardwareMap hardwareMap) {
            leftSlide = hardwareMap.get(DcMotorEx.class, "leftSlide");
            leftSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            leftSlide.setDirection(DcMotorSimple.Direction.FORWARD);
            rightSlide = hardwareMap.get(DcMotorEx.class, "rightSlide");
            rightSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            rightSlide.setDirection(DcMotorSimple.Direction.REVERSE);
        }
        public class slidesUp implements Action {
            private boolean initialized = false;

            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                if (!initialized) {
                    leftSlide.setPower(1);
                    rightSlide.setPower(1);
                    initialized = true;
                }

                double pos = leftSlide.getCurrentPosition() + rightSlide.getCurrentPosition();
                packet.put("liftPos", pos);
                //Todo - Tune Max Value for specimen place
                if (pos < 3000.0) {
                    return true;
                } else {
                    leftSlide.setPower(0);
                    rightSlide.setPower(0);
                    return false;
                }
            }
        }
        public Action SlidesUp() {
            return new slidesUp();
        }
        public class slidesDown implements Action {
            private boolean initialized = false;

            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                if (!initialized) {
                    leftSlide.setPower(-1);
                    rightSlide.setPower(-1);
                    initialized = true;
                }

                double pos = leftSlide.getCurrentPosition() + rightSlide.getCurrentPosition();
                packet.put("liftPos", pos);
                //Todo - Tune Min Value for slide retraction
                if (pos > 100.0) {
                    return true;
                } else {
                    leftSlide.setPower(0);
                    rightSlide.setPower(0);
                    return false;
                }
            }
        }
        public Action SlidesDown(){
            return new slidesDown();
        }
    }

    public class hangMotors {
        private DcMotorEx leftHang;
        private DcMotorEx rightHang;

        public hangMotors(HardwareMap hardwareMap) {
            leftHang = hardwareMap.get(DcMotorEx.class, "leftHang");
            leftHang.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            leftHang.setDirection(DcMotorSimple.Direction.FORWARD);
            rightHang = hardwareMap.get(DcMotorEx.class, "rightHang");
            rightHang.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            rightHang.setDirection(DcMotorSimple.Direction.REVERSE);
        }
        public class pull implements Action {
            private boolean initialized = false;

            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                if (!initialized) {
                    leftHang.setPower(1);
                    rightHang.setPower(1);
                    initialized = true;
                }

                double pos = leftHang.getCurrentPosition() + rightHang.getCurrentPosition();
                packet.put("liftPos", pos);
                //Todo - Tune Max Pull
                if (pos < 3000.0) {
                    return true;
                } else {
                    leftHang.setPower(0);
                    rightHang.setPower(0);
                    return false;
                }
            }
        }
        public Action Pull() {
            return new pull();
        }
        public class release implements Action {
            private boolean initialized = false;

            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                if (!initialized) {
                    leftHang.setPower(-1);
                    rightHang.setPower(-1);
                    initialized = true;
                }

                double pos = leftHang.getCurrentPosition() + rightHang.getCurrentPosition();
                packet.put("liftPos", pos);
                //Todo - Tune Release to touch ascent lvl 2
                if (pos > 100.0) {
                    return true;
                } else {
                    leftHang.setPower(0);
                    rightHang.setPower(0);
                    return false;
                }
            }
        }
        public Action Release(){
            return new release();
        }
    }

    public class verticalClaw {
        private Servo verticalClaw;

        public verticalClaw(HardwareMap hardwareMap) {
            verticalClaw = hardwareMap.get(Servo.class, "verticalClaw");
        }
        public class closeClaw implements Action {
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                verticalClaw.setPosition(0); //Todo - Tune Value (Closed Claw)
                return false;
            }
        }
        public Action CloseClaw() {
            return new closeClaw();
        }
        public class openClaw implements Action {
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                verticalClaw.setPosition(1); //Todo - Tune Value (Open Claw)
                return false;
            }
        }
        public Action OpenClaw() {
            return new openClaw();
        }
    }

    public class verticalPivotClaw {
        private Servo verticalPivotClaw;

        public verticalPivotClaw(HardwareMap hardwareMap) {
            verticalPivotClaw = hardwareMap.get(Servo.class, "verticalPivotClaw");
        }
        public class grabPos implements Action {
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                verticalPivotClaw.setPosition(0); //Grab Position //Todo - Tune Value
                return false;
            }
        }
        public Action GrabPos() {
            return new grabPos();
        }
        public class placingPos implements Action {
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                verticalPivotClaw.setPosition(1); //Placing Position (Flipped 180 degrees) //Todo - Tune Value
                return false;
            }
        }
        public Action Placingpos() {
            return new placingPos();
        }

    }
    public class extensionPivotClaw {
        private Servo extensionPivotClaw;

        public extensionPivotClaw(HardwareMap hardwareMap) {
            extensionPivotClaw = hardwareMap.get(Servo.class, "extensionPivotClaw");
        }
        public class straight implements Action {
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                extensionPivotClaw.setPosition(0.73);
                return false;
            }
        }
        public Action Straight() {
            return new straight();
        }

    }
    public class extensionClaw {
        private Servo extensionClaw;

        public extensionClaw(HardwareMap hardwareMap) {
            extensionClaw = hardwareMap.get(Servo.class, "extensionClaw");
        }
        public class open implements Action {
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                extensionClaw.setPosition(0.2);
                return false;
            }
        }
        public Action Open() {
            return new open();
        }
        public class close implements Action {
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                extensionClaw.setPosition(0.42);
                return false;
            }
        }
        public Action Close() {
            return new close();
        }
    }
    public class verticalClawAngle {
        private Servo verticalClawAngle;

        public verticalClawAngle(HardwareMap hardwareMap) {
            verticalClawAngle = hardwareMap.get(Servo.class, "verticalClawAngle");
        }
        public class grabPos implements Action {
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                verticalClawAngle.setPosition(0); //Grab Position //Todo - Tune Value
                return false;
            }
        }
        public Action GrabPos() {
            return new grabPos();
        }
        public class placingPos implements Action {
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                verticalClawAngle.setPosition(1); //Placing Position //Todo - Tune Value
                return false;
            }
        }
        public Action Placingpos() {
            return new placingPos();
        }
    }
    public class extensionPivot {
        private Servo extensionLeftPivot;
        private Servo extensionRightPivot;

        public extensionPivot(HardwareMap hardwareMap) {
            extensionLeftPivot = hardwareMap.get(Servo.class, "extensionLeftPivot");
            extensionRightPivot = hardwareMap.get(Servo.class, "extensionRightPivot");
        }
        public class grabPos implements Action {
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                extensionLeftPivot.setPosition(0.8);
                extensionRightPivot.setPosition(0.3);
                return false;
            }
        }
        public Action GrabPos() {
            return new grabPos();
        }
        public class slideOrTransfer implements Action {
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                extensionLeftPivot.setPosition(0);
                extensionRightPivot.setPosition(0.95);
                return false;
            }
        }
        public Action SlideOrTransfer() {
            return new slideOrTransfer();
        }
    }
    public class horizontalSlides {
        private Servo leftExtension;
        private Servo rightExtension;

        public horizontalSlides(HardwareMap hardwareMap) {
            leftExtension = hardwareMap.get(Servo.class, "leftExtension");
            rightExtension = hardwareMap.get(Servo.class, "rightExtension");
        }
        public class slidesOut implements Action {
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                leftExtension.setPosition(0.6);
                rightExtension.setPosition(0.45);
                return false;
            }
        }
        public Action SlidesOut() {
            return new slidesOut();
        }
        public class slidesReturn implements Action {
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                leftExtension.setPosition(0);
                rightExtension.setPosition(1);
                return false;
            }
        }
        public Action SlidesReturn() {
            return new slidesReturn();
        }
    }
    public class moveArm {
        private Servo leftArm;
        private Servo rightArm;

        public moveArm (HardwareMap hardwareMap) {
            leftArm = hardwareMap.get(Servo.class, "leftArm");
            rightArm = hardwareMap.get(Servo.class, "rightArm");
        }
        public class wallGrab implements Action {
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                leftArm.setPosition(0.6);
                rightArm.setPosition(0.45);
                return false;
            }
        }
        public Action WallGrab() {
            return new wallGrab();
        }
        public class placeSpecimen implements Action {
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                leftArm.setPosition(0.2);
                rightArm.setPosition(0.85);
                return false;
            }
        }
        public Action PlaceSpecimen() {
            return new placeSpecimen();
        }
    }

    public void runOpMode() {
        //Todo Line 226 could be the wrong pose,change based on starting position of path
        Pose2d initialPose = new Pose2d(11.8, 61.7, Math.toRadians(90));
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);
        hangMotors HangMotors = new hangMotors(hardwareMap); //Motors - Hang
        verticalSlideExtension VerticalSlideExtension = new verticalSlideExtension(hardwareMap); //Motors - Vertical
        verticalClaw VerticalClaw = new verticalClaw(hardwareMap); //Servo - Vertical
        verticalPivotClaw VerticalPivotClaw = new verticalPivotClaw(hardwareMap); //Servo - Vertical
        verticalClawAngle VerticalClawAngle = new verticalClawAngle(hardwareMap); //Servo - Vertical
        moveArm MoveArm = new moveArm(hardwareMap); //Servo - Vertical
        horizontalSlides HorizontalSlides = new horizontalSlides(hardwareMap); //Servo - Horizontal
        extensionPivotClaw ExtensionPivotClaw = new extensionPivotClaw(hardwareMap); //Servo - Horizontal
        extensionPivot ExtensionPivot = new extensionPivot(hardwareMap); //Servo - Horizontal
        extensionClaw ExtensionClaw = new extensionClaw(hardwareMap); //Servo - Horizontal

        TrajectoryActionBuilder wait = drive.actionBuilder(initialPose)
                //Wait timer
                .waitSeconds(0.25);

        TrajectoryActionBuilder travelToScore = drive.actionBuilder(initialPose) //Todo create travelToScore.build
                //Path #1
                .lineToYSplineHeading(33, Math.toRadians(0))
                .waitSeconds(2)
                .setTangent(Math.toRadians(90))
                .lineToY(48)
                .setTangent(Math.toRadians(0))
                .lineToX(32)
                .strafeTo(new Vector2d(44.5, 30))
                .turn(Math.toRadians(180))
                .lineToX(47.5)
                .waitSeconds(3);

        TrajectoryActionBuilder backAndRight = drive.actionBuilder(initialPose) //Todo create backAndRight.build
                //Path #2
                .lineToYSplineHeading(33, Math.toRadians(0))
                .waitSeconds(2)
                .setTangent(Math.toRadians(90))
                .lineToY(48)
                .setTangent(Math.toRadians(0))
                .lineToX(32)
                .strafeTo(new Vector2d(44.5, 30))
                .turn(Math.toRadians(180))
                .lineToX(47.5)
                .waitSeconds(3);

        TrajectoryActionBuilder push1 = drive.actionBuilder(initialPose) //Todo create push1.build
                //Path #3
                .lineToYSplineHeading(33, Math.toRadians(0))
                .waitSeconds(2)
                .setTangent(Math.toRadians(90))
                .lineToY(48)
                .setTangent(Math.toRadians(0))
                .lineToX(32)
                .strafeTo(new Vector2d(44.5, 30))
                .turn(Math.toRadians(180))
                .lineToX(47.5)
                .waitSeconds(3);

        TrajectoryActionBuilder prepPush2 = drive.actionBuilder(initialPose) //Todo create prepPush2.build
                //Path #4
                .lineToYSplineHeading(33, Math.toRadians(0))
                .waitSeconds(2)
                .setTangent(Math.toRadians(90))
                .lineToY(48)
                .setTangent(Math.toRadians(0))
                .lineToX(32)
                .strafeTo(new Vector2d(44.5, 30))
                .turn(Math.toRadians(180))
                .lineToX(47.5)
                .waitSeconds(3);

        TrajectoryActionBuilder push2 = drive.actionBuilder(initialPose) //Todo create pus2.build
                //Path #5
                .lineToYSplineHeading(33, Math.toRadians(0))
                .waitSeconds(2)
                .setTangent(Math.toRadians(90))
                .lineToY(48)
                .setTangent(Math.toRadians(0))
                .lineToX(32)
                .strafeTo(new Vector2d(44.5, 30))
                .turn(Math.toRadians(180))
                .lineToX(47.5)
                .waitSeconds(3);

        TrajectoryActionBuilder prepPush3 = drive.actionBuilder(initialPose) //Todo create prepPush3.build
                //Path #6
                .lineToYSplineHeading(33, Math.toRadians(0))
                .waitSeconds(2)
                .setTangent(Math.toRadians(90))
                .lineToY(48)
                .setTangent(Math.toRadians(0))
                .lineToX(32)
                .strafeTo(new Vector2d(44.5, 30))
                .turn(Math.toRadians(180))
                .lineToX(47.5)
                .waitSeconds(3);

        TrajectoryActionBuilder push3 = drive.actionBuilder(initialPose) //Todo create push3.build
                //Path #7
                .lineToYSplineHeading(33, Math.toRadians(0))
                .waitSeconds(2)
                .setTangent(Math.toRadians(90))
                .lineToY(48)
                .setTangent(Math.toRadians(0))
                .lineToX(32)
                .strafeTo(new Vector2d(44.5, 30))
                .turn(Math.toRadians(180))
                .lineToX(47.5)
                .waitSeconds(3);

        TrajectoryActionBuilder grabWall = drive.actionBuilder(initialPose) //Todo create grabWall.build
                //Path #8, #11, #14, #17
                .lineToYSplineHeading(33, Math.toRadians(0))
                .waitSeconds(2)
                .setTangent(Math.toRadians(90))
                .lineToY(48)
                .setTangent(Math.toRadians(0))
                .lineToX(32)
                .strafeTo(new Vector2d(44.5, 30))
                .turn(Math.toRadians(180))
                .lineToX(47.5)
                .waitSeconds(3);

        TrajectoryActionBuilder clipOnBar = drive.actionBuilder(initialPose) //Todo create clipOnBar.build
                //Path #9, #12, #15, #18
                .lineToYSplineHeading(33, Math.toRadians(0))
                .waitSeconds(2)
                .setTangent(Math.toRadians(90))
                .lineToY(48)
                .setTangent(Math.toRadians(0))
                .lineToX(32)
                .strafeTo(new Vector2d(44.5, 30))
                .turn(Math.toRadians(180))
                .lineToX(47.5)
                .waitSeconds(3);

        TrajectoryActionBuilder clearClipArea = drive.actionBuilder(initialPose) //Todo - create clearClipArea.build
                //Path #10, #13, #16
                .lineToYSplineHeading(33, Math.toRadians(0))
                .waitSeconds(2)
                .setTangent(Math.toRadians(90))
                .lineToY(48)
                .setTangent(Math.toRadians(0))
                .lineToX(32)
                .strafeTo(new Vector2d(44.5, 30))
                .turn(Math.toRadians(180))
                .lineToX(47.5)
                .waitSeconds(3);

        TrajectoryActionBuilder park = drive.actionBuilder(initialPose) //Todo - Create Park.build
                //Path #19
                .lineToYSplineHeading(33, Math.toRadians(0))
                .waitSeconds(2)
                .setTangent(Math.toRadians(90))
                .lineToY(48)
                .setTangent(Math.toRadians(0))
                .lineToX(32)
                .strafeTo(new Vector2d(44.5, 30))
                .turn(Math.toRadians(180))
                .lineToX(47.5)
                .waitSeconds(3);

        //Closes claw upon init
        Actions.runBlocking(VerticalClaw.CloseClaw());

        while (!isStopRequested() && !opModeIsActive()) {
            telemetry.addData("Position during Init", "Test Telemetry Working");
            telemetry.update();
        }

        waitForStart();

        if (isStopRequested()) return;

        Actions.runBlocking(
                new SequentialAction(
                        //Specimen #1 ->
                        travelToScore.build(),
                        VerticalSlideExtension.SlidesUp(),
                        MoveArm.PlaceSpecimen(),
                        VerticalPivotClaw.Placingpos(),
                        VerticalClawAngle.Placingpos(),
                        VerticalClaw.OpenClaw(),
                        wait.build(),
                        backAndRight.build(),
                        HorizontalSlides.SlidesOut(),
                        ExtensionPivot.SlideOrTransfer(),
                        push1.build(),
                        prepPush2.build(),
                        push2.build(),
                        prepPush3.build(),
                        push3.build(),
                        HorizontalSlides.SlidesReturn(),
                        //Specimen #2 ->
                        MoveArm.WallGrab(),
                        VerticalPivotClaw.GrabPos(),
                        VerticalClawAngle.GrabPos(),
                        grabWall.build(),
                        VerticalClaw.CloseClaw(),
                        wait.build(),
                        MoveArm.PlaceSpecimen(),
                        VerticalPivotClaw.Placingpos(),
                        VerticalClawAngle.Placingpos(),
                        clipOnBar.build(),
                        clearClipArea.build(),
                        VerticalClaw.OpenClaw(),
                        wait.build(),
                        //Specimen #3 ->
                        MoveArm.WallGrab(),
                        VerticalPivotClaw.GrabPos(),
                        VerticalClawAngle.GrabPos(),
                        grabWall.build(),
                        VerticalClaw.CloseClaw(),
                        wait.build(),
                        MoveArm.PlaceSpecimen(),
                        VerticalPivotClaw.Placingpos(),
                        VerticalClawAngle.Placingpos(),
                        clipOnBar.build(),
                        clearClipArea.build(),
                        VerticalClaw.OpenClaw(),
                        wait.build(),
                        //Specimen #4 ->
                        MoveArm.WallGrab(),
                        VerticalPivotClaw.GrabPos(),
                        VerticalClawAngle.GrabPos(),
                        grabWall.build(),
                        VerticalClaw.CloseClaw(),
                        wait.build(),
                        MoveArm.PlaceSpecimen(),
                        VerticalPivotClaw.Placingpos(),
                        VerticalClawAngle.Placingpos(),
                        clipOnBar.build(),
                        clearClipArea.build(),
                        VerticalClaw.OpenClaw(),
                        wait.build(),
                        //Specimen #5 ->
                        MoveArm.WallGrab(),
                        VerticalPivotClaw.GrabPos(),
                        VerticalClawAngle.GrabPos(),
                        grabWall.build(),
                        VerticalClaw.CloseClaw(),
                        wait.build(),
                        MoveArm.PlaceSpecimen(),
                        VerticalPivotClaw.Placingpos(),
                        VerticalClawAngle.Placingpos(),
                        clipOnBar.build(),
                        VerticalClaw.OpenClaw(),
                        wait.build(),
                        park.build()
                )
        );
    }
}