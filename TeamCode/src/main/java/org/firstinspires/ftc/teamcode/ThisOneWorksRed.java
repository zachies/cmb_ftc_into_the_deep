package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Actions.Common;

@Autonomous (name = "ThisOneWorksRed")
public class ThisOneWorksRed extends LinearOpMode {
    private DcMotorEx horizontal;
    private Servo HSClaw;
    private Servo HSPivot;
    private DcMotorEx vertical;
    private Servo VSClaw;
    private Servo VSPivot;

    @Override
    public void runOpMode() throws InterruptedException {

        Pose2d initialPose = new Pose2d(9, -64, Math.toRadians(90)); //y used to be 64 new Pose2d(10, -53, Math.toRadians(90)
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);

        TrajectoryActionBuilder ToScore1 = drive.actionBuilder(initialPose)
                //to sub and score1, x:9
                .lineToY(-31); //used to be -32

        TrajectoryActionBuilder BotBack = ToScore1.endTrajectory().fresh() //why does it say "BotToSub" and not "drive"?
                .lineToY(-36);

        TrajectoryActionBuilder ToPickUp2 = BotBack.endTrajectory().fresh()
//                .setTangent(Math.toRadians(0)) //0,5, 10
//                //spline to zone pickup2
//                .splineToLinearHeading(new Pose2d(36, -56, Math.toRadians(-45)), Math.toRadians(315)); //40, 52
        .setTangent(Math.toRadians(0))
                .strafeToLinearHeading(new Vector2d(36,-56), Math.toRadians(-45));

        TrajectoryActionBuilder  Score2 = ToPickUp2.endTrajectory().fresh()
                //spline to sub score2, x:7 //experiment with math to radians 90 in real time!!!!
                .setTangent(Math.toRadians(125)) // BEST is tan = 125 //below used to be 9,-34 //y=-32
                .splineToLinearHeading(new Pose2d(7, -31, Math.toRadians(90)), Math.toRadians(90)); //good = tangent 145 or 130, current is 90

        TrajectoryActionBuilder  BotBack2 = Score2.endTrajectory().fresh()
                .lineToY(-36);

        TrajectoryActionBuilder  Grab1 = BotBack2.endTrajectory().fresh()
                .strafeToLinearHeading(new Vector2d(52,-44), Math.toRadians(90));

        TrajectoryActionBuilder  Turn1 = Grab1.endTrajectory().fresh()
                .turnTo(Math.toRadians(270));

        TrajectoryActionBuilder  Score3 = Turn1.endTrajectory().fresh()
                .setTangent(Math.toRadians(125))
                //spline to sub score3
                .splineToLinearHeading(new Pose2d(5, -31, Math.toRadians(90)), Math.toRadians(90));

        TrajectoryActionBuilder BotBack3 = ToScore1.endTrajectory().fresh() //why does it say "BotToSub" and not "drive"?
                .lineToY(-36);

        TrajectoryActionBuilder Park = BotBack3.endTrajectory().fresh()
                .strafeTo(new Vector2d(45,-60), new TranslationalVelConstraint(70.0));

         //-45
        TrajectoryActionBuilder  Grab2 = Turn1.endTrajectory().fresh()
                .turnTo(Math.toRadians(180))
                .turnTo(Math.toRadians(55));


//        TrajectoryActionBuilder  BackRightSpline = Score2.endTrajectory().fresh()
//                //First is for turning==favorable!
//                .setTangent(Math.toRadians(-45)) // -45
//                .splineToConstantHeading(new Vector2d(50,-44), Math.PI / 2); // right-ish //y=-43

//        TrajectoryActionBuilder  BackRightSpline = Score2.endTrajectory().fresh()
//                // back and right fast
//                .setTangent(Math.toRadians(-45)) // -45 // x used to be 38 for both
//                .splineToConstantHeading(new Vector2d(34,-36), Math.PI / 2) // right-ish
//                .splineToConstantHeading(new Vector2d(34,-14), Math.PI / 2) // x 35 y -36 up-ish
//
//                .setTangent(Math.toRadians(270))
//                //forward
//                .lineToY(-14)
//                .setTangent(Math.toRadians(180)) //resetting tangent so it doesn't mess with x axis---done multiple times for x and y
//                //right
//                .lineToX(47)
//                .setTangent(Math.toRadians(270))
//                //down push
//                .lineToY(-57);

        horizontal = hardwareMap.get(DcMotorEx.class, "horizontal");
        horizontal.setDirection(DcMotor.Direction.REVERSE);
        horizontal.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        horizontal.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        horizontal.setTargetPosition(0);
        horizontal.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        vertical = hardwareMap.get(DcMotorEx.class, "vertical");
        vertical.setDirection(DcMotor.Direction.REVERSE);
        vertical.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        vertical.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        vertical.setTargetPosition(0);
        vertical.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        HSClaw = hardwareMap.get(Servo.class, "HSlideClaw");
        HSPivot = hardwareMap.get(Servo.class, "HSlidePivot");
        VSClaw = hardwareMap.get(Servo.class, "VSlideClaw");
        VSPivot = hardwareMap.get(Servo.class, "VSlidePivot");

