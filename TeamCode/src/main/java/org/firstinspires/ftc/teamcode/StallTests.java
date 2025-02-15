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

@Autonomous(name = "StallTests", group = "Autonomous")
public class StallTests extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        VerticalSlide verticalSlide = new VerticalSlide(hardwareMap);
        HorizontalSlide horizontalSlide = new HorizontalSlide(hardwareMap);

        // try 9, -64, 90 (MeepMeep StartingPos)
        //new Pose2d(7, -64, Math.toRadians(90));
        Pose2d initialPose = new Pose2d(9, -64, Math.toRadians(90)); //y used to be 64 new Pose2d(10, -53, Math.toRadians(90)
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);

        TrajectoryActionBuilder ToScore1 = drive.actionBuilder(initialPose)
                //to sub and score1, x:9
                .lineToY(-31); //used to be -32

        TrajectoryActionBuilder BotBack = ToScore1.endTrajectory().fresh() //why does it say "BotToSub" and not "drive"?
                .lineToY(-36);

        TrajectoryActionBuilder ToPickUp2 = BotBack.endTrajectory().fresh()
                .setTangent(Math.toRadians(0)) //0,5, 10
                //spline to zone pickup2
                .splineToLinearHeading(new Pose2d(36, -56, Math.toRadians(-45)), Math.toRadians(315)); //40, 52

        TrajectoryActionBuilder Score2 = ToPickUp2.endTrajectory().fresh()
                //spline to sub score2, x:7 //experiment with math to radians 90 in real time!!!!
                .setTangent(Math.toRadians(125)) // BEST is tan = 125 //below used to be 9,-34 //y=-32
                .splineToLinearHeading(new Pose2d(7, -31, Math.toRadians(90)), Math.toRadians(90)); //good = tangent 145 or 130, current is 90

