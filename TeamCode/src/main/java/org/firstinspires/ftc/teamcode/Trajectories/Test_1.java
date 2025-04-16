package org.firstinspires.ftc.teamcode.Trajectories;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Trajectory;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.MecanumDrive;

public class Test_1 extends LinearOpMode {
    @Override
    public void runOpMode() {
        Pose2d initialPose = new Pose2d(5,5,5);
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);

        TrajectoryActionBuilder tab1 = drive.actionBuilder(initialPose)
                .lineToYSplineHeading(33, Math.toRadians(0))
                .waitSeconds(3);
//
//        Trajectory myTrajectory = drive.actionBuilder(new Pose2d(25, 25, Math.toRadians(90)))
//                .strafeTo(5)
//                .forward(5)
//                .build();
//
//        waitForStart();
//
//        if(isStopRequested()) return;
//
//        drive.FollowTrajectoryAction(myTrajectory);
    }
}
