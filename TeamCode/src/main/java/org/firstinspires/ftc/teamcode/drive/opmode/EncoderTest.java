package org.firstinspires.ftc.teamcode.drive.opmode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp(name = "EncoderTest", group = "Drive")
public class EncoderTest extends LinearOpMode {


    @Override
    public void runOpMode() throws InterruptedException {
        DcMotorEx leftFront;
        DcMotorEx leftRear;
        DcMotorEx rightRear;
        DcMotorEx rightFront;

        leftFront = hardwareMap.get(DcMotorEx .class, "FL");
        leftRear = hardwareMap.get(DcMotorEx.class, "BL");
        rightRear = hardwareMap.get(DcMotorEx.class, "BR");
        rightFront = hardwareMap.get(DcMotorEx.class, "FR");

        waitForStart();

        while (opModeIsActive()) {
            telemetry.addData("Left Encoder", rightFront.getCurrentPosition());
            telemetry.addData("Right Encoder", leftFront.getCurrentPosition());
            telemetry.addData("Middle Encoder", leftRear.getCurrentPosition());
            telemetry.update();
        }
    }
}