        HSClaw.setPosition(0); //init close
        HSPivot.setPosition(0); //init up

        VSClaw.setPosition(0); //init close
        VSPivot.setPosition(0.65); //init up

        waitForStart();

       if(isStopRequested()) return;

        Actions.runBlocking(new SequentialAction(
//        //Pick Up and prep
//                    new ParallelAction(
//                    vsPivotDown(),
//                    vsClawOpen(),
//                    hsClawOpen(),
//                    horizontalOutFar()
//                    ),
//
//                    hsPivotDown(),
//                    hsClawClose(),
//                    hsPivotUp(),
//
//        //Transfer (can be done while moving)
//                    horizontalIn(),
//                    vsClawClose(),
//                    hsClawOpen(),
//                    vsPivotUp(),

//this is old code copied
        new ParallelAction(
                vsClawClose(), //already init here
                ToScore1.build(),
                verticalUp()
        ),
               //vsClawClose(),
               verticalClick(),
               vsClawOpen(),
               //new SleepAction(2), // can this time be shortened?

                BotBack.build(),

                new ParallelAction (
                       verticalDown(),
                       vsPivotDown(),
                       ToPickUp2.build()
                ),

                    //Pick Up and prep
                    new ParallelAction(
                    //vsPivotDown(), //already done
                    //vsClawOpen(), //same
                    hsClawOpen(),
                    horizontalOut(),
                    hsPivotDown()
                    ),

                    hsClawClose(),
                    hsPivotUp(),

                new SequentialAction(
                    //Transfer (can be done while moving)
                    horizontalIn(),

                       vsClawCloseFast(), //used to not be Fast
                       hsClawOpen(),

                        new ParallelAction(
                        Score2.build(),
                        verticalUp(),
                        vsPivotUp()
                       )
                    ),

                verticalClick(),
                vsClawOpen(),
                BotBack2.build(),
                        new ParallelAction(
                                Grab1.build(),
                                verticalDown(),
                                vsPivotDown()
                        ),
               new ParallelAction(
                       horizontalOutMid(),
                       hsPivotDown()
               ),
                hsClawClose(),
                hsPivotMid(),
                Turn1.build(),

                new ParallelAction(
                        hsClawOpenFast(),
                        horizontalIn()
                ),

                new SleepAction(2),

                        horizontalOutMid(),
                        hsPivotDown(),

                hsClawClose(),
                hsPivotUp(),

                horizontalIn(), //make sure stuff finishes quickly
                new ParallelAction(
                    Score3.build(),
                    new SequentialAction(
                            vsClawCloseFast(), //used to not be Fast
                            hsClawOpen(),
                            new ParallelAction(
                            verticalUp(),
                            vsPivotUp()
                        )
                    )
                ),

                verticalClick(),
                vsClawOpen(),
                BotBack3.build(),

                new ParallelAction(
                Park.build(),
                verticalDown(),
                vsPivotDown()
                )

                )
        );

    }

    public Action verticalUp() {
        return new MoveVertical(vertical, 1225);
    }
    public Action verticalDown() {
        return new MoveVertical(vertical, 0);
    }
    public Action verticalBasket() {
        return new MoveVertical(vertical, 2760);
    }
    public Action verticalClick() {
        return new MoveVertical(vertical, 680);
    } //used to be 675

    public Action horizontalOutMid() {
        return new MoveHorizontal(horizontal, 1600); //1400
    } //used to be 945 or something
    public Action horizontalOut() {
        return new MoveHorizontal(horizontal, 620);
    }
    public Action horizontalIn() {
        return new MoveHorizontal(horizontal, 0);
    }
    public Action horizontalOutFar() { return new MoveHorizontal(horizontal, 2250); //1238
    }
    public Action vsPivotUp() {
        return new VSPivotAction(VSPivot, 0.65);
    }
    public Action vsPivotDown() {
        return new VSPivotAction(VSPivot, 1);
    }
    public Action hsPivotDown() {
        return new HSPivotAction(HSPivot, 0.72);
    }
    public Action hsPivotMid() {
        return new HSPivotQuickAction(HSPivot, 0.36);
    }
    public Action hsPivotUp() {
        return new HSPivotAction(HSPivot, 0.01);
    }
    public Action hsClawOpen() {
        return new HSClawAction(HSClaw, 0.57);
    }
    public Action hsClawOpenFast() {
        return new HSClawAction(HSClaw, 0.57, 0.3);
    }
    public Action hsClawClose() {
        return new HSClawAction(HSClaw, 0);
    }
    public Action vsClawOpen() {
        return new VSClawAction(VSClaw, 0.40);
    }

    public Action vsClawCloseFast() {
        return new VSClawAction(VSClaw, 0, 0.3);
    }
    public Action vsClawClose() {
        return new VSClawAction(VSClaw, 0);
    }
}

class MoveVertical implements Action {
    private boolean initialized = false;
    public DcMotorEx vertical;
    int position;

    public MoveVertical(DcMotorEx vertical, int position) {
        this.vertical = vertical;
        this.position = position;
    }