//        TrajectoryActionBuilder  BackRightSpline = Score2.endTrajectory().fresh()
//                //First is for turning==favorable!
//                .setTangent(Math.toRadians(-45)) // -45
//                .splineToConstantHeading(new Vector2d(50,-44), Math.PI / 2); // right-ish //y=-43

        TrajectoryActionBuilder BackRightSpline = Score2.endTrajectory().fresh()
                // back and right fast
                .setTangent(Math.toRadians(-45)) // -45 // x used to be 38 for both
                .splineToConstantHeading(new Vector2d(34, -36), Math.PI / 2) // right-ish
                .splineToConstantHeading(new Vector2d(34, -14), Math.PI / 2) // x 35 y -36 up-ish

                .setTangent(Math.toRadians(270))
                //forward
                .lineToY(-14)
                .setTangent(Math.toRadians(180)) //resetting tangent so it doesn't mess with x axis---done multiple times for x and y
                //right
                .lineToX(47)
                .setTangent(Math.toRadians(270))
                //down push
                .lineToY(-57);

        telemetry.addData("VSClaw Pos", verticalSlide.getPosition());

        waitForStart();

        if (isStopRequested()) return;

        Actions.runBlocking(new SequentialAction(
                //Servos individually, sequentially, parallel
//                verticalSlide.vsClawOpen(),
//                verticalSlide.vsClawClose(),
//                verticalSlide.vsPivotUp(),
//                verticalSlide.vsPivotDown(),
//
//                horizontalSlide.hsClawOpen(),
//                horizontalSlide.hsClawClose(),
//                horizontalSlide.hsPivotDown(),
//                horizontalSlide.hsPivotUp(),
//
//                //Motors individually, sequentially, parallel
//                verticalSlide.verticalUp(),
//                verticalSlide.verticalClick(),
//                verticalSlide.verticalDown(),
//                verticalSlide.verticalBasket(),
//
//                horizontalSlide.horizontalOut(),
//                horizontalSlide.horizontalIn(),
//                horizontalSlide.horizontalOutFar(),
//
//                //Servo+Motor sequentially, parallel (make sure parallel won't break it)
//                horizontalSlide.horizontalOut(),
//                horizontalSlide.hsClawOpen(),
//
//                //Motor+Servo sequentially, parallel
//                horizontalSlide.hsClawClose(),
//                horizontalSlide.horizontalIn(),

                //Next do drive stall test
                //Is there a long stall between movements?
                //Are the positions accurate?
                //How fast can the drive train motors be sped up or other constants be changed?
                //Also consider building large movements and putting in markers? Do more research on trajectories!

                ToScore1.build(),
                BotBack.build(),
                ToPickUp2.build(),
                Score2.build()
                //there are more

                //optional--test drive,servo, and motor combinations
                //test transfer from the slide classes, from common once, multiple times, after movements
//                Common.PickUp(horizontalSlide, verticalSlide),
//                Common.Transfer(horizontalSlide, verticalSlide)

                )
        );
    }
}
//                new ParallelAction(
//                        ToScore1.build(),
//                        verticalSlide.verticalUp()
//                ),
//
//                verticalSlide.verticalClick(),
//                verticalSlide.vsClawOpen(),
//                new SleepAction(0.5), // can this time be shortened?
//
//                BotBack.build(),
//
//                new ParallelAction (
//                        verticalSlide.verticalDown(),
//                        verticalSlide.vsPivotDown(),
//                        ToPickUp2.build()
//                ),
//
//                Common.PickUp(horizontalSlide, verticalSlide),
//                //new SleepAction(1), //most likely not necessary
//
//                        //new ParallelAction (
//                        new SequentialAction (
//                        Common.Transfer(horizontalSlide, verticalSlide),
//                                new SleepAction(0.2),
//                        verticalSlide.verticalUp(),
//                        horizontalSlide.hsClawClose()
//                        ),
//                        //new SleepAction(0.2) // add .2? used to be 1
//                        //),
//                        Score2.build(),
//                        verticalSlide.verticalClick(),
//
//                        verticalSlide.vsClawOpen(),
//
//                        BackRightSpline.build(),
//
//                        new ParallelAction (
//                        verticalSlide.verticalDown(),
//                        verticalSlide.vsPivotDown(),
//                        horizontalSlide.hsClawOpen()
//                        )
//                )
//        );
//
//
//    }
//}
//
//                        horizontalSlide.horizontalOutFar(),
//                        horizontalSlide.hsClawOpen(),
//                        //HSPivot Down
//                        horizontalSlide.hsPivotDown(),
//                        new SleepAction(0.5),
//                        //HSClaw Close
//                        horizontalSlide.hsClawClose(),
//                        new SleepAction(0.5)

//                        horizontalSlide.hsPivotDown(),
//                        new SleepAction(0.2),
//                        horizontalSlide.hsClawClose()


//                //Transfer NOTE: Theoretically no waits are needed - TEST
//                new SequentialAction(
//                        //prerequisites
//                // move VS Pivot down (already done)
//                //verticalSlide.vsPivotDown(),
//                // open VS Claw (already done)
//                //verticalSlide.vsClawOpen(),
//
//                        //PREP FOR AUTO ONLY
//                //HSlide Out (find amount)
//                        //put action here
//                //HSPivot Down
//                horizontalSlide.hsPivotDown(),
//                //HSClaw Close
//                horizontalSlide.hsClawClose(),
//                        //PREP FINISHED
//
//                // HSPivot Up
//                horizontalSlide.hsPivotUp(),
//                // HSlide in
//                new SleepAction(0.5),
//                horizontalSlide.horizontalIn(),
//                //VS Claw close
//                //sleep(1500); // REPLACE LATER FOR CONDITION IF touch sensor IS PRESSED
//                verticalSlide.vsClawClose(),
//                new SleepAction(0.5),
//                //HS Claw open
//                horizontalSlide.hsClawOpen(),
//                new SleepAction(0.1),
//                //VS Pivot up
//                verticalSlide.vsPivotUp()
//                )
                //bot pushes in all 3 samples
                //bot transfer, spline to, score, spline back, repeat
