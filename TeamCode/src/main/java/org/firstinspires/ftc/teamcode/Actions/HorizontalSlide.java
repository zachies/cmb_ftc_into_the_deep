package org.firstinspires.ftc.teamcode.Actions;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

public class HorizontalSlide {
    private DcMotorEx horizontal;
    private Servo HSClaw;
    private Servo HSPivot;


    public HorizontalSlide(HardwareMap hardwareMap) {
        horizontal = hardwareMap.get(DcMotorEx.class, "horizontal");
        horizontal.setDirection(DcMotor.Direction.REVERSE);
        horizontal.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        horizontal.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        horizontal.setTargetPosition(0);
        horizontal.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        HSClaw = hardwareMap.get(Servo.class, "HSlideClaw");
        HSPivot = hardwareMap.get(Servo.class, "HSlidePivot");

        HSClaw.setPosition(0); //init close
        HSPivot.setPosition(0); //init up
    }

    public class HorizontalOut implements Action {
        private boolean initialized = false;

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            if (!initialized) {
                horizontal.setTargetPosition(620);
                horizontal.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                horizontal.setPower(1);
                initialized = true;
            }
            telemetryPacket.put("horizontal", horizontal.getCurrentPosition());
            return horizontal.getCurrentPosition() < 620; //used to be !=
        }
    }
    public Action horizontalOut() {
        return new HorizontalSlide.HorizontalOut();
    }

    //or do this: //doube check its finished being written!!d
//    public class HorizontalOut implements Action {
//        private boolean initialized = false;
//
//        @Override
//        public boolean run(@NonNull TelemetryPacket packet) {
//            if (!initialized) {
//                horizontal.setTargetPosition(620);
//                horizontal.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//                horizontal.setPower(1);
//                initialized = true;
//            }
//
//            double pos = horizontal.getCurrentPosition();
//            packet.put("horizontalPos", pos);
//            if (pos < 620) {
//                return true;
//            } else {
//                horizontal.setPower(0);
//                return false;
//            }
//        }
//    }
//    public Action horizontalOut() {
//        return new HorizontalOut();
//    }

    public class HorizontalOutFar implements Action {

        private boolean initialized = false;

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            if (!initialized) {
                horizontal.setTargetPosition(1238); //currently a guessed amount //used to be 1238,1245
                horizontal.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                horizontal.setPower(1);
                initialized = true;
            }
            telemetryPacket.put("horizontal", horizontal.getCurrentPosition());

            return horizontal.getCurrentPosition() != 1238; //used to be !=, other <
        }
    }
    public Action horizontalOutFar() {
        return new HorizontalSlide.HorizontalOutFar();
    }

    public class HorizontalIn implements Action {
        private boolean initialized = false;

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            if (!initialized) {
                horizontal.setTargetPosition(0);
                horizontal.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                horizontal.setPower(1);
                initialized = true;
            }
            telemetryPacket.put("horizontal", horizontal.getCurrentPosition());
            return horizontal.getCurrentPosition() != 0;
        }

    }
    public Action horizontalIn() {
        return new HorizontalSlide.HorizontalIn();
    }

    public class HSClawMove implements Action {
        private boolean initialized = false;
        double position;

        public HSClawMove(double position) {
            this.position = position;
        }

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            if (!initialized) {
                HSClaw.setPosition(position);
                initialized = true;
            }
            return false;
        }

    }
    public Action hsClawOpen() {
        return new HorizontalSlide.HSClawMove(0.57);
    }
    public Action hsClawClose() {
        return new HorizontalSlide.HSClawMove(0);
    }

    public class HSPivotUp implements Action {
        private boolean initialized = false;

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {

            HSPivot.setPosition(0.01);
            initialized = true;
            //telemetryPacket.put("HSPivot", HSPivot.getPosition());
            //return Math.abs(HSPivot.getPosition() - 0) > 0.01;
            return false;
        }
    }

    public Action hsPivotUp() { return new HorizontalSlide.HSPivotUp(); }

    public class HSPivotDown implements Action {
        private boolean initialized = false;

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            HSPivot.setPosition(0.72);
            initialized = true;

            //return true;
            return false;
        }
    }
    public Action hsPivotDown() { return new HorizontalSlide.HSPivotDown(); }

    //or do this:
//    public class HSPivotDown implements Action {
//        ElapsedTimed timer;
//
//        @Override
//        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
//            if (timer == null) {
//            timer = new ElapsedTime();
//
//            HSPivot.setPosition(0.72);
//        }
//            return timer.seconds() < 3;
//    }
//    public Action hsPivotDown() { return new HorizontalSlide.HSPivotDown(); }


}