    @Override
    public boolean run(@NonNull TelemetryPacket telemetryPacket) {
        if (!initialized) {
            vertical.setTargetPosition(position);
            vertical.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            vertical.setPower(1);
            initialized = true;
        }
        telemetryPacket.put("state", "MoveVertical " + position);
        telemetryPacket.put("vertical", vertical.getCurrentPosition());
        return Math.abs(vertical.getTargetPosition() - vertical.getCurrentPosition()) > 20;
    }
}

class MoveHorizontal implements Action {
    private boolean initialized = false;
    public DcMotorEx horizontal;
    int position;

    ElapsedTime timer;

    public MoveHorizontal(DcMotorEx vertical, int position) {
        this.horizontal = vertical;
        this.position = position;
    }

    @Override
    public boolean run(@NonNull TelemetryPacket telemetryPacket) {
        if (!initialized) {
            horizontal.setTargetPosition(position);
            horizontal.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            horizontal.setPower(1);
            initialized = true;
            timer = new ElapsedTime();
        }
        telemetryPacket.put("horizontal", horizontal.getCurrentPosition());
        telemetryPacket.put("state", "MoveHorizontal " + position);
        telemetryPacket.put("timer", timer.milliseconds());
        boolean status = Math.abs(horizontal.getTargetPosition() - horizontal.getCurrentPosition()) >= 30;
        //if (timer.seconds() > 4) {
           // status = false;
        //}
        return status;
    }
}

    class VSPivotAction implements Action {
    Servo VSPivot;
    double position;
    ElapsedTime timer;
    public VSPivotAction(Servo VSPivot, double position) {
        this.VSPivot = VSPivot;
        this.position = position;
    }

    @Override
    public boolean run(@NonNull TelemetryPacket telemetryPacket) {
        if (timer == null) {
            timer = new ElapsedTime();
            VSPivot.setPosition(position);
        }
        return timer.seconds() < 0.60;
    }
}

class HSPivotAction implements Action {
    Servo HSPivot;
    double position;
    ElapsedTime timer;
    public HSPivotAction(Servo HSPivot, double position) {
        this.HSPivot = HSPivot;
        this.position = position;
    }

    @Override
    public boolean run(@NonNull TelemetryPacket telemetryPacket) {
        if (timer == null) {
            timer = new ElapsedTime();
            HSPivot.setPosition(position);
        }
        return timer.seconds() < 0.65;
    }
}

class HSPivotQuickAction implements Action {
    Servo HSPivot;
    double position;
    ElapsedTime timer;
    public HSPivotQuickAction(Servo HSPivot, double position) {
        this.HSPivot = HSPivot;
        this.position = position;
    }

    @Override
    public boolean run(@NonNull TelemetryPacket telemetryPacket) {
        if (timer == null) {
            timer = new ElapsedTime();
            HSPivot.setPosition(position);
        }
        return timer.seconds() < 0.40;
    }
}

class HSClawAction implements Action {
    Servo HSClaw;
    double position;
    ElapsedTime timer;
    double duration;
    public HSClawAction(Servo HSClaw, double position, double duration) {
        this.HSClaw = HSClaw;
        this.position = position;
        this.duration = duration;
    }

    public HSClawAction(Servo HSClaw, double position) {
        this.HSClaw = HSClaw;
        this.position = position;
        this.duration = 0.6;
    }

    @Override
    public boolean run(@NonNull TelemetryPacket telemetryPacket) {
        if (timer == null) {
            timer = new ElapsedTime();
            HSClaw.setPosition(position);
        }

        return timer.seconds() < duration;
    }
}

class VSClawAction implements Action {
    Servo VSClaw;
    double position;
    ElapsedTime timer;
    double duration;
    public VSClawAction(Servo VSClaw, double position, double duration) {
        this.VSClaw = VSClaw;
        this.position = position;
        this.duration = duration;
    }

    public VSClawAction(Servo VSClaw, double position) {
        this.VSClaw = VSClaw;
        this.position = position;
        this.duration = 0.6;
    }

    @Override
    public boolean run(@NonNull TelemetryPacket telemetryPacket) {
        if (timer == null) {
            timer = new ElapsedTime();

            VSClaw.setPosition(position);
        }
        telemetryPacket.put("vsClaw", VSClaw.getPosition());
        telemetryPacket.put("state", "vsClawAction " + position);

        return timer.seconds() < duration; //0.60, had to increase to hold it tightly
    }
}
//failed attempt at making transfer into one action
//class Common {
//    public static Action PickUp(VSPivotAction vsPivotDown,
//                                HSPivotAction hsPivotDown,
//                                HSPivotAction hsPivotUp,
//                                VSClawAction vsClawOpen,
//                                HSClawAction hsClawOpen,
//                                HSClawAction hsClawClose,
//                                MoveHorizontal horizontalOutFar) {
//        return new SequentialAction (
//                new ParallelAction(
//                        vsPivotDown(),
//                        vsClawOpen(),
//                        hsClawOpen(),
//                        horizontalOutFar()
//                ),
//
//                hsPivotDown(),
//                hsClawClose(),
//                hsPivotUp(),
//
//        );
//    }
//}


