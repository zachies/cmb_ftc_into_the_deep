package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Actions.Common;
import org.firstinspires.ftc.teamcode.Actions.HorizontalSlide;
import org.firstinspires.ftc.teamcode.Actions.VerticalSlide;

@Autonomous(name = "AutonomousYellow", group = "Autonomous")
public class AutonomousYellow extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        VerticalSlide verticalSlide = new VerticalSlide(hardwareMap);
        HorizontalSlide horizontalSlide = new HorizontalSlide(hardwareMap);

        Pose2d initialPose = new Pose2d(-31, -64, Math.toRadians(180)); //y used to be 64 new Pose2d(10, -53, Math.toRadians(90)
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);


        TrajectoryActionBuilder ToScore1 = drive.actionBuilder(initialPose)
//                //to scorePrep1
//                .lineToX(-48)
                //to score1
                .lineToX(-57);

        TrajectoryActionBuilder BotBack = ToScore1.endTrajectory().fresh()
                //back up
                .lineToX(-35); //-48

        TrajectoryActionBuilder BotToSub = BotBack.endTrajectory().fresh()
                //to sub strafe up
                .strafeTo(new Vector2d(-35, -10))
                //to sub backup
                .setTangent(Math.toRadians(180))
                .lineToX(-22);

        TrajectoryActionBuilder BackUp = ToScore1.endTrajectory().fresh()
                //back up
                .lineToX(-46); //-48

        TrajectoryActionBuilder PickUp2 = BackUp.endTrajectory().fresh()
                //to pickup2
                // other option
                //.strafeTo(new Vector2d(-40, -52))
                //.splineToLinearHeading(new Pose2d(-46, -40, Math.toRadians(90)), Math.toRadians(90));
                .strafeToLinearHeading(new Vector2d(-46, -40), Math.toRadians(90)); //y=-40, -37 //used -46, -40 heading = 85, 90

        TrajectoryActionBuilder ToScore2Prep = PickUp2.endTrajectory().fresh()
                //to scorePrep2
                // other option .splineToLinearHeading(new Pose2d(-55, -53, Math.toRadians(225)), Math.toRadians(225)) //-53, -55
                .strafeToLinearHeading(new Vector2d(-55, -53), Math.toRadians(225));

        TrajectoryActionBuilder ToScore2 = ToScore2Prep.endTrajectory().fresh()
                //to score2
                .strafeTo(new Vector2d(-58, -59)); //used to be x -58, y -56

        TrajectoryActionBuilder BackUp2 = ToScore2.endTrajectory().fresh()
                //back up
                .splineToLinearHeading(new Pose2d(-55, -53, Math.toRadians(225)), Math.toRadians(225));

        //.strafeTo(new Vector2d(-35, -10))?
        //to sub
        // .strafeToLinearHeading(new Vector2d(-35, -10), Math.toRadians(180))
        //.setTangent(Math.toRadians(180))
        //.lineToX(-22);

        TrajectoryActionBuilder SquareUp = ToScore2.endTrajectory().fresh()
                .splineToLinearHeading(new Pose2d(-55, -53, Math.toRadians(225)), Math.toRadians(225))
                .turnTo(Math.toRadians(90));


        telemetry.addData("VSClaw Pos", verticalSlide.getPosition());

        waitForStart();

        if(isStopRequested()) return;

        Actions.runBlocking(new SequentialAction(
                        //verticalSlide.verticalBasket(),
                        ToScore1.build(),
                        //verticalSlide.vsClawOpen(),
                        //new SleepAction(0.1), // can this time be shortened?

//                BotBack.build(),
//
//                new ParallelAction (
//                verticalSlide.vsPivotDown(),
//                verticalSlide.vsClawClose(),
//                verticalSlide.verticalDown(),
//                BotToSub.build()

                        //alternate option instead of and after Bot Back
                        BackUp.build(),
                        new ParallelAction (
                                //horizontalSlide.hsClawClose(),
                                //verticalSlide.vsPivotDown(),
                                //verticalSlide.vsClawOpen(), //already happened?
                                //verticalSlide.verticalDown(),
                                PickUp2.build()
                        ),


//                new SequentialAction(
//                        //move VSPivot down (already done)
//                        verticalSlide.vsPivotDown(),
//                        //open VSClaw (already done)
//                        verticalSlide.vsClawOpen(),
//                        //HSClaw open
//                        horizontalSlide.hsClawOpen(),
//
//                        // HSlide Out
//                        horizontalSlide.horizontalOut(),
//                        //HSPivot Down
//                        horizontalSlide.hsPivotDown(),
//                        new SleepAction(0.5),
//                        //HSClaw Close
//                        horizontalSlide.hsClawClose(),
//                        new SleepAction(0.5)
//                );

                        // HSlide Out
//                        horizontalSlide.horizontalOut(),
//                        horizontalSlide.hsClawOpen(),
//                        //HSPivot Down
//                        horizontalSlide.hsPivotDown(),
//                        new SleepAction(0.5),
//                        //HSClaw Close
//                        horizontalSlide.hsClawClose(),
//                        new SleepAction(0.5),
//
//                        //common
//                        new SequentialAction(
//                                // HSPivot Up
//                                horizontalSlide.hsPivotUp(),
//                                // HSlide in
//                                new SleepAction(0.5),
//                                horizontalSlide.horizontalIn(),
//                                //VS Claw close
//                                verticalSlide.vsClawClose(),
//                                new SleepAction(0.2), //THIS
//                                //HS Claw open
//                                horizontalSlide.hsClawOpen(),
//                                new SleepAction(0.1),
//                                //VS Pivot up
//                                verticalSlide.vsPivotUp(),
//                                new SleepAction(0.1)
//                        ),

                        // new SleepAction(0.2), //is it necessary?

                        new ParallelAction (
                                //verticalSlide.verticalBasket(),
                                //horizontalSlide.hsClawClose(),
                                ToScore2Prep.build()
                        ),

                        ToScore2.build(),

                        //verticalSlide.vsClawOpen(),
                        BackUp2.build(),
                        //verticalSlide.verticalDown(),
                        //verticalSlide.vsPivotDown(),

                        new ParallelAction (
                                SquareUp.build()
                                //for init not to backfire
//                                horizontalSlide.hsClawOpen(),
//                                verticalSlide.vsPivotDown(),
//                                verticalSlide.vsClawOpen()
                        )

                )
        );


    }
}