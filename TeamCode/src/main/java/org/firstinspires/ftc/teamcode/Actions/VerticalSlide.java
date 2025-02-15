package org.firstinspires.ftc.teamcode.Actions;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class VerticalSlide {
    private DcMotorEx vertical;
    private Servo VSClaw;
    private Servo VSPivot;

    public double getPosition() {
        return VSClaw.getPosition();
    }


    public VerticalSlide(HardwareMap hardwareMap) {
        vertical = hardwareMap.get(DcMotorEx.class, "vertical");
        vertical.setDirection(DcMotor.Direction.REVERSE);
        vertical.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        vertical.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        vertical.setTargetPosition(0);
        vertical.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        VSClaw = hardwareMap.get(Servo.class, "VSlideClaw");
        VSPivot = hardwareMap.get(Servo.class, "VSlidePivot");

        //init closed
        VSClaw.setPosition(0); //supposed to be 0!!
        //init up
        VSPivot.setPosition(0.65);
    }

    public class VerticalUp implements Action {
        private boolean initialized = false;

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            if (!initialized) {
                vertical.setTargetPosition(1225);
                vertical.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                vertical.setPower(1);
                initialized = true;
            }
            telemetryPacket.put("vertical", vertical.getCurrentPosition());
            return vertical.getCurrentPosition() != 1225;
        }

    }
    public Action verticalUp() {
        return new VerticalUp();
    }

    public class VerticalBasket implements Action {
        private boolean initialized = false;

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            if (!initialized) {
                vertical.setTargetPosition(2790); //used to be 2800
                vertical.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                vertical.setPower(1);
                initialized = true;
            }
            telemetryPacket.put("vertical", vertical.getCurrentPosition());
            return vertical.getCurrentPosition() != 2790;
        }

    }
    public Action verticalBasket() {
        return new VerticalBasket();
    }

    public class VerticalClick implements Action {
        private boolean initialized = false;

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            if (!initialized) {
                vertical.setTargetPosition(675); //used to be 675, 665
                vertical.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                vertical.setPower(1);
                initialized = true;
            }
            telemetryPacket.put("vertical", vertical.getCurrentPosition());
            return vertical.getCurrentPosition() != 675;
        }

    }
    public Action verticalClick() {
        return new VerticalClick();
    }

    public class VerticalDown implements Action {
        private boolean initialized = false;

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            if (!initialized) {
                vertical.setTargetPosition(0);
                vertical.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                vertical.setPower(1);
                initialized = true;
            }
            telemetryPacket.put("vertical", vertical.getCurrentPosition());
            return vertical.getCurrentPosition() != 0;
        }

    }
    public Action verticalDown() {
        return new VerticalDown();
    }

    public class VSClawMove implements Action {
        private boolean initialized = false;
        double position;

        public VSClawMove(double position) {
            this.position = position;
        }

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            VSClaw.setPosition(position);
            return false;
        }

    }
    public Action vsClawOpen() {
        return new VSClawMove(0.40);
    }
    public Action vsClawClose() {
        return new VSClawMove(0);
    }

    public class VSPivotMove implements Action {
        private boolean initialized = false;
        double position;

        public VSPivotMove(double position) {
            this.position = position;
        }

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {

            VSPivot.setPosition(position);
            initialized = true;

            //return true;
            return false;
            //return Math.abs(VSPivot.getPosition() - position) > 0.01;
        }

    }
    public Action vsPivotUp() { return new VSPivotMove(0.65); }
    public Action vsPivotDown() { return new VSPivotMove(1); }
}



