import processing.core.PApplet;
import com.jsyn.unitgen.TriangleOscillator;
import com.jsyn.Synthesizer;
import com.jsyn.unitgen.LineOut;
import processing.core.PConstants;

import java.util.ArrayList;
import java.util.List;

import java.lang.Math;

public class Pendulum {
    //Instance Variables
    public final Point p1;
    public final Point p2;
    private final PApplet p;
    private final float g = 0.5f;
    public final TriangleOscillator triOsc;
    private final float detune;
    static List<Integer> steps = new ArrayList<>();
    private final float p1RandVel;
    private final float p2RandVel;

    //Class variables
    public static int octaves = 2;
    public static int detuneMag;
    public static String oscMode = "oscillate";
    public static String noteMode = "notes";

    public Pendulum(PApplet p, float r1, float r2, float m1, float m2, float a1, float a2, Synthesizer syn, LineOut lo){
        this.p1 = new Point(p, 200, 100, r1, m1, a1);
        this.p2 = new Point(p, 0, 200, r2, m2, a2);
        this.p = p;
        this.triOsc = new TriangleOscillator();
        this.triOsc.amplitude.set(0.1);
        this.p1RandVel = p.random(-0.03f, 0.03f);
        this.p2RandVel = p.random(-0.03f, 0.03f);

        syn.add(triOsc);
        triOsc.output.connect(0, lo.input, 0);
        triOsc.output.connect(0, lo.input, 1);

        this.detune = p.random(-1f, 1f);
    }

    public void update(){

        //-------- Pendulum Physics  --------
        if(oscMode.equals("physics")) {
            //Find acceleration 1
            float num1 = -g * (2 * p1.mass + p2.mass) * (float) Math.sin(p1.angle);
            float num2 = -p2.mass * g * (float) Math.sin(p1.angle - 2 * p2.angle);
            float num3 = -2 * (float) Math.sin(p1.angle - p2.angle) * p2.mass;
            float num4 = (p2.vel * p2.vel * p2.r + p1.vel * p1.vel * p1.r * (float) Math.cos(p1.angle - p2.angle));
            float den = p1.r * (2 * p1.mass + p2.mass - p2.mass * (float) Math.cos(2 * p1.angle - 2 * p2.angle));
            p1.acc = (num1 + num2 + num3 * num4) / den;

            //Find acceleration 2
            num1 = 2 * (float) Math.sin(p1.angle - p2.angle);
            num2 = p1.vel * p1.vel * p1.r * (p1.mass + p2.mass);
            num3 = g * (p1.mass + p2.mass) * (float) Math.cos(p1.angle);
            num4 = p2.vel * p2.vel * p2.r * p2.mass * (float) Math.cos(p1.angle - p2.angle);
            den = p2.r * (2 * p1.mass + p2.mass - p2.mass * (float) Math.cos(2 * p1.angle - 2 * p2.angle));
            p2.acc = (num1 * (num2 + num3 + num4)) / den;

            //Update Point 1
            p1.vel += p1.acc;
            p1.angle += p1.vel;
            //Uddate Point 2
            p2.vel += p2.acc;
            p2.angle += p2.vel;

            if ((p1.vel > 0.6 || p1.vel < -0.6) || (p2.vel > 0.6 || p2.vel < -0.6)) {
                System.out.println(p1.vel);
                p1.vel *= 0.1f;
                p1.acc *= 0.1f;
                p2.vel *= 0.1f;
                p2.acc *= 0.1f;
            }
        }

        else if(oscMode.equals("oscillate")){
            p1.angle += p1RandVel;
            p2.angle += p2RandVel;
        }

        p1.x = p1.r * (float) Math.sin(p1.angle);
        p1.y = p1.r * (float) Math.cos(p1.angle);

        p2.x = p1.x + p2.r * (float) Math.sin(p2.angle);
        p2.y = p1.y + p2.r * (float) Math.cos(p2.angle);


        //------- Draw The Pendulum ----------------
        p.stroke(0, 0, 0);
        p1.draw(0, 0, 0);
        p.line(0, 0, p1.x, p1.y);
        p2.draw(0, 200, 0);
        p.line(p1.x, p1.y, p2.x, p2.y);

        // --------- Update Synth Frequency ----------
        int frequency = 0;

        if(noteMode.equals("smooth")){
            frequency = 600 - (int)p2.y;
        }

        else if(noteMode.equals("notes")){
            if(!steps.isEmpty()) frequency = (int)setToMajor(octaves);
            else frequency = 1;
        }

        triOsc.frequency.set(frequency + detune*detuneMag);

        // --------- Draw Line -------------
        p.stroke(200);
        p.fill(200);
        p.line(-200, (int)p2.y, (int)p2.x, (int)p2.y); // p2 line
        p.textAlign(PConstants.LEFT, PConstants.BOTTOM);
        p.text(frequency+"hz", -200, (int)p2.y);
    }

    public double setToMajor(int octaves) {
        // Normalize y to a range [0, 1]
        double yNorm = (-p2.y + 200) / 400;

        // Total MIDI range covered (numNotes must be linear in y)
        int numNotes = steps.size() * octaves;

        // Compute MIDI note linearly from y
        double midiNote = 60 + yNorm * (12 * octaves);

        // Snap MIDI note to the closest scale degree
        int baseNote = (int) Math.floor(midiNote / 12) * 12;  // Closest octave base
        int closestStep = steps.get(0);
        for (int step : steps) {
            if (Math.abs(midiNote - (baseNote + step)) < Math.abs(midiNote - (baseNote + closestStep))) {
                closestStep = step;
            }
        }

        // Final MIDI note in scale
        midiNote = baseNote + closestStep;

        // Convert to frequency
        return 440.0 * Math.pow(2, (midiNote - 69) / 12.0);
    }


}
