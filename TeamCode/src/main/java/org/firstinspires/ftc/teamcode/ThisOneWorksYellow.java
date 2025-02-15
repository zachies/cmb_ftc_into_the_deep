package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Actions.VerticalSlide;

@Autonomous (name = "ThisOneWorksYellow")
public class ThisOneWorksYellow extends LinearOpMode {
    private DcMotorEx horizontal;

    private Servo HSClaw;
    private Servo HSPivot;

    private DcMotorEx vertical;
    private Servo VSClaw;
    private Servo VSPivot;

    @Override
    public void runOpMode() throws InterruptedException {
        Pose2d initialPose = new Pose2d(-31, -64, Math.toRadians(180));
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);

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

        VSClaw.setPosition(0); //supposed to be 0!!
        //init up
        VSPivot.setPosition(0.65);

        waitForStart();

        Actions.runBlocking(new SequentialAction(
//                verticalUp(),
//                verticalClick(),
//                horizontalOut(),
//                horizontalIn(),
//                verticalBasket(),
//                horizontalOutFar(),
//                verticalDown(),
//                horizontalOut(),
//                horizontalIn()
                    new ParallelAction(
                    vsPivotDown(),
                    vsClawOpen(),
                    hsClawOpen(),
                    horizontalOutFar()
                    ),

                    hsPivotDown(),
                    hsClawClose(),
                    hsPivotUp(),
                    horizontalIn(),
                    vsClawClose(),
                    hsClawOpen(),
                    vsPivotUp()
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
        return new MoveVertical(vertical, 675);
    }

    public Action horizontalOut() {
        return new MoveHorizontal(horizontal, 620);
    }

    public Action horizontalIn() {
        return new MoveHorizontal(horizontal, 0);
    }

    public Action horizontalOutFar() {
        return new MoveHorizontal(horizontal, 2250); //1238
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

    public Action hsPivotUp() {
        return new HSPivotAction(HSPivot, 0.01);
    }
    public Action hsClawOpen() {
        return new HSClawAction(HSClaw, 0.57);
    }
    public Action hsClawClose() {
        return new HSClawAction(HSClaw, 0);
    }
    public Action vsClawOpen() {
        return new VSClawAction(VSClaw, 0.40);
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
        }

        VSPivot.setPosition(position);

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
        }

        HSPivot.setPosition(position);

        return timer.seconds() < 0.65;
    }
}

class HSClawAction implements Action {
    Servo HSClaw;
    double position;
    ElapsedTime timer;
    public HSClawAction(Servo HSClaw, double position) {
        this.HSClaw = HSClaw;
        this.position = position;
    }

    @Override
    public boolean run(@NonNull TelemetryPacket telemetryPacket) {
        if (timer == null) {
            timer = new ElapsedTime();
        }

        HSClaw.setPosition(position);

        return timer.seconds() < 0.60;
    }
}

class VSClawAction implements Action {
    Servo VSClaw;
    double position;
    ElapsedTime timer;
    public VSClawAction(Servo VSClaw, double position) {
        this.VSClaw = VSClaw;
        this.position = position;
    }

    @Override
    public boolean run(@NonNull TelemetryPacket telemetryPacket) {
        if (timer == null) {
            timer = new ElapsedTime();
        }

        VSClaw.setPosition(position);

        return timer.seconds() < 0.60;
    }
}


