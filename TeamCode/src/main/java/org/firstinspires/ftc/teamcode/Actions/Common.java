package org.firstinspires.ftc.teamcode.Actions;


import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;

public class Common {
    public static Action PickUp(HorizontalSlide horizontalSlide, VerticalSlide verticalSlide) {
        return new SequentialAction(
                //move VSPivot down (already done)
                verticalSlide.vsPivotDown(),
                //open VSClaw (already done)
                verticalSlide.vsClawOpen(),
                //HSClaw open
                horizontalSlide.hsClawOpen(),

                // HSlide Out
                horizontalSlide.horizontalOut(),
                //HSPivot Down
                horizontalSlide.hsPivotDown(),
                new SleepAction(0.5),
                //HSClaw Close
                horizontalSlide.hsClawClose(),
                new SleepAction(0.5)
        );
    }
    public static Action Transfer(HorizontalSlide horizontalSlide, VerticalSlide verticalSlide) {
        return new SequentialAction(
                // HSPivot Up
                horizontalSlide.hsPivotUp(),
                // HSlide in
                new SleepAction(0.5),
                horizontalSlide.horizontalIn(),

                //sleep(1500); // REPLACE LATER FOR CONDITION IF touch sensor IS PRESSED
                //new SleepAction(0.5), //not in our usual transfer, is it necessary?

                //VS Claw close
                verticalSlide.vsClawClose(),
                new SleepAction(0.2), //THIS
                //HS Claw open
                horizontalSlide.hsClawOpen(),
                new SleepAction(0.1),
                //VS Pivot up
                verticalSlide.vsPivotUp(),
                new SleepAction(0.1)
        );
    }
}
